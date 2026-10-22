package com.craft;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

public class App {

    // testing
    public static void main(String[] args) {
                        //JPA
        //Student student1 = new Student();
        //student1.setName("Mohammed ali");
        //student1.setTech("Python");
        //createStudent(student1);
        //System.out.println(getStudentById(2));
        //updateStudentName("Alemayehu", 2);
        //System.out.println(getStudentById(2));
        //deleteStudentById(1);

                       //JPQL
        EntityManager em = getEntityManager();
        try {
            // A) Select All Students
            System.out.println("\n--- All Students ---");
            selectAllStudents(em);

            // B) Select Student by Tech
            System.out.println("\n--- Students Using Python ---");
            selectStudentsByTech(em, "Python");

            // C) Select Student by Name and Tech
            System.out.println("\n--- Student by Name and Tech ---");
            selectStudentsByNameAndTech(em, "Mohammed ali", "Python");

            // D) Retrieve All Student Names
            System.out.println("\n--- All Student Names ---");
            retrieveAllStudentNames(em);

        } finally {
            em.close();
        }
    }

    public static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("studentPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // create student
    public static void createStudent(Student student) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(student);
        entityManager.getTransaction().commit();
    }

    // get student
    public static Student getStudentById(int id) {
        EntityManager entityManager = getEntityManager();
        return entityManager.find(Student.class, id);
    }

    // update student
    public static void updateStudentName(String newName, int id) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();

        Student student = em.find(Student.class, id);
        student.setName(newName);

        em.getTransaction().commit();
        em.close();
    }

    // update both name and tech
    public static void updateStudentNameAndTech(String newName, String newTech,
                                                int id) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        Student student = entityManager.find(Student.class, id);
        student.setName(newName);
        student.setTech(newTech);
        entityManager.getTransaction().commit();
    }

    // delete student
    public static void deleteStudentById(int id) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            Student student = em.find(Student.class, id);

            if (student != null) {
                em.remove(student);
            }

            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

/// //////////////JPQL
    // A) Select All Students
    public static void selectAllStudents(EntityManager em) {

        List<Student> students = em.createQuery(
                "SELECT s FROM Student s", Student.class
        ).getResultList();

        for (Student s : students) {
            System.out.println(s);
        }
    }

    // B) Select Student by Tech
    public static void selectStudentsByTech(EntityManager em, String tech) {

        List<Student> students = em.createQuery(
                "SELECT s FROM Student s WHERE s.tech = :tech",
                Student.class
        ).setParameter("tech", tech).getResultList();

        for (Student s : students) {
            System.out.println(s);
        }
    }

    // C) Select Student by Name and Tech
    public static void selectStudentsByNameAndTech(
            EntityManager em, String name, String tech) {

        List<Student> students = em.createQuery(
                        "SELECT s FROM Student s WHERE s.name = :name AND s.tech = :tech",
                        Student.class
                ).setParameter("name", name)
                .setParameter("tech", tech)
                .getResultList();

        for (Student s : students) {
            System.out.println(s);
        }
    }

    // D) Retrieve All Student Names
    public static void retrieveAllStudentNames(EntityManager em) {

        List<String> names = em.createQuery(
                "SELECT s.name FROM Student s", String.class
        ).getResultList();

        for (String name : names) {
            System.out.println(name);
        }
    }


}