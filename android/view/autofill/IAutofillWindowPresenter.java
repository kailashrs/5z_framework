package android.view.autofill;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.WindowManager.LayoutParams;

public abstract interface IAutofillWindowPresenter
  extends IInterface
{
  public abstract void hide(Rect paramRect)
    throws RemoteException;
  
  public abstract void show(WindowManager.LayoutParams paramLayoutParams, Rect paramRect, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAutofillWindowPresenter
  {
    private static final String DESCRIPTOR = "android.view.autofill.IAutofillWindowPresenter";
    static final int TRANSACTION_hide = 2;
    static final int TRANSACTION_show = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.autofill.IAutofillWindowPresenter");
    }
    
    public static IAutofillWindowPresenter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.autofill.IAutofillWindowPresenter");
      if ((localIInterface != null) && ((localIInterface instanceof IAutofillWindowPresenter))) {
        return (IAutofillWindowPresenter)localIInterface;
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
        Rect localRect = null;
        Object localObject = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.view.autofill.IAutofillWindowPresenter");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject;
          }
          hide(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.view.autofill.IAutofillWindowPresenter");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (WindowManager.LayoutParams)WindowManager.LayoutParams.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        if (paramParcel1.readInt() != 0) {
          localRect = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
        }
        boolean bool;
        if (paramParcel1.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        show(paramParcel2, localRect, bool, paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.view.autofill.IAutofillWindowPresenter");
      return true;
    }
    
    private static class Proxy
      implements IAutofillWindowPresenter
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
        return "android.view.autofill.IAutofillWindowPresenter";
      }
      
      public void hide(Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutofillWindowPresenter");
          if (paramRect != null)
          {
            localParcel.writeInt(1);
            paramRect.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void show(WindowManager.LayoutParams paramLayoutParams, Rect paramRect, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.autofill.IAutofillWindowPresenter");
          if (paramLayoutParams != null)
          {
            localParcel.writeInt(1);
            paramLayoutParams.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramRect != null)
          {
            localParcel.writeInt(1);
            paramRect.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
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
