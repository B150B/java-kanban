package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
     InMemoryTaskManager inMemoryTaskManager;
     Task task1;
     Task task2;
     Epic epic;
     SubTask subtask1;
     SubTask subTask2;



    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task1 = new Task("Таск1","ОписаниеТаск1");
        inMemoryTaskManager.addTask(task1);
        epic = new Epic("Эпик1","ОписаниеЭпик1");
        inMemoryTaskManager.addEpic(epic);
        subtask1 = new SubTask("Сабтаск1","ОписаниеСабтаск1",2, Status.NEW);
        inMemoryTaskManager.addSubTask(subtask1);
    }

    @Test
    void addNewTask () {
        Task task2 = new Task("Таск2","Таск2");
        inMemoryTaskManager.addTask(task2);
        assertEquals(task2,inMemoryTaskManager.getTask(4));
    }

    @Test
    void idMustBeDiffrent() {
        assertFalse (task1.getId() == epic.getId() || task1.getId() == subtask1.getId() || epic.getId() == subtask1.getId());
    }

    @Test
    void taskMustNotChangeAfterBeingAddedInTaskManager () {
        Task task2 = new Task("Таск2","Описание2");
        String task2NameBeforeTaskManager = task2.getName();
        String task2DescrBeforeTaskManager = task2.getDescription();
        inMemoryTaskManager.addTask(task2);
        String task2NameAfterTaskManager = inMemoryTaskManager.getTask(4).getName();
        String task2DescrAfterTaskManager = inMemoryTaskManager.getTask(4).getDescription();
        boolean result = task2NameBeforeTaskManager.equals(task2NameAfterTaskManager) && task2DescrBeforeTaskManager.equals(task2DescrAfterTaskManager);
        assertTrue(result);
    }
}