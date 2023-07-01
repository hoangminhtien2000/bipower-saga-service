package com.biplus.saga.domain.message.employee;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SourceMessage("com.biplus.employee.domain.message.UpdateLaborContractTypeSuccessReply")
public class UpdateLaborContractTypeSuccessReply implements Serializable {
    private Long employeeId;
}
