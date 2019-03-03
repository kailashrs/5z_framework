package android.text.util;

public class Rfc822Token
{
  private String mAddress;
  private String mComment;
  private String mName;
  
  public Rfc822Token(String paramString1, String paramString2, String paramString3)
  {
    mName = paramString1;
    mAddress = paramString2;
    mComment = paramString3;
  }
  
  public static String quoteComment(String paramString)
  {
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      if ((c == '(') || (c == ')') || (c == '\\')) {
        localStringBuilder.append('\\');
      }
      localStringBuilder.append(c);
    }
    return localStringBuilder.toString();
  }
  
  public static String quoteName(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      if ((c == '\\') || (c == '"')) {
        localStringBuilder.append('\\');
      }
      localStringBuilder.append(c);
    }
    return localStringBuilder.toString();
  }
  
  public static String quoteNameIfNecessary(String paramString)
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      if (((k < 65) || (k > 90)) && ((k < 97) || (k > 122)) && (k != 32) && ((k < 48) || (k > 57)))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append('"');
        localStringBuilder.append(quoteName(paramString));
        localStringBuilder.append('"');
        return localStringBuilder.toString();
      }
    }
    return paramString;
  }
  
  private static boolean stringEquals(String paramString1, String paramString2)
  {
    if (paramString1 == null)
    {
      boolean bool;
      if (paramString2 == null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    return paramString1.equals(paramString2);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof Rfc822Token;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (Rfc822Token)paramObject;
    bool1 = bool2;
    if (stringEquals(mName, mName))
    {
      bool1 = bool2;
      if (stringEquals(mAddress, mAddress))
      {
        bool1 = bool2;
        if (stringEquals(mComment, mComment)) {
          bool1 = true;
        }
      }
    }
    return bool1;
  }
  
  public String getAddress()
  {
    return mAddress;
  }
  
  public String getComment()
  {
    return mComment;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int hashCode()
  {
    int i = 17;
    if (mName != null) {
      i = 31 * 17 + mName.hashCode();
    }
    int j = i;
    if (mAddress != null) {
      j = 31 * i + mAddress.hashCode();
    }
    i = j;
    if (mComment != null) {
      i = 31 * j + mComment.hashCode();
    }
    return i;
  }
  
  public void setAddress(String paramString)
  {
    mAddress = paramString;
  }
  
  public void setComment(String paramString)
  {
    mComment = paramString;
  }
  
  public void setName(String paramString)
  {
    mName = paramString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((mName != null) && (mName.length() != 0))
    {
      localStringBuilder.append(quoteNameIfNecessary(mName));
      localStringBuilder.append(' ');
    }
    if ((mComment != null) && (mComment.length() != 0))
    {
      localStringBuilder.append('(');
      localStringBuilder.append(quoteComment(mComment));
      localStringBuilder.append(") ");
    }
    if ((mAddress != null) && (mAddress.length() != 0))
    {
      localStringBuilder.append('<');
      localStringBuilder.append(mAddress);
      localStringBuilder.append('>');
    }
    return localStringBuilder.toString();
  }
}
