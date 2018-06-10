import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ClasroomDAOTest {
    @Before
    public void biuld() {
    }

    @Test
    public void testGetNames(){
        ClasroomDAO clasroom = null;
        try {
            clasroom = new ClasroomDAO();
        } catch (IOException e) {
            System.err.println("error in classroom creation");
        }
        for (String className : clasroom.getClassroomNames()){
        }
    }
}