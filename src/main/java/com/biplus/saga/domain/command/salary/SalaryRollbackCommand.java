package com.biplus.saga.domain.command.salary;

import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.producer.TargetMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.salary.domain.command.SalaryRollbackCommand")
public class SalaryRollbackCommand implements CompensatableCommand, Serializable {
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
}
