package api;

import manager.InMemoryTaskManager;

public class HistoryHandler extends BaseCollectionHandler {


    public HistoryHandler(InMemoryTaskManager manager) {
        super(manager);
    }

    @Override
    protected Object getCollection() {
        return manager.getHistory();
    }
}
