package android.sax;

import java.util.ArrayList;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

public class Element
{
  Children children;
  final int depth;
  EndElementListener endElementListener;
  EndTextElementListener endTextElementListener;
  final String localName;
  final Element parent;
  ArrayList<Element> requiredChilden;
  StartElementListener startElementListener;
  final String uri;
  boolean visited;
  
  Element(Element paramElement, String paramString1, String paramString2, int paramInt)
  {
    parent = paramElement;
    uri = paramString1;
    localName = paramString2;
    depth = paramInt;
  }
  
  static String toString(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("'");
    if (paramString1.equals(""))
    {
      paramString1 = paramString2;
    }
    else
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(paramString1);
      localStringBuilder2.append(":");
      localStringBuilder2.append(paramString2);
      paramString1 = localStringBuilder2.toString();
    }
    localStringBuilder1.append(paramString1);
    localStringBuilder1.append("'");
    return localStringBuilder1.toString();
  }
  
  void checkRequiredChildren(Locator paramLocator)
    throws SAXParseException
  {
    Object localObject = requiredChilden;
    if (localObject != null)
    {
      int i = ((ArrayList)localObject).size() - 1;
      while (i >= 0)
      {
        Element localElement = (Element)((ArrayList)localObject).get(i);
        if (visited)
        {
          i--;
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Element named ");
          ((StringBuilder)localObject).append(this);
          ((StringBuilder)localObject).append(" is missing required child element named ");
          ((StringBuilder)localObject).append(localElement);
          ((StringBuilder)localObject).append(".");
          throw new BadXmlException(((StringBuilder)localObject).toString(), paramLocator);
        }
      }
    }
  }
  
  public Element getChild(String paramString)
  {
    return getChild("", paramString);
  }
  
  public Element getChild(String paramString1, String paramString2)
  {
    if (endTextElementListener == null)
    {
      if (children == null) {
        children = new Children();
      }
      return children.getOrCreate(this, paramString1, paramString2);
    }
    throw new IllegalStateException("This element already has an end text element listener. It cannot have children.");
  }
  
  public Element requireChild(String paramString)
  {
    return requireChild("", paramString);
  }
  
  public Element requireChild(String paramString1, String paramString2)
  {
    paramString1 = getChild(paramString1, paramString2);
    if (requiredChilden == null)
    {
      requiredChilden = new ArrayList();
      requiredChilden.add(paramString1);
    }
    else if (!requiredChilden.contains(paramString1))
    {
      requiredChilden.add(paramString1);
    }
    return paramString1;
  }
  
  void resetRequiredChildren()
  {
    ArrayList localArrayList = requiredChilden;
    if (localArrayList != null) {
      for (int i = localArrayList.size() - 1; i >= 0; i--) {
        getvisited = false;
      }
    }
  }
  
  public void setElementListener(ElementListener paramElementListener)
  {
    setStartElementListener(paramElementListener);
    setEndElementListener(paramElementListener);
  }
  
  public void setEndElementListener(EndElementListener paramEndElementListener)
  {
    if (endElementListener == null)
    {
      endElementListener = paramEndElementListener;
      return;
    }
    throw new IllegalStateException("End element listener has already been set.");
  }
  
  public void setEndTextElementListener(EndTextElementListener paramEndTextElementListener)
  {
    if (endTextElementListener == null)
    {
      if (children == null)
      {
        endTextElementListener = paramEndTextElementListener;
        return;
      }
      throw new IllegalStateException("This element already has children. It cannot have an end text element listener.");
    }
    throw new IllegalStateException("End text element listener has already been set.");
  }
  
  public void setStartElementListener(StartElementListener paramStartElementListener)
  {
    if (startElementListener == null)
    {
      startElementListener = paramStartElementListener;
      return;
    }
    throw new IllegalStateException("Start element listener has already been set.");
  }
  
  public void setTextElementListener(TextElementListener paramTextElementListener)
  {
    setStartElementListener(paramTextElementListener);
    setEndTextElementListener(paramTextElementListener);
  }
  
  public String toString()
  {
    return toString(uri, localName);
  }
}
