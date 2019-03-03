package android.util;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;

public class DebugUtils
{
  public DebugUtils() {}
  
  public static void buildShortClassTag(Object paramObject, StringBuilder paramStringBuilder)
  {
    if (paramObject == null)
    {
      paramStringBuilder.append("null");
    }
    else
    {
      String str1 = paramObject.getClass().getSimpleName();
      String str2;
      if (str1 != null)
      {
        str2 = str1;
        if (!str1.isEmpty()) {}
      }
      else
      {
        str1 = paramObject.getClass().getName();
        int i = str1.lastIndexOf('.');
        str2 = str1;
        if (i > 0) {
          str2 = str1.substring(i + 1);
        }
      }
      paramStringBuilder.append(str2);
      paramStringBuilder.append('{');
      paramStringBuilder.append(Integer.toHexString(System.identityHashCode(paramObject)));
    }
  }
  
  private static String constNameWithoutPrefix(String paramString, Field paramField)
  {
    return paramField.getName().substring(paramString.length());
  }
  
  public static String flagsToString(Class<?> paramClass, String paramString, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    int j;
    if (paramInt == 0) {
      j = 1;
    } else {
      j = 0;
    }
    paramClass = paramClass.getDeclaredFields();
    int k = paramClass.length;
    while (i < k)
    {
      Field localField = paramClass[i];
      int m = localField.getModifiers();
      int n = paramInt;
      if (Modifier.isStatic(m))
      {
        n = paramInt;
        if (Modifier.isFinal(m))
        {
          n = paramInt;
          if (localField.getType().equals(Integer.TYPE))
          {
            n = paramInt;
            if (localField.getName().startsWith(paramString))
            {
              n = paramInt;
              try
              {
                m = localField.getInt(null);
                if ((m == 0) && (j != 0))
                {
                  n = paramInt;
                  return constNameWithoutPrefix(paramString, localField);
                }
                n = paramInt;
                if ((paramInt & m) != 0)
                {
                  paramInt &= m;
                  n = paramInt;
                  localStringBuilder.append(constNameWithoutPrefix(paramString, localField));
                  n = paramInt;
                  localStringBuilder.append('|');
                  n = paramInt;
                }
              }
              catch (IllegalAccessException localIllegalAccessException) {}
            }
          }
        }
      }
      i++;
      paramInt = n;
    }
    if ((paramInt == 0) && (localStringBuilder.length() != 0)) {
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    } else {
      localStringBuilder.append(Integer.toHexString(paramInt));
    }
    return localStringBuilder.toString();
  }
  
  public static boolean isObjectSelected(Object paramObject)
  {
    boolean bool1 = false;
    Object localObject1 = System.getenv("ANDROID_OBJECT_FILTER");
    boolean bool2 = bool1;
    if (localObject1 != null)
    {
      bool2 = bool1;
      if (((String)localObject1).length() > 0)
      {
        String[] arrayOfString1 = ((String)localObject1).split("@");
        bool2 = bool1;
        if (paramObject.getClass().getSimpleName().matches(arrayOfString1[0]))
        {
          bool2 = false;
          for (int i = 1; i < arrayOfString1.length; i++)
          {
            String[] arrayOfString2 = arrayOfString1[i].split("=");
            Class localClass = paramObject.getClass();
            localObject1 = localClass;
            try
            {
              Object localObject2;
              Method localMethod;
              do
              {
                localObject2 = new java/lang/StringBuilder;
                ((StringBuilder)localObject2).<init>();
                ((StringBuilder)localObject2).append("get");
                ((StringBuilder)localObject2).append(arrayOfString2[0].substring(0, 1).toUpperCase(Locale.ROOT));
                ((StringBuilder)localObject2).append(arrayOfString2[0].substring(1));
                localMethod = ((Class)localObject1).getDeclaredMethod(((StringBuilder)localObject2).toString(), (Class[])null);
                localObject2 = localClass.getSuperclass();
                localObject1 = localObject2;
              } while ((localObject2 != null) && (localMethod == null));
              bool1 = bool2;
              if (localMethod != null)
              {
                localObject1 = localMethod.invoke(paramObject, (Object[])null);
                if (localObject1 != null) {
                  localObject1 = localObject1.toString();
                } else {
                  localObject1 = "null";
                }
                bool1 = ((String)localObject1).matches(arrayOfString2[1]);
                bool1 = bool2 | bool1;
              }
            }
            catch (InvocationTargetException localInvocationTargetException)
            {
              localInvocationTargetException.printStackTrace();
            }
            catch (IllegalAccessException localIllegalAccessException)
            {
              for (;;)
              {
                localIllegalAccessException.printStackTrace();
                bool1 = bool2;
              }
            }
            catch (NoSuchMethodException localNoSuchMethodException)
            {
              for (;;)
              {
                localNoSuchMethodException.printStackTrace();
                bool1 = bool2;
              }
            }
            bool2 = bool1;
          }
        }
      }
    }
    return bool2;
  }
  
  public static void printSizeValue(PrintWriter paramPrintWriter, long paramLong)
  {
    float f1 = (float)paramLong;
    String str1 = "";
    float f2 = f1;
    if (f1 > 900.0F)
    {
      str1 = "KB";
      f2 = f1 / 1024.0F;
    }
    f1 = f2;
    if (f2 > 900.0F)
    {
      str1 = "MB";
      f1 = f2 / 1024.0F;
    }
    f2 = f1;
    if (f1 > 900.0F)
    {
      str1 = "GB";
      f2 = f1 / 1024.0F;
    }
    f1 = f2;
    if (f2 > 900.0F)
    {
      str1 = "TB";
      f1 = f2 / 1024.0F;
    }
    f2 = f1;
    String str2 = str1;
    if (f1 > 900.0F)
    {
      str2 = "PB";
      f2 = f1 / 1024.0F;
    }
    if (f2 < 1.0F) {
      str1 = String.format("%.2f", new Object[] { Float.valueOf(f2) });
    }
    for (;;)
    {
      break;
      if (f2 < 10.0F) {
        str1 = String.format("%.1f", new Object[] { Float.valueOf(f2) });
      } else if (f2 < 100.0F) {
        str1 = String.format("%.0f", new Object[] { Float.valueOf(f2) });
      } else {
        str1 = String.format("%.0f", new Object[] { Float.valueOf(f2) });
      }
    }
    paramPrintWriter.print(str1);
    paramPrintWriter.print(str2);
  }
  
  public static String sizeValueToString(long paramLong, StringBuilder paramStringBuilder)
  {
    StringBuilder localStringBuilder = paramStringBuilder;
    if (paramStringBuilder == null) {
      localStringBuilder = new StringBuilder(32);
    }
    float f1 = (float)paramLong;
    paramStringBuilder = "";
    float f2 = f1;
    if (f1 > 900.0F)
    {
      paramStringBuilder = "KB";
      f2 = f1 / 1024.0F;
    }
    f1 = f2;
    if (f2 > 900.0F)
    {
      paramStringBuilder = "MB";
      f1 = f2 / 1024.0F;
    }
    f2 = f1;
    if (f1 > 900.0F)
    {
      paramStringBuilder = "GB";
      f2 = f1 / 1024.0F;
    }
    f1 = f2;
    if (f2 > 900.0F)
    {
      paramStringBuilder = "TB";
      f1 = f2 / 1024.0F;
    }
    f2 = f1;
    Object localObject = paramStringBuilder;
    if (f1 > 900.0F)
    {
      localObject = "PB";
      f2 = f1 / 1024.0F;
    }
    if (f2 < 1.0F) {
      paramStringBuilder = String.format("%.2f", new Object[] { Float.valueOf(f2) });
    }
    for (;;)
    {
      break;
      if (f2 < 10.0F) {
        paramStringBuilder = String.format("%.1f", new Object[] { Float.valueOf(f2) });
      } else if (f2 < 100.0F) {
        paramStringBuilder = String.format("%.0f", new Object[] { Float.valueOf(f2) });
      } else {
        paramStringBuilder = String.format("%.0f", new Object[] { Float.valueOf(f2) });
      }
    }
    localStringBuilder.append(paramStringBuilder);
    localStringBuilder.append((String)localObject);
    return localStringBuilder.toString();
  }
  
  public static String valueToString(Class<?> paramClass, String paramString, int paramInt)
  {
    for (Object localObject : paramClass.getDeclaredFields())
    {
      int k = ((Field)localObject).getModifiers();
      if ((Modifier.isStatic(k)) && (Modifier.isFinal(k)) && (((Field)localObject).getType().equals(Integer.TYPE)) && (((Field)localObject).getName().startsWith(paramString))) {
        try
        {
          if (paramInt == ((Field)localObject).getInt(null))
          {
            localObject = constNameWithoutPrefix(paramString, (Field)localObject);
            return localObject;
          }
        }
        catch (IllegalAccessException localIllegalAccessException) {}
      }
    }
    return Integer.toString(paramInt);
  }
}
