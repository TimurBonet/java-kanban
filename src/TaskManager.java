import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int uniqueId = 0;
    public HashMap<Integer, Task> task = new HashMap<>();
    public HashMap<Integer, SubTask> subTask = new HashMap<>();
    public HashMap<Integer, Epic> epic = new HashMap<>();

    public void createTask(Task newTask) {                              // Создать новую обычную задачу
        task.put(uniqueId, newTask);
        uniqueId++;
    }

    public void updateTask(int uniqueId, Task newTask) {
        if (!task.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        task.put(uniqueId, newTask);
    }

    public void createSubTask(SubTask newSubTask) {                     // Создать новую субзадачу
        int idOfEpic = newSubTask.idOfEpic;                             // извлекаем имя эпика для которого она создана

        if (!epic.containsKey(idOfEpic)) {
            System.out.println(idOfEpic + " - Эпик-задача с таким Id отуствует");
        }
        for (int key : epic.keySet()) {                                  // проходим по мапе с эпиками
            if (idOfEpic == key) {                                      //если ID из субзадачи эквивалентно  ID эпика
                subTask.put(uniqueId, newSubTask);                      // добавляем субзадачу в мапу субзадач
                uniqueId++;                                             // меняем айди
                return;
            }
        }
    }

    public void updateSubTask(int uniqueId, SubTask newSubTask) {
        if (!subTask.containsKey(uniqueId)) {
            System.out.println("Неверный Id");
            return;
        }
        subTask.put(uniqueId, newSubTask);
        for (Integer key : subTask.keySet()) {
            if (newSubTask.idOfEpic == subTask.get(key).idOfEpic) {
                epic.get(newSubTask.idOfEpic).subTaskStatus.clear();
                epic.get(newSubTask.idOfEpic).subTaskStatus.add(subTask.get(key).status);
                epic.get(newSubTask.idOfEpic).subTaskForEpic.put(key, subTask.get(key));
            }
        }
    }

    public void createEpic(Epic newEpic) {                              // Создать новый эпик-задачу
        epic.put(uniqueId, newEpic);
        uniqueId++;
    }

    public void updateEpic(int uniqueId, Epic newEpic) {
        for (Integer key : subTask.keySet()) {
            if (uniqueId == subTask.get(key).idOfEpic) {
                newEpic.subTaskStatus.add(subTask.get(key).status);
                newEpic.subTaskForEpic.put(key, subTask.get(key));
            }
        }
        newEpic.status = newEpic.getStatus(newEpic.subTaskStatus);
        epic.put(uniqueId, newEpic);
    }

// Блок получения задачи по ID

    public String getEpicById(int uniqueId) {                           //Получить эпик-задачу по ID
        if (!epic.containsKey(uniqueId)) {
            return "Отсутствует Эпик - задача с таким ID.";
        }
        for (Integer key : subTask.keySet()) {
            if (uniqueId == subTask.get(key).idOfEpic) {
                epic.get(uniqueId).subTaskStatus.add(subTask.get(key).status);
                epic.get(uniqueId).subTaskForEpic.put(key, subTask.get(key));
            }
        }
        Epic newEpic = epic.get(uniqueId);
        newEpic.status = newEpic.getStatus(newEpic.subTaskStatus);
        epic.put(uniqueId, newEpic);
        return "uniqueId='" + uniqueId + '\'' + " : " + epic.get(uniqueId).toString();
    }

    public String getTaskById(int uniqueId) {                               // Получить обычную задачу по ID
        String result = null;
        if (!task.containsKey(uniqueId)) {
            return "Отсутствует стандартная задача с таким ID.";
        }
        for (Integer key : task.keySet()) {
            if (uniqueId == key) {
                result = task.get(uniqueId).toString();
            }
        }
        return "uniqueId='" + uniqueId + '\'' + " : " + result;
    }

    public String getSubTaskById(int uniqueId) {                            // Получить субзадачу по ID
        String result = null;

        if (!subTask.containsKey(uniqueId)) {
            return "Отсутствует субзадача с таким ID.";
        }
        for (Integer key : subTask.keySet()) {
            if (uniqueId == key) {
                result = subTask.get(uniqueId).toString();
            }
        }
        return "uniqueId='" + uniqueId + '\'' + " : " + result;
    }

// Блок вызова списка задач

    public void getTaskList(HashMap<Integer, Task> task) {
        for (Integer key : task.keySet()) {
            System.out.println("uniqueId='" + key + '\'' + " : " + task.get(key));
        }
    }

    public void getSubTaskList(HashMap<Integer, SubTask> subTask) {
        for (Integer key : subTask.keySet()) {
            System.out.println("uniqueId='" + key + '\'' + " : " + subTask.get(key));
        }
    }

    public void getEpicList(HashMap<Integer, Epic> epic) {
        for (Integer key : epic.keySet()) {
            for (Integer key2 : subTask.keySet()) {
                if (subTask.get(key2).idOfEpic == key2) {
                    epic.get(key).subTaskForEpic.put(key2, subTask.get(key2));
                }
            }
        }
        for (Integer key : epic.keySet()) {
            System.out.println(this.getEpicById(key));
        }
    }

    public String getSubTaskOfEpic(int uniqueId) {            //Или лучше getSubTaskListOfThisEpic ?
        HashMap<Integer, SubTask> subTaskList;
        if (!epic.containsKey(uniqueId)) {
            return "Нет эпической задачи с таким Id.";
        }
        Epic thisEpic = epic.get(uniqueId);
        if (thisEpic.subTaskForEpic.isEmpty()) {
            return "Текущая эпик-задача не имеет субзадач";
        } else {
            subTaskList = thisEpic.subTaskForEpic;
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
        subTask.clear();                // предполагается что subTask могут существовать только как часть Epic задачи.
        epic.clear();
    }

    // Блок удаления по ID

    public void clearTaskById(int uniqueId) {
        if (!task.containsKey(uniqueId)) {
            System.out.println("Нет обычной задачи с таким Id.");
        } else {
            task.remove(uniqueId);
        }
    }

    public void clearSubTaskById(int uniqueId) {
        if (!subTask.containsKey(uniqueId)) {
            System.out.println("Нет субзадачи с таким Id.");
        } else {
            for (Integer key : epic.keySet()) {
                epic.get(key).subTaskForEpic.remove(uniqueId);
                epic.get(key).subTaskStatus.remove(subTask.get(uniqueId).status);
            }
            subTask.remove(uniqueId);
        }
    }

    public void clearEpicById(int uniqueId) {
        if (!epic.containsKey(uniqueId)) {
            System.out.println("Нет эпик-задачи с таким Id.");
        } else {
            ArrayList<Integer> idOfSubTasks = new ArrayList<>();
            for (Integer key : subTask.keySet()) {
                if (subTask.get(key).idOfEpic == uniqueId) {
                    idOfSubTasks.add(key);
                }
            }
            for (int i : idOfSubTasks) {
                subTask.remove(i);
            }
            epic.remove(uniqueId);
        }
    }

}
