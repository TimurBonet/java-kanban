package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    /*int uniqueId = 0;
    public HashMap<Integer, Task> taskMap = new HashMap<>();
    public HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    public HashMap<Integer, Epic> epicMap = new HashMap<>();*/

    void assigningId();

    void createTask(Task newTask);

    String setStatus(String status);

    void updateTask(Task newTask);

    void createSubTask(SubTask newSubTask);

    void updateSubTask(SubTask newSubTask);

    void createEpic(Epic newEpic);

    void updateEpic(Epic newEpic);

    String getStatus(List<SubTask> subTaskForEpic);

    // Блок получения задачи по ID

    Epic getEpicById(int uniqueId);

    Task getTaskById(int uniqueId);

    SubTask getSubTaskById(int uniqueId);

    // Блок вызова списка задач (Стоит ли попробовать унифицировать это поигравшись с <T extends Class> ?? или нет смысла?)

    List<Task> getTaskList(HashMap<Integer, Task> task);

    List<SubTask> getSubTaskList(HashMap<Integer, SubTask> subTask);

    List<Epic> getEpicList(HashMap<Integer, Epic> epic);

    List<SubTask> getEpicSubtasks(int uniqueId);

    // Блок удаления всех задач

    void clearAllTasks(HashMap<Integer, Task> task);

    void clearAllSubTasks(HashMap<Integer, SubTask> subTask);

    void clearAllEpic(HashMap<Integer, Epic> epic);

    // Блок удаления по ID

    void clearTaskById(int uniqueId);

    void clearSubTaskById(int uniqueId);

    void clearEpicById(int uniqueId);

}
