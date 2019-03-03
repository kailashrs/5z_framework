package android.view.textclassifier;

import com.android.internal.util.Preconditions;

final class TextClassificationSession
  implements TextClassifier
{
  static final boolean DEBUG_LOG_ENABLED = true;
  private static final String LOG_TAG = "TextClassificationSession";
  private final TextClassificationContext mClassificationContext;
  private final TextClassifier mDelegate;
  private boolean mDestroyed;
  private final SelectionEventHelper mEventHelper;
  private final TextClassificationSessionId mSessionId;
  
  TextClassificationSession(TextClassificationContext paramTextClassificationContext, TextClassifier paramTextClassifier)
  {
    mClassificationContext = ((TextClassificationContext)Preconditions.checkNotNull(paramTextClassificationContext));
    mDelegate = ((TextClassifier)Preconditions.checkNotNull(paramTextClassifier));
    mSessionId = new TextClassificationSessionId();
    mEventHelper = new SelectionEventHelper(mSessionId, mClassificationContext);
    initializeRemoteSession();
  }
  
  private void checkDestroyed()
  {
    if (!mDestroyed) {
      return;
    }
    throw new IllegalStateException("This TextClassification session has been destroyed");
  }
  
  private void initializeRemoteSession()
  {
    if ((mDelegate instanceof SystemTextClassifier)) {
      ((SystemTextClassifier)mDelegate).initializeRemoteSession(mClassificationContext, mSessionId);
    }
  }
  
  public TextClassification classifyText(TextClassification.Request paramRequest)
  {
    checkDestroyed();
    return mDelegate.classifyText(paramRequest);
  }
  
  public void destroy()
  {
    mEventHelper.endSession();
    mDelegate.destroy();
    mDestroyed = true;
  }
  
  public TextLinks generateLinks(TextLinks.Request paramRequest)
  {
    checkDestroyed();
    return mDelegate.generateLinks(paramRequest);
  }
  
  public boolean isDestroyed()
  {
    return mDestroyed;
  }
  
  public void onSelectionEvent(SelectionEvent paramSelectionEvent)
  {
    checkDestroyed();
    Preconditions.checkNotNull(paramSelectionEvent);
    if (mEventHelper.sanitizeEvent(paramSelectionEvent)) {
      mDelegate.onSelectionEvent(paramSelectionEvent);
    }
  }
  
  public TextSelection suggestSelection(TextSelection.Request paramRequest)
  {
    checkDestroyed();
    return mDelegate.suggestSelection(paramRequest);
  }
  
  private static final class SelectionEventHelper
  {
    private final TextClassificationContext mContext;
    private int mInvocationMethod = 0;
    private SelectionEvent mPrevEvent;
    private final TextClassificationSessionId mSessionId;
    private SelectionEvent mSmartEvent;
    private SelectionEvent mStartEvent;
    
    SelectionEventHelper(TextClassificationSessionId paramTextClassificationSessionId, TextClassificationContext paramTextClassificationContext)
    {
      mSessionId = ((TextClassificationSessionId)Preconditions.checkNotNull(paramTextClassificationSessionId));
      mContext = ((TextClassificationContext)Preconditions.checkNotNull(paramTextClassificationContext));
    }
    
    private static boolean isPlatformLocalTextClassifierSmartSelection(String paramString)
    {
      return "androidtc".equals(SelectionSessionLogger.SignatureParser.getClassifierId(paramString));
    }
    
    private void modifyAutoSelectionEventType(SelectionEvent paramSelectionEvent)
    {
      switch (paramSelectionEvent.getEventType())
      {
      default: 
        return;
      }
      if (isPlatformLocalTextClassifierSmartSelection(paramSelectionEvent.getResultId()))
      {
        if (paramSelectionEvent.getAbsoluteEnd() - paramSelectionEvent.getAbsoluteStart() > 1) {
          paramSelectionEvent.setEventType(4);
        } else {
          paramSelectionEvent.setEventType(3);
        }
      }
      else {
        paramSelectionEvent.setEventType(5);
      }
    }
    
    private void updateInvocationMethod(SelectionEvent paramSelectionEvent)
    {
      paramSelectionEvent.setTextClassificationSessionContext(mContext);
      if (paramSelectionEvent.getInvocationMethod() == 0) {
        paramSelectionEvent.setInvocationMethod(mInvocationMethod);
      } else {
        mInvocationMethod = paramSelectionEvent.getInvocationMethod();
      }
    }
    
    void endSession()
    {
      mPrevEvent = null;
      mSmartEvent = null;
      mStartEvent = null;
    }
    
    boolean sanitizeEvent(SelectionEvent paramSelectionEvent)
    {
      updateInvocationMethod(paramSelectionEvent);
      modifyAutoSelectionEventType(paramSelectionEvent);
      int i = paramSelectionEvent.getEventType();
      boolean bool = false;
      if ((i != 1) && (mStartEvent == null))
      {
        Log.d("TextClassificationSession", "Selection session not yet started. Ignoring event");
        return false;
      }
      long l = System.currentTimeMillis();
      switch (paramSelectionEvent.getEventType())
      {
      default: 
        break;
      case 3: 
      case 4: 
        mSmartEvent = paramSelectionEvent;
        break;
      case 2: 
      case 5: 
        if ((mPrevEvent != null) && (mPrevEvent.getAbsoluteStart() == paramSelectionEvent.getAbsoluteStart()) && (mPrevEvent.getAbsoluteEnd() == paramSelectionEvent.getAbsoluteEnd())) {
          return false;
        }
        break;
      case 1: 
        if (paramSelectionEvent.getAbsoluteEnd() == paramSelectionEvent.getAbsoluteStart() + 1) {
          bool = true;
        }
        Preconditions.checkArgument(bool);
        paramSelectionEvent.setSessionId(mSessionId);
        mStartEvent = paramSelectionEvent;
      }
      paramSelectionEvent.setEventTime(l);
      if (mStartEvent != null) {
        paramSelectionEvent.setSessionId(mStartEvent.getSessionId()).setDurationSinceSessionStart(l - mStartEvent.getEventTime()).setStart(paramSelectionEvent.getAbsoluteStart() - mStartEvent.getAbsoluteStart()).setEnd(paramSelectionEvent.getAbsoluteEnd() - mStartEvent.getAbsoluteStart());
      }
      if (mSmartEvent != null) {
        paramSelectionEvent.setResultId(mSmartEvent.getResultId()).setSmartStart(mSmartEvent.getAbsoluteStart() - mStartEvent.getAbsoluteStart()).setSmartEnd(mSmartEvent.getAbsoluteEnd() - mStartEvent.getAbsoluteStart());
      }
      if (mPrevEvent != null) {
        paramSelectionEvent.setDurationSincePreviousEvent(l - mPrevEvent.getEventTime()).setEventIndex(mPrevEvent.getEventIndex() + 1);
      }
      mPrevEvent = paramSelectionEvent;
      return true;
    }
  }
}
