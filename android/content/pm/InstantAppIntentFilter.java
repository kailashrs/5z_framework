package android.content.pm;

import android.annotation.SystemApi;
import android.content.IntentFilter;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

@SystemApi
public final class InstantAppIntentFilter
  implements Parcelable
{
  public static final Parcelable.Creator<InstantAppIntentFilter> CREATOR = new Parcelable.Creator()
  {
    public InstantAppIntentFilter createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InstantAppIntentFilter(paramAnonymousParcel);
    }
    
    public InstantAppIntentFilter[] newArray(int paramAnonymousInt)
    {
      return new InstantAppIntentFilter[paramAnonymousInt];
    }
  };
  private final List<IntentFilter> mFilters = new ArrayList();
  private final String mSplitName;
  
  InstantAppIntentFilter(Parcel paramParcel)
  {
    mSplitName = paramParcel.readString();
    paramParcel.readList(mFilters, null);
  }
  
  public InstantAppIntentFilter(String paramString, List<IntentFilter> paramList)
  {
    if ((paramList != null) && (paramList.size() != 0))
    {
      mSplitName = paramString;
      mFilters.addAll(paramList);
      return;
    }
    throw new IllegalArgumentException();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<IntentFilter> getFilters()
  {
    return mFilters;
  }
  
  public String getSplitName()
  {
    return mSplitName;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mSplitName);
    paramParcel.writeList(mFilters);
  }
}
