package at.campus02.dbp2.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    private EntityManager manager;

    public StudentDaoImpl (EntityManagerFactory factory) {
        manager = factory.createEntityManager();
    }





    @Override
    public boolean create(Student student) {
        if(student==null)
        return false;
        else
            return true;
    }

    @Override
    public Student update(Student student) {
        return null;
    }

    @Override
    public void delete(Student student) {

    }

    @Override
    public Student find(Integer id) {
        return null;
    }

    @Override
    public List<Student> findAll() {
        return null;
    }

    @Override
    public List<Student> findAllByLastname(String lastname) {
        return null;
    }

    @Override
    public List<Student> findAllBornBefore(int year) {
        return null;
    }

    @Override
    public List<Student> findAllByGender(Gender gender) {
        return null;
    }

    @Override
    public void close() {

    }
}
