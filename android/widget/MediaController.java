package android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.policy.PhoneWindow;
import java.util.Formatter;
import java.util.Locale;

public class MediaController
  extends FrameLayout
{
  private static final int sDefaultTimeout = 3000;
  private final AccessibilityManager mAccessibilityManager;
  private View mAnchor;
  private final Context mContext;
  private TextView mCurrentTime;
  private View mDecor;
  private WindowManager.LayoutParams mDecorLayoutParams;
  private boolean mDragging;
  private TextView mEndTime;
  private final Runnable mFadeOut = new Runnable()
  {
    public void run()
    {
      hide();
    }
  };
  private ImageButton mFfwdButton;
  private final View.OnClickListener mFfwdListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      int i = mPlayer.getCurrentPosition();
      mPlayer.seekTo(i + 15000);
      MediaController.this.setProgress();
      show(3000);
    }
  };
  StringBuilder mFormatBuilder;
  Formatter mFormatter;
  private boolean mFromXml;
  private final View.OnLayoutChangeListener mLayoutChangeListener = new View.OnLayoutChangeListener()
  {
    public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
    {
      MediaController.this.updateFloatingWindowLayout();
      if (mShowing) {
        mWindowManager.updateViewLayout(mDecor, mDecorLayoutParams);
      }
    }
  };
  private boolean mListenersSet;
  private ImageButton mNextButton;
  private View.OnClickListener mNextListener;
  private ImageButton mPauseButton;
  private CharSequence mPauseDescription;
  private final View.OnClickListener mPauseListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      MediaController.this.doPauseResume();
      show(3000);
    }
  };
  private CharSequence mPlayDescription;
  private MediaPlayerControl mPlayer;
  private ImageButton mPrevButton;
  private View.OnClickListener mPrevListener;
  private ProgressBar mProgress;
  private ImageButton mRewButton;
  private final View.OnClickListener mRewListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      int i = mPlayer.getCurrentPosition();
      mPlayer.seekTo(i - 5000);
      MediaController.this.setProgress();
      show(3000);
    }
  };
  private View mRoot;
  private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener()
  {
    public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
    {
      if (!paramAnonymousBoolean) {
        return;
      }
      long l = mPlayer.getDuration();
      l = paramAnonymousInt * l / 1000L;
      mPlayer.seekTo((int)l);
      if (mCurrentTime != null) {
        mCurrentTime.setText(MediaController.this.stringForTime((int)l));
      }
    }
    
    public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
    {
      show(3600000);
      MediaController.access$602(MediaController.this, true);
      removeCallbacks(mShowProgress);
    }
    
    public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
    {
      MediaController.access$602(MediaController.this, false);
      MediaController.this.setProgress();
      MediaController.this.updatePausePlay();
      show(3000);
      post(mShowProgress);
    }
  };
  private final Runnable mShowProgress = new Runnable()
  {
    public void run()
    {
      int i = MediaController.this.setProgress();
      if ((!mDragging) && (mShowing) && (mPlayer.isPlaying())) {
        postDelayed(mShowProgress, 1000 - i % 1000);
      }
    }
  };
  private boolean mShowing;
  private final View.OnTouchListener mTouchListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      if ((paramAnonymousMotionEvent.getAction() == 0) && (mShowing)) {
        hide();
      }
      return false;
    }
  };
  private final boolean mUseFastForward;
  private Window mWindow;
  private WindowManager mWindowManager;
  
  public MediaController(Context paramContext)
  {
    this(paramContext, true);
  }
  
  public MediaController(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    mRoot = this;
    mContext = paramContext;
    mUseFastForward = true;
    mFromXml = true;
    mAccessibilityManager = AccessibilityManager.getInstance(paramContext);
  }
  
  public MediaController(Context paramContext, boolean paramBoolean)
  {
    super(paramContext);
    mContext = paramContext;
    mUseFastForward = paramBoolean;
    initFloatingWindowLayout();
    initFloatingWindow();
    mAccessibilityManager = AccessibilityManager.getInstance(paramContext);
  }
  
  private void disableUnsupportedButtons()
  {
    try
    {
      if ((mPauseButton != null) && (!mPlayer.canPause())) {
        mPauseButton.setEnabled(false);
      }
      if ((mRewButton != null) && (!mPlayer.canSeekBackward())) {
        mRewButton.setEnabled(false);
      }
      if ((mFfwdButton != null) && (!mPlayer.canSeekForward())) {
        mFfwdButton.setEnabled(false);
      }
      if ((mProgress != null) && (!mPlayer.canSeekBackward()) && (!mPlayer.canSeekForward())) {
        mProgress.setEnabled(false);
      }
    }
    catch (IncompatibleClassChangeError localIncompatibleClassChangeError) {}
  }
  
  private void doPauseResume()
  {
    if (mPlayer.isPlaying()) {
      mPlayer.pause();
    } else {
      mPlayer.start();
    }
    updatePausePlay();
  }
  
  private void initControllerView(View paramView)
  {
    Object localObject = mContext.getResources();
    mPlayDescription = ((Resources)localObject).getText(17040293);
    mPauseDescription = ((Resources)localObject).getText(17040292);
    mPauseButton = ((ImageButton)paramView.findViewById(16909224));
    if (mPauseButton != null)
    {
      mPauseButton.requestFocus();
      mPauseButton.setOnClickListener(mPauseListener);
    }
    mFfwdButton = ((ImageButton)paramView.findViewById(16908935));
    localObject = mFfwdButton;
    int i = 0;
    int j;
    if (localObject != null)
    {
      mFfwdButton.setOnClickListener(mFfwdListener);
      if (!mFromXml)
      {
        localObject = mFfwdButton;
        if (mUseFastForward) {
          j = 0;
        } else {
          j = 8;
        }
        ((ImageButton)localObject).setVisibility(j);
      }
    }
    mRewButton = ((ImageButton)paramView.findViewById(16909295));
    if (mRewButton != null)
    {
      mRewButton.setOnClickListener(mRewListener);
      if (!mFromXml)
      {
        localObject = mRewButton;
        if (mUseFastForward) {
          j = i;
        } else {
          j = 8;
        }
        ((ImageButton)localObject).setVisibility(j);
      }
    }
    mNextButton = ((ImageButton)paramView.findViewById(16909159));
    if ((mNextButton != null) && (!mFromXml) && (!mListenersSet)) {
      mNextButton.setVisibility(8);
    }
    mPrevButton = ((ImageButton)paramView.findViewById(16909259));
    if ((mPrevButton != null) && (!mFromXml) && (!mListenersSet)) {
      mPrevButton.setVisibility(8);
    }
    mProgress = ((ProgressBar)paramView.findViewById(16909118));
    if (mProgress != null)
    {
      if ((mProgress instanceof SeekBar)) {
        ((SeekBar)mProgress).setOnSeekBarChangeListener(mSeekListener);
      }
      mProgress.setMax(1000);
    }
    mEndTime = ((TextView)paramView.findViewById(16909468));
    mCurrentTime = ((TextView)paramView.findViewById(16909471));
    mFormatBuilder = new StringBuilder();
    mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    installPrevNextListeners();
  }
  
  private void initFloatingWindow()
  {
    mWindowManager = ((WindowManager)mContext.getSystemService("window"));
    mWindow = new PhoneWindow(mContext);
    mWindow.setWindowManager(mWindowManager, null, null);
    mWindow.requestFeature(1);
    mDecor = mWindow.getDecorView();
    mDecor.setOnTouchListener(mTouchListener);
    mWindow.setContentView(this);
    mWindow.setBackgroundDrawableResource(17170445);
    mWindow.setVolumeControlStream(3);
    setFocusable(true);
    setFocusableInTouchMode(true);
    setDescendantFocusability(262144);
    requestFocus();
  }
  
  private void initFloatingWindowLayout()
  {
    mDecorLayoutParams = new WindowManager.LayoutParams();
    WindowManager.LayoutParams localLayoutParams = mDecorLayoutParams;
    gravity = 51;
    height = -2;
    x = 0;
    format = -3;
    type = 1000;
    flags |= 0x820020;
    token = null;
    windowAnimations = 0;
  }
  
  private void installPrevNextListeners()
  {
    ImageButton localImageButton = mNextButton;
    boolean bool1 = false;
    boolean bool2;
    if (localImageButton != null)
    {
      mNextButton.setOnClickListener(mNextListener);
      localImageButton = mNextButton;
      if (mNextListener != null) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      localImageButton.setEnabled(bool2);
    }
    if (mPrevButton != null)
    {
      mPrevButton.setOnClickListener(mPrevListener);
      localImageButton = mPrevButton;
      bool2 = bool1;
      if (mPrevListener != null) {
        bool2 = true;
      }
      localImageButton.setEnabled(bool2);
    }
  }
  
  private int setProgress()
  {
    if ((mPlayer != null) && (!mDragging))
    {
      int i = mPlayer.getCurrentPosition();
      int j = mPlayer.getDuration();
      if (mProgress != null)
      {
        if (j > 0)
        {
          long l = 1000L * i / j;
          mProgress.setProgress((int)l);
        }
        int k = mPlayer.getBufferPercentage();
        mProgress.setSecondaryProgress(k * 10);
      }
      if (mEndTime != null) {
        mEndTime.setText(stringForTime(j));
      }
      if (mCurrentTime != null) {
        mCurrentTime.setText(stringForTime(i));
      }
      return i;
    }
    return 0;
  }
  
  private String stringForTime(int paramInt)
  {
    int i = paramInt / 1000;
    paramInt = i % 60;
    int j = i / 60 % 60;
    i /= 3600;
    mFormatBuilder.setLength(0);
    if (i > 0) {
      return mFormatter.format("%d:%02d:%02d", new Object[] { Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(paramInt) }).toString();
    }
    return mFormatter.format("%02d:%02d", new Object[] { Integer.valueOf(j), Integer.valueOf(paramInt) }).toString();
  }
  
  private void updateFloatingWindowLayout()
  {
    int[] arrayOfInt = new int[2];
    mAnchor.getLocationOnScreen(arrayOfInt);
    mDecor.measure(View.MeasureSpec.makeMeasureSpec(mAnchor.getWidth(), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(mAnchor.getHeight(), Integer.MIN_VALUE));
    WindowManager.LayoutParams localLayoutParams = mDecorLayoutParams;
    width = mAnchor.getWidth();
    x = (arrayOfInt[0] + (mAnchor.getWidth() - width) / 2);
    y = (arrayOfInt[1] + mAnchor.getHeight() - mDecor.getMeasuredHeight());
  }
  
  private void updatePausePlay()
  {
    if ((mRoot != null) && (mPauseButton != null))
    {
      if (mPlayer.isPlaying())
      {
        mPauseButton.setImageResource(17301539);
        mPauseButton.setContentDescription(mPauseDescription);
      }
      else
      {
        mPauseButton.setImageResource(17301540);
        mPauseButton.setContentDescription(mPlayDescription);
      }
      return;
    }
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    int j;
    if ((paramKeyEvent.getRepeatCount() == 0) && (paramKeyEvent.getAction() == 0)) {
      j = 1;
    } else {
      j = 0;
    }
    if ((i != 79) && (i != 85) && (i != 62))
    {
      if (i == 126)
      {
        if ((j != 0) && (!mPlayer.isPlaying()))
        {
          mPlayer.start();
          updatePausePlay();
          show(3000);
        }
        return true;
      }
      if ((i != 86) && (i != 127))
      {
        if ((i != 25) && (i != 24) && (i != 164) && (i != 27))
        {
          if ((i != 4) && (i != 82))
          {
            show(3000);
            return super.dispatchKeyEvent(paramKeyEvent);
          }
          if (j != 0) {
            hide();
          }
          return true;
        }
        return super.dispatchKeyEvent(paramKeyEvent);
      }
      if ((j != 0) && (mPlayer.isPlaying()))
      {
        mPlayer.pause();
        updatePausePlay();
        show(3000);
      }
      return true;
    }
    if (j != 0)
    {
      doPauseResume();
      show(3000);
      if (mPauseButton != null) {
        mPauseButton.requestFocus();
      }
    }
    return true;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return MediaController.class.getName();
  }
  
  public void hide()
  {
    if (mAnchor == null) {
      return;
    }
    if (mShowing)
    {
      try
      {
        removeCallbacks(mShowProgress);
        mWindowManager.removeView(mDecor);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.w("MediaController", "already removed");
      }
      mShowing = false;
    }
  }
  
  public boolean isShowing()
  {
    return mShowing;
  }
  
  protected View makeControllerView()
  {
    mRoot = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(17367204, null);
    initControllerView(mRoot);
    return mRoot;
  }
  
  public void onFinishInflate()
  {
    if (mRoot != null) {
      initControllerView(mRoot);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if (i != 3) {
      switch (i)
      {
      default: 
        break;
      case 1: 
        show(3000);
        break;
      case 0: 
        show(0);
        break;
      }
    } else {
      hide();
    }
    return true;
  }
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent)
  {
    show(3000);
    return false;
  }
  
  public void setAnchorView(View paramView)
  {
    if (mAnchor != null) {
      mAnchor.removeOnLayoutChangeListener(mLayoutChangeListener);
    }
    mAnchor = paramView;
    if (mAnchor != null) {
      mAnchor.addOnLayoutChangeListener(mLayoutChangeListener);
    }
    paramView = new FrameLayout.LayoutParams(-1, -1);
    removeAllViews();
    addView(makeControllerView(), paramView);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if (mPauseButton != null) {
      mPauseButton.setEnabled(paramBoolean);
    }
    if (mFfwdButton != null) {
      mFfwdButton.setEnabled(paramBoolean);
    }
    if (mRewButton != null) {
      mRewButton.setEnabled(paramBoolean);
    }
    ImageButton localImageButton = mNextButton;
    boolean bool1 = false;
    boolean bool2;
    if (localImageButton != null)
    {
      localImageButton = mNextButton;
      if ((paramBoolean) && (mNextListener != null)) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      localImageButton.setEnabled(bool2);
    }
    if (mPrevButton != null)
    {
      localImageButton = mPrevButton;
      bool2 = bool1;
      if (paramBoolean)
      {
        bool2 = bool1;
        if (mPrevListener != null) {
          bool2 = true;
        }
      }
      localImageButton.setEnabled(bool2);
    }
    if (mProgress != null) {
      mProgress.setEnabled(paramBoolean);
    }
    disableUnsupportedButtons();
    super.setEnabled(paramBoolean);
  }
  
  public void setMediaPlayer(MediaPlayerControl paramMediaPlayerControl)
  {
    mPlayer = paramMediaPlayerControl;
    updatePausePlay();
  }
  
  public void setPrevNextListeners(View.OnClickListener paramOnClickListener1, View.OnClickListener paramOnClickListener2)
  {
    mNextListener = paramOnClickListener1;
    mPrevListener = paramOnClickListener2;
    mListenersSet = true;
    if (mRoot != null)
    {
      installPrevNextListeners();
      if ((mNextButton != null) && (!mFromXml)) {
        mNextButton.setVisibility(0);
      }
      if ((mPrevButton != null) && (!mFromXml)) {
        mPrevButton.setVisibility(0);
      }
    }
  }
  
  public void show()
  {
    show(3000);
  }
  
  public void show(int paramInt)
  {
    if ((!mShowing) && (mAnchor != null))
    {
      setProgress();
      if (mPauseButton != null) {
        mPauseButton.requestFocus();
      }
      disableUnsupportedButtons();
      updateFloatingWindowLayout();
      mWindowManager.addView(mDecor, mDecorLayoutParams);
      mShowing = true;
    }
    updatePausePlay();
    post(mShowProgress);
    if ((paramInt != 0) && (!mAccessibilityManager.isTouchExplorationEnabled()))
    {
      removeCallbacks(mFadeOut);
      postDelayed(mFadeOut, paramInt);
    }
  }
  
  public static abstract interface MediaPlayerControl
  {
    public abstract boolean canPause();
    
    public abstract boolean canSeekBackward();
    
    public abstract boolean canSeekForward();
    
    public abstract int getAudioSessionId();
    
    public abstract int getBufferPercentage();
    
    public abstract int getCurrentPosition();
    
    public abstract int getDuration();
    
    public abstract boolean isPlaying();
    
    public abstract void pause();
    
    public abstract void seekTo(int paramInt);
    
    public abstract void start();
  }
}
