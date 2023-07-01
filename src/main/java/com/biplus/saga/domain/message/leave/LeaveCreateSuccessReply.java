package com.biplus.saga.domain.message.leave;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.dto.leave.EmployeeLeaveDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@SourceMessage("com.biplus.salary.domain.message.LeaveCreateSuccessReply")
public class LeaveCreateSuccessReply implements Serializable {
    private String businessCode;
    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private Boolean isSendMail;
    private EmployeeLeaveDTO data;
}
