package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.view.IDragAndDropPermissions;
import com.android.internal.view.IDragAndDropPermissions.Stub;

public final class DragAndDropPermissions
  implements Parcelable
{
  public static final Parcelable.Creator<DragAndDropPermissions> CREATOR = new Parcelable.Creator()
  {
    public DragAndDropPermissions createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DragAndDropPermissions(paramAnonymousParcel, null);
    }
    
    public DragAndDropPermissions[] newArray(int paramAnonymousInt)
    {
      return new DragAndDropPermissions[paramAnonymousInt];
    }
  };
  private final IDragAndDropPermissions mDragAndDropPermissions;
  private IBinder mTransientToken;
  
  private DragAndDropPermissions(Parcel paramParcel)
  {
    mDragAndDropPermissions = IDragAndDropPermissions.Stub.asInterface(paramParcel.readStrongBinder());
    mTransientToken = paramParcel.readStrongBinder();
  }
  
  private DragAndDropPermissions(IDragAndDropPermissions paramIDragAndDropPermissions)
  {
    mDragAndDropPermissions = paramIDragAndDropPermissions;
  }
  
  public static DragAndDropPermissions obtain(DragEvent paramDragEvent)
  {
    if (paramDragEvent.getDragAndDropPermissions() == null) {
      return null;
    }
    return new DragAndDropPermissions(paramDragEvent.getDragAndDropPermissions());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void release()
  {
    try
    {
      mDragAndDropPermissions.release();
      mTransientToken = null;
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public boolean take(IBinder paramIBinder)
  {
    try
    {
      mDragAndDropPermissions.take(paramIBinder);
      return true;
    }
    catch (RemoteException paramIBinder) {}
    return false;
  }
  
  public boolean takeTransient()
  {
    try
    {
      Binder localBinder = new android/os/Binder;
      localBinder.<init>();
      mTransientToken = localBinder;
      mDragAndDropPermissions.takeTransient(mTransientToken);
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongInterface(mDragAndDropPermissions);
    paramParcel.writeStrongBinder(mTransientToken);
  }
}
