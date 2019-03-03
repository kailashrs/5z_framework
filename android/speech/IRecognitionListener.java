package android.speech;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IRecognitionListener
  extends IInterface
{
  public abstract void onBeginningOfSpeech()
    throws RemoteException;
  
  public abstract void onBufferReceived(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onEndOfSpeech()
    throws RemoteException;
  
  public abstract void onError(int paramInt)
    throws RemoteException;
  
  public abstract void onEvent(int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onPartialResults(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onReadyForSpeech(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onResults(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onRmsChanged(float paramFloat)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRecognitionListener
  {
    private static final String DESCRIPTOR = "android.speech.IRecognitionListener";
    static final int TRANSACTION_onBeginningOfSpeech = 2;
    static final int TRANSACTION_onBufferReceived = 4;
    static final int TRANSACTION_onEndOfSpeech = 5;
    static final int TRANSACTION_onError = 6;
    static final int TRANSACTION_onEvent = 9;
    static final int TRANSACTION_onPartialResults = 8;
    static final int TRANSACTION_onReadyForSpeech = 1;
    static final int TRANSACTION_onResults = 7;
    static final int TRANSACTION_onRmsChanged = 3;
    
    public Stub()
    {
      attachInterface(this, "android.speech.IRecognitionListener");
    }
    
    public static IRecognitionListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.speech.IRecognitionListener");
      if ((localIInterface != null) && ((localIInterface instanceof IRecognitionListener))) {
        return (IRecognitionListener)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 9: 
          paramParcel1.enforceInterface("android.speech.IRecognitionListener");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          onEvent(paramInt1, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.speech.IRecognitionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onPartialResults(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.speech.IRecognitionListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onResults(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.speech.IRecognitionListener");
          onError(paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.speech.IRecognitionListener");
          onEndOfSpeech();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.speech.IRecognitionListener");
          onBufferReceived(paramParcel1.createByteArray());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.speech.IRecognitionListener");
          onRmsChanged(paramParcel1.readFloat());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.speech.IRecognitionListener");
          onBeginningOfSpeech();
          return true;
        }
        paramParcel1.enforceInterface("android.speech.IRecognitionListener");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject3;
        }
        onReadyForSpeech(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.speech.IRecognitionListener");
      return true;
    }
    
    private static class Proxy
      implements IRecognitionListener
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
        return "android.speech.IRecognitionListener";
      }
      
      public void onBeginningOfSpeech()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionListener");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onBufferReceived(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionListener");
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEndOfSpeech()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionListener");
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onError(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEvent(int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionListener");
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
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPartialResults(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionListener");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public void onReadyForSpeech(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionListener");
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
      
      public void onResults(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionListener");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRmsChanged(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.speech.IRecognitionListener");
          localParcel.writeFloat(paramFloat);
          mRemote.transact(3, localParcel, null, 1);
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
