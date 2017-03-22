import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

// SAX classes.

public class ToXML {

    BufferedReader in;
    StreamResult out;

    TransformerHandler th;
    AttributesImpl atts;

    String documentReadPath = "D:\\Random\\upload\\data.txt";
    String documetParsePath = "D:\\Random\\upload\\data.xml";


    public void changeToXML() {
        try {
            in = new BufferedReader(new FileReader(documentReadPath));
            out = new StreamResult(documetParsePath);
            initXML();
            String str;
            while ((str = in.readLine()) != null) {
                process(str);
            }
            in.close();
            closeXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initXML() throws ParserConfigurationException,
            TransformerConfigurationException, SAXException {
        // JAXP + SAX
        SAXTransformerFactory tf =
                (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        th = tf.newTransformerHandler();
        Transformer serializer = th.getTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        // pretty XML output
        serializer.setOutputProperty
                ("{http://xml.apache.org/xslt}indent-amount", "4");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        th.setResult(out);
        th.startDocument();
        atts = new AttributesImpl();
        th.startElement("", "", "HOWTOS", atts);
    }

    public void process(String s) throws SAXException {
        String reg = "[\\|,]";
        String[] elements = s.split(reg);

        System.out.println(Arrays.toString(elements));
        atts.clear();
        th.startElement("", "", "TOPIC", atts);

        th.startElement("", "", "TITLE", atts);
        th.characters(elements[0].toCharArray(), 0, elements[0].length());
        th.endElement("", "", "TITLE");

        th.startElement("", "", "URL", atts);
        th.characters(elements[1].toCharArray(), 0, elements[1].length());
        th.endElement("", "", "URL");

        th.startElement("", "", "THIRD", atts);
        th.characters(elements[2].toCharArray(), 0, elements[2].length());
        th.endElement("", "", "THIRD");



        th.endElement("", "", "TOPIC");
    }

    public void closeXML() throws SAXException {
        th.endElement("", "", "HOWTOS");
        th.endDocument();
    }
}