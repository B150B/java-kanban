import java.util.ArrayList;

import manager.TaskManager;
import task.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

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

        System.out.println("Список всех Task");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Список всех Epic");
        System.out.println(taskManager.getAllEpic());
        System.out.println("Список всех SubTask");
        System.out.println(taskManager.getAllSubTasks());

        SubTask subTask4 = new SubTask("Сабтаск2-2", "Описание сабтаска 2-2", 4, Status.DONE);
        taskManager.updateSubTask(subTask2, subTask4);
        System.out.println("Обновленный эпик4");
        System.out.println(taskManager.getEpic(4));

        SubTask subTask5 = new SubTask("Сабтаск3-2", "Описание сабтаска 3-2", 4, Status.DONE);
        taskManager.updateSubTask(subTask3, subTask5);
        System.out.println("Еще раз обновленный эпик4");
        System.out.println(taskManager.getEpic(4));


        taskManager.deleteTask(1);
        System.out.println("Удалили задачу 1, вывод всех задач после удаления");
        System.out.println(taskManager.getAllTasks());

        taskManager.deleteEpic(4);
        System.out.println("Удалили эпик с id4, вывод всех эпиков после удаления");
        System.out.println(taskManager.getAllEpic());
        System.out.println("Удалили эпик с id4, вывод всех сабтасков после удаления");
        System.out.println(taskManager.getAllSubTasks());


    }


}





