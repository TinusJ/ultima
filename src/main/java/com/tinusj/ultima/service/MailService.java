package com.tinusj.ultima.service;

import com.tinusj.ultima.dao.dto.ComposeEmailDto;
import com.tinusj.ultima.dao.dto.EmailDto;
import com.tinusj.ultima.dao.dto.EmailFolderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MailService {
    List<EmailFolderDto> getFolders();
    Page<EmailDto> listEmails(Long folderId, String keyword, Pageable pageable);
    EmailDto getEmail(Long id);
    EmailDto composeEmail(ComposeEmailDto emailDto);
    EmailDto moveEmail(Long id, Long newFolderId);
    EmailDto markEmailRead(Long id, boolean isRead);
}