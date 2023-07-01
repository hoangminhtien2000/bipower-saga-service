package com.biplus.saga.domain.request.employee;

import com.biplus.saga.domain.request.team.TeamAndRoleRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EmployeeHistoryRequest {
    private Boolean isApproval;
    private String note;
    private Long employeeId;
    private List<TeamAndRoleRequest> teams;
}
