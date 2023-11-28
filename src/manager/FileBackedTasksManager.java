package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.lang.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file ;

    
    public FileBackedTasksManager(File file) {
         this.file = file;
    }

    public void save() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))){
            String header = "id,type,name,status,description,epic,startTime,duration";
            br.write(header);
            br.newLine();

            for (Integer i : taskMap.keySet()){
                final Task task = taskMap.get(i);
                br.write(toString(task));
                br.newLine();
            }

            for (Integer i : subTaskMap.keySet()){
                final Task task = subTaskMap.get(i);
                br.write(toString(task));
                br.newLine();
            }

            for (Integer i : epicMap.keySet()){
                final Task task = epicMap.get(i);
                br.write(toString(task));
                br.newLine();
            }

            br.newLine();
            br.write(historyToString(this.inMemoryHistoryManager));
            br.newLine();

        } catch (IOException e) {
            throw new ManagerSaveException("Не могу сохранить файл" + file.getName());

        }

    }

    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        if(file.exists()) {
            try (BufferedReader csvReader = new BufferedReader(new FileReader(file))) {
                List<String> linesOfCSV = new ArrayList<>();
                List<String[]> lines = new ArrayList<>();
                int idFromSave;
                while (csvReader.ready()) {
                    linesOfCSV.add(csvReader.readLine());
                }
                for (String s : linesOfCSV) {
                    lines.add(s.split(","));
                }
                idFromSave = getMaxIdFromFile(lines);
                System.out.println("Max: " + idFromSave);
                for (int j = 1; j < linesOfCSV.size(); j++) {
                    if (linesOfCSV.get(j).isEmpty() && j < linesOfCSV.size() - 1) {
                        StringBuilder txt = new StringBuilder();
                        for (String s : linesOfCSV.get(j + 1).split(",")) {
                            txt.append(s).append(",");
                        }
                        for (Integer i : historyFromString(txt.toString())) {
                            Task task = getHistoryFromList(i, fileBackedTasksManager);
                            fileBackedTasksManager.inMemoryHistoryManager.add(task);
                        }
                        break;
                    }
                    if (linesOfCSV.get(j).isEmpty() && j == linesOfCSV.size() - 1) {
                        break;
                    }
                    distributeTaskToMap(fromString(linesOfCSV.get(j)), fileBackedTasksManager);
                }
                for (SubTask s : fileBackedTasksManager.getSubTaskList()) {
                    Epic epic = fileBackedTasksManager.epicMap.get(s.getEpicId());
                    epic.getSubTaskForEpic().add(s);
                }
                fileBackedTasksManager.uniqueId = idFromSave + 1;
            } catch (IOException e) {
                throw new ManagerSaveException("Не могу прочитать из файла" + file.getName());
            }
        }
        return fileBackedTasksManager;
    }

    public static Task getHistoryFromList(Integer i, FileBackedTasksManager fileBackedTasksManager){
        Task task = null;
            if(fileBackedTasksManager.taskMap.containsKey(i)){
                task = fileBackedTasksManager.getTaskById(i);
            } else if(fileBackedTasksManager.subTaskMap.containsKey(i)){
                task = fileBackedTasksManager.getSubTaskById(i);
            } else if (fileBackedTasksManager.epicMap.containsKey(i)) {
                task = fileBackedTasksManager.getEpicById(i);
            }
            return task;
    }

    public static void distributeTaskToMap(Task task,FileBackedTasksManager fileBackedTasksManager) {
        switch (task.getType()) {
            case "TASK":
                fileBackedTasksManager.taskMap.put(task.getId(), task);
                break;
            case "SUBTASK":
                fileBackedTasksManager.subTaskMap.put(task.getId(), (SubTask) task);
                break;
            case "EPIC":
                fileBackedTasksManager.epicMap.put(task.getId(), (Epic) task);
                break;
        }
    }

    public static int getMaxIdFromFile(List<String[]> lines) {
        int idTasks = -1;
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i)[0].isEmpty()) {
                break;
            }
            int id = Integer.parseInt(lines.get(i)[0]);
            if (id > idTasks) {
                idTasks = id;
            }
        }
        return idTasks;
    }

    public String toString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm_dd.MM.yyyy");
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + task.getEpicId() + "," + task.getStartTime().format(formatter) + "," + task.getDuration();
    }

    public static Task fromString(String value) {
        int id = -1;
        String[] curLine = value.split(",");
        Task resultTask = null;
        switch (curLine[1]) {
            case "TASK":
                id = Integer.parseInt(curLine[0]);
                Task task = new Task(curLine[2], curLine[4], curLine[3],curLine[6] ,Long.parseLong(curLine[7]));
                task.setId(id);
                System.out.println(task);
                resultTask = task;
                break;
            case "SUBTASK":
                id = Integer.parseInt(curLine[0]);
                SubTask subTask = new SubTask(curLine[2], curLine[4], curLine[3], Integer.parseInt(curLine[5]), curLine[6], Long.parseLong(curLine[7]));
                subTask.setId(id);
                System.out.println(subTask);
                resultTask = subTask;
                break;
            case "EPIC":
                id = Integer.parseInt(curLine[0]);
                Epic epic = new Epic(curLine[2], curLine[4]);
                epic.setId(id);
                epic.setStatus(curLine[3]);
                System.out.println(epic);
                resultTask = epic;
                break;
        }
        return resultTask;
    }

    String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        List<Task> hist = manager.getHistory();
        if (hist.isEmpty()) {
            return "";
        }
        for (Task t : hist) {
            history.append(t.getId()).append(",");
        }
        return history.toString();
    }


    static List<Integer> historyFromString(String value) {
        String[] lineOfId = value.split(",");
        List<Integer> idList = new ArrayList<>(lineOfId.length);
        for (String s : lineOfId) {
            idList.add(Integer.parseInt(s));
        }
        return idList;
    }

    public  TreeSet<Task> getPrioritizedTasks() {
        TreeSet<Task> allTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        allTasks.addAll(taskMap.values());
        allTasks.addAll(subTaskMap.values());
        allTasks.addAll(epicMap.values());

        return allTasks;

    }


    @Override
    public HashMap<Integer, Task> getTaskMap() {
        return super.getTaskMap();
    }

    @Override
    public HashMap<Integer, SubTask> getSubTaskMap() {
        return super.getSubTaskMap();
    }

    @Override
    public HashMap<Integer, Epic> getEpicMap() {
        return super.getEpicMap();
    }

    // выше добавить чтение из файла
    @Override
    public HistoryManager getInMemoryHistoryManager() {
        return super.getInMemoryHistoryManager();
    }

    @Override
    void assigningId() {
        super.assigningId();
    }

    @Override
    public void createTask(Task newTask) {
        super.createTask(newTask);
        save();
    }

    @Override
    public String setStatus(String status) {
        return super.setStatus(status);
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void createSubTask(SubTask newSubTask) {
        super.createSubTask(newSubTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        super.updateSubTask(newSubTask);
        save();
    }

    @Override
    public void createEpic(Epic newEpic) {
        super.createEpic(newEpic);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public String getStatus(List<SubTask> subTaskForEpic) {
        return super.getStatus(subTaskForEpic);
    }

    @Override
    public Epic getEpicById(int uniqueId) {
        Epic thisEpic = super.getEpicById(uniqueId);
        save();
        return thisEpic;
    }

    @Override
    public Task getTaskById(int uniqueId) {
        Task thisTask = super.getTaskById(uniqueId);
        save();
        return thisTask;
    }

    @Override
    public SubTask getSubTaskById(int uniqueId) {
        SubTask thisSubTask = super.getSubTaskById(uniqueId);
        save();
        return thisSubTask;
    }

    @Override
    public List<Task> getTaskList() {
        return super.getTaskList();
    }

    @Override
    public List<SubTask> getSubTaskList() {
        return super.getSubTaskList();
    }

    @Override
    public List<Epic> getEpicList() {
        return super.getEpicList();
    }

    @Override
    public List<SubTask> getEpicSubtasks(int uniqueId) {
        return super.getEpicSubtasks(uniqueId);
    }

    @Override
    public void clearAllTasks(HashMap<Integer, Task> task) {
        super.clearAllTasks(task);
        save();
    }

    @Override
    public void clearAllSubTasks(HashMap<Integer, SubTask> subTask) {
        super.clearAllSubTasks(subTask);
        save();
    }

    @Override
    public void clearAllEpic(HashMap<Integer, Epic> epic) {
        super.clearAllEpic(epic);
        save();
    }

    @Override
    public void clearTaskById(int uniqueId) {
        super.clearTaskById(uniqueId);
        save();
    }

    @Override
    public void clearSubTaskById(int uniqueId) {
        super.clearSubTaskById(uniqueId);
        save();
    }

    @Override
    public void clearEpicById(int uniqueId) {
        super.clearEpicById(uniqueId);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public static void main(String[] args){

        File file = new File("tasks.csv");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.createTask(new Task("это1", "собрать вещи в дорогу", "NEW","21-13_11.12.2023", 69));
        fileBackedTasksManager.createTask(new Task("это2 столика", "Выбрать столик в мебельном", "IN_PROGRESS","15-13_17.12.2023", 34));
        fileBackedTasksManager.createEpic(new Epic("это3", "Собираем вещи перевозим"));
        fileBackedTasksManager.createEpic(new Epic("это4", "Разбираем по винтикам темы"));
        fileBackedTasksManager.createSubTask(new SubTask("это5 вещей", "Собираем вещи перевозим", "DONE", 2,"11-13_13.12.2023", 60));
        fileBackedTasksManager.createSubTask(new SubTask("это6 посуду", "Укладываем посуду", "DONE", 2,"12-56_16.12.2023", 20));
        fileBackedTasksManager.createTask(new Task("это7", "собрать вещи в дорогу", "NEW", "21-13_22.12.2023",50));
        fileBackedTasksManager.createTask(new Task("это8", "собрать вещи в дорогу", "NEW", "22-11_23.12.2023",20));
        fileBackedTasksManager.getEpicById(2);
        fileBackedTasksManager.getTaskById(0);
        fileBackedTasksManager.getTaskById(1);

        System.out.println(fileBackedTasksManager.inMemoryHistoryManager);
        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(file);
        System.out.println(fileBackedTasksManager2.getTaskList());
        System.out.println(fileBackedTasksManager2.getSubTaskList()); // здесь такой сложный метод из-за того что inMemoryTaskManager не static
        // конструктор public List<Task> getTaskList(HashMap<Integer, Task> task) {return new ArrayList<>(task.values());}  находится в InMemoryTaskManager
        System.out.println(fileBackedTasksManager2.getEpicList());
        System.out.println("Ниже сортированные задачи");
        System.out.println(fileBackedTasksManager2.getPrioritizedTasks());
    }
}
