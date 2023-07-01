package com.biplus.saga.domain.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruitmentProposalResponse {
    private Long id;
    private Long processId;
    private Long proposalStatus;
    private ItemResponse status;
    private Long netSalary;
    private Long applyPositionId;
    private ItemResponse applyPosition;
    private Long teamId;
    private TeamResponse team;
    private Double productivity;
    private String project;
    private String hrNote;
    private Long hrLeadId;
    private EmployeeDataResponse hrLead;
    private String hrLeadNote;
    private Long cooId;
    private EmployeeDataResponse coo;
    private String bodNote;
    private EmployeeDataResponse hr;
}
