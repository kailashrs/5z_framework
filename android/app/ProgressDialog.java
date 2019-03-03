package android.app;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.R.styleable;
import java.text.NumberFormat;

@Deprecated
public class ProgressDialog
  extends AlertDialog
{
  public static final int STYLE_HORIZONTAL = 1;
  public static final int STYLE_SPINNER = 0;
  private boolean mHasStarted;
  private int mIncrementBy;
  private int mIncrementSecondaryBy;
  private boolean mIndeterminate;
  private Drawable mIndeterminateDrawable;
  private int mMax;
  private CharSequence mMessage;
  private TextView mMessageView;
  private ProgressBar mProgress;
  private Drawable mProgressDrawable;
  private TextView mProgressNumber;
  private String mProgressNumberFormat;
  private TextView mProgressPercent;
  private NumberFormat mProgressPercentFormat;
  private int mProgressStyle = 0;
  private int mProgressVal;
  private int mSecondaryProgressVal;
  private Handler mViewUpdateHandler;
  
  public ProgressDialog(Context paramContext)
  {
    super(paramContext);
    initFormats();
  }
  
  public ProgressDialog(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
    initFormats();
  }
  
  private void initFormats()
  {
    mProgressNumberFormat = "%1d/%2d";
    mProgressPercentFormat = NumberFormat.getPercentInstance();
    mProgressPercentFormat.setMaximumFractionDigits(0);
  }
  
  private void onProgressChanged()
  {
    if ((mProgressStyle == 1) && (mViewUpdateHandler != null) && (!mViewUpdateHandler.hasMessages(0))) {
      mViewUpdateHandler.sendEmptyMessage(0);
    }
  }
  
  public static ProgressDialog show(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    return show(paramContext, paramCharSequence1, paramCharSequence2, false);
  }
  
  public static ProgressDialog show(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean)
  {
    return show(paramContext, paramCharSequence1, paramCharSequence2, paramBoolean, false, null);
  }
  
  public static ProgressDialog show(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean1, boolean paramBoolean2)
  {
    return show(paramContext, paramCharSequence1, paramCharSequence2, paramBoolean1, paramBoolean2, null);
  }
  
  public static ProgressDialog show(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, boolean paramBoolean1, boolean paramBoolean2, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    paramContext = new ProgressDialog(paramContext);
    paramContext.setTitle(paramCharSequence1);
    paramContext.setMessage(paramCharSequence2);
    paramContext.setIndeterminate(paramBoolean1);
    paramContext.setCancelable(paramBoolean2);
    paramContext.setOnCancelListener(paramOnCancelListener);
    paramContext.show();
    return paramContext;
  }
  
  public int getMax()
  {
    if (mProgress != null) {
      return mProgress.getMax();
    }
    return mMax;
  }
  
  public int getProgress()
  {
    if (mProgress != null) {
      return mProgress.getProgress();
    }
    return mProgressVal;
  }
  
  public int getSecondaryProgress()
  {
    if (mProgress != null) {
      return mProgress.getSecondaryProgress();
    }
    return mSecondaryProgressVal;
  }
  
  public void incrementProgressBy(int paramInt)
  {
    if (mProgress != null)
    {
      mProgress.incrementProgressBy(paramInt);
      onProgressChanged();
    }
    else
    {
      mIncrementBy += paramInt;
    }
  }
  
  public void incrementSecondaryProgressBy(int paramInt)
  {
    if (mProgress != null)
    {
      mProgress.incrementSecondaryProgressBy(paramInt);
      onProgressChanged();
    }
    else
    {
      mIncrementSecondaryBy += paramInt;
    }
  }
  
  public boolean isIndeterminate()
  {
    if (mProgress != null) {
      return mProgress.isIndeterminate();
    }
    return mIndeterminate;
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    Object localObject = LayoutInflater.from(mContext);
    TypedArray localTypedArray = mContext.obtainStyledAttributes(null, R.styleable.AlertDialog, 16842845, 0);
    if (mProgressStyle == 1)
    {
      mViewUpdateHandler = new Handler()
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          super.handleMessage(paramAnonymousMessage);
          int i = mProgress.getProgress();
          int j = mProgress.getMax();
          if (mProgressNumberFormat != null)
          {
            paramAnonymousMessage = mProgressNumberFormat;
            mProgressNumber.setText(String.format(paramAnonymousMessage, new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));
          }
          else
          {
            mProgressNumber.setText("");
          }
          if (mProgressPercentFormat != null)
          {
            double d = i / j;
            paramAnonymousMessage = new SpannableString(mProgressPercentFormat.format(d));
            paramAnonymousMessage.setSpan(new StyleSpan(1), 0, paramAnonymousMessage.length(), 33);
            mProgressPercent.setText(paramAnonymousMessage);
          }
          else
          {
            mProgressPercent.setText("");
          }
        }
      };
      localObject = ((LayoutInflater)localObject).inflate(localTypedArray.getResourceId(13, 17367086), null);
      mProgress = ((ProgressBar)((View)localObject).findViewById(16908301));
      mProgressNumber = ((TextView)((View)localObject).findViewById(16909267));
      mProgressPercent = ((TextView)((View)localObject).findViewById(16909268));
      setView((View)localObject);
    }
    else
    {
      localObject = ((LayoutInflater)localObject).inflate(localTypedArray.getResourceId(18, 17367273), null);
      mProgress = ((ProgressBar)((View)localObject).findViewById(16908301));
      mMessageView = ((TextView)((View)localObject).findViewById(16908299));
      setView((View)localObject);
    }
    localTypedArray.recycle();
    if (mMax > 0) {
      setMax(mMax);
    }
    if (mProgressVal > 0) {
      setProgress(mProgressVal);
    }
    if (mSecondaryProgressVal > 0) {
      setSecondaryProgress(mSecondaryProgressVal);
    }
    if (mIncrementBy > 0) {
      incrementProgressBy(mIncrementBy);
    }
    if (mIncrementSecondaryBy > 0) {
      incrementSecondaryProgressBy(mIncrementSecondaryBy);
    }
    if (mProgressDrawable != null) {
      setProgressDrawable(mProgressDrawable);
    }
    if (mIndeterminateDrawable != null) {
      setIndeterminateDrawable(mIndeterminateDrawable);
    }
    if (mMessage != null) {
      setMessage(mMessage);
    }
    setIndeterminate(mIndeterminate);
    onProgressChanged();
    super.onCreate(paramBundle);
  }
  
  public void onStart()
  {
    super.onStart();
    mHasStarted = true;
  }
  
  protected void onStop()
  {
    super.onStop();
    mHasStarted = false;
  }
  
  public void setIndeterminate(boolean paramBoolean)
  {
    if (mProgress != null) {
      mProgress.setIndeterminate(paramBoolean);
    } else {
      mIndeterminate = paramBoolean;
    }
  }
  
  public void setIndeterminateDrawable(Drawable paramDrawable)
  {
    if (mProgress != null) {
      mProgress.setIndeterminateDrawable(paramDrawable);
    } else {
      mIndeterminateDrawable = paramDrawable;
    }
  }
  
  public void setMax(int paramInt)
  {
    if (mProgress != null)
    {
      mProgress.setMax(paramInt);
      onProgressChanged();
    }
    else
    {
      mMax = paramInt;
    }
  }
  
  public void setMessage(CharSequence paramCharSequence)
  {
    if (mProgress != null)
    {
      if (mProgressStyle == 1) {
        super.setMessage(paramCharSequence);
      } else {
        mMessageView.setText(paramCharSequence);
      }
    }
    else {
      mMessage = paramCharSequence;
    }
  }
  
  public void setProgress(int paramInt)
  {
    if (mHasStarted)
    {
      mProgress.setProgress(paramInt);
      onProgressChanged();
    }
    else
    {
      mProgressVal = paramInt;
    }
  }
  
  public void setProgressDrawable(Drawable paramDrawable)
  {
    if (mProgress != null) {
      mProgress.setProgressDrawable(paramDrawable);
    } else {
      mProgressDrawable = paramDrawable;
    }
  }
  
  public void setProgressNumberFormat(String paramString)
  {
    mProgressNumberFormat = paramString;
    onProgressChanged();
  }
  
  public void setProgressPercentFormat(NumberFormat paramNumberFormat)
  {
    mProgressPercentFormat = paramNumberFormat;
    onProgressChanged();
  }
  
  public void setProgressStyle(int paramInt)
  {
    mProgressStyle = paramInt;
  }
  
  public void setSecondaryProgress(int paramInt)
  {
    if (mProgress != null)
    {
      mProgress.setSecondaryProgress(paramInt);
      onProgressChanged();
    }
    else
    {
      mSecondaryProgressVal = paramInt;
    }
  }
}
