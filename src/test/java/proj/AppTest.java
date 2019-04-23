package proj;

import org.junit.Test;
import proj.domain.Nota;
import proj.domain.Student;
import proj.domain.Tema;
import proj.repository.NotaXMLRepo;
import proj.repository.StudentXMLRepo;
import proj.repository.TemaXMLRepo;
import proj.service.Service;
import proj.validation.NotaValidator;
import proj.validation.StudentValidator;
import proj.validation.TemaValidator;
import proj.validation.ValidationException;

import java.time.LocalDate;
import java.util.Date;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void addStudentTest(){
        Service service = getService();
        long countBefore = StreamSupport.stream(service.getAllStudenti().spliterator(), false).count();
        service.addStudent(new Student("100", "Paul", 10, "a@a.a"));
        long countAfter = StreamSupport.stream(service.getAllStudenti().spliterator(), false).count();
        assertTrue(countBefore+1==countAfter);
        Student stud = service.findStudent("100");
        assertNotNull(stud);
        assertEquals(stud.getNume(),"Paul");
        assertEquals(stud.getEmail(), "a@a.a");
        assertEquals(stud.getID(),"100");
        assertEquals(stud.getGrupa(),10);
        service.deleteStudent("100");
        long countAfterDelete = StreamSupport.stream(service.getAllStudenti().spliterator(), false).count();
        assertEquals(countBefore, countAfterDelete);
        assertTrue(true);
    }

    @Test
    public void addStudentFailTest(){
        Service service = getService();

        try{
            service.addStudent(new Student("", "Paul", 10, "a@a.a"));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }

        try{
            service.addStudent(new Student("10", "", 10, "a@a.a"));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }

        try{
            service.addStudent(new Student("10", "Paul", -1, "a@a.a"));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }

        try{
            service.addStudent(new Student("10", "Paul", 10, ""));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }

    }


    @Test
    public void addAssignment(){
        Service service = getService();


        long countBefore = StreamSupport.stream(service.getAllTeme().spliterator(), false).count();
        Tema addedOne = service.addTema(new Tema("100", "O tema", 3,5));
        assertNotNull(addedOne);
        long countAfter = StreamSupport.stream(service.getAllTeme().spliterator(), false).count();
        assertTrue(countBefore+1==countAfter);
        Tema tema = service.findTema("100");
        assertEquals(tema.getID(),"100");
        assertEquals(tema.getDeadline(),3);
        assertEquals(tema.getPrimire(),5);
        assertEquals(tema.getDescriere(),"O tema");
        assertNotNull(tema);
        service.deleteTema("100");
        long countAfterDelete = StreamSupport.stream(service.getAllTeme().spliterator(), false).count();
        assertEquals(countBefore, countAfterDelete);
    }

    @Test
    public void addAssignmentFail(){
        Service service = getService();

        try{
            service.addTema(new Tema("", "O tema", 3,5));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }

        try{
            service.addTema(new Tema("100", "", 3,5));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }

        try{
            service.addTema(new Tema("100", "O tema", 0,5));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }

        try{
            service.addTema(new Tema("100", "O tema", 15,5));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }

        try{
            service.addTema(new Tema("100", "O tema", 2,0));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }

        try{
            service.addTema(new Tema("100", "O tema", 1,15));
            assertTrue(false);
        } catch (ValidationException vex){
            assertTrue(true);
        }
    }


    @Test
    public void addAssignment2(){
        Service service = getService();

        long countBefore = StreamSupport.stream(service.getAllTeme().spliterator(), false).count();
        Tema addedOne = service.addTema(new Tema("100", "O tema", 3,5));
        Tema addedAgain = service.addTema(new Tema("100", "O tema", 3,5));

        assertEquals(addedOne, addedAgain);
        service.deleteTema("100");
    }

    private Service getService() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        return new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    public void addStudentBigBang(){
        addStudentTest();
    }

    @Test
    public void addAssignmentBigBang(){
        addAssignment();
    }

    @Test
    public void addGradeBigBang(){
        Service service = getService();
        Tema tema = service.addTema(new Tema("101", "O tema", 1,14));
        Student student  = service.addStudent(new Student("101", "Paul", 10, "a@a.a"));
        String idNota = student.getID() + "#" + tema.getID();
        double notaFinala = service.addNota(new Nota(idNota,student.getID(),tema.getID(),10, LocalDate.of(2019,4,10)),"bine");
        assertEquals(10,notaFinala,0);
        service.deleteNota(idNota);
        service.deleteTema("101");
        service.deleteStudent("101");
    }

    @Test
    public void bigBang(){
        addStudentBigBang();
        addAssignmentBigBang();
        addGradeBigBang();
    }

    @Test
    public void addStudentTopDown(){
        addStudentTest();
    }

    @Test
    public void addAssignmentTopDown(){
        addStudentTopDown();
        addAssignment();
    }

    @Test
    public void addGradeTopDown(){
        addAssignmentTopDown();
        Service service = getService();
        Tema tema = service.addTema(new Tema("101", "O tema", 1,14));
        Student student  = service.addStudent(new Student("101", "Paul", 10, "a@a.a"));
        String idNota = student.getID() + "#" + tema.getID();
        double notaFinala = service.addNota(new Nota(idNota,student.getID(),tema.getID(),10, LocalDate.of(2019,4,10)),"bine");
        assertEquals(10,notaFinala,0);
        service.deleteNota(idNota);
        service.deleteTema("101");
        service.deleteStudent("101");
    }

}
