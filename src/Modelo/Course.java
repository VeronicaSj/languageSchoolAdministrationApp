package Modelo;

/**
 * Modelo de curso
 * @author veron
 */
public class Course {
    String id;
    String name;
    int examCount;

    /**
     * Constructor de alumno
     * @param id id del alumno
     * @param name nombre del alumno
     */
    /**
     * Constructor de curso
     * @param id id del curso
     * @param name nombre del curso
     * @param examCount cantidad de examenes del curso
     */
    public Course(String id, String name, int examCount) {
        this.id = id;
        this.name = name;
        this.examCount=examCount;
    }
    
    /**
     * Constructor de curso
     * @param id id del curso
     */
    public Course(String id) { this.id = id; }
    
    
    /**
     * getter del id del curso
     * @return el id del curso
     */
    public String getId() { return id; }
    
    /**
     * getter del nombre del curso
     * @return el nombre del curso
     */
    public String getName() { return name; }
    
    /**
     * getter de la cantidad de examenes del curso
     * @return cantidad de examenes del curso
     */
    public int getExamCount() { return examCount; }
    
    
}
