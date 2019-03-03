package com.android.internal.textservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.textservice.TextInfo;

public abstract interface ISpellCheckerSession
  extends IInterface
{
  public abstract void onCancel()
    throws RemoteException;
  
  public abstract void onClose()
    throws RemoteException;
  
  public abstract void onGetSentenceSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt)
    throws RemoteException;
  
  public abstract void onGetSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISpellCheckerSession
  {
    private static final String DESCRIPTOR = "com.android.internal.textservice.ISpellCheckerSession";
    static final int TRANSACTION_onCancel = 3;
    static final int TRANSACTION_onClose = 4;
    static final int TRANSACTION_onGetSentenceSuggestionsMultiple = 2;
    static final int TRANSACTION_onGetSuggestionsMultiple = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.textservice.ISpellCheckerSession");
    }
    
    public static ISpellCheckerSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.textservice.ISpellCheckerSession");
      if ((localIInterface != null) && ((localIInterface instanceof ISpellCheckerSession))) {
        return (ISpellCheckerSession)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.textservice.ISpellCheckerSession");
          onClose();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.textservice.ISpellCheckerSession");
          onCancel();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.textservice.ISpellCheckerSession");
          onGetSentenceSuggestionsMultiple((TextInfo[])paramParcel1.createTypedArray(TextInfo.CREATOR), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.textservice.ISpellCheckerSession");
        paramParcel2 = (TextInfo[])paramParcel1.createTypedArray(TextInfo.CREATOR);
        paramInt1 = paramParcel1.readInt();
        boolean bool;
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        onGetSuggestionsMultiple(paramParcel2, paramInt1, bool);
        return true;
      }
      paramParcel2.writeString("com.android.internal.textservice.ISpellCheckerSession");
      return true;
    }
    
    private static class Proxy
      implements ISpellCheckerSession
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
        return "com.android.internal.textservice.ISpellCheckerSession";
      }
      
      public void onCancel()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ISpellCheckerSession");
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onClose()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ISpellCheckerSession");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetSentenceSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ISpellCheckerSession");
          localParcel.writeTypedArray(paramArrayOfTextInfo, 0);
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ISpellCheckerSession");
          localParcel.writeTypedArray(paramArrayOfTextInfo, 0);
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
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
