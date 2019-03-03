package com.android.internal.textservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SuggestionsInfo;

public abstract interface ISpellCheckerSessionListener
  extends IInterface
{
  public abstract void onGetSentenceSuggestions(SentenceSuggestionsInfo[] paramArrayOfSentenceSuggestionsInfo)
    throws RemoteException;
  
  public abstract void onGetSuggestions(SuggestionsInfo[] paramArrayOfSuggestionsInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ISpellCheckerSessionListener
  {
    private static final String DESCRIPTOR = "com.android.internal.textservice.ISpellCheckerSessionListener";
    static final int TRANSACTION_onGetSentenceSuggestions = 2;
    static final int TRANSACTION_onGetSuggestions = 1;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.textservice.ISpellCheckerSessionListener");
    }
    
    public static ISpellCheckerSessionListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.textservice.ISpellCheckerSessionListener");
      if ((localIInterface != null) && ((localIInterface instanceof ISpellCheckerSessionListener))) {
        return (ISpellCheckerSessionListener)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.textservice.ISpellCheckerSessionListener");
          onGetSentenceSuggestions((SentenceSuggestionsInfo[])paramParcel1.createTypedArray(SentenceSuggestionsInfo.CREATOR));
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.textservice.ISpellCheckerSessionListener");
        onGetSuggestions((SuggestionsInfo[])paramParcel1.createTypedArray(SuggestionsInfo.CREATOR));
        return true;
      }
      paramParcel2.writeString("com.android.internal.textservice.ISpellCheckerSessionListener");
      return true;
    }
    
    private static class Proxy
      implements ISpellCheckerSessionListener
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
        return "com.android.internal.textservice.ISpellCheckerSessionListener";
      }
      
      public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] paramArrayOfSentenceSuggestionsInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ISpellCheckerSessionListener");
          localParcel.writeTypedArray(paramArrayOfSentenceSuggestionsInfo, 0);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onGetSuggestions(SuggestionsInfo[] paramArrayOfSuggestionsInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.textservice.ISpellCheckerSessionListener");
          localParcel.writeTypedArray(paramArrayOfSuggestionsInfo, 0);
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
