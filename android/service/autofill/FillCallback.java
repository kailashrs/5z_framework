package android.service.autofill;

import android.os.RemoteException;

public final class FillCallback
{
  private final IFillCallback mCallback;
  private boolean mCalled;
  private final int mRequestId;
  
  public FillCallback(IFillCallback paramIFillCallback, int paramInt)
  {
    mCallback = paramIFillCallback;
    mRequestId = paramInt;
  }
  
  private void assertNotCalled()
  {
    if (!mCalled) {
      return;
    }
    throw new IllegalStateException("Already called");
  }
  
  public void onFailure(CharSequence paramCharSequence)
  {
    assertNotCalled();
    mCalled = true;
    try
    {
      mCallback.onFailure(mRequestId, paramCharSequence);
    }
    catch (RemoteException paramCharSequence)
    {
      paramCharSequence.rethrowAsRuntimeException();
    }
  }
  
  public void onSuccess(FillResponse paramFillResponse)
  {
    assertNotCalled();
    mCalled = true;
    if (paramFillResponse != null) {
      paramFillResponse.setRequestId(mRequestId);
    }
    try
    {
      mCallback.onSuccess(paramFillResponse);
    }
    catch (RemoteException paramFillResponse)
    {
      paramFillResponse.rethrowAsRuntimeException();
    }
  }
}
