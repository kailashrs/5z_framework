package com.android.internal.telecom;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.VideoProfile;
import android.telecom.VideoProfile.CameraCapabilities;

public abstract interface IVideoCallback
  extends IInterface
{
  public abstract void changeCallDataUsage(long paramLong)
    throws RemoteException;
  
  public abstract void changeCameraCapabilities(VideoProfile.CameraCapabilities paramCameraCapabilities)
    throws RemoteException;
  
  public abstract void changePeerDimensions(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void changeVideoQuality(int paramInt)
    throws RemoteException;
  
  public abstract void handleCallSessionEvent(int paramInt)
    throws RemoteException;
  
  public abstract void receiveSessionModifyRequest(VideoProfile paramVideoProfile)
    throws RemoteException;
  
  public abstract void receiveSessionModifyResponse(int paramInt, VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVideoCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.telecom.IVideoCallback";
    static final int TRANSACTION_changeCallDataUsage = 5;
    static final int TRANSACTION_changeCameraCapabilities = 6;
    static final int TRANSACTION_changePeerDimensions = 4;
    static final int TRANSACTION_changeVideoQuality = 7;
    static final int TRANSACTION_handleCallSessionEvent = 3;
    static final int TRANSACTION_receiveSessionModifyRequest = 1;
    static final int TRANSACTION_receiveSessionModifyResponse = 2;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telecom.IVideoCallback");
    }
    
    public static IVideoCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telecom.IVideoCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IVideoCallback))) {
        return (IVideoCallback)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoCallback");
          changeVideoQuality(paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VideoProfile.CameraCapabilities)VideoProfile.CameraCapabilities.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          changeCameraCapabilities(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoCallback");
          changeCallDataUsage(paramParcel1.readLong());
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoCallback");
          changePeerDimensions(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoCallback");
          handleCallSessionEvent(paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telecom.IVideoCallback");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          receiveSessionModifyResponse(paramInt1, paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telecom.IVideoCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (VideoProfile)VideoProfile.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject2;
        }
        receiveSessionModifyRequest(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.telecom.IVideoCallback");
      return true;
    }
    
    private static class Proxy
      implements IVideoCallback
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
      
      public void changeCallDataUsage(long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoCallback");
          localParcel.writeLong(paramLong);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void changeCameraCapabilities(VideoProfile.CameraCapabilities paramCameraCapabilities)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoCallback");
          if (paramCameraCapabilities != null)
          {
            localParcel.writeInt(1);
            paramCameraCapabilities.writeToParcel(localParcel, 0);
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
      
      public void changePeerDimensions(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoCallback");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void changeVideoQuality(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telecom.IVideoCallback";
      }
      
      public void handleCallSessionEvent(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void receiveSessionModifyRequest(VideoProfile paramVideoProfile)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoCallback");
          if (paramVideoProfile != null)
          {
            localParcel.writeInt(1);
            paramVideoProfile.writeToParcel(localParcel, 0);
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
      
      public void receiveSessionModifyResponse(int paramInt, VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telecom.IVideoCallback");
          localParcel.writeInt(paramInt);
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
