package com.android.internal.appwidget;

import android.app.IApplicationThread;
import android.app.IApplicationThread.Stub;
import android.app.IServiceConnection;
import android.app.IServiceConnection.Stub;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.widget.RemoteViews;

public abstract interface IAppWidgetService
  extends IInterface
{
  public abstract int allocateAppWidgetId(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean bindAppWidgetId(String paramString, int paramInt1, int paramInt2, ComponentName paramComponentName, Bundle paramBundle)
    throws RemoteException;
  
  public abstract boolean bindRemoteViewsService(String paramString, int paramInt1, Intent paramIntent, IApplicationThread paramIApplicationThread, IBinder paramIBinder, IServiceConnection paramIServiceConnection, int paramInt2)
    throws RemoteException;
  
  public abstract IntentSender createAppWidgetConfigIntentSender(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void deleteAllHosts()
    throws RemoteException;
  
  public abstract void deleteAppWidgetId(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void deleteHost(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int[] getAppWidgetIds(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract int[] getAppWidgetIdsForHost(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract AppWidgetProviderInfo getAppWidgetInfo(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract Bundle getAppWidgetOptions(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract RemoteViews getAppWidgetViews(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getInstalledProvidersForProfile(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract boolean hasBindAppWidgetPermission(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isBoundWidgetPackage(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isRequestPinAppWidgetSupported()
    throws RemoteException;
  
  public abstract void notifyAppWidgetViewDataChanged(String paramString, int[] paramArrayOfInt, int paramInt)
    throws RemoteException;
  
  public abstract void partiallyUpdateAppWidgetIds(String paramString, int[] paramArrayOfInt, RemoteViews paramRemoteViews)
    throws RemoteException;
  
  public abstract boolean requestPinAppWidget(String paramString, ComponentName paramComponentName, Bundle paramBundle, IntentSender paramIntentSender)
    throws RemoteException;
  
  public abstract void setBindAppWidgetPermission(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract ParceledListSlice startListening(IAppWidgetHost paramIAppWidgetHost, String paramString, int paramInt, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void stopListening(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void updateAppWidgetIds(String paramString, int[] paramArrayOfInt, RemoteViews paramRemoteViews)
    throws RemoteException;
  
  public abstract void updateAppWidgetOptions(String paramString, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void updateAppWidgetProvider(ComponentName paramComponentName, RemoteViews paramRemoteViews)
    throws RemoteException;
  
  public abstract void updateAppWidgetProviderInfo(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAppWidgetService
  {
    private static final String DESCRIPTOR = "com.android.internal.appwidget.IAppWidgetService";
    static final int TRANSACTION_allocateAppWidgetId = 3;
    static final int TRANSACTION_bindAppWidgetId = 21;
    static final int TRANSACTION_bindRemoteViewsService = 22;
    static final int TRANSACTION_createAppWidgetConfigIntentSender = 9;
    static final int TRANSACTION_deleteAllHosts = 6;
    static final int TRANSACTION_deleteAppWidgetId = 4;
    static final int TRANSACTION_deleteHost = 5;
    static final int TRANSACTION_getAppWidgetIds = 23;
    static final int TRANSACTION_getAppWidgetIdsForHost = 8;
    static final int TRANSACTION_getAppWidgetInfo = 18;
    static final int TRANSACTION_getAppWidgetOptions = 12;
    static final int TRANSACTION_getAppWidgetViews = 7;
    static final int TRANSACTION_getInstalledProvidersForProfile = 17;
    static final int TRANSACTION_hasBindAppWidgetPermission = 19;
    static final int TRANSACTION_isBoundWidgetPackage = 24;
    static final int TRANSACTION_isRequestPinAppWidgetSupported = 26;
    static final int TRANSACTION_notifyAppWidgetViewDataChanged = 16;
    static final int TRANSACTION_partiallyUpdateAppWidgetIds = 13;
    static final int TRANSACTION_requestPinAppWidget = 25;
    static final int TRANSACTION_setBindAppWidgetPermission = 20;
    static final int TRANSACTION_startListening = 1;
    static final int TRANSACTION_stopListening = 2;
    static final int TRANSACTION_updateAppWidgetIds = 10;
    static final int TRANSACTION_updateAppWidgetOptions = 11;
    static final int TRANSACTION_updateAppWidgetProvider = 14;
    static final int TRANSACTION_updateAppWidgetProviderInfo = 15;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.appwidget.IAppWidgetService");
    }
    
    public static IAppWidgetService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.appwidget.IAppWidgetService");
      if ((localIInterface != null) && ((localIInterface instanceof IAppWidgetService))) {
        return (IAppWidgetService)localIInterface;
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
        boolean bool = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        String str = null;
        int[] arrayOfInt = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramInt1 = isRequestPinAppWidgetSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = arrayOfInt;
          }
          paramInt1 = requestPinAppWidget(str, (ComponentName)localObject4, (Bundle)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramInt1 = isBoundWidgetPackage(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramParcel1 = getAppWidgetIds(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          localObject3 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          paramInt1 = bindRemoteViewsService((String)localObject3, paramInt1, (Intent)localObject4, IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readStrongBinder(), IServiceConnection.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          localObject3 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          paramInt1 = bindAppWidgetId((String)localObject3, paramInt2, paramInt1, (ComponentName)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          localObject4 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setBindAppWidgetPermission((String)localObject4, paramInt1, bool);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramInt1 = hasBindAppWidgetPermission(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramParcel1 = getAppWidgetInfo(paramParcel1.readString(), paramParcel1.readInt());
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
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramParcel1 = getInstalledProvidersForProfile(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
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
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          notifyAppWidgetViewDataChanged(paramParcel1.readString(), paramParcel1.createIntArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject2;
          }
          updateAppWidgetProviderInfo((ComponentName)localObject4, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RemoteViews)RemoteViews.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject3;
          }
          updateAppWidgetProvider((ComponentName)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          localObject3 = paramParcel1.readString();
          arrayOfInt = paramParcel1.createIntArray();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RemoteViews)RemoteViews.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject4;
          }
          partiallyUpdateAppWidgetIds((String)localObject3, arrayOfInt, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramParcel1 = getAppWidgetOptions(paramParcel1.readString(), paramParcel1.readInt());
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
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          localObject4 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          updateAppWidgetOptions((String)localObject4, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          localObject4 = paramParcel1.readString();
          localObject3 = paramParcel1.createIntArray();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RemoteViews)RemoteViews.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          updateAppWidgetIds((String)localObject4, (int[])localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramParcel1 = createAppWidgetConfigIntentSender(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramParcel1 = getAppWidgetIdsForHost(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramParcel1 = getAppWidgetViews(paramParcel1.readString(), paramParcel1.readInt());
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
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          deleteAllHosts();
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          deleteHost(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          deleteAppWidgetId(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          paramInt1 = allocateAppWidgetId(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
          stopListening(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.appwidget.IAppWidgetService");
        paramParcel1 = startListening(IAppWidgetHost.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.createIntArray());
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
      paramParcel2.writeString("com.android.internal.appwidget.IAppWidgetService");
      return true;
    }
    
    private static class Proxy
      implements IAppWidgetService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int allocateAppWidgetId(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean bindAppWidgetId(String paramString, int paramInt1, int paramInt2, ComponentName paramComponentName, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
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
      
      public boolean bindRemoteViewsService(String paramString, int paramInt1, Intent paramIntent, IApplicationThread paramIApplicationThread, IBinder paramIBinder, IServiceConnection paramIServiceConnection, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          boolean bool = true;
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          paramIntent = null;
          if (paramIApplicationThread != null) {
            paramString = paramIApplicationThread.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeStrongBinder(paramIBinder);
          paramString = paramIntent;
          if (paramIServiceConnection != null) {
            paramString = paramIServiceConnection.asBinder();
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
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
      
      public IntentSender createAppWidgetConfigIntentSender(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (IntentSender)IntentSender.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void deleteAllHosts()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
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
      
      public void deleteAppWidgetId(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void deleteHost(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getAppWidgetIds(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createIntArray();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getAppWidgetIdsForHost(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createIntArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public AppWidgetProviderInfo getAppWidgetInfo(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (AppWidgetProviderInfo)AppWidgetProviderInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getAppWidgetOptions(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public RemoteViews getAppWidgetViews(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (RemoteViews)RemoteViews.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getInstalledProvidersForProfile(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.appwidget.IAppWidgetService";
      }
      
      public boolean hasBindAppWidgetPermission(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public boolean isBoundWidgetPackage(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public boolean isRequestPinAppWidgetSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(26, localParcel1, localParcel2, 0);
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
      
      public void notifyAppWidgetViewDataChanged(String paramString, int[] paramArrayOfInt, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeIntArray(paramArrayOfInt);
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void partiallyUpdateAppWidgetIds(String paramString, int[] paramArrayOfInt, RemoteViews paramRemoteViews)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeIntArray(paramArrayOfInt);
          if (paramRemoteViews != null)
          {
            localParcel1.writeInt(1);
            paramRemoteViews.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean requestPinAppWidget(String paramString, ComponentName paramComponentName, Bundle paramBundle, IntentSender paramIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIntentSender != null)
          {
            localParcel1.writeInt(1);
            paramIntentSender.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
      
      public void setBindAppWidgetPermission(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice startListening(IAppWidgetHost paramIAppWidgetHost, String paramString, int paramInt, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          Object localObject = null;
          if (paramIAppWidgetHost != null) {
            paramIAppWidgetHost = paramIAppWidgetHost.asBinder();
          } else {
            paramIAppWidgetHost = null;
          }
          localParcel1.writeStrongBinder(paramIAppWidgetHost);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIAppWidgetHost = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIAppWidgetHost = localObject;
          }
          return paramIAppWidgetHost;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopListening(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateAppWidgetIds(String paramString, int[] paramArrayOfInt, RemoteViews paramRemoteViews)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeIntArray(paramArrayOfInt);
          if (paramRemoteViews != null)
          {
            localParcel1.writeInt(1);
            paramRemoteViews.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateAppWidgetOptions(String paramString, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateAppWidgetProvider(ComponentName paramComponentName, RemoteViews paramRemoteViews)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRemoteViews != null)
          {
            localParcel1.writeInt(1);
            paramRemoteViews.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateAppWidgetProviderInfo(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.appwidget.IAppWidgetService");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
