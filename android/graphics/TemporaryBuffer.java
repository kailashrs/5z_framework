package android.graphics;

import com.android.internal.util.ArrayUtils;

public class TemporaryBuffer
{
  private static char[] sTemp = null;
  
  public TemporaryBuffer() {}
  
  public static char[] obtain(int paramInt)
  {
    try
    {
      char[] arrayOfChar1 = sTemp;
      sTemp = null;
      char[] arrayOfChar2;
      if (arrayOfChar1 != null)
      {
        arrayOfChar2 = arrayOfChar1;
        if (arrayOfChar1.length >= paramInt) {}
      }
      else
      {
        arrayOfChar2 = ArrayUtils.newUnpaddedCharArray(paramInt);
      }
      return arrayOfChar2;
    }
    finally {}
  }
  
  public static void recycle(char[] paramArrayOfChar)
  {
    if (paramArrayOfChar.length > 1000) {
      return;
    }
    try
    {
      sTemp = paramArrayOfChar;
      return;
    }
    finally {}
  }
}
