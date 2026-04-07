package com.teddy.youtuberef.service.mapper;

import org.mapstruct.BeanMapping;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface EntityMapper<D,E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtos);
    List<D> toDto(List<E> entities);
}
