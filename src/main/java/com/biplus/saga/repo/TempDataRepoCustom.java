package com.biplus.saga.repo;

import com.biplus.saga.domain.entity.TempData;

public interface TempDataRepoCustom {
    TempData findByOrderIdAndFunctionCode(Long orderId, String functionCode);
}
