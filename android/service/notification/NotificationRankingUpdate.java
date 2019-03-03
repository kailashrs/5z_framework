package android.service.notification;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class NotificationRankingUpdate
  implements Parcelable
{
  public static final Parcelable.Creator<NotificationRankingUpdate> CREATOR = new Parcelable.Creator()
  {
    public NotificationRankingUpdate createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NotificationRankingUpdate(paramAnonymousParcel);
    }
    
    public NotificationRankingUpdate[] newArray(int paramAnonymousInt)
    {
      return new NotificationRankingUpdate[paramAnonymousInt];
    }
  };
  private final Bundle mChannels;
  private final Bundle mHidden;
  private final int[] mImportance;
  private final Bundle mImportanceExplanation;
  private final String[] mInterceptedKeys;
  private final String[] mKeys;
  private final Bundle mOverrideGroupKeys;
  private final Bundle mOverridePeople;
  private final Bundle mShowBadge;
  private final Bundle mSnoozeCriteria;
  private final Bundle mSuppressedVisualEffects;
  private final Bundle mUserSentiment;
  private final Bundle mVisibilityOverrides;
  
  public NotificationRankingUpdate(Parcel paramParcel)
  {
    mKeys = paramParcel.readStringArray();
    mInterceptedKeys = paramParcel.readStringArray();
    mVisibilityOverrides = paramParcel.readBundle();
    mSuppressedVisualEffects = paramParcel.readBundle();
    mImportance = new int[mKeys.length];
    paramParcel.readIntArray(mImportance);
    mImportanceExplanation = paramParcel.readBundle();
    mOverrideGroupKeys = paramParcel.readBundle();
    mChannels = paramParcel.readBundle();
    mOverridePeople = paramParcel.readBundle();
    mSnoozeCriteria = paramParcel.readBundle();
    mShowBadge = paramParcel.readBundle();
    mUserSentiment = paramParcel.readBundle();
    mHidden = paramParcel.readBundle();
  }
  
  public NotificationRankingUpdate(String[] paramArrayOfString1, String[] paramArrayOfString2, Bundle paramBundle1, Bundle paramBundle2, int[] paramArrayOfInt, Bundle paramBundle3, Bundle paramBundle4, Bundle paramBundle5, Bundle paramBundle6, Bundle paramBundle7, Bundle paramBundle8, Bundle paramBundle9, Bundle paramBundle10)
  {
    mKeys = paramArrayOfString1;
    mInterceptedKeys = paramArrayOfString2;
    mVisibilityOverrides = paramBundle1;
    mSuppressedVisualEffects = paramBundle2;
    mImportance = paramArrayOfInt;
    mImportanceExplanation = paramBundle3;
    mOverrideGroupKeys = paramBundle4;
    mChannels = paramBundle5;
    mOverridePeople = paramBundle6;
    mSnoozeCriteria = paramBundle7;
    mShowBadge = paramBundle8;
    mUserSentiment = paramBundle9;
    mHidden = paramBundle10;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Bundle getChannels()
  {
    return mChannels;
  }
  
  public Bundle getHidden()
  {
    return mHidden;
  }
  
  public int[] getImportance()
  {
    return mImportance;
  }
  
  public Bundle getImportanceExplanation()
  {
    return mImportanceExplanation;
  }
  
  public String[] getInterceptedKeys()
  {
    return mInterceptedKeys;
  }
  
  public String[] getOrderedKeys()
  {
    return mKeys;
  }
  
  public Bundle getOverrideGroupKeys()
  {
    return mOverrideGroupKeys;
  }
  
  public Bundle getOverridePeople()
  {
    return mOverridePeople;
  }
  
  public Bundle getShowBadge()
  {
    return mShowBadge;
  }
  
  public Bundle getSnoozeCriteria()
  {
    return mSnoozeCriteria;
  }
  
  public Bundle getSuppressedVisualEffects()
  {
    return mSuppressedVisualEffects;
  }
  
  public Bundle getUserSentiment()
  {
    return mUserSentiment;
  }
  
  public Bundle getVisibilityOverrides()
  {
    return mVisibilityOverrides;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStringArray(mKeys);
    paramParcel.writeStringArray(mInterceptedKeys);
    paramParcel.writeBundle(mVisibilityOverrides);
    paramParcel.writeBundle(mSuppressedVisualEffects);
    paramParcel.writeIntArray(mImportance);
    paramParcel.writeBundle(mImportanceExplanation);
    paramParcel.writeBundle(mOverrideGroupKeys);
    paramParcel.writeBundle(mChannels);
    paramParcel.writeBundle(mOverridePeople);
    paramParcel.writeBundle(mSnoozeCriteria);
    paramParcel.writeBundle(mShowBadge);
    paramParcel.writeBundle(mUserSentiment);
    paramParcel.writeBundle(mHidden);
  }
}
