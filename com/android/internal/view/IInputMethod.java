package com.android.internal.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.view.InputChannel;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputMethodSubtype;

public abstract interface IInputMethod
  extends IInterface
{
  public abstract void attachToken(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void bindInput(InputBinding paramInputBinding)
    throws RemoteException;
  
  public abstract void changeInputMethodSubtype(InputMethodSubtype paramInputMethodSubtype)
    throws RemoteException;
  
  public abstract void createSession(InputChannel paramInputChannel, IInputSessionCallback paramIInputSessionCallback)
    throws RemoteException;
  
  public abstract void hideSoftInput(int paramInt, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract void revokeSession(IInputMethodSession paramIInputMethodSession)
    throws RemoteException;
  
  public abstract void setSessionEnabled(IInputMethodSession paramIInputMethodSession, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void showSoftInput(int paramInt, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract void startInput(IBinder paramIBinder, IInputContext paramIInputContext, int paramInt, EditorInfo paramEditorInfo, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void unbindInput()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputMethod
  {
    private static final String DESCRIPTOR = "com.android.internal.view.IInputMethod";
    static final int TRANSACTION_attachToken = 1;
    static final int TRANSACTION_bindInput = 2;
    static final int TRANSACTION_changeInputMethodSubtype = 10;
    static final int TRANSACTION_createSession = 5;
    static final int TRANSACTION_hideSoftInput = 9;
    static final int TRANSACTION_revokeSession = 7;
    static final int TRANSACTION_setSessionEnabled = 6;
    static final int TRANSACTION_showSoftInput = 8;
    static final int TRANSACTION_startInput = 4;
    static final int TRANSACTION_unbindInput = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.view.IInputMethod");
    }
    
    public static IInputMethod asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.view.IInputMethod");
      if ((localIInterface != null) && ((localIInterface instanceof IInputMethod))) {
        return (IInputMethod)localIInterface;
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
        IBinder localIBinder = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        IInputContext localIInputContext = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (InputMethodSubtype)InputMethodSubtype.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIInputContext;
          }
          changeInputMethodSubtype(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIBinder;
          }
          hideSoftInput(paramInt1, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          showSoftInput(paramInt1, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
          revokeSession(IInputMethodSession.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
          paramParcel2 = IInputMethodSession.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setSessionEnabled(paramParcel2, bool);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (InputChannel)InputChannel.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          createSession(paramParcel2, IInputSessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
          localIBinder = paramParcel1.readStrongBinder();
          localIInputContext = IInputContext.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel2 = (EditorInfo)EditorInfo.CREATOR.createFromParcel(paramParcel1);; paramParcel2 = localObject3) {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          startInput(localIBinder, localIInputContext, paramInt1, paramParcel2, bool);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
          unbindInput();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (InputBinding)InputBinding.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          bindInput(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.view.IInputMethod");
        attachToken(paramParcel1.readStrongBinder());
        return true;
      }
      paramParcel2.writeString("com.android.internal.view.IInputMethod");
      return true;
    }
    
    private static class Proxy
      implements IInputMethod
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
      
      public void attachToken(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          localParcel.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void bindInput(InputBinding paramInputBinding)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          if (paramInputBinding != null)
          {
            localParcel.writeInt(1);
            paramInputBinding.writeToParcel(localParcel, 0);
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
      
      public void changeInputMethodSubtype(InputMethodSubtype paramInputMethodSubtype)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          if (paramInputMethodSubtype != null)
          {
            localParcel.writeInt(1);
            paramInputMethodSubtype.writeToParcel(localParcel, 0);
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
      
      public void createSession(InputChannel paramInputChannel, IInputSessionCallback paramIInputSessionCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          if (paramInputChannel != null)
          {
            localParcel.writeInt(1);
            paramInputChannel.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIInputSessionCallback != null) {
            paramInputChannel = paramIInputSessionCallback.asBinder();
          } else {
            paramInputChannel = null;
          }
          localParcel.writeStrongBinder(paramInputChannel);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.view.IInputMethod";
      }
      
      public void hideSoftInput(int paramInt, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          localParcel.writeInt(paramInt);
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
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
      
      public void revokeSession(IInputMethodSession paramIInputMethodSession)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          if (paramIInputMethodSession != null) {
            paramIInputMethodSession = paramIInputMethodSession.asBinder();
          } else {
            paramIInputMethodSession = null;
          }
          localParcel.writeStrongBinder(paramIInputMethodSession);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setSessionEnabled(IInputMethodSession paramIInputMethodSession, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          if (paramIInputMethodSession != null) {
            paramIInputMethodSession = paramIInputMethodSession.asBinder();
          } else {
            paramIInputMethodSession = null;
          }
          localParcel.writeStrongBinder(paramIInputMethodSession);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showSoftInput(int paramInt, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          localParcel.writeInt(paramInt);
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
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
      
      public void startInput(IBinder paramIBinder, IInputContext paramIInputContext, int paramInt, EditorInfo paramEditorInfo, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          localParcel.writeStrongBinder(paramIBinder);
          if (paramIInputContext != null) {
            paramIBinder = paramIInputContext.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramInt);
          if (paramEditorInfo != null)
          {
            localParcel.writeInt(1);
            paramEditorInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unbindInput()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethod");
          mRemote.transact(3, localParcel, null, 1);
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
