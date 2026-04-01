package com.teddy.youtuberef.service.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.teddy.youtuberef.entity.VideoEntity;
import com.teddy.youtuberef.entity.enums.VideoStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    private String id;
    private String url;
    private String description;
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private VideoStatus status = VideoStatus.DRAFT;

    /**
     * @author: ThangLe
     * @since: 3/31/2026 5:44 PM
     * @description: Tạo DTO chung cả request và response,
     * có thể dùng để chuyển đổi qua lại giữa entity và DTO
     * @update:
     * - ThangLe (3/31/2026 - 5:48 PM): map từ entity qua DTO
     */
    public static VideoDto from(@NonNull final VideoEntity entity){
        return VideoDto.builder()
                .id(entity.getId())
                .url(entity.getUrl())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }
    /**
     *  - ThangLe (3/31/2026 - 5:54 PM): map từ DTO tới Entity
     */
    public VideoEntity toEntity(){
        return VideoEntity.builder()
                .id(this.getId())
                .url(this.getUrl())
                .description(this.getDescription())
                .status(this.getStatus() != null ? this.getStatus() : VideoStatus.DRAFT)
                .build();
    }
}
