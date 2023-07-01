package com.biplus.saga.domain.request.salary;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OvertimeToApproverRequest {
    private List<Long> overtimeIds;
}
