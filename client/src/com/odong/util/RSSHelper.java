package com.odong.util;

import android.util.Log;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;

/**
 * Created by flamen on 13-12-9.
 */
public class RSSHelper {

    public RSSHelper() {
        factory = SAXParserFactory.newInstance();
    }

    public void parse(String url, ContentHandler handler) throws ParserConfigurationException,SAXException,IOException{

            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(handler);
            InputSource is = new InputSource(new URL(url).openStream());
            reader.parse(is);


    }

    private SAXParserFactory factory;

    public static RSSHelper get() {
        return instance;
    }

    static {
        instance = new RSSHelper();
    }

    private final static RSSHelper instance;
}
