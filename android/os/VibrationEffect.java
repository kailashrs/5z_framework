package android.os;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.MathUtils;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Arrays;

public abstract class VibrationEffect
  implements Parcelable
{
  public static final Parcelable.Creator<VibrationEffect> CREATOR = new Parcelable.Creator()
  {
    public VibrationEffect createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      if (i == 1) {
        return new VibrationEffect.OneShot(paramAnonymousParcel);
      }
      if (i == 2) {
        return new VibrationEffect.Waveform(paramAnonymousParcel);
      }
      if (i == 3) {
        return new VibrationEffect.Prebaked(paramAnonymousParcel);
      }
      throw new IllegalStateException("Unexpected vibration event type token in parcel.");
    }
    
    public VibrationEffect[] newArray(int paramAnonymousInt)
    {
      return new VibrationEffect[paramAnonymousInt];
    }
  };
  public static final int DEFAULT_AMPLITUDE = -1;
  public static final int EFFECT_CLICK = 0;
  public static final int EFFECT_DOUBLE_CLICK = 1;
  public static final int EFFECT_HEAVY_CLICK = 5;
  public static final int EFFECT_POP = 4;
  public static final int EFFECT_THUD = 3;
  public static final int EFFECT_TICK = 2;
  public static final int MAX_AMPLITUDE = 255;
  private static final int PARCEL_TOKEN_EFFECT = 3;
  private static final int PARCEL_TOKEN_ONE_SHOT = 1;
  private static final int PARCEL_TOKEN_WAVEFORM = 2;
  @VisibleForTesting
  public static final int[] RINGTONES = { 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
  
  public VibrationEffect() {}
  
  public static VibrationEffect createOneShot(long paramLong, int paramInt)
  {
    OneShot localOneShot = new OneShot(paramLong, paramInt);
    localOneShot.validate();
    return localOneShot;
  }
  
  public static VibrationEffect createWaveform(long[] paramArrayOfLong, int paramInt)
  {
    int[] arrayOfInt = new int[paramArrayOfLong.length];
    for (int i = 0; i < paramArrayOfLong.length / 2; i++) {
      arrayOfInt[(i * 2 + 1)] = -1;
    }
    return createWaveform(paramArrayOfLong, arrayOfInt, paramInt);
  }
  
  public static VibrationEffect createWaveform(long[] paramArrayOfLong, int[] paramArrayOfInt, int paramInt)
  {
    paramArrayOfLong = new Waveform(paramArrayOfLong, paramArrayOfInt, paramInt);
    paramArrayOfLong.validate();
    return paramArrayOfLong;
  }
  
  public static VibrationEffect get(int paramInt)
  {
    return get(paramInt, true);
  }
  
  public static VibrationEffect get(int paramInt, boolean paramBoolean)
  {
    Prebaked localPrebaked = new Prebaked(paramInt, paramBoolean);
    localPrebaked.validate();
    return localPrebaked;
  }
  
  public static VibrationEffect get(Uri paramUri, Context paramContext)
  {
    String[] arrayOfString = paramContext.getResources().getStringArray(17236033);
    for (int i = 0; (i < arrayOfString.length) && (i < RINGTONES.length); i++) {
      if (arrayOfString[i] != null)
      {
        Uri localUri = paramContext.getContentResolver().uncanonicalize(Uri.parse(arrayOfString[i]));
        if ((localUri != null) && (localUri.equals(paramUri))) {
          return get(RINGTONES[i]);
        }
      }
    }
    return null;
  }
  
  protected static int scale(int paramInt1, float paramFloat, int paramInt2)
  {
    paramFloat = MathUtils.pow(paramInt1 / 255.0F, paramFloat);
    return (int)(paramInt2 * paramFloat);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public abstract long getDuration();
  
  public abstract void validate();
  
  public static class OneShot
    extends VibrationEffect
    implements Parcelable
  {
    public static final Parcelable.Creator<OneShot> CREATOR = new Parcelable.Creator()
    {
      public VibrationEffect.OneShot createFromParcel(Parcel paramAnonymousParcel)
      {
        paramAnonymousParcel.readInt();
        return new VibrationEffect.OneShot(paramAnonymousParcel);
      }
      
      public VibrationEffect.OneShot[] newArray(int paramAnonymousInt)
      {
        return new VibrationEffect.OneShot[paramAnonymousInt];
      }
    };
    private final int mAmplitude;
    private final long mDuration;
    
    public OneShot(long paramLong, int paramInt)
    {
      mDuration = paramLong;
      mAmplitude = paramInt;
    }
    
    public OneShot(Parcel paramParcel)
    {
      mDuration = paramParcel.readLong();
      mAmplitude = paramParcel.readInt();
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof OneShot;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramObject = (OneShot)paramObject;
      bool1 = bool2;
      if (mDuration == mDuration)
      {
        bool1 = bool2;
        if (mAmplitude == mAmplitude) {
          bool1 = true;
        }
      }
      return bool1;
    }
    
    public int getAmplitude()
    {
      return mAmplitude;
    }
    
    public long getDuration()
    {
      return mDuration;
    }
    
    public int hashCode()
    {
      return 17 + (int)mDuration * 37 + 37 * mAmplitude;
    }
    
    public OneShot resolve(int paramInt)
    {
      if ((paramInt <= 255) && (paramInt >= 0))
      {
        if (mAmplitude == -1) {
          return new OneShot(mDuration, paramInt);
        }
        return this;
      }
      throw new IllegalArgumentException("Amplitude is negative or greater than MAX_AMPLITUDE");
    }
    
    public VibrationEffect scale(float paramFloat, int paramInt)
    {
      paramInt = scale(mAmplitude, paramFloat, paramInt);
      return new OneShot(mDuration, paramInt);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("OneShot{mDuration=");
      localStringBuilder.append(mDuration);
      localStringBuilder.append(", mAmplitude=");
      localStringBuilder.append(mAmplitude);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void validate()
    {
      if ((mAmplitude >= -1) && (mAmplitude != 0) && (mAmplitude <= 255))
      {
        if (mDuration > 0L) {
          return;
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("duration must be positive (duration=");
        localStringBuilder.append(mDuration);
        localStringBuilder.append(")");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("amplitude must either be DEFAULT_AMPLITUDE, or between 1 and 255 inclusive (amplitude=");
      localStringBuilder.append(mAmplitude);
      localStringBuilder.append(")");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(1);
      paramParcel.writeLong(mDuration);
      paramParcel.writeInt(mAmplitude);
    }
  }
  
  public static class Prebaked
    extends VibrationEffect
    implements Parcelable
  {
    public static final Parcelable.Creator<Prebaked> CREATOR = new Parcelable.Creator()
    {
      public VibrationEffect.Prebaked createFromParcel(Parcel paramAnonymousParcel)
      {
        paramAnonymousParcel.readInt();
        return new VibrationEffect.Prebaked(paramAnonymousParcel);
      }
      
      public VibrationEffect.Prebaked[] newArray(int paramAnonymousInt)
      {
        return new VibrationEffect.Prebaked[paramAnonymousInt];
      }
    };
    private final int mEffectId;
    private int mEffectStrength;
    private final boolean mFallback;
    
    public Prebaked(int paramInt, boolean paramBoolean)
    {
      mEffectId = paramInt;
      mFallback = paramBoolean;
      mEffectStrength = 1;
    }
    
    public Prebaked(Parcel paramParcel)
    {
      this(i, bool);
      mEffectStrength = paramParcel.readInt();
    }
    
    private static boolean isValidEffectStrength(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return false;
      }
      return true;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof Prebaked;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramObject = (Prebaked)paramObject;
      bool1 = bool2;
      if (mEffectId == mEffectId)
      {
        bool1 = bool2;
        if (mFallback == mFallback)
        {
          bool1 = bool2;
          if (mEffectStrength == mEffectStrength) {
            bool1 = true;
          }
        }
      }
      return bool1;
    }
    
    public long getDuration()
    {
      return -1L;
    }
    
    public int getEffectStrength()
    {
      return mEffectStrength;
    }
    
    public int getId()
    {
      return mEffectId;
    }
    
    public int hashCode()
    {
      return 17 + mEffectId * 37 + 37 * mEffectStrength;
    }
    
    public void setEffectStrength(int paramInt)
    {
      if (isValidEffectStrength(paramInt))
      {
        mEffectStrength = paramInt;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid effect strength: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public boolean shouldFallback()
    {
      return mFallback;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Prebaked{mEffectId=");
      localStringBuilder.append(mEffectId);
      localStringBuilder.append(", mEffectStrength=");
      localStringBuilder.append(mEffectStrength);
      localStringBuilder.append(", mFallback=");
      localStringBuilder.append(mFallback);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void validate()
    {
      switch (mEffectId)
      {
      default: 
        if ((mEffectId < RINGTONES[0]) || (mEffectId > RINGTONES[(RINGTONES.length - 1)])) {
          break label130;
        }
        break;
      }
      if (isValidEffectStrength(mEffectStrength)) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown prebaked effect strength (value=");
      localStringBuilder.append(mEffectStrength);
      localStringBuilder.append(")");
      throw new IllegalArgumentException(localStringBuilder.toString());
      label130:
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown prebaked effect type (value=");
      localStringBuilder.append(mEffectId);
      localStringBuilder.append(")");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(3);
      paramParcel.writeInt(mEffectId);
      paramParcel.writeByte((byte)mFallback);
      paramParcel.writeInt(mEffectStrength);
    }
  }
  
  public static class Waveform
    extends VibrationEffect
    implements Parcelable
  {
    public static final Parcelable.Creator<Waveform> CREATOR = new Parcelable.Creator()
    {
      public VibrationEffect.Waveform createFromParcel(Parcel paramAnonymousParcel)
      {
        paramAnonymousParcel.readInt();
        return new VibrationEffect.Waveform(paramAnonymousParcel);
      }
      
      public VibrationEffect.Waveform[] newArray(int paramAnonymousInt)
      {
        return new VibrationEffect.Waveform[paramAnonymousInt];
      }
    };
    private final int[] mAmplitudes;
    private final int mRepeat;
    private final long[] mTimings;
    
    public Waveform(Parcel paramParcel)
    {
      this(paramParcel.createLongArray(), paramParcel.createIntArray(), paramParcel.readInt());
    }
    
    public Waveform(long[] paramArrayOfLong, int[] paramArrayOfInt, int paramInt)
    {
      mTimings = new long[paramArrayOfLong.length];
      System.arraycopy(paramArrayOfLong, 0, mTimings, 0, paramArrayOfLong.length);
      mAmplitudes = new int[paramArrayOfInt.length];
      System.arraycopy(paramArrayOfInt, 0, mAmplitudes, 0, paramArrayOfInt.length);
      mRepeat = paramInt;
    }
    
    private static boolean hasNonZeroEntry(long[] paramArrayOfLong)
    {
      int i = paramArrayOfLong.length;
      for (int j = 0; j < i; j++) {
        if (paramArrayOfLong[j] != 0L) {
          return true;
        }
      }
      return false;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof Waveform;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramObject = (Waveform)paramObject;
      bool1 = bool2;
      if (Arrays.equals(mTimings, mTimings))
      {
        bool1 = bool2;
        if (Arrays.equals(mAmplitudes, mAmplitudes))
        {
          bool1 = bool2;
          if (mRepeat == mRepeat) {
            bool1 = true;
          }
        }
      }
      return bool1;
    }
    
    public int[] getAmplitudes()
    {
      return mAmplitudes;
    }
    
    public long getDuration()
    {
      if (mRepeat >= 0) {
        return Long.MAX_VALUE;
      }
      long l = 0L;
      long[] arrayOfLong = mTimings;
      int i = arrayOfLong.length;
      for (int j = 0; j < i; j++) {
        l += arrayOfLong[j];
      }
      return l;
    }
    
    public int getRepeatIndex()
    {
      return mRepeat;
    }
    
    public long[] getTimings()
    {
      return mTimings;
    }
    
    public int hashCode()
    {
      return 17 + Arrays.hashCode(mTimings) * 37 + Arrays.hashCode(mAmplitudes) * 37 + 37 * mRepeat;
    }
    
    public Waveform resolve(int paramInt)
    {
      if ((paramInt <= 255) && (paramInt >= 0))
      {
        int[] arrayOfInt = Arrays.copyOf(mAmplitudes, mAmplitudes.length);
        for (int i = 0; i < arrayOfInt.length; i++) {
          if (arrayOfInt[i] == -1) {
            arrayOfInt[i] = paramInt;
          }
        }
        return new Waveform(mTimings, arrayOfInt, mRepeat);
      }
      throw new IllegalArgumentException("Amplitude is negative or greater than MAX_AMPLITUDE");
    }
    
    public VibrationEffect scale(float paramFloat, int paramInt)
    {
      if ((paramFloat == 1.0F) && (paramInt == 255)) {
        return new Waveform(mTimings, mAmplitudes, mRepeat);
      }
      int[] arrayOfInt = Arrays.copyOf(mAmplitudes, mAmplitudes.length);
      for (int i = 0; i < arrayOfInt.length; i++) {
        arrayOfInt[i] = scale(arrayOfInt[i], paramFloat, paramInt);
      }
      return new Waveform(mTimings, arrayOfInt, mRepeat);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Waveform{mTimings=");
      localStringBuilder.append(Arrays.toString(mTimings));
      localStringBuilder.append(", mAmplitudes=");
      localStringBuilder.append(Arrays.toString(mAmplitudes));
      localStringBuilder.append(", mRepeat=");
      localStringBuilder.append(mRepeat);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void validate()
    {
      if (mTimings.length == mAmplitudes.length)
      {
        if (hasNonZeroEntry(mTimings))
        {
          localObject = mTimings;
          int i = localObject.length;
          int j = 0;
          int k = 0;
          while (k < i) {
            if (localObject[k] >= 0L)
            {
              k++;
            }
            else
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("timings must all be >= 0 (timings=");
              ((StringBuilder)localObject).append(Arrays.toString(mTimings));
              ((StringBuilder)localObject).append(")");
              throw new IllegalArgumentException(((StringBuilder)localObject).toString());
            }
          }
          localObject = mAmplitudes;
          i = localObject.length;
          k = j;
          while (k < i)
          {
            j = localObject[k];
            if ((j >= -1) && (j <= 255))
            {
              k++;
            }
            else
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("amplitudes must all be DEFAULT_AMPLITUDE or between 0 and 255 (amplitudes=");
              ((StringBuilder)localObject).append(Arrays.toString(mAmplitudes));
              ((StringBuilder)localObject).append(")");
              throw new IllegalArgumentException(((StringBuilder)localObject).toString());
            }
          }
          if ((mRepeat >= -1) && (mRepeat < mTimings.length)) {
            return;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("repeat index must be within the bounds of the timings array (timings.length=");
          ((StringBuilder)localObject).append(mTimings.length);
          ((StringBuilder)localObject).append(", index=");
          ((StringBuilder)localObject).append(mRepeat);
          ((StringBuilder)localObject).append(")");
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("at least one timing must be non-zero (timings=");
        ((StringBuilder)localObject).append(Arrays.toString(mTimings));
        ((StringBuilder)localObject).append(")");
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("timing and amplitude arrays must be of equal length (timings.length=");
      ((StringBuilder)localObject).append(mTimings.length);
      ((StringBuilder)localObject).append(", amplitudes.length=");
      ((StringBuilder)localObject).append(mAmplitudes.length);
      ((StringBuilder)localObject).append(")");
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(2);
      paramParcel.writeLongArray(mTimings);
      paramParcel.writeIntArray(mAmplitudes);
      paramParcel.writeInt(mRepeat);
    }
  }
}
