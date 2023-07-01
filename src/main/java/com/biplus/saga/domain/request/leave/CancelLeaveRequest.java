package com.biplus.saga.domain.request.leave;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author congpham
 * @since on 27/07/2022
 */
@Getter
@Setter
public class CancelLeaveRequest {
    private List<Long> employeeLeaveIds;
    private String note;
}
