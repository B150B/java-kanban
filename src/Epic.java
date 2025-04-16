import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<SubTask> subTasks;

    Epic(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
    }


    @Override
    public String toString() {
        return String.format("ID задачи - %d, Название - %s, Описание - %s, Статус - %s, подзадачи - %s",id,name,description, status.toString(), subTasks.toString());
    }
}
