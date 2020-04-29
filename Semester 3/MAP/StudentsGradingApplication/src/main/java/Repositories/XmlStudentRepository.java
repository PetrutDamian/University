package Repositories;

import Domain.Student;
import Validation.ValidationException;
import Validation.Validator;
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

public class XmlStudentRepository extends AbstractXMLRepository<Integer, Student> {
    public XmlStudentRepository(String filename, Validator<Student> val) throws ValidationException {
        super(filename, val);
    }

    @Override
    protected void writeToFile(String filename) {
        try {
            DocumentBuilder db = this.factory.newDocumentBuilder();

            Document doc = db.newDocument();
            Element el = doc.createElement("students");

            doc.appendChild(el);
            for (Student st:
                    super.findAll()) {
                el.appendChild(getNode(doc, st.getId(), st.getNume(), st.getPrenume(), st.getGrp(),
                        st.getEmail(),st.getCadruDidacticIndrumatorLab()));
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

    private  Node getNode(Document doc, int id, String nume,String prenume, int grp, String email,String cadru){
        Element student = doc.createElement("student");
        student.setAttribute("id", String.valueOf(id));

        student.appendChild(getSubnode(doc, "nume", nume));
        student.appendChild(getSubnode(doc,"prenume",prenume));
        student.appendChild(getSubnode(doc, "grupa", String.valueOf(grp)));
        student.appendChild(getSubnode(doc,"email",email));
        student.appendChild(getSubnode(doc,"cadru",cadru));
        return student;
    }
    @Override
    protected void loadFromFile(String filename) throws ValidationException {
        try {
            DocumentBuilder db = this.factory.newDocumentBuilder();
            Document doc = db.parse(new File(this.filename));
            Element el =  doc.getDocumentElement();
            NodeList nodeList = el.getElementsByTagName("student");

            for(int i = 0; i<nodeList.getLength(); i++){
                int id, grp;
                String nume,prenume, email,cadru;

                Element item = (Element) nodeList.item(i);
                id = Integer.parseInt(item.getAttribute("id"));

                NodeList nodeList1 = item.getElementsByTagName("grupa");
                grp = Integer.parseInt(nodeList1.item(0).getChildNodes().item(0).getNodeValue());

                nodeList1 = item.getElementsByTagName("nume");
                nume = nodeList1.item(0).getChildNodes().item(0).getNodeValue();

                nodeList1 = item.getElementsByTagName("prenume");
                prenume = nodeList1.item(0).getChildNodes().item(0).getNodeValue();

                nodeList1 = item.getElementsByTagName("email");
                email = nodeList1.item(0).getChildNodes().item(0).getNodeValue();

                nodeList1 = item.getElementsByTagName("cadru");
                cadru = nodeList1.item(0).getChildNodes().item(0).getNodeValue();

                Student st = new Student(id,nume,prenume,grp,email,cadru);
                super.save(st);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }
}
