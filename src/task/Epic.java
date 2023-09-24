package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<SubTask> subTaskForEpic = new ArrayList<>();
    //protected String status;

    public Epic(String name, String description) {
        super.name = name;
        super.description = description;
    }

    public List<SubTask> getSubTaskForEpic() {
        return subTaskForEpic;
    }

    @Override
    public String toString() {
        String subTask;
        if (subTaskForEpic.isEmpty()) {
            subTask = "отсутствуют";
        } else {
            subTask = subTaskForEpic.toString();
        }
        return "Tasks.Epic{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ",  subtasks=' " + subTaskForEpic + '\'' +
                '}' + "\n";
    }


}
