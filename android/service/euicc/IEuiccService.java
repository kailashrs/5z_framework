package android.service.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.euicc.DownloadableSubscription;

public abstract interface IEuiccService
  extends IInterface
{
  public abstract void deleteSubscription(int paramInt, String paramString, IDeleteSubscriptionCallback paramIDeleteSubscriptionCallback)
    throws RemoteException;
  
  public abstract void downloadSubscription(int paramInt, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean1, boolean paramBoolean2, IDownloadSubscriptionCallback paramIDownloadSubscriptionCallback)
    throws RemoteException;
  
  public abstract void eraseSubscriptions(int paramInt, IEraseSubscriptionsCallback paramIEraseSubscriptionsCallback)
    throws RemoteException;
  
  public abstract void getDefaultDownloadableSubscriptionList(int paramInt, boolean paramBoolean, IGetDefaultDownloadableSubscriptionListCallback paramIGetDefaultDownloadableSubscriptionListCallback)
    throws RemoteException;
  
  public abstract void getDownloadableSubscriptionMetadata(int paramInt, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, IGetDownloadableSubscriptionMetadataCallback paramIGetDownloadableSubscriptionMetadataCallback)
    throws RemoteException;
  
  public abstract void getEid(int paramInt, IGetEidCallback paramIGetEidCallback)
    throws RemoteException;
  
  public abstract void getEuiccInfo(int paramInt, IGetEuiccInfoCallback paramIGetEuiccInfoCallback)
    throws RemoteException;
  
  public abstract void getEuiccProfileInfoList(int paramInt, IGetEuiccProfileInfoListCallback paramIGetEuiccProfileInfoListCallback)
    throws RemoteException;
  
  public abstract void getOtaStatus(int paramInt, IGetOtaStatusCallback paramIGetOtaStatusCallback)
    throws RemoteException;
  
  public abstract void retainSubscriptionsForFactoryReset(int paramInt, IRetainSubscriptionsForFactoryResetCallback paramIRetainSubscriptionsForFactoryResetCallback)
    throws RemoteException;
  
  public abstract void startOtaIfNecessary(int paramInt, IOtaStatusChangedCallback paramIOtaStatusChangedCallback)
    throws RemoteException;
  
  public abstract void switchToSubscription(int paramInt, String paramString, boolean paramBoolean, ISwitchToSubscriptionCallback paramISwitchToSubscriptionCallback)
    throws RemoteException;
  
  public abstract void updateSubscriptionNickname(int paramInt, String paramString1, String paramString2, IUpdateSubscriptionNicknameCallback paramIUpdateSubscriptionNicknameCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IEuiccService
  {
    private static final String DESCRIPTOR = "android.service.euicc.IEuiccService";
    static final int TRANSACTION_deleteSubscription = 9;
    static final int TRANSACTION_downloadSubscription = 1;
    static final int TRANSACTION_eraseSubscriptions = 12;
    static final int TRANSACTION_getDefaultDownloadableSubscriptionList = 7;
    static final int TRANSACTION_getDownloadableSubscriptionMetadata = 2;
    static final int TRANSACTION_getEid = 3;
    static final int TRANSACTION_getEuiccInfo = 8;
    static final int TRANSACTION_getEuiccProfileInfoList = 6;
    static final int TRANSACTION_getOtaStatus = 4;
    static final int TRANSACTION_retainSubscriptionsForFactoryReset = 13;
    static final int TRANSACTION_startOtaIfNecessary = 5;
    static final int TRANSACTION_switchToSubscription = 10;
    static final int TRANSACTION_updateSubscriptionNickname = 11;
    
    public Stub()
    {
      attachInterface(this, "android.service.euicc.IEuiccService");
    }
    
    public static IEuiccService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.euicc.IEuiccService");
      if ((localIInterface != null) && ((localIInterface instanceof IEuiccService))) {
        return (IEuiccService)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 13: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          retainSubscriptionsForFactoryReset(paramParcel1.readInt(), IRetainSubscriptionsForFactoryResetCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          eraseSubscriptions(paramParcel1.readInt(), IEraseSubscriptionsCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          updateSubscriptionNickname(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), IUpdateSubscriptionNicknameCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          paramInt1 = paramParcel1.readInt();
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          switchToSubscription(paramInt1, paramParcel2, bool3, ISwitchToSubscriptionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          deleteSubscription(paramParcel1.readInt(), paramParcel1.readString(), IDeleteSubscriptionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          getEuiccInfo(paramParcel1.readInt(), IGetEuiccInfoCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          paramInt1 = paramParcel1.readInt();
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          getDefaultDownloadableSubscriptionList(paramInt1, bool3, IGetDefaultDownloadableSubscriptionListCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          getEuiccProfileInfoList(paramParcel1.readInt(), IGetEuiccProfileInfoListCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          startOtaIfNecessary(paramParcel1.readInt(), IOtaStatusChangedCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          getOtaStatus(paramParcel1.readInt(), IGetOtaStatusCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          getEid(paramParcel1.readInt(), IGetEidCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (DownloadableSubscription)DownloadableSubscription.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          bool3 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          getDownloadableSubscriptionMetadata(paramInt1, paramParcel2, bool3, IGetDownloadableSubscriptionMetadataCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("android.service.euicc.IEuiccService");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel2 = (DownloadableSubscription)DownloadableSubscription.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject1) {
          break;
        }
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        } else {
          bool3 = false;
        }
        if (paramParcel1.readInt() != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        downloadSubscription(paramInt1, paramParcel2, bool3, bool1, IDownloadSubscriptionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.service.euicc.IEuiccService");
      return true;
    }
    
    private static class Proxy
      implements IEuiccService
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
      
      public void deleteSubscription(int paramInt, String paramString, IDeleteSubscriptionCallback paramIDeleteSubscriptionCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          if (paramIDeleteSubscriptionCallback != null) {
            paramString = paramIDeleteSubscriptionCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void downloadSubscription(int paramInt, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean1, boolean paramBoolean2, IDownloadSubscriptionCallback paramIDownloadSubscriptionCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          if (paramDownloadableSubscription != null)
          {
            localParcel.writeInt(1);
            paramDownloadableSubscription.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          if (paramIDownloadSubscriptionCallback != null) {
            paramDownloadableSubscription = paramIDownloadSubscriptionCallback.asBinder();
          } else {
            paramDownloadableSubscription = null;
          }
          localParcel.writeStrongBinder(paramDownloadableSubscription);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void eraseSubscriptions(int paramInt, IEraseSubscriptionsCallback paramIEraseSubscriptionsCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          if (paramIEraseSubscriptionsCallback != null) {
            paramIEraseSubscriptionsCallback = paramIEraseSubscriptionsCallback.asBinder();
          } else {
            paramIEraseSubscriptionsCallback = null;
          }
          localParcel.writeStrongBinder(paramIEraseSubscriptionsCallback);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getDefaultDownloadableSubscriptionList(int paramInt, boolean paramBoolean, IGetDefaultDownloadableSubscriptionListCallback paramIGetDefaultDownloadableSubscriptionListCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          if (paramIGetDefaultDownloadableSubscriptionListCallback != null) {
            paramIGetDefaultDownloadableSubscriptionListCallback = paramIGetDefaultDownloadableSubscriptionListCallback.asBinder();
          } else {
            paramIGetDefaultDownloadableSubscriptionListCallback = null;
          }
          localParcel.writeStrongBinder(paramIGetDefaultDownloadableSubscriptionListCallback);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getDownloadableSubscriptionMetadata(int paramInt, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, IGetDownloadableSubscriptionMetadataCallback paramIGetDownloadableSubscriptionMetadataCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          if (paramDownloadableSubscription != null)
          {
            localParcel.writeInt(1);
            paramDownloadableSubscription.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          if (paramIGetDownloadableSubscriptionMetadataCallback != null) {
            paramDownloadableSubscription = paramIGetDownloadableSubscriptionMetadataCallback.asBinder();
          } else {
            paramDownloadableSubscription = null;
          }
          localParcel.writeStrongBinder(paramDownloadableSubscription);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getEid(int paramInt, IGetEidCallback paramIGetEidCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          if (paramIGetEidCallback != null) {
            paramIGetEidCallback = paramIGetEidCallback.asBinder();
          } else {
            paramIGetEidCallback = null;
          }
          localParcel.writeStrongBinder(paramIGetEidCallback);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getEuiccInfo(int paramInt, IGetEuiccInfoCallback paramIGetEuiccInfoCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          if (paramIGetEuiccInfoCallback != null) {
            paramIGetEuiccInfoCallback = paramIGetEuiccInfoCallback.asBinder();
          } else {
            paramIGetEuiccInfoCallback = null;
          }
          localParcel.writeStrongBinder(paramIGetEuiccInfoCallback);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getEuiccProfileInfoList(int paramInt, IGetEuiccProfileInfoListCallback paramIGetEuiccProfileInfoListCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          if (paramIGetEuiccProfileInfoListCallback != null) {
            paramIGetEuiccProfileInfoListCallback = paramIGetEuiccProfileInfoListCallback.asBinder();
          } else {
            paramIGetEuiccProfileInfoListCallback = null;
          }
          localParcel.writeStrongBinder(paramIGetEuiccProfileInfoListCallback);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.euicc.IEuiccService";
      }
      
      public void getOtaStatus(int paramInt, IGetOtaStatusCallback paramIGetOtaStatusCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          if (paramIGetOtaStatusCallback != null) {
            paramIGetOtaStatusCallback = paramIGetOtaStatusCallback.asBinder();
          } else {
            paramIGetOtaStatusCallback = null;
          }
          localParcel.writeStrongBinder(paramIGetOtaStatusCallback);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void retainSubscriptionsForFactoryReset(int paramInt, IRetainSubscriptionsForFactoryResetCallback paramIRetainSubscriptionsForFactoryResetCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          if (paramIRetainSubscriptionsForFactoryResetCallback != null) {
            paramIRetainSubscriptionsForFactoryResetCallback = paramIRetainSubscriptionsForFactoryResetCallback.asBinder();
          } else {
            paramIRetainSubscriptionsForFactoryResetCallback = null;
          }
          localParcel.writeStrongBinder(paramIRetainSubscriptionsForFactoryResetCallback);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startOtaIfNecessary(int paramInt, IOtaStatusChangedCallback paramIOtaStatusChangedCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          if (paramIOtaStatusChangedCallback != null) {
            paramIOtaStatusChangedCallback = paramIOtaStatusChangedCallback.asBinder();
          } else {
            paramIOtaStatusChangedCallback = null;
          }
          localParcel.writeStrongBinder(paramIOtaStatusChangedCallback);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void switchToSubscription(int paramInt, String paramString, boolean paramBoolean, ISwitchToSubscriptionCallback paramISwitchToSubscriptionCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          localParcel.writeInt(paramBoolean);
          if (paramISwitchToSubscriptionCallback != null) {
            paramString = paramISwitchToSubscriptionCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updateSubscriptionNickname(int paramInt, String paramString1, String paramString2, IUpdateSubscriptionNicknameCallback paramIUpdateSubscriptionNicknameCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.euicc.IEuiccService");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramIUpdateSubscriptionNicknameCallback != null) {
            paramString1 = paramIUpdateSubscriptionNicknameCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(11, localParcel, null, 1);
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
