package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import java.util.UUID;

public final class BluetoothHeadsetClientCall
  implements Parcelable
{
  public static final int CALL_STATE_ACTIVE = 0;
  public static final int CALL_STATE_ALERTING = 3;
  public static final int CALL_STATE_DIALING = 2;
  public static final int CALL_STATE_HELD = 1;
  public static final int CALL_STATE_HELD_BY_RESPONSE_AND_HOLD = 6;
  public static final int CALL_STATE_INCOMING = 4;
  public static final int CALL_STATE_TERMINATED = 7;
  public static final int CALL_STATE_WAITING = 5;
  public static final Parcelable.Creator<BluetoothHeadsetClientCall> CREATOR = new Parcelable.Creator()
  {
    public BluetoothHeadsetClientCall createFromParcel(Parcel paramAnonymousParcel)
    {
      BluetoothDevice localBluetoothDevice = (BluetoothDevice)paramAnonymousParcel.readParcelable(null);
      int i = paramAnonymousParcel.readInt();
      UUID localUUID = UUID.fromString(paramAnonymousParcel.readString());
      int j = paramAnonymousParcel.readInt();
      String str = paramAnonymousParcel.readString();
      boolean bool1;
      if (paramAnonymousParcel.readInt() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramAnonymousParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3;
      if (paramAnonymousParcel.readInt() == 1) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      return new BluetoothHeadsetClientCall(localBluetoothDevice, i, localUUID, j, str, bool1, bool2, bool3);
    }
    
    public BluetoothHeadsetClientCall[] newArray(int paramAnonymousInt)
    {
      return new BluetoothHeadsetClientCall[paramAnonymousInt];
    }
  };
  private final long mCreationElapsedMilli;
  private final BluetoothDevice mDevice;
  private final int mId;
  private final boolean mInBandRing;
  private boolean mMultiParty;
  private String mNumber;
  private final boolean mOutgoing;
  private int mState;
  private final UUID mUUID;
  
  public BluetoothHeadsetClientCall(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    this(paramBluetoothDevice, paramInt1, UUID.randomUUID(), paramInt2, paramString, paramBoolean1, paramBoolean2, paramBoolean3);
  }
  
  public BluetoothHeadsetClientCall(BluetoothDevice paramBluetoothDevice, int paramInt1, UUID paramUUID, int paramInt2, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    mDevice = paramBluetoothDevice;
    mId = paramInt1;
    mUUID = paramUUID;
    mState = paramInt2;
    if (paramString != null) {
      paramBluetoothDevice = paramString;
    } else {
      paramBluetoothDevice = "";
    }
    mNumber = paramBluetoothDevice;
    mMultiParty = paramBoolean1;
    mOutgoing = paramBoolean2;
    mInBandRing = paramBoolean3;
    mCreationElapsedMilli = SystemClock.elapsedRealtime();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getCreationElapsedMilli()
  {
    return mCreationElapsedMilli;
  }
  
  public BluetoothDevice getDevice()
  {
    return mDevice;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public String getNumber()
  {
    return mNumber;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public UUID getUUID()
  {
    return mUUID;
  }
  
  public boolean isInBandRing()
  {
    return mInBandRing;
  }
  
  public boolean isMultiParty()
  {
    return mMultiParty;
  }
  
  public boolean isOutgoing()
  {
    return mOutgoing;
  }
  
  public void setMultiParty(boolean paramBoolean)
  {
    mMultiParty = paramBoolean;
  }
  
  public void setNumber(String paramString)
  {
    mNumber = paramString;
  }
  
  public void setState(int paramInt)
  {
    mState = paramInt;
  }
  
  public String toString()
  {
    return toString(false);
  }
  
  public String toString(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("BluetoothHeadsetClientCall{mDevice: ");
    Object localObject;
    if (paramBoolean) {
      localObject = mDevice;
    } else {
      localObject = Integer.valueOf(mDevice.hashCode());
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append(", mId: ");
    localStringBuilder.append(mId);
    localStringBuilder.append(", mUUID: ");
    localStringBuilder.append(mUUID);
    localStringBuilder.append(", mState: ");
    switch (mState)
    {
    default: 
      localStringBuilder.append(mState);
      break;
    case 7: 
      localStringBuilder.append("TERMINATED");
      break;
    case 6: 
      localStringBuilder.append("HELD_BY_RESPONSE_AND_HOLD");
      break;
    case 5: 
      localStringBuilder.append("WAITING");
      break;
    case 4: 
      localStringBuilder.append("INCOMING");
      break;
    case 3: 
      localStringBuilder.append("ALERTING");
      break;
    case 2: 
      localStringBuilder.append("DIALING");
      break;
    case 1: 
      localStringBuilder.append("HELD");
      break;
    case 0: 
      localStringBuilder.append("ACTIVE");
    }
    localStringBuilder.append(", mNumber: ");
    if (paramBoolean) {
      localObject = mNumber;
    } else {
      localObject = Integer.valueOf(mNumber.hashCode());
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append(", mMultiParty: ");
    localStringBuilder.append(mMultiParty);
    localStringBuilder.append(", mOutgoing: ");
    localStringBuilder.append(mOutgoing);
    localStringBuilder.append(", mInBandRing: ");
    localStringBuilder.append(mInBandRing);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mDevice, 0);
    paramParcel.writeInt(mId);
    paramParcel.writeString(mUUID.toString());
    paramParcel.writeInt(mState);
    paramParcel.writeString(mNumber);
    paramParcel.writeInt(mMultiParty);
    paramParcel.writeInt(mOutgoing);
    paramParcel.writeInt(mInBandRing);
  }
}
