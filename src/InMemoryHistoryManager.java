import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();
    private static final int HISTORY_LIMIT = 10;

    @Override
    public void add(Task task) {
        if (task == null) return;
        history.add(task);
        if (history.size() > HISTORY_LIMIT) {
            history.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }
}
