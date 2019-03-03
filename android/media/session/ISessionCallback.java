package android.media.session;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;

public abstract interface ISessionCallback
  extends IInterface
{
  public abstract void onAdjustVolume(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, int paramInt3)
    throws RemoteException;
  
  public abstract void onCommand(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract void onCustomAction(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onFastForward(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void onMediaButton(String paramString, int paramInt1, int paramInt2, Intent paramIntent, int paramInt3, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract void onMediaButtonFromController(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Intent paramIntent)
    throws RemoteException;
  
  public abstract void onNext(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void onPause(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void onPlay(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void onPlayFromMediaId(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onPlayFromSearch(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onPlayFromUri(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onPrepare(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void onPrepareFromMediaId(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onPrepareFromSearch(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onPrepareFromUri(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onPrevious(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void onRate(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Rating paramRating)
    throws RemoteException;
  
  public abstract void onRewind(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public abstract void onSeekTo(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
    throws RemoteException;
  
  public abstract void onSetVolumeTo(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, int paramInt3)
    throws RemoteException;
  
  public abstract void onSkipToTrack(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
    throws RemoteException;
  
  public abstract void onStop(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISessionCallback
  {
    private static final String DESCRIPTOR = "android.media.session.ISessionCallback";
    static final int TRANSACTION_onAdjustVolume = 22;
    static final int TRANSACTION_onCommand = 1;
    static final int TRANSACTION_onCustomAction = 21;
    static final int TRANSACTION_onFastForward = 17;
    static final int TRANSACTION_onMediaButton = 2;
    static final int TRANSACTION_onMediaButtonFromController = 3;
    static final int TRANSACTION_onNext = 15;
    static final int TRANSACTION_onPause = 13;
    static final int TRANSACTION_onPlay = 8;
    static final int TRANSACTION_onPlayFromMediaId = 9;
    static final int TRANSACTION_onPlayFromSearch = 10;
    static final int TRANSACTION_onPlayFromUri = 11;
    static final int TRANSACTION_onPrepare = 4;
    static final int TRANSACTION_onPrepareFromMediaId = 5;
    static final int TRANSACTION_onPrepareFromSearch = 6;
    static final int TRANSACTION_onPrepareFromUri = 7;
    static final int TRANSACTION_onPrevious = 16;
    static final int TRANSACTION_onRate = 20;
    static final int TRANSACTION_onRewind = 18;
    static final int TRANSACTION_onSeekTo = 19;
    static final int TRANSACTION_onSetVolumeTo = 23;
    static final int TRANSACTION_onSkipToTrack = 12;
    static final int TRANSACTION_onStop = 14;
    
    public Stub()
    {
      attachInterface(this, "android.media.session.ISessionCallback");
    }
    
    public static ISessionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.session.ISessionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ISessionCallback))) {
        return (ISessionCallback)localIInterface;
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
        ISessionControllerCallback localISessionControllerCallback = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        String str = null;
        Object localObject8 = null;
        Object localObject9 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 23: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onSetVolumeTo(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onAdjustVolume(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          localObject8 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          paramParcel2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject9) {
            break;
          }
          onCustomAction((String)localObject8, paramInt2, paramInt1, paramParcel2, (String)localObject2, paramParcel1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          localObject8 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          paramParcel2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Rating)Rating.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject1) {
            break;
          }
          onRate((String)localObject8, paramInt1, paramInt2, paramParcel2, paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onSeekTo(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onRewind(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onFastForward(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onPrevious(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onNext(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onStop(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onPause(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onSkipToTrack(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          localObject8 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          localISessionControllerCallback = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject2) {
            break;
          }
          onPlayFromUri((String)localObject8, paramInt2, paramInt1, localISessionControllerCallback, paramParcel2, paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          localObject2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject8 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject3) {
            break;
          }
          onPlayFromSearch(paramParcel2, paramInt1, paramInt2, (ISessionControllerCallback)localObject2, (String)localObject8, paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          localObject2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject8 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject4) {
            break;
          }
          onPlayFromMediaId(paramParcel2, paramInt1, paramInt2, (ISessionControllerCallback)localObject2, (String)localObject8, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onPlay(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          localObject2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          localObject8 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localISessionControllerCallback) {
            break;
          }
          onPrepareFromUri((String)localObject2, paramInt1, paramInt2, (ISessionControllerCallback)localObject8, paramParcel2, paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          localObject2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject8 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject5) {
            break;
          }
          onPrepareFromSearch(paramParcel2, paramInt1, paramInt2, (ISessionControllerCallback)localObject2, (String)localObject8, paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          paramParcel2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          localObject2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject8 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject6) {
            break;
          }
          onPrepareFromMediaId(paramParcel2, paramInt1, paramInt2, (ISessionControllerCallback)localObject2, (String)localObject8, paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          onPrepare(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          localObject8 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          paramParcel2 = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject7) {
            break;
          }
          onMediaButtonFromController((String)localObject8, paramInt1, paramInt2, paramParcel2, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.session.ISessionCallback");
          localObject8 = paramParcel1.readString();
          int i = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = str) {
            break;
          }
          onMediaButton((String)localObject8, i, paramInt1, paramParcel2, paramInt2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.session.ISessionCallback");
        str = paramParcel1.readString();
        paramInt2 = paramParcel1.readInt();
        paramInt1 = paramParcel1.readInt();
        localISessionControllerCallback = ISessionControllerCallback.Stub.asInterface(paramParcel1.readStrongBinder());
        localObject2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {}
        for (paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject8) {
          break;
        }
        onCommand(str, paramInt2, paramInt1, localISessionControllerCallback, (String)localObject2, paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.media.session.ISessionCallback");
      return true;
    }
    
    private static class Proxy
      implements ISessionCallback
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
        return "android.media.session.ISessionCallback";
      }
      
      public void onAdjustVolume(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          localParcel.writeInt(paramInt3);
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCommand(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          localParcel.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
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
      
      public void onCustomAction(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          localParcel.writeString(paramString2);
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
      
      public void onFastForward(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onMediaButton(String paramString, int paramInt1, int paramInt2, Intent paramIntent, int paramInt3, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt3);
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
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
      
      public void onMediaButtonFromController(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Intent paramIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNext(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPause(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPlay(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPlayFromMediaId(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          localParcel.writeString(paramString2);
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
      
      public void onPlayFromSearch(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          localParcel.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPlayFromUri(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPrepare(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPrepareFromMediaId(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          localParcel.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPrepareFromSearch(String paramString1, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString1);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString1 = paramISessionControllerCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          localParcel.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPrepareFromUri(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Uri paramUri, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
      
      public void onPrevious(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRate(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, Rating paramRating)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          if (paramRating != null)
          {
            localParcel.writeInt(1);
            paramRating.writeToParcel(localParcel, 0);
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
      
      public void onRewind(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSeekTo(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          localParcel.writeLong(paramLong);
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSetVolumeTo(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          localParcel.writeInt(paramInt3);
          mRemote.transact(23, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSkipToTrack(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          localParcel.writeLong(paramLong);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStop(String paramString, int paramInt1, int paramInt2, ISessionControllerCallback paramISessionControllerCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.session.ISessionCallback");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramISessionControllerCallback != null) {
            paramString = paramISessionControllerCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(14, localParcel, null, 1);
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
