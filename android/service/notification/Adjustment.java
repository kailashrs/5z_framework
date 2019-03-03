package android.service.notification;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class Adjustment
  implements Parcelable
{
  public static final Parcelable.Creator<Adjustment> CREATOR = new Parcelable.Creator()
  {
    public Adjustment createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Adjustment(paramAnonymousParcel);
    }
    
    public Adjustment[] newArray(int paramAnonymousInt)
    {
      return new Adjustment[paramAnonymousInt];
    }
  };
  public static final String KEY_GROUP_KEY = "key_group_key";
  public static final String KEY_PEOPLE = "key_people";
  public static final String KEY_SNOOZE_CRITERIA = "key_snooze_criteria";
  public static final String KEY_USER_SENTIMENT = "key_user_sentiment";
  private final CharSequence mExplanation;
  private final String mKey;
  private final String mPackage;
  private final Bundle mSignals;
  private final int mUser;
  
  protected Adjustment(Parcel paramParcel)
  {
    if (paramParcel.readInt() == 1) {
      mPackage = paramParcel.readString();
    } else {
      mPackage = null;
    }
    if (paramParcel.readInt() == 1) {
      mKey = paramParcel.readString();
    } else {
      mKey = null;
    }
    if (paramParcel.readInt() == 1) {
      mExplanation = paramParcel.readCharSequence();
    } else {
      mExplanation = null;
    }
    mSignals = paramParcel.readBundle();
    mUser = paramParcel.readInt();
  }
  
  public Adjustment(String paramString1, String paramString2, Bundle paramBundle, CharSequence paramCharSequence, int paramInt)
  {
    mPackage = paramString1;
    mKey = paramString2;
    mSignals = paramBundle;
    mExplanation = paramCharSequence;
    mUser = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public CharSequence getExplanation()
  {
    return mExplanation;
  }
  
  public String getKey()
  {
    return mKey;
  }
  
  public String getPackage()
  {
    return mPackage;
  }
  
  public Bundle getSignals()
  {
    return mSignals;
  }
  
  public int getUser()
  {
    return mUser;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Adjustment{mSignals=");
    localStringBuilder.append(mSignals);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mPackage != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(mPackage);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mKey != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(mKey);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mExplanation != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeCharSequence(mExplanation);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeBundle(mSignals);
    paramParcel.writeInt(mUser);
  }
}
