package android.speech.tts;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public abstract interface ITextToSpeechService
  extends IInterface
{
  public abstract String[] getClientDefaultLanguage()
    throws RemoteException;
  
  public abstract String getDefaultVoiceNameFor(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract String[] getFeaturesForLanguage(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract String[] getLanguage()
    throws RemoteException;
  
  public abstract List<Voice> getVoices()
    throws RemoteException;
  
  public abstract int isLanguageAvailable(String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract boolean isSpeaking()
    throws RemoteException;
  
  public abstract int loadLanguage(IBinder paramIBinder, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract int loadVoice(IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract int playAudio(IBinder paramIBinder, Uri paramUri, int paramInt, Bundle paramBundle, String paramString)
    throws RemoteException;
  
  public abstract int playSilence(IBinder paramIBinder, long paramLong, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setCallback(IBinder paramIBinder, ITextToSpeechCallback paramITextToSpeechCallback)
    throws RemoteException;
  
  public abstract int speak(IBinder paramIBinder, CharSequence paramCharSequence, int paramInt, Bundle paramBundle, String paramString)
    throws RemoteException;
  
  public abstract int stop(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int synthesizeToFileDescriptor(IBinder paramIBinder, CharSequence paramCharSequence, ParcelFileDescriptor paramParcelFileDescriptor, Bundle paramBundle, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITextToSpeechService
  {
    private static final String DESCRIPTOR = "android.speech.tts.ITextToSpeechService";
    static final int TRANSACTION_getClientDefaultLanguage = 8;
    static final int TRANSACTION_getDefaultVoiceNameFor = 15;
    static final int TRANSACTION_getFeaturesForLanguage = 10;
    static final int TRANSACTION_getLanguage = 7;
    static final int TRANSACTION_getVoices = 13;
    static final int TRANSACTION_isLanguageAvailable = 9;
    static final int TRANSACTION_isSpeaking = 5;
    static final int TRANSACTION_loadLanguage = 11;
    static final int TRANSACTION_loadVoice = 14;
    static final int TRANSACTION_playAudio = 3;
    static final int TRANSACTION_playSilence = 4;
    static final int TRANSACTION_setCallback = 12;
    static final int TRANSACTION_speak = 1;
    static final int TRANSACTION_stop = 6;
    static final int TRANSACTION_synthesizeToFileDescriptor = 2;
    
    public Stub()
    {
      attachInterface(this, "android.speech.tts.ITextToSpeechService");
    }
    
    public static ITextToSpeechService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.speech.tts.ITextToSpeechService");
      if ((localIInterface != null) && ((localIInterface instanceof ITextToSpeechService))) {
        return (ITextToSpeechService)localIInterface;
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
        IBinder localIBinder = null;
        Object localObject2 = null;
        Object localObject3;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 15: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramParcel1 = getDefaultVoiceNameFor(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramInt1 = loadVoice(paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramParcel1 = getVoices();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          setCallback(paramParcel1.readStrongBinder(), ITextToSpeechCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramInt1 = loadLanguage(paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramParcel1 = getFeaturesForLanguage(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramInt1 = isLanguageAvailable(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramParcel1 = getClientDefaultLanguage();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramParcel1 = getLanguage();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramInt1 = stop(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramInt1 = isSpeaking();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          paramInt1 = playSilence(paramParcel1.readStrongBinder(), paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          localObject1 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramInt1 = playAudio((IBinder)localObject1, (Uri)localObject3, paramInt1, (Bundle)localObject2, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
          localIBinder = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramInt1 = synthesizeToFileDescriptor(localIBinder, (CharSequence)localObject3, (ParcelFileDescriptor)localObject2, (Bundle)localObject1, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.speech.tts.ITextToSpeechService");
        localObject1 = paramParcel1.readStrongBinder();
        if (paramParcel1.readInt() != 0) {
          localObject3 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject3 = null;
        }
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {}
        for (localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; localObject2 = localIBinder) {
          break;
        }
        paramInt1 = speak((IBinder)localObject1, (CharSequence)localObject3, paramInt1, (Bundle)localObject2, paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.speech.tts.ITextToSpeechService");
      return true;
    }
    
    private static class Proxy
      implements ITextToSpeechService
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
      
      public String[] getClientDefaultLanguage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          mRemote.transact(8, localParcel1, localParcel2, 0);
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
      
      public String getDefaultVoiceNameFor(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
      
      public String[] getFeaturesForLanguage(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.createStringArray();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.speech.tts.ITextToSpeechService";
      }
      
      public String[] getLanguage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
      
      public List<Voice> getVoices()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(Voice.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int isLanguageAvailable(String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public boolean isSpeaking()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(5, localParcel1, localParcel2, 0);
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
      
      public int loadLanguage(IBinder paramIBinder, String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(11, localParcel1, localParcel2, 0);
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
      
      public int loadVoice(IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public int playAudio(IBinder paramIBinder, Uri paramUri, int paramInt, Bundle paramBundle, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public int playSilence(IBinder paramIBinder, long paramLong, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public void setCallback(IBinder paramIBinder, ITextToSpeechCallback paramITextToSpeechCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramITextToSpeechCallback != null) {
            paramIBinder = paramITextToSpeechCallback.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public int speak(IBinder paramIBinder, CharSequence paramCharSequence, int paramInt, Bundle paramBundle, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public int stop(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
      
      public int synthesizeToFileDescriptor(IBinder paramIBinder, CharSequence paramCharSequence, ParcelFileDescriptor paramParcelFileDescriptor, Bundle paramBundle, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.speech.tts.ITextToSpeechService");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
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
    }
  }
}
