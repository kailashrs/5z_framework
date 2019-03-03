package android.service.euicc;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.euicc.DownloadableSubscription;
import java.util.Arrays;
import java.util.List;

@SystemApi
public final class GetDefaultDownloadableSubscriptionListResult
  implements Parcelable
{
  public static final Parcelable.Creator<GetDefaultDownloadableSubscriptionListResult> CREATOR = new Parcelable.Creator()
  {
    public GetDefaultDownloadableSubscriptionListResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new GetDefaultDownloadableSubscriptionListResult(paramAnonymousParcel, null);
    }
    
    public GetDefaultDownloadableSubscriptionListResult[] newArray(int paramAnonymousInt)
    {
      return new GetDefaultDownloadableSubscriptionListResult[paramAnonymousInt];
    }
  };
  private final DownloadableSubscription[] mSubscriptions;
  @Deprecated
  public final int result;
  
  public GetDefaultDownloadableSubscriptionListResult(int paramInt, DownloadableSubscription[] paramArrayOfDownloadableSubscription)
  {
    result = paramInt;
    if (result == 0)
    {
      mSubscriptions = paramArrayOfDownloadableSubscription;
    }
    else
    {
      if (paramArrayOfDownloadableSubscription != null) {
        break label34;
      }
      mSubscriptions = null;
    }
    return;
    label34:
    paramArrayOfDownloadableSubscription = new StringBuilder();
    paramArrayOfDownloadableSubscription.append("Error result with non-null subscriptions: ");
    paramArrayOfDownloadableSubscription.append(paramInt);
    throw new IllegalArgumentException(paramArrayOfDownloadableSubscription.toString());
  }
  
  private GetDefaultDownloadableSubscriptionListResult(Parcel paramParcel)
  {
    result = paramParcel.readInt();
    mSubscriptions = ((DownloadableSubscription[])paramParcel.createTypedArray(DownloadableSubscription.CREATOR));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<DownloadableSubscription> getDownloadableSubscriptions()
  {
    if (mSubscriptions == null) {
      return null;
    }
    return Arrays.asList(mSubscriptions);
  }
  
  public int getResult()
  {
    return result;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(result);
    paramParcel.writeTypedArray(mSubscriptions, paramInt);
  }
}
