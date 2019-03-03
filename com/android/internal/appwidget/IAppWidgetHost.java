package com.android.internal.appwidget;

import android.appwidget.AppWidgetProviderInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.widget.RemoteViews;

public abstract interface IAppWidgetHost
  extends IInterface
{
  public abstract void providerChanged(int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
    throws RemoteException;
  
  public abstract void providersChanged()
    throws RemoteException;
  
  public abstract void updateAppWidget(int paramInt, RemoteViews paramRemoteViews)
    throws RemoteException;
  
  public abstract void viewDataChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAppWidgetHost
  {
    private static final String DESCRIPTOR = "com.android.internal.appwidget.IAppWidgetHost";
    static final int TRANSACTION_providerChanged = 2;
    static final int TRANSACTION_providersChanged = 3;
    static final int TRANSACTION_updateAppWidget = 1;
    static final int TRANSACTION_viewDataChanged = 4;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.appwidget.IAppWidgetHost");
    }
    
    public static IAppWidgetHost asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.appwidget.IAppWidgetHost");
      if ((localIInterface != null) && ((localIInterface instanceof IAppWidgetHost))) {
        return (IAppWidgetHost)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetHost");
          viewDataChanged(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetHost");
          providersChanged();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetHost");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AppWidgetProviderInfo)AppWidgetProviderInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          providerChanged(paramInt1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetHost");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (RemoteViews)RemoteViews.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        updateAppWidget(paramInt1, paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.appwidget.IAppWidgetHost");
      return true;
    }
    
    private static class Proxy
      implements IAppWidgetHost
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
        return "com.android.internal.appwidget.IAppWidgetHost";
      }
      
      public void providerChanged(int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetHost");
          localParcel.writeInt(paramInt);
          if (paramAppWidgetProviderInfo != null)
          {
            localParcel.writeInt(1);
            paramAppWidgetProviderInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void providersChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetHost");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updateAppWidget(int paramInt, RemoteViews paramRemoteViews)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetHost");
          localParcel.writeInt(paramInt);
          if (paramRemoteViews != null)
          {
            localParcel.writeInt(1);
            paramRemoteViews.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void viewDataChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetHost");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(4, localParcel, null, 1);
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
