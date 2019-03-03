package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import java.util.Collections;
import java.util.List;

public class StringParceledListSlice
  extends BaseParceledListSlice<String>
{
  public static final Parcelable.ClassLoaderCreator<StringParceledListSlice> CREATOR = new Parcelable.ClassLoaderCreator()
  {
    public StringParceledListSlice createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StringParceledListSlice(paramAnonymousParcel, null, null);
    }
    
    public StringParceledListSlice createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
    {
      return new StringParceledListSlice(paramAnonymousParcel, paramAnonymousClassLoader, null);
    }
    
    public StringParceledListSlice[] newArray(int paramAnonymousInt)
    {
      return new StringParceledListSlice[paramAnonymousInt];
    }
  };
  
  private StringParceledListSlice(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    super(paramParcel, paramClassLoader);
  }
  
  public StringParceledListSlice(List<String> paramList)
  {
    super(paramList);
  }
  
  public static StringParceledListSlice emptyList()
  {
    return new StringParceledListSlice(Collections.emptyList());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  protected Parcelable.Creator<?> readParcelableCreator(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    return Parcel.STRING_CREATOR;
  }
  
  protected void writeElement(String paramString, Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(paramString);
  }
  
  protected void writeParcelableCreator(String paramString, Parcel paramParcel) {}
}
