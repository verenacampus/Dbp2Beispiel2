import at.campus02.dbp2.jpa.Gender;
import at.campus02.dbp2.jpa.Student;
import at.campus02.dbp2.jpa.StudentDao;
import at.campus02.dbp2.jpa.StudentDaoImpl;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class StudentDaoSpec {

    private EntityManagerFactory factory;
    private EntityManager manager;
    private StudentDao dao;

    // <editor-fold desc= "Hilfsfunktionen">
    @Before
    public void setUp() {
        factory = Persistence.createEntityManagerFactory("nameOfJpaPersistenceUnit");
        manager = factory.createEntityManager();
        dao = new StudentDaoImpl(factory);
    }

    @After
    public void tearDown(){
        dao.close();
        if(manager.isOpen()){
            manager.close();
        }
        if(factory.isOpen()){
            factory.close();
        }
    }

    private Student prepareStudent(String firstname, String lastname, Gender gender, String birthdayString) {
        Student student = new Student();
        student.setFirstName(firstname);
        student.setLastName(lastname);
        student.setGender(gender);
        if (birthdayString != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            student.setBirthday(LocalDate.parse(birthdayString, formatter));
        }
        return student;
    }

    private void create(Student student) {
        manager.getTransaction().begin();
        manager.persist(student);
        manager.getTransaction().commit();
    }

// </editor-fold>
    //@Ignore
    @Test
    public void ensureThatToUppercaseResultsInAllUppercaseLetters() {
        //given

        String str1 = "string";
        //when

        String result = str1.toUpperCase();
        //then

        assertThat(result, is("STRING"));
    }


    @Test
    public void createNullAsStudentReturnsFalse() {
        //create:
        //wird null übergeben), soll false zurückgegeben werden.

        //when
        boolean result = dao.create(null);

        //then
        assertThat(result, is(false));

    }

    @Test
    public void createPersistsStudentInDatabaseAndReturnsTrue() {
        /*
        Nach erfolgreichem Aufruf soll der Student in der Datenbank existieren und true zurückgegeben werden.
         */

        // given

        Student student = prepareStudent("Firstname", "Lastname", Gender.FEMALE, "13.05.1979");


        //when
        boolean result = dao.create(student);

        //then
        assertThat(result, is(true));
        //überprüfen, ob der Student in der Datenbank existiert.

        Student fromDB = manager.find(Student.class, student.getId());
        assertThat(fromDB, is(student));
    }

    @Test
    public void createAlreadyExistingStudentReturnsFalse() {

        Student student = prepareStudent("Firstname", "Lastname", Gender.FEMALE, "13.05.1979");

        create(student);

        // when
        boolean result = dao.create(student);

        //then
        assertThat(result, is(false));

    }


    @Test
    public void findStudentReturnsEntityFromDatabase() {
        //given
        Student student = prepareStudent("Firstname", "Lastname", Gender.FEMALE, "13.05.1979");
        create(student);
        // when
        Student resultIMPL = dao.find(student.getId());
        Student resultueberpruefung = manager.find(Student.class, student.getId());

        // then
        assertThat(resultIMPL, is(resultueberpruefung));
    }

    @Test
    public void findStudentWithNullAsIdReturnsNull() {
        //expect
        assertThat(dao.find(null), is(nullValue()));
    }

    @Test
    public void findStudentWithNotExistingIdReturnsNull() {
        //expect
        assertThat(dao.find(1111), is(nullValue()));
    }

    /*
    Nach erfolgreichem Aufruf soll die Entity in der Datenbank die geänderten Werte übernommen haben
    und das geänderte Objekt zurückgegeben werden.
     */
    @Test
    public void updateStudentChangesValuesInDatabase() {
        Student student = prepareStudent("Firstname", "Lastname", Gender.FEMALE, "13.05.1979");
        create(student);
        manager.clear();

        // when
        student.setLastName("Married-Now");
        Student resultIMPL = dao.update(student);
        Student manuell = manager.find(Student.class, student.getId());


        // then
        assertThat(resultIMPL.getLastName(), is("Married-Now"));
        assertThat(manuell.getLastName(), is("Married-Now"));
        assertThat(resultIMPL, is(manuell));
    }

    //Bei Angabe von nul) soll null zurückgegeben werden.

    @Test
    public void updateNullAsStudentReturnsNull() {
    //expect
        assertThat(dao.update(null), is(nullValue()));
    }


//Bei Angabe eines nicht existierenden Studenten (oder null) soll null zurückgegeben werden.
    @Test
    public void updateNotExistingStudentReturnsNull(){
       //given
        Student student = prepareStudent("Firstname", "Lastname", Gender.FEMALE, "13.05.1979");

        //expect
        assertThat(dao.update(student), is(nullValue()));
    }



  //  delete: Nach erfolgreichem Aufruf soll die Entity nicht mehr in der Datenbank existieren.

    @Test
    public void deleteStudentRemovesEntityFromDatabase(){
        //given
        Student student = prepareStudent("Firstname", "Lastname", Gender.FEMALE, "13.05.1979");
        create(student);
        manager.clear();

        //when
            int id = student.getId();
            dao.delete(student);

        //then
        assertThat(dao.find(id), is(nullValue()));
        assertThat(manager.find(Student.class, id), is(nullValue()));
    }

//    Bei Angabe eines nicht existierenden Studenten (oder null) soll sich nichts ändern, aber auch keine Exception geworfen werden.
    @Test
    public void deleteNullOrNotExistingStudentDoesNotThrowExeption(){
        //expect no exception
        dao.delete(null);
        dao.delete(prepareStudent("firstname", "lastname", null, null));
    }

    //findAll: Es werden alle in der Datenbank existierenden Studenten gefunden.
    @Test
    public void findAllReturnsAllEntitiesFromDatabase(){
        //given
        Student student1 = prepareStudent("Firstname1", "Lastname1", Gender.FEMALE, "13.05.1979");
        create(student1);
        Student student2 = prepareStudent("Firstname2", "Lastname2", Gender.MALE, "13.05.1979");
        create(student2);
        Student student3 = prepareStudent("Firstname3", "Lastname3", Gender.FEMALE, "13.05.1997");
        create(student3);
        manager.clear();

        //when
        List <Student> result = dao.findAll();

        //expect
        assertThat(result.size(), is(3));
        assertThat(result, hasItems(student1, student2, student3));

    }




}