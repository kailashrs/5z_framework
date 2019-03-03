package android.media.midi;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public final class MidiDeviceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<MidiDeviceInfo> CREATOR = new Parcelable.Creator()
  {
    public MidiDeviceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      int m = paramAnonymousParcel.readInt();
      String[] arrayOfString1 = paramAnonymousParcel.createStringArray();
      String[] arrayOfString2 = paramAnonymousParcel.createStringArray();
      boolean bool;
      if (paramAnonymousParcel.readInt() == 1) {
        bool = true;
      } else {
        bool = false;
      }
      paramAnonymousParcel.readBundle();
      return new MidiDeviceInfo(i, j, k, m, arrayOfString1, arrayOfString2, paramAnonymousParcel.readBundle(), bool);
    }
    
    public MidiDeviceInfo[] newArray(int paramAnonymousInt)
    {
      return new MidiDeviceInfo[paramAnonymousInt];
    }
  };
  public static final String PROPERTY_ALSA_CARD = "alsa_card";
  public static final String PROPERTY_ALSA_DEVICE = "alsa_device";
  public static final String PROPERTY_BLUETOOTH_DEVICE = "bluetooth_device";
  public static final String PROPERTY_MANUFACTURER = "manufacturer";
  public static final String PROPERTY_NAME = "name";
  public static final String PROPERTY_PRODUCT = "product";
  public static final String PROPERTY_SERIAL_NUMBER = "serial_number";
  public static final String PROPERTY_SERVICE_INFO = "service_info";
  public static final String PROPERTY_USB_DEVICE = "usb_device";
  public static final String PROPERTY_VERSION = "version";
  private static final String TAG = "MidiDeviceInfo";
  public static final int TYPE_BLUETOOTH = 3;
  public static final int TYPE_USB = 1;
  public static final int TYPE_VIRTUAL = 2;
  private final int mId;
  private final int mInputPortCount;
  private final String[] mInputPortNames;
  private final boolean mIsPrivate;
  private final int mOutputPortCount;
  private final String[] mOutputPortNames;
  private final Bundle mProperties;
  private final int mType;
  
  public MidiDeviceInfo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String[] paramArrayOfString1, String[] paramArrayOfString2, Bundle paramBundle, boolean paramBoolean)
  {
    mType = paramInt1;
    mId = paramInt2;
    mInputPortCount = paramInt3;
    mOutputPortCount = paramInt4;
    if (paramArrayOfString1 == null) {
      mInputPortNames = new String[paramInt3];
    } else {
      mInputPortNames = paramArrayOfString1;
    }
    if (paramArrayOfString2 == null) {
      mOutputPortNames = new String[paramInt4];
    } else {
      mOutputPortNames = paramArrayOfString2;
    }
    mProperties = paramBundle;
    mIsPrivate = paramBoolean;
  }
  
  private Bundle getBasicProperties(String[] paramArrayOfString)
  {
    Bundle localBundle = new Bundle();
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      Object localObject1 = paramArrayOfString[j];
      Object localObject2 = mProperties.get((String)localObject1);
      if (localObject2 != null) {
        if ((localObject2 instanceof String))
        {
          localBundle.putString((String)localObject1, (String)localObject2);
        }
        else if ((localObject2 instanceof Integer))
        {
          localBundle.putInt((String)localObject1, ((Integer)localObject2).intValue());
        }
        else
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unsupported property type: ");
          ((StringBuilder)localObject1).append(localObject2.getClass().getName());
          Log.w("MidiDeviceInfo", ((StringBuilder)localObject1).toString());
        }
      }
    }
    return localBundle;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof MidiDeviceInfo;
    boolean bool2 = false;
    if (bool1)
    {
      if (mId == mId) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getInputPortCount()
  {
    return mInputPortCount;
  }
  
  public int getOutputPortCount()
  {
    return mOutputPortCount;
  }
  
  public PortInfo[] getPorts()
  {
    PortInfo[] arrayOfPortInfo = new PortInfo[mInputPortCount + mOutputPortCount];
    int i = 0;
    int j = 0;
    int k = 0;
    while (k < mInputPortCount)
    {
      arrayOfPortInfo[j] = new PortInfo(1, k, mInputPortNames[k]);
      k++;
      j++;
    }
    k = j;
    for (j = i; j < mOutputPortCount; j++)
    {
      arrayOfPortInfo[k] = new PortInfo(2, j, mOutputPortNames[j]);
      k++;
    }
    return arrayOfPortInfo;
  }
  
  public Bundle getProperties()
  {
    return mProperties;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public int hashCode()
  {
    return mId;
  }
  
  public boolean isPrivate()
  {
    return mIsPrivate;
  }
  
  public String toString()
  {
    mProperties.getString("name");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("MidiDeviceInfo[mType=");
    localStringBuilder.append(mType);
    localStringBuilder.append(",mInputPortCount=");
    localStringBuilder.append(mInputPortCount);
    localStringBuilder.append(",mOutputPortCount=");
    localStringBuilder.append(mOutputPortCount);
    localStringBuilder.append(",mProperties=");
    localStringBuilder.append(mProperties);
    localStringBuilder.append(",mIsPrivate=");
    localStringBuilder.append(mIsPrivate);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mId);
    paramParcel.writeInt(mInputPortCount);
    paramParcel.writeInt(mOutputPortCount);
    paramParcel.writeStringArray(mInputPortNames);
    paramParcel.writeStringArray(mOutputPortNames);
    paramParcel.writeInt(mIsPrivate);
    paramParcel.writeBundle(getBasicProperties(new String[] { "name", "manufacturer", "product", "version", "serial_number", "alsa_card", "alsa_device" }));
    paramParcel.writeBundle(mProperties);
  }
  
  public static final class PortInfo
  {
    public static final int TYPE_INPUT = 1;
    public static final int TYPE_OUTPUT = 2;
    private final String mName;
    private final int mPortNumber;
    private final int mPortType;
    
    PortInfo(int paramInt1, int paramInt2, String paramString)
    {
      mPortType = paramInt1;
      mPortNumber = paramInt2;
      if (paramString == null) {
        paramString = "";
      }
      mName = paramString;
    }
    
    public String getName()
    {
      return mName;
    }
    
    public int getPortNumber()
    {
      return mPortNumber;
    }
    
    public int getType()
    {
      return mPortType;
    }
  }
}
