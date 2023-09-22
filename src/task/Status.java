package task;

public enum Status {
    NEW ("NEW"),
    IN_PROGRESS ("IN_PROGRESS"),
    DONE ("DONE");

    private String title ;

    Status (String title) {
        this.title = title;
    }

    public String getTitle () {
        return title;
    }

}
