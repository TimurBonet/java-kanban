package manager;

import task.Task;

import java.io.File;

public final class Managers <T extends Task>{

    static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(){
       // return new InMemoryTaskManager();
        return new FileBackedTasksManager(new File("tasks.csv"));
    }
}
