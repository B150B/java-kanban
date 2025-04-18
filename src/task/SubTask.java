package task;

public class SubTask extends Task {

    private int partOfEpic;


    public SubTask(String name, String description, int partOfEpic, Status status) {
        super(name, description);
        this.partOfEpic = partOfEpic;
        setStatus(status);
    }


    public int getPartOfEpic() {     //Узнаем частью какого эпика является подзадача
        return partOfEpic;
    }


    @Override
    public String toString() {
        return String.format("%n-ID подзадачи - %d, Часть Эпика - %d Название - %s, Описание - %s, Статус - %s", getId(), partOfEpic, getName(), getDescription(), getStatus().toString());
    }
}
