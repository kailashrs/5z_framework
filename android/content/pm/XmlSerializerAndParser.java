package android.content.pm;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public abstract interface XmlSerializerAndParser<T>
{
  public abstract T createFromXml(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException;
  
  public abstract void writeAsXml(T paramT, XmlSerializer paramXmlSerializer)
    throws IOException;
}
