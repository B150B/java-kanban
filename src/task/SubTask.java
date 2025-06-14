package task;

public class SubTask extends Task {

    private int partOfEpic;


    public SubTask(String name, String description, int partOfEpic, Status status) {
        super(name, description);
        this.partOfEpic = partOfEpic;
        setStatus(status);
    }

    public SubTask(int id, String name, String description, Status status, int partOfEpic) {
        super(id, name, description, status);
        this.partOfEpic = partOfEpic;
    }


    public int getPartOfEpic() {     //Узнаем частью какого эпика является подзадача
        return partOfEpic;
    }


    @Override
    public String toString() {
        return String.format("%n-ID подзадачи - %d, Часть Эпика - %d Название - %s, Описание - %s, Статус - %s", getId(), partOfEpic, getName(), getDescription(), getStatus().toString());
    }


    public String toCSVLine() {
        return String.format("%d,%s,%s,%s,%s,%d", getId(), TaskType.SUBTASK.toString(), getName(), getStatus().toString(), getDescription(), getPartOfEpic());
    }

    public static SubTask fromCSVLine(String stringData) {
        String[] dataArray = stringData.split(",");
        int id = Integer.parseInt(dataArray[0]);
        String name = dataArray[2];
        Status status = Status.valueOf(dataArray[3]);
        String description = dataArray[4];
        int partOfEpic = Integer.parseInt(dataArray[5]);
        return new SubTask(id, name, description, status, partOfEpic);
    }


}
