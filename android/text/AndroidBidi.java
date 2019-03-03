package android.text;

import android.icu.lang.UCharacter;
import android.icu.text.Bidi;
import android.icu.text.BidiClassifier;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public class AndroidBidi
{
  private static final EmojiBidiOverride sEmojiBidiOverride = new EmojiBidiOverride();
  
  public AndroidBidi() {}
  
  public static int bidi(int paramInt, char[] paramArrayOfChar, byte[] paramArrayOfByte)
  {
    if ((paramArrayOfChar != null) && (paramArrayOfByte != null))
    {
      int i = paramArrayOfChar.length;
      if (paramArrayOfByte.length >= i)
      {
        int j = 0;
        int k;
        switch (paramInt)
        {
        case 0: 
        default: 
          paramInt = 0;
          k = paramInt;
          break;
        case 2: 
          paramInt = 126;
          k = paramInt;
          break;
        case 1: 
          paramInt = 0;
          k = paramInt;
          break;
        case -1: 
          paramInt = 1;
          k = paramInt;
          break;
        case -2: 
          paramInt = 127;
          k = paramInt;
        }
        Bidi localBidi = new Bidi(i, 0);
        localBidi.setCustomClassifier(sEmojiBidiOverride);
        localBidi.setPara(paramArrayOfChar, k, null);
        for (paramInt = j; paramInt < i; paramInt++) {
          paramArrayOfByte[paramInt] = localBidi.getLevelAt(paramInt);
        }
        if ((localBidi.getParaLevel() & 0x1) == 0) {
          paramInt = 1;
        } else {
          paramInt = -1;
        }
        return paramInt;
      }
      throw new IndexOutOfBoundsException();
    }
    throw new NullPointerException();
  }
  
  public static Layout.Directions directions(int paramInt1, byte[] paramArrayOfByte, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4)
  {
    if (paramInt4 == 0) {
      return Layout.DIRS_ALL_LEFT_TO_RIGHT;
    }
    int i;
    if (paramInt1 == 1) {
      i = 0;
    } else {
      i = 1;
    }
    int j = paramArrayOfByte[paramInt2];
    paramInt1 = j;
    int k = 1;
    int m = paramInt2 + 1;
    int i2;
    while (m < paramInt2 + paramInt4)
    {
      n = paramArrayOfByte[m];
      i1 = j;
      i2 = k;
      if (n != j)
      {
        i1 = n;
        i2 = k + 1;
      }
      m++;
      j = i1;
      k = i2;
    }
    m = paramInt4;
    int i1 = k;
    int n = m;
    if ((j & 0x1) != (i & 0x1))
    {
      for (;;)
      {
        m--;
        j = m;
        if (m < 0) {
          break;
        }
        j = paramArrayOfChar[(paramInt3 + m)];
        if (j == 10)
        {
          j = m - 1;
          break;
        }
        if ((j != 32) && (j != 9))
        {
          j = m;
          break;
        }
      }
      paramInt3 = j + 1;
      i1 = k;
      n = paramInt3;
      if (paramInt3 != paramInt4)
      {
        i1 = k + 1;
        n = paramInt3;
      }
    }
    if ((i1 == 1) && (paramInt1 == i))
    {
      if ((paramInt1 & 0x1) != 0) {
        return Layout.DIRS_ALL_RIGHT_TO_LEFT;
      }
      return Layout.DIRS_ALL_LEFT_TO_RIGHT;
    }
    paramArrayOfChar = new int[i1 * 2];
    paramInt3 = paramInt1;
    int i3 = paramInt1 << 26;
    int i4 = 1;
    int i5 = paramInt2;
    int i6 = paramInt2 + n;
    j = paramInt1;
    k = paramInt2;
    m = paramInt1;
    paramInt1 = j;
    while (k < i6)
    {
      i2 = paramArrayOfByte[k];
      int i7 = paramInt1;
      int i8 = m;
      j = paramInt3;
      int i9 = i3;
      int i10 = i4;
      int i11 = i5;
      if (i2 != m)
      {
        i8 = i2;
        if (i2 > paramInt3)
        {
          j = i2;
          m = paramInt1;
        }
        else
        {
          m = paramInt1;
          j = paramInt3;
          if (i2 < paramInt1)
          {
            m = i2;
            j = paramInt3;
          }
        }
        paramInt1 = i4 + 1;
        paramArrayOfChar[i4] = (k - i5 | i3);
        i10 = paramInt1 + 1;
        paramArrayOfChar[paramInt1] = (k - paramInt2);
        i9 = i8 << 26;
        i11 = k;
        i7 = m;
      }
      k++;
      paramInt1 = i7;
      m = i8;
      paramInt3 = j;
      i3 = i9;
      i4 = i10;
      i5 = i11;
    }
    paramArrayOfChar[i4] = (paramInt2 + n - i5 | i3);
    if (n < paramInt4)
    {
      paramInt2 = i4 + 1;
      paramArrayOfChar[paramInt2] = n;
      paramArrayOfChar[(paramInt2 + 1)] = (paramInt4 - n | i << 26);
    }
    if ((paramInt1 & 0x1) == i)
    {
      paramInt4 = paramInt1 + 1;
      if (paramInt3 > paramInt4) {
        paramInt1 = 1;
      } else {
        paramInt1 = 0;
      }
      paramInt2 = paramInt1;
      paramInt1 = paramInt4;
    }
    else
    {
      paramInt2 = 1;
      if (i1 <= 1) {
        paramInt2 = 0;
      }
    }
    paramInt4 = paramInt1;
    if (paramInt2 != 0)
    {
      paramInt3--;
      paramInt2 = paramInt1;
      for (;;)
      {
        paramInt4 = paramInt2;
        if (paramInt3 < paramInt2) {
          break;
        }
        for (paramInt1 = 0; paramInt1 < paramArrayOfChar.length; paramInt1 += 2) {
          if (paramArrayOfByte[paramArrayOfChar[paramInt1]] >= paramInt3)
          {
            for (paramInt4 = paramInt1 + 2; (paramInt4 < paramArrayOfChar.length) && (paramArrayOfByte[paramArrayOfChar[paramInt4]] >= paramInt3); paramInt4 += 2) {}
            k = paramInt1;
            for (paramInt1 = paramInt4 - 2; k < paramInt1; paramInt1 -= 2)
            {
              m = paramArrayOfChar[k];
              paramArrayOfChar[k] = paramArrayOfChar[paramInt1];
              paramArrayOfChar[paramInt1] = m;
              m = paramArrayOfChar[(k + 1)];
              paramArrayOfChar[(k + 1)] = paramArrayOfChar[(paramInt1 + 1)];
              paramArrayOfChar[(paramInt1 + 1)] = m;
              k += 2;
            }
            paramInt1 = paramInt4 + 2;
          }
        }
        paramInt3--;
      }
    }
    return new Layout.Directions(paramArrayOfChar);
  }
  
  public static class EmojiBidiOverride
    extends BidiClassifier
  {
    private static final int NO_OVERRIDE = UCharacter.getIntPropertyMaxValue(4096) + 1;
    
    public EmojiBidiOverride()
    {
      super();
    }
    
    public int classify(int paramInt)
    {
      if (Emoji.isNewEmoji(paramInt)) {
        return 10;
      }
      return NO_OVERRIDE;
    }
  }
}
