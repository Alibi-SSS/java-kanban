import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Создаем эпик
        Epic epic = new Epic("Переезд", "Организовать переезд в другой офис");
        manager.createEpic(epic);

        // Создаем подзадачи для эпика
        SubTask subtask1 = new SubTask("Собрать коробки", "Упаковать вещи", Status.NEW, epic.getId());
        SubTask subtask2 = new SubTask("Перевезти вещи", "Доставить в новый офис", Status.NEW, epic.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        // Создаем обычную задачу
        Task task = new Task("Позвонить клиенту", "Обсудить договор", Status.IN_PROGRESS);
        manager.createTask(task);

        // Выводим все задачи
        System.out.println("Все задачи:");
        manager.getAllTasks().forEach(System.out::println);

        System.out.println("\nВсе эпики:");
        manager.getAllEpics().forEach(System.out::println);

        System.out.println("\nВсе подзадачи:");
        manager.getAllSubtasks().forEach(System.out::println);

        // Меняем статус подзадачи и проверяем статус эпика
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);

        System.out.println("\nСтатус эпика после изменения подзадачи: " + epic.getStatus());

        // допустим, задачи уже созданы
        manager.getTaskById(1);
        manager.getSubtaskById(1);
        manager.getEpicById(1);
        manager.getSubtaskById(1);

        List<Task> history = manager.getHistory();

        // Выведет: [10, 13, 4, 13] (если ID совпадают с кодом выше)
        System.out.println("\nИстория поиска: " + "\n"  + history);
    }
}