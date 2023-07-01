package com.biplus.saga.domain.request.leave;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ConfirmListOfLeavesRequest {
    private List<Long> employeeLeaveIds;
    private Boolean isApproved = true;
    private String note;
}
