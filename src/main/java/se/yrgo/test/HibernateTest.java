package se.yrgo.test;

import jakarta.persistence.*;

import se.yrgo.domain.Student;
import se.yrgo.domain.Subject;
import se.yrgo.domain.Tutor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class HibernateTest {
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("databaseConfig");

    public static void main(String[] args) {
        //setUpData();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //Task-1 - Navigating across relationships (Using member of)
        Subject science = em.find(Subject.class, 2);
        Query query1 = em.createQuery("select tutor.teachingGroup from Tutor as tutor where :subject member of tutor.subjectsToTeach");
        query1.setParameter("subject", science);
        List<Student> studentWithScienceTeacher = query1.getResultList();
        System.out.println("Students whose tutor can teach science:");
        for (Student student : studentWithScienceTeacher) {
            System.out.println(student);
        }

        //Task-2 - Report Query - Multiple fields (Use join)
        //Query query2 = em.createQuery("select student.name, tutor.name from Tutor tutor join tutor.teachingGroup as student");
        //List<Objects[]> results = query2.getResultList();
        //for (Object[] item : results) {
        //    System.out.println("Student " + item[0] + " has tutor: " + item[1]);
        //}

        //Task-3 - Report Query - Aggregation
        //double averageSemesterLength = (Double) em.createQuery("select avg(subject.numberOfSemesters) from Subject subject").getSingleResult();
        //System.out.println("Average semester length for the subjects is: " + averageSemesterLength);

        //Task-4 - Query With Aggregation
        //int maxSalary = (int) em.createQuery("select max(tutor.salary) from Tutor tutor").getSingleResult();
        //System.out.println("Max salary from the tutor is: " + maxSalary);

        //Task-5 - NamedQuery
        //List<Tutor> results = em.createNamedQuery("searchByNumber", Tutor.class).setParameter("name", 10000).getResultList();
        //System.out.println("Tutors with salary higher than 10000:");
        //for(Tutor name : results) {
        //    System.out.println(name);
        //}

        tx.commit();
        em.close();
    }

    public static void setUpData() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();


        Subject mathematics = new Subject("Mathematics", 2);
        Subject science = new Subject("Science", 2);
        Subject programming = new Subject("Programming", 3);
        em.persist(mathematics);
        em.persist(science);
        em.persist(programming);

        Tutor t1 = new Tutor("ABC123", "Johan Smith", 40000);
        t1.addSubjectsToTeach(mathematics);
        t1.addSubjectsToTeach(science);


        Tutor t2 = new Tutor("DEF456", "Sara Svensson", 20000);
        t2.addSubjectsToTeach(mathematics);
        t2.addSubjectsToTeach(science);

        // This tutor is the only tutor who can teach History
        Tutor t3 = new Tutor("GHI678", "Karin Lindberg", 0);
        t3.addSubjectsToTeach(programming);

        em.persist(t1);
        em.persist(t2);
        em.persist(t3);


        t1.createStudentAndAddtoTeachingGroup("Jimi Hendriks", "1-HEN-2019", "Street 1", "city 2", "1212");
        t1.createStudentAndAddtoTeachingGroup("Bruce Lee", "2-LEE-2019", "Street 2", "city 2", "2323");
        t3.createStudentAndAddtoTeachingGroup("Roger Waters", "3-WAT-2018", "Street 3", "city 3", "34343");

        tx.commit();
        em.close();
    }


}
