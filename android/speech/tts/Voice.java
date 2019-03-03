package android.speech.tts;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Voice
  implements Parcelable
{
  public static final Parcelable.Creator<Voice> CREATOR = new Parcelable.Creator()
  {
    public Voice createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Voice(paramAnonymousParcel, null);
    }
    
    public Voice[] newArray(int paramAnonymousInt)
    {
      return new Voice[paramAnonymousInt];
    }
  };
  public static final int LATENCY_HIGH = 400;
  public static final int LATENCY_LOW = 200;
  public static final int LATENCY_NORMAL = 300;
  public static final int LATENCY_VERY_HIGH = 500;
  public static final int LATENCY_VERY_LOW = 100;
  public static final int QUALITY_HIGH = 400;
  public static final int QUALITY_LOW = 200;
  public static final int QUALITY_NORMAL = 300;
  public static final int QUALITY_VERY_HIGH = 500;
  public static final int QUALITY_VERY_LOW = 100;
  private final Set<String> mFeatures;
  private final int mLatency;
  private final Locale mLocale;
  private final String mName;
  private final int mQuality;
  private final boolean mRequiresNetworkConnection;
  
  private Voice(Parcel paramParcel)
  {
    mName = paramParcel.readString();
    mLocale = ((Locale)paramParcel.readSerializable());
    mQuality = paramParcel.readInt();
    mLatency = paramParcel.readInt();
    int i = paramParcel.readByte();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mRequiresNetworkConnection = bool;
    mFeatures = new HashSet();
    Collections.addAll(mFeatures, paramParcel.readStringArray());
  }
  
  public Voice(String paramString, Locale paramLocale, int paramInt1, int paramInt2, boolean paramBoolean, Set<String> paramSet)
  {
    mName = paramString;
    mLocale = paramLocale;
    mQuality = paramInt1;
    mLatency = paramInt2;
    mRequiresNetworkConnection = paramBoolean;
    mFeatures = paramSet;
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
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (Voice)paramObject;
    if (mFeatures == null)
    {
      if (mFeatures != null) {
        return false;
      }
    }
    else if (!mFeatures.equals(mFeatures)) {
      return false;
    }
    if (mLatency != mLatency) {
      return false;
    }
    if (mLocale == null)
    {
      if (mLocale != null) {
        return false;
      }
    }
    else if (!mLocale.equals(mLocale)) {
      return false;
    }
    if (mName == null)
    {
      if (mName != null) {
        return false;
      }
    }
    else if (!mName.equals(mName)) {
      return false;
    }
    if (mQuality != mQuality) {
      return false;
    }
    return mRequiresNetworkConnection == mRequiresNetworkConnection;
  }
  
  public Set<String> getFeatures()
  {
    return mFeatures;
  }
  
  public int getLatency()
  {
    return mLatency;
  }
  
  public Locale getLocale()
  {
    return mLocale;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getQuality()
  {
    return mQuality;
  }
  
  public int hashCode()
  {
    Set localSet = mFeatures;
    int i = 0;
    int j;
    if (localSet == null) {
      j = 0;
    } else {
      j = mFeatures.hashCode();
    }
    int k = mLatency;
    int m;
    if (mLocale == null) {
      m = 0;
    } else {
      m = mLocale.hashCode();
    }
    if (mName != null) {
      i = mName.hashCode();
    }
    int n = mQuality;
    int i1;
    if (mRequiresNetworkConnection) {
      i1 = 1231;
    } else {
      i1 = 1237;
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * 1 + j) + k) + m) + i) + n) + i1;
  }
  
  public boolean isNetworkConnectionRequired()
  {
    return mRequiresNetworkConnection;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(64);
    localStringBuilder.append("Voice[Name: ");
    localStringBuilder.append(mName);
    localStringBuilder.append(", locale: ");
    localStringBuilder.append(mLocale);
    localStringBuilder.append(", quality: ");
    localStringBuilder.append(mQuality);
    localStringBuilder.append(", latency: ");
    localStringBuilder.append(mLatency);
    localStringBuilder.append(", requiresNetwork: ");
    localStringBuilder.append(mRequiresNetworkConnection);
    localStringBuilder.append(", features: ");
    localStringBuilder.append(mFeatures.toString());
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mName);
    paramParcel.writeSerializable(mLocale);
    paramParcel.writeInt(mQuality);
    paramParcel.writeInt(mLatency);
    paramParcel.writeByte((byte)mRequiresNetworkConnection);
    paramParcel.writeStringList(new ArrayList(mFeatures));
  }
}
