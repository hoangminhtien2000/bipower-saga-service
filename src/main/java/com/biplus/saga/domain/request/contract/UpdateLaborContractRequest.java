package com.biplus.saga.domain.request.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class UpdateLaborContractRequest {
    private Long contractId;
    private byte[] fileContract;
    private String fileName;
    private Integer contractTerm;

    private String laborContractPath;

    private String resultEvaluation;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate contractTerminationDate;

    private Boolean isSendApproval;

    private Boolean isFirstContract = false;

    private Long insuranceSalary = 0L;

    private Long netSalary = 0L;

    private Long grossSalary = 0L;

    private Long probationalSalary = 0L;

    private Long officialSalary = 0L;

    private Long negotiableSalary = 0L;

    private Long contractSalary = 0L;

    private String contractCode;
}
