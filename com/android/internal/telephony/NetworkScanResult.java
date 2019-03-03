package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.CellInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class NetworkScanResult
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkScanResult> CREATOR = new Parcelable.Creator()
  {
    public NetworkScanResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkScanResult(paramAnonymousParcel, null);
    }
    
    public NetworkScanResult[] newArray(int paramAnonymousInt)
    {
      return new NetworkScanResult[paramAnonymousInt];
    }
  };
  public static final int SCAN_STATUS_COMPLETE = 2;
  public static final int SCAN_STATUS_PARTIAL = 1;
  public List<CellInfo> networkInfos;
  public int scanError;
  public int scanStatus;
  
  public NetworkScanResult(int paramInt1, int paramInt2, List<CellInfo> paramList)
  {
    scanStatus = paramInt1;
    scanError = paramInt2;
    networkInfos = paramList;
  }
  
  private NetworkScanResult(Parcel paramParcel)
  {
    scanStatus = paramParcel.readInt();
    scanError = paramParcel.readInt();
    ArrayList localArrayList = new ArrayList();
    paramParcel.readParcelableList(localArrayList, Object.class.getClassLoader());
    networkInfos = localArrayList;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    try
    {
      NetworkScanResult localNetworkScanResult = (NetworkScanResult)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (scanStatus == scanStatus)
      {
        bool2 = bool1;
        if (scanError == scanError)
        {
          bool2 = bool1;
          if (networkInfos.equals(networkInfos)) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public int hashCode()
  {
    return scanStatus * 31 + scanError * 23 + Objects.hashCode(networkInfos) * 37;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(scanStatus);
    paramParcel.writeInt(scanError);
    paramParcel.writeParcelableList(networkInfos, paramInt);
  }
}
