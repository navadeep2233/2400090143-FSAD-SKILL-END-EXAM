package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.Date;
import java.util.Scanner;

public class ClientDemo {

    public static void main(String[] args) {
        // Initialize SessionFactory
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = cfg.buildSessionFactory();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Department Management ---");
            System.out.println("1. Insert a new Department");
            System.out.println("2. Delete a Department by ID");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    insertDepartment(sessionFactory, scanner);
                    break;
                case 2:
                    deleteDepartment(sessionFactory, scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    sessionFactory.close();
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void insertDepartment(SessionFactory sessionFactory, Scanner scanner) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            System.out.print("Enter Department Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Department Description: ");
            String description = scanner.nextLine();

            System.out.print("Enter Department Status: ");
            String status = scanner.nextLine();

            Department department = new Department(name, description, new Date(), status);
            session.save(department);

            transaction.commit();
            System.out.println("Department inserted successfully with ID: " + department.getId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void deleteDepartment(SessionFactory sessionFactory, Scanner scanner) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            System.out.print("Enter Department ID to delete: ");
            int id = scanner.nextInt();

            Department department = session.get(Department.class, id);
            if (department != null) {
                session.delete(department);
                transaction.commit();
                System.out.println("Department with ID " + id + " deleted successfully.");
            } else {
                System.out.println("Department with ID " + id + " not found.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
