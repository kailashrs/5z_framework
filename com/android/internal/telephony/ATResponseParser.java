package com.android.internal.telephony;

public class ATResponseParser
{
  private String mLine;
  private int mNext = 0;
  private int mTokEnd;
  private int mTokStart;
  
  public ATResponseParser(String paramString)
  {
    mLine = paramString;
  }
  
  private void nextTok()
  {
    int i = mLine.length();
    if (mNext == 0) {
      skipPrefix();
    }
    if (mNext < i) {
      try
      {
        Object localObject = mLine;
        int j = mNext;
        mNext = (j + 1);
        j = skipWhiteSpace(((String)localObject).charAt(j));
        if (j == 34)
        {
          if (mNext < i)
          {
            localObject = mLine;
            j = mNext;
            mNext = (j + 1);
            j = ((String)localObject).charAt(j);
            mTokStart = (mNext - 1);
            while ((j != 34) && (mNext < i))
            {
              localObject = mLine;
              j = mNext;
              mNext = (j + 1);
              j = ((String)localObject).charAt(j);
            }
            if (j == 34)
            {
              mTokEnd = (mNext - 1);
              if (mNext < i)
              {
                localObject = mLine;
                j = mNext;
                mNext = (j + 1);
                if (((String)localObject).charAt(j) != ',')
                {
                  localObject = new com/android/internal/telephony/ATParseEx;
                  ((ATParseEx)localObject).<init>();
                  throw ((Throwable)localObject);
                }
              }
            }
            else
            {
              localObject = new com/android/internal/telephony/ATParseEx;
              ((ATParseEx)localObject).<init>();
              throw ((Throwable)localObject);
            }
          }
          else
          {
            localObject = new com/android/internal/telephony/ATParseEx;
            ((ATParseEx)localObject).<init>();
            throw ((Throwable)localObject);
          }
        }
        else
        {
          mTokStart = (mNext - 1);
          mTokEnd = mTokStart;
          for (int k = j; k != 44; k = j)
          {
            if (!Character.isWhitespace(k)) {
              mTokEnd = mNext;
            }
            if (mNext == i) {
              break;
            }
            localObject = mLine;
            j = mNext;
            mNext = (j + 1);
            j = ((String)localObject).charAt(j);
          }
        }
        return;
      }
      catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
      {
        throw new ATParseEx();
      }
    }
    throw new ATParseEx();
  }
  
  private void skipPrefix()
  {
    mNext = 0;
    int i = mLine.length();
    while (mNext < i)
    {
      String str = mLine;
      int j = mNext;
      mNext = (j + 1);
      if (str.charAt(j) == ':') {
        return;
      }
    }
    throw new ATParseEx("missing prefix");
  }
  
  private char skipWhiteSpace(char paramChar)
  {
    int i = mLine.length();
    for (char c = paramChar; (mNext < i) && (Character.isWhitespace(c)); c = paramChar)
    {
      String str = mLine;
      paramChar = mNext;
      mNext = (paramChar + '\001');
      paramChar = str.charAt(paramChar);
    }
    if (!Character.isWhitespace(c)) {
      return c;
    }
    throw new ATParseEx();
  }
  
  public boolean hasMore()
  {
    boolean bool;
    if (mNext < mLine.length()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean nextBoolean()
  {
    nextTok();
    if (mTokEnd - mTokStart <= 1)
    {
      int i = mLine.charAt(mTokStart);
      if (i == 48) {
        return false;
      }
      if (i == 49) {
        return true;
      }
      throw new ATParseEx();
    }
    throw new ATParseEx();
  }
  
  public int nextInt()
  {
    int i = 0;
    nextTok();
    int j = mTokStart;
    while (j < mTokEnd)
    {
      int k = mLine.charAt(j);
      if ((k >= 48) && (k <= 57))
      {
        i = i * 10 + (k - 48);
        j++;
      }
      else
      {
        throw new ATParseEx();
      }
    }
    return i;
  }
  
  public String nextString()
  {
    nextTok();
    return mLine.substring(mTokStart, mTokEnd);
  }
}
