package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Trace;
import java.util.Objects;

public class ActivityConfigurationChangeItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<ActivityConfigurationChangeItem> CREATOR = new Parcelable.Creator()
  {
    public ActivityConfigurationChangeItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ActivityConfigurationChangeItem(paramAnonymousParcel, null);
    }
    
    public ActivityConfigurationChangeItem[] newArray(int paramAnonymousInt)
    {
      return new ActivityConfigurationChangeItem[paramAnonymousInt];
    }
  };
  private Configuration mConfiguration;
  
  private ActivityConfigurationChangeItem() {}
  
  private ActivityConfigurationChangeItem(Parcel paramParcel)
  {
    mConfiguration = ((Configuration)paramParcel.readTypedObject(Configuration.CREATOR));
  }
  
  public static ActivityConfigurationChangeItem obtain(Configuration paramConfiguration)
  {
    ActivityConfigurationChangeItem localActivityConfigurationChangeItem1 = (ActivityConfigurationChangeItem)ObjectPool.obtain(ActivityConfigurationChangeItem.class);
    ActivityConfigurationChangeItem localActivityConfigurationChangeItem2 = localActivityConfigurationChangeItem1;
    if (localActivityConfigurationChangeItem1 == null) {
      localActivityConfigurationChangeItem2 = new ActivityConfigurationChangeItem();
    }
    mConfiguration = paramConfiguration;
    return localActivityConfigurationChangeItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (ActivityConfigurationChangeItem)paramObject;
      return Objects.equals(mConfiguration, mConfiguration);
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityConfigChanged");
    paramClientTransactionHandler.handleActivityConfigurationChanged(paramIBinder, mConfiguration, -1);
    Trace.traceEnd(64L);
  }
  
  public int hashCode()
  {
    return mConfiguration.hashCode();
  }
  
  public void recycle()
  {
    mConfiguration = null;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ActivityConfigurationChange{config=");
    localStringBuilder.append(mConfiguration);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedObject(mConfiguration, paramInt);
  }
}
