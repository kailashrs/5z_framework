package android.os;

import android.util.MathUtils;

public class ParcelableParcel
  implements Parcelable
{
  public static final Parcelable.ClassLoaderCreator<ParcelableParcel> CREATOR = new Parcelable.ClassLoaderCreator()
  {
    public ParcelableParcel createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ParcelableParcel(paramAnonymousParcel, null);
    }
    
    public ParcelableParcel createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
    {
      return new ParcelableParcel(paramAnonymousParcel, paramAnonymousClassLoader);
    }
    
    public ParcelableParcel[] newArray(int paramAnonymousInt)
    {
      return new ParcelableParcel[paramAnonymousInt];
    }
  };
  final ClassLoader mClassLoader;
  final Parcel mParcel = Parcel.obtain();
  
  public ParcelableParcel(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    mClassLoader = paramClassLoader;
    int i = paramParcel.readInt();
    if (i >= 0)
    {
      int j = paramParcel.dataPosition();
      paramParcel.setDataPosition(MathUtils.addOrThrow(j, i));
      mParcel.appendFrom(paramParcel, j, i);
      return;
    }
    throw new IllegalArgumentException("Negative size read from parcel");
  }
  
  public ParcelableParcel(ClassLoader paramClassLoader)
  {
    mClassLoader = paramClassLoader;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ClassLoader getClassLoader()
  {
    return mClassLoader;
  }
  
  public Parcel getParcel()
  {
    mParcel.setDataPosition(0);
    return mParcel;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mParcel.dataSize());
    paramParcel.appendFrom(mParcel, 0, mParcel.dataSize());
  }
}
