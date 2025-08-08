import java.util.*;

public interface TaskManager {
    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<SubTask> getAllSubtasks();

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    Task getTaskById(int id);
    Epic getEpicById(int id);
    SubTask getSubtaskById(int id);

    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubtask(SubTask subtask);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(SubTask subtask);

    void deleteTaskById(int id);
    void deleteEpicById(int id);
    void deleteSubtaskById(int id);

    List<SubTask> getSubtasksByEpicId(int epicId);

    List<Task> getHistory();
}