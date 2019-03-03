package android.service.autofill;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.view.autofill.AutofillValue;
import java.util.List;

public abstract interface IAutofillFieldClassificationService
  extends IInterface
{
  public abstract void getScores(RemoteCallback paramRemoteCallback, String paramString, Bundle paramBundle, List<AutofillValue> paramList, String[] paramArrayOfString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAutofillFieldClassificationService
  {
    private static final String DESCRIPTOR = "android.service.autofill.IAutofillFieldClassificationService";
    static final int TRANSACTION_getScores = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.autofill.IAutofillFieldClassificationService");
    }
    
    public static IAutofillFieldClassificationService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.autofill.IAutofillFieldClassificationService");
      if ((localIInterface != null) && ((localIInterface instanceof IAutofillFieldClassificationService))) {
        return (IAutofillFieldClassificationService)localIInterface;
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
        paramParcel2.writeString("android.service.autofill.IAutofillFieldClassificationService");
        return true;
      }
      paramParcel1.enforceInterface("android.service.autofill.IAutofillFieldClassificationService");
      paramInt1 = paramParcel1.readInt();
      Bundle localBundle = null;
      if (paramInt1 != 0) {
        paramParcel2 = (RemoteCallback)RemoteCallback.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      String str = paramParcel1.readString();
      if (paramParcel1.readInt() != 0) {
        localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
      }
      for (;;)
      {
        break;
      }
      getScores(paramParcel2, str, localBundle, paramParcel1.createTypedArrayList(AutofillValue.CREATOR), paramParcel1.createStringArray());
      return true;
    }
    
    private static class Proxy
      implements IAutofillFieldClassificationService
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
        return "android.service.autofill.IAutofillFieldClassificationService";
      }
      
      public void getScores(RemoteCallback paramRemoteCallback, String paramString, Bundle paramBundle, List<AutofillValue> paramList, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.autofill.IAutofillFieldClassificationService");
          if (paramRemoteCallback != null)
          {
            localParcel.writeInt(1);
            paramRemoteCallback.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
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
          localParcel.writeTypedList(paramList);
          localParcel.writeStringArray(paramArrayOfString);
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
