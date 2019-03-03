package android.media.tv;

import android.graphics.Rect;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.Surface;

public abstract interface ITvInputSession
  extends IInterface
{
  public abstract void appPrivateCommand(String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void createOverlayView(IBinder paramIBinder, Rect paramRect)
    throws RemoteException;
  
  public abstract void dispatchSurfaceChanged(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void relayoutOverlayView(Rect paramRect)
    throws RemoteException;
  
  public abstract void release()
    throws RemoteException;
  
  public abstract void removeOverlayView()
    throws RemoteException;
  
  public abstract void selectTrack(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setCaptionEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setMain(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSurface(Surface paramSurface)
    throws RemoteException;
  
  public abstract void setVolume(float paramFloat)
    throws RemoteException;
  
  public abstract void startRecording(Uri paramUri)
    throws RemoteException;
  
  public abstract void stopRecording()
    throws RemoteException;
  
  public abstract void timeShiftEnablePositionTracking(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void timeShiftPause()
    throws RemoteException;
  
  public abstract void timeShiftPlay(Uri paramUri)
    throws RemoteException;
  
  public abstract void timeShiftResume()
    throws RemoteException;
  
  public abstract void timeShiftSeekTo(long paramLong)
    throws RemoteException;
  
  public abstract void timeShiftSetPlaybackParams(PlaybackParams paramPlaybackParams)
    throws RemoteException;
  
  public abstract void tune(Uri paramUri, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void unblockContent(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITvInputSession
  {
    private static final String DESCRIPTOR = "android.media.tv.ITvInputSession";
    static final int TRANSACTION_appPrivateCommand = 9;
    static final int TRANSACTION_createOverlayView = 10;
    static final int TRANSACTION_dispatchSurfaceChanged = 4;
    static final int TRANSACTION_relayoutOverlayView = 11;
    static final int TRANSACTION_release = 1;
    static final int TRANSACTION_removeOverlayView = 12;
    static final int TRANSACTION_selectTrack = 8;
    static final int TRANSACTION_setCaptionEnabled = 7;
    static final int TRANSACTION_setMain = 2;
    static final int TRANSACTION_setSurface = 3;
    static final int TRANSACTION_setVolume = 5;
    static final int TRANSACTION_startRecording = 20;
    static final int TRANSACTION_stopRecording = 21;
    static final int TRANSACTION_timeShiftEnablePositionTracking = 19;
    static final int TRANSACTION_timeShiftPause = 15;
    static final int TRANSACTION_timeShiftPlay = 14;
    static final int TRANSACTION_timeShiftResume = 16;
    static final int TRANSACTION_timeShiftSeekTo = 17;
    static final int TRANSACTION_timeShiftSetPlaybackParams = 18;
    static final int TRANSACTION_tune = 6;
    static final int TRANSACTION_unblockContent = 13;
    
    public Stub()
    {
      attachInterface(this, "android.media.tv.ITvInputSession");
    }
    
    public static ITvInputSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.tv.ITvInputSession");
      if ((localIInterface != null) && ((localIInterface instanceof ITvInputSession))) {
        return (ITvInputSession)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 21: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          stopRecording();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          startRecording(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          timeShiftEnablePositionTracking(bool3);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PlaybackParams)PlaybackParams.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          timeShiftSetPlaybackParams(paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          timeShiftSeekTo(paramParcel1.readLong());
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          timeShiftResume();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          timeShiftPause();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          timeShiftPlay(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          unblockContent(paramParcel1.readString());
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          removeOverlayView();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          relayoutOverlayView(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          paramParcel2 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          createOverlayView(paramParcel2, paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          appPrivateCommand(paramParcel2, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          selectTrack(paramParcel1.readInt(), paramParcel1.readString());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          setCaptionEnabled(bool3);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          tune(paramParcel2, paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          setVolume(paramParcel1.readFloat());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          dispatchSurfaceChanged(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          setSurface(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
          bool3 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          setMain(bool3);
          return true;
        }
        paramParcel1.enforceInterface("android.media.tv.ITvInputSession");
        release();
        return true;
      }
      paramParcel2.writeString("android.media.tv.ITvInputSession");
      return true;
    }
    
    private static class Proxy
      implements ITvInputSession
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void appPrivateCommand(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
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
          mRemote.transact(9, localParcel, null, 1);
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
      
      public void createOverlayView(IBinder paramIBinder, Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramRect != null)
          {
            localParcel.writeInt(1);
            paramRect.writeToParcel(localParcel, 0);
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
      
      public void dispatchSurfaceChanged(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
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
        return "android.media.tv.ITvInputSession";
      }
      
      public void relayoutOverlayView(Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          if (paramRect != null)
          {
            localParcel.writeInt(1);
            paramRect.writeToParcel(localParcel, 0);
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
      
      public void release()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeOverlayView()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void selectTrack(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
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
      
      public void setCaptionEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setMain(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setSurface(Surface paramSurface)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          if (paramSurface != null)
          {
            localParcel.writeInt(1);
            paramSurface.writeToParcel(localParcel, 0);
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
      
      public void setVolume(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          localParcel.writeFloat(paramFloat);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startRecording(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
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
      
      public void stopRecording()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void timeShiftEnablePositionTracking(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void timeShiftPause()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void timeShiftPlay(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
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
      
      public void timeShiftResume()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void timeShiftSeekTo(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          localParcel.writeLong(paramLong);
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void timeShiftSetPlaybackParams(PlaybackParams paramPlaybackParams)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          if (paramPlaybackParams != null)
          {
            localParcel.writeInt(1);
            paramPlaybackParams.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void tune(Uri paramUri, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
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
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unblockContent(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.tv.ITvInputSession");
          localParcel.writeString(paramString);
          mRemote.transact(13, localParcel, null, 1);
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
