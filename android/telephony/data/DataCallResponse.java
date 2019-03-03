package android.telephony.data;

import android.net.LinkAddress;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DataCallResponse
  implements Parcelable
{
  public static final Parcelable.Creator<DataCallResponse> CREATOR = new Parcelable.Creator()
  {
    public DataCallResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DataCallResponse(paramAnonymousParcel);
    }
    
    public DataCallResponse[] newArray(int paramAnonymousInt)
    {
      return new DataCallResponse[paramAnonymousInt];
    }
  };
  private final int mActive;
  private final List<LinkAddress> mAddresses;
  private final int mCid;
  private final List<InetAddress> mDnses;
  private final List<InetAddress> mGateways;
  private final String mIfname;
  private final int mMtu;
  private final List<String> mPcscfs;
  private final int mStatus;
  private final int mSuggestedRetryTime;
  private final String mType;
  
  public DataCallResponse(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, String paramString2, List<LinkAddress> paramList, List<InetAddress> paramList1, List<InetAddress> paramList2, List<String> paramList3, int paramInt5)
  {
    mStatus = paramInt1;
    mSuggestedRetryTime = paramInt2;
    mCid = paramInt3;
    mActive = paramInt4;
    if (paramString1 == null) {
      paramString1 = "";
    }
    mType = paramString1;
    if (paramString2 == null) {
      paramString2 = "";
    }
    mIfname = paramString2;
    if (paramList == null) {
      paramString1 = new ArrayList();
    } else {
      paramString1 = paramList;
    }
    mAddresses = paramString1;
    if (paramList1 == null) {
      paramString1 = new ArrayList();
    } else {
      paramString1 = paramList1;
    }
    mDnses = paramString1;
    if (paramList2 == null) {
      paramList2 = new ArrayList();
    }
    mGateways = paramList2;
    if (paramList3 == null) {
      paramList3 = new ArrayList();
    }
    mPcscfs = paramList3;
    mMtu = paramInt5;
  }
  
  public DataCallResponse(Parcel paramParcel)
  {
    mStatus = paramParcel.readInt();
    mSuggestedRetryTime = paramParcel.readInt();
    mCid = paramParcel.readInt();
    mActive = paramParcel.readInt();
    mType = paramParcel.readString();
    mIfname = paramParcel.readString();
    mAddresses = new ArrayList();
    paramParcel.readList(mAddresses, LinkAddress.class.getClassLoader());
    mDnses = new ArrayList();
    paramParcel.readList(mDnses, InetAddress.class.getClassLoader());
    mGateways = new ArrayList();
    paramParcel.readList(mGateways, InetAddress.class.getClassLoader());
    mPcscfs = new ArrayList();
    paramParcel.readList(mPcscfs, InetAddress.class.getClassLoader());
    mMtu = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof DataCallResponse)))
    {
      paramObject = (DataCallResponse)paramObject;
      if ((mStatus != mStatus) || (mSuggestedRetryTime != mSuggestedRetryTime) || (mCid != mCid) || (mActive != mActive) || (!mType.equals(mType)) || (!mIfname.equals(mIfname)) || (mAddresses.size() != mAddresses.size()) || (!mAddresses.containsAll(mAddresses)) || (mDnses.size() != mDnses.size()) || (!mDnses.containsAll(mDnses)) || (mGateways.size() != mGateways.size()) || (!mGateways.containsAll(mGateways)) || (mPcscfs.size() != mPcscfs.size()) || (!mPcscfs.containsAll(mPcscfs)) || (mMtu != mMtu)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getActive()
  {
    return mActive;
  }
  
  public List<LinkAddress> getAddresses()
  {
    return mAddresses;
  }
  
  public int getCallId()
  {
    return mCid;
  }
  
  public List<InetAddress> getDnses()
  {
    return mDnses;
  }
  
  public List<InetAddress> getGateways()
  {
    return mGateways;
  }
  
  public String getIfname()
  {
    return mIfname;
  }
  
  public int getMtu()
  {
    return mMtu;
  }
  
  public List<String> getPcscfs()
  {
    return mPcscfs;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public int getSuggestedRetryTime()
  {
    return mSuggestedRetryTime;
  }
  
  public String getType()
  {
    return mType;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mStatus), Integer.valueOf(mSuggestedRetryTime), Integer.valueOf(mCid), Integer.valueOf(mActive), mType, mIfname, mAddresses, mDnses, mGateways, mPcscfs, Integer.valueOf(mMtu) });
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("DataCallResponse: {");
    localStringBuffer.append(" status=");
    localStringBuffer.append(mStatus);
    localStringBuffer.append(" retry=");
    localStringBuffer.append(mSuggestedRetryTime);
    localStringBuffer.append(" cid=");
    localStringBuffer.append(mCid);
    localStringBuffer.append(" active=");
    localStringBuffer.append(mActive);
    localStringBuffer.append(" type=");
    localStringBuffer.append(mType);
    localStringBuffer.append(" ifname=");
    localStringBuffer.append(mIfname);
    localStringBuffer.append(" addresses=");
    localStringBuffer.append(mAddresses);
    localStringBuffer.append(" dnses=");
    localStringBuffer.append(mDnses);
    localStringBuffer.append(" gateways=");
    localStringBuffer.append(mGateways);
    localStringBuffer.append(" pcscf=");
    localStringBuffer.append(mPcscfs);
    localStringBuffer.append(" mtu=");
    localStringBuffer.append(mMtu);
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mStatus);
    paramParcel.writeInt(mSuggestedRetryTime);
    paramParcel.writeInt(mCid);
    paramParcel.writeInt(mActive);
    paramParcel.writeString(mType);
    paramParcel.writeString(mIfname);
    paramParcel.writeList(mAddresses);
    paramParcel.writeList(mDnses);
    paramParcel.writeList(mGateways);
    paramParcel.writeList(mPcscfs);
    paramParcel.writeInt(mMtu);
  }
}
