package android.telecom;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.ICallScreeningAdapter;
import com.android.internal.telecom.ICallScreeningService.Stub;

public abstract class CallScreeningService
  extends Service
{
  private static final int MSG_SCREEN_CALL = 1;
  public static final String SERVICE_INTERFACE = "android.telecom.CallScreeningService";
  private ICallScreeningAdapter mCallScreeningAdapter;
  private final Handler mHandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (what == 1) {
        paramAnonymousMessage = (SomeArgs)obj;
      }
      try
      {
        CallScreeningService.access$002(CallScreeningService.this, (ICallScreeningAdapter)arg1);
        onScreenCall(Call.Details.createFromParcelableCall((ParcelableCall)arg2));
        return;
      }
      finally
      {
        paramAnonymousMessage.recycle();
      }
    }
  };
  
  public CallScreeningService() {}
  
  public IBinder onBind(Intent paramIntent)
  {
    Log.v(this, "onBind", new Object[0]);
    return new CallScreeningBinder(null);
  }
  
  public abstract void onScreenCall(Call.Details paramDetails);
  
  public boolean onUnbind(Intent paramIntent)
  {
    Log.v(this, "onUnbind", new Object[0]);
    return false;
  }
  
  public final void respondToCall(Call.Details paramDetails, CallResponse paramCallResponse)
  {
    try
    {
      if (paramCallResponse.getDisallowCall()) {
        mCallScreeningAdapter.disallowCall(paramDetails.getTelecomCallId(), paramCallResponse.getRejectCall(), paramCallResponse.getSkipCallLog() ^ true, paramCallResponse.getSkipNotification() ^ true);
      } else {
        mCallScreeningAdapter.allowCall(paramDetails.getTelecomCallId());
      }
    }
    catch (RemoteException paramDetails) {}
  }
  
  public static class CallResponse
  {
    private final boolean mShouldDisallowCall;
    private final boolean mShouldRejectCall;
    private final boolean mShouldSkipCallLog;
    private final boolean mShouldSkipNotification;
    
    private CallResponse(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
    {
      if ((!paramBoolean1) && ((paramBoolean2) || (paramBoolean3) || (paramBoolean4))) {
        throw new IllegalStateException("Invalid response state for allowed call.");
      }
      mShouldDisallowCall = paramBoolean1;
      mShouldRejectCall = paramBoolean2;
      mShouldSkipCallLog = paramBoolean3;
      mShouldSkipNotification = paramBoolean4;
    }
    
    public boolean getDisallowCall()
    {
      return mShouldDisallowCall;
    }
    
    public boolean getRejectCall()
    {
      return mShouldRejectCall;
    }
    
    public boolean getSkipCallLog()
    {
      return mShouldSkipCallLog;
    }
    
    public boolean getSkipNotification()
    {
      return mShouldSkipNotification;
    }
    
    public static class Builder
    {
      private boolean mShouldDisallowCall;
      private boolean mShouldRejectCall;
      private boolean mShouldSkipCallLog;
      private boolean mShouldSkipNotification;
      
      public Builder() {}
      
      public CallScreeningService.CallResponse build()
      {
        return new CallScreeningService.CallResponse(mShouldDisallowCall, mShouldRejectCall, mShouldSkipCallLog, mShouldSkipNotification, null);
      }
      
      public Builder setDisallowCall(boolean paramBoolean)
      {
        mShouldDisallowCall = paramBoolean;
        return this;
      }
      
      public Builder setRejectCall(boolean paramBoolean)
      {
        mShouldRejectCall = paramBoolean;
        return this;
      }
      
      public Builder setSkipCallLog(boolean paramBoolean)
      {
        mShouldSkipCallLog = paramBoolean;
        return this;
      }
      
      public Builder setSkipNotification(boolean paramBoolean)
      {
        mShouldSkipNotification = paramBoolean;
        return this;
      }
    }
  }
  
  private final class CallScreeningBinder
    extends ICallScreeningService.Stub
  {
    private CallScreeningBinder() {}
    
    public void screenCall(ICallScreeningAdapter paramICallScreeningAdapter, ParcelableCall paramParcelableCall)
    {
      Log.v(this, "screenCall", new Object[0]);
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramICallScreeningAdapter;
      arg2 = paramParcelableCall;
      mHandler.obtainMessage(1, localSomeArgs).sendToTarget();
    }
  }
}
