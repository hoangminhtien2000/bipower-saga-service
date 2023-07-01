package com.biplus.saga.domain.message.attendance;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.dto.attendance.AttendanceHistoryDTO;
import com.biplus.saga.domain.dto.email.EmailInfoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SourceMessage("com.biplus.salary.domain.message.ConfirmManyRequestEditWorkingTimeSuccessReply")
public class ConfirmManyRequestEditWorkingTimeSuccessReply implements Serializable {
    private List<EmailInfoDTO> listMailInfo;
    private Boolean isSendMail;
    private List<AttendanceHistoryDTO> data;
}
