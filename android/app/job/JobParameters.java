package android.app.job;

import android.content.ClipData;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.RemoteException;

public class JobParameters
  implements Parcelable
{
  public static final Parcelable.Creator<JobParameters> CREATOR = new Parcelable.Creator()
  {
    public JobParameters createFromParcel(Parcel paramAnonymousParcel)
    {
      return new JobParameters(paramAnonymousParcel, null);
    }
    
    public JobParameters[] newArray(int paramAnonymousInt)
    {
      return new JobParameters[paramAnonymousInt];
    }
  };
  public static final int REASON_CANCELED = 0;
  public static final int REASON_CONSTRAINTS_NOT_SATISFIED = 1;
  public static final int REASON_DEVICE_IDLE = 4;
  public static final int REASON_PREEMPT = 2;
  public static final int REASON_TIMEOUT = 3;
  private final IBinder callback;
  private final ClipData clipData;
  private final int clipGrantFlags;
  private String debugStopReason;
  private final PersistableBundle extras;
  private final int jobId;
  private final String[] mTriggeredContentAuthorities;
  private final Uri[] mTriggeredContentUris;
  private final Network network;
  private final boolean overrideDeadlineExpired;
  private int stopReason;
  private final Bundle transientExtras;
  
  public JobParameters(IBinder paramIBinder, int paramInt1, PersistableBundle paramPersistableBundle, Bundle paramBundle, ClipData paramClipData, int paramInt2, boolean paramBoolean, Uri[] paramArrayOfUri, String[] paramArrayOfString, Network paramNetwork)
  {
    jobId = paramInt1;
    extras = paramPersistableBundle;
    transientExtras = paramBundle;
    clipData = paramClipData;
    clipGrantFlags = paramInt2;
    callback = paramIBinder;
    overrideDeadlineExpired = paramBoolean;
    mTriggeredContentUris = paramArrayOfUri;
    mTriggeredContentAuthorities = paramArrayOfString;
    network = paramNetwork;
  }
  
  private JobParameters(Parcel paramParcel)
  {
    jobId = paramParcel.readInt();
    extras = paramParcel.readPersistableBundle();
    transientExtras = paramParcel.readBundle();
    int i = paramParcel.readInt();
    boolean bool = false;
    if (i != 0)
    {
      clipData = ((ClipData)ClipData.CREATOR.createFromParcel(paramParcel));
      clipGrantFlags = paramParcel.readInt();
    }
    else
    {
      clipData = null;
      clipGrantFlags = 0;
    }
    callback = paramParcel.readStrongBinder();
    if (paramParcel.readInt() == 1) {
      bool = true;
    }
    overrideDeadlineExpired = bool;
    mTriggeredContentUris = ((Uri[])paramParcel.createTypedArray(Uri.CREATOR));
    mTriggeredContentAuthorities = paramParcel.createStringArray();
    if (paramParcel.readInt() != 0) {
      network = ((Network)Network.CREATOR.createFromParcel(paramParcel));
    } else {
      network = null;
    }
    stopReason = paramParcel.readInt();
    debugStopReason = paramParcel.readString();
  }
  
  public static String getReasonName(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unknown:");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    case 4: 
      return "device_idle";
    case 3: 
      return "timeout";
    case 2: 
      return "preempt";
    case 1: 
      return "constraints";
    }
    return "canceled";
  }
  
  public void completeWork(JobWorkItem paramJobWorkItem)
  {
    try
    {
      if (getCallback().completeWork(getJobId(), paramJobWorkItem.getWorkId())) {
        return;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Given work is not active: ");
      localStringBuilder.append(paramJobWorkItem);
      localIllegalArgumentException.<init>(localStringBuilder.toString());
      throw localIllegalArgumentException;
    }
    catch (RemoteException paramJobWorkItem)
    {
      throw paramJobWorkItem.rethrowFromSystemServer();
    }
  }
  
  public JobWorkItem dequeueWork()
  {
    try
    {
      JobWorkItem localJobWorkItem = getCallback().dequeueWork(getJobId());
      return localJobWorkItem;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public IJobCallback getCallback()
  {
    return IJobCallback.Stub.asInterface(callback);
  }
  
  public ClipData getClipData()
  {
    return clipData;
  }
  
  public int getClipGrantFlags()
  {
    return clipGrantFlags;
  }
  
  public String getDebugStopReason()
  {
    return debugStopReason;
  }
  
  public PersistableBundle getExtras()
  {
    return extras;
  }
  
  public int getJobId()
  {
    return jobId;
  }
  
  public Network getNetwork()
  {
    return network;
  }
  
  public int getStopReason()
  {
    return stopReason;
  }
  
  public Bundle getTransientExtras()
  {
    return transientExtras;
  }
  
  public String[] getTriggeredContentAuthorities()
  {
    return mTriggeredContentAuthorities;
  }
  
  public Uri[] getTriggeredContentUris()
  {
    return mTriggeredContentUris;
  }
  
  public boolean isOverrideDeadlineExpired()
  {
    return overrideDeadlineExpired;
  }
  
  public void setStopReason(int paramInt, String paramString)
  {
    stopReason = paramInt;
    debugStopReason = paramString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(jobId);
    paramParcel.writePersistableBundle(extras);
    paramParcel.writeBundle(transientExtras);
    if (clipData != null)
    {
      paramParcel.writeInt(1);
      clipData.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(clipGrantFlags);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeStrongBinder(callback);
    paramParcel.writeInt(overrideDeadlineExpired);
    paramParcel.writeTypedArray(mTriggeredContentUris, paramInt);
    paramParcel.writeStringArray(mTriggeredContentAuthorities);
    if (network != null)
    {
      paramParcel.writeInt(1);
      network.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(stopReason);
    paramParcel.writeString(debugStopReason);
  }
}
