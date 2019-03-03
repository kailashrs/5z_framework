package android.nfc;

import android.app.PendingIntent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface INfcAdapter
  extends IInterface
{
  public abstract void addNfcUnlockHandler(INfcUnlockHandler paramINfcUnlockHandler, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract boolean disable(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean disableNdefPush()
    throws RemoteException;
  
  public abstract void dispatch(Tag paramTag)
    throws RemoteException;
  
  public abstract boolean enable()
    throws RemoteException;
  
  public abstract boolean enableNdefPush()
    throws RemoteException;
  
  public abstract INfcAdapterExtras getNfcAdapterExtrasInterface(String paramString)
    throws RemoteException;
  
  public abstract IBinder getNfcAdapterVendorInterface(String paramString)
    throws RemoteException;
  
  public abstract INfcCardEmulation getNfcCardEmulationInterface()
    throws RemoteException;
  
  public abstract INfcDta getNfcDtaInterface(String paramString)
    throws RemoteException;
  
  public abstract INfcFCardEmulation getNfcFCardEmulationInterface()
    throws RemoteException;
  
  public abstract INfcTag getNfcTagInterface()
    throws RemoteException;
  
  public abstract int getState()
    throws RemoteException;
  
  public abstract boolean ignore(int paramInt1, int paramInt2, ITagRemovedCallback paramITagRemovedCallback)
    throws RemoteException;
  
  public abstract void invokeBeam()
    throws RemoteException;
  
  public abstract void invokeBeamInternal(BeamShareData paramBeamShareData)
    throws RemoteException;
  
  public abstract boolean isNdefPushEnabled()
    throws RemoteException;
  
  public abstract void pausePolling(int paramInt)
    throws RemoteException;
  
  public abstract void removeNfcUnlockHandler(INfcUnlockHandler paramINfcUnlockHandler)
    throws RemoteException;
  
  public abstract void resumePolling()
    throws RemoteException;
  
  public abstract void setAppCallback(IAppCallback paramIAppCallback)
    throws RemoteException;
  
  public abstract void setForegroundDispatch(PendingIntent paramPendingIntent, IntentFilter[] paramArrayOfIntentFilter, TechListParcel paramTechListParcel)
    throws RemoteException;
  
  public abstract void setP2pModes(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setReaderMode(IBinder paramIBinder, IAppCallback paramIAppCallback, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void verifyNfcPermission()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INfcAdapter
  {
    private static final String DESCRIPTOR = "android.nfc.INfcAdapter";
    static final int TRANSACTION_addNfcUnlockHandler = 23;
    static final int TRANSACTION_disable = 8;
    static final int TRANSACTION_disableNdefPush = 11;
    static final int TRANSACTION_dispatch = 20;
    static final int TRANSACTION_enable = 9;
    static final int TRANSACTION_enableNdefPush = 10;
    static final int TRANSACTION_getNfcAdapterExtrasInterface = 4;
    static final int TRANSACTION_getNfcAdapterVendorInterface = 6;
    static final int TRANSACTION_getNfcCardEmulationInterface = 2;
    static final int TRANSACTION_getNfcDtaInterface = 5;
    static final int TRANSACTION_getNfcFCardEmulationInterface = 3;
    static final int TRANSACTION_getNfcTagInterface = 1;
    static final int TRANSACTION_getState = 7;
    static final int TRANSACTION_ignore = 19;
    static final int TRANSACTION_invokeBeam = 17;
    static final int TRANSACTION_invokeBeamInternal = 18;
    static final int TRANSACTION_isNdefPushEnabled = 12;
    static final int TRANSACTION_pausePolling = 13;
    static final int TRANSACTION_removeNfcUnlockHandler = 24;
    static final int TRANSACTION_resumePolling = 14;
    static final int TRANSACTION_setAppCallback = 16;
    static final int TRANSACTION_setForegroundDispatch = 15;
    static final int TRANSACTION_setP2pModes = 22;
    static final int TRANSACTION_setReaderMode = 21;
    static final int TRANSACTION_verifyNfcPermission = 25;
    
    public Stub()
    {
      attachInterface(this, "android.nfc.INfcAdapter");
    }
    
    public static INfcAdapter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.nfc.INfcAdapter");
      if ((localIInterface != null) && ((localIInterface instanceof INfcAdapter))) {
        return (INfcAdapter)localIInterface;
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
        IBinder localIBinder = null;
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
        case 25: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          verifyNfcPermission();
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          removeNfcUnlockHandler(INfcUnlockHandler.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          addNfcUnlockHandler(INfcUnlockHandler.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          setP2pModes(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          localIBinder = paramParcel1.readStrongBinder();
          localObject3 = IAppCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject8;
          }
          setReaderMode(localIBinder, (IAppCallback)localObject3, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Tag)Tag.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          dispatch(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          paramInt1 = ignore(paramParcel1.readInt(), paramParcel1.readInt(), ITagRemovedCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BeamShareData)BeamShareData.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          invokeBeamInternal(paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          invokeBeam();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          setAppCallback(IAppCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = null;
          }
          localObject3 = (IntentFilter[])paramParcel1.createTypedArray(IntentFilter.CREATOR);
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (TechListParcel)TechListParcel.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIBinder;
          }
          setForegroundDispatch((PendingIntent)localObject8, (IntentFilter[])localObject3, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          resumePolling();
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          pausePolling(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          paramInt1 = isNdefPushEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          paramInt1 = disableNdefPush();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          paramInt1 = enableNdefPush();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          paramInt1 = enable();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          paramInt1 = disable(bool);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          paramInt1 = getState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          paramParcel1 = getNfcAdapterVendorInterface(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          localObject8 = getNfcDtaInterface(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject3;
          if (localObject8 != null) {
            paramParcel1 = ((INfcDta)localObject8).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          localObject8 = getNfcAdapterExtrasInterface(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel1 = localObject4;
          if (localObject8 != null) {
            paramParcel1 = ((INfcAdapterExtras)localObject8).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          localObject8 = getNfcFCardEmulationInterface();
          paramParcel2.writeNoException();
          paramParcel1 = localObject5;
          if (localObject8 != null) {
            paramParcel1 = ((INfcFCardEmulation)localObject8).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.nfc.INfcAdapter");
          localObject8 = getNfcCardEmulationInterface();
          paramParcel2.writeNoException();
          paramParcel1 = localObject6;
          if (localObject8 != null) {
            paramParcel1 = ((INfcCardEmulation)localObject8).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.nfc.INfcAdapter");
        localObject8 = getNfcTagInterface();
        paramParcel2.writeNoException();
        paramParcel1 = localObject7;
        if (localObject8 != null) {
          paramParcel1 = ((INfcTag)localObject8).asBinder();
        }
        paramParcel2.writeStrongBinder(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.nfc.INfcAdapter");
      return true;
    }
    
    private static class Proxy
      implements INfcAdapter
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addNfcUnlockHandler(INfcUnlockHandler paramINfcUnlockHandler, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          if (paramINfcUnlockHandler != null) {
            paramINfcUnlockHandler = paramINfcUnlockHandler.asBinder();
          } else {
            paramINfcUnlockHandler = null;
          }
          localParcel1.writeStrongBinder(paramINfcUnlockHandler);
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
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
      
      public boolean disable(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public boolean disableNdefPush()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
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
      
      public void dispatch(Tag paramTag)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          if (paramTag != null)
          {
            localParcel1.writeInt(1);
            paramTag.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public boolean enable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
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
      
      public boolean enableNdefPush()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
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
      
      public String getInterfaceDescriptor()
      {
        return "android.nfc.INfcAdapter";
      }
      
      public INfcAdapterExtras getNfcAdapterExtrasInterface(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = INfcAdapterExtras.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder getNfcAdapterVendorInterface(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readStrongBinder();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public INfcCardEmulation getNfcCardEmulationInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          INfcCardEmulation localINfcCardEmulation = INfcCardEmulation.Stub.asInterface(localParcel2.readStrongBinder());
          return localINfcCardEmulation;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public INfcDta getNfcDtaInterface(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = INfcDta.Stub.asInterface(localParcel2.readStrongBinder());
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public INfcFCardEmulation getNfcFCardEmulationInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          INfcFCardEmulation localINfcFCardEmulation = INfcFCardEmulation.Stub.asInterface(localParcel2.readStrongBinder());
          return localINfcFCardEmulation;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public INfcTag getNfcTagInterface()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          INfcTag localINfcTag = INfcTag.Stub.asInterface(localParcel2.readStrongBinder());
          return localINfcTag;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
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
      
      public boolean ignore(int paramInt1, int paramInt2, ITagRemovedCallback paramITagRemovedCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramITagRemovedCallback != null) {
            paramITagRemovedCallback = paramITagRemovedCallback.asBinder();
          } else {
            paramITagRemovedCallback = null;
          }
          localParcel1.writeStrongBinder(paramITagRemovedCallback);
          paramITagRemovedCallback = mRemote;
          boolean bool = false;
          paramITagRemovedCallback.transact(19, localParcel1, localParcel2, 0);
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
      
      public void invokeBeam()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.nfc.INfcAdapter");
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void invokeBeamInternal(BeamShareData paramBeamShareData)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.nfc.INfcAdapter");
          if (paramBeamShareData != null)
          {
            localParcel.writeInt(1);
            paramBeamShareData.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean isNdefPushEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(12, localParcel1, localParcel2, 0);
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
      
      public void pausePolling(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          localParcel1.writeInt(paramInt);
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
      
      public void removeNfcUnlockHandler(INfcUnlockHandler paramINfcUnlockHandler)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          if (paramINfcUnlockHandler != null) {
            paramINfcUnlockHandler = paramINfcUnlockHandler.asBinder();
          } else {
            paramINfcUnlockHandler = null;
          }
          localParcel1.writeStrongBinder(paramINfcUnlockHandler);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resumePolling()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
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
      
      public void setAppCallback(IAppCallback paramIAppCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          if (paramIAppCallback != null) {
            paramIAppCallback = paramIAppCallback.asBinder();
          } else {
            paramIAppCallback = null;
          }
          localParcel1.writeStrongBinder(paramIAppCallback);
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
      
      public void setForegroundDispatch(PendingIntent paramPendingIntent, IntentFilter[] paramArrayOfIntentFilter, TechListParcel paramTechListParcel)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeTypedArray(paramArrayOfIntentFilter, 0);
          if (paramTechListParcel != null)
          {
            localParcel1.writeInt(1);
            paramTechListParcel.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setP2pModes(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setReaderMode(IBinder paramIBinder, IAppCallback paramIAppCallback, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIAppCallback != null) {
            paramIBinder = paramIAppCallback.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void verifyNfcPermission()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.nfc.INfcAdapter");
          mRemote.transact(25, localParcel1, localParcel2, 0);
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
