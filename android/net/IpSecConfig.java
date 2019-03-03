package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.annotations.VisibleForTesting;

public final class IpSecConfig
  implements Parcelable
{
  public static final Parcelable.Creator<IpSecConfig> CREATOR = new Parcelable.Creator()
  {
    public IpSecConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IpSecConfig(paramAnonymousParcel, null);
    }
    
    public IpSecConfig[] newArray(int paramAnonymousInt)
    {
      return new IpSecConfig[paramAnonymousInt];
    }
  };
  private static final String TAG = "IpSecConfig";
  private IpSecAlgorithm mAuthenticatedEncryption;
  private IpSecAlgorithm mAuthentication;
  private String mDestinationAddress = "";
  private int mEncapRemotePort;
  private int mEncapSocketResourceId = -1;
  private int mEncapType = 0;
  private IpSecAlgorithm mEncryption;
  private int mMarkMask;
  private int mMarkValue;
  private int mMode = 0;
  private int mNattKeepaliveInterval;
  private Network mNetwork;
  private String mSourceAddress = "";
  private int mSpiResourceId = -1;
  
  @VisibleForTesting
  public IpSecConfig() {}
  
  @VisibleForTesting
  public IpSecConfig(IpSecConfig paramIpSecConfig)
  {
    mMode = mMode;
    mSourceAddress = mSourceAddress;
    mDestinationAddress = mDestinationAddress;
    mNetwork = mNetwork;
    mSpiResourceId = mSpiResourceId;
    mEncryption = mEncryption;
    mAuthentication = mAuthentication;
    mAuthenticatedEncryption = mAuthenticatedEncryption;
    mEncapType = mEncapType;
    mEncapSocketResourceId = mEncapSocketResourceId;
    mEncapRemotePort = mEncapRemotePort;
    mNattKeepaliveInterval = mNattKeepaliveInterval;
    mMarkValue = mMarkValue;
    mMarkMask = mMarkMask;
  }
  
  private IpSecConfig(Parcel paramParcel)
  {
    mMode = paramParcel.readInt();
    mSourceAddress = paramParcel.readString();
    mDestinationAddress = paramParcel.readString();
    mNetwork = ((Network)paramParcel.readParcelable(Network.class.getClassLoader()));
    mSpiResourceId = paramParcel.readInt();
    mEncryption = ((IpSecAlgorithm)paramParcel.readParcelable(IpSecAlgorithm.class.getClassLoader()));
    mAuthentication = ((IpSecAlgorithm)paramParcel.readParcelable(IpSecAlgorithm.class.getClassLoader()));
    mAuthenticatedEncryption = ((IpSecAlgorithm)paramParcel.readParcelable(IpSecAlgorithm.class.getClassLoader()));
    mEncapType = paramParcel.readInt();
    mEncapSocketResourceId = paramParcel.readInt();
    mEncapRemotePort = paramParcel.readInt();
    mNattKeepaliveInterval = paramParcel.readInt();
    mMarkValue = paramParcel.readInt();
    mMarkMask = paramParcel.readInt();
  }
  
  @VisibleForTesting
  public static boolean equals(IpSecConfig paramIpSecConfig1, IpSecConfig paramIpSecConfig2)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if ((paramIpSecConfig1 != null) && (paramIpSecConfig2 != null))
    {
      if ((mMode == mMode) && (mSourceAddress.equals(mSourceAddress)) && (mDestinationAddress.equals(mDestinationAddress)) && (((mNetwork != null) && (mNetwork.equals(mNetwork))) || ((mNetwork == mNetwork) && (mEncapType == mEncapType) && (mEncapSocketResourceId == mEncapSocketResourceId) && (mEncapRemotePort == mEncapRemotePort) && (mNattKeepaliveInterval == mNattKeepaliveInterval) && (mSpiResourceId == mSpiResourceId) && (IpSecAlgorithm.equals(mEncryption, mEncryption)) && (IpSecAlgorithm.equals(mAuthenticatedEncryption, mAuthenticatedEncryption)) && (IpSecAlgorithm.equals(mAuthentication, mAuthentication)) && (mMarkValue == mMarkValue) && (mMarkMask == mMarkMask)))) {
        bool2 = true;
      }
      return bool2;
    }
    bool2 = bool1;
    if (paramIpSecConfig1 == paramIpSecConfig2) {
      bool2 = true;
    }
    return bool2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public IpSecAlgorithm getAuthenticatedEncryption()
  {
    return mAuthenticatedEncryption;
  }
  
  public IpSecAlgorithm getAuthentication()
  {
    return mAuthentication;
  }
  
  public String getDestinationAddress()
  {
    return mDestinationAddress;
  }
  
  public int getEncapRemotePort()
  {
    return mEncapRemotePort;
  }
  
  public int getEncapSocketResourceId()
  {
    return mEncapSocketResourceId;
  }
  
  public int getEncapType()
  {
    return mEncapType;
  }
  
  public IpSecAlgorithm getEncryption()
  {
    return mEncryption;
  }
  
  public int getMarkMask()
  {
    return mMarkMask;
  }
  
  public int getMarkValue()
  {
    return mMarkValue;
  }
  
  public int getMode()
  {
    return mMode;
  }
  
  public int getNattKeepaliveInterval()
  {
    return mNattKeepaliveInterval;
  }
  
  public Network getNetwork()
  {
    return mNetwork;
  }
  
  public String getSourceAddress()
  {
    return mSourceAddress;
  }
  
  public int getSpiResourceId()
  {
    return mSpiResourceId;
  }
  
  public void setAuthenticatedEncryption(IpSecAlgorithm paramIpSecAlgorithm)
  {
    mAuthenticatedEncryption = paramIpSecAlgorithm;
  }
  
  public void setAuthentication(IpSecAlgorithm paramIpSecAlgorithm)
  {
    mAuthentication = paramIpSecAlgorithm;
  }
  
  public void setDestinationAddress(String paramString)
  {
    mDestinationAddress = paramString;
  }
  
  public void setEncapRemotePort(int paramInt)
  {
    mEncapRemotePort = paramInt;
  }
  
  public void setEncapSocketResourceId(int paramInt)
  {
    mEncapSocketResourceId = paramInt;
  }
  
  public void setEncapType(int paramInt)
  {
    mEncapType = paramInt;
  }
  
  public void setEncryption(IpSecAlgorithm paramIpSecAlgorithm)
  {
    mEncryption = paramIpSecAlgorithm;
  }
  
  public void setMarkMask(int paramInt)
  {
    mMarkMask = paramInt;
  }
  
  public void setMarkValue(int paramInt)
  {
    mMarkValue = paramInt;
  }
  
  public void setMode(int paramInt)
  {
    mMode = paramInt;
  }
  
  public void setNattKeepaliveInterval(int paramInt)
  {
    mNattKeepaliveInterval = paramInt;
  }
  
  public void setNetwork(Network paramNetwork)
  {
    mNetwork = paramNetwork;
  }
  
  public void setSourceAddress(String paramString)
  {
    mSourceAddress = paramString;
  }
  
  public void setSpiResourceId(int paramInt)
  {
    mSpiResourceId = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mMode=");
    String str;
    if (mMode == 1) {
      str = "TUNNEL";
    } else {
      str = "TRANSPORT";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", mSourceAddress=");
    localStringBuilder.append(mSourceAddress);
    localStringBuilder.append(", mDestinationAddress=");
    localStringBuilder.append(mDestinationAddress);
    localStringBuilder.append(", mNetwork=");
    localStringBuilder.append(mNetwork);
    localStringBuilder.append(", mEncapType=");
    localStringBuilder.append(mEncapType);
    localStringBuilder.append(", mEncapSocketResourceId=");
    localStringBuilder.append(mEncapSocketResourceId);
    localStringBuilder.append(", mEncapRemotePort=");
    localStringBuilder.append(mEncapRemotePort);
    localStringBuilder.append(", mNattKeepaliveInterval=");
    localStringBuilder.append(mNattKeepaliveInterval);
    localStringBuilder.append("{mSpiResourceId=");
    localStringBuilder.append(mSpiResourceId);
    localStringBuilder.append(", mEncryption=");
    localStringBuilder.append(mEncryption);
    localStringBuilder.append(", mAuthentication=");
    localStringBuilder.append(mAuthentication);
    localStringBuilder.append(", mAuthenticatedEncryption=");
    localStringBuilder.append(mAuthenticatedEncryption);
    localStringBuilder.append(", mMarkValue=");
    localStringBuilder.append(mMarkValue);
    localStringBuilder.append(", mMarkMask=");
    localStringBuilder.append(mMarkMask);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mMode);
    paramParcel.writeString(mSourceAddress);
    paramParcel.writeString(mDestinationAddress);
    paramParcel.writeParcelable(mNetwork, paramInt);
    paramParcel.writeInt(mSpiResourceId);
    paramParcel.writeParcelable(mEncryption, paramInt);
    paramParcel.writeParcelable(mAuthentication, paramInt);
    paramParcel.writeParcelable(mAuthenticatedEncryption, paramInt);
    paramParcel.writeInt(mEncapType);
    paramParcel.writeInt(mEncapSocketResourceId);
    paramParcel.writeInt(mEncapRemotePort);
    paramParcel.writeInt(mNattKeepaliveInterval);
    paramParcel.writeInt(mMarkValue);
    paramParcel.writeInt(mMarkMask);
  }
}
