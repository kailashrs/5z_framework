package android.view.autofill;

import android.content.ComponentName;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.service.autofill.FillEventHistory;
import android.service.autofill.UserData;
import java.util.List;

public abstract interface IAutoFillManager
  extends IInterface
{
  public abstract int addClient(IAutoFillManagerClient paramIAutoFillManagerClient, int paramInt)
    throws RemoteException;
  
  public abstract void cancelSession(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void disableOwnedAutofillServices(int paramInt)
    throws RemoteException;
  
  public abstract void finishSession(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ComponentName getAutofillServiceComponentName()
    throws RemoteException;
  
  public abstract String[] getAvailableFieldClassificationAlgorithms()
    throws RemoteException;
  
  public abstract String getDefaultFieldClassificationAlgorithm()
    throws RemoteException;
  
  public abstract FillEventHistory getFillEventHistory()
    throws RemoteException;
  
  public abstract UserData getUserData()
    throws RemoteException;
  
  public abstract String getUserDataId()
    throws RemoteException;
  
  public abstract boolean isFieldClassificationEnabled()
    throws RemoteException;
  
  public abstract boolean isServiceEnabled(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isServiceSupported(int paramInt)
    throws RemoteException;
  
  public abstract void onPendingSaveUi(int paramInt, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void removeClient(IAutoFillManagerClient paramIAutoFillManagerClient, int paramInt)
    throws RemoteException;
  
  public abstract boolean restoreSession(int paramInt, IBinder paramIBinder1, IBinder paramIBinder2)
    throws RemoteException;
  
  public abstract void setAuthenticationResult(Bundle paramBundle, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void setAutofillFailure(int paramInt1, List<AutofillId> paramList, int paramInt2)
    throws RemoteException;
  
  public abstract void setHasCallback(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setUserData(UserData paramUserData)
    throws RemoteException;
  
  public abstract int startSession(IBinder paramIBinder1, IBinder paramIBinder2, AutofillId paramAutofillId, Rect paramRect, AutofillValue paramAutofillValue, int paramInt1, boolean paramBoolean1, int paramInt2, ComponentName paramComponentName, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract int updateOrRestartSession(IBinder paramIBinder1, IBinder paramIBinder2, AutofillId paramAutofillId, Rect paramRect, AutofillValue paramAutofillValue, int paramInt1, boolean paramBoolean1, int paramInt2, ComponentName paramComponentName, int paramInt3, int paramInt4, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void updateSession(int paramInt1, AutofillId paramAutofillId, Rect paramRect, AutofillValue paramAutofillValue, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAutoFillManager
  {
    private static final String DESCRIPTOR = "android.view.autofill.IAutoFillManager";
    static final int TRANSACTION_addClient = 1;
    static final int TRANSACTION_cancelSession = 10;
    static final int TRANSACTION_disableOwnedAutofillServices = 13;
    static final int TRANSACTION_finishSession = 9;
    static final int TRANSACTION_getAutofillServiceComponentName = 21;
    static final int TRANSACTION_getAvailableFieldClassificationAlgorithms = 22;
    static final int TRANSACTION_getDefaultFieldClassificationAlgorithm = 23;
    static final int TRANSACTION_getFillEventHistory = 4;
    static final int TRANSACTION_getUserData = 17;
    static final int TRANSACTION_getUserDataId = 18;
    static final int TRANSACTION_isFieldClassificationEnabled = 20;
    static final int TRANSACTION_isServiceEnabled = 15;
    static final int TRANSACTION_isServiceSupported = 14;
    static final int TRANSACTION_onPendingSaveUi = 16;
    static final int TRANSACTION_removeClient = 2;
    static final int TRANSACTION_restoreSession = 5;
    static final int TRANSACTION_setAuthenticationResult = 11;
    static final int TRANSACTION_setAutofillFailure = 8;
    static final int TRANSACTION_setHasCallback = 12;
    static final int TRANSACTION_setUserData = 19;
    static final int TRANSACTION_startSession = 3;
    static final int TRANSACTION_updateOrRestartSession = 7;
    static final int TRANSACTION_updateSession = 6;
    
    public Stub()
    {
      attachInterface(this, "android.view.autofill.IAutoFillManager");
    }
    
    public static IAutoFillManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.autofill.IAutoFillManager");
      if ((localIInterface != null) && ((localIInterface instanceof IAutoFillManager))) {
        return (IAutoFillManager)localIInterface;
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
        Rect localRect = null;
        Object localObject1 = null;
        IBinder localIBinder1 = null;
        Object localObject2 = null;
        IBinder localIBinder2;
        AutofillValue localAutofillValue;
        boolean bool2;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 23: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramParcel1 = getDefaultFieldClassificationAlgorithm();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramParcel1 = getAvailableFieldClassificationAlgorithms();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramParcel1 = getAutofillServiceComponentName();
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
        case 20: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramInt1 = isFieldClassificationEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserData)UserData.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          setUserData(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramParcel1 = getUserDataId();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramParcel1 = getUserData();
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
        case 16: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          onPendingSaveUi(paramParcel1.readInt(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramInt1 = isServiceEnabled(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramInt1 = isServiceSupported(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          disableOwnedAutofillServices(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          setHasCallback(paramInt2, paramInt1, bool1);
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localRect;
          }
          setAuthenticationResult((Bundle)localObject2, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          cancelSession(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          finishSession(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          setAutofillFailure(paramParcel1.readInt(), paramParcel1.createTypedArrayList(AutofillId.CREATOR), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          localIBinder1 = paramParcel1.readStrongBinder();
          localIBinder2 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (AutofillId)AutofillId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localRect = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localRect = null;
          }
          if (paramParcel1.readInt() != 0) {
            localAutofillValue = (AutofillValue)AutofillValue.CREATOR.createFromParcel(paramParcel1);
          } else {
            localAutofillValue = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          int i = paramParcel1.readInt();
          int j = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          paramInt1 = updateOrRestartSession(localIBinder1, localIBinder2, (AutofillId)localObject2, localRect, localAutofillValue, paramInt1, bool1, paramInt2, (ComponentName)localObject1, i, j, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (AutofillId)AutofillId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localRect = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localRect = null;
          }
          if (paramParcel1.readInt() != 0) {
            localAutofillValue = (AutofillValue)AutofillValue.CREATOR.createFromParcel(paramParcel1);
          } else {
            localAutofillValue = null;
          }
          updateSession(paramInt1, (AutofillId)localObject2, localRect, localAutofillValue, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramInt1 = restoreSession(paramParcel1.readInt(), paramParcel1.readStrongBinder(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          paramParcel1 = getFillEventHistory();
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
        case 3: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          localIBinder2 = paramParcel1.readStrongBinder();
          IBinder localIBinder3 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            localObject2 = (AutofillId)AutofillId.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localRect = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            localRect = null;
          }
          if (paramParcel1.readInt() != 0) {
            localAutofillValue = (AutofillValue)AutofillValue.CREATOR.createFromParcel(paramParcel1);
          } else {
            localAutofillValue = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);; localObject1 = localIBinder1) {
            break;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          paramInt1 = startSession(localIBinder2, localIBinder3, (AutofillId)localObject2, localRect, localAutofillValue, paramInt1, bool1, paramInt2, (ComponentName)localObject1, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
          removeClient(IAutoFillManagerClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.view.autofill.IAutoFillManager");
        paramInt1 = addClient(IAutoFillManagerClient.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.view.autofill.IAutoFillManager");
      return true;
    }
    
    private static class Proxy
      implements IAutoFillManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int addClient(IAutoFillManagerClient paramIAutoFillManagerClient, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          if (paramIAutoFillManagerClient != null) {
            paramIAutoFillManagerClient = paramIAutoFillManagerClient.asBinder();
          } else {
            paramIAutoFillManagerClient = null;
          }
          localParcel1.writeStrongBinder(paramIAutoFillManagerClient);
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
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
      
      public void cancelSession(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableOwnedAutofillServices(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
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
      
      public void finishSession(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public ComponentName getAutofillServiceComponentName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getAvailableFieldClassificationAlgorithms()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDefaultFieldClassificationAlgorithm()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public FillEventHistory getFillEventHistory()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          FillEventHistory localFillEventHistory;
          if (localParcel2.readInt() != 0) {
            localFillEventHistory = (FillEventHistory)FillEventHistory.CREATOR.createFromParcel(localParcel2);
          } else {
            localFillEventHistory = null;
          }
          return localFillEventHistory;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.view.autofill.IAutoFillManager";
      }
      
      public UserData getUserData()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          UserData localUserData;
          if (localParcel2.readInt() != 0) {
            localUserData = (UserData)UserData.CREATOR.createFromParcel(localParcel2);
          } else {
            localUserData = null;
          }
          return localUserData;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getUserDataId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isFieldClassificationEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(20, localParcel1, localParcel2, 0);
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
      
      public boolean isServiceEnabled(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public boolean isServiceSupported(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public void onPendingSaveUi(int paramInt, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public void removeClient(IAutoFillManagerClient paramIAutoFillManagerClient, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          if (paramIAutoFillManagerClient != null) {
            paramIAutoFillManagerClient = paramIAutoFillManagerClient.asBinder();
          } else {
            paramIAutoFillManagerClient = null;
          }
          localParcel1.writeStrongBinder(paramIAutoFillManagerClient);
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean restoreSession(int paramInt, IBinder paramIBinder1, IBinder paramIBinder2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder1);
          localParcel1.writeStrongBinder(paramIBinder2);
          paramIBinder1 = mRemote;
          boolean bool = false;
          paramIBinder1.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public void setAuthenticationResult(Bundle paramBundle, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAutofillFailure(int paramInt1, List<AutofillId> paramList, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeTypedList(paramList);
          localParcel1.writeInt(paramInt2);
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
      
      public void setHasCallback(int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUserData(UserData paramUserData)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          if (paramUserData != null)
          {
            localParcel1.writeInt(1);
            paramUserData.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public int startSession(IBinder paramIBinder1, IBinder paramIBinder2, AutofillId paramAutofillId, Rect paramRect, AutofillValue paramAutofillValue, int paramInt1, boolean paramBoolean1, int paramInt2, ComponentName paramComponentName, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeStrongBinder(paramIBinder1);
          localParcel1.writeStrongBinder(paramIBinder2);
          if (paramAutofillId != null)
          {
            localParcel1.writeInt(1);
            paramAutofillId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramAutofillValue != null)
          {
            localParcel1.writeInt(1);
            paramAutofillValue.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramInt2);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public int updateOrRestartSession(IBinder paramIBinder1, IBinder paramIBinder2, AutofillId paramAutofillId, Rect paramRect, AutofillValue paramAutofillValue, int paramInt1, boolean paramBoolean1, int paramInt2, ComponentName paramComponentName, int paramInt3, int paramInt4, boolean paramBoolean2)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 13
        //   5: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 14
        //   10: aload 13
        //   12: ldc 32
        //   14: invokevirtual 36	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 13
        //   19: aload_1
        //   20: invokevirtual 45	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   23: aload 13
        //   25: aload_2
        //   26: invokevirtual 45	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
        //   29: aload_3
        //   30: ifnull +19 -> 49
        //   33: aload 13
        //   35: iconst_1
        //   36: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   39: aload_3
        //   40: aload 13
        //   42: iconst_0
        //   43: invokevirtual 147	android/view/autofill/AutofillId:writeToParcel	(Landroid/os/Parcel;I)V
        //   46: goto +9 -> 55
        //   49: aload 13
        //   51: iconst_0
        //   52: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   55: aload 4
        //   57: ifnull +20 -> 77
        //   60: aload 13
        //   62: iconst_1
        //   63: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   66: aload 4
        //   68: aload 13
        //   70: iconst_0
        //   71: invokevirtual 150	android/graphics/Rect:writeToParcel	(Landroid/os/Parcel;I)V
        //   74: goto +9 -> 83
        //   77: aload 13
        //   79: iconst_0
        //   80: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   83: aload 5
        //   85: ifnull +20 -> 105
        //   88: aload 13
        //   90: iconst_1
        //   91: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   94: aload 5
        //   96: aload 13
        //   98: iconst_0
        //   99: invokevirtual 153	android/view/autofill/AutofillValue:writeToParcel	(Landroid/os/Parcel;I)V
        //   102: goto +9 -> 111
        //   105: aload 13
        //   107: iconst_0
        //   108: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   111: aload 13
        //   113: iload 6
        //   115: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   118: aload 13
        //   120: iload 7
        //   122: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   125: aload 13
        //   127: iload 8
        //   129: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   132: aload 9
        //   134: ifnull +20 -> 154
        //   137: aload 13
        //   139: iconst_1
        //   140: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   143: aload 9
        //   145: aload 13
        //   147: iconst_0
        //   148: invokevirtual 154	android/content/ComponentName:writeToParcel	(Landroid/os/Parcel;I)V
        //   151: goto +9 -> 160
        //   154: aload 13
        //   156: iconst_0
        //   157: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   160: aload 13
        //   162: iload 10
        //   164: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   167: aload 13
        //   169: iload 11
        //   171: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   174: aload 13
        //   176: iload 12
        //   178: invokevirtual 49	android/os/Parcel:writeInt	(I)V
        //   181: aload_0
        //   182: getfield 19	android/view/autofill/IAutoFillManager$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   185: bipush 7
        //   187: aload 13
        //   189: aload 14
        //   191: iconst_0
        //   192: invokeinterface 55 5 0
        //   197: pop
        //   198: aload 14
        //   200: invokevirtual 58	android/os/Parcel:readException	()V
        //   203: aload 14
        //   205: invokevirtual 62	android/os/Parcel:readInt	()I
        //   208: istore 6
        //   210: aload 14
        //   212: invokevirtual 65	android/os/Parcel:recycle	()V
        //   215: aload 13
        //   217: invokevirtual 65	android/os/Parcel:recycle	()V
        //   220: iload 6
        //   222: ireturn
        //   223: astore_1
        //   224: goto +36 -> 260
        //   227: astore_1
        //   228: goto +32 -> 260
        //   231: astore_1
        //   232: goto +28 -> 260
        //   235: astore_1
        //   236: goto +24 -> 260
        //   239: astore_1
        //   240: goto +20 -> 260
        //   243: astore_1
        //   244: goto +16 -> 260
        //   247: astore_1
        //   248: goto +12 -> 260
        //   251: astore_1
        //   252: goto +8 -> 260
        //   255: astore_1
        //   256: goto +4 -> 260
        //   259: astore_1
        //   260: aload 14
        //   262: invokevirtual 65	android/os/Parcel:recycle	()V
        //   265: aload 13
        //   267: invokevirtual 65	android/os/Parcel:recycle	()V
        //   270: aload_1
        //   271: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	272	0	this	Proxy
        //   0	272	1	paramIBinder1	IBinder
        //   0	272	2	paramIBinder2	IBinder
        //   0	272	3	paramAutofillId	AutofillId
        //   0	272	4	paramRect	Rect
        //   0	272	5	paramAutofillValue	AutofillValue
        //   0	272	6	paramInt1	int
        //   0	272	7	paramBoolean1	boolean
        //   0	272	8	paramInt2	int
        //   0	272	9	paramComponentName	ComponentName
        //   0	272	10	paramInt3	int
        //   0	272	11	paramInt4	int
        //   0	272	12	paramBoolean2	boolean
        //   3	263	13	localParcel1	Parcel
        //   8	253	14	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   181	210	223	finally
        //   174	181	227	finally
        //   167	174	231	finally
        //   160	167	235	finally
        //   125	132	239	finally
        //   137	151	239	finally
        //   154	160	239	finally
        //   118	125	243	finally
        //   111	118	247	finally
        //   23	29	251	finally
        //   33	46	251	finally
        //   49	55	251	finally
        //   60	74	251	finally
        //   77	83	251	finally
        //   88	102	251	finally
        //   105	111	251	finally
        //   17	23	255	finally
        //   10	17	259	finally
      }
      
      public void updateSession(int paramInt1, AutofillId paramAutofillId, Rect paramRect, AutofillValue paramAutofillValue, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.autofill.IAutoFillManager");
          localParcel1.writeInt(paramInt1);
          if (paramAutofillId != null)
          {
            localParcel1.writeInt(1);
            paramAutofillId.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramAutofillValue != null)
          {
            localParcel1.writeInt(1);
            paramAutofillValue.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
