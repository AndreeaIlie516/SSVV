package Assignment2;

import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddAssignmentTest {

    private TemaXMLRepo temaFileRepository;
    private TemaValidator temaValidator;
    private Service service;

    @BeforeAll
    static void createXML() {
        File xml = new File("fisiere/temeTest.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(xml))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<inbox>\n" +
                    "\n" +
                    "</inbox>");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setup() {
        this.temaFileRepository = new TemaXMLRepo("fisiere/temeTest.xml");
        this.temaValidator = new TemaValidator();
        this.service = new Service(null, null, this.temaFileRepository, this.temaValidator, null, null);
    }

    @AfterAll
    static void removeXML() {
        new File("fisiere/temeTest.xml").delete();
    }

    @Test
    void testAddAssignmentValidAssignment() {
        Tema newTema = new Tema("1", "alaBala", 1, 1);
        this.service.addTema(newTema);
        assertEquals(this.service.getAllTeme().iterator().next(), newTema);
    }

    @Test
    void testAddAssignmentEmptyID() {
        Tema newTema = new Tema("", "alaBala", 1, 1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema));
    }

    @Test
    void testAddAssignmentNullID() {
        Tema newTema = new Tema(null, "alaBala", 1, 1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema));
    }


    @Test
    void testAddAssignmentEmptyDescription() {
        Tema newTema = new Tema("1", "", 1, 1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema));
    }
    @Test
    void testAddAssignmentDeadlineTooLarge() {
        Tema newTema = new Tema("1", "alaBala", 15, 1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema));
    }

    @Test
    void testAddAssignmentDeadlineTooSmall() {
        Tema newTema = new Tema("1", "alaBala", 0, 1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema));
    }

    @Test
    void testAddAssignmentReceivedTooSmall() {
        Tema newTema = new Tema("1", "alaBala", 1, 0);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema));
    }

    @Test
    void testAddAssignmentReceivedTooLarge() {
        Tema newTema = new Tema("1", "alaBala", 0, 15);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema));
    }

    @Test
    void testAddAssignmentDuplicateAssignment() {
        Tema newTema = new Tema("1", "a", 1, 1);
        this.service.addTema(newTema);

        Tema newTema2 = new Tema("1", "a", 1, 1);

        assertEquals(this.service.addTema(newTema2).getID(), newTema.getID());
    }

}
