package android.hardware.display;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class WifiDisplaySessionInfo
  implements Parcelable
{
  public static final Parcelable.Creator<WifiDisplaySessionInfo> CREATOR = new Parcelable.Creator()
  {
    public WifiDisplaySessionInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      if (paramAnonymousParcel.readInt() != 0) {}
      for (boolean bool = true;; bool = false) {
        break;
      }
      return new WifiDisplaySessionInfo(bool, paramAnonymousParcel.readInt(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString());
    }
    
    public WifiDisplaySessionInfo[] newArray(int paramAnonymousInt)
    {
      return new WifiDisplaySessionInfo[paramAnonymousInt];
    }
  };
  private final boolean mClient;
  private final String mGroupId;
  private final String mIP;
  private final String mPassphrase;
  private final int mSessionId;
  
  public WifiDisplaySessionInfo()
  {
    this(true, 0, "", "", "");
  }
  
  public WifiDisplaySessionInfo(boolean paramBoolean, int paramInt, String paramString1, String paramString2, String paramString3)
  {
    mClient = paramBoolean;
    mSessionId = paramInt;
    mGroupId = paramString1;
    mPassphrase = paramString2;
    mIP = paramString3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getGroupId()
  {
    return mGroupId;
  }
  
  public String getIP()
  {
    return mIP;
  }
  
  public String getPassphrase()
  {
    return mPassphrase;
  }
  
  public int getSessionId()
  {
    return mSessionId;
  }
  
  public boolean isClient()
  {
    return mClient;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WifiDisplaySessionInfo:\n    Client/Owner: ");
    String str;
    if (mClient) {
      str = "Client";
    } else {
      str = "Owner";
    }
    localStringBuilder.append(str);
    localStringBuilder.append("\n    GroupId: ");
    localStringBuilder.append(mGroupId);
    localStringBuilder.append("\n    Passphrase: ");
    localStringBuilder.append(mPassphrase);
    localStringBuilder.append("\n    SessionId: ");
    localStringBuilder.append(mSessionId);
    localStringBuilder.append("\n    IP Address: ");
    localStringBuilder.append(mIP);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mClient);
    paramParcel.writeInt(mSessionId);
    paramParcel.writeString(mGroupId);
    paramParcel.writeString(mPassphrase);
    paramParcel.writeString(mIP);
  }
}
