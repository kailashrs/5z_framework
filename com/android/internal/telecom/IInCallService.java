package com.android.internal.telecom;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.CallAudioState;
import android.telecom.ParcelableCall;

public abstract interface IInCallService
  extends IInterface
{
  public abstract void addCall(ParcelableCall paramParcelableCall)
    throws RemoteException;
  
  public abstract void bringToForeground(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onCallAudioStateChanged(CallAudioState paramCallAudioState)
    throws RemoteException;
  
  public abstract void onCanAddCallChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onConnectionEvent(String paramString1, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onHandoverComplete(String paramString)
    throws RemoteException;
  
  public abstract void onHandoverFailed(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onRttInitiationFailure(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onRttUpgradeRequest(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setInCallAdapter(IInCallAdapter paramIInCallAdapter)
    throws RemoteException;
  
  public abstract void setPostDial(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setPostDialWait(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void silenceRinger()
    throws RemoteException;
  
  public abstract void updateCall(ParcelableCall paramParcelableCall)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInCallService
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.IInCallService";
    static final int TRANSACTION_addCall = 2;
    static final int TRANSACTION_bringToForeground = 7;
    static final int TRANSACTION_onCallAudioStateChanged = 6;
    static final int TRANSACTION_onCanAddCallChanged = 8;
    static final int TRANSACTION_onConnectionEvent = 10;
    static final int TRANSACTION_onHandoverComplete = 14;
    static final int TRANSACTION_onHandoverFailed = 13;
    static final int TRANSACTION_onRttInitiationFailure = 12;
    static final int TRANSACTION_onRttUpgradeRequest = 11;
    static final int TRANSACTION_setInCallAdapter = 1;
    static final int TRANSACTION_setPostDial = 4;
    static final int TRANSACTION_setPostDialWait = 5;
    static final int TRANSACTION_silenceRinger = 9;
    static final int TRANSACTION_updateCall = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.IInCallService");
    }
    
    public static IInCallService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.IInCallService");
      if ((localIInterface != null) && ((localIInterface instanceof IInCallService))) {
        return (IInCallService)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        String str = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          onHandoverComplete(paramParcel1.readString());
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          onHandoverFailed(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          onRttInitiationFailure(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          onRttUpgradeRequest(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          paramParcel2 = paramParcel1.readString();
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onConnectionEvent(paramParcel2, str, paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          silenceRinger();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onCanAddCallChanged(bool2);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          bringToForeground(bool2);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CallAudioState)CallAudioState.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          onCallAudioStateChanged(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          setPostDialWait(paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          setPostDial(paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelableCall)ParcelableCall.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          updateCall(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelableCall)ParcelableCall.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          addCall(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telecom.IInCallService");
        setInCallAdapter(IInCallAdapter.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("com.android.internal.telecom.IInCallService");
      return true;
    }
    
    private static class Proxy
      implements IInCallService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addCall(ParcelableCall paramParcelableCall)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          if (paramParcelableCall != null)
          {
            localParcel.writeInt(1);
            paramParcelableCall.writeToParcel(localParcel, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void bringToForeground(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telecom.IInCallService";
      }
      
      public void onCallAudioStateChanged(CallAudioState paramCallAudioState)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          if (paramCallAudioState != null)
          {
            localParcel.writeInt(1);
            paramCallAudioState.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCanAddCallChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onConnectionEvent(String paramString1, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onHandoverComplete(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          localParcel.writeString(paramString);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onHandoverFailed(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRttInitiationFailure(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRttUpgradeRequest(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setInCallAdapter(IInCallAdapter paramIInCallAdapter)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          if (paramIInCallAdapter != null) {
            paramIInCallAdapter = paramIInCallAdapter.asBinder();
          } else {
            paramIInCallAdapter = null;
          }
          localParcel.writeStrongBinder(paramIInCallAdapter);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPostDial(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPostDialWait(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void silenceRinger()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updateCall(ParcelableCall paramParcelableCall)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallService");
          if (paramParcelableCall != null)
          {
            localParcel.writeInt(1);
            paramParcelableCall.writeToParcel(localParcel, 0);
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
