package com.teddy.youtuberef.web.rest;

import com.teddy.youtuberef.service.dto.VideoDto;
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
    ResponseEntity<VideoDto> createVideo(@RequestBody VideoDto videoDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Page<VideoDto>> getAllVideos();

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<VideoDto> getVideoById(@NotNull @PathVariable(name = "id") final String id);

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<VideoDto> updateVideo(@RequestBody VideoDto videoDto);

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> deleteVideo(@RequestBody final List<String> ids);
}
