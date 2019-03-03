package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public class GpsMeasurement
  implements Parcelable
{
  private static final short ADR_ALL = 7;
  public static final short ADR_STATE_CYCLE_SLIP = 4;
  public static final short ADR_STATE_RESET = 2;
  public static final short ADR_STATE_UNKNOWN = 0;
  public static final short ADR_STATE_VALID = 1;
  public static final Parcelable.Creator<GpsMeasurement> CREATOR = new Parcelable.Creator()
  {
    public GpsMeasurement createFromParcel(Parcel paramAnonymousParcel)
    {
      GpsMeasurement localGpsMeasurement = new GpsMeasurement();
      GpsMeasurement.access$002(localGpsMeasurement, paramAnonymousParcel.readInt());
      GpsMeasurement.access$102(localGpsMeasurement, paramAnonymousParcel.readByte());
      GpsMeasurement.access$202(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$302(localGpsMeasurement, (short)paramAnonymousParcel.readInt());
      GpsMeasurement.access$402(localGpsMeasurement, paramAnonymousParcel.readLong());
      GpsMeasurement.access$502(localGpsMeasurement, paramAnonymousParcel.readLong());
      GpsMeasurement.access$602(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$702(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$802(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$902(localGpsMeasurement, (short)paramAnonymousParcel.readInt());
      GpsMeasurement.access$1002(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$1102(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$1202(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$1302(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$1402(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$1502(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$1602(localGpsMeasurement, paramAnonymousParcel.readFloat());
      GpsMeasurement.access$1702(localGpsMeasurement, paramAnonymousParcel.readLong());
      GpsMeasurement.access$1802(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$1902(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$2002(localGpsMeasurement, paramAnonymousParcel.readByte());
      GpsMeasurement.access$2102(localGpsMeasurement, paramAnonymousParcel.readInt());
      GpsMeasurement.access$2202(localGpsMeasurement, (short)paramAnonymousParcel.readInt());
      GpsMeasurement.access$2302(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$2402(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$2502(localGpsMeasurement, paramAnonymousParcel.readByte());
      GpsMeasurement.access$2602(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$2702(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$2802(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$2902(localGpsMeasurement, paramAnonymousParcel.readDouble());
      GpsMeasurement.access$3002(localGpsMeasurement, paramAnonymousParcel.readDouble());
      boolean bool;
      if (paramAnonymousParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      GpsMeasurement.access$3102(localGpsMeasurement, bool);
      return localGpsMeasurement;
    }
    
    public GpsMeasurement[] newArray(int paramAnonymousInt)
    {
      return new GpsMeasurement[paramAnonymousInt];
    }
  };
  private static final int GPS_MEASUREMENT_HAS_UNCORRECTED_PSEUDORANGE_RATE = 262144;
  private static final int HAS_AZIMUTH = 8;
  private static final int HAS_AZIMUTH_UNCERTAINTY = 16;
  private static final int HAS_BIT_NUMBER = 8192;
  private static final int HAS_CARRIER_CYCLES = 1024;
  private static final int HAS_CARRIER_FREQUENCY = 512;
  private static final int HAS_CARRIER_PHASE = 2048;
  private static final int HAS_CARRIER_PHASE_UNCERTAINTY = 4096;
  private static final int HAS_CODE_PHASE = 128;
  private static final int HAS_CODE_PHASE_UNCERTAINTY = 256;
  private static final int HAS_DOPPLER_SHIFT = 32768;
  private static final int HAS_DOPPLER_SHIFT_UNCERTAINTY = 65536;
  private static final int HAS_ELEVATION = 2;
  private static final int HAS_ELEVATION_UNCERTAINTY = 4;
  private static final int HAS_NO_FLAGS = 0;
  private static final int HAS_PSEUDORANGE = 32;
  private static final int HAS_PSEUDORANGE_UNCERTAINTY = 64;
  private static final int HAS_SNR = 1;
  private static final int HAS_TIME_FROM_LAST_BIT = 16384;
  private static final int HAS_USED_IN_FIX = 131072;
  public static final byte LOSS_OF_LOCK_CYCLE_SLIP = 2;
  public static final byte LOSS_OF_LOCK_OK = 1;
  public static final byte LOSS_OF_LOCK_UNKNOWN = 0;
  public static final byte MULTIPATH_INDICATOR_DETECTED = 1;
  public static final byte MULTIPATH_INDICATOR_NOT_USED = 2;
  public static final byte MULTIPATH_INDICATOR_UNKNOWN = 0;
  private static final short STATE_ALL = 31;
  public static final short STATE_BIT_SYNC = 2;
  public static final short STATE_CODE_LOCK = 1;
  public static final short STATE_MSEC_AMBIGUOUS = 16;
  public static final short STATE_SUBFRAME_SYNC = 4;
  public static final short STATE_TOW_DECODED = 8;
  public static final short STATE_UNKNOWN = 0;
  private double mAccumulatedDeltaRangeInMeters;
  private short mAccumulatedDeltaRangeState;
  private double mAccumulatedDeltaRangeUncertaintyInMeters;
  private double mAzimuthInDeg;
  private double mAzimuthUncertaintyInDeg;
  private int mBitNumber;
  private long mCarrierCycles;
  private float mCarrierFrequencyInHz;
  private double mCarrierPhase;
  private double mCarrierPhaseUncertainty;
  private double mCn0InDbHz;
  private double mCodePhaseInChips;
  private double mCodePhaseUncertaintyInChips;
  private double mDopplerShiftInHz;
  private double mDopplerShiftUncertaintyInHz;
  private double mElevationInDeg;
  private double mElevationUncertaintyInDeg;
  private int mFlags;
  private byte mLossOfLock;
  private byte mMultipathIndicator;
  private byte mPrn;
  private double mPseudorangeInMeters;
  private double mPseudorangeRateInMetersPerSec;
  private double mPseudorangeRateUncertaintyInMetersPerSec;
  private double mPseudorangeUncertaintyInMeters;
  private long mReceivedGpsTowInNs;
  private long mReceivedGpsTowUncertaintyInNs;
  private double mSnrInDb;
  private short mState;
  private short mTimeFromLastBitInMs;
  private double mTimeOffsetInNs;
  private boolean mUsedInFix;
  
  GpsMeasurement()
  {
    initialize();
  }
  
  private String getAccumulatedDeltaRangeStateString()
  {
    if (mAccumulatedDeltaRangeState == 0) {
      return "Unknown";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    if ((mAccumulatedDeltaRangeState & 0x1) == 1) {
      localStringBuilder.append("Valid|");
    }
    if ((mAccumulatedDeltaRangeState & 0x2) == 2) {
      localStringBuilder.append("Reset|");
    }
    if ((mAccumulatedDeltaRangeState & 0x4) == 4) {
      localStringBuilder.append("CycleSlip|");
    }
    int i = mAccumulatedDeltaRangeState & 0xFFFFFFF8;
    if (i > 0)
    {
      localStringBuilder.append("Other(");
      localStringBuilder.append(Integer.toBinaryString(i));
      localStringBuilder.append(")|");
    }
    localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }
  
  private String getLossOfLockString()
  {
    switch (mLossOfLock)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<Invalid:");
      localStringBuilder.append(mLossOfLock);
      localStringBuilder.append(">");
      return localStringBuilder.toString();
    case 2: 
      return "CycleSlip";
    case 1: 
      return "Ok";
    }
    return "Unknown";
  }
  
  private String getMultipathIndicatorString()
  {
    switch (mMultipathIndicator)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<Invalid:");
      localStringBuilder.append(mMultipathIndicator);
      localStringBuilder.append(">");
      return localStringBuilder.toString();
    case 2: 
      return "NotUsed";
    case 1: 
      return "Detected";
    }
    return "Unknown";
  }
  
  private String getStateString()
  {
    if (mState == 0) {
      return "Unknown";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    if ((mState & 0x1) == 1) {
      localStringBuilder.append("CodeLock|");
    }
    if ((mState & 0x2) == 2) {
      localStringBuilder.append("BitSync|");
    }
    if ((mState & 0x4) == 4) {
      localStringBuilder.append("SubframeSync|");
    }
    if ((mState & 0x8) == 8) {
      localStringBuilder.append("TowDecoded|");
    }
    if ((mState & 0x10) == 16) {
      localStringBuilder.append("MsecAmbiguous");
    }
    int i = mState & 0xFFFFFFE0;
    if (i > 0)
    {
      localStringBuilder.append("Other(");
      localStringBuilder.append(Integer.toBinaryString(i));
      localStringBuilder.append(")|");
    }
    localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }
  
  private void initialize()
  {
    mFlags = 0;
    setPrn((byte)Byte.MIN_VALUE);
    setTimeOffsetInNs(-9.223372036854776E18D);
    setState((short)0);
    setReceivedGpsTowInNs(Long.MIN_VALUE);
    setReceivedGpsTowUncertaintyInNs(Long.MAX_VALUE);
    setCn0InDbHz(Double.MIN_VALUE);
    setPseudorangeRateInMetersPerSec(Double.MIN_VALUE);
    setPseudorangeRateUncertaintyInMetersPerSec(Double.MIN_VALUE);
    setAccumulatedDeltaRangeState((short)0);
    setAccumulatedDeltaRangeInMeters(Double.MIN_VALUE);
    setAccumulatedDeltaRangeUncertaintyInMeters(Double.MIN_VALUE);
    resetPseudorangeInMeters();
    resetPseudorangeUncertaintyInMeters();
    resetCodePhaseInChips();
    resetCodePhaseUncertaintyInChips();
    resetCarrierFrequencyInHz();
    resetCarrierCycles();
    resetCarrierPhase();
    resetCarrierPhaseUncertainty();
    setLossOfLock((byte)0);
    resetBitNumber();
    resetTimeFromLastBitInMs();
    resetDopplerShiftInHz();
    resetDopplerShiftUncertaintyInHz();
    setMultipathIndicator((byte)0);
    resetSnrInDb();
    resetElevationInDeg();
    resetElevationUncertaintyInDeg();
    resetAzimuthInDeg();
    resetAzimuthUncertaintyInDeg();
    setUsedInFix(false);
  }
  
  private boolean isFlagSet(int paramInt)
  {
    boolean bool;
    if ((mFlags & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void resetFlag(int paramInt)
  {
    mFlags &= paramInt;
  }
  
  private void setFlag(int paramInt)
  {
    mFlags |= paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public double getAccumulatedDeltaRangeInMeters()
  {
    return mAccumulatedDeltaRangeInMeters;
  }
  
  public short getAccumulatedDeltaRangeState()
  {
    return mAccumulatedDeltaRangeState;
  }
  
  public double getAccumulatedDeltaRangeUncertaintyInMeters()
  {
    return mAccumulatedDeltaRangeUncertaintyInMeters;
  }
  
  public double getAzimuthInDeg()
  {
    return mAzimuthInDeg;
  }
  
  public double getAzimuthUncertaintyInDeg()
  {
    return mAzimuthUncertaintyInDeg;
  }
  
  public int getBitNumber()
  {
    return mBitNumber;
  }
  
  public long getCarrierCycles()
  {
    return mCarrierCycles;
  }
  
  public float getCarrierFrequencyInHz()
  {
    return mCarrierFrequencyInHz;
  }
  
  public double getCarrierPhase()
  {
    return mCarrierPhase;
  }
  
  public double getCarrierPhaseUncertainty()
  {
    return mCarrierPhaseUncertainty;
  }
  
  public double getCn0InDbHz()
  {
    return mCn0InDbHz;
  }
  
  public double getCodePhaseInChips()
  {
    return mCodePhaseInChips;
  }
  
  public double getCodePhaseUncertaintyInChips()
  {
    return mCodePhaseUncertaintyInChips;
  }
  
  public double getDopplerShiftInHz()
  {
    return mDopplerShiftInHz;
  }
  
  public double getDopplerShiftUncertaintyInHz()
  {
    return mDopplerShiftUncertaintyInHz;
  }
  
  public double getElevationInDeg()
  {
    return mElevationInDeg;
  }
  
  public double getElevationUncertaintyInDeg()
  {
    return mElevationUncertaintyInDeg;
  }
  
  public byte getLossOfLock()
  {
    return mLossOfLock;
  }
  
  public byte getMultipathIndicator()
  {
    return mMultipathIndicator;
  }
  
  public byte getPrn()
  {
    return mPrn;
  }
  
  public double getPseudorangeInMeters()
  {
    return mPseudorangeInMeters;
  }
  
  public double getPseudorangeRateInMetersPerSec()
  {
    return mPseudorangeRateInMetersPerSec;
  }
  
  public double getPseudorangeRateUncertaintyInMetersPerSec()
  {
    return mPseudorangeRateUncertaintyInMetersPerSec;
  }
  
  public double getPseudorangeUncertaintyInMeters()
  {
    return mPseudorangeUncertaintyInMeters;
  }
  
  public long getReceivedGpsTowInNs()
  {
    return mReceivedGpsTowInNs;
  }
  
  public long getReceivedGpsTowUncertaintyInNs()
  {
    return mReceivedGpsTowUncertaintyInNs;
  }
  
  public double getSnrInDb()
  {
    return mSnrInDb;
  }
  
  public short getState()
  {
    return mState;
  }
  
  public short getTimeFromLastBitInMs()
  {
    return mTimeFromLastBitInMs;
  }
  
  public double getTimeOffsetInNs()
  {
    return mTimeOffsetInNs;
  }
  
  public boolean hasAzimuthInDeg()
  {
    return isFlagSet(8);
  }
  
  public boolean hasAzimuthUncertaintyInDeg()
  {
    return isFlagSet(16);
  }
  
  public boolean hasBitNumber()
  {
    return isFlagSet(8192);
  }
  
  public boolean hasCarrierCycles()
  {
    return isFlagSet(1024);
  }
  
  public boolean hasCarrierFrequencyInHz()
  {
    return isFlagSet(512);
  }
  
  public boolean hasCarrierPhase()
  {
    return isFlagSet(2048);
  }
  
  public boolean hasCarrierPhaseUncertainty()
  {
    return isFlagSet(4096);
  }
  
  public boolean hasCodePhaseInChips()
  {
    return isFlagSet(128);
  }
  
  public boolean hasCodePhaseUncertaintyInChips()
  {
    return isFlagSet(256);
  }
  
  public boolean hasDopplerShiftInHz()
  {
    return isFlagSet(32768);
  }
  
  public boolean hasDopplerShiftUncertaintyInHz()
  {
    return isFlagSet(65536);
  }
  
  public boolean hasElevationInDeg()
  {
    return isFlagSet(2);
  }
  
  public boolean hasElevationUncertaintyInDeg()
  {
    return isFlagSet(4);
  }
  
  public boolean hasPseudorangeInMeters()
  {
    return isFlagSet(32);
  }
  
  public boolean hasPseudorangeUncertaintyInMeters()
  {
    return isFlagSet(64);
  }
  
  public boolean hasSnrInDb()
  {
    return isFlagSet(1);
  }
  
  public boolean hasTimeFromLastBitInMs()
  {
    return isFlagSet(16384);
  }
  
  public boolean isPseudorangeRateCorrected()
  {
    return isFlagSet(262144) ^ true;
  }
  
  public boolean isUsedInFix()
  {
    return mUsedInFix;
  }
  
  public void reset()
  {
    initialize();
  }
  
  public void resetAzimuthInDeg()
  {
    resetFlag(8);
    mAzimuthInDeg = NaN.0D;
  }
  
  public void resetAzimuthUncertaintyInDeg()
  {
    resetFlag(16);
    mAzimuthUncertaintyInDeg = NaN.0D;
  }
  
  public void resetBitNumber()
  {
    resetFlag(8192);
    mBitNumber = Integer.MIN_VALUE;
  }
  
  public void resetCarrierCycles()
  {
    resetFlag(1024);
    mCarrierCycles = Long.MIN_VALUE;
  }
  
  public void resetCarrierFrequencyInHz()
  {
    resetFlag(512);
    mCarrierFrequencyInHz = NaN.0F;
  }
  
  public void resetCarrierPhase()
  {
    resetFlag(2048);
    mCarrierPhase = NaN.0D;
  }
  
  public void resetCarrierPhaseUncertainty()
  {
    resetFlag(4096);
    mCarrierPhaseUncertainty = NaN.0D;
  }
  
  public void resetCodePhaseInChips()
  {
    resetFlag(128);
    mCodePhaseInChips = NaN.0D;
  }
  
  public void resetCodePhaseUncertaintyInChips()
  {
    resetFlag(256);
    mCodePhaseUncertaintyInChips = NaN.0D;
  }
  
  public void resetDopplerShiftInHz()
  {
    resetFlag(32768);
    mDopplerShiftInHz = NaN.0D;
  }
  
  public void resetDopplerShiftUncertaintyInHz()
  {
    resetFlag(65536);
    mDopplerShiftUncertaintyInHz = NaN.0D;
  }
  
  public void resetElevationInDeg()
  {
    resetFlag(2);
    mElevationInDeg = NaN.0D;
  }
  
  public void resetElevationUncertaintyInDeg()
  {
    resetFlag(4);
    mElevationUncertaintyInDeg = NaN.0D;
  }
  
  public void resetPseudorangeInMeters()
  {
    resetFlag(32);
    mPseudorangeInMeters = NaN.0D;
  }
  
  public void resetPseudorangeUncertaintyInMeters()
  {
    resetFlag(64);
    mPseudorangeUncertaintyInMeters = NaN.0D;
  }
  
  public void resetSnrInDb()
  {
    resetFlag(1);
    mSnrInDb = NaN.0D;
  }
  
  public void resetTimeFromLastBitInMs()
  {
    resetFlag(16384);
    mTimeFromLastBitInMs = ((short)'耀');
  }
  
  public void set(GpsMeasurement paramGpsMeasurement)
  {
    mFlags = mFlags;
    mPrn = ((byte)mPrn);
    mTimeOffsetInNs = mTimeOffsetInNs;
    mState = ((short)mState);
    mReceivedGpsTowInNs = mReceivedGpsTowInNs;
    mReceivedGpsTowUncertaintyInNs = mReceivedGpsTowUncertaintyInNs;
    mCn0InDbHz = mCn0InDbHz;
    mPseudorangeRateInMetersPerSec = mPseudorangeRateInMetersPerSec;
    mPseudorangeRateUncertaintyInMetersPerSec = mPseudorangeRateUncertaintyInMetersPerSec;
    mAccumulatedDeltaRangeState = ((short)mAccumulatedDeltaRangeState);
    mAccumulatedDeltaRangeInMeters = mAccumulatedDeltaRangeInMeters;
    mAccumulatedDeltaRangeUncertaintyInMeters = mAccumulatedDeltaRangeUncertaintyInMeters;
    mPseudorangeInMeters = mPseudorangeInMeters;
    mPseudorangeUncertaintyInMeters = mPseudorangeUncertaintyInMeters;
    mCodePhaseInChips = mCodePhaseInChips;
    mCodePhaseUncertaintyInChips = mCodePhaseUncertaintyInChips;
    mCarrierFrequencyInHz = mCarrierFrequencyInHz;
    mCarrierCycles = mCarrierCycles;
    mCarrierPhase = mCarrierPhase;
    mCarrierPhaseUncertainty = mCarrierPhaseUncertainty;
    mLossOfLock = ((byte)mLossOfLock);
    mBitNumber = mBitNumber;
    mTimeFromLastBitInMs = ((short)mTimeFromLastBitInMs);
    mDopplerShiftInHz = mDopplerShiftInHz;
    mDopplerShiftUncertaintyInHz = mDopplerShiftUncertaintyInHz;
    mMultipathIndicator = ((byte)mMultipathIndicator);
    mSnrInDb = mSnrInDb;
    mElevationInDeg = mElevationInDeg;
    mElevationUncertaintyInDeg = mElevationUncertaintyInDeg;
    mAzimuthInDeg = mAzimuthInDeg;
    mAzimuthUncertaintyInDeg = mAzimuthUncertaintyInDeg;
    mUsedInFix = mUsedInFix;
  }
  
  public void setAccumulatedDeltaRangeInMeters(double paramDouble)
  {
    mAccumulatedDeltaRangeInMeters = paramDouble;
  }
  
  public void setAccumulatedDeltaRangeState(short paramShort)
  {
    mAccumulatedDeltaRangeState = ((short)paramShort);
  }
  
  public void setAccumulatedDeltaRangeUncertaintyInMeters(double paramDouble)
  {
    mAccumulatedDeltaRangeUncertaintyInMeters = paramDouble;
  }
  
  public void setAzimuthInDeg(double paramDouble)
  {
    setFlag(8);
    mAzimuthInDeg = paramDouble;
  }
  
  public void setAzimuthUncertaintyInDeg(double paramDouble)
  {
    setFlag(16);
    mAzimuthUncertaintyInDeg = paramDouble;
  }
  
  public void setBitNumber(int paramInt)
  {
    setFlag(8192);
    mBitNumber = paramInt;
  }
  
  public void setCarrierCycles(long paramLong)
  {
    setFlag(1024);
    mCarrierCycles = paramLong;
  }
  
  public void setCarrierFrequencyInHz(float paramFloat)
  {
    setFlag(512);
    mCarrierFrequencyInHz = paramFloat;
  }
  
  public void setCarrierPhase(double paramDouble)
  {
    setFlag(2048);
    mCarrierPhase = paramDouble;
  }
  
  public void setCarrierPhaseUncertainty(double paramDouble)
  {
    setFlag(4096);
    mCarrierPhaseUncertainty = paramDouble;
  }
  
  public void setCn0InDbHz(double paramDouble)
  {
    mCn0InDbHz = paramDouble;
  }
  
  public void setCodePhaseInChips(double paramDouble)
  {
    setFlag(128);
    mCodePhaseInChips = paramDouble;
  }
  
  public void setCodePhaseUncertaintyInChips(double paramDouble)
  {
    setFlag(256);
    mCodePhaseUncertaintyInChips = paramDouble;
  }
  
  public void setDopplerShiftInHz(double paramDouble)
  {
    setFlag(32768);
    mDopplerShiftInHz = paramDouble;
  }
  
  public void setDopplerShiftUncertaintyInHz(double paramDouble)
  {
    setFlag(65536);
    mDopplerShiftUncertaintyInHz = paramDouble;
  }
  
  public void setElevationInDeg(double paramDouble)
  {
    setFlag(2);
    mElevationInDeg = paramDouble;
  }
  
  public void setElevationUncertaintyInDeg(double paramDouble)
  {
    setFlag(4);
    mElevationUncertaintyInDeg = paramDouble;
  }
  
  public void setLossOfLock(byte paramByte)
  {
    mLossOfLock = ((byte)paramByte);
  }
  
  public void setMultipathIndicator(byte paramByte)
  {
    mMultipathIndicator = ((byte)paramByte);
  }
  
  public void setPrn(byte paramByte)
  {
    mPrn = ((byte)paramByte);
  }
  
  public void setPseudorangeInMeters(double paramDouble)
  {
    setFlag(32);
    mPseudorangeInMeters = paramDouble;
  }
  
  public void setPseudorangeRateInMetersPerSec(double paramDouble)
  {
    mPseudorangeRateInMetersPerSec = paramDouble;
  }
  
  public void setPseudorangeRateUncertaintyInMetersPerSec(double paramDouble)
  {
    mPseudorangeRateUncertaintyInMetersPerSec = paramDouble;
  }
  
  public void setPseudorangeUncertaintyInMeters(double paramDouble)
  {
    setFlag(64);
    mPseudorangeUncertaintyInMeters = paramDouble;
  }
  
  public void setReceivedGpsTowInNs(long paramLong)
  {
    mReceivedGpsTowInNs = paramLong;
  }
  
  public void setReceivedGpsTowUncertaintyInNs(long paramLong)
  {
    mReceivedGpsTowUncertaintyInNs = paramLong;
  }
  
  public void setSnrInDb(double paramDouble)
  {
    setFlag(1);
    mSnrInDb = paramDouble;
  }
  
  public void setState(short paramShort)
  {
    mState = ((short)paramShort);
  }
  
  public void setTimeFromLastBitInMs(short paramShort)
  {
    setFlag(16384);
    mTimeFromLastBitInMs = ((short)paramShort);
  }
  
  public void setTimeOffsetInNs(double paramDouble)
  {
    mTimeOffsetInNs = paramDouble;
  }
  
  public void setUsedInFix(boolean paramBoolean)
  {
    mUsedInFix = paramBoolean;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("GpsMeasurement:\n");
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "Prn", Byte.valueOf(mPrn) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "TimeOffsetInNs", Double.valueOf(mTimeOffsetInNs) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "State", getStateString() }));
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "ReceivedGpsTowInNs", Long.valueOf(mReceivedGpsTowInNs), "ReceivedGpsTowUncertaintyInNs", Long.valueOf(mReceivedGpsTowUncertaintyInNs) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "Cn0InDbHz", Double.valueOf(mCn0InDbHz) }));
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "PseudorangeRateInMetersPerSec", Double.valueOf(mPseudorangeRateInMetersPerSec), "PseudorangeRateUncertaintyInMetersPerSec", Double.valueOf(mPseudorangeRateUncertaintyInMetersPerSec) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "PseudorangeRateIsCorrected", Boolean.valueOf(isPseudorangeRateCorrected()) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "AccumulatedDeltaRangeState", getAccumulatedDeltaRangeStateString() }));
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "AccumulatedDeltaRangeInMeters", Double.valueOf(mAccumulatedDeltaRangeInMeters), "AccumulatedDeltaRangeUncertaintyInMeters", Double.valueOf(mAccumulatedDeltaRangeUncertaintyInMeters) }));
    boolean bool = hasPseudorangeInMeters();
    Object localObject1 = null;
    Object localObject2;
    if (bool) {
      localObject2 = Double.valueOf(mPseudorangeInMeters);
    } else {
      localObject2 = null;
    }
    if (hasPseudorangeUncertaintyInMeters()) {
      localObject3 = Double.valueOf(mPseudorangeUncertaintyInMeters);
    } else {
      localObject3 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "PseudorangeInMeters", localObject2, "PseudorangeUncertaintyInMeters", localObject3 }));
    if (hasCodePhaseInChips()) {
      localObject2 = Double.valueOf(mCodePhaseInChips);
    } else {
      localObject2 = null;
    }
    if (hasCodePhaseUncertaintyInChips()) {
      localObject3 = Double.valueOf(mCodePhaseUncertaintyInChips);
    } else {
      localObject3 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "CodePhaseInChips", localObject2, "CodePhaseUncertaintyInChips", localObject3 }));
    if (hasCarrierFrequencyInHz()) {
      localObject2 = Float.valueOf(mCarrierFrequencyInHz);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "CarrierFrequencyInHz", localObject2 }));
    if (hasCarrierCycles()) {
      localObject2 = Long.valueOf(mCarrierCycles);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "CarrierCycles", localObject2 }));
    if (hasCarrierPhase()) {
      localObject2 = Double.valueOf(mCarrierPhase);
    } else {
      localObject2 = null;
    }
    if (hasCarrierPhaseUncertainty()) {
      localObject3 = Double.valueOf(mCarrierPhaseUncertainty);
    } else {
      localObject3 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "CarrierPhase", localObject2, "CarrierPhaseUncertainty", localObject3 }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "LossOfLock", getLossOfLockString() }));
    if (hasBitNumber()) {
      localObject2 = Integer.valueOf(mBitNumber);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "BitNumber", localObject2 }));
    if (hasTimeFromLastBitInMs()) {
      localObject2 = Short.valueOf(mTimeFromLastBitInMs);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "TimeFromLastBitInMs", localObject2 }));
    if (hasDopplerShiftInHz()) {
      localObject2 = Double.valueOf(mDopplerShiftInHz);
    } else {
      localObject2 = null;
    }
    if (hasDopplerShiftUncertaintyInHz()) {
      localObject3 = Double.valueOf(mDopplerShiftUncertaintyInHz);
    } else {
      localObject3 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "DopplerShiftInHz", localObject2, "DopplerShiftUncertaintyInHz", localObject3 }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "MultipathIndicator", getMultipathIndicatorString() }));
    if (hasSnrInDb()) {
      localObject2 = Double.valueOf(mSnrInDb);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "SnrInDb", localObject2 }));
    if (hasElevationInDeg()) {
      localObject2 = Double.valueOf(mElevationInDeg);
    } else {
      localObject2 = null;
    }
    if (hasElevationUncertaintyInDeg()) {
      localObject3 = Double.valueOf(mElevationUncertaintyInDeg);
    } else {
      localObject3 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "ElevationInDeg", localObject2, "ElevationUncertaintyInDeg", localObject3 }));
    if (hasAzimuthInDeg()) {
      localObject2 = Double.valueOf(mAzimuthInDeg);
    } else {
      localObject2 = null;
    }
    Object localObject3 = localObject1;
    if (hasAzimuthUncertaintyInDeg()) {
      localObject3 = Double.valueOf(mAzimuthUncertaintyInDeg);
    }
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "AzimuthInDeg", localObject2, "AzimuthUncertaintyInDeg", localObject3 }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "UsedInFix", Boolean.valueOf(mUsedInFix) }));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mFlags);
    paramParcel.writeByte(mPrn);
    paramParcel.writeDouble(mTimeOffsetInNs);
    paramParcel.writeInt(mState);
    paramParcel.writeLong(mReceivedGpsTowInNs);
    paramParcel.writeLong(mReceivedGpsTowUncertaintyInNs);
    paramParcel.writeDouble(mCn0InDbHz);
    paramParcel.writeDouble(mPseudorangeRateInMetersPerSec);
    paramParcel.writeDouble(mPseudorangeRateUncertaintyInMetersPerSec);
    paramParcel.writeInt(mAccumulatedDeltaRangeState);
    paramParcel.writeDouble(mAccumulatedDeltaRangeInMeters);
    paramParcel.writeDouble(mAccumulatedDeltaRangeUncertaintyInMeters);
    paramParcel.writeDouble(mPseudorangeInMeters);
    paramParcel.writeDouble(mPseudorangeUncertaintyInMeters);
    paramParcel.writeDouble(mCodePhaseInChips);
    paramParcel.writeDouble(mCodePhaseUncertaintyInChips);
    paramParcel.writeFloat(mCarrierFrequencyInHz);
    paramParcel.writeLong(mCarrierCycles);
    paramParcel.writeDouble(mCarrierPhase);
    paramParcel.writeDouble(mCarrierPhaseUncertainty);
    paramParcel.writeByte(mLossOfLock);
    paramParcel.writeInt(mBitNumber);
    paramParcel.writeInt(mTimeFromLastBitInMs);
    paramParcel.writeDouble(mDopplerShiftInHz);
    paramParcel.writeDouble(mDopplerShiftUncertaintyInHz);
    paramParcel.writeByte(mMultipathIndicator);
    paramParcel.writeDouble(mSnrInDb);
    paramParcel.writeDouble(mElevationInDeg);
    paramParcel.writeDouble(mElevationUncertaintyInDeg);
    paramParcel.writeDouble(mAzimuthInDeg);
    paramParcel.writeDouble(mAzimuthUncertaintyInDeg);
    paramParcel.writeInt(mUsedInFix);
  }
}
