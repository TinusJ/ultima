package com.tinusj.ultima.service.impl;

import com.tinusj.ultima.dao.dto.ComposeEmailDto;
import com.tinusj.ultima.dao.dto.EmailDto;
import com.tinusj.ultima.dao.dto.EmailFolderDto;
import com.tinusj.ultima.dao.entity.EmailEntity;
import com.tinusj.ultima.dao.entity.EmailFolderEntity;
import com.tinusj.ultima.dao.entity.User;
import com.tinusj.ultima.exception.ResourceNotFoundException;
import com.tinusj.ultima.repository.EmailFolderRepository;
import com.tinusj.ultima.repository.EmailRepository;
import com.tinusj.ultima.repository.UserRepository;
import com.tinusj.ultima.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final EmailRepository emailRepository;
    private final EmailFolderRepository folderRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void checkEmailAccess(EmailEntity email) {
        User currentUser = getCurrentUser();
        boolean isRecipient = email.getRecipientsTo().contains(currentUser) ||
                email.getRecipientsCc().contains(currentUser) ||
                email.getRecipientsBcc().contains(currentUser);
        boolean isSender = email.getSender().getId().equals(currentUser.getId());
        if (!isRecipient && !isSender) {
            throw new SecurityException("User not authorized to access this email");
        }
    }

    @Override
    public List<EmailFolderDto> getFolders() {
        User currentUser = getCurrentUser();
        return folderRepository.findByUserId(currentUser.getId()).stream()
                .map(folder -> new EmailFolderDto(
                        folder.getId(),
                        folder.getName(),
                        folder.getType(),
                        folder.getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmailDto> listEmails(Long folderId, String keyword, Pageable pageable) {
        User currentUser = getCurrentUser();
        EmailFolderEntity folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " + folderId));
        if (!folder.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("User not authorized to access this folder");
        }

        return emailRepository.findByFolderId(folderId, keyword, pageable)
                .map(email -> new EmailDto(
                        email.getId(),
                        email.getSender().getId(),
                        email.getSender().getEmail(),
                        email.getRecipientsTo().stream().map(User::getId).collect(Collectors.toSet()),
                        email.getRecipientsCc().stream().map(User::getId).collect(Collectors.toSet()),
                        email.getRecipientsBcc().stream().map(User::getId).collect(Collectors.toSet()),
                        email.getSubject(),
                        email.getBody(),
                        email.getSentAt(),
                        email.isRead(),
                        email.getFolder().getId(),
                        email.getFolder().getName()));
    }

    @Override
    public EmailDto getEmail(Long id) {
        EmailEntity email = emailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found: " + id));
        checkEmailAccess(email);

        if (!email.isRead()) {
            email.setRead(true);
            emailRepository.save(email);
        }

        return new EmailDto(
                email.getId(),
                email.getSender().getId(),
                email.getSender().getEmail(),
                email.getRecipientsTo().stream().map(User::getId).collect(Collectors.toSet()),
                email.getRecipientsCc().stream().map(User::getId).collect(Collectors.toSet()),
                email.getRecipientsBcc().stream().map(User::getId).collect(Collectors.toSet()),
                email.getSubject(),
                email.getBody(),
                email.getSentAt(),
                email.isRead(),
                email.getFolder().getId(),
                email.getFolder().getName());
    }

    @Override
    public EmailDto composeEmail(ComposeEmailDto emailDto) {
        User currentUser = getCurrentUser();
        EmailEntity email = new EmailEntity();
        email.setSender(currentUser);
        email.setSubject(emailDto.subject());
        email.setBody(emailDto.body());
        email.setSentAt(LocalDateTime.now());
        email.setRead(false);

        // Set recipients
        if (emailDto.recipientToIds() != null) {
            Set<User> to = emailDto.recipientToIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id)))
                    .collect(Collectors.toSet());
            email.setRecipientsTo(to);
        }
        if (emailDto.recipientCcIds() != null) {
            Set<User> cc = emailDto.recipientCcIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id)))
                    .collect(Collectors.toSet());
            email.setRecipientsCc(cc);
        }
        if (emailDto.recipientBccIds() != null) {
            Set<User> bcc = emailDto.recipientBccIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id)))
                    .collect(Collectors.toSet());
            email.setRecipientsBcc(bcc);
        }

        // Determine folder
        EmailFolderEntity folder;
        if (emailDto.isDraft()) {
            folder = folderRepository.findByUserIdAndType(currentUser.getId(), "DRAFTS")
                    .orElseThrow(() -> new ResourceNotFoundException("Drafts folder not found"));
        } else {
            folder = folderRepository.findByUserIdAndType(currentUser.getId(), "SENT")
                    .orElseThrow(() -> new ResourceNotFoundException("Sent folder not found"));
        }
        email.setFolder(folder);

        email = emailRepository.save(email);

        // If not a draft, move to recipients' Inbox
        if (!emailDto.isDraft()) {
            Set<User> allRecipients = email.getRecipientsTo();
            allRecipients.addAll(email.getRecipientsCc());
            allRecipients.addAll(email.getRecipientsBcc());
            for (User recipient : allRecipients) {
                EmailEntity recipientEmail = new EmailEntity();
                recipientEmail.setSender(email.getSender());
                recipientEmail.setRecipientsTo(email.getRecipientsTo());
                recipientEmail.setRecipientsCc(email.getRecipientsCc());
                recipientEmail.setRecipientsBcc(email.getRecipientsBcc());
                recipientEmail.setSubject(email.getSubject());
                recipientEmail.setBody(email.getBody());
                recipientEmail.setSentAt(email.getSentAt());
                recipientEmail.setRead(false);
                EmailFolderEntity inbox = folderRepository.findByUserIdAndType(recipient.getId(), "INBOX")
                        .orElseThrow(() -> new ResourceNotFoundException("Inbox folder not found for user: " + recipient.getId()));
                recipientEmail.setFolder(inbox);
                emailRepository.save(recipientEmail);

                // Notify recipient via WebSocket
                messagingTemplate.convertAndSend("/topic/mail/" + recipient.getId(), new EmailDto(
                        recipientEmail.getId(),
                        recipientEmail.getSender().getId(),
                        recipientEmail.getSender().getEmail(),
                        recipientEmail.getRecipientsTo().stream().map(User::getId).collect(Collectors.toSet()),
                        recipientEmail.getRecipientsCc().stream().map(User::getId).collect(Collectors.toSet()),
                        recipientEmail.getRecipientsBcc().stream().map(User::getId).collect(Collectors.toSet()),
                        recipientEmail.getSubject(),
                        recipientEmail.getBody(),
                        recipientEmail.getSentAt(),
                        recipientEmail.isRead(),
                        recipientEmail.getFolder().getId(),
                        recipientEmail.getFolder().getName()));
            }
        }

        return new EmailDto(
                email.getId(),
                email.getSender().getId(),
                email.getSender().getEmail(),
                email.getRecipientsTo().stream().map(User::getId).collect(Collectors.toSet()),
                email.getRecipientsCc().stream().map(User::getId).collect(Collectors.toSet()),
                email.getRecipientsBcc().stream().map(User::getId).collect(Collectors.toSet()),
                email.getSubject(),
                email.getBody(),
                email.getSentAt(),
                email.isRead(),
                email.getFolder().getId(),
                email.getFolder().getName());
    }

    @Override
    public EmailDto moveEmail(Long id, Long newFolderId) {
        EmailEntity email = emailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found: " + id));
        checkEmailAccess(email);

        EmailFolderEntity newFolder = folderRepository.findById(newFolderId)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " + newFolderId));
        if (!newFolder.getUser().getId().equals(email.getFolder().getUser().getId())) {
            throw new SecurityException("Cannot move email to a folder belonging to another user");
        }

        email.setFolder(newFolder);
        email = emailRepository.save(email);

        return new EmailDto(
                email.getId(),
                email.getSender().getId(),
                email.getSender().getEmail(),
                email.getRecipientsTo().stream().map(User::getId).collect(Collectors.toSet()),
                email.getRecipientsCc().stream().map(User::getId).collect(Collectors.toSet()),
                email.getRecipientsBcc().stream().map(User::getId).collect(Collectors.toSet()),
                email.getSubject(),
                email.getBody(),
                email.getSentAt(),
                email.isRead(),
                email.getFolder().getId(),
                email.getFolder().getName());
    }

    @Override
    public EmailDto markEmailRead(Long id, boolean isRead) {
        EmailEntity email = emailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found: " + id));
        checkEmailAccess(email);

        email.setRead(isRead);
        email = emailRepository.save(email);

        return new EmailDto(
                email.getId(),
                email.getSender().getId(),
                email.getSender().getEmail(),
                email.getRecipientsTo().stream().map(User::getId).collect(Collectors.toSet()),
                email.getRecipientsCc().stream().map(User::getId).collect(Collectors.toSet()),
                email.getRecipientsBcc().stream().map(User::getId).collect(Collectors.toSet()),
                email.getSubject(),
                email.getBody(),
                email.getSentAt(),
                email.isRead(),
                email.getFolder().getId(),
                email.getFolder().getName());
    }
}