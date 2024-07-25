package Modelo;

/**
 * Modelo de matricula
 * @author veron
 */
public class Registration {
    Alumn alumn;
    Course course;
    int average;

    /**
     * constructor de matricula
     * @param alumn alumno de la matricula
     * @param course curso de la matricula
     * @param average nota media del alumno para esta matricula
     */
    public Registration(Alumn alumn, Course course, int average) {
        this.alumn = alumn;
        this.course = course;
        this.average = average;
    }
    
    /**
     * getter del alumno
     * @return alumno de la matricula
     */
    public Alumn getAlumn() {return alumn;}
    
    /**
     * getter del curso
     * @return curso de la matricula
     */
    public Course getCourse() {return course;}
    
    /**
     * getter de la nota media
     * @return nota media del alumno para esta matricula
     */
    public int getAverage() {return average;}
}
