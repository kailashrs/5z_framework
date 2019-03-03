package android.media.midi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class MidiDeviceStatus
  implements Parcelable
{
  public static final Parcelable.Creator<MidiDeviceStatus> CREATOR = new Parcelable.Creator()
  {
    public MidiDeviceStatus createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MidiDeviceStatus((MidiDeviceInfo)paramAnonymousParcel.readParcelable(MidiDeviceInfo.class.getClassLoader()), paramAnonymousParcel.createBooleanArray(), paramAnonymousParcel.createIntArray());
    }
    
    public MidiDeviceStatus[] newArray(int paramAnonymousInt)
    {
      return new MidiDeviceStatus[paramAnonymousInt];
    }
  };
  private static final String TAG = "MidiDeviceStatus";
  private final MidiDeviceInfo mDeviceInfo;
  private final boolean[] mInputPortOpen;
  private final int[] mOutputPortOpenCount;
  
  public MidiDeviceStatus(MidiDeviceInfo paramMidiDeviceInfo)
  {
    mDeviceInfo = paramMidiDeviceInfo;
    mInputPortOpen = new boolean[paramMidiDeviceInfo.getInputPortCount()];
    mOutputPortOpenCount = new int[paramMidiDeviceInfo.getOutputPortCount()];
  }
  
  public MidiDeviceStatus(MidiDeviceInfo paramMidiDeviceInfo, boolean[] paramArrayOfBoolean, int[] paramArrayOfInt)
  {
    mDeviceInfo = paramMidiDeviceInfo;
    mInputPortOpen = new boolean[paramArrayOfBoolean.length];
    System.arraycopy(paramArrayOfBoolean, 0, mInputPortOpen, 0, paramArrayOfBoolean.length);
    mOutputPortOpenCount = new int[paramArrayOfInt.length];
    System.arraycopy(paramArrayOfInt, 0, mOutputPortOpenCount, 0, paramArrayOfInt.length);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public MidiDeviceInfo getDeviceInfo()
  {
    return mDeviceInfo;
  }
  
  public int getOutputPortOpenCount(int paramInt)
  {
    return mOutputPortOpenCount[paramInt];
  }
  
  public boolean isInputPortOpen(int paramInt)
  {
    return mInputPortOpen[paramInt];
  }
  
  public String toString()
  {
    int i = mDeviceInfo.getInputPortCount();
    int j = mDeviceInfo.getOutputPortCount();
    StringBuilder localStringBuilder = new StringBuilder("mInputPortOpen=[");
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      localStringBuilder.append(mInputPortOpen[m]);
      if (m < i - 1) {
        localStringBuilder.append(",");
      }
    }
    localStringBuilder.append("] mOutputPortOpenCount=[");
    for (m = k; m < j; m++)
    {
      localStringBuilder.append(mOutputPortOpenCount[m]);
      if (m < j - 1) {
        localStringBuilder.append(",");
      }
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mDeviceInfo, paramInt);
    paramParcel.writeBooleanArray(mInputPortOpen);
    paramParcel.writeIntArray(mOutputPortOpenCount);
  }
}
