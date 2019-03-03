package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Objects;

public final class VolumeShaper
  implements AutoCloseable
{
  private int mId;
  private final WeakReference<PlayerBase> mWeakPlayerBase;
  
  VolumeShaper(Configuration paramConfiguration, PlayerBase paramPlayerBase)
  {
    mWeakPlayerBase = new WeakReference(paramPlayerBase);
    mId = applyPlayer(paramConfiguration, new VolumeShaper.Operation.Builder().defer().build());
  }
  
  private int applyPlayer(Configuration paramConfiguration, Operation paramOperation)
  {
    if (mWeakPlayerBase != null)
    {
      PlayerBase localPlayerBase = (PlayerBase)mWeakPlayerBase.get();
      if (localPlayerBase != null)
      {
        int i = localPlayerBase.playerApplyVolumeShaper(paramConfiguration, paramOperation);
        if (i < 0)
        {
          if (i == -38) {
            throw new IllegalStateException("player or VolumeShaper deallocated");
          }
          paramConfiguration = new StringBuilder();
          paramConfiguration.append("invalid configuration or operation: ");
          paramConfiguration.append(i);
          throw new IllegalArgumentException(paramConfiguration.toString());
        }
        return i;
      }
      throw new IllegalStateException("player deallocated");
    }
    throw new IllegalStateException("uninitialized shaper");
  }
  
  private State getStatePlayer(int paramInt)
  {
    if (mWeakPlayerBase != null)
    {
      Object localObject = (PlayerBase)mWeakPlayerBase.get();
      if (localObject != null)
      {
        localObject = ((PlayerBase)localObject).playerGetVolumeShaperState(paramInt);
        if (localObject != null) {
          return localObject;
        }
        throw new IllegalStateException("shaper cannot be found");
      }
      throw new IllegalStateException("player deallocated");
    }
    throw new IllegalStateException("uninitialized shaper");
  }
  
  public void apply(Operation paramOperation)
  {
    applyPlayer(new Configuration(mId), paramOperation);
  }
  
  public void close()
  {
    try
    {
      Configuration localConfiguration = new android/media/VolumeShaper$Configuration;
      localConfiguration.<init>(mId);
      VolumeShaper.Operation.Builder localBuilder = new android/media/VolumeShaper$Operation$Builder;
      localBuilder.<init>();
      applyPlayer(localConfiguration, localBuilder.terminate().build());
    }
    catch (IllegalStateException localIllegalStateException) {}
    if (mWeakPlayerBase != null) {
      mWeakPlayerBase.clear();
    }
  }
  
  protected void finalize()
  {
    close();
  }
  
  int getId()
  {
    return mId;
  }
  
  public float getVolume()
  {
    return getStatePlayer(mId).getVolume();
  }
  
  public void replace(Configuration paramConfiguration, Operation paramOperation, boolean paramBoolean)
  {
    mId = applyPlayer(paramConfiguration, new VolumeShaper.Operation.Builder(paramOperation).replace(mId, paramBoolean).build());
  }
  
  public static final class Configuration
    implements Parcelable
  {
    public static final Parcelable.Creator<Configuration> CREATOR = new Parcelable.Creator()
    {
      public VolumeShaper.Configuration createFromParcel(Parcel paramAnonymousParcel)
      {
        int i = paramAnonymousParcel.readInt();
        int j = paramAnonymousParcel.readInt();
        if (i == 0) {
          return new VolumeShaper.Configuration(j);
        }
        int k = paramAnonymousParcel.readInt();
        double d = paramAnonymousParcel.readDouble();
        int m = paramAnonymousParcel.readInt();
        paramAnonymousParcel.readFloat();
        paramAnonymousParcel.readFloat();
        int n = paramAnonymousParcel.readInt();
        float[] arrayOfFloat1 = new float[n];
        float[] arrayOfFloat2 = new float[n];
        for (int i1 = 0; i1 < n; i1++)
        {
          arrayOfFloat1[i1] = paramAnonymousParcel.readFloat();
          arrayOfFloat2[i1] = paramAnonymousParcel.readFloat();
        }
        return new VolumeShaper.Configuration(i, j, k, d, m, arrayOfFloat1, arrayOfFloat2, null);
      }
      
      public VolumeShaper.Configuration[] newArray(int paramAnonymousInt)
      {
        return new VolumeShaper.Configuration[paramAnonymousInt];
      }
    };
    public static final Configuration CUBIC_RAMP;
    public static final int INTERPOLATOR_TYPE_CUBIC = 2;
    public static final int INTERPOLATOR_TYPE_CUBIC_MONOTONIC = 3;
    public static final int INTERPOLATOR_TYPE_LINEAR = 1;
    public static final int INTERPOLATOR_TYPE_STEP = 0;
    public static final Configuration LINEAR_RAMP = new Builder().setInterpolatorType(1).setCurve(new float[] { 0.0F, 1.0F }, new float[] { 0.0F, 1.0F }).setDuration(1000L).build();
    private static final int MAXIMUM_CURVE_POINTS = 16;
    public static final int OPTION_FLAG_CLOCK_TIME = 2;
    private static final int OPTION_FLAG_PUBLIC_ALL = 3;
    public static final int OPTION_FLAG_VOLUME_IN_DBFS = 1;
    public static final Configuration SCURVE_RAMP;
    public static final Configuration SINE_RAMP;
    static final int TYPE_ID = 0;
    static final int TYPE_SCALE = 1;
    private final double mDurationMs;
    private final int mId;
    private final int mInterpolatorType;
    private final int mOptionFlags;
    private final float[] mTimes;
    private final int mType;
    private final float[] mVolumes;
    
    static
    {
      CUBIC_RAMP = new Builder().setInterpolatorType(2).setCurve(new float[] { 0.0F, 1.0F }, new float[] { 0.0F, 1.0F }).setDuration(1000L).build();
      float[] arrayOfFloat1 = new float[16];
      float[] arrayOfFloat2 = new float[16];
      float[] arrayOfFloat3 = new float[16];
      for (int i = 0; i < 16; i++)
      {
        arrayOfFloat1[i] = (i / 15.0F);
        float f = (float)Math.sin(arrayOfFloat1[i] * 3.141592653589793D / 2.0D);
        arrayOfFloat2[i] = f;
        arrayOfFloat3[i] = (f * f);
      }
      SINE_RAMP = new Builder().setInterpolatorType(2).setCurve(arrayOfFloat1, arrayOfFloat2).setDuration(1000L).build();
      SCURVE_RAMP = new Builder().setInterpolatorType(2).setCurve(arrayOfFloat1, arrayOfFloat3).setDuration(1000L).build();
    }
    
    public Configuration(int paramInt)
    {
      if (paramInt >= 0)
      {
        mType = 0;
        mId = paramInt;
        mInterpolatorType = 0;
        mOptionFlags = 0;
        mDurationMs = 0.0D;
        mTimes = null;
        mVolumes = null;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("negative id ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    private Configuration(int paramInt1, int paramInt2, int paramInt3, double paramDouble, int paramInt4, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
    {
      mType = paramInt1;
      mId = paramInt2;
      mOptionFlags = paramInt3;
      mDurationMs = paramDouble;
      mInterpolatorType = paramInt4;
      mTimes = paramArrayOfFloat1;
      mVolumes = paramArrayOfFloat2;
    }
    
    private static String checkCurveForErrors(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, boolean paramBoolean)
    {
      if (paramArrayOfFloat1 == null) {
        return "times array must be non-null";
      }
      if (paramArrayOfFloat2 == null) {
        return "volumes array must be non-null";
      }
      if (paramArrayOfFloat1.length != paramArrayOfFloat2.length) {
        return "array length must match";
      }
      if (paramArrayOfFloat1.length < 2) {
        return "array length must be at least 2";
      }
      if (paramArrayOfFloat1.length > 16) {
        return "array length must be no larger than 16";
      }
      int i = 0;
      int j = 0;
      if (paramArrayOfFloat1[0] != 0.0F) {
        return "times must start at 0.f";
      }
      int k = paramArrayOfFloat1.length;
      int m = 1;
      if (paramArrayOfFloat1[(k - 1)] != 1.0F) {
        return "times must end at 1.f";
      }
      while (m < paramArrayOfFloat1.length)
      {
        if (paramArrayOfFloat1[m] <= paramArrayOfFloat1[(m - 1)])
        {
          paramArrayOfFloat1 = new StringBuilder();
          paramArrayOfFloat1.append("times not monotonic increasing, check index ");
          paramArrayOfFloat1.append(m);
          return paramArrayOfFloat1.toString();
        }
        m++;
      }
      if (paramBoolean) {
        for (m = j; m < paramArrayOfFloat2.length; m++) {
          if (paramArrayOfFloat2[m] > 0.0F)
          {
            paramArrayOfFloat1 = new StringBuilder();
            paramArrayOfFloat1.append("volumes for log scale cannot be positive, check index ");
            paramArrayOfFloat1.append(m);
            return paramArrayOfFloat1.toString();
          }
        }
      }
      m = i;
      while (m < paramArrayOfFloat2.length) {
        if ((paramArrayOfFloat2[m] >= 0.0F) && (paramArrayOfFloat2[m] <= 1.0F))
        {
          m++;
        }
        else
        {
          paramArrayOfFloat1 = new StringBuilder();
          paramArrayOfFloat1.append("volumes for linear scale must be between 0.f and 1.f, check index ");
          paramArrayOfFloat1.append(m);
          return paramArrayOfFloat1.toString();
        }
      }
      return null;
    }
    
    private static void checkCurveForErrorsAndThrowException(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, boolean paramBoolean1, boolean paramBoolean2)
    {
      paramArrayOfFloat1 = checkCurveForErrors(paramArrayOfFloat1, paramArrayOfFloat2, paramBoolean1);
      if (paramArrayOfFloat1 != null)
      {
        if (paramBoolean2) {
          throw new IllegalStateException(paramArrayOfFloat1);
        }
        throw new IllegalArgumentException(paramArrayOfFloat1);
      }
    }
    
    private static void checkValidVolumeAndThrowException(float paramFloat, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        if (paramFloat > 0.0F) {
          throw new IllegalArgumentException("dbfs volume must be 0.f or less");
        }
      }
      else {
        if ((paramFloat < 0.0F) || (paramFloat > 1.0F)) {
          break label36;
        }
      }
      return;
      label36:
      throw new IllegalArgumentException("volume must be >= 0.f and <= 1.f");
    }
    
    private static void clampVolume(float[] paramArrayOfFloat, boolean paramBoolean)
    {
      int i = 0;
      int j = 0;
      if (paramBoolean) {
        while (j < paramArrayOfFloat.length)
        {
          if (paramArrayOfFloat[j] > 0.0F) {
            paramArrayOfFloat[j] = 0.0F;
          }
          j++;
        }
      }
      for (j = i; j < paramArrayOfFloat.length; j++) {
        if (paramArrayOfFloat[j] < 0.0F) {
          paramArrayOfFloat[j] = 0.0F;
        } else if (paramArrayOfFloat[j] > 1.0F) {
          paramArrayOfFloat[j] = 1.0F;
        }
      }
    }
    
    public static int getMaximumCurvePoints()
    {
      return 16;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof Configuration)) {
        return false;
      }
      boolean bool = true;
      if (paramObject == this) {
        return true;
      }
      paramObject = (Configuration)paramObject;
      if ((mType != mType) || (mId != mId) || ((mType == 0) || ((mOptionFlags != mOptionFlags) || (mDurationMs != mDurationMs) || (mInterpolatorType != mInterpolatorType) || (!Arrays.equals(mTimes, mTimes)) || (!Arrays.equals(mVolumes, mVolumes))))) {
        bool = false;
      }
      return bool;
    }
    
    int getAllOptionFlags()
    {
      return mOptionFlags;
    }
    
    public long getDuration()
    {
      return mDurationMs;
    }
    
    public int getId()
    {
      return mId;
    }
    
    public int getInterpolatorType()
    {
      return mInterpolatorType;
    }
    
    public int getOptionFlags()
    {
      return mOptionFlags & 0x3;
    }
    
    public float[] getTimes()
    {
      return mTimes;
    }
    
    public int getType()
    {
      return mType;
    }
    
    public float[] getVolumes()
    {
      return mVolumes;
    }
    
    public int hashCode()
    {
      int i;
      if (mType == 0) {
        i = Objects.hash(new Object[] { Integer.valueOf(mType), Integer.valueOf(mId) });
      } else {
        i = Objects.hash(new Object[] { Integer.valueOf(mType), Integer.valueOf(mId), Integer.valueOf(mOptionFlags), Double.valueOf(mDurationMs), Integer.valueOf(mInterpolatorType), Integer.valueOf(Arrays.hashCode(mTimes)), Integer.valueOf(Arrays.hashCode(mVolumes)) });
      }
      return i;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("VolumeShaper.Configuration{mType = ");
      localStringBuilder.append(mType);
      localStringBuilder.append(", mId = ");
      localStringBuilder.append(mId);
      Object localObject;
      if (mType == 0)
      {
        localObject = "}";
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(", mOptionFlags = 0x");
        ((StringBuilder)localObject).append(Integer.toHexString(mOptionFlags).toUpperCase());
        ((StringBuilder)localObject).append(", mDurationMs = ");
        ((StringBuilder)localObject).append(mDurationMs);
        ((StringBuilder)localObject).append(", mInterpolatorType = ");
        ((StringBuilder)localObject).append(mInterpolatorType);
        ((StringBuilder)localObject).append(", mTimes[] = ");
        ((StringBuilder)localObject).append(Arrays.toString(mTimes));
        ((StringBuilder)localObject).append(", mVolumes[] = ");
        ((StringBuilder)localObject).append(Arrays.toString(mVolumes));
        ((StringBuilder)localObject).append("}");
        localObject = ((StringBuilder)localObject).toString();
      }
      localStringBuilder.append((String)localObject);
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mType);
      paramParcel.writeInt(mId);
      if (mType != 0)
      {
        paramParcel.writeInt(mOptionFlags);
        paramParcel.writeDouble(mDurationMs);
        paramParcel.writeInt(mInterpolatorType);
        paramParcel.writeFloat(0.0F);
        paramParcel.writeFloat(0.0F);
        paramParcel.writeInt(mTimes.length);
        for (paramInt = 0; paramInt < mTimes.length; paramInt++)
        {
          paramParcel.writeFloat(mTimes[paramInt]);
          paramParcel.writeFloat(mVolumes[paramInt]);
        }
      }
    }
    
    public static final class Builder
    {
      private double mDurationMs = 1000.0D;
      private int mId = -1;
      private int mInterpolatorType = 2;
      private int mOptionFlags = 2;
      private float[] mTimes = null;
      private int mType = 1;
      private float[] mVolumes = null;
      
      public Builder() {}
      
      public Builder(VolumeShaper.Configuration paramConfiguration)
      {
        mType = paramConfiguration.getType();
        mId = paramConfiguration.getId();
        mOptionFlags = paramConfiguration.getAllOptionFlags();
        mInterpolatorType = paramConfiguration.getInterpolatorType();
        mDurationMs = paramConfiguration.getDuration();
        mTimes = ((float[])paramConfiguration.getTimes().clone());
        mVolumes = ((float[])paramConfiguration.getVolumes().clone());
      }
      
      public VolumeShaper.Configuration build()
      {
        boolean bool;
        if ((mOptionFlags & 0x1) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        VolumeShaper.Configuration.checkCurveForErrorsAndThrowException(mTimes, mVolumes, bool, true);
        return new VolumeShaper.Configuration(mType, mId, mOptionFlags, mDurationMs, mInterpolatorType, mTimes, mVolumes, null);
      }
      
      public Builder invertVolumes()
      {
        int i = mOptionFlags;
        int j = 1;
        int k = 0;
        boolean bool;
        if ((i & 0x1) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        VolumeShaper.Configuration.checkCurveForErrorsAndThrowException(mTimes, mVolumes, bool, true);
        float f1 = mVolumes[0];
        float f4;
        for (float f2 = mVolumes[0]; j < mVolumes.length; f2 = f4)
        {
          float f3;
          if (mVolumes[j] < f1)
          {
            f3 = mVolumes[j];
            f4 = f2;
          }
          else
          {
            f3 = f1;
            f4 = f2;
            if (mVolumes[j] > f2)
            {
              f4 = mVolumes[j];
              f3 = f1;
            }
          }
          j++;
          f1 = f3;
        }
        for (j = k; j < mVolumes.length; j++) {
          mVolumes[j] = (f2 + f1 - mVolumes[j]);
        }
        return this;
      }
      
      public Builder reflectTimes()
      {
        int i = mOptionFlags;
        int j = 0;
        boolean bool;
        if ((i & 0x1) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        VolumeShaper.Configuration.checkCurveForErrorsAndThrowException(mTimes, mVolumes, bool, true);
        while (j < mTimes.length / 2)
        {
          float f = mTimes[j];
          mTimes[j] = (1.0F - mTimes[(mTimes.length - 1 - j)]);
          mTimes[(mTimes.length - 1 - j)] = (1.0F - f);
          f = mVolumes[j];
          mVolumes[j] = mVolumes[(mVolumes.length - 1 - j)];
          mVolumes[(mVolumes.length - 1 - j)] = f;
          j++;
        }
        if ((0x1 & mTimes.length) != 0) {
          mTimes[j] = (1.0F - mTimes[j]);
        }
        return this;
      }
      
      public Builder scaleToEndVolume(float paramFloat)
      {
        int i = mOptionFlags;
        int j = 0;
        int k = 0;
        boolean bool;
        if ((i & 0x1) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        VolumeShaper.Configuration.checkCurveForErrorsAndThrowException(mTimes, mVolumes, bool, true);
        VolumeShaper.Configuration.checkValidVolumeAndThrowException(paramFloat, bool);
        float f1 = mVolumes[0];
        float f2 = mVolumes[(mVolumes.length - 1)];
        if (f2 == f1)
        {
          while (k < mVolumes.length)
          {
            mVolumes[k] += mTimes[k] * (paramFloat - f1);
            k++;
          }
        }
        else
        {
          paramFloat = (paramFloat - f1) / (f2 - f1);
          for (k = j; k < mVolumes.length; k++) {
            mVolumes[k] = ((mVolumes[k] - f1) * paramFloat + f1);
          }
        }
        VolumeShaper.Configuration.clampVolume(mVolumes, bool);
        return this;
      }
      
      public Builder scaleToStartVolume(float paramFloat)
      {
        int i = mOptionFlags;
        int j = 0;
        int k = 0;
        boolean bool;
        if ((i & 0x1) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        VolumeShaper.Configuration.checkCurveForErrorsAndThrowException(mTimes, mVolumes, bool, true);
        VolumeShaper.Configuration.checkValidVolumeAndThrowException(paramFloat, bool);
        float f1 = mVolumes[0];
        float f2 = mVolumes[(mVolumes.length - 1)];
        if (f2 == f1)
        {
          while (k < mVolumes.length)
          {
            mVolumes[k] += (1.0F - mTimes[k]) * (paramFloat - f1);
            k++;
          }
        }
        else
        {
          paramFloat = (paramFloat - f2) / (f1 - f2);
          for (k = j; k < mVolumes.length; k++) {
            mVolumes[k] = ((mVolumes[k] - f2) * paramFloat + f2);
          }
        }
        VolumeShaper.Configuration.clampVolume(mVolumes, bool);
        return this;
      }
      
      public Builder setCurve(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
      {
        int i = mOptionFlags;
        boolean bool = true;
        if ((i & 0x1) == 0) {
          bool = false;
        }
        VolumeShaper.Configuration.checkCurveForErrorsAndThrowException(paramArrayOfFloat1, paramArrayOfFloat2, bool, false);
        mTimes = ((float[])paramArrayOfFloat1.clone());
        mVolumes = ((float[])paramArrayOfFloat2.clone());
        return this;
      }
      
      public Builder setDuration(long paramLong)
      {
        if (paramLong > 0L)
        {
          mDurationMs = paramLong;
          return this;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("duration: ");
        localStringBuilder.append(paramLong);
        localStringBuilder.append(" not positive");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      
      public Builder setId(int paramInt)
      {
        if (paramInt >= -1)
        {
          mId = paramInt;
          return this;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("invalid id: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      
      public Builder setInterpolatorType(int paramInt)
      {
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("invalid interpolatorType: ");
          localStringBuilder.append(paramInt);
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        mInterpolatorType = paramInt;
        return this;
      }
      
      public Builder setOptionFlags(int paramInt)
      {
        if ((paramInt & 0xFFFFFFFC) == 0)
        {
          mOptionFlags = (mOptionFlags & 0xFFFFFFFC | paramInt);
          return this;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("invalid bits in flag: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface InterpolatorType {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface OptionFlag {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Type {}
  }
  
  public static final class Operation
    implements Parcelable
  {
    public static final Parcelable.Creator<Operation> CREATOR = new Parcelable.Creator()
    {
      public VolumeShaper.Operation createFromParcel(Parcel paramAnonymousParcel)
      {
        return new VolumeShaper.Operation(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readFloat(), null);
      }
      
      public VolumeShaper.Operation[] newArray(int paramAnonymousInt)
      {
        return new VolumeShaper.Operation[paramAnonymousInt];
      }
    };
    private static final int FLAG_CREATE_IF_NEEDED = 16;
    private static final int FLAG_DEFER = 8;
    private static final int FLAG_JOIN = 4;
    private static final int FLAG_NONE = 0;
    private static final int FLAG_PUBLIC_ALL = 3;
    private static final int FLAG_REVERSE = 1;
    private static final int FLAG_TERMINATE = 2;
    public static final Operation PLAY = new Builder().build();
    public static final Operation REVERSE = new Builder().reverse().build();
    private final int mFlags;
    private final int mReplaceId;
    private final float mXOffset;
    
    private Operation(int paramInt1, int paramInt2, float paramFloat)
    {
      mFlags = paramInt1;
      mReplaceId = paramInt2;
      mXOffset = paramFloat;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof Operation)) {
        return false;
      }
      boolean bool = true;
      if (paramObject == this) {
        return true;
      }
      paramObject = (Operation)paramObject;
      if ((mFlags != mFlags) || (mReplaceId != mReplaceId) || (Float.compare(mXOffset, mXOffset) != 0)) {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Integer.valueOf(mFlags), Integer.valueOf(mReplaceId), Float.valueOf(mXOffset) });
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("VolumeShaper.Operation{mFlags = 0x");
      localStringBuilder.append(Integer.toHexString(mFlags).toUpperCase());
      localStringBuilder.append(", mReplaceId = ");
      localStringBuilder.append(mReplaceId);
      localStringBuilder.append(", mXOffset = ");
      localStringBuilder.append(mXOffset);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mFlags);
      paramParcel.writeInt(mReplaceId);
      paramParcel.writeFloat(mXOffset);
    }
    
    public static final class Builder
    {
      int mFlags;
      int mReplaceId;
      float mXOffset;
      
      public Builder()
      {
        mFlags = 0;
        mReplaceId = -1;
        mXOffset = NaN.0F;
      }
      
      public Builder(VolumeShaper.Operation paramOperation)
      {
        mReplaceId = mReplaceId;
        mFlags = mFlags;
        mXOffset = mXOffset;
      }
      
      private Builder setFlags(int paramInt)
      {
        if ((paramInt & 0xFFFFFFFC) == 0)
        {
          mFlags = (mFlags & 0xFFFFFFFC | paramInt);
          return this;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("flag has unknown bits set: ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      
      public VolumeShaper.Operation build()
      {
        return new VolumeShaper.Operation(mFlags, mReplaceId, mXOffset, null);
      }
      
      public Builder createIfNeeded()
      {
        mFlags |= 0x10;
        return this;
      }
      
      public Builder defer()
      {
        mFlags |= 0x8;
        return this;
      }
      
      public Builder replace(int paramInt, boolean paramBoolean)
      {
        mReplaceId = paramInt;
        if (paramBoolean) {
          mFlags |= 0x4;
        } else {
          mFlags &= 0xFFFFFFFB;
        }
        return this;
      }
      
      public Builder reverse()
      {
        mFlags ^= 0x1;
        return this;
      }
      
      public Builder setXOffset(float paramFloat)
      {
        if (paramFloat >= 0.0F)
        {
          if (paramFloat <= 1.0F)
          {
            mXOffset = paramFloat;
            return this;
          }
          throw new IllegalArgumentException("xOffset > 1.f not allowed");
        }
        throw new IllegalArgumentException("Negative xOffset not allowed");
      }
      
      public Builder terminate()
      {
        mFlags |= 0x2;
        return this;
      }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Flag {}
  }
  
  public static final class State
    implements Parcelable
  {
    public static final Parcelable.Creator<State> CREATOR = new Parcelable.Creator()
    {
      public VolumeShaper.State createFromParcel(Parcel paramAnonymousParcel)
      {
        return new VolumeShaper.State(paramAnonymousParcel.readFloat(), paramAnonymousParcel.readFloat());
      }
      
      public VolumeShaper.State[] newArray(int paramAnonymousInt)
      {
        return new VolumeShaper.State[paramAnonymousInt];
      }
    };
    private float mVolume;
    private float mXOffset;
    
    State(float paramFloat1, float paramFloat2)
    {
      mVolume = paramFloat1;
      mXOffset = paramFloat2;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof State)) {
        return false;
      }
      boolean bool = true;
      if (paramObject == this) {
        return true;
      }
      paramObject = (State)paramObject;
      if ((mVolume != mVolume) || (mXOffset != mXOffset)) {
        bool = false;
      }
      return bool;
    }
    
    public float getVolume()
    {
      return mVolume;
    }
    
    public float getXOffset()
    {
      return mXOffset;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Float.valueOf(mVolume), Float.valueOf(mXOffset) });
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("VolumeShaper.State{mVolume = ");
      localStringBuilder.append(mVolume);
      localStringBuilder.append(", mXOffset = ");
      localStringBuilder.append(mXOffset);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeFloat(mVolume);
      paramParcel.writeFloat(mXOffset);
    }
  }
}
