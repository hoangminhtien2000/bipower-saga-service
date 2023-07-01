package com.biplus.saga.domain.command.team;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.producer.TargetMessage;
import com.biplus.saga.domain.request.team.TeamAndRoleRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.team.domain.command.TeamAssignRoleAndTeamForEmployeeCommand")
public class TeamAssignRoleAndTeamForEmployeeCommand implements CompensatableCommand, Serializable {
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
    private List<TeamAndRoleRequest> teams;
    private Boolean isAssignTeamAndRole;
    private Long employeeId;
    private String companyEmail;
}
