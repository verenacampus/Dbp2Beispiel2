import at.campus02.dbp2.jpa.Gender;
import at.campus02.dbp2.jpa.Student;
import at.campus02.dbp2.jpa.StudentDao;
import at.campus02.dbp2.jpa.StudentDaoImpl;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class StudentDaoSpec {



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
                public void createNullAsStudentReturnsFalse(){
            //create:
            //wird null übergeben), soll false zurückgegeben werden.

            //given
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("nameOfJpaPersistenceUnit");
            StudentDao dao = new StudentDaoImpl(factory);

            //when
            boolean result =  dao.create(null);

            //then
            assertThat(result, is(false));

        }

        @Test
    public  void createPersistsStudentInDatabaseAndReturnsTrue() {
        /*
        Nach erfolgreichem Aufruf soll der Student in der Datenbank existieren und true zurückgegeben werden.
         */

            // given
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("nameOfJpaPersistenceUnit");

            Student student = new Student();
            student.setLastName("Lastname");
            student.setFirstName("Firstname");
            student.setGender(Gender.FEMALE);
            StudentDao dao = new StudentDaoImpl(factory);

            //when
            boolean result = dao.create(student);

            //then
            assertThat(result, is(true));
            //überprüfen, ob der Student in der Datenbank existiert.


            EntityManager manager = factory.createEntityManager();
           Student fromDB = manager.find(Student.class, student.getId());
           assertThat(fromDB, is(student));



        }


    }



