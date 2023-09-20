public class SubTask extends Task {
    protected int epicId ;

    public SubTask(String name, String description, int statusId, int epicId ) {
        super.name = name;
        super.description = description;
        super.statusId = statusId;
        this.epicId  = epicId ;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                " uniqueId='" + id + '\'' +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", epicId ='" + epicId  + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
