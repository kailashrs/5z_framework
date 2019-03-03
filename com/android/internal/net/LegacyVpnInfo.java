package com.android.internal.net;

import android.app.PendingIntent;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public class LegacyVpnInfo
  implements Parcelable
{
  public static final Parcelable.Creator<LegacyVpnInfo> CREATOR = new Parcelable.Creator()
  {
    public LegacyVpnInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      LegacyVpnInfo localLegacyVpnInfo = new LegacyVpnInfo();
      key = paramAnonymousParcel.readString();
      state = paramAnonymousParcel.readInt();
      intent = ((PendingIntent)paramAnonymousParcel.readParcelable(null));
      return localLegacyVpnInfo;
    }
    
    public LegacyVpnInfo[] newArray(int paramAnonymousInt)
    {
      return new LegacyVpnInfo[paramAnonymousInt];
    }
  };
  public static final int STATE_CONNECTED = 3;
  public static final int STATE_CONNECTING = 2;
  public static final int STATE_DISCONNECTED = 0;
  public static final int STATE_FAILED = 5;
  public static final int STATE_INITIALIZING = 1;
  public static final int STATE_TIMEOUT = 4;
  private static final String TAG = "LegacyVpnInfo";
  public PendingIntent intent;
  public String key;
  public int state = -1;
  
  public LegacyVpnInfo() {}
  
  public static int stateFromNetworkInfo(NetworkInfo paramNetworkInfo)
  {
    switch (2.$SwitchMap$android$net$NetworkInfo$DetailedState[paramNetworkInfo.getDetailedState().ordinal()])
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unhandled state ");
      localStringBuilder.append(paramNetworkInfo.getDetailedState());
      localStringBuilder.append(" ; treating as disconnected");
      Log.w("LegacyVpnInfo", localStringBuilder.toString());
      return 0;
    case 4: 
      return 5;
    case 3: 
      return 0;
    case 2: 
      return 3;
    }
    return 2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(key);
    paramParcel.writeInt(state);
    paramParcel.writeParcelable(intent, paramInt);
  }
}
