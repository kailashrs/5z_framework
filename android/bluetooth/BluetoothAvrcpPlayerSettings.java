package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class BluetoothAvrcpPlayerSettings
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothAvrcpPlayerSettings> CREATOR = new Parcelable.Creator()
  {
    public BluetoothAvrcpPlayerSettings createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothAvrcpPlayerSettings(paramAnonymousParcel, null);
    }
    
    public BluetoothAvrcpPlayerSettings[] newArray(int paramAnonymousInt)
    {
      return new BluetoothAvrcpPlayerSettings[paramAnonymousInt];
    }
  };
  public static final int SETTING_EQUALIZER = 1;
  public static final int SETTING_REPEAT = 2;
  public static final int SETTING_SCAN = 8;
  public static final int SETTING_SHUFFLE = 4;
  public static final int STATE_ALL_TRACK = 3;
  public static final int STATE_GROUP = 4;
  public static final int STATE_INVALID = -1;
  public static final int STATE_OFF = 0;
  public static final int STATE_ON = 1;
  public static final int STATE_SINGLE_TRACK = 2;
  public static final String TAG = "BluetoothAvrcpPlayerSettings";
  private int mSettings;
  private Map<Integer, Integer> mSettingsValue = new HashMap();
  
  public BluetoothAvrcpPlayerSettings(int paramInt)
  {
    mSettings = paramInt;
  }
  
  private BluetoothAvrcpPlayerSettings(Parcel paramParcel)
  {
    mSettings = paramParcel.readInt();
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++) {
      mSettingsValue.put(Integer.valueOf(paramParcel.readInt()), Integer.valueOf(paramParcel.readInt()));
    }
  }
  
  public void addSettingValue(int paramInt1, int paramInt2)
  {
    if ((mSettings & paramInt1) != 0)
    {
      mSettingsValue.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Setting not supported: ");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" ");
    localStringBuilder.append(mSettings);
    Log.e("BluetoothAvrcpPlayerSettings", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Setting not supported: ");
    localStringBuilder.append(paramInt1);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getSettingValue(int paramInt)
  {
    if ((mSettings & paramInt) != 0)
    {
      localObject = (Integer)mSettingsValue.get(Integer.valueOf(paramInt));
      if (localObject == null) {
        return -1;
      }
      return ((Integer)localObject).intValue();
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Setting not supported: ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" ");
    ((StringBuilder)localObject).append(mSettings);
    Log.e("BluetoothAvrcpPlayerSettings", ((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Setting not supported: ");
    ((StringBuilder)localObject).append(paramInt);
    throw new IllegalStateException(((StringBuilder)localObject).toString());
  }
  
  public int getSettings()
  {
    return mSettings;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSettings);
    paramParcel.writeInt(mSettingsValue.size());
    Iterator localIterator = mSettingsValue.keySet().iterator();
    while (localIterator.hasNext())
    {
      paramInt = ((Integer)localIterator.next()).intValue();
      paramParcel.writeInt(paramInt);
      paramParcel.writeInt(((Integer)mSettingsValue.get(Integer.valueOf(paramInt))).intValue());
    }
  }
}
