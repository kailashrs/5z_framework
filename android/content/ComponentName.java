package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;

public final class ComponentName
  implements Parcelable, Cloneable, Comparable<ComponentName>
{
  public static final Parcelable.Creator<ComponentName> CREATOR = new Parcelable.Creator()
  {
    public ComponentName createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ComponentName(paramAnonymousParcel);
    }
    
    public ComponentName[] newArray(int paramAnonymousInt)
    {
      return new ComponentName[paramAnonymousInt];
    }
  };
  private final String mClass;
  private final String mPackage;
  
  public ComponentName(Context paramContext, Class<?> paramClass)
  {
    mPackage = paramContext.getPackageName();
    mClass = paramClass.getName();
  }
  
  public ComponentName(Context paramContext, String paramString)
  {
    if (paramString != null)
    {
      mPackage = paramContext.getPackageName();
      mClass = paramString;
      return;
    }
    throw new NullPointerException("class name is null");
  }
  
  public ComponentName(Parcel paramParcel)
  {
    mPackage = paramParcel.readString();
    if (mPackage != null)
    {
      mClass = paramParcel.readString();
      if (mClass != null) {
        return;
      }
      throw new NullPointerException("class name is null");
    }
    throw new NullPointerException("package name is null");
  }
  
  private ComponentName(String paramString, Parcel paramParcel)
  {
    mPackage = paramString;
    mClass = paramParcel.readString();
  }
  
  public ComponentName(String paramString1, String paramString2)
  {
    if (paramString1 != null)
    {
      if (paramString2 != null)
      {
        mPackage = paramString1;
        mClass = paramString2;
        return;
      }
      throw new NullPointerException("class name is null");
    }
    throw new NullPointerException("package name is null");
  }
  
  private static void appendShortClassName(StringBuilder paramStringBuilder, String paramString1, String paramString2)
  {
    if (paramString2.startsWith(paramString1))
    {
      int i = paramString1.length();
      int j = paramString2.length();
      if ((j > i) && (paramString2.charAt(i) == '.'))
      {
        paramStringBuilder.append(paramString2, i, j);
        return;
      }
    }
    paramStringBuilder.append(paramString2);
  }
  
  public static void appendShortString(StringBuilder paramStringBuilder, String paramString1, String paramString2)
  {
    paramStringBuilder.append(paramString1);
    paramStringBuilder.append('/');
    appendShortClassName(paramStringBuilder, paramString1, paramString2);
  }
  
  public static ComponentName createRelative(Context paramContext, String paramString)
  {
    return createRelative(paramContext.getPackageName(), paramString);
  }
  
  public static ComponentName createRelative(String paramString1, String paramString2)
  {
    if (!TextUtils.isEmpty(paramString2))
    {
      if (paramString2.charAt(0) == '.')
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString1);
        localStringBuilder.append(paramString2);
        paramString2 = localStringBuilder.toString();
      }
      return new ComponentName(paramString1, paramString2);
    }
    throw new IllegalArgumentException("class name cannot be empty");
  }
  
  private static void printShortClassName(PrintWriter paramPrintWriter, String paramString1, String paramString2)
  {
    if (paramString2.startsWith(paramString1))
    {
      int i = paramString1.length();
      int j = paramString2.length();
      if ((j > i) && (paramString2.charAt(i) == '.'))
      {
        paramPrintWriter.write(paramString2, i, j - i);
        return;
      }
    }
    paramPrintWriter.print(paramString2);
  }
  
  public static void printShortString(PrintWriter paramPrintWriter, String paramString1, String paramString2)
  {
    paramPrintWriter.print(paramString1);
    paramPrintWriter.print('/');
    printShortClassName(paramPrintWriter, paramString1, paramString2);
  }
  
  public static ComponentName readFromParcel(Parcel paramParcel)
  {
    String str = paramParcel.readString();
    if (str != null) {
      paramParcel = new ComponentName(str, paramParcel);
    } else {
      paramParcel = null;
    }
    return paramParcel;
  }
  
  public static ComponentName unflattenFromString(String paramString)
  {
    int i = paramString.indexOf('/');
    if ((i >= 0) && (i + 1 < paramString.length()))
    {
      String str1 = paramString.substring(0, i);
      String str2 = paramString.substring(i + 1);
      paramString = str2;
      if (str2.length() > 0)
      {
        paramString = str2;
        if (str2.charAt(0) == '.')
        {
          paramString = new StringBuilder();
          paramString.append(str1);
          paramString.append(str2);
          paramString = paramString.toString();
        }
      }
      return new ComponentName(str1, paramString);
    }
    return null;
  }
  
  public static void writeToParcel(ComponentName paramComponentName, Parcel paramParcel)
  {
    if (paramComponentName != null) {
      paramComponentName.writeToParcel(paramParcel, 0);
    } else {
      paramParcel.writeString(null);
    }
  }
  
  public void appendShortString(StringBuilder paramStringBuilder)
  {
    appendShortString(paramStringBuilder, mPackage, mClass);
  }
  
  public ComponentName clone()
  {
    return new ComponentName(mPackage, mClass);
  }
  
  public int compareTo(ComponentName paramComponentName)
  {
    int i = mPackage.compareTo(mPackage);
    if (i != 0) {
      return i;
    }
    return mClass.compareTo(mClass);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if (paramObject != null) {
      try
      {
        paramObject = (ComponentName)paramObject;
        boolean bool2 = bool1;
        if (mPackage.equals(mPackage))
        {
          boolean bool3 = mClass.equals(mClass);
          bool2 = bool1;
          if (bool3) {
            bool2 = true;
          }
        }
        return bool2;
      }
      catch (ClassCastException paramObject) {}
    }
    return false;
  }
  
  public String flattenToShortString()
  {
    StringBuilder localStringBuilder = new StringBuilder(mPackage.length() + mClass.length());
    appendShortString(localStringBuilder, mPackage, mClass);
    return localStringBuilder.toString();
  }
  
  public String flattenToString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mPackage);
    localStringBuilder.append("/");
    localStringBuilder.append(mClass);
    return localStringBuilder.toString();
  }
  
  public String getClassName()
  {
    return mClass;
  }
  
  public String getPackageName()
  {
    return mPackage;
  }
  
  public String getShortClassName()
  {
    if (mClass.startsWith(mPackage))
    {
      int i = mPackage.length();
      int j = mClass.length();
      if ((j > i) && (mClass.charAt(i) == '.')) {
        return mClass.substring(i, j);
      }
    }
    return mClass;
  }
  
  public int hashCode()
  {
    return mPackage.hashCode() + mClass.hashCode();
  }
  
  public String toShortString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(mPackage);
    localStringBuilder.append("/");
    localStringBuilder.append(mClass);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ComponentInfo{");
    localStringBuilder.append(mPackage);
    localStringBuilder.append("/");
    localStringBuilder.append(mClass);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackage);
    paramParcel.writeString(mClass);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1138166333441L, mPackage);
    paramProtoOutputStream.write(1138166333442L, mClass);
    paramProtoOutputStream.end(paramLong);
  }
  
  @FunctionalInterface
  public static abstract interface WithComponentName
  {
    public abstract ComponentName getComponentName();
  }
}
