package manager.api;


import com.google.gson.reflect.TypeToken;
import manager.HttpTaskServerTest;
import org.junit.jupiter.api.*;
import task.Status;
import task.Task;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskListTypeToken extends TypeToken<List<Task>> {

}

@DisplayName("Тест TaskApi")
public class TaskApiTest extends HttpTaskServerTest {

    @Nested
    @DisplayName("GET-запросы")
    class GetTests {
        @Test
        public void testGetTask() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/tasks/2", "GET");
            Task result = gson.fromJson(response.body(), Task.class);
            assertEquals(manager.getTask(2), result);
            assertEquals(200, response.statusCode());
        }

        @Test
        public void testGetAllTasks() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/tasks", "GET");
            List<Task> result = gson.fromJson(response.body(), new TaskListTypeToken().getType());
            List<Task> expected = manager.getAllTasks();
            assertArrayEquals(result.toArray(), expected.toArray());
            assertEquals(200, response.statusCode());
        }

        @Test
        public void testGetNonExistentTask() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/tasks/222", "GET");
            assertEquals(404, response.statusCode());
        }
    }

    @Nested
    @DisplayName("Post-запросы")
    class PostTest {
        @Test
        public void testPostCreateTask() throws IOException, InterruptedException {
            Task taskWithId6 = new Task("Таск с ид 6", "Описание таска с ид 6");
            String taskWithId6Json = gson.toJson(taskWithId6);
            HttpResponse<String> response = sendRequest("http://localhost:8080/tasks", "POST", taskWithId6Json);
            assertEquals(201, response.statusCode());
            List<Task> tasksFromManager = manager.getAllTasks();
            assertEquals(3, tasksFromManager.size());
            assertEquals(taskWithId6.getName(), tasksFromManager.get(2).getName());
        }


        @Test
        public void testPostCreateTaskWithOverlap() throws IOException, InterruptedException {
            Task taskWithId6 = new Task("Таск с ид 6", "Описание таска с ид 6", Status.NEW
                    , Duration.ofMinutes(100), LocalDateTime.now().minus(Duration.ofMinutes(50)));
            String taskWithId6Json = gson.toJson(taskWithId6);
            HttpResponse<String> response = sendRequest("http://localhost:8080/tasks", "POST", taskWithId6Json);
            assertEquals(406, response.statusCode());
            List<Task> tasksFromManager = manager.getAllTasks();
            assertEquals(2, tasksFromManager.size());
        }


        @Test
        public void testPostUpdateTask() throws IOException, InterruptedException {
            Task updatedTask1 = new Task(1, "Обновленный таск1", "Описание обновленного таск1", Status.NEW);
            String updatedTask1Json = gson.toJson(updatedTask1);
            HttpResponse<String> response = sendRequest("http://localhost:8080/tasks/1", "POST", updatedTask1Json);
            assertEquals(201, response.statusCode());
            Task result = manager.getTask(1);
            assertEquals(updatedTask1.getName(), result.getName());
        }
    }


    @Test
    @DisplayName("Delete-запрос")
    public void testDeleteTask() throws IOException, InterruptedException {
        HttpResponse<String> response = sendRequest("http://localhost:8080/tasks/2", "DELETE");
        assertEquals(200, response.statusCode());
        List<Task> result = manager.getAllTasks();
        List<Task> expected = List.of(task2);
        assertArrayEquals(result.toArray(), expected.toArray());
    }


}
