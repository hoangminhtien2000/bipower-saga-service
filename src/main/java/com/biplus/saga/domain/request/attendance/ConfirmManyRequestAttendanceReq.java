package com.biplus.saga.domain.request.attendance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmManyRequestAttendanceReq {
    private List<Long> attendanceIds;
    private Boolean isApproved;
    private String note;
}
