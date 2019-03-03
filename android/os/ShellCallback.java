package android.os;

import android.util.Log;
import com.android.internal.os.IShellCallback;
import com.android.internal.os.IShellCallback.Stub;

public class ShellCallback
  implements Parcelable
{
  public static final Parcelable.Creator<ShellCallback> CREATOR = new Parcelable.Creator()
  {
    public ShellCallback createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ShellCallback(paramAnonymousParcel);
    }
    
    public ShellCallback[] newArray(int paramAnonymousInt)
    {
      return new ShellCallback[paramAnonymousInt];
    }
  };
  static final boolean DEBUG = false;
  static final String TAG = "ShellCallback";
  final boolean mLocal;
  IShellCallback mShellCallback;
  
  public ShellCallback()
  {
    mLocal = true;
  }
  
  ShellCallback(Parcel paramParcel)
  {
    mLocal = false;
    mShellCallback = IShellCallback.Stub.asInterface(paramParcel.readStrongBinder());
    if (mShellCallback != null) {
      Binder.allowBlocking(mShellCallback.asBinder());
    }
  }
  
  public static void writeToParcel(ShellCallback paramShellCallback, Parcel paramParcel)
  {
    if (paramShellCallback == null) {
      paramParcel.writeStrongBinder(null);
    } else {
      paramShellCallback.writeToParcel(paramParcel, 0);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ParcelFileDescriptor onOpenFile(String paramString1, String paramString2, String paramString3)
  {
    return null;
  }
  
  public ParcelFileDescriptor openFile(String paramString1, String paramString2, String paramString3)
  {
    if (mLocal) {
      return onOpenFile(paramString1, paramString2, paramString3);
    }
    if (mShellCallback != null) {
      try
      {
        paramString2 = mShellCallback.openFile(paramString1, paramString2, paramString3);
        return paramString2;
      }
      catch (RemoteException paramString3)
      {
        paramString2 = new StringBuilder();
        paramString2.append("Failure opening ");
        paramString2.append(paramString1);
        Log.w("ShellCallback", paramString2.toString(), paramString3);
      }
    }
    return null;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    try
    {
      if (mShellCallback == null)
      {
        MyShellCallback localMyShellCallback = new android/os/ShellCallback$MyShellCallback;
        localMyShellCallback.<init>(this);
        mShellCallback = localMyShellCallback;
      }
      paramParcel.writeStrongBinder(mShellCallback.asBinder());
      return;
    }
    finally {}
  }
  
  class MyShellCallback
    extends IShellCallback.Stub
  {
    MyShellCallback() {}
    
    public ParcelFileDescriptor openFile(String paramString1, String paramString2, String paramString3)
    {
      return onOpenFile(paramString1, paramString2, paramString3);
    }
  }
}
