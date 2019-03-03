package android.graphics;

import android.graphics.fonts.FontVariationAxis;
import android.text.FontConfig;
import android.text.FontConfig.Alias;
import android.text.FontConfig.Family;
import android.text.FontConfig.Font;
import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FontListParser
{
  private static final Pattern FILENAME_WHITESPACE_PATTERN = Pattern.compile("^[ \\n\\r\\t]+|[ \\n\\r\\t]+$");
  
  public FontListParser() {}
  
  public static FontConfig parse(InputStream paramInputStream)
    throws XmlPullParserException, IOException
  {
    try
    {
      Object localObject1 = Xml.newPullParser();
      ((XmlPullParser)localObject1).setInput(paramInputStream, null);
      ((XmlPullParser)localObject1).nextTag();
      localObject1 = readFamilies((XmlPullParser)localObject1);
      return localObject1;
    }
    finally
    {
      paramInputStream.close();
    }
  }
  
  private static FontConfig.Alias readAlias(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    String str1 = paramXmlPullParser.getAttributeValue(null, "name");
    String str2 = paramXmlPullParser.getAttributeValue(null, "to");
    String str3 = paramXmlPullParser.getAttributeValue(null, "weight");
    int i;
    if (str3 == null) {
      i = 400;
    } else {
      i = Integer.parseInt(str3);
    }
    skip(paramXmlPullParser);
    return new FontConfig.Alias(str1, str2, i);
  }
  
  private static FontVariationAxis readAxis(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    String str1 = paramXmlPullParser.getAttributeValue(null, "tag");
    String str2 = paramXmlPullParser.getAttributeValue(null, "stylevalue");
    skip(paramXmlPullParser);
    return new FontVariationAxis(str1, Float.parseFloat(str2));
  }
  
  private static FontConfig readFamilies(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    paramXmlPullParser.require(2, null, "familyset");
    while (paramXmlPullParser.next() != 3) {
      if (paramXmlPullParser.getEventType() == 2)
      {
        String str = paramXmlPullParser.getName();
        if (str.equals("family")) {
          localArrayList1.add(readFamily(paramXmlPullParser));
        } else if (str.equals("alias")) {
          localArrayList2.add(readAlias(paramXmlPullParser));
        } else {
          skip(paramXmlPullParser);
        }
      }
    }
    return new FontConfig((FontConfig.Family[])localArrayList1.toArray(new FontConfig.Family[localArrayList1.size()]), (FontConfig.Alias[])localArrayList2.toArray(new FontConfig.Alias[localArrayList2.size()]));
  }
  
  private static FontConfig.Family readFamily(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    String str1 = paramXmlPullParser.getAttributeValue(null, "name");
    Object localObject = paramXmlPullParser.getAttributeValue(null, "lang");
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = ((String)localObject).split("\\s+");
    }
    String str2 = paramXmlPullParser.getAttributeValue(null, "variant");
    ArrayList localArrayList = new ArrayList();
    while (paramXmlPullParser.next() != 3) {
      if (paramXmlPullParser.getEventType() == 2) {
        if (paramXmlPullParser.getName().equals("font")) {
          localArrayList.add(readFont(paramXmlPullParser));
        } else {
          skip(paramXmlPullParser);
        }
      }
    }
    int i = 0;
    int j = i;
    if (str2 != null) {
      if (str2.equals("compact"))
      {
        j = 1;
      }
      else
      {
        j = i;
        if (str2.equals("elegant")) {
          j = 2;
        }
      }
    }
    return new FontConfig.Family(str1, (FontConfig.Font[])localArrayList.toArray(new FontConfig.Font[localArrayList.size()]), (String[])localObject, j);
  }
  
  private static FontConfig.Font readFont(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    Object localObject = paramXmlPullParser.getAttributeValue(null, "index");
    if (localObject == null) {}
    for (int i = 0;; i = Integer.parseInt((String)localObject)) {
      break;
    }
    ArrayList localArrayList = new ArrayList();
    localObject = paramXmlPullParser.getAttributeValue(null, "weight");
    if (localObject == null) {}
    for (int j = 400;; j = Integer.parseInt((String)localObject)) {
      break;
    }
    boolean bool = "italic".equals(paramXmlPullParser.getAttributeValue(null, "style"));
    String str = paramXmlPullParser.getAttributeValue(null, "fallbackFor");
    localObject = new StringBuilder();
    while (paramXmlPullParser.next() != 3)
    {
      if (paramXmlPullParser.getEventType() == 4) {
        ((StringBuilder)localObject).append(paramXmlPullParser.getText());
      }
      if (paramXmlPullParser.getEventType() == 2) {
        if (paramXmlPullParser.getName().equals("axis")) {
          localArrayList.add(readAxis(paramXmlPullParser));
        } else {
          skip(paramXmlPullParser);
        }
      }
    }
    return new FontConfig.Font(FILENAME_WHITESPACE_PATTERN.matcher((CharSequence)localObject).replaceAll(""), i, (FontVariationAxis[])localArrayList.toArray(new FontVariationAxis[localArrayList.size()]), j, bool, str);
  }
  
  private static void skip(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = 1;
    while (i > 0) {
      switch (paramXmlPullParser.next())
      {
      default: 
        break;
      case 3: 
        i--;
        break;
      case 2: 
        i++;
      }
    }
  }
}
