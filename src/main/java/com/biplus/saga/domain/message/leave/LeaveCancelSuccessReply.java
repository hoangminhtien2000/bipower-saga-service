package com.biplus.saga.domain.message.leave;

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
@SourceMessage("com.biplus.salary.domain.message.LeaveCancelSuccessReply")
public class LeaveCancelSuccessReply implements Serializable {
    private List<EmailInfoDTO> listMailInfo;
    private Boolean isSendMail;
    private List<EmployeeLeaveDTO> data;
}
