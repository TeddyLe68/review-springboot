package com.teddy.youtuberef.web.rest;

import com.teddy.youtuberef.service.dto.VideoDto;
import com.teddy.youtuberef.service.dto.response.Response;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/video")
public interface VideoController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Response<VideoDto> createVideo(@RequestBody VideoDto videoDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Response<Page<VideoDto>> getAllVideos();

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
