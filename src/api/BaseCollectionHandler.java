package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;

import java.io.IOException;

public abstract class BaseCollectionHandler extends BaseHttpHandler implements HttpHandler {
    protected final InMemoryTaskManager manager;

    public BaseCollectionHandler(InMemoryTaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                Object collection = getCollection();
                sendJson(exchange, 200, collection);
            }
        } catch (Exception exception) {
            System.out.println("Произошла ошибка " + exception.getMessage());
        }
    }

    protected abstract Object getCollection();
}
