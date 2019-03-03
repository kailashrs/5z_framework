package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import java.util.Collections;
import java.util.List;

public class ParceledListSlice<T extends Parcelable>
  extends BaseParceledListSlice<T>
{
  public static final Parcelable.ClassLoaderCreator<ParceledListSlice> CREATOR = new Parcelable.ClassLoaderCreator()
  {
    public ParceledListSlice createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ParceledListSlice(paramAnonymousParcel, null, null);
    }
    
    public ParceledListSlice createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
    {
      return new ParceledListSlice(paramAnonymousParcel, paramAnonymousClassLoader, null);
    }
    
    public ParceledListSlice[] newArray(int paramAnonymousInt)
    {
      return new ParceledListSlice[paramAnonymousInt];
    }
  };
  
  private ParceledListSlice(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    super(paramParcel, paramClassLoader);
  }
  
  public ParceledListSlice(List<T> paramList)
  {
    super(paramList);
  }
  
  public static <T extends Parcelable> ParceledListSlice<T> emptyList()
  {
    return new ParceledListSlice(Collections.emptyList());
  }
  
  public int describeContents()
  {
    int i = 0;
    List localList = getList();
    for (int j = 0; j < localList.size(); j++) {
      i |= ((Parcelable)localList.get(j)).describeContents();
    }
    return i;
  }
  
  protected Parcelable.Creator<?> readParcelableCreator(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    return paramParcel.readParcelableCreator(paramClassLoader);
  }
  
  protected void writeElement(T paramT, Parcel paramParcel, int paramInt)
  {
    paramT.writeToParcel(paramParcel, paramInt);
  }
  
  protected void writeParcelableCreator(T paramT, Parcel paramParcel)
  {
    paramParcel.writeParcelableCreator(paramT);
  }
}
