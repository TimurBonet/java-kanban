import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int uniqueId = 0;
    public HashMap<Integer, Task> taskMap = new HashMap<>();
    public HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    public HashMap<Integer, Epic> epicMap = new HashMap<>();

    public void createTask(Task newTask) {                              // Создать новую обычную задачу
        newTask.id = uniqueId;
        newTask.status = setStatus(newTask.statusId);
        taskMap.put(uniqueId, newTask);
        uniqueId++;
    }

    public String setStatus(int statusId) {
        String result = "";
        switch (statusId) {
            case 1 :
                result = Status.NEW.getTitle();
                break;
            case 2 :
                result = Status.IN_PROGRESS.getTitle();
                break;
            case 3 :
                result = Status.DONE.getTitle();
                break;
            default:
                System.out.println("Ошибка. Выберите одно из трёх сотояний: " +
                        "1 - \"NEW\", 2 - \"IN_PROGRESS\", 3- \"DONE\"");
        }
        return result;
    }

    public void updateTask(int uniqueId, Task newTask) {
        if (!taskMap.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        newTask.status = setStatus(newTask.statusId);
        newTask.id = uniqueId;
        taskMap.put(uniqueId, newTask);
    }

    public void createSubTask(SubTask newSubTask) {                     // Создать новую субзадачу
        int epicId  = newSubTask.epicId ;                             // извлекаем имя эпика для которого она создана

        if (!epicMap.containsKey(epicId )) {
            System.out.println(epicId  + " - Эпик-задача с таким Id отуствует");
        }
        for (int key : epicMap.keySet()) {                                  // проходим по мапе с эпиками
            if (epicId  == key) {                                      //если ID из субзадачи эквивалентно  ID эпика
                newSubTask.status = setStatus(newSubTask.statusId);
                newSubTask.id = uniqueId;
                subTaskMap.put(uniqueId, newSubTask);                      // добавляем субзадачу в мапу субзадач
                uniqueId++;                                             // меняем айди
                return;
            }
        }
    }

    public void updateSubTask(int uniqueId, SubTask newSubTask) {
        if (!subTaskMap.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        newSubTask.status = setStatus(newSubTask.statusId);
        newSubTask.id = uniqueId;
        subTaskMap.put(uniqueId, newSubTask);
        for (Integer key : subTaskMap.keySet()) {
            if (newSubTask.epicId  == subTaskMap.get(key).epicId ) {
                epicMap.get(newSubTask.epicId ).subTaskStatuses.clear();
                epicMap.get(newSubTask.epicId ).subTaskStatuses.add(subTaskMap.get(key).status);
                epicMap.get(newSubTask.epicId ).getSubTaskForEpic().put(key, subTaskMap.get(key));
            }
        }
    }

    public void updateSubTask(/*int uniqueId, */ SubTask newSubTask) {
        if (!subTaskMap.containsKey(uniqueId)) {
        System.out.println("Неверный Id");
        return;
    }
    newSubTask.status = setStatus(newSubTask.statusId);
    newSubTask.id = uniqueId;
        subTaskMap.put(uniqueId, newSubTask);
        for (Integer key : subTaskMap.keySet()) {
        if (newSubTask.epicId  == subTaskMap.get(key).epicId ) {
            epicMap.get(newSubTask.epicId ).subTaskStatuses.clear();
            epicMap.get(newSubTask.epicId ).subTaskStatuses.add(subTaskMap.get(key).status);
            epicMap.get(newSubTask.epicId ).getSubTaskForEpic().put(key, subTaskMap.get(key));
        }
    }
}

    public void createEpic(Epic newEpic) {                              // Создать новый эпик-задачу
        newEpic.id = uniqueId;
        epicMap.put(uniqueId, newEpic);
        uniqueId++;
    }

    public void updateEpic(int uniqueId, Epic newEpic) {
        for (Integer key : subTaskMap.keySet()) {
            if (uniqueId == subTaskMap.get(key).epicId ) {
                newEpic.subTaskStatuses.add(subTaskMap.get(key).status);
                newEpic.getSubTaskForEpic().put(key, subTaskMap.get(key));
            }
        }
        newEpic.id = uniqueId;
        newEpic.status = getStatus(newEpic.subTaskStatuses);
        epicMap.put(uniqueId, newEpic);
    }

    public String getStatus(ArrayList<String> subTaskStatuses) {
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

    public String getEpicById(int uniqueId) {                           //Получить эпик-задачу по ID
        if (!epicMap.containsKey(uniqueId)) {
            return "Отсутствует Эпик - задача с таким ID.";
        }
        for (Integer key : subTaskMap.keySet()) {
            if (uniqueId == subTaskMap.get(key).epicId ) {
                epicMap.get(uniqueId).subTaskStatuses.add(subTaskMap.get(key).status);
                epicMap.get(uniqueId).getSubTaskForEpic().put(key, subTaskMap.get(key));
            }
        }
        Epic newEpic = epicMap.get(uniqueId);
        newEpic.status = getStatus(newEpic.subTaskStatuses);
        epicMap.put(uniqueId, newEpic);
        return epicMap.get(uniqueId).toString();
    }

    public String getTaskById(int uniqueId) {                               // Получить обычную задачу по ID
        String result = null;
        if (!taskMap.containsKey(uniqueId)) {
            return "Отсутствует стандартная задача с таким ID.";
        }
        for (Integer key : taskMap.keySet()) {
            if (uniqueId == key) {
                result = taskMap.get(uniqueId).toString();
            }
        }
        return /*"uniqueId='" + uniqueId + '\'' + " : " + */result;
    }

    public String getSubTaskById(int uniqueId) {                            // Получить субзадачу по ID
        String result = null;

        if (!subTaskMap.containsKey(uniqueId)) {
            return "Отсутствует субзадача с таким ID.";
        }
        for (Integer key : subTaskMap.keySet()) {
            if (uniqueId == key) {
                result = subTaskMap.get(uniqueId).toString();
            }
        }
        return result;
    }

// Блок вызова списка задач

    public void getTaskList(HashMap<Integer, Task> task) {
        for (Integer key : task.keySet()) {
            System.out.println(task.get(key));
        }
    }

    public void getSubTaskList(HashMap<Integer, SubTask> subTask) {
        for (Integer key : subTask.keySet()) {
            System.out.println(subTask.get(key));
        }
    }

    public void getEpicList(HashMap<Integer, Epic> epic) {
        for (Integer key : epic.keySet()) {
            for (Integer key2 : subTaskMap.keySet()) {
                if (subTaskMap.get(key2).epicId  == key2) {
                    epic.get(key).getSubTaskForEpic().put(key2, subTaskMap.get(key2));
                }
            }
        }
        for (Integer key : epic.keySet()) {
            System.out.println(this.getEpicById(key));
        }
    }

    public String getSubTaskOfEpic(int uniqueId) {            //Или лучше getSubTaskListOfThisEpic ?
        HashMap<Integer, SubTask> subTaskList;
        if (!epicMap.containsKey(uniqueId)) {
            return "Нет эпической задачи с таким Id.";
        }
        Epic thisEpic = epicMap.get(uniqueId);
        if (thisEpic.getSubTaskForEpic().isEmpty()) {
            return "Текущая эпик-задача не имеет субзадач";
        } else {
            subTaskList = thisEpic.getSubTaskForEpic();
        }
        return subTaskList.toString();
    }

// Блок удаления всех задач

    public void clearAllTasks(HashMap<Integer, Task> task) {
        task.clear();
    }

    public void clearAllSubTasks(HashMap<Integer, SubTask> subTask) {
        subTask.clear();
    }

    public void clearAllEpic(HashMap<Integer, Epic> epic) {
        subTaskMap.clear();                // предполагается что subTask могут существовать только как часть Epic задачи.
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
                epicMap.get(key).getSubTaskForEpic().remove(uniqueId);
                epicMap.get(key).subTaskStatuses.remove(subTaskMap.get(uniqueId).status);
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
                if (subTaskMap.get(key).epicId  == uniqueId) {
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
