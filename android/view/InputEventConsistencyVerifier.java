package android.view;

import android.os.Build;
import android.util.Log;

public final class InputEventConsistencyVerifier
{
  private static final String EVENT_TYPE_GENERIC_MOTION = "GenericMotionEvent";
  private static final String EVENT_TYPE_KEY = "KeyEvent";
  private static final String EVENT_TYPE_TOUCH = "TouchEvent";
  private static final String EVENT_TYPE_TRACKBALL = "TrackballEvent";
  public static final int FLAG_RAW_DEVICE_INPUT = 1;
  private static final boolean IS_ENG_BUILD = Build.IS_ENG;
  private static final int RECENT_EVENTS_TO_LOG = 5;
  private int mButtonsPressed;
  private final Object mCaller;
  private InputEvent mCurrentEvent;
  private String mCurrentEventType;
  private final int mFlags;
  private boolean mHoverEntered;
  private KeyState mKeyStateList;
  private int mLastEventSeq;
  private String mLastEventType;
  private int mLastNestingLevel;
  private final String mLogTag;
  private int mMostRecentEventIndex;
  private InputEvent[] mRecentEvents;
  private boolean[] mRecentEventsUnhandled;
  private int mTouchEventStreamDeviceId = -1;
  private boolean mTouchEventStreamIsTainted;
  private int mTouchEventStreamPointers;
  private int mTouchEventStreamSource;
  private boolean mTouchEventStreamUnhandled;
  private boolean mTrackballDown;
  private boolean mTrackballUnhandled;
  private StringBuilder mViolationMessage;
  
  public InputEventConsistencyVerifier(Object paramObject, int paramInt)
  {
    this(paramObject, paramInt, null);
  }
  
  public InputEventConsistencyVerifier(Object paramObject, int paramInt, String paramString)
  {
    mCaller = paramObject;
    mFlags = paramInt;
    if (paramString == null) {
      paramString = "InputEventConsistencyVerifier";
    }
    mLogTag = paramString;
  }
  
  private void addKeyState(int paramInt1, int paramInt2, int paramInt3)
  {
    KeyState localKeyState = KeyState.obtain(paramInt1, paramInt2, paramInt3);
    next = mKeyStateList;
    mKeyStateList = localKeyState;
  }
  
  private static void appendEvent(StringBuilder paramStringBuilder, int paramInt, InputEvent paramInputEvent, boolean paramBoolean)
  {
    paramStringBuilder.append(paramInt);
    paramStringBuilder.append(": sent at ");
    paramStringBuilder.append(paramInputEvent.getEventTimeNano());
    paramStringBuilder.append(", ");
    if (paramBoolean) {
      paramStringBuilder.append("(unhandled) ");
    }
    paramStringBuilder.append(paramInputEvent);
  }
  
  private void ensureActionButtonIsNonZeroForThisAction(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getActionButton() == 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("No action button set. Action button should always be non-zero for ");
      localStringBuilder.append(MotionEvent.actionToString(paramMotionEvent.getAction()));
      problem(localStringBuilder.toString());
    }
  }
  
  private void ensureHistorySizeIsZeroForThisAction(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getHistorySize();
    if (i != 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("History size is ");
      localStringBuilder.append(i);
      localStringBuilder.append(" but it should always be 0 for ");
      localStringBuilder.append(MotionEvent.actionToString(paramMotionEvent.getAction()));
      problem(localStringBuilder.toString());
    }
  }
  
  private void ensureMetaStateIsNormalized(int paramInt)
  {
    int i = KeyEvent.normalizeMetaState(paramInt);
    if (i != paramInt) {
      problem(String.format("Metastate not normalized.  Was 0x%08x but expected 0x%08x.", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(i) }));
    }
  }
  
  private void ensurePointerCountIsOneForThisAction(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getPointerCount();
    if (i != 1)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Pointer count is ");
      localStringBuilder.append(i);
      localStringBuilder.append(" but it should always be 1 for ");
      localStringBuilder.append(MotionEvent.actionToString(paramMotionEvent.getAction()));
      problem(localStringBuilder.toString());
    }
  }
  
  private KeyState findKeyState(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    Object localObject = null;
    for (KeyState localKeyState = mKeyStateList; localKeyState != null; localKeyState = next)
    {
      if ((deviceId == paramInt1) && (source == paramInt2) && (keyCode == paramInt3))
      {
        if (paramBoolean)
        {
          if (localObject != null) {
            next = next;
          } else {
            mKeyStateList = next;
          }
          next = null;
        }
        return localKeyState;
      }
      localObject = localKeyState;
    }
    return null;
  }
  
  private void finishEvent()
  {
    if ((mViolationMessage != null) && (mViolationMessage.length() != 0))
    {
      if (!mCurrentEvent.isTainted())
      {
        Object localObject = mViolationMessage;
        ((StringBuilder)localObject).append("\n  in ");
        ((StringBuilder)localObject).append(mCaller);
        mViolationMessage.append("\n  ");
        appendEvent(mViolationMessage, 0, mCurrentEvent, false);
        if (mRecentEvents != null)
        {
          mViolationMessage.append("\n  -- recent events --");
          for (i = 0; i < 5; i++)
          {
            int j = (mMostRecentEventIndex + 5 - i) % 5;
            localObject = mRecentEvents[j];
            if (localObject == null) {
              break;
            }
            mViolationMessage.append("\n  ");
            appendEvent(mViolationMessage, i + 1, (InputEvent)localObject, mRecentEventsUnhandled[j]);
          }
        }
        Log.d(mLogTag, mViolationMessage.toString());
        mCurrentEvent.setTainted(true);
      }
      mViolationMessage.setLength(0);
    }
    if (mRecentEvents == null)
    {
      mRecentEvents = new InputEvent[5];
      mRecentEventsUnhandled = new boolean[5];
    }
    int i = (mMostRecentEventIndex + 1) % 5;
    mMostRecentEventIndex = i;
    if (mRecentEvents[i] != null) {
      mRecentEvents[i].recycle();
    }
    mRecentEvents[i] = mCurrentEvent.copy();
    mRecentEventsUnhandled[i] = false;
    mCurrentEvent = null;
    mCurrentEventType = null;
  }
  
  public static boolean isInstrumentationEnabled()
  {
    return IS_ENG_BUILD;
  }
  
  private void problem(String paramString)
  {
    if (mViolationMessage == null) {
      mViolationMessage = new StringBuilder();
    }
    if (mViolationMessage.length() == 0)
    {
      StringBuilder localStringBuilder = mViolationMessage;
      localStringBuilder.append(mCurrentEventType);
      localStringBuilder.append(": ");
    }
    else
    {
      mViolationMessage.append("\n  ");
    }
    mViolationMessage.append(paramString);
  }
  
  private boolean startEvent(InputEvent paramInputEvent, int paramInt, String paramString)
  {
    int i = paramInputEvent.getSequenceNumber();
    if ((i == mLastEventSeq) && (paramInt < mLastNestingLevel) && (paramString == mLastEventType)) {
      return false;
    }
    if (paramInt > 0)
    {
      mLastEventSeq = i;
      mLastEventType = paramString;
      mLastNestingLevel = paramInt;
    }
    else
    {
      mLastEventSeq = -1;
      mLastEventType = null;
      mLastNestingLevel = 0;
    }
    mCurrentEvent = paramInputEvent;
    mCurrentEventType = paramString;
    return true;
  }
  
  public void onGenericMotionEvent(MotionEvent paramMotionEvent, int paramInt)
  {
    if (!startEvent(paramMotionEvent, paramInt, "GenericMotionEvent")) {
      return;
    }
    try
    {
      ensureMetaStateIsNormalized(paramMotionEvent.getMetaState());
      paramInt = paramMotionEvent.getAction();
      int i = paramMotionEvent.getSource();
      int j = paramMotionEvent.getButtonState();
      int k = paramMotionEvent.getActionButton();
      if ((i & 0x2) != 0)
      {
        switch (paramInt)
        {
        default: 
          break;
        case 12: 
          ensureActionButtonIsNonZeroForThisAction(paramMotionEvent);
          if ((mButtonsPressed & k) != k)
          {
            paramMotionEvent = new java/lang/StringBuilder;
            paramMotionEvent.<init>();
            paramMotionEvent.append("Action button for ACTION_BUTTON_RELEASE event is ");
            paramMotionEvent.append(k);
            paramMotionEvent.append(", but it was either never pressed or has already been released.");
            problem(paramMotionEvent.toString());
          }
          mButtonsPressed &= k;
          if ((k == 32) && ((j & 0x2) == 0)) {
            mButtonsPressed &= 0xFFFFFFFD;
          } else if ((k == 64) && ((j & 0x4) == 0)) {
            mButtonsPressed &= 0xFFFFFFFB;
          }
          if (mButtonsPressed == j) {
            break label513;
          }
          problem(String.format("Reported button state differs from expected button state based on press and release events. Is 0x%08x but expected 0x%08x.", new Object[] { Integer.valueOf(j), Integer.valueOf(mButtonsPressed) }));
          break;
        case 11: 
          ensureActionButtonIsNonZeroForThisAction(paramMotionEvent);
          if ((mButtonsPressed & k) != 0)
          {
            paramMotionEvent = new java/lang/StringBuilder;
            paramMotionEvent.<init>();
            paramMotionEvent.append("Action button for ACTION_BUTTON_PRESS event is ");
            paramMotionEvent.append(k);
            paramMotionEvent.append(", but it has already been pressed and has yet to be released.");
            problem(paramMotionEvent.toString());
          }
          mButtonsPressed |= k;
          if ((k == 32) && ((j & 0x2) != 0)) {
            mButtonsPressed |= 0x2;
          } else if ((k == 64) && ((j & 0x4) != 0)) {
            mButtonsPressed |= 0x4;
          }
          if (mButtonsPressed == j) {
            break label513;
          }
          problem(String.format("Reported button state differs from expected button state based on press and release events. Is 0x%08x but expected 0x%08x.", new Object[] { Integer.valueOf(j), Integer.valueOf(mButtonsPressed) }));
          break;
        case 10: 
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          if (!mHoverEntered) {
            problem("ACTION_HOVER_EXIT without prior ACTION_HOVER_ENTER");
          }
          mHoverEntered = false;
          break;
        case 9: 
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          mHoverEntered = true;
          break;
        case 8: 
          ensureHistorySizeIsZeroForThisAction(paramMotionEvent);
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          break;
        case 7: 
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          break;
        }
        problem("Invalid action for generic pointer event.");
      }
      else if ((i & 0x10) != 0)
      {
        if (paramInt != 2) {
          problem("Invalid action for generic joystick event.");
        } else {
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
        }
      }
      label513:
      return;
    }
    finally
    {
      finishEvent();
    }
  }
  
  public void onInputEvent(InputEvent paramInputEvent, int paramInt)
  {
    if ((paramInputEvent instanceof KeyEvent))
    {
      onKeyEvent((KeyEvent)paramInputEvent, paramInt);
    }
    else
    {
      paramInputEvent = (MotionEvent)paramInputEvent;
      if (paramInputEvent.isTouchEvent()) {
        onTouchEvent(paramInputEvent, paramInt);
      } else if ((paramInputEvent.getSource() & 0x4) != 0) {
        onTrackballEvent(paramInputEvent, paramInt);
      } else {
        onGenericMotionEvent(paramInputEvent, paramInt);
      }
    }
  }
  
  public void onKeyEvent(KeyEvent paramKeyEvent, int paramInt)
  {
    if (!startEvent(paramKeyEvent, paramInt, "KeyEvent")) {
      return;
    }
    try
    {
      ensureMetaStateIsNormalized(paramKeyEvent.getMetaState());
      int i = paramKeyEvent.getAction();
      paramInt = paramKeyEvent.getDeviceId();
      int j = paramKeyEvent.getSource();
      int k = paramKeyEvent.getKeyCode();
      switch (i)
      {
      default: 
        paramKeyEvent = new java/lang/StringBuilder;
        break;
      case 2: 
        break;
      case 1: 
        paramKeyEvent = findKeyState(paramInt, j, k, true);
        if (paramKeyEvent == null) {
          problem("ACTION_UP but key was not down.");
        } else {
          paramKeyEvent.recycle();
        }
        break;
      case 0: 
        KeyState localKeyState = findKeyState(paramInt, j, k, false);
        if (localKeyState != null)
        {
          if (unhandled) {
            unhandled = false;
          } else if (((0x1 & mFlags) == 0) && (paramKeyEvent.getRepeatCount() == 0)) {
            problem("ACTION_DOWN but key is already down and this event is not a key repeat.");
          }
        }
        else {
          addKeyState(paramInt, j, k);
        }
        break;
      }
      paramKeyEvent.<init>();
      paramKeyEvent.append("Invalid action ");
      paramKeyEvent.append(KeyEvent.actionToString(i));
      paramKeyEvent.append(" for key event.");
      problem(paramKeyEvent.toString());
      return;
    }
    finally
    {
      finishEvent();
    }
  }
  
  public void onTouchEvent(MotionEvent paramMotionEvent, int paramInt)
  {
    if (!startEvent(paramMotionEvent, paramInt, "TouchEvent")) {
      return;
    }
    int i = paramMotionEvent.getAction();
    if ((i != 0) && (i != 3) && (i != 4)) {
      paramInt = 0;
    } else {
      paramInt = 1;
    }
    if ((paramInt != 0) && ((mTouchEventStreamIsTainted) || (mTouchEventStreamUnhandled)))
    {
      mTouchEventStreamIsTainted = false;
      mTouchEventStreamUnhandled = false;
      mTouchEventStreamPointers = 0;
    }
    if (mTouchEventStreamIsTainted) {
      paramMotionEvent.setTainted(true);
    }
    try
    {
      ensureMetaStateIsNormalized(paramMotionEvent.getMetaState());
      int j = paramMotionEvent.getDeviceId();
      int k = paramMotionEvent.getSource();
      StringBuilder localStringBuilder;
      if ((paramInt == 0) && (mTouchEventStreamDeviceId != -1) && ((mTouchEventStreamDeviceId != j) || (mTouchEventStreamSource != k)))
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Touch event stream contains events from multiple sources: previous device id ");
        localStringBuilder.append(mTouchEventStreamDeviceId);
        localStringBuilder.append(", previous source ");
        localStringBuilder.append(Integer.toHexString(mTouchEventStreamSource));
        localStringBuilder.append(", new device id ");
        localStringBuilder.append(j);
        localStringBuilder.append(", new source ");
        localStringBuilder.append(Integer.toHexString(k));
        problem(localStringBuilder.toString());
      }
      mTouchEventStreamDeviceId = j;
      mTouchEventStreamSource = k;
      paramInt = paramMotionEvent.getPointerCount();
      if ((k & 0x2) != 0)
      {
        switch (i)
        {
        default: 
          j = paramMotionEvent.getActionMasked();
          break;
        case 4: 
          if (mTouchEventStreamPointers != 0) {
            problem("ACTION_OUTSIDE but pointers are still down.");
          }
          ensureHistorySizeIsZeroForThisAction(paramMotionEvent);
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          mTouchEventStreamIsTainted = false;
          break;
        case 3: 
          mTouchEventStreamPointers = 0;
          mTouchEventStreamIsTainted = false;
          break;
        case 2: 
          i = Integer.bitCount(mTouchEventStreamPointers);
          if (paramInt == i) {
            break label915;
          }
          paramMotionEvent = new java/lang/StringBuilder;
          paramMotionEvent.<init>();
          paramMotionEvent.append("ACTION_MOVE contained ");
          paramMotionEvent.append(paramInt);
          paramMotionEvent.append(" pointers but there are currently ");
          paramMotionEvent.append(i);
          paramMotionEvent.append(" pointers down.");
          problem(paramMotionEvent.toString());
          mTouchEventStreamIsTainted = true;
          break;
        case 1: 
          ensureHistorySizeIsZeroForThisAction(paramMotionEvent);
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          mTouchEventStreamPointers = 0;
          mTouchEventStreamIsTainted = false;
          break;
        case 0: 
          if (mTouchEventStreamPointers != 0) {
            problem("ACTION_DOWN but pointers are already down.  Probably missing ACTION_UP from previous gesture.");
          }
          ensureHistorySizeIsZeroForThisAction(paramMotionEvent);
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          mTouchEventStreamPointers = (1 << paramMotionEvent.getPointerId(0));
          break;
        }
        k = paramMotionEvent.getActionIndex();
        if (j == 5)
        {
          if (mTouchEventStreamPointers == 0)
          {
            problem("ACTION_POINTER_DOWN but no other pointers were down.");
            mTouchEventStreamIsTainted = true;
          }
          if ((k >= 0) && (k < paramInt))
          {
            i = paramMotionEvent.getPointerId(k);
            paramInt = 1 << i;
            if ((mTouchEventStreamPointers & paramInt) != 0)
            {
              localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("ACTION_POINTER_DOWN specified pointer id ");
              localStringBuilder.append(i);
              localStringBuilder.append(" which is already down.");
              problem(localStringBuilder.toString());
              mTouchEventStreamIsTainted = true;
            }
            else
            {
              mTouchEventStreamPointers |= paramInt;
            }
          }
          else
          {
            localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("ACTION_POINTER_DOWN index is ");
            localStringBuilder.append(k);
            localStringBuilder.append(" but the pointer count is ");
            localStringBuilder.append(paramInt);
            localStringBuilder.append(".");
            problem(localStringBuilder.toString());
            mTouchEventStreamIsTainted = true;
          }
          ensureHistorySizeIsZeroForThisAction(paramMotionEvent);
        }
        else if (j == 6)
        {
          if ((k >= 0) && (k < paramInt))
          {
            i = paramMotionEvent.getPointerId(k);
            paramInt = 1 << i;
            if ((mTouchEventStreamPointers & paramInt) == 0)
            {
              localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("ACTION_POINTER_UP specified pointer id ");
              localStringBuilder.append(i);
              localStringBuilder.append(" which is not currently down.");
              problem(localStringBuilder.toString());
              mTouchEventStreamIsTainted = true;
            }
            else
            {
              mTouchEventStreamPointers &= paramInt;
            }
          }
          else
          {
            localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("ACTION_POINTER_UP index is ");
            localStringBuilder.append(k);
            localStringBuilder.append(" but the pointer count is ");
            localStringBuilder.append(paramInt);
            localStringBuilder.append(".");
            problem(localStringBuilder.toString());
            mTouchEventStreamIsTainted = true;
          }
          ensureHistorySizeIsZeroForThisAction(paramMotionEvent);
        }
        else
        {
          paramMotionEvent = new java/lang/StringBuilder;
          paramMotionEvent.<init>();
          paramMotionEvent.append("Invalid action ");
          paramMotionEvent.append(MotionEvent.actionToString(i));
          paramMotionEvent.append(" for touch event.");
          problem(paramMotionEvent.toString());
        }
      }
      else
      {
        problem("Source was not SOURCE_CLASS_POINTER.");
      }
      label915:
      return;
    }
    finally
    {
      finishEvent();
    }
  }
  
  public void onTrackballEvent(MotionEvent paramMotionEvent, int paramInt)
  {
    if (!startEvent(paramMotionEvent, paramInt, "TrackballEvent")) {
      return;
    }
    try
    {
      ensureMetaStateIsNormalized(paramMotionEvent.getMetaState());
      paramInt = paramMotionEvent.getAction();
      if ((paramMotionEvent.getSource() & 0x4) != 0)
      {
        StringBuilder localStringBuilder;
        switch (paramInt)
        {
        default: 
          localStringBuilder = new java/lang/StringBuilder;
          break;
        case 2: 
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          break;
        case 1: 
          if (!mTrackballDown)
          {
            problem("ACTION_UP but trackball is not down.");
          }
          else
          {
            mTrackballDown = false;
            mTrackballUnhandled = false;
          }
          ensureHistorySizeIsZeroForThisAction(paramMotionEvent);
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          break;
        case 0: 
          if ((mTrackballDown) && (!mTrackballUnhandled))
          {
            problem("ACTION_DOWN but trackball is already down.");
          }
          else
          {
            mTrackballDown = true;
            mTrackballUnhandled = false;
          }
          ensureHistorySizeIsZeroForThisAction(paramMotionEvent);
          ensurePointerCountIsOneForThisAction(paramMotionEvent);
          break;
        }
        localStringBuilder.<init>();
        localStringBuilder.append("Invalid action ");
        localStringBuilder.append(MotionEvent.actionToString(paramInt));
        localStringBuilder.append(" for trackball event.");
        problem(localStringBuilder.toString());
        if ((mTrackballDown) && (paramMotionEvent.getPressure() <= 0.0F)) {
          problem("Trackball is down but pressure is not greater than 0.");
        } else if ((!mTrackballDown) && (paramMotionEvent.getPressure() != 0.0F)) {
          problem("Trackball is up but pressure is not equal to 0.");
        }
      }
      else
      {
        problem("Source was not SOURCE_CLASS_TRACKBALL.");
      }
      return;
    }
    finally
    {
      finishEvent();
    }
  }
  
  public void onUnhandledEvent(InputEvent paramInputEvent, int paramInt)
  {
    if (paramInt != mLastNestingLevel) {
      return;
    }
    if (mRecentEventsUnhandled != null) {
      mRecentEventsUnhandled[mMostRecentEventIndex] = true;
    }
    if ((paramInputEvent instanceof KeyEvent))
    {
      paramInputEvent = (KeyEvent)paramInputEvent;
      paramInputEvent = findKeyState(paramInputEvent.getDeviceId(), paramInputEvent.getSource(), paramInputEvent.getKeyCode(), false);
      if (paramInputEvent != null) {
        unhandled = true;
      }
    }
    else
    {
      paramInputEvent = (MotionEvent)paramInputEvent;
      if (paramInputEvent.isTouchEvent()) {
        mTouchEventStreamUnhandled = true;
      } else if (((paramInputEvent.getSource() & 0x4) != 0) && (mTrackballDown)) {
        mTrackballUnhandled = true;
      }
    }
  }
  
  public void reset()
  {
    mLastEventSeq = -1;
    mLastNestingLevel = 0;
    mTrackballDown = false;
    mTrackballUnhandled = false;
    mTouchEventStreamPointers = 0;
    mTouchEventStreamIsTainted = false;
    mTouchEventStreamUnhandled = false;
    mHoverEntered = false;
    mButtonsPressed = 0;
    while (mKeyStateList != null)
    {
      KeyState localKeyState = mKeyStateList;
      mKeyStateList = next;
      localKeyState.recycle();
    }
  }
  
  private static final class KeyState
  {
    private static KeyState mRecycledList;
    private static Object mRecycledListLock = new Object();
    public int deviceId;
    public int keyCode;
    public KeyState next;
    public int source;
    public boolean unhandled;
    
    private KeyState() {}
    
    public static KeyState obtain(int paramInt1, int paramInt2, int paramInt3)
    {
      synchronized (mRecycledListLock)
      {
        KeyState localKeyState = mRecycledList;
        if (localKeyState != null) {
          mRecycledList = next;
        } else {
          localKeyState = new KeyState();
        }
        deviceId = paramInt1;
        source = paramInt2;
        keyCode = paramInt3;
        unhandled = false;
        return localKeyState;
      }
    }
    
    public void recycle()
    {
      synchronized (mRecycledListLock)
      {
        next = mRecycledList;
        mRecycledList = next;
        return;
      }
    }
  }
}
