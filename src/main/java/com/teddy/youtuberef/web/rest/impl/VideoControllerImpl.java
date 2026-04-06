package com.teddy.youtuberef.web.rest.impl;

import com.teddy.youtuberef.service.VideoService;
import com.teddy.youtuberef.service.dto.VideoDto;
import com.teddy.youtuberef.service.dto.request.PagingRequest;
import com.teddy.youtuberef.service.dto.request.VideoSearchRequest;
import com.teddy.youtuberef.service.dto.response.PageableData;
import com.teddy.youtuberef.service.dto.response.PagingResponse;
import com.teddy.youtuberef.service.dto.response.Response;
import com.teddy.youtuberef.web.rest.VideoController;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoControllerImpl implements VideoController {

    private final VideoService videoService;

    @Override
    public Response<VideoDto> createVideo(@NonNull final VideoDto videoDto) {
        log.info("====== Create Video request: {}",videoDto);
        final VideoDto video = videoService.createVideo(videoDto);
        log.info("===== Create Video response: {}",video);
        return Response.created(video);
    }

    /**
     * Get video with filter and paging
     * Structure PagingRequest,PagingResponse
     * @param request
     * @return
     */
    @Override
    public Response<PagingResponse<VideoDto>> getVideos(@RequestBody final VideoSearchRequest request) {
        log.info("====== Get Video ======");
        final Page<VideoDto> videos = videoService.getVideos(request);
        final PagingRequest paging = request.getPaging();
        return Response.ok(
                new PagingResponse<VideoDto>()
                        .setContents(videos.getContent())
                        .setPaging(
                                new PageableData()
                                        .setPageNumber(paging.getPage()-1)
                                        .setPageSize(paging.getSize())
                                        .setTotalPages(videos.getTotalPages())
                                        .setTotalRecord(videos.getTotalElements())
                        )
        );
    }

    @Override
    public Response<VideoDto> getVideoById(@NonNull final String id) {
        log.info("====== Get Video request id: {}",id);
        final VideoDto video = videoService.getVideo(id);
        log.info("===== Get Video response: {}",video);
        return Response.ok(video);

    }

    @Override
    public Response<VideoDto> updateVideo(@NonNull final VideoDto videoDto) {
        log.info("====== Update Video request: {}",videoDto);
        final VideoDto video = videoService.updateVideo(videoDto);
        log.info("===== Update Video response: {}",video);
        return Response.ok(video);
    }

    @Override
    public Response<Void> deleteVideo(@NonNull final List<String> ids) {
        log.info("====== Delete Video request ids: {}",ids);
        videoService.deleteVideo(ids);
        log.info("===== Delete Video response: {}",ids);
        return Response.noContent();
    }
}
