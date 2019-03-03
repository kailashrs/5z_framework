package android.os.health;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;

public class HealthKeys
{
  public static final int BASE_PACKAGE = 40000;
  public static final int BASE_PID = 20000;
  public static final int BASE_PROCESS = 30000;
  public static final int BASE_SERVICE = 50000;
  public static final int BASE_UID = 10000;
  public static final int TYPE_COUNT = 5;
  public static final int TYPE_MEASUREMENT = 1;
  public static final int TYPE_MEASUREMENTS = 4;
  public static final int TYPE_STATS = 2;
  public static final int TYPE_TIMER = 0;
  public static final int TYPE_TIMERS = 3;
  public static final int UNKNOWN_KEY = 0;
  
  public HealthKeys() {}
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({java.lang.annotation.ElementType.FIELD})
  public static @interface Constant
  {
    int type();
  }
  
  public static class Constants
  {
    private final String mDataType;
    private final int[][] mKeys = new int[5][];
    
    public Constants(Class paramClass)
    {
      mDataType = paramClass.getSimpleName();
      Field[] arrayOfField = paramClass.getDeclaredFields();
      int i = arrayOfField.length;
      HealthKeys.SortedIntArray[] arrayOfSortedIntArray = new HealthKeys.SortedIntArray[mKeys.length];
      int j = 0;
      for (int k = 0; k < arrayOfSortedIntArray.length; k++) {
        arrayOfSortedIntArray[k] = new HealthKeys.SortedIntArray(i);
      }
      for (k = 0; k < i; k++)
      {
        paramClass = arrayOfField[k];
        Object localObject = (HealthKeys.Constant)paramClass.getAnnotation(HealthKeys.Constant.class);
        if (localObject != null)
        {
          int m = ((HealthKeys.Constant)localObject).type();
          if (m < arrayOfSortedIntArray.length)
          {
            try
            {
              arrayOfSortedIntArray[m].addValue(paramClass.getInt(null));
            }
            catch (IllegalAccessException localIllegalAccessException)
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Can't read constant value type=");
              ((StringBuilder)localObject).append(m);
              ((StringBuilder)localObject).append(" field=");
              ((StringBuilder)localObject).append(paramClass);
              throw new RuntimeException(((StringBuilder)localObject).toString(), localIllegalAccessException);
            }
          }
          else
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Unknown Constant type ");
            ((StringBuilder)localObject).append(m);
            ((StringBuilder)localObject).append(" on ");
            ((StringBuilder)localObject).append(paramClass);
            throw new RuntimeException(((StringBuilder)localObject).toString());
          }
        }
      }
      for (k = j; k < localIllegalAccessException.length; k++) {
        mKeys[k] = localIllegalAccessException[k].getArray();
      }
    }
    
    public String getDataType()
    {
      return mDataType;
    }
    
    public int getIndex(int paramInt1, int paramInt2)
    {
      int i = Arrays.binarySearch(mKeys[paramInt1], paramInt2);
      if (i >= 0) {
        return i;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown Constant ");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append(" (of type ");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" )");
      throw new RuntimeException(localStringBuilder.toString());
    }
    
    public int[] getKeys(int paramInt)
    {
      return mKeys[paramInt];
    }
    
    public int getSize(int paramInt)
    {
      return mKeys[paramInt].length;
    }
  }
  
  private static class SortedIntArray
  {
    int[] mArray;
    int mCount;
    
    SortedIntArray(int paramInt)
    {
      mArray = new int[paramInt];
    }
    
    void addValue(int paramInt)
    {
      int[] arrayOfInt = mArray;
      int i = mCount;
      mCount = (i + 1);
      arrayOfInt[i] = paramInt;
    }
    
    int[] getArray()
    {
      if (mCount == mArray.length)
      {
        Arrays.sort(mArray);
        return mArray;
      }
      int[] arrayOfInt = new int[mCount];
      System.arraycopy(mArray, 0, arrayOfInt, 0, mCount);
      Arrays.sort(arrayOfInt);
      return arrayOfInt;
    }
  }
}
