package android.service.autofill;

import android.content.IntentSender;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;

public final class SaveCallback
{
  private final ISaveCallback mCallback;
  private boolean mCalled;
  
  SaveCallback(ISaveCallback paramISaveCallback)
  {
    mCallback = paramISaveCallback;
  }
  
  private void assertNotCalled()
  {
    if (!mCalled) {
      return;
    }
    throw new IllegalStateException("Already called");
  }
  
  private void onSuccessInternal(IntentSender paramIntentSender)
  {
    assertNotCalled();
    mCalled = true;
    try
    {
      mCallback.onSuccess(paramIntentSender);
    }
    catch (RemoteException paramIntentSender)
    {
      paramIntentSender.rethrowAsRuntimeException();
    }
  }
  
  public void onFailure(CharSequence paramCharSequence)
  {
    assertNotCalled();
    mCalled = true;
    try
    {
      mCallback.onFailure(paramCharSequence);
    }
    catch (RemoteException paramCharSequence)
    {
      paramCharSequence.rethrowAsRuntimeException();
    }
  }
  
  public void onSuccess()
  {
    onSuccessInternal(null);
  }
  
  public void onSuccess(IntentSender paramIntentSender)
  {
    onSuccessInternal((IntentSender)Preconditions.checkNotNull(paramIntentSender));
  }
}
