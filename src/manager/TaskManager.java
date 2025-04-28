package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;

public interface TaskManager {
    int getNewId();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<SubTask> getSubTasksFromEpic(int id);

    ArrayList<Epic> getAllEpic();

    void clearAllTasks();

    void clearAllSubTasks();

    void clearAllEpics();

    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    void updateTask(Task updatedTask);

    void updateSubTask(SubTask updatedSubTask);

    void updateEpic(Epic updatedEpic);

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteEpic(int id);

    void setEpicStatus(Epic epic);

}
