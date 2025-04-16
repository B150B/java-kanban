import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    static int id = 1;
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();


    static public int getNewId() {
        id++;
        return (id - 1);
    }


    public void addTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }


    public void addSubTask(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
        epicHashMap.get(subTask.getPartOfEpic()).subTasks.add(subTask);
        epicHashMap.get(subTask.getPartOfEpic()).checkStatus();
    }


    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : taskHashMap.values()) {
            result.add(task);
        }
        return result;
    }


    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> result = new ArrayList<>();
        for (SubTask subtask : subTaskHashMap.values()) {
            result.add(subtask);
        }
        return result;
    }


    public ArrayList<SubTask> getSubTasksFromEpic(int id) {
        ArrayList<SubTask> result = new ArrayList<>();
        for (SubTask subTask : subTaskHashMap.values()) {
            if (subTask.partOfEpic == id) {
                result.add(subTask);
            }
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


    public void clearAllTasks() {
        taskHashMap.clear();
    }


    public void clearAllSubTasks() {
        subTaskHashMap.clear();
    }


    public void clearAllEpics() {
        epicHashMap.clear();
    }


    public Task getTask(int id) {
        return taskHashMap.get(id);
    }


    public SubTask getSubTask(int id) {
        return subTaskHashMap.get(id);
    }


    public Epic getEpic(int id) {
        return epicHashMap.get(id);
    }


    public void updateTask(Task oldTask, Task updatedTask) {
        taskHashMap.get(oldTask.getId()).name = updatedTask.name;
        taskHashMap.get(oldTask.getId()).description = updatedTask.description;
        taskHashMap.get(oldTask.getId()).status = updatedTask.status;
    }


    public void updateSubTask(SubTask oldSubTask, Task updatedSubTask) {
        subTaskHashMap.get(oldSubTask.getId()).name = updatedSubTask.name;
        subTaskHashMap.get(oldSubTask.getId()).description = updatedSubTask.description;
        subTaskHashMap.get(oldSubTask.getId()).status = updatedSubTask.status;
        epicHashMap.get(oldSubTask.getPartOfEpic()).checkStatus();
    }


    public void deleteTask(int id) {
        taskHashMap.remove(id);
    }


    public void deleteSubTask(int id) {
        subTaskHashMap.remove(id);
    }


    public void deleteEpic(int id) {
        epicHashMap.remove(id);
    }


}
