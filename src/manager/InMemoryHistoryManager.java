package manager;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> history;


    @Override
    public void add(Task task) {
        if (history == null) {
            history = new ArrayList<>();
        }
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.remove(9);
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }


}
