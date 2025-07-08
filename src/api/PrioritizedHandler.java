package api;

import manager.InMemoryTaskManager;

public class PrioritizedHandler extends BaseCollectionHandler {

    public PrioritizedHandler(InMemoryTaskManager manager) {
        super(manager);
    }

    @Override
    protected Object getCollection() {
        return manager.getPrioritizedTasks();
    }
}

