package android.app.servertransaction;

import android.app.ActivityManager;
import android.app.ClientTransactionHandler;
import android.app.IActivityManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.Trace;

public class ResumeActivityItem
  extends ActivityLifecycleItem
{
  public static final Parcelable.Creator<ResumeActivityItem> CREATOR = new Parcelable.Creator()
  {
    public ResumeActivityItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ResumeActivityItem(paramAnonymousParcel, null);
    }
    
    public ResumeActivityItem[] newArray(int paramAnonymousInt)
    {
      return new ResumeActivityItem[paramAnonymousInt];
    }
  };
  private static final String TAG = "ResumeActivityItem";
  private boolean mIsForward;
  private int mProcState;
  private boolean mUpdateProcState;
  
  private ResumeActivityItem() {}
  
  private ResumeActivityItem(Parcel paramParcel)
  {
    mProcState = paramParcel.readInt();
    mUpdateProcState = paramParcel.readBoolean();
    mIsForward = paramParcel.readBoolean();
  }
  
  public static ResumeActivityItem obtain(int paramInt, boolean paramBoolean)
  {
    ResumeActivityItem localResumeActivityItem1 = (ResumeActivityItem)ObjectPool.obtain(ResumeActivityItem.class);
    ResumeActivityItem localResumeActivityItem2 = localResumeActivityItem1;
    if (localResumeActivityItem1 == null) {
      localResumeActivityItem2 = new ResumeActivityItem();
    }
    mProcState = paramInt;
    mUpdateProcState = true;
    mIsForward = paramBoolean;
    return localResumeActivityItem2;
  }
  
  public static ResumeActivityItem obtain(boolean paramBoolean)
  {
    ResumeActivityItem localResumeActivityItem1 = (ResumeActivityItem)ObjectPool.obtain(ResumeActivityItem.class);
    ResumeActivityItem localResumeActivityItem2 = localResumeActivityItem1;
    if (localResumeActivityItem1 == null) {
      localResumeActivityItem2 = new ResumeActivityItem();
    }
    mProcState = -1;
    mUpdateProcState = false;
    mIsForward = paramBoolean;
    return localResumeActivityItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (ResumeActivityItem)paramObject;
      if ((mProcState != mProcState) || (mUpdateProcState != mUpdateProcState) || (mIsForward != mIsForward)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityResume");
    paramClientTransactionHandler.handleResumeActivity(paramIBinder, true, mIsForward, "RESUME_ACTIVITY");
    Trace.traceEnd(64L);
  }
  
  public int getTargetState()
  {
    return 3;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * 17 + mProcState) + mUpdateProcState) + mIsForward;
  }
  
  public void postExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    try
    {
      ActivityManager.getService().activityResumed(paramIBinder);
      return;
    }
    catch (RemoteException paramClientTransactionHandler)
    {
      throw paramClientTransactionHandler.rethrowFromSystemServer();
    }
  }
  
  public void preExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder)
  {
    if (mUpdateProcState) {
      paramClientTransactionHandler.updateProcessState(mProcState, false);
    }
  }
  
  public void recycle()
  {
    super.recycle();
    mProcState = -1;
    mUpdateProcState = false;
    mIsForward = false;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ResumeActivityItem{procState=");
    localStringBuilder.append(mProcState);
    localStringBuilder.append(",updateProcState=");
    localStringBuilder.append(mUpdateProcState);
    localStringBuilder.append(",isForward=");
    localStringBuilder.append(mIsForward);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mProcState);
    paramParcel.writeBoolean(mUpdateProcState);
    paramParcel.writeBoolean(mIsForward);
  }
}
