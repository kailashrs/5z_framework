package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.app.IApplicationThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientTransaction
  implements Parcelable, ObjectPoolItem
{
  public static final Parcelable.Creator<ClientTransaction> CREATOR = new Parcelable.Creator()
  {
    public ClientTransaction createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ClientTransaction(paramAnonymousParcel, null);
    }
    
    public ClientTransaction[] newArray(int paramAnonymousInt)
    {
      return new ClientTransaction[paramAnonymousInt];
    }
  };
  private List<ClientTransactionItem> mActivityCallbacks;
  private IBinder mActivityToken;
  private IApplicationThread mClient;
  private ActivityLifecycleItem mLifecycleStateRequest;
  
  private ClientTransaction() {}
  
  private ClientTransaction(Parcel paramParcel)
  {
    mClient = ((IApplicationThread)paramParcel.readStrongBinder());
    if (paramParcel.readBoolean()) {
      mActivityToken = paramParcel.readStrongBinder();
    }
    mLifecycleStateRequest = ((ActivityLifecycleItem)paramParcel.readParcelable(getClass().getClassLoader()));
    if (paramParcel.readBoolean())
    {
      mActivityCallbacks = new ArrayList();
      paramParcel.readParcelableList(mActivityCallbacks, getClass().getClassLoader());
    }
  }
  
  public static ClientTransaction obtain(IApplicationThread paramIApplicationThread, IBinder paramIBinder)
  {
    ClientTransaction localClientTransaction1 = (ClientTransaction)ObjectPool.obtain(ClientTransaction.class);
    ClientTransaction localClientTransaction2 = localClientTransaction1;
    if (localClientTransaction1 == null) {
      localClientTransaction2 = new ClientTransaction();
    }
    mClient = paramIApplicationThread;
    mActivityToken = paramIBinder;
    return localClientTransaction2;
  }
  
  public void addCallback(ClientTransactionItem paramClientTransactionItem)
  {
    if (mActivityCallbacks == null) {
      mActivityCallbacks = new ArrayList();
    }
    mActivityCallbacks.add(paramClientTransactionItem);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (ClientTransaction)paramObject;
      if ((!Objects.equals(mActivityCallbacks, mActivityCallbacks)) || (!Objects.equals(mLifecycleStateRequest, mLifecycleStateRequest)) || (mClient != mClient) || (mActivityToken != mActivityToken)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public IBinder getActivityToken()
  {
    return mActivityToken;
  }
  
  List<ClientTransactionItem> getCallbacks()
  {
    return mActivityCallbacks;
  }
  
  public IApplicationThread getClient()
  {
    return mClient;
  }
  
  @VisibleForTesting
  public ActivityLifecycleItem getLifecycleStateRequest()
  {
    return mLifecycleStateRequest;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + Objects.hashCode(mActivityCallbacks)) + Objects.hashCode(mLifecycleStateRequest);
  }
  
  public void preExecute(ClientTransactionHandler paramClientTransactionHandler)
  {
    if (mActivityCallbacks != null)
    {
      int i = mActivityCallbacks.size();
      for (int j = 0; j < i; j++) {
        ((ClientTransactionItem)mActivityCallbacks.get(j)).preExecute(paramClientTransactionHandler, mActivityToken);
      }
    }
    if (mLifecycleStateRequest != null) {
      mLifecycleStateRequest.preExecute(paramClientTransactionHandler, mActivityToken);
    }
  }
  
  public void recycle()
  {
    if (mActivityCallbacks != null)
    {
      int i = mActivityCallbacks.size();
      for (int j = 0; j < i; j++) {
        ((ClientTransactionItem)mActivityCallbacks.get(j)).recycle();
      }
      mActivityCallbacks.clear();
    }
    if (mLifecycleStateRequest != null)
    {
      mLifecycleStateRequest.recycle();
      mLifecycleStateRequest = null;
    }
    mClient = null;
    mActivityToken = null;
    ObjectPool.recycle(this);
  }
  
  public void schedule()
    throws RemoteException
  {
    mClient.scheduleTransaction(this);
  }
  
  public void setLifecycleStateRequest(ActivityLifecycleItem paramActivityLifecycleItem)
  {
    mLifecycleStateRequest = paramActivityLifecycleItem;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(mClient.asBinder());
    IBinder localIBinder = mActivityToken;
    boolean bool1 = false;
    if (localIBinder != null) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    paramParcel.writeBoolean(bool2);
    if (bool2) {
      paramParcel.writeStrongBinder(mActivityToken);
    }
    paramParcel.writeParcelable(mLifecycleStateRequest, paramInt);
    boolean bool2 = bool1;
    if (mActivityCallbacks != null) {
      bool2 = true;
    }
    paramParcel.writeBoolean(bool2);
    if (bool2) {
      paramParcel.writeParcelableList(mActivityCallbacks, paramInt);
    }
  }
}
