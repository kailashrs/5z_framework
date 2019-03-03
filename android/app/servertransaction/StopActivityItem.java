package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Trace;

public class StopActivityItem
  extends ActivityLifecycleItem
{
  public static final Parcelable.Creator<StopActivityItem> CREATOR = new Parcelable.Creator()
  {
    public StopActivityItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StopActivityItem(paramAnonymousParcel, null);
    }
    
    public StopActivityItem[] newArray(int paramAnonymousInt)
    {
      return new StopActivityItem[paramAnonymousInt];
    }
  };
  private static final String TAG = "StopActivityItem";
  private int mConfigChanges;
  private boolean mShowWindow;
  
  private StopActivityItem() {}
  
  private StopActivityItem(Parcel paramParcel)
  {
    mShowWindow = paramParcel.readBoolean();
    mConfigChanges = paramParcel.readInt();
  }
  
  public static StopActivityItem obtain(boolean paramBoolean, int paramInt)
  {
    StopActivityItem localStopActivityItem1 = (StopActivityItem)ObjectPool.obtain(StopActivityItem.class);
    StopActivityItem localStopActivityItem2 = localStopActivityItem1;
    if (localStopActivityItem1 == null) {
      localStopActivityItem2 = new StopActivityItem();
    }
    mShowWindow = paramBoolean;
    mConfigChanges = paramInt;
    return localStopActivityItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (StopActivityItem)paramObject;
      if ((mShowWindow != mShowWindow) || (mConfigChanges != mConfigChanges)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityStop");
    paramClientTransactionHandler.handleStopActivity(paramIBinder, mShowWindow, mConfigChanges, paramPendingTransactionActions, true, "STOP_ACTIVITY_ITEM");
    Trace.traceEnd(64L);
  }
  
  public int getTargetState()
  {
    return 5;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + mShowWindow) + mConfigChanges;
  }
  
  public void postExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    paramClientTransactionHandler.reportStop(paramPendingTransactionActions);
  }
  
  public void recycle()
  {
    super.recycle();
    mShowWindow = false;
    mConfigChanges = 0;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("StopActivityItem{showWindow=");
    localStringBuilder.append(mShowWindow);
    localStringBuilder.append(",configChanges=");
    localStringBuilder.append(mConfigChanges);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBoolean(mShowWindow);
    paramParcel.writeInt(mConfigChanges);
  }
}
