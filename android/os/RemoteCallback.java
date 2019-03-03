package android.os;

import android.annotation.SystemApi;

@SystemApi
public final class RemoteCallback
  implements Parcelable
{
  public static final Parcelable.Creator<RemoteCallback> CREATOR = new Parcelable.Creator()
  {
    public RemoteCallback createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RemoteCallback(paramAnonymousParcel);
    }
    
    public RemoteCallback[] newArray(int paramAnonymousInt)
    {
      return new RemoteCallback[paramAnonymousInt];
    }
  };
  private final IRemoteCallback mCallback;
  private final Handler mHandler;
  private final OnResultListener mListener;
  
  RemoteCallback(Parcel paramParcel)
  {
    mListener = null;
    mHandler = null;
    mCallback = IRemoteCallback.Stub.asInterface(paramParcel.readStrongBinder());
  }
  
  public RemoteCallback(OnResultListener paramOnResultListener)
  {
    this(paramOnResultListener, null);
  }
  
  public RemoteCallback(OnResultListener paramOnResultListener, Handler paramHandler)
  {
    if (paramOnResultListener != null)
    {
      mListener = paramOnResultListener;
      mHandler = paramHandler;
      mCallback = new IRemoteCallback.Stub()
      {
        public void sendResult(Bundle paramAnonymousBundle)
        {
          RemoteCallback.this.sendResult(paramAnonymousBundle);
        }
      };
      return;
    }
    throw new NullPointerException("listener cannot be null");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void sendResult(final Bundle paramBundle)
  {
    if (mListener != null)
    {
      if (mHandler != null) {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            mListener.onResult(paramBundle);
          }
        });
      } else {
        mListener.onResult(paramBundle);
      }
    }
    else {
      try
      {
        mCallback.sendResult(paramBundle);
      }
      catch (RemoteException paramBundle) {}
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(mCallback.asBinder());
  }
  
  public static abstract interface OnResultListener
  {
    public abstract void onResult(Bundle paramBundle);
  }
}
