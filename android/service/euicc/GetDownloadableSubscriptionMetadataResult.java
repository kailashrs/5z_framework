package android.service.euicc;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.euicc.DownloadableSubscription;

@SystemApi
public final class GetDownloadableSubscriptionMetadataResult
  implements Parcelable
{
  public static final Parcelable.Creator<GetDownloadableSubscriptionMetadataResult> CREATOR = new Parcelable.Creator()
  {
    public GetDownloadableSubscriptionMetadataResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new GetDownloadableSubscriptionMetadataResult(paramAnonymousParcel, null);
    }
    
    public GetDownloadableSubscriptionMetadataResult[] newArray(int paramAnonymousInt)
    {
      return new GetDownloadableSubscriptionMetadataResult[paramAnonymousInt];
    }
  };
  private final DownloadableSubscription mSubscription;
  @Deprecated
  public final int result;
  
  public GetDownloadableSubscriptionMetadataResult(int paramInt, DownloadableSubscription paramDownloadableSubscription)
  {
    result = paramInt;
    if (result == 0)
    {
      mSubscription = paramDownloadableSubscription;
    }
    else
    {
      if (paramDownloadableSubscription != null) {
        break label34;
      }
      mSubscription = null;
    }
    return;
    label34:
    paramDownloadableSubscription = new StringBuilder();
    paramDownloadableSubscription.append("Error result with non-null subscription: ");
    paramDownloadableSubscription.append(paramInt);
    throw new IllegalArgumentException(paramDownloadableSubscription.toString());
  }
  
  private GetDownloadableSubscriptionMetadataResult(Parcel paramParcel)
  {
    result = paramParcel.readInt();
    mSubscription = ((DownloadableSubscription)paramParcel.readTypedObject(DownloadableSubscription.CREATOR));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public DownloadableSubscription getDownloadableSubscription()
  {
    return mSubscription;
  }
  
  public int getResult()
  {
    return result;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(result);
    paramParcel.writeTypedObject(mSubscription, paramInt);
  }
}
