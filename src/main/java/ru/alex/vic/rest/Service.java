package ru.alex.vic.rest;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class Service {

    private final Map<String, TaskStatus> tasks = new ConcurrentHashMap<>();

    public TaskStatus startTask(String name) {
        return tasks.compute(name, (key, oldValue) -> {
            if (oldValue == null) {
                final TaskStatus res = new TaskStatus(name);
                return res;
            } else {

                return oldValue;
            }
        });
    }

    public TaskStatus progressTask(String name, Integer progres) {
        return tasks.compute(name, (key, oldValue) -> {
            if (oldValue == null) {
                throw new IllegalStateException();
            } else {
                oldValue.setComplete(progres);
                return oldValue;
            }
        });
    }

    public TaskStatus stopTask(String name) {
        return tasks.compute(name, (key, oldValue) -> {
            if (oldValue == null) {
                throw new IllegalStateException();
            } else {
                oldValue.stop();
                return oldValue;
            }
        });

    }
}
