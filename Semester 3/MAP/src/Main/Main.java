package Main;
import Controller.Service;
import Domain.Grade;
import Domain.HomeWork;
import Domain.Student;
import Repositories.*;
import Tests.TestRepo;
import UserInterface.Console;
import Validation.GradeValidator;
import Validation.HomeWorkValidator;
import Validation.StudentValidator;
import Validation.ValidationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws ValidationException, IOException, ParserConfigurationException, SAXException {


        TestRepo tester = new TestRepo();
        tester.testAll();
        StudentValidator studVal = new StudentValidator();
        HomeWorkValidator homeVal = new HomeWorkValidator();
        GradeValidator gradeVal = new GradeValidator();


        XmlStudentRepository studRepo = new XmlStudentRepository("D:/JavaProjects/map_lab4/src/XmlRepos/XmlStudentRepo.xml",studVal);
        XmlHomeWorkRepository homeRepo = new XmlHomeWorkRepository("D:/JavaProjects/map_lab4/src/XmlRepos/XmlHomeWorkRepo.xml",homeVal);
        XmlGradeRepository gradeRepo = new XmlGradeRepository("D:/JavaProjects/map_lab4/src/XmlRepos/XmlGradeRepo.xml",gradeVal);
        //FileStudentRepository studRepo = new FileStudentRepository("StudentFileRepo.csv",studVal);
        //FileHomeWorkRepository homeRepo = new FileHomeWorkRepository("HomeWorkFileRepo.csv",homeVal);
        //FileGradeRepository gradeRepo = new FileGradeRepository("GradeFileRepo.csv",gradeVal);
        //StudentSqlRepo studRepo = new StudentSqlRepo(studVal);
       // HomeWorkSqlRepo homeRepo = new HomeWorkSqlRepo(homeVal);
        //GradeSqlRepo gradeRepo = new GradeSqlRepo(gradeVal);

        Service serv = new Service(studRepo,homeRepo,gradeRepo);
        Console c = new Console(serv);
        c.run();
    }
}
