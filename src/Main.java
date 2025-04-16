public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");


        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        Task task3 = new Task("Задача 3", "Описание 3");
        Task task4 = new Task("Обновленная Задача 3", "Описание новой 3");

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        Epic epic1 = new Epic("Эпик1","Описания эпика1");
        Epic epic2 = new Epic("Эпик2","Описания эпика2");

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subTask1 = new SubTask("Подзадача эпика1-1","Описания подзадачи эпика 1-1", epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача эпика1-2","Описания подзадачи эпика 1-1", epic1.getId());
        SubTask subTask3 = new SubTask("Подзадача эпика2-1","Описания подзадачи эпика 2-1", epic2.getId());

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);








        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task.toString());
        }

        taskManager.updateTask(task3,task4);

        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task.toString());
        }


        for (Epic epic : taskManager.getAllEpic()) {
            System.out.println(epic.toString());
        }





    }


    }





