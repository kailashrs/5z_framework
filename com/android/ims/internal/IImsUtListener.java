package com.android.ims.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;

public abstract interface IImsUtListener
  extends IInterface
{
  public abstract void onSupplementaryServiceIndication(ImsSsData paramImsSsData)
    throws RemoteException;
  
  public abstract void utConfigurationCallBarringQueried(IImsUt paramIImsUt, int paramInt, ImsSsInfo[] paramArrayOfImsSsInfo)
    throws RemoteException;
  
  public abstract void utConfigurationCallForwardQueried(IImsUt paramIImsUt, int paramInt, ImsCallForwardInfo[] paramArrayOfImsCallForwardInfo)
    throws RemoteException;
  
  public abstract void utConfigurationCallWaitingQueried(IImsUt paramIImsUt, int paramInt, ImsSsInfo[] paramArrayOfImsSsInfo)
    throws RemoteException;
  
  public abstract void utConfigurationQueried(IImsUt paramIImsUt, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void utConfigurationQueryFailed(IImsUt paramIImsUt, int paramInt, ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void utConfigurationUpdateFailed(IImsUt paramIImsUt, int paramInt, ImsReasonInfo paramImsReasonInfo)
    throws RemoteException;
  
  public abstract void utConfigurationUpdated(IImsUt paramIImsUt, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsUtListener
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsUtListener";
    static final int TRANSACTION_onSupplementaryServiceIndication = 8;
    static final int TRANSACTION_utConfigurationCallBarringQueried = 5;
    static final int TRANSACTION_utConfigurationCallForwardQueried = 6;
    static final int TRANSACTION_utConfigurationCallWaitingQueried = 7;
    static final int TRANSACTION_utConfigurationQueried = 3;
    static final int TRANSACTION_utConfigurationQueryFailed = 4;
    static final int TRANSACTION_utConfigurationUpdateFailed = 2;
    static final int TRANSACTION_utConfigurationUpdated = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsUtListener");
    }
    
    public static IImsUtListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsUtListener");
      if ((localIInterface != null) && ((localIInterface instanceof IImsUtListener))) {
        return (IImsUtListener)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsUtListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsSsData)ImsSsData.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          onSupplementaryServiceIndication(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsUtListener");
          utConfigurationCallWaitingQueried(IImsUt.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), (ImsSsInfo[])paramParcel1.createTypedArray(ImsSsInfo.CREATOR));
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsUtListener");
          utConfigurationCallForwardQueried(IImsUt.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), (ImsCallForwardInfo[])paramParcel1.createTypedArray(ImsCallForwardInfo.CREATOR));
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsUtListener");
          utConfigurationCallBarringQueried(IImsUt.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), (ImsSsInfo[])paramParcel1.createTypedArray(ImsSsInfo.CREATOR));
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsUtListener");
          paramParcel2 = IImsUt.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          utConfigurationQueryFailed(paramParcel2, paramInt1, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsUtListener");
          paramParcel2 = IImsUt.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          utConfigurationQueried(paramParcel2, paramInt1, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsUtListener");
          paramParcel2 = IImsUt.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsReasonInfo)ImsReasonInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          utConfigurationUpdateFailed(paramParcel2, paramInt1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsUtListener");
        utConfigurationUpdated(IImsUt.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsUtListener");
      return true;
    }
    
    private static class Proxy
      implements IImsUtListener
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
        return "com.android.ims.internal.IImsUtListener";
      }
      
      public void onSupplementaryServiceIndication(ImsSsData paramImsSsData)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsUtListener");
          if (paramImsSsData != null)
          {
            localParcel.writeInt(1);
            paramImsSsData.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void utConfigurationCallBarringQueried(IImsUt paramIImsUt, int paramInt, ImsSsInfo[] paramArrayOfImsSsInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsUtListener");
          if (paramIImsUt != null) {
            paramIImsUt = paramIImsUt.asBinder();
          } else {
            paramIImsUt = null;
          }
          localParcel.writeStrongBinder(paramIImsUt);
          localParcel.writeInt(paramInt);
          localParcel.writeTypedArray(paramArrayOfImsSsInfo, 0);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void utConfigurationCallForwardQueried(IImsUt paramIImsUt, int paramInt, ImsCallForwardInfo[] paramArrayOfImsCallForwardInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsUtListener");
          if (paramIImsUt != null) {
            paramIImsUt = paramIImsUt.asBinder();
          } else {
            paramIImsUt = null;
          }
          localParcel.writeStrongBinder(paramIImsUt);
          localParcel.writeInt(paramInt);
          localParcel.writeTypedArray(paramArrayOfImsCallForwardInfo, 0);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void utConfigurationCallWaitingQueried(IImsUt paramIImsUt, int paramInt, ImsSsInfo[] paramArrayOfImsSsInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsUtListener");
          if (paramIImsUt != null) {
            paramIImsUt = paramIImsUt.asBinder();
          } else {
            paramIImsUt = null;
          }
          localParcel.writeStrongBinder(paramIImsUt);
          localParcel.writeInt(paramInt);
          localParcel.writeTypedArray(paramArrayOfImsSsInfo, 0);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void utConfigurationQueried(IImsUt paramIImsUt, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsUtListener");
          if (paramIImsUt != null) {
            paramIImsUt = paramIImsUt.asBinder();
          } else {
            paramIImsUt = null;
          }
          localParcel.writeStrongBinder(paramIImsUt);
          localParcel.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public void utConfigurationQueryFailed(IImsUt paramIImsUt, int paramInt, ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsUtListener");
          if (paramIImsUt != null) {
            paramIImsUt = paramIImsUt.asBinder();
          } else {
            paramIImsUt = null;
          }
          localParcel.writeStrongBinder(paramIImsUt);
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
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void utConfigurationUpdateFailed(IImsUt paramIImsUt, int paramInt, ImsReasonInfo paramImsReasonInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsUtListener");
          if (paramIImsUt != null) {
            paramIImsUt = paramIImsUt.asBinder();
          } else {
            paramIImsUt = null;
          }
          localParcel.writeStrongBinder(paramIImsUt);
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
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void utConfigurationUpdated(IImsUt paramIImsUt, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsUtListener");
          if (paramIImsUt != null) {
            paramIImsUt = paramIImsUt.asBinder();
          } else {
            paramIImsUt = null;
          }
          localParcel.writeStrongBinder(paramIImsUt);
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
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
