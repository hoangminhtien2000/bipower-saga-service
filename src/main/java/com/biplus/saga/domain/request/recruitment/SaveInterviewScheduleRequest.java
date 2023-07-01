package com.biplus.saga.domain.request.recruitment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SaveInterviewScheduleRequest {

    private Long id;
    private Long candidateId;
    private String title;
    private String interviewFromTime;
    private String interviewToTime;
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

}
