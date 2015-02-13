package nsu.ccfit.ru.weathernovosibapi19;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Android on 22.11.2014.
 */
public class GetXmlFromUrl {
    Document document=null;
    public GetXmlFromUrl(String url)
    {
        document = getXML(url);
    }

    private Document getXML(String url) {
            Document document=null;
            try {
                URL urlLink = new URL(url);
                DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null;
                builder = factory.newDocumentBuilder();
                document = builder.parse(urlLink.openStream());
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (SAXException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return document;
    }
    public Document getXMLSign()
    {
        return document;
    }
}
