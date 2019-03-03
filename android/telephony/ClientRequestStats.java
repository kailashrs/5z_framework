package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ClientRequestStats
  implements Parcelable
{
  public static final Parcelable.Creator<ClientRequestStats> CREATOR = new Parcelable.Creator()
  {
    public ClientRequestStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ClientRequestStats(paramAnonymousParcel);
    }
    
    public ClientRequestStats[] newArray(int paramAnonymousInt)
    {
      return new ClientRequestStats[paramAnonymousInt];
    }
  };
  private static final int REQUEST_HISTOGRAM_BUCKET_COUNT = 5;
  private String mCallingPackage;
  private long mCompletedRequestsCount = 0L;
  private long mCompletedRequestsWakelockTime = 0L;
  private long mPendingRequestsCount = 0L;
  private long mPendingRequestsWakelockTime = 0L;
  private SparseArray<TelephonyHistogram> mRequestHistograms = new SparseArray();
  
  public ClientRequestStats() {}
  
  public ClientRequestStats(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public ClientRequestStats(ClientRequestStats paramClientRequestStats)
  {
    mCallingPackage = paramClientRequestStats.getCallingPackage();
    mCompletedRequestsCount = paramClientRequestStats.getCompletedRequestsCount();
    mCompletedRequestsWakelockTime = paramClientRequestStats.getCompletedRequestsWakelockTime();
    mPendingRequestsCount = paramClientRequestStats.getPendingRequestsCount();
    mPendingRequestsWakelockTime = paramClientRequestStats.getPendingRequestsWakelockTime();
    Iterator localIterator = paramClientRequestStats.getRequestHistograms().iterator();
    while (localIterator.hasNext())
    {
      paramClientRequestStats = (TelephonyHistogram)localIterator.next();
      mRequestHistograms.put(paramClientRequestStats.getId(), paramClientRequestStats);
    }
  }
  
  public void addCompletedWakelockTime(long paramLong)
  {
    mCompletedRequestsWakelockTime += paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getCallingPackage()
  {
    return mCallingPackage;
  }
  
  public long getCompletedRequestsCount()
  {
    return mCompletedRequestsCount;
  }
  
  public long getCompletedRequestsWakelockTime()
  {
    return mCompletedRequestsWakelockTime;
  }
  
  public long getPendingRequestsCount()
  {
    return mPendingRequestsCount;
  }
  
  public long getPendingRequestsWakelockTime()
  {
    return mPendingRequestsWakelockTime;
  }
  
  public List<TelephonyHistogram> getRequestHistograms()
  {
    synchronized (mRequestHistograms)
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>(mRequestHistograms.size());
      for (int i = 0; i < mRequestHistograms.size(); i++)
      {
        TelephonyHistogram localTelephonyHistogram = new android/telephony/TelephonyHistogram;
        localTelephonyHistogram.<init>((TelephonyHistogram)mRequestHistograms.valueAt(i));
        localArrayList.add(localTelephonyHistogram);
      }
      return localArrayList;
    }
  }
  
  public void incrementCompletedRequestsCount()
  {
    mCompletedRequestsCount += 1L;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mCallingPackage = paramParcel.readString();
    mCompletedRequestsWakelockTime = paramParcel.readLong();
    mCompletedRequestsCount = paramParcel.readLong();
    mPendingRequestsWakelockTime = paramParcel.readLong();
    mPendingRequestsCount = paramParcel.readLong();
    Object localObject = new ArrayList();
    paramParcel.readTypedList((List)localObject, TelephonyHistogram.CREATOR);
    localObject = ((ArrayList)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      paramParcel = (TelephonyHistogram)((Iterator)localObject).next();
      mRequestHistograms.put(paramParcel.getId(), paramParcel);
    }
  }
  
  public void setCallingPackage(String paramString)
  {
    mCallingPackage = paramString;
  }
  
  public void setPendingRequestsCount(long paramLong)
  {
    mPendingRequestsCount = paramLong;
  }
  
  public void setPendingRequestsWakelockTime(long paramLong)
  {
    mPendingRequestsWakelockTime = paramLong;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ClientRequestStats{mCallingPackage='");
    localStringBuilder.append(mCallingPackage);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mCompletedRequestsWakelockTime=");
    localStringBuilder.append(mCompletedRequestsWakelockTime);
    localStringBuilder.append(", mCompletedRequestsCount=");
    localStringBuilder.append(mCompletedRequestsCount);
    localStringBuilder.append(", mPendingRequestsWakelockTime=");
    localStringBuilder.append(mPendingRequestsWakelockTime);
    localStringBuilder.append(", mPendingRequestsCount=");
    localStringBuilder.append(mPendingRequestsCount);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void updateRequestHistograms(int paramInt1, int paramInt2)
  {
    synchronized (mRequestHistograms)
    {
      TelephonyHistogram localTelephonyHistogram1 = (TelephonyHistogram)mRequestHistograms.get(paramInt1);
      TelephonyHistogram localTelephonyHistogram2 = localTelephonyHistogram1;
      if (localTelephonyHistogram1 == null)
      {
        localTelephonyHistogram2 = new android/telephony/TelephonyHistogram;
        localTelephonyHistogram2.<init>(1, paramInt1, 5);
        mRequestHistograms.put(paramInt1, localTelephonyHistogram2);
      }
      localTelephonyHistogram2.addTimeTaken(paramInt2);
      return;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mCallingPackage);
    paramParcel.writeLong(mCompletedRequestsWakelockTime);
    paramParcel.writeLong(mCompletedRequestsCount);
    paramParcel.writeLong(mPendingRequestsWakelockTime);
    paramParcel.writeLong(mPendingRequestsCount);
    paramParcel.writeTypedList(getRequestHistograms());
  }
}
