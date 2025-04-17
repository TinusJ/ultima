package com.tinusj.ultima.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Getter
@Setter
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private FolderEntity folder;
}