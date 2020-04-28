package Domain;

import java.time.LocalDateTime;

public class Grade extends Entity<String> {
    private LocalDateTime data;
    private String teacher;
    private float grade;
    private int idStudent;
    private int idHomeWork;

    public Grade(int idStudent,int idHomeWork, LocalDateTime data, String teacher,float grade) {
        super.setId(idStudent + ":" + idHomeWork);
        this.data = data;
        this.teacher = teacher;
        this.grade = grade;
        this.idStudent = idStudent;
        this.idHomeWork = idHomeWork;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public float getGrade(){return grade;}

    public void setGrade(int grade){this.grade = grade;}

    public int getIdHomeWork() {
        return idHomeWork;
    }

    public void setIdHomeWork(int idHomeWork) {
        this.idHomeWork = idHomeWork;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }
}
