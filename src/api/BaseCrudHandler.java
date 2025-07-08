package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;

import java.io.IOException;

abstract class BaseCrudHandler<T> extends BaseHttpHandler implements HttpHandler {
    protected final InMemoryTaskManager manager;

    public BaseCrudHandler(InMemoryTaskManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            switch (method) {
                case "GET":
                    getHandler(exchange, path);
                    break;
                case "POST":
                    postHandler(exchange, path);
                    break;
                case "DELETE":
                    deleteHandler(exchange, path);
                    break;
            }

        } catch (Exception exception) {
            System.out.println("Произошла ошибка " + exception.getMessage());
            sendText(exchange, "Произощла ошибка: " + exception.getMessage(), 500);
        }
    }


    protected abstract void getHandler(HttpExchange exchange, String path) throws IOException;

    protected abstract void postHandler(HttpExchange exchange, String path) throws IOException;

    protected abstract void deleteHandler(HttpExchange exchange, String path) throws IOException;


}
