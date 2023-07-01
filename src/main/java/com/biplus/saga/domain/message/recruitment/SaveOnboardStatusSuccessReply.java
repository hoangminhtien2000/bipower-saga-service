package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SourceMessage("com.biplus.recruitment.domain.message.SaveOnboardStatusSuccessReply")
public class SaveOnboardStatusSuccessReply implements Serializable {

    private Long candidateId;
    private String candidateName;
    private String candidateLevel;
    private String candidatePosition;
    private String stackTech;
    private String emailTemplate;
    private List<String> toEmails;
    private Long netSalary;

}
