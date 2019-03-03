package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class GnssMeasurement
  implements Parcelable
{
  public static final int ADR_STATE_ALL = 31;
  public static final int ADR_STATE_CYCLE_SLIP = 4;
  public static final int ADR_STATE_HALF_CYCLE_REPORTED = 16;
  public static final int ADR_STATE_HALF_CYCLE_RESOLVED = 8;
  public static final int ADR_STATE_RESET = 2;
  public static final int ADR_STATE_UNKNOWN = 0;
  public static final int ADR_STATE_VALID = 1;
  public static final Parcelable.Creator<GnssMeasurement> CREATOR = new Parcelable.Creator()
  {
    public GnssMeasurement createFromParcel(Parcel paramAnonymousParcel)
    {
      GnssMeasurement localGnssMeasurement = new GnssMeasurement();
      GnssMeasurement.access$002(localGnssMeasurement, paramAnonymousParcel.readInt());
      GnssMeasurement.access$102(localGnssMeasurement, paramAnonymousParcel.readInt());
      GnssMeasurement.access$202(localGnssMeasurement, paramAnonymousParcel.readInt());
      GnssMeasurement.access$302(localGnssMeasurement, paramAnonymousParcel.readDouble());
      GnssMeasurement.access$402(localGnssMeasurement, paramAnonymousParcel.readInt());
      GnssMeasurement.access$502(localGnssMeasurement, paramAnonymousParcel.readLong());
      GnssMeasurement.access$602(localGnssMeasurement, paramAnonymousParcel.readLong());
      GnssMeasurement.access$702(localGnssMeasurement, paramAnonymousParcel.readDouble());
      GnssMeasurement.access$802(localGnssMeasurement, paramAnonymousParcel.readDouble());
      GnssMeasurement.access$902(localGnssMeasurement, paramAnonymousParcel.readDouble());
      GnssMeasurement.access$1002(localGnssMeasurement, paramAnonymousParcel.readInt());
      GnssMeasurement.access$1102(localGnssMeasurement, paramAnonymousParcel.readDouble());
      GnssMeasurement.access$1202(localGnssMeasurement, paramAnonymousParcel.readDouble());
      GnssMeasurement.access$1302(localGnssMeasurement, paramAnonymousParcel.readFloat());
      GnssMeasurement.access$1402(localGnssMeasurement, paramAnonymousParcel.readLong());
      GnssMeasurement.access$1502(localGnssMeasurement, paramAnonymousParcel.readDouble());
      GnssMeasurement.access$1602(localGnssMeasurement, paramAnonymousParcel.readDouble());
      GnssMeasurement.access$1702(localGnssMeasurement, paramAnonymousParcel.readInt());
      GnssMeasurement.access$1802(localGnssMeasurement, paramAnonymousParcel.readDouble());
      GnssMeasurement.access$1902(localGnssMeasurement, paramAnonymousParcel.readDouble());
      return localGnssMeasurement;
    }
    
    public GnssMeasurement[] newArray(int paramAnonymousInt)
    {
      return new GnssMeasurement[paramAnonymousInt];
    }
  };
  private static final int HAS_AUTOMATIC_GAIN_CONTROL = 8192;
  private static final int HAS_CARRIER_CYCLES = 1024;
  private static final int HAS_CARRIER_FREQUENCY = 512;
  private static final int HAS_CARRIER_PHASE = 2048;
  private static final int HAS_CARRIER_PHASE_UNCERTAINTY = 4096;
  private static final int HAS_NO_FLAGS = 0;
  private static final int HAS_SNR = 1;
  public static final int MULTIPATH_INDICATOR_DETECTED = 1;
  public static final int MULTIPATH_INDICATOR_NOT_DETECTED = 2;
  public static final int MULTIPATH_INDICATOR_UNKNOWN = 0;
  private static final int STATE_ALL = 16383;
  public static final int STATE_BDS_D2_BIT_SYNC = 256;
  public static final int STATE_BDS_D2_SUBFRAME_SYNC = 512;
  public static final int STATE_BIT_SYNC = 2;
  public static final int STATE_CODE_LOCK = 1;
  public static final int STATE_GAL_E1BC_CODE_LOCK = 1024;
  public static final int STATE_GAL_E1B_PAGE_SYNC = 4096;
  public static final int STATE_GAL_E1C_2ND_CODE_LOCK = 2048;
  public static final int STATE_GLO_STRING_SYNC = 64;
  public static final int STATE_GLO_TOD_DECODED = 128;
  public static final int STATE_GLO_TOD_KNOWN = 32768;
  public static final int STATE_MSEC_AMBIGUOUS = 16;
  public static final int STATE_SBAS_SYNC = 8192;
  public static final int STATE_SUBFRAME_SYNC = 4;
  public static final int STATE_SYMBOL_SYNC = 32;
  public static final int STATE_TOW_DECODED = 8;
  public static final int STATE_TOW_KNOWN = 16384;
  public static final int STATE_UNKNOWN = 0;
  private double mAccumulatedDeltaRangeMeters;
  private int mAccumulatedDeltaRangeState;
  private double mAccumulatedDeltaRangeUncertaintyMeters;
  private double mAutomaticGainControlLevelInDb;
  private long mCarrierCycles;
  private float mCarrierFrequencyHz;
  private double mCarrierPhase;
  private double mCarrierPhaseUncertainty;
  private double mCn0DbHz;
  private int mConstellationType;
  private int mFlags;
  private int mMultipathIndicator;
  private double mPseudorangeRateMetersPerSecond;
  private double mPseudorangeRateUncertaintyMetersPerSecond;
  private long mReceivedSvTimeNanos;
  private long mReceivedSvTimeUncertaintyNanos;
  private double mSnrInDb;
  private int mState;
  private int mSvid;
  private double mTimeOffsetNanos;
  
  public GnssMeasurement()
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
    if ((mAccumulatedDeltaRangeState & 0x8) == 8) {
      localStringBuilder.append("HalfCycleResolved|");
    }
    if ((mAccumulatedDeltaRangeState & 0x10) == 16) {
      localStringBuilder.append("HalfCycleReported|");
    }
    int i = mAccumulatedDeltaRangeState & 0xFFFFFFE0;
    if (i > 0)
    {
      localStringBuilder.append("Other(");
      localStringBuilder.append(Integer.toBinaryString(i));
      localStringBuilder.append(")|");
    }
    localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }
  
  private String getMultipathIndicatorString()
  {
    switch (mMultipathIndicator)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<Invalid: ");
      localStringBuilder.append(mMultipathIndicator);
      localStringBuilder.append(">");
      return localStringBuilder.toString();
    case 2: 
      return "NotDetected";
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
    if ((mState & 0x1) != 0) {
      localStringBuilder.append("CodeLock|");
    }
    if ((mState & 0x2) != 0) {
      localStringBuilder.append("BitSync|");
    }
    if ((mState & 0x4) != 0) {
      localStringBuilder.append("SubframeSync|");
    }
    if ((mState & 0x8) != 0) {
      localStringBuilder.append("TowDecoded|");
    }
    if ((mState & 0x4000) != 0) {
      localStringBuilder.append("TowKnown|");
    }
    if ((mState & 0x10) != 0) {
      localStringBuilder.append("MsecAmbiguous|");
    }
    if ((mState & 0x20) != 0) {
      localStringBuilder.append("SymbolSync|");
    }
    if ((mState & 0x40) != 0) {
      localStringBuilder.append("GloStringSync|");
    }
    if ((mState & 0x80) != 0) {
      localStringBuilder.append("GloTodDecoded|");
    }
    if ((mState & 0x8000) != 0) {
      localStringBuilder.append("GloTodKnown|");
    }
    if ((mState & 0x100) != 0) {
      localStringBuilder.append("BdsD2BitSync|");
    }
    if ((mState & 0x200) != 0) {
      localStringBuilder.append("BdsD2SubframeSync|");
    }
    if ((mState & 0x400) != 0) {
      localStringBuilder.append("GalE1bcCodeLock|");
    }
    if ((mState & 0x800) != 0) {
      localStringBuilder.append("E1c2ndCodeLock|");
    }
    if ((mState & 0x1000) != 0) {
      localStringBuilder.append("GalE1bPageSync|");
    }
    if ((mState & 0x2000) != 0) {
      localStringBuilder.append("SbasSync|");
    }
    int i = mState & 0xC000;
    if (i > 0)
    {
      localStringBuilder.append("Other(");
      localStringBuilder.append(Integer.toBinaryString(i));
      localStringBuilder.append(")|");
    }
    localStringBuilder.setLength(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }
  
  private void initialize()
  {
    mFlags = 0;
    setSvid(0);
    setTimeOffsetNanos(-9.223372036854776E18D);
    setState(0);
    setReceivedSvTimeNanos(Long.MIN_VALUE);
    setReceivedSvTimeUncertaintyNanos(Long.MAX_VALUE);
    setCn0DbHz(Double.MIN_VALUE);
    setPseudorangeRateMetersPerSecond(Double.MIN_VALUE);
    setPseudorangeRateUncertaintyMetersPerSecond(Double.MIN_VALUE);
    setAccumulatedDeltaRangeState(0);
    setAccumulatedDeltaRangeMeters(Double.MIN_VALUE);
    setAccumulatedDeltaRangeUncertaintyMeters(Double.MIN_VALUE);
    resetCarrierFrequencyHz();
    resetCarrierCycles();
    resetCarrierPhase();
    resetCarrierPhaseUncertainty();
    setMultipathIndicator(0);
    resetSnrInDb();
    resetAutomaticGainControlLevel();
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
  
  public double getAccumulatedDeltaRangeMeters()
  {
    return mAccumulatedDeltaRangeMeters;
  }
  
  public int getAccumulatedDeltaRangeState()
  {
    return mAccumulatedDeltaRangeState;
  }
  
  public double getAccumulatedDeltaRangeUncertaintyMeters()
  {
    return mAccumulatedDeltaRangeUncertaintyMeters;
  }
  
  public double getAutomaticGainControlLevelDb()
  {
    return mAutomaticGainControlLevelInDb;
  }
  
  @Deprecated
  public long getCarrierCycles()
  {
    return mCarrierCycles;
  }
  
  public float getCarrierFrequencyHz()
  {
    return mCarrierFrequencyHz;
  }
  
  @Deprecated
  public double getCarrierPhase()
  {
    return mCarrierPhase;
  }
  
  @Deprecated
  public double getCarrierPhaseUncertainty()
  {
    return mCarrierPhaseUncertainty;
  }
  
  public double getCn0DbHz()
  {
    return mCn0DbHz;
  }
  
  public int getConstellationType()
  {
    return mConstellationType;
  }
  
  public int getMultipathIndicator()
  {
    return mMultipathIndicator;
  }
  
  public double getPseudorangeRateMetersPerSecond()
  {
    return mPseudorangeRateMetersPerSecond;
  }
  
  public double getPseudorangeRateUncertaintyMetersPerSecond()
  {
    return mPseudorangeRateUncertaintyMetersPerSecond;
  }
  
  public long getReceivedSvTimeNanos()
  {
    return mReceivedSvTimeNanos;
  }
  
  public long getReceivedSvTimeUncertaintyNanos()
  {
    return mReceivedSvTimeUncertaintyNanos;
  }
  
  public double getSnrInDb()
  {
    return mSnrInDb;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public int getSvid()
  {
    return mSvid;
  }
  
  public double getTimeOffsetNanos()
  {
    return mTimeOffsetNanos;
  }
  
  public boolean hasAutomaticGainControlLevelDb()
  {
    return isFlagSet(8192);
  }
  
  @Deprecated
  public boolean hasCarrierCycles()
  {
    return isFlagSet(1024);
  }
  
  public boolean hasCarrierFrequencyHz()
  {
    return isFlagSet(512);
  }
  
  @Deprecated
  public boolean hasCarrierPhase()
  {
    return isFlagSet(2048);
  }
  
  @Deprecated
  public boolean hasCarrierPhaseUncertainty()
  {
    return isFlagSet(4096);
  }
  
  public boolean hasSnrInDb()
  {
    return isFlagSet(1);
  }
  
  public void reset()
  {
    initialize();
  }
  
  public void resetAutomaticGainControlLevel()
  {
    resetFlag(8192);
    mAutomaticGainControlLevelInDb = NaN.0D;
  }
  
  @Deprecated
  public void resetCarrierCycles()
  {
    resetFlag(1024);
    mCarrierCycles = Long.MIN_VALUE;
  }
  
  public void resetCarrierFrequencyHz()
  {
    resetFlag(512);
    mCarrierFrequencyHz = NaN.0F;
  }
  
  @Deprecated
  public void resetCarrierPhase()
  {
    resetFlag(2048);
    mCarrierPhase = NaN.0D;
  }
  
  @Deprecated
  public void resetCarrierPhaseUncertainty()
  {
    resetFlag(4096);
    mCarrierPhaseUncertainty = NaN.0D;
  }
  
  public void resetSnrInDb()
  {
    resetFlag(1);
    mSnrInDb = NaN.0D;
  }
  
  public void set(GnssMeasurement paramGnssMeasurement)
  {
    mFlags = mFlags;
    mSvid = mSvid;
    mConstellationType = mConstellationType;
    mTimeOffsetNanos = mTimeOffsetNanos;
    mState = mState;
    mReceivedSvTimeNanos = mReceivedSvTimeNanos;
    mReceivedSvTimeUncertaintyNanos = mReceivedSvTimeUncertaintyNanos;
    mCn0DbHz = mCn0DbHz;
    mPseudorangeRateMetersPerSecond = mPseudorangeRateMetersPerSecond;
    mPseudorangeRateUncertaintyMetersPerSecond = mPseudorangeRateUncertaintyMetersPerSecond;
    mAccumulatedDeltaRangeState = mAccumulatedDeltaRangeState;
    mAccumulatedDeltaRangeMeters = mAccumulatedDeltaRangeMeters;
    mAccumulatedDeltaRangeUncertaintyMeters = mAccumulatedDeltaRangeUncertaintyMeters;
    mCarrierFrequencyHz = mCarrierFrequencyHz;
    mCarrierCycles = mCarrierCycles;
    mCarrierPhase = mCarrierPhase;
    mCarrierPhaseUncertainty = mCarrierPhaseUncertainty;
    mMultipathIndicator = mMultipathIndicator;
    mSnrInDb = mSnrInDb;
    mAutomaticGainControlLevelInDb = mAutomaticGainControlLevelInDb;
  }
  
  public void setAccumulatedDeltaRangeMeters(double paramDouble)
  {
    mAccumulatedDeltaRangeMeters = paramDouble;
  }
  
  public void setAccumulatedDeltaRangeState(int paramInt)
  {
    mAccumulatedDeltaRangeState = paramInt;
  }
  
  public void setAccumulatedDeltaRangeUncertaintyMeters(double paramDouble)
  {
    mAccumulatedDeltaRangeUncertaintyMeters = paramDouble;
  }
  
  public void setAutomaticGainControlLevelInDb(double paramDouble)
  {
    setFlag(8192);
    mAutomaticGainControlLevelInDb = paramDouble;
  }
  
  @Deprecated
  public void setCarrierCycles(long paramLong)
  {
    setFlag(1024);
    mCarrierCycles = paramLong;
  }
  
  public void setCarrierFrequencyHz(float paramFloat)
  {
    setFlag(512);
    mCarrierFrequencyHz = paramFloat;
  }
  
  @Deprecated
  public void setCarrierPhase(double paramDouble)
  {
    setFlag(2048);
    mCarrierPhase = paramDouble;
  }
  
  @Deprecated
  public void setCarrierPhaseUncertainty(double paramDouble)
  {
    setFlag(4096);
    mCarrierPhaseUncertainty = paramDouble;
  }
  
  public void setCn0DbHz(double paramDouble)
  {
    mCn0DbHz = paramDouble;
  }
  
  public void setConstellationType(int paramInt)
  {
    mConstellationType = paramInt;
  }
  
  public void setMultipathIndicator(int paramInt)
  {
    mMultipathIndicator = paramInt;
  }
  
  public void setPseudorangeRateMetersPerSecond(double paramDouble)
  {
    mPseudorangeRateMetersPerSecond = paramDouble;
  }
  
  public void setPseudorangeRateUncertaintyMetersPerSecond(double paramDouble)
  {
    mPseudorangeRateUncertaintyMetersPerSecond = paramDouble;
  }
  
  public void setReceivedSvTimeNanos(long paramLong)
  {
    mReceivedSvTimeNanos = paramLong;
  }
  
  public void setReceivedSvTimeUncertaintyNanos(long paramLong)
  {
    mReceivedSvTimeUncertaintyNanos = paramLong;
  }
  
  public void setSnrInDb(double paramDouble)
  {
    setFlag(1);
    mSnrInDb = paramDouble;
  }
  
  public void setState(int paramInt)
  {
    mState = paramInt;
  }
  
  public void setSvid(int paramInt)
  {
    mSvid = paramInt;
  }
  
  public void setTimeOffsetNanos(double paramDouble)
  {
    mTimeOffsetNanos = paramDouble;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("GnssMeasurement:\n");
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "Svid", Integer.valueOf(mSvid) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "ConstellationType", Integer.valueOf(mConstellationType) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "TimeOffsetNanos", Double.valueOf(mTimeOffsetNanos) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "State", getStateString() }));
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "ReceivedSvTimeNanos", Long.valueOf(mReceivedSvTimeNanos), "ReceivedSvTimeUncertaintyNanos", Long.valueOf(mReceivedSvTimeUncertaintyNanos) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "Cn0DbHz", Double.valueOf(mCn0DbHz) }));
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "PseudorangeRateMetersPerSecond", Double.valueOf(mPseudorangeRateMetersPerSecond), "PseudorangeRateUncertaintyMetersPerSecond", Double.valueOf(mPseudorangeRateUncertaintyMetersPerSecond) }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "AccumulatedDeltaRangeState", getAccumulatedDeltaRangeStateString() }));
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "AccumulatedDeltaRangeMeters", Double.valueOf(mAccumulatedDeltaRangeMeters), "AccumulatedDeltaRangeUncertaintyMeters", Double.valueOf(mAccumulatedDeltaRangeUncertaintyMeters) }));
    boolean bool = hasCarrierFrequencyHz();
    Object localObject1 = null;
    if (bool) {
      localObject2 = Float.valueOf(mCarrierFrequencyHz);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "CarrierFrequencyHz", localObject2 }));
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
    Double localDouble;
    if (hasCarrierPhaseUncertainty()) {
      localDouble = Double.valueOf(mCarrierPhaseUncertainty);
    } else {
      localDouble = null;
    }
    localStringBuilder.append(String.format("   %-29s = %-25s   %-40s = %s\n", new Object[] { "CarrierPhase", localObject2, "CarrierPhaseUncertainty", localDouble }));
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "MultipathIndicator", getMultipathIndicatorString() }));
    if (hasSnrInDb()) {
      localObject2 = Double.valueOf(mSnrInDb);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "SnrInDb", localObject2 }));
    Object localObject2 = localObject1;
    if (hasAutomaticGainControlLevelDb()) {
      localObject2 = Double.valueOf(mAutomaticGainControlLevelInDb);
    }
    localStringBuilder.append(String.format("   %-29s = %s\n", new Object[] { "AgcLevelDb", localObject2 }));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mFlags);
    paramParcel.writeInt(mSvid);
    paramParcel.writeInt(mConstellationType);
    paramParcel.writeDouble(mTimeOffsetNanos);
    paramParcel.writeInt(mState);
    paramParcel.writeLong(mReceivedSvTimeNanos);
    paramParcel.writeLong(mReceivedSvTimeUncertaintyNanos);
    paramParcel.writeDouble(mCn0DbHz);
    paramParcel.writeDouble(mPseudorangeRateMetersPerSecond);
    paramParcel.writeDouble(mPseudorangeRateUncertaintyMetersPerSecond);
    paramParcel.writeInt(mAccumulatedDeltaRangeState);
    paramParcel.writeDouble(mAccumulatedDeltaRangeMeters);
    paramParcel.writeDouble(mAccumulatedDeltaRangeUncertaintyMeters);
    paramParcel.writeFloat(mCarrierFrequencyHz);
    paramParcel.writeLong(mCarrierCycles);
    paramParcel.writeDouble(mCarrierPhase);
    paramParcel.writeDouble(mCarrierPhaseUncertainty);
    paramParcel.writeInt(mMultipathIndicator);
    paramParcel.writeDouble(mSnrInDb);
    paramParcel.writeDouble(mAutomaticGainControlLevelInDb);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AdrState {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MultipathIndicator {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface State {}
}
