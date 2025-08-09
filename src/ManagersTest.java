import Managers.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import Managers.TaskManager;
import Managers.HistoryManager;

public class ManagersTest {

    @Test
    void managersReturnInitializedManagers() {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Assertions.assertNotNull(manager, "Managers.TaskManager должен быть инициализирован");
        Assertions.assertNotNull(historyManager, "Managers.Managers.HistoryManager должен быть инициализирован");
    }
}
