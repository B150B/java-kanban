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

    public void checkStatus () { //Проверяем статус эпика по подзадачам внутри него
        boolean isNew = true;
        boolean isDone = true;
        for (SubTask subTask : subTasks) {
            if (subTask.status.equals(Status.IN_PROGRESS)) {
                isNew = false;
                isDone = false;
            } else if (subTask.status.equals(Status.NEW)) {
                isDone = false;
            }
        }
        if (isDone) {
            status = Status.DONE;
        } else if (isNew) {
            status = Status.NEW;
        } else {
            status = Status.IN_PROGRESS;
        }
    }
}
