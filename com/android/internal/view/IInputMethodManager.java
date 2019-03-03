package com.android.internal.view;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.style.SuggestionSpan;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.inputmethod.IInputContentUriToken;
import com.android.internal.inputmethod.IInputContentUriToken.Stub;
import java.util.ArrayList;
import java.util.List;

public abstract interface IInputMethodManager
  extends IInterface
{
  public abstract void addClient(IInputMethodClient paramIInputMethodClient, IInputContext paramIInputContext, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void clearLastInputMethodWindowForTransition(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract IInputContentUriToken createInputContentUriToken(IBinder paramIBinder, Uri paramUri, String paramString)
    throws RemoteException;
  
  public abstract void finishInput(IInputMethodClient paramIInputMethodClient)
    throws RemoteException;
  
  public abstract InputMethodSubtype getCurrentInputMethodSubtype()
    throws RemoteException;
  
  public abstract List<InputMethodInfo> getEnabledInputMethodList()
    throws RemoteException;
  
  public abstract List<InputMethodSubtype> getEnabledInputMethodSubtypeList(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract List<InputMethodInfo> getInputMethodList()
    throws RemoteException;
  
  public abstract int getInputMethodWindowVisibleHeight()
    throws RemoteException;
  
  public abstract InputMethodSubtype getLastInputMethodSubtype()
    throws RemoteException;
  
  public abstract List getShortcutInputMethodsAndSubtypes()
    throws RemoteException;
  
  public abstract List<InputMethodInfo> getVrInputMethodList()
    throws RemoteException;
  
  public abstract void hideMySoftInput(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract boolean hideSoftInput(IInputMethodClient paramIInputMethodClient, int paramInt, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract boolean isInputMethodPickerShownForTest()
    throws RemoteException;
  
  public abstract boolean notifySuggestionPicked(SuggestionSpan paramSuggestionSpan, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void notifyUserAction(int paramInt)
    throws RemoteException;
  
  public abstract void registerSuggestionSpansForNotification(SuggestionSpan[] paramArrayOfSuggestionSpan)
    throws RemoteException;
  
  public abstract void removeClient(IInputMethodClient paramIInputMethodClient)
    throws RemoteException;
  
  public abstract void reportFullscreenMode(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setAdditionalInputMethodSubtypes(String paramString, InputMethodSubtype[] paramArrayOfInputMethodSubtype)
    throws RemoteException;
  
  public abstract boolean setCurrentInputMethodSubtype(InputMethodSubtype paramInputMethodSubtype)
    throws RemoteException;
  
  public abstract void setImeWindowStatus(IBinder paramIBinder1, IBinder paramIBinder2, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setInputMethod(IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract void setInputMethodAndSubtype(IBinder paramIBinder, String paramString, InputMethodSubtype paramInputMethodSubtype)
    throws RemoteException;
  
  public abstract boolean shouldOfferSwitchingToNextInputMethod(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void showInputMethodAndSubtypeEnablerFromClient(IInputMethodClient paramIInputMethodClient, String paramString)
    throws RemoteException;
  
  public abstract void showInputMethodPickerFromClient(IInputMethodClient paramIInputMethodClient, int paramInt)
    throws RemoteException;
  
  public abstract void showMySoftInput(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract boolean showSoftInput(IInputMethodClient paramIInputMethodClient, int paramInt, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract InputBindResult startInputOrWindowGainedFocus(int paramInt1, IInputMethodClient paramIInputMethodClient, IBinder paramIBinder, int paramInt2, int paramInt3, int paramInt4, EditorInfo paramEditorInfo, IInputContext paramIInputContext, int paramInt5, int paramInt6)
    throws RemoteException;
  
  public abstract boolean switchToNextInputMethod(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean switchToPreviousInputMethod(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void updateStatusIcon(IBinder paramIBinder, String paramString, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputMethodManager
  {
    private static final String DESCRIPTOR = "com.android.internal.view.IInputMethodManager";
    static final int TRANSACTION_addClient = 7;
    static final int TRANSACTION_clearLastInputMethodWindowForTransition = 31;
    static final int TRANSACTION_createInputContentUriToken = 32;
    static final int TRANSACTION_finishInput = 9;
    static final int TRANSACTION_getCurrentInputMethodSubtype = 24;
    static final int TRANSACTION_getEnabledInputMethodList = 3;
    static final int TRANSACTION_getEnabledInputMethodSubtypeList = 4;
    static final int TRANSACTION_getInputMethodList = 1;
    static final int TRANSACTION_getInputMethodWindowVisibleHeight = 30;
    static final int TRANSACTION_getLastInputMethodSubtype = 5;
    static final int TRANSACTION_getShortcutInputMethodsAndSubtypes = 6;
    static final int TRANSACTION_getVrInputMethodList = 2;
    static final int TRANSACTION_hideMySoftInput = 18;
    static final int TRANSACTION_hideSoftInput = 11;
    static final int TRANSACTION_isInputMethodPickerShownForTest = 15;
    static final int TRANSACTION_notifySuggestionPicked = 23;
    static final int TRANSACTION_notifyUserAction = 34;
    static final int TRANSACTION_registerSuggestionSpansForNotification = 22;
    static final int TRANSACTION_removeClient = 8;
    static final int TRANSACTION_reportFullscreenMode = 33;
    static final int TRANSACTION_setAdditionalInputMethodSubtypes = 29;
    static final int TRANSACTION_setCurrentInputMethodSubtype = 25;
    static final int TRANSACTION_setImeWindowStatus = 21;
    static final int TRANSACTION_setInputMethod = 16;
    static final int TRANSACTION_setInputMethodAndSubtype = 17;
    static final int TRANSACTION_shouldOfferSwitchingToNextInputMethod = 28;
    static final int TRANSACTION_showInputMethodAndSubtypeEnablerFromClient = 14;
    static final int TRANSACTION_showInputMethodPickerFromClient = 13;
    static final int TRANSACTION_showMySoftInput = 19;
    static final int TRANSACTION_showSoftInput = 10;
    static final int TRANSACTION_startInputOrWindowGainedFocus = 12;
    static final int TRANSACTION_switchToNextInputMethod = 27;
    static final int TRANSACTION_switchToPreviousInputMethod = 26;
    static final int TRANSACTION_updateStatusIcon = 20;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.view.IInputMethodManager");
    }
    
    public static IInputMethodManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.view.IInputMethodManager");
      if ((localIInterface != null) && ((localIInterface instanceof IInputMethodManager))) {
        return (IInputMethodManager)localIInterface;
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
        IInputMethodClient localIInputMethodClient = null;
        Object localObject5 = null;
        IBinder localIBinder = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          notifyUserAction(paramParcel1.readInt());
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          localObject3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          reportFullscreenMode((IBinder)localObject3, bool2);
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          localObject4 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = null;
          }
          localObject3 = createInputContentUriToken((IBinder)localObject4, (Uri)localObject3, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel1 = localIBinder;
          if (localObject3 != null) {
            paramParcel1 = ((IInputContentUriToken)localObject3).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          clearLastInputMethodWindowForTransition(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          paramInt1 = getInputMethodWindowVisibleHeight();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          setAdditionalInputMethodSubtypes(paramParcel1.readString(), (InputMethodSubtype[])paramParcel1.createTypedArray(InputMethodSubtype.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          paramInt1 = shouldOfferSwitchingToNextInputMethod(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          localObject3 = paramParcel1.readStrongBinder();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          paramInt1 = switchToNextInputMethod((IBinder)localObject3, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          paramInt1 = switchToPreviousInputMethod(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (InputMethodSubtype)InputMethodSubtype.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = setCurrentInputMethodSubtype(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          paramParcel1 = getCurrentInputMethodSubtype();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          if (paramParcel1.readInt() != 0) {
            localObject3 = (SuggestionSpan)SuggestionSpan.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject3 = localObject2;
          }
          paramInt1 = notifySuggestionPicked((SuggestionSpan)localObject3, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          registerSuggestionSpansForNotification((SuggestionSpan[])paramParcel1.createTypedArray(SuggestionSpan.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          setImeWindowStatus(paramParcel1.readStrongBinder(), paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          updateStatusIcon(paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          showMySoftInput(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          hideMySoftInput(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          localIBinder = paramParcel1.readStrongBinder();
          localObject4 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (InputMethodSubtype)InputMethodSubtype.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject3;
          }
          setInputMethodAndSubtype(localIBinder, (String)localObject4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          setInputMethod(paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          paramInt1 = isInputMethodPickerShownForTest();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          showInputMethodAndSubtypeEnablerFromClient(IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          showInputMethodPickerFromClient(IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          int i = paramParcel1.readInt();
          localIInputMethodClient = IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder());
          localIBinder = paramParcel1.readStrongBinder();
          paramInt2 = paramParcel1.readInt();
          int j = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (localObject3 = (EditorInfo)EditorInfo.CREATOR.createFromParcel(paramParcel1);; localObject3 = localObject4) {
            break;
          }
          paramParcel1 = startInputOrWindowGainedFocus(i, localIInputMethodClient, localIBinder, paramInt2, j, paramInt1, (EditorInfo)localObject3, IInputContext.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          localObject3 = IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIInputMethodClient;
          }
          paramInt1 = hideSoftInput((IInputMethodClient)localObject3, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          localObject3 = IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder());
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          paramInt1 = showSoftInput((IInputMethodClient)localObject3, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          finishInput(IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          removeClient(IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          addClient(IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder()), IInputContext.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          paramParcel1 = getShortcutInputMethodsAndSubtypes();
          paramParcel2.writeNoException();
          paramParcel2.writeList(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          paramParcel1 = getLastInputMethodSubtype();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 4: 
          bool2 = false;
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          localObject3 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          paramParcel1 = getEnabledInputMethodSubtypeList((String)localObject3, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          paramParcel1 = getEnabledInputMethodList();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
          paramParcel1 = getVrInputMethodList();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.view.IInputMethodManager");
        paramParcel1 = getInputMethodList();
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.view.IInputMethodManager");
      return true;
    }
    
    private static class Proxy
      implements IInputMethodManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addClient(IInputMethodClient paramIInputMethodClient, IInputContext paramIInputContext, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          Object localObject = null;
          if (paramIInputMethodClient != null) {
            paramIInputMethodClient = paramIInputMethodClient.asBinder();
          } else {
            paramIInputMethodClient = null;
          }
          localParcel1.writeStrongBinder(paramIInputMethodClient);
          paramIInputMethodClient = localObject;
          if (paramIInputContext != null) {
            paramIInputMethodClient = paramIInputContext.asBinder();
          }
          localParcel1.writeStrongBinder(paramIInputMethodClient);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void clearLastInputMethodWindowForTransition(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IInputContentUriToken createInputContentUriToken(IBinder paramIBinder, Uri paramUri, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIBinder = IInputContentUriToken.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void finishInput(IInputMethodClient paramIInputMethodClient)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          if (paramIInputMethodClient != null) {
            paramIInputMethodClient = paramIInputMethodClient.asBinder();
          } else {
            paramIInputMethodClient = null;
          }
          localParcel1.writeStrongBinder(paramIInputMethodClient);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public InputMethodSubtype getCurrentInputMethodSubtype()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          InputMethodSubtype localInputMethodSubtype;
          if (localParcel2.readInt() != 0) {
            localInputMethodSubtype = (InputMethodSubtype)InputMethodSubtype.CREATOR.createFromParcel(localParcel2);
          } else {
            localInputMethodSubtype = null;
          }
          return localInputMethodSubtype;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<InputMethodInfo> getEnabledInputMethodList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(InputMethodInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<InputMethodSubtype> getEnabledInputMethodSubtypeList(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(InputMethodSubtype.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<InputMethodInfo> getInputMethodList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(InputMethodInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getInputMethodWindowVisibleHeight()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.view.IInputMethodManager";
      }
      
      public InputMethodSubtype getLastInputMethodSubtype()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          InputMethodSubtype localInputMethodSubtype;
          if (localParcel2.readInt() != 0) {
            localInputMethodSubtype = (InputMethodSubtype)InputMethodSubtype.CREATOR.createFromParcel(localParcel2);
          } else {
            localInputMethodSubtype = null;
          }
          return localInputMethodSubtype;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List getShortcutInputMethodsAndSubtypes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.readArrayList(getClass().getClassLoader());
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<InputMethodInfo> getVrInputMethodList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(InputMethodInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void hideMySoftInput(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hideSoftInput(IInputMethodClient paramIInputMethodClient, int paramInt, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          if (paramIInputMethodClient != null) {
            paramIInputMethodClient = paramIInputMethodClient.asBinder();
          } else {
            paramIInputMethodClient = null;
          }
          localParcel1.writeStrongBinder(paramIInputMethodClient);
          localParcel1.writeInt(paramInt);
          boolean bool = true;
          if (paramResultReceiver != null)
          {
            localParcel1.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isInputMethodPickerShownForTest()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean notifySuggestionPicked(SuggestionSpan paramSuggestionSpan, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          boolean bool = true;
          if (paramSuggestionSpan != null)
          {
            localParcel1.writeInt(1);
            paramSuggestionSpan.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyUserAction(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel.writeInt(paramInt);
          mRemote.transact(34, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registerSuggestionSpansForNotification(SuggestionSpan[] paramArrayOfSuggestionSpan)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeTypedArray(paramArrayOfSuggestionSpan, 0);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeClient(IInputMethodClient paramIInputMethodClient)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          if (paramIInputMethodClient != null) {
            paramIInputMethodClient = paramIInputMethodClient.asBinder();
          } else {
            paramIInputMethodClient = null;
          }
          localParcel1.writeStrongBinder(paramIInputMethodClient);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportFullscreenMode(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAdditionalInputMethodSubtypes(String paramString, InputMethodSubtype[] paramArrayOfInputMethodSubtype)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeString(paramString);
          localParcel1.writeTypedArray(paramArrayOfInputMethodSubtype, 0);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setCurrentInputMethodSubtype(InputMethodSubtype paramInputMethodSubtype)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          boolean bool = true;
          if (paramInputMethodSubtype != null)
          {
            localParcel1.writeInt(1);
            paramInputMethodSubtype.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setImeWindowStatus(IBinder paramIBinder1, IBinder paramIBinder2, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder1);
          localParcel1.writeStrongBinder(paramIBinder2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInputMethod(IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInputMethodAndSubtype(IBinder paramIBinder, String paramString, InputMethodSubtype paramInputMethodSubtype)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          if (paramInputMethodSubtype != null)
          {
            localParcel1.writeInt(1);
            paramInputMethodSubtype.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean shouldOfferSwitchingToNextInputMethod(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showInputMethodAndSubtypeEnablerFromClient(IInputMethodClient paramIInputMethodClient, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          if (paramIInputMethodClient != null) {
            paramIInputMethodClient = paramIInputMethodClient.asBinder();
          } else {
            paramIInputMethodClient = null;
          }
          localParcel1.writeStrongBinder(paramIInputMethodClient);
          localParcel1.writeString(paramString);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showInputMethodPickerFromClient(IInputMethodClient paramIInputMethodClient, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          if (paramIInputMethodClient != null) {
            paramIInputMethodClient = paramIInputMethodClient.asBinder();
          } else {
            paramIInputMethodClient = null;
          }
          localParcel1.writeStrongBinder(paramIInputMethodClient);
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showMySoftInput(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean showSoftInput(IInputMethodClient paramIInputMethodClient, int paramInt, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          if (paramIInputMethodClient != null) {
            paramIInputMethodClient = paramIInputMethodClient.asBinder();
          } else {
            paramIInputMethodClient = null;
          }
          localParcel1.writeStrongBinder(paramIInputMethodClient);
          localParcel1.writeInt(paramInt);
          boolean bool = true;
          if (paramResultReceiver != null)
          {
            localParcel1.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public InputBindResult startInputOrWindowGainedFocus(int paramInt1, IInputMethodClient paramIInputMethodClient, IBinder paramIBinder, int paramInt2, int paramInt3, int paramInt4, EditorInfo paramEditorInfo, IInputContext paramIInputContext, int paramInt5, int paramInt6)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 11
        //   5: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 12
        //   10: aload 11
        //   12: ldc 32
        //   14: invokevirtual 36	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 11
        //   19: iload_1
        //   20: invokevirtual 52	android/os/Parcel:writeInt	(I)V
        //   23: aload_2
        //   24: ifnull +13 -> 37
        //   27: aload_2
        //   28: invokeinterface 42 1 0
        //   33: astore_2
        //   34: goto +5 -> 39
        //   37: aconst_null
        //   38: astore_2
        //   39: aload 11
        //   41: aload_2
        //   42: invokevirtual 45	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   45: aload 11
        //   47: aload_3
        //   48: invokevirtual 45	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   51: aload 11
        //   53: iload 4
        //   55: invokevirtual 52	android/os/Parcel:writeInt	(I)V
        //   58: aload 11
        //   60: iload 5
        //   62: invokevirtual 52	android/os/Parcel:writeInt	(I)V
        //   65: aload 11
        //   67: iload 6
        //   69: invokevirtual 52	android/os/Parcel:writeInt	(I)V
        //   72: aload 7
        //   74: ifnull +20 -> 94
        //   77: aload 11
        //   79: iconst_1
        //   80: invokevirtual 52	android/os/Parcel:writeInt	(I)V
        //   83: aload 7
        //   85: aload 11
        //   87: iconst_0
        //   88: invokevirtual 189	android/view/inputmethod/EditorInfo:writeToParcel	(Landroid/os/Parcel;I)V
        //   91: goto +9 -> 100
        //   94: aload 11
        //   96: iconst_0
        //   97: invokevirtual 52	android/os/Parcel:writeInt	(I)V
        //   100: aload 8
        //   102: ifnull +14 -> 116
        //   105: aload 8
        //   107: invokeinterface 48 1 0
        //   112: astore_2
        //   113: goto +5 -> 118
        //   116: aconst_null
        //   117: astore_2
        //   118: aload 11
        //   120: aload_2
        //   121: invokevirtual 45	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   124: aload 11
        //   126: iload 9
        //   128: invokevirtual 52	android/os/Parcel:writeInt	(I)V
        //   131: aload 11
        //   133: iload 10
        //   135: invokevirtual 52	android/os/Parcel:writeInt	(I)V
        //   138: aload_0
        //   139: getfield 19	com/android/internal/view/IInputMethodManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   142: bipush 12
        //   144: aload 11
        //   146: aload 12
        //   148: iconst_0
        //   149: invokeinterface 58 5 0
        //   154: pop
        //   155: aload 12
        //   157: invokevirtual 61	android/os/Parcel:readException	()V
        //   160: aload 12
        //   162: invokevirtual 94	android/os/Parcel:readInt	()I
        //   165: ifeq +20 -> 185
        //   168: getstatic 192	com/android/internal/view/InputBindResult:CREATOR	Landroid/os/Parcelable$Creator;
        //   171: aload 12
        //   173: invokeinterface 106 2 0
        //   178: checkcast 191	com/android/internal/view/InputBindResult
        //   181: astore_2
        //   182: goto +5 -> 187
        //   185: aconst_null
        //   186: astore_2
        //   187: aload 12
        //   189: invokevirtual 64	android/os/Parcel:recycle	()V
        //   192: aload 11
        //   194: invokevirtual 64	android/os/Parcel:recycle	()V
        //   197: aload_2
        //   198: areturn
        //   199: astore_2
        //   200: goto +32 -> 232
        //   203: astore_2
        //   204: goto +28 -> 232
        //   207: astore_2
        //   208: goto +24 -> 232
        //   211: astore_2
        //   212: goto +20 -> 232
        //   215: astore_2
        //   216: goto +16 -> 232
        //   219: astore_2
        //   220: goto +12 -> 232
        //   223: astore_2
        //   224: goto +8 -> 232
        //   227: astore_2
        //   228: goto +4 -> 232
        //   231: astore_2
        //   232: aload 12
        //   234: invokevirtual 64	android/os/Parcel:recycle	()V
        //   237: aload 11
        //   239: invokevirtual 64	android/os/Parcel:recycle	()V
        //   242: aload_2
        //   243: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	244	0	this	Proxy
        //   0	244	1	paramInt1	int
        //   0	244	2	paramIInputMethodClient	IInputMethodClient
        //   0	244	3	paramIBinder	IBinder
        //   0	244	4	paramInt2	int
        //   0	244	5	paramInt3	int
        //   0	244	6	paramInt4	int
        //   0	244	7	paramEditorInfo	EditorInfo
        //   0	244	8	paramIInputContext	IInputContext
        //   0	244	9	paramInt5	int
        //   0	244	10	paramInt6	int
        //   3	235	11	localParcel1	Parcel
        //   8	225	12	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   138	182	199	finally
        //   131	138	203	finally
        //   124	131	207	finally
        //   65	72	211	finally
        //   77	91	211	finally
        //   94	100	211	finally
        //   105	113	211	finally
        //   118	124	211	finally
        //   58	65	215	finally
        //   51	58	219	finally
        //   45	51	223	finally
        //   17	23	227	finally
        //   27	34	227	finally
        //   39	45	227	finally
        //   10	17	231	finally
      }
      
      public boolean switchToNextInputMethod(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean switchToPreviousInputMethod(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateStatusIcon(IBinder paramIBinder, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.view.IInputMethodManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
