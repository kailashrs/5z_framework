package android.media.tv;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

@SystemApi
public class TvStreamConfig
  implements Parcelable
{
  public static final Parcelable.Creator<TvStreamConfig> CREATOR = new Parcelable.Creator()
  {
    public TvStreamConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      try
      {
        TvStreamConfig.Builder localBuilder = new android/media/tv/TvStreamConfig$Builder;
        localBuilder.<init>();
        paramAnonymousParcel = localBuilder.streamId(paramAnonymousParcel.readInt()).type(paramAnonymousParcel.readInt()).maxWidth(paramAnonymousParcel.readInt()).maxHeight(paramAnonymousParcel.readInt()).generation(paramAnonymousParcel.readInt()).build();
        return paramAnonymousParcel;
      }
      catch (Exception paramAnonymousParcel)
      {
        Log.e(TvStreamConfig.TAG, "Exception creating TvStreamConfig from parcel", paramAnonymousParcel);
      }
      return null;
    }
    
    public TvStreamConfig[] newArray(int paramAnonymousInt)
    {
      return new TvStreamConfig[paramAnonymousInt];
    }
  };
  public static final int STREAM_TYPE_BUFFER_PRODUCER = 2;
  public static final int STREAM_TYPE_INDEPENDENT_VIDEO_SOURCE = 1;
  static final String TAG = TvStreamConfig.class.getSimpleName();
  private int mGeneration;
  private int mMaxHeight;
  private int mMaxWidth;
  private int mStreamId;
  private int mType;
  
  private TvStreamConfig() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if (paramObject == null) {
      return false;
    }
    if (!(paramObject instanceof TvStreamConfig)) {
      return false;
    }
    paramObject = (TvStreamConfig)paramObject;
    boolean bool2 = bool1;
    if (mGeneration == mGeneration)
    {
      bool2 = bool1;
      if (mStreamId == mStreamId)
      {
        bool2 = bool1;
        if (mType == mType)
        {
          bool2 = bool1;
          if (mMaxWidth == mMaxWidth)
          {
            bool2 = bool1;
            if (mMaxHeight == mMaxHeight) {
              bool2 = true;
            }
          }
        }
      }
    }
    return bool2;
  }
  
  public int getGeneration()
  {
    return mGeneration;
  }
  
  public int getMaxHeight()
  {
    return mMaxHeight;
  }
  
  public int getMaxWidth()
  {
    return mMaxWidth;
  }
  
  public int getStreamId()
  {
    return mStreamId;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("TvStreamConfig {mStreamId=");
    localStringBuilder.append(mStreamId);
    localStringBuilder.append(";mType=");
    localStringBuilder.append(mType);
    localStringBuilder.append(";mGeneration=");
    localStringBuilder.append(mGeneration);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mStreamId);
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mMaxWidth);
    paramParcel.writeInt(mMaxHeight);
    paramParcel.writeInt(mGeneration);
  }
  
  public static final class Builder
  {
    private Integer mGeneration;
    private Integer mMaxHeight;
    private Integer mMaxWidth;
    private Integer mStreamId;
    private Integer mType;
    
    public Builder() {}
    
    public TvStreamConfig build()
    {
      if ((mStreamId != null) && (mType != null) && (mMaxWidth != null) && (mMaxHeight != null) && (mGeneration != null))
      {
        TvStreamConfig localTvStreamConfig = new TvStreamConfig(null);
        TvStreamConfig.access$102(localTvStreamConfig, mStreamId.intValue());
        TvStreamConfig.access$202(localTvStreamConfig, mType.intValue());
        TvStreamConfig.access$302(localTvStreamConfig, mMaxWidth.intValue());
        TvStreamConfig.access$402(localTvStreamConfig, mMaxHeight.intValue());
        TvStreamConfig.access$502(localTvStreamConfig, mGeneration.intValue());
        return localTvStreamConfig;
      }
      throw new UnsupportedOperationException();
    }
    
    public Builder generation(int paramInt)
    {
      mGeneration = Integer.valueOf(paramInt);
      return this;
    }
    
    public Builder maxHeight(int paramInt)
    {
      mMaxHeight = Integer.valueOf(paramInt);
      return this;
    }
    
    public Builder maxWidth(int paramInt)
    {
      mMaxWidth = Integer.valueOf(paramInt);
      return this;
    }
    
    public Builder streamId(int paramInt)
    {
      mStreamId = Integer.valueOf(paramInt);
      return this;
    }
    
    public Builder type(int paramInt)
    {
      mType = Integer.valueOf(paramInt);
      return this;
    }
  }
}
