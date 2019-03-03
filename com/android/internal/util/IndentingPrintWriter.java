package com.android.internal.util;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;

public class IndentingPrintWriter
  extends PrintWriter
{
  private char[] mCurrentIndent;
  private int mCurrentLength;
  private boolean mEmptyLine = true;
  private StringBuilder mIndentBuilder = new StringBuilder();
  private char[] mSingleChar = new char[1];
  private final String mSingleIndent;
  private final int mWrapLength;
  
  public IndentingPrintWriter(Writer paramWriter, String paramString)
  {
    this(paramWriter, paramString, -1);
  }
  
  public IndentingPrintWriter(Writer paramWriter, String paramString, int paramInt)
  {
    super(paramWriter);
    mSingleIndent = paramString;
    mWrapLength = paramInt;
  }
  
  private void maybeWriteIndent()
  {
    if (mEmptyLine)
    {
      mEmptyLine = false;
      if (mIndentBuilder.length() != 0)
      {
        if (mCurrentIndent == null) {
          mCurrentIndent = mIndentBuilder.toString().toCharArray();
        }
        super.write(mCurrentIndent, 0, mCurrentIndent.length);
      }
    }
  }
  
  public IndentingPrintWriter decreaseIndent()
  {
    mIndentBuilder.delete(0, mSingleIndent.length());
    mCurrentIndent = null;
    return this;
  }
  
  public IndentingPrintWriter increaseIndent()
  {
    mIndentBuilder.append(mSingleIndent);
    mCurrentIndent = null;
    return this;
  }
  
  public IndentingPrintWriter printHexPair(String paramString, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("=0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    localStringBuilder.append(" ");
    print(localStringBuilder.toString());
    return this;
  }
  
  public IndentingPrintWriter printPair(String paramString, Object paramObject)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("=");
    localStringBuilder.append(String.valueOf(paramObject));
    localStringBuilder.append(" ");
    print(localStringBuilder.toString());
    return this;
  }
  
  public IndentingPrintWriter printPair(String paramString, Object[] paramArrayOfObject)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("=");
    localStringBuilder.append(Arrays.toString(paramArrayOfObject));
    localStringBuilder.append(" ");
    print(localStringBuilder.toString());
    return this;
  }
  
  public void println()
  {
    write(10);
  }
  
  public IndentingPrintWriter setIndent(int paramInt)
  {
    StringBuilder localStringBuilder = mIndentBuilder;
    int i = 0;
    localStringBuilder.setLength(0);
    while (i < paramInt)
    {
      increaseIndent();
      i++;
    }
    return this;
  }
  
  public IndentingPrintWriter setIndent(String paramString)
  {
    mIndentBuilder.setLength(0);
    mIndentBuilder.append(paramString);
    mCurrentIndent = null;
    return this;
  }
  
  public void write(int paramInt)
  {
    mSingleChar[0] = ((char)(char)paramInt);
    write(mSingleChar, 0, 1);
  }
  
  public void write(String paramString, int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2];
    paramString.getChars(paramInt1, paramInt2 - paramInt1, arrayOfChar, 0);
    write(arrayOfChar, 0, paramInt2);
  }
  
  public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = mIndentBuilder.length();
    int j = paramInt1;
    int k = j;
    for (int m = j; m < paramInt1 + paramInt2; m = j)
    {
      j = m + 1;
      int n = paramArrayOfChar[m];
      mCurrentLength += 1;
      m = k;
      if (n == 10)
      {
        maybeWriteIndent();
        super.write(paramArrayOfChar, k, j - k);
        m = j;
        mEmptyLine = true;
        mCurrentLength = 0;
      }
      k = m;
      if (mWrapLength > 0)
      {
        k = m;
        if (mCurrentLength >= mWrapLength - i) {
          if (!mEmptyLine)
          {
            super.write(10);
            mEmptyLine = true;
            mCurrentLength = (j - m);
            k = m;
          }
          else
          {
            maybeWriteIndent();
            super.write(paramArrayOfChar, m, j - m);
            super.write(10);
            mEmptyLine = true;
            k = j;
            mCurrentLength = 0;
          }
        }
      }
    }
    if (k != m)
    {
      maybeWriteIndent();
      super.write(paramArrayOfChar, k, m - k);
    }
  }
}
