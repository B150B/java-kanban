public class SubTask extends Task {

    int partOfEpic;

    public SubTask(int id, String name, String description, int partOfEpic) {
        super(id, name, description);
        this.partOfEpic = partOfEpic;

    }


    @Override
    public String toString() {
        return String.format("ID задачи - %d, Часть Эпика - %d Название - %s, Описание - %s, Статус - %s",id, partOfEpic, name,description, taskStatus.toString());
    }
}
