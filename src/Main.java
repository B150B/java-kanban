public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");


        TaskManager taskManager = new TaskManager();

        taskManager.addTask("Задача 1", "Описание 1");
        taskManager.addTask("Задача 2", "Описание 2");
        taskManager.addTask("Задача 3", "Описание 3");

        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task.toString());
        }



        taskManager.addEpic("Задача Эпик", "Описание Эпик");
        taskManager.addSubTask(4,"ПодЗадача 1", "ПодОписание 1");
        taskManager.addSubTask(4,"ПодЗадача 2", "ПодОписание 2");

        for (Epic epic : taskManager.getAllEpic()) {
            System.out.println(epic.toString());
        }
    }


    }





