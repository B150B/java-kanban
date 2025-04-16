import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<SubTask> subTasks;

    Epic(int id, String name, String description) {
        super(id, name, description);
        subTasks = new ArrayList<>();
    }


    @Override
    public String toString() {
        return String.format("ID задачи - %d, Название - %s, Описание - %s, Статус - %s, подзадачи - %s",id,name,description, taskStatus.toString(), subTasks.toString());
    }
}
