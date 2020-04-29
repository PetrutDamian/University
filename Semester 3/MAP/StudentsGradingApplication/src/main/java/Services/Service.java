package Services;

import Domain.Grade;
import Domain.HomeWork;
import Domain.Student;
import Main.SaptamanaCurenta;
import Main.StructuraAnUniversitar;
import Observer.Observable;
import Observer.Observer;
import Repositories.AbstractRepository;
import Validation.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import utils.DtoStudentNota;
import utils.DtoTemaNota;
import utils.EventTypes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service implements Observable {
    private ArrayList<Observer> observers = new ArrayList<>();
    AbstractRepository<Integer, Student> studentRepo;
    AbstractRepository<Integer, HomeWork> homeWorkRepo;
    AbstractRepository<String, Grade> gradeRepo;
    public Service(AbstractRepository<Integer,Student>studentRepo,
                   AbstractRepository<Integer,HomeWork>homeWorkRepo,
                   AbstractRepository<String,Grade>gradeRepo){
        this.studentRepo=studentRepo;
        this.homeWorkRepo=homeWorkRepo;
        this.gradeRepo=gradeRepo;
    }
    /*
    Extends a deadline
    Returns true if successful
            false if homework wasn't found
     Throws ServiceException if trying to modify an already passed deadlineWeek
     */
    public void modifyDeadline(int id,int deadlineWeek) throws ServiceException, ValidationException {
        HomeWork hm = homeWorkRepo.findOne(id);
        if(hm==null)
            throw new ServiceException("Id not found!\n");
        if(hm.getDeadlineWeek()< SaptamanaCurenta.getCurent())
            throw new ServiceException("Deadline already passed!\n");
        int oldDeadline = hm.getDeadlineWeek();
        if(oldDeadline>deadlineWeek)
            throw new ServiceException("Deadline can only be extended!\n");
        homeWorkRepo.update(new HomeWork(id,hm.getDescriere(),hm.getStartWeek(),deadlineWeek));
        notifyObservers(EventTypes.HOMEWORK);
    }

    public void addHomeWork(int id,String des,int deadlineWeek ) throws ValidationException, ServiceException {
        int startWeek = SaptamanaCurenta.getCurent();
        HomeWork hm = new HomeWork(id,des,startWeek,deadlineWeek);
        if(homeWorkRepo.save(hm)!=null)
            throw new ServiceException("Id already exists!\n");
        notifyObservers(EventTypes.HOMEWORK);
    }
    private void writeToXml(Document doc, int idSt,int idHm,String teacher,float grade,String feedback,String filename){
        Element el = doc.getDocumentElement();
        String name = studentRepo.findOne(idSt).getNume();
        String week = String.valueOf(SaptamanaCurenta.getCurent());
        String gradeString = String.valueOf(grade);
        String deadline = String.valueOf(homeWorkRepo.findOne(idHm).getDeadlineWeek());
        el.appendChild(formNode(doc,String.valueOf(idHm),gradeString,week,deadline,feedback));

        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            DOMSource ds = new DOMSource(doc);
            StreamResult sRes = new StreamResult(new File(filename));
            Transformer transformer = tf.newTransformer();
            transformer.transform(ds, sRes);
        }catch (TransformerException ex){
            ex.printStackTrace();
        }
    }

    private Node formNode(Document doc,  String idHm, String gradeString, String week, String deadline, String feedback) {
        Element grade = doc.createElement("grade");

        grade.appendChild(formSubnode(doc,"tema",idHm));
        grade.appendChild(formSubnode(doc,"nota",gradeString));
        grade.appendChild(formSubnode(doc,"predata",week));
        grade.appendChild(formSubnode(doc,"deadline",deadline));
        grade.appendChild(formSubnode(doc,"feedback",feedback));
        return grade;
    }

    private Node formSubnode(Document doc, String tag, String value) {
        Element node =doc.createElement(tag);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    public void addGrade(int idStudent, int idHomeWork,String teacher,float grade,boolean motivat,int nrIntarziere,String feedback) throws ServiceException, ValidationException {
        String key =idStudent+ ":" + idHomeWork;
        if (gradeRepo.findOne(key) != null) {
            throw new ServiceException("Nota existenta!\n");
        }
        HomeWork hm = homeWorkRepo.findOne(idHomeWork);
        if(grade<1 || grade >10)
            throw new ServiceException("Nota invalida! Nota trebuie sa fie intre 1 si 10!\n");
        if (hm == null)
            throw new ServiceException("Homework id not found!\n");
        if (studentRepo.findOne(idStudent) == null)
            throw new ServiceException("Student id not found!\n");
        if(teacher.equals(""))
            throw new ServiceException("Teacher invalid!\n");
        LocalDateTime date = LocalDateTime.now();
        if(!motivat){
            if(nrIntarziere>=0)
            {
                date = SaptamanaCurenta.getDate(nrIntarziere+hm.getDeadlineWeek());
                grade = Math.max(1,grade-nrIntarziere);
            }
            else{
                nrIntarziere = SaptamanaCurenta.getCurent()-hm.getDeadlineWeek();
                if(nrIntarziere>2)
                    throw new ServiceException("Tema nu se mai poate preda!");
                grade = grade - (Math.max(0,nrIntarziere));
            }
        }
        gradeRepo.save(new Grade(idStudent, idHomeWork, date, teacher, grade));
        notifyObservers(EventTypes.GRADE);
        String filename = "D:/JavaProjects/MapGui/src/main/java/StudentsInfo/"+
                studentRepo.findOne(idStudent).getNume() +idStudent+".xml";
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document d = dBuilder.parse(new File(filename));
            writeToXml(d,idStudent,idHomeWork,teacher,grade,feedback,filename);

        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //prima nota a studentului
            try {
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                Element el = doc.createElement("Student");
                doc.appendChild(el);
                writeToXml(doc,idStudent,idHomeWork,teacher,grade,feedback,filename);
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace();
            }
        }
    }
    public float[] previewAddGrade(int idStudent, int idHomeWork,String teacher,float grade,boolean motivat,int nrIntarziere) throws ServiceException {
        String key = idStudent + ":" + idHomeWork;
        if (gradeRepo.findOne(key) != null) {
            throw new ServiceException("Studentul are deja o nota asignata la aceasta tema!\n");
        }
        if(grade<1 || grade >10)
            throw new ServiceException("Nota invalida! Nota trebuie sa fie intre 1 si 10!\n");
        HomeWork hm = homeWorkRepo.findOne(idHomeWork);
        if (teacher.equals(""))
            throw new ServiceException("Nu ati introdus numele profesorului!\n");
        LocalDateTime date = LocalDateTime.now();
        if (!motivat) {
            if (nrIntarziere >= 0) {
                date = SaptamanaCurenta.getDate(nrIntarziere + hm.getDeadlineWeek());
                grade = Math.max(1, grade - nrIntarziere);
            } else {
                nrIntarziere = SaptamanaCurenta.getCurent() - hm.getDeadlineWeek();
                if (nrIntarziere > 2)
                    throw new ServiceException("Intarziere > 2 saptamani! Tema nu se mai poate preda!");
                grade = grade - (Math.max(0, nrIntarziere));
            }
        }
        return new float[]{grade, nrIntarziere};
    }
    public void addStudent(int id, int grp, String nume, String prenume, String email, String cadruDidactic) throws ServiceException, ValidationException {
        Student st = new Student(id,nume,prenume,grp,email,cadruDidactic);
        if(studentRepo.save(st)!=null)
            throw new ServiceException("Id already exists!\n");
        notifyObservers(EventTypes.STUDENT);
    }

    public void updateStudent(int id, int grp, String nume, String prenume, String email, String cadruDidactic) throws ValidationException, ServiceException {
        Student st = new Student(id,nume,prenume,grp,email,cadruDidactic);
        if(studentRepo.update(st)!=null)
            throw new ServiceException("Id not found!\n");
        notifyObservers(EventTypes.STUDENT);
    }

    public void removeStudent(int id) throws ServiceException {
        Student st = studentRepo.delete(id);
        if(st==null)
            throw new ServiceException("Id not found!\n");
        notifyObservers(EventTypes.STUDENT);
        Predicate<Grade> mypred = x->x.getIdStudent()==id;
        Iterator<Grade> it = gradeRepo.findAll().iterator();
        ArrayList<String> keys = new ArrayList<String>();
        int index=0;
        while(it.hasNext()){
            Grade g = it.next();
            if(mypred.test(g)){
                keys.add(index,String.valueOf(id)+":"+String.valueOf(g.getIdHomeWork()));
                index++;
            }
        }
        for(String key:keys)
            gradeRepo.delete(key);
        notifyObservers(EventTypes.GRADE);
    }

    public void removeHomeWork(int id) throws ServiceException {
        HomeWork hm = homeWorkRepo.delete(id);
        if(hm==null)
            throw new ServiceException("Id not found!\n");
        notifyObservers(EventTypes.HOMEWORK);
        Predicate<Grade> mypred = x->x.getIdHomeWork()==id;
        Iterator<Grade> it = gradeRepo.findAll().iterator();
        ArrayList<String> keys = new ArrayList<String>();
        int index=0;
        while(it.hasNext()){
            Grade g = it.next();
            if(mypred.test(g)){
                keys.add(index,String.valueOf(g.getIdStudent())+":"+String.valueOf(id));
                index++;
            }
        }
        for(String key:keys)
            gradeRepo.delete(key);
        notifyObservers(EventTypes.GRADE);
    }

    public Collection<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Collection<HomeWork> getAllHomeWorks() {
        return homeWorkRepo.findAll();
    }

    public List<Student> filterStudentsByGroup(int grp) {
            return studentRepo.findAll().stream().filter(x->x.getGrp()==grp).collect(Collectors.toList());
    }

    public List<Student> filterStudentsByHm(int hmId) {
        Collection<Grade> allGrades = gradeRepo.findAll();
        Predicate<Student> hasHm = x->{return (allGrades.contains(gradeRepo.findOne(x.getId()+":"+hmId)));};
        return studentRepo.findAll().stream().filter(hasHm).collect(Collectors.toList());
    }

    public List<Student> filterStByHmAndTeacher(int hmId, String teacher) {
        Collection<Grade> allGrades = gradeRepo.findAll();
        Predicate<Student> hasHm = x->{
            return (allGrades.contains(gradeRepo.findOne(x.getId()+":"+hmId))); };
        Predicate<Student> teacherPred = x->{
            return (gradeRepo.findOne(x.getId()+":"+hmId).getTeacher().equals(teacher));
        };
        return studentRepo.findAll().stream().filter(hasHm.and(teacherPred)).collect(Collectors.toList());
    }

    public List<Grade> filterGrades(int hmId, int week) {
        Predicate<Grade> temaP = x->{return x.getIdHomeWork()==hmId;};
        Predicate<Grade> weekP = x->{
            LocalDateTime date = x.getData();
            LocalDateTime[] dates = StructuraAnUniversitar.getWeeks();
            int wk=1;
            for(LocalDateTime ldate:dates)
                if(date.isAfter(ldate))
                    wk++;
            return wk==week;
           };
        return gradeRepo.findAll().stream().filter(temaP.and(weekP)).collect(Collectors.toList());
    }
    public ArrayList<DtoStudentNota> raportStudentNota(){
        int nrSaptamani=0;

        //List<HomeWork> hms = homeWorkRepo.findAll().stream().filter(h->h.getDeadlineWeek()<SaptamanaCurenta.getCurent()).collect(Collectors.toList());
        Collection<HomeWork> hms = homeWorkRepo.findAll();
        for(HomeWork h:hms)
            nrSaptamani+=(h.getDeadlineWeek()-h.getStartWeek()+1);
        ArrayList<DtoStudentNota> raport = new ArrayList<>();
        for(Student s:studentRepo.findAll()){
            float sum=0;
            for(HomeWork h:hms)
            {
                float grade = 1;
                Grade g = gradeRepo.findOne(s.getId()+":"+h.getId());
                if(g!=null)
                    grade = g.getGrade();
                grade= grade * (h.getDeadlineWeek()-h.getStartWeek()+1)/nrSaptamani;
                sum+=grade;
            }
            raport.add(new DtoStudentNota(s,sum));
        }
    return raport;
    }
    public ArrayList<DtoTemaNota> raportTemaNota(){
        //Collection<HomeWork> hms = homeWorkRepo.findAll();
        ArrayList<DtoTemaNota> raport = new ArrayList<>();

        List<HomeWork> hms = homeWorkRepo.findAll().stream().filter(h->h.getDeadlineWeek()<SaptamanaCurenta.getCurent()).collect(Collectors.toList());

        for(HomeWork h:hms){
            float sum=0;
            for(Student st:studentRepo.findAll()){
                Grade g = gradeRepo.findOne(st.getId()+":"+h.getId());
                float grade = 1;
                if(g!=null)
                    grade = g.getGrade();
                sum+=grade;
            }
            sum=sum/studentRepo.findAll().size();
            raport.add(new DtoTemaNota(h,sum));
        }
        return raport;
    }
    public ArrayList<DtoStudentNota> raportStudentPredate(){
        List<HomeWork> filtered = homeWorkRepo.findAll().stream().filter(h->h.getDeadlineWeek()<SaptamanaCurenta.getCurent()).collect(Collectors.toList());
        ArrayList<DtoStudentNota> raport = new ArrayList<>();
        for(Student st:studentRepo.findAll()) {
            float nrIntarziate = 0;
            for (HomeWork hm : filtered) {
                Grade g = gradeRepo.findOne(st.getId() + ":" + hm.getId());
                if (g == null)
                    nrIntarziate++;
                else {
                    LocalDateTime data = g.getData();
                    int week = StructuraAnUniversitar.getCorrespondingWeek(data);
                    if (week > hm.getDeadlineWeek())
                        nrIntarziate++;
                }
            }
            raport.add(new DtoStudentNota(st,nrIntarziate));
        }
            return raport;
    }

    public Collection<Grade> getAllGrades() {
        return gradeRepo.findAll();
    }

    @Override
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
    observers.remove(obs);
    }

    @Override
    public void notifyObservers(EventTypes e) {
    observers.stream().forEach(x->x.update(e));
    }
}
