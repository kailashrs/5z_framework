package com.android.internal.telephony;

import android.content.ContentValues;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.telephony.uicc.AdnRecord;
import java.util.ArrayList;
import java.util.List;

public abstract interface IIccPhoneBook
  extends IInterface
{
  public abstract int getADNReadyForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract int[] getAdnRecordsCapacity()
    throws RemoteException;
  
  public abstract int[] getAdnRecordsCapacityForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract List<AdnRecord> getAdnRecordsInEf(int paramInt)
    throws RemoteException;
  
  public abstract List<AdnRecord> getAdnRecordsInEfForSubscriber(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int[] getAdnRecordsSize(int paramInt)
    throws RemoteException;
  
  public abstract int[] getAdnRecordsSizeForSubscriber(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int getRowIdForNewAddedRecordForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract int[] getSIMPhoneBookProperty(int paramInt)
    throws RemoteException;
  
  public abstract int[] getSIMPhoneBookPropertyForSubscriber(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean updateAdnRecordsInEfByIndex(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, String paramString4)
    throws RemoteException;
  
  public abstract boolean updateAdnRecordsInEfByIndexForSubscriber(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt3, String paramString5)
    throws RemoteException;
  
  public abstract boolean updateAdnRecordsInEfBySearch(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws RemoteException;
  
  public abstract boolean updateAdnRecordsInEfBySearchForSubscriber(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws RemoteException;
  
  public abstract boolean updateAdnRecordsWithContentValuesInEfBySearchUsingSubId(int paramInt1, int paramInt2, ContentValues paramContentValues, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IIccPhoneBook
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.IIccPhoneBook";
    static final int TRANSACTION_getADNReadyForSubscriber = 13;
    static final int TRANSACTION_getAdnRecordsCapacity = 14;
    static final int TRANSACTION_getAdnRecordsCapacityForSubscriber = 15;
    static final int TRANSACTION_getAdnRecordsInEf = 1;
    static final int TRANSACTION_getAdnRecordsInEfForSubscriber = 2;
    static final int TRANSACTION_getAdnRecordsSize = 8;
    static final int TRANSACTION_getAdnRecordsSizeForSubscriber = 9;
    static final int TRANSACTION_getRowIdForNewAddedRecordForSubscriber = 12;
    static final int TRANSACTION_getSIMPhoneBookProperty = 10;
    static final int TRANSACTION_getSIMPhoneBookPropertyForSubscriber = 11;
    static final int TRANSACTION_updateAdnRecordsInEfByIndex = 6;
    static final int TRANSACTION_updateAdnRecordsInEfByIndexForSubscriber = 7;
    static final int TRANSACTION_updateAdnRecordsInEfBySearch = 3;
    static final int TRANSACTION_updateAdnRecordsInEfBySearchForSubscriber = 4;
    static final int TRANSACTION_updateAdnRecordsWithContentValuesInEfBySearchUsingSubId = 5;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.IIccPhoneBook");
    }
    
    public static IIccPhoneBook asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.IIccPhoneBook");
      if ((localIInterface != null) && ((localIInterface instanceof IIccPhoneBook))) {
        return (IIccPhoneBook)localIInterface;
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
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramParcel1 = getAdnRecordsCapacityForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramParcel1 = getAdnRecordsCapacity();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramInt1 = getADNReadyForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramInt1 = getRowIdForNewAddedRecordForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramParcel1 = getSIMPhoneBookPropertyForSubscriber(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramParcel1 = getSIMPhoneBookProperty(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramParcel1 = getAdnRecordsSizeForSubscriber(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramParcel1 = getAdnRecordsSize(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramInt1 = updateAdnRecordsInEfByIndexForSubscriber(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramInt1 = updateAdnRecordsInEfByIndex(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          ContentValues localContentValues;
          if (paramParcel1.readInt() != 0) {
            localContentValues = (ContentValues)ContentValues.CREATOR.createFromParcel(paramParcel1);
          } else {
            localContentValues = null;
          }
          paramInt1 = updateAdnRecordsWithContentValuesInEfBySearchUsingSubId(paramInt1, paramInt2, localContentValues, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramInt1 = updateAdnRecordsInEfBySearchForSubscriber(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramInt1 = updateAdnRecordsInEfBySearch(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
          paramParcel1 = getAdnRecordsInEfForSubscriber(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.IIccPhoneBook");
        paramParcel1 = getAdnRecordsInEf(paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.IIccPhoneBook");
      return true;
    }
    
    private static class Proxy
      implements IIccPhoneBook
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
      
      public int getADNReadyForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
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
      
      public int[] getAdnRecordsCapacity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public int[] getAdnRecordsCapacityForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
      
      public List<AdnRecord> getAdnRecordsInEf(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(AdnRecord.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<AdnRecord> getAdnRecordsInEfForSubscriber(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(AdnRecord.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getAdnRecordsSize(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public int[] getAdnRecordsSizeForSubscriber(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.IIccPhoneBook";
      }
      
      public int getRowIdForNewAddedRecordForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
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
      
      public int[] getSIMPhoneBookProperty(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
      
      public int[] getSIMPhoneBookPropertyForSubscriber(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(11, localParcel1, localParcel2, 0);
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
      
      public boolean updateAdnRecordsInEfByIndex(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, String paramString4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString4);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(6, localParcel1, localParcel2, 0);
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
      
      public boolean updateAdnRecordsInEfByIndexForSubscriber(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt3, String paramString5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString5);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(7, localParcel1, localParcel2, 0);
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
      
      public boolean updateAdnRecordsInEfBySearch(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          localParcel1.writeString(paramString5);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(3, localParcel1, localParcel2, 0);
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
      
      public boolean updateAdnRecordsInEfBySearchForSubscriber(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          localParcel1.writeString(paramString5);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(4, localParcel1, localParcel2, 0);
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
      
      public boolean updateAdnRecordsWithContentValuesInEfBySearchUsingSubId(int paramInt1, int paramInt2, ContentValues paramContentValues, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IIccPhoneBook");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          boolean bool = true;
          if (paramContentValues != null)
          {
            localParcel1.writeInt(1);
            paramContentValues.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
            bool = false;
          }
          return bool;
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
