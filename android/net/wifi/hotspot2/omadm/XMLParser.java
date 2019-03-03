package android.net.wifi.hotspot2.omadm;

import android.text.TextUtils;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser
  extends DefaultHandler
{
  private XMLNode mCurrent = null;
  private XMLNode mRoot = null;
  
  public XMLParser() {}
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    mCurrent.addText(new String(paramArrayOfChar, paramInt1, paramInt2));
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    if (paramString3.equals(mCurrent.getTag()))
    {
      mCurrent.close();
      mCurrent = mCurrent.getParent();
      return;
    }
    paramString1 = new StringBuilder();
    paramString1.append("End tag '");
    paramString1.append(paramString3);
    paramString1.append("' doesn't match current node: ");
    paramString1.append(mCurrent);
    throw new SAXException(paramString1.toString());
  }
  
  public XMLNode parse(String paramString)
    throws IOException, SAXException
  {
    if (!TextUtils.isEmpty(paramString))
    {
      mRoot = null;
      mCurrent = null;
      try
      {
        SAXParser localSAXParser = SAXParserFactory.newInstance().newSAXParser();
        InputSource localInputSource = new org/xml/sax/InputSource;
        StringReader localStringReader = new java/io/StringReader;
        localStringReader.<init>(paramString);
        localInputSource.<init>(localStringReader);
        localSAXParser.parse(localInputSource, this);
        paramString = mRoot;
        return paramString;
      }
      catch (ParserConfigurationException paramString)
      {
        throw new SAXException(paramString);
      }
    }
    throw new IOException("XML string not provided");
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    paramString1 = mCurrent;
    mCurrent = new XMLNode(paramString1, paramString3);
    if (mRoot == null)
    {
      mRoot = mCurrent;
    }
    else
    {
      if (paramString1 == null) {
        break label49;
      }
      paramString1.addChild(mCurrent);
    }
    return;
    label49:
    throw new SAXException("More than one root nodes");
  }
}
