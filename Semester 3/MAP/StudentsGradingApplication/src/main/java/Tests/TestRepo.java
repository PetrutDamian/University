package Tests;

import Domain.Student;
import Domain.HomeWork;
import Repositories.HomeWorkRepository;
import Repositories.StudentRepository;
import Validation.HomeWorkValidator;
import Validation.StudentValidator;
import Validation.ValidationException;

import java.util.Collection;
import java.util.Iterator;

public class TestRepo {
    public void testAll() throws ValidationException {
        studentTestRepo();
        homeWorkTestRepo();
    }
    public void homeWorkTestRepo() throws ValidationException{


        HomeWorkValidator val = new HomeWorkValidator();
        HomeWorkRepository repo = new HomeWorkRepository(val);
        HomeWork hm1 = new HomeWork(1,"des1",1,4);
        HomeWork hm2 = new HomeWork(1,"des2",3,4);
        HomeWork hm3 = new HomeWork(3,"des3",5,7);
        HomeWork hm4 = new HomeWork(3,"des4",1,3);
        HomeWork hm5 = new HomeWork(5,"des5",20,10);

        repo.save(hm1);
        assert(repo.size()==1);
        HomeWork hm = repo.findOne(1);
        assert(hm.getId()==1);
        assert(hm.getDescriere().equals("des1"));
        assert(hm.getStartWeek()==1);
        assert(hm.getDeadlineWeek()==4);

        try{
            repo.save(null);
            assert(false);
        }catch(IllegalArgumentException ex){}
        assert(repo.save(hm2)==hm2);
        try{
            repo.save(hm5);
            assert(false);
        }catch(ValidationException ex){
            assert (ex.getError().equals("Saptamana invalida!\nStart week >= deadline week!\n"));
        }
        repo.save(hm3);

        repo.update(hm4);
        assert(repo.findOne(3).getDescriere().equals("des4"));
        assert(repo.delete(10)==null);
        repo.delete(3);
        assert(repo.size()==1);

        Iterator<HomeWork> it = repo.findAll().iterator();
        assert(it.next().getDescriere().equals("des1"));
        assert(!it.hasNext());

    }
    public void studentTestRepo() throws ValidationException {
        StudentValidator sVal = new StudentValidator();
        StudentRepository sRepo = new StudentRepository(sVal);
        assert(sRepo.size()==0);
        Student s1 = new Student(1,"Pop1","ion",224,
                "popion@gmail.com","Iancu Mircea");
        Student s2 = new Student(-2,"Pop2","ion",224,
                "popion@gmail.com","Iancu Mircea");
        Student s3 = new Student(1,"Pop3","ion",224,
                "popion@gmail.com","Iancu Mircea");
        Student s4 = new Student(1,"Pop4","ion",224,
                "popion@gmail.com","Iancu Mircea");
        try {
            assert (sRepo.save(s1) == null);
        }catch(ValidationException ex) {assert (false);}

        try {
            sRepo.save(s2);
            assert(false);
        } catch (ValidationException ex) {
            assert(ex.getError().equals("Id invalid!\n"));
        }

        try {
            sRepo.save(null);
            assert(false);
        } catch (IllegalArgumentException | ValidationException ex) {
            assert(ex instanceof IllegalArgumentException);
        }

        assert(sRepo.save(s3) == s3);//acelasi id
        assert(sRepo.update(s4)==null);
        Student s = sRepo.findOne(1);
        assert(s.getNume().equals("Pop4"));
        s = sRepo.delete(1);


        assert(sRepo.size()==0);
        sRepo.save(new Student(10,"a","b",226,"dwq@dsa","pdwq"));
        sRepo.save(new Student(11,"q","b",226,"dwq@dsa","pdwq"));
        Collection<Student> c = sRepo.findAll();
        Iterator<Student> it = c.iterator();
        assert (it.hasNext());
        assert(it.next().getId()==10);
        assert(it.next().getId()==11);
        assert(!it.hasNext());

    }
}


