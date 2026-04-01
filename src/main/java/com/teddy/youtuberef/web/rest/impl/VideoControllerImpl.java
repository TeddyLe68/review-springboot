package com.teddy.youtuberef.web.rest.impl;

import com.teddy.youtuberef.service.VideoService;
import com.teddy.youtuberef.service.dto.VideoDto;
import com.teddy.youtuberef.web.rest.VideoController;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoControllerImpl implements VideoController {

    private final VideoService videoService;

    @Override
    public ResponseEntity<VideoDto> createVideo(@NonNull final VideoDto videoDto) {
        log.info("====== Create Video request: {}",videoDto);
        final VideoDto video = videoService.createVideo(videoDto);
        log.info("===== Create Video response: {}",video);
        return ResponseEntity
                .created(URI.create("/"+ video.getId()))
                .body(video);
    }

    @Override
    public ResponseEntity<Page<VideoDto>> getAllVideos() {
        log.info("====== Get Video ======");
        final Page<VideoDto> videos = videoService.getVideos();
        return ResponseEntity
                .ok()
                .body(videos);
    }

    @Override
    public ResponseEntity<VideoDto> getVideoById(@NonNull final String id) {
        log.info("====== Get Video request id: {}",id);
        final VideoDto video = videoService.getVideo(id);
        log.info("===== Get Video response: {}",video);
        return ResponseEntity
                .ok()
                .body(video);
    }

    @Override
    public ResponseEntity<VideoDto> updateVideo(@NonNull final VideoDto videoDto) {
        log.info("====== Update Video request: {}",videoDto);
        final VideoDto video = videoService.updateVideo(videoDto);
        log.info("===== Update Video response: {}",video);
        return ResponseEntity
                .ok()
                .body(video);
    }

    @Override
    public ResponseEntity<Void> deleteVideo(@NonNull final List<String> ids) {
        log.info("====== Delete Video request ids: {}",ids);
        videoService.deleteVideo(ids);
        log.info("===== Delete Video response: {}",ids);
        return ResponseEntity
                .noContent()
                .build();
    }
}
