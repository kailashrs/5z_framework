package android.speech.tts;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ITextToSpeechCallback
  extends IInterface
{
  public abstract void onAudioAvailable(String paramString, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onBeginSynthesis(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onError(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onRangeStart(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void onStart(String paramString)
    throws RemoteException;
  
  public abstract void onStop(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onSuccess(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITextToSpeechCallback
  {
    private static final String DESCRIPTOR = "android.speech.tts.ITextToSpeechCallback";
    static final int TRANSACTION_onAudioAvailable = 6;
    static final int TRANSACTION_onBeginSynthesis = 5;
    static final int TRANSACTION_onError = 4;
    static final int TRANSACTION_onRangeStart = 7;
    static final int TRANSACTION_onStart = 1;
    static final int TRANSACTION_onStop = 3;
    static final int TRANSACTION_onSuccess = 2;
    
    public Stub()
    {
      attachInterface(this, "android.speech.tts.ITextToSpeechCallback");
    }
    
    public static ITextToSpeechCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.speech.tts.ITextToSpeechCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITextToSpeechCallback))) {
        return (ITextToSpeechCallback)localIInterface;
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
        case 7: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechCallback");
          onRangeStart(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechCallback");
          onAudioAvailable(paramParcel1.readString(), paramParcel1.createByteArray());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechCallback");
          onBeginSynthesis(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechCallback");
          onError(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechCallback");
          paramParcel2 = paramParcel1.readString();
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          onStop(paramParcel2, bool);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechCallback");
          onSuccess(paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechCallback");
        onStart(paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("android.speech.tts.ITextToSpeechCallback");
      return true;
    }
    
    private static class Proxy
      implements ITextToSpeechCallback
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
      
      public String getInterfaceDescriptor()
      {
        return "android.speech.tts.ITextToSpeechCallback";
      }
      
      public void onAudioAvailable(String paramString, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.tts.ITextToSpeechCallback");
          localParcel.writeString(paramString);
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onBeginSynthesis(String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.tts.ITextToSpeechCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onError(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.tts.ITextToSpeechCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRangeStart(String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.tts.ITextToSpeechCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStart(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.tts.ITextToSpeechCallback");
          localParcel.writeString(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStop(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.tts.ITextToSpeechCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSuccess(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.tts.ITextToSpeechCallback");
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
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
