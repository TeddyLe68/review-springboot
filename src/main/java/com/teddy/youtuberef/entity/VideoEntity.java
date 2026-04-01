package com.teddy.youtuberef.entity;

import com.teddy.youtuberef.entity.enums.VideoStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "video")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoEntity extends AbstractAuditingEntity<String>{

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    @Column(name = "url",  nullable = false)
    private String url;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VideoStatus status = VideoStatus.DRAFT;
}
