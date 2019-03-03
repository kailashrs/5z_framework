package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ImsiEncryptionInfo;

public abstract interface IPhoneSubInfo
  extends IInterface
{
  public abstract ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract String getCompleteVoiceMailNumber()
    throws RemoteException;
  
  public abstract String getCompleteVoiceMailNumberForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract String getDeviceId(String paramString)
    throws RemoteException;
  
  public abstract String getDeviceIdForPhone(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getDeviceSvn(String paramString)
    throws RemoteException;
  
  public abstract String getDeviceSvnUsingSubId(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getGroupIdLevel1ForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getIccSerialNumber(String paramString)
    throws RemoteException;
  
  public abstract String getIccSerialNumberForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getIccSimChallengeResponse(int paramInt1, int paramInt2, int paramInt3, String paramString)
    throws RemoteException;
  
  public abstract String getImeiForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getIsimDomain(int paramInt)
    throws RemoteException;
  
  public abstract String getIsimImpi(int paramInt)
    throws RemoteException;
  
  public abstract String[] getIsimImpu(int paramInt)
    throws RemoteException;
  
  public abstract String getIsimIst(int paramInt)
    throws RemoteException;
  
  public abstract String[] getIsimPcscf(int paramInt)
    throws RemoteException;
  
  public abstract String getLine1AlphaTag(String paramString)
    throws RemoteException;
  
  public abstract String getLine1AlphaTagForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getLine1Number(String paramString)
    throws RemoteException;
  
  public abstract String getLine1NumberForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getMsisdn(String paramString)
    throws RemoteException;
  
  public abstract String getMsisdnForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getNaiForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getSubscriberId(String paramString)
    throws RemoteException;
  
  public abstract String getSubscriberIdForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getVoiceMailAlphaTag(String paramString)
    throws RemoteException;
  
  public abstract String getVoiceMailAlphaTagForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getVoiceMailNumber(String paramString)
    throws RemoteException;
  
  public abstract String getVoiceMailNumberForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void resetCarrierKeysForImsiEncryption(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setCarrierInfoForImsiEncryption(int paramInt, String paramString, ImsiEncryptionInfo paramImsiEncryptionInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPhoneSubInfo
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.IPhoneSubInfo";
    static final int TRANSACTION_getCarrierInfoForImsiEncryption = 22;
    static final int TRANSACTION_getCompleteVoiceMailNumber = 20;
    static final int TRANSACTION_getCompleteVoiceMailNumberForSubscriber = 21;
    static final int TRANSACTION_getDeviceId = 1;
    static final int TRANSACTION_getDeviceIdForPhone = 3;
    static final int TRANSACTION_getDeviceSvn = 5;
    static final int TRANSACTION_getDeviceSvnUsingSubId = 6;
    static final int TRANSACTION_getGroupIdLevel1ForSubscriber = 9;
    static final int TRANSACTION_getIccSerialNumber = 10;
    static final int TRANSACTION_getIccSerialNumberForSubscriber = 11;
    static final int TRANSACTION_getIccSimChallengeResponse = 32;
    static final int TRANSACTION_getImeiForSubscriber = 4;
    static final int TRANSACTION_getIsimDomain = 28;
    static final int TRANSACTION_getIsimImpi = 27;
    static final int TRANSACTION_getIsimImpu = 29;
    static final int TRANSACTION_getIsimIst = 30;
    static final int TRANSACTION_getIsimPcscf = 31;
    static final int TRANSACTION_getLine1AlphaTag = 14;
    static final int TRANSACTION_getLine1AlphaTagForSubscriber = 15;
    static final int TRANSACTION_getLine1Number = 12;
    static final int TRANSACTION_getLine1NumberForSubscriber = 13;
    static final int TRANSACTION_getMsisdn = 16;
    static final int TRANSACTION_getMsisdnForSubscriber = 17;
    static final int TRANSACTION_getNaiForSubscriber = 2;
    static final int TRANSACTION_getSubscriberId = 7;
    static final int TRANSACTION_getSubscriberIdForSubscriber = 8;
    static final int TRANSACTION_getVoiceMailAlphaTag = 25;
    static final int TRANSACTION_getVoiceMailAlphaTagForSubscriber = 26;
    static final int TRANSACTION_getVoiceMailNumber = 18;
    static final int TRANSACTION_getVoiceMailNumberForSubscriber = 19;
    static final int TRANSACTION_resetCarrierKeysForImsiEncryption = 24;
    static final int TRANSACTION_setCarrierInfoForImsiEncryption = 23;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.IPhoneSubInfo");
    }
    
    public static IPhoneSubInfo asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.IPhoneSubInfo");
      if ((localIInterface != null) && ((localIInterface instanceof IPhoneSubInfo))) {
        return (IPhoneSubInfo)localIInterface;
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
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getIccSimChallengeResponse(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getIsimPcscf(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getIsimIst(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getIsimImpu(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getIsimDomain(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getIsimImpi(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getVoiceMailAlphaTagForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getVoiceMailAlphaTag(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          resetCarrierKeysForImsiEncryption(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramInt1 = paramParcel1.readInt();
          String str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsiEncryptionInfo)ImsiEncryptionInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          setCarrierInfoForImsiEncryption(paramInt1, str, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getCarrierInfoForImsiEncryption(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
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
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getCompleteVoiceMailNumberForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getCompleteVoiceMailNumber();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getVoiceMailNumberForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getVoiceMailNumber(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getMsisdnForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getMsisdn(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getLine1AlphaTagForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getLine1AlphaTag(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getLine1NumberForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getLine1Number(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getIccSerialNumberForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getIccSerialNumber(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getGroupIdLevel1ForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getSubscriberIdForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getSubscriberId(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getDeviceSvnUsingSubId(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getDeviceSvn(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getImeiForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getDeviceIdForPhone(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
          paramParcel1 = getNaiForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.IPhoneSubInfo");
        paramParcel1 = getDeviceId(paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeString(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.IPhoneSubInfo");
      return true;
    }
    
    private static class Proxy
      implements IPhoneSubInfo
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
      
      public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ImsiEncryptionInfo)ImsiEncryptionInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public String getCompleteVoiceMailNumber()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          mRemote.transact(20, localParcel1, localParcel2, 0);
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
      
      public String getCompleteVoiceMailNumberForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
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
      
      public String getDeviceId(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDeviceIdForPhone(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDeviceSvn(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDeviceSvnUsingSubId(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getGroupIdLevel1ForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getIccSerialNumber(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getIccSerialNumberForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getIccSimChallengeResponse(int paramInt1, int paramInt2, int paramInt3, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getImeiForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.IPhoneSubInfo";
      }
      
      public String getIsimDomain(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
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
      
      public String getIsimImpi(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          mRemote.transact(27, localParcel1, localParcel2, 0);
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
      
      public String[] getIsimImpu(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getIsimIst(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          mRemote.transact(30, localParcel1, localParcel2, 0);
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
      
      public String[] getIsimPcscf(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getLine1AlphaTag(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeString(paramString);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getLine1AlphaTagForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getLine1Number(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeString(paramString);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getLine1NumberForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getMsisdn(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeString(paramString);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getMsisdnForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getNaiForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getSubscriberId(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeString(paramString);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getSubscriberIdForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getVoiceMailAlphaTag(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeString(paramString);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getVoiceMailAlphaTagForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getVoiceMailNumber(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeString(paramString);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getVoiceMailNumberForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resetCarrierKeysForImsiEncryption(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public void setCarrierInfoForImsiEncryption(int paramInt, String paramString, ImsiEncryptionInfo paramImsiEncryptionInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IPhoneSubInfo");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          if (paramImsiEncryptionInfo != null)
          {
            localParcel1.writeInt(1);
            paramImsiEncryptionInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
