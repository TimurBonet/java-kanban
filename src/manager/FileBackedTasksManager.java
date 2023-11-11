package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.*;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private String directory = "c:\\tasksDirectory";
    private String fileName = "\\tasks.csv";
    private String history;
    static List<Integer> historyFromString;
    private Path path;
    private HashMap<Integer, String> saveMap = new HashMap<>();
    private String  header = "id,type,name,status,description,epic";
    static InMemoryTaskManager taskManager = new InMemoryTaskManager(); // обратить внимание
    HistoryManager manager = this.inMemoryHistoryManager;
    
    public void writeHeader(String text, String directory, String fileName) throws IOException{
        path = Files.createFile(Paths.get(directory, fileName));
        Files.writeString(path, text, StandardOpenOption.APPEND);
        Files.writeString(path, "\n", StandardOpenOption.APPEND);
    }

    public void save() {
        try {
            if (!Files.isDirectory(Paths.get(directory))) {
                Files.createDirectory(Paths.get(directory));
                writeHeader(header,directory,fileName);
            } else if (!Files.exists(Paths.get(directory, fileName))) {
                writeHeader(header,directory,fileName);
            }
            path = Path.of(directory, fileName);
            Files.deleteIfExists(path);
            writeHeader(header,directory,fileName);
            List<Task> tsk = this.getTaskList(this.taskMap);
            for (Task t : tsk) {
                saveMap.put(t.getId(), toString(t));
            }
            List<SubTask> sbTsk = this.getSubTaskList(this.subTaskMap);
            for (SubTask s : sbTsk) {
                saveMap.put(s.getId(), toString(s));
            }
            List<Epic> ep = this.getEpicList(this.epicMap);
            for (Epic e : ep) {
                saveMap.put(e.getId(), toString(e));
            }
            for (Integer i : saveMap.keySet()) {
                Files.writeString(path, saveMap.get(i), StandardOpenOption.APPEND);
                Files.writeString(path, "\n", StandardOpenOption.APPEND);
            }
            history = historyToString(manager);
            Files.writeString(path, "\n", StandardOpenOption.APPEND);
            Files.writeString(path, history, StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new ManagerSaveException("Не могу прочитать файл" + fileName + e);
        }

    }

    //public void loadFromFile(Path path) {
    static FileBackedTasksManager loadFromFile(File file){
        try (BufferedReader csvReader = new BufferedReader(new FileReader(file))) {
            List<String> linesOfCSV = new ArrayList<>();
            List<String[]> lines = new ArrayList<>();
            int idFromSave = -1;
            while (csvReader.ready()) {
                linesOfCSV.add(csvReader.readLine());
            }
            for (String s : linesOfCSV) {                   
                lines.add(s.split(","));
            }
            idFromSave = getMaxIdFromFile(lines);
            System.out.println("Max: " + idFromSave);
            parseFromFileToTasks(lines);
            taskManager.uniqueId = idFromSave + 1;
        } catch (IOException e) {
            System.out.println("Not found");
        }
        return new FileBackedTasksManager();
    }
    
    public static int getMaxIdFromFile(List<String[]> lines){
        int idTasks = -1;
        for (int i = 1; i < lines.size(); i++) {
            if(lines.get(i)[0].isEmpty()){
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
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + task.getEpicId();
    }

    public static void parseFromFileToTasks(List<String[]> lines) {   // мой аналог Task fromString(String value) если не подходит, буду переписывать.
        int id = -1;
        List<Integer> historyId = null;
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i)[0].isEmpty() && i < lines.size()-1){
                StringBuilder txt = new StringBuilder();
                for (String s : lines.get(i + 1)) {
                    txt.append(s).append(",");
                }
                historyId = historyFromString(txt.toString());
                historyFromString = historyId;
                i++;
            } else if(lines.get(i)[0].isEmpty()) {
                break;
            } else {
                switch (lines.get(i)[1]) {
                    case "TASK":
                        id = Integer.parseInt(lines.get(i)[0]);
                        Task task = new Task(lines.get(i)[2], lines.get(i)[4], lines.get(i)[3]);
                        task.setId(id);
                        System.out.println(task);
                        taskManager.taskMap.put(id, task);
                        break;
                    case "SUBTASK":
                        id = Integer.parseInt(lines.get(i)[0]);
                        SubTask subTask = new SubTask(lines.get(i)[2], lines.get(i)[4], lines.get(i)[3], Integer.parseInt(lines.get(i)[5]));
                        subTask.setId(id);
                        System.out.println(subTask);
                        taskManager.subTaskMap.put(id, subTask);
                        break;
                    case "EPIC":
                        id = Integer.parseInt(lines.get(i)[0]);
                        Epic epic = new Epic(lines.get(i)[2], lines.get(i)[4]);
                        epic.setId(id);
                        epic.setStatus(lines.get(i)[3]);
                        System.out.println(epic);
                        taskManager.epicMap.put(id, epic);
                        break;
                }
            }
        }
    }

    String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        List<Task> hist = manager.getHistory();
        if(hist.isEmpty()){
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
        Task thisTask =super.getTaskById(uniqueId);
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
    public List<Task> getTaskList(HashMap<Integer, Task> task) {
        return super.getTaskList(task);
    }

    @Override
    public List<SubTask> getSubTaskList(HashMap<Integer, SubTask> subTask) {
        return super.getSubTaskList(subTask);
    }

    @Override
    public List<Epic> getEpicList(HashMap<Integer, Epic> epic) {
        return super.getEpicList(epic);
    }

    @Override
    public List<SubTask> getEpicSubtasks(int uniqueId) {
        return super.getEpicSubtasks(uniqueId);
    }

    @Override
    public void clearAllTasks(HashMap<Integer, Task> task) {
        super.clearAllTasks(task);
    }

    @Override
    public void clearAllSubTasks(HashMap<Integer, SubTask> subTask) {
        super.clearAllSubTasks(subTask);
    }

    @Override
    public void clearAllEpic(HashMap<Integer, Epic> epic) {
        super.clearAllEpic(epic);
    }

    @Override
    public void clearTaskById(int uniqueId) {
        super.clearTaskById(uniqueId);
    }

    @Override
    public void clearSubTaskById(int uniqueId) {
        super.clearSubTaskById(uniqueId);
    }

    @Override
    public void clearEpicById(int uniqueId) {
        super.clearEpicById(uniqueId);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public static void main(String[] args) {
        Path path = Path.of("c:\\tasksDirectory\\tasks.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.createTask(new Task("это1", "собрать вещи в дорогу", "NEW"));
        fileBackedTasksManager.createTask(new Task("это2 столика", "Выбрать столик в мебельном", "IN_PROGRESS"));
        fileBackedTasksManager.createEpic(new Epic("это3", "Собираем вещи перевозим"));
        fileBackedTasksManager.createEpic(new Epic("это4", "Разбираем по винтикам темы"));
        fileBackedTasksManager.createSubTask(new SubTask("это5 вещей", "Собираем вещи перевозим", "DONE", 2));
        fileBackedTasksManager.createSubTask(new SubTask("это6 посуду", "Укладываем посуду", "DONE", 2));
        fileBackedTasksManager.createTask(new Task("это7", "собрать вещи в дорогу", "NEW"));
        fileBackedTasksManager.createTask(new Task("это8", "собрать вещи в дорогу", "NEW"));
        fileBackedTasksManager.getEpicById(2);
        fileBackedTasksManager.getTaskById(0);
        fileBackedTasksManager.getTaskById(1);
        for(Integer i : fileBackedTasksManager.saveMap.keySet()){
            System.out.println(fileBackedTasksManager.saveMap.get(i));
        }
        System.out.println(fileBackedTasksManager.history);

        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(file);
        for (Integer i : historyFromString){
            System.out.print(i + " ");
        }

    }
}
