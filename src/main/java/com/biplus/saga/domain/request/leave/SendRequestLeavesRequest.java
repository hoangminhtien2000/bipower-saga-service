package com.biplus.saga.domain.request.leave;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendRequestLeavesRequest {
    private List<Long> employeeLeaveIds;
    private Boolean isApproval = false;
    private String note;
}
