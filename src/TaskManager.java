import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int id = 1;
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();


    static public int getNewId() {
        id++;
        return (id - 1);
    }


    public void addTask(String name, String description) {
        int tempId = getNewId();
        taskHashMap.put(tempId, new Task(tempId, name, description));
    }

    public void addEpic(String name, String description) {
        int tempId = getNewId();
        epicHashMap.put(tempId, new Epic(tempId,name,description));
    }

    public void addSubTask(int id, String name, String description) {
        if (epicHashMap.containsKey(id)) {
            SubTask subTask = new SubTask(getNewId(),name,description,id);
            epicHashMap.get(id).subTasks.add(subTask);
        }
    }




    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : taskHashMap.values()) {
            result.add(task);
        }
        return result;
    }


    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> result = new ArrayList<>();
        for (Epic epic : epicHashMap.values()) {
            result.add(epic);
        }
        return result;
    }


}
