package android.media.tv;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class TvInputHardwareInfo
  implements Parcelable
{
  public static final int CABLE_CONNECTION_STATUS_CONNECTED = 1;
  public static final int CABLE_CONNECTION_STATUS_DISCONNECTED = 2;
  public static final int CABLE_CONNECTION_STATUS_UNKNOWN = 0;
  public static final Parcelable.Creator<TvInputHardwareInfo> CREATOR = new Parcelable.Creator()
  {
    public TvInputHardwareInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      try
      {
        TvInputHardwareInfo localTvInputHardwareInfo = new android/media/tv/TvInputHardwareInfo;
        localTvInputHardwareInfo.<init>(null);
        localTvInputHardwareInfo.readFromParcel(paramAnonymousParcel);
        return localTvInputHardwareInfo;
      }
      catch (Exception paramAnonymousParcel)
      {
        Log.e("TvInputHardwareInfo", "Exception creating TvInputHardwareInfo from parcel", paramAnonymousParcel);
      }
      return null;
    }
    
    public TvInputHardwareInfo[] newArray(int paramAnonymousInt)
    {
      return new TvInputHardwareInfo[paramAnonymousInt];
    }
  };
  static final String TAG = "TvInputHardwareInfo";
  public static final int TV_INPUT_TYPE_COMPONENT = 6;
  public static final int TV_INPUT_TYPE_COMPOSITE = 3;
  public static final int TV_INPUT_TYPE_DISPLAY_PORT = 10;
  public static final int TV_INPUT_TYPE_DVI = 8;
  public static final int TV_INPUT_TYPE_HDMI = 9;
  public static final int TV_INPUT_TYPE_OTHER_HARDWARE = 1;
  public static final int TV_INPUT_TYPE_SCART = 5;
  public static final int TV_INPUT_TYPE_SVIDEO = 4;
  public static final int TV_INPUT_TYPE_TUNER = 2;
  public static final int TV_INPUT_TYPE_VGA = 7;
  private String mAudioAddress;
  private int mAudioType;
  private int mCableConnectionStatus;
  private int mDeviceId;
  private int mHdmiPortId;
  private int mType;
  
  private TvInputHardwareInfo() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getAudioAddress()
  {
    return mAudioAddress;
  }
  
  public int getAudioType()
  {
    return mAudioType;
  }
  
  public int getCableConnectionStatus()
  {
    return mCableConnectionStatus;
  }
  
  public int getDeviceId()
  {
    return mDeviceId;
  }
  
  public int getHdmiPortId()
  {
    if (mType == 9) {
      return mHdmiPortId;
    }
    throw new IllegalStateException();
  }
  
  public int getType()
  {
    return mType;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mDeviceId = paramParcel.readInt();
    mType = paramParcel.readInt();
    mAudioType = paramParcel.readInt();
    mAudioAddress = paramParcel.readString();
    if (mType == 9) {
      mHdmiPortId = paramParcel.readInt();
    }
    mCableConnectionStatus = paramParcel.readInt();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("TvInputHardwareInfo {id=");
    localStringBuilder.append(mDeviceId);
    localStringBuilder.append(", type=");
    localStringBuilder.append(mType);
    localStringBuilder.append(", audio_type=");
    localStringBuilder.append(mAudioType);
    localStringBuilder.append(", audio_addr=");
    localStringBuilder.append(mAudioAddress);
    if (mType == 9)
    {
      localStringBuilder.append(", hdmi_port=");
      localStringBuilder.append(mHdmiPortId);
    }
    localStringBuilder.append(", cable_connection_status=");
    localStringBuilder.append(mCableConnectionStatus);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mDeviceId);
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mAudioType);
    paramParcel.writeString(mAudioAddress);
    if (mType == 9) {
      paramParcel.writeInt(mHdmiPortId);
    }
    paramParcel.writeInt(mCableConnectionStatus);
  }
  
  public static final class Builder
  {
    private String mAudioAddress = "";
    private int mAudioType = 0;
    private Integer mCableConnectionStatus = Integer.valueOf(0);
    private Integer mDeviceId = null;
    private Integer mHdmiPortId = null;
    private Integer mType = null;
    
    public Builder() {}
    
    public Builder audioAddress(String paramString)
    {
      mAudioAddress = paramString;
      return this;
    }
    
    public Builder audioType(int paramInt)
    {
      mAudioType = paramInt;
      return this;
    }
    
    public TvInputHardwareInfo build()
    {
      if ((mDeviceId != null) && (mType != null))
      {
        if (((mType.intValue() == 9) && (mHdmiPortId == null)) || ((mType.intValue() != 9) && (mHdmiPortId != null))) {
          throw new UnsupportedOperationException();
        }
        TvInputHardwareInfo localTvInputHardwareInfo = new TvInputHardwareInfo(null);
        TvInputHardwareInfo.access$102(localTvInputHardwareInfo, mDeviceId.intValue());
        TvInputHardwareInfo.access$202(localTvInputHardwareInfo, mType.intValue());
        TvInputHardwareInfo.access$302(localTvInputHardwareInfo, mAudioType);
        if (mAudioType != 0) {
          TvInputHardwareInfo.access$402(localTvInputHardwareInfo, mAudioAddress);
        }
        if (mHdmiPortId != null) {
          TvInputHardwareInfo.access$502(localTvInputHardwareInfo, mHdmiPortId.intValue());
        }
        TvInputHardwareInfo.access$602(localTvInputHardwareInfo, mCableConnectionStatus.intValue());
        return localTvInputHardwareInfo;
      }
      throw new UnsupportedOperationException();
    }
    
    public Builder cableConnectionStatus(int paramInt)
    {
      mCableConnectionStatus = Integer.valueOf(paramInt);
      return this;
    }
    
    public Builder deviceId(int paramInt)
    {
      mDeviceId = Integer.valueOf(paramInt);
      return this;
    }
    
    public Builder hdmiPortId(int paramInt)
    {
      mHdmiPortId = Integer.valueOf(paramInt);
      return this;
    }
    
    public Builder type(int paramInt)
    {
      mType = Integer.valueOf(paramInt);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CableConnectionStatus {}
}
