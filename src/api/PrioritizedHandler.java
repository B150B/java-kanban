package api;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final InMemoryTaskManager manager;


    public PrioritizedHandler(InMemoryTaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                sendJson(exchange, 200, manager.getPrioritizedTasks());
            }
        } catch (Exception exception) {
            System.out.println("Произошла ошибка " + exception.getMessage());
        }
    }
}

