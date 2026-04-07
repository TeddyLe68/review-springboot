package com.teddy.youtuberef.service.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.teddy.youtuberef.entity.VideoEntity;
import com.teddy.youtuberef.entity.enums.VideoStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    private String id;
    private String url;
    private String description;
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private VideoStatus status = VideoStatus.DRAFT;
}
