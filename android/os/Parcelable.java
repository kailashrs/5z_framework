package android.os;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract interface Parcelable
{
  public static final int CONTENTS_FILE_DESCRIPTOR = 1;
  public static final int PARCELABLE_ELIDE_DUPLICATES = 2;
  public static final int PARCELABLE_WRITE_RETURN_VALUE = 1;
  
  public abstract int describeContents();
  
  public abstract void writeToParcel(Parcel paramParcel, int paramInt);
  
  public static abstract interface ClassLoaderCreator<T>
    extends Parcelable.Creator<T>
  {
    public abstract T createFromParcel(Parcel paramParcel, ClassLoader paramClassLoader);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ContentsFlags {}
  
  public static abstract interface Creator<T>
  {
    public abstract T createFromParcel(Parcel paramParcel);
    
    public abstract T[] newArray(int paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface WriteFlags {}
}
