package com.biplus.saga.repo;

import com.biplus.saga.domain.entity.NotifyMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyMessageRepo extends JpaRepository<NotifyMessage, Long> {
    List<NotifyMessage> findByBatchCode(String batchCode);

    List<NotifyMessage> findByNotifyMessageId(Long notifyMessageId);

    List<NotifyMessage> findUnreadByReceiver(String receiver);

    Page<NotifyMessage> findByReceiver(String receiver, Pageable pageable);
}
