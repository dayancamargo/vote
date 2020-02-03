package com.test.meeting.model.response;

import com.test.meeting.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Pagination extends BaseModel {
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    public Pagination() {
        page = 0;
    }

    public Pagination(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    /**
     * Validate if has a next page;
     *
     * @return true if is not the last page; false if is or total pages is null;
     */
    public Boolean hasNextPage() {
        if (!Objects.isNull(totalPages)) {
            return page < totalPages;
        } else {
            return false;
        }
    }

    /**
     * Return next page or null if is the last one
     *
     * @return Integer with next number page
     */
    public Integer nextPage() {
        if (hasNextPage()) {
            return page + 1;
        } else {
            return null;
        }
    }
}
