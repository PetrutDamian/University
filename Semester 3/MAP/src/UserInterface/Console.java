package UserInterface;

import Controller.Service;
import Controller.ServiceException;
import Domain.Grade;
import Domain.HomeWork;
import Domain.Student;
import Main.SaptamanaCurenta;
import Validation.ValidationException;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {
    private Service serv;
    private Scanner scanner = new Scanner(System.in);


    public Console(Service serv){
        this.serv = serv;
    }
    private void modifyDeadline() throws ServiceException, ValidationException {
        int id,newDeadline;
        System.out.println("id = ");
        id = Integer.parseInt(scanner.nextLine());
        System.out.println("new deadline week = ");
        newDeadline = Integer.parseInt(scanner.nextLine());
        serv.modifyDeadline(id,newDeadline);
    }

    private void addHomeWork() throws ValidationException, ServiceException {
        int id, deadline;
        String descriere;
        System.out.print("id =");
        id = Integer.parseInt(scanner.nextLine());
        System.out.print("descriere:");
        descriere = scanner.nextLine();
        System.out.print("deadline week = ");
        deadline = Integer.parseInt(scanner.nextLine());
        serv.addHomeWork(id,descriere,deadline);
    }

    private void addGrade() throws ServiceException, ValidationException {
        int studentId,homeworkid,nrMotivate;
        String feedback;
        float grade;
        String teacher;
        System.out.print("Student id =");
        studentId = Integer.parseInt(scanner.nextLine());
        System.out.print("Homework id =");
        homeworkid = Integer.parseInt(scanner.nextLine());
        System.out.print("Teacher's name:");
        teacher = scanner.nextLine();
        System.out.print("Grade given:");
        grade = Float.parseFloat(scanner.nextLine());
        System.out.print("Numar de absente motivate :");
        nrMotivate = Integer.parseInt(scanner.nextLine());
        System.out.println("Feedback:");
        feedback = scanner.nextLine();
        serv.addGrade(studentId,homeworkid,teacher,grade,nrMotivate,feedback);
    }

    private void message(){
        System.out.println("1 : Modify deadline\n2 : Add HomeWork\n3 : Add grade\n4 : Add student\n5: Update student\n" +
                "6: Remove student\n7 : Remove homework\n8: View all students\n9: View all homeworks\n10: FilterStudentsByGroup\n" +
                "11: FiterStudentsByHomeWork\n12 : FilterStudentsByHomeWork+Teacher\n13 : FilterGrades\n14 : View all grades\n");
    }
    public void run(){
        int x;
        while(true){
            System.out.println("----------------------------------------------------------------------------------------------------------------------");
            message();
            System.out.println("Saptamna curenta este:"+ SaptamanaCurenta.getCurent());
            System.out.print(">>");
            try {
                x = Integer.parseInt(scanner.nextLine());
                switch(x){
                    case 1:
                        modifyDeadline();
                        break;
                    case 2:
                        addHomeWork();
                        break;
                    case 3:
                        addGrade();
                        break;
                    case 4:
                        addStudent();
                        break;
                    case 5:
                        updateStudent();
                        break;
                    case 6:
                        removeStudent();
                        break;
                    case 7:
                        removeHomeWork();
                        break;
                    case 8:
                        getAllStudents();
                        break;
                    case 9:
                        getAllHomeWorks();
                        break;
                    case 10:
                        filterStudentsByGroup();
                        break;
                    case 11:
                        filterStudentsByHomeWork();
                        break;
                    case 12:
                        filterStByHmAndTeacher();
                        break;
                    case 13:
                        filterGrades();
                        break;
                    case 14:
                        getAllGrades();
                        break;
                }
            }
            catch(ValidationException ex){
                System.out.print(ex.getError());
            }
            catch(ServiceException ex){
                System.out.print(ex.getError());
            }
        }
    }

    private void getAllGrades() {
        Collection<Grade> col = serv.getAllGrades();
        Iterator<Grade> it = col.iterator();
        if(!it.hasNext())
            System.out.println("No students!\n");
        else {
            System.out.println("STUDENT ID    HOMEWORK    TEACHER    GRADE");
            while (it.hasNext()) {
                Grade g = it.next();
                System.out.println(g.getIdStudent() + "             "+g.getIdHomeWork()+ "             "+g.getTeacher()+"     " +
                        "    "+g.getGrade());
            }
        }
    }

    private void filterGrades() {
        int hmId,week;
        System.out.println("Homework id: ");
        hmId=Integer.parseInt(scanner.nextLine());
        System.out.println("Week : ");
        week = Integer.parseInt(scanner.nextLine());
        List<Grade> filtered = serv.filterGrades(hmId,week);
        for(Grade g:filtered){
            System.out.println("id = "+g.getId() + " "+g.getGrade() + " "+g.getTeacher());
        }

    }

    private void printFilteredStudents(List<Student> l){
        System.out.println("Lista studentilor:");
        for(Student st:l){System.out.println("id="+st.getId()+ " "+st.getNume()+ " "+st.getPrenume() + " "+ st.getEmail());}
    }
    private void filterStByHmAndTeacher() {
        int id;
        System.out.println("id-ul temei: ");
        id = Integer.parseInt(scanner.nextLine());
        String teacher;
        System.out.println("Profesor: ");
        teacher = scanner.nextLine();
        printFilteredStudents(serv.filterStByHmAndTeacher(id,teacher));
    }

    private void filterStudentsByHomeWork() {
        int id;
        System.out.println("id-ul temei: ");
        id = Integer.parseInt(scanner.nextLine());
        printFilteredStudents(serv.filterStudentsByHm(id));
    }

    private void filterStudentsByGroup() {
        int grp;
        System.out.print("Grupa dupa care se filtreaza: ");
        grp = Integer.parseInt(scanner.nextLine());
        printFilteredStudents(serv.filterStudentsByGroup(grp));
    }

    private void getAllHomeWorks() {
        Collection<HomeWork> col = serv.getAllHomeWorks();
        Iterator<HomeWork> it = col.iterator();
        if(!it.hasNext())
            System.out.println("No students!\n");
        else {
            System.out.println("ID    DESCRIERE    START    DEADLINE");
            while (it.hasNext()) {
                HomeWork hm = it.next();
                System.out.println(hm.getId()+"    "+hm.getDescriere()+"  "+hm.getStartWeek()+"     "+hm.getDeadlineWeek());
            }
        }
    }

    private void getAllStudents() {
        Collection<Student> col = serv.getAllStudents();
        Iterator<Student> it = col.iterator();
        if(!it.hasNext())
            System.out.println("No students!\n");
        else {
            System.out.println("ID    NUME    PRENUME    GRUPA    EMAIL    PROFESOR");
            while (it.hasNext()) {
                Student st = it.next();
                System.out.println(st.getId()+"    "+st.getNume()+"   "+st.getPrenume()+
                        "    "+st.getGrp()+"     "+st.getEmail()+"   "+st.getCadruDidacticIndrumatorLab());
            }
        }
    }


    private void removeHomeWork() throws ServiceException {
        int id;
        System.out.print("Homework id:");
        id = Integer.parseInt(scanner.nextLine());
        serv.removeHomeWork(id);
    }

    private void removeStudent() throws ServiceException {
        int id;
        System.out.print("Student id:");
        id = Integer.parseInt(scanner.nextLine());
        serv.removeStudent(id);
    }

    private void updateStudent() throws ValidationException, ServiceException {
        int id,grp;
        String nume,prenume,email,cadruDidactic;
        System.out.print("Student id:");
        id = Integer.parseInt(scanner.nextLine());
        System.out.println("Nume:");
        nume = scanner.nextLine();
        System.out.println("Prenume:");
        prenume = scanner.nextLine();
        System.out.println("grupa:");
        grp = Integer.parseInt(scanner.nextLine());
        System.out.println("email:");
        email = scanner.nextLine();
        System.out.println("Cadru didactic indrumator lab:");
        cadruDidactic = scanner.nextLine();
        serv.updateStudent(id,grp,nume,prenume,email,cadruDidactic);
    }

    private void addStudent() throws ServiceException, ValidationException {
        int id,grp;
        String nume,prenume,email,cadruDidactic;
        System.out.print("Student id:");
        id = Integer.parseInt(scanner.nextLine());
        System.out.println("Nume:");
        nume = scanner.nextLine();
        System.out.println("Prenume:");
        prenume = scanner.nextLine();
        System.out.println("grupa:");
        grp = Integer.parseInt(scanner.nextLine());
        System.out.println("email:");
        email = scanner.nextLine();
        System.out.println("Cadru didactic indrumator lab:");
        cadruDidactic = scanner.nextLine();
        serv.addStudent(id,grp,nume,prenume,email,cadruDidactic);
    }

}
