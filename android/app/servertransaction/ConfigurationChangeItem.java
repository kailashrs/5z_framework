package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class ConfigurationChangeItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<ConfigurationChangeItem> CREATOR = new Parcelable.Creator()
  {
    public ConfigurationChangeItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ConfigurationChangeItem(paramAnonymousParcel, null);
    }
    
    public ConfigurationChangeItem[] newArray(int paramAnonymousInt)
    {
      return new ConfigurationChangeItem[paramAnonymousInt];
    }
  };
  private Configuration mConfiguration;
  
  private ConfigurationChangeItem() {}
  
  private ConfigurationChangeItem(Parcel paramParcel)
  {
    mConfiguration = ((Configuration)paramParcel.readTypedObject(Configuration.CREATOR));
  }
  
  public static ConfigurationChangeItem obtain(Configuration paramConfiguration)
  {
    ConfigurationChangeItem localConfigurationChangeItem1 = (ConfigurationChangeItem)ObjectPool.obtain(ConfigurationChangeItem.class);
    ConfigurationChangeItem localConfigurationChangeItem2 = localConfigurationChangeItem1;
    if (localConfigurationChangeItem1 == null) {
      localConfigurationChangeItem2 = new ConfigurationChangeItem();
    }
    mConfiguration = paramConfiguration;
    return localConfigurationChangeItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (ConfigurationChangeItem)paramObject;
      return Objects.equals(mConfiguration, mConfiguration);
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    paramClientTransactionHandler.handleConfigurationChanged(mConfiguration);
  }
  
  public int hashCode()
  {
    return mConfiguration.hashCode();
  }
  
  public void preExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder)
  {
    paramClientTransactionHandler.updatePendingConfiguration(mConfiguration);
  }
  
  public void recycle()
  {
    mConfiguration = null;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ConfigurationChangeItem{config=");
    localStringBuilder.append(mConfiguration);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedObject(mConfiguration, paramInt);
  }
}
