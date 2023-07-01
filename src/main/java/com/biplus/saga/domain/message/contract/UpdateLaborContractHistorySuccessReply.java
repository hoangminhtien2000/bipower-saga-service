package com.biplus.saga.domain.message.contract;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.AllArgsConstructor;
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
@SourceMessage("com.biplus.contract.domain.message.UpdateLaborContractHistorySuccessReply")
public class UpdateLaborContractHistorySuccessReply implements Serializable {
    private Long employeeId;
    private String laborContractType;
    private String laborContractStatus;
    private Long netSalary;

    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private String businessCode;
    private Boolean isSendMail;
}
