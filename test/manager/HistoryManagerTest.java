package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    HistoryManager historyManager;
    InMemoryTaskManager taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
        task = new Task("nt1", "dt1", "NEW", "08-09_09.05.2024", 56);
        taskManager.createTask(task);
        epic = new Epic("ne1", "de1");
        taskManager.createEpic(epic);
        subTask = new SubTask("nst11", "dst11", "NEW", epic.getId(), "23-23_23.12.2023", 12);
        taskManager.createSubTask(subTask);
    }

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void remove() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        history.remove(0);
        assertNotNull(history, "История не пустая.");
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals(task, history.get(0), "Не соответствует запрашиваемому элементу");
    }
}