package android.service.voice;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.app.IVoiceInteractionSessionShowCallback.Stub;

public abstract interface IVoiceInteractionSession
  extends IInterface
{
  public abstract void closeSystemDialogs()
    throws RemoteException;
  
  public abstract void destroy()
    throws RemoteException;
  
  public abstract void handleAssist(Bundle paramBundle, AssistStructure paramAssistStructure, AssistContent paramAssistContent, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void handleScreenshot(Bitmap paramBitmap)
    throws RemoteException;
  
  public abstract void hide()
    throws RemoteException;
  
  public abstract void onLockscreenShown()
    throws RemoteException;
  
  public abstract void show(Bundle paramBundle, int paramInt, IVoiceInteractionSessionShowCallback paramIVoiceInteractionSessionShowCallback)
    throws RemoteException;
  
  public abstract void taskFinished(Intent paramIntent, int paramInt)
    throws RemoteException;
  
  public abstract void taskStarted(Intent paramIntent, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IVoiceInteractionSession
  {
    private static final String DESCRIPTOR = "android.service.voice.IVoiceInteractionSession";
    static final int TRANSACTION_closeSystemDialogs = 7;
    static final int TRANSACTION_destroy = 9;
    static final int TRANSACTION_handleAssist = 3;
    static final int TRANSACTION_handleScreenshot = 4;
    static final int TRANSACTION_hide = 2;
    static final int TRANSACTION_onLockscreenShown = 8;
    static final int TRANSACTION_show = 1;
    static final int TRANSACTION_taskFinished = 6;
    static final int TRANSACTION_taskStarted = 5;
    
    public Stub()
    {
      attachInterface(this, "android.service.voice.IVoiceInteractionSession");
    }
    
    public static IVoiceInteractionSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.voice.IVoiceInteractionSession");
      if ((localIInterface != null) && ((localIInterface instanceof IVoiceInteractionSession))) {
        return (IVoiceInteractionSession)localIInterface;
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
        AssistContent localAssistContent = null;
        Object localObject3 = null;
        AssistStructure localAssistStructure = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 9: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSession");
          destroy();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSession");
          onLockscreenShown();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSession");
          closeSystemDialogs();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localAssistStructure;
          }
          taskFinished(paramParcel2, paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          taskStarted(paramParcel2, paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          handleScreenshot(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localAssistStructure = (AssistStructure)AssistStructure.CREATOR.createFromParcel(paramParcel1);
          } else {
            localAssistStructure = null;
          }
          if (paramParcel1.readInt() != 0) {
            localAssistContent = (AssistContent)AssistContent.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          handleAssist(paramParcel2, localAssistStructure, localAssistContent, paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSession");
          hide();
          return true;
        }
        paramParcel1.enforceInterface("android.service.voice.IVoiceInteractionSession");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = localObject3;
        }
        show(paramParcel2, paramParcel1.readInt(), IVoiceInteractionSessionShowCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.service.voice.IVoiceInteractionSession");
      return true;
    }
    
    private static class Proxy
      implements IVoiceInteractionSession
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
      
      public void closeSystemDialogs()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSession");
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void destroy()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSession");
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.voice.IVoiceInteractionSession";
      }
      
      public void handleAssist(Bundle paramBundle, AssistStructure paramAssistStructure, AssistContent paramAssistContent, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSession");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramAssistStructure != null)
          {
            localParcel.writeInt(1);
            paramAssistStructure.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramAssistContent != null)
          {
            localParcel.writeInt(1);
            paramAssistContent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void handleScreenshot(Bitmap paramBitmap)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSession");
          if (paramBitmap != null)
          {
            localParcel.writeInt(1);
            paramBitmap.writeToParcel(localParcel, 0);
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
      
      public void hide()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSession");
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLockscreenShown()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSession");
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void show(Bundle paramBundle, int paramInt, IVoiceInteractionSessionShowCallback paramIVoiceInteractionSessionShowCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSession");
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
          if (paramIVoiceInteractionSessionShowCallback != null) {
            paramBundle = paramIVoiceInteractionSessionShowCallback.asBinder();
          } else {
            paramBundle = null;
          }
          localParcel.writeStrongBinder(paramBundle);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void taskFinished(Intent paramIntent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSession");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void taskStarted(Intent paramIntent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.voice.IVoiceInteractionSession");
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
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
