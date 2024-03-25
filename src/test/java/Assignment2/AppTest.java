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

public class AppTest {
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
    void setup() {
        this.studentFileRepository = new StudentXMLRepo("fisiere/studentiTest.xml");
        this.studentValidator = new StudentValidator();
        this.service = new Service(this.studentFileRepository, this.studentValidator, null, null, null, null);
    }

    @AfterAll
    static void removeXML() {
        new File("fisiere/studentiTest.xml").delete();
    }

    @Test
    void testAddStudentOnGroup() {
        Student newStudent1 = new Student("100", "a", 931, "aa");
        Student newStudent2 = new Student("101", "a", -6, "aa");
        Student newStudent3 = new Student("102", "a", 0, "aa");
        this.service.addStudent(newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
        this.service.addStudent(newStudent3);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
        assertEquals(students.next(), newStudent3);

        this.service.deleteStudent("100");
        this.service.deleteStudent("102");
    }

    @Test
    void testAddStudentOnEmail() {
        Student newStudent1 = new Student("20", "Ion Popescu", 933, "alabala@yahoo.com");
        Student newStudent2 = new Student("21", "Ion Popescu", 933, "");
        Student newStudent3 = new Student("22", "Ion Popescu", 933, null);
        this.service.addStudent(newStudent1);
        var students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }

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

}