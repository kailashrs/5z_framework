package com.android.internal.telephony.euicc;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.euicc.DownloadableSubscription;
import android.telephony.euicc.EuiccInfo;

public abstract interface IEuiccController
  extends IInterface
{
  public abstract void continueOperation(Intent paramIntent, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void deleteSubscription(int paramInt, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void downloadSubscription(DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void eraseSubscriptions(PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void getDefaultDownloadableSubscriptionList(String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void getDownloadableSubscriptionMetadata(DownloadableSubscription paramDownloadableSubscription, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract String getEid()
    throws RemoteException;
  
  public abstract EuiccInfo getEuiccInfo()
    throws RemoteException;
  
  public abstract int getOtaStatus()
    throws RemoteException;
  
  public abstract void retainSubscriptionsForFactoryReset(PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void switchToSubscription(int paramInt, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void updateSubscriptionNickname(int paramInt, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IEuiccController
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IEuiccController";
    static final int TRANSACTION_continueOperation = 1;
    static final int TRANSACTION_deleteSubscription = 8;
    static final int TRANSACTION_downloadSubscription = 6;
    static final int TRANSACTION_eraseSubscriptions = 11;
    static final int TRANSACTION_getDefaultDownloadableSubscriptionList = 3;
    static final int TRANSACTION_getDownloadableSubscriptionMetadata = 2;
    static final int TRANSACTION_getEid = 4;
    static final int TRANSACTION_getEuiccInfo = 7;
    static final int TRANSACTION_getOtaStatus = 5;
    static final int TRANSACTION_retainSubscriptionsForFactoryReset = 12;
    static final int TRANSACTION_switchToSubscription = 9;
    static final int TRANSACTION_updateSubscriptionNickname = 10;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.euicc.IEuiccController");
    }
    
    public static IEuiccController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.euicc.IEuiccController");
      if ((localIInterface != null) && ((localIInterface instanceof IEuiccController))) {
        return (IEuiccController)localIInterface;
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
        boolean bool = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        String str1 = null;
        Object localObject5 = null;
        String str2 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          retainSubscriptionsForFactoryReset(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          eraseSubscriptions(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          paramInt1 = paramParcel1.readInt();
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          updateSubscriptionNickname(paramInt1, paramParcel2, paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          paramInt1 = paramParcel1.readInt();
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          switchToSubscription(paramInt1, paramParcel2, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          paramInt1 = paramParcel1.readInt();
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          deleteSubscription(paramInt1, paramParcel2, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          paramParcel1 = getEuiccInfo();
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
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (DownloadableSubscription)DownloadableSubscription.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          downloadSubscription(paramParcel2, bool, str2, paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          paramInt1 = getOtaStatus();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          paramParcel1 = getEid();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          getDefaultDownloadableSubscriptionList(paramParcel2, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (DownloadableSubscription)DownloadableSubscription.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          getDownloadableSubscriptionMetadata(paramParcel2, str1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.euicc.IEuiccController");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject6;
        }
        continueOperation(paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.euicc.IEuiccController");
      return true;
    }
    
    private static class Proxy
      implements IEuiccController
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
      
      public void continueOperation(Intent paramIntent, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deleteSubscription(int paramInt, String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel, 0);
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
      
      public void downloadSubscription(DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
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
          localParcel.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel, 0);
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
      
      public void eraseSubscriptions(PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          if (paramPendingIntent != null)
          {
            localParcel.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getDefaultDownloadableSubscriptionList(String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          localParcel.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel, 0);
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
      
      public void getDownloadableSubscriptionMetadata(DownloadableSubscription paramDownloadableSubscription, String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          if (paramDownloadableSubscription != null)
          {
            localParcel.writeInt(1);
            paramDownloadableSubscription.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel, 0);
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
      
      public String getEid()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public EuiccInfo getEuiccInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          EuiccInfo localEuiccInfo;
          if (localParcel2.readInt() != 0) {
            localEuiccInfo = (EuiccInfo)EuiccInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localEuiccInfo = null;
          }
          return localEuiccInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.euicc.IEuiccController";
      }
      
      public int getOtaStatus()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public void retainSubscriptionsForFactoryReset(PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          if (paramPendingIntent != null)
          {
            localParcel.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel, 0);
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
      
      public void switchToSubscription(int paramInt, String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void updateSubscriptionNickname(int paramInt, String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.euicc.IEuiccController");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel, 0);
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
    }
  }
}
