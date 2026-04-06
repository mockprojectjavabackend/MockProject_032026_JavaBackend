package com.mockproject.notary_admin_server.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommissionQuery {
    private String status;
    private String state;
    private String expirationDate;
    private String search;
    private int page = 1;
    private int limit = 10;
}
