package com.android.internal.telephony;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface ISms
  extends IInterface
{
  public abstract boolean copyMessageToIccEfForSubscriber(int paramInt1, String paramString, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract String createAppSpecificSmsToken(int paramInt, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract boolean disableCellBroadcastForSubscriber(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract boolean disableCellBroadcastRangeForSubscriber(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract boolean enableCellBroadcastForSubscriber(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract boolean enableCellBroadcastRangeForSubscriber(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getImsSmsFormatForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract int getPreferredSmsSubscription()
    throws RemoteException;
  
  public abstract int getPremiumSmsPermission(String paramString)
    throws RemoteException;
  
  public abstract int getPremiumSmsPermissionForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int getSmsCapacityOnIccForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract void injectSmsPduForSubscriber(int paramInt, byte[] paramArrayOfByte, String paramString, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract boolean isImsSmsSupportedForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract boolean isSMSPromptEnabled()
    throws RemoteException;
  
  public abstract boolean isSmsSimPickActivityNeeded(int paramInt)
    throws RemoteException;
  
  public abstract void sendDataForSubscriber(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
    throws RemoteException;
  
  public abstract void sendDataForSubscriberWithSelfPermissions(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
    throws RemoteException;
  
  public abstract void sendMultipartTextForSubscriber(int paramInt, String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void sendMultipartTextForSubscriberWithOptions(int paramInt1, String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
    throws RemoteException;
  
  public abstract void sendStoredMultipartText(int paramInt, String paramString1, Uri paramUri, String paramString2, List<PendingIntent> paramList1, List<PendingIntent> paramList2)
    throws RemoteException;
  
  public abstract void sendStoredText(int paramInt, String paramString1, Uri paramUri, String paramString2, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
    throws RemoteException;
  
  public abstract void sendTextForSubscriber(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void sendTextForSubscriberWithOptions(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
    throws RemoteException;
  
  public abstract void sendTextForSubscriberWithSelfPermissions(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPremiumSmsPermission(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setPremiumSmsPermissionForSubscriber(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract boolean updateMessageOnIccEfForSubscriber(int paramInt1, String paramString, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISms
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.ISms";
    static final int TRANSACTION_copyMessageToIccEfForSubscriber = 3;
    static final int TRANSACTION_createAppSpecificSmsToken = 27;
    static final int TRANSACTION_disableCellBroadcastForSubscriber = 13;
    static final int TRANSACTION_disableCellBroadcastRangeForSubscriber = 15;
    static final int TRANSACTION_enableCellBroadcastForSubscriber = 12;
    static final int TRANSACTION_enableCellBroadcastRangeForSubscriber = 14;
    static final int TRANSACTION_getAllMessagesFromIccEfForSubscriber = 1;
    static final int TRANSACTION_getImsSmsFormatForSubscriber = 23;
    static final int TRANSACTION_getPreferredSmsSubscription = 22;
    static final int TRANSACTION_getPremiumSmsPermission = 16;
    static final int TRANSACTION_getPremiumSmsPermissionForSubscriber = 17;
    static final int TRANSACTION_getSmsCapacityOnIccForSubscriber = 28;
    static final int TRANSACTION_injectSmsPduForSubscriber = 9;
    static final int TRANSACTION_isImsSmsSupportedForSubscriber = 20;
    static final int TRANSACTION_isSMSPromptEnabled = 24;
    static final int TRANSACTION_isSmsSimPickActivityNeeded = 21;
    static final int TRANSACTION_sendDataForSubscriber = 4;
    static final int TRANSACTION_sendDataForSubscriberWithSelfPermissions = 5;
    static final int TRANSACTION_sendMultipartTextForSubscriber = 10;
    static final int TRANSACTION_sendMultipartTextForSubscriberWithOptions = 11;
    static final int TRANSACTION_sendStoredMultipartText = 26;
    static final int TRANSACTION_sendStoredText = 25;
    static final int TRANSACTION_sendTextForSubscriber = 6;
    static final int TRANSACTION_sendTextForSubscriberWithOptions = 8;
    static final int TRANSACTION_sendTextForSubscriberWithSelfPermissions = 7;
    static final int TRANSACTION_setPremiumSmsPermission = 18;
    static final int TRANSACTION_setPremiumSmsPermissionForSubscriber = 19;
    static final int TRANSACTION_updateMessageOnIccEfForSubscriber = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.ISms");
    }
    
    public static ISms asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.ISms");
      if ((localIInterface != null) && ((localIInterface instanceof ISms))) {
        return (ISms)localIInterface;
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
        Object localObject5 = null;
        Object localObject6;
        boolean bool1;
        boolean bool2;
        String str;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = getSmsCapacityOnIccForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = paramParcel1.readInt();
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject5;
          }
          paramParcel1 = createAppSpecificSmsToken(paramInt1, (String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = paramParcel1.readInt();
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          sendStoredMultipartText(paramInt1, (String)localObject2, (Uri)localObject1, paramParcel1.readString(), paramParcel1.createTypedArrayList(PendingIntent.CREATOR), paramParcel1.createTypedArrayList(PendingIntent.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = paramParcel1.readInt();
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          localObject4 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          sendStoredText(paramInt1, (String)localObject3, (Uri)localObject1, (String)localObject4, (PendingIntent)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = isSMSPromptEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramParcel1 = getImsSmsFormatForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = getPreferredSmsSubscription();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = isSmsSimPickActivityNeeded(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = isImsSmsSupportedForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          setPremiumSmsPermissionForSubscriber(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          setPremiumSmsPermission(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = getPremiumSmsPermissionForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = getPremiumSmsPermission(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = disableCellBroadcastRangeForSubscriber(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = enableCellBroadcastRangeForSubscriber(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = disableCellBroadcastForSubscriber(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = enableCellBroadcastForSubscriber(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt2 = paramParcel1.readInt();
          localObject4 = paramParcel1.readString();
          localObject2 = paramParcel1.readString();
          localObject5 = paramParcel1.readString();
          localObject6 = paramParcel1.createStringArrayList();
          localObject1 = paramParcel1.createTypedArrayList(PendingIntent.CREATOR);
          localObject3 = paramParcel1.createTypedArrayList(PendingIntent.CREATOR);
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          sendMultipartTextForSubscriberWithOptions(paramInt2, (String)localObject4, (String)localObject2, (String)localObject5, (List)localObject6, (List)localObject1, (List)localObject3, bool1, paramInt1, bool2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = paramParcel1.readInt();
          localObject4 = paramParcel1.readString();
          localObject2 = paramParcel1.readString();
          localObject6 = paramParcel1.readString();
          localObject3 = paramParcel1.createStringArrayList();
          localObject5 = paramParcel1.createTypedArrayList(PendingIntent.CREATOR);
          localObject1 = paramParcel1.createTypedArrayList(PendingIntent.CREATOR);
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          sendMultipartTextForSubscriber(paramInt1, (String)localObject4, (String)localObject2, (String)localObject6, (List)localObject3, (List)localObject5, (List)localObject1, bool1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = paramParcel1.readInt();
          localObject3 = paramParcel1.createByteArray();
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          injectSmsPduForSubscriber(paramInt1, (byte[])localObject3, (String)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt2 = paramParcel1.readInt();
          localObject6 = paramParcel1.readString();
          localObject4 = paramParcel1.readString();
          localObject3 = paramParcel1.readString();
          localObject5 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          sendTextForSubscriberWithOptions(paramInt2, (String)localObject6, (String)localObject4, (String)localObject3, (String)localObject5, (PendingIntent)localObject1, (PendingIntent)localObject2, bool1, paramInt1, bool2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = paramParcel1.readInt();
          localObject6 = paramParcel1.readString();
          str = paramParcel1.readString();
          localObject5 = paramParcel1.readString();
          localObject4 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);; localObject2 = localObject3) {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          sendTextForSubscriberWithSelfPermissions(paramInt1, (String)localObject6, str, (String)localObject5, (String)localObject4, (PendingIntent)localObject1, (PendingIntent)localObject2, bool1);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = paramParcel1.readInt();
          localObject6 = paramParcel1.readString();
          str = paramParcel1.readString();
          localObject3 = paramParcel1.readString();
          localObject5 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);; localObject2 = localObject4) {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          sendTextForSubscriber(paramInt1, (String)localObject6, str, (String)localObject3, (String)localObject5, (PendingIntent)localObject1, (PendingIntent)localObject2, bool1);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt2 = paramParcel1.readInt();
          localObject4 = paramParcel1.readString();
          localObject5 = paramParcel1.readString();
          localObject3 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          localObject2 = paramParcel1.createByteArray();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          sendDataForSubscriberWithSelfPermissions(paramInt2, (String)localObject4, (String)localObject5, (String)localObject3, paramInt1, (byte[])localObject2, (PendingIntent)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = paramParcel1.readInt();
          localObject5 = paramParcel1.readString();
          localObject2 = paramParcel1.readString();
          localObject3 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          localObject4 = paramParcel1.createByteArray();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          sendDataForSubscriber(paramInt1, (String)localObject5, (String)localObject2, (String)localObject3, paramInt2, (byte[])localObject4, (PendingIntent)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = copyMessageToIccEfForSubscriber(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
          paramInt1 = updateMessageOnIccEfForSubscriber(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.ISms");
        paramParcel1 = getAllMessagesFromIccEfForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.ISms");
      return true;
    }
    
    private static class Proxy
      implements ISms
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
      
      public boolean copyMessageToIccEfForSubscriber(int paramInt1, String paramString, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(3, localParcel1, localParcel2, 0);
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
      
      public String createAppSpecificSmsToken(int paramInt, String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(27, localParcel1, localParcel2, 0);
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
      
      public boolean disableCellBroadcastForSubscriber(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(13, localParcel1, localParcel2, 0);
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
      
      public boolean disableCellBroadcastRangeForSubscriber(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(15, localParcel1, localParcel2, 0);
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
      
      public boolean enableCellBroadcastForSubscriber(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(12, localParcel1, localParcel2, 0);
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
      
      public boolean enableCellBroadcastRangeForSubscriber(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(14, localParcel1, localParcel2, 0);
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
      
      public List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(SmsRawData.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getImsSmsFormatForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.ISms";
      }
      
      public int getPreferredSmsSubscription()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          mRemote.transact(22, localParcel1, localParcel2, 0);
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
      
      public int getPremiumSmsPermission(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeString(paramString);
          mRemote.transact(16, localParcel1, localParcel2, 0);
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
      
      public int getPremiumSmsPermissionForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(17, localParcel1, localParcel2, 0);
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
      
      public int getSmsCapacityOnIccForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
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
      
      public void injectSmsPduForSubscriber(int paramInt, byte[] paramArrayOfByte, String paramString, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeString(paramString);
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isImsSmsSupportedForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(20, localParcel1, localParcel2, 0);
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
      
      public boolean isSMSPromptEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
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
      
      public boolean isSmsSimPickActivityNeeded(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(21, localParcel1, localParcel2, 0);
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
      
      public void sendDataForSubscriber(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeByteArray(paramArrayOfByte);
          if (paramPendingIntent1 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent2 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendDataForSubscriberWithSelfPermissions(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeByteArray(paramArrayOfByte);
          if (paramPendingIntent1 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent2 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendMultipartTextForSubscriber(int paramInt, String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeStringList(paramList);
          localParcel1.writeTypedList(paramList1);
          localParcel1.writeTypedList(paramList2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public void sendMultipartTextForSubscriberWithOptions(int paramInt1, String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 34
        //   14: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 12
        //   19: iload_1
        //   20: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   23: aload 12
        //   25: aload_2
        //   26: invokevirtual 45	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   29: aload 12
        //   31: aload_3
        //   32: invokevirtual 45	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   35: aload 12
        //   37: aload 4
        //   39: invokevirtual 45	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   42: aload 12
        //   44: aload 5
        //   46: invokevirtual 124	android/os/Parcel:writeStringList	(Ljava/util/List;)V
        //   49: aload 12
        //   51: aload 6
        //   53: invokevirtual 127	android/os/Parcel:writeTypedList	(Ljava/util/List;)V
        //   56: aload 12
        //   58: aload 7
        //   60: invokevirtual 127	android/os/Parcel:writeTypedList	(Ljava/util/List;)V
        //   63: aload 12
        //   65: iload 8
        //   67: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   70: aload 12
        //   72: iload 9
        //   74: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   77: aload 12
        //   79: iload 10
        //   81: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   84: aload 12
        //   86: iload 11
        //   88: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   91: aload_0
        //   92: getfield 19	com/android/internal/telephony/ISms$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   95: bipush 11
        //   97: aload 12
        //   99: aload 13
        //   101: iconst_0
        //   102: invokeinterface 55 5 0
        //   107: pop
        //   108: aload 13
        //   110: invokevirtual 58	android/os/Parcel:readException	()V
        //   113: aload 13
        //   115: invokevirtual 65	android/os/Parcel:recycle	()V
        //   118: aload 12
        //   120: invokevirtual 65	android/os/Parcel:recycle	()V
        //   123: return
        //   124: astore_2
        //   125: goto +44 -> 169
        //   128: astore_2
        //   129: goto +40 -> 169
        //   132: astore_2
        //   133: goto +36 -> 169
        //   136: astore_2
        //   137: goto +32 -> 169
        //   140: astore_2
        //   141: goto +28 -> 169
        //   144: astore_2
        //   145: goto +24 -> 169
        //   148: astore_2
        //   149: goto +20 -> 169
        //   152: astore_2
        //   153: goto +16 -> 169
        //   156: astore_2
        //   157: goto +12 -> 169
        //   160: astore_2
        //   161: goto +8 -> 169
        //   164: astore_2
        //   165: goto +4 -> 169
        //   168: astore_2
        //   169: aload 13
        //   171: invokevirtual 65	android/os/Parcel:recycle	()V
        //   174: aload 12
        //   176: invokevirtual 65	android/os/Parcel:recycle	()V
        //   179: aload_2
        //   180: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	181	0	this	Proxy
        //   0	181	1	paramInt1	int
        //   0	181	2	paramString1	String
        //   0	181	3	paramString2	String
        //   0	181	4	paramString3	String
        //   0	181	5	paramList	List<String>
        //   0	181	6	paramList1	List<PendingIntent>
        //   0	181	7	paramList2	List<PendingIntent>
        //   0	181	8	paramBoolean1	boolean
        //   0	181	9	paramInt2	int
        //   0	181	10	paramBoolean2	boolean
        //   0	181	11	paramInt3	int
        //   3	172	12	localParcel1	Parcel
        //   8	162	13	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   91	113	124	finally
        //   84	91	128	finally
        //   77	84	132	finally
        //   70	77	136	finally
        //   63	70	140	finally
        //   56	63	144	finally
        //   49	56	148	finally
        //   42	49	152	finally
        //   35	42	156	finally
        //   29	35	160	finally
        //   23	29	164	finally
        //   10	23	168	finally
      }
      
      public void sendStoredMultipartText(int paramInt, String paramString1, Uri paramUri, String paramString2, List<PendingIntent> paramList1, List<PendingIntent> paramList2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          localParcel1.writeTypedList(paramList1);
          localParcel1.writeTypedList(paramList2);
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
      
      public void sendStoredText(int paramInt, String paramString1, Uri paramUri, String paramString2, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          if (paramPendingIntent1 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent2 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendTextForSubscriber(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          if (paramPendingIntent1 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent2 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public void sendTextForSubscriberWithOptions(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, int paramInt3)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 34
        //   14: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 12
        //   19: iload_1
        //   20: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   23: aload 12
        //   25: aload_2
        //   26: invokevirtual 45	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   29: aload 12
        //   31: aload_3
        //   32: invokevirtual 45	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   35: aload 12
        //   37: aload 4
        //   39: invokevirtual 45	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   42: aload 12
        //   44: aload 5
        //   46: invokevirtual 45	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   49: aload 6
        //   51: ifnull +20 -> 71
        //   54: aload 12
        //   56: iconst_1
        //   57: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   60: aload 6
        //   62: aload 12
        //   64: iconst_0
        //   65: invokevirtual 74	android/app/PendingIntent:writeToParcel	(Landroid/os/Parcel;I)V
        //   68: goto +9 -> 77
        //   71: aload 12
        //   73: iconst_0
        //   74: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   77: aload 7
        //   79: ifnull +20 -> 99
        //   82: aload 12
        //   84: iconst_1
        //   85: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   88: aload 7
        //   90: aload 12
        //   92: iconst_0
        //   93: invokevirtual 74	android/app/PendingIntent:writeToParcel	(Landroid/os/Parcel;I)V
        //   96: goto +9 -> 105
        //   99: aload 12
        //   101: iconst_0
        //   102: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   105: aload 12
        //   107: iload 8
        //   109: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   112: aload 12
        //   114: iload 9
        //   116: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   119: aload 12
        //   121: iload 10
        //   123: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   126: aload 12
        //   128: iload 11
        //   130: invokevirtual 42	android/os/Parcel:writeInt	(I)V
        //   133: aload_0
        //   134: getfield 19	com/android/internal/telephony/ISms$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   137: bipush 8
        //   139: aload 12
        //   141: aload 13
        //   143: iconst_0
        //   144: invokeinterface 55 5 0
        //   149: pop
        //   150: aload 13
        //   152: invokevirtual 58	android/os/Parcel:readException	()V
        //   155: aload 13
        //   157: invokevirtual 65	android/os/Parcel:recycle	()V
        //   160: aload 12
        //   162: invokevirtual 65	android/os/Parcel:recycle	()V
        //   165: return
        //   166: astore_2
        //   167: goto +40 -> 207
        //   170: astore_2
        //   171: goto +36 -> 207
        //   174: astore_2
        //   175: goto +32 -> 207
        //   178: astore_2
        //   179: goto +28 -> 207
        //   182: astore_2
        //   183: goto +24 -> 207
        //   186: astore_2
        //   187: goto +20 -> 207
        //   190: astore_2
        //   191: goto +16 -> 207
        //   194: astore_2
        //   195: goto +12 -> 207
        //   198: astore_2
        //   199: goto +8 -> 207
        //   202: astore_2
        //   203: goto +4 -> 207
        //   206: astore_2
        //   207: aload 13
        //   209: invokevirtual 65	android/os/Parcel:recycle	()V
        //   212: aload 12
        //   214: invokevirtual 65	android/os/Parcel:recycle	()V
        //   217: aload_2
        //   218: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	219	0	this	Proxy
        //   0	219	1	paramInt1	int
        //   0	219	2	paramString1	String
        //   0	219	3	paramString2	String
        //   0	219	4	paramString3	String
        //   0	219	5	paramString4	String
        //   0	219	6	paramPendingIntent1	PendingIntent
        //   0	219	7	paramPendingIntent2	PendingIntent
        //   0	219	8	paramBoolean1	boolean
        //   0	219	9	paramInt2	int
        //   0	219	10	paramBoolean2	boolean
        //   0	219	11	paramInt3	int
        //   3	210	12	localParcel1	Parcel
        //   8	200	13	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   133	155	166	finally
        //   126	133	170	finally
        //   119	126	174	finally
        //   112	119	178	finally
        //   105	112	182	finally
        //   42	49	186	finally
        //   54	68	186	finally
        //   71	77	186	finally
        //   82	96	186	finally
        //   99	105	186	finally
        //   35	42	190	finally
        //   29	35	194	finally
        //   23	29	198	finally
        //   17	23	202	finally
        //   10	17	206	finally
      }
      
      public void sendTextForSubscriberWithSelfPermissions(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          if (paramPendingIntent1 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent2 != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPremiumSmsPermission(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPremiumSmsPermissionForSubscriber(int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean updateMessageOnIccEfForSubscriber(int paramInt1, String paramString, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ISms");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeByteArray(paramArrayOfByte);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(2, localParcel1, localParcel2, 0);
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
    }
  }
}
