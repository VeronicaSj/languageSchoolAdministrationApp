package Modelo;

/**
 * Modelo de alumno.
 * @author veron
 */
public class Alumn {
    String id;
    String name;
    
    /**
     * Constructor de alumno
     * @param id id del alumno
     * @param name nombre del alumno
     */
    public Alumn(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * Constructor de alumno
     * @param id id del alumno
     */
    public Alumn(String id){this.id = id;}
    
    /**
     * getter del id del alumno
     * @return el id del alumno
     */
    public String getId() {return id;}
    
    /**
     * getter del nombre del alumno
     * @return el nombre del alumno
     */
    public String getName() {return name;}
    
}
