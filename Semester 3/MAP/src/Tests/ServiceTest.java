package Tests;

import Controller.Service;
import Controller.ServiceException;
import Domain.HomeWork;
import Domain.Student;
import Main.SaptamanaCurenta;
import Repositories.*;
import Validation.GradeValidator;
import Validation.HomeWorkValidator;
import Validation.StudentValidator;
import Validation.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private StudentValidator studVal;
    private HomeWorkValidator homeVal;
    private GradeValidator gradeVal;
    private FileStudentRepository studRepo;
    private FileHomeWorkRepository homeRepo;
    private FileGradeRepository gradeRepo;
    private Service serv;
    @BeforeEach
    void setUp() {
        try{
            studVal = new StudentValidator();
            homeVal = new HomeWorkValidator();
            gradeVal = new GradeValidator();
            studRepo = new FileStudentRepository("StudentTestRepo.csv", studVal);
            homeRepo = new FileHomeWorkRepository("HomeWorkTestRepo.csv", homeVal);
            gradeRepo = new FileGradeRepository("GradeTestRepo.csv", gradeVal);
            serv = new Service(studRepo, homeRepo, gradeRepo);
        }catch(Exception e){e.printStackTrace();assert(false);}

        try(BufferedWriter writer1 = new BufferedWriter(new FileWriter("HomeWorkTestRepo.csv"));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("StudentTestRepo.csv"));
            BufferedWriter writer3 = new BufferedWriter(new FileWriter("GradeTestRepo.csv"));
        ){}
        catch(IOException ex){ex.printStackTrace();assert(false);}
    }

    @AfterEach
    void tearDown() {
        try(BufferedWriter writer1 = new BufferedWriter(new FileWriter("HomeWorkTestRepo.csv"));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("StudentTestRepo.csv"));
            BufferedWriter writer3 = new BufferedWriter(new FileWriter("GradeTestRepo.csv"));
        ){}
        catch(IOException ex){ex.printStackTrace();assert(false);}
    }
    @Test
    void filter(){
        try {
            XmlStudentRepository sRepo = new XmlStudentRepository("D:/JavaProjects/map_lab4/src/XmlRepos/XmlStudentTestRepo.xml",studVal);
            XmlHomeWorkRepository hmRepo = new XmlHomeWorkRepository("D:/JavaProjects/map_lab4/src/XmlRepos/XmlHomeWorkTestRepo.xml",homeVal);
            XmlGradeRepository gRepo = new XmlGradeRepository("D:/JavaProjects/map_lab4/src/XmlRepos/XmlGradeTestRepo.xml",gradeVal);
            Service serv = new Service(sRepo,hmRepo,gRepo);
            List<Student> l = serv.filterStudentsByGroup(220);
            assert(l.size()==2);
            assert(l.get(0).getGrp()==220 && l.get(1).getGrp()==220);
        } catch (ValidationException e) {
            e.printStackTrace();assert(false);
        }
    }
    @Test
    void modifyDeadline() {
        try {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter("HomeWorkTestRepo.csv"))) {
                writer.append("1,descriere,1,13\n2,des,1,2\n3,des,1,13\n");
                writer.flush();
            }
            FileStudentRepository studRepo = new FileStudentRepository("StudentTestRepo.csv", studVal);
            FileHomeWorkRepository homeRepo = new FileHomeWorkRepository("HomeWorkTestRepo.csv", homeVal);
            FileGradeRepository gradeRepo = new FileGradeRepository("GradeTestRepo.csv", gradeVal);
            Service serv = new Service(studRepo, homeRepo, gradeRepo);
            serv.modifyDeadline(1,14);
            try {
                serv.modifyDeadline(1, 10);
                assert (false);
            }catch(ServiceException ex){ assert(ex.getError().equals("Deadline can only be extended!\n")); }
            try{
                serv.modifyDeadline(2,6);
                assert(false);
            }catch (ServiceException ex){assert(ex.getError().equals("Deadline already passed!\n"));}
            try{
                serv.modifyDeadline(5,6);
                assert(false);
            }catch(ServiceException ex){assert(ex.getError().equals("Id not found!\n"));}
            try(BufferedReader reader = new BufferedReader(new FileReader("HomeWorkTestRepo.csv"))) {
                assert (reader.readLine().equals("1,descriere,1,14"));
                assert (reader.readLine().equals("2,des,1,2"));
                assert (reader.readLine().equals("3,des,1,13"));
            }
        }catch(Exception e){e.printStackTrace();assert(false);}
    }
    @Test
    void addHomeWork() {
        try{
            serv.addHomeWork(1,"description",14);
            serv.addHomeWork(2,"description2",14);
            try{
                serv.addHomeWork(1,"des",12);
                assert(false);
            }catch(ServiceException ex){assert(ex.getError().equals("Id already exists!\n"));}
            try{
                serv.addHomeWork(5,"qwe",-4);
                assert(false);
            }catch(ValidationException ex){assert(ex.getError().equals("Saptamana invalida!\nStart week >= deadline week!\n"));}
            try(BufferedReader reader = new BufferedReader(new FileReader("HomeWorkTestRepo.csv"))){
                String crt = String.valueOf(SaptamanaCurenta.getCurent());
                assert(reader.readLine().equals("1,description,"+crt+",14"));
                assert(reader.readLine().equals("2,description2,"+crt+",14"));
            }
        }catch(Exception e){e.printStackTrace();assert(false);}

    }
    @Test
    void crudOperations(){
        try {
            serv.addStudent(1, 200, "Zidan", "Carmin", "email", "cadru");
            serv.addStudent(2, 210, "Meri", "Sadu", "email", "cadru");


            try{
                serv.addStudent(1,421,"dsa","dwq","dq","dwq");
                assert(false);
            }catch (ServiceException ex){ assert ex.getError().equals("Id already exists!\n");}

            try {
                serv.addStudent(-1, 321, "dwq", "dwqe", "dwq", "dwqd");
                assert(false);
            }catch (ValidationException ex){assert ex.getError().equals("Id invalid!\n");}

            serv.updateStudent(1,300,"q","w","email","cadru");
            try{
                serv.updateStudent(10,4321,"dqw","ewq","dqw","dwq");
                assert(false);
            } catch (ServiceException ex){assert ex.getError().equals("Id not found!\n");}
            serv.removeStudent(2);


            serv.addHomeWork(1,"des1",14);
            serv.addHomeWork(2,"des2",14);
            try{
                serv.addHomeWork(1,"des321",12);
                assert(false);
            }catch(ServiceException ex){assert ex.getError().equals("Id already exists!\n");}
            try{
                serv.addHomeWork(4,"des",20);
                assert(false);
            }catch(ValidationException ex){assert ex.getError().equals("Saptamana invalida!\n");}
            serv.removeHomeWork(1);

            Iterator<Student> it = serv.getAllStudents().iterator();
            Student st = it.next();
            assert(st.getId()==1 && st.getNume().equals("q") && st.getGrp()==300 && st.getPrenume().equals("w"));
            assert(!it.hasNext());
            Iterator<HomeWork> it2 = serv.getAllHomeWorks().iterator();
            HomeWork hm = it2.next();
            assert(hm.getId()==2 && hm.getDescriere().equals("des2")&& hm.getDeadlineWeek()==14
                    && hm.getStartWeek()==SaptamanaCurenta.getCurent());
            assert(!it.hasNext());
        }catch (Exception e){e.printStackTrace();assert(false);}
    }
    @Test
    void addGrade() {
        try{
            studRepo.save(new Student(1,"Iontest","Marius",200,"ion@yahoo.com","Marcel"));
            homeRepo.save(new HomeWork(1,"description",2,14));
            homeRepo.save(new HomeWork(2,"description2",1,2));
            homeRepo.save(new HomeWork(3,"des",1,SaptamanaCurenta.getCurent()-1));
            homeRepo.save(new HomeWork(4,"des",1,SaptamanaCurenta.getCurent()-2));

            serv.addGrade(1,1,"teacher",10,0,"feedback1");
            try {
                serv.addGrade(1, 2, "teacher", (float) 9.5, 0, "feedback1");
                assert(false);
            }catch (ServiceException ex){assert(ex.getError().equals("Tema nu mai poate fi predata!\n"));}
            serv.addGrade(1,3,"teacher",9,0,"feedback1");
            serv.addGrade(1,4,"teacher",8,2,"feedback1");
            try{
                serv.addGrade(1,1,"dqw",4,1,"feedback1");
                assert(false);
            }catch(ServiceException ex){assert(ex.getError().equals("Nota existenta!\n"));}
            try(BufferedReader reader = new BufferedReader(new FileReader("GradeTestRepo.csv"))){
                String date = String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                assert(reader.readLine().equals("1,1,"+date+",teacher,10.0"));
                assert(reader.readLine().equals("1,3,"+date+",teacher,8.0"));
                assert(reader.readLine().equals("1,4,"+date+",teacher,8.0"));
            }
            try(BufferedWriter writer = new BufferedWriter(new FileWriter("D:/JavaProjects/map_lab3/src/StudentsInfo/Iontest1.txt"))){
            }catch (Exception ex){ ex.printStackTrace();assert(false); }
        }catch(Exception e){e.printStackTrace();assert(false);}
    }
}