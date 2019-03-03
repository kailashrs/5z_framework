package android.nfc;

import android.content.ComponentName;
import android.nfc.cardemulation.AidGroup;
import android.nfc.cardemulation.ApduServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface INfcCardEmulation
  extends IInterface
{
  public abstract AidGroup getAidGroupForService(int paramInt, ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract List<ApduServiceInfo> getServices(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isDefaultServiceForAid(int paramInt, ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract boolean isDefaultServiceForCategory(int paramInt, ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract boolean registerAidGroupForService(int paramInt, ComponentName paramComponentName, AidGroup paramAidGroup)
    throws RemoteException;
  
  public abstract boolean removeAidGroupForService(int paramInt, ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract boolean setDefaultForNextTap(int paramInt, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean setDefaultServiceForCategory(int paramInt, ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract boolean setPreferredService(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean supportsAidPrefixRegistration()
    throws RemoteException;
  
  public abstract boolean unsetPreferredService()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INfcCardEmulation
  {
    private static final String DESCRIPTOR = "android.nfc.INfcCardEmulation";
    static final int TRANSACTION_getAidGroupForService = 6;
    static final int TRANSACTION_getServices = 8;
    static final int TRANSACTION_isDefaultServiceForAid = 2;
    static final int TRANSACTION_isDefaultServiceForCategory = 1;
    static final int TRANSACTION_registerAidGroupForService = 5;
    static final int TRANSACTION_removeAidGroupForService = 7;
    static final int TRANSACTION_setDefaultForNextTap = 4;
    static final int TRANSACTION_setDefaultServiceForCategory = 3;
    static final int TRANSACTION_setPreferredService = 9;
    static final int TRANSACTION_supportsAidPrefixRegistration = 11;
    static final int TRANSACTION_unsetPreferredService = 10;
    
    public Stub()
    {
      attachInterface(this, "android.nfc.INfcCardEmulation");
    }
    
    public static INfcCardEmulation asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.nfc.INfcCardEmulation");
      if ((localIInterface != null) && ((localIInterface instanceof INfcCardEmulation))) {
        return (INfcCardEmulation)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          paramInt1 = supportsAidPrefixRegistration();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          paramInt1 = unsetPreferredService();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject8;
          }
          paramInt1 = setPreferredService(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          paramParcel1 = getServices(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject8 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject1;
          }
          paramInt1 = removeAidGroupForService(paramInt1, (ComponentName)localObject8, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject8 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject2;
          }
          paramParcel1 = getAidGroupForService(paramInt1, (ComponentName)localObject8, paramParcel1.readString());
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
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject8 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AidGroup)AidGroup.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = registerAidGroupForService(paramInt1, (ComponentName)localObject8, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramInt1 = setDefaultForNextTap(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject8 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject5;
          }
          paramInt1 = setDefaultServiceForCategory(paramInt1, (ComponentName)localObject8, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject8 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject6;
          }
          paramInt1 = isDefaultServiceForAid(paramInt1, (ComponentName)localObject8, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.nfc.INfcCardEmulation");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          localObject8 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject8 = localObject7;
        }
        paramInt1 = isDefaultServiceForCategory(paramInt1, (ComponentName)localObject8, paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.nfc.INfcCardEmulation");
      return true;
    }
    
    private static class Proxy
      implements INfcCardEmulation
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
      
      public AidGroup getAidGroupForService(int paramInt, ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
          localParcel1.writeInt(paramInt);
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
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (AidGroup)AidGroup.CREATOR.createFromParcel(localParcel2);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.nfc.INfcCardEmulation";
      }
      
      public List<ApduServiceInfo> getServices(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(ApduServiceInfo.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isDefaultServiceForAid(int paramInt, ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
          localParcel1.writeInt(paramInt);
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
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public boolean isDefaultServiceForCategory(int paramInt, ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
          localParcel1.writeInt(paramInt);
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
          localParcel1.writeString(paramString);
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
      
      public boolean registerAidGroupForService(int paramInt, ComponentName paramComponentName, AidGroup paramAidGroup)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
          localParcel1.writeInt(paramInt);
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
          if (paramAidGroup != null)
          {
            localParcel1.writeInt(1);
            paramAidGroup.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public boolean removeAidGroupForService(int paramInt, ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
          localParcel1.writeInt(paramInt);
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
          localParcel1.writeString(paramString);
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
      
      public boolean setDefaultForNextTap(int paramInt, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
          localParcel1.writeInt(paramInt);
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
      
      public boolean setDefaultServiceForCategory(int paramInt, ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
          localParcel1.writeInt(paramInt);
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
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public boolean setPreferredService(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
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
      
      public boolean supportsAidPrefixRegistration()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(11, localParcel1, localParcel2, 0);
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
      
      public boolean unsetPreferredService()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcCardEmulation");
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
    }
  }
}
