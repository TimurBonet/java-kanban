package task;

import manager.TasksTypes;

import static manager.TasksTypes.SUBTASK;
import static manager.TasksTypes.TASK;

public class Task {
    protected String type;
    protected String name;
    protected String description;
    protected String status;
    protected int id;
    protected int epicId;


    public Task() {
    }

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TASK.getType();
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
                '}' + "\n";
    }
}
