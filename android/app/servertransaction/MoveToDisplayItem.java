package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Trace;
import java.util.Objects;

public class MoveToDisplayItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<MoveToDisplayItem> CREATOR = new Parcelable.Creator()
  {
    public MoveToDisplayItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MoveToDisplayItem(paramAnonymousParcel, null);
    }
    
    public MoveToDisplayItem[] newArray(int paramAnonymousInt)
    {
      return new MoveToDisplayItem[paramAnonymousInt];
    }
  };
  private Configuration mConfiguration;
  private int mTargetDisplayId;
  
  private MoveToDisplayItem() {}
  
  private MoveToDisplayItem(Parcel paramParcel)
  {
    mTargetDisplayId = paramParcel.readInt();
    mConfiguration = ((Configuration)paramParcel.readTypedObject(Configuration.CREATOR));
  }
  
  public static MoveToDisplayItem obtain(int paramInt, Configuration paramConfiguration)
  {
    MoveToDisplayItem localMoveToDisplayItem1 = (MoveToDisplayItem)ObjectPool.obtain(MoveToDisplayItem.class);
    MoveToDisplayItem localMoveToDisplayItem2 = localMoveToDisplayItem1;
    if (localMoveToDisplayItem1 == null) {
      localMoveToDisplayItem2 = new MoveToDisplayItem();
    }
    mTargetDisplayId = paramInt;
    mConfiguration = paramConfiguration;
    return localMoveToDisplayItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (MoveToDisplayItem)paramObject;
      if ((mTargetDisplayId != mTargetDisplayId) || (!Objects.equals(mConfiguration, mConfiguration))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityMovedToDisplay");
    paramClientTransactionHandler.handleActivityConfigurationChanged(paramIBinder, mConfiguration, mTargetDisplayId);
    Trace.traceEnd(64L);
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + mTargetDisplayId) + mConfiguration.hashCode();
  }
  
  public void recycle()
  {
    mTargetDisplayId = 0;
    mConfiguration = null;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("MoveToDisplayItem{targetDisplayId=");
    localStringBuilder.append(mTargetDisplayId);
    localStringBuilder.append(",configuration=");
    localStringBuilder.append(mConfiguration);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mTargetDisplayId);
    paramParcel.writeTypedObject(mConfiguration, paramInt);
  }
}
