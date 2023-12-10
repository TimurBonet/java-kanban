package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static manager.TasksTypes.EPIC;

public class Epic extends Task {
    protected List<SubTask> subTaskForEpic = new ArrayList<>(); // если поле static то startTime 1го сабтаска отображается, но у всех.
                          // в противном случае не вносятся изменения в startTime и Duration, а дублировать из в Epic, думаю, неверно

    public Epic(String name, String description) {
        super.name = name;
        super.description = description;
        super.type = EPIC.getType();
        super.startTime = getStartTime();
        super.duration = getDuration();
        super.endTime = getEndTime();
    }

    public List<SubTask> getSubTaskForEpic() {
        return this.subTaskForEpic;
    }
    @Override
    public Long getDuration(){
        long i = 0;
        if(subTaskForEpic.isEmpty()){
            return i;
        }
        for (SubTask s: subTaskForEpic ) {
            i+=s.getDuration();
        }
        return  i;
    }
    @Override
    public void setStartTime(LocalDateTime startTime){
        super.startTime = startTime;
    }
    @Override
    public LocalDateTime getStartTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm_dd.MM.yyyy");
        if (!this.subTaskForEpic.isEmpty()) {
            startTime = this.subTaskForEpic.get(0).getStartTime();
        }else {
            return LocalDateTime.parse("00-00_01.01.1970",formatter);
        }
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }
    public void setEndTime() {
        super.endTime = super.startTime.plus(Duration.ofMinutes(super.duration));
    }

    @Override
    public String toString() {
        return "Tasks.Epic{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ",  subtasks=' " + subTaskForEpic + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", endTime='" + endTime + '\'' +
                '}' + "\n";

    }


}
