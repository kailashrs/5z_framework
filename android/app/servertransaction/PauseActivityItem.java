package android.app.servertransaction;

import android.app.ActivityManager;
import android.app.ClientTransactionHandler;
import android.app.IActivityManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.Trace;

public class PauseActivityItem
  extends ActivityLifecycleItem
{
  public static final Parcelable.Creator<PauseActivityItem> CREATOR = new Parcelable.Creator()
  {
    public PauseActivityItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PauseActivityItem(paramAnonymousParcel, null);
    }
    
    public PauseActivityItem[] newArray(int paramAnonymousInt)
    {
      return new PauseActivityItem[paramAnonymousInt];
    }
  };
  private static final String TAG = "PauseActivityItem";
  private int mConfigChanges;
  private boolean mDontReport;
  private boolean mFinished;
  private boolean mUserLeaving;
  
  private PauseActivityItem() {}
  
  private PauseActivityItem(Parcel paramParcel)
  {
    mFinished = paramParcel.readBoolean();
    mUserLeaving = paramParcel.readBoolean();
    mConfigChanges = paramParcel.readInt();
    mDontReport = paramParcel.readBoolean();
  }
  
  public static PauseActivityItem obtain()
  {
    PauseActivityItem localPauseActivityItem1 = (PauseActivityItem)ObjectPool.obtain(PauseActivityItem.class);
    PauseActivityItem localPauseActivityItem2 = localPauseActivityItem1;
    if (localPauseActivityItem1 == null) {
      localPauseActivityItem2 = new PauseActivityItem();
    }
    mFinished = false;
    mUserLeaving = false;
    mConfigChanges = 0;
    mDontReport = true;
    return localPauseActivityItem2;
  }
  
  public static PauseActivityItem obtain(boolean paramBoolean1, boolean paramBoolean2, int paramInt, boolean paramBoolean3)
  {
    PauseActivityItem localPauseActivityItem1 = (PauseActivityItem)ObjectPool.obtain(PauseActivityItem.class);
    PauseActivityItem localPauseActivityItem2 = localPauseActivityItem1;
    if (localPauseActivityItem1 == null) {
      localPauseActivityItem2 = new PauseActivityItem();
    }
    mFinished = paramBoolean1;
    mUserLeaving = paramBoolean2;
    mConfigChanges = paramInt;
    mDontReport = paramBoolean3;
    return localPauseActivityItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (PauseActivityItem)paramObject;
      if ((mFinished != mFinished) || (mUserLeaving != mUserLeaving) || (mConfigChanges != mConfigChanges) || (mDontReport != mDontReport)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityPause");
    paramClientTransactionHandler.handlePauseActivity(paramIBinder, mFinished, mUserLeaving, mConfigChanges, paramPendingTransactionActions, "PAUSE_ACTIVITY_ITEM");
    Trace.traceEnd(64L);
  }
  
  public int getTargetState()
  {
    return 4;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * 17 + mFinished) + mUserLeaving) + mConfigChanges) + mDontReport;
  }
  
  public void postExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    if (mDontReport) {
      return;
    }
    try
    {
      ActivityManager.getService().activityPaused(paramIBinder);
      return;
    }
    catch (RemoteException paramClientTransactionHandler)
    {
      throw paramClientTransactionHandler.rethrowFromSystemServer();
    }
  }
  
  public void recycle()
  {
    super.recycle();
    mFinished = false;
    mUserLeaving = false;
    mConfigChanges = 0;
    mDontReport = false;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PauseActivityItem{finished=");
    localStringBuilder.append(mFinished);
    localStringBuilder.append(",userLeaving=");
    localStringBuilder.append(mUserLeaving);
    localStringBuilder.append(",configChanges=");
    localStringBuilder.append(mConfigChanges);
    localStringBuilder.append(",dontReport=");
    localStringBuilder.append(mDontReport);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBoolean(mFinished);
    paramParcel.writeBoolean(mUserLeaving);
    paramParcel.writeInt(mConfigChanges);
    paramParcel.writeBoolean(mDontReport);
  }
}
