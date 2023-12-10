package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static manager.TasksTypes.SUBTASK;

public class SubTask extends Task {

    public SubTask(String name, String description, String status, int epicId, String startTime, long duration) {
        super.name = name;
        super.description = description;
        super.status = status;
        super.epicId = epicId;
        super.type = SUBTASK.getType();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm_dd.MM.yyyy");
        super.startTime = LocalDateTime.parse(startTime,formatter);
        super.duration = duration;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Tasks.SubTask{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", epicId ='" + epicId + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}' + "\n";

    }
}
