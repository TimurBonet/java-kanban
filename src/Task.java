public class Task {
    String name;
    String description;
    String[] statusList = {"NEW", "IN_PROGRESS", "DONE"};
    String status;

    public Task() {
    }

    public Task(String name, String description, int statusId) {
        this.name = name;
        this.description = description;
        this.status = setStatus(statusId);
    }

    public String setStatus(int statusId) {
        if (statusId <= 0 && statusId >= 4) {
            String errorTextx = "Ошибка. Выберите одно из трёх сотояний: 1 - \"NEW\", 2 - \"IN_PROGRESS\", 3- \"DONE\"";
            return errorTextx;
        }
        return statusList[statusId - 1];
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
