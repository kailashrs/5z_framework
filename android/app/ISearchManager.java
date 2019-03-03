package android.app;

import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface ISearchManager
  extends IInterface
{
  public abstract List<ResolveInfo> getGlobalSearchActivities()
    throws RemoteException;
  
  public abstract ComponentName getGlobalSearchActivity()
    throws RemoteException;
  
  public abstract SearchableInfo getSearchableInfo(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract List<SearchableInfo> getSearchablesInGlobalSearch()
    throws RemoteException;
  
  public abstract ComponentName getWebSearchActivity()
    throws RemoteException;
  
  public abstract void launchAssist(Bundle paramBundle)
    throws RemoteException;
  
  public abstract boolean launchLegacyAssist(String paramString, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISearchManager
  {
    private static final String DESCRIPTOR = "android.app.ISearchManager";
    static final int TRANSACTION_getGlobalSearchActivities = 3;
    static final int TRANSACTION_getGlobalSearchActivity = 4;
    static final int TRANSACTION_getSearchableInfo = 1;
    static final int TRANSACTION_getSearchablesInGlobalSearch = 2;
    static final int TRANSACTION_getWebSearchActivity = 5;
    static final int TRANSACTION_launchAssist = 6;
    static final int TRANSACTION_launchLegacyAssist = 7;
    
    public Stub()
    {
      attachInterface(this, "android.app.ISearchManager");
    }
    
    public static ISearchManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.ISearchManager");
      if ((localIInterface != null) && ((localIInterface instanceof ISearchManager))) {
        return (ISearchManager)localIInterface;
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
        String str = null;
        Object localObject1 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("android.app.ISearchManager");
          str = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = launchLegacyAssist(str, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.ISearchManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          launchAssist(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.ISearchManager");
          paramParcel1 = getWebSearchActivity();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.ISearchManager");
          paramParcel1 = getGlobalSearchActivity();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.ISearchManager");
          paramParcel1 = getGlobalSearchActivities();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.ISearchManager");
          paramParcel1 = getSearchablesInGlobalSearch();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.app.ISearchManager");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        paramParcel1 = getSearchableInfo(paramParcel1);
        paramParcel2.writeNoException();
        if (paramParcel1 != null)
        {
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
        }
        else
        {
          paramParcel2.writeInt(0);
        }
        return true;
      }
      paramParcel2.writeString("android.app.ISearchManager");
      return true;
    }
    
    private static class Proxy
      implements ISearchManager
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
      
      public List<ResolveInfo> getGlobalSearchActivities()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.ISearchManager");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ResolveInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getGlobalSearchActivity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.ISearchManager");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.ISearchManager";
      }
      
      public SearchableInfo getSearchableInfo(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.ISearchManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (SearchableInfo)SearchableInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<SearchableInfo> getSearchablesInGlobalSearch()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.ISearchManager");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(SearchableInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getWebSearchActivity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.ISearchManager");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void launchAssist(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.ISearchManager");
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean launchLegacyAssist(String paramString, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.ISearchManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
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
