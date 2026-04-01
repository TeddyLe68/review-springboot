package com.teddy.youtuberef.repository;

import com.teddy.youtuberef.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<VideoEntity, String> {

    List<VideoEntity> findAllByIdIn(List<String> ids);
}
