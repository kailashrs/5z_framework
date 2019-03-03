package android.view.autofill;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.KeyEvent;
import java.util.List;

public abstract interface IAutoFillManagerClient
  extends IInterface
{
  public abstract void authenticate(int paramInt1, int paramInt2, IntentSender paramIntentSender, Intent paramIntent)
    throws RemoteException;
  
  public abstract void autofill(int paramInt, List<AutofillId> paramList, List<AutofillValue> paramList1)
    throws RemoteException;
  
  public abstract void dispatchUnhandledKey(int paramInt, AutofillId paramAutofillId, KeyEvent paramKeyEvent)
    throws RemoteException;
  
  public abstract void notifyNoFillUi(int paramInt1, AutofillId paramAutofillId, int paramInt2)
    throws RemoteException;
  
  public abstract void requestHideFillUi(int paramInt, AutofillId paramAutofillId)
    throws RemoteException;
  
  public abstract void requestShowFillUi(int paramInt1, AutofillId paramAutofillId, int paramInt2, int paramInt3, Rect paramRect, IAutofillWindowPresenter paramIAutofillWindowPresenter)
    throws RemoteException;
  
  public abstract void setSaveUiState(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSessionFinished(int paramInt)
    throws RemoteException;
  
  public abstract void setState(int paramInt)
    throws RemoteException;
  
  public abstract void setTrackedViews(int paramInt, AutofillId[] paramArrayOfAutofillId1, boolean paramBoolean1, boolean paramBoolean2, AutofillId[] paramArrayOfAutofillId2, AutofillId paramAutofillId)
    throws RemoteException;
  
  public abstract void startIntentSender(IntentSender paramIntentSender, Intent paramIntent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAutoFillManagerClient
  {
    private static final String DESCRIPTOR = "android.view.autofill.IAutoFillManagerClient";
    static final int TRANSACTION_authenticate = 3;
    static final int TRANSACTION_autofill = 2;
    static final int TRANSACTION_dispatchUnhandledKey = 8;
    static final int TRANSACTION_notifyNoFillUi = 7;
    static final int TRANSACTION_requestHideFillUi = 6;
    static final int TRANSACTION_requestShowFillUi = 5;
    static final int TRANSACTION_setSaveUiState = 10;
    static final int TRANSACTION_setSessionFinished = 11;
    static final int TRANSACTION_setState = 1;
    static final int TRANSACTION_setTrackedViews = 4;
    static final int TRANSACTION_startIntentSender = 9;
    
    public Stub()
    {
      attachInterface(this, "android.view.autofill.IAutoFillManagerClient");
    }
    
    public static IAutoFillManagerClient asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.autofill.IAutoFillManagerClient");
      if ((localIInterface != null) && ((localIInterface instanceof IAutoFillManagerClient))) {
        return (IAutoFillManagerClient)localIInterface;
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
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          setSessionFinished(paramParcel1.readInt());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          setSaveUiState(paramInt1, bool1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject5;
          }
          startIntentSender(paramParcel2, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (AutofillId)AutofillId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          dispatchUnhandledKey(paramInt1, paramParcel2, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (AutofillId)AutofillId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          notifyNoFillUi(paramInt1, paramParcel2, paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AutofillId)AutofillId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          requestHideFillUi(paramInt1, paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (AutofillId)AutofillId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          paramInt2 = paramParcel1.readInt();
          int i = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject5 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = null;
          }
          requestShowFillUi(paramInt1, paramParcel2, paramInt2, i, (Rect)localObject5, IAutofillWindowPresenter.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          paramInt1 = paramParcel1.readInt();
          paramParcel2 = (AutofillId[])paramParcel1.createTypedArray(AutofillId.CREATOR);
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          boolean bool2;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          localObject5 = (AutofillId[])paramParcel1.createTypedArray(AutofillId.CREATOR);
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AutofillId)AutofillId.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          setTrackedViews(paramInt1, paramParcel2, bool1, bool2, (AutofillId[])localObject5, paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          authenticate(paramInt2, paramInt1, paramParcel2, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
          autofill(paramParcel1.readInt(), paramParcel1.createTypedArrayList(AutofillId.CREATOR), paramParcel1.createTypedArrayList(AutofillValue.CREATOR));
          return true;
        }
        paramParcel1.enforceInterface("android.view.autofill.IAutoFillManagerClient");
        setState(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.view.autofill.IAutoFillManagerClient");
      return true;
    }
    
    private static class Proxy
      implements IAutoFillManagerClient
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
      
      public void authenticate(int paramInt1, int paramInt2, IntentSender paramIntentSender, Intent paramIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          if (paramIntentSender != null)
          {
            localParcel.writeInt(1);
            paramIntentSender.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
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
      
      public void autofill(int paramInt, List<AutofillId> paramList, List<AutofillValue> paramList1)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt);
          localParcel.writeTypedList(paramList);
          localParcel.writeTypedList(paramList1);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dispatchUnhandledKey(int paramInt, AutofillId paramAutofillId, KeyEvent paramKeyEvent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt);
          if (paramAutofillId != null)
          {
            localParcel.writeInt(1);
            paramAutofillId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramKeyEvent != null)
          {
            localParcel.writeInt(1);
            paramKeyEvent.writeToParcel(localParcel, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.view.autofill.IAutoFillManagerClient";
      }
      
      public void notifyNoFillUi(int paramInt1, AutofillId paramAutofillId, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt1);
          if (paramAutofillId != null)
          {
            localParcel.writeInt(1);
            paramAutofillId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt2);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void requestHideFillUi(int paramInt, AutofillId paramAutofillId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt);
          if (paramAutofillId != null)
          {
            localParcel.writeInt(1);
            paramAutofillId.writeToParcel(localParcel, 0);
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
      
      public void requestShowFillUi(int paramInt1, AutofillId paramAutofillId, int paramInt2, int paramInt3, Rect paramRect, IAutofillWindowPresenter paramIAutofillWindowPresenter)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt1);
          if (paramAutofillId != null)
          {
            localParcel.writeInt(1);
            paramAutofillId.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          if (paramRect != null)
          {
            localParcel.writeInt(1);
            paramRect.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIAutofillWindowPresenter != null) {
            paramAutofillId = paramIAutofillWindowPresenter.asBinder();
          } else {
            paramAutofillId = null;
          }
          localParcel.writeStrongBinder(paramAutofillId);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setSaveUiState(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setSessionFinished(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setState(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setTrackedViews(int paramInt, AutofillId[] paramArrayOfAutofillId1, boolean paramBoolean1, boolean paramBoolean2, AutofillId[] paramArrayOfAutofillId2, AutofillId paramAutofillId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          localParcel.writeInt(paramInt);
          localParcel.writeTypedArray(paramArrayOfAutofillId1, 0);
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          localParcel.writeTypedArray(paramArrayOfAutofillId2, 0);
          if (paramAutofillId != null)
          {
            localParcel.writeInt(1);
            paramAutofillId.writeToParcel(localParcel, 0);
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
      
      public void startIntentSender(IntentSender paramIntentSender, Intent paramIntent)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutoFillManagerClient");
          if (paramIntentSender != null)
          {
            localParcel.writeInt(1);
            paramIntentSender.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIntent != null)
          {
            localParcel.writeInt(1);
            paramIntent.writeToParcel(localParcel, 0);
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
    }
  }
}
