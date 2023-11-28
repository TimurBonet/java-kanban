package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static manager.TasksTypes.EPIC;

public class Epic extends Task {
    protected List<SubTask> subTaskForEpic = new ArrayList<>(); // если поле static то startTime 1го сабтаска отображается, но у всех.
    protected LocalDateTime endTime =null;                      // в противном случае не вносятся изменения в startTime и Duration, а дублировать из в Epic, думаю, неверно


    public Epic(String name, String description) {
        super.name = name;
        super.description = description;
        super.type = EPIC.getType();
        getEpicStartTime();
        this.duration = Duration.ofMinutes(this.epicDuration());
        this.endTime = getEndTime();
    }

    public List<SubTask> getSubTaskForEpic() {
        return this.subTaskForEpic;
    }

    public long  epicDuration(){
        long i = 0;
        List<SubTask> sbt = this.getSubTaskForEpic();
        for (SubTask s: sbt ) {
            i+=s.getDuration();
        }
        return  i;
        //super.duration = Duration.ofMinutes(i);
    }

    public void getEpicStartTime(){
        if (!this.getSubTaskForEpic().isEmpty()) {
            this.startTime = this.getSubTaskForEpic().get(0).getStartTime();

        }else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm dd.MM.yyyy");
            this.startTime =  LocalDateTime.parse("00-00 01.01.1970", formatter);
        }
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
                ",  subtasks=' " + this.subTaskForEpic + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration.toMinutes() + '\'' +
                ", endTime='" + endTime + '\'' +
                '}' + "\n";

    }


}
