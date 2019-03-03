package com.android.internal.telecom;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.VideoProfile;
import android.view.Surface;

public abstract interface IVideoProvider
  extends IInterface
{
  public abstract void addVideoCallback(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void removeVideoCallback(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void requestCallDataUsage()
    throws RemoteException;
  
  public abstract void requestCameraCapabilities()
    throws RemoteException;
  
  public abstract void sendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
    throws RemoteException;
  
  public abstract void sendSessionModifyResponse(VideoProfile paramVideoProfile)
    throws RemoteException;
  
  public abstract void setCamera(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void setDeviceOrientation(int paramInt)
    throws RemoteException;
  
  public abstract void setDisplaySurface(Surface paramSurface)
    throws RemoteException;
  
  public abstract void setPauseImage(Uri paramUri)
    throws RemoteException;
  
  public abstract void setPreviewSurface(Surface paramSurface)
    throws RemoteException;
  
  public abstract void setZoom(float paramFloat)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVideoProvider
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.IVideoProvider";
    static final int TRANSACTION_addVideoCallback = 1;
    static final int TRANSACTION_removeVideoCallback = 2;
    static final int TRANSACTION_requestCallDataUsage = 11;
    static final int TRANSACTION_requestCameraCapabilities = 10;
    static final int TRANSACTION_sendSessionModifyRequest = 8;
    static final int TRANSACTION_sendSessionModifyResponse = 9;
    static final int TRANSACTION_setCamera = 3;
    static final int TRANSACTION_setDeviceOrientation = 6;
    static final int TRANSACTION_setDisplaySurface = 5;
    static final int TRANSACTION_setPauseImage = 12;
    static final int TRANSACTION_setPreviewSurface = 4;
    static final int TRANSACTION_setZoom = 7;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.IVideoProvider");
    }
    
    public static IVideoProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.IVideoProvider");
      if ((localIInterface != null) && ((localIInterface instanceof IVideoProvider))) {
        return (IVideoProvider)localIInterface;
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
        Object localObject5 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          setPauseImage(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          requestCallDataUsage();
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          requestCameraCapabilities();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          sendSessionModifyResponse(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          sendSessionModifyRequest(paramParcel2, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          setZoom(paramParcel1.readFloat());
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          setDeviceOrientation(paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          setDisplaySurface(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          setPreviewSurface(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          setCamera(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
          removeVideoCallback(paramParcel1.readStrongBinder());
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telecom.IVideoProvider");
        addVideoCallback(paramParcel1.readStrongBinder());
        return true;
      }
      paramParcel2.writeString("com.android.internal.telecom.IVideoProvider");
      return true;
    }
    
    private static class Proxy
      implements IVideoProvider
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addVideoCallback(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          localParcel.writeStrongBinder(paramIBinder);
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
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telecom.IVideoProvider";
      }
      
      public void removeVideoCallback(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void requestCallDataUsage()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void requestCameraCapabilities()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          if (paramVideoProfile1 != null)
          {
            localParcel.writeInt(1);
            paramVideoProfile1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramVideoProfile2 != null)
          {
            localParcel.writeInt(1);
            paramVideoProfile2.writeToParcel(localParcel, 0);
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
      
      public void sendSessionModifyResponse(VideoProfile paramVideoProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          if (paramVideoProfile != null)
          {
            localParcel.writeInt(1);
            paramVideoProfile.writeToParcel(localParcel, 0);
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
      
      public void setCamera(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setDeviceOrientation(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setDisplaySurface(Surface paramSurface)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          if (paramSurface != null)
          {
            localParcel.writeInt(1);
            paramSurface.writeToParcel(localParcel, 0);
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
      
      public void setPauseImage(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPreviewSurface(Surface paramSurface)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          if (paramSurface != null)
          {
            localParcel.writeInt(1);
            paramSurface.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setZoom(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoProvider");
          localParcel.writeFloat(paramFloat);
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
