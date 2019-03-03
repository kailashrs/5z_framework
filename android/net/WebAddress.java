package android.net;

import android.annotation.SystemApi;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SystemApi
public class WebAddress
{
  static final int MATCH_GROUP_AUTHORITY = 2;
  static final int MATCH_GROUP_HOST = 3;
  static final int MATCH_GROUP_PATH = 5;
  static final int MATCH_GROUP_PORT = 4;
  static final int MATCH_GROUP_SCHEME = 1;
  static Pattern sAddressPattern = Pattern.compile("(?:(http|https|file)\\:\\/\\/)?(?:([-A-Za-z0-9$_.+!*'(),;?&=]+(?:\\:[-A-Za-z0-9$_.+!*'(),;?&=]+)?)@)?([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯%_-][a-zA-Z0-9 -퟿豈-﷏ﷰ-￯%_\\.-]*|\\[[0-9a-fA-F:\\.]+\\])?(?:\\:([0-9]*))?(\\/?[^#]*)?.*", 2);
  private String mAuthInfo;
  private String mHost;
  private String mPath;
  private int mPort;
  private String mScheme;
  
  public WebAddress(String paramString)
    throws ParseException
  {
    if (paramString != null)
    {
      mScheme = "";
      mHost = "";
      mPort = -1;
      mPath = "/";
      mAuthInfo = "";
      paramString = sAddressPattern.matcher(paramString);
      if (paramString.matches())
      {
        String str = paramString.group(1);
        if (str != null) {
          mScheme = str.toLowerCase(Locale.ROOT);
        }
        str = paramString.group(2);
        if (str != null) {
          mAuthInfo = str;
        }
        str = paramString.group(3);
        if (str != null) {
          mHost = str;
        }
        str = paramString.group(4);
        if ((str != null) && (str.length() > 0)) {
          try
          {
            mPort = Integer.parseInt(str);
          }
          catch (NumberFormatException paramString)
          {
            throw new ParseException("Bad port");
          }
        }
        str = paramString.group(5);
        if ((str != null) && (str.length() > 0)) {
          if (str.charAt(0) == '/')
          {
            mPath = str;
          }
          else
          {
            paramString = new StringBuilder();
            paramString.append("/");
            paramString.append(str);
            mPath = paramString.toString();
          }
        }
        if ((mPort == 443) && (mScheme.equals(""))) {
          mScheme = "https";
        } else if (mPort == -1) {
          if (mScheme.equals("https")) {
            mPort = 443;
          } else {
            mPort = 80;
          }
        }
        if (mScheme.equals("")) {
          mScheme = "http";
        }
        return;
      }
      throw new ParseException("Bad address");
    }
    throw new NullPointerException();
  }
  
  public String getAuthInfo()
  {
    return mAuthInfo;
  }
  
  public String getHost()
  {
    return mHost;
  }
  
  public String getPath()
  {
    return mPath;
  }
  
  public int getPort()
  {
    return mPort;
  }
  
  public String getScheme()
  {
    return mScheme;
  }
  
  public void setAuthInfo(String paramString)
  {
    mAuthInfo = paramString;
  }
  
  public void setHost(String paramString)
  {
    mHost = paramString;
  }
  
  public void setPath(String paramString)
  {
    mPath = paramString;
  }
  
  public void setPort(int paramInt)
  {
    mPort = paramInt;
  }
  
  public void setScheme(String paramString)
  {
    mScheme = paramString;
  }
  
  public String toString()
  {
    Object localObject1 = "";
    Object localObject2;
    if ((mPort == 443) || (!mScheme.equals("https")))
    {
      localObject2 = localObject1;
      if (mPort != 80)
      {
        localObject2 = localObject1;
        if (!mScheme.equals("http")) {}
      }
    }
    else
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(":");
      ((StringBuilder)localObject2).append(Integer.toString(mPort));
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = "";
    if (mAuthInfo.length() > 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(mAuthInfo);
      ((StringBuilder)localObject1).append("@");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mScheme);
    localStringBuilder.append("://");
    localStringBuilder.append((String)localObject1);
    localStringBuilder.append(mHost);
    localStringBuilder.append((String)localObject2);
    localStringBuilder.append(mPath);
    return localStringBuilder.toString();
  }
}
