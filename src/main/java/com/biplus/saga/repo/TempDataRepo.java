package com.biplus.saga.repo;

import com.biplus.saga.domain.entity.TempData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempDataRepo extends JpaRepository<TempData, Long>, TempDataRepoCustom {
    TempData findFirstByRequestIdAndFunctionCodeAndTenant(String requestId, String functionCode, String tenant);
}
