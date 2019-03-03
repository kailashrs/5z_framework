package android.filterfw.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternScanner
{
  private Pattern mIgnorePattern;
  private String mInput;
  private int mLineNo = 0;
  private int mOffset = 0;
  private int mStartOfLine = 0;
  
  public PatternScanner(String paramString)
  {
    mInput = paramString;
  }
  
  public PatternScanner(String paramString, Pattern paramPattern)
  {
    mInput = paramString;
    mIgnorePattern = paramPattern;
    skip(mIgnorePattern);
  }
  
  public boolean atEnd()
  {
    boolean bool;
    if (mOffset >= mInput.length()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String eat(Pattern paramPattern, String paramString)
  {
    paramPattern = tryEat(paramPattern);
    if (paramPattern != null) {
      return paramPattern;
    }
    throw new RuntimeException(unexpectedTokenMessage(paramString));
  }
  
  public int lineNo()
  {
    return mLineNo;
  }
  
  public boolean peek(Pattern paramPattern)
  {
    if (mIgnorePattern != null) {
      skip(mIgnorePattern);
    }
    paramPattern = paramPattern.matcher(mInput);
    paramPattern.region(mOffset, mInput.length());
    return paramPattern.lookingAt();
  }
  
  public void skip(Pattern paramPattern)
  {
    paramPattern = paramPattern.matcher(mInput);
    paramPattern.region(mOffset, mInput.length());
    if (paramPattern.lookingAt())
    {
      updateLineCount(mOffset, paramPattern.end());
      mOffset = paramPattern.end();
    }
  }
  
  public String tryEat(Pattern paramPattern)
  {
    if (mIgnorePattern != null) {
      skip(mIgnorePattern);
    }
    Matcher localMatcher = paramPattern.matcher(mInput);
    localMatcher.region(mOffset, mInput.length());
    paramPattern = null;
    if (localMatcher.lookingAt())
    {
      updateLineCount(mOffset, localMatcher.end());
      mOffset = localMatcher.end();
      paramPattern = mInput.substring(localMatcher.start(), localMatcher.end());
    }
    if ((paramPattern != null) && (mIgnorePattern != null)) {
      skip(mIgnorePattern);
    }
    return paramPattern;
  }
  
  public String unexpectedTokenMessage(String paramString)
  {
    String str = mInput.substring(mStartOfLine, mOffset);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unexpected token on line ");
    localStringBuilder.append(mLineNo + 1);
    localStringBuilder.append(" after '");
    localStringBuilder.append(str);
    localStringBuilder.append("' <- Expected ");
    localStringBuilder.append(paramString);
    localStringBuilder.append("!");
    return localStringBuilder.toString();
  }
  
  public void updateLineCount(int paramInt1, int paramInt2)
  {
    while (paramInt1 < paramInt2)
    {
      if (mInput.charAt(paramInt1) == '\n')
      {
        mLineNo += 1;
        mStartOfLine = (paramInt1 + 1);
      }
      paramInt1++;
    }
  }
}
