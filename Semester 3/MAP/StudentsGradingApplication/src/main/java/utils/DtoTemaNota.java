package utils;

import Domain.HomeWork;
import Domain.Student;

public class DtoTemaNota {
    private HomeWork hm;
    private float grade;

    public DtoTemaNota(HomeWork hm, float grade) {
        this.hm = hm;
        this.grade = grade;
    }

    public HomeWork getTema() {
        return hm;
    }

    public float getGrade() {
        return grade;
    }
}
