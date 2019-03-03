package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.aidl.IImsCallSessionListener;
import android.telephony.ims.aidl.IImsCallSessionListener.Stub;

public abstract interface IImsCallSession
  extends IInterface
{
  public abstract void accept(int paramInt, ImsStreamMediaProfile paramImsStreamMediaProfile)
    throws RemoteException;
  
  public abstract void close()
    throws RemoteException;
  
  public abstract void deflect(String paramString)
    throws RemoteException;
  
  public abstract void extendToConference(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract String getCallId()
    throws RemoteException;
  
  public abstract ImsCallProfile getCallProfile()
    throws RemoteException;
  
  public abstract ImsCallProfile getLocalCallProfile()
    throws RemoteException;
  
  public abstract String getProperty(String paramString)
    throws RemoteException;
  
  public abstract ImsCallProfile getRemoteCallProfile()
    throws RemoteException;
  
  public abstract int getState()
    throws RemoteException;
  
  public abstract IImsVideoCallProvider getVideoCallProvider()
    throws RemoteException;
  
  public abstract void hold(ImsStreamMediaProfile paramImsStreamMediaProfile)
    throws RemoteException;
  
  public abstract void inviteParticipants(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract boolean isInCall()
    throws RemoteException;
  
  public abstract boolean isMultiparty()
    throws RemoteException;
  
  public abstract void merge()
    throws RemoteException;
  
  public abstract void reject(int paramInt)
    throws RemoteException;
  
  public abstract void removeParticipants(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void resume(ImsStreamMediaProfile paramImsStreamMediaProfile)
    throws RemoteException;
  
  public abstract void sendDtmf(char paramChar, Message paramMessage)
    throws RemoteException;
  
  public abstract void sendRttMessage(String paramString)
    throws RemoteException;
  
  public abstract void sendRttModifyRequest(ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void sendRttModifyResponse(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void sendUssd(String paramString)
    throws RemoteException;
  
  public abstract void setListener(IImsCallSessionListener paramIImsCallSessionListener)
    throws RemoteException;
  
  public abstract void setMute(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void start(String paramString, ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void startConference(String[] paramArrayOfString, ImsCallProfile paramImsCallProfile)
    throws RemoteException;
  
  public abstract void startDtmf(char paramChar)
    throws RemoteException;
  
  public abstract void stopDtmf()
    throws RemoteException;
  
  public abstract void terminate(int paramInt)
    throws RemoteException;
  
  public abstract void update(int paramInt, ImsStreamMediaProfile paramImsStreamMediaProfile)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IImsCallSession
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsCallSession";
    static final int TRANSACTION_accept = 13;
    static final int TRANSACTION_close = 1;
    static final int TRANSACTION_deflect = 14;
    static final int TRANSACTION_extendToConference = 21;
    static final int TRANSACTION_getCallId = 2;
    static final int TRANSACTION_getCallProfile = 3;
    static final int TRANSACTION_getLocalCallProfile = 4;
    static final int TRANSACTION_getProperty = 6;
    static final int TRANSACTION_getRemoteCallProfile = 5;
    static final int TRANSACTION_getState = 7;
    static final int TRANSACTION_getVideoCallProvider = 28;
    static final int TRANSACTION_hold = 17;
    static final int TRANSACTION_inviteParticipants = 22;
    static final int TRANSACTION_isInCall = 8;
    static final int TRANSACTION_isMultiparty = 29;
    static final int TRANSACTION_merge = 19;
    static final int TRANSACTION_reject = 15;
    static final int TRANSACTION_removeParticipants = 23;
    static final int TRANSACTION_resume = 18;
    static final int TRANSACTION_sendDtmf = 24;
    static final int TRANSACTION_sendRttMessage = 32;
    static final int TRANSACTION_sendRttModifyRequest = 30;
    static final int TRANSACTION_sendRttModifyResponse = 31;
    static final int TRANSACTION_sendUssd = 27;
    static final int TRANSACTION_setListener = 9;
    static final int TRANSACTION_setMute = 10;
    static final int TRANSACTION_start = 11;
    static final int TRANSACTION_startConference = 12;
    static final int TRANSACTION_startDtmf = 25;
    static final int TRANSACTION_stopDtmf = 26;
    static final int TRANSACTION_terminate = 16;
    static final int TRANSACTION_update = 20;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsCallSession");
    }
    
    public static IImsCallSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsCallSession");
      if ((localIInterface != null) && ((localIInterface instanceof IImsCallSession))) {
        return (IImsCallSession)localIInterface;
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
        Object localObject5 = null;
        Object localObject6 = null;
        IImsVideoCallProvider localIImsVideoCallProvider = null;
        Object localObject7 = null;
        Object localObject8 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 32: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          sendRttMessage(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          sendRttModifyResponse(bool2);
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          sendRttModifyRequest(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramInt1 = isMultiparty();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          localIImsVideoCallProvider = getVideoCallProvider();
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject1;
          if (localIImsVideoCallProvider != null) {
            paramParcel1 = localIImsVideoCallProvider.asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          sendUssd(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          stopDtmf();
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          startDtmf((char)paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          char c = (char)paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Message)Message.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          sendDtmf(c, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          removeParticipants(paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          inviteParticipants(paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          extendToConference(paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsStreamMediaProfile)ImsStreamMediaProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          update(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          merge();
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsStreamMediaProfile)ImsStreamMediaProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          resume(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsStreamMediaProfile)ImsStreamMediaProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          hold(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          terminate(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          reject(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          deflect(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsStreamMediaProfile)ImsStreamMediaProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          accept(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          localObject1 = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIImsVideoCallProvider;
          }
          startConference((String[])localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          start((String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setMute(bool2);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          setListener(IImsCallSessionListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramInt1 = isInCall();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramInt1 = getState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramParcel1 = getProperty(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramParcel1 = getRemoteCallProfile();
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
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramParcel1 = getLocalCallProfile();
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
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramParcel1 = getCallProfile();
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
          paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
          paramParcel1 = getCallId();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsCallSession");
        close();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsCallSession");
      return true;
    }
    
    private static class Proxy
      implements IImsCallSession
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void accept(int paramInt, ImsStreamMediaProfile paramImsStreamMediaProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeInt(paramInt);
          if (paramImsStreamMediaProfile != null)
          {
            localParcel1.writeInt(1);
            paramImsStreamMediaProfile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void close()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
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
      
      public void deflect(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeString(paramString);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void extendToConference(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCallId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public ImsCallProfile getCallProfile()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ImsCallProfile localImsCallProfile;
          if (localParcel2.readInt() != 0) {
            localImsCallProfile = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(localParcel2);
          } else {
            localImsCallProfile = null;
          }
          return localImsCallProfile;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.ims.internal.IImsCallSession";
      }
      
      public ImsCallProfile getLocalCallProfile()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ImsCallProfile localImsCallProfile;
          if (localParcel2.readInt() != 0) {
            localImsCallProfile = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(localParcel2);
          } else {
            localImsCallProfile = null;
          }
          return localImsCallProfile;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getProperty(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
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
      
      public ImsCallProfile getRemoteCallProfile()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ImsCallProfile localImsCallProfile;
          if (localParcel2.readInt() != 0) {
            localImsCallProfile = (ImsCallProfile)ImsCallProfile.CREATOR.createFromParcel(localParcel2);
          } else {
            localImsCallProfile = null;
          }
          return localImsCallProfile;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
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
      
      public IImsVideoCallProvider getVideoCallProvider()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsVideoCallProvider localIImsVideoCallProvider = IImsVideoCallProvider.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsVideoCallProvider;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void hold(ImsStreamMediaProfile paramImsStreamMediaProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          if (paramImsStreamMediaProfile != null)
          {
            localParcel1.writeInt(1);
            paramImsStreamMediaProfile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void inviteParticipants(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isInCall()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(8, localParcel1, localParcel2, 0);
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
      
      public boolean isMultiparty()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(29, localParcel1, localParcel2, 0);
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
      
      public void merge()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
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
      
      public void reject(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeParticipants(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeStringArray(paramArrayOfString);
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
      
      public void resume(ImsStreamMediaProfile paramImsStreamMediaProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          if (paramImsStreamMediaProfile != null)
          {
            localParcel1.writeInt(1);
            paramImsStreamMediaProfile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void sendDtmf(char paramChar, Message paramMessage)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeInt(paramChar);
          if (paramMessage != null)
          {
            localParcel1.writeInt(1);
            paramMessage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void sendRttMessage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeString(paramString);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendRttModifyRequest(ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          if (paramImsCallProfile != null)
          {
            localParcel1.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendRttModifyResponse(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeInt(paramBoolean);
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
      
      public void sendUssd(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeString(paramString);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setListener(IImsCallSessionListener paramIImsCallSessionListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          if (paramIImsCallSessionListener != null) {
            paramIImsCallSessionListener = paramIImsCallSessionListener.asBinder();
          } else {
            paramIImsCallSessionListener = null;
          }
          localParcel1.writeStrongBinder(paramIImsCallSessionListener);
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
      
      public void setMute(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
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
      
      public void start(String paramString, ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeString(paramString);
          if (paramImsCallProfile != null)
          {
            localParcel1.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startConference(String[] paramArrayOfString, ImsCallProfile paramImsCallProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeStringArray(paramArrayOfString);
          if (paramImsCallProfile != null)
          {
            localParcel1.writeInt(1);
            paramImsCallProfile.writeToParcel(localParcel1, 0);
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
      
      public void startDtmf(char paramChar)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeInt(paramChar);
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
      
      public void stopDtmf()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
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
      
      public void terminate(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void update(int paramInt, ImsStreamMediaProfile paramImsStreamMediaProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.ims.internal.IImsCallSession");
          localParcel1.writeInt(paramInt);
          if (paramImsStreamMediaProfile != null)
          {
            localParcel1.writeInt(1);
            paramImsStreamMediaProfile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(20, localParcel1, localParcel2, 0);
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
