package android.hardware.radio;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SystemApi
public class RadioManager
{
  public static final int BAND_AM = 0;
  public static final int BAND_AM_HD = 3;
  public static final int BAND_FM = 1;
  public static final int BAND_FM_HD = 2;
  public static final int BAND_INVALID = -1;
  public static final int CLASS_AM_FM = 0;
  public static final int CLASS_DT = 2;
  public static final int CLASS_SAT = 1;
  public static final int CONFIG_DAB_DAB_LINKING = 6;
  public static final int CONFIG_DAB_DAB_SOFT_LINKING = 8;
  public static final int CONFIG_DAB_FM_LINKING = 7;
  public static final int CONFIG_DAB_FM_SOFT_LINKING = 9;
  public static final int CONFIG_FORCE_ANALOG = 2;
  public static final int CONFIG_FORCE_DIGITAL = 3;
  public static final int CONFIG_FORCE_MONO = 1;
  public static final int CONFIG_RDS_AF = 4;
  public static final int CONFIG_RDS_REG = 5;
  public static final int REGION_ITU_1 = 0;
  public static final int REGION_ITU_2 = 1;
  public static final int REGION_JAPAN = 3;
  public static final int REGION_KOREA = 4;
  public static final int REGION_OIRT = 2;
  public static final int STATUS_BAD_VALUE = -22;
  public static final int STATUS_DEAD_OBJECT = -32;
  public static final int STATUS_ERROR = Integer.MIN_VALUE;
  public static final int STATUS_INVALID_OPERATION = -38;
  public static final int STATUS_NO_INIT = -19;
  public static final int STATUS_OK = 0;
  public static final int STATUS_PERMISSION_DENIED = -1;
  public static final int STATUS_TIMED_OUT = -110;
  private static final String TAG = "BroadcastRadio.manager";
  private final Map<Announcement.OnListUpdatedListener, ICloseHandle> mAnnouncementListeners = new HashMap();
  private final Context mContext;
  private final IRadioService mService;
  
  public RadioManager(Context paramContext)
    throws ServiceManager.ServiceNotFoundException
  {
    mContext = paramContext;
    mService = IRadioService.Stub.asInterface(ServiceManager.getServiceOrThrow("broadcastradio"));
  }
  
  private native int nativeListModules(List<ModuleProperties> paramList);
  
  public void addAnnouncementListener(Set<Integer> paramSet, Announcement.OnListUpdatedListener paramOnListUpdatedListener)
  {
    addAnnouncementListener(_..Lambda.RadioManager.cfMLnpQqL72UMrjmCGbrhAOHHgg.INSTANCE, paramSet, paramOnListUpdatedListener);
  }
  
  /* Error */
  public void addAnnouncementListener(final Executor paramExecutor, Set<Integer> paramSet, final Announcement.OnListUpdatedListener paramOnListUpdatedListener)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 187	java/util/Objects:requireNonNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   4: pop
    //   5: aload_3
    //   6: invokestatic 187	java/util/Objects:requireNonNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   9: pop
    //   10: aload_2
    //   11: invokeinterface 193 1 0
    //   16: getstatic 198	android/hardware/radio/_$$Lambda$RadioManager$UV1wDVoVlbcxpr8zevj_aMFtUGw:INSTANCE	Landroid/hardware/radio/-$$Lambda$RadioManager$UV1wDVoVlbcxpr8zevj_aMFtUGw;
    //   19: invokeinterface 204 2 0
    //   24: invokeinterface 210 1 0
    //   29: astore_2
    //   30: new 6	android/hardware/radio/RadioManager$1
    //   33: dup
    //   34: aload_0
    //   35: aload_1
    //   36: aload_3
    //   37: invokespecial 213	android/hardware/radio/RadioManager$1:<init>	(Landroid/hardware/radio/RadioManager;Ljava/util/concurrent/Executor;Landroid/hardware/radio/Announcement$OnListUpdatedListener;)V
    //   40: astore 4
    //   42: aload_0
    //   43: getfield 129	android/hardware/radio/RadioManager:mAnnouncementListeners	Ljava/util/Map;
    //   46: astore 5
    //   48: aload 5
    //   50: monitorenter
    //   51: aconst_null
    //   52: astore_1
    //   53: aload_0
    //   54: getfield 147	android/hardware/radio/RadioManager:mService	Landroid/hardware/radio/IRadioService;
    //   57: aload_2
    //   58: aload 4
    //   60: invokeinterface 218 3 0
    //   65: astore_2
    //   66: aload_2
    //   67: astore_1
    //   68: goto +13 -> 81
    //   71: astore_1
    //   72: goto +41 -> 113
    //   75: astore_2
    //   76: aload_2
    //   77: invokevirtual 222	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   80: pop
    //   81: aload_1
    //   82: invokestatic 187	java/util/Objects:requireNonNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   85: pop
    //   86: aload_0
    //   87: getfield 129	android/hardware/radio/RadioManager:mAnnouncementListeners	Ljava/util/Map;
    //   90: aload_3
    //   91: aload_1
    //   92: invokeinterface 228 3 0
    //   97: checkcast 230	android/hardware/radio/ICloseHandle
    //   100: astore_1
    //   101: aload_1
    //   102: ifnull +7 -> 109
    //   105: aload_1
    //   106: invokestatic 236	android/hardware/radio/Utils:close	(Landroid/hardware/radio/ICloseHandle;)V
    //   109: aload 5
    //   111: monitorexit
    //   112: return
    //   113: aload 5
    //   115: monitorexit
    //   116: aload_1
    //   117: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	118	0	this	RadioManager
    //   0	118	1	paramExecutor	Executor
    //   0	118	2	paramSet	Set<Integer>
    //   0	118	3	paramOnListUpdatedListener	Announcement.OnListUpdatedListener
    //   40	19	4	local1	1
    //   46	68	5	localMap	Map
    // Exception table:
    //   from	to	target	type
    //   53	66	71	finally
    //   76	81	71	finally
    //   81	101	71	finally
    //   105	109	71	finally
    //   109	112	71	finally
    //   113	116	71	finally
    //   53	66	75	android/os/RemoteException
  }
  
  public int listModules(List<ModuleProperties> paramList)
  {
    if (paramList == null)
    {
      Log.e("BroadcastRadio.manager", "the output list must not be empty");
      return -22;
    }
    Log.d("BroadcastRadio.manager", "Listing available tuners...");
    try
    {
      List localList = mService.listModules();
      if (localList == null)
      {
        Log.e("BroadcastRadio.manager", "Returned list was a null");
        return Integer.MIN_VALUE;
      }
      paramList.addAll(localList);
      return 0;
    }
    catch (RemoteException paramList)
    {
      Log.e("BroadcastRadio.manager", "Failed listing available tuners", paramList);
    }
    return -32;
  }
  
  public RadioTuner openTuner(int paramInt, BandConfig paramBandConfig, boolean paramBoolean, RadioTuner.Callback paramCallback, Handler paramHandler)
  {
    if (paramCallback != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Opening tuner ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("...");
      Log.d("BroadcastRadio.manager", localStringBuilder.toString());
      paramHandler = new TunerCallbackAdapter(paramCallback, paramHandler);
      try
      {
        paramCallback = mService.openTuner(paramInt, paramBandConfig, paramBoolean, paramHandler);
        if (paramCallback == null)
        {
          Log.e("BroadcastRadio.manager", "Failed to open tuner");
          return null;
        }
        if (paramBandConfig != null) {
          paramInt = paramBandConfig.getType();
        } else {
          paramInt = -1;
        }
        return new TunerAdapter(paramCallback, paramHandler, paramInt);
      }
      catch (RemoteException|IllegalArgumentException paramBandConfig)
      {
        Log.e("BroadcastRadio.manager", "Failed to open tuner", paramBandConfig);
        return null;
      }
    }
    throw new IllegalArgumentException("callback must not be empty");
  }
  
  public void removeAnnouncementListener(Announcement.OnListUpdatedListener paramOnListUpdatedListener)
  {
    Objects.requireNonNull(paramOnListUpdatedListener);
    synchronized (mAnnouncementListeners)
    {
      paramOnListUpdatedListener = (ICloseHandle)mAnnouncementListeners.remove(paramOnListUpdatedListener);
      if (paramOnListUpdatedListener != null) {
        Utils.close(paramOnListUpdatedListener);
      }
      return;
    }
  }
  
  public static class AmBandConfig
    extends RadioManager.BandConfig
  {
    public static final Parcelable.Creator<AmBandConfig> CREATOR = new Parcelable.Creator()
    {
      public RadioManager.AmBandConfig createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RadioManager.AmBandConfig(paramAnonymousParcel, null);
      }
      
      public RadioManager.AmBandConfig[] newArray(int paramAnonymousInt)
      {
        return new RadioManager.AmBandConfig[paramAnonymousInt];
      }
    };
    private final boolean mStereo;
    
    AmBandConfig(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
    {
      super(paramInt2, paramInt3, paramInt4, paramInt5);
      mStereo = paramBoolean;
    }
    
    public AmBandConfig(RadioManager.AmBandDescriptor paramAmBandDescriptor)
    {
      super();
      mStereo = paramAmBandDescriptor.isStereoSupported();
    }
    
    private AmBandConfig(Parcel paramParcel)
    {
      super(null);
      int i = paramParcel.readByte();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      mStereo = bool;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!super.equals(paramObject)) {
        return false;
      }
      if (!(paramObject instanceof AmBandConfig)) {
        return false;
      }
      paramObject = (AmBandConfig)paramObject;
      return mStereo == paramObject.getStereo();
    }
    
    public boolean getStereo()
    {
      return mStereo;
    }
    
    public int hashCode()
    {
      return 31 * super.hashCode() + mStereo;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AmBandConfig [");
      localStringBuilder.append(super.toString());
      localStringBuilder.append(", mStereo=");
      localStringBuilder.append(mStereo);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeByte((byte)mStereo);
    }
    
    public static class Builder
    {
      private final RadioManager.BandDescriptor mDescriptor;
      private boolean mStereo;
      
      public Builder(RadioManager.AmBandConfig paramAmBandConfig)
      {
        mDescriptor = new RadioManager.BandDescriptor(paramAmBandConfig.getRegion(), paramAmBandConfig.getType(), paramAmBandConfig.getLowerLimit(), paramAmBandConfig.getUpperLimit(), paramAmBandConfig.getSpacing());
        mStereo = paramAmBandConfig.getStereo();
      }
      
      public Builder(RadioManager.AmBandDescriptor paramAmBandDescriptor)
      {
        mDescriptor = new RadioManager.BandDescriptor(paramAmBandDescriptor.getRegion(), paramAmBandDescriptor.getType(), paramAmBandDescriptor.getLowerLimit(), paramAmBandDescriptor.getUpperLimit(), paramAmBandDescriptor.getSpacing());
        mStereo = paramAmBandDescriptor.isStereoSupported();
      }
      
      public RadioManager.AmBandConfig build()
      {
        return new RadioManager.AmBandConfig(mDescriptor.getRegion(), mDescriptor.getType(), mDescriptor.getLowerLimit(), mDescriptor.getUpperLimit(), mDescriptor.getSpacing(), mStereo);
      }
      
      public Builder setStereo(boolean paramBoolean)
      {
        mStereo = paramBoolean;
        return this;
      }
    }
  }
  
  public static class AmBandDescriptor
    extends RadioManager.BandDescriptor
  {
    public static final Parcelable.Creator<AmBandDescriptor> CREATOR = new Parcelable.Creator()
    {
      public RadioManager.AmBandDescriptor createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RadioManager.AmBandDescriptor(paramAnonymousParcel, null);
      }
      
      public RadioManager.AmBandDescriptor[] newArray(int paramAnonymousInt)
      {
        return new RadioManager.AmBandDescriptor[paramAnonymousInt];
      }
    };
    private final boolean mStereo;
    
    public AmBandDescriptor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
    {
      super(paramInt2, paramInt3, paramInt4, paramInt5);
      mStereo = paramBoolean;
    }
    
    private AmBandDescriptor(Parcel paramParcel)
    {
      super(null);
      int i = paramParcel.readByte();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      mStereo = bool;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!super.equals(paramObject)) {
        return false;
      }
      if (!(paramObject instanceof AmBandDescriptor)) {
        return false;
      }
      paramObject = (AmBandDescriptor)paramObject;
      return mStereo == paramObject.isStereoSupported();
    }
    
    public int hashCode()
    {
      return 31 * super.hashCode() + mStereo;
    }
    
    public boolean isStereoSupported()
    {
      return mStereo;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AmBandDescriptor [ ");
      localStringBuilder.append(super.toString());
      localStringBuilder.append(" mStereo=");
      localStringBuilder.append(mStereo);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeByte((byte)mStereo);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Band {}
  
  public static class BandConfig
    implements Parcelable
  {
    public static final Parcelable.Creator<BandConfig> CREATOR = new Parcelable.Creator()
    {
      public RadioManager.BandConfig createFromParcel(Parcel paramAnonymousParcel)
      {
        int i = RadioManager.BandDescriptor.access$100(paramAnonymousParcel);
        switch (i)
        {
        default: 
          paramAnonymousParcel = new StringBuilder();
          paramAnonymousParcel.append("Unsupported band: ");
          paramAnonymousParcel.append(i);
          throw new IllegalArgumentException(paramAnonymousParcel.toString());
        case 1: 
        case 2: 
          return new RadioManager.FmBandConfig(paramAnonymousParcel, null);
        }
        return new RadioManager.AmBandConfig(paramAnonymousParcel, null);
      }
      
      public RadioManager.BandConfig[] newArray(int paramAnonymousInt)
      {
        return new RadioManager.BandConfig[paramAnonymousInt];
      }
    };
    final RadioManager.BandDescriptor mDescriptor;
    
    BandConfig(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      mDescriptor = new RadioManager.BandDescriptor(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }
    
    BandConfig(RadioManager.BandDescriptor paramBandDescriptor)
    {
      mDescriptor = ((RadioManager.BandDescriptor)Objects.requireNonNull(paramBandDescriptor));
    }
    
    private BandConfig(Parcel paramParcel)
    {
      mDescriptor = new RadioManager.BandDescriptor(paramParcel, null);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof BandConfig)) {
        return false;
      }
      paramObject = ((BandConfig)paramObject).getDescriptor();
      int i;
      if (mDescriptor == null) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if (paramObject == null) {
        j = 1;
      } else {
        j = 0;
      }
      if (i != j) {
        return false;
      }
      return (mDescriptor == null) || (mDescriptor.equals(paramObject));
    }
    
    RadioManager.BandDescriptor getDescriptor()
    {
      return mDescriptor;
    }
    
    public int getLowerLimit()
    {
      return mDescriptor.getLowerLimit();
    }
    
    public int getRegion()
    {
      return mDescriptor.getRegion();
    }
    
    public int getSpacing()
    {
      return mDescriptor.getSpacing();
    }
    
    public int getType()
    {
      return mDescriptor.getType();
    }
    
    public int getUpperLimit()
    {
      return mDescriptor.getUpperLimit();
    }
    
    public int hashCode()
    {
      return 31 * 1 + mDescriptor.hashCode();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("BandConfig [ ");
      localStringBuilder.append(mDescriptor.toString());
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      mDescriptor.writeToParcel(paramParcel, paramInt);
    }
  }
  
  public static class BandDescriptor
    implements Parcelable
  {
    public static final Parcelable.Creator<BandDescriptor> CREATOR = new Parcelable.Creator()
    {
      public RadioManager.BandDescriptor createFromParcel(Parcel paramAnonymousParcel)
      {
        int i = RadioManager.BandDescriptor.lookupTypeFromParcel(paramAnonymousParcel);
        switch (i)
        {
        default: 
          paramAnonymousParcel = new StringBuilder();
          paramAnonymousParcel.append("Unsupported band: ");
          paramAnonymousParcel.append(i);
          throw new IllegalArgumentException(paramAnonymousParcel.toString());
        case 1: 
        case 2: 
          return new RadioManager.FmBandDescriptor(paramAnonymousParcel, null);
        }
        return new RadioManager.AmBandDescriptor(paramAnonymousParcel, null);
      }
      
      public RadioManager.BandDescriptor[] newArray(int paramAnonymousInt)
      {
        return new RadioManager.BandDescriptor[paramAnonymousInt];
      }
    };
    private final int mLowerLimit;
    private final int mRegion;
    private final int mSpacing;
    private final int mType;
    private final int mUpperLimit;
    
    BandDescriptor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      if ((paramInt2 != 0) && (paramInt2 != 1) && (paramInt2 != 2) && (paramInt2 != 3))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported band: ");
        localStringBuilder.append(paramInt2);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      mRegion = paramInt1;
      mType = paramInt2;
      mLowerLimit = paramInt3;
      mUpperLimit = paramInt4;
      mSpacing = paramInt5;
    }
    
    private BandDescriptor(Parcel paramParcel)
    {
      mRegion = paramParcel.readInt();
      mType = paramParcel.readInt();
      mLowerLimit = paramParcel.readInt();
      mUpperLimit = paramParcel.readInt();
      mSpacing = paramParcel.readInt();
    }
    
    private static int lookupTypeFromParcel(Parcel paramParcel)
    {
      int i = paramParcel.dataPosition();
      paramParcel.readInt();
      int j = paramParcel.readInt();
      paramParcel.setDataPosition(i);
      return j;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof BandDescriptor)) {
        return false;
      }
      paramObject = (BandDescriptor)paramObject;
      if (mRegion != paramObject.getRegion()) {
        return false;
      }
      if (mType != paramObject.getType()) {
        return false;
      }
      if (mLowerLimit != paramObject.getLowerLimit()) {
        return false;
      }
      if (mUpperLimit != paramObject.getUpperLimit()) {
        return false;
      }
      return mSpacing == paramObject.getSpacing();
    }
    
    public int getLowerLimit()
    {
      return mLowerLimit;
    }
    
    public int getRegion()
    {
      return mRegion;
    }
    
    public int getSpacing()
    {
      return mSpacing;
    }
    
    public int getType()
    {
      return mType;
    }
    
    public int getUpperLimit()
    {
      return mUpperLimit;
    }
    
    public int hashCode()
    {
      return 31 * (31 * (31 * (31 * (31 * 1 + mRegion) + mType) + mLowerLimit) + mUpperLimit) + mSpacing;
    }
    
    public boolean isAmBand()
    {
      boolean bool;
      if ((mType != 0) && (mType != 3)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean isFmBand()
    {
      int i = mType;
      boolean bool1 = true;
      boolean bool2 = bool1;
      if (i != 1) {
        if (mType == 2) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
      return bool2;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("BandDescriptor [mRegion=");
      localStringBuilder.append(mRegion);
      localStringBuilder.append(", mType=");
      localStringBuilder.append(mType);
      localStringBuilder.append(", mLowerLimit=");
      localStringBuilder.append(mLowerLimit);
      localStringBuilder.append(", mUpperLimit=");
      localStringBuilder.append(mUpperLimit);
      localStringBuilder.append(", mSpacing=");
      localStringBuilder.append(mSpacing);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mRegion);
      paramParcel.writeInt(mType);
      paramParcel.writeInt(mLowerLimit);
      paramParcel.writeInt(mUpperLimit);
      paramParcel.writeInt(mSpacing);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ConfigFlag {}
  
  public static class FmBandConfig
    extends RadioManager.BandConfig
  {
    public static final Parcelable.Creator<FmBandConfig> CREATOR = new Parcelable.Creator()
    {
      public RadioManager.FmBandConfig createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RadioManager.FmBandConfig(paramAnonymousParcel, null);
      }
      
      public RadioManager.FmBandConfig[] newArray(int paramAnonymousInt)
      {
        return new RadioManager.FmBandConfig[paramAnonymousInt];
      }
    };
    private final boolean mAf;
    private final boolean mEa;
    private final boolean mRds;
    private final boolean mStereo;
    private final boolean mTa;
    
    FmBandConfig(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
    {
      super(paramInt2, paramInt3, paramInt4, paramInt5);
      mStereo = paramBoolean1;
      mRds = paramBoolean2;
      mTa = paramBoolean3;
      mAf = paramBoolean4;
      mEa = paramBoolean5;
    }
    
    public FmBandConfig(RadioManager.FmBandDescriptor paramFmBandDescriptor)
    {
      super();
      mStereo = paramFmBandDescriptor.isStereoSupported();
      mRds = paramFmBandDescriptor.isRdsSupported();
      mTa = paramFmBandDescriptor.isTaSupported();
      mAf = paramFmBandDescriptor.isAfSupported();
      mEa = paramFmBandDescriptor.isEaSupported();
    }
    
    private FmBandConfig(Parcel paramParcel)
    {
      super(null);
      int i = paramParcel.readByte();
      boolean bool1 = false;
      if (i == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mStereo = bool2;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mRds = bool2;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mTa = bool2;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mAf = bool2;
      boolean bool2 = bool1;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      }
      mEa = bool2;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!super.equals(paramObject)) {
        return false;
      }
      if (!(paramObject instanceof FmBandConfig)) {
        return false;
      }
      paramObject = (FmBandConfig)paramObject;
      if (mStereo != mStereo) {
        return false;
      }
      if (mRds != mRds) {
        return false;
      }
      if (mTa != mTa) {
        return false;
      }
      if (mAf != mAf) {
        return false;
      }
      return mEa == mEa;
    }
    
    public boolean getAf()
    {
      return mAf;
    }
    
    public boolean getEa()
    {
      return mEa;
    }
    
    public boolean getRds()
    {
      return mRds;
    }
    
    public boolean getStereo()
    {
      return mStereo;
    }
    
    public boolean getTa()
    {
      return mTa;
    }
    
    public int hashCode()
    {
      return 31 * (31 * (31 * (31 * (31 * super.hashCode() + mStereo) + mRds) + mTa) + mAf) + mEa;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("FmBandConfig [");
      localStringBuilder.append(super.toString());
      localStringBuilder.append(", mStereo=");
      localStringBuilder.append(mStereo);
      localStringBuilder.append(", mRds=");
      localStringBuilder.append(mRds);
      localStringBuilder.append(", mTa=");
      localStringBuilder.append(mTa);
      localStringBuilder.append(", mAf=");
      localStringBuilder.append(mAf);
      localStringBuilder.append(", mEa =");
      localStringBuilder.append(mEa);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeByte((byte)mStereo);
      paramParcel.writeByte((byte)mRds);
      paramParcel.writeByte((byte)mTa);
      paramParcel.writeByte((byte)mAf);
      paramParcel.writeByte((byte)mEa);
    }
    
    public static class Builder
    {
      private boolean mAf;
      private final RadioManager.BandDescriptor mDescriptor;
      private boolean mEa;
      private boolean mRds;
      private boolean mStereo;
      private boolean mTa;
      
      public Builder(RadioManager.FmBandConfig paramFmBandConfig)
      {
        mDescriptor = new RadioManager.BandDescriptor(paramFmBandConfig.getRegion(), paramFmBandConfig.getType(), paramFmBandConfig.getLowerLimit(), paramFmBandConfig.getUpperLimit(), paramFmBandConfig.getSpacing());
        mStereo = paramFmBandConfig.getStereo();
        mRds = paramFmBandConfig.getRds();
        mTa = paramFmBandConfig.getTa();
        mAf = paramFmBandConfig.getAf();
        mEa = paramFmBandConfig.getEa();
      }
      
      public Builder(RadioManager.FmBandDescriptor paramFmBandDescriptor)
      {
        mDescriptor = new RadioManager.BandDescriptor(paramFmBandDescriptor.getRegion(), paramFmBandDescriptor.getType(), paramFmBandDescriptor.getLowerLimit(), paramFmBandDescriptor.getUpperLimit(), paramFmBandDescriptor.getSpacing());
        mStereo = paramFmBandDescriptor.isStereoSupported();
        mRds = paramFmBandDescriptor.isRdsSupported();
        mTa = paramFmBandDescriptor.isTaSupported();
        mAf = paramFmBandDescriptor.isAfSupported();
        mEa = paramFmBandDescriptor.isEaSupported();
      }
      
      public RadioManager.FmBandConfig build()
      {
        return new RadioManager.FmBandConfig(mDescriptor.getRegion(), mDescriptor.getType(), mDescriptor.getLowerLimit(), mDescriptor.getUpperLimit(), mDescriptor.getSpacing(), mStereo, mRds, mTa, mAf, mEa);
      }
      
      public Builder setAf(boolean paramBoolean)
      {
        mAf = paramBoolean;
        return this;
      }
      
      public Builder setEa(boolean paramBoolean)
      {
        mEa = paramBoolean;
        return this;
      }
      
      public Builder setRds(boolean paramBoolean)
      {
        mRds = paramBoolean;
        return this;
      }
      
      public Builder setStereo(boolean paramBoolean)
      {
        mStereo = paramBoolean;
        return this;
      }
      
      public Builder setTa(boolean paramBoolean)
      {
        mTa = paramBoolean;
        return this;
      }
    }
  }
  
  public static class FmBandDescriptor
    extends RadioManager.BandDescriptor
  {
    public static final Parcelable.Creator<FmBandDescriptor> CREATOR = new Parcelable.Creator()
    {
      public RadioManager.FmBandDescriptor createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RadioManager.FmBandDescriptor(paramAnonymousParcel, null);
      }
      
      public RadioManager.FmBandDescriptor[] newArray(int paramAnonymousInt)
      {
        return new RadioManager.FmBandDescriptor[paramAnonymousInt];
      }
    };
    private final boolean mAf;
    private final boolean mEa;
    private final boolean mRds;
    private final boolean mStereo;
    private final boolean mTa;
    
    public FmBandDescriptor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
    {
      super(paramInt2, paramInt3, paramInt4, paramInt5);
      mStereo = paramBoolean1;
      mRds = paramBoolean2;
      mTa = paramBoolean3;
      mAf = paramBoolean4;
      mEa = paramBoolean5;
    }
    
    private FmBandDescriptor(Parcel paramParcel)
    {
      super(null);
      int i = paramParcel.readByte();
      boolean bool1 = false;
      if (i == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mStereo = bool2;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mRds = bool2;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mTa = bool2;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mAf = bool2;
      boolean bool2 = bool1;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      }
      mEa = bool2;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!super.equals(paramObject)) {
        return false;
      }
      if (!(paramObject instanceof FmBandDescriptor)) {
        return false;
      }
      paramObject = (FmBandDescriptor)paramObject;
      if (mStereo != paramObject.isStereoSupported()) {
        return false;
      }
      if (mRds != paramObject.isRdsSupported()) {
        return false;
      }
      if (mTa != paramObject.isTaSupported()) {
        return false;
      }
      if (mAf != paramObject.isAfSupported()) {
        return false;
      }
      return mEa == paramObject.isEaSupported();
    }
    
    public int hashCode()
    {
      return 31 * (31 * (31 * (31 * (31 * super.hashCode() + mStereo) + mRds) + mTa) + mAf) + mEa;
    }
    
    public boolean isAfSupported()
    {
      return mAf;
    }
    
    public boolean isEaSupported()
    {
      return mEa;
    }
    
    public boolean isRdsSupported()
    {
      return mRds;
    }
    
    public boolean isStereoSupported()
    {
      return mStereo;
    }
    
    public boolean isTaSupported()
    {
      return mTa;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("FmBandDescriptor [ ");
      localStringBuilder.append(super.toString());
      localStringBuilder.append(" mStereo=");
      localStringBuilder.append(mStereo);
      localStringBuilder.append(", mRds=");
      localStringBuilder.append(mRds);
      localStringBuilder.append(", mTa=");
      localStringBuilder.append(mTa);
      localStringBuilder.append(", mAf=");
      localStringBuilder.append(mAf);
      localStringBuilder.append(", mEa =");
      localStringBuilder.append(mEa);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeByte((byte)mStereo);
      paramParcel.writeByte((byte)mRds);
      paramParcel.writeByte((byte)mTa);
      paramParcel.writeByte((byte)mAf);
      paramParcel.writeByte((byte)mEa);
    }
  }
  
  public static class ModuleProperties
    implements Parcelable
  {
    public static final Parcelable.Creator<ModuleProperties> CREATOR = new Parcelable.Creator()
    {
      public RadioManager.ModuleProperties createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RadioManager.ModuleProperties(paramAnonymousParcel, null);
      }
      
      public RadioManager.ModuleProperties[] newArray(int paramAnonymousInt)
      {
        return new RadioManager.ModuleProperties[paramAnonymousInt];
      }
    };
    private final RadioManager.BandDescriptor[] mBands;
    private final int mClassId;
    private final Map<String, Integer> mDabFrequencyTable;
    private final int mId;
    private final String mImplementor;
    private final boolean mIsBgScanSupported;
    private final boolean mIsCaptureSupported;
    private final boolean mIsInitializationRequired;
    private final int mNumAudioSources;
    private final int mNumTuners;
    private final String mProduct;
    private final String mSerial;
    private final String mServiceName;
    private final Set<Integer> mSupportedIdentifierTypes;
    private final Set<Integer> mSupportedProgramTypes;
    private final Map<String, String> mVendorInfo;
    private final String mVersion;
    
    public ModuleProperties(int paramInt1, String paramString1, int paramInt2, String paramString2, String paramString3, String paramString4, String paramString5, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, RadioManager.BandDescriptor[] paramArrayOfBandDescriptor, boolean paramBoolean3, int[] paramArrayOfInt1, int[] paramArrayOfInt2, Map<String, Integer> paramMap, Map<String, String> paramMap1)
    {
      mId = paramInt1;
      if (TextUtils.isEmpty(paramString1)) {
        paramString1 = "default";
      }
      mServiceName = paramString1;
      mClassId = paramInt2;
      mImplementor = paramString2;
      mProduct = paramString3;
      mVersion = paramString4;
      mSerial = paramString5;
      mNumTuners = paramInt3;
      mNumAudioSources = paramInt4;
      mIsInitializationRequired = paramBoolean1;
      mIsCaptureSupported = paramBoolean2;
      mBands = paramArrayOfBandDescriptor;
      mIsBgScanSupported = paramBoolean3;
      mSupportedProgramTypes = arrayToSet(paramArrayOfInt1);
      mSupportedIdentifierTypes = arrayToSet(paramArrayOfInt2);
      if (paramMap != null)
      {
        paramString1 = paramMap.entrySet().iterator();
        while (paramString1.hasNext())
        {
          paramString2 = (Map.Entry)paramString1.next();
          Objects.requireNonNull((String)paramString2.getKey());
          Objects.requireNonNull((Integer)paramString2.getValue());
        }
      }
      mDabFrequencyTable = paramMap;
      if (paramMap1 == null) {
        paramMap1 = new HashMap();
      }
      mVendorInfo = paramMap1;
    }
    
    private ModuleProperties(Parcel paramParcel)
    {
      mId = paramParcel.readInt();
      Object localObject = paramParcel.readString();
      if (TextUtils.isEmpty((CharSequence)localObject)) {
        localObject = "default";
      }
      mServiceName = ((String)localObject);
      mClassId = paramParcel.readInt();
      mImplementor = paramParcel.readString();
      mProduct = paramParcel.readString();
      mVersion = paramParcel.readString();
      mSerial = paramParcel.readString();
      mNumTuners = paramParcel.readInt();
      mNumAudioSources = paramParcel.readInt();
      int i = paramParcel.readInt();
      boolean bool1 = false;
      if (i == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mIsInitializationRequired = bool2;
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mIsCaptureSupported = bool2;
      localObject = paramParcel.readParcelableArray(RadioManager.BandDescriptor.class.getClassLoader());
      mBands = new RadioManager.BandDescriptor[localObject.length];
      for (i = 0; i < localObject.length; i++) {
        mBands[i] = ((RadioManager.BandDescriptor)localObject[i]);
      }
      boolean bool2 = bool1;
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      }
      mIsBgScanSupported = bool2;
      mSupportedProgramTypes = arrayToSet(paramParcel.createIntArray());
      mSupportedIdentifierTypes = arrayToSet(paramParcel.createIntArray());
      mDabFrequencyTable = Utils.readStringIntMap(paramParcel);
      mVendorInfo = Utils.readStringMap(paramParcel);
    }
    
    private static Set<Integer> arrayToSet(int[] paramArrayOfInt)
    {
      return (Set)Arrays.stream(paramArrayOfInt).boxed().collect(Collectors.toSet());
    }
    
    private static int[] setToArray(Set<Integer> paramSet)
    {
      return paramSet.stream().mapToInt(_..Lambda.RadioManager.ModuleProperties.UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof ModuleProperties)) {
        return false;
      }
      paramObject = (ModuleProperties)paramObject;
      if (mId != paramObject.getId()) {
        return false;
      }
      if (!TextUtils.equals(mServiceName, mServiceName)) {
        return false;
      }
      if (mClassId != mClassId) {
        return false;
      }
      if (!Objects.equals(mImplementor, mImplementor)) {
        return false;
      }
      if (!Objects.equals(mProduct, mProduct)) {
        return false;
      }
      if (!Objects.equals(mVersion, mVersion)) {
        return false;
      }
      if (!Objects.equals(mSerial, mSerial)) {
        return false;
      }
      if (mNumTuners != mNumTuners) {
        return false;
      }
      if (mNumAudioSources != mNumAudioSources) {
        return false;
      }
      if (mIsInitializationRequired != mIsInitializationRequired) {
        return false;
      }
      if (mIsCaptureSupported != mIsCaptureSupported) {
        return false;
      }
      if (!Objects.equals(mBands, mBands)) {
        return false;
      }
      if (mIsBgScanSupported != mIsBgScanSupported) {
        return false;
      }
      if (!Objects.equals(mDabFrequencyTable, mDabFrequencyTable)) {
        return false;
      }
      return Objects.equals(mVendorInfo, mVendorInfo);
    }
    
    public RadioManager.BandDescriptor[] getBands()
    {
      return mBands;
    }
    
    public int getClassId()
    {
      return mClassId;
    }
    
    public Map<String, Integer> getDabFrequencyTable()
    {
      return mDabFrequencyTable;
    }
    
    public int getId()
    {
      return mId;
    }
    
    public String getImplementor()
    {
      return mImplementor;
    }
    
    public int getNumAudioSources()
    {
      return mNumAudioSources;
    }
    
    public int getNumTuners()
    {
      return mNumTuners;
    }
    
    public String getProduct()
    {
      return mProduct;
    }
    
    public String getSerial()
    {
      return mSerial;
    }
    
    public String getServiceName()
    {
      return mServiceName;
    }
    
    public Map<String, String> getVendorInfo()
    {
      return mVendorInfo;
    }
    
    public String getVersion()
    {
      return mVersion;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Integer.valueOf(mId), mServiceName, Integer.valueOf(mClassId), mImplementor, mProduct, mVersion, mSerial, Integer.valueOf(mNumTuners), Integer.valueOf(mNumAudioSources), Boolean.valueOf(mIsInitializationRequired), Boolean.valueOf(mIsCaptureSupported), mBands, Boolean.valueOf(mIsBgScanSupported), mDabFrequencyTable, mVendorInfo });
    }
    
    public boolean isBackgroundScanningSupported()
    {
      return mIsBgScanSupported;
    }
    
    public boolean isCaptureSupported()
    {
      return mIsCaptureSupported;
    }
    
    public boolean isInitializationRequired()
    {
      return mIsInitializationRequired;
    }
    
    public boolean isProgramIdentifierSupported(int paramInt)
    {
      return mSupportedIdentifierTypes.contains(Integer.valueOf(paramInt));
    }
    
    public boolean isProgramTypeSupported(int paramInt)
    {
      return mSupportedProgramTypes.contains(Integer.valueOf(paramInt));
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ModuleProperties [mId=");
      localStringBuilder.append(mId);
      localStringBuilder.append(", mServiceName=");
      localStringBuilder.append(mServiceName);
      localStringBuilder.append(", mClassId=");
      localStringBuilder.append(mClassId);
      localStringBuilder.append(", mImplementor=");
      localStringBuilder.append(mImplementor);
      localStringBuilder.append(", mProduct=");
      localStringBuilder.append(mProduct);
      localStringBuilder.append(", mVersion=");
      localStringBuilder.append(mVersion);
      localStringBuilder.append(", mSerial=");
      localStringBuilder.append(mSerial);
      localStringBuilder.append(", mNumTuners=");
      localStringBuilder.append(mNumTuners);
      localStringBuilder.append(", mNumAudioSources=");
      localStringBuilder.append(mNumAudioSources);
      localStringBuilder.append(", mIsInitializationRequired=");
      localStringBuilder.append(mIsInitializationRequired);
      localStringBuilder.append(", mIsCaptureSupported=");
      localStringBuilder.append(mIsCaptureSupported);
      localStringBuilder.append(", mIsBgScanSupported=");
      localStringBuilder.append(mIsBgScanSupported);
      localStringBuilder.append(", mBands=");
      localStringBuilder.append(Arrays.toString(mBands));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mId);
      paramParcel.writeString(mServiceName);
      paramParcel.writeInt(mClassId);
      paramParcel.writeString(mImplementor);
      paramParcel.writeString(mProduct);
      paramParcel.writeString(mVersion);
      paramParcel.writeString(mSerial);
      paramParcel.writeInt(mNumTuners);
      paramParcel.writeInt(mNumAudioSources);
      paramParcel.writeInt(mIsInitializationRequired);
      paramParcel.writeInt(mIsCaptureSupported);
      paramParcel.writeParcelableArray(mBands, paramInt);
      paramParcel.writeInt(mIsBgScanSupported);
      paramParcel.writeIntArray(setToArray(mSupportedProgramTypes));
      paramParcel.writeIntArray(setToArray(mSupportedIdentifierTypes));
      Utils.writeStringIntMap(paramParcel, mDabFrequencyTable);
      Utils.writeStringMap(paramParcel, mVendorInfo);
    }
  }
  
  public static class ProgramInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<ProgramInfo> CREATOR = new Parcelable.Creator()
    {
      public RadioManager.ProgramInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RadioManager.ProgramInfo(paramAnonymousParcel, null);
      }
      
      public RadioManager.ProgramInfo[] newArray(int paramAnonymousInt)
      {
        return new RadioManager.ProgramInfo[paramAnonymousInt];
      }
    };
    private static final int FLAG_LIVE = 1;
    private static final int FLAG_MUTED = 2;
    private static final int FLAG_STEREO = 32;
    private static final int FLAG_TRAFFIC_ANNOUNCEMENT = 8;
    private static final int FLAG_TRAFFIC_PROGRAM = 4;
    private static final int FLAG_TUNED = 16;
    private final int mInfoFlags;
    private final ProgramSelector.Identifier mLogicallyTunedTo;
    private final RadioMetadata mMetadata;
    private final ProgramSelector.Identifier mPhysicallyTunedTo;
    private final Collection<ProgramSelector.Identifier> mRelatedContent;
    private final ProgramSelector mSelector;
    private final int mSignalQuality;
    private final Map<String, String> mVendorInfo;
    
    public ProgramInfo(ProgramSelector paramProgramSelector, ProgramSelector.Identifier paramIdentifier1, ProgramSelector.Identifier paramIdentifier2, Collection<ProgramSelector.Identifier> paramCollection, int paramInt1, int paramInt2, RadioMetadata paramRadioMetadata, Map<String, String> paramMap)
    {
      mSelector = ((ProgramSelector)Objects.requireNonNull(paramProgramSelector));
      mLogicallyTunedTo = paramIdentifier1;
      mPhysicallyTunedTo = paramIdentifier2;
      if (paramCollection == null)
      {
        mRelatedContent = Collections.emptyList();
      }
      else
      {
        Preconditions.checkCollectionElementsNotNull(paramCollection, "relatedContent");
        mRelatedContent = paramCollection;
      }
      mInfoFlags = paramInt1;
      mSignalQuality = paramInt2;
      mMetadata = paramRadioMetadata;
      if (paramMap == null) {
        paramProgramSelector = new HashMap();
      } else {
        paramProgramSelector = paramMap;
      }
      mVendorInfo = paramProgramSelector;
    }
    
    private ProgramInfo(Parcel paramParcel)
    {
      mSelector = ((ProgramSelector)Objects.requireNonNull((ProgramSelector)paramParcel.readTypedObject(ProgramSelector.CREATOR)));
      mLogicallyTunedTo = ((ProgramSelector.Identifier)paramParcel.readTypedObject(ProgramSelector.Identifier.CREATOR));
      mPhysicallyTunedTo = ((ProgramSelector.Identifier)paramParcel.readTypedObject(ProgramSelector.Identifier.CREATOR));
      mRelatedContent = paramParcel.createTypedArrayList(ProgramSelector.Identifier.CREATOR);
      mInfoFlags = paramParcel.readInt();
      mSignalQuality = paramParcel.readInt();
      mMetadata = ((RadioMetadata)paramParcel.readTypedObject(RadioMetadata.CREATOR));
      mVendorInfo = Utils.readStringMap(paramParcel);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof ProgramInfo)) {
        return false;
      }
      paramObject = (ProgramInfo)paramObject;
      if (!Objects.equals(mSelector, mSelector)) {
        return false;
      }
      if (!Objects.equals(mLogicallyTunedTo, mLogicallyTunedTo)) {
        return false;
      }
      if (!Objects.equals(mPhysicallyTunedTo, mPhysicallyTunedTo)) {
        return false;
      }
      if (!Objects.equals(mRelatedContent, mRelatedContent)) {
        return false;
      }
      if (mInfoFlags != mInfoFlags) {
        return false;
      }
      if (mSignalQuality != mSignalQuality) {
        return false;
      }
      if (!Objects.equals(mMetadata, mMetadata)) {
        return false;
      }
      return Objects.equals(mVendorInfo, mVendorInfo);
    }
    
    @Deprecated
    public int getChannel()
    {
      try
      {
        long l = mSelector.getFirstId(1);
        return (int)l;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.w("BroadcastRadio.manager", "Not an AM/FM program");
      }
      return 0;
    }
    
    public ProgramSelector.Identifier getLogicallyTunedTo()
    {
      return mLogicallyTunedTo;
    }
    
    public RadioMetadata getMetadata()
    {
      return mMetadata;
    }
    
    public ProgramSelector.Identifier getPhysicallyTunedTo()
    {
      return mPhysicallyTunedTo;
    }
    
    public Collection<ProgramSelector.Identifier> getRelatedContent()
    {
      return mRelatedContent;
    }
    
    public ProgramSelector getSelector()
    {
      return mSelector;
    }
    
    public int getSignalStrength()
    {
      return mSignalQuality;
    }
    
    @Deprecated
    public int getSubChannel()
    {
      try
      {
        long l = mSelector.getFirstId(4);
        return (int)l + 1;
      }
      catch (IllegalArgumentException localIllegalArgumentException) {}
      return 0;
    }
    
    public Map<String, String> getVendorInfo()
    {
      return mVendorInfo;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mSelector, mLogicallyTunedTo, mPhysicallyTunedTo, mRelatedContent, Integer.valueOf(mInfoFlags), Integer.valueOf(mSignalQuality), mMetadata, mVendorInfo });
    }
    
    @Deprecated
    public boolean isDigital()
    {
      ProgramSelector.Identifier localIdentifier1 = mLogicallyTunedTo;
      ProgramSelector.Identifier localIdentifier2 = localIdentifier1;
      if (localIdentifier1 == null) {
        localIdentifier2 = mSelector.getPrimaryId();
      }
      int i = localIdentifier2.getType();
      boolean bool = true;
      if ((i == 1) || (i == 2)) {
        bool = false;
      }
      return bool;
    }
    
    public boolean isLive()
    {
      int i = mInfoFlags;
      boolean bool = true;
      if ((i & 0x1) == 0) {
        bool = false;
      }
      return bool;
    }
    
    public boolean isMuted()
    {
      boolean bool;
      if ((mInfoFlags & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isStereo()
    {
      boolean bool;
      if ((mInfoFlags & 0x20) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isTrafficAnnouncementActive()
    {
      boolean bool;
      if ((mInfoFlags & 0x8) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isTrafficProgram()
    {
      boolean bool;
      if ((mInfoFlags & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isTuned()
    {
      boolean bool;
      if ((mInfoFlags & 0x10) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ProgramInfo [selector=");
      localStringBuilder.append(mSelector);
      localStringBuilder.append(", logicallyTunedTo=");
      localStringBuilder.append(Objects.toString(mLogicallyTunedTo));
      localStringBuilder.append(", physicallyTunedTo=");
      localStringBuilder.append(Objects.toString(mPhysicallyTunedTo));
      localStringBuilder.append(", relatedContent=");
      localStringBuilder.append(mRelatedContent.size());
      localStringBuilder.append(", infoFlags=");
      localStringBuilder.append(mInfoFlags);
      localStringBuilder.append(", mSignalQuality=");
      localStringBuilder.append(mSignalQuality);
      localStringBuilder.append(", mMetadata=");
      localStringBuilder.append(Objects.toString(mMetadata));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeTypedObject(mSelector, paramInt);
      paramParcel.writeTypedObject(mLogicallyTunedTo, paramInt);
      paramParcel.writeTypedObject(mPhysicallyTunedTo, paramInt);
      Utils.writeTypedCollection(paramParcel, mRelatedContent);
      paramParcel.writeInt(mInfoFlags);
      paramParcel.writeInt(mSignalQuality);
      paramParcel.writeTypedObject(mMetadata, paramInt);
      Utils.writeStringMap(paramParcel, mVendorInfo);
    }
  }
}
