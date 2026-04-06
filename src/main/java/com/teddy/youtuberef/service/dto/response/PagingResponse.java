package com.teddy.youtuberef.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * Create PagingResponse common response for all paging response
 * @param <T>
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingResponse<T> {
    /**
     * Response content list
     * Paging response (page number, page size, total pages, total record)
     */
    List<T> contents = new ArrayList<>();
    PageableData paging;

    public PagingResponse<T> setContents(final List<T> contents) {
        this.contents = contents;
        return this;
    }

    public PagingResponse<T> setPaging(final PageableData paging) {
        this.paging = paging;
        return this;
    }
}
