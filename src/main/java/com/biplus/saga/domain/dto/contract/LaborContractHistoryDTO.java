package com.biplus.saga.domain.dto.contract;

import com.biplus.saga.domain.dto.AbstractBaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LaborContractHistoryDTO extends AbstractBaseDTO implements Serializable {
    private String status;
    private String note;
}

