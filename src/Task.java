public class Task {
    int id;
    String name;
    String description;
    Status status;


    public Task(String name, String description) {
        this.description = description;
        this.id = TaskManager.getNewId();
        this.name = name;
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        return String.format("ID задачи - %d, Название - %s, Описание - %s, Статус - %s",id,name,description, status.toString());
    }

    public int getId() {
        return id;
    }
}
