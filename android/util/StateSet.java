package android.util;

import com.android.internal.R.styleable;

public class StateSet
{
  public static final int[] NOTHING;
  public static final int VIEW_STATE_ACCELERATED = 64;
  public static final int VIEW_STATE_ACTIVATED = 32;
  public static final int VIEW_STATE_DRAG_CAN_ACCEPT = 256;
  public static final int VIEW_STATE_DRAG_HOVERED = 512;
  public static final int VIEW_STATE_ENABLED = 8;
  public static final int VIEW_STATE_FOCUSED = 4;
  public static final int VIEW_STATE_HOVERED = 128;
  static final int[] VIEW_STATE_IDS = { 16842909, 1, 16842913, 2, 16842908, 4, 16842910, 8, 16842919, 16, 16843518, 32, 16843547, 64, 16843623, 128, 16843624, 256, 16843625, 512 };
  public static final int VIEW_STATE_PRESSED = 16;
  public static final int VIEW_STATE_SELECTED = 2;
  private static final int[][] VIEW_STATE_SETS;
  public static final int VIEW_STATE_WINDOW_FOCUSED = 1;
  public static final int[] WILD_CARD;
  
  static
  {
    if (VIEW_STATE_IDS.length / 2 == R.styleable.ViewDrawableStates.length)
    {
      int[] arrayOfInt1 = new int[VIEW_STATE_IDS.length];
      int j;
      int k;
      for (int i = 0; i < R.styleable.ViewDrawableStates.length; i++)
      {
        j = R.styleable.ViewDrawableStates[i];
        for (k = 0; k < VIEW_STATE_IDS.length; k += 2) {
          if (VIEW_STATE_IDS[k] == j)
          {
            arrayOfInt1[(i * 2)] = j;
            arrayOfInt1[(i * 2 + 1)] = VIEW_STATE_IDS[(k + 1)];
          }
        }
      }
      VIEW_STATE_SETS = new int[1 << VIEW_STATE_IDS.length / 2][];
      for (i = 0; i < VIEW_STATE_SETS.length; i++)
      {
        int[] arrayOfInt2 = new int[Integer.bitCount(i)];
        k = 0;
        int m = 0;
        while (m < arrayOfInt1.length)
        {
          j = k;
          if ((arrayOfInt1[(m + 1)] & i) != 0)
          {
            arrayOfInt2[k] = arrayOfInt1[m];
            j = k + 1;
          }
          m += 2;
          k = j;
        }
        VIEW_STATE_SETS[i] = arrayOfInt2;
      }
      WILD_CARD = new int[0];
      NOTHING = new int[] { 0 };
      return;
    }
    throw new IllegalStateException("VIEW_STATE_IDs array length does not match ViewDrawableStates style array");
  }
  
  public StateSet() {}
  
  public static boolean containsAttribute(int[][] paramArrayOfInt, int paramInt)
  {
    if (paramArrayOfInt != null)
    {
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++)
      {
        int[] arrayOfInt = paramArrayOfInt[j];
        if (arrayOfInt == null) {
          break;
        }
        int k = arrayOfInt.length;
        int m = 0;
        while (m < k)
        {
          int n = arrayOfInt[m];
          if ((n != paramInt) && (-n != paramInt)) {
            m++;
          } else {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public static String dump(int[] paramArrayOfInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++) {
      switch (paramArrayOfInt[j])
      {
      default: 
        break;
      case 16843518: 
        localStringBuilder.append("A ");
        break;
      case 16842919: 
        localStringBuilder.append("P ");
        break;
      case 16842913: 
        localStringBuilder.append("S ");
        break;
      case 16842912: 
        localStringBuilder.append("C ");
        break;
      case 16842910: 
        localStringBuilder.append("E ");
        break;
      case 16842909: 
        localStringBuilder.append("W ");
        break;
      case 16842908: 
        localStringBuilder.append("F ");
      }
    }
    return localStringBuilder.toString();
  }
  
  public static int[] get(int paramInt)
  {
    if (paramInt < VIEW_STATE_SETS.length) {
      return VIEW_STATE_SETS[paramInt];
    }
    throw new IllegalArgumentException("Invalid state set mask");
  }
  
  public static boolean isWildCard(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    boolean bool = false;
    if ((i != 0) && (paramArrayOfInt[0] != 0)) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public static boolean stateSetMatches(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++)
    {
      int k = paramArrayOfInt[j];
      if (k == 0) {
        return true;
      }
      if (k > 0)
      {
        if (paramInt != k) {
          return false;
        }
      }
      else if (paramInt == -k) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean stateSetMatches(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    boolean bool1 = true;
    if (paramArrayOfInt2 == null)
    {
      boolean bool2 = bool1;
      if (paramArrayOfInt1 != null) {
        if (isWildCard(paramArrayOfInt1)) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
      return bool2;
    }
    int i = paramArrayOfInt1.length;
    int j = paramArrayOfInt2.length;
    for (int k = 0; k < i; k++)
    {
      int m = paramArrayOfInt1[k];
      if (m == 0) {
        return true;
      }
      int n;
      if (m > 0)
      {
        n = 1;
      }
      else
      {
        n = 0;
        m = -m;
      }
      int i1 = 0;
      int i3;
      for (int i2 = 0;; i2++)
      {
        i3 = i1;
        if (i2 >= j) {
          break;
        }
        i3 = paramArrayOfInt2[i2];
        if (i3 == 0)
        {
          i3 = i1;
          if (n == 0) {
            break;
          }
          return false;
        }
        if (i3 == m)
        {
          if (n != 0)
          {
            i3 = 1;
            break;
          }
          return false;
        }
      }
      if ((n != 0) && (i3 == 0)) {
        return false;
      }
    }
    return true;
  }
  
  public static int[] trimStateSet(int[] paramArrayOfInt, int paramInt)
  {
    if (paramArrayOfInt.length == paramInt) {
      return paramArrayOfInt;
    }
    int[] arrayOfInt = new int[paramInt];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramInt);
    return arrayOfInt;
  }
}
