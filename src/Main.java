import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("Поездка", "собрать вещи в дорогу", 1));
        taskManager.createTask(new Task("Покупка столика", "Выбрать столик в мебельном", 2));
        System.out.println("Проверка списка Task");
        taskManager.getTaskList(taskManager.taskMap);

        taskManager.createEpic(
                new Epic("Переселение", "Собираем вещи, перевозим"));
        taskManager.createEpic(
                new Epic("Учеба", "Разбираем по винтикам темы"));
        System.out.println("Проверка списка Epic");
        taskManager.getEpicList(taskManager.epicMap);

        taskManager.createSubTask(
                new SubTask("Перевозка вещей", "Собираем вещи, перевозим", 3, 2 ));
        taskManager.createSubTask(
                new SubTask("Перевозим посуду", "Укладываем посуду", 3, 2 ));
        taskManager.createSubTask(
                new SubTask("Перевозим мебель", "Разобрать и перевезти диван", 2, 2));
        taskManager.createSubTask(
                new SubTask("Подготовка к учебе", "раскладываем тетрадки", 1, 3 ));
        System.out.println("Проверка списка subTask");
        taskManager.getSubTaskList(taskManager.subTaskMap);
        System.out.println("Проверка списка Epic после внесения subTask");
        taskManager.getEpicList(taskManager.epicMap);
        System.out.println("Проверка списка вызова по ID");
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getTaskById(0));
        System.out.println("Проверка вызова списка SubTask из Epic");
        System.out.println(taskManager.getSubTaskOfEpic(3));
        System.out.println("Проверка удаления SubTask по Id");
        taskManager.clearSubTaskById(6);
        System.out.println("Просмотрим через вызов эпика Id=2, где была эта субзадача," +
                " статус должен смениться на 'DONE'");
        System.out.println(taskManager.getEpicById(2));
        System.out.println("Проверка удаления Epic по Id, далее вызов списка SubTask," +
                " все свзяанные с Epic SubTask должны удаляться");
        taskManager.clearEpicById(2);
        taskManager.getSubTaskList(taskManager.subTaskMap);
        taskManager.updateSubTask(7,
                new SubTask("Подготовка к учебе"," достали ручки ",3,3));
        taskManager.getSubTaskList(taskManager.subTaskMap);
        taskManager.getEpicList(taskManager.epicMap);
        System.out.println("Проверка замены Epic по Id  (оставляем SubTask)");
        taskManager.updateEpic(3, new Epic("Другой урок","Пришли на другой урок"));
        taskManager.getEpicList(taskManager.epicMap);
    }
}
