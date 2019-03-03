package com.android.internal.location;

import android.location.LocationRequest;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.TimeUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ProviderRequest
  implements Parcelable
{
  public static final Parcelable.Creator<ProviderRequest> CREATOR = new Parcelable.Creator()
  {
    public ProviderRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      ProviderRequest localProviderRequest = new ProviderRequest();
      int i = paramAnonymousParcel.readInt();
      int j = 0;
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      reportLocation = bool;
      interval = paramAnonymousParcel.readLong();
      lowPowerMode = paramAnonymousParcel.readBoolean();
      i = paramAnonymousParcel.readInt();
      while (j < i)
      {
        locationRequests.add((LocationRequest)LocationRequest.CREATOR.createFromParcel(paramAnonymousParcel));
        j++;
      }
      return localProviderRequest;
    }
    
    public ProviderRequest[] newArray(int paramAnonymousInt)
    {
      return new ProviderRequest[paramAnonymousInt];
    }
  };
  public long interval = Long.MAX_VALUE;
  public List<LocationRequest> locationRequests = new ArrayList();
  public boolean lowPowerMode = false;
  public boolean reportLocation = false;
  
  public ProviderRequest() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("ProviderRequest[");
    if (reportLocation)
    {
      localStringBuilder1.append("ON");
      localStringBuilder1.append(" interval=");
      TimeUtils.formatDuration(interval, localStringBuilder1);
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" lowPowerMode=");
      localStringBuilder2.append(lowPowerMode);
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    else
    {
      localStringBuilder1.append("OFF");
    }
    localStringBuilder1.append(']');
    return localStringBuilder1.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(reportLocation);
    paramParcel.writeLong(interval);
    paramParcel.writeBoolean(lowPowerMode);
    paramParcel.writeInt(locationRequests.size());
    Iterator localIterator = locationRequests.iterator();
    while (localIterator.hasNext()) {
      ((LocationRequest)localIterator.next()).writeToParcel(paramParcel, paramInt);
    }
  }
}
