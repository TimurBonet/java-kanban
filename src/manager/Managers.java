package manager;

import task.Task;

public final class Managers <T extends Task>{

    static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    static TaskManager Default(){
        return new InMemoryTaskManager();
    }
}
