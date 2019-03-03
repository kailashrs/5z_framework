package com.android.internal.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Xml;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class XmlUtils
{
  private static final String STRING_ARRAY_SEPARATOR = ":";
  
  public XmlUtils() {}
  
  public static final void beginDocument(XmlPullParser paramXmlPullParser, String paramString)
    throws XmlPullParserException, IOException
  {
    int i;
    do
    {
      i = paramXmlPullParser.next();
    } while ((i != 2) && (i != 1));
    if (i == 2)
    {
      if (paramXmlPullParser.getName().equals(paramString)) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected start tag: found ");
      localStringBuilder.append(paramXmlPullParser.getName());
      localStringBuilder.append(", expected ");
      localStringBuilder.append(paramString);
      throw new XmlPullParserException(localStringBuilder.toString());
    }
    throw new XmlPullParserException("No start tag found");
  }
  
  public static final boolean convertValueToBoolean(CharSequence paramCharSequence, boolean paramBoolean)
  {
    boolean bool = false;
    if (paramCharSequence == null) {
      return paramBoolean;
    }
    if ((!paramCharSequence.equals("1")) && (!paramCharSequence.equals("true")))
    {
      paramBoolean = bool;
      if (!paramCharSequence.equals("TRUE")) {}
    }
    else
    {
      paramBoolean = true;
    }
    return paramBoolean;
  }
  
  public static final int convertValueToInt(CharSequence paramCharSequence, int paramInt)
  {
    if (paramCharSequence == null) {
      return paramInt;
    }
    paramCharSequence = paramCharSequence.toString();
    int i = 1;
    paramInt = 0;
    int j = paramCharSequence.length();
    int k = 10;
    if ('-' == paramCharSequence.charAt(0))
    {
      i = -1;
      paramInt = 0 + 1;
    }
    if ('0' == paramCharSequence.charAt(paramInt))
    {
      if (paramInt == j - 1) {
        return 0;
      }
      j = paramCharSequence.charAt(paramInt + 1);
      if ((120 != j) && (88 != j))
      {
        j = paramInt + 1;
        paramInt = 8;
      }
      else
      {
        j = paramInt + 2;
        paramInt = 16;
      }
      k = paramInt;
    }
    else
    {
      j = paramInt;
      if ('#' == paramCharSequence.charAt(paramInt))
      {
        j = paramInt + 1;
        k = 16;
      }
    }
    return Integer.parseInt(paramCharSequence.substring(j), k) * i;
  }
  
  public static final int convertValueToList(CharSequence paramCharSequence, String[] paramArrayOfString, int paramInt)
  {
    if (paramCharSequence != null) {
      for (int i = 0; i < paramArrayOfString.length; i++) {
        if (paramCharSequence.equals(paramArrayOfString[i])) {
          return i;
        }
      }
    }
    return paramInt;
  }
  
  public static int convertValueToUnsignedInt(String paramString, int paramInt)
  {
    if (paramString == null) {
      return paramInt;
    }
    return parseUnsignedIntAttribute(paramString);
  }
  
  public static final void nextElement(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i;
    do
    {
      i = paramXmlPullParser.next();
    } while ((i != 2) && (i != 1));
  }
  
  public static boolean nextElementWithin(XmlPullParser paramXmlPullParser, int paramInt)
    throws IOException, XmlPullParserException
  {
    for (;;)
    {
      int i = paramXmlPullParser.next();
      if ((i == 1) || ((i == 3) && (paramXmlPullParser.getDepth() == paramInt))) {
        break;
      }
      if ((i == 2) && (paramXmlPullParser.getDepth() == paramInt + 1)) {
        return true;
      }
    }
    return false;
  }
  
  public static int parseUnsignedIntAttribute(CharSequence paramCharSequence)
  {
    paramCharSequence = paramCharSequence.toString();
    int i = 0;
    int j = paramCharSequence.length();
    int k = 10;
    if ('0' == paramCharSequence.charAt(0))
    {
      if (j - 1 == 0) {
        return 0;
      }
      i = paramCharSequence.charAt(0 + 1);
      if ((120 != i) && (88 != i))
      {
        i = 0 + 1;
        k = 8;
      }
      else
      {
        i = 0 + 2;
        k = 16;
      }
    }
    else if ('#' == paramCharSequence.charAt(0))
    {
      i = 0 + 1;
      k = 16;
    }
    return (int)Long.parseLong(paramCharSequence.substring(i), k);
  }
  
  public static Bitmap readBitmapAttribute(XmlPullParser paramXmlPullParser, String paramString)
  {
    paramXmlPullParser = readByteArrayAttribute(paramXmlPullParser, paramString);
    if (paramXmlPullParser != null) {
      return BitmapFactory.decodeByteArray(paramXmlPullParser, 0, paramXmlPullParser.length);
    }
    return null;
  }
  
  public static boolean readBooleanAttribute(XmlPullParser paramXmlPullParser, String paramString)
  {
    return Boolean.parseBoolean(paramXmlPullParser.getAttributeValue(null, paramString));
  }
  
  public static boolean readBooleanAttribute(XmlPullParser paramXmlPullParser, String paramString, boolean paramBoolean)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (paramXmlPullParser == null) {
      return paramBoolean;
    }
    return Boolean.parseBoolean(paramXmlPullParser);
  }
  
  public static byte[] readByteArrayAttribute(XmlPullParser paramXmlPullParser, String paramString)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (paramXmlPullParser != null) {
      return Base64.decode(paramXmlPullParser, 0);
    }
    return null;
  }
  
  public static float readFloatAttribute(XmlPullParser paramXmlPullParser, String paramString)
    throws IOException
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    try
    {
      float f = Float.parseFloat(paramXmlPullParser);
      return f;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("problem parsing ");
      localStringBuilder.append(paramString);
      localStringBuilder.append("=");
      localStringBuilder.append(paramXmlPullParser);
      localStringBuilder.append(" as long");
      throw new ProtocolException(localStringBuilder.toString());
    }
  }
  
  public static int readIntAttribute(XmlPullParser paramXmlPullParser, String paramString)
    throws IOException
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    try
    {
      int i = Integer.parseInt(paramXmlPullParser);
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("problem parsing ");
      localStringBuilder.append(paramString);
      localStringBuilder.append("=");
      localStringBuilder.append(paramXmlPullParser);
      localStringBuilder.append(" as int");
      throw new ProtocolException(localStringBuilder.toString());
    }
  }
  
  public static int readIntAttribute(XmlPullParser paramXmlPullParser, String paramString, int paramInt)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (TextUtils.isEmpty(paramXmlPullParser)) {
      return paramInt;
    }
    try
    {
      int i = Integer.parseInt(paramXmlPullParser);
      return i;
    }
    catch (NumberFormatException paramXmlPullParser) {}
    return paramInt;
  }
  
  public static final ArrayList readListXml(InputStream paramInputStream)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = Xml.newPullParser();
    localXmlPullParser.setInput(paramInputStream, StandardCharsets.UTF_8.name());
    return (ArrayList)readValueXml(localXmlPullParser, new String[1]);
  }
  
  public static long readLongAttribute(XmlPullParser paramXmlPullParser, String paramString)
    throws IOException
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    try
    {
      long l = Long.parseLong(paramXmlPullParser);
      return l;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("problem parsing ");
      localStringBuilder.append(paramString);
      localStringBuilder.append("=");
      localStringBuilder.append(paramXmlPullParser);
      localStringBuilder.append(" as long");
      throw new ProtocolException(localStringBuilder.toString());
    }
  }
  
  public static long readLongAttribute(XmlPullParser paramXmlPullParser, String paramString, long paramLong)
  {
    paramXmlPullParser = paramXmlPullParser.getAttributeValue(null, paramString);
    if (TextUtils.isEmpty(paramXmlPullParser)) {
      return paramLong;
    }
    try
    {
      long l = Long.parseLong(paramXmlPullParser);
      return l;
    }
    catch (NumberFormatException paramXmlPullParser) {}
    return paramLong;
  }
  
  public static final HashMap<String, ?> readMapXml(InputStream paramInputStream)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = Xml.newPullParser();
    localXmlPullParser.setInput(paramInputStream, StandardCharsets.UTF_8.name());
    return (HashMap)readValueXml(localXmlPullParser, new String[1]);
  }
  
  public static final HashSet readSetXml(InputStream paramInputStream)
    throws XmlPullParserException, IOException
  {
    XmlPullParser localXmlPullParser = Xml.newPullParser();
    localXmlPullParser.setInput(paramInputStream, null);
    return (HashSet)readValueXml(localXmlPullParser, new String[1]);
  }
  
  public static String readStringAttribute(XmlPullParser paramXmlPullParser, String paramString)
  {
    return paramXmlPullParser.getAttributeValue(null, paramString);
  }
  
  public static final ArrayMap<String, ?> readThisArrayMapXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString, ReadMapCallback paramReadMapCallback)
    throws XmlPullParserException, IOException
  {
    ArrayMap localArrayMap = new ArrayMap();
    int i = paramXmlPullParser.getEventType();
    do
    {
      if (i == 2)
      {
        Object localObject = readThisValueXml(paramXmlPullParser, paramArrayOfString, paramReadMapCallback, true);
        localArrayMap.put(paramArrayOfString[0], localObject);
      }
      else if (i == 3)
      {
        if (paramXmlPullParser.getName().equals(paramString)) {
          return localArrayMap;
        }
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("Expected ");
        paramArrayOfString.append(paramString);
        paramArrayOfString.append(" end tag at: ");
        paramArrayOfString.append(paramXmlPullParser.getName());
        throw new XmlPullParserException(paramArrayOfString.toString());
      }
      i = paramXmlPullParser.next();
    } while (i != 1);
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("Document ended before ");
    paramXmlPullParser.append(paramString);
    paramXmlPullParser.append(" end tag");
    throw new XmlPullParserException(paramXmlPullParser.toString());
  }
  
  public static final boolean[] readThisBooleanArrayXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    try
    {
      int i = Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "num"));
      paramXmlPullParser.next();
      paramArrayOfString = new boolean[i];
      int j = 0;
      int k = paramXmlPullParser.getEventType();
      for (;;)
      {
        if (k == 2)
        {
          if (paramXmlPullParser.getName().equals("item"))
          {
            try
            {
              paramArrayOfString[j] = Boolean.parseBoolean(paramXmlPullParser.getAttributeValue(null, "value"));
              i = j;
            }
            catch (NumberFormatException paramXmlPullParser)
            {
              throw new XmlPullParserException("Not a number in value attribute in item");
            }
            catch (NullPointerException paramXmlPullParser)
            {
              throw new XmlPullParserException("Need value attribute in item");
            }
          }
          else
          {
            paramString = new StringBuilder();
            paramString.append("Expected item tag at: ");
            paramString.append(paramXmlPullParser.getName());
            throw new XmlPullParserException(paramString.toString());
          }
        }
        else
        {
          i = j;
          if (k == 3)
          {
            if (paramXmlPullParser.getName().equals(paramString)) {
              return paramArrayOfString;
            }
            if (paramXmlPullParser.getName().equals("item"))
            {
              i = j + 1;
            }
            else
            {
              paramArrayOfString = new StringBuilder();
              paramArrayOfString.append("Expected ");
              paramArrayOfString.append(paramString);
              paramArrayOfString.append(" end tag at: ");
              paramArrayOfString.append(paramXmlPullParser.getName());
              throw new XmlPullParserException(paramArrayOfString.toString());
            }
          }
        }
        k = paramXmlPullParser.next();
        if (k == 1) {
          break;
        }
        j = i;
      }
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Document ended before ");
      paramXmlPullParser.append(paramString);
      paramXmlPullParser.append(" end tag");
      throw new XmlPullParserException(paramXmlPullParser.toString());
    }
    catch (NumberFormatException paramXmlPullParser)
    {
      throw new XmlPullParserException("Not a number in num attribute in string-array");
    }
    catch (NullPointerException paramXmlPullParser)
    {
      throw new XmlPullParserException("Need num attribute in string-array");
    }
  }
  
  public static final byte[] readThisByteArrayXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    try
    {
      int i = Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "num"));
      byte[] arrayOfByte = new byte[i];
      int j = paramXmlPullParser.getEventType();
      do
      {
        if (j == 4)
        {
          if (i > 0)
          {
            paramArrayOfString = paramXmlPullParser.getText();
            if ((paramArrayOfString != null) && (paramArrayOfString.length() == i * 2))
            {
              for (j = 0; j < i; j++)
              {
                int k = paramArrayOfString.charAt(2 * j);
                int m = paramArrayOfString.charAt(2 * j + 1);
                if (k > 97) {
                  k = k - 97 + 10;
                } else {
                  k -= 48;
                }
                if (m > 97) {
                  m = m - 97 + 10;
                } else {
                  m -= 48;
                }
                arrayOfByte[j] = ((byte)(byte)((k & 0xF) << 4 | m & 0xF));
              }
            }
            else
            {
              paramXmlPullParser = new StringBuilder();
              paramXmlPullParser.append("Invalid value found in byte-array: ");
              paramXmlPullParser.append(paramArrayOfString);
              throw new XmlPullParserException(paramXmlPullParser.toString());
            }
          }
        }
        else if (j == 3)
        {
          if (paramXmlPullParser.getName().equals(paramString)) {
            return arrayOfByte;
          }
          paramArrayOfString = new StringBuilder();
          paramArrayOfString.append("Expected ");
          paramArrayOfString.append(paramString);
          paramArrayOfString.append(" end tag at: ");
          paramArrayOfString.append(paramXmlPullParser.getName());
          throw new XmlPullParserException(paramArrayOfString.toString());
        }
        j = paramXmlPullParser.next();
      } while (j != 1);
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Document ended before ");
      paramXmlPullParser.append(paramString);
      paramXmlPullParser.append(" end tag");
      throw new XmlPullParserException(paramXmlPullParser.toString());
    }
    catch (NumberFormatException paramXmlPullParser)
    {
      throw new XmlPullParserException("Not a number in num attribute in byte-array");
    }
    catch (NullPointerException paramXmlPullParser)
    {
      throw new XmlPullParserException("Need num attribute in byte-array");
    }
  }
  
  public static final double[] readThisDoubleArrayXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    try
    {
      int i = Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "num"));
      paramXmlPullParser.next();
      paramArrayOfString = new double[i];
      int j = 0;
      int k = paramXmlPullParser.getEventType();
      for (;;)
      {
        if (k == 2)
        {
          if (paramXmlPullParser.getName().equals("item"))
          {
            try
            {
              paramArrayOfString[j] = Double.parseDouble(paramXmlPullParser.getAttributeValue(null, "value"));
              i = j;
            }
            catch (NumberFormatException paramXmlPullParser)
            {
              throw new XmlPullParserException("Not a number in value attribute in item");
            }
            catch (NullPointerException paramXmlPullParser)
            {
              throw new XmlPullParserException("Need value attribute in item");
            }
          }
          else
          {
            paramString = new StringBuilder();
            paramString.append("Expected item tag at: ");
            paramString.append(paramXmlPullParser.getName());
            throw new XmlPullParserException(paramString.toString());
          }
        }
        else
        {
          i = j;
          if (k == 3)
          {
            if (paramXmlPullParser.getName().equals(paramString)) {
              return paramArrayOfString;
            }
            if (paramXmlPullParser.getName().equals("item"))
            {
              i = j + 1;
            }
            else
            {
              paramArrayOfString = new StringBuilder();
              paramArrayOfString.append("Expected ");
              paramArrayOfString.append(paramString);
              paramArrayOfString.append(" end tag at: ");
              paramArrayOfString.append(paramXmlPullParser.getName());
              throw new XmlPullParserException(paramArrayOfString.toString());
            }
          }
        }
        k = paramXmlPullParser.next();
        if (k == 1) {
          break;
        }
        j = i;
      }
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Document ended before ");
      paramXmlPullParser.append(paramString);
      paramXmlPullParser.append(" end tag");
      throw new XmlPullParserException(paramXmlPullParser.toString());
    }
    catch (NumberFormatException paramXmlPullParser)
    {
      throw new XmlPullParserException("Not a number in num attribute in double-array");
    }
    catch (NullPointerException paramXmlPullParser)
    {
      throw new XmlPullParserException("Need num attribute in double-array");
    }
  }
  
  public static final int[] readThisIntArrayXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    try
    {
      int i = Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "num"));
      paramXmlPullParser.next();
      paramArrayOfString = new int[i];
      int j = 0;
      i = paramXmlPullParser.getEventType();
      for (;;)
      {
        int k;
        if (i == 2)
        {
          if (paramXmlPullParser.getName().equals("item"))
          {
            try
            {
              paramArrayOfString[j] = Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "value"));
              k = j;
            }
            catch (NumberFormatException paramXmlPullParser)
            {
              throw new XmlPullParserException("Not a number in value attribute in item");
            }
            catch (NullPointerException paramXmlPullParser)
            {
              throw new XmlPullParserException("Need value attribute in item");
            }
          }
          else
          {
            paramString = new StringBuilder();
            paramString.append("Expected item tag at: ");
            paramString.append(paramXmlPullParser.getName());
            throw new XmlPullParserException(paramString.toString());
          }
        }
        else
        {
          k = j;
          if (i == 3)
          {
            if (paramXmlPullParser.getName().equals(paramString)) {
              return paramArrayOfString;
            }
            if (paramXmlPullParser.getName().equals("item"))
            {
              k = j + 1;
            }
            else
            {
              paramArrayOfString = new StringBuilder();
              paramArrayOfString.append("Expected ");
              paramArrayOfString.append(paramString);
              paramArrayOfString.append(" end tag at: ");
              paramArrayOfString.append(paramXmlPullParser.getName());
              throw new XmlPullParserException(paramArrayOfString.toString());
            }
          }
        }
        i = paramXmlPullParser.next();
        if (i == 1) {
          break;
        }
        j = k;
      }
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Document ended before ");
      paramXmlPullParser.append(paramString);
      paramXmlPullParser.append(" end tag");
      throw new XmlPullParserException(paramXmlPullParser.toString());
    }
    catch (NumberFormatException paramXmlPullParser)
    {
      throw new XmlPullParserException("Not a number in num attribute in int-array");
    }
    catch (NullPointerException paramXmlPullParser)
    {
      throw new XmlPullParserException("Need num attribute in int-array");
    }
  }
  
  public static final ArrayList readThisListXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    return readThisListXml(paramXmlPullParser, paramString, paramArrayOfString, null, false);
  }
  
  private static final ArrayList readThisListXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString, ReadMapCallback paramReadMapCallback, boolean paramBoolean)
    throws XmlPullParserException, IOException
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramXmlPullParser.getEventType();
    do
    {
      if (i == 2)
      {
        localArrayList.add(readThisValueXml(paramXmlPullParser, paramArrayOfString, paramReadMapCallback, paramBoolean));
      }
      else if (i == 3)
      {
        if (paramXmlPullParser.getName().equals(paramString)) {
          return localArrayList;
        }
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("Expected ");
        paramArrayOfString.append(paramString);
        paramArrayOfString.append(" end tag at: ");
        paramArrayOfString.append(paramXmlPullParser.getName());
        throw new XmlPullParserException(paramArrayOfString.toString());
      }
      i = paramXmlPullParser.next();
    } while (i != 1);
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("Document ended before ");
    paramXmlPullParser.append(paramString);
    paramXmlPullParser.append(" end tag");
    throw new XmlPullParserException(paramXmlPullParser.toString());
  }
  
  public static final long[] readThisLongArrayXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    try
    {
      int i = Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "num"));
      paramXmlPullParser.next();
      paramArrayOfString = new long[i];
      i = 0;
      int j = paramXmlPullParser.getEventType();
      for (;;)
      {
        int k;
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("item"))
          {
            try
            {
              paramArrayOfString[i] = Long.parseLong(paramXmlPullParser.getAttributeValue(null, "value"));
              k = i;
            }
            catch (NumberFormatException paramXmlPullParser)
            {
              throw new XmlPullParserException("Not a number in value attribute in item");
            }
            catch (NullPointerException paramXmlPullParser)
            {
              throw new XmlPullParserException("Need value attribute in item");
            }
          }
          else
          {
            paramString = new StringBuilder();
            paramString.append("Expected item tag at: ");
            paramString.append(paramXmlPullParser.getName());
            throw new XmlPullParserException(paramString.toString());
          }
        }
        else
        {
          k = i;
          if (j == 3)
          {
            if (paramXmlPullParser.getName().equals(paramString)) {
              return paramArrayOfString;
            }
            if (paramXmlPullParser.getName().equals("item"))
            {
              k = i + 1;
            }
            else
            {
              paramArrayOfString = new StringBuilder();
              paramArrayOfString.append("Expected ");
              paramArrayOfString.append(paramString);
              paramArrayOfString.append(" end tag at: ");
              paramArrayOfString.append(paramXmlPullParser.getName());
              throw new XmlPullParserException(paramArrayOfString.toString());
            }
          }
        }
        j = paramXmlPullParser.next();
        if (j == 1) {
          break;
        }
        i = k;
      }
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Document ended before ");
      paramXmlPullParser.append(paramString);
      paramXmlPullParser.append(" end tag");
      throw new XmlPullParserException(paramXmlPullParser.toString());
    }
    catch (NumberFormatException paramXmlPullParser)
    {
      throw new XmlPullParserException("Not a number in num attribute in long-array");
    }
    catch (NullPointerException paramXmlPullParser)
    {
      throw new XmlPullParserException("Need num attribute in long-array");
    }
  }
  
  public static final HashMap<String, ?> readThisMapXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    return readThisMapXml(paramXmlPullParser, paramString, paramArrayOfString, null);
  }
  
  public static final HashMap<String, ?> readThisMapXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString, ReadMapCallback paramReadMapCallback)
    throws XmlPullParserException, IOException
  {
    HashMap localHashMap = new HashMap();
    int i = paramXmlPullParser.getEventType();
    do
    {
      if (i == 2)
      {
        Object localObject = readThisValueXml(paramXmlPullParser, paramArrayOfString, paramReadMapCallback, false);
        localHashMap.put(paramArrayOfString[0], localObject);
      }
      else if (i == 3)
      {
        if (paramXmlPullParser.getName().equals(paramString)) {
          return localHashMap;
        }
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("Expected ");
        paramArrayOfString.append(paramString);
        paramArrayOfString.append(" end tag at: ");
        paramArrayOfString.append(paramXmlPullParser.getName());
        throw new XmlPullParserException(paramArrayOfString.toString());
      }
      i = paramXmlPullParser.next();
    } while (i != 1);
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("Document ended before ");
    paramXmlPullParser.append(paramString);
    paramXmlPullParser.append(" end tag");
    throw new XmlPullParserException(paramXmlPullParser.toString());
  }
  
  private static final Object readThisPrimitiveValueXml(XmlPullParser paramXmlPullParser, String paramString)
    throws XmlPullParserException, IOException
  {
    try
    {
      if (paramString.equals("int")) {
        return Integer.valueOf(Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "value")));
      }
      if (paramString.equals("long")) {
        return Long.valueOf(paramXmlPullParser.getAttributeValue(null, "value"));
      }
      if (paramString.equals("float")) {
        return new Float(paramXmlPullParser.getAttributeValue(null, "value"));
      }
      if (paramString.equals("double")) {
        return new Double(paramXmlPullParser.getAttributeValue(null, "value"));
      }
      if (paramString.equals("boolean"))
      {
        paramXmlPullParser = Boolean.valueOf(paramXmlPullParser.getAttributeValue(null, "value"));
        return paramXmlPullParser;
      }
      return null;
    }
    catch (NumberFormatException paramXmlPullParser)
    {
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Not a number in value attribute in <");
      paramXmlPullParser.append(paramString);
      paramXmlPullParser.append(">");
      throw new XmlPullParserException(paramXmlPullParser.toString());
    }
    catch (NullPointerException paramXmlPullParser)
    {
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Need value attribute in <");
      paramXmlPullParser.append(paramString);
      paramXmlPullParser.append(">");
      throw new XmlPullParserException(paramXmlPullParser.toString());
    }
  }
  
  public static final HashSet readThisSetXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    return readThisSetXml(paramXmlPullParser, paramString, paramArrayOfString, null, false);
  }
  
  private static final HashSet readThisSetXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString, ReadMapCallback paramReadMapCallback, boolean paramBoolean)
    throws XmlPullParserException, IOException
  {
    HashSet localHashSet = new HashSet();
    int i = paramXmlPullParser.getEventType();
    do
    {
      if (i == 2)
      {
        localHashSet.add(readThisValueXml(paramXmlPullParser, paramArrayOfString, paramReadMapCallback, paramBoolean));
      }
      else if (i == 3)
      {
        if (paramXmlPullParser.getName().equals(paramString)) {
          return localHashSet;
        }
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("Expected ");
        paramArrayOfString.append(paramString);
        paramArrayOfString.append(" end tag at: ");
        paramArrayOfString.append(paramXmlPullParser.getName());
        throw new XmlPullParserException(paramArrayOfString.toString());
      }
      i = paramXmlPullParser.next();
    } while (i != 1);
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("Document ended before ");
    paramXmlPullParser.append(paramString);
    paramXmlPullParser.append(" end tag");
    throw new XmlPullParserException(paramXmlPullParser.toString());
  }
  
  public static final String[] readThisStringArrayXml(XmlPullParser paramXmlPullParser, String paramString, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    try
    {
      int i = Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "num"));
      paramXmlPullParser.next();
      paramArrayOfString = new String[i];
      i = 0;
      int j = paramXmlPullParser.getEventType();
      for (;;)
      {
        int k;
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("item"))
          {
            try
            {
              paramArrayOfString[i] = paramXmlPullParser.getAttributeValue(null, "value");
              k = i;
            }
            catch (NumberFormatException paramXmlPullParser)
            {
              throw new XmlPullParserException("Not a number in value attribute in item");
            }
            catch (NullPointerException paramXmlPullParser)
            {
              throw new XmlPullParserException("Need value attribute in item");
            }
          }
          else
          {
            paramString = new StringBuilder();
            paramString.append("Expected item tag at: ");
            paramString.append(paramXmlPullParser.getName());
            throw new XmlPullParserException(paramString.toString());
          }
        }
        else
        {
          k = i;
          if (j == 3)
          {
            if (paramXmlPullParser.getName().equals(paramString)) {
              return paramArrayOfString;
            }
            if (paramXmlPullParser.getName().equals("item"))
            {
              k = i + 1;
            }
            else
            {
              paramArrayOfString = new StringBuilder();
              paramArrayOfString.append("Expected ");
              paramArrayOfString.append(paramString);
              paramArrayOfString.append(" end tag at: ");
              paramArrayOfString.append(paramXmlPullParser.getName());
              throw new XmlPullParserException(paramArrayOfString.toString());
            }
          }
        }
        j = paramXmlPullParser.next();
        if (j == 1) {
          break;
        }
        i = k;
      }
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Document ended before ");
      paramXmlPullParser.append(paramString);
      paramXmlPullParser.append(" end tag");
      throw new XmlPullParserException(paramXmlPullParser.toString());
    }
    catch (NumberFormatException paramXmlPullParser)
    {
      throw new XmlPullParserException("Not a number in num attribute in string-array");
    }
    catch (NullPointerException paramXmlPullParser)
    {
      throw new XmlPullParserException("Need num attribute in string-array");
    }
  }
  
  private static final Object readThisValueXml(XmlPullParser paramXmlPullParser, String[] paramArrayOfString, ReadMapCallback paramReadMapCallback, boolean paramBoolean)
    throws XmlPullParserException, IOException
  {
    String str1 = paramXmlPullParser.getAttributeValue(null, "name");
    String str2 = paramXmlPullParser.getName();
    int i;
    if (str2.equals("null"))
    {
      paramReadMapCallback = null;
    }
    else
    {
      if (str2.equals("string"))
      {
        paramReadMapCallback = "";
        do
        {
          for (;;)
          {
            i = paramXmlPullParser.next();
            if (i == 1) {
              break label223;
            }
            if (i == 3)
            {
              if (paramXmlPullParser.getName().equals("string"))
              {
                paramArrayOfString[0] = str1;
                return paramReadMapCallback;
              }
              paramArrayOfString = new StringBuilder();
              paramArrayOfString.append("Unexpected end tag in <string>: ");
              paramArrayOfString.append(paramXmlPullParser.getName());
              throw new XmlPullParserException(paramArrayOfString.toString());
            }
            if (i != 4) {
              break;
            }
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append(paramReadMapCallback);
            ((StringBuilder)localObject).append(paramXmlPullParser.getText());
            paramReadMapCallback = ((StringBuilder)localObject).toString();
          }
        } while (i != 2);
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("Unexpected start tag in <string>: ");
        paramArrayOfString.append(paramXmlPullParser.getName());
        throw new XmlPullParserException(paramArrayOfString.toString());
        label223:
        throw new XmlPullParserException("Unexpected end of document in <string>");
      }
      Object localObject = readThisPrimitiveValueXml(paramXmlPullParser, str2);
      if (localObject == null) {
        break label511;
      }
      paramReadMapCallback = (ReadMapCallback)localObject;
    }
    do
    {
      i = paramXmlPullParser.next();
      if (i == 1) {
        break label468;
      }
      if (i == 3)
      {
        if (paramXmlPullParser.getName().equals(str2))
        {
          paramArrayOfString[0] = str1;
          return paramReadMapCallback;
        }
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("Unexpected end tag in <");
        paramArrayOfString.append(str2);
        paramArrayOfString.append(">: ");
        paramArrayOfString.append(paramXmlPullParser.getName());
        throw new XmlPullParserException(paramArrayOfString.toString());
      }
      if (i == 4) {
        break;
      }
    } while (i != 2);
    paramArrayOfString = new StringBuilder();
    paramArrayOfString.append("Unexpected start tag in <");
    paramArrayOfString.append(str2);
    paramArrayOfString.append(">: ");
    paramArrayOfString.append(paramXmlPullParser.getName());
    throw new XmlPullParserException(paramArrayOfString.toString());
    paramArrayOfString = new StringBuilder();
    paramArrayOfString.append("Unexpected text in <");
    paramArrayOfString.append(str2);
    paramArrayOfString.append(">: ");
    paramArrayOfString.append(paramXmlPullParser.getName());
    throw new XmlPullParserException(paramArrayOfString.toString());
    label468:
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("Unexpected end of document in <");
    paramXmlPullParser.append(str2);
    paramXmlPullParser.append(">");
    throw new XmlPullParserException(paramXmlPullParser.toString());
    label511:
    if (str2.equals("byte-array"))
    {
      paramXmlPullParser = readThisByteArrayXml(paramXmlPullParser, "byte-array", paramArrayOfString);
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    if (str2.equals("int-array"))
    {
      paramXmlPullParser = readThisIntArrayXml(paramXmlPullParser, "int-array", paramArrayOfString);
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    if (str2.equals("long-array"))
    {
      paramXmlPullParser = readThisLongArrayXml(paramXmlPullParser, "long-array", paramArrayOfString);
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    if (str2.equals("double-array"))
    {
      paramXmlPullParser = readThisDoubleArrayXml(paramXmlPullParser, "double-array", paramArrayOfString);
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    if (str2.equals("string-array"))
    {
      paramXmlPullParser = readThisStringArrayXml(paramXmlPullParser, "string-array", paramArrayOfString);
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    if (str2.equals("boolean-array"))
    {
      paramXmlPullParser = readThisBooleanArrayXml(paramXmlPullParser, "boolean-array", paramArrayOfString);
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    if (str2.equals("map"))
    {
      paramXmlPullParser.next();
      if (paramBoolean) {
        paramXmlPullParser = readThisArrayMapXml(paramXmlPullParser, "map", paramArrayOfString, paramReadMapCallback);
      } else {
        paramXmlPullParser = readThisMapXml(paramXmlPullParser, "map", paramArrayOfString, paramReadMapCallback);
      }
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    if (str2.equals("list"))
    {
      paramXmlPullParser.next();
      paramXmlPullParser = readThisListXml(paramXmlPullParser, "list", paramArrayOfString, paramReadMapCallback, paramBoolean);
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    if (str2.equals("set"))
    {
      paramXmlPullParser.next();
      paramXmlPullParser = readThisSetXml(paramXmlPullParser, "set", paramArrayOfString, paramReadMapCallback, paramBoolean);
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    if (paramReadMapCallback != null)
    {
      paramXmlPullParser = paramReadMapCallback.readThisUnknownObjectXml(paramXmlPullParser, str2);
      paramArrayOfString[0] = str1;
      return paramXmlPullParser;
    }
    paramXmlPullParser = new StringBuilder();
    paramXmlPullParser.append("Unknown tag: ");
    paramXmlPullParser.append(str2);
    throw new XmlPullParserException(paramXmlPullParser.toString());
  }
  
  public static Uri readUriAttribute(XmlPullParser paramXmlPullParser, String paramString)
  {
    Object localObject = null;
    paramString = paramXmlPullParser.getAttributeValue(null, paramString);
    paramXmlPullParser = localObject;
    if (paramString != null) {
      paramXmlPullParser = Uri.parse(paramString);
    }
    return paramXmlPullParser;
  }
  
  public static final Object readValueXml(XmlPullParser paramXmlPullParser, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getEventType();
    do
    {
      if (i == 2) {
        return readThisValueXml(paramXmlPullParser, paramArrayOfString, null, false);
      }
      if (i == 3) {
        break label95;
      }
      if (i == 4) {
        break;
      }
      i = paramXmlPullParser.next();
    } while (i != 1);
    throw new XmlPullParserException("Unexpected end of document");
    paramArrayOfString = new StringBuilder();
    paramArrayOfString.append("Unexpected text: ");
    paramArrayOfString.append(paramXmlPullParser.getText());
    throw new XmlPullParserException(paramArrayOfString.toString());
    label95:
    paramArrayOfString = new StringBuilder();
    paramArrayOfString.append("Unexpected end tag at: ");
    paramArrayOfString.append(paramXmlPullParser.getName());
    throw new XmlPullParserException(paramArrayOfString.toString());
  }
  
  public static void skipCurrentTag(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    int j;
    do
    {
      j = paramXmlPullParser.next();
    } while ((j != 1) && ((j != 3) || (paramXmlPullParser.getDepth() > i)));
  }
  
  @Deprecated
  public static void writeBitmapAttribute(XmlSerializer paramXmlSerializer, String paramString, Bitmap paramBitmap)
    throws IOException
  {
    if (paramBitmap != null)
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      paramBitmap.compress(Bitmap.CompressFormat.PNG, 90, localByteArrayOutputStream);
      writeByteArrayAttribute(paramXmlSerializer, paramString, localByteArrayOutputStream.toByteArray());
    }
  }
  
  public static final void writeBooleanArrayXml(boolean[] paramArrayOfBoolean, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    if (paramArrayOfBoolean == null)
    {
      paramXmlSerializer.startTag(null, "null");
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    paramXmlSerializer.startTag(null, "boolean-array");
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    int i = paramArrayOfBoolean.length;
    paramXmlSerializer.attribute(null, "num", Integer.toString(i));
    for (int j = 0; j < i; j++)
    {
      paramXmlSerializer.startTag(null, "item");
      paramXmlSerializer.attribute(null, "value", Boolean.toString(paramArrayOfBoolean[j]));
      paramXmlSerializer.endTag(null, "item");
    }
    paramXmlSerializer.endTag(null, "boolean-array");
  }
  
  public static void writeBooleanAttribute(XmlSerializer paramXmlSerializer, String paramString, boolean paramBoolean)
    throws IOException
  {
    paramXmlSerializer.attribute(null, paramString, Boolean.toString(paramBoolean));
  }
  
  public static void writeByteArrayAttribute(XmlSerializer paramXmlSerializer, String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte != null) {
      paramXmlSerializer.attribute(null, paramString, Base64.encodeToString(paramArrayOfByte, 0));
    }
  }
  
  public static final void writeByteArrayXml(byte[] paramArrayOfByte, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    if (paramArrayOfByte == null)
    {
      paramXmlSerializer.startTag(null, "null");
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    paramXmlSerializer.startTag(null, "byte-array");
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    int i = paramArrayOfByte.length;
    paramXmlSerializer.attribute(null, "num", Integer.toString(i));
    paramString = new StringBuilder(paramArrayOfByte.length * 2);
    for (int j = 0; j < i; j++)
    {
      int k = paramArrayOfByte[j];
      int m = k >> 4 & 0xF;
      if (m >= 10) {
        m = 97 + m - 10;
      } else {
        m += 48;
      }
      paramString.append((char)m);
      m = k & 0xF;
      if (m >= 10) {
        m = 97 + m - 10;
      } else {
        m += 48;
      }
      paramString.append((char)m);
    }
    paramXmlSerializer.text(paramString.toString());
    paramXmlSerializer.endTag(null, "byte-array");
  }
  
  public static final void writeDoubleArrayXml(double[] paramArrayOfDouble, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    if (paramArrayOfDouble == null)
    {
      paramXmlSerializer.startTag(null, "null");
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    paramXmlSerializer.startTag(null, "double-array");
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    int i = paramArrayOfDouble.length;
    paramXmlSerializer.attribute(null, "num", Integer.toString(i));
    for (int j = 0; j < i; j++)
    {
      paramXmlSerializer.startTag(null, "item");
      paramXmlSerializer.attribute(null, "value", Double.toString(paramArrayOfDouble[j]));
      paramXmlSerializer.endTag(null, "item");
    }
    paramXmlSerializer.endTag(null, "double-array");
  }
  
  public static void writeFloatAttribute(XmlSerializer paramXmlSerializer, String paramString, float paramFloat)
    throws IOException
  {
    paramXmlSerializer.attribute(null, paramString, Float.toString(paramFloat));
  }
  
  public static final void writeIntArrayXml(int[] paramArrayOfInt, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    if (paramArrayOfInt == null)
    {
      paramXmlSerializer.startTag(null, "null");
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    paramXmlSerializer.startTag(null, "int-array");
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    int i = paramArrayOfInt.length;
    paramXmlSerializer.attribute(null, "num", Integer.toString(i));
    for (int j = 0; j < i; j++)
    {
      paramXmlSerializer.startTag(null, "item");
      paramXmlSerializer.attribute(null, "value", Integer.toString(paramArrayOfInt[j]));
      paramXmlSerializer.endTag(null, "item");
    }
    paramXmlSerializer.endTag(null, "int-array");
  }
  
  public static void writeIntAttribute(XmlSerializer paramXmlSerializer, String paramString, int paramInt)
    throws IOException
  {
    paramXmlSerializer.attribute(null, paramString, Integer.toString(paramInt));
  }
  
  public static final void writeListXml(List paramList, OutputStream paramOutputStream)
    throws XmlPullParserException, IOException
  {
    XmlSerializer localXmlSerializer = Xml.newSerializer();
    localXmlSerializer.setOutput(paramOutputStream, StandardCharsets.UTF_8.name());
    localXmlSerializer.startDocument(null, Boolean.valueOf(true));
    localXmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
    writeListXml(paramList, null, localXmlSerializer);
    localXmlSerializer.endDocument();
  }
  
  public static final void writeListXml(List paramList, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    if (paramList == null)
    {
      paramXmlSerializer.startTag(null, "null");
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    paramXmlSerializer.startTag(null, "list");
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    int i = paramList.size();
    for (int j = 0; j < i; j++) {
      writeValueXml(paramList.get(j), null, paramXmlSerializer);
    }
    paramXmlSerializer.endTag(null, "list");
  }
  
  public static final void writeLongArrayXml(long[] paramArrayOfLong, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    if (paramArrayOfLong == null)
    {
      paramXmlSerializer.startTag(null, "null");
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    paramXmlSerializer.startTag(null, "long-array");
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    int i = paramArrayOfLong.length;
    paramXmlSerializer.attribute(null, "num", Integer.toString(i));
    for (int j = 0; j < i; j++)
    {
      paramXmlSerializer.startTag(null, "item");
      paramXmlSerializer.attribute(null, "value", Long.toString(paramArrayOfLong[j]));
      paramXmlSerializer.endTag(null, "item");
    }
    paramXmlSerializer.endTag(null, "long-array");
  }
  
  public static void writeLongAttribute(XmlSerializer paramXmlSerializer, String paramString, long paramLong)
    throws IOException
  {
    paramXmlSerializer.attribute(null, paramString, Long.toString(paramLong));
  }
  
  public static final void writeMapXml(Map paramMap, OutputStream paramOutputStream)
    throws XmlPullParserException, IOException
  {
    FastXmlSerializer localFastXmlSerializer = new FastXmlSerializer();
    localFastXmlSerializer.setOutput(paramOutputStream, StandardCharsets.UTF_8.name());
    localFastXmlSerializer.startDocument(null, Boolean.valueOf(true));
    localFastXmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
    writeMapXml(paramMap, null, localFastXmlSerializer);
    localFastXmlSerializer.endDocument();
  }
  
  public static final void writeMapXml(Map paramMap, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    writeMapXml(paramMap, paramString, paramXmlSerializer, null);
  }
  
  public static final void writeMapXml(Map paramMap, String paramString, XmlSerializer paramXmlSerializer, WriteMapCallback paramWriteMapCallback)
    throws XmlPullParserException, IOException
  {
    if (paramMap == null)
    {
      paramXmlSerializer.startTag(null, "null");
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    paramXmlSerializer.startTag(null, "map");
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    writeMapXml(paramMap, paramXmlSerializer, paramWriteMapCallback);
    paramXmlSerializer.endTag(null, "map");
  }
  
  public static final void writeMapXml(Map paramMap, XmlSerializer paramXmlSerializer, WriteMapCallback paramWriteMapCallback)
    throws XmlPullParserException, IOException
  {
    if (paramMap == null) {
      return;
    }
    paramMap = paramMap.entrySet().iterator();
    while (paramMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramMap.next();
      writeValueXml(localEntry.getValue(), (String)localEntry.getKey(), paramXmlSerializer, paramWriteMapCallback);
    }
  }
  
  public static final void writeSetXml(Set paramSet, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    if (paramSet == null)
    {
      paramXmlSerializer.startTag(null, "null");
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    paramXmlSerializer.startTag(null, "set");
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    paramSet = paramSet.iterator();
    while (paramSet.hasNext()) {
      writeValueXml(paramSet.next(), null, paramXmlSerializer);
    }
    paramXmlSerializer.endTag(null, "set");
  }
  
  public static final void writeStringArrayXml(String[] paramArrayOfString, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    if (paramArrayOfString == null)
    {
      paramXmlSerializer.startTag(null, "null");
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    paramXmlSerializer.startTag(null, "string-array");
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    int i = paramArrayOfString.length;
    paramXmlSerializer.attribute(null, "num", Integer.toString(i));
    for (int j = 0; j < i; j++)
    {
      paramXmlSerializer.startTag(null, "item");
      paramXmlSerializer.attribute(null, "value", paramArrayOfString[j]);
      paramXmlSerializer.endTag(null, "item");
    }
    paramXmlSerializer.endTag(null, "string-array");
  }
  
  public static void writeStringAttribute(XmlSerializer paramXmlSerializer, String paramString, CharSequence paramCharSequence)
    throws IOException
  {
    if (paramCharSequence != null) {
      paramXmlSerializer.attribute(null, paramString, paramCharSequence.toString());
    }
  }
  
  public static void writeUriAttribute(XmlSerializer paramXmlSerializer, String paramString, Uri paramUri)
    throws IOException
  {
    if (paramUri != null) {
      paramXmlSerializer.attribute(null, paramString, paramUri.toString());
    }
  }
  
  public static final void writeValueXml(Object paramObject, String paramString, XmlSerializer paramXmlSerializer)
    throws XmlPullParserException, IOException
  {
    writeValueXml(paramObject, paramString, paramXmlSerializer, null);
  }
  
  private static final void writeValueXml(Object paramObject, String paramString, XmlSerializer paramXmlSerializer, WriteMapCallback paramWriteMapCallback)
    throws XmlPullParserException, IOException
  {
    if (paramObject == null)
    {
      paramXmlSerializer.startTag(null, "null");
      if (paramString != null) {
        paramXmlSerializer.attribute(null, "name", paramString);
      }
      paramXmlSerializer.endTag(null, "null");
      return;
    }
    if ((paramObject instanceof String))
    {
      paramXmlSerializer.startTag(null, "string");
      if (paramString != null) {
        paramXmlSerializer.attribute(null, "name", paramString);
      }
      paramXmlSerializer.text(paramObject.toString());
      paramXmlSerializer.endTag(null, "string");
      return;
    }
    if ((paramObject instanceof Integer)) {
      paramWriteMapCallback = "int";
    }
    for (;;)
    {
      break;
      if ((paramObject instanceof Long))
      {
        paramWriteMapCallback = "long";
      }
      else if ((paramObject instanceof Float))
      {
        paramWriteMapCallback = "float";
      }
      else if ((paramObject instanceof Double))
      {
        paramWriteMapCallback = "double";
      }
      else
      {
        if (!(paramObject instanceof Boolean)) {
          break label220;
        }
        paramWriteMapCallback = "boolean";
      }
    }
    paramXmlSerializer.startTag(null, paramWriteMapCallback);
    if (paramString != null) {
      paramXmlSerializer.attribute(null, "name", paramString);
    }
    paramXmlSerializer.attribute(null, "value", paramObject.toString());
    paramXmlSerializer.endTag(null, paramWriteMapCallback);
    return;
    label220:
    if ((paramObject instanceof byte[]))
    {
      writeByteArrayXml((byte[])paramObject, paramString, paramXmlSerializer);
      return;
    }
    if ((paramObject instanceof int[]))
    {
      writeIntArrayXml((int[])paramObject, paramString, paramXmlSerializer);
      return;
    }
    if ((paramObject instanceof long[]))
    {
      writeLongArrayXml((long[])paramObject, paramString, paramXmlSerializer);
      return;
    }
    if ((paramObject instanceof double[]))
    {
      writeDoubleArrayXml((double[])paramObject, paramString, paramXmlSerializer);
      return;
    }
    if ((paramObject instanceof String[]))
    {
      writeStringArrayXml((String[])paramObject, paramString, paramXmlSerializer);
      return;
    }
    if ((paramObject instanceof boolean[]))
    {
      writeBooleanArrayXml((boolean[])paramObject, paramString, paramXmlSerializer);
      return;
    }
    if ((paramObject instanceof Map))
    {
      writeMapXml((Map)paramObject, paramString, paramXmlSerializer);
      return;
    }
    if ((paramObject instanceof List))
    {
      writeListXml((List)paramObject, paramString, paramXmlSerializer);
      return;
    }
    if ((paramObject instanceof Set))
    {
      writeSetXml((Set)paramObject, paramString, paramXmlSerializer);
      return;
    }
    if ((paramObject instanceof CharSequence))
    {
      paramXmlSerializer.startTag(null, "string");
      if (paramString != null) {
        paramXmlSerializer.attribute(null, "name", paramString);
      }
      paramXmlSerializer.text(paramObject.toString());
      paramXmlSerializer.endTag(null, "string");
      return;
    }
    if (paramWriteMapCallback != null)
    {
      paramWriteMapCallback.writeUnknownObject(paramObject, paramString, paramXmlSerializer);
      return;
    }
    paramString = new StringBuilder();
    paramString.append("writeValueXml: unable to write value ");
    paramString.append(paramObject);
    throw new RuntimeException(paramString.toString());
  }
  
  public static abstract interface ReadMapCallback
  {
    public abstract Object readThisUnknownObjectXml(XmlPullParser paramXmlPullParser, String paramString)
      throws XmlPullParserException, IOException;
  }
  
  public static abstract interface WriteMapCallback
  {
    public abstract void writeUnknownObject(Object paramObject, String paramString, XmlSerializer paramXmlSerializer)
      throws XmlPullParserException, IOException;
  }
}
