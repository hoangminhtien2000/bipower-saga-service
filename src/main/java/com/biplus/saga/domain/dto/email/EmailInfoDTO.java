package com.biplus.saga.domain.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class EmailInfoDTO {
    private String businessCode;
    private List<String> paramsSubject;
    private Map<String, Object> paramsContent;
    private List<String> toEmails;
    private List<String> ccEmails;
    private Boolean isSendMail;
}
