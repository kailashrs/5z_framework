package com.android.internal.view;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputContentInfo;

public abstract interface IInputContext
  extends IInterface
{
  public abstract void beginBatchEdit()
    throws RemoteException;
  
  public abstract void clearMetaKeyStates(int paramInt)
    throws RemoteException;
  
  public abstract void commitCompletion(CompletionInfo paramCompletionInfo)
    throws RemoteException;
  
  public abstract void commitContent(InputContentInfo paramInputContentInfo, int paramInt1, Bundle paramBundle, int paramInt2, IInputContextCallback paramIInputContextCallback)
    throws RemoteException;
  
  public abstract void commitCorrection(CorrectionInfo paramCorrectionInfo)
    throws RemoteException;
  
  public abstract void commitText(CharSequence paramCharSequence, int paramInt)
    throws RemoteException;
  
  public abstract void deleteSurroundingText(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void deleteSurroundingTextInCodePoints(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void endBatchEdit()
    throws RemoteException;
  
  public abstract void finishComposingText()
    throws RemoteException;
  
  public abstract void getCursorCapsMode(int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
    throws RemoteException;
  
  public abstract void getExtractedText(ExtractedTextRequest paramExtractedTextRequest, int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
    throws RemoteException;
  
  public abstract void getSelectedText(int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
    throws RemoteException;
  
  public abstract void getTextAfterCursor(int paramInt1, int paramInt2, int paramInt3, IInputContextCallback paramIInputContextCallback)
    throws RemoteException;
  
  public abstract void getTextBeforeCursor(int paramInt1, int paramInt2, int paramInt3, IInputContextCallback paramIInputContextCallback)
    throws RemoteException;
  
  public abstract void performContextMenuAction(int paramInt)
    throws RemoteException;
  
  public abstract void performEditorAction(int paramInt)
    throws RemoteException;
  
  public abstract void performPrivateCommand(String paramString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void requestUpdateCursorAnchorInfo(int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
    throws RemoteException;
  
  public abstract void sendKeyEvent(KeyEvent paramKeyEvent)
    throws RemoteException;
  
  public abstract void setComposingRegion(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setComposingText(CharSequence paramCharSequence, int paramInt)
    throws RemoteException;
  
  public abstract void setSelection(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputContext
  {
    private static final String DESCRIPTOR = "com.android.internal.view.IInputContext";
    static final int TRANSACTION_beginBatchEdit = 15;
    static final int TRANSACTION_clearMetaKeyStates = 18;
    static final int TRANSACTION_commitCompletion = 10;
    static final int TRANSACTION_commitContent = 23;
    static final int TRANSACTION_commitCorrection = 11;
    static final int TRANSACTION_commitText = 9;
    static final int TRANSACTION_deleteSurroundingText = 5;
    static final int TRANSACTION_deleteSurroundingTextInCodePoints = 6;
    static final int TRANSACTION_endBatchEdit = 16;
    static final int TRANSACTION_finishComposingText = 8;
    static final int TRANSACTION_getCursorCapsMode = 3;
    static final int TRANSACTION_getExtractedText = 4;
    static final int TRANSACTION_getSelectedText = 21;
    static final int TRANSACTION_getTextAfterCursor = 2;
    static final int TRANSACTION_getTextBeforeCursor = 1;
    static final int TRANSACTION_performContextMenuAction = 14;
    static final int TRANSACTION_performEditorAction = 13;
    static final int TRANSACTION_performPrivateCommand = 19;
    static final int TRANSACTION_requestUpdateCursorAnchorInfo = 22;
    static final int TRANSACTION_sendKeyEvent = 17;
    static final int TRANSACTION_setComposingRegion = 20;
    static final int TRANSACTION_setComposingText = 7;
    static final int TRANSACTION_setSelection = 12;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.view.IInputContext");
    }
    
    public static IInputContext asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.view.IInputContext");
      if ((localIInterface != null) && ((localIInterface instanceof IInputContext))) {
        return (IInputContext)localIInterface;
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
        Object localObject6 = null;
        Object localObject7 = null;
        Bundle localBundle = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (InputContentInfo)InputContentInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          commitContent(paramParcel2, paramInt1, localBundle, paramParcel1.readInt(), IInputContextCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          requestUpdateCursorAnchorInfo(paramParcel1.readInt(), paramParcel1.readInt(), IInputContextCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          getSelectedText(paramParcel1.readInt(), paramParcel1.readInt(), IInputContextCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          setComposingRegion(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          paramParcel2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          performPrivateCommand(paramParcel2, paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          clearMetaKeyStates(paramParcel1.readInt());
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          sendKeyEvent(paramParcel1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          endBatchEdit();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          beginBatchEdit();
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          performContextMenuAction(paramParcel1.readInt());
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          performEditorAction(paramParcel1.readInt());
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          setSelection(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CorrectionInfo)CorrectionInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          commitCorrection(paramParcel1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CompletionInfo)CompletionInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          commitCompletion(paramParcel1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject5;
          }
          commitText(paramParcel2, paramParcel1.readInt());
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          finishComposingText();
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject6;
          }
          setComposingText(paramParcel2, paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          deleteSurroundingTextInCodePoints(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          deleteSurroundingText(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (ExtractedTextRequest)ExtractedTextRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject7;
          }
          getExtractedText(paramParcel2, paramParcel1.readInt(), paramParcel1.readInt(), IInputContextCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          getCursorCapsMode(paramParcel1.readInt(), paramParcel1.readInt(), IInputContextCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
          getTextAfterCursor(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), IInputContextCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.view.IInputContext");
        getTextBeforeCursor(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), IInputContextCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("com.android.internal.view.IInputContext");
      return true;
    }
    
    private static class Proxy
      implements IInputContext
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
      
      public void beginBatchEdit()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void clearMetaKeyStates(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt);
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void commitCompletion(CompletionInfo paramCompletionInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          if (paramCompletionInfo != null)
          {
            localParcel.writeInt(1);
            paramCompletionInfo.writeToParcel(localParcel, 0);
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
      
      public void commitContent(InputContentInfo paramInputContentInfo, int paramInt1, Bundle paramBundle, int paramInt2, IInputContextCallback paramIInputContextCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          if (paramInputContentInfo != null)
          {
            localParcel.writeInt(1);
            paramInputContentInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt2);
          if (paramIInputContextCallback != null) {
            paramInputContentInfo = paramIInputContextCallback.asBinder();
          } else {
            paramInputContentInfo = null;
          }
          localParcel.writeStrongBinder(paramInputContentInfo);
          mRemote.transact(23, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void commitCorrection(CorrectionInfo paramCorrectionInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          if (paramCorrectionInfo != null)
          {
            localParcel.writeInt(1);
            paramCorrectionInfo.writeToParcel(localParcel, 0);
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
      
      public void commitText(CharSequence paramCharSequence, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
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
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deleteSurroundingText(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void deleteSurroundingTextInCodePoints(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void endBatchEdit()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void finishComposingText()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getCursorCapsMode(int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramIInputContextCallback != null) {
            paramIInputContextCallback = paramIInputContextCallback.asBinder();
          } else {
            paramIInputContextCallback = null;
          }
          localParcel.writeStrongBinder(paramIInputContextCallback);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getExtractedText(ExtractedTextRequest paramExtractedTextRequest, int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          if (paramExtractedTextRequest != null)
          {
            localParcel.writeInt(1);
            paramExtractedTextRequest.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramIInputContextCallback != null) {
            paramExtractedTextRequest = paramIInputContextCallback.asBinder();
          } else {
            paramExtractedTextRequest = null;
          }
          localParcel.writeStrongBinder(paramExtractedTextRequest);
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
        return "com.android.internal.view.IInputContext";
      }
      
      public void getSelectedText(int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramIInputContextCallback != null) {
            paramIInputContextCallback = paramIInputContextCallback.asBinder();
          } else {
            paramIInputContextCallback = null;
          }
          localParcel.writeStrongBinder(paramIInputContextCallback);
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getTextAfterCursor(int paramInt1, int paramInt2, int paramInt3, IInputContextCallback paramIInputContextCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramIInputContextCallback != null) {
            paramIInputContextCallback = paramIInputContextCallback.asBinder();
          } else {
            paramIInputContextCallback = null;
          }
          localParcel.writeStrongBinder(paramIInputContextCallback);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getTextBeforeCursor(int paramInt1, int paramInt2, int paramInt3, IInputContextCallback paramIInputContextCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramIInputContextCallback != null) {
            paramIInputContextCallback = paramIInputContextCallback.asBinder();
          } else {
            paramIInputContextCallback = null;
          }
          localParcel.writeStrongBinder(paramIInputContextCallback);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void performContextMenuAction(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void performEditorAction(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void performPrivateCommand(String paramString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
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
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void requestUpdateCursorAnchorInfo(int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramIInputContextCallback != null) {
            paramIInputContextCallback = paramIInputContextCallback.asBinder();
          } else {
            paramIInputContextCallback = null;
          }
          localParcel.writeStrongBinder(paramIInputContextCallback);
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void sendKeyEvent(KeyEvent paramKeyEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          if (paramKeyEvent != null)
          {
            localParcel.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setComposingRegion(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setComposingText(CharSequence paramCharSequence, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
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
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setSelection(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputContext");
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
    }
  }
}
