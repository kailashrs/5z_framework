package android.content.pm;

import android.app.IApplicationThread;
import android.app.IApplicationThread.Stub;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;
import java.util.List;

public abstract interface ICrossProfileApps
  extends IInterface
{
  public abstract List<UserHandle> getTargetUserProfiles(String paramString)
    throws RemoteException;
  
  public abstract void startActivityAsUser(IApplicationThread paramIApplicationThread, String paramString, ComponentName paramComponentName, UserHandle paramUserHandle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICrossProfileApps
  {
    private static final String DESCRIPTOR = "android.content.pm.ICrossProfileApps";
    static final int TRANSACTION_getTargetUserProfiles = 2;
    static final int TRANSACTION_startActivityAsUser = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.ICrossProfileApps");
    }
    
    public static ICrossProfileApps asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.ICrossProfileApps");
      if ((localIInterface != null) && ((localIInterface instanceof ICrossProfileApps))) {
        return (ICrossProfileApps)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.ICrossProfileApps");
          paramParcel1 = getTargetUserProfiles(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.ICrossProfileApps");
        IApplicationThread localIApplicationThread = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
        String str = paramParcel1.readString();
        paramInt1 = paramParcel1.readInt();
        Object localObject = null;
        ComponentName localComponentName;
        if (paramInt1 != 0) {
          localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
        } else {
          localComponentName = null;
        }
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject;
        }
        startActivityAsUser(localIApplicationThread, str, localComponentName, paramParcel1);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.content.pm.ICrossProfileApps");
      return true;
    }
    
    private static class Proxy
      implements ICrossProfileApps
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.content.pm.ICrossProfileApps";
      }
      
      public List<UserHandle> getTargetUserProfiles(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ICrossProfileApps");
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(UserHandle.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startActivityAsUser(IApplicationThread paramIApplicationThread, String paramString, ComponentName paramComponentName, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ICrossProfileApps");
          if (paramIApplicationThread != null) {
            paramIApplicationThread = paramIApplicationThread.asBinder();
          } else {
            paramIApplicationThread = null;
          }
          localParcel1.writeStrongBinder(paramIApplicationThread);
          localParcel1.writeString(paramString);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
