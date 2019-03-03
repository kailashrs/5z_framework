package android.widget;

import android.os.Handler;

class DoubleDigitManager
{
  private Integer intermediateDigit;
  private final CallBack mCallBack;
  private final long timeoutInMillis;
  
  public DoubleDigitManager(long paramLong, CallBack paramCallBack)
  {
    timeoutInMillis = paramLong;
    mCallBack = paramCallBack;
  }
  
  public void reportDigit(int paramInt)
  {
    if (intermediateDigit == null)
    {
      intermediateDigit = Integer.valueOf(paramInt);
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          if (intermediateDigit != null)
          {
            mCallBack.singleDigitFinal(intermediateDigit.intValue());
            DoubleDigitManager.access$002(DoubleDigitManager.this, null);
          }
        }
      }, timeoutInMillis);
      if (!mCallBack.singleDigitIntermediate(paramInt))
      {
        intermediateDigit = null;
        mCallBack.singleDigitFinal(paramInt);
      }
    }
    else if (mCallBack.twoDigitsFinal(intermediateDigit.intValue(), paramInt))
    {
      intermediateDigit = null;
    }
  }
  
  static abstract interface CallBack
  {
    public abstract void singleDigitFinal(int paramInt);
    
    public abstract boolean singleDigitIntermediate(int paramInt);
    
    public abstract boolean twoDigitsFinal(int paramInt1, int paramInt2);
  }
}
