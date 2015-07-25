package zbynek.remotehouseholdcontrol.nettools;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;
import android.util.Xml;

public class StatusesXmlParser {

	private static final String DATA_TAG = "DATA";

	@Deprecated
	public static LinkedHashMap<String, String> parseStatusesFromXml(
	  String xmlString) throws XmlPullParserException, IOException {
		return new LinkedHashMap<String, String>(new ArrayMap<String, String>(
		  parseXml(xmlString)));
	}

	public static SimpleArrayMap<String, String> parseXml(String xml) throws
	  XmlPullParserException, IOException {
	  SimpleArrayMap<String, String> m = new SimpleArrayMap<String, String>();
		XmlPullParser parser = Xml.newPullParser();
    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
    parser.setInput(new StringReader(xml));
    parser.nextTag();
    parser.require(XmlPullParser.START_TAG, null, DATA_TAG);
    while (!(parser.next() == XmlPullParser.END_TAG 
      && DATA_TAG.equals(parser.getName()))) {

			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String elementName = parser.getName();
			parser.next();
			String value = parser.getText();
			m.put(elementName, value);
		}
	  return m;
	}
}
