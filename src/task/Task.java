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
    protected Duration duration = null;
    protected LocalDateTime startTime = null;

    public Task() {
    }

    public Task(String name, String description, String status, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TASK.getType();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm_dd.MM.yyyy");
        this.startTime = LocalDateTime.parse(startTime,formatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public LocalDateTime getEndTime(){
        return getStartTime().plus(duration);
    }

    public long getDuration(){
        try {
            return duration.toMinutes();
        }catch (NullPointerException e){
            e.getMessage();
        }
        return 0;
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

    @Override
    public String toString() {
        return "Tasks.Task{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration.toMinutes() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}' + "\n";
    }
}
