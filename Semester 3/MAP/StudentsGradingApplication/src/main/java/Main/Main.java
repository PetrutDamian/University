package Main;
import Services.Service;
import Repositories.*;
import Tests.TestRepo;
import UserInterface.Console;
import Validation.GradeValidator;
import Validation.HomeWorkValidator;
import Validation.StudentValidator;
import Validation.ValidationException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws ValidationException, IOException, ParserConfigurationException, SAXException {


        //TestRepo tester = new TestRepo();
        //tester.testAll();
        StudentValidator studVal = new StudentValidator();
        HomeWorkValidator homeVal = new HomeWorkValidator();
        GradeValidator gradeVal = new GradeValidator();


        XmlStudentRepository studRepo = new XmlStudentRepository("D:/JavaProjects/MapGui/src/main/java/XmlRepos/XmlStudentRepo.xml",studVal);
        XmlHomeWorkRepository homeRepo = new XmlHomeWorkRepository("D:/JavaProjects/MapGui/src/main/java/XmlRepos/XmlHomeWorkRepo.xml",homeVal);
        XmlGradeRepository gradeRepo = new XmlGradeRepository("D:/JavaProjects/MapGui/src/main/java/XmlRepos/XmlGradeRepo.xml",gradeVal);
        //FileStudentRepository studRepo = new FileStudentRepository("StudentFileRepo.csv",studVal);
        //FileHomeWorkRepository homeRepo = new FileHomeWorkRepository("HomeWorkFileRepo.csv",homeVal);
        //FileGradeRepository gradeRepo = new FileGradeRepository("GradeFileRepo.csv",gradeVal);
        //StudentSqlRepo studRepo = new StudentSqlRepo(studVal);
        //HomeWorkSqlRepo homeRepo = new HomeWorkSqlRepo(homeVal);
        //GradeSqlRepo gradeRepo = new GradeSqlRepo(gradeVal);

        Service serv = new Service(studRepo,homeRepo,gradeRepo);
        Console c = new Console(serv);
        c.run();
    }
}
