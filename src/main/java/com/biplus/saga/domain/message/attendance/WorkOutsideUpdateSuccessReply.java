package com.biplus.saga.domain.message.attendance;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SourceMessage("com.biplus.salary.domain.message.WorkOutsideUpdateSuccessReply")
public class WorkOutsideUpdateSuccessReply implements Serializable {
    private String businessCode;
    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private Boolean isSendMail;
    private List<String> paramsSubject;
}
