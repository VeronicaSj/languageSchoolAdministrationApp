package Controller;

import Modelo.Alumn;
import Modelo.Course;
import Modelo.Exam;
import Modelo.Registration;
import etc.DBAccess;
import etc.FileExporter;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controlador de la aplicacion
 * @author veron
 */
public class MainController {
    DBAccess dba;
    FileExporter fileHelp;

    /**
     * Constructor del controlador. Inicializa el acceso a base de datos  el 
     * exportador de ficheros
     */
    public MainController() {
        this.dba = new DBAccess();
        this.fileHelp = new FileExporter();
    }
    
    /**
     * Funcion hace connexion a la base de datos
     * @return true si la connexion ha sido exitosa
     */
    public boolean DBConnect (){ return dba.dbConnect(); }
    
    /**
     * Funcion que pide una lista de todos los alumnos de la tabla alumnos
     * @return lista de todos los alumnos. null si ha habido algun error
     */
    public ArrayList<Alumn> getAlumnList(){
        return dba.getAlumnList();
    }
    
    /**
     * Funcion que pide una lista de todos los cursos de la tabla alumnos
     * @return lista de todos los cursos. null si ha habido algun error
     */
    public ArrayList<Course> getCourseList(){
        return dba.getCourseList();
    }
    
    /**
     * Funcion que pide una lista de todas las matriculas de la tabla matriculas
     * @return lista de todas las matriculas. null si ha habido algun error
     */
    public ArrayList<Registration> getRegistrationList(){
        return dba.getRegistrationList();
    }
    
    /**
     * Funcion que pide una lista de los examenes de la tabla examenes que 
     * coincidan con el codigo de alumno y el codigo de alumno pasados por 
     * parametro
     * @param idAlu codigo de alumno
     * @param idCourse codigo de alumno
     * @return lista de los examenes de la tabla examenes que 
     * coincidan con el codigo de alumno y el codigo de alumno pasados por 
     * parametro
     */
    public ArrayList<Exam> getExamList(String idAlumn, String idCourse){
        return dba.getExamList(idAlumn, idCourse);
    }
    
    /**
     * Funcion que actualiza un registro de examen en la base de datos
     * @param idExam identificador del examen
     * @param idAlu identificador del alumno
     * @param idCourse identificador del curso
     * @param calif nueva nota
     * @param date nueva fecha
     * @return cantidad de examenes actualizados. -1 si ha habido algun error
     */
    public int updateExam(int countExam, String idAlumn, String idCourse, 
            float calific, Date date){
        return dba.updateExam(countExam, idAlumn, idCourse, calific, date);
    }
    
    /**
     * Funcion para crear un registro de matricula
     * @param idAlu idntificador del alumno
     * @param idCourse idntificador del curso
     * @return 0 si todo ha ido bien. -1 si ha habido algún error
     */
    public int registerAlumn(String idAlumn, String idCourse) {
        return dba.registerAlumn(idAlumn, idCourse);
    }
    
    /**
     * Funcion para obtener la nota media de un alumno en un curso (matricula)
     * @param idAlu idntificador del alumno
     * @param idCourse idntificador del curso
     * @return nota media
     */
    public int getAvrgCalifcation(String idAlumn, String idCourse){
        return dba.getAvrgCalifcation(idAlumn, idCourse);
    }
    
    /**
     * Funcion que obtiene los cursos en los que está matriculado un alumno a 
     * partir un id de alumno pasado por parametro
     * @param aluList id de alumno 
     * @return cursos en los que está matriculado un alumno
     */
    public ArrayList<Course> getCourseList(String aluList){
        return dba.getCourseList(aluList);
    }
    
    /**
     * Funcion que cierra la conexion
     * @return true si la conexion se ha cerrado
     */
    public boolean closeDbConnection() {
        return dba.closeConnection();
    }
    
    /**
     * Funcion que generar un .json donde se  mostrará un boletín de notas de la
     * matricula pasada por parametro
     * @param file File donde queremos introducir el json
     * @param reg matricula de la vamos a hacer el boletin
     * @return 
     */
    public boolean generateJSON(File file, Registration reg){
        return fileHelp.generateJSON(file, reg, 
                getExamList(reg.getAlumn().getId(),
                        reg.getCourse().getId()));
    }
    
    /**
     * Funcion que genera  un fichero XML donde se mostrarán los datos de los 
     * alumnos con los cursos en los que está matriculado cada uno de ellos.
     * @param file
     * @return true si todo ha ido bien
     */
    public boolean generateXML(File file){
        boolean res = false;
        HashMap<String, ArrayList<Course>> coursesEachAlumn =
                new HashMap<String, ArrayList<Course>>();
        String aluId = "";
        ArrayList<Alumn> aluList = getAlumnList();
        if (aluList != null) {
            for (int i = 0; i < aluList.size(); i++) {
                aluId = aluList.get(i).getId();
                ArrayList<Course> courseList = getCourseList(aluId);
                if (courseList != null) { coursesEachAlumn.put(aluId, courseList);
                }else {
                    coursesEachAlumn = null;
                    break;
                }
            }
            if (coursesEachAlumn != null) {
                res = fileHelp.generateXML(file, aluList,coursesEachAlumn);
            }
        }
        return res;
    }
}
