package io.netty.example.study.client.dispatcher;

import io.netty.example.study.common.OperationResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestPendingCenter {

    private Map<Long, OperationResultFuture> map = new ConcurrentHashMap<>();

    public void add(Long streamId, OperationResultFuture future) {
        this.map.put(streamId, future);
    }

    public void set(Long steamId, OperationResult result) {
        OperationResultFuture resultFuture = this.map.get(steamId);
        if (resultFuture != null) {
            resultFuture.setSuccess(result);
            this.map.remove(steamId);
        }
    }

}
