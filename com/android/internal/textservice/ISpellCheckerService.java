package com.android.internal.textservice;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ISpellCheckerService
  extends IInterface
{
  public abstract void getISpellCheckerSession(String paramString, ISpellCheckerSessionListener paramISpellCheckerSessionListener, Bundle paramBundle, ISpellCheckerServiceCallback paramISpellCheckerServiceCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISpellCheckerService
  {
    private static final String DESCRIPTOR = "com.android.internal.textservice.ISpellCheckerService";
    static final int TRANSACTION_getISpellCheckerSession = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.textservice.ISpellCheckerService");
    }
    
    public static ISpellCheckerService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.textservice.ISpellCheckerService");
      if ((localIInterface != null) && ((localIInterface instanceof ISpellCheckerService))) {
        return (ISpellCheckerService)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("com.android.internal.textservice.ISpellCheckerService");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.textservice.ISpellCheckerService");
      String str = paramParcel1.readString();
      ISpellCheckerSessionListener localISpellCheckerSessionListener = ISpellCheckerSessionListener.Stub.asInterface(paramParcel1.readStrongBinder());
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      getISpellCheckerSession(str, localISpellCheckerSessionListener, paramParcel2, ISpellCheckerServiceCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
      return true;
    }
    
    private static class Proxy
      implements ISpellCheckerService
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
      
      public void getISpellCheckerSession(String paramString, ISpellCheckerSessionListener paramISpellCheckerSessionListener, Bundle paramBundle, ISpellCheckerServiceCallback paramISpellCheckerServiceCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ISpellCheckerService");
          localParcel.writeString(paramString);
          if (paramISpellCheckerSessionListener != null) {
            paramString = paramISpellCheckerSessionListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramISpellCheckerServiceCallback != null) {
            paramString = paramISpellCheckerServiceCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel.writeStrongBinder(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.textservice.ISpellCheckerService";
      }
    }
  }
}
