package android.sax;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RootElement
  extends Element
{
  final Handler handler = new Handler();
  
  public RootElement(String paramString)
  {
    this("", paramString);
  }
  
  public RootElement(String paramString1, String paramString2)
  {
    super(null, paramString1, paramString2, 0);
  }
  
  public ContentHandler getContentHandler()
  {
    return handler;
  }
  
  class Handler
    extends DefaultHandler
  {
    StringBuilder bodyBuilder = null;
    Element current = null;
    int depth = -1;
    Locator locator;
    
    Handler() {}
    
    public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
      throws SAXException
    {
      if (bodyBuilder != null) {
        bodyBuilder.append(paramArrayOfChar, paramInt1, paramInt2);
      }
    }
    
    public void endElement(String paramString1, String paramString2, String paramString3)
      throws SAXException
    {
      paramString1 = current;
      if (depth == depth)
      {
        paramString1.checkRequiredChildren(locator);
        if (endElementListener != null) {
          endElementListener.end();
        }
        if (bodyBuilder != null)
        {
          paramString2 = bodyBuilder.toString();
          bodyBuilder = null;
          endTextElementListener.end(paramString2);
        }
        current = parent;
      }
      depth -= 1;
    }
    
    public void setDocumentLocator(Locator paramLocator)
    {
      locator = paramLocator;
    }
    
    void start(Element paramElement, Attributes paramAttributes)
    {
      current = paramElement;
      if (startElementListener != null) {
        startElementListener.start(paramAttributes);
      }
      if (endTextElementListener != null) {
        bodyBuilder = new StringBuilder();
      }
      paramElement.resetRequiredChildren();
      visited = true;
    }
    
    public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
      throws SAXException
    {
      int i = depth + 1;
      depth = i;
      if (i == 0)
      {
        startRoot(paramString1, paramString2, paramAttributes);
        return;
      }
      if (bodyBuilder == null)
      {
        if (i == current.depth + 1)
        {
          paramString3 = current.children;
          if (paramString3 != null)
          {
            paramString1 = paramString3.get(paramString1, paramString2);
            if (paramString1 != null) {
              start(paramString1, paramAttributes);
            }
          }
        }
        return;
      }
      paramString1 = new StringBuilder();
      paramString1.append("Encountered mixed content within text element named ");
      paramString1.append(current);
      paramString1.append(".");
      throw new BadXmlException(paramString1.toString(), locator);
    }
    
    void startRoot(String paramString1, String paramString2, Attributes paramAttributes)
      throws SAXException
    {
      RootElement localRootElement = RootElement.this;
      if ((uri.compareTo(paramString1) == 0) && (localName.compareTo(paramString2) == 0))
      {
        start(localRootElement, paramAttributes);
        return;
      }
      paramAttributes = new StringBuilder();
      paramAttributes.append("Root element name does not match. Expected: ");
      paramAttributes.append(localRootElement);
      paramAttributes.append(", Got: ");
      paramAttributes.append(Element.toString(paramString1, paramString2));
      throw new BadXmlException(paramAttributes.toString(), locator);
    }
  }
}
