package Modelo;

import java.sql.Date;

/**
 * Modelo de examen
 * @author veron
 */
public class Exam {
    int examId;
    Date date;
    float calification;

    /**
     * Constructor de examen
     * @param examId identificador del examen
     * @param date fecha del examen
     * @param calification nota del examen
     */
    public Exam(int examId, Date date, float calification) {
        this.examId = examId;
        this.date = date;
        this.calification = calification;
    }

    /**
     * getter del identificador del examen
     * @return identificador del examen
     */
    public int getExamId() {return examId;}
    
    /**
     * getter de la fecha de examen
     * @return fecha de examen
     */
    public Date getDate() {return date;}
    
    /**
     * getter de la nota del examen
     * @return nota del examen
     */
    public float getCalification() {return calification;}
}
