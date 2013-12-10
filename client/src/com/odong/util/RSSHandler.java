package com.odong.util;

import android.util.Log;
import com.odong.Constants;
import com.odong.model.RSSFeed;
import com.odong.model.RSSItem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by flamen on 13-12-9.
 */
public class RSSHandler extends DefaultHandler {
    @Override
    public void startDocument() throws SAXException {
        feed = new RSSFeed();
        item = new RSSItem();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("channel")) {
            state = State.NULL;
            return;
        }
        if (localName.equals("item")) {
            item = new RSSItem();
            return;
        }
        if (localName.equals("title")) {
            state = State.TITLE;
            return;
        }
        if (localName.equals("description")) {
            state = State.DESCRIPTION;
            return;
        }
        if (localName.equals("link")) {
            state = State.LINK;
            return;
        }
        if (localName.equals("category")) {
            state = State.CATEGORY;
            return;
        }
        if (localName.equals("pubDate")) {
            state = State.PUB_DATE;
            return;
        }
        state = State.NULL;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("item")) {
            feed.addItem(item);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        switch (state) {
            case TITLE:
                item.setTitle(str);
                state = State.NULL;
                break;
            case LINK:
                item.setLink(str);
                state = State.NULL;
                break;
            case DESCRIPTION:
                item.setDescription(str);
                state = State.NULL;
                break;
            case CATEGORY:
                item.setCategory(str);
                state = State.NULL;
                break;
            case PUB_DATE:
                try {
                    Date date = from.parse(str);
                    item.setPubDate(to.format(date));
                } catch (ParseException e) {
                    Log.e(Constants.LOG_NAME, "解析时间出错", e);
                }
                state = State.NULL;
                break;

        }
    }

    private RSSItem item;
    private RSSFeed feed;
    private State state;
    private DateFormat from = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
    private DateFormat to = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    enum State {
        NULL, TITLE, LINK, DESCRIPTION, CATEGORY, PUB_DATE
    }

    public RSSFeed getFeed() {
        return feed;
    }
}
