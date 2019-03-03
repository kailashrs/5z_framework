package com.android.internal.alsa;

public class LineTokenizer
{
  public static final int kTokenNotFound = -1;
  private final String mDelimiters;
  
  public LineTokenizer(String paramString)
  {
    mDelimiters = paramString;
  }
  
  int nextDelimiter(String paramString, int paramInt)
  {
    int i = paramString.length();
    int j;
    for (;;)
    {
      j = -1;
      if ((paramInt >= i) || (mDelimiters.indexOf(paramString.charAt(paramInt)) != -1)) {
        break;
      }
      paramInt++;
    }
    if (paramInt < i) {
      j = paramInt;
    }
    return j;
  }
  
  int nextToken(String paramString, int paramInt)
  {
    int i = paramString.length();
    int j;
    for (;;)
    {
      j = -1;
      if ((paramInt >= i) || (mDelimiters.indexOf(paramString.charAt(paramInt)) == -1)) {
        break;
      }
      paramInt++;
    }
    if (paramInt < i) {
      j = paramInt;
    }
    return j;
  }
}
