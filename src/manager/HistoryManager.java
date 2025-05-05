package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

interface HistoryManager {



    public void add (Task task);

    public List<Task> getHistory();

    }

