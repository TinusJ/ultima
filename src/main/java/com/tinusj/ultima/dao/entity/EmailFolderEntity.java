package com.tinusj.ultima.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "email_folders")
public class EmailFolderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // SYSTEM (Inbox, Sent, Drafts, Trash) or CUSTOM

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}