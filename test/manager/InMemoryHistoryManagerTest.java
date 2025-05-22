package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static InMemoryHistoryManager inMemoryHistoryManager;
    static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    void getHistoryAdding5Tasks() {
        Task task1 = new Task("Таск1","Описание1");
        Task task2 = new Task("Таск2","Описание2");
        Task task3 = new Task("Таск3","Описание3");
        Task task4 = new Task("Таск4","Описание4");
        Task task5 = new Task("Таск5","Описание5");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.addTask(task4);
        inMemoryTaskManager.addTask(task5);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        ArrayList<Task> result = inMemoryHistoryManager.getHistory();
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);
        expected.add(task3);
        expected.add(task4);
        expected.add(task5);
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    void adding3EqualTasksGetHistoryMustReturn1Record() {
        Task task1 = new Task("Таск1","Описание1");
        inMemoryTaskManager.addTask(task1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task1);
        int expected = 1;
        int result = inMemoryHistoryManager.getHistory().size();
        assertEquals(expected,result);
    }

    @Test
    void adding5TasksDeleteFirstTask () {
        Task task1 = new Task("Таск1","Описание1");
        Task task2 = new Task("Таск2","Описание2");
        Task task3 = new Task("Таск3","Описание3");
        Task task4 = new Task("Таск4","Описание4");
        Task task5 = new Task("Таск5","Описание5");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.addTask(task4);
        inMemoryTaskManager.addTask(task5);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.remove(1);
        ArrayList<Task> result = inMemoryHistoryManager.getHistory();
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(task2);
        expected.add(task3);
        expected.add(task4);
        expected.add(task5);
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    void adding5TasksDeleteMiddleTask () {
        Task task1 = new Task("Таск1","Описание1");
        Task task2 = new Task("Таск2","Описание2");
        Task task3 = new Task("Таск3","Описание3");
        Task task4 = new Task("Таск4","Описание4");
        Task task5 = new Task("Таск5","Описание5");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.addTask(task4);
        inMemoryTaskManager.addTask(task5);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.remove(3);
        ArrayList<Task> result = inMemoryHistoryManager.getHistory();
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);
        expected.add(task4);
        expected.add(task5);
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    void adding5TasksDeleteLastTask () {
        Task task1 = new Task("Таск1","Описание1");
        Task task2 = new Task("Таск2","Описание2");
        Task task3 = new Task("Таск3","Описание3");
        Task task4 = new Task("Таск4","Описание4");
        Task task5 = new Task("Таск5","Описание5");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.addTask(task4);
        inMemoryTaskManager.addTask(task5);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.remove(5);
        ArrayList<Task> result = inMemoryHistoryManager.getHistory();
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);
        expected.add(task3);
        expected.add(task4);
        assertArrayEquals(expected.toArray(), result.toArray());
    }

}