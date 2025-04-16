public class SubTask extends Task {

    int partOfEpic;

    public SubTask(String name, String description, int partOfEpic) {
        super(name, description);
        this.partOfEpic = partOfEpic;
    }


    public int getPartOfEpic() {
        return partOfEpic;
    }


    @Override
    public String toString() {
        return String.format("%n-ID подзадачи - %d, Часть Эпика - %d Название - %s, Описание - %s, Статус - %s",id, partOfEpic, name,description, status.toString());
    }
}
