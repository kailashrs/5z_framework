package android.media;

import android.util.Pair;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public final class MicrophoneInfo
{
  public static final int CHANNEL_MAPPING_DIRECT = 1;
  public static final int CHANNEL_MAPPING_PROCESSED = 2;
  public static final int DIRECTIONALITY_BI_DIRECTIONAL = 2;
  public static final int DIRECTIONALITY_CARDIOID = 3;
  public static final int DIRECTIONALITY_HYPER_CARDIOID = 4;
  public static final int DIRECTIONALITY_OMNI = 1;
  public static final int DIRECTIONALITY_SUPER_CARDIOID = 5;
  public static final int DIRECTIONALITY_UNKNOWN = 0;
  public static final int GROUP_UNKNOWN = -1;
  public static final int INDEX_IN_THE_GROUP_UNKNOWN = -1;
  public static final int LOCATION_MAINBODY = 1;
  public static final int LOCATION_MAINBODY_MOVABLE = 2;
  public static final int LOCATION_PERIPHERAL = 3;
  public static final int LOCATION_UNKNOWN = 0;
  public static final Coordinate3F ORIENTATION_UNKNOWN = new Coordinate3F(0.0F, 0.0F, 0.0F);
  public static final Coordinate3F POSITION_UNKNOWN = new Coordinate3F(-3.4028235E38F, -3.4028235E38F, -3.4028235E38F);
  public static final float SENSITIVITY_UNKNOWN = -3.4028235E38F;
  public static final float SPL_UNKNOWN = -3.4028235E38F;
  private String mAddress;
  private List<Pair<Integer, Integer>> mChannelMapping;
  private String mDeviceId;
  private int mDirectionality;
  private List<Pair<Float, Float>> mFrequencyResponse;
  private int mGroup;
  private int mIndexInTheGroup;
  private int mLocation;
  private float mMaxSpl;
  private float mMinSpl;
  private Coordinate3F mOrientation;
  private int mPortId;
  private Coordinate3F mPosition;
  private float mSensitivity;
  private int mType;
  
  MicrophoneInfo(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, int paramInt4, Coordinate3F paramCoordinate3F1, Coordinate3F paramCoordinate3F2, List<Pair<Float, Float>> paramList, List<Pair<Integer, Integer>> paramList1, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt5)
  {
    mDeviceId = paramString1;
    mType = paramInt1;
    mAddress = paramString2;
    mLocation = paramInt2;
    mGroup = paramInt3;
    mIndexInTheGroup = paramInt4;
    mPosition = paramCoordinate3F1;
    mOrientation = paramCoordinate3F2;
    mFrequencyResponse = paramList;
    mChannelMapping = paramList1;
    mSensitivity = paramFloat1;
    mMaxSpl = paramFloat2;
    mMinSpl = paramFloat3;
    mDirectionality = paramInt5;
  }
  
  public String getAddress()
  {
    return mAddress;
  }
  
  public List<Pair<Integer, Integer>> getChannelMapping()
  {
    return mChannelMapping;
  }
  
  public String getDescription()
  {
    return mDeviceId;
  }
  
  public int getDirectionality()
  {
    return mDirectionality;
  }
  
  public List<Pair<Float, Float>> getFrequencyResponse()
  {
    return mFrequencyResponse;
  }
  
  public int getGroup()
  {
    return mGroup;
  }
  
  public int getId()
  {
    return mPortId;
  }
  
  public int getIndexInTheGroup()
  {
    return mIndexInTheGroup;
  }
  
  public int getInternalDeviceType()
  {
    return mType;
  }
  
  public int getLocation()
  {
    return mLocation;
  }
  
  public float getMaxSpl()
  {
    return mMaxSpl;
  }
  
  public float getMinSpl()
  {
    return mMinSpl;
  }
  
  public Coordinate3F getOrientation()
  {
    return mOrientation;
  }
  
  public Coordinate3F getPosition()
  {
    return mPosition;
  }
  
  public float getSensitivity()
  {
    return mSensitivity;
  }
  
  public int getType()
  {
    return AudioDeviceInfo.convertInternalDeviceToDeviceType(mType);
  }
  
  public void setChannelMapping(List<Pair<Integer, Integer>> paramList)
  {
    mChannelMapping = paramList;
  }
  
  public void setId(int paramInt)
  {
    mPortId = paramInt;
  }
  
  public static final class Coordinate3F
  {
    public final float x;
    public final float y;
    public final float z;
    
    Coordinate3F(float paramFloat1, float paramFloat2, float paramFloat3)
    {
      x = paramFloat1;
      y = paramFloat2;
      z = paramFloat3;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof Coordinate3F)) {
        return false;
      }
      paramObject = (Coordinate3F)paramObject;
      if ((x != x) || (y != y) || (z != z)) {
        bool = false;
      }
      return bool;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MicrophoneDirectionality {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MicrophoneLocation {}
}
