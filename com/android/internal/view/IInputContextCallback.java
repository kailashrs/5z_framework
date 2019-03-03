package com.android.internal.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.inputmethod.ExtractedText;

public abstract interface IInputContextCallback
  extends IInterface
{
  public abstract void setCommitContentResult(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setCursorCapsMode(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setExtractedText(ExtractedText paramExtractedText, int paramInt)
    throws RemoteException;
  
  public abstract void setRequestUpdateCursorAnchorInfoResult(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setSelectedText(CharSequence paramCharSequence, int paramInt)
    throws RemoteException;
  
  public abstract void setTextAfterCursor(CharSequence paramCharSequence, int paramInt)
    throws RemoteException;
  
  public abstract void setTextBeforeCursor(CharSequence paramCharSequence, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputContextCallback
  {
    private static final String DESCRIPTOR = "com.android.internal.view.IInputContextCallback";
    static final int TRANSACTION_setCommitContentResult = 7;
    static final int TRANSACTION_setCursorCapsMode = 3;
    static final int TRANSACTION_setExtractedText = 4;
    static final int TRANSACTION_setRequestUpdateCursorAnchorInfoResult = 6;
    static final int TRANSACTION_setSelectedText = 5;
    static final int TRANSACTION_setTextAfterCursor = 2;
    static final int TRANSACTION_setTextBeforeCursor = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.view.IInputContextCallback");
    }
    
    public static IInputContextCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.view.IInputContextCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IInputContextCallback))) {
        return (IInputContextCallback)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContextCallback");
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setCommitContentResult(bool2, paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContextCallback");
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          setRequestUpdateCursorAnchorInfoResult(bool2, paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContextCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject4;
          }
          setSelectedText(paramParcel2, paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContextCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ExtractedText)ExtractedText.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          setExtractedText(paramParcel2, paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContextCallback");
          setCursorCapsMode(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContextCallback");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          setTextAfterCursor(paramParcel2, paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.view.IInputContextCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = localObject3;
        }
        setTextBeforeCursor(paramParcel2, paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("com.android.internal.view.IInputContextCallback");
      return true;
    }
    
    private static class Proxy
      implements IInputContextCallback
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
        return "com.android.internal.view.IInputContextCallback";
      }
      
      public void setCommitContentResult(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContextCallback");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setCursorCapsMode(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContextCallback");
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
      
      public void setExtractedText(ExtractedText paramExtractedText, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContextCallback");
          if (paramExtractedText != null)
          {
            localParcel.writeInt(1);
            paramExtractedText.writeToParcel(localParcel, 0);
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
      
      public void setRequestUpdateCursorAnchorInfoResult(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContextCallback");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setSelectedText(CharSequence paramCharSequence, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContextCallback");
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
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
      
      public void setTextAfterCursor(CharSequence paramCharSequence, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContextCallback");
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setTextBeforeCursor(CharSequence paramCharSequence, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContextCallback");
          if (paramCharSequence != null)
          {
            localParcel.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel, 0);
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
    }
  }
}
