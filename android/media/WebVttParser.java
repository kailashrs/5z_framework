package android.media;

import android.util.Log;
import java.util.Vector;

class WebVttParser
{
  private static final String TAG = "WebVttParser";
  private String mBuffer = "";
  private TextTrackCue mCue;
  private Vector<String> mCueTexts;
  private WebVttCueListener mListener;
  private final Phase mParseCueId = new Phase()
  {
    public void parse(String paramAnonymousString)
    {
      if (paramAnonymousString.length() == 0) {
        return;
      }
      if ((paramAnonymousString.equals("NOTE")) || (paramAnonymousString.startsWith("NOTE "))) {
        WebVttParser.access$102(WebVttParser.this, mParseCueText);
      }
      WebVttParser.access$902(WebVttParser.this, new TextTrackCue());
      mCueTexts.clear();
      WebVttParser.access$102(WebVttParser.this, mParseCueTime);
      if (paramAnonymousString.contains("-->")) {
        mPhase.parse(paramAnonymousString);
      } else {
        mCue.mId = paramAnonymousString;
      }
    }
  };
  private final Phase mParseCueText = new Phase()
  {
    public void parse(String paramAnonymousString)
    {
      if (paramAnonymousString.length() == 0)
      {
        yieldCue();
        WebVttParser.access$102(WebVttParser.this, mParseCueId);
        return;
      }
      if (mCue != null) {
        mCueTexts.add(paramAnonymousString);
      }
    }
  };
  private final Phase mParseCueTime = new Phase()
  {
    public void parse(String paramAnonymousString)
    {
      int i = paramAnonymousString.indexOf("-->");
      if (i < 0)
      {
        WebVttParser.access$902(WebVttParser.this, null);
        WebVttParser.access$102(WebVttParser.this, mParseCueId);
        return;
      }
      String str1 = paramAnonymousString.substring(0, i).trim();
      String str2 = paramAnonymousString.substring(i + 3).replaceFirst("^\\s+", "").replaceFirst("\\s+", " ");
      i = str2.indexOf(' ');
      if (i > 0) {
        paramAnonymousString = str2.substring(0, i);
      } else {
        paramAnonymousString = str2;
      }
      if (i > 0) {
        str2 = str2.substring(i + 1);
      } else {
        str2 = "";
      }
      mCue.mStartTimeMs = WebVttParser.parseTimestampMs(str1);
      mCue.mEndTimeMs = WebVttParser.parseTimestampMs(paramAnonymousString);
      for (str1 : str2.split(" +"))
      {
        int k = str1.indexOf(':');
        if ((k > 0) && (k != str1.length() - 1))
        {
          str2 = str1.substring(0, k);
          str1 = str1.substring(k + 1);
          if (str2.equals("region")) {
            mCue.mRegionId = str1;
          } else if (str2.equals("vertical"))
          {
            if (str1.equals("rl")) {
              mCue.mWritingDirection = 101;
            } else if (str1.equals("lr")) {
              mCue.mWritingDirection = 102;
            } else {
              WebVttParser.this.log_warning("cue setting", str2, "has invalid value", str1);
            }
          }
          else if (str2.equals("line")) {
            try
            {
              if (str1.endsWith("%"))
              {
                mCue.mSnapToLines = false;
                mCue.mLinePosition = Integer.valueOf(WebVttParser.parseIntPercentage(str1));
              }
              else if (str1.matches(".*[^0-9].*"))
              {
                WebVttParser.this.log_warning("cue setting", str2, "contains an invalid character", str1);
              }
              else
              {
                mCue.mSnapToLines = true;
                mCue.mLinePosition = Integer.valueOf(Integer.parseInt(str1));
              }
            }
            catch (NumberFormatException localNumberFormatException1)
            {
              WebVttParser.this.log_warning("cue setting", str2, "is not numeric or percentage", str1);
            }
          } else if (str2.equals("position")) {
            try
            {
              mCue.mTextPosition = WebVttParser.parseIntPercentage(str1);
            }
            catch (NumberFormatException localNumberFormatException2)
            {
              WebVttParser.this.log_warning("cue setting", str2, "is not numeric or percentage", str1);
            }
          } else if (str2.equals("size")) {
            try
            {
              mCue.mSize = WebVttParser.parseIntPercentage(str1);
            }
            catch (NumberFormatException localNumberFormatException3)
            {
              WebVttParser.this.log_warning("cue setting", str2, "is not numeric or percentage", str1);
            }
          } else if (str2.equals("align")) {
            if (str1.equals("start")) {
              mCue.mAlignment = 201;
            } else if (str1.equals("middle")) {
              mCue.mAlignment = 200;
            } else if (str1.equals("end")) {
              mCue.mAlignment = 202;
            } else if (str1.equals("left")) {
              mCue.mAlignment = 203;
            } else if (str1.equals("right")) {
              mCue.mAlignment = 204;
            } else {
              WebVttParser.this.log_warning("cue setting", str2, "has invalid value", str1);
            }
          }
        }
      }
      if ((mCue.mLinePosition != null) || (mCue.mSize != 100) || (mCue.mWritingDirection != 100)) {
        mCue.mRegionId = "";
      }
      WebVttParser.access$102(WebVttParser.this, mParseCueText);
    }
  };
  private final Phase mParseHeader = new Phase()
  {
    public void parse(String paramAnonymousString)
    {
      if (paramAnonymousString.length() == 0)
      {
        WebVttParser.access$102(WebVttParser.this, mParseCueId);
      }
      else if (paramAnonymousString.contains("-->"))
      {
        WebVttParser.access$102(WebVttParser.this, mParseCueTime);
        mPhase.parse(paramAnonymousString);
      }
      else
      {
        int i = paramAnonymousString.indexOf(':');
        if ((i <= 0) || (i >= paramAnonymousString.length() - 1)) {
          WebVttParser.this.log_warning("meta data header has invalid format", paramAnonymousString);
        }
        String str = paramAnonymousString.substring(0, i);
        paramAnonymousString = paramAnonymousString.substring(i + 1);
        if (str.equals("Region"))
        {
          paramAnonymousString = parseRegion(paramAnonymousString);
          mListener.onRegionParsed(paramAnonymousString);
        }
      }
    }
    
    TextTrackRegion parseRegion(String paramAnonymousString)
    {
      TextTrackRegion localTextTrackRegion = new TextTrackRegion();
      paramAnonymousString = paramAnonymousString.split(" +");
      int i = paramAnonymousString.length;
      int j = 0;
      int k = 0;
      label469:
      while (k < i)
      {
        String str1 = paramAnonymousString[k];
        int m = str1.indexOf('=');
        if ((m > 0) && (m != str1.length() - 1))
        {
          String str2 = str1.substring(j, m);
          str1 = str1.substring(m + 1);
          if (str2.equals("id")) {
            mId = str1;
          } else if (str2.equals("width")) {
            try
            {
              mWidth = WebVttParser.parseFloatPercentage(str1);
            }
            catch (NumberFormatException localNumberFormatException2)
            {
              WebVttParser.this.log_warning("region setting", str2, "has invalid value", localNumberFormatException2.getMessage(), str1);
            }
          } else {
            for (;;)
            {
              j = 0;
              break label469;
              if (str2.equals("lines"))
              {
                if (str1.matches(".*[^0-9].*")) {
                  WebVttParser.this.log_warning("lines", str2, "contains an invalid character", str1);
                } else {
                  try
                  {
                    mLines = Integer.parseInt(str1);
                  }
                  catch (NumberFormatException localNumberFormatException3)
                  {
                    WebVttParser.this.log_warning("region setting", str2, "is not numeric", str1);
                  }
                }
              }
              else if ((!str2.equals("regionanchor")) && (!str2.equals("viewportanchor")))
              {
                if (str2.equals("scroll")) {
                  if (str1.equals("up")) {
                    mScrollValue = 301;
                  } else {
                    WebVttParser.this.log_warning("region setting", str2, "has invalid value", str1);
                  }
                }
              }
              else
              {
                j = str1.indexOf(",");
                if (j < 0)
                {
                  WebVttParser.this.log_warning("region setting", str2, "contains no comma", str1);
                }
                else
                {
                  Object localObject = str1.substring(0, j);
                  str1 = str1.substring(j + 1);
                  try
                  {
                    float f1 = WebVttParser.parseFloatPercentage((String)localObject);
                    try
                    {
                      float f2 = WebVttParser.parseFloatPercentage(str1);
                      if (str2.charAt(0) == 'r')
                      {
                        mAnchorPointX = f1;
                        mAnchorPointY = f2;
                      }
                      else
                      {
                        mViewportAnchorPointX = f1;
                        mViewportAnchorPointY = f2;
                      }
                    }
                    catch (NumberFormatException localNumberFormatException4)
                    {
                      localObject = WebVttParser.this;
                      String str3 = localNumberFormatException4.getMessage();
                      j = 0;
                      ((WebVttParser)localObject).log_warning("region setting", str2, "has invalid y component", str3, str1);
                    }
                    k++;
                  }
                  catch (NumberFormatException localNumberFormatException1)
                  {
                    j = 0;
                    WebVttParser.this.log_warning("region setting", str2, "has invalid x component", localNumberFormatException1.getMessage(), (String)localObject);
                  }
                }
              }
            }
          }
        }
      }
      return localTextTrackRegion;
    }
  };
  private final Phase mParseStart = new Phase()
  {
    public void parse(String paramAnonymousString)
    {
      String str = paramAnonymousString;
      if (paramAnonymousString.startsWith("﻿")) {
        str = paramAnonymousString.substring(1);
      }
      if ((!str.equals("WEBVTT")) && (!str.startsWith("WEBVTT ")) && (!str.startsWith("WEBVTT\t")))
      {
        WebVttParser.this.log_warning("Not a WEBVTT header", str);
        WebVttParser.access$102(WebVttParser.this, mSkipRest);
      }
      else
      {
        WebVttParser.access$102(WebVttParser.this, mParseHeader);
      }
    }
  };
  private Phase mPhase = mParseStart;
  private final Phase mSkipRest = new Phase()
  {
    public void parse(String paramAnonymousString) {}
  };
  
  WebVttParser(WebVttCueListener paramWebVttCueListener)
  {
    mListener = paramWebVttCueListener;
    mCueTexts = new Vector();
  }
  
  private void log_warning(String paramString1, String paramString2)
  {
    String str = getClass().getName();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" ('");
    localStringBuilder.append(paramString2);
    localStringBuilder.append("')");
    Log.w(str, localStringBuilder.toString());
  }
  
  private void log_warning(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    String str = getClass().getName();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" '");
    localStringBuilder.append(paramString2);
    localStringBuilder.append("' ");
    localStringBuilder.append(paramString3);
    localStringBuilder.append(" ('");
    localStringBuilder.append(paramString4);
    localStringBuilder.append("')");
    Log.w(str, localStringBuilder.toString());
  }
  
  private void log_warning(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    String str = getClass().getName();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" '");
    localStringBuilder.append(paramString2);
    localStringBuilder.append("' ");
    localStringBuilder.append(paramString3);
    localStringBuilder.append(" ('");
    localStringBuilder.append(paramString5);
    localStringBuilder.append("' ");
    localStringBuilder.append(paramString4);
    localStringBuilder.append(")");
    Log.w(str, localStringBuilder.toString());
  }
  
  public static float parseFloatPercentage(String paramString)
    throws NumberFormatException
  {
    if (paramString.endsWith("%"))
    {
      paramString = paramString.substring(0, paramString.length() - 1);
      if (!paramString.matches(".*[^0-9.].*")) {
        try
        {
          float f = Float.parseFloat(paramString);
          if ((f >= 0.0F) && (f <= 100.0F)) {
            return f;
          }
          paramString = new java/lang/NumberFormatException;
          paramString.<init>("is out of range");
          throw paramString;
        }
        catch (NumberFormatException paramString)
        {
          throw new NumberFormatException("is not a number");
        }
      }
      throw new NumberFormatException("contains an invalid character");
    }
    throw new NumberFormatException("does not end in %");
  }
  
  public static int parseIntPercentage(String paramString)
    throws NumberFormatException
  {
    if (paramString.endsWith("%"))
    {
      paramString = paramString.substring(0, paramString.length() - 1);
      if (!paramString.matches(".*[^0-9].*")) {
        try
        {
          int i = Integer.parseInt(paramString);
          if ((i >= 0) && (i <= 100)) {
            return i;
          }
          paramString = new java/lang/NumberFormatException;
          paramString.<init>("is out of range");
          throw paramString;
        }
        catch (NumberFormatException paramString)
        {
          throw new NumberFormatException("is not a number");
        }
      }
      throw new NumberFormatException("contains an invalid character");
    }
    throw new NumberFormatException("does not end in %");
  }
  
  public static long parseTimestampMs(String paramString)
    throws NumberFormatException
  {
    if (paramString.matches("(\\d+:)?[0-5]\\d:[0-5]\\d\\.\\d{3}"))
    {
      paramString = paramString.split("\\.", 2);
      long l = 0L;
      int i = 0;
      String[] arrayOfString = paramString[0].split(":");
      int j = arrayOfString.length;
      while (i < j)
      {
        l = 60L * l + Long.parseLong(arrayOfString[i]);
        i++;
      }
      return 1000L * l + Long.parseLong(paramString[1]);
    }
    throw new NumberFormatException("has invalid format");
  }
  
  public static String timeToString(long paramLong)
  {
    return String.format("%d:%02d:%02d.%03d", new Object[] { Long.valueOf(paramLong / 3600000L), Long.valueOf(paramLong / 60000L % 60L), Long.valueOf(paramLong / 1000L % 60L), Long.valueOf(paramLong % 1000L) });
  }
  
  public void eos()
  {
    if (mBuffer.endsWith("\r")) {
      mBuffer = mBuffer.substring(0, mBuffer.length() - 1);
    }
    mPhase.parse(mBuffer);
    mBuffer = "";
    yieldCue();
    mPhase = mParseStart;
  }
  
  public void parse(String paramString)
  {
    int i = 0;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mBuffer);
    localStringBuilder.append(paramString.replace("\000", "�"));
    mBuffer = localStringBuilder.toString().replace("\r\n", "\n");
    boolean bool = mBuffer.endsWith("\r");
    int j = 0;
    if (bool)
    {
      i = 1;
      mBuffer = mBuffer.substring(0, mBuffer.length() - 1);
    }
    paramString = mBuffer.split("[\r\n]");
    while (j < paramString.length - 1)
    {
      mPhase.parse(paramString[j]);
      j++;
    }
    mBuffer = paramString[(paramString.length - 1)];
    if (i != 0)
    {
      paramString = new StringBuilder();
      paramString.append(mBuffer);
      paramString.append("\r");
      mBuffer = paramString.toString();
    }
  }
  
  public void yieldCue()
  {
    if ((mCue != null) && (mCueTexts.size() > 0))
    {
      mCue.mStrings = new String[mCueTexts.size()];
      mCueTexts.toArray(mCue.mStrings);
      mCueTexts.clear();
      mListener.onCueParsed(mCue);
    }
    mCue = null;
  }
  
  static abstract interface Phase
  {
    public abstract void parse(String paramString);
  }
}
