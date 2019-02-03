package ru.alex.vic.rest;

import java.util.Objects;

import static ru.alex.vic.rest.TaskStatus.Status.INIT;
import static ru.alex.vic.rest.TaskStatus.Status.RUN;
import static ru.alex.vic.rest.TaskStatus.Status.STOP;

public class TaskStatus {

    public enum Status {
        INIT, RUN, STOP
    }

    public boolean isRunning() {
        return status == RUN;
    }

    public boolean isStoped() {
        return status == STOP;
    }

    public boolean isInitialized() {
        return status == INIT;
    }


    private final String name;

    private volatile int complete;

    private volatile Status status;


    public TaskStatus(String name) {
        this.name = name;
        this.status = INIT;
    }


    public String getName() {
        return name;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public void start() {
        this.status = RUN;
    }

    public void stop() {
        this.status = STOP;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskStatus that = (TaskStatus) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "TaskStatus{" +
                "name='" + name + '\'' +
                ", complete=" + complete +
                ", status=" + status +
                '}';
    }
}
