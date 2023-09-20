import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task {
    //private HashMap<Integer, SubTask> subTaskForEpic = new HashMap<>();
    private List<SubTask> subTaskForEpic = new ArrayList<>();
    private ArrayList<String> subTaskStatuses = new ArrayList<>();
    protected String status;

    public Epic(String name, String description) {
        super.name = name;
        super.description = description;
    }

    /*public HashMap<Integer, SubTask> getSubTaskForEpic() {
        return subTaskForEpic;
    }*/
    public List<SubTask> getSubTaskForEpic() {
        return subTaskForEpic;
    }


    public ArrayList<String> getSubTaskStatuses() {
        return subTaskStatuses;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        String subTask;
        if (subTaskForEpic.isEmpty()) {
            subTask = "отсутствуют";
        } else {
            subTask = subTaskForEpic.toString();
        }
        return "Epic{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ",  subtasks=' " + subTaskForEpic + '\'' +
                '}';
    }


}
