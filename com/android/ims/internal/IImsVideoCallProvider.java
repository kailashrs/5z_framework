package com.android.ims.internal;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.VideoProfile;
import android.view.Surface;

public abstract interface IImsVideoCallProvider
  extends IInterface
{
  public abstract void requestCallDataUsage()
    throws RemoteException;
  
  public abstract void requestCameraCapabilities()
    throws RemoteException;
  
  public abstract void sendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
    throws RemoteException;
  
  public abstract void sendSessionModifyResponse(VideoProfile paramVideoProfile)
    throws RemoteException;
  
  public abstract void setCallback(IImsVideoCallCallback paramIImsVideoCallCallback)
    throws RemoteException;
  
  public abstract void setCamera(String paramString, int paramInt)
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
    implements IImsVideoCallProvider
  {
    private static final String DESCRIPTOR = "com.android.ims.internal.IImsVideoCallProvider";
    static final int TRANSACTION_requestCallDataUsage = 10;
    static final int TRANSACTION_requestCameraCapabilities = 9;
    static final int TRANSACTION_sendSessionModifyRequest = 7;
    static final int TRANSACTION_sendSessionModifyResponse = 8;
    static final int TRANSACTION_setCallback = 1;
    static final int TRANSACTION_setCamera = 2;
    static final int TRANSACTION_setDeviceOrientation = 5;
    static final int TRANSACTION_setDisplaySurface = 4;
    static final int TRANSACTION_setPauseImage = 11;
    static final int TRANSACTION_setPreviewSurface = 3;
    static final int TRANSACTION_setZoom = 6;
    
    public Stub()
    {
      attachInterface(this, "com.android.ims.internal.IImsVideoCallProvider");
    }
    
    public static IImsVideoCallProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.ims.internal.IImsVideoCallProvider");
      if ((localIInterface != null) && ((localIInterface instanceof IImsVideoCallProvider))) {
        return (IImsVideoCallProvider)localIInterface;
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
        case 11: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          setPauseImage(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
          requestCallDataUsage();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
          requestCameraCapabilities();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          sendSessionModifyResponse(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
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
        case 6: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
          setZoom(paramParcel1.readFloat());
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
          setDeviceOrientation(paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          setDisplaySurface(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          setPreviewSurface(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
          setCamera(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("com.android.ims.internal.IImsVideoCallProvider");
        setCallback(IImsVideoCallCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("com.android.ims.internal.IImsVideoCallProvider");
      return true;
    }
    
    private static class Proxy
      implements IImsVideoCallProvider
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
        return "com.android.ims.internal.IImsVideoCallProvider";
      }
      
      public void requestCallDataUsage()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
          mRemote.transact(10, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
          mRemote.transact(9, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
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
          mRemote.transact(7, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
          if (paramVideoProfile != null)
          {
            localParcel.writeInt(1);
            paramVideoProfile.writeToParcel(localParcel, 0);
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
      
      public void setCallback(IImsVideoCallCallback paramIImsVideoCallCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
          if (paramIImsVideoCallCallback != null) {
            paramIImsVideoCallCallback = paramIImsVideoCallCallback.asBinder();
          } else {
            paramIImsVideoCallCallback = null;
          }
          localParcel.writeStrongBinder(paramIImsVideoCallCallback);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setCamera(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
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
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
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
      
      public void setPauseImage(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
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
      
      public void setPreviewSurface(Surface paramSurface)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
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
      
      public void setZoom(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.ims.internal.IImsVideoCallProvider");
          localParcel.writeFloat(paramFloat);
          mRemote.transact(6, localParcel, null, 1);
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
