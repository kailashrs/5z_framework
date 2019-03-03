package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;

public final class NetworkScanRequest
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkScanRequest> CREATOR = new Parcelable.Creator()
  {
    public NetworkScanRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkScanRequest(paramAnonymousParcel, null);
    }
    
    public NetworkScanRequest[] newArray(int paramAnonymousInt)
    {
      return new NetworkScanRequest[paramAnonymousInt];
    }
  };
  public static final int MAX_BANDS = 8;
  public static final int MAX_CHANNELS = 32;
  public static final int MAX_INCREMENTAL_PERIODICITY_SEC = 10;
  public static final int MAX_MCC_MNC_LIST_SIZE = 20;
  public static final int MAX_RADIO_ACCESS_NETWORKS = 8;
  public static final int MAX_SEARCH_MAX_SEC = 3600;
  public static final int MAX_SEARCH_PERIODICITY_SEC = 300;
  public static final int MIN_INCREMENTAL_PERIODICITY_SEC = 1;
  public static final int MIN_SEARCH_MAX_SEC = 60;
  public static final int MIN_SEARCH_PERIODICITY_SEC = 5;
  public static final int SCAN_TYPE_ONE_SHOT = 0;
  public static final int SCAN_TYPE_PERIODIC = 1;
  private boolean mIncrementalResults;
  private int mIncrementalResultsPeriodicity;
  private int mMaxSearchTime;
  private ArrayList<String> mMccMncs;
  private int mScanType;
  private int mSearchPeriodicity;
  private RadioAccessSpecifier[] mSpecifiers;
  
  public NetworkScanRequest(int paramInt1, RadioAccessSpecifier[] paramArrayOfRadioAccessSpecifier, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, ArrayList<String> paramArrayList)
  {
    mScanType = paramInt1;
    if (paramArrayOfRadioAccessSpecifier != null) {
      mSpecifiers = ((RadioAccessSpecifier[])paramArrayOfRadioAccessSpecifier.clone());
    } else {
      mSpecifiers = null;
    }
    mSearchPeriodicity = paramInt2;
    mMaxSearchTime = paramInt3;
    mIncrementalResults = paramBoolean;
    mIncrementalResultsPeriodicity = paramInt4;
    if (paramArrayList != null) {
      mMccMncs = ((ArrayList)paramArrayList.clone());
    } else {
      mMccMncs = new ArrayList();
    }
  }
  
  private NetworkScanRequest(Parcel paramParcel)
  {
    mScanType = paramParcel.readInt();
    mSpecifiers = ((RadioAccessSpecifier[])paramParcel.readParcelableArray(Object.class.getClassLoader(), RadioAccessSpecifier.class));
    mSearchPeriodicity = paramParcel.readInt();
    mMaxSearchTime = paramParcel.readInt();
    mIncrementalResults = paramParcel.readBoolean();
    mIncrementalResultsPeriodicity = paramParcel.readInt();
    mMccMncs = new ArrayList();
    paramParcel.readStringList(mMccMncs);
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
      NetworkScanRequest localNetworkScanRequest = (NetworkScanRequest)paramObject;
      if (paramObject == null) {
        return false;
      }
      boolean bool2 = bool1;
      if (mScanType == mScanType)
      {
        bool2 = bool1;
        if (Arrays.equals(mSpecifiers, mSpecifiers))
        {
          bool2 = bool1;
          if (mSearchPeriodicity == mSearchPeriodicity)
          {
            bool2 = bool1;
            if (mMaxSearchTime == mMaxSearchTime)
            {
              bool2 = bool1;
              if (mIncrementalResults == mIncrementalResults)
              {
                bool2 = bool1;
                if (mIncrementalResultsPeriodicity == mIncrementalResultsPeriodicity)
                {
                  bool2 = bool1;
                  if (mMccMncs != null)
                  {
                    bool2 = bool1;
                    if (mMccMncs.equals(mMccMncs)) {
                      bool2 = true;
                    }
                  }
                }
              }
            }
          }
        }
      }
      return bool2;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public boolean getIncrementalResults()
  {
    return mIncrementalResults;
  }
  
  public int getIncrementalResultsPeriodicity()
  {
    return mIncrementalResultsPeriodicity;
  }
  
  public int getMaxSearchTime()
  {
    return mMaxSearchTime;
  }
  
  public ArrayList<String> getPlmns()
  {
    return (ArrayList)mMccMncs.clone();
  }
  
  public int getScanType()
  {
    return mScanType;
  }
  
  public int getSearchPeriodicity()
  {
    return mSearchPeriodicity;
  }
  
  public RadioAccessSpecifier[] getSpecifiers()
  {
    RadioAccessSpecifier[] arrayOfRadioAccessSpecifier;
    if (mSpecifiers == null) {
      arrayOfRadioAccessSpecifier = null;
    } else {
      arrayOfRadioAccessSpecifier = (RadioAccessSpecifier[])mSpecifiers.clone();
    }
    return arrayOfRadioAccessSpecifier;
  }
  
  public int hashCode()
  {
    int i = mScanType;
    int j = Arrays.hashCode(mSpecifiers);
    int k = mSearchPeriodicity;
    int m = mMaxSearchTime;
    boolean bool = mIncrementalResults;
    int n = 1;
    if (bool != true) {
      n = 0;
    }
    return i * 31 + j * 37 + k * 41 + m * 43 + n * 47 + mIncrementalResultsPeriodicity * 53 + mMccMncs.hashCode() * 59;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mScanType);
    paramParcel.writeParcelableArray(mSpecifiers, paramInt);
    paramParcel.writeInt(mSearchPeriodicity);
    paramParcel.writeInt(mMaxSearchTime);
    paramParcel.writeBoolean(mIncrementalResults);
    paramParcel.writeInt(mIncrementalResultsPeriodicity);
    paramParcel.writeStringList(mMccMncs);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScanType {}
}
