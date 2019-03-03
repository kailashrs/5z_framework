package com.android.internal.telecom;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.PhoneAccountHandle;
import java.util.List;

public abstract interface IInCallAdapter
  extends IInterface
{
  public abstract void answerCall(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void conference(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void deflectCall(String paramString, Uri paramUri)
    throws RemoteException;
  
  public abstract void disconnectCall(String paramString)
    throws RemoteException;
  
  public abstract void handoverTo(String paramString, PhoneAccountHandle paramPhoneAccountHandle, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void holdCall(String paramString)
    throws RemoteException;
  
  public abstract void mergeConference(String paramString)
    throws RemoteException;
  
  public abstract void mute(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void phoneAccountSelected(String paramString, PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void playDtmfTone(String paramString, char paramChar)
    throws RemoteException;
  
  public abstract void postDialContinue(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void pullExternalCall(String paramString)
    throws RemoteException;
  
  public abstract void putExtras(String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void rejectCall(String paramString1, boolean paramBoolean, String paramString2)
    throws RemoteException;
  
  public abstract void removeExtras(String paramString, List<String> paramList)
    throws RemoteException;
  
  public abstract void respondToRttRequest(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void sendCallEvent(String paramString1, String paramString2, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void sendRttRequest(String paramString)
    throws RemoteException;
  
  public abstract void setAudioRoute(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setRttMode(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void splitFromConference(String paramString)
    throws RemoteException;
  
  public abstract void stopDtmfTone(String paramString)
    throws RemoteException;
  
  public abstract void stopRtt(String paramString)
    throws RemoteException;
  
  public abstract void swapConference(String paramString)
    throws RemoteException;
  
  public abstract void turnOffProximitySensor(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void turnOnProximitySensor()
    throws RemoteException;
  
  public abstract void unholdCall(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInCallAdapter
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.IInCallAdapter";
    static final int TRANSACTION_answerCall = 1;
    static final int TRANSACTION_conference = 13;
    static final int TRANSACTION_deflectCall = 2;
    static final int TRANSACTION_disconnectCall = 4;
    static final int TRANSACTION_handoverTo = 27;
    static final int TRANSACTION_holdCall = 5;
    static final int TRANSACTION_mergeConference = 15;
    static final int TRANSACTION_mute = 7;
    static final int TRANSACTION_phoneAccountSelected = 12;
    static final int TRANSACTION_playDtmfTone = 9;
    static final int TRANSACTION_postDialContinue = 11;
    static final int TRANSACTION_pullExternalCall = 19;
    static final int TRANSACTION_putExtras = 21;
    static final int TRANSACTION_rejectCall = 3;
    static final int TRANSACTION_removeExtras = 22;
    static final int TRANSACTION_respondToRttRequest = 24;
    static final int TRANSACTION_sendCallEvent = 20;
    static final int TRANSACTION_sendRttRequest = 23;
    static final int TRANSACTION_setAudioRoute = 8;
    static final int TRANSACTION_setRttMode = 26;
    static final int TRANSACTION_splitFromConference = 14;
    static final int TRANSACTION_stopDtmfTone = 10;
    static final int TRANSACTION_stopRtt = 25;
    static final int TRANSACTION_swapConference = 16;
    static final int TRANSACTION_turnOffProximitySensor = 18;
    static final int TRANSACTION_turnOnProximitySensor = 17;
    static final int TRANSACTION_unholdCall = 6;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.IInCallAdapter");
    }
    
    public static IInCallAdapter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.IInCallAdapter");
      if ((localIInterface != null) && ((localIInterface instanceof IInCallAdapter))) {
        return (IInCallAdapter)localIInterface;
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
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        Object localObject1 = null;
        String str1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        String str2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          handoverTo(str1, paramParcel2, paramInt1, paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          setRttMode(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          stopRtt(paramParcel1.readString());
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          respondToRttRequest(paramParcel2, paramInt1, bool6);
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          sendRttRequest(paramParcel1.readString());
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          removeExtras(paramParcel1.readString(), paramParcel1.createStringArrayList());
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          putExtras(paramParcel2, paramParcel1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          paramParcel2 = paramParcel1.readString();
          str2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          sendCallEvent(paramParcel2, str2, paramInt1, paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          pullExternalCall(paramParcel1.readString());
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          bool6 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          turnOffProximitySensor(bool6);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          turnOnProximitySensor();
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          swapConference(paramParcel1.readString());
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          mergeConference(paramParcel1.readString());
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          splitFromConference(paramParcel1.readString());
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          conference(paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          bool6 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          phoneAccountSelected(str2, paramParcel2, bool6);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          paramParcel2 = paramParcel1.readString();
          bool6 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          postDialContinue(paramParcel2, bool6);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          stopDtmfTone(paramParcel1.readString());
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          playDtmfTone(paramParcel1.readString(), (char)paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          setAudioRoute(paramParcel1.readInt(), paramParcel1.readString());
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          bool6 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          mute(bool6);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          unholdCall(paramParcel1.readString());
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          holdCall(paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          disconnectCall(paramParcel1.readString());
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          paramParcel2 = paramParcel1.readString();
          bool6 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          rejectCall(paramParcel2, bool6, paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          deflectCall(paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telecom.IInCallAdapter");
        answerCall(paramParcel1.readString(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("com.android.internal.telecom.IInCallAdapter");
      return true;
    }
    
    private static class Proxy
      implements IInCallAdapter
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void answerCall(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void conference(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deflectCall(String paramString, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
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
      
      public void disconnectCall(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telecom.IInCallAdapter";
      }
      
      public void handoverTo(String paramString, PhoneAccountHandle paramPhoneAccountHandle, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          if (paramPhoneAccountHandle != null)
          {
            localParcel.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
          mRemote.transact(27, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void holdCall(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void mergeConference(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void mute(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void phoneAccountSelected(String paramString, PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          if (paramPhoneAccountHandle != null)
          {
            localParcel.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void playDtmfTone(String paramString, char paramChar)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramChar);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void postDialContinue(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void pullExternalCall(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void putExtras(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void rejectCall(String paramString1, boolean paramBoolean, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramBoolean);
          localParcel.writeString(paramString2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeExtras(String paramString, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          localParcel.writeStringList(paramList);
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void respondToRttRequest(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(24, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendCallEvent(String paramString1, String paramString2, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
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
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendRttRequest(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(23, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setAudioRoute(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setRttMode(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(26, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void splitFromConference(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopDtmfTone(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopRtt(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(25, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void swapConference(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void turnOffProximitySensor(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void turnOnProximitySensor()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unholdCall(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IInCallAdapter");
          localParcel.writeString(paramString);
          mRemote.transact(6, localParcel, null, 1);
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
