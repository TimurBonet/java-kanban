package manager;
import task.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    int uniqueId = 0;
    public HashMap<Integer, Task> taskMap = new HashMap<>();
    public HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    public HashMap<Integer, Epic> epicMap = new HashMap<>();

    public void assigningId(){
        uniqueId++;
    }


    public void createTask(Task newTask) {                              // Создать новую обычную задачу
        newTask.setId(uniqueId);
        taskMap.put(uniqueId, newTask);
        assigningId();
    }

    public String setStatus(String status) {
        String result = "";
        switch (status) {
            case "NEW":
                result = Status.NEW.getTitle();
                break;
            case "IN_PROGRESS":
                result = Status.IN_PROGRESS.getTitle();
                break;
            case "DONE":
                result = Status.DONE.getTitle();
                break;
            default:
                System.out.println("Ошибка. Выберите одно из трёх сотояний: " +
                        "1 - \"NEW\", 2 - \"IN_PROGRESS\", 3- \"DONE\"");
        }
        return result;
    }

    public void updateTask(Task newTask) {                  //  Теперь при обновлении задачи она заносится полностью
        int uniqueId = newTask.getId();                     //  что, вроде как, соответствует ТЗ
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        taskMap.put(uniqueId, newTask);
    }

    public void createSubTask(SubTask newSubTask) {                     // Создать новую субзадачу
        int epicId = newSubTask.getEpicId();                             // извлекаем имя эпика для которого она создана

        if (!epicMap.containsKey(epicId)) {
            System.out.println(epicId + " - Эпик-задача с таким Id отуствует");
        }
        newSubTask.setId(uniqueId);
        for (int key : epicMap.keySet()) {                                // проходим по мапе с эпиками
            if (epicId == key) {                                          //если ID из субзадачи эквивалентно ID эпика
                subTaskMap.put(uniqueId, newSubTask);                     // добавляем субзадачу в мапу субзадач
                epicMap.get(epicId).getSubTaskForEpic().add(newSubTask);  // добавляем субзадачу в список субзадач эпика
                assigningId();                                            // меняем айди
                return;
            }
        }
    }

    public void updateSubTask(SubTask newSubTask) {                     // обновить субзадачу
        int uniqueId = newSubTask.getId();
        if (!subTaskMap.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        for (Integer key : subTaskMap.keySet()) {
            if (newSubTask.getEpicId() == subTaskMap.get(key).getEpicId()) {
                for(SubTask sub : epicMap.get(newSubTask.getEpicId()).getSubTaskForEpic()) {
                    if(sub.getEpicId() == newSubTask.getEpicId()) {
                        int index = epicMap.get(newSubTask.getEpicId()).getSubTaskForEpic().indexOf(sub);
                        epicMap.get(newSubTask.getEpicId()).getSubTaskForEpic().set(index, newSubTask);
                    }
                }
            }
        }
    }

    public void createEpic(Epic newEpic) {                              // Создать новый эпик-задачу
        newEpic.setId(uniqueId);
        epicMap.put(uniqueId, newEpic);
        assigningId();
    }

    public void updateEpic(Epic newEpic) {
        int uniqueId = newEpic.getId();
        for (Integer key : subTaskMap.keySet()) {
            if (uniqueId == subTaskMap.get(key).getEpicId()) {
                newEpic.getSubTaskForEpic().add(subTaskMap.get(key));
            }
        }
        newEpic.setId(uniqueId);
        newEpic.setStatus(getStatus(newEpic.getSubTaskForEpic()));
        epicMap.put(uniqueId, newEpic);
    }

    public String getStatus(List<SubTask> subTaskForEpic) {
        ArrayList<String> subTaskStatuses = new ArrayList<>();
        for (SubTask s :subTaskForEpic) {
            subTaskStatuses.add(s.getStatus());
        }
        if (subTaskStatuses.isEmpty()) {
            return "NEW";
        }
        if ((subTaskStatuses.contains("NEW"))
                && (!subTaskStatuses.contains("IN_PROGRESS"))
                && (!subTaskStatuses.contains("DONE"))) {
            return "NEW";
        } else if ((subTaskStatuses.contains("DONE"))
                && (!subTaskStatuses.contains("NEW"))
                && (!subTaskStatuses.contains("IN_PROGRESS"))) {
            return "DONE";
        } else {
            return "IN_PROGRESS";
        }
    }

// Блок получения задачи по ID

    public Epic getEpicById(int uniqueId) {                           //Получить эпик-задачу по ID
        if (!epicMap.containsKey(uniqueId)) {
            System.out.println("Отсутствует Эпик - задача с таким ID.");
            return null;
        }
        Epic newEpic = epicMap.get(uniqueId);
        newEpic.setStatus(getStatus(newEpic.getSubTaskForEpic()));
        epicMap.put(uniqueId, newEpic);
        return epicMap.get(uniqueId);
    }

    public Task getTaskById(int uniqueId) {                               // Получить обычную задачу по ID
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Отсутствует стандартная задача с таким ID.");
        }
        return taskMap.get(uniqueId);
    }

    public SubTask getSubTaskById(int uniqueId) {                            // Получить субзадачу по ID
        if (!subTaskMap.containsKey(uniqueId)) {
            System.out.println("Отсутствует субзадача с таким ID.");
        }
        return subTaskMap.get(uniqueId);
    }

// Блок вызова списка задач

    public List<Task> getTaskList(HashMap<Integer, Task> task) {
        return new ArrayList<>(task.values());
    }

    public List<SubTask> getSubTaskList(HashMap<Integer, SubTask> subTask) {
        return new ArrayList<>(subTask.values());
    }

    public List<Epic> getEpicList(HashMap<Integer, Epic> epic) {
        return new ArrayList<>(epic.values());
    }

    public List<SubTask> getEpicSubtasks(int uniqueId) {
        if (!epicMap.containsKey(uniqueId)) {
            System.out.println("Нет эпической задачи с таким Id.");
        }
        //Epic thisEpic = epicMap.get(uniqueId);
        if (epicMap.get(uniqueId).getSubTaskForEpic().isEmpty()) {
            System.out.println("Текущая эпик-задача не имеет субзадач");
        }
        return epicMap.get(uniqueId).getSubTaskForEpic();
    }

// Блок удаления всех задач

    public void clearAllTasks(HashMap<Integer, Task> task) {
        task.clear();
    }

    public void clearAllSubTasks(HashMap<Integer, SubTask> subTask) {
        subTask.clear();
    }

    public void clearAllEpic(HashMap<Integer, Epic> epic) {
        subTaskMap.clear();                // предполагается что subTask могут существовать только как часть Tasks.Epic задачи.
        epic.clear();
    }

    // Блок удаления по ID

    public void clearTaskById(int uniqueId) {
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Нет обычной задачи с таким Id.");
        } else {
            taskMap.remove(uniqueId);
        }
    }

    public void clearSubTaskById(int uniqueId) {
        if (!subTaskMap.containsKey(uniqueId)) {
            System.out.println("Нет субзадачи с таким Id.");
        } else {
            for (Integer key : epicMap.keySet()) {
                epicMap.get(key).getSubTaskForEpic().remove(subTaskMap.get(uniqueId));
            }
            subTaskMap.remove(uniqueId);
        }
    }

    public void clearEpicById(int uniqueId) {
        if (!epicMap.containsKey(uniqueId)) {
            System.out.println("Нет эпик-задачи с таким Id.");
        } else {
            ArrayList<Integer> idOfSubTasks = new ArrayList<>();
            for (Integer key : subTaskMap.keySet()) {
                if (subTaskMap.get(key).getEpicId() == uniqueId) {
                    idOfSubTasks.add(key);
                }
            }
            for (int i : idOfSubTasks) {
                subTaskMap.remove(i);
            }
            epicMap.remove(uniqueId);
        }
    }

}