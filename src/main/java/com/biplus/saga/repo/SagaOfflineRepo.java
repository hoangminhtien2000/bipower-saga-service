package com.biplus.saga.repo;

import com.biplus.saga.domain.entity.SagaOffline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SagaOfflineRepo extends JpaRepository<SagaOffline, Long>, JpaSpecificationExecutor<SagaOffline> {
    List<SagaOffline> findByBatchCode(String batchCode);

    List<SagaOffline> findByBatchCodeAndStatus(String batchCode, Integer status);

    List<SagaOffline> findByBatchCodeAndStatusIn(String batchCode, List<Integer> statusList);

    SagaOffline findBySagaOfflineId(Long sagaOfflineId);

    @Query("select case when count(a) > 0 then true else false end from SagaOffline a where a.account = ?1 and a.status in (0, 1)")
    boolean existsAccountInProcess(String account);

    @Query("select case when count(a) > 0 then true else false end from SagaOffline a where a.batchCode = ?1 and a.isRegistered = 1 and a.customerId is not null and a.agreementId is not null")
    boolean checkHasRegistered(String batchCode);

    List<SagaOffline> findByOrderIdAndStatusIn(Long orderId, List<Integer> listStatus);
    boolean existsByOrderIdAndSagaTypeAndStatus(Long orderId, String sagaType, Integer status);

    List<SagaOffline> findByOrderIdAndSagaType(Long orderId, String sagaType);
}
