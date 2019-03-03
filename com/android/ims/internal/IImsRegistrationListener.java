package com.android.ims.internal;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.ImsReasonInfo;

public abstract interface IImsRegistrationListener
  extends IInterface
{
  public abstract void registrationAssociatedUriChanged(Uri[] paramArrayOfUri)
    throws RemoteException;
  
  public abstract void registrationChangeFailed(int paramInt, ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void registrationConnected()
    throws RemoteException;
  
  public abstract void registrationConnectedWithRadioTech(int paramInt)
    throws RemoteException;
  
  public abstract void registrationDisconnected(ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void registrationFeatureCapabilityChanged(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws RemoteException;
  
  public abstract void registrationProgressing()
    throws RemoteException;
  
  public abstract void registrationProgressingWithRadioTech(int paramInt)
    throws RemoteException;
  
  public abstract void registrationResumed()
    throws RemoteException;
  
  public abstract void registrationServiceCapabilityChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void registrationSuspended()
    throws RemoteException;
  
  public abstract void voiceMessageCountUpdate(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsRegistrationListener
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsRegistrationListener";
    static final int TRANSACTION_registrationAssociatedUriChanged = 11;
    static final int TRANSACTION_registrationChangeFailed = 12;
    static final int TRANSACTION_registrationConnected = 1;
    static final int TRANSACTION_registrationConnectedWithRadioTech = 3;
    static final int TRANSACTION_registrationDisconnected = 5;
    static final int TRANSACTION_registrationFeatureCapabilityChanged = 9;
    static final int TRANSACTION_registrationProgressing = 2;
    static final int TRANSACTION_registrationProgressingWithRadioTech = 4;
    static final int TRANSACTION_registrationResumed = 6;
    static final int TRANSACTION_registrationServiceCapabilityChanged = 8;
    static final int TRANSACTION_registrationSuspended = 7;
    static final int TRANSACTION_voiceMessageCountUpdate = 10;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsRegistrationListener");
    }
    
    public static IImsRegistrationListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsRegistrationListener");
      if ((localIInterface != null) && ((localIInterface instanceof IImsRegistrationListener))) {
        return (IImsRegistrationListener)localIInterface;
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
        case 12: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          registrationChangeFailed(paramInt1, paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          registrationAssociatedUriChanged((Uri[])paramParcel1.createTypedArray(Uri.CREATOR));
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          voiceMessageCountUpdate(paramParcel1.readInt());
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          registrationFeatureCapabilityChanged(paramParcel1.readInt(), paramParcel1.createIntArray(), paramParcel1.createIntArray());
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          registrationServiceCapabilityChanged(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          registrationSuspended();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          registrationResumed();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          registrationDisconnected(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          registrationProgressingWithRadioTech(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          registrationConnectedWithRadioTech(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
          registrationProgressing();
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsRegistrationListener");
        registrationConnected();
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsRegistrationListener");
      return true;
    }
    
    private static class Proxy
      implements IImsRegistrationListener
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
        return "com.android.ims.internal.IImsRegistrationListener";
      }
      
      public void registrationAssociatedUriChanged(Uri[] paramArrayOfUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          localParcel.writeTypedArray(paramArrayOfUri, 0);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationChangeFailed(int paramInt, ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          localParcel.writeInt(paramInt);
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationConnected()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationConnectedWithRadioTech(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationDisconnected(ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          if (paramImsReasonInfo != null)
          {
            localParcel.writeInt(1);
            paramImsReasonInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationFeatureCapabilityChanged(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          localParcel.writeInt(paramInt);
          localParcel.writeIntArray(paramArrayOfInt1);
          localParcel.writeIntArray(paramArrayOfInt2);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationProgressing()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationProgressingWithRadioTech(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationResumed()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationServiceCapabilityChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registrationSuspended()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void voiceMessageCountUpdate(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsRegistrationListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(10, localParcel, null, 1);
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
