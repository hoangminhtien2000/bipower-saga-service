package com.biplus.saga.domain.command.recruitment;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.RetryableCommand;
import com.biplus.core.tram.command.TenantAware;
import com.biplus.core.tram.producer.TargetMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.recruitment.domain.command.SaveInterviewScheduleCommand")
public class SaveInterviewScheduleCommand implements RetryableCommand, TenantAware {

    private Long id;
    private Long candidateId;
    private String title;
    private LocalDateTime interviewFromTime;
    private LocalDateTime interviewToTime;
    private Long placeId;
    private Long interviewTypeId;
    private Long hrId;
    private Long interviewerId;
    private String interviewLink;
    private String description;
    private Long currentSalary;
    private Long expectSalary;
    private LocalDateTime onboardTime;
    private String note;
    private Boolean isJoined;

    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;



}
