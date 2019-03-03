package android.view.accessibility;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface IAccessibilityInteractionConnectionCallback
  extends IInterface
{
  public abstract void setFindAccessibilityNodeInfoResult(AccessibilityNodeInfo paramAccessibilityNodeInfo, int paramInt)
    throws RemoteException;
  
  public abstract void setFindAccessibilityNodeInfosResult(List<AccessibilityNodeInfo> paramList, int paramInt)
    throws RemoteException;
  
  public abstract void setPerformAccessibilityActionResult(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAccessibilityInteractionConnectionCallback
  {
    private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityInteractionConnectionCallback";
    static final int TRANSACTION_setFindAccessibilityNodeInfoResult = 1;
    static final int TRANSACTION_setFindAccessibilityNodeInfosResult = 2;
    static final int TRANSACTION_setPerformAccessibilityActionResult = 3;
    
    public Stub()
    {
      attachInterface(this, "android.view.accessibility.IAccessibilityInteractionConnectionCallback");
    }
    
    public static IAccessibilityInteractionConnectionCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.accessibility.IAccessibilityInteractionConnectionCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IAccessibilityInteractionConnectionCallback))) {
        return (IAccessibilityInteractionConnectionCallback)localIInterface;
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
        case 3: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityInteractionConnectionCallback");
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          setPerformAccessibilityActionResult(bool, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityInteractionConnectionCallback");
          setFindAccessibilityNodeInfosResult(paramParcel1.createTypedArrayList(AccessibilityNodeInfo.CREATOR), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.view.accessibility.IAccessibilityInteractionConnectionCallback");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (AccessibilityNodeInfo)AccessibilityNodeInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = null;
        }
        setFindAccessibilityNodeInfoResult(paramParcel2, paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.view.accessibility.IAccessibilityInteractionConnectionCallback");
      return true;
    }
    
    private static class Proxy
      implements IAccessibilityInteractionConnectionCallback
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
        return "android.view.accessibility.IAccessibilityInteractionConnectionCallback";
      }
      
      public void setFindAccessibilityNodeInfoResult(AccessibilityNodeInfo paramAccessibilityNodeInfo, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.accessibility.IAccessibilityInteractionConnectionCallback");
          if (paramAccessibilityNodeInfo != null)
          {
            localParcel.writeInt(1);
            paramAccessibilityNodeInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setFindAccessibilityNodeInfosResult(List<AccessibilityNodeInfo> paramList, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.accessibility.IAccessibilityInteractionConnectionCallback");
          localParcel.writeTypedList(paramList);
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setPerformAccessibilityActionResult(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.accessibility.IAccessibilityInteractionConnectionCallback");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
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
