package com.android.internal.widget;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.widget.RemoteViews;

public abstract interface IRemoteViewsFactory
  extends IInterface
{
  public abstract int getCount()
    throws RemoteException;
  
  public abstract long getItemId(int paramInt)
    throws RemoteException;
  
  public abstract RemoteViews getLoadingView()
    throws RemoteException;
  
  public abstract RemoteViews getViewAt(int paramInt)
    throws RemoteException;
  
  public abstract int getViewTypeCount()
    throws RemoteException;
  
  public abstract boolean hasStableIds()
    throws RemoteException;
  
  public abstract boolean isCreated()
    throws RemoteException;
  
  public abstract void onDataSetChanged()
    throws RemoteException;
  
  public abstract void onDataSetChangedAsync()
    throws RemoteException;
  
  public abstract void onDestroy(Intent paramIntent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRemoteViewsFactory
  {
    private static final String DESCRIPTOR = "com.android.internal.widget.IRemoteViewsFactory";
    static final int TRANSACTION_getCount = 4;
    static final int TRANSACTION_getItemId = 8;
    static final int TRANSACTION_getLoadingView = 6;
    static final int TRANSACTION_getViewAt = 5;
    static final int TRANSACTION_getViewTypeCount = 7;
    static final int TRANSACTION_hasStableIds = 9;
    static final int TRANSACTION_isCreated = 10;
    static final int TRANSACTION_onDataSetChanged = 1;
    static final int TRANSACTION_onDataSetChangedAsync = 2;
    static final int TRANSACTION_onDestroy = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.widget.IRemoteViewsFactory");
    }
    
    public static IRemoteViewsFactory asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.widget.IRemoteViewsFactory");
      if ((localIInterface != null) && ((localIInterface instanceof IRemoteViewsFactory))) {
        return (IRemoteViewsFactory)localIInterface;
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
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
          paramInt1 = isCreated();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
          paramInt1 = hasStableIds();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
          long l = getItemId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
          paramInt1 = getViewTypeCount();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
          paramParcel1 = getLoadingView();
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
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
          paramParcel1 = getViewAt(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
          paramInt1 = getCount();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onDestroy(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
          onDataSetChangedAsync();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.widget.IRemoteViewsFactory");
        onDataSetChanged();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.widget.IRemoteViewsFactory");
      return true;
    }
    
    private static class Proxy
      implements IRemoteViewsFactory
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
      
      public int getCount()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.widget.IRemoteViewsFactory";
      }
      
      public long getItemId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public RemoteViews getLoadingView()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          RemoteViews localRemoteViews;
          if (localParcel2.readInt() != 0) {
            localRemoteViews = (RemoteViews)RemoteViews.CREATOR.createFromParcel(localParcel2);
          } else {
            localRemoteViews = null;
          }
          return localRemoteViews;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public RemoteViews getViewAt(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          RemoteViews localRemoteViews;
          if (localParcel2.readInt() != 0) {
            localRemoteViews = (RemoteViews)RemoteViews.CREATOR.createFromParcel(localParcel2);
          } else {
            localRemoteViews = null;
          }
          return localRemoteViews;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getViewTypeCount()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasStableIds()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isCreated()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onDataSetChanged()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
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
      
      public void onDataSetChangedAsync()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDestroy(Intent paramIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.widget.IRemoteViewsFactory");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
