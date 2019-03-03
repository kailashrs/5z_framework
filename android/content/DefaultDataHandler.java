package android.content;

import android.net.Uri;
import android.util.Xml;
import android.util.Xml.Encoding;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class DefaultDataHandler
  implements ContentInsertHandler
{
  private static final String ARG = "arg";
  private static final String COL = "col";
  private static final String DEL = "del";
  private static final String POSTFIX = "postfix";
  private static final String ROW = "row";
  private static final String SELECT = "select";
  private static final String URI_STR = "uri";
  private ContentResolver mContentResolver;
  private Stack<Uri> mUris = new Stack();
  private ContentValues mValues;
  
  public DefaultDataHandler() {}
  
  private Uri insertRow()
  {
    Uri localUri = mContentResolver.insert((Uri)mUris.lastElement(), mValues);
    mValues = null;
    return localUri;
  }
  
  private void parseRow(Attributes paramAttributes)
    throws SAXException
  {
    Object localObject = paramAttributes.getValue("uri");
    if (localObject != null)
    {
      localObject = Uri.parse((String)localObject);
      if (localObject != null)
      {
        paramAttributes = (Attributes)localObject;
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("attribute ");
        ((StringBuilder)localObject).append(paramAttributes.getValue("uri"));
        ((StringBuilder)localObject).append(" parsing failure");
        throw new SAXException(((StringBuilder)localObject).toString());
      }
    }
    else
    {
      if (mUris.size() <= 0) {
        break label136;
      }
      paramAttributes = paramAttributes.getValue("postfix");
      if (paramAttributes != null) {
        paramAttributes = Uri.withAppendedPath((Uri)mUris.lastElement(), paramAttributes);
      } else {
        paramAttributes = (Uri)mUris.lastElement();
      }
    }
    mUris.push(paramAttributes);
    return;
    label136:
    throw new SAXException("attribute parsing failure");
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {}
  
  public void endDocument()
    throws SAXException
  {}
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    if ("row".equals(paramString2)) {
      if (!mUris.empty())
      {
        if (mValues != null) {
          insertRow();
        }
        mUris.pop();
      }
      else
      {
        throw new SAXException("uri mismatch");
      }
    }
  }
  
  public void endPrefixMapping(String paramString)
    throws SAXException
  {}
  
  public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {}
  
  public void insert(ContentResolver paramContentResolver, InputStream paramInputStream)
    throws IOException, SAXException
  {
    mContentResolver = paramContentResolver;
    Xml.parse(paramInputStream, Xml.Encoding.UTF_8, this);
  }
  
  public void insert(ContentResolver paramContentResolver, String paramString)
    throws SAXException
  {
    mContentResolver = paramContentResolver;
    Xml.parse(paramString, this);
  }
  
  public void processingInstruction(String paramString1, String paramString2)
    throws SAXException
  {}
  
  public void setDocumentLocator(Locator paramLocator) {}
  
  public void skippedEntity(String paramString)
    throws SAXException
  {}
  
  public void startDocument()
    throws SAXException
  {}
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if ("row".equals(paramString2))
    {
      if (mValues != null)
      {
        if (!mUris.empty())
        {
          paramString1 = insertRow();
          if (paramString1 != null)
          {
            mUris.pop();
            mUris.push(paramString1);
            parseRow(paramAttributes);
          }
          else
          {
            paramString1 = new StringBuilder();
            paramString1.append("insert to uri ");
            paramString1.append(((Uri)mUris.lastElement()).toString());
            paramString1.append(" failure");
            throw new SAXException(paramString1.toString());
          }
        }
        else
        {
          throw new SAXException("uri is empty");
        }
      }
      else if (paramAttributes.getLength() == 0) {
        mUris.push((Uri)mUris.lastElement());
      } else {
        parseRow(paramAttributes);
      }
    }
    else
    {
      boolean bool = "col".equals(paramString2);
      int i = 0;
      if (bool)
      {
        i = paramAttributes.getLength();
        if (i == 2)
        {
          paramString1 = paramAttributes.getValue(0);
          paramString2 = paramAttributes.getValue(1);
          if ((paramString1 != null) && (paramString1.length() > 0) && (paramString2 != null) && (paramString2.length() > 0))
          {
            if (mValues == null) {
              mValues = new ContentValues();
            }
            mValues.put(paramString1, paramString2);
          }
          else
          {
            throw new SAXException("illegal attributes value");
          }
        }
        else
        {
          paramString1 = new StringBuilder();
          paramString1.append("illegal attributes number ");
          paramString1.append(i);
          throw new SAXException(paramString1.toString());
        }
      }
      else
      {
        if (!"del".equals(paramString2)) {
          break label491;
        }
        paramString1 = Uri.parse(paramAttributes.getValue("uri"));
        if (paramString1 == null) {
          break label443;
        }
        int j = paramAttributes.getLength() - 2;
        if (j > 0)
        {
          paramString2 = new String[j];
          while (i < j)
          {
            paramString2[i] = paramAttributes.getValue(i + 2);
            i++;
          }
          mContentResolver.delete(paramString1, paramAttributes.getValue(1), paramString2);
        }
        else if (j == 0)
        {
          mContentResolver.delete(paramString1, paramAttributes.getValue(1), null);
        }
        else
        {
          mContentResolver.delete(paramString1, null, null);
        }
      }
    }
    return;
    label443:
    paramString1 = new StringBuilder();
    paramString1.append("attribute ");
    paramString1.append(paramAttributes.getValue("uri"));
    paramString1.append(" parsing failure");
    throw new SAXException(paramString1.toString());
    label491:
    paramString1 = new StringBuilder();
    paramString1.append("unknown element: ");
    paramString1.append(paramString2);
    throw new SAXException(paramString1.toString());
  }
  
  public void startPrefixMapping(String paramString1, String paramString2)
    throws SAXException
  {}
}
