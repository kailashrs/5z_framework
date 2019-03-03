package android.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MailTo
{
  private static final String BODY = "body";
  private static final String CC = "cc";
  public static final String MAILTO_SCHEME = "mailto:";
  private static final String SUBJECT = "subject";
  private static final String TO = "to";
  private HashMap<String, String> mHeaders = new HashMap();
  
  private MailTo() {}
  
  public static boolean isMailTo(String paramString)
  {
    return (paramString != null) && (paramString.startsWith("mailto:"));
  }
  
  public static MailTo parse(String paramString)
    throws ParseException
  {
    if (paramString != null)
    {
      if (isMailTo(paramString))
      {
        Object localObject1 = Uri.parse(paramString.substring("mailto:".length()));
        MailTo localMailTo = new MailTo();
        paramString = ((Uri)localObject1).getQuery();
        Object localObject2;
        if (paramString != null)
        {
          String[] arrayOfString = paramString.split("&");
          int i = arrayOfString.length;
          for (int j = 0; j < i; j++)
          {
            paramString = arrayOfString[j].split("=");
            if (paramString.length != 0)
            {
              localObject2 = mHeaders;
              String str = Uri.decode(paramString[0]).toLowerCase(Locale.ROOT);
              if (paramString.length > 1) {
                paramString = Uri.decode(paramString[1]);
              } else {
                paramString = null;
              }
              ((HashMap)localObject2).put(str, paramString);
            }
          }
        }
        localObject1 = ((Uri)localObject1).getPath();
        if (localObject1 != null)
        {
          localObject2 = localMailTo.getTo();
          paramString = (String)localObject1;
          if (localObject2 != null)
          {
            paramString = new StringBuilder();
            paramString.append((String)localObject1);
            paramString.append(", ");
            paramString.append((String)localObject2);
            paramString = paramString.toString();
          }
          mHeaders.put("to", paramString);
        }
        return localMailTo;
      }
      throw new ParseException("Not a mailto scheme");
    }
    throw new NullPointerException();
  }
  
  public String getBody()
  {
    return (String)mHeaders.get("body");
  }
  
  public String getCc()
  {
    return (String)mHeaders.get("cc");
  }
  
  public Map<String, String> getHeaders()
  {
    return mHeaders;
  }
  
  public String getSubject()
  {
    return (String)mHeaders.get("subject");
  }
  
  public String getTo()
  {
    return (String)mHeaders.get("to");
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("mailto:");
    localStringBuilder.append('?');
    Iterator localIterator = mHeaders.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localStringBuilder.append(Uri.encode((String)localEntry.getKey()));
      localStringBuilder.append('=');
      localStringBuilder.append(Uri.encode((String)localEntry.getValue()));
      localStringBuilder.append('&');
    }
    return localStringBuilder.toString();
  }
}
