package Repositories;

import Domain.HomeWork;
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

public class XmlHomeWorkRepository extends AbstractXMLRepository<Integer, HomeWork> {
    public XmlHomeWorkRepository(String filename, Validator<HomeWork> val) throws ValidationException {
        super(filename, val);
    }

    @Override
    protected void writeToFile(String filename) {
        try {
            DocumentBuilder db = this.factory.newDocumentBuilder();

            Document doc = db.newDocument();
            Element el = doc.createElement("homeworks");

            doc.appendChild(el);
            for (HomeWork hm:
                    super.findAll()) {
                el.appendChild(getNode(doc,hm.getId(),hm.getDescriere(),hm.getStartWeek(),hm.getDeadlineWeek()));
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

    private Node getNode(Document doc, int id, String descriere,int startWeek, int deadlineWeek){
        Element homework = doc.createElement("homework");
        homework.setAttribute("id", String.valueOf(id));

        homework.appendChild(getSubnode(doc, "descriere", descriere));
        homework.appendChild(getSubnode(doc,"startWeek",String.valueOf(startWeek)));
        homework.appendChild(getSubnode(doc, "deadlineWeek", String.valueOf(deadlineWeek)));
        return homework;
    }
    @Override
    protected void loadFromFile(String filename) throws ValidationException {
        try {
            DocumentBuilder db = this.factory.newDocumentBuilder();
            Document doc = db.parse(new File(this.filename));
            Element el =  doc.getDocumentElement();
            NodeList nodeList = el.getElementsByTagName("homework");

            for(int i = 0; i<nodeList.getLength(); i++){
                int id,startWeek,deadlineWeek;
                String descriere;

                Element item = (Element) nodeList.item(i);
                id = Integer.parseInt(item.getAttribute("id"));

                NodeList nodeList1 = item.getElementsByTagName("descriere");
                descriere= nodeList1.item(0).getChildNodes().item(0).getNodeValue();

                nodeList1 = item.getElementsByTagName("startWeek");
                startWeek = Integer.parseInt(nodeList1.item(0).getChildNodes().item(0).getNodeValue());

                nodeList1 = item.getElementsByTagName("deadlineWeek");
                deadlineWeek = Integer.parseInt(nodeList1.item(0).getChildNodes().item(0).getNodeValue());

                HomeWork hm = new HomeWork(id,descriere,startWeek,deadlineWeek);
                super.save(hm);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }
}
