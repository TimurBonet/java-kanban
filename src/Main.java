import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("Поездка", "собрать вещи в дорогу", "NEW"));
        taskManager.createTask(new Task("Покупка столика", "Выбрать столик в мебельном", "IN_PROGRESS"));
        System.out.println("Проверка списка Tasks.Task");
        System.out.println(taskManager.getTaskList(taskManager.taskMap));

        taskManager.createEpic(
                new Epic("Переселение", "Собираем вещи, перевозим"));
        taskManager.createEpic(
                new Epic("Учеба", "Разбираем по винтикам темы"));
        System.out.println("Проверка списка Tasks.Epic");
        System.out.println(taskManager.getEpicList(taskManager.epicMap));

        taskManager.createSubTask(
                new SubTask("Перевозка вещей", "Собираем вещи, перевозим", "DONE", 2 ));
        taskManager.createSubTask(
                new SubTask("Перевозим посуду", "Укладываем посуду", "DONE", 2 ));
        taskManager.createSubTask(
                new SubTask("Перевозим мебель", "Разобрать и перевезти диван", "IN_PROGRESS", 2));
        taskManager.createSubTask(
                new SubTask("Подготовка к учебе", "раскладываем тетрадки", "NEW", 3 ));
        System.out.println("Проверка списка subTask");
        System.out.println(taskManager.getSubTaskList(taskManager.subTaskMap));
        System.out.println("Проверка списка Tasks.Epic после внесения subTask");
        System.out.println(taskManager.getEpicList(taskManager.epicMap));
        System.out.println("Проверка списка вызова по ID");
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getTaskById(0));
        System.out.println("Проверка вызова списка Tasks.SubTask из Tasks.Epic");
        System.out.println(taskManager.getEpicSubtasks(3));
     /*   System.out.println("-----");
        System.out.println(taskManager.getSubTaskList(taskManager.subTaskMap));
        System.out.println(taskManager.getEpicSubtasks(2));
        System.out.println("-----");*/
        System.out.println("Проверка удаления Tasks.SubTask по Id");
        taskManager.clearSubTaskById(6);
        System.out.println("Просмотрим через вызов эпика Id=2, где была эта субзадача," +
                " статус должен смениться на 'DONE'");
        System.out.println(taskManager.getEpicById(2));
        System.out.println("Проверка удаления Tasks.Epic по Id, далее вызов списка Tasks.SubTask," +
                " все свзяанные с Tasks.Epic Tasks.SubTask должны удаляться");
        taskManager.clearEpicById(2);
        taskManager.getSubTaskList(taskManager.subTaskMap);
        taskManager.updateSubTask(
                new SubTask("Подготовка к учебе"," достали ручки ","DONE",3));
        taskManager.getSubTaskList(taskManager.subTaskMap);
        taskManager.getEpicList(taskManager.epicMap);
        System.out.println("Проверка замены Tasks.Epic по Id  (оставляем Tasks.SubTask)");
        taskManager.updateEpic( new Epic("Другой урок","Пришли на другой урок"));
        taskManager.getEpicList(taskManager.epicMap);
    }
}
