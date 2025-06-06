package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File file;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    private void save() {

        try {
            if (!Files.exists(file.toPath())) {
                Files.createFile(file.toPath());
            }

            ArrayList<String> taskCSVLinesArray = new ArrayList<>();
            for (Task task : taskHashMap.values()) {
                taskCSVLinesArray.add(task.toCSVLine());
            }
            for (Epic epic : epicHashMap.values()) {
                taskCSVLinesArray.add(epic.toCSVLine());
            }
            for (SubTask subTask : subTaskHashMap.values()) {
                taskCSVLinesArray.add(subTask.toCSVLine());
            }
            Files.write(file.toPath(), taskCSVLinesArray);
        } catch (Exception e) {
            System.out.println("Произошла ошибка записи в файл! " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager restoredManager = new FileBackedTaskManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String[] taskCSVArray = br.readLine().split("\\R");
                for (String taskCSV : taskCSVArray) {
                    String[] taskData = taskCSV.split(",");
                    String taskType = taskData[1];
                    switch (taskType) {
                        case "TASK":
                            Task task = Task.fromCSVLine(taskCSV);
                            restoredManager.putTaskWithID(task);
                            break;
                        case "SUBTASK":
                            SubTask subTask = SubTask.fromCSVLine(taskCSV);
                            restoredManager.putSubtaskWithID(subTask);
                            break;
                        case "EPIC":
                            Epic epic = Epic.fromCSVLine(taskCSV);
                            restoredManager.putEpicWithID(epic);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка загрузки файла! " + e.getMessage());
        }

        return restoredManager;
    }


    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    public void putTaskWithID(Task task) {
        taskHashMap.put(task.getId(), task);
        id = Math.max(task.getId() + 1, id);
    }

    public void putEpicWithID(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
        id = Math.max(epic.getId() + 1, id);
    }

    public void putSubtaskWithID(SubTask subTask) {
        subTaskHashMap.put(subTask.getId(), subTask);
        id = Math.max(subTask.getId() + 1, id);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubTasks() {
        super.clearAllSubTasks();
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return super.getAllEpic();
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return super.getAllSubTasks();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public Epic getEpic(int id) {
        return super.getEpic(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public SubTask getSubTask(int id) {
        return super.getSubTask(id);
    }

    @Override
    public ArrayList<SubTask> getSubTasksFromEpic(int id) {
        return super.getSubTasksFromEpic(id);
    }

    @Override
    public Task getTask(int id) {
        return super.getTask(id);
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        super.updateEpic(updatedEpic);
        save();
    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        super.updateSubTask(updatedSubTask);
        save();
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        save();
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
