package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
    void addNewSubTask() {
        SubTask subTask2 = new SubTask("СабТаск2", "Описание СабТаск2", Status.NEW, 2);
        taskManager.addSubTask(subTask2);
        assertEquals(subTask2, taskManager.getSubTask(4));
    }

    @Test
    void addNewEpic() {
        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        taskManager.addEpic(epic2);
        assertEquals(epic2, taskManager.getEpic(4));
    }

    @Test
    void updateTask() {
        Task task2 = new Task("Таск2", "Таск2");
        taskManager.addTask(task2);
        task2.setName("ОбновленныйТаск2");
        Task expected = new Task(4, "ОбновленныйТаск2", "Таск2", Status.NEW);
        taskManager.updateTask(task2);
        assertEquals(expected, taskManager.getTask(4));
    }

    @Test
    void updateSubTask() {
        SubTask subTask2 = new SubTask("СабТаск2", "Описание СабТаск2", Status.NEW, 2);
        taskManager.addSubTask(subTask2);
        subTask2.setName("ОбновленныйСабТаск2");
        SubTask expected = new SubTask(4, "ОбновленныйТаск2", "Таск2", Status.NEW, 2);
        taskManager.updateSubTask(subTask2);
        assertEquals(expected, taskManager.getSubTask(4));
    }

    @Test
    void updateEpic() {
        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        taskManager.addEpic(epic2);
        epic2.setName("ОбновленныйТаск2");
        Epic expected = new Epic(4, "ОбновленныйТаск2", "Таск2", Status.NEW, List.of(3));
        taskManager.updateEpic(epic2);
        assertEquals(expected, taskManager.getEpic(4));
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

    @Test
    void receivingAllTasks() {
        Task task2 = new Task("Таск2", "Описание Таск2");
        taskManager.addTask(task2);
        List<Task> expected = List.of(task1, task2);
        List<Task> result = taskManager.getAllTasks();
        assertArrayEquals(expected.toArray(), result.toArray());
    }


    @Test
    void receivingAllSubTasks() {
        SubTask subTask2 = new SubTask("СабТаск2", "Описание СабТаск2", Status.NEW, 2);
        taskManager.addSubTask(subTask2);
        List<SubTask> expected = List.of(subtask1, subTask2);
        List<SubTask> result = taskManager.getAllSubTasks();
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    void receivingAllEpic() {
        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        taskManager.addEpic(epic2);
        List<Epic> expected = List.of(epic, epic2);
        List<Epic> result = taskManager.getAllEpic();
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    void deletingTask() {
        List<Task> expected = taskManager.getAllTasks();
        Task task2 = new Task("Таск2", "Описание Таск2");
        taskManager.addTask(task2);
        taskManager.deleteTask(4);
        List<Task> result = taskManager.getAllTasks();
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    void deletingSubTask() {
        List<SubTask> expected = taskManager.getAllSubTasks();
        SubTask subTask2 = new SubTask("СабТаск2", "Описание СабТаск2", Status.NEW, 2);
        taskManager.addSubTask(subTask2);
        taskManager.deleteSubTask(4);
        List<SubTask> result = taskManager.getAllSubTasks();
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    void deletingEpic() {
        List<Epic> expected = taskManager.getAllEpic();
        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        taskManager.addEpic(epic2);
        taskManager.deleteEpic(4);
        List<Epic> result = taskManager.getAllEpic();
        assertArrayEquals(expected.toArray(), result.toArray());
    }


}
