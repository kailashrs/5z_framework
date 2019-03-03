package com.android.internal.textservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface ITextServicesSessionListener
  extends IInterface
{
  public abstract void onServiceConnected(ISpellCheckerSession paramISpellCheckerSession)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITextServicesSessionListener
  {
    private static final String DESCRIPTOR = "com.android.internal.textservice.ITextServicesSessionListener";
    static final int TRANSACTION_onServiceConnected = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.textservice.ITextServicesSessionListener");
    }
    
    public static ITextServicesSessionListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.textservice.ITextServicesSessionListener");
      if ((localIInterface != null) && ((localIInterface instanceof ITextServicesSessionListener))) {
        return (ITextServicesSessionListener)localIInterface;
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
        paramParcel2.writeString("com.android.internal.textservice.ITextServicesSessionListener");
        return true;
      }
      paramParcel1.enforceInterface("com.android.internal.textservice.ITextServicesSessionListener");
      onServiceConnected(ISpellCheckerSession.Stub.asInterface(paramParcel1.readStrongBinder()));
      return true;
    }
    
    private static class Proxy
      implements ITextServicesSessionListener
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
        return "com.android.internal.textservice.ITextServicesSessionListener";
      }
      
      public void onServiceConnected(ISpellCheckerSession paramISpellCheckerSession)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ITextServicesSessionListener");
          if (paramISpellCheckerSession != null) {
            paramISpellCheckerSession = paramISpellCheckerSession.asBinder();
          } else {
            paramISpellCheckerSession = null;
          }
          localParcel.writeStrongBinder(paramISpellCheckerSession);
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
