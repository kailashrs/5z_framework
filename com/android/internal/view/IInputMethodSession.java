package com.android.internal.view;

import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.ExtractedText;

public abstract interface IInputMethodSession
  extends IInterface
{
  public abstract void appPrivateCommand(String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void displayCompletions(CompletionInfo[] paramArrayOfCompletionInfo)
    throws RemoteException;
  
  public abstract void finishInput()
    throws RemoteException;
  
  public abstract void finishSession()
    throws RemoteException;
  
  public abstract void toggleSoftInput(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void updateCursor(Rect paramRect)
    throws RemoteException;
  
  public abstract void updateCursorAnchorInfo(CursorAnchorInfo paramCursorAnchorInfo)
    throws RemoteException;
  
  public abstract void updateExtractedText(int paramInt, ExtractedText paramExtractedText)
    throws RemoteException;
  
  public abstract void updateSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
    throws RemoteException;
  
  public abstract void viewClicked(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputMethodSession
  {
    private static final String DESCRIPTOR = "com.android.internal.view.IInputMethodSession";
    static final int TRANSACTION_appPrivateCommand = 7;
    static final int TRANSACTION_displayCompletions = 6;
    static final int TRANSACTION_finishInput = 1;
    static final int TRANSACTION_finishSession = 9;
    static final int TRANSACTION_toggleSoftInput = 8;
    static final int TRANSACTION_updateCursor = 5;
    static final int TRANSACTION_updateCursorAnchorInfo = 10;
    static final int TRANSACTION_updateExtractedText = 2;
    static final int TRANSACTION_updateSelection = 3;
    static final int TRANSACTION_viewClicked = 4;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.view.IInputMethodSession");
    }
    
    public static IInputMethodSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.view.IInputMethodSession");
      if ((localIInterface != null) && ((localIInterface instanceof IInputMethodSession))) {
        return (IInputMethodSession)localIInterface;
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
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CursorAnchorInfo)CursorAnchorInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          updateCursorAnchorInfo(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
          finishSession();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
          toggleSoftInput(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          appPrivateCommand(paramParcel2, paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
          displayCompletions((CompletionInfo[])paramParcel1.createTypedArray(CompletionInfo.CREATOR));
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          updateCursor(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          viewClicked(bool);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
          updateSelection(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ExtractedText)ExtractedText.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          updateExtractedText(paramInt1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.view.IInputMethodSession");
        finishInput();
        return true;
      }
      paramParcel2.writeString("com.android.internal.view.IInputMethodSession");
      return true;
    }
    
    private static class Proxy
      implements IInputMethodSession
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
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
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
          mRemote.transact(7, localParcel, null, 1);
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
      
      public void displayCompletions(CompletionInfo[] paramArrayOfCompletionInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
          localParcel.writeTypedArray(paramArrayOfCompletionInfo, 0);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void finishInput()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void finishSession()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
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
        return "com.android.internal.view.IInputMethodSession";
      }
      
      public void toggleSoftInput(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
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
      
      public void updateCursor(Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
          if (paramRect != null)
          {
            localParcel.writeInt(1);
            paramRect.writeToParcel(localParcel, 0);
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
      
      public void updateCursorAnchorInfo(CursorAnchorInfo paramCursorAnchorInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
          if (paramCursorAnchorInfo != null)
          {
            localParcel.writeInt(1);
            paramCursorAnchorInfo.writeToParcel(localParcel, 0);
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
      
      public void updateExtractedText(int paramInt, ExtractedText paramExtractedText)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
          localParcel.writeInt(paramInt);
          if (paramExtractedText != null)
          {
            localParcel.writeInt(1);
            paramExtractedText.writeToParcel(localParcel, 0);
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
      
      public void updateSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          localParcel.writeInt(paramInt5);
          localParcel.writeInt(paramInt6);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void viewClicked(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodSession");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(4, localParcel, null, 1);
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
