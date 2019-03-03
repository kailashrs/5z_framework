package android.hardware.display;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class WifiDisplayStatus
  implements Parcelable
{
  public static final Parcelable.Creator<WifiDisplayStatus> CREATOR = new Parcelable.Creator()
  {
    public WifiDisplayStatus createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      WifiDisplay localWifiDisplay = null;
      if (paramAnonymousParcel.readInt() != 0) {
        localWifiDisplay = (WifiDisplay)WifiDisplay.CREATOR.createFromParcel(paramAnonymousParcel);
      }
      WifiDisplay[] arrayOfWifiDisplay = (WifiDisplay[])WifiDisplay.CREATOR.newArray(paramAnonymousParcel.readInt());
      for (int m = 0; m < arrayOfWifiDisplay.length; m++) {
        arrayOfWifiDisplay[m] = ((WifiDisplay)WifiDisplay.CREATOR.createFromParcel(paramAnonymousParcel));
      }
      return new WifiDisplayStatus(i, j, k, localWifiDisplay, arrayOfWifiDisplay, (WifiDisplaySessionInfo)WifiDisplaySessionInfo.CREATOR.createFromParcel(paramAnonymousParcel));
    }
    
    public WifiDisplayStatus[] newArray(int paramAnonymousInt)
    {
      return new WifiDisplayStatus[paramAnonymousInt];
    }
  };
  public static final int DISPLAY_STATE_CONNECTED = 2;
  public static final int DISPLAY_STATE_CONNECTING = 1;
  public static final int DISPLAY_STATE_NOT_CONNECTED = 0;
  public static final int FEATURE_STATE_DISABLED = 1;
  public static final int FEATURE_STATE_OFF = 2;
  public static final int FEATURE_STATE_ON = 3;
  public static final int FEATURE_STATE_UNAVAILABLE = 0;
  public static final int SCAN_STATE_NOT_SCANNING = 0;
  public static final int SCAN_STATE_SCANNING = 1;
  private final WifiDisplay mActiveDisplay;
  private final int mActiveDisplayState;
  private final WifiDisplay[] mDisplays;
  private final int mFeatureState;
  private final int mScanState;
  private final WifiDisplaySessionInfo mSessionInfo;
  
  public WifiDisplayStatus()
  {
    this(0, 0, 0, null, WifiDisplay.EMPTY_ARRAY, null);
  }
  
  public WifiDisplayStatus(int paramInt1, int paramInt2, int paramInt3, WifiDisplay paramWifiDisplay, WifiDisplay[] paramArrayOfWifiDisplay, WifiDisplaySessionInfo paramWifiDisplaySessionInfo)
  {
    if (paramArrayOfWifiDisplay != null)
    {
      mFeatureState = paramInt1;
      mScanState = paramInt2;
      mActiveDisplayState = paramInt3;
      mActiveDisplay = paramWifiDisplay;
      mDisplays = paramArrayOfWifiDisplay;
      if (paramWifiDisplaySessionInfo != null) {
        paramWifiDisplay = paramWifiDisplaySessionInfo;
      } else {
        paramWifiDisplay = new WifiDisplaySessionInfo();
      }
      mSessionInfo = paramWifiDisplay;
      return;
    }
    throw new IllegalArgumentException("displays must not be null");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public WifiDisplay getActiveDisplay()
  {
    return mActiveDisplay;
  }
  
  public int getActiveDisplayState()
  {
    return mActiveDisplayState;
  }
  
  public WifiDisplay[] getDisplays()
  {
    return mDisplays;
  }
  
  public int getFeatureState()
  {
    return mFeatureState;
  }
  
  public int getScanState()
  {
    return mScanState;
  }
  
  public WifiDisplaySessionInfo getSessionInfo()
  {
    return mSessionInfo;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WifiDisplayStatus{featureState=");
    localStringBuilder.append(mFeatureState);
    localStringBuilder.append(", scanState=");
    localStringBuilder.append(mScanState);
    localStringBuilder.append(", activeDisplayState=");
    localStringBuilder.append(mActiveDisplayState);
    localStringBuilder.append(", activeDisplay=");
    localStringBuilder.append(mActiveDisplay);
    localStringBuilder.append(", displays=");
    localStringBuilder.append(Arrays.toString(mDisplays));
    localStringBuilder.append(", sessionInfo=");
    localStringBuilder.append(mSessionInfo);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mFeatureState);
    paramParcel.writeInt(mScanState);
    paramParcel.writeInt(mActiveDisplayState);
    Object localObject = mActiveDisplay;
    int i = 0;
    if (localObject != null)
    {
      paramParcel.writeInt(1);
      mActiveDisplay.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mDisplays.length);
    localObject = mDisplays;
    int j = localObject.length;
    while (i < j)
    {
      localObject[i].writeToParcel(paramParcel, paramInt);
      i++;
    }
    mSessionInfo.writeToParcel(paramParcel, paramInt);
  }
}
