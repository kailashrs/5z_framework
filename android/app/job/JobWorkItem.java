package android.app.job;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class JobWorkItem
  implements Parcelable
{
  public static final Parcelable.Creator<JobWorkItem> CREATOR = new Parcelable.Creator()
  {
    public JobWorkItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new JobWorkItem(paramAnonymousParcel);
    }
    
    public JobWorkItem[] newArray(int paramAnonymousInt)
    {
      return new JobWorkItem[paramAnonymousInt];
    }
  };
  int mDeliveryCount;
  Object mGrants;
  final Intent mIntent;
  final long mNetworkDownloadBytes;
  final long mNetworkUploadBytes;
  int mWorkId;
  
  public JobWorkItem(Intent paramIntent)
  {
    mIntent = paramIntent;
    mNetworkDownloadBytes = -1L;
    mNetworkUploadBytes = -1L;
  }
  
  @Deprecated
  public JobWorkItem(Intent paramIntent, long paramLong)
  {
    this(paramIntent, paramLong, -1L);
  }
  
  public JobWorkItem(Intent paramIntent, long paramLong1, long paramLong2)
  {
    mIntent = paramIntent;
    mNetworkDownloadBytes = paramLong1;
    mNetworkUploadBytes = paramLong2;
  }
  
  JobWorkItem(Parcel paramParcel)
  {
    if (paramParcel.readInt() != 0) {
      mIntent = ((Intent)Intent.CREATOR.createFromParcel(paramParcel));
    } else {
      mIntent = null;
    }
    mNetworkDownloadBytes = paramParcel.readLong();
    mNetworkUploadBytes = paramParcel.readLong();
    mDeliveryCount = paramParcel.readInt();
    mWorkId = paramParcel.readInt();
  }
  
  public void bumpDeliveryCount()
  {
    mDeliveryCount += 1;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getDeliveryCount()
  {
    return mDeliveryCount;
  }
  
  @Deprecated
  public long getEstimatedNetworkBytes()
  {
    if ((mNetworkDownloadBytes == -1L) && (mNetworkUploadBytes == -1L)) {
      return -1L;
    }
    if (mNetworkDownloadBytes == -1L) {
      return mNetworkUploadBytes;
    }
    if (mNetworkUploadBytes == -1L) {
      return mNetworkDownloadBytes;
    }
    return mNetworkDownloadBytes + mNetworkUploadBytes;
  }
  
  public long getEstimatedNetworkDownloadBytes()
  {
    return mNetworkDownloadBytes;
  }
  
  public long getEstimatedNetworkUploadBytes()
  {
    return mNetworkUploadBytes;
  }
  
  public Object getGrants()
  {
    return mGrants;
  }
  
  public Intent getIntent()
  {
    return mIntent;
  }
  
  public int getWorkId()
  {
    return mWorkId;
  }
  
  public void setGrants(Object paramObject)
  {
    mGrants = paramObject;
  }
  
  public void setWorkId(int paramInt)
  {
    mWorkId = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(64);
    localStringBuilder.append("JobWorkItem{id=");
    localStringBuilder.append(mWorkId);
    localStringBuilder.append(" intent=");
    localStringBuilder.append(mIntent);
    if (mNetworkDownloadBytes != -1L)
    {
      localStringBuilder.append(" downloadBytes=");
      localStringBuilder.append(mNetworkDownloadBytes);
    }
    if (mNetworkUploadBytes != -1L)
    {
      localStringBuilder.append(" uploadBytes=");
      localStringBuilder.append(mNetworkUploadBytes);
    }
    if (mDeliveryCount != 0)
    {
      localStringBuilder.append(" dcount=");
      localStringBuilder.append(mDeliveryCount);
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mIntent != null)
    {
      paramParcel.writeInt(1);
      mIntent.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeLong(mNetworkDownloadBytes);
    paramParcel.writeLong(mNetworkUploadBytes);
    paramParcel.writeInt(mDeliveryCount);
    paramParcel.writeInt(mWorkId);
  }
}
