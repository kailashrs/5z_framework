package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.List;

public final class ChangedPackages
  implements Parcelable
{
  public static final Parcelable.Creator<ChangedPackages> CREATOR = new Parcelable.Creator()
  {
    public ChangedPackages createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ChangedPackages(paramAnonymousParcel);
    }
    
    public ChangedPackages[] newArray(int paramAnonymousInt)
    {
      return new ChangedPackages[paramAnonymousInt];
    }
  };
  private final List<String> mPackageNames;
  private final int mSequenceNumber;
  
  public ChangedPackages(int paramInt, List<String> paramList)
  {
    mSequenceNumber = paramInt;
    mPackageNames = paramList;
  }
  
  protected ChangedPackages(Parcel paramParcel)
  {
    mSequenceNumber = paramParcel.readInt();
    mPackageNames = paramParcel.createStringArrayList();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<String> getPackageNames()
  {
    return mPackageNames;
  }
  
  public int getSequenceNumber()
  {
    return mSequenceNumber;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSequenceNumber);
    paramParcel.writeStringList(mPackageNames);
  }
}
