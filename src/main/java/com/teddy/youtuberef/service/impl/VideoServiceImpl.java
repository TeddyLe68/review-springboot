package com.teddy.youtuberef.service.impl;

import com.teddy.youtuberef.entity.VideoEntity;
import com.teddy.youtuberef.entity.enums.VideoStatus;
import com.teddy.youtuberef.repository.VideoRepository;
import com.teddy.youtuberef.service.VideoService;
import com.teddy.youtuberef.service.dto.VideoDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    @Override
    public VideoDto getVideo(@NonNull final String id) {

        return videoRepository.findById(id)
                // entity -> VideoDto.from(entity)
                .map(VideoDto::from)
                .orElseThrow(()-> new RuntimeException("Video không tồn tại với id là: " + id));
    }

    @Override
    public Page<VideoDto> getVideos() {
        return null;
    }

    @Override
    public VideoDto createVideo(@NonNull final VideoDto videoDto) {
        final VideoEntity entity = videoDto.toEntity();
        return VideoDto.from(videoRepository.save(entity));
    }

    @Override
    public VideoDto updateVideo(@NonNull final VideoDto videoDto) {
        final String id = videoDto.getId();
       if(videoRepository.existsById(videoDto.getId())){
           final VideoEntity entity = videoDto.toEntity();
           return VideoDto.from(videoRepository.save(entity));
       }
       throw new RuntimeException("Không tìm thấy video với id là : " + id);
    }

    @Override
    public void deleteVideo(List<String> id) {
        final List<VideoEntity> videos =  videoRepository.findAllByIdIn(id);

        videos.forEach(video->video.setStatus(VideoStatus.DELETED));

        videoRepository.saveAll(videos);

    }
}
