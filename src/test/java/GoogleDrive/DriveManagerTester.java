package GoogleDrive;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DriveManagerTester {

    DriveManager manager;

    @Before
    public void initDriveManager() {
        try {
            manager = new DriveManager();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleTest1() {
        manager.getFolder("1U94xu-lXDdrSH9gsV6ol0J2N8HDwgiwT");
        manager.finishManager();
    }
}
