package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Trace;

public class DestroyActivityItem
  extends ActivityLifecycleItem
{
  public static final Parcelable.Creator<DestroyActivityItem> CREATOR = new Parcelable.Creator()
  {
    public DestroyActivityItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DestroyActivityItem(paramAnonymousParcel, null);
    }
    
    public DestroyActivityItem[] newArray(int paramAnonymousInt)
    {
      return new DestroyActivityItem[paramAnonymousInt];
    }
  };
  private int mConfigChanges;
  private boolean mFinished;
  
  private DestroyActivityItem() {}
  
  private DestroyActivityItem(Parcel paramParcel)
  {
    mFinished = paramParcel.readBoolean();
    mConfigChanges = paramParcel.readInt();
  }
  
  public static DestroyActivityItem obtain(boolean paramBoolean, int paramInt)
  {
    DestroyActivityItem localDestroyActivityItem1 = (DestroyActivityItem)ObjectPool.obtain(DestroyActivityItem.class);
    DestroyActivityItem localDestroyActivityItem2 = localDestroyActivityItem1;
    if (localDestroyActivityItem1 == null) {
      localDestroyActivityItem2 = new DestroyActivityItem();
    }
    mFinished = paramBoolean;
    mConfigChanges = paramInt;
    return localDestroyActivityItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (DestroyActivityItem)paramObject;
      if ((mFinished != mFinished) || (mConfigChanges != mConfigChanges)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityDestroy");
    paramClientTransactionHandler.handleDestroyActivity(paramIBinder, mFinished, mConfigChanges, false, "DestroyActivityItem");
    Trace.traceEnd(64L);
  }
  
  public int getTargetState()
  {
    return 6;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + mFinished) + mConfigChanges;
  }
  
  public void recycle()
  {
    super.recycle();
    mFinished = false;
    mConfigChanges = 0;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DestroyActivityItem{finished=");
    localStringBuilder.append(mFinished);
    localStringBuilder.append(",mConfigChanges=");
    localStringBuilder.append(mConfigChanges);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBoolean(mFinished);
    paramParcel.writeInt(mConfigChanges);
  }
}
