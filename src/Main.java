import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createTask(new Task("Поездка", "собрать вещи в дорогу", "NEW"));
        taskManager.createTask(
                new Task("Покупка столика", "Выбрать столик в мебельном", "IN_PROGRESS"));
        //System.out.println("Проверка списка Tasks.Task");
        //System.out.println(taskManager.getTaskList(taskManager.getTaskMap()));

        taskManager.createEpic(
                new Epic("Переселение", "Собираем вещи, перевозим"));
        taskManager.createEpic(
                new Epic("Учеба", "Разбираем по винтикам темы"));
        //System.out.println("Проверка списка Tasks.Epic");
        //System.out.println(taskManager.getEpicList(taskManager.getEpicMap()));

        taskManager.createSubTask(
                new SubTask("Перевозка вещей", "Собираем вещи, перевозим", "DONE", 2));
        taskManager.createSubTask(
                new SubTask("Перевозим посуду", "Укладываем посуду", "DONE", 2));
        taskManager.createSubTask(
                new SubTask("Перевозим мебель", "Разобрать и перевезти диван", "IN_PROGRESS", 2));
        taskManager.createSubTask(
                new SubTask("Подготовка к учебе", "раскладываем тетрадки", "NEW", 3));
        /*System.out.println("Проверка списка subTask");
        System.out.println(taskManager.getSubTaskList(taskManager.getSubTaskMap()));
        System.out.println("Проверка списка Tasks.Epic после внесения subTask");
        System.out.println(taskManager.getEpicList(taskManager.getEpicMap()));
        System.out.println("Проверка списка вызова по ID");
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getTaskById(0));
        System.out.println("Проверка вызова списка Tasks.SubTask из Tasks.Epic");
        System.out.println(taskManager.getEpicSubtasks(3));

        System.out.println("Проверка удаления Tasks.SubTask по Id");
        taskManager.clearSubTaskById(6);
        System.out.println("Просмотрим через вызов эпика Id=2, где была эта субзадача," +
                " статус должен смениться на 'DONE'");
        System.out.println(taskManager.getEpicById(2));
        System.out.println("Проверка удаления Tasks.Epic по Id, далее вызов списка Tasks.SubTask," +
                " все связанные с Tasks.Epic Tasks.SubTask должны удаляться");
        taskManager.clearEpicById(2);
        System.out.println(taskManager.getSubTaskList(taskManager.getSubTaskMap()));
        System.out.println(taskManager.getEpicList(taskManager.getEpicMap()));
        System.out.println("--------------------------");
        System.out.println("Проверка обновления Tasks.SubTask (смотрим статус Tasks.Epic)"); // на вход берём имеющуюся
        SubTask updateSubtask = taskManager.getSubTaskMap().get(7);                               // вносим изменения
        updateSubtask.setStatus("IN_PROGRESS");
        updateSubtask.setDescription("тетради разложены");
        taskManager.updateSubTask(updateSubtask);                                            // обновляем
        System.out.println(taskManager.getEpicList(taskManager.getEpicMap()));
        System.out.println(taskManager.getSubTaskList(taskManager.getSubTaskMap()));
        System.out.println("************************");
        System.out.println("Проверка замены Tasks.Epic по Id  (оставляем Tasks.SubTask)");
        Epic updateEpic = taskManager.getEpicMap().get(3);
        updateEpic.setDescription("Пришли на другой урок");
        updateEpic.setName("Другой урок");
        taskManager.updateEpic(updateEpic);
        System.out.println(taskManager.getEpicList(taskManager.getEpicMap()));*/
        System.out.println("-----Вызываем 11 задач по id-------");

        taskManager.getEpicById(2);
        taskManager.getEpicById(3);
        taskManager.getSubTaskById(7);
        taskManager.getSubTaskById(5);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(7);
        taskManager.getEpicById(3);
        taskManager.getSubTaskById(5);
        taskManager.getTaskById(0);
        taskManager.getSubTaskById(7);
        taskManager.getTaskById(1);
        System.out.println("-----END----");
        //HistoryManager historyManager = new InMemoryHistoryManager();
        System.out.println(taskManager.getHistory());
        System.out.println("Длина списка " + taskManager.getHistory().size());
        taskManager.clearSubTaskById(5);
        System.out.println(taskManager.getHistory());
        System.out.println("Длина списка " + taskManager.getHistory().size());
        taskManager.clearEpicById(2);
        System.out.println(taskManager.getHistory());
        System.out.println("Длина списка " + taskManager.getHistory().size());
        taskManager.clearEpicById(3);
        System.out.println(taskManager.getHistory());
        System.out.println("Длина списка " + taskManager.getHistory().size());
        taskManager.clearTaskById(0);
        System.out.println(taskManager.getHistory());
        System.out.println("Длина списка " + taskManager.getHistory().size());
    }
}
