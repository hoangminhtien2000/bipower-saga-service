package com.biplus.saga.domain.command.email;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.producer.TargetMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.email.domain.command.SendEmailCommand")
public class SendEmailCommand implements CompensatableCommand, Serializable {
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
    private String businessCode;
    private List<String> paramsSubject;
    private Map<String, Object> paramsContent;
    private List<String> toEmails;
    private List<String> ccEmails;
    private Boolean isSendMail;
    private boolean isCustomSubject;
    private String subjectCustom;
}
