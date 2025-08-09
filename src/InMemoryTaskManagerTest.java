import Managers.Managers;
import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Managers.Task;
import Managers.TaskManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    void tasksAreEqualById() {
        Task task1 = new Task("Test", "desc", Status.NEW);
        Task task2 = new Task("Another", "desc", Status.IN_PROGRESS);
        task1.setId(42);
        task2.setId(42);
        Assertions.assertEquals(task1, task2, "Задачи с одинаковыми id должны быть равны");
    }

    @Test
    void epicsAndSubtasksEqualById() {
        Epic epic1 = new Epic("Epic1", "desc");
        Epic epic2 = new Epic("Epic2", "desc");
        epic1.setId(100);
        epic2.setId(100);
        Assertions.assertEquals(epic1, epic2, "Эпики с одинаковыми id должны быть равны");

        SubTask st1 = new SubTask("Sub1", "desc", Status.NEW, 1);
        SubTask st2 = new SubTask("Sub2", "desc", Status.DONE, 1);
        st1.setId(200);
        st2.setId(200);
        Assertions.assertEquals(st1, st2, "Подзадачи с одинаковыми id должны быть равны");
    }

    @Test
    void epicCannotBeItsOwnSubtask() {
        Epic epic = new Epic("Tasks.Epic", "desc");
        manager.createEpic(epic);
        SubTask sub = new SubTask("Sub", "desc", Status.NEW, epic.getId());
        sub.setId(epic.getId());

        Assertions.assertNotEquals(epic.getId(), sub.getId(), "Эпик не должен быть своей же подзадачей");
    }

    @Test
    void subtaskCannotBeItsOwnEpic() {
        Epic epic = new Epic("Tasks.Epic", "desc");
        manager.createEpic(epic);

        SubTask sub = new SubTask("Sub", "desc", Status.NEW, epic.getId());
        manager.createSubtask(sub);

        Assertions.assertNotEquals(sub.getId(), sub.getEpicId(), "Подзадача не может быть своим эпиком");
    }

    @Test
    void canAddAndFindTasksById() {
        Task task = new Task("Managers.Task", "desc", Status.NEW);
        manager.createTask(task);
        Task fetched = manager.getTaskById(task.getId());
        Assertions.assertEquals(task, fetched);

        Epic epic = new Epic("Tasks.Epic", "desc");
        manager.createEpic(epic);
        Epic fetchedEpic = manager.getEpicById(epic.getId());
        Assertions.assertEquals(epic, fetchedEpic);

        SubTask sub = new SubTask("Sub", "desc", Status.NEW, epic.getId());
        manager.createSubtask(sub);
        SubTask fetchedSub = manager.getSubtaskById(sub.getId());
        Assertions.assertEquals(sub, fetchedSub);
    }

    @Test
    void manualAndAutoIdDoNotConflict() {
        Task manualTask = new Task("Manual", "desc", Status.NEW);
        manualTask.setId(100);
        manager.createTask(manualTask);

        Task autoTask = new Task("Auto", "desc", Status.NEW);
        manager.createTask(autoTask);

        Assertions.assertNotEquals(manualTask.getId(), autoTask.getId(), "ID не должны конфликтовать");
    }

    @Test
    void taskRemainsUnchangedAfterAdd() {
        Task task = new Task("Original", "desc", Status.IN_PROGRESS);
        manager.createTask(task);

        Task fromManager = manager.getTaskById(task.getId());
        Assertions.assertEquals(task.getName(), fromManager.getName());
        Assertions.assertEquals(task.getDescription(), fromManager.getDescription());
        Assertions.assertEquals(task.getStatus(), fromManager.getStatus());
    }

    @Test
    void historyManagerStoresOriginalTask() {
        Task task = new Task("HistoryTest", "desc", Status.NEW);
        manager.createTask(task);
        Task fetched = manager.getTaskById(task.getId());

        List<Task> history = manager.getHistory();
        Assertions.assertTrue(history.contains(fetched), "История должна содержать просмотренную задачу");
        Assertions.assertEquals(fetched, history.get(history.size() - 1), "Последний элемент истории — последняя просмотренная задача");
    }
}
