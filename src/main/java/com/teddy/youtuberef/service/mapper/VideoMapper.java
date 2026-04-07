package com.teddy.youtuberef.service.mapper;

import com.teddy.youtuberef.entity.VideoEntity;
import com.teddy.youtuberef.service.dto.VideoDto;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface VideoMapper extends EntityMapper<VideoDto,VideoEntity>{
}
