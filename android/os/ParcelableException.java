package android.os;

import java.lang.reflect.Constructor;

public final class ParcelableException
  extends RuntimeException
  implements Parcelable
{
  public static final Parcelable.Creator<ParcelableException> CREATOR = new Parcelable.Creator()
  {
    public ParcelableException createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ParcelableException(ParcelableException.readFromParcel(paramAnonymousParcel));
    }
    
    public ParcelableException[] newArray(int paramAnonymousInt)
    {
      return new ParcelableException[paramAnonymousInt];
    }
  };
  
  public ParcelableException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
  
  public static Throwable readFromParcel(Parcel paramParcel)
  {
    String str = paramParcel.readString();
    paramParcel = paramParcel.readString();
    try
    {
      Object localObject = Class.forName(str, true, Parcelable.class.getClassLoader());
      if (Throwable.class.isAssignableFrom((Class)localObject))
      {
        localObject = (Throwable)((Class)localObject).getConstructor(new Class[] { String.class }).newInstance(new Object[] { paramParcel });
        return localObject;
      }
    }
    catch (ReflectiveOperationException localReflectiveOperationException) {}
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(str);
    localStringBuilder.append(": ");
    localStringBuilder.append(paramParcel);
    return new RuntimeException(localStringBuilder.toString());
  }
  
  public static void writeToParcel(Parcel paramParcel, Throwable paramThrowable)
  {
    paramParcel.writeString(paramThrowable.getClass().getName());
    paramParcel.writeString(paramThrowable.getMessage());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public <T extends Throwable> void maybeRethrow(Class<T> paramClass)
    throws Throwable
  {
    if (!paramClass.isAssignableFrom(getCause().getClass())) {
      return;
    }
    throw getCause();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcel(paramParcel, getCause());
  }
}
