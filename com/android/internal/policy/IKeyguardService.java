package com.android.internal.policy;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;

public abstract interface IKeyguardService
  extends IInterface
{
  public abstract void addStateMonitorCallback(IKeyguardStateCallback paramIKeyguardStateCallback)
    throws RemoteException;
  
  public abstract void dismiss(IKeyguardDismissCallback paramIKeyguardDismissCallback, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void doKeyguardTimeout(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onBootCompleted()
    throws RemoteException;
  
  public abstract void onDreamingStarted()
    throws RemoteException;
  
  public abstract void onDreamingStopped()
    throws RemoteException;
  
  public abstract void onFinishedGoingToSleep(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onFinishedWakingUp()
    throws RemoteException;
  
  public abstract void onScreenTurnedOff()
    throws RemoteException;
  
  public abstract void onScreenTurnedOn()
    throws RemoteException;
  
  public abstract void onScreenTurningOff()
    throws RemoteException;
  
  public abstract void onScreenTurningOn(IKeyguardDrawnCallback paramIKeyguardDrawnCallback)
    throws RemoteException;
  
  public abstract void onShortPowerPressedGoHome()
    throws RemoteException;
  
  public abstract void onStartedGoingToSleep(int paramInt)
    throws RemoteException;
  
  public abstract void onStartedWakingUp()
    throws RemoteException;
  
  public abstract void onSystemReady()
    throws RemoteException;
  
  public abstract void setCurrentUser(int paramInt)
    throws RemoteException;
  
  public abstract void setKeyguardEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setOccluded(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void setSwitchingUser(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void startKeyguardExitAnimation(long paramLong1, long paramLong2)
    throws RemoteException;
  
  public abstract void verifyUnlock(IKeyguardExitCallback paramIKeyguardExitCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IKeyguardService
  {
    private static final String DESCRIPTOR = "com.android.internal.policy.IKeyguardService";
    static final int TRANSACTION_addStateMonitorCallback = 2;
    static final int TRANSACTION_dismiss = 4;
    static final int TRANSACTION_doKeyguardTimeout = 17;
    static final int TRANSACTION_onBootCompleted = 20;
    static final int TRANSACTION_onDreamingStarted = 5;
    static final int TRANSACTION_onDreamingStopped = 6;
    static final int TRANSACTION_onFinishedGoingToSleep = 8;
    static final int TRANSACTION_onFinishedWakingUp = 10;
    static final int TRANSACTION_onScreenTurnedOff = 14;
    static final int TRANSACTION_onScreenTurnedOn = 12;
    static final int TRANSACTION_onScreenTurningOff = 13;
    static final int TRANSACTION_onScreenTurningOn = 11;
    static final int TRANSACTION_onShortPowerPressedGoHome = 22;
    static final int TRANSACTION_onStartedGoingToSleep = 7;
    static final int TRANSACTION_onStartedWakingUp = 9;
    static final int TRANSACTION_onSystemReady = 16;
    static final int TRANSACTION_setCurrentUser = 19;
    static final int TRANSACTION_setKeyguardEnabled = 15;
    static final int TRANSACTION_setOccluded = 1;
    static final int TRANSACTION_setSwitchingUser = 18;
    static final int TRANSACTION_startKeyguardExitAnimation = 21;
    static final int TRANSACTION_verifyUnlock = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.policy.IKeyguardService");
    }
    
    public static IKeyguardService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.policy.IKeyguardService");
      if ((localIInterface != null) && ((localIInterface instanceof IKeyguardService))) {
        return (IKeyguardService)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onShortPowerPressedGoHome();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          startKeyguardExitAnimation(paramParcel1.readLong(), paramParcel1.readLong());
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onBootCompleted();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          setCurrentUser(paramParcel1.readInt());
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setSwitchingUser(bool4);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          doKeyguardTimeout(paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onSystemReady();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          bool4 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setKeyguardEnabled(bool4);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onScreenTurnedOff();
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onScreenTurningOff();
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onScreenTurnedOn();
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onScreenTurningOn(IKeyguardDrawnCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onFinishedWakingUp();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onStartedWakingUp();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          paramInt1 = paramParcel1.readInt();
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          onFinishedGoingToSleep(paramInt1, bool4);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onStartedGoingToSleep(paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onDreamingStopped();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          onDreamingStarted();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          paramParcel2 = IKeyguardDismissCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          dismiss(paramParcel2, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          verifyUnlock(IKeyguardExitCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
          addStateMonitorCallback(IKeyguardStateCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.policy.IKeyguardService");
        if (paramParcel1.readInt() != 0) {
          bool4 = true;
        } else {
          bool4 = false;
        }
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        }
        setOccluded(bool4, bool3);
        return true;
      }
      paramParcel2.writeString("com.android.internal.policy.IKeyguardService");
      return true;
    }
    
    private static class Proxy
      implements IKeyguardService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addStateMonitorCallback(IKeyguardStateCallback paramIKeyguardStateCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          if (paramIKeyguardStateCallback != null) {
            paramIKeyguardStateCallback = paramIKeyguardStateCallback.asBinder();
          } else {
            paramIKeyguardStateCallback = null;
          }
          localParcel.writeStrongBinder(paramIKeyguardStateCallback);
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
      
      public void dismiss(IKeyguardDismissCallback paramIKeyguardDismissCallback, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          if (paramIKeyguardDismissCallback != null) {
            paramIKeyguardDismissCallback = paramIKeyguardDismissCallback.asBinder();
          } else {
            paramIKeyguardDismissCallback = null;
          }
          localParcel.writeStrongBinder(paramIKeyguardDismissCallback);
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void doKeyguardTimeout(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.policy.IKeyguardService";
      }
      
      public void onBootCompleted()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDreamingStarted()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onDreamingStopped()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFinishedGoingToSleep(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFinishedWakingUp()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onScreenTurnedOff()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onScreenTurnedOn()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onScreenTurningOff()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onScreenTurningOn(IKeyguardDrawnCallback paramIKeyguardDrawnCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          if (paramIKeyguardDrawnCallback != null) {
            paramIKeyguardDrawnCallback = paramIKeyguardDrawnCallback.asBinder();
          } else {
            paramIKeyguardDrawnCallback = null;
          }
          localParcel.writeStrongBinder(paramIKeyguardDrawnCallback);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onShortPowerPressedGoHome()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStartedGoingToSleep(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          localParcel.writeInt(paramInt);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStartedWakingUp()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSystemReady()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setCurrentUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          localParcel.writeInt(paramInt);
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setKeyguardEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setOccluded(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setSwitchingUser(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startKeyguardExitAnimation(long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          localParcel.writeLong(paramLong1);
          localParcel.writeLong(paramLong2);
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void verifyUnlock(IKeyguardExitCallback paramIKeyguardExitCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.policy.IKeyguardService");
          if (paramIKeyguardExitCallback != null) {
            paramIKeyguardExitCallback = paramIKeyguardExitCallback.asBinder();
          } else {
            paramIKeyguardExitCallback = null;
          }
          localParcel.writeStrongBinder(paramIKeyguardExitCallback);
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
