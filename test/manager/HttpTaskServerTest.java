package manager;

import API.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskListTypeToken extends TypeToken<List<Task>> {

}

class SubTaskListTypeToken extends TypeToken<List<SubTask>> {

}

class EpicListTypeToken extends TypeToken<List<Epic>> {

}


class HttpTaskServerTest {
    private InMemoryTaskManager manager = new InMemoryTaskManager();
    private HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    Gson gson = httpTaskServer.getGson();
    Epic epic;
    Task task1;
    Task task2;
    SubTask subTask1;
    SubTask subTask2;

    public HttpTaskServerTest() throws IOException {

    }


    @BeforeEach
    public void beforeEach() throws IOException {
        httpTaskServer.startServer();
        epic = new Epic("Эпик", "Описание эпика");
        task1 = new Task("Таск1", "Описание таск1");
        task2 = new Task("Таск2", "Описание таск2", Status.NEW, Duration.ofMinutes(50), LocalDateTime.now());
        subTask1 = new SubTask("Сабтаск1", "Описание сабтаск1", Status.NEW, 1);
        subTask2 = new SubTask("Сабтаск2", "Описание сабтаск2", Status.IN_PROGRESS, 1);
        manager.addEpic(epic); //id=1
        manager.addTask(task1); // id=2
        manager.addTask(task2); //id=3
        manager.addSubTask(subTask1); //id=4
        manager.addSubTask(subTask2); //id=5
    }

    @AfterEach
    public void afterEach() {
        manager.clearAllTasks();
        manager.clearAllSubTasks();
        manager.clearAllEpics();
        httpTaskServer.stopServer();
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        Task result = gson.fromJson(response.body(), Task.class);
        assertEquals(manager.getTask(2), result);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        List<Task> result = gson.fromJson(response.body(), new TaskListTypeToken().getType());
        List<Task> expected = manager.getAllTasks();
        assertArrayEquals(result.toArray(), expected.toArray());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testGetNonExistentTask() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/222");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testPostCreateTask() throws IOException, InterruptedException {
        Task taskWithId6 = new Task("Таск с ид 6", "Описание таска с ид 6");
        String taskWithId6Json = gson.toJson(taskWithId6);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskWithId6Json))
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
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
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskWithId6Json))
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(406, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(2, tasksFromManager.size());
    }


    @Test
    public void testPostUpdateTask() throws IOException, InterruptedException {
        Task updatedTask1 = new Task(1, "Обновленный таск1", "Описание обновленного таск1", Status.NEW);
        String updatedTask1Json = gson.toJson(updatedTask1);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTask1Json))
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(201, response.statusCode());
        Task result = manager.getTask(1);
        assertEquals(updatedTask1.getName(), result.getName());
    }


    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(200, response.statusCode());
        List<Task> result = manager.getAllTasks();
        List<Task> expected = List.of(task2);
        assertArrayEquals(result.toArray(), expected.toArray());
    }


    @Test
    public void testGetSubTask() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        SubTask result = gson.fromJson(response.body(), SubTask.class);
        assertEquals(manager.getSubTask(4), result);
        assertEquals(200, response.statusCode());
    }


    @Test
    public void testGetAllSubTasks() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        List<SubTask> result = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());
        List<SubTask> expected = manager.getAllSubTasks();
        assertArrayEquals(result.toArray(), expected.toArray());
        assertEquals(200, response.statusCode());
    }


    @Test
    public void testGetNonExistentSubTask() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/222");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(404, response.statusCode());
    }


    @Test
    public void testPostCreateSubTask() throws IOException, InterruptedException {
        SubTask subTaskWithId6 = new SubTask("СубТаск с ид 6", "Описание Субтаска с ид 6", Status.NEW, 1);
        String subTaskWithId6Json = gson.toJson(subTaskWithId6);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskWithId6Json))
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
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
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskWithId6Json))
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(406, response.statusCode());
        List<SubTask> subTasksFromManager = manager.getAllSubTasks();
        assertEquals(2, subTasksFromManager.size());
    }


    @Test
    public void testPostUpdateSubTask() throws IOException, InterruptedException {
        SubTask updatedSubTask1 = new SubTask(4, "Обновленный сабтаск1", "Описание обновленного сабтаск1", Status.NEW, 1);
        String updatedSubTask1Json = gson.toJson(updatedSubTask1);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedSubTask1Json))
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(201, response.statusCode());
        SubTask result = manager.getSubTask(4);
        assertEquals(updatedSubTask1.getName(), result.getName());
    }


    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/4");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(200, response.statusCode());
        List<SubTask> result = manager.getAllSubTasks();
        List<SubTask> expected = List.of(subTask2);
        assertArrayEquals(result.toArray(), expected.toArray());
    }


    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        Epic result = gson.fromJson(response.body(), Epic.class);
        assertEquals(manager.getEpic(1), result);
        assertEquals(200, response.statusCode());
    }


    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        manager.addEpic(epic2);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        List<Epic> result = gson.fromJson(response.body(), new EpicListTypeToken().getType());
        List<Epic> expected = manager.getAllEpic();
        assertArrayEquals(result.toArray(), expected.toArray());
        assertEquals(200, response.statusCode());
    }


    @Test
    public void testGetNonExistentEpic() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/222");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(404, response.statusCode());
    }


    @Test
    public void testGetSubtaskIdsFromEpic() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        List<SubTask> result = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());
        List<SubTask> expected = List.of(subTask1, subTask2);
        assertArrayEquals(result.toArray(), expected.toArray());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testGetSubtaskIdsFromNonExistentEpic() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/222/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(404, response.statusCode());
    }


    @Test
    public void testPostCreateEpic() throws IOException, InterruptedException {
        Epic epic2 = new Epic("Эпик2", "Описание Эпика2");
        manager.addEpic(epic2);
        SubTask subTask3 = new SubTask("СабТаск3", "Описание сабтаска3", Status.NEW, 6);
        manager.addSubTask(subTask3);
        String epic2Json = gson.toJson(epic2);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epic2Json))
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(201, response.statusCode());
        List<Epic> epicsFromManager = manager.getAllEpic();
        assertEquals(3, epicsFromManager.size());
        assertEquals(epic2.getName(), epicsFromManager.get(1).getName());
    }


    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(200, response.statusCode());
        List<Epic> result = manager.getAllEpic();
        assertEquals(result.size(), 0);
    }


    @Test
    public void testHistory() throws IOException, InterruptedException {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url1 = URI.create("http://localhost:8080/tasks/2");
        URI url2 = URI.create("http://localhost:8080/tasks/3");
        URI url3 = URI.create("http://localhost:8080/history");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url3)
                .GET()
                .build();

        HttpResponse<String> response1 = httpClient.send(request1, handler);
        HttpResponse<String> response2 = httpClient.send(request2, handler);
        HttpResponse<String> response3 = httpClient.send(request3, handler);
        List<Task> resultHistory = gson.fromJson(response3.body(), new TaskListTypeToken().getType());
        List<Task> expected = List.of(task1, task2);
        assertEquals(200, response1.statusCode());
        assertArrayEquals(resultHistory.toArray(), expected.toArray());
    }


    @Test
    public void testPrioritized() throws IOException, InterruptedException {
        Task task6 = new Task("Таск с ид 6", "Описание таска с ид 6", Status.NEW
                , Duration.ofMinutes(100), LocalDateTime.now().plus(Duration.ofMinutes(150)));
        manager.addTask(task6);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, handler);
        List<Task> result = gson.fromJson(response.body(), new TaskListTypeToken().getType());
        List<Task> expected = List.of(task2, task6);
        assertEquals(200, response.statusCode());
        assertArrayEquals(result.toArray(), expected.toArray());
    }

}
