package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RemoteAnimationAdapter
  implements Parcelable
{
  public static final Parcelable.Creator<RemoteAnimationAdapter> CREATOR = new Parcelable.Creator()
  {
    public RemoteAnimationAdapter createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RemoteAnimationAdapter(paramAnonymousParcel);
    }
    
    public RemoteAnimationAdapter[] newArray(int paramAnonymousInt)
    {
      return new RemoteAnimationAdapter[paramAnonymousInt];
    }
  };
  private int mCallingPid;
  private final long mDuration;
  private final IRemoteAnimationRunner mRunner;
  private final long mStatusBarTransitionDelay;
  
  public RemoteAnimationAdapter(Parcel paramParcel)
  {
    mRunner = IRemoteAnimationRunner.Stub.asInterface(paramParcel.readStrongBinder());
    mDuration = paramParcel.readLong();
    mStatusBarTransitionDelay = paramParcel.readLong();
  }
  
  public RemoteAnimationAdapter(IRemoteAnimationRunner paramIRemoteAnimationRunner, long paramLong1, long paramLong2)
  {
    mRunner = paramIRemoteAnimationRunner;
    mDuration = paramLong1;
    mStatusBarTransitionDelay = paramLong2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCallingPid()
  {
    return mCallingPid;
  }
  
  public long getDuration()
  {
    return mDuration;
  }
  
  public IRemoteAnimationRunner getRunner()
  {
    return mRunner;
  }
  
  public long getStatusBarTransitionDelay()
  {
    return mStatusBarTransitionDelay;
  }
  
  public void setCallingPid(int paramInt)
  {
    mCallingPid = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongInterface(mRunner);
    paramParcel.writeLong(mDuration);
    paramParcel.writeLong(mStatusBarTransitionDelay);
  }
}
