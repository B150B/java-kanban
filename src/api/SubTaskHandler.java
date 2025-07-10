package api;


import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;

import task.SubTask;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubTaskHandler extends BaseCrudHandler<SubTask> {


    public SubTaskHandler(InMemoryTaskManager manager) {
        super(manager);
    }

    @Override
    protected void getHandler(HttpExchange exchange, String path) throws IOException {
        if (requestHasId(path)) {
            try {
                sendJson(exchange, 200, manager.getSubTask(getIdFromRequest(path)));
            } catch (Exception exception) {
                sendNotFound(exchange);
            }
        } else {
            sendJson(exchange, 200, manager.getAllSubTasks());
        }
    }

    @Override
    protected void postHandler(HttpExchange exchange, String path) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        if (requestHasId(path)) {
            manager.updateSubTask(subTask);
            sendText(exchange, "Подзадача успешно обновлена", 201);
        } else {
            if (manager.addSubTask(subTask)) {
                sendText(exchange, "Подзадача успешно добавлена", 201);
            } else {
                sendHasInteractions(exchange);
            }
        }
    }

    @Override
    protected void deleteHandler(HttpExchange exchange, String path) throws IOException {
        int id = getIdFromRequest(path);
        try {
            manager.deleteSubTask(id);
            sendText(exchange, "Задача успешно удалена", 200);
        } catch (Exception e) {
            sendNotFound(exchange);
        }
    }


}
