package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task1;
    protected Epic epic;
    protected SubTask subtask1;

    protected abstract T createTaskManager();

    @BeforeEach
    void beforeEach() {
        taskManager = createTaskManager();
        task1 = new Task("Таск1", "ОписаниеТаск1");
        taskManager.addTask(task1);
        epic = new Epic("Эпик1", "ОписаниеЭпик1");
        taskManager.addEpic(epic);
        subtask1 = new SubTask("Сабтаск1", "ОписаниеСабтаск1", Status.NEW, 2, Duration.ofMinutes(50), LocalDateTime.now());
        taskManager.addSubTask(subtask1);
    }

    @Test
    void addNewTask() {
        Task task2 = new Task("Таск2", "Таск2");
        taskManager.addTask(task2);
        assertEquals(task2, taskManager.getTask(4));
    }

    @Test
    void idMustBeDiffrent() {
        assertFalse(task1.getId() == epic.getId() || task1.getId() == subtask1.getId() || epic.getId() == subtask1.getId());
    }

    @Test
    void taskMustNotChangeAfterBeingAddedInTaskManager() {
        Task task2 = new Task("Таск2", "Описание2");
        String task2NameBeforeTaskManager = task2.getName();
        String task2DescrBeforeTaskManager = task2.getDescription();
        taskManager.addTask(task2);
        String task2NameAfterTaskManager = taskManager.getTask(4).getName();
        String task2DescrAfterTaskManager = taskManager.getTask(4).getDescription();
        boolean result = task2NameBeforeTaskManager.equals(task2NameAfterTaskManager) && task2DescrBeforeTaskManager.equals(task2DescrAfterTaskManager);
        assertTrue(result);
    }

    @Test
    void subTaskMustHaveParentalEpicId() {
        assertEquals(epic.getId(), subtask1.getPartOfEpic());
    }

    @Test
    void ifTasksTimeOverlapNewTaskMustNotBeAdded() {
        Task mustBeAddedTask = new Task("Сабтаск1", "ОписаниеСабтаск1", Status.NEW,
                Duration.ofMinutes(50), LocalDateTime.now());
        Task mustNotBeAddedTask = new Task("Сабтаск2", "ОписаниеСабтаск2", Status.NEW,
                Duration.ofMinutes(50), LocalDateTime.now().plus(Duration.ofMinutes(20)));
        T overLapTaskManager = createTaskManager();
        overLapTaskManager.addTask(mustBeAddedTask);
        overLapTaskManager.addTask(mustNotBeAddedTask);
        assertThrows(NullPointerException.class, () -> overLapTaskManager.getTask(2));
    }


}
