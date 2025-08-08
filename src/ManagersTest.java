import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    void managersReturnInitializedManagers() {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(manager, "TaskManager должен быть инициализирован");
        assertNotNull(historyManager, "HistoryManager должен быть инициализирован");
    }
}
