package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CellNetworkScanResult
  implements Parcelable
{
  public static final Parcelable.Creator<CellNetworkScanResult> CREATOR = new Parcelable.Creator()
  {
    public CellNetworkScanResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CellNetworkScanResult(paramAnonymousParcel, null);
    }
    
    public CellNetworkScanResult[] newArray(int paramAnonymousInt)
    {
      return new CellNetworkScanResult[paramAnonymousInt];
    }
  };
  public static final int STATUS_RADIO_GENERIC_FAILURE = 3;
  public static final int STATUS_RADIO_NOT_AVAILABLE = 2;
  public static final int STATUS_SUCCESS = 1;
  public static final int STATUS_UNKNOWN_ERROR = 4;
  private final List<OperatorInfo> mOperators;
  private final int mStatus;
  
  public CellNetworkScanResult(int paramInt, List<OperatorInfo> paramList)
  {
    mStatus = paramInt;
    mOperators = paramList;
  }
  
  private CellNetworkScanResult(Parcel paramParcel)
  {
    mStatus = paramParcel.readInt();
    int i = paramParcel.readInt();
    if (i > 0)
    {
      mOperators = new ArrayList();
      for (int j = 0; j < i; j++) {
        mOperators.add((OperatorInfo)OperatorInfo.CREATOR.createFromParcel(paramParcel));
      }
    }
    mOperators = null;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<OperatorInfo> getOperators()
  {
    return mOperators;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("CellNetworkScanResult: {");
    localStringBuffer.append(" status:");
    localStringBuffer.append(mStatus);
    if (mOperators != null)
    {
      Iterator localIterator = mOperators.iterator();
      while (localIterator.hasNext())
      {
        OperatorInfo localOperatorInfo = (OperatorInfo)localIterator.next();
        localStringBuffer.append(" network:");
        localStringBuffer.append(localOperatorInfo);
      }
    }
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mStatus);
    Iterator localIterator;
    if ((mOperators != null) && (mOperators.size() > 0))
    {
      paramParcel.writeInt(mOperators.size());
      localIterator = mOperators.iterator();
    }
    while (localIterator.hasNext())
    {
      ((OperatorInfo)localIterator.next()).writeToParcel(paramParcel, paramInt);
      continue;
      paramParcel.writeInt(0);
    }
  }
}
