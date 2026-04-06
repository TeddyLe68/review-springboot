package com.teddy.youtuberef.service;

import com.teddy.youtuberef.service.dto.VideoDto;
import com.teddy.youtuberef.service.dto.request.VideoSearchRequest;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VideoService {
    VideoDto getVideo(@NotNull final String id);

    Page<VideoDto> getVideos(@NonNull final VideoSearchRequest request);

    VideoDto createVideo(@NotNull final VideoDto videoDto);

    VideoDto updateVideo(@NotNull final VideoDto videoDto);

    void deleteVideo(@NotNull final List<String> id);
}
