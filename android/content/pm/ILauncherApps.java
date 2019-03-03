package android.content.pm;

import android.app.IApplicationThread;
import android.app.IApplicationThread.Stub;
import android.content.ComponentName;
import android.content.IntentSender;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;
import java.util.List;

public abstract interface ILauncherApps
  extends IInterface
{
  public abstract void addOnAppsChangedListener(String paramString, IOnAppsChangedListener paramIOnAppsChangedListener)
    throws RemoteException;
  
  public abstract ApplicationInfo getApplicationInfo(String paramString1, String paramString2, int paramInt, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract ParceledListSlice getLauncherActivities(String paramString1, String paramString2, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract ParceledListSlice getShortcutConfigActivities(String paramString1, String paramString2, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract IntentSender getShortcutConfigActivityIntent(String paramString, ComponentName paramComponentName, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor getShortcutIconFd(String paramString1, String paramString2, String paramString3, int paramInt)
    throws RemoteException;
  
  public abstract int getShortcutIconResId(String paramString1, String paramString2, String paramString3, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getShortcuts(String paramString1, long paramLong, String paramString2, List paramList, ComponentName paramComponentName, int paramInt, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract Bundle getSuspendedPackageLauncherExtras(String paramString, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract boolean hasShortcutHostPermission(String paramString)
    throws RemoteException;
  
  public abstract boolean isActivityEnabled(String paramString, ComponentName paramComponentName, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract boolean isPackageEnabled(String paramString1, String paramString2, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract void pinShortcuts(String paramString1, String paramString2, List<String> paramList, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract void removeOnAppsChangedListener(IOnAppsChangedListener paramIOnAppsChangedListener)
    throws RemoteException;
  
  public abstract ActivityInfo resolveActivity(String paramString, ComponentName paramComponentName, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract void showAppDetailsAsUser(IApplicationThread paramIApplicationThread, String paramString, ComponentName paramComponentName, Rect paramRect, Bundle paramBundle, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract void startActivityAsUser(IApplicationThread paramIApplicationThread, String paramString, ComponentName paramComponentName, Rect paramRect, Bundle paramBundle, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract boolean startShortcut(String paramString1, String paramString2, String paramString3, Rect paramRect, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILauncherApps
  {
    private static final String DESCRIPTOR = "android.content.pm.ILauncherApps";
    static final int TRANSACTION_addOnAppsChangedListener = 1;
    static final int TRANSACTION_getApplicationInfo = 10;
    static final int TRANSACTION_getLauncherActivities = 3;
    static final int TRANSACTION_getShortcutConfigActivities = 17;
    static final int TRANSACTION_getShortcutConfigActivityIntent = 18;
    static final int TRANSACTION_getShortcutIconFd = 15;
    static final int TRANSACTION_getShortcutIconResId = 14;
    static final int TRANSACTION_getShortcuts = 11;
    static final int TRANSACTION_getSuspendedPackageLauncherExtras = 8;
    static final int TRANSACTION_hasShortcutHostPermission = 16;
    static final int TRANSACTION_isActivityEnabled = 9;
    static final int TRANSACTION_isPackageEnabled = 7;
    static final int TRANSACTION_pinShortcuts = 12;
    static final int TRANSACTION_removeOnAppsChangedListener = 2;
    static final int TRANSACTION_resolveActivity = 4;
    static final int TRANSACTION_showAppDetailsAsUser = 6;
    static final int TRANSACTION_startActivityAsUser = 5;
    static final int TRANSACTION_startShortcut = 13;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.ILauncherApps");
    }
    
    public static ILauncherApps asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.ILauncherApps");
      if ((localIInterface != null) && ((localIInterface instanceof ILauncherApps))) {
        return (ILauncherApps)localIInterface;
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
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        String str = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 18: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject12;
          }
          paramParcel1 = getShortcutConfigActivityIntent((String)localObject2, (ComponentName)localObject3, paramParcel1);
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
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject2 = paramParcel1.readString();
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramParcel1 = getShortcutConfigActivities((String)localObject2, (String)localObject3, paramParcel1);
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
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          paramInt1 = hasShortcutHostPermission(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          paramParcel1 = getShortcutIconFd(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
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
        case 14: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          paramInt1 = getShortcutIconResId(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject9 = paramParcel1.readString();
          str = paramParcel1.readString();
          localObject4 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramInt1 = startShortcut((String)localObject9, str, (String)localObject4, (Rect)localObject3, (Bundle)localObject2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject2 = paramParcel1.readString();
          str = paramParcel1.readString();
          localObject4 = paramParcel1.createStringArrayList();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject3;
          }
          pinShortcuts((String)localObject2, str, (List)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          str = paramParcel1.readString();
          long l = paramParcel1.readLong();
          localObject2 = paramParcel1.readString();
          localObject9 = paramParcel1.readArrayList(getClass().getClassLoader());
          if (paramParcel1.readInt() != 0) {
            localObject3 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject4) {
            break;
          }
          paramParcel1 = getShortcuts(str, l, (String)localObject2, (List)localObject9, (ComponentName)localObject3, paramInt1, paramParcel1);
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
        case 10: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject2 = paramParcel1.readString();
          localObject3 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          paramParcel1 = getApplicationInfo((String)localObject2, (String)localObject3, paramInt1, paramParcel1);
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
        case 9: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          paramInt1 = isActivityEnabled((String)localObject2, (ComponentName)localObject3, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramParcel1 = getSuspendedPackageLauncherExtras((String)localObject3, paramParcel1);
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
        case 7: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject3 = paramParcel1.readString();
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          paramInt1 = isPackageEnabled((String)localObject3, (String)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject9 = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject12 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject4 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = str) {
            break;
          }
          showAppDetailsAsUser((IApplicationThread)localObject9, (String)localObject12, (ComponentName)localObject3, (Rect)localObject2, (Bundle)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject12 = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject4 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject9) {
            break;
          }
          startActivityAsUser((IApplicationThread)localObject12, str, (ComponentName)localObject3, (Rect)localObject2, (Bundle)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          paramParcel1 = resolveActivity((String)localObject2, (ComponentName)localObject3, paramParcel1);
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
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          localObject3 = paramParcel1.readString();
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          paramParcel1 = getLauncherActivities((String)localObject3, (String)localObject2, paramParcel1);
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
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
          removeOnAppsChangedListener(IOnAppsChangedListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.ILauncherApps");
        addOnAppsChangedListener(paramParcel1.readString(), IOnAppsChangedListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.content.pm.ILauncherApps");
      return true;
    }
    
    private static class Proxy
      implements ILauncherApps
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addOnAppsChangedListener(String paramString, IOnAppsChangedListener paramIOnAppsChangedListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString);
          if (paramIOnAppsChangedListener != null) {
            paramString = paramIOnAppsChangedListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public ApplicationInfo getApplicationInfo(String paramString1, String paramString2, int paramInt, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.content.pm.ILauncherApps";
      }
      
      public ParceledListSlice getLauncherActivities(String paramString1, String paramString2, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getShortcutConfigActivities(String paramString1, String paramString2, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IntentSender getShortcutConfigActivityIntent(String paramString, ComponentName paramComponentName, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
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
          mRemote.transact(18, localParcel1, localParcel2, 0);
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
      
      public ParcelFileDescriptor getShortcutIconFd(String paramString1, String paramString2, String paramString3, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getShortcutIconResId(String paramString1, String paramString2, String paramString3, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt);
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getShortcuts(String paramString1, long paramLong, String paramString2, List paramList, ComponentName paramComponentName, int paramInt, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString1);
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString2);
          localParcel1.writeList(paramList);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getSuspendedPackageLauncherExtras(String paramString, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public boolean hasShortcutHostPermission(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(16, localParcel1, localParcel2, 0);
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
      
      public boolean isActivityEnabled(String paramString, ComponentName paramComponentName, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
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
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public boolean isPackageEnabled(String paramString1, String paramString2, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          boolean bool = true;
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
      
      public void pinShortcuts(String paramString1, String paramString2, List<String> paramList, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeStringList(paramList);
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeOnAppsChangedListener(IOnAppsChangedListener paramIOnAppsChangedListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          if (paramIOnAppsChangedListener != null) {
            paramIOnAppsChangedListener = paramIOnAppsChangedListener.asBinder();
          } else {
            paramIOnAppsChangedListener = null;
          }
          localParcel1.writeStrongBinder(paramIOnAppsChangedListener);
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
      
      public ActivityInfo resolveActivity(String paramString, ComponentName paramComponentName, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
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
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public void showAppDetailsAsUser(IApplicationThread paramIApplicationThread, String paramString, ComponentName paramComponentName, Rect paramRect, Bundle paramBundle, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
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
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
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
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
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
      
      public void startActivityAsUser(IApplicationThread paramIApplicationThread, String paramString, ComponentName paramComponentName, Rect paramRect, Bundle paramBundle, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
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
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
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
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public boolean startShortcut(String paramString1, String paramString2, String paramString3, Rect paramRect, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.ILauncherApps");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          boolean bool = true;
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
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
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
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
