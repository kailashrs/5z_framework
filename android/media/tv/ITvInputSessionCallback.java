package android.media.tv;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface ITvInputSessionCallback
  extends IInterface
{
  public abstract void onChannelRetuned(Uri paramUri)
    throws RemoteException;
  
  public abstract void onContentAllowed()
    throws RemoteException;
  
  public abstract void onContentBlocked(String paramString)
    throws RemoteException;
  
  public abstract void onError(int paramInt)
    throws RemoteException;
  
  public abstract void onLayoutSurface(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void onRecordingStopped(Uri paramUri)
    throws RemoteException;
  
  public abstract void onSessionCreated(ITvInputSession paramITvInputSession, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void onSessionEvent(String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onTimeShiftCurrentPositionChanged(long paramLong)
    throws RemoteException;
  
  public abstract void onTimeShiftStartPositionChanged(long paramLong)
    throws RemoteException;
  
  public abstract void onTimeShiftStatusChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onTrackSelected(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void onTracksChanged(List<TvTrackInfo> paramList)
    throws RemoteException;
  
  public abstract void onTuned(Uri paramUri)
    throws RemoteException;
  
  public abstract void onVideoAvailable()
    throws RemoteException;
  
  public abstract void onVideoUnavailable(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvInputSessionCallback
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvInputSessionCallback";
    static final int TRANSACTION_onChannelRetuned = 3;
    static final int TRANSACTION_onContentAllowed = 8;
    static final int TRANSACTION_onContentBlocked = 9;
    static final int TRANSACTION_onError = 16;
    static final int TRANSACTION_onLayoutSurface = 10;
    static final int TRANSACTION_onRecordingStopped = 15;
    static final int TRANSACTION_onSessionCreated = 1;
    static final int TRANSACTION_onSessionEvent = 2;
    static final int TRANSACTION_onTimeShiftCurrentPositionChanged = 13;
    static final int TRANSACTION_onTimeShiftStartPositionChanged = 12;
    static final int TRANSACTION_onTimeShiftStatusChanged = 11;
    static final int TRANSACTION_onTrackSelected = 5;
    static final int TRANSACTION_onTracksChanged = 4;
    static final int TRANSACTION_onTuned = 14;
    static final int TRANSACTION_onVideoAvailable = 6;
    static final int TRANSACTION_onVideoUnavailable = 7;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvInputSessionCallback");
    }
    
    public static ITvInputSessionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvInputSessionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof ITvInputSessionCallback))) {
        return (ITvInputSessionCallback)localIInterface;
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
        case 16: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onError(paramParcel1.readInt());
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          onRecordingStopped(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onTuned(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onTimeShiftCurrentPositionChanged(paramParcel1.readLong());
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onTimeShiftStartPositionChanged(paramParcel1.readLong());
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onTimeShiftStatusChanged(paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onLayoutSurface(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onContentBlocked(paramParcel1.readString());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onContentAllowed();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onVideoUnavailable(paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onVideoAvailable();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onTrackSelected(paramParcel1.readInt(), paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          onTracksChanged(paramParcel1.createTypedArrayList(TvTrackInfo.CREATOR));
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onChannelRetuned(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onSessionEvent(paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvInputSessionCallback");
        onSessionCreated(ITvInputSession.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readStrongBinder());
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvInputSessionCallback");
      return true;
    }
    
    private static class Proxy
      implements ITvInputSessionCallback
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
        return "android.media.tv.ITvInputSessionCallback";
      }
      
      public void onChannelRetuned(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
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
      
      public void onContentAllowed()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onContentBlocked(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          localParcel.writeString(paramString);
          mRemote.transact(9, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLayoutSurface(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRecordingStopped(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionCreated(ITvInputSession paramITvInputSession, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          if (paramITvInputSession != null) {
            paramITvInputSession = paramITvInputSession.asBinder();
          } else {
            paramITvInputSession = null;
          }
          localParcel.writeStrongBinder(paramITvInputSession);
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionEvent(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
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
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTimeShiftCurrentPositionChanged(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          localParcel.writeLong(paramLong);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTimeShiftStartPositionChanged(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          localParcel.writeLong(paramLong);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTimeShiftStatusChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTrackSelected(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeString(paramString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTracksChanged(List<TvTrackInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          localParcel.writeTypedList(paramList);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTuned(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVideoAvailable()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVideoUnavailable(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSessionCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(7, localParcel, null, 1);
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
