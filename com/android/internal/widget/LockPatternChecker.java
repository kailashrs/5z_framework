package com.android.internal.widget;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LockPatternChecker
{
  public LockPatternChecker() {}
  
  public static AsyncTask<?, ?, ?> checkPassword(LockPatternUtils paramLockPatternUtils, final String paramString, final int paramInt, final OnCheckCallback paramOnCheckCallback)
  {
    paramLockPatternUtils = new AsyncTask()
    {
      private int mThrottleTimeout;
      
      protected Boolean doInBackground(Void... paramAnonymousVarArgs)
      {
        try
        {
          LockPatternUtils localLockPatternUtils = LockPatternChecker.this;
          String str = paramString;
          int i = paramInt;
          LockPatternChecker.OnCheckCallback localOnCheckCallback = paramOnCheckCallback;
          Objects.requireNonNull(localOnCheckCallback);
          paramAnonymousVarArgs = new com/android/internal/widget/_$$Lambda$TTC7hNz7BTsLwhNRb2L5kl_7mdU;
          paramAnonymousVarArgs.<init>(localOnCheckCallback);
          boolean bool = localLockPatternUtils.checkPassword(str, i, paramAnonymousVarArgs);
          return Boolean.valueOf(bool);
        }
        catch (LockPatternUtils.RequestThrottledException paramAnonymousVarArgs)
        {
          mThrottleTimeout = paramAnonymousVarArgs.getTimeoutMs();
        }
        return Boolean.valueOf(false);
      }
      
      protected void onCancelled()
      {
        paramOnCheckCallback.onCancelled();
      }
      
      protected void onPostExecute(Boolean paramAnonymousBoolean)
      {
        paramOnCheckCallback.onChecked(paramAnonymousBoolean.booleanValue(), mThrottleTimeout);
      }
    };
    paramLockPatternUtils.execute(new Void[0]);
    return paramLockPatternUtils;
  }
  
  public static AsyncTask<?, ?, ?> checkPattern(final LockPatternUtils paramLockPatternUtils, List<LockPatternView.Cell> paramList, final int paramInt, final OnCheckCallback paramOnCheckCallback)
  {
    paramLockPatternUtils = new AsyncTask()
    {
      private int mThrottleTimeout;
      private List<LockPatternView.Cell> patternCopy;
      
      protected Boolean doInBackground(Void... paramAnonymousVarArgs)
      {
        try
        {
          LockPatternUtils localLockPatternUtils = paramLockPatternUtils;
          List localList = patternCopy;
          int i = paramInt;
          paramAnonymousVarArgs = paramOnCheckCallback;
          Objects.requireNonNull(paramAnonymousVarArgs);
          _..Lambda.TTC7hNz7BTsLwhNRb2L5kl_7mdU localTTC7hNz7BTsLwhNRb2L5kl_7mdU = new com/android/internal/widget/_$$Lambda$TTC7hNz7BTsLwhNRb2L5kl_7mdU;
          localTTC7hNz7BTsLwhNRb2L5kl_7mdU.<init>(paramAnonymousVarArgs);
          boolean bool = localLockPatternUtils.checkPattern(localList, i, localTTC7hNz7BTsLwhNRb2L5kl_7mdU);
          return Boolean.valueOf(bool);
        }
        catch (LockPatternUtils.RequestThrottledException paramAnonymousVarArgs)
        {
          mThrottleTimeout = paramAnonymousVarArgs.getTimeoutMs();
        }
        return Boolean.valueOf(false);
      }
      
      protected void onCancelled()
      {
        paramOnCheckCallback.onCancelled();
      }
      
      protected void onPostExecute(Boolean paramAnonymousBoolean)
      {
        paramOnCheckCallback.onChecked(paramAnonymousBoolean.booleanValue(), mThrottleTimeout);
      }
      
      protected void onPreExecute()
      {
        patternCopy = new ArrayList(LockPatternChecker.this);
      }
    };
    paramLockPatternUtils.execute(new Void[0]);
    return paramLockPatternUtils;
  }
  
  public static AsyncTask<?, ?, ?> verifyPassword(LockPatternUtils paramLockPatternUtils, final String paramString, final long paramLong, int paramInt, final OnVerifyCallback paramOnVerifyCallback)
  {
    paramLockPatternUtils = new AsyncTask()
    {
      private int mThrottleTimeout;
      
      protected byte[] doInBackground(Void... paramAnonymousVarArgs)
      {
        try
        {
          paramAnonymousVarArgs = verifyPassword(paramString, paramLong, paramOnVerifyCallback);
          return paramAnonymousVarArgs;
        }
        catch (LockPatternUtils.RequestThrottledException paramAnonymousVarArgs)
        {
          mThrottleTimeout = paramAnonymousVarArgs.getTimeoutMs();
        }
        return null;
      }
      
      protected void onPostExecute(byte[] paramAnonymousArrayOfByte)
      {
        val$callback.onVerified(paramAnonymousArrayOfByte, mThrottleTimeout);
      }
    };
    paramLockPatternUtils.execute(new Void[0]);
    return paramLockPatternUtils;
  }
  
  public static AsyncTask<?, ?, ?> verifyPattern(final LockPatternUtils paramLockPatternUtils, List<LockPatternView.Cell> paramList, final long paramLong, int paramInt, final OnVerifyCallback paramOnVerifyCallback)
  {
    paramLockPatternUtils = new AsyncTask()
    {
      private int mThrottleTimeout;
      private List<LockPatternView.Cell> patternCopy;
      
      protected byte[] doInBackground(Void... paramAnonymousVarArgs)
      {
        try
        {
          paramAnonymousVarArgs = paramLockPatternUtils.verifyPattern(patternCopy, paramLong, paramOnVerifyCallback);
          return paramAnonymousVarArgs;
        }
        catch (LockPatternUtils.RequestThrottledException paramAnonymousVarArgs)
        {
          mThrottleTimeout = paramAnonymousVarArgs.getTimeoutMs();
        }
        return null;
      }
      
      protected void onPostExecute(byte[] paramAnonymousArrayOfByte)
      {
        val$callback.onVerified(paramAnonymousArrayOfByte, mThrottleTimeout);
      }
      
      protected void onPreExecute()
      {
        patternCopy = new ArrayList(LockPatternChecker.this);
      }
    };
    paramLockPatternUtils.execute(new Void[0]);
    return paramLockPatternUtils;
  }
  
  public static AsyncTask<?, ?, ?> verifyTiedProfileChallenge(LockPatternUtils paramLockPatternUtils, final String paramString, final boolean paramBoolean, final long paramLong, int paramInt, final OnVerifyCallback paramOnVerifyCallback)
  {
    paramLockPatternUtils = new AsyncTask()
    {
      private int mThrottleTimeout;
      
      protected byte[] doInBackground(Void... paramAnonymousVarArgs)
      {
        try
        {
          paramAnonymousVarArgs = verifyTiedProfileChallenge(paramString, paramBoolean, paramLong, paramOnVerifyCallback);
          return paramAnonymousVarArgs;
        }
        catch (LockPatternUtils.RequestThrottledException paramAnonymousVarArgs)
        {
          mThrottleTimeout = paramAnonymousVarArgs.getTimeoutMs();
        }
        return null;
      }
      
      protected void onPostExecute(byte[] paramAnonymousArrayOfByte)
      {
        val$callback.onVerified(paramAnonymousArrayOfByte, mThrottleTimeout);
      }
    };
    paramLockPatternUtils.execute(new Void[0]);
    return paramLockPatternUtils;
  }
  
  public static abstract interface OnCheckCallback
  {
    public void onCancelled() {}
    
    public abstract void onChecked(boolean paramBoolean, int paramInt);
    
    public void onEarlyMatched() {}
  }
  
  public static abstract interface OnVerifyCallback
  {
    public abstract void onVerified(byte[] paramArrayOfByte, int paramInt);
  }
}
