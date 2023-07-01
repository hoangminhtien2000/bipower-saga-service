package com.biplus.saga.domain.message.team;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.dto.email.EmailInfoDTO;
import com.biplus.saga.domain.dto.leave.EmployeeLeaveDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SourceMessage("com.biplus.team.domain.message.TeamAssignRoleAndTeamForEmployeeSuccessReply")
public class TeamAssignRoleAndTeamForEmployeeSuccessReply implements Serializable {}
