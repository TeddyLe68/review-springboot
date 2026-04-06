package com.teddy.youtuberef.repository;

import com.teddy.youtuberef.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * VideoRepository is a repository interface for VideoEntity
 * It extends JpaRepository and JpaSpecificationExecutor
 * JpaRepository provides basic CRUD operations
 * JpaSpecificationExecutor provides support for JPA Criteria API
 */

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, String>, JpaSpecificationExecutor<VideoEntity> {

    List<VideoEntity> findAllByIdIn(List<String> ids);
}
