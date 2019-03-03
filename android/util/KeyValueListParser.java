package android.util;

import android.text.TextUtils.SimpleStringSplitter;
import android.text.TextUtils.StringSplitter;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Iterator;

public class KeyValueListParser
{
  private final TextUtils.StringSplitter mSplitter;
  private final ArrayMap<String, String> mValues = new ArrayMap();
  
  public KeyValueListParser(char paramChar)
  {
    mSplitter = new TextUtils.SimpleStringSplitter(paramChar);
  }
  
  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    paramString = (String)mValues.get(paramString);
    if (paramString != null) {
      try
      {
        boolean bool = Boolean.parseBoolean(paramString);
        return bool;
      }
      catch (NumberFormatException paramString) {}
    }
    return paramBoolean;
  }
  
  public long getDurationMillis(String paramString, long paramLong)
  {
    paramString = (String)mValues.get(paramString);
    if (paramString != null) {
      try
      {
        if ((!paramString.startsWith("P")) && (!paramString.startsWith("p"))) {
          return Long.parseLong(paramString);
        }
        long l = Duration.parse(paramString).toMillis();
        return l;
      }
      catch (NumberFormatException|DateTimeParseException paramString) {}
    }
    return paramLong;
  }
  
  public float getFloat(String paramString, float paramFloat)
  {
    paramString = (String)mValues.get(paramString);
    if (paramString != null) {
      try
      {
        float f = Float.parseFloat(paramString);
        return f;
      }
      catch (NumberFormatException paramString) {}
    }
    return paramFloat;
  }
  
  public int getInt(String paramString, int paramInt)
  {
    paramString = (String)mValues.get(paramString);
    if (paramString != null) {
      try
      {
        int i = Integer.parseInt(paramString);
        return i;
      }
      catch (NumberFormatException paramString) {}
    }
    return paramInt;
  }
  
  public int[] getIntArray(String paramString, int[] paramArrayOfInt)
  {
    paramString = (String)mValues.get(paramString);
    if (paramString != null) {
      try
      {
        paramString = paramString.split(":");
        if (paramString.length > 0)
        {
          int[] arrayOfInt = new int[paramString.length];
          for (int i = 0; i < paramString.length; i++) {
            arrayOfInt[i] = Integer.parseInt(paramString[i]);
          }
          return arrayOfInt;
        }
      }
      catch (NumberFormatException paramString) {}
    }
    return paramArrayOfInt;
  }
  
  public long getLong(String paramString, long paramLong)
  {
    paramString = (String)mValues.get(paramString);
    if (paramString != null) {
      try
      {
        long l = Long.parseLong(paramString);
        return l;
      }
      catch (NumberFormatException paramString) {}
    }
    return paramLong;
  }
  
  public String getString(String paramString1, String paramString2)
  {
    paramString1 = (String)mValues.get(paramString1);
    if (paramString1 != null) {
      return paramString1;
    }
    return paramString2;
  }
  
  public String keyAt(int paramInt)
  {
    return (String)mValues.keyAt(paramInt);
  }
  
  public void setString(String paramString)
    throws IllegalArgumentException
  {
    mValues.clear();
    if (paramString != null)
    {
      mSplitter.setString(paramString);
      Object localObject = mSplitter.iterator();
      while (((Iterator)localObject).hasNext())
      {
        String str = (String)((Iterator)localObject).next();
        int i = str.indexOf('=');
        if (i >= 0)
        {
          mValues.put(str.substring(0, i).trim(), str.substring(i + 1).trim());
        }
        else
        {
          mValues.clear();
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("'");
          ((StringBuilder)localObject).append(str);
          ((StringBuilder)localObject).append("' in '");
          ((StringBuilder)localObject).append(paramString);
          ((StringBuilder)localObject).append("' is not a valid key-value pair");
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        }
      }
    }
  }
  
  public int size()
  {
    return mValues.size();
  }
}
