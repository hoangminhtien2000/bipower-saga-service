package com.biplus.saga.domain.request.salary;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ConfirmManyOvertimeRequest {
    private List<Long> overtimeIds;

    private Boolean isApproved;

    private String note;
}
