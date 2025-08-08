import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

public class InMemoryTaskManager implements TaskManager{
    private int nextId = 1;
    private final HistoryManager historyManager;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subtasks = new HashMap<>();

    private final LinkedList<Task> history = new LinkedList<>();
    int historyLim = 10;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    // Генерация нового ID
    private int generateId() {
        return nextId++;
    }

    // a. Получение списка всех задач
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // b. Удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear(); // Удаляем все подзадачи, т.к. они связаны с эпиками
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        // Обновляем статусы эпиков, т.к. подзадачи удалены
        for (Epic epic : epics.values()) {
            updateEpicStatus(epic.getId());
        }
    }

    // c. Получение задачи по ID
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    public SubTask getSubtaskById(int id) {
        SubTask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    // d. Создание задач
    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(SubTask subtask) {
        int epicId = subtask.getEpicId();
        if (!epics.containsKey(epicId)) {
            throw new IllegalArgumentException("Эпик с ID=" + epicId + " не найден!");
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epics.get(epicId).addSubtaskId(subtask.getId());
        updateEpicStatus(epicId); // Обновляем статус эпика
    }

    // e. Обновление задач
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            throw new IllegalArgumentException("Задача с ID=" + task.getId() + " не найдена!");
        }
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            throw new IllegalArgumentException("Эпик с ID=" + epic.getId() + " не найден!");
        }
        Epic savedEpic = epics.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    public void updateSubtask(SubTask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            throw new IllegalArgumentException("Подзадача с ID=" + subtask.getId() + " не найдена!");
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId()); // Обновляем статус эпика
    }

    // f. Удаление по ID
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            // Удаляем все подзадачи эпика
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    public void deleteSubtaskById(int id) {
        SubTask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtaskId(id);
            updateEpicStatus(epic.getId()); // Обновляем статус эпика
        }
    }

    // Дополнительный метод: Получение списка подзадач эпика
    public List<SubTask> getSubtasksByEpicId(int epicId) {
        if (!epics.containsKey(epicId)) {
            throw new IllegalArgumentException("Эпик с ID=" + epicId + " не найден!");
        }
        List<SubTask> result = new ArrayList<>();
        for (int subtaskId : epics.get(epicId).getSubtaskIds()) {
            result.add(subtasks.get(subtaskId));
        }
        return result;
    }

    // Обновление статуса эпика
    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (int subtaskId : subtaskIds) {
            SubTask subtask = subtasks.get(subtaskId);

            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            } if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void add(Task task) {
        if (task == null) return;
        history.add(task);
        if (history.size() > historyLim) {
            history.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
