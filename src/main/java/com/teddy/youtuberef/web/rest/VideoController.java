package com.teddy.youtuberef.web.rest;

import com.teddy.youtuberef.service.dto.VideoDto;
import com.teddy.youtuberef.service.dto.request.VideoSearchRequest;
import com.teddy.youtuberef.service.dto.response.PagingResponse;
import com.teddy.youtuberef.service.dto.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/video")
public interface VideoController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Response<VideoDto> createVideo(@Valid @RequestBody VideoDto videoDto);

    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    Response<PagingResponse<VideoDto>> getVideos(@RequestBody final VideoSearchRequest request);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Response<VideoDto> getVideoById(@NotNull @PathVariable(name = "id") final String id);

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    Response<VideoDto> updateVideo(@RequestBody VideoDto videoDto);

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    Response<Void> deleteVideo(@RequestBody final List<String> ids);
}
