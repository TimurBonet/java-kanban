package task;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static manager.TasksTypes.TASK;

public class Task {
    protected String type;
    protected String name;
    protected String description;
    protected String status;
    protected int id;
    protected int epicId;
    protected Long duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task() {
    }

    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.status = task.status;
        this.description = task.description;
        this.duration = task.duration;
        this.startTime = task.startTime;
    }

    public Task(String name, String description, String status, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TASK.getType();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm_dd.MM.yyyy");
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) {
            return null;
        }
        return startTime;
    }

    public LocalDateTime getEndTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm_dd.MM.yyyy");
        if (duration == 0 && startTime == null) {
            return LocalDateTime.parse("00-00_01.01.1970", formatter);
        } else if (duration == 0) {
            return startTime;
        } else {
            return getStartTime().plus(Duration.ofMinutes(duration));
        }
    }

    public Long getDuration() {
        return duration;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return this.type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    @Override
    public String toString() {
        return "Tasks.Task{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}' + "\n";
    }
}
