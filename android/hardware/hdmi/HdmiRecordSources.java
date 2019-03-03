package android.hardware.hdmi;

import android.annotation.SystemApi;
import android.util.Log;

@SystemApi
public final class HdmiRecordSources
{
  public static final int ANALOGUE_BROADCAST_TYPE_CABLE = 0;
  public static final int ANALOGUE_BROADCAST_TYPE_SATELLITE = 1;
  public static final int ANALOGUE_BROADCAST_TYPE_TERRESTRIAL = 2;
  public static final int BROADCAST_SYSTEM_NTSC_M = 3;
  public static final int BROADCAST_SYSTEM_PAL_BG = 0;
  public static final int BROADCAST_SYSTEM_PAL_DK = 8;
  public static final int BROADCAST_SYSTEM_PAL_I = 4;
  public static final int BROADCAST_SYSTEM_PAL_M = 2;
  public static final int BROADCAST_SYSTEM_PAL_OTHER_SYSTEM = 31;
  public static final int BROADCAST_SYSTEM_SECAM_BG = 6;
  public static final int BROADCAST_SYSTEM_SECAM_DK = 5;
  public static final int BROADCAST_SYSTEM_SECAM_L = 7;
  public static final int BROADCAST_SYSTEM_SECAM_LP = 1;
  private static final int CHANNEL_NUMBER_FORMAT_1_PART = 1;
  private static final int CHANNEL_NUMBER_FORMAT_2_PART = 2;
  public static final int DIGITAL_BROADCAST_TYPE_ARIB = 0;
  public static final int DIGITAL_BROADCAST_TYPE_ARIB_BS = 8;
  public static final int DIGITAL_BROADCAST_TYPE_ARIB_CS = 9;
  public static final int DIGITAL_BROADCAST_TYPE_ARIB_T = 10;
  public static final int DIGITAL_BROADCAST_TYPE_ATSC = 1;
  public static final int DIGITAL_BROADCAST_TYPE_ATSC_CABLE = 16;
  public static final int DIGITAL_BROADCAST_TYPE_ATSC_SATELLITE = 17;
  public static final int DIGITAL_BROADCAST_TYPE_ATSC_TERRESTRIAL = 18;
  public static final int DIGITAL_BROADCAST_TYPE_DVB = 2;
  public static final int DIGITAL_BROADCAST_TYPE_DVB_C = 24;
  public static final int DIGITAL_BROADCAST_TYPE_DVB_S = 25;
  public static final int DIGITAL_BROADCAST_TYPE_DVB_S2 = 26;
  public static final int DIGITAL_BROADCAST_TYPE_DVB_T = 27;
  private static final int RECORD_SOURCE_TYPE_ANALOGUE_SERVICE = 3;
  private static final int RECORD_SOURCE_TYPE_DIGITAL_SERVICE = 2;
  private static final int RECORD_SOURCE_TYPE_EXTERNAL_PHYSICAL_ADDRESS = 5;
  private static final int RECORD_SOURCE_TYPE_EXTERNAL_PLUG = 4;
  private static final int RECORD_SOURCE_TYPE_OWN_SOURCE = 1;
  private static final String TAG = "HdmiRecordSources";
  
  private HdmiRecordSources() {}
  
  @SystemApi
  public static boolean checkRecordSource(byte[] paramArrayOfByte)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0))
    {
      int i = paramArrayOfByte[0];
      int j = paramArrayOfByte.length - 1;
      switch (i)
      {
      default: 
        return false;
      case 5: 
        if (j == 2) {
          bool5 = true;
        }
        return bool5;
      case 4: 
        bool5 = bool1;
        if (j == 1) {
          bool5 = true;
        }
        return bool5;
      case 3: 
        bool5 = bool2;
        if (j == 4) {
          bool5 = true;
        }
        return bool5;
      case 2: 
        bool5 = bool3;
        if (j == 7) {
          bool5 = true;
        }
        return bool5;
      }
      bool5 = bool4;
      if (j == 0) {
        bool5 = true;
      }
      return bool5;
    }
    return false;
  }
  
  public static AnalogueServiceSource ofAnalogue(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 >= 0) && (paramInt1 <= 2))
    {
      if ((paramInt2 >= 0) && (paramInt2 <= 65535))
      {
        if ((paramInt3 >= 0) && (paramInt3 <= 31)) {
          return new AnalogueServiceSource(paramInt1, paramInt2, paramInt3, null);
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid Broadcast system:");
        localStringBuilder.append(paramInt3);
        Log.w("HdmiRecordSources", localStringBuilder.toString());
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid Broadcast system:");
        localStringBuilder.append(paramInt3);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid frequency value[0x0000-0xFFFF]:");
      localStringBuilder.append(paramInt2);
      Log.w("HdmiRecordSources", localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid frequency value[0x0000-0xFFFF]:");
      localStringBuilder.append(paramInt2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid Broadcast type:");
    localStringBuilder.append(paramInt1);
    Log.w("HdmiRecordSources", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid Broadcast type:");
    localStringBuilder.append(paramInt1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static DigitalServiceSource ofArib(int paramInt, AribData paramAribData)
  {
    if (paramAribData != null)
    {
      if (paramInt != 0) {
        switch (paramInt)
        {
        default: 
          paramAribData = new StringBuilder();
          paramAribData.append("Invalid ARIB type:");
          paramAribData.append(paramInt);
          Log.w("HdmiRecordSources", paramAribData.toString());
          throw new IllegalArgumentException("type should not be null.");
        }
      }
      return new DigitalServiceSource(0, paramInt, paramAribData, null);
    }
    throw new IllegalArgumentException("data should not be null.");
  }
  
  public static DigitalServiceSource ofAtsc(int paramInt, AtscData paramAtscData)
  {
    if (paramAtscData != null)
    {
      if (paramInt != 1) {
        switch (paramInt)
        {
        default: 
          paramAtscData = new StringBuilder();
          paramAtscData.append("Invalid ATSC type:");
          paramAtscData.append(paramInt);
          Log.w("HdmiRecordSources", paramAtscData.toString());
          paramAtscData = new StringBuilder();
          paramAtscData.append("Invalid ATSC type:");
          paramAtscData.append(paramInt);
          throw new IllegalArgumentException(paramAtscData.toString());
        }
      }
      return new DigitalServiceSource(0, paramInt, paramAtscData, null);
    }
    throw new IllegalArgumentException("data should not be null.");
  }
  
  public static DigitalServiceSource ofDigitalChannelId(int paramInt, DigitalChannelData paramDigitalChannelData)
  {
    if (paramDigitalChannelData != null)
    {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            switch (paramInt)
            {
            default: 
              paramDigitalChannelData = new StringBuilder();
              paramDigitalChannelData.append("Invalid broadcast type:");
              paramDigitalChannelData.append(paramInt);
              Log.w("HdmiRecordSources", paramDigitalChannelData.toString());
              paramDigitalChannelData = new StringBuilder();
              paramDigitalChannelData.append("Invalid broadcast system value:");
              paramDigitalChannelData.append(paramInt);
              throw new IllegalArgumentException(paramDigitalChannelData.toString());
            }
            break;
          }
          break;
        }
        break;
      }
      return new DigitalServiceSource(1, paramInt, paramDigitalChannelData, null);
    }
    throw new IllegalArgumentException("data should not be null.");
  }
  
  public static DigitalServiceSource ofDvb(int paramInt, DvbData paramDvbData)
  {
    if (paramDvbData != null)
    {
      if (paramInt != 2) {
        switch (paramInt)
        {
        default: 
          paramDvbData = new StringBuilder();
          paramDvbData.append("Invalid DVB type:");
          paramDvbData.append(paramInt);
          Log.w("HdmiRecordSources", paramDvbData.toString());
          paramDvbData = new StringBuilder();
          paramDvbData.append("Invalid DVB type:");
          paramDvbData.append(paramInt);
          throw new IllegalArgumentException(paramDvbData.toString());
        }
      }
      return new DigitalServiceSource(0, paramInt, paramDvbData, null);
    }
    throw new IllegalArgumentException("data should not be null.");
  }
  
  public static ExternalPhysicalAddress ofExternalPhysicalAddress(int paramInt)
  {
    if ((0xFFFF0000 & paramInt) == 0) {
      return new ExternalPhysicalAddress(paramInt, null);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid physical address:");
    localStringBuilder.append(paramInt);
    Log.w("HdmiRecordSources", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid physical address:");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static ExternalPlugData ofExternalPlug(int paramInt)
  {
    if ((paramInt >= 1) && (paramInt <= 255)) {
      return new ExternalPlugData(paramInt, null);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid plug number[1-255]");
    localStringBuilder.append(paramInt);
    Log.w("HdmiRecordSources", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid plug number[1-255]");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static OwnSource ofOwnSource()
  {
    return new OwnSource(null);
  }
  
  private static int shortToByteArray(short paramShort, byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)(byte)(paramShort >>> 8 & 0xFF));
    paramArrayOfByte[(paramInt + 1)] = ((byte)(byte)(paramShort & 0xFF));
    return 2;
  }
  
  private static int threeFieldsToSixBytes(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4)
  {
    shortToByteArray((short)paramInt1, paramArrayOfByte, paramInt4);
    shortToByteArray((short)paramInt2, paramArrayOfByte, paramInt4 + 2);
    shortToByteArray((short)paramInt3, paramArrayOfByte, paramInt4 + 4);
    return 6;
  }
  
  @SystemApi
  public static final class AnalogueServiceSource
    extends HdmiRecordSources.RecordSource
  {
    static final int EXTRA_DATA_SIZE = 4;
    private final int mBroadcastSystem;
    private final int mBroadcastType;
    private final int mFrequency;
    
    private AnalogueServiceSource(int paramInt1, int paramInt2, int paramInt3)
    {
      super(4);
      mBroadcastType = paramInt1;
      mFrequency = paramInt2;
      mBroadcastSystem = paramInt3;
    }
    
    int extraParamToByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      paramArrayOfByte[paramInt] = ((byte)(byte)mBroadcastType);
      HdmiRecordSources.shortToByteArray((short)mFrequency, paramArrayOfByte, paramInt + 1);
      paramArrayOfByte[(paramInt + 3)] = ((byte)(byte)mBroadcastSystem);
      return 4;
    }
  }
  
  public static final class AribData
    implements HdmiRecordSources.DigitalServiceIdentification
  {
    private final int mOriginalNetworkId;
    private final int mServiceId;
    private final int mTransportStreamId;
    
    public AribData(int paramInt1, int paramInt2, int paramInt3)
    {
      mTransportStreamId = paramInt1;
      mServiceId = paramInt2;
      mOriginalNetworkId = paramInt3;
    }
    
    public int toByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      return HdmiRecordSources.threeFieldsToSixBytes(mTransportStreamId, mServiceId, mOriginalNetworkId, paramArrayOfByte, paramInt);
    }
  }
  
  public static final class AtscData
    implements HdmiRecordSources.DigitalServiceIdentification
  {
    private final int mProgramNumber;
    private final int mTransportStreamId;
    
    public AtscData(int paramInt1, int paramInt2)
    {
      mTransportStreamId = paramInt1;
      mProgramNumber = paramInt2;
    }
    
    public int toByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      return HdmiRecordSources.threeFieldsToSixBytes(mTransportStreamId, mProgramNumber, 0, paramArrayOfByte, paramInt);
    }
  }
  
  private static final class ChannelIdentifier
  {
    private final int mChannelNumberFormat;
    private final int mMajorChannelNumber;
    private final int mMinorChannelNumber;
    
    private ChannelIdentifier(int paramInt1, int paramInt2, int paramInt3)
    {
      mChannelNumberFormat = paramInt1;
      mMajorChannelNumber = paramInt2;
      mMinorChannelNumber = paramInt3;
    }
    
    private int toByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      paramArrayOfByte[paramInt] = ((byte)(byte)(mChannelNumberFormat << 2 | mMajorChannelNumber >>> 8 & 0x3));
      paramArrayOfByte[(paramInt + 1)] = ((byte)(byte)(mMajorChannelNumber & 0xFF));
      HdmiRecordSources.shortToByteArray((short)mMinorChannelNumber, paramArrayOfByte, paramInt + 2);
      return 4;
    }
  }
  
  public static final class DigitalChannelData
    implements HdmiRecordSources.DigitalServiceIdentification
  {
    private final HdmiRecordSources.ChannelIdentifier mChannelIdentifier;
    
    private DigitalChannelData(HdmiRecordSources.ChannelIdentifier paramChannelIdentifier)
    {
      mChannelIdentifier = paramChannelIdentifier;
    }
    
    public static DigitalChannelData ofOneNumber(int paramInt)
    {
      return new DigitalChannelData(new HdmiRecordSources.ChannelIdentifier(1, 0, paramInt, null));
    }
    
    public static DigitalChannelData ofTwoNumbers(int paramInt1, int paramInt2)
    {
      return new DigitalChannelData(new HdmiRecordSources.ChannelIdentifier(2, paramInt1, paramInt2, null));
    }
    
    public int toByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      mChannelIdentifier.toByteArray(paramArrayOfByte, paramInt);
      paramArrayOfByte[(paramInt + 4)] = ((byte)0);
      paramArrayOfByte[(paramInt + 5)] = ((byte)0);
      return 6;
    }
  }
  
  private static abstract interface DigitalServiceIdentification
  {
    public abstract int toByteArray(byte[] paramArrayOfByte, int paramInt);
  }
  
  @SystemApi
  public static final class DigitalServiceSource
    extends HdmiRecordSources.RecordSource
  {
    private static final int DIGITAL_SERVICE_IDENTIFIED_BY_CHANNEL = 1;
    private static final int DIGITAL_SERVICE_IDENTIFIED_BY_DIGITAL_ID = 0;
    static final int EXTRA_DATA_SIZE = 7;
    private final int mBroadcastSystem;
    private final HdmiRecordSources.DigitalServiceIdentification mIdentification;
    private final int mIdentificationMethod;
    
    private DigitalServiceSource(int paramInt1, int paramInt2, HdmiRecordSources.DigitalServiceIdentification paramDigitalServiceIdentification)
    {
      super(7);
      mIdentificationMethod = paramInt1;
      mBroadcastSystem = paramInt2;
      mIdentification = paramDigitalServiceIdentification;
    }
    
    int extraParamToByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      paramArrayOfByte[paramInt] = ((byte)(byte)(mIdentificationMethod << 7 | mBroadcastSystem & 0x7F));
      mIdentification.toByteArray(paramArrayOfByte, paramInt + 1);
      return 7;
    }
  }
  
  public static final class DvbData
    implements HdmiRecordSources.DigitalServiceIdentification
  {
    private final int mOriginalNetworkId;
    private final int mServiceId;
    private final int mTransportStreamId;
    
    public DvbData(int paramInt1, int paramInt2, int paramInt3)
    {
      mTransportStreamId = paramInt1;
      mServiceId = paramInt2;
      mOriginalNetworkId = paramInt3;
    }
    
    public int toByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      return HdmiRecordSources.threeFieldsToSixBytes(mTransportStreamId, mServiceId, mOriginalNetworkId, paramArrayOfByte, paramInt);
    }
  }
  
  @SystemApi
  public static final class ExternalPhysicalAddress
    extends HdmiRecordSources.RecordSource
  {
    static final int EXTRA_DATA_SIZE = 2;
    private final int mPhysicalAddress;
    
    private ExternalPhysicalAddress(int paramInt)
    {
      super(2);
      mPhysicalAddress = paramInt;
    }
    
    int extraParamToByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      HdmiRecordSources.shortToByteArray((short)mPhysicalAddress, paramArrayOfByte, paramInt);
      return 2;
    }
  }
  
  @SystemApi
  public static final class ExternalPlugData
    extends HdmiRecordSources.RecordSource
  {
    static final int EXTRA_DATA_SIZE = 1;
    private final int mPlugNumber;
    
    private ExternalPlugData(int paramInt)
    {
      super(1);
      mPlugNumber = paramInt;
    }
    
    int extraParamToByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      paramArrayOfByte[paramInt] = ((byte)(byte)mPlugNumber);
      return 1;
    }
  }
  
  @SystemApi
  public static final class OwnSource
    extends HdmiRecordSources.RecordSource
  {
    private static final int EXTRA_DATA_SIZE = 0;
    
    private OwnSource()
    {
      super(0);
    }
    
    int extraParamToByteArray(byte[] paramArrayOfByte, int paramInt)
    {
      return 0;
    }
  }
  
  @SystemApi
  public static abstract class RecordSource
  {
    final int mExtraDataSize;
    final int mSourceType;
    
    RecordSource(int paramInt1, int paramInt2)
    {
      mSourceType = paramInt1;
      mExtraDataSize = paramInt2;
    }
    
    abstract int extraParamToByteArray(byte[] paramArrayOfByte, int paramInt);
    
    final int getDataSize(boolean paramBoolean)
    {
      int i;
      if (paramBoolean) {
        i = mExtraDataSize + 1;
      } else {
        i = mExtraDataSize;
      }
      return i;
    }
    
    final int toByteArray(boolean paramBoolean, byte[] paramArrayOfByte, int paramInt)
    {
      int i = paramInt;
      if (paramBoolean)
      {
        paramArrayOfByte[paramInt] = ((byte)(byte)mSourceType);
        i = paramInt + 1;
      }
      extraParamToByteArray(paramArrayOfByte, i);
      return getDataSize(paramBoolean);
    }
  }
}
