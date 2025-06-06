package manager;


import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {

    protected int id = 1;
    protected Map<Integer, Task> taskHashMap = new HashMap<>();
    protected Map<Integer, Epic> epicHashMap = new HashMap<>();
    protected Map<Integer, SubTask> subTaskHashMap = new HashMap<>();
    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();


    private int getNewId() {
        return id++;
    }


    @Override
    public void addTask(Task task) {
        int newId = getNewId();
        task.setId(newId);
        taskHashMap.put(newId, task);
    }


    @Override
    public void addEpic(Epic epic) {
        int newId = getNewId();
        epic.setId(newId);
        epicHashMap.put(newId, epic);
    }


    @Override
    public void addSubTask(SubTask subTask) {
        int newId = getNewId();
        subTask.setId(newId);
        subTaskHashMap.put(newId, subTask);
        Epic parentalEpic = epicHashMap.get(subTask.getPartOfEpic());
        parentalEpic.addSubtaskToEpic(subTask);
        setEpicStatus(parentalEpic);
    }


    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskHashMap.values());
    }


    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTaskHashMap.values());
    }


    @Override
    public ArrayList<SubTask> getSubTasksFromEpic(int id) {
        Epic epic = epicHashMap.get(id);
        ArrayList<SubTask> result = new ArrayList<>();
        for (Integer subTaskId : epic.getSubtasksIds()) {
            result.add(subTaskHashMap.get(subTaskId));
        }
        return result;
    }


    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicHashMap.values());
    }


    @Override
    public void clearAllTasks() {
        for (int id : taskHashMap.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
        taskHashMap.clear();
    }


    @Override
    public void clearAllSubTasks() {
        for (int id : subTaskHashMap.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
        subTaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.clearAllSubTasksFromEpic();
            setEpicStatus(epic);
        }
    }


    @Override
    public void clearAllEpics() {
        for (int id : epicHashMap.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
        for (int id : subTaskHashMap.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
        epicHashMap.clear();
        subTaskHashMap.clear();
    }


    @Override
    public Task getTask(int id) {
        Task task = taskHashMap.get(id);
        inMemoryHistoryManager.add(task);
        return task;
    }


    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        inMemoryHistoryManager.add(subTask);
        return subTask;
    }


    @Override
    public Epic getEpic(int id) {
        Epic epic = epicHashMap.get(id);
        inMemoryHistoryManager.add(epic);
        return epic;
    }


    @Override
    public void updateTask(Task updatedTask) {
        taskHashMap.put(updatedTask.getId(), updatedTask);
    }


    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        subTaskHashMap.put(updatedSubTask.getId(), updatedSubTask);
        setEpicStatus(epicHashMap.get(updatedSubTask.getPartOfEpic()));
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        epicHashMap.put(updatedEpic.getId(), updatedEpic);
    }


    @Override
    public void deleteTask(int id) {
        inMemoryHistoryManager.remove(id);
        taskHashMap.remove(id);
    }


    @Override
    public void deleteSubTask(int id) {
        inMemoryHistoryManager.remove(id);
        Epic parentalEpic = epicHashMap.get(subTaskHashMap.get(id).getPartOfEpic());
        subTaskHashMap.remove(id);
        parentalEpic.deleteSubtaskFromEpic(id);
        setEpicStatus(parentalEpic);
    }


    @Override
    public void deleteEpic(int id) {
        inMemoryHistoryManager.remove(id);
        for (int subTasksId : epicHashMap.get(id).getSubtasksIds()) {
            subTaskHashMap.remove(subTasksId);
            inMemoryHistoryManager.remove(subTasksId);
        }
        epicHashMap.remove(id);
    }


    private void setEpicStatus(Epic epic) { //Проверяем статус эпика по подзадачам внутри него
        boolean isNew = true;
        boolean isDone = true;
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (int subTaskId : epic.getSubtasksIds()) {
            subTasks.add(subTaskHashMap.get(subTaskId));
        }
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                isNew = false;
                isDone = false;
            } else if (subTask.getStatus().equals(Status.NEW)) {
                isDone = false;
            } else {
                isNew = false;
            }
        }
        if (isDone) {
            epic.setStatus(Status.DONE);
        } else if (isNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) inMemoryHistoryManager.getHistory();
    }


}
