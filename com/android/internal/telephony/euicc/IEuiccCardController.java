package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IEuiccCardController
  extends IInterface
{
  public abstract void authenticateServer(String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, IAuthenticateServerCallback paramIAuthenticateServerCallback)
    throws RemoteException;
  
  public abstract void cancelSession(String paramString1, String paramString2, byte[] paramArrayOfByte, int paramInt, ICancelSessionCallback paramICancelSessionCallback)
    throws RemoteException;
  
  public abstract void deleteProfile(String paramString1, String paramString2, String paramString3, IDeleteProfileCallback paramIDeleteProfileCallback)
    throws RemoteException;
  
  public abstract void disableProfile(String paramString1, String paramString2, String paramString3, boolean paramBoolean, IDisableProfileCallback paramIDisableProfileCallback)
    throws RemoteException;
  
  public abstract void getAllProfiles(String paramString1, String paramString2, IGetAllProfilesCallback paramIGetAllProfilesCallback)
    throws RemoteException;
  
  public abstract void getDefaultSmdpAddress(String paramString1, String paramString2, IGetDefaultSmdpAddressCallback paramIGetDefaultSmdpAddressCallback)
    throws RemoteException;
  
  public abstract void getEuiccChallenge(String paramString1, String paramString2, IGetEuiccChallengeCallback paramIGetEuiccChallengeCallback)
    throws RemoteException;
  
  public abstract void getEuiccInfo1(String paramString1, String paramString2, IGetEuiccInfo1Callback paramIGetEuiccInfo1Callback)
    throws RemoteException;
  
  public abstract void getEuiccInfo2(String paramString1, String paramString2, IGetEuiccInfo2Callback paramIGetEuiccInfo2Callback)
    throws RemoteException;
  
  public abstract void getProfile(String paramString1, String paramString2, String paramString3, IGetProfileCallback paramIGetProfileCallback)
    throws RemoteException;
  
  public abstract void getRulesAuthTable(String paramString1, String paramString2, IGetRulesAuthTableCallback paramIGetRulesAuthTableCallback)
    throws RemoteException;
  
  public abstract void getSmdsAddress(String paramString1, String paramString2, IGetSmdsAddressCallback paramIGetSmdsAddressCallback)
    throws RemoteException;
  
  public abstract void listNotifications(String paramString1, String paramString2, int paramInt, IListNotificationsCallback paramIListNotificationsCallback)
    throws RemoteException;
  
  public abstract void loadBoundProfilePackage(String paramString1, String paramString2, byte[] paramArrayOfByte, ILoadBoundProfilePackageCallback paramILoadBoundProfilePackageCallback)
    throws RemoteException;
  
  public abstract void prepareDownload(String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, IPrepareDownloadCallback paramIPrepareDownloadCallback)
    throws RemoteException;
  
  public abstract void removeNotificationFromList(String paramString1, String paramString2, int paramInt, IRemoveNotificationFromListCallback paramIRemoveNotificationFromListCallback)
    throws RemoteException;
  
  public abstract void resetMemory(String paramString1, String paramString2, int paramInt, IResetMemoryCallback paramIResetMemoryCallback)
    throws RemoteException;
  
  public abstract void retrieveNotification(String paramString1, String paramString2, int paramInt, IRetrieveNotificationCallback paramIRetrieveNotificationCallback)
    throws RemoteException;
  
  public abstract void retrieveNotificationList(String paramString1, String paramString2, int paramInt, IRetrieveNotificationListCallback paramIRetrieveNotificationListCallback)
    throws RemoteException;
  
  public abstract void setDefaultSmdpAddress(String paramString1, String paramString2, String paramString3, ISetDefaultSmdpAddressCallback paramISetDefaultSmdpAddressCallback)
    throws RemoteException;
  
  public abstract void setNickname(String paramString1, String paramString2, String paramString3, String paramString4, ISetNicknameCallback paramISetNicknameCallback)
    throws RemoteException;
  
  public abstract void switchToProfile(String paramString1, String paramString2, String paramString3, boolean paramBoolean, ISwitchToProfileCallback paramISwitchToProfileCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IEuiccCardController
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IEuiccCardController";
    static final int TRANSACTION_authenticateServer = 15;
    static final int TRANSACTION_cancelSession = 18;
    static final int TRANSACTION_deleteProfile = 6;
    static final int TRANSACTION_disableProfile = 3;
    static final int TRANSACTION_getAllProfiles = 1;
    static final int TRANSACTION_getDefaultSmdpAddress = 8;
    static final int TRANSACTION_getEuiccChallenge = 12;
    static final int TRANSACTION_getEuiccInfo1 = 13;
    static final int TRANSACTION_getEuiccInfo2 = 14;
    static final int TRANSACTION_getProfile = 2;
    static final int TRANSACTION_getRulesAuthTable = 11;
    static final int TRANSACTION_getSmdsAddress = 9;
    static final int TRANSACTION_listNotifications = 19;
    static final int TRANSACTION_loadBoundProfilePackage = 17;
    static final int TRANSACTION_prepareDownload = 16;
    static final int TRANSACTION_removeNotificationFromList = 22;
    static final int TRANSACTION_resetMemory = 7;
    static final int TRANSACTION_retrieveNotification = 21;
    static final int TRANSACTION_retrieveNotificationList = 20;
    static final int TRANSACTION_setDefaultSmdpAddress = 10;
    static final int TRANSACTION_setNickname = 5;
    static final int TRANSACTION_switchToProfile = 4;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.euicc.IEuiccCardController");
    }
    
    public static IEuiccCardController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.euicc.IEuiccCardController");
      if ((localIInterface != null) && ((localIInterface instanceof IEuiccCardController))) {
        return (IEuiccCardController)localIInterface;
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
        String str1;
        String str2;
        boolean bool;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          removeNotificationFromList(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), IRemoveNotificationFromListCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          retrieveNotification(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), IRetrieveNotificationCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          retrieveNotificationList(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), IRetrieveNotificationListCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          listNotifications(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), IListNotificationsCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          cancelSession(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readInt(), ICancelSessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          loadBoundProfilePackage(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.createByteArray(), ILoadBoundProfilePackageCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          prepareDownload(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), IPrepareDownloadCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          authenticateServer(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), paramParcel1.createByteArray(), IAuthenticateServerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          getEuiccInfo2(paramParcel1.readString(), paramParcel1.readString(), IGetEuiccInfo2Callback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          getEuiccInfo1(paramParcel1.readString(), paramParcel1.readString(), IGetEuiccInfo1Callback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          getEuiccChallenge(paramParcel1.readString(), paramParcel1.readString(), IGetEuiccChallengeCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          getRulesAuthTable(paramParcel1.readString(), paramParcel1.readString(), IGetRulesAuthTableCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          setDefaultSmdpAddress(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), ISetDefaultSmdpAddressCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          getSmdsAddress(paramParcel1.readString(), paramParcel1.readString(), IGetSmdsAddressCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          getDefaultSmdpAddress(paramParcel1.readString(), paramParcel1.readString(), IGetDefaultSmdpAddressCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          resetMemory(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), IResetMemoryCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          deleteProfile(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), IDeleteProfileCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          setNickname(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), ISetNicknameCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          str1 = paramParcel1.readString();
          paramParcel2 = paramParcel1.readString();
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          switchToProfile(str1, paramParcel2, str2, bool, ISwitchToProfileCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          str2 = paramParcel1.readString();
          paramParcel2 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          disableProfile(str2, paramParcel2, str1, bool, IDisableProfileCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
          getProfile(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), IGetProfileCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccCardController");
        getAllProfiles(paramParcel1.readString(), paramParcel1.readString(), IGetAllProfilesCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.euicc.IEuiccCardController");
      return true;
    }
    
    private static class Proxy
      implements IEuiccCardController
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
      
      public void authenticateServer(String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, IAuthenticateServerCallback paramIAuthenticateServerCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          localParcel.writeByteArray(paramArrayOfByte1);
          localParcel.writeByteArray(paramArrayOfByte2);
          localParcel.writeByteArray(paramArrayOfByte3);
          localParcel.writeByteArray(paramArrayOfByte4);
          if (paramIAuthenticateServerCallback != null) {
            paramString1 = paramIAuthenticateServerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void cancelSession(String paramString1, String paramString2, byte[] paramArrayOfByte, int paramInt, ICancelSessionCallback paramICancelSessionCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeByteArray(paramArrayOfByte);
          localParcel.writeInt(paramInt);
          if (paramICancelSessionCallback != null) {
            paramString1 = paramICancelSessionCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deleteProfile(String paramString1, String paramString2, String paramString3, IDeleteProfileCallback paramIDeleteProfileCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          if (paramIDeleteProfileCallback != null) {
            paramString1 = paramIDeleteProfileCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void disableProfile(String paramString1, String paramString2, String paramString3, boolean paramBoolean, IDisableProfileCallback paramIDisableProfileCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          localParcel.writeInt(paramBoolean);
          if (paramIDisableProfileCallback != null) {
            paramString1 = paramIDisableProfileCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getAllProfiles(String paramString1, String paramString2, IGetAllProfilesCallback paramIGetAllProfilesCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramIGetAllProfilesCallback != null) {
            paramString1 = paramIGetAllProfilesCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getDefaultSmdpAddress(String paramString1, String paramString2, IGetDefaultSmdpAddressCallback paramIGetDefaultSmdpAddressCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramIGetDefaultSmdpAddressCallback != null) {
            paramString1 = paramIGetDefaultSmdpAddressCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getEuiccChallenge(String paramString1, String paramString2, IGetEuiccChallengeCallback paramIGetEuiccChallengeCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramIGetEuiccChallengeCallback != null) {
            paramString1 = paramIGetEuiccChallengeCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getEuiccInfo1(String paramString1, String paramString2, IGetEuiccInfo1Callback paramIGetEuiccInfo1Callback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramIGetEuiccInfo1Callback != null) {
            paramString1 = paramIGetEuiccInfo1Callback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getEuiccInfo2(String paramString1, String paramString2, IGetEuiccInfo2Callback paramIGetEuiccInfo2Callback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramIGetEuiccInfo2Callback != null) {
            paramString1 = paramIGetEuiccInfo2Callback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.euicc.IEuiccCardController";
      }
      
      public void getProfile(String paramString1, String paramString2, String paramString3, IGetProfileCallback paramIGetProfileCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          if (paramIGetProfileCallback != null) {
            paramString1 = paramIGetProfileCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getRulesAuthTable(String paramString1, String paramString2, IGetRulesAuthTableCallback paramIGetRulesAuthTableCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramIGetRulesAuthTableCallback != null) {
            paramString1 = paramIGetRulesAuthTableCallback.asBinder();
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
      
      public void getSmdsAddress(String paramString1, String paramString2, IGetSmdsAddressCallback paramIGetSmdsAddressCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramIGetSmdsAddressCallback != null) {
            paramString1 = paramIGetSmdsAddressCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void listNotifications(String paramString1, String paramString2, int paramInt, IListNotificationsCallback paramIListNotificationsCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramInt);
          if (paramIListNotificationsCallback != null) {
            paramString1 = paramIListNotificationsCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void loadBoundProfilePackage(String paramString1, String paramString2, byte[] paramArrayOfByte, ILoadBoundProfilePackageCallback paramILoadBoundProfilePackageCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeByteArray(paramArrayOfByte);
          if (paramILoadBoundProfilePackageCallback != null) {
            paramString1 = paramILoadBoundProfilePackageCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void prepareDownload(String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, IPrepareDownloadCallback paramIPrepareDownloadCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeByteArray(paramArrayOfByte1);
          localParcel.writeByteArray(paramArrayOfByte2);
          localParcel.writeByteArray(paramArrayOfByte3);
          localParcel.writeByteArray(paramArrayOfByte4);
          if (paramIPrepareDownloadCallback != null) {
            paramString1 = paramIPrepareDownloadCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeNotificationFromList(String paramString1, String paramString2, int paramInt, IRemoveNotificationFromListCallback paramIRemoveNotificationFromListCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramInt);
          if (paramIRemoveNotificationFromListCallback != null) {
            paramString1 = paramIRemoveNotificationFromListCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void resetMemory(String paramString1, String paramString2, int paramInt, IResetMemoryCallback paramIResetMemoryCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramInt);
          if (paramIResetMemoryCallback != null) {
            paramString1 = paramIResetMemoryCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void retrieveNotification(String paramString1, String paramString2, int paramInt, IRetrieveNotificationCallback paramIRetrieveNotificationCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramInt);
          if (paramIRetrieveNotificationCallback != null) {
            paramString1 = paramIRetrieveNotificationCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void retrieveNotificationList(String paramString1, String paramString2, int paramInt, IRetrieveNotificationListCallback paramIRetrieveNotificationListCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramInt);
          if (paramIRetrieveNotificationListCallback != null) {
            paramString1 = paramIRetrieveNotificationListCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setDefaultSmdpAddress(String paramString1, String paramString2, String paramString3, ISetDefaultSmdpAddressCallback paramISetDefaultSmdpAddressCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          if (paramISetDefaultSmdpAddressCallback != null) {
            paramString1 = paramISetDefaultSmdpAddressCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setNickname(String paramString1, String paramString2, String paramString3, String paramString4, ISetNicknameCallback paramISetNicknameCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          localParcel.writeString(paramString4);
          if (paramISetNicknameCallback != null) {
            paramString1 = paramISetNicknameCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void switchToProfile(String paramString1, String paramString2, String paramString3, boolean paramBoolean, ISwitchToProfileCallback paramISwitchToProfileCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccCardController");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeString(paramString3);
          localParcel.writeInt(paramBoolean);
          if (paramISwitchToProfileCallback != null) {
            paramString1 = paramISwitchToProfileCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(4, localParcel, null, 1);
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
