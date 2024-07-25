package etc;

import Modelo.Alumn;
import Modelo.Course;
import Modelo.Exam;
import Modelo.Registration;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.spi.DirStateFactory.Result;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

/**
 * Exportador de ficheros
 * @author veron
 */
public class FileExporter {
    /**
     * Constructor del exportador de ficheros
     */
    public FileExporter(){}

    /**
     * Funcion que generar un .json donde se  mostrará un boletín de notas de la
     * matricula pasada por parametro
     * @param file File donde queremos introducir el json
     * @param reg matricula de la vamos a hacer el boletin
     * @param examList lista de examenes de los que vamos a hacer el boletin
     * @return true si todo ha salido bien. false en caso contrario
     */
    public boolean generateJSON(File file, Registration reg, ArrayList<Exam> examList) {
        boolean res = false;
        Gson gson = new Gson();
        FileWriter fw = null;
        JsonWriter jw = null;
        try {
            if(!file.exists()){
                file.createNewFile();
                fw = new FileWriter(file);
                jw = gson.newJsonWriter(fw);
                auxJsonWriter(jw, reg, examList);
                res = true;
            }
        } catch (IOException ex) {res = false;
        } finally { //finalmente liveramos los recursos
            try { 
                if (jw != null) { jw.close(); } 
                if (fw != null) { fw.close(); } 
            } catch (IOException ex) { }
        }
        return res;
    }
    
    /**
     * Funcion auxiliar que hace todas las escrituras del json.
     * Esta funcion nos ayuda a tener el codigo mejor organizado. 
     * @param jw writer 
     * @param reg matricula de la vamos a hacer el boletin
     * @param examList lista de examenes de los que vamos a hacer el boletin
     * @throws IOException lanzamos la excepcion para controlarla con más 
     * facilidad allí donde llamemos a esta funcion
     */
    private void auxJsonWriter(JsonWriter jw, Registration reg, 
            ArrayList<Exam> examList) throws IOException{
        jw.beginObject();
            jw.name("idAlumn"); jw.value(reg.getAlumn().getId());
            jw.name("idCourse"); jw.value(reg.getCourse().getId());
            jw.name("average"); jw.value(reg.getAverage());
            jw.name("ExamList");
            jw.beginArray();
            for (int i = 0; i < examList.size(); i++) {
                jw.beginObject();
                    jw.name("examId"); jw.value(examList.get(i).getExamId());
                    jw.name("date"); jw.value(examList.get(i).getDate().toString());
                    jw.name("calification"); jw.value(examList.get(i).getCalification());
                jw.endObject();
            }
            jw.endArray();
        jw.endObject();
    }
    
    /* EJEMPLO DEL RESULTADO QUE QUERÍA OBTENER AL HACER EL JSON
    {
        "alumno":"002", 
        "curso":"I002", 
        "notaMedia":"0.0", 
        "examenes":[
            {
                "numExamen":"1", "fecha":"2023-05-15", "nota":"0.0"
            },{
                "numExamen":"2", "fecha":"2023-05-15", "nota":"0.0"
            }
        ]
    }
    */
    
    
    /**
     * Funcion que genera  un fichero XML donde se mostrarán los datos de los 
     * alumnos con los cursos en los que está matriculado cada uno de ellos.
     * @param file File donde vamos a crear el xml
     * @param alumnList lista de alumnos
     * @param coursesEachAlumn diccionario con la lista de cursos de cada alumno
     * @return true si todo ha ido bien
     */
    public boolean generateXML(File file, ArrayList<Alumn> alumnList, 
            HashMap<String, ArrayList<Course>> coursesEachAlumn){
        boolean res = false;
        Transformer xformer;
        Source source;
        StreamResult result;
        Element root;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        try {
            //instanciamos el documento que vamos a formar
            Document doc = factory.newDocumentBuilder().newDocument();

            root = doc.createElement("Alumns");//creamos el nodo raiz
            
            //editamos el nodo raiz. insertamos cada alumno con sus cursos
            //recorremos la lista de alumnos
            for (int i = 0; i < alumnList.size(); i++) {
                Node aluNode = doc.createElement("Alumn");//creamos nodo alumn
                
                //creamos nodo idalumn
                Node idAluNode = doc.createElement("idAlumn");
                //creamos nodo namealumn
                Node nameAluNode = doc.createElement("nameAlumn");
                
                //añadimos el texto a idAluNode y nameAluNode
                idAluNode.appendChild(doc.createTextNode(alumnList.get(i).getId()));
                nameAluNode.appendChild(doc.createTextNode(alumnList.get(i).getName()));
                
                //añadimos los nodos idAluNode y nameAluNode al nodo alumnos
                aluNode.appendChild(idAluNode);
                aluNode.appendChild(nameAluNode);
                
                Node coursesNode = doc.createElement("courses");//creamos nodo cursos
                //recogemos la lista de cursos de cada alumno en cada vuelta
                ArrayList<Course> list = coursesEachAlumn.get(alumnList.get(i).getId());
                //rellenamos coursesNode con su lista de cursos
                createApendCourses(doc, coursesNode, list); 
                
                //añadimos coursesNode a el alumno
                aluNode.appendChild(coursesNode); 
                
                //finalmente añadimos el alumno a la lista de alumnos
                root.appendChild(aluNode);
            }
            
            doc.appendChild(root);//damos contexto a la lista de alumnos
            
            //instanciamos el transformer
            xformer = TransformerFactory.newInstance().newTransformer();
            
            //Propiedades del fichero XML de salida
            xformer.setOutputProperty(OutputKeys.METHOD, "xml");
            xformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            
            // Definimos la Entrada y la Salida de la Transformacion
            source = new DOMSource(doc);
            result = new StreamResult(file);

            // Realizamos la Transformación mediante el metodo transform()
            xformer.transform(source, result);
            res = true; //si todo va bien retornamos true
        } catch (ParserConfigurationException ex) { res = false;
        } catch (TransformerConfigurationException ex) {res = false;
        } catch (TransformerException ex) {res = false;
        }
        return res;
    }
    
    /**
     * funcion auxiliar que rellena un nodo con la lista de cursos dada y lo 
     * retorna
     * @param doc contexto del nodo que estamos formando
     * @param coursesNode nodo que vamos a rellenar
     * @param list lista de cursos que vamos a introducir en el nodo
     * @return nodo que contiene la lista de cursos
     */
    private Node createApendCourses(Document doc, Node coursesNode, ArrayList<Course> list){
        for (int i = 0; i < list.size(); i++) {//recorremos lista de cursos
            Node courseNode = doc.createElement("course");//creamso nodo curso
            //creamos los nodos de id, nombre y numero de examenes del curso y les introducimos su texto
            Node idCoursesNode = doc.createElement("idcourses");
            idCoursesNode.appendChild(doc.createTextNode(list.get(i).getId()));
            Node nameCoursesNode = doc.createElement("namecourses");
            nameCoursesNode.appendChild(doc.createTextNode(list.get(i).getName()));
            Node examCountNode = doc.createElement("examCount");
            examCountNode.appendChild(doc.createTextNode(list.get(i).getExamCount()+""));
            
            courseNode.appendChild(idCoursesNode);//añadimos nodo id al curso
            courseNode.appendChild(nameCoursesNode);//añadimos nodo nombre al curso
            courseNode.appendChild(examCountNode);//añadimos nodo numExams al curso
            //añadimos curso al nodo lista de cursos
            coursesNode.appendChild(courseNode);
        }
        return coursesNode;//devolvemos el nodo lista de cursos
    }
    
    /*
    <Alumns>
        <Alumn>
            <id>...</id>
            <name>...</name>
            <cursos>
                <curso>
                    <id>...</id>
                    <name>...</name>
                    <examCount>...</examCount>
                </curso>
                <curso>
                    <id>...</id>
                    <name>...</name>
                    <examCount>...</examCount>
                </curso>
            </cursos>
        </Alumn>
        <Alumn>
            <id>...</id>
            <name>...</name>
            <cursos>
                <curso>
                    <id>...</id>
                    <name>...</name>
                    <examCount>...</examCount>
                </curso>
                <curso>
                    <id>...</id>
                    <name>...</name>
                    <examCount>...</examCount>
                </curso>
            </cursos>
        </Alumn>
    </Alumns>
    */
}
