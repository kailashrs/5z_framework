package android.media.tv;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.InputChannel;
import java.util.List;

public abstract interface ITvInputClient
  extends IInterface
{
  public abstract void onChannelRetuned(Uri paramUri, int paramInt)
    throws RemoteException;
  
  public abstract void onContentAllowed(int paramInt)
    throws RemoteException;
  
  public abstract void onContentBlocked(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onError(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onLayoutSurface(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    throws RemoteException;
  
  public abstract void onRecordingStopped(Uri paramUri, int paramInt)
    throws RemoteException;
  
  public abstract void onSessionCreated(String paramString, IBinder paramIBinder, InputChannel paramInputChannel, int paramInt)
    throws RemoteException;
  
  public abstract void onSessionEvent(String paramString, Bundle paramBundle, int paramInt)
    throws RemoteException;
  
  public abstract void onSessionReleased(int paramInt)
    throws RemoteException;
  
  public abstract void onTimeShiftCurrentPositionChanged(long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void onTimeShiftStartPositionChanged(long paramLong, int paramInt)
    throws RemoteException;
  
  public abstract void onTimeShiftStatusChanged(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void onTrackSelected(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void onTracksChanged(List<TvTrackInfo> paramList, int paramInt)
    throws RemoteException;
  
  public abstract void onTuned(int paramInt, Uri paramUri)
    throws RemoteException;
  
  public abstract void onVideoAvailable(int paramInt)
    throws RemoteException;
  
  public abstract void onVideoUnavailable(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvInputClient
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvInputClient";
    static final int TRANSACTION_onChannelRetuned = 4;
    static final int TRANSACTION_onContentAllowed = 9;
    static final int TRANSACTION_onContentBlocked = 10;
    static final int TRANSACTION_onError = 17;
    static final int TRANSACTION_onLayoutSurface = 11;
    static final int TRANSACTION_onRecordingStopped = 16;
    static final int TRANSACTION_onSessionCreated = 1;
    static final int TRANSACTION_onSessionEvent = 3;
    static final int TRANSACTION_onSessionReleased = 2;
    static final int TRANSACTION_onTimeShiftCurrentPositionChanged = 14;
    static final int TRANSACTION_onTimeShiftStartPositionChanged = 13;
    static final int TRANSACTION_onTimeShiftStatusChanged = 12;
    static final int TRANSACTION_onTrackSelected = 6;
    static final int TRANSACTION_onTracksChanged = 5;
    static final int TRANSACTION_onTuned = 15;
    static final int TRANSACTION_onVideoAvailable = 7;
    static final int TRANSACTION_onVideoUnavailable = 8;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvInputClient");
    }
    
    public static ITvInputClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvInputClient");
      if ((localIInterface != null) && ((localIInterface instanceof ITvInputClient))) {
        return (ITvInputClient)localIInterface;
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
        String str1 = null;
        String str2 = null;
        IBinder localIBinder = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 17: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onError(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localIBinder;
          }
          onRecordingStopped(paramParcel2, paramParcel1.readInt());
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onTuned(paramInt1, paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onTimeShiftCurrentPositionChanged(paramParcel1.readLong(), paramParcel1.readInt());
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onTimeShiftStartPositionChanged(paramParcel1.readLong(), paramParcel1.readInt());
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onTimeShiftStatusChanged(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onLayoutSurface(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onContentBlocked(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onContentAllowed(paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onVideoUnavailable(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onVideoAvailable(paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onTrackSelected(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onTracksChanged(paramParcel1.createTypedArrayList(TvTrackInfo.CREATOR), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          onChannelRetuned(paramParcel2, paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = str1;
          }
          onSessionEvent(str2, paramParcel2, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
          onSessionReleased(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvInputClient");
        str1 = paramParcel1.readString();
        localIBinder = paramParcel1.readStrongBinder();
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (InputChannel)InputChannel.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = str2;
        }
        onSessionCreated(str1, localIBinder, paramParcel2, paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvInputClient");
      return true;
    }
    
    private static class Proxy
      implements ITvInputClient
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
        return "android.media.tv.ITvInputClient";
      }
      
      public void onChannelRetuned(Uri paramUri, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onContentAllowed(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onContentBlocked(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onError(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLayoutSurface(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          localParcel.writeInt(paramInt5);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRecordingStopped(Uri paramUri, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionCreated(String paramString, IBinder paramIBinder, InputChannel paramInputChannel, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeString(paramString);
          localParcel.writeStrongBinder(paramIBinder);
          if (paramInputChannel != null)
          {
            localParcel.writeInt(1);
            paramInputChannel.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionEvent(String paramString, Bundle paramBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
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
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionReleased(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTimeShiftCurrentPositionChanged(long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTimeShiftStartPositionChanged(long paramLong, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeLong(paramLong);
          localParcel.writeInt(paramInt);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTimeShiftStatusChanged(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTrackSelected(int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeInt(paramInt1);
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt2);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTracksChanged(List<TvTrackInfo> paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeTypedList(paramList);
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onTuned(int paramInt, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeInt(paramInt);
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
      
      public void onVideoAvailable(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onVideoUnavailable(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputClient");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(8, localParcel, null, 1);
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
