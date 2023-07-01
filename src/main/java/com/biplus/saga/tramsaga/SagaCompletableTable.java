package com.biplus.saga.tramsaga;

import com.biplus.saga.domain.dto.ActionType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SagaCompletableTable {
    private final ConcurrentMap<SagaCompletableKey, CompletableFuture> completableFutureMap = new ConcurrentHashMap<>();

    public void put(ActionType action, String sagaId, CompletableFuture completableFuture) {
        completableFutureMap.put(new SagaCompletableKey(action, sagaId), completableFuture);
    }

    public CompletableFuture get(ActionType action, String sagaId) {
        return completableFutureMap.get(new SagaCompletableKey(action, sagaId));
    }

    public void remove(ActionType action, String sagaId) {
        completableFutureMap.remove(new SagaCompletableKey(action, sagaId));
    }

    public int size() {
        return completableFutureMap.size();
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class SagaCompletableKey {
        private ActionType action;
        private String sagaId;

    }
}
