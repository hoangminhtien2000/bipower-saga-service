package com.biplus.saga.domain.message.employee;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@SourceMessage("com.biplus.employee.domain.message.EmployeeCreateSuccessReply")
public class EmployeeCreateSuccessReply {
    private String businessCode;
    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private Boolean isSendMail;
}
