package etc;

import Modelo.Alumn;
import Modelo.Course;
import Modelo.Exam;
import Modelo.Registration;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Acceso a la base de datos
 * @author veron
 */
public class DBAccess {
    private Connection con;
    
    //CONSTANTES PARA FACILITAR ACTUALIZACIONES EN LA BASE DE DATOS
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "AD_TEMA02";
    private static final String DB_PW = "AD_TEMA02";
    private static final String T_ALUMNOS="ALUMNOS";
    private static final String T_ALUMNOS_COL_1_CODIGO="cCodAlu";
    private static final String T_ALUMNOS_COL_2_NOMBRE="cNomAlu";
    private static final String T_CURSOS="CURSOS";
    private static final String T_CURSOS_COL_1_CODIGO="cCodCurso";
    private static final String T_CURSOS_COL_2_NOMBRE="cNomCurso";
    private static final String T_CURSOS_COL_3_NUM_EXA="nNumExa";
    private static final String T_MATRICULAS="MATRICULAS";
    private static final String T_MATRICULAS_COL_1_CODIGO_ALUMN="cCodAlu";
    private static final String T_MATRICULAS_COL_2_CODIGO_CURSO="cCodCurso";
    private static final String T_MATRICULAS_COL_3_NOTA_MEDIA="nNotaMedia";
    private static final String T_EXAMENES="EXAMENES";
    private static final String T_EXAMENES_COL_1_CODIGO_ALUMN="cCodAlu";
    private static final String T_EXAMENES_COL_2_CODIGO_CURSO="cCodCurso";
    private static final String T_EXAMENES_COL_3_NUM_EXA="nNumExam";
    private static final String T_EXAMENES_COL_4_FECHA_EXA="dFecExam";
    private static final String T_EXAMENES_COL_5_NOTA_EXA="nNotaExam";
    
    /**
     * Constructor del acceso a la base de datos. Carga el driver
     */
    public DBAccess() {
        //Llamamos al diver
        try{ Class.forName(DRIVER); } catch (ClassNotFoundException ex) { }
    }
    
    /**
     * Funcion que hace la conexion a la base de datos
     * @return true si la conexion ha sido exitosa. False en caso contrario
     */
    public boolean dbConnect (){
        boolean res = false;
        try{
            //pedimos la conexion al driver
            con=DriverManager.getConnection( DB_URL, DB_USER, DB_PW);
            res = true;//true si la conexion ha sido exitosa
        } catch(SQLException ex){ 
            //si ha habido algun problema conexion a null y retornamos false
            con=null; res = false; }
        return res;
    }
    
    /**
     * Funcion que pide una lista de todos los alumnos de la tabla alumnos
     * @return lista de todos los alumnos. null si ha habido algun error
     */
    public ArrayList<Alumn> getAlumnList(){
        ArrayList<Alumn> res = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            res = new ArrayList<Alumn>();
            st= con.createStatement();//creamos el statement
            rs= st.executeQuery("select * from " + T_ALUMNOS);//hacemos consulta
            //recorremos rs y vamos añadiendo cada resultado a la lista
            while(rs.next()){
                res.add(new Alumn(rs.getNString(T_ALUMNOS_COL_1_CODIGO),
                    rs.getString(T_ALUMNOS_COL_2_NOMBRE)));
            }
        } catch (SQLException ex){ res = null;//si hay error devolvemos null
        } finally { //finalmente liberamos los recursos
            try {
                if(rs != null){ rs.close(); }
                if(st != null){ st.close(); }
            } catch (SQLException ex){}
        }
        return res;
    }
    
    /**
     * Funcion que pide una lista de todos los cursos de la tabla alumnos
     * @return lista de todos los cursos. null si ha habido algun error
     */
    public ArrayList<Course> getCourseList(){
        ArrayList<Course> res = null;
            Statement st = null;
            ResultSet rs = null;
        try {
            res = new ArrayList<Course>();
            st= con.createStatement(); //creamos el statement
            rs= st.executeQuery("select * from "+T_CURSOS);//hacemos consulta
            //recorremos rs y vamos añadiendo cada resultado a la lista
            while(rs.next()){
                res.add(new Course(rs.getString(T_CURSOS_COL_1_CODIGO),
                    rs.getString(T_CURSOS_COL_2_NOMBRE), 
                    rs.getInt(T_CURSOS_COL_3_NUM_EXA)));
            }
        } catch (SQLException ex){ res = null;//si hay error devolvemos null
        } finally { //finalmente liberamos los recursos
            try {
                if(rs != null){ rs.close(); }
                if(st != null){ st.close(); }
            } catch (SQLException ex){}
        }
        return res;
    }
    
    /**
     * Funcion que pide una lista de todas las matriculas de la tabla matriculas
     * @return lista de todas las matriculas. null si ha habido algun error
     */
    public ArrayList<Registration> getRegistrationList(){
        ArrayList<Registration> res = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            res = new ArrayList<Registration>();
            st= con.createStatement(); //creamos el statement
            //hacemos consulta
            rs= st.executeQuery("select al." + T_ALUMNOS_COL_1_CODIGO + ", al." 
                    + T_ALUMNOS_COL_2_NOMBRE + ", " + "c." 
                    + T_CURSOS_COL_1_CODIGO + ", c." + T_CURSOS_COL_2_NOMBRE 
                    + ", m." + T_MATRICULAS_COL_3_NOTA_MEDIA + " from " 
                    + T_MATRICULAS + " m left outer join " + T_ALUMNOS 
                    + " al on m." + T_MATRICULAS_COL_1_CODIGO_ALUMN + " =  al." 
                    + T_ALUMNOS_COL_1_CODIGO + " \nleft outer join " + T_CURSOS 
                    + " c on m." + T_MATRICULAS_COL_2_CODIGO_CURSO + " = "
                    + "c." + T_CURSOS_COL_1_CODIGO + "");
            //recorremos rs y vamos añadiendo cada resultado a la lista
            while(rs.next()){
                res.add(new Registration(new Alumn(rs.getString(
                        T_ALUMNOS_COL_1_CODIGO),  
                        rs.getString(T_ALUMNOS_COL_2_NOMBRE)),
                        new Course(rs.getString(T_CURSOS_COL_1_CODIGO), 
                        rs.getString(T_CURSOS_COL_2_NOMBRE), 0),
                        rs.getInt(T_MATRICULAS_COL_3_NOTA_MEDIA)));
            }
        } catch (SQLException ex){ res = null;//si hay error devolvemos null
        } finally { //finalmente liberamos los recursos
            try {
                if(rs != null){ rs.close(); }
                if(st != null){ st.close(); }
            } catch (SQLException ex){}
        }
        return res;
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
    public ArrayList<Exam> getExamList(String idAlu, String idCourse){
        ArrayList<Exam> res = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            res=new ArrayList<Exam>();
            st= con.createStatement(); //creamos el statement
            //hacemos consulta
            rs= st.executeQuery("select "+T_EXAMENES_COL_3_NUM_EXA
                    + ", "+ T_EXAMENES_COL_4_FECHA_EXA + ", "
                    + T_EXAMENES_COL_5_NOTA_EXA + " from " + T_EXAMENES 
                    + " where "+T_EXAMENES_COL_1_CODIGO_ALUMN + " = '" + idAlu
                    + "' and "+T_EXAMENES_COL_2_CODIGO_CURSO+" = '"+idCourse
                    + "'");
            //recorremos rs y vamos añadiendo cada resultado a la lista
            while(rs.next()){
                res.add(new Exam(rs.getInt(T_EXAMENES_COL_3_NUM_EXA),
                        rs.getDate(T_EXAMENES_COL_4_FECHA_EXA),
                        rs.getFloat(T_EXAMENES_COL_5_NOTA_EXA)));
            }
        } catch (SQLException ex){ res = null;//si hay error devolvemos null
        } finally { //finalmente liberamos los recursos
            try {
                if(rs != null){ rs.close(); }
                if(st != null){ st.close(); }
            } catch (SQLException ex){}
        }
        return res;
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
    public int updateExam(int idExam, String idAlu,String idCourse, 
            float calif, Date date){
        PreparedStatement ps = null;//uso prepSt para evitar sqlInjections
        int res = -1;
        String prepStatment="update " + T_EXAMENES + " set " 
                + T_EXAMENES_COL_4_FECHA_EXA + "= ? , " 
                + T_EXAMENES_COL_5_NOTA_EXA + "= ? where " 
                + T_EXAMENES_COL_1_CODIGO_ALUMN + "= ? and " 
                + T_EXAMENES_COL_2_CODIGO_CURSO + "=? and "
                + T_EXAMENES_COL_3_NUM_EXA + "=?";
        try {
            ps=con.prepareStatement(prepStatment); //creamos el prepStatement
            //rellenamos los parametros del prepSt
            ps.setDate(1, date);
            ps.setFloat(2, calif);
            ps.setString(3, idAlu);
            ps.setString(4, idCourse);
            ps.setInt(5, idExam);
            res = ps.executeUpdate();//ejecutamos el prepEst
        } catch (SQLException ex) { res = -1; //si hay error retornamos -1
        } finally { //finalmente liberamos los recursos
            try { if(ps != null){ ps.close(); } } catch (SQLException ex){}
        }
        return res;
    }
    
    /**
     * Funcion para crear un registro de matricula
     * @param idAlu idntificador del alumno
     * @param idCourse idntificador del curso
     * @return 0 si todo ha ido bien. -1 si ha habido algún error
     */
    public int registerAlumn(String idAlu, String idCourse) {
        int res=0;
        CallableStatement cst = null;
        String proc="{call sp_AltaMatricula(?,?,?)}";
        try{
            cst=con.prepareCall(proc); //creamos el callable
            //establecemso los parametros
            cst.setString(1, idAlu);
            cst.setString(2, idCourse);
            cst.registerOutParameter(3, Types.INTEGER);
            cst.execute();//ejecutamos el callable
            res = cst.getInt(3);//recogemos el parametro de salida
        } catch (SQLException ex) { res=-1; //si hay error retornaremos -1
        } finally { //finalmente liberamos los recursos
            try { if(cst != null){ cst.close(); } } catch (SQLException ex){}
        }
        return res;
    }
    
    /**
     * Funcion para obtener la nota media de un alumno en un curso (matricula)
     * @param idAlu idntificador del alumno
     * @param idCourse idntificador del curso
     * @return nota media
     */
    public int getAvrgCalifcation(String idAlumn, String idCourse){
        int res = -1;
        Statement st = null;
        ResultSet rs = null;
        
        try {
            st= con.createStatement();//creamos el statement
            //ejecutamos el st
            rs= st.executeQuery("select " 
                    + T_MATRICULAS_COL_3_NOTA_MEDIA + " from " + T_MATRICULAS
                    + " where "+T_MATRICULAS_COL_1_CODIGO_ALUMN + " = '" 
                    + idAlumn + "' and "+T_MATRICULAS_COL_2_CODIGO_CURSO+" = '"
                    + idCourse + "'");
            //recorremos el rs y lo guardamos en la variable de retorno.
            while(rs.next()){// Esperamos tener 1 resultado
                res = rs.getInt(T_MATRICULAS_COL_3_NOTA_MEDIA);
            }
        } catch (SQLException ex){ res = -1;//si hay error devolvemos -1
        } finally { //finalmente liberamos los recursos
            try {
                if(rs != null){ rs.close(); }
                if(st != null){ st.close(); }
            } catch (SQLException ex){}
        }
        return res;
    }
    
    
    
    
    
    public ArrayList<Course> getCourseList(String idAlu){
        ArrayList<Course> res = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            res = new ArrayList<Course>();
            st= con.createStatement(); //creamos el statement
            //hacemos consulta
            rs= st.executeQuery("select m." +  T_MATRICULAS_COL_2_CODIGO_CURSO 
                    + ", c." + T_CURSOS_COL_2_NOMBRE + " from " + T_MATRICULAS 
                    + " m left outer join " + T_CURSOS 
                    + " c on m." + T_MATRICULAS_COL_2_CODIGO_CURSO + " = "
                    + "c." + T_CURSOS_COL_1_CODIGO + " where " 
                    + T_MATRICULAS_COL_1_CODIGO_ALUMN + " = '" + idAlu + "'");
            //recorremos rs y vamos añadiendo cada resultado a la lista
            while(rs.next()){
                res.add( new Course(rs.getString(T_CURSOS_COL_1_CODIGO), 
                        rs.getString(T_CURSOS_COL_2_NOMBRE), 0));
            }
        } catch (SQLException ex){ res = null;//si hay error devolvemos null
            System.out.println(ex.getMessage());
        } finally { //finalmente liberamos los recursos
            try {
                if(rs != null){ rs.close(); }
                if(st != null){ st.close(); }
            } catch (SQLException ex){ }
        }
        return res;
    }
    
    
    
    
    /**
     * Funcion que cierra la conexion
     * @return true si la conexion se ha cerrado
     */
    public boolean closeConnection() {
        boolean res=true;
        try{ con.close(); //cerramos la conexion
        }catch(SQLException ex){ res=false; }//controlamos los errores
        return res;
    }
}
