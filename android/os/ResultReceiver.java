package android.os;

import com.android.internal.os.IResultReceiver;
import com.android.internal.os.IResultReceiver.Stub;

public class ResultReceiver
  implements Parcelable
{
  public static final Parcelable.Creator<ResultReceiver> CREATOR = new Parcelable.Creator()
  {
    public ResultReceiver createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ResultReceiver(paramAnonymousParcel);
    }
    
    public ResultReceiver[] newArray(int paramAnonymousInt)
    {
      return new ResultReceiver[paramAnonymousInt];
    }
  };
  final Handler mHandler;
  final boolean mLocal;
  IResultReceiver mReceiver;
  
  public ResultReceiver(Handler paramHandler)
  {
    mLocal = true;
    mHandler = paramHandler;
  }
  
  ResultReceiver(Parcel paramParcel)
  {
    mLocal = false;
    mHandler = null;
    mReceiver = IResultReceiver.Stub.asInterface(paramParcel.readStrongBinder());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  protected void onReceiveResult(int paramInt, Bundle paramBundle) {}
  
  public void send(int paramInt, Bundle paramBundle)
  {
    if (mLocal)
    {
      if (mHandler != null) {
        mHandler.post(new MyRunnable(paramInt, paramBundle));
      } else {
        onReceiveResult(paramInt, paramBundle);
      }
      return;
    }
    if (mReceiver != null) {
      try
      {
        mReceiver.send(paramInt, paramBundle);
      }
      catch (RemoteException paramBundle) {}
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    try
    {
      if (mReceiver == null)
      {
        MyResultReceiver localMyResultReceiver = new android/os/ResultReceiver$MyResultReceiver;
        localMyResultReceiver.<init>(this);
        mReceiver = localMyResultReceiver;
      }
      paramParcel.writeStrongBinder(mReceiver.asBinder());
      return;
    }
    finally {}
  }
  
  class MyResultReceiver
    extends IResultReceiver.Stub
  {
    MyResultReceiver() {}
    
    public void send(int paramInt, Bundle paramBundle)
    {
      if (mHandler != null) {
        mHandler.post(new ResultReceiver.MyRunnable(ResultReceiver.this, paramInt, paramBundle));
      } else {
        onReceiveResult(paramInt, paramBundle);
      }
    }
  }
  
  class MyRunnable
    implements Runnable
  {
    final int mResultCode;
    final Bundle mResultData;
    
    MyRunnable(int paramInt, Bundle paramBundle)
    {
      mResultCode = paramInt;
      mResultData = paramBundle;
    }
    
    public void run()
    {
      onReceiveResult(mResultCode, mResultData);
    }
  }
}
