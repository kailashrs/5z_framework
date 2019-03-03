package com.android.internal.util;

import android.util.Log;
import android.util.SparseArray;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MessageUtils
{
  private static final boolean DBG = false;
  public static final String[] DEFAULT_PREFIXES = { "CMD_", "EVENT_" };
  private static final String TAG = MessageUtils.class.getSimpleName();
  
  public MessageUtils() {}
  
  public static SparseArray<String> findMessageNames(Class[] paramArrayOfClass)
  {
    return findMessageNames(paramArrayOfClass, DEFAULT_PREFIXES);
  }
  
  public static SparseArray<String> findMessageNames(Class[] paramArrayOfClass, String[] paramArrayOfString)
  {
    SparseArray localSparseArray = new SparseArray();
    int i = paramArrayOfClass.length;
    int j = 0;
    for (;;)
    {
      if (j < i)
      {
        Object localObject1 = paramArrayOfClass[j];
        String str2 = ((Class)localObject1).getName();
        try
        {
          for (str2 : ((Class)localObject1).getDeclaredFields())
          {
            int n = str2.getModifiers();
            if (!(Modifier.isStatic(n) ^ true | Modifier.isFinal(n) ^ true))
            {
              localObject2 = str2.getName();
              int i1 = paramArrayOfString.length;
              n = 0;
              while (n < i1) {
                if (((String)localObject2).startsWith(paramArrayOfString[n])) {
                  try
                  {
                    str2.setAccessible(true);
                    try
                    {
                      int i2 = str2.getInt(null);
                      String str3 = (String)localSparseArray.get(i2);
                      if ((str3 != null) && (!str3.equals(localObject2)))
                      {
                        DuplicateConstantError localDuplicateConstantError = new com/android/internal/util/MessageUtils$DuplicateConstantError;
                        localDuplicateConstantError.<init>((String)localObject2, str3, i2);
                        throw localDuplicateConstantError;
                      }
                      localSparseArray.put(i2, localObject2);
                    }
                    catch (IllegalArgumentException|ExceptionInInitializerError localIllegalArgumentException)
                    {
                      break;
                    }
                    n++;
                  }
                  catch (SecurityException|IllegalAccessException localSecurityException2) {}
                }
              }
            }
          }
        }
        catch (SecurityException localSecurityException1)
        {
          String str1 = TAG;
          Object localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Can't list fields of class ");
          ((StringBuilder)localObject2).append(localIllegalArgumentException);
          Log.e(str1, ((StringBuilder)localObject2).toString());
          j++;
        }
      }
    }
    return localSparseArray;
  }
  
  public static class DuplicateConstantError
    extends Error
  {
    private DuplicateConstantError() {}
    
    public DuplicateConstantError(String paramString1, String paramString2, int paramInt)
    {
      super();
    }
  }
}
