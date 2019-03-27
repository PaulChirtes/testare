package proj;

import org.junit.Test;
import proj.domain.Student;
import proj.repository.NotaXMLRepo;
import proj.repository.StudentXMLRepo;
import proj.repository.TemaXMLRepo;
import proj.service.Service;
import proj.validation.NotaValidator;
import proj.validation.StudentValidator;
import proj.validation.TemaValidator;

import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void addStudentTest(){
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        Service service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
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
}
