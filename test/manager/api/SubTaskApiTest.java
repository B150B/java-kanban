package manager.api;


import com.google.gson.reflect.TypeToken;
import manager.HttpTaskServerTest;
import org.junit.jupiter.api.*;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskListTypeToken extends TypeToken<List<SubTask>> {

}

@DisplayName("Тест SubTaskApi")
public class SubTaskApiTest extends HttpTaskServerTest {


    @Nested
    @DisplayName("GET-запросы")
    class GetTests {
        @Test
        public void testGetSubTask() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/subtasks/4", "GET");
            SubTask result = gson.fromJson(response.body(), SubTask.class);
            assertEquals(manager.getSubTask(4), result);
            assertEquals(200, response.statusCode());
        }


        @Test
        public void testGetAllSubTasks() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/subtasks", "GET");
            List<SubTask> result = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());
            List<SubTask> expected = manager.getAllSubTasks();
            assertArrayEquals(result.toArray(), expected.toArray());
            assertEquals(200, response.statusCode());
        }


        @Test
        public void testGetNonExistentSubTask() throws IOException, InterruptedException {
            HttpResponse<String> response = sendRequest("http://localhost:8080/subtasks/222", "GET");
            assertEquals(404, response.statusCode());
        }
    }


    @Nested
    @DisplayName("Post-запросы")
    class PostTest {
        @Test
        public void testPostCreateSubTask() throws IOException, InterruptedException {
            SubTask subTaskWithId6 = new SubTask("СубТаск с ид 6", "Описание Субтаска с ид 6", Status.NEW, 1);
            String subTaskWithId6Json = gson.toJson(subTaskWithId6);
            HttpResponse<String> response = sendRequest("http://localhost:8080/subtasks", "POST", subTaskWithId6Json);
            assertEquals(201, response.statusCode());
            List<SubTask> subTasksFromManager = manager.getAllSubTasks();
            assertEquals(3, subTasksFromManager.size());
            assertEquals(subTaskWithId6.getName(), subTasksFromManager.get(2).getName());
        }


        @Test
        public void testPostCreateSubTaskWithOverlap() throws IOException, InterruptedException {
            Task subTaskWithId6 = new SubTask("Таск с ид 6", "Описание таска с ид 6", Status.NEW, 1
                    , Duration.ofMinutes(100), LocalDateTime.now().minus(Duration.ofMinutes(50)));
            String subTaskWithId6Json = gson.toJson(subTaskWithId6);
            HttpResponse<String> response = sendRequest("http://localhost:8080/subtasks", "POST", subTaskWithId6Json);
            assertEquals(406, response.statusCode());
            List<SubTask> subTasksFromManager = manager.getAllSubTasks();
            assertEquals(2, subTasksFromManager.size());
        }


        @Test
        public void testPostUpdateSubTask() throws IOException, InterruptedException {
            SubTask updatedSubTask1 = new SubTask(4, "Обновленный сабтаск1", "Описание обновленного сабтаск1", Status.NEW, 1);
            String updatedSubTask1Json = gson.toJson(updatedSubTask1);
            HttpResponse<String> response = sendRequest("http://localhost:8080/subtasks/4", "POST", updatedSubTask1Json);
            assertEquals(201, response.statusCode());
            SubTask result = manager.getSubTask(4);
            assertEquals(updatedSubTask1.getName(), result.getName());
        }
    }


    @Test
    @DisplayName("Delete-запрос")
    public void testDeleteSubTask() throws IOException, InterruptedException {
        HttpResponse<String> response = sendRequest("http://localhost:8080/subtasks/4", "DELETE");
        assertEquals(200, response.statusCode());
        List<SubTask> result = manager.getAllSubTasks();
        List<SubTask> expected = List.of(subTask2);
        assertArrayEquals(result.toArray(), expected.toArray());
    }


}
