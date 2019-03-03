package android.content.pm;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.List;

public abstract interface IShortcutService
  extends IInterface
{
  public abstract boolean addDynamicShortcuts(String paramString, ParceledListSlice paramParceledListSlice, int paramInt)
    throws RemoteException;
  
  public abstract void applyRestore(byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract Intent createShortcutResultIntent(String paramString, ShortcutInfo paramShortcutInfo, int paramInt)
    throws RemoteException;
  
  public abstract void disableShortcuts(String paramString, List paramList, CharSequence paramCharSequence, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void enableShortcuts(String paramString, List paramList, int paramInt)
    throws RemoteException;
  
  public abstract byte[] getBackupPayload(int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getDynamicShortcuts(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getIconMaxDimensions(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getManifestShortcuts(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getMaxShortcutCountPerActivity(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getPinnedShortcuts(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract long getRateLimitResetTime(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getRemainingCallCount(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isRequestPinItemSupported(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onApplicationActive(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void removeAllDynamicShortcuts(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void removeDynamicShortcuts(String paramString, List paramList, int paramInt)
    throws RemoteException;
  
  public abstract void reportShortcutUsed(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract boolean requestPinShortcut(String paramString, ShortcutInfo paramShortcutInfo, IntentSender paramIntentSender, int paramInt)
    throws RemoteException;
  
  public abstract void resetThrottling()
    throws RemoteException;
  
  public abstract boolean setDynamicShortcuts(String paramString, ParceledListSlice paramParceledListSlice, int paramInt)
    throws RemoteException;
  
  public abstract boolean updateShortcuts(String paramString, ParceledListSlice paramParceledListSlice, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IShortcutService
  {
    private static final String DESCRIPTOR = "android.content.pm.IShortcutService";
    static final int TRANSACTION_addDynamicShortcuts = 4;
    static final int TRANSACTION_applyRestore = 21;
    static final int TRANSACTION_createShortcutResultIntent = 10;
    static final int TRANSACTION_disableShortcuts = 11;
    static final int TRANSACTION_enableShortcuts = 12;
    static final int TRANSACTION_getBackupPayload = 20;
    static final int TRANSACTION_getDynamicShortcuts = 2;
    static final int TRANSACTION_getIconMaxDimensions = 16;
    static final int TRANSACTION_getManifestShortcuts = 3;
    static final int TRANSACTION_getMaxShortcutCountPerActivity = 13;
    static final int TRANSACTION_getPinnedShortcuts = 7;
    static final int TRANSACTION_getRateLimitResetTime = 15;
    static final int TRANSACTION_getRemainingCallCount = 14;
    static final int TRANSACTION_isRequestPinItemSupported = 22;
    static final int TRANSACTION_onApplicationActive = 19;
    static final int TRANSACTION_removeAllDynamicShortcuts = 6;
    static final int TRANSACTION_removeDynamicShortcuts = 5;
    static final int TRANSACTION_reportShortcutUsed = 17;
    static final int TRANSACTION_requestPinShortcut = 9;
    static final int TRANSACTION_resetThrottling = 18;
    static final int TRANSACTION_setDynamicShortcuts = 1;
    static final int TRANSACTION_updateShortcuts = 8;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IShortcutService");
    }
    
    public static IShortcutService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IShortcutService");
      if ((localIInterface != null) && ((localIInterface instanceof IShortcutService))) {
        return (IShortcutService)localIInterface;
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
        String str = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 22: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          paramInt1 = isRequestPinItemSupported(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          applyRestore(paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          paramParcel1 = getBackupPayload(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          onApplicationActive(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          resetThrottling();
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          reportShortcutUsed(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          paramInt1 = getIconMaxDimensions(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          long l = getRateLimitResetTime(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          paramInt1 = getRemainingCallCount(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          paramInt1 = getMaxShortcutCountPerActivity(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          enableShortcuts(paramParcel1.readString(), paramParcel1.readArrayList(getClass().getClassLoader()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          str = paramParcel1.readString();
          localObject1 = paramParcel1.readArrayList(getClass().getClassLoader());
          if (paramParcel1.readInt() != 0) {
            localObject4 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          disableShortcuts(str, (List)localObject1, (CharSequence)localObject4, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ShortcutInfo)ShortcutInfo.CREATOR.createFromParcel(paramParcel1);
          }
          paramParcel1 = createShortcutResultIntent((String)localObject1, (ShortcutInfo)localObject4, paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ShortcutInfo)ShortcutInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);
          }
          paramInt1 = requestPinShortcut(str, (ShortcutInfo)localObject4, (IntentSender)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = str;
          }
          paramInt1 = updateShortcuts((String)localObject1, (ParceledListSlice)localObject4, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          paramParcel1 = getPinnedShortcuts(paramParcel1.readString(), paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          removeAllDynamicShortcuts(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          removeDynamicShortcuts(paramParcel1.readString(), paramParcel1.readArrayList(getClass().getClassLoader()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject2;
          }
          paramInt1 = addDynamicShortcuts((String)localObject1, (ParceledListSlice)localObject4, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          paramParcel1 = getManifestShortcuts(paramParcel1.readString(), paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.content.pm.IShortcutService");
          paramParcel1 = getDynamicShortcuts(paramParcel1.readString(), paramParcel1.readInt());
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
        paramParcel1.enforceInterface("android.content.pm.IShortcutService");
        localObject1 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          localObject4 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject4 = localObject3;
        }
        paramInt1 = setDynamicShortcuts((String)localObject1, (ParceledListSlice)localObject4, paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.content.pm.IShortcutService");
      return true;
    }
    
    private static class Proxy
      implements IShortcutService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean addDynamicShortcuts(String paramString, ParceledListSlice paramParceledListSlice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public void applyRestore(byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
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
      
      public Intent createShortcutResultIntent(String paramString, ShortcutInfo paramShortcutInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          if (paramShortcutInfo != null)
          {
            localParcel1.writeInt(1);
            paramShortcutInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
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
      
      public void disableShortcuts(String paramString, List paramList, CharSequence paramCharSequence, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeList(paramList);
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void enableShortcuts(String paramString, List paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeList(paramList);
          localParcel1.writeInt(paramInt);
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
      
      public byte[] getBackupPayload(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getDynamicShortcuts(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public int getIconMaxDimensions(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.content.pm.IShortcutService";
      }
      
      public ParceledListSlice getManifestShortcuts(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public int getMaxShortcutCountPerActivity(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getPinnedShortcuts(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
      
      public long getRateLimitResetTime(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
      
      public int getRemainingCallCount(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
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
      
      public boolean isRequestPinItemSupported(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
      
      public void onApplicationActive(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeAllDynamicShortcuts(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void removeDynamicShortcuts(String paramString, List paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          localParcel1.writeList(paramList);
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
      
      public void reportShortcutUsed(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean requestPinShortcut(String paramString, ShortcutInfo paramShortcutInfo, IntentSender paramIntentSender, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramShortcutInfo != null)
          {
            localParcel1.writeInt(1);
            paramShortcutInfo.writeToParcel(localParcel1, 0);
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
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public void resetThrottling()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setDynamicShortcuts(String paramString, ParceledListSlice paramParceledListSlice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public boolean updateShortcuts(String paramString, ParceledListSlice paramParceledListSlice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IShortcutService");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
