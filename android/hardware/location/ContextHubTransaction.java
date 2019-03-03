package android.hardware.location;

import android.annotation.SystemApi;
import android.os.Handler;
import android.os.HandlerExecutor;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SystemApi
public class ContextHubTransaction<T>
{
  public static final int RESULT_FAILED_AT_HUB = 5;
  public static final int RESULT_FAILED_BAD_PARAMS = 2;
  public static final int RESULT_FAILED_BUSY = 4;
  public static final int RESULT_FAILED_HAL_UNAVAILABLE = 8;
  public static final int RESULT_FAILED_SERVICE_INTERNAL_FAILURE = 7;
  public static final int RESULT_FAILED_TIMEOUT = 6;
  public static final int RESULT_FAILED_UNINITIALIZED = 3;
  public static final int RESULT_FAILED_UNKNOWN = 1;
  public static final int RESULT_SUCCESS = 0;
  private static final String TAG = "ContextHubTransaction";
  public static final int TYPE_DISABLE_NANOAPP = 3;
  public static final int TYPE_ENABLE_NANOAPP = 2;
  public static final int TYPE_LOAD_NANOAPP = 0;
  public static final int TYPE_QUERY_NANOAPPS = 4;
  public static final int TYPE_UNLOAD_NANOAPP = 1;
  private final CountDownLatch mDoneSignal = new CountDownLatch(1);
  private Executor mExecutor = null;
  private boolean mIsResponseSet = false;
  private OnCompleteListener<T> mListener = null;
  private Response<T> mResponse;
  private int mTransactionType;
  
  ContextHubTransaction(int paramInt)
  {
    mTransactionType = paramInt;
  }
  
  public static String typeToString(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    default: 
      if (paramBoolean) {
        str = "Unknown";
      }
      break;
    case 4: 
      if (paramBoolean) {
        str = "Query";
      } else {
        str = "query";
      }
      return str;
    case 3: 
      if (paramBoolean) {
        str = "Disable";
      } else {
        str = "disable";
      }
      return str;
    case 2: 
      if (paramBoolean) {
        str = "Enable";
      } else {
        str = "enable";
      }
      return str;
    case 1: 
      if (paramBoolean) {
        str = "Unload";
      } else {
        str = "unload";
      }
      return str;
    case 0: 
      if (paramBoolean) {
        str = "Load";
      } else {
        str = "load";
      }
      return str;
    }
    String str = "unknown";
    return str;
  }
  
  public int getType()
  {
    return mTransactionType;
  }
  
  public void setOnCompleteListener(OnCompleteListener<T> paramOnCompleteListener)
  {
    setOnCompleteListener(paramOnCompleteListener, new HandlerExecutor(Handler.getMain()));
  }
  
  public void setOnCompleteListener(OnCompleteListener<T> paramOnCompleteListener, Executor paramExecutor)
  {
    try
    {
      Preconditions.checkNotNull(paramOnCompleteListener, "OnCompleteListener cannot be null");
      Preconditions.checkNotNull(paramExecutor, "Executor cannot be null");
      if (mListener == null)
      {
        mListener = paramOnCompleteListener;
        mExecutor = paramExecutor;
        if (mDoneSignal.getCount() == 0L)
        {
          paramOnCompleteListener = mExecutor;
          paramExecutor = new android/hardware/location/_$$Lambda$ContextHubTransaction$7a5H6DrY_dOy9M3qnYHhlmDHRNQ;
          paramExecutor.<init>(this);
          paramOnCompleteListener.execute(paramExecutor);
        }
        return;
      }
      paramOnCompleteListener = new java/lang/IllegalStateException;
      paramOnCompleteListener.<init>("Cannot set ContextHubTransaction listener multiple times");
      throw paramOnCompleteListener;
    }
    finally {}
  }
  
  void setResponse(Response<T> paramResponse)
  {
    try
    {
      Preconditions.checkNotNull(paramResponse, "Response cannot be null");
      if (!mIsResponseSet)
      {
        mResponse = paramResponse;
        mIsResponseSet = true;
        mDoneSignal.countDown();
        if (mListener != null)
        {
          paramResponse = mExecutor;
          _..Lambda.ContextHubTransaction.RNVGnle3xCUm9u68syzn6_2znnU localRNVGnle3xCUm9u68syzn6_2znnU = new android/hardware/location/_$$Lambda$ContextHubTransaction$RNVGnle3xCUm9u68syzn6_2znnU;
          localRNVGnle3xCUm9u68syzn6_2znnU.<init>(this);
          paramResponse.execute(localRNVGnle3xCUm9u68syzn6_2znnU);
        }
        return;
      }
      paramResponse = new java/lang/IllegalStateException;
      paramResponse.<init>("Cannot set response of ContextHubTransaction multiple times");
      throw paramResponse;
    }
    finally {}
  }
  
  public Response<T> waitForResponse(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, TimeoutException
  {
    if (mDoneSignal.await(paramLong, paramTimeUnit)) {
      return mResponse;
    }
    throw new TimeoutException("Timed out while waiting for transaction");
  }
  
  @FunctionalInterface
  public static abstract interface OnCompleteListener<L>
  {
    public abstract void onComplete(ContextHubTransaction<L> paramContextHubTransaction, ContextHubTransaction.Response<L> paramResponse);
  }
  
  public static class Response<R>
  {
    private R mContents;
    private int mResult;
    
    Response(int paramInt, R paramR)
    {
      mResult = paramInt;
      mContents = paramR;
    }
    
    public R getContents()
    {
      return mContents;
    }
    
    public int getResult()
    {
      return mResult;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Result {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Type {}
}
