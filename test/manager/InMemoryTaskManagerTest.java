package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

public class InMemoryTaskManagerTest<T extends TaskManager> extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp(){
        taskManager = new InMemoryTaskManager();
        task = new Task("nt1","dt1","NEW","08-09_09.05.2024", 56);
        taskManager.createTask(task);
        epic = new Epic("ne1","de1");
        taskManager.createEpic(epic);
        subTask = new SubTask("nst11","dst11","NEW", epic.getId(),"23-23_23.12.2023", 12);
        taskManager.createSubTask(subTask);
    }


    @Override
    @Test
    void createTask() {
        super.createTask();

    }

    @Override
    @Test
    void createSubTask() {
        super.createSubTask();

    }

    @Override
    @Test
    void createEpic() {
        super.createEpic();

    }

    @Override
    @Test
    void setStatus() {
        super.setStatus();
    }

    @Override
    @Test
    void updateTask() {
        super.updateTask();
    }

    @Override
    @Test
    void updateSubTask() {
        super.updateSubTask();
    }

    @Override
    @Test
    void updateEpic() {
        super.updateEpic();
    }

    @Override
    @Test
    void getStatus() {
        super.getStatus();
    }

    @Override
    @Test
    void getEpicById() {
        super.getEpicById();
    }

    @Override
    @Test
    void getTaskById() {
        super.getTaskById();
    }

    @Override
    @Test
    void getSubTaskById() {
        super.getSubTaskById();
    }

    @Override
    @Test
    void getTaskList() {
        super.getTaskList();
    }

    @Override
    @Test
    void getSubTaskList() {
        super.getSubTaskList();
    }

    @Override
    @Test
    void getEpicList() {
        super.getEpicList();
    }

    @Override
    @Test
    void getEpicSubtasks() {
        super.getEpicSubtasks();
    }

    @Override
    @Test
    void clearAllTasks() {
        super.clearAllTasks();
    }

    @Override
    @Test
    void clearAllSubTasks() {
        super.clearAllSubTasks();
    }

    @Override
    @Test
    void clearAllEpic() {
        super.clearAllEpic();
    }

    @Override
    @Test
    void clearTaskById() {
        super.clearTaskById();
    }

    @Override
    @Test
    void clearSubTaskById() {
        super.clearSubTaskById();
    }

    @Override
    @Test
    void clearEpicById() {
        super.clearEpicById();
    }

    @Override
    @Test
    void getHistory() {
        super.getHistory();
    }
}
