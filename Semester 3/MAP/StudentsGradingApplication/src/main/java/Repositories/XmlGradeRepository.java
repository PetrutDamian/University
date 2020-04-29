package Repositories;


import Domain.Grade;
import Validation.ValidationException;
import Validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class XmlGradeRepository extends AbstractXMLRepository<String, Grade>{
    public XmlGradeRepository(String filename, Validator<Grade> val) throws ValidationException {
        super(filename, val);
    }

    @Override
    protected void writeToFile(String filename) {
        try {
            DocumentBuilder db = this.factory.newDocumentBuilder();

            Document doc = db.newDocument();
            Element el = doc.createElement("grades");

            doc.appendChild(el);
            for (Grade grade:
                    super.findAll()) {
                el.appendChild(getNode(doc,grade.getIdStudent(),grade.getIdHomeWork(),grade.getGrade(),grade.getTeacher(),
                        grade.getData()));
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            DOMSource ds = new DOMSource(doc);
            StreamResult sRes = new StreamResult(new File(this.filename));
            Transformer transformer = tf.newTransformer();
            transformer.transform(ds, sRes);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private Node getNode(Document doc, int idStudent, int idHomeWork, float nota, String teacher, LocalDateTime date){
        Element grade = doc.createElement("grade");

        grade.appendChild(getSubnode(doc, "idStudent", String.valueOf(idStudent)));
        grade.appendChild(getSubnode(doc,"idHomeWork",String.valueOf(idHomeWork)));
        grade.appendChild(getSubnode(doc, "nota", String.valueOf(nota)));
        grade.appendChild(getSubnode(doc, "teacher", teacher));
        grade.appendChild(getSubnode(doc, "data", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        return grade;
    }
    @Override
    protected void loadFromFile(String filename) throws ValidationException {
        try {
            DocumentBuilder db = this.factory.newDocumentBuilder();
            Document doc = db.parse(new File(this.filename));
            Element el =  doc.getDocumentElement();
            NodeList nodeList = el.getElementsByTagName("grade");

            for(int i = 0; i<nodeList.getLength(); i++){
                int idStudent,idHomeWork;
                float nota;
                String teacher;
                LocalDateTime date;

                Element item = (Element) nodeList.item(i);

                NodeList nodeList1 = item.getElementsByTagName("idStudent");
                idStudent= Integer.parseInt(nodeList1.item(0).getChildNodes().item(0).getNodeValue());

                nodeList1 = item.getElementsByTagName("idHomeWork");
                idHomeWork = Integer.parseInt(nodeList1.item(0).getChildNodes().item(0).getNodeValue());

                nodeList1 = item.getElementsByTagName("teacher");
                teacher = nodeList1.item(0).getChildNodes().item(0).getNodeValue();

                nodeList1 = item.getElementsByTagName("nota");
                nota = Float.parseFloat(nodeList1.item(0).getChildNodes().item(0).getNodeValue());

                nodeList1 = item.getElementsByTagName("data");
                date = LocalDateTime.parse(nodeList1.item(0).getChildNodes().item(0).getNodeValue(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                Grade gr = new Grade(idStudent,idHomeWork,date,teacher,nota);
                super.save(gr);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }
}
