package com.android.internal.telephony;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IMms
  extends IInterface
{
  public abstract Uri addMultimediaMessageDraft(String paramString, Uri paramUri)
    throws RemoteException;
  
  public abstract Uri addTextMessageDraft(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract boolean archiveStoredConversation(String paramString, long paramLong, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean deleteStoredConversation(String paramString, long paramLong)
    throws RemoteException;
  
  public abstract boolean deleteStoredMessage(String paramString, Uri paramUri)
    throws RemoteException;
  
  public abstract void downloadMessage(int paramInt, String paramString1, String paramString2, Uri paramUri, Bundle paramBundle, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract boolean getAutoPersisting()
    throws RemoteException;
  
  public abstract Bundle getCarrierConfigValues(int paramInt)
    throws RemoteException;
  
  public abstract Uri importMultimediaMessage(String paramString1, Uri paramUri, String paramString2, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract Uri importTextMessage(String paramString1, String paramString2, int paramInt, String paramString3, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void sendMessage(int paramInt, String paramString1, Uri paramUri, String paramString2, Bundle paramBundle, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void sendStoredMessage(int paramInt, String paramString, Uri paramUri, Bundle paramBundle, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract void setAutoPersisting(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean updateStoredMessageStatus(String paramString, Uri paramUri, ContentValues paramContentValues)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IMms
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.IMms";
    static final int TRANSACTION_addMultimediaMessageDraft = 11;
    static final int TRANSACTION_addTextMessageDraft = 10;
    static final int TRANSACTION_archiveStoredConversation = 9;
    static final int TRANSACTION_deleteStoredConversation = 7;
    static final int TRANSACTION_deleteStoredMessage = 6;
    static final int TRANSACTION_downloadMessage = 2;
    static final int TRANSACTION_getAutoPersisting = 14;
    static final int TRANSACTION_getCarrierConfigValues = 3;
    static final int TRANSACTION_importMultimediaMessage = 5;
    static final int TRANSACTION_importTextMessage = 4;
    static final int TRANSACTION_sendMessage = 1;
    static final int TRANSACTION_sendStoredMessage = 12;
    static final int TRANSACTION_setAutoPersisting = 13;
    static final int TRANSACTION_updateStoredMessageStatus = 8;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.IMms");
    }
    
    public static IMms asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.IMms");
      if ((localIInterface != null) && ((localIInterface instanceof IMms))) {
        return (IMms)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        String str1 = null;
        String str2 = null;
        String str3 = null;
        long l;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          paramInt1 = getAutoPersisting();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setAutoPersisting((String)localObject1, bool2);
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          paramInt1 = paramParcel1.readInt();
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = str3) {
            break;
          }
          sendStoredMessage(paramInt1, str1, (Uri)localObject1, (Bundle)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          paramParcel1 = addMultimediaMessageDraft((String)localObject2, paramParcel1);
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
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          paramParcel1 = addTextMessageDraft(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
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
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          localObject1 = paramParcel1.readString();
          l = paramParcel1.readLong();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          paramInt1 = archiveStoredConversation((String)localObject1, l, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ContentValues)ContentValues.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          paramInt1 = updateStoredMessageStatus(str1, (Uri)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          paramInt1 = deleteStoredConversation(paramParcel1.readString(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = deleteStoredMessage((String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);; localObject1 = localObject4) {
            break;
          }
          str1 = paramParcel1.readString();
          l = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          paramParcel1 = importMultimediaMessage((String)localObject2, (Uri)localObject1, str1, l, bool2, bool1);
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
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          localObject1 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          localObject2 = paramParcel1.readString();
          l = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          paramParcel1 = importTextMessage((String)localObject1, str1, paramInt1, (String)localObject2, l, bool2, bool1);
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
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          paramParcel1 = getCarrierConfigValues(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
          paramInt1 = paramParcel1.readInt();
          str2 = paramParcel1.readString();
          str3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = str1) {
            break;
          }
          downloadMessage(paramInt1, str2, str3, (Uri)localObject1, (Bundle)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.IMms");
        paramInt1 = paramParcel1.readInt();
        str1 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          localObject1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject1 = null;
        }
        str3 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject2 = null;
        }
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = str2) {
          break;
        }
        sendMessage(paramInt1, str1, (Uri)localObject1, str3, (Bundle)localObject2, paramParcel1);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.IMms");
      return true;
    }
    
    private static class Proxy
      implements IMms
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public Uri addMultimediaMessageDraft(String paramString, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeString(paramString);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
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
      
      public Uri addTextMessageDraft(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
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
      
      public boolean archiveStoredConversation(String paramString, long paramLong, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramBoolean);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean deleteStoredConversation(String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(7, localParcel1, localParcel2, 0);
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
      
      public boolean deleteStoredMessage(String paramString, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
      
      public void downloadMessage(int paramInt, String paramString1, String paramString2, Uri paramUri, Bundle paramBundle, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getAutoPersisting()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(14, localParcel1, localParcel2, 0);
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
      
      public Bundle getCarrierConfigValues(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Bundle localBundle;
          if (localParcel2.readInt() != 0) {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localBundle = null;
          }
          return localBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.IMms";
      }
      
      public Uri importMultimediaMessage(String paramString1, Uri paramUri, String paramString2, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
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
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
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
      
      public Uri importTextMessage(String paramString1, String paramString2, int paramInt, String paramString3, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString3);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
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
      
      public void sendMessage(int paramInt, String paramString1, Uri paramUri, String paramString2, Bundle paramBundle, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
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
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendStoredMessage(int paramInt, String paramString, Uri paramUri, Bundle paramBundle, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAutoPersisting(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean updateStoredMessageStatus(String paramString, Uri paramUri, ContentValues paramContentValues)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.IMms");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramContentValues != null)
          {
            localParcel1.writeInt(1);
            paramContentValues.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
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
