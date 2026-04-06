package com.teddy.youtuberef.service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teddy.youtuberef.entity.VideoEntity;
import com.teddy.youtuberef.entity.enums.VideoStatus;
import com.teddy.youtuberef.repository.spectification.VideoSpectification;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Create VideoSearchRequest for search video with filter and paging
 * Structure VideoSearchRequest extends FilterRequest
 * FilterRequest have paging and specification
 */
@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoSearchRequest extends FilterRequest<VideoEntity> {
    String url;
    String description;
    List<VideoStatus> status =  new ArrayList<>();

    @Override
    public Specification<VideoEntity> specification() {

        return VideoSpectification.builder()
                .withUrl(this.url)
                .withDescription(this.description)
                .withStatus(this.status)
                .build();
    }
}
