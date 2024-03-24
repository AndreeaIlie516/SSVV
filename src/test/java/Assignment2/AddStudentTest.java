package Assignment2;

import domain.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.StudentXMLRepo;
import service.Service;
import validation.StudentValidator;
import validation.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AddStudentTest {
    private StudentXMLRepo studentFileRepository;
    private StudentValidator studentValidator;
    private Service service;

    @BeforeAll
    static void createXML() {
        File xml = new File("fisiere/studentiTest.xml");
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
        this.studentFileRepository = new StudentXMLRepo("fisiere/studentiTest.xml");
        this.studentValidator = new StudentValidator();
        this.service = new Service(this.studentFileRepository, this.studentValidator, null, null, null, null);
    }

    @AfterAll
    static void removeXML() {
        new File("fisiere/studentiTest.xml").delete();
    }

    /**
     * test Student duplicates
     */
    @Test
    public void testAddDuplicateStudent(){
        Student newStudent1 = new Student("3", "Stefan cel Mare", 933, "stefan@gmail.com");

        Student stud1 = this.service.addStudent(newStudent1);
        assertNull(stud1);

        Student stud2 = this.service.addStudent(newStudent1);
        assertEquals(newStudent1.getID(), stud2.getID());

        this.service.deleteStudent("3");
    }

    @Test
    public void testAddNonDuplicateStudent(){
        Student newStudent1 = new Student("11", "Gigi", 935, "gigi@gmail.com");
        Student newStudent2 = new Student("111", "Gigi", 935, "gigi@gmail.com");


        Student stud1 = this.service.addStudent(newStudent1);
        assertNull(stud1);

        Student stud2 = this.service.addStudent(newStudent2);
        assertNull(stud2);

        var students = this.service.getAllStudenti().iterator();

        assertEquals(students.next().getID(), newStudent1.getID());
        assertEquals(students.next().getID(), newStudent2.getID());

        this.service.deleteStudent("11");
        this.service.deleteStudent("111");
    }

    /**
     * test Student id
     */
    @Test
    public void testAddStudentWithValidId() {
        Student newStudent = new Student("2345", "Bobo", 111, "bobo@gmail.com");

        this.service.addStudent(newStudent);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent.getID());

        this.service.deleteStudent("2345");
    }

    @Test
    public void testAddStudentWithEmptyId() {
        Student newStudent = new Student("", "Bobo", 111, "bobo@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent));
    }

    @Test
    public void testAddStudentWithNullId() {
        Student newStudent = new Student(null, "Bobo", 111, "bobo@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent));
    }

    /**
     * test Student name
     */
    @Test
    public void testAddStudentWithValidName(){
        Student newStudent1 = new Student("12223", "MrBean", 223, "mrBean@gmail.com");

        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());

        this.service.deleteStudent("12223");
    }

    @Test
    public void testAddStudentWithEmptyName(){
        Student newStudent1 = new Student("12223", "", 223, "mrBean@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent1));

    }

    @Test
    public void testAddStudentWithNullName(){
        Student newStudent1 = new Student("12223", null, 223, "mrBean@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent1));
    }


    /**
     * test Student group
     */
    @Test
    public void testAddStudentFromValidGroup() {
        Student newStudent1 = new Student("1234", "Batman", 844, "batman@gmail.com");

        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent1.getID());

        this.service.deleteStudent("1234");
    }

    @Test
    public void testAddStudentFromInvalidGroup() {
        Student newStudent1 = new Student("1234", "Batman", -6, "batman@gmail.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent1));
    }

    /**
     * test Student email
     */
    @Test
    public void testAddStudentWithValidEmail() {
        Student newStudent = new Student("1111", "BobTheBuilder", 334, "bobTheBuilder@gmail.com");

        this.service.addStudent(newStudent);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent.getID());

        this.service.deleteStudent("1111");
    }

    @Test
    public void testAddStudentWithEmptyEmail() {
        Student newStudent = new Student("1111", "BobTheBuilder", 334, "");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent));
    }

    @Test
    public void testAddStudentWithNullEmail() {
        Student newStudent = new Student("1111", "BobTheBuilder", 334, null);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent));
    }



    /**
     * BVA Test case
     */
    @Test
    public void testAddStudentGroupLowerBVABound(){
        Student newStudent = new Student("1", "OliMurs", 0, "oli@gmail.com");

        this.service.addStudent(newStudent);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next().getID(), newStudent.getID());

        this.service.deleteStudent("1");
    }



}
