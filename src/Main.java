import java.util.ArrayList;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import task.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        Epic epic1 = new Epic("Эпик1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик2", "Описание эпика 2");

        SubTask subTask1 = new SubTask("Сабтаск 1", "Описание сабтаска 1", 3, Status.NEW);
        SubTask subTask2 = new SubTask("Сабтаск 2", "Описание сабтаска 2", 4, Status.NEW);
        SubTask subTask3 = new SubTask("Сабтаск 3", "Описание сабтаска 3", 4, Status.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(subTask1);
        historyManager.add(subTask2);
        historyManager.add(subTask3);

        System.out.println(historyManager.getHistory());




    }


}





