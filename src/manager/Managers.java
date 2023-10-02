package manager;

import task.Task;

public final class Managers <T extends Task>{

    static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
}
