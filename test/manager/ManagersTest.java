package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getReadyToWorkDefault() {
        assertFalse(Managers.getDefault() == null);
    }

    @Test
    void getReadyToWorkDefaultHistory() {
        assertFalse(Managers.getDefaultHistory() == null);
    }
}