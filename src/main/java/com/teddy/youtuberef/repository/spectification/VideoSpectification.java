package com.teddy.youtuberef.repository.spectification;

import com.teddy.youtuberef.entity.VideoEntity;
import com.teddy.youtuberef.entity.enums.VideoStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VideoSpectification {

    private static final String FIELD_URL = "url";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_STATUS = "status";

    // 1.Tạo một list các specification để lưu trữ các điều kiện tìm kiếm
    private final List<Specification<VideoEntity>> specifications = new ArrayList<>();

    // 2. Dùng builder pattern để tạo ra một đối tượng VideoSpecification,
    // giúp dễ dàng thêm các điều kiện tìm kiếm một cách linh hoạt
    public static VideoSpectification builder(){
        return new VideoSpectification();
    }

    // 3. Cấu hình builder để kết hợp tất cả các điều kiện tìm kiếm thành một Specification duy nhất
    public Specification<VideoEntity> build(){
        return (
                root, query, criteriaBuilder) ->
                criteriaBuilder
                        .and(specifications.stream()
                                .filter(Objects::nonNull)
                                .map(s->s.toPredicate(root, query, criteriaBuilder))
                                .toArray(Predicate[]::new)
        );
    }


    public VideoSpectification withUrl(final String url){
        if(!ObjectUtils.isEmpty(url)){
            specifications.add(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(criteriaBuilder.upper(root.get(FIELD_URL)), like(url))

            );
        }
        return this;
    }
    public VideoSpectification withDescription(final String description){
        if(!ObjectUtils.isEmpty(description)){
            specifications.add(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(criteriaBuilder.upper(root.get(FIELD_DESCRIPTION)), like(description))

            );
        }
        return this;
    }
    public VideoSpectification withStatus(final List<VideoStatus> status){
        if(!ObjectUtils.isEmpty(status)){
            specifications.add(
                    (root, query, criteriaBuilder) ->
                            root.get(FIELD_STATUS).in(status)
            );
        }
        return this;
    }

    public static String like(final String value){
        return "%"+value.toUpperCase()+"%";
    }
}
