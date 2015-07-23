package com.example.ivan.recoammico;

/**
 * Parser for tour XML file
 * Parse the Connection elements (lines similar to
 * <tourml:Connection tourml:srcId="stop-562" tourml:destId="stop-464" tourml:priority="1"/>
 * )
 * where sourceId defines a zone (carte de zone)
 * and destId is the id of a POI in this zone
 *
 * Unfinished because not yet useful although it works.
 * Good start for XML parsing.
 */

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TourmlParser {
    private XmlPullParserFactory xmlFactoryObj;
    private String _filename = null;
    // for debugging:
    private static final String TAG = "reco";

    public TourmlParser(String filename){
        this._filename = filename;
    }

    public void parseTourmlConnections() {
        XmlPullParser parser = null;
        FileInputStream fis = null;
        String srcId = "";
        String destId = "";

        try {
            xmlFactoryObj = XmlPullParserFactory.newInstance();
            xmlFactoryObj.setNamespaceAware(true);
            parser = xmlFactoryObj.newPullParser();
        } catch (Exception e) {
            Log.i(TAG, "XmlPullParserFactory error. " + e);
        }

        try {
            File myXML = new File(this._filename);
            fis = new FileInputStream(myXML);
        } catch (IOException e) {
            Log.i(TAG, "File error. " + e);
        }

        try {
            parser.setInput(fis, null);
        } catch (Exception e) {
            Log.i(TAG, "XmlPullParserFactory error. " + e);
        }

        int event;
        try {
            event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                //if(event == XmlPullParser.START_DOCUMENT) {
                //    Log.i(TAG, "In start document");
                //}
                String name = parser.getName();
                //Log.i(TAG, "parser.getName(): " + name);
                if (event == XmlPullParser.END_TAG && name.equals("Connection")){
                    srcId = parser.getAttributeValue(null, "srcId");
                    destId = parser.getAttributeValue(null, "destId");
                    Log.i(TAG, "srcId: " + srcId + " - destId: " + destId);
                }

                /* // keep this snippet for parsing the text. see: http://www.tutorialspoint.com/android/android_xml_parsers.htm
                switch (event){
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        if(name.equals("Connection")){
                            //srcId = parser.getAttributeValue("tourml", "srcId");
                            srcId = parser.getAttributeValue(null, "srcId");
                            //destId = parser.getAttributeValue("tourml", "destId");
                            destId = parser.getAttributeValue(null, "destId");
                            Log.i(TAG, "srcId: " + srcId + " - destId: " + destId);
                            //Log.i(TAG, parser.getAttributeName(0) + " " + parser.getAttributeName(1));
                        }
                        break;
                }*/
                event = parser.next();
            }
        } catch (Exception e) {
            Log.i(TAG, "XmlPullParserFactory error: " + e);
        }
    }

}
