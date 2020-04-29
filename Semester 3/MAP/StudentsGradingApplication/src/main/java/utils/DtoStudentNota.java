package utils;

import Domain.Student;

public class DtoStudentNota {
    private Student st;
    private float grade;

    public DtoStudentNota(Student st, float grade) {
        this.st = st;
        this.grade = grade;
    }

    public Student getSt() {
        return st;
    }

    public float getGrade() {
        return grade;
    }
    public String getNumeComplet(){
        return st.getNume()+ " "+st.getPrenume();
    }
}
