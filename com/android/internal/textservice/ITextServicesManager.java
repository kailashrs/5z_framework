package com.android.internal.textservice;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.textservice.SpellCheckerInfo;
import android.view.textservice.SpellCheckerSubtype;

public abstract interface ITextServicesManager
  extends IInterface
{
  public abstract void finishSpellCheckerService(ISpellCheckerSessionListener paramISpellCheckerSessionListener)
    throws RemoteException;
  
  public abstract SpellCheckerInfo getCurrentSpellChecker(String paramString)
    throws RemoteException;
  
  public abstract SpellCheckerSubtype getCurrentSpellCheckerSubtype(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract SpellCheckerInfo[] getEnabledSpellCheckers()
    throws RemoteException;
  
  public abstract void getSpellCheckerService(String paramString1, String paramString2, ITextServicesSessionListener paramITextServicesSessionListener, ISpellCheckerSessionListener paramISpellCheckerSessionListener, Bundle paramBundle)
    throws RemoteException;
  
  public abstract boolean isSpellCheckerEnabled()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITextServicesManager
  {
    private static final String DESCRIPTOR = "com.android.internal.textservice.ITextServicesManager";
    static final int TRANSACTION_finishSpellCheckerService = 4;
    static final int TRANSACTION_getCurrentSpellChecker = 1;
    static final int TRANSACTION_getCurrentSpellCheckerSubtype = 2;
    static final int TRANSACTION_getEnabledSpellCheckers = 6;
    static final int TRANSACTION_getSpellCheckerService = 3;
    static final int TRANSACTION_isSpellCheckerEnabled = 5;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.textservice.ITextServicesManager");
    }
    
    public static ITextServicesManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.textservice.ITextServicesManager");
      if ((localIInterface != null) && ((localIInterface instanceof ITextServicesManager))) {
        return (ITextServicesManager)localIInterface;
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
        String str1;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.textservice.ITextServicesManager");
          paramParcel1 = getEnabledSpellCheckers();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.textservice.ITextServicesManager");
          paramInt1 = isSpellCheckerEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.textservice.ITextServicesManager");
          finishSpellCheckerService(ISpellCheckerSessionListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.textservice.ITextServicesManager");
          str1 = paramParcel1.readString();
          String str2 = paramParcel1.readString();
          ITextServicesSessionListener localITextServicesSessionListener = ITextServicesSessionListener.Stub.asInterface(paramParcel1.readStrongBinder());
          paramParcel2 = ISpellCheckerSessionListener.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = null) {
            break;
          }
          getSpellCheckerService(str1, str2, localITextServicesSessionListener, paramParcel2, paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.textservice.ITextServicesManager");
          str1 = paramParcel1.readString();
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          paramParcel1 = getCurrentSpellCheckerSubtype(str1, bool);
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
        }
        paramParcel1.enforceInterface("com.android.internal.textservice.ITextServicesManager");
        paramParcel1 = getCurrentSpellChecker(paramParcel1.readString());
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
      }
      paramParcel2.writeString("com.android.internal.textservice.ITextServicesManager");
      return true;
    }
    
    private static class Proxy
      implements ITextServicesManager
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
      
      public void finishSpellCheckerService(ISpellCheckerSessionListener paramISpellCheckerSessionListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ITextServicesManager");
          if (paramISpellCheckerSessionListener != null) {
            paramISpellCheckerSessionListener = paramISpellCheckerSessionListener.asBinder();
          } else {
            paramISpellCheckerSessionListener = null;
          }
          localParcel.writeStrongBinder(paramISpellCheckerSessionListener);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public SpellCheckerInfo getCurrentSpellChecker(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.textservice.ITextServicesManager");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (SpellCheckerInfo)SpellCheckerInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SpellCheckerSubtype getCurrentSpellCheckerSubtype(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.textservice.ITextServicesManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (SpellCheckerSubtype)SpellCheckerSubtype.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SpellCheckerInfo[] getEnabledSpellCheckers()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.textservice.ITextServicesManager");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          SpellCheckerInfo[] arrayOfSpellCheckerInfo = (SpellCheckerInfo[])localParcel2.createTypedArray(SpellCheckerInfo.CREATOR);
          return arrayOfSpellCheckerInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.textservice.ITextServicesManager";
      }
      
      public void getSpellCheckerService(String paramString1, String paramString2, ITextServicesSessionListener paramITextServicesSessionListener, ISpellCheckerSessionListener paramISpellCheckerSessionListener, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ITextServicesManager");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          if (paramITextServicesSessionListener != null) {
            paramString1 = paramITextServicesSessionListener.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          if (paramISpellCheckerSessionListener != null) {
            paramString1 = paramISpellCheckerSessionListener.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public boolean isSpellCheckerEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.textservice.ITextServicesManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(5, localParcel1, localParcel2, 0);
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
    }
  }
}
