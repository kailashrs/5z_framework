package android.media.session;

import android.app.PendingIntent;
import android.content.pm.ParceledListSlice;
import android.media.MediaMetadata;
import android.media.Rating;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.KeyEvent;

public abstract interface ISessionController
  extends IInterface
{
  public abstract void adjustVolume(String paramString, ISessionControllerCallback paramISessionControllerCallback, boolean paramBoolean, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void fastForward(String paramString, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract Bundle getExtras()
    throws RemoteException;
  
  public abstract long getFlags()
    throws RemoteException;
  
  public abstract PendingIntent getLaunchPendingIntent()
    throws RemoteException;
  
  public abstract MediaMetadata getMetadata()
    throws RemoteException;
  
  public abstract String getPackageName()
    throws RemoteException;
  
  public abstract PlaybackState getPlaybackState()
    throws RemoteException;
  
  public abstract ParceledListSlice getQueue()
    throws RemoteException;
  
  public abstract CharSequence getQueueTitle()
    throws RemoteException;
  
  public abstract int getRatingType()
    throws RemoteException;
  
  public abstract String getTag()
    throws RemoteException;
  
  public abstract ParcelableVolumeInfo getVolumeAttributes()
    throws RemoteException;
  
  public abstract boolean isTransportControlEnabled()
    throws RemoteException;
  
  public abstract void next(String paramString, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void pause(String paramString, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void play(String paramString, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void playFromMediaId(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void playFromSearch(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void playFromUri(String paramString, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void prepare(String paramString, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void prepareFromMediaId(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void prepareFromSearch(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void prepareFromUri(String paramString, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void previous(String paramString, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void rate(String paramString, ISessionControllerCallback paramISessionControllerCallback, Rating paramRating)
    throws RemoteException;
  
  public abstract void registerCallbackListener(String paramString, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void rewind(String paramString, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void seekTo(String paramString, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
    throws RemoteException;
  
  public abstract void sendCommand(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract void sendCustomAction(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract boolean sendMediaButton(String paramString, ISessionControllerCallback paramISessionControllerCallback, boolean paramBoolean, KeyEvent paramKeyEvent)
    throws RemoteException;
  
  public abstract void setVolumeTo(String paramString, ISessionControllerCallback paramISessionControllerCallback, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void skipToQueueItem(String paramString, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
    throws RemoteException;
  
  public abstract void stop(String paramString, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void unregisterCallbackListener(ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISessionController
  {
    private static final String DESCRIPTOR = "android.media.session.ISessionController";
    static final int TRANSACTION_adjustVolume = 11;
    static final int TRANSACTION_fastForward = 26;
    static final int TRANSACTION_getExtras = 35;
    static final int TRANSACTION_getFlags = 9;
    static final int TRANSACTION_getLaunchPendingIntent = 8;
    static final int TRANSACTION_getMetadata = 31;
    static final int TRANSACTION_getPackageName = 6;
    static final int TRANSACTION_getPlaybackState = 32;
    static final int TRANSACTION_getQueue = 33;
    static final int TRANSACTION_getQueueTitle = 34;
    static final int TRANSACTION_getRatingType = 36;
    static final int TRANSACTION_getTag = 7;
    static final int TRANSACTION_getVolumeAttributes = 10;
    static final int TRANSACTION_isTransportControlEnabled = 5;
    static final int TRANSACTION_next = 24;
    static final int TRANSACTION_pause = 22;
    static final int TRANSACTION_play = 17;
    static final int TRANSACTION_playFromMediaId = 18;
    static final int TRANSACTION_playFromSearch = 19;
    static final int TRANSACTION_playFromUri = 20;
    static final int TRANSACTION_prepare = 13;
    static final int TRANSACTION_prepareFromMediaId = 14;
    static final int TRANSACTION_prepareFromSearch = 15;
    static final int TRANSACTION_prepareFromUri = 16;
    static final int TRANSACTION_previous = 25;
    static final int TRANSACTION_rate = 29;
    static final int TRANSACTION_registerCallbackListener = 3;
    static final int TRANSACTION_rewind = 27;
    static final int TRANSACTION_seekTo = 28;
    static final int TRANSACTION_sendCommand = 1;
    static final int TRANSACTION_sendCustomAction = 30;
    static final int TRANSACTION_sendMediaButton = 2;
    static final int TRANSACTION_setVolumeTo = 12;
    static final int TRANSACTION_skipToQueueItem = 21;
    static final int TRANSACTION_stop = 23;
    static final int TRANSACTION_unregisterCallbackListener = 4;
    
    public Stub()
    {
      attachInterface(this, "android.media.session.ISessionController");
    }
    
    public static ISessionController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.session.ISessionController");
      if ((localIInterface != null) && ((localIInterface instanceof ISessionController))) {
        return (ISessionController)localIInterface;
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
        boolean bool = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 36: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramInt1 = getRatingType();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramParcel1 = getExtras();
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
        case 34: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramParcel1 = getQueueTitle();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramParcel1 = getQueue();
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
        case 32: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramParcel1 = getPlaybackState();
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
        case 31: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramParcel1 = getMetadata();
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
        case 30: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject3 = paramParcel1.readString();
          localObject2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject5 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject9;
          }
          sendCustomAction((String)localObject3, (ISessionControllerCallback)localObject2, (String)localObject5, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject9 = paramParcel1.readString();
          localObject2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rating)Rating.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          rate((String)localObject9, (ISessionControllerCallback)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          seekTo(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          rewind(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          fastForward(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          previous(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          next(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          stop(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          pause(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          skipToQueueItem(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject5 = paramParcel1.readString();
          localObject3 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          playFromUri((String)localObject5, (ISessionControllerCallback)localObject3, (Uri)localObject9, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject2 = paramParcel1.readString();
          localObject5 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject9 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject3;
          }
          playFromSearch((String)localObject2, (ISessionControllerCallback)localObject5, (String)localObject9, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject2 = paramParcel1.readString();
          localObject5 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject9 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          playFromMediaId((String)localObject2, (ISessionControllerCallback)localObject5, (String)localObject9, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          play(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject3 = paramParcel1.readString();
          localObject2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject9 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject9 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject5;
          }
          prepareFromUri((String)localObject3, (ISessionControllerCallback)localObject2, (Uri)localObject9, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject2 = paramParcel1.readString();
          localObject5 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject9 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          prepareFromSearch((String)localObject2, (ISessionControllerCallback)localObject5, (String)localObject9, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject9 = paramParcel1.readString();
          localObject5 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          prepareFromMediaId((String)localObject9, (ISessionControllerCallback)localObject5, (String)localObject2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          prepare(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          setVolumeTo(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject2 = paramParcel1.readString();
          localObject9 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          adjustVolume((String)localObject2, (ISessionControllerCallback)localObject9, bool, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramParcel1 = getVolumeAttributes();
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
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          long l = getFlags();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramParcel1 = getLaunchPendingIntent();
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
        case 7: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramParcel1 = getTag();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramParcel1 = getPackageName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          paramInt1 = isTransportControlEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          unregisterCallbackListener(ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          registerCallbackListener(paramParcel1.readString(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.session.ISessionController");
          localObject2 = paramParcel1.readString();
          localObject9 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          paramInt1 = sendMediaButton((String)localObject2, (ISessionControllerCallback)localObject9, bool, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.session.ISessionController");
        localObject5 = paramParcel1.readString();
        localObject3 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
        localObject2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          localObject9 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject9 = null;
        }
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        sendCommand((String)localObject5, (ISessionControllerCallback)localObject3, (String)localObject2, (Bundle)localObject9, paramParcel1);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.media.session.ISessionController");
      return true;
    }
    
    private static class Proxy
      implements ISessionController
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void adjustVolume(String paramString, ISessionControllerCallback paramISessionControllerCallback, boolean paramBoolean, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void fastForward(String paramString, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public Bundle getExtras()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(35, localParcel1, localParcel2, 0);
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
      
      public long getFlags()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.session.ISessionController";
      }
      
      public PendingIntent getLaunchPendingIntent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PendingIntent localPendingIntent;
          if (localParcel2.readInt() != 0) {
            localPendingIntent = (PendingIntent)PendingIntent.CREATOR.createFromParcel(localParcel2);
          } else {
            localPendingIntent = null;
          }
          return localPendingIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public MediaMetadata getMetadata()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          MediaMetadata localMediaMetadata;
          if (localParcel2.readInt() != 0) {
            localMediaMetadata = (MediaMetadata)MediaMetadata.CREATOR.createFromParcel(localParcel2);
          } else {
            localMediaMetadata = null;
          }
          return localMediaMetadata;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getPackageName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
      
      public PlaybackState getPlaybackState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PlaybackState localPlaybackState;
          if (localParcel2.readInt() != 0) {
            localPlaybackState = (PlaybackState)PlaybackState.CREATOR.createFromParcel(localParcel2);
          } else {
            localPlaybackState = null;
          }
          return localPlaybackState;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getQueue()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getQueueTitle()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          CharSequence localCharSequence;
          if (localParcel2.readInt() != 0) {
            localCharSequence = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            localCharSequence = null;
          }
          return localCharSequence;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getRatingType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(36, localParcel1, localParcel2, 0);
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
      
      public String getTag()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(7, localParcel1, localParcel2, 0);
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
      
      public ParcelableVolumeInfo getVolumeAttributes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParcelableVolumeInfo localParcelableVolumeInfo;
          if (localParcel2.readInt() != 0) {
            localParcelableVolumeInfo = (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localParcelableVolumeInfo = null;
          }
          return localParcelableVolumeInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isTransportControlEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
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
      
      public void next(String paramString, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void pause(String paramString, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void play(String paramString, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void playFromMediaId(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString1);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
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
      
      public void playFromSearch(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString1);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
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
      
      public void playFromUri(String paramString, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void prepare(String paramString, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void prepareFromMediaId(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString1);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
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
      
      public void prepareFromSearch(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString1);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
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
      
      public void prepareFromUri(String paramString, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void previous(String paramString, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void rate(String paramString, ISessionControllerCallback paramISessionControllerCallback, Rating paramRating)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          if (paramRating != null)
          {
            localParcel1.writeInt(1);
            paramRating.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void registerCallbackListener(String paramString, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void rewind(String paramString, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void seekTo(String paramString, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeLong(paramLong);
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
      
      public void sendCommand(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString1);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
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
          if (paramResultReceiver != null)
          {
            localParcel1.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel1, 0);
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
      
      public void sendCustomAction(String paramString1, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString1);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
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
      
      public boolean sendMediaButton(String paramString, ISessionControllerCallback paramISessionControllerCallback, boolean paramBoolean, KeyEvent paramKeyEvent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramBoolean);
          boolean bool = true;
          if (paramKeyEvent != null)
          {
            localParcel1.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
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
      
      public void setVolumeTo(String paramString, ISessionControllerCallback paramISessionControllerCallback, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void skipToQueueItem(String paramString, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeLong(paramLong);
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
      
      public void stop(String paramString, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          localParcel1.writeString(paramString);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void unregisterCallbackListener(ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.session.ISessionController");
          if (paramISessionControllerCallback != null) {
            paramISessionControllerCallback = paramISessionControllerCallback.asBinder();
          } else {
            paramISessionControllerCallback = null;
          }
          localParcel1.writeStrongBinder(paramISessionControllerCallback);
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
    }
  }
}
