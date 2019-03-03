package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.SubscriptionInfo;
import java.util.List;

public abstract interface ISub
  extends IInterface
{
  public abstract int addSubInfoRecord(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void clearDefaultsForInactiveSubIds()
    throws RemoteException;
  
  public abstract int clearSubInfo()
    throws RemoteException;
  
  public abstract List<SubscriptionInfo> getAccessibleSubscriptionInfoList(String paramString)
    throws RemoteException;
  
  public abstract int[] getActiveSubIdList()
    throws RemoteException;
  
  public abstract int getActiveSubInfoCount(String paramString)
    throws RemoteException;
  
  public abstract int getActiveSubInfoCountMax()
    throws RemoteException;
  
  public abstract SubscriptionInfo getActiveSubscriptionInfo(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract SubscriptionInfo getActiveSubscriptionInfoForIccId(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract List<SubscriptionInfo> getActiveSubscriptionInfoList(String paramString)
    throws RemoteException;
  
  public abstract int getAllSubInfoCount(String paramString)
    throws RemoteException;
  
  public abstract List<SubscriptionInfo> getAllSubInfoList(String paramString)
    throws RemoteException;
  
  public abstract List<SubscriptionInfo> getAvailableSubscriptionInfoList(String paramString)
    throws RemoteException;
  
  public abstract int getDefaultDataSubId()
    throws RemoteException;
  
  public abstract int getDefaultSmsSubId()
    throws RemoteException;
  
  public abstract int getDefaultSubId()
    throws RemoteException;
  
  public abstract int getDefaultVoiceSubId()
    throws RemoteException;
  
  public abstract int getPhoneId(int paramInt)
    throws RemoteException;
  
  public abstract int getSimStateForSlotIndex(int paramInt)
    throws RemoteException;
  
  public abstract int getSlotIndex(int paramInt)
    throws RemoteException;
  
  public abstract int[] getSubId(int paramInt)
    throws RemoteException;
  
  public abstract String getSubscriptionProperty(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean isActiveSubId(int paramInt)
    throws RemoteException;
  
  public abstract void requestEmbeddedSubscriptionInfoListRefresh()
    throws RemoteException;
  
  public abstract int setDataRoaming(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setDefaultDataSubId(int paramInt)
    throws RemoteException;
  
  public abstract void setDefaultSmsSubId(int paramInt)
    throws RemoteException;
  
  public abstract void setDefaultVoiceSubId(int paramInt)
    throws RemoteException;
  
  public abstract int setDisplayName(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int setDisplayNameUsingSrc(String paramString, int paramInt, long paramLong)
    throws RemoteException;
  
  public abstract int setDisplayNumber(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int setIconTint(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setSubscriptionProperty(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISub
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.ISub";
    static final int TRANSACTION_addSubInfoRecord = 12;
    static final int TRANSACTION_clearDefaultsForInactiveSubIds = 29;
    static final int TRANSACTION_clearSubInfo = 21;
    static final int TRANSACTION_getAccessibleSubscriptionInfoList = 10;
    static final int TRANSACTION_getActiveSubIdList = 30;
    static final int TRANSACTION_getActiveSubInfoCount = 7;
    static final int TRANSACTION_getActiveSubInfoCountMax = 8;
    static final int TRANSACTION_getActiveSubscriptionInfo = 3;
    static final int TRANSACTION_getActiveSubscriptionInfoForIccId = 4;
    static final int TRANSACTION_getActiveSubscriptionInfoForSimSlotIndex = 5;
    static final int TRANSACTION_getActiveSubscriptionInfoList = 6;
    static final int TRANSACTION_getAllSubInfoCount = 2;
    static final int TRANSACTION_getAllSubInfoList = 1;
    static final int TRANSACTION_getAvailableSubscriptionInfoList = 9;
    static final int TRANSACTION_getDefaultDataSubId = 23;
    static final int TRANSACTION_getDefaultSmsSubId = 27;
    static final int TRANSACTION_getDefaultSubId = 20;
    static final int TRANSACTION_getDefaultVoiceSubId = 25;
    static final int TRANSACTION_getPhoneId = 22;
    static final int TRANSACTION_getSimStateForSlotIndex = 33;
    static final int TRANSACTION_getSlotIndex = 18;
    static final int TRANSACTION_getSubId = 19;
    static final int TRANSACTION_getSubscriptionProperty = 32;
    static final int TRANSACTION_isActiveSubId = 34;
    static final int TRANSACTION_requestEmbeddedSubscriptionInfoListRefresh = 11;
    static final int TRANSACTION_setDataRoaming = 17;
    static final int TRANSACTION_setDefaultDataSubId = 24;
    static final int TRANSACTION_setDefaultSmsSubId = 28;
    static final int TRANSACTION_setDefaultVoiceSubId = 26;
    static final int TRANSACTION_setDisplayName = 14;
    static final int TRANSACTION_setDisplayNameUsingSrc = 15;
    static final int TRANSACTION_setDisplayNumber = 16;
    static final int TRANSACTION_setIconTint = 13;
    static final int TRANSACTION_setSubscriptionProperty = 31;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.ISub");
    }
    
    public static ISub asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.ISub");
      if ((localIInterface != null) && ((localIInterface instanceof ISub))) {
        return (ISub)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = isActiveSubId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getSimStateForSlotIndex(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramParcel1 = getSubscriptionProperty(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          setSubscriptionProperty(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramParcel1 = getActiveSubIdList();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          clearDefaultsForInactiveSubIds();
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          setDefaultSmsSubId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getDefaultSmsSubId();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          setDefaultVoiceSubId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getDefaultVoiceSubId();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          setDefaultDataSubId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getDefaultDataSubId();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getPhoneId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = clearSubInfo();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getDefaultSubId();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramParcel1 = getSubId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getSlotIndex(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = setDataRoaming(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = setDisplayNumber(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = setDisplayNameUsingSrc(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = setDisplayName(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = setIconTint(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = addSubInfoRecord(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          requestEmbeddedSubscriptionInfoListRefresh();
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramParcel1 = getAccessibleSubscriptionInfoList(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramParcel1 = getAvailableSubscriptionInfoList(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getActiveSubInfoCountMax();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getActiveSubInfoCount(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramParcel1 = getActiveSubscriptionInfoList(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramParcel1 = getActiveSubscriptionInfoForSimSlotIndex(paramParcel1.readInt(), paramParcel1.readString());
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
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramParcel1 = getActiveSubscriptionInfoForIccId(paramParcel1.readString(), paramParcel1.readString());
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
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramParcel1 = getActiveSubscriptionInfo(paramParcel1.readInt(), paramParcel1.readString());
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
          paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
          paramInt1 = getAllSubInfoCount(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.ISub");
        paramParcel1 = getAllSubInfoList(paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.ISub");
      return true;
    }
    
    private static class Proxy
      implements ISub
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int addSubInfoRecord(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void clearDefaultsForInactiveSubIds()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int clearSubInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          mRemote.transact(21, localParcel1, localParcel2, 0);
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
      
      public List<SubscriptionInfo> getAccessibleSubscriptionInfoList(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(SubscriptionInfo.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getActiveSubIdList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getActiveSubInfoCount(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeString(paramString);
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
      
      public int getActiveSubInfoCountMax()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public SubscriptionInfo getActiveSubscriptionInfo(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (SubscriptionInfo)SubscriptionInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public SubscriptionInfo getActiveSubscriptionInfoForIccId(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (SubscriptionInfo)SubscriptionInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (SubscriptionInfo)SubscriptionInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public List<SubscriptionInfo> getActiveSubscriptionInfoList(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(SubscriptionInfo.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getAllSubInfoCount(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public List<SubscriptionInfo> getAllSubInfoList(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(SubscriptionInfo.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<SubscriptionInfo> getAvailableSubscriptionInfoList(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeString(paramString);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(SubscriptionInfo.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDefaultDataSubId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          mRemote.transact(23, localParcel1, localParcel2, 0);
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
      
      public int getDefaultSmsSubId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          mRemote.transact(27, localParcel1, localParcel2, 0);
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
      
      public int getDefaultSubId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          mRemote.transact(20, localParcel1, localParcel2, 0);
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
      
      public int getDefaultVoiceSubId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          mRemote.transact(25, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.ISub";
      }
      
      public int getPhoneId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          mRemote.transact(22, localParcel1, localParcel2, 0);
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
      
      public int getSimStateForSlotIndex(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          mRemote.transact(33, localParcel1, localParcel2, 0);
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
      
      public int getSlotIndex(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          mRemote.transact(18, localParcel1, localParcel2, 0);
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
      
      public int[] getSubId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getSubscriptionProperty(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.readString();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isActiveSubId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public void requestEmbeddedSubscriptionInfoListRefresh()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.ISub");
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public int setDataRoaming(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDefaultDataSubId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
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
      
      public void setDefaultSmsSubId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDefaultVoiceSubId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setDisplayName(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
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
      
      public int setDisplayNameUsingSrc(String paramString, int paramInt, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeLong(paramLong);
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
      
      public int setDisplayNumber(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
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
      
      public int setIconTint(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSubscriptionProperty(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISub");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(31, localParcel1, localParcel2, 0);
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
