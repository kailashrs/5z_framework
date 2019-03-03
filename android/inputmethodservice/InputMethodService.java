package android.inputmethodservice;

import android.R.styleable;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnComputeInternalInsetsListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class InputMethodService
  extends AbstractInputMethodService
{
  public static final int BACK_DISPOSITION_ADJUST_NOTHING = 3;
  public static final int BACK_DISPOSITION_DEFAULT = 0;
  private static final int BACK_DISPOSITION_MAX = 3;
  private static final int BACK_DISPOSITION_MIN = 0;
  @Deprecated
  public static final int BACK_DISPOSITION_WILL_DISMISS = 2;
  @Deprecated
  public static final int BACK_DISPOSITION_WILL_NOT_DISMISS = 1;
  static final boolean DEBUG = false;
  public static final int IME_ACTIVE = 1;
  public static final int IME_VISIBLE = 2;
  static final int MOVEMENT_DOWN = -1;
  static final int MOVEMENT_UP = -2;
  static final String TAG = "InputMethodService";
  final View.OnClickListener mActionClickListener = new _..Lambda.InputMethodService.wp8DeVGx_WDOPw4F6an7QbwVxf0(this);
  int mBackDisposition;
  FrameLayout mCandidatesFrame;
  boolean mCandidatesViewStarted;
  int mCandidatesVisibility;
  CompletionInfo[] mCurCompletions;
  ViewGroup mExtractAccessories;
  View mExtractAction;
  ExtractEditText mExtractEditText;
  FrameLayout mExtractFrame;
  View mExtractView;
  boolean mExtractViewHidden;
  ExtractedText mExtractedText;
  int mExtractedToken;
  boolean mFullscreenApplied;
  ViewGroup mFullscreenArea;
  InputMethodManager mImm;
  boolean mInShowWindow;
  LayoutInflater mInflater;
  boolean mInitialized;
  InputBinding mInputBinding;
  InputConnection mInputConnection;
  EditorInfo mInputEditorInfo;
  FrameLayout mInputFrame;
  boolean mInputStarted;
  View mInputView;
  boolean mInputViewStarted;
  final ViewTreeObserver.OnComputeInternalInsetsListener mInsetsComputer = new _..Lambda.InputMethodService.8T9TmAUIN7vW9eU6kTg8309_d4E(this);
  boolean mIsFullscreen;
  boolean mIsInputViewShown;
  boolean mLastShowInputRequested;
  View mRootView;
  private SettingsObserver mSettingsObserver;
  boolean mShouldClearInsetOfPreviousIme;
  int mShowInputFlags;
  boolean mShowInputRequested;
  private IBinder mStartInputToken;
  InputConnection mStartedInputConnection;
  int mStatusIcon;
  int mTheme = 0;
  TypedArray mThemeAttrs;
  final Insets mTmpInsets = new Insets();
  final int[] mTmpLocation = new int[2];
  IBinder mToken;
  SoftInputWindow mWindow;
  boolean mWindowAdded;
  boolean mWindowCreated;
  boolean mWindowVisible;
  boolean mWindowWasVisible;
  
  public InputMethodService() {}
  
  private void clearInsetOfPreviousIme()
  {
    if (!mShouldClearInsetOfPreviousIme) {
      return;
    }
    mImm.clearLastInputMethodWindowForTransition(mToken);
    mShouldClearInsetOfPreviousIme = false;
  }
  
  private boolean dispatchOnShowInputRequested(int paramInt, boolean paramBoolean)
  {
    paramBoolean = onShowInputRequested(paramInt, paramBoolean);
    if (paramBoolean) {
      mShowInputFlags = paramInt;
    } else {
      mShowInputFlags = 0;
    }
    return paramBoolean;
  }
  
  private void doHideWindow()
  {
    mImm.setImeWindowStatus(mToken, mStartInputToken, 0, mBackDisposition);
    hideWindow();
  }
  
  private void finishViews()
  {
    if (mInputViewStarted) {
      onFinishInputView(false);
    } else if (mCandidatesViewStarted) {
      onFinishCandidatesView(false);
    }
    mInputViewStarted = false;
    mCandidatesViewStarted = false;
  }
  
  private ExtractEditText getExtractEditTextIfVisible()
  {
    if ((isExtractViewShown()) && (isInputViewShown())) {
      return mExtractEditText;
    }
    return null;
  }
  
  private int getIconForImeAction(int paramInt)
  {
    switch (paramInt & 0xFF)
    {
    default: 
      return 17302664;
    case 7: 
      return 17302663;
    case 6: 
      return 17302660;
    case 5: 
      return 17302662;
    case 4: 
      return 17302666;
    case 3: 
      return 17302665;
    }
    return 17302661;
  }
  
  private boolean handleBack(boolean paramBoolean)
  {
    if (mShowInputRequested)
    {
      if (paramBoolean) {
        requestHideSelf(0);
      }
      return true;
    }
    if (mWindowVisible)
    {
      if (mCandidatesVisibility == 0)
      {
        if (paramBoolean) {
          setCandidatesViewShown(false);
        }
      }
      else if (paramBoolean) {
        doHideWindow();
      }
      return true;
    }
    return false;
  }
  
  private static int mapToImeWindowStatus(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 2;
    } else {
      i = 0;
    }
    return i | 0x1;
  }
  
  private void onToggleSoftInput(int paramInt1, int paramInt2)
  {
    if (isInputViewShown()) {
      requestHideSelf(paramInt2);
    } else {
      requestShowSelf(paramInt1);
    }
  }
  
  private void resetStateForNewConfiguration()
  {
    boolean bool1 = mWindowVisible;
    int i = mShowInputFlags;
    boolean bool2 = mShowInputRequested;
    Object localObject = mCurCompletions;
    initViews();
    int j = 0;
    mInputViewStarted = false;
    mCandidatesViewStarted = false;
    if (mInputStarted) {
      doStartInput(getCurrentInputConnection(), getCurrentInputEditorInfo(), true);
    }
    if (bool1)
    {
      if (bool2)
      {
        if (dispatchOnShowInputRequested(i, true))
        {
          showWindow(true);
          if (localObject != null)
          {
            mCurCompletions = ((CompletionInfo[])localObject);
            onDisplayCompletions((CompletionInfo[])localObject);
          }
        }
        else
        {
          doHideWindow();
        }
      }
      else if (mCandidatesVisibility == 0) {
        showWindow(false);
      } else {
        doHideWindow();
      }
      bool1 = onEvaluateInputViewShown();
      localObject = mImm;
      IBinder localIBinder1 = mToken;
      IBinder localIBinder2 = mStartInputToken;
      if (bool1) {
        j = 2;
      }
      ((InputMethodManager)localObject).setImeWindowStatus(localIBinder1, localIBinder2, j | 0x1, mBackDisposition);
    }
  }
  
  void doFinishInput()
  {
    if (mInputViewStarted) {
      onFinishInputView(true);
    } else if (mCandidatesViewStarted) {
      onFinishCandidatesView(true);
    }
    mInputViewStarted = false;
    mCandidatesViewStarted = false;
    if (mInputStarted) {
      onFinishInput();
    }
    mInputStarted = false;
    mStartedInputConnection = null;
    mCurCompletions = null;
  }
  
  boolean doMovementKey(int paramInt1, KeyEvent paramKeyEvent, int paramInt2)
  {
    ExtractEditText localExtractEditText = getExtractEditTextIfVisible();
    if (localExtractEditText != null)
    {
      MovementMethod localMovementMethod = localExtractEditText.getMovementMethod();
      Object localObject = localExtractEditText.getLayout();
      if ((localMovementMethod != null) && (localObject != null)) {
        if (paramInt2 == -1)
        {
          if (localMovementMethod.onKeyDown(localExtractEditText, localExtractEditText.getText(), paramInt1, paramKeyEvent))
          {
            reportExtractedMovement(paramInt1, 1);
            return true;
          }
        }
        else if (paramInt2 == -2)
        {
          if (localMovementMethod.onKeyUp(localExtractEditText, localExtractEditText.getText(), paramInt1, paramKeyEvent)) {
            return true;
          }
        }
        else if (localMovementMethod.onKeyOther(localExtractEditText, localExtractEditText.getText(), paramKeyEvent))
        {
          reportExtractedMovement(paramInt1, paramInt2);
        }
        else
        {
          localObject = KeyEvent.changeAction(paramKeyEvent, 0);
          if (localMovementMethod.onKeyDown(localExtractEditText, localExtractEditText.getText(), paramInt1, (KeyEvent)localObject))
          {
            paramKeyEvent = KeyEvent.changeAction(paramKeyEvent, 1);
            localMovementMethod.onKeyUp(localExtractEditText, localExtractEditText.getText(), paramInt1, paramKeyEvent);
            for (;;)
            {
              paramInt2--;
              if (paramInt2 <= 0) {
                break;
              }
              localMovementMethod.onKeyDown(localExtractEditText, localExtractEditText.getText(), paramInt1, (KeyEvent)localObject);
              localMovementMethod.onKeyUp(localExtractEditText, localExtractEditText.getText(), paramInt1, paramKeyEvent);
            }
            reportExtractedMovement(paramInt1, paramInt2);
          }
        }
      }
      switch (paramInt1)
      {
      default: 
        break;
      case 19: 
      case 20: 
      case 21: 
      case 22: 
        return true;
      }
    }
    return false;
  }
  
  void doStartInput(InputConnection paramInputConnection, EditorInfo paramEditorInfo, boolean paramBoolean)
  {
    if (!paramBoolean) {
      doFinishInput();
    }
    mInputStarted = true;
    mStartedInputConnection = paramInputConnection;
    mInputEditorInfo = paramEditorInfo;
    initialize();
    onStartInput(paramEditorInfo, paramBoolean);
    if (mWindowVisible) {
      if (mShowInputRequested)
      {
        mInputViewStarted = true;
        onStartInputView(mInputEditorInfo, paramBoolean);
        startExtractingText(true);
      }
      else if (mCandidatesVisibility == 0)
      {
        mCandidatesViewStarted = true;
        onStartCandidatesView(mInputEditorInfo, paramBoolean);
      }
    }
  }
  
  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramFileDescriptor = new PrintWriterPrinter(paramPrintWriter);
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("Input method service state for ");
    paramPrintWriter.append(this);
    paramPrintWriter.append(":");
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mWindowCreated=");
    paramPrintWriter.append(mWindowCreated);
    paramPrintWriter.append(" mWindowAdded=");
    paramPrintWriter.append(mWindowAdded);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mWindowVisible=");
    paramPrintWriter.append(mWindowVisible);
    paramPrintWriter.append(" mWindowWasVisible=");
    paramPrintWriter.append(mWindowWasVisible);
    paramPrintWriter.append(" mInShowWindow=");
    paramPrintWriter.append(mInShowWindow);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  Configuration=");
    paramPrintWriter.append(getResources().getConfiguration());
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mToken=");
    paramPrintWriter.append(mToken);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mInputBinding=");
    paramPrintWriter.append(mInputBinding);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mInputConnection=");
    paramPrintWriter.append(mInputConnection);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mStartedInputConnection=");
    paramPrintWriter.append(mStartedInputConnection);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mInputStarted=");
    paramPrintWriter.append(mInputStarted);
    paramPrintWriter.append(" mInputViewStarted=");
    paramPrintWriter.append(mInputViewStarted);
    paramPrintWriter.append(" mCandidatesViewStarted=");
    paramPrintWriter.append(mCandidatesViewStarted);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mStartInputToken=");
    paramPrintWriter.append(mStartInputToken);
    paramFileDescriptor.println(paramPrintWriter.toString());
    if (mInputEditorInfo != null)
    {
      paramFileDescriptor.println("  mInputEditorInfo:");
      mInputEditorInfo.dump(paramFileDescriptor, "    ");
    }
    else
    {
      paramFileDescriptor.println("  mInputEditorInfo: null");
    }
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mShowInputRequested=");
    paramPrintWriter.append(mShowInputRequested);
    paramPrintWriter.append(" mLastShowInputRequested=");
    paramPrintWriter.append(mLastShowInputRequested);
    paramPrintWriter.append(" mShowInputFlags=0x");
    paramPrintWriter.append(Integer.toHexString(mShowInputFlags));
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mCandidatesVisibility=");
    paramPrintWriter.append(mCandidatesVisibility);
    paramPrintWriter.append(" mFullscreenApplied=");
    paramPrintWriter.append(mFullscreenApplied);
    paramPrintWriter.append(" mIsFullscreen=");
    paramPrintWriter.append(mIsFullscreen);
    paramPrintWriter.append(" mExtractViewHidden=");
    paramPrintWriter.append(mExtractViewHidden);
    paramFileDescriptor.println(paramPrintWriter.toString());
    if (mExtractedText != null)
    {
      paramFileDescriptor.println("  mExtractedText:");
      paramPrintWriter = new StringBuilder();
      paramPrintWriter.append("    text=");
      paramPrintWriter.append(mExtractedText.text.length());
      paramPrintWriter.append(" chars startOffset=");
      paramPrintWriter.append(mExtractedText.startOffset);
      paramFileDescriptor.println(paramPrintWriter.toString());
      paramPrintWriter = new StringBuilder();
      paramPrintWriter.append("    selectionStart=");
      paramPrintWriter.append(mExtractedText.selectionStart);
      paramPrintWriter.append(" selectionEnd=");
      paramPrintWriter.append(mExtractedText.selectionEnd);
      paramPrintWriter.append(" flags=0x");
      paramPrintWriter.append(Integer.toHexString(mExtractedText.flags));
      paramFileDescriptor.println(paramPrintWriter.toString());
    }
    else
    {
      paramFileDescriptor.println("  mExtractedText: null");
    }
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mExtractedToken=");
    paramPrintWriter.append(mExtractedToken);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  mIsInputViewShown=");
    paramPrintWriter.append(mIsInputViewShown);
    paramPrintWriter.append(" mStatusIcon=");
    paramPrintWriter.append(mStatusIcon);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramFileDescriptor.println("Last computed insets:");
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("  contentTopInsets=");
    paramPrintWriter.append(mTmpInsets.contentTopInsets);
    paramPrintWriter.append(" visibleTopInsets=");
    paramPrintWriter.append(mTmpInsets.visibleTopInsets);
    paramPrintWriter.append(" touchableInsets=");
    paramPrintWriter.append(mTmpInsets.touchableInsets);
    paramPrintWriter.append(" touchableRegion=");
    paramPrintWriter.append(mTmpInsets.touchableRegion);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append(" mShouldClearInsetOfPreviousIme=");
    paramPrintWriter.append(mShouldClearInsetOfPreviousIme);
    paramFileDescriptor.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append(" mSettingsObserver=");
    paramPrintWriter.append(mSettingsObserver);
    paramFileDescriptor.println(paramPrintWriter.toString());
  }
  
  @Deprecated
  public boolean enableHardwareAcceleration()
  {
    if (mWindow == null) {
      return ActivityManager.isHighEndGfx();
    }
    throw new IllegalStateException("Must be called before onCreate()");
  }
  
  public final void exposeContent(InputContentInfo paramInputContentInfo, InputConnection paramInputConnection)
  {
    if (paramInputConnection == null) {
      return;
    }
    if (getCurrentInputConnection() != paramInputConnection) {
      return;
    }
    mImm.exposeContent(mToken, paramInputContentInfo, getCurrentInputEditorInfo());
  }
  
  public int getBackDisposition()
  {
    return mBackDisposition;
  }
  
  public int getCandidatesHiddenVisibility()
  {
    int i;
    if (isExtractViewShown()) {
      i = 8;
    } else {
      i = 4;
    }
    return i;
  }
  
  public InputBinding getCurrentInputBinding()
  {
    return mInputBinding;
  }
  
  public InputConnection getCurrentInputConnection()
  {
    InputConnection localInputConnection = mStartedInputConnection;
    if (localInputConnection != null) {
      return localInputConnection;
    }
    return mInputConnection;
  }
  
  public EditorInfo getCurrentInputEditorInfo()
  {
    return mInputEditorInfo;
  }
  
  public boolean getCurrentInputStarted()
  {
    return mInputStarted;
  }
  
  public int getInputMethodWindowRecommendedHeight()
  {
    return mImm.getInputMethodWindowVisibleHeight();
  }
  
  public LayoutInflater getLayoutInflater()
  {
    return mInflater;
  }
  
  public int getMaxWidth()
  {
    return ((WindowManager)getSystemService("window")).getDefaultDisplay().getWidth();
  }
  
  public CharSequence getTextForImeAction(int paramInt)
  {
    switch (paramInt & 0xFF)
    {
    default: 
      return getText(17040122);
    case 7: 
      return getText(17040126);
    case 6: 
      return getText(17040123);
    case 5: 
      return getText(17040125);
    case 4: 
      return getText(17040128);
    case 3: 
      return getText(17040127);
    case 2: 
      return getText(17040124);
    }
    return null;
  }
  
  public Dialog getWindow()
  {
    return mWindow;
  }
  
  public void hideStatusIcon()
  {
    mStatusIcon = 0;
    mImm.hideStatusIconInternal(mToken);
  }
  
  public void hideWindow()
  {
    finishViews();
    if (mWindowVisible)
    {
      mWindow.hide();
      mWindowVisible = false;
      onWindowHidden();
      mWindowWasVisible = false;
    }
    updateFullscreenMode();
  }
  
  void initViews()
  {
    mInitialized = false;
    mWindowCreated = false;
    mShowInputRequested = false;
    mShowInputFlags = 0;
    mThemeAttrs = obtainStyledAttributes(R.styleable.InputMethodService);
    mRootView = mInflater.inflate(17367186, null);
    mWindow.setContentView(mRootView);
    mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(mInsetsComputer);
    mRootView.getViewTreeObserver().addOnComputeInternalInsetsListener(mInsetsComputer);
    if (Settings.Global.getInt(getContentResolver(), "fancy_ime_animations", 0) != 0) {
      mWindow.getWindow().setWindowAnimations(16974583);
    }
    mFullscreenArea = ((ViewGroup)mRootView.findViewById(16908982));
    mExtractViewHidden = false;
    mExtractFrame = ((FrameLayout)mRootView.findViewById(16908316));
    mExtractView = null;
    mExtractEditText = null;
    mExtractAccessories = null;
    mExtractAction = null;
    mFullscreenApplied = false;
    mCandidatesFrame = ((FrameLayout)mRootView.findViewById(16908317));
    mInputFrame = ((FrameLayout)mRootView.findViewById(16908318));
    mInputView = null;
    mIsInputViewShown = false;
    mExtractFrame.setVisibility(8);
    mCandidatesVisibility = getCandidatesHiddenVisibility();
    mCandidatesFrame.setVisibility(mCandidatesVisibility);
    mInputFrame.setVisibility(8);
  }
  
  void initialize()
  {
    if (!mInitialized)
    {
      mInitialized = true;
      onInitializeInterface();
    }
  }
  
  public boolean isExtractViewShown()
  {
    boolean bool;
    if ((mIsFullscreen) && (!mExtractViewHidden)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isFullscreenMode()
  {
    return mIsFullscreen;
  }
  
  public boolean isInputViewShown()
  {
    boolean bool;
    if ((mIsInputViewShown) && (mWindowVisible)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isShowInputRequested()
  {
    return mShowInputRequested;
  }
  
  public void onAppPrivateCommand(String paramString, Bundle paramBundle) {}
  
  public void onBindInput() {}
  
  public void onComputeInsets(Insets paramInsets)
  {
    int[] arrayOfInt = mTmpLocation;
    if (mInputFrame.getVisibility() == 0) {
      mInputFrame.getLocationInWindow(arrayOfInt);
    } else {
      arrayOfInt[1] = getWindow().getWindow().getDecorView().getHeight();
    }
    if (isFullscreenMode()) {
      contentTopInsets = getWindow().getWindow().getDecorView().getHeight();
    } else {
      contentTopInsets = arrayOfInt[1];
    }
    if (mCandidatesFrame.getVisibility() == 0) {
      mCandidatesFrame.getLocationInWindow(arrayOfInt);
    }
    visibleTopInsets = arrayOfInt[1];
    touchableInsets = 2;
    touchableRegion.setEmpty();
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    resetStateForNewConfiguration();
  }
  
  public void onConfigureWindow(Window paramWindow, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = mWindow.getWindow().getAttributes().height;
    if (paramBoolean1) {
      i = -1;
    } else {
      i = -2;
    }
    paramBoolean1 = mIsInputViewShown;
    mWindow.getWindow().setLayout(-1, i);
  }
  
  public void onCreate()
  {
    mTheme = Resources.selectSystemTheme(mTheme, getApplicationInfotargetSdkVersion, 16973908, 16973951, 16974142, 16974142);
    super.setTheme(mTheme);
    super.onCreate();
    mImm = ((InputMethodManager)getSystemService("input_method"));
    mSettingsObserver = SettingsObserver.createAndRegister(this);
    boolean bool;
    if (mImm.getInputMethodWindowVisibleHeight() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    mShouldClearInsetOfPreviousIme = bool;
    mInflater = ((LayoutInflater)getSystemService("layout_inflater"));
    mWindow = new SoftInputWindow(this, "InputMethod", mTheme, null, null, mDispatcherState, 2011, 80, false);
    mWindow.getWindow().setFlags(Integer.MIN_VALUE, Integer.MIN_VALUE);
    initViews();
    mWindow.getWindow().setLayout(-1, -2);
  }
  
  public View onCreateCandidatesView()
  {
    return null;
  }
  
  public View onCreateExtractTextView()
  {
    return mInflater.inflate(17367187, null);
  }
  
  public AbstractInputMethodService.AbstractInputMethodImpl onCreateInputMethodInterface()
  {
    return new InputMethodImpl();
  }
  
  public AbstractInputMethodService.AbstractInputMethodSessionImpl onCreateInputMethodSessionInterface()
  {
    return new InputMethodSessionImpl();
  }
  
  public View onCreateInputView()
  {
    return null;
  }
  
  protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype paramInputMethodSubtype) {}
  
  public void onDestroy()
  {
    super.onDestroy();
    mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(mInsetsComputer);
    doFinishInput();
    if (mWindowAdded)
    {
      mWindow.getWindow().setWindowAnimations(0);
      mWindow.dismiss();
    }
    if (mSettingsObserver != null)
    {
      mSettingsObserver.unregister();
      mSettingsObserver = null;
    }
  }
  
  public void onDisplayCompletions(CompletionInfo[] paramArrayOfCompletionInfo) {}
  
  public boolean onEvaluateFullscreenMode()
  {
    if (getResourcesgetConfigurationorientation != 2) {
      return false;
    }
    return (mInputEditorInfo == null) || ((mInputEditorInfo.imeOptions & 0x2000000) == 0);
  }
  
  public boolean onEvaluateInputViewShown()
  {
    Object localObject = mSettingsObserver;
    boolean bool = false;
    if (localObject == null)
    {
      Log.w("InputMethodService", "onEvaluateInputViewShown: mSettingsObserver must not be null here.");
      return false;
    }
    if (mSettingsObserver.shouldShowImeWithHardKeyboard()) {
      return true;
    }
    localObject = getResources().getConfiguration();
    if ((keyboard != 1) && (hardKeyboardHidden != 2)) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public boolean onExtractTextContextMenuItem(int paramInt)
  {
    InputConnection localInputConnection = getCurrentInputConnection();
    if (localInputConnection != null) {
      localInputConnection.performContextMenuAction(paramInt);
    }
    return true;
  }
  
  public void onExtractedCursorMovement(int paramInt1, int paramInt2)
  {
    if ((mExtractEditText != null) && (paramInt2 != 0))
    {
      if (mExtractEditText.hasVerticalScrollBar()) {
        setCandidatesViewShown(false);
      }
      return;
    }
  }
  
  public void onExtractedDeleteText(int paramInt1, int paramInt2)
  {
    InputConnection localInputConnection = getCurrentInputConnection();
    if (localInputConnection != null)
    {
      localInputConnection.finishComposingText();
      localInputConnection.setSelection(paramInt1, paramInt1);
      localInputConnection.deleteSurroundingText(0, paramInt2 - paramInt1);
    }
  }
  
  public void onExtractedReplaceText(int paramInt1, int paramInt2, CharSequence paramCharSequence)
  {
    InputConnection localInputConnection = getCurrentInputConnection();
    if (localInputConnection != null)
    {
      localInputConnection.setComposingRegion(paramInt1, paramInt2);
      localInputConnection.commitText(paramCharSequence, 1);
    }
  }
  
  public void onExtractedSelectionChanged(int paramInt1, int paramInt2)
  {
    InputConnection localInputConnection = getCurrentInputConnection();
    if (localInputConnection != null) {
      localInputConnection.setSelection(paramInt1, paramInt2);
    }
  }
  
  public void onExtractedSetSpan(Object paramObject, int paramInt1, int paramInt2, int paramInt3)
  {
    InputConnection localInputConnection = getCurrentInputConnection();
    if (localInputConnection != null)
    {
      if (!localInputConnection.setSelection(paramInt1, paramInt2)) {
        return;
      }
      CharSequence localCharSequence = localInputConnection.getSelectedText(1);
      if ((localCharSequence instanceof Spannable))
      {
        ((Spannable)localCharSequence).setSpan(paramObject, 0, localCharSequence.length(), paramInt3);
        localInputConnection.setComposingRegion(paramInt1, paramInt2);
        localInputConnection.commitText(localCharSequence, 1);
      }
    }
  }
  
  public void onExtractedTextClicked()
  {
    if (mExtractEditText == null) {
      return;
    }
    if (mExtractEditText.hasVerticalScrollBar()) {
      setCandidatesViewShown(false);
    }
  }
  
  public void onExtractingInputChanged(EditorInfo paramEditorInfo)
  {
    if (inputType == 0) {
      requestHideSelf(2);
    }
  }
  
  public void onFinishCandidatesView(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      InputConnection localInputConnection = getCurrentInputConnection();
      if (localInputConnection != null) {
        localInputConnection.finishComposingText();
      }
    }
  }
  
  public void onFinishInput()
  {
    InputConnection localInputConnection = getCurrentInputConnection();
    if (localInputConnection != null) {
      localInputConnection.finishComposingText();
    }
  }
  
  public void onFinishInputView(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      InputConnection localInputConnection = getCurrentInputConnection();
      if (localInputConnection != null) {
        localInputConnection.finishComposingText();
      }
    }
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public void onInitializeInterface() {}
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCode() == 4)
    {
      ExtractEditText localExtractEditText = getExtractEditTextIfVisible();
      if ((localExtractEditText != null) && (localExtractEditText.handleBackInTextActionModeIfNeeded(paramKeyEvent))) {
        return true;
      }
      if (handleBack(false))
      {
        paramKeyEvent.startTracking();
        return true;
      }
      return false;
    }
    return doMovementKey(paramInt, paramKeyEvent, -1);
  }
  
  public boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    return doMovementKey(paramInt1, paramKeyEvent, paramInt2);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCode() == 4)
    {
      ExtractEditText localExtractEditText = getExtractEditTextIfVisible();
      if ((localExtractEditText != null) && (localExtractEditText.handleBackInTextActionModeIfNeeded(paramKeyEvent))) {
        return true;
      }
      if ((paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled())) {
        return handleBack(true);
      }
    }
    return doMovementKey(paramInt, paramKeyEvent, -2);
  }
  
  public boolean onShowInputRequested(int paramInt, boolean paramBoolean)
  {
    if (!onEvaluateInputViewShown()) {
      return false;
    }
    if ((paramInt & 0x1) == 0)
    {
      if ((!paramBoolean) && (onEvaluateFullscreenMode())) {
        return false;
      }
      if ((!mSettingsObserver.shouldShowImeWithHardKeyboard()) && (getResourcesgetConfigurationkeyboard != 1)) {
        return false;
      }
    }
    return true;
  }
  
  public void onStartCandidatesView(EditorInfo paramEditorInfo, boolean paramBoolean) {}
  
  public void onStartInput(EditorInfo paramEditorInfo, boolean paramBoolean) {}
  
  public void onStartInputView(EditorInfo paramEditorInfo, boolean paramBoolean) {}
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public void onUnbindInput() {}
  
  @Deprecated
  public void onUpdateCursor(Rect paramRect) {}
  
  public void onUpdateCursorAnchorInfo(CursorAnchorInfo paramCursorAnchorInfo) {}
  
  public void onUpdateExtractedText(int paramInt, ExtractedText paramExtractedText)
  {
    if (mExtractedToken != paramInt) {
      return;
    }
    if ((paramExtractedText != null) && (mExtractEditText != null))
    {
      mExtractedText = paramExtractedText;
      mExtractEditText.setExtractedText(paramExtractedText);
    }
  }
  
  public void onUpdateExtractingViews(EditorInfo paramEditorInfo)
  {
    if (!isExtractViewShown()) {
      return;
    }
    if (mExtractAccessories == null) {
      return;
    }
    CharSequence localCharSequence = actionLabel;
    int i = 1;
    int j = i;
    if (localCharSequence == null) {
      if (((imeOptions & 0xFF) != 1) && ((imeOptions & 0x20000000) == 0) && (inputType != 0)) {
        j = i;
      } else {
        j = 0;
      }
    }
    if (j != 0)
    {
      mExtractAccessories.setVisibility(0);
      if (mExtractAction != null)
      {
        if ((mExtractAction instanceof ImageButton))
        {
          ((ImageButton)mExtractAction).setImageResource(getIconForImeAction(imeOptions));
          if (actionLabel != null) {
            mExtractAction.setContentDescription(actionLabel);
          } else {
            mExtractAction.setContentDescription(getTextForImeAction(imeOptions));
          }
        }
        else if (actionLabel != null)
        {
          ((TextView)mExtractAction).setText(actionLabel);
        }
        else
        {
          ((TextView)mExtractAction).setText(getTextForImeAction(imeOptions));
        }
        mExtractAction.setOnClickListener(mActionClickListener);
      }
    }
    else
    {
      mExtractAccessories.setVisibility(8);
      if (mExtractAction != null) {
        mExtractAction.setOnClickListener(null);
      }
    }
  }
  
  public void onUpdateExtractingVisibility(EditorInfo paramEditorInfo)
  {
    if ((inputType != 0) && ((imeOptions & 0x10000000) == 0))
    {
      setExtractViewShown(true);
      return;
    }
    setExtractViewShown(false);
  }
  
  public void onUpdateSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    ExtractEditText localExtractEditText = mExtractEditText;
    if ((localExtractEditText != null) && (isFullscreenMode()) && (mExtractedText != null))
    {
      paramInt1 = mExtractedText.startOffset;
      localExtractEditText.startInternalChanges();
      paramInt2 = paramInt3 - paramInt1;
      paramInt4 -= paramInt1;
      paramInt3 = localExtractEditText.getText().length();
      if (paramInt2 < 0)
      {
        paramInt1 = 0;
      }
      else
      {
        paramInt1 = paramInt2;
        if (paramInt2 > paramInt3) {
          paramInt1 = paramInt3;
        }
      }
      if (paramInt4 < 0)
      {
        paramInt2 = 0;
      }
      else
      {
        paramInt2 = paramInt4;
        if (paramInt4 > paramInt3) {
          paramInt2 = paramInt3;
        }
      }
      localExtractEditText.setSelection(paramInt1, paramInt2);
      localExtractEditText.finishInternalChanges();
    }
  }
  
  public void onViewClicked(boolean paramBoolean) {}
  
  public void onWindowHidden() {}
  
  public void onWindowShown() {}
  
  void reportExtractedMovement(int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = 0;
    switch (paramInt1)
    {
    default: 
      paramInt1 = i;
      paramInt2 = j;
      break;
    case 22: 
      paramInt1 = paramInt2;
      paramInt2 = j;
      break;
    case 21: 
      paramInt1 = -paramInt2;
      paramInt2 = j;
      break;
    case 20: 
      paramInt1 = i;
      break;
    case 19: 
      paramInt2 = -paramInt2;
      paramInt1 = i;
    }
    onExtractedCursorMovement(paramInt1, paramInt2);
  }
  
  public void requestHideSelf(int paramInt)
  {
    mImm.hideSoftInputFromInputMethodInternal(mToken, paramInt);
  }
  
  public final void requestShowSelf(int paramInt)
  {
    mImm.showSoftInputFromInputMethodInternal(mToken, paramInt);
  }
  
  public boolean sendDefaultEditorAction(boolean paramBoolean)
  {
    EditorInfo localEditorInfo = getCurrentInputEditorInfo();
    if ((localEditorInfo != null) && ((!paramBoolean) || ((imeOptions & 0x40000000) == 0)) && ((imeOptions & 0xFF) != 1))
    {
      InputConnection localInputConnection = getCurrentInputConnection();
      if (localInputConnection != null) {
        localInputConnection.performEditorAction(imeOptions & 0xFF);
      }
      return true;
    }
    return false;
  }
  
  public void sendDownUpKeyEvents(int paramInt)
  {
    InputConnection localInputConnection = getCurrentInputConnection();
    if (localInputConnection == null) {
      return;
    }
    long l = SystemClock.uptimeMillis();
    localInputConnection.sendKeyEvent(new KeyEvent(l, l, 0, paramInt, 0, 0, -1, 0, 6));
    localInputConnection.sendKeyEvent(new KeyEvent(l, SystemClock.uptimeMillis(), 1, paramInt, 0, 0, -1, 0, 6));
  }
  
  public void sendKeyChar(char paramChar)
  {
    if (paramChar != '\n')
    {
      if ((paramChar >= '0') && (paramChar <= '9'))
      {
        sendDownUpKeyEvents(paramChar - '0' + 7);
      }
      else
      {
        InputConnection localInputConnection = getCurrentInputConnection();
        if (localInputConnection != null) {
          localInputConnection.commitText(String.valueOf(paramChar), 1);
        }
      }
    }
    else if (!sendDefaultEditorAction(true)) {
      sendDownUpKeyEvents(66);
    }
  }
  
  public void setBackDisposition(int paramInt)
  {
    if (paramInt == mBackDisposition) {
      return;
    }
    if ((paramInt <= 3) && (paramInt >= 0))
    {
      mBackDisposition = paramInt;
      mImm.setImeWindowStatus(mToken, mStartInputToken, mapToImeWindowStatus(isInputViewShown()), mBackDisposition);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid back disposition value (");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(") specified.");
    Log.e("InputMethodService", localStringBuilder.toString());
  }
  
  public void setCandidatesView(View paramView)
  {
    mCandidatesFrame.removeAllViews();
    mCandidatesFrame.addView(paramView, new FrameLayout.LayoutParams(-1, -2));
  }
  
  public void setCandidatesViewShown(boolean paramBoolean)
  {
    updateCandidatesVisibility(paramBoolean);
    if ((!mShowInputRequested) && (mWindowVisible != paramBoolean)) {
      if (paramBoolean) {
        showWindow(false);
      } else {
        doHideWindow();
      }
    }
  }
  
  public void setExtractView(View paramView)
  {
    mExtractFrame.removeAllViews();
    mExtractFrame.addView(paramView, new FrameLayout.LayoutParams(-1, -1));
    mExtractView = paramView;
    if (paramView != null)
    {
      mExtractEditText = ((ExtractEditText)paramView.findViewById(16908325));
      mExtractEditText.setIME(this);
      mExtractAction = paramView.findViewById(16909040);
      if (mExtractAction != null) {
        mExtractAccessories = ((ViewGroup)paramView.findViewById(16909039));
      }
      startExtractingText(false);
    }
    else
    {
      mExtractEditText = null;
      mExtractAccessories = null;
      mExtractAction = null;
    }
  }
  
  public void setExtractViewShown(boolean paramBoolean)
  {
    if (mExtractViewHidden == paramBoolean)
    {
      mExtractViewHidden = (paramBoolean ^ true);
      updateExtractFrameVisibility();
    }
  }
  
  public void setInputView(View paramView)
  {
    mInputFrame.removeAllViews();
    mInputFrame.addView(paramView, new FrameLayout.LayoutParams(-1, -2));
    mInputView = paramView;
  }
  
  public void setTheme(int paramInt)
  {
    if (mWindow == null)
    {
      mTheme = paramInt;
      return;
    }
    throw new IllegalStateException("Must be called before onCreate()");
  }
  
  public final boolean shouldOfferSwitchingToNextInputMethod()
  {
    return mImm.shouldOfferSwitchingToNextInputMethodInternal(mToken);
  }
  
  public void showStatusIcon(int paramInt)
  {
    mStatusIcon = paramInt;
    mImm.showStatusIconInternal(mToken, getPackageName(), paramInt);
  }
  
  /* Error */
  public void showWindow(boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 479	android/inputmethodservice/InputMethodService:mInShowWindow	Z
    //   4: ifeq +13 -> 17
    //   7: ldc 46
    //   9: ldc_w 1129
    //   12: invokestatic 899	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   15: pop
    //   16: return
    //   17: aload_0
    //   18: aload_0
    //   19: getfield 241	android/inputmethodservice/InputMethodService:mWindowVisible	Z
    //   22: putfield 475	android/inputmethodservice/InputMethodService:mWindowWasVisible	Z
    //   25: aload_0
    //   26: iconst_1
    //   27: putfield 479	android/inputmethodservice/InputMethodService:mInShowWindow	Z
    //   30: aload_0
    //   31: iload_1
    //   32: invokevirtual 1132	android/inputmethodservice/InputMethodService:showWindowInner	(Z)V
    //   35: aload_0
    //   36: iconst_1
    //   37: putfield 475	android/inputmethodservice/InputMethodService:mWindowWasVisible	Z
    //   40: aload_0
    //   41: iconst_0
    //   42: putfield 479	android/inputmethodservice/InputMethodService:mInShowWindow	Z
    //   45: return
    //   46: astore_2
    //   47: goto +16 -> 63
    //   50: astore_2
    //   51: aload_0
    //   52: iconst_0
    //   53: putfield 241	android/inputmethodservice/InputMethodService:mWindowVisible	Z
    //   56: aload_0
    //   57: iconst_0
    //   58: putfield 469	android/inputmethodservice/InputMethodService:mWindowAdded	Z
    //   61: aload_2
    //   62: athrow
    //   63: aload_0
    //   64: iconst_1
    //   65: putfield 475	android/inputmethodservice/InputMethodService:mWindowWasVisible	Z
    //   68: aload_0
    //   69: iconst_0
    //   70: putfield 479	android/inputmethodservice/InputMethodService:mInShowWindow	Z
    //   73: aload_2
    //   74: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	75	0	this	InputMethodService
    //   0	75	1	paramBoolean	boolean
    //   46	1	2	localObject	Object
    //   50	24	2	localBadTokenException	WindowManager.BadTokenException
    // Exception table:
    //   from	to	target	type
    //   17	35	46	finally
    //   51	63	46	finally
    //   17	35	50	android/view/WindowManager$BadTokenException
  }
  
  void showWindowInner(boolean paramBoolean)
  {
    int i = 0;
    int j = mWindowVisible;
    if (isInputViewShown()) {
      k = 2;
    } else {
      k = 0;
    }
    int n = j | k;
    mWindowVisible = true;
    int k = i;
    if (!mShowInputRequested)
    {
      k = i;
      if (mInputStarted)
      {
        k = i;
        if (paramBoolean)
        {
          m = 1;
          mShowInputRequested = true;
        }
      }
    }
    initialize();
    updateFullscreenMode();
    updateInputViewShown();
    if ((!mWindowAdded) || (!mWindowCreated))
    {
      mWindowAdded = true;
      mWindowCreated = true;
      initialize();
      View localView = onCreateCandidatesView();
      if (localView != null) {
        setCandidatesView(localView);
      }
    }
    if (mShowInputRequested)
    {
      if (!mInputViewStarted)
      {
        mInputViewStarted = true;
        onStartInputView(mInputEditorInfo, false);
      }
    }
    else if (!mCandidatesViewStarted)
    {
      mCandidatesViewStarted = true;
      onStartCandidatesView(mInputEditorInfo, false);
    }
    if (m != 0) {
      startExtractingText(false);
    }
    int m = mapToImeWindowStatus(isInputViewShown());
    if (n != m) {
      mImm.setImeWindowStatus(mToken, mStartInputToken, m, mBackDisposition);
    }
    if ((n & 0x1) == 0)
    {
      onWindowShown();
      mWindow.show();
      mShouldClearInsetOfPreviousIme = false;
    }
  }
  
  void startExtractingText(boolean paramBoolean)
  {
    ExtractEditText localExtractEditText = mExtractEditText;
    if ((localExtractEditText != null) && (getCurrentInputStarted()) && (isFullscreenMode()))
    {
      mExtractedToken += 1;
      Object localObject1 = new ExtractedTextRequest();
      token = mExtractedToken;
      flags = 1;
      hintMaxLines = 10;
      hintMaxChars = 10000;
      InputConnection localInputConnection = getCurrentInputConnection();
      if (localInputConnection == null) {
        localObject1 = null;
      } else {
        localObject1 = localInputConnection.getExtractedText((ExtractedTextRequest)localObject1, 1);
      }
      mExtractedText = ((ExtractedText)localObject1);
      if ((mExtractedText == null) || (localInputConnection == null))
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Unexpected null in startExtractingText : mExtractedText = ");
        ((StringBuilder)localObject1).append(mExtractedText);
        ((StringBuilder)localObject1).append(", input connection = ");
        ((StringBuilder)localObject1).append(localInputConnection);
        Log.e("InputMethodService", ((StringBuilder)localObject1).toString());
      }
      localObject1 = getCurrentInputEditorInfo();
      try
      {
        localExtractEditText.startInternalChanges();
        onUpdateExtractingVisibility((EditorInfo)localObject1);
        onUpdateExtractingViews((EditorInfo)localObject1);
        int i = inputType;
        int j = i;
        if ((i & 0xF) == 1)
        {
          j = i;
          if ((0x40000 & i) != 0) {
            j = i | 0x20000;
          }
        }
        localExtractEditText.setInputType(j);
        localExtractEditText.setHint(hintText);
        if (mExtractedText != null)
        {
          localExtractEditText.setEnabled(true);
          localExtractEditText.setExtractedText(mExtractedText);
        }
        else
        {
          localExtractEditText.setEnabled(false);
          localExtractEditText.setText("");
        }
        localExtractEditText.finishInternalChanges();
        if (paramBoolean) {
          onExtractingInputChanged((EditorInfo)localObject1);
        }
      }
      finally
      {
        localExtractEditText.finishInternalChanges();
      }
    }
  }
  
  public void switchInputMethod(String paramString)
  {
    mImm.setInputMethodInternal(mToken, paramString);
  }
  
  public final void switchInputMethod(String paramString, InputMethodSubtype paramInputMethodSubtype)
  {
    mImm.setInputMethodAndSubtypeInternal(mToken, paramString, paramInputMethodSubtype);
  }
  
  public final boolean switchToNextInputMethod(boolean paramBoolean)
  {
    return mImm.switchToNextInputMethodInternal(mToken, paramBoolean);
  }
  
  public final boolean switchToPreviousInputMethod()
  {
    return mImm.switchToPreviousInputMethodInternal(mToken);
  }
  
  void updateCandidatesVisibility(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 0;
    } else {
      i = getCandidatesHiddenVisibility();
    }
    if (mCandidatesVisibility != i)
    {
      mCandidatesFrame.setVisibility(i);
      mCandidatesVisibility = i;
    }
  }
  
  void updateExtractFrameVisibility()
  {
    int i;
    if (isFullscreenMode())
    {
      if (mExtractViewHidden) {
        i = 4;
      } else {
        i = 0;
      }
      mExtractFrame.setVisibility(i);
    }
    else
    {
      i = 0;
      mExtractFrame.setVisibility(8);
    }
    int j = mCandidatesVisibility;
    int k = 1;
    boolean bool;
    if (j == 0) {
      bool = true;
    } else {
      bool = false;
    }
    updateCandidatesVisibility(bool);
    if ((mWindowWasVisible) && (mFullscreenArea.getVisibility() != i))
    {
      TypedArray localTypedArray = mThemeAttrs;
      if (i != 0) {
        k = 2;
      }
      k = localTypedArray.getResourceId(k, 0);
      if (k != 0) {
        mFullscreenArea.startAnimation(AnimationUtils.loadAnimation(this, k));
      }
    }
    mFullscreenArea.setVisibility(i);
  }
  
  public void updateFullscreenMode()
  {
    boolean bool;
    if ((mShowInputRequested) && (onEvaluateFullscreenMode())) {
      bool = true;
    } else {
      bool = false;
    }
    int i;
    if (mLastShowInputRequested != mShowInputRequested) {
      i = 1;
    } else {
      i = 0;
    }
    if ((mIsFullscreen != bool) || (!mFullscreenApplied))
    {
      i = 1;
      mIsFullscreen = bool;
      if ((mImm != null) && (mToken != null)) {
        mImm.reportFullscreenMode(mToken, mIsFullscreen);
      }
      mFullscreenApplied = true;
      initialize();
      Object localObject = (LinearLayout.LayoutParams)mFullscreenArea.getLayoutParams();
      if (bool)
      {
        mFullscreenArea.setBackgroundDrawable(mThemeAttrs.getDrawable(0));
        height = 0;
        weight = 1.0F;
      }
      else
      {
        mFullscreenArea.setBackgroundDrawable(null);
        height = -2;
        weight = 0.0F;
      }
      ((ViewGroup)mFullscreenArea.getParent()).updateViewLayout(mFullscreenArea, (ViewGroup.LayoutParams)localObject);
      if (bool)
      {
        if (mExtractView == null)
        {
          localObject = onCreateExtractTextView();
          if (localObject != null) {
            setExtractView((View)localObject);
          }
        }
        startExtractingText(false);
      }
      updateExtractFrameVisibility();
    }
    if (i != 0)
    {
      onConfigureWindow(mWindow.getWindow(), bool, true ^ mShowInputRequested);
      mLastShowInputRequested = mShowInputRequested;
    }
  }
  
  public void updateInputViewShown()
  {
    boolean bool = mShowInputRequested;
    int i = 0;
    if ((bool) && (onEvaluateInputViewShown())) {
      bool = true;
    } else {
      bool = false;
    }
    if ((mIsInputViewShown != bool) && (mWindowVisible))
    {
      mIsInputViewShown = bool;
      Object localObject = mInputFrame;
      if (!bool) {
        i = 8;
      }
      ((FrameLayout)localObject).setVisibility(i);
      if (mInputView == null)
      {
        initialize();
        localObject = onCreateInputView();
        if (localObject != null) {
          setInputView((View)localObject);
        }
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BackDispositionMode {}
  
  public class InputMethodImpl
    extends AbstractInputMethodService.AbstractInputMethodImpl
  {
    public InputMethodImpl()
    {
      super();
    }
    
    public void attachToken(IBinder paramIBinder)
    {
      if (mToken == null)
      {
        mToken = paramIBinder;
        mWindow.setToken(paramIBinder);
      }
    }
    
    public void bindInput(InputBinding paramInputBinding)
    {
      mInputBinding = paramInputBinding;
      mInputConnection = paramInputBinding.getConnection();
      if ((mImm != null) && (mToken != null)) {
        mImm.reportFullscreenMode(mToken, mIsFullscreen);
      }
      initialize();
      onBindInput();
    }
    
    public void changeInputMethodSubtype(InputMethodSubtype paramInputMethodSubtype)
    {
      onCurrentInputMethodSubtypeChanged(paramInputMethodSubtype);
    }
    
    public void dispatchStartInputWithToken(InputConnection paramInputConnection, EditorInfo paramEditorInfo, boolean paramBoolean, IBinder paramIBinder)
    {
      InputMethodService.access$002(InputMethodService.this, paramIBinder);
      super.dispatchStartInputWithToken(paramInputConnection, paramEditorInfo, paramBoolean, paramIBinder);
    }
    
    public void hideSoftInput(int paramInt, ResultReceiver paramResultReceiver)
    {
      boolean bool = isInputViewShown();
      InputMethodService localInputMethodService = InputMethodService.this;
      paramInt = 0;
      mShowInputFlags = 0;
      mShowInputRequested = false;
      InputMethodService.this.doHideWindow();
      InputMethodService.this.clearInsetOfPreviousIme();
      if (paramResultReceiver != null)
      {
        if (bool != isInputViewShown()) {
          paramInt = 3;
        } else if (!bool) {
          paramInt = 1;
        }
        paramResultReceiver.send(paramInt, null);
      }
    }
    
    public void restartInput(InputConnection paramInputConnection, EditorInfo paramEditorInfo)
    {
      doStartInput(paramInputConnection, paramEditorInfo, true);
    }
    
    public void showSoftInput(int paramInt, ResultReceiver paramResultReceiver)
    {
      boolean bool = isInputViewShown();
      InputMethodService localInputMethodService = InputMethodService.this;
      int i = 0;
      if (localInputMethodService.dispatchOnShowInputRequested(paramInt, false)) {
        try
        {
          showWindow(true);
        }
        catch (WindowManager.BadTokenException localBadTokenException) {}
      }
      InputMethodService.this.clearInsetOfPreviousIme();
      mImm.setImeWindowStatus(mToken, mStartInputToken, InputMethodService.mapToImeWindowStatus(isInputViewShown()), mBackDisposition);
      if (paramResultReceiver != null)
      {
        if (bool != isInputViewShown()) {
          paramInt = 2;
        } else if (bool) {
          paramInt = i;
        } else {
          paramInt = 1;
        }
        paramResultReceiver.send(paramInt, null);
      }
    }
    
    public void startInput(InputConnection paramInputConnection, EditorInfo paramEditorInfo)
    {
      doStartInput(paramInputConnection, paramEditorInfo, false);
    }
    
    public void unbindInput()
    {
      onUnbindInput();
      mInputBinding = null;
      mInputConnection = null;
    }
  }
  
  public class InputMethodSessionImpl
    extends AbstractInputMethodService.AbstractInputMethodSessionImpl
  {
    public InputMethodSessionImpl()
    {
      super();
    }
    
    public void appPrivateCommand(String paramString, Bundle paramBundle)
    {
      if (!isEnabled()) {
        return;
      }
      onAppPrivateCommand(paramString, paramBundle);
    }
    
    public void displayCompletions(CompletionInfo[] paramArrayOfCompletionInfo)
    {
      if (!isEnabled()) {
        return;
      }
      mCurCompletions = paramArrayOfCompletionInfo;
      onDisplayCompletions(paramArrayOfCompletionInfo);
    }
    
    public void finishInput()
    {
      if (!isEnabled()) {
        return;
      }
      doFinishInput();
    }
    
    public void toggleSoftInput(int paramInt1, int paramInt2)
    {
      InputMethodService.this.onToggleSoftInput(paramInt1, paramInt2);
    }
    
    public void updateCursor(Rect paramRect)
    {
      if (!isEnabled()) {
        return;
      }
      onUpdateCursor(paramRect);
    }
    
    public void updateCursorAnchorInfo(CursorAnchorInfo paramCursorAnchorInfo)
    {
      if (!isEnabled()) {
        return;
      }
      onUpdateCursorAnchorInfo(paramCursorAnchorInfo);
    }
    
    public void updateExtractedText(int paramInt, ExtractedText paramExtractedText)
    {
      if (!isEnabled()) {
        return;
      }
      onUpdateExtractedText(paramInt, paramExtractedText);
    }
    
    public void updateSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
    {
      if (!isEnabled()) {
        return;
      }
      onUpdateSelection(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
    }
    
    public void viewClicked(boolean paramBoolean)
    {
      if (!isEnabled()) {
        return;
      }
      onViewClicked(paramBoolean);
    }
  }
  
  public static final class Insets
  {
    public static final int TOUCHABLE_INSETS_CONTENT = 1;
    public static final int TOUCHABLE_INSETS_FRAME = 0;
    public static final int TOUCHABLE_INSETS_REGION = 3;
    public static final int TOUCHABLE_INSETS_VISIBLE = 2;
    public int contentTopInsets;
    public int touchableInsets;
    public final Region touchableRegion = new Region();
    public int visibleTopInsets;
    
    public Insets() {}
  }
  
  private static final class SettingsObserver
    extends ContentObserver
  {
    private final InputMethodService mService;
    private int mShowImeWithHardKeyboard = 0;
    
    private SettingsObserver(InputMethodService paramInputMethodService)
    {
      super();
      mService = paramInputMethodService;
    }
    
    public static SettingsObserver createAndRegister(InputMethodService paramInputMethodService)
    {
      SettingsObserver localSettingsObserver = new SettingsObserver(paramInputMethodService);
      paramInputMethodService.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("show_ime_with_hard_keyboard"), false, localSettingsObserver);
      return localSettingsObserver;
    }
    
    private boolean shouldShowImeWithHardKeyboard()
    {
      if (mShowImeWithHardKeyboard == 0)
      {
        int i;
        if (Settings.Secure.getInt(mService.getContentResolver(), "show_ime_with_hard_keyboard", 0) != 0) {
          i = 2;
        } else {
          i = 1;
        }
        mShowImeWithHardKeyboard = i;
      }
      switch (mShowImeWithHardKeyboard)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unexpected mShowImeWithHardKeyboard=");
        localStringBuilder.append(mShowImeWithHardKeyboard);
        Log.e("InputMethodService", localStringBuilder.toString());
        return false;
      case 2: 
        return true;
      }
      return false;
    }
    
    public void onChange(boolean paramBoolean, Uri paramUri)
    {
      if (Settings.Secure.getUriFor("show_ime_with_hard_keyboard").equals(paramUri))
      {
        int i;
        if (Settings.Secure.getInt(mService.getContentResolver(), "show_ime_with_hard_keyboard", 0) != 0) {
          i = 2;
        } else {
          i = 1;
        }
        mShowImeWithHardKeyboard = i;
        mService.resetStateForNewConfiguration();
      }
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SettingsObserver{mShowImeWithHardKeyboard=");
      localStringBuilder.append(mShowImeWithHardKeyboard);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    void unregister()
    {
      mService.getContentResolver().unregisterContentObserver(this);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private static @interface ShowImeWithHardKeyboardType
    {
      public static final int FALSE = 1;
      public static final int TRUE = 2;
      public static final int UNKNOWN = 0;
    }
  }
}
