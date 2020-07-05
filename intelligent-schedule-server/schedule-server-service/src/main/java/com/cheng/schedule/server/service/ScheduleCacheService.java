package com.cheng.schedule.server.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ScheduleCacheService {

    /**
     * 待产生Set
     */
    private Map<Long, Long> generateSet = new ConcurrentHashMap();
    /**
     * 待分发任务Set
     */
    private Map<Long, Long> dispatchSet = new ConcurrentHashMap<>();

    public boolean addGenerateQueue(Long id) {
        if (generateSet.containsKey(id)) {
            return false;
        }
        generateSet.put(id, System.currentTimeMillis());
        return true;
    }

    /**
     * @param id
     * @return
     */
    public void removeGenerateQueue(Long id) {
        generateSet.remove(id);
    }

    public boolean addDispatchQueue(Long id) {
//        if (dispatchSet.containsKey(id)) {
//            return false;
//        }
//        dispatchSet.put(id, System.currentTimeMillis());
        return true;
    }

    /**
     * @param id
     * @return
     */
    public void removeDispatchQueue(Long id) {
        dispatchSet.remove(id);
    }
}
