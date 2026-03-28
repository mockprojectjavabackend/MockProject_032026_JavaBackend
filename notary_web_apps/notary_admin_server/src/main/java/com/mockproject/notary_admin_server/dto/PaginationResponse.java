package com.mockproject.notary_admin_server.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationResponse<T> {
    private Pagination pagination;
    private T meta;

    @Getter
    @Setter
    public static class Pagination {
        private int page;
        private int limit;
        private long total;
        private int total_pages;
    }

    public static <T> PaginationResponse<List<T>> of(Page<T> page) {

        PaginationResponse<List<T>> response = new PaginationResponse<>();

        Pagination p = new Pagination();
        p.setPage(page.getNumber() + 1);
        p.setLimit(page.getSize());
        p.setTotal(page.getTotalElements());
        p.setTotal_pages(page.getTotalPages());

        response.setPagination(p);
        response.setMeta(page.getContent());

        return response;
    }
}
