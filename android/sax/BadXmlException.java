package android.sax;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

class BadXmlException
  extends SAXParseException
{
  public BadXmlException(String paramString, Locator paramLocator)
  {
    super(paramString, paramLocator);
  }
  
  public String getMessage()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Line ");
    localStringBuilder.append(getLineNumber());
    localStringBuilder.append(": ");
    localStringBuilder.append(super.getMessage());
    return localStringBuilder.toString();
  }
}
