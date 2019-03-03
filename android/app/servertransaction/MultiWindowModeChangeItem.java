package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class MultiWindowModeChangeItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<MultiWindowModeChangeItem> CREATOR = new Parcelable.Creator()
  {
    public MultiWindowModeChangeItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MultiWindowModeChangeItem(paramAnonymousParcel, null);
    }
    
    public MultiWindowModeChangeItem[] newArray(int paramAnonymousInt)
    {
      return new MultiWindowModeChangeItem[paramAnonymousInt];
    }
  };
  private boolean mIsInMultiWindowMode;
  private Configuration mOverrideConfig;
  
  private MultiWindowModeChangeItem() {}
  
  private MultiWindowModeChangeItem(Parcel paramParcel)
  {
    mIsInMultiWindowMode = paramParcel.readBoolean();
    mOverrideConfig = ((Configuration)paramParcel.readTypedObject(Configuration.CREATOR));
  }
  
  public static MultiWindowModeChangeItem obtain(boolean paramBoolean, Configuration paramConfiguration)
  {
    MultiWindowModeChangeItem localMultiWindowModeChangeItem1 = (MultiWindowModeChangeItem)ObjectPool.obtain(MultiWindowModeChangeItem.class);
    MultiWindowModeChangeItem localMultiWindowModeChangeItem2 = localMultiWindowModeChangeItem1;
    if (localMultiWindowModeChangeItem1 == null) {
      localMultiWindowModeChangeItem2 = new MultiWindowModeChangeItem();
    }
    mIsInMultiWindowMode = paramBoolean;
    mOverrideConfig = paramConfiguration;
    return localMultiWindowModeChangeItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (MultiWindowModeChangeItem)paramObject;
      if ((mIsInMultiWindowMode != mIsInMultiWindowMode) || (!Objects.equals(mOverrideConfig, mOverrideConfig))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    paramClientTransactionHandler.handleMultiWindowModeChanged(paramIBinder, mIsInMultiWindowMode, mOverrideConfig);
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + mIsInMultiWindowMode) + mOverrideConfig.hashCode();
  }
  
  public void recycle()
  {
    mIsInMultiWindowMode = false;
    mOverrideConfig = null;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("MultiWindowModeChangeItem{isInMultiWindowMode=");
    localStringBuilder.append(mIsInMultiWindowMode);
    localStringBuilder.append(",overrideConfig=");
    localStringBuilder.append(mOverrideConfig);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBoolean(mIsInMultiWindowMode);
    paramParcel.writeTypedObject(mOverrideConfig, paramInt);
  }
}
