package com.biplus.saga.domain.message.salary;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.dto.email.EmailInfoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SourceMessage("com.biplus.salary.domain.message.OvertimeApproveSuccessReply")
public class OvertimeApproveSuccessReply implements Serializable {
    private List<EmailInfoDTO> listMailInfo;
    private Boolean isSendMail;
}
