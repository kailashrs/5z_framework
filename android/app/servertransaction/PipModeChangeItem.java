package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class PipModeChangeItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<PipModeChangeItem> CREATOR = new Parcelable.Creator()
  {
    public PipModeChangeItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PipModeChangeItem(paramAnonymousParcel, null);
    }
    
    public PipModeChangeItem[] newArray(int paramAnonymousInt)
    {
      return new PipModeChangeItem[paramAnonymousInt];
    }
  };
  private boolean mIsInPipMode;
  private Configuration mOverrideConfig;
  
  private PipModeChangeItem() {}
  
  private PipModeChangeItem(Parcel paramParcel)
  {
    mIsInPipMode = paramParcel.readBoolean();
    mOverrideConfig = ((Configuration)paramParcel.readTypedObject(Configuration.CREATOR));
  }
  
  public static PipModeChangeItem obtain(boolean paramBoolean, Configuration paramConfiguration)
  {
    PipModeChangeItem localPipModeChangeItem1 = (PipModeChangeItem)ObjectPool.obtain(PipModeChangeItem.class);
    PipModeChangeItem localPipModeChangeItem2 = localPipModeChangeItem1;
    if (localPipModeChangeItem1 == null) {
      localPipModeChangeItem2 = new PipModeChangeItem();
    }
    mIsInPipMode = paramBoolean;
    mOverrideConfig = paramConfiguration;
    return localPipModeChangeItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (PipModeChangeItem)paramObject;
      if ((mIsInPipMode != mIsInPipMode) || (!Objects.equals(mOverrideConfig, mOverrideConfig))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    paramClientTransactionHandler.handlePictureInPictureModeChanged(paramIBinder, mIsInPipMode, mOverrideConfig);
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + mIsInPipMode) + mOverrideConfig.hashCode();
  }
  
  public void recycle()
  {
    mIsInPipMode = false;
    mOverrideConfig = null;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PipModeChangeItem{isInPipMode=");
    localStringBuilder.append(mIsInPipMode);
    localStringBuilder.append(",overrideConfig=");
    localStringBuilder.append(mOverrideConfig);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBoolean(mIsInPipMode);
    paramParcel.writeTypedObject(mOverrideConfig, paramInt);
  }
}
