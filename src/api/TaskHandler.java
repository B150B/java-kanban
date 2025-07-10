package api;

import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;
import task.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseCrudHandler<Task> {

    public TaskHandler(InMemoryTaskManager manager) {
        super(manager);
    }


    @Override
    protected void getHandler(HttpExchange exchange, String path) throws IOException {
        if (requestHasId(path)) {
            try {
                sendJson(exchange, 200, manager.getTask(getIdFromRequest(path)));
            } catch (Exception exception) {
                sendNotFound(exchange);
            }
        } else {
            sendJson(exchange, 200, manager.getAllTasks());
        }
    }

    @Override
    protected void postHandler(HttpExchange exchange, String path) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body, Task.class);
        if (requestHasId(path)) {
            manager.updateTask(task);
            sendText(exchange, "Задача успешно обновлена", 201);
        } else {
            if (manager.addTask(task)) {
                sendText(exchange, "Задача успешно добавлена", 201);
            } else {
                sendHasInteractions(exchange);
            }
        }
    }

    @Override
    protected void deleteHandler(HttpExchange exchange, String path) throws IOException {
        int id = getIdFromRequest(path);
        try {
            manager.deleteTask(id);
            sendText(exchange, "Задача успешно удалена", 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }


}
