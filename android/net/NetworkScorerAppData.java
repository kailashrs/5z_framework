package android.net;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class NetworkScorerAppData
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkScorerAppData> CREATOR = new Parcelable.Creator()
  {
    public NetworkScorerAppData createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkScorerAppData(paramAnonymousParcel);
    }
    
    public NetworkScorerAppData[] newArray(int paramAnonymousInt)
    {
      return new NetworkScorerAppData[paramAnonymousInt];
    }
  };
  private final ComponentName mEnableUseOpenWifiActivity;
  private final String mNetworkAvailableNotificationChannelId;
  private final ComponentName mRecommendationService;
  private final String mRecommendationServiceLabel;
  public final int packageUid;
  
  public NetworkScorerAppData(int paramInt, ComponentName paramComponentName1, String paramString1, ComponentName paramComponentName2, String paramString2)
  {
    packageUid = paramInt;
    mRecommendationService = paramComponentName1;
    mRecommendationServiceLabel = paramString1;
    mEnableUseOpenWifiActivity = paramComponentName2;
    mNetworkAvailableNotificationChannelId = paramString2;
  }
  
  protected NetworkScorerAppData(Parcel paramParcel)
  {
    packageUid = paramParcel.readInt();
    mRecommendationService = ComponentName.readFromParcel(paramParcel);
    mRecommendationServiceLabel = paramParcel.readString();
    mEnableUseOpenWifiActivity = ComponentName.readFromParcel(paramParcel);
    mNetworkAvailableNotificationChannelId = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (NetworkScorerAppData)paramObject;
      if ((packageUid != packageUid) || (!Objects.equals(mRecommendationService, mRecommendationService)) || (!Objects.equals(mRecommendationServiceLabel, mRecommendationServiceLabel)) || (!Objects.equals(mEnableUseOpenWifiActivity, mEnableUseOpenWifiActivity)) || (!Objects.equals(mNetworkAvailableNotificationChannelId, mNetworkAvailableNotificationChannelId))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public ComponentName getEnableUseOpenWifiActivity()
  {
    return mEnableUseOpenWifiActivity;
  }
  
  public String getNetworkAvailableNotificationChannelId()
  {
    return mNetworkAvailableNotificationChannelId;
  }
  
  public ComponentName getRecommendationServiceComponent()
  {
    return mRecommendationService;
  }
  
  public String getRecommendationServiceLabel()
  {
    return mRecommendationServiceLabel;
  }
  
  public String getRecommendationServicePackageName()
  {
    return mRecommendationService.getPackageName();
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(packageUid), mRecommendationService, mRecommendationServiceLabel, mEnableUseOpenWifiActivity, mNetworkAvailableNotificationChannelId });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NetworkScorerAppData{packageUid=");
    localStringBuilder.append(packageUid);
    localStringBuilder.append(", mRecommendationService=");
    localStringBuilder.append(mRecommendationService);
    localStringBuilder.append(", mRecommendationServiceLabel=");
    localStringBuilder.append(mRecommendationServiceLabel);
    localStringBuilder.append(", mEnableUseOpenWifiActivity=");
    localStringBuilder.append(mEnableUseOpenWifiActivity);
    localStringBuilder.append(", mNetworkAvailableNotificationChannelId=");
    localStringBuilder.append(mNetworkAvailableNotificationChannelId);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(packageUid);
    ComponentName.writeToParcel(mRecommendationService, paramParcel);
    paramParcel.writeString(mRecommendationServiceLabel);
    ComponentName.writeToParcel(mEnableUseOpenWifiActivity, paramParcel);
    paramParcel.writeString(mNetworkAvailableNotificationChannelId);
  }
}
