package com.android.internal.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Stream;

public class StateMachine
{
  public static final boolean HANDLED = true;
  public static final boolean NOT_HANDLED = false;
  private static final int SM_INIT_CMD = -2;
  private static final int SM_QUIT_CMD = -1;
  private String mName;
  private SmHandler mSmHandler;
  private HandlerThread mSmThread;
  
  protected StateMachine(String paramString)
  {
    mSmThread = new HandlerThread(paramString);
    mSmThread.start();
    initStateMachine(paramString, mSmThread.getLooper());
  }
  
  protected StateMachine(String paramString, Handler paramHandler)
  {
    initStateMachine(paramString, paramHandler.getLooper());
  }
  
  protected StateMachine(String paramString, Looper paramLooper)
  {
    initStateMachine(paramString, paramLooper);
  }
  
  private void initStateMachine(String paramString, Looper paramLooper)
  {
    mName = paramString;
    mSmHandler = new SmHandler(paramLooper, this, null);
  }
  
  public void addLogRec(String paramString)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    mLogRecords.add(this, localSmHandler.getCurrentMessage(), paramString, localSmHandler.getCurrentState(), mStateStack[mStateStackTopIndex].state, mDestState);
  }
  
  public final void addState(State paramState)
  {
    mSmHandler.addState(paramState, null);
  }
  
  public final void addState(State paramState1, State paramState2)
  {
    mSmHandler.addState(paramState1, paramState2);
  }
  
  public final Collection<LogRec> copyLogRecs()
  {
    Vector localVector = new Vector();
    Object localObject = mSmHandler;
    if (localObject != null)
    {
      localObject = access$1800mLogRecVector.iterator();
      while (((Iterator)localObject).hasNext()) {
        localVector.add((LogRec)((Iterator)localObject).next());
      }
    }
    return localVector;
  }
  
  public final void deferMessage(Message paramMessage)
  {
    mSmHandler.deferMessage(paramMessage);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(getName());
    paramFileDescriptor.append(":");
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" total records=");
    paramFileDescriptor.append(getLogRecCount());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (int i = 0; i < getLogRecSize(); i++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" rec[");
      paramFileDescriptor.append(i);
      paramFileDescriptor.append("]: ");
      paramFileDescriptor.append(getLogRec(i).toString());
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramPrintWriter.flush();
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("curState=");
    paramFileDescriptor.append(getCurrentState().getName());
    paramPrintWriter.println(paramFileDescriptor.toString());
  }
  
  public final Message getCurrentMessage()
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return null;
    }
    return localSmHandler.getCurrentMessage();
  }
  
  public final IState getCurrentState()
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return null;
    }
    return localSmHandler.getCurrentState();
  }
  
  public final Handler getHandler()
  {
    return mSmHandler;
  }
  
  public final LogRec getLogRec(int paramInt)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return null;
    }
    return mLogRecords.get(paramInt);
  }
  
  public final int getLogRecCount()
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return 0;
    }
    return mLogRecords.count();
  }
  
  @VisibleForTesting
  public final int getLogRecMaxSize()
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return 0;
    }
    return access$1800mMaxSize;
  }
  
  public final int getLogRecSize()
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return 0;
    }
    return mLogRecords.size();
  }
  
  protected String getLogRecString(Message paramMessage)
  {
    return "";
  }
  
  public final String getName()
  {
    return mName;
  }
  
  protected String getWhatToString(int paramInt)
  {
    return null;
  }
  
  protected void haltedProcessMessage(Message paramMessage) {}
  
  protected final boolean hasDeferredMessages(int paramInt)
  {
    Object localObject = mSmHandler;
    if (localObject == null) {
      return false;
    }
    localObject = mDeferredMessages.iterator();
    while (((Iterator)localObject).hasNext()) {
      if (nextwhat == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  protected final boolean hasMessages(int paramInt)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return false;
    }
    return localSmHandler.hasMessages(paramInt);
  }
  
  public boolean isDbg()
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return false;
    }
    return localSmHandler.isDbg();
  }
  
  protected final boolean isQuit(Message paramMessage)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null)
    {
      boolean bool;
      if (what == -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    return localSmHandler.isQuit(paramMessage);
  }
  
  protected void log(String paramString)
  {
    Log.d(mName, paramString);
  }
  
  protected void logAndAddLogRec(String paramString)
  {
    addLogRec(paramString);
    log(paramString);
  }
  
  protected void logd(String paramString)
  {
    Log.d(mName, paramString);
  }
  
  protected void loge(String paramString)
  {
    Log.e(mName, paramString);
  }
  
  protected void loge(String paramString, Throwable paramThrowable)
  {
    Log.e(mName, paramString, paramThrowable);
  }
  
  protected void logi(String paramString)
  {
    Log.i(mName, paramString);
  }
  
  protected void logv(String paramString)
  {
    Log.v(mName, paramString);
  }
  
  protected void logw(String paramString)
  {
    Log.w(mName, paramString);
  }
  
  public final Message obtainMessage()
  {
    return Message.obtain(mSmHandler);
  }
  
  public final Message obtainMessage(int paramInt)
  {
    return Message.obtain(mSmHandler, paramInt);
  }
  
  public final Message obtainMessage(int paramInt1, int paramInt2)
  {
    return Message.obtain(mSmHandler, paramInt1, paramInt2, 0);
  }
  
  public final Message obtainMessage(int paramInt1, int paramInt2, int paramInt3)
  {
    return Message.obtain(mSmHandler, paramInt1, paramInt2, paramInt3);
  }
  
  public final Message obtainMessage(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    return Message.obtain(mSmHandler, paramInt1, paramInt2, paramInt3, paramObject);
  }
  
  public final Message obtainMessage(int paramInt, Object paramObject)
  {
    return Message.obtain(mSmHandler, paramInt, paramObject);
  }
  
  protected void onHalting() {}
  
  protected void onPostHandleMessage(Message paramMessage) {}
  
  protected void onPreHandleMessage(Message paramMessage) {}
  
  protected void onQuitting() {}
  
  public final void quit()
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.quit();
  }
  
  public final void quitNow()
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.quitNow();
  }
  
  protected boolean recordLogRec(Message paramMessage)
  {
    return true;
  }
  
  protected final void removeDeferredMessages(int paramInt)
  {
    Object localObject = mSmHandler;
    if (localObject == null) {
      return;
    }
    localObject = mDeferredMessages.iterator();
    while (((Iterator)localObject).hasNext()) {
      if (nextwhat == paramInt) {
        ((Iterator)localObject).remove();
      }
    }
  }
  
  protected final void removeMessages(int paramInt)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.removeMessages(paramInt);
  }
  
  public final void removeState(State paramState)
  {
    mSmHandler.removeState(paramState);
  }
  
  public void sendMessage(int paramInt)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessage(obtainMessage(paramInt));
  }
  
  public void sendMessage(int paramInt1, int paramInt2)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessage(obtainMessage(paramInt1, paramInt2));
  }
  
  public void sendMessage(int paramInt1, int paramInt2, int paramInt3)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessage(obtainMessage(paramInt1, paramInt2, paramInt3));
  }
  
  public void sendMessage(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessage(obtainMessage(paramInt1, paramInt2, paramInt3, paramObject));
  }
  
  public void sendMessage(int paramInt, Object paramObject)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessage(obtainMessage(paramInt, paramObject));
  }
  
  public void sendMessage(Message paramMessage)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessage(paramMessage);
  }
  
  protected final void sendMessageAtFrontOfQueue(int paramInt)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageAtFrontOfQueue(obtainMessage(paramInt));
  }
  
  protected final void sendMessageAtFrontOfQueue(int paramInt1, int paramInt2)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageAtFrontOfQueue(obtainMessage(paramInt1, paramInt2));
  }
  
  protected final void sendMessageAtFrontOfQueue(int paramInt1, int paramInt2, int paramInt3)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageAtFrontOfQueue(obtainMessage(paramInt1, paramInt2, paramInt3));
  }
  
  protected final void sendMessageAtFrontOfQueue(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageAtFrontOfQueue(obtainMessage(paramInt1, paramInt2, paramInt3, paramObject));
  }
  
  protected final void sendMessageAtFrontOfQueue(int paramInt, Object paramObject)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageAtFrontOfQueue(obtainMessage(paramInt, paramObject));
  }
  
  protected final void sendMessageAtFrontOfQueue(Message paramMessage)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageAtFrontOfQueue(paramMessage);
  }
  
  public void sendMessageDelayed(int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageDelayed(obtainMessage(paramInt1, paramInt2, paramInt3), paramLong);
  }
  
  public void sendMessageDelayed(int paramInt1, int paramInt2, int paramInt3, Object paramObject, long paramLong)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageDelayed(obtainMessage(paramInt1, paramInt2, paramInt3, paramObject), paramLong);
  }
  
  public void sendMessageDelayed(int paramInt1, int paramInt2, long paramLong)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageDelayed(obtainMessage(paramInt1, paramInt2), paramLong);
  }
  
  public void sendMessageDelayed(int paramInt, long paramLong)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageDelayed(obtainMessage(paramInt), paramLong);
  }
  
  public void sendMessageDelayed(int paramInt, Object paramObject, long paramLong)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageDelayed(obtainMessage(paramInt, paramObject), paramLong);
  }
  
  public void sendMessageDelayed(Message paramMessage, long paramLong)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.sendMessageDelayed(paramMessage, paramLong);
  }
  
  public void setDbg(boolean paramBoolean)
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.setDbg(paramBoolean);
  }
  
  public final void setInitialState(State paramState)
  {
    mSmHandler.setInitialState(paramState);
  }
  
  public final void setLogOnlyTransitions(boolean paramBoolean)
  {
    mSmHandler.mLogRecords.setLogOnlyTransitions(paramBoolean);
  }
  
  public final void setLogRecSize(int paramInt)
  {
    mSmHandler.mLogRecords.setSize(paramInt);
  }
  
  public void start()
  {
    SmHandler localSmHandler = mSmHandler;
    if (localSmHandler == null) {
      return;
    }
    localSmHandler.completeConstruction();
  }
  
  public String toString()
  {
    Object localObject1 = "(null)";
    Object localObject2 = "(null)";
    Object localObject4;
    try
    {
      Object localObject3 = mName.toString();
      localObject1 = localObject3;
      String str = mSmHandler.getCurrentState().getName().toString();
      localObject2 = str;
      localObject1 = localObject3;
      localObject3 = localObject2;
    }
    catch (NullPointerException localNullPointerException)
    {
      localObject4 = localObject2;
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("name=");
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(" state=");
    ((StringBuilder)localObject2).append(localObject4);
    return ((StringBuilder)localObject2).toString();
  }
  
  public final void transitionTo(IState paramIState)
  {
    mSmHandler.transitionTo(paramIState);
  }
  
  public final void transitionToHaltingState()
  {
    mSmHandler.transitionTo(mSmHandler.mHaltingState);
  }
  
  protected void unhandledMessage(Message paramMessage)
  {
    if (mSmHandler.mDbg)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(" - unhandledMessage: msg.what=");
      localStringBuilder.append(what);
      loge(localStringBuilder.toString());
    }
  }
  
  public static class LogRec
  {
    private IState mDstState;
    private String mInfo;
    private IState mOrgState;
    private StateMachine mSm;
    private IState mState;
    private long mTime;
    private int mWhat;
    
    LogRec(StateMachine paramStateMachine, Message paramMessage, String paramString, IState paramIState1, IState paramIState2, IState paramIState3)
    {
      update(paramStateMachine, paramMessage, paramString, paramIState1, paramIState2, paramIState3);
    }
    
    public IState getDestState()
    {
      return mDstState;
    }
    
    public String getInfo()
    {
      return mInfo;
    }
    
    public IState getOriginalState()
    {
      return mOrgState;
    }
    
    public IState getState()
    {
      return mState;
    }
    
    public long getTime()
    {
      return mTime;
    }
    
    public long getWhat()
    {
      return mWhat;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("time=");
      Object localObject = Calendar.getInstance();
      ((Calendar)localObject).setTimeInMillis(mTime);
      localStringBuilder.append(String.format("%tm-%td %tH:%tM:%tS.%tL", new Object[] { localObject, localObject, localObject, localObject, localObject, localObject }));
      localStringBuilder.append(" processed=");
      if (mState == null) {
        localObject = "<null>";
      } else {
        localObject = mState.getName();
      }
      localStringBuilder.append((String)localObject);
      localStringBuilder.append(" org=");
      if (mOrgState == null) {
        localObject = "<null>";
      } else {
        localObject = mOrgState.getName();
      }
      localStringBuilder.append((String)localObject);
      localStringBuilder.append(" dest=");
      if (mDstState == null) {
        localObject = "<null>";
      } else {
        localObject = mDstState.getName();
      }
      localStringBuilder.append((String)localObject);
      localStringBuilder.append(" what=");
      if (mSm != null) {
        localObject = mSm.getWhatToString(mWhat);
      } else {
        localObject = "";
      }
      if (TextUtils.isEmpty((CharSequence)localObject))
      {
        localStringBuilder.append(mWhat);
        localStringBuilder.append("(0x");
        localStringBuilder.append(Integer.toHexString(mWhat));
        localStringBuilder.append(")");
      }
      else
      {
        localStringBuilder.append((String)localObject);
      }
      if (!TextUtils.isEmpty(mInfo))
      {
        localStringBuilder.append(" ");
        localStringBuilder.append(mInfo);
      }
      return localStringBuilder.toString();
    }
    
    public void update(StateMachine paramStateMachine, Message paramMessage, String paramString, IState paramIState1, IState paramIState2, IState paramIState3)
    {
      mSm = paramStateMachine;
      mTime = System.currentTimeMillis();
      int i;
      if (paramMessage != null) {
        i = what;
      } else {
        i = 0;
      }
      mWhat = i;
      mInfo = paramString;
      mState = paramIState1;
      mOrgState = paramIState2;
      mDstState = paramIState3;
    }
  }
  
  private static class LogRecords
  {
    private static final int DEFAULT_SIZE = 20;
    private int mCount = 0;
    private boolean mLogOnlyTransitions = false;
    private Vector<StateMachine.LogRec> mLogRecVector = new Vector();
    private int mMaxSize = 20;
    private int mOldestIndex = 0;
    
    private LogRecords() {}
    
    void add(StateMachine paramStateMachine, Message paramMessage, String paramString, IState paramIState1, IState paramIState2, IState paramIState3)
    {
      try
      {
        mCount += 1;
        StateMachine.LogRec localLogRec;
        if (mLogRecVector.size() < mMaxSize)
        {
          Vector localVector = mLogRecVector;
          localLogRec = new com/android/internal/util/StateMachine$LogRec;
          localLogRec.<init>(paramStateMachine, paramMessage, paramString, paramIState1, paramIState2, paramIState3);
          localVector.add(localLogRec);
        }
        else
        {
          localLogRec = (StateMachine.LogRec)mLogRecVector.get(mOldestIndex);
          mOldestIndex += 1;
          if (mOldestIndex >= mMaxSize) {
            mOldestIndex = 0;
          }
          localLogRec.update(paramStateMachine, paramMessage, paramString, paramIState1, paramIState2, paramIState3);
        }
        return;
      }
      finally {}
    }
    
    void cleanup()
    {
      try
      {
        mLogRecVector.clear();
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    int count()
    {
      try
      {
        int i = mCount;
        return i;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    StateMachine.LogRec get(int paramInt)
    {
      try
      {
        int i = mOldestIndex + paramInt;
        paramInt = i;
        if (i >= mMaxSize) {
          paramInt = i - mMaxSize;
        }
        i = size();
        if (paramInt >= i) {
          return null;
        }
        StateMachine.LogRec localLogRec = (StateMachine.LogRec)mLogRecVector.get(paramInt);
        return localLogRec;
      }
      finally {}
    }
    
    boolean logOnlyTransitions()
    {
      try
      {
        boolean bool = mLogOnlyTransitions;
        return bool;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    void setLogOnlyTransitions(boolean paramBoolean)
    {
      try
      {
        mLogOnlyTransitions = paramBoolean;
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    void setSize(int paramInt)
    {
      try
      {
        mMaxSize = paramInt;
        mOldestIndex = 0;
        mCount = 0;
        mLogRecVector.clear();
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    int size()
    {
      try
      {
        int i = mLogRecVector.size();
        return i;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }
  
  private static class SmHandler
    extends Handler
  {
    private static final Object mSmHandlerObj = new Object();
    private boolean mDbg = false;
    private ArrayList<Message> mDeferredMessages = new ArrayList();
    private State mDestState;
    private HaltingState mHaltingState = new HaltingState(null);
    private boolean mHasQuit = false;
    private State mInitialState;
    private boolean mIsConstructionCompleted;
    private StateMachine.LogRecords mLogRecords = new StateMachine.LogRecords(null);
    private Message mMsg;
    private QuittingState mQuittingState = new QuittingState(null);
    private StateMachine mSm;
    private HashMap<State, StateInfo> mStateInfo = new HashMap();
    private StateInfo[] mStateStack;
    private int mStateStackTopIndex = -1;
    private StateInfo[] mTempStateStack;
    private int mTempStateStackCount;
    private boolean mTransitionInProgress = false;
    
    private SmHandler(Looper paramLooper, StateMachine paramStateMachine)
    {
      super();
      mSm = paramStateMachine;
      addState(mHaltingState, null);
      addState(mQuittingState, null);
    }
    
    private final StateInfo addState(State paramState1, State paramState2)
    {
      if (mDbg)
      {
        StateMachine localStateMachine = mSm;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("addStateInternal: E state=");
        ((StringBuilder)localObject1).append(paramState1.getName());
        ((StringBuilder)localObject1).append(",parent=");
        if (paramState2 == null) {
          localObject2 = "";
        } else {
          localObject2 = paramState2.getName();
        }
        ((StringBuilder)localObject1).append((String)localObject2);
        localStateMachine.log(((StringBuilder)localObject1).toString());
      }
      Object localObject2 = null;
      if (paramState2 != null)
      {
        localObject1 = (StateInfo)mStateInfo.get(paramState2);
        localObject2 = localObject1;
        if (localObject1 == null) {
          localObject2 = addState(paramState2, null);
        }
      }
      Object localObject1 = (StateInfo)mStateInfo.get(paramState1);
      paramState2 = (State)localObject1;
      if (localObject1 == null)
      {
        paramState2 = new StateInfo(null);
        mStateInfo.put(paramState1, paramState2);
      }
      if ((parentStateInfo != null) && (parentStateInfo != localObject2)) {
        throw new RuntimeException("state already added");
      }
      state = paramState1;
      parentStateInfo = ((StateInfo)localObject2);
      active = false;
      if (mDbg)
      {
        paramState1 = mSm;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("addStateInternal: X stateInfo: ");
        ((StringBuilder)localObject2).append(paramState2);
        paramState1.log(((StringBuilder)localObject2).toString());
      }
      return paramState2;
    }
    
    private final void cleanupAfterQuitting()
    {
      if (mSm.mSmThread != null)
      {
        getLooper().quit();
        StateMachine.access$402(mSm, null);
      }
      StateMachine.access$502(mSm, null);
      mSm = null;
      mMsg = null;
      mLogRecords.cleanup();
      mStateStack = null;
      mTempStateStack = null;
      mStateInfo.clear();
      mInitialState = null;
      mDestState = null;
      mDeferredMessages.clear();
      mHasQuit = true;
    }
    
    private final void completeConstruction()
    {
      if (mDbg) {
        mSm.log("completeConstruction: E");
      }
      int i = 0;
      Object localObject1 = mStateInfo.values().iterator();
      Object localObject2;
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (StateInfo)((Iterator)localObject1).next();
        for (int j = 0; localObject2 != null; j++) {
          localObject2 = parentStateInfo;
        }
        int k = i;
        if (i < j) {
          k = j;
        }
        i = k;
      }
      if (mDbg)
      {
        localObject2 = mSm;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("completeConstruction: maxDepth=");
        ((StringBuilder)localObject1).append(i);
        ((StateMachine)localObject2).log(((StringBuilder)localObject1).toString());
      }
      mStateStack = new StateInfo[i];
      mTempStateStack = new StateInfo[i];
      setupInitialStateStack();
      sendMessageAtFrontOfQueue(obtainMessage(-2, mSmHandlerObj));
      if (mDbg) {
        mSm.log("completeConstruction: X");
      }
    }
    
    private final void deferMessage(Message paramMessage)
    {
      if (mDbg)
      {
        localObject = mSm;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("deferMessage: msg=");
        localStringBuilder.append(what);
        ((StateMachine)localObject).log(localStringBuilder.toString());
      }
      Object localObject = obtainMessage();
      ((Message)localObject).copyFrom(paramMessage);
      mDeferredMessages.add(localObject);
    }
    
    private final Message getCurrentMessage()
    {
      return mMsg;
    }
    
    private final IState getCurrentState()
    {
      return mStateStack[mStateStackTopIndex].state;
    }
    
    private final void invokeEnterMethods(int paramInt)
    {
      for (int i = paramInt; i <= mStateStackTopIndex; i++)
      {
        if (paramInt == mStateStackTopIndex) {
          mTransitionInProgress = false;
        }
        if (mDbg)
        {
          StateMachine localStateMachine = mSm;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("invokeEnterMethods: ");
          localStringBuilder.append(mStateStack[i].state.getName());
          localStateMachine.log(localStringBuilder.toString());
        }
        mStateStack[i].state.enter();
        mStateStack[i].active = true;
      }
      mTransitionInProgress = false;
    }
    
    private final void invokeExitMethods(StateInfo paramStateInfo)
    {
      while ((mStateStackTopIndex >= 0) && (mStateStack[mStateStackTopIndex] != paramStateInfo))
      {
        State localState = mStateStack[mStateStackTopIndex].state;
        if (mDbg)
        {
          StateMachine localStateMachine = mSm;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("invokeExitMethods: ");
          localStringBuilder.append(localState.getName());
          localStateMachine.log(localStringBuilder.toString());
        }
        localState.exit();
        mStateStack[mStateStackTopIndex].active = false;
        mStateStackTopIndex -= 1;
      }
    }
    
    private final boolean isDbg()
    {
      return mDbg;
    }
    
    private final boolean isQuit(Message paramMessage)
    {
      boolean bool;
      if ((what == -1) && (obj == mSmHandlerObj)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private final void moveDeferredMessageAtFrontOfQueue()
    {
      for (int i = mDeferredMessages.size() - 1; i >= 0; i--)
      {
        Message localMessage = (Message)mDeferredMessages.get(i);
        if (mDbg)
        {
          StateMachine localStateMachine = mSm;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("moveDeferredMessageAtFrontOfQueue; what=");
          localStringBuilder.append(what);
          localStateMachine.log(localStringBuilder.toString());
        }
        sendMessageAtFrontOfQueue(localMessage);
      }
      mDeferredMessages.clear();
    }
    
    private final int moveTempStateStackToStateStack()
    {
      int i = mStateStackTopIndex + 1;
      int j = mTempStateStackCount - 1;
      int k = i;
      Object localObject1;
      Object localObject2;
      while (j >= 0)
      {
        if (mDbg)
        {
          localObject1 = mSm;
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("moveTempStackToStateStack: i=");
          ((StringBuilder)localObject2).append(j);
          ((StringBuilder)localObject2).append(",j=");
          ((StringBuilder)localObject2).append(k);
          ((StateMachine)localObject1).log(((StringBuilder)localObject2).toString());
        }
        mStateStack[k] = mTempStateStack[j];
        k++;
        j--;
      }
      mStateStackTopIndex = (k - 1);
      if (mDbg)
      {
        localObject2 = mSm;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("moveTempStackToStateStack: X mStateStackTop=");
        ((StringBuilder)localObject1).append(mStateStackTopIndex);
        ((StringBuilder)localObject1).append(",startingIndex=");
        ((StringBuilder)localObject1).append(i);
        ((StringBuilder)localObject1).append(",Top=");
        ((StringBuilder)localObject1).append(mStateStack[mStateStackTopIndex].state.getName());
        ((StateMachine)localObject2).log(((StringBuilder)localObject1).toString());
      }
      return i;
    }
    
    private void performTransitions(State paramState, Message paramMessage)
    {
      State localState = mStateStack[mStateStackTopIndex].state;
      int i;
      if ((mSm.recordLogRec(mMsg)) && (obj != mSmHandlerObj)) {
        i = 1;
      } else {
        i = 0;
      }
      if (mLogRecords.logOnlyTransitions())
      {
        if (mDestState != null) {
          mLogRecords.add(mSm, mMsg, mSm.getLogRecString(mMsg), paramState, localState, mDestState);
        }
      }
      else if (i != 0) {
        mLogRecords.add(mSm, mMsg, mSm.getLogRecString(mMsg), paramState, localState, mDestState);
      }
      paramMessage = mDestState;
      paramState = paramMessage;
      if (paramMessage != null)
      {
        for (paramState = paramMessage;; paramState = mDestState)
        {
          if (mDbg) {
            mSm.log("handleMessage: new destination call exit/enter");
          }
          paramMessage = setupTempStateStackWithStatesToEnter(paramState);
          mTransitionInProgress = true;
          invokeExitMethods(paramMessage);
          invokeEnterMethods(moveTempStateStackToStateStack());
          moveDeferredMessageAtFrontOfQueue();
          if (paramState == mDestState) {
            break;
          }
        }
        mDestState = null;
      }
      if (paramState != null) {
        if (paramState == mQuittingState)
        {
          mSm.onQuitting();
          cleanupAfterQuitting();
        }
        else if (paramState == mHaltingState)
        {
          mSm.onHalting();
        }
      }
    }
    
    private final State processMsg(Message paramMessage)
    {
      Object localObject1 = mStateStack[mStateStackTopIndex];
      Object localObject3;
      if (mDbg)
      {
        localObject2 = mSm;
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("processMsg: ");
        ((StringBuilder)localObject3).append(state.getName());
        ((StateMachine)localObject2).log(((StringBuilder)localObject3).toString());
      }
      Object localObject2 = localObject1;
      if (isQuit(paramMessage)) {
        transitionTo(mQuittingState);
      } else {
        for (;;)
        {
          localObject1 = localObject2;
          if (state.processMessage(paramMessage)) {
            break;
          }
          localObject1 = parentStateInfo;
          if (localObject1 == null)
          {
            mSm.unhandledMessage(paramMessage);
            break;
          }
          localObject2 = localObject1;
          if (mDbg)
          {
            localObject3 = mSm;
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("processMsg: ");
            ((StringBuilder)localObject2).append(state.getName());
            ((StateMachine)localObject3).log(((StringBuilder)localObject2).toString());
            localObject2 = localObject1;
          }
        }
      }
      if (localObject1 != null) {
        paramMessage = state;
      } else {
        paramMessage = null;
      }
      return paramMessage;
    }
    
    private final void quit()
    {
      if (mDbg) {
        mSm.log("quit:");
      }
      sendMessage(obtainMessage(-1, mSmHandlerObj));
    }
    
    private final void quitNow()
    {
      if (mDbg) {
        mSm.log("quitNow:");
      }
      sendMessageAtFrontOfQueue(obtainMessage(-1, mSmHandlerObj));
    }
    
    private void removeState(State paramState)
    {
      StateInfo localStateInfo = (StateInfo)mStateInfo.get(paramState);
      if ((localStateInfo != null) && (!active))
      {
        if (mStateInfo.values().stream().filter(new _..Lambda.StateMachine.SmHandler.KkPO7NIVuI9r_FPEGrY6ux6a5Ks(localStateInfo)).findAny().isPresent()) {
          return;
        }
        mStateInfo.remove(paramState);
        return;
      }
    }
    
    private final void setDbg(boolean paramBoolean)
    {
      mDbg = paramBoolean;
    }
    
    private final void setInitialState(State paramState)
    {
      if (mDbg)
      {
        StateMachine localStateMachine = mSm;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("setInitialState: initialState=");
        localStringBuilder.append(paramState.getName());
        localStateMachine.log(localStringBuilder.toString());
      }
      mInitialState = paramState;
    }
    
    private final void setupInitialStateStack()
    {
      if (mDbg)
      {
        localObject = mSm;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("setupInitialStateStack: E mInitialState=");
        localStringBuilder.append(mInitialState.getName());
        ((StateMachine)localObject).log(localStringBuilder.toString());
      }
      Object localObject = (StateInfo)mStateInfo.get(mInitialState);
      for (int i = 0;; i = mTempStateStackCount + 1)
      {
        mTempStateStackCount = i;
        if (localObject == null) {
          break;
        }
        mTempStateStack[mTempStateStackCount] = localObject;
        localObject = parentStateInfo;
      }
      mStateStackTopIndex = -1;
      moveTempStateStackToStateStack();
    }
    
    private final StateInfo setupTempStateStackWithStatesToEnter(State paramState)
    {
      mTempStateStackCount = 0;
      paramState = (StateInfo)mStateInfo.get(paramState);
      Object localObject;
      do
      {
        localObject = mTempStateStack;
        int i = mTempStateStackCount;
        mTempStateStackCount = (i + 1);
        localObject[i] = paramState;
        localObject = parentStateInfo;
        if (localObject == null) {
          break;
        }
        paramState = (State)localObject;
      } while (!active);
      if (mDbg)
      {
        StateMachine localStateMachine = mSm;
        paramState = new StringBuilder();
        paramState.append("setupTempStateStackWithStatesToEnter: X mTempStateStackCount=");
        paramState.append(mTempStateStackCount);
        paramState.append(",curStateInfo: ");
        paramState.append(localObject);
        localStateMachine.log(paramState.toString());
      }
      return localObject;
    }
    
    private final void transitionTo(IState paramIState)
    {
      Object localObject;
      if (mTransitionInProgress)
      {
        String str = mSm.mName;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("transitionTo called while transition already in progress to ");
        ((StringBuilder)localObject).append(mDestState);
        ((StringBuilder)localObject).append(", new target state=");
        ((StringBuilder)localObject).append(paramIState);
        Log.wtf(str, ((StringBuilder)localObject).toString());
      }
      mDestState = ((State)paramIState);
      if (mDbg)
      {
        localObject = mSm;
        paramIState = new StringBuilder();
        paramIState.append("transitionTo: destState=");
        paramIState.append(mDestState.getName());
        ((StateMachine)localObject).log(paramIState.toString());
      }
    }
    
    public final void handleMessage(Message paramMessage)
    {
      if (!mHasQuit)
      {
        if ((mSm != null) && (what != -2) && (what != -1)) {
          mSm.onPreHandleMessage(paramMessage);
        }
        if (mDbg)
        {
          localObject = mSm;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("handleMessage: E msg.what=");
          localStringBuilder.append(what);
          ((StateMachine)localObject).log(localStringBuilder.toString());
        }
        mMsg = paramMessage;
        Object localObject = null;
        if ((!mIsConstructionCompleted) && (mMsg.what != -1))
        {
          if ((!mIsConstructionCompleted) && (mMsg.what == -2) && (mMsg.obj == mSmHandlerObj))
          {
            mIsConstructionCompleted = true;
            invokeEnterMethods(0);
          }
          else
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("StateMachine.handleMessage: The start method not called, received msg: ");
            ((StringBuilder)localObject).append(paramMessage);
            throw new RuntimeException(((StringBuilder)localObject).toString());
          }
        }
        else {
          localObject = processMsg(paramMessage);
        }
        performTransitions((State)localObject, paramMessage);
        if ((mDbg) && (mSm != null)) {
          mSm.log("handleMessage: X");
        }
        if ((mSm != null) && (what != -2) && (what != -1)) {
          mSm.onPostHandleMessage(paramMessage);
        }
      }
    }
    
    private class HaltingState
      extends State
    {
      private HaltingState() {}
      
      public boolean processMessage(Message paramMessage)
      {
        haltedProcessMessage(paramMessage);
        return true;
      }
    }
    
    private class QuittingState
      extends State
    {
      private QuittingState() {}
      
      public boolean processMessage(Message paramMessage)
      {
        return false;
      }
    }
    
    private class StateInfo
    {
      boolean active;
      StateInfo parentStateInfo;
      State state;
      
      private StateInfo() {}
      
      public String toString()
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("state=");
        localStringBuilder.append(state.getName());
        localStringBuilder.append(",active=");
        localStringBuilder.append(active);
        localStringBuilder.append(",parent=");
        String str;
        if (parentStateInfo == null) {
          str = "null";
        } else {
          str = parentStateInfo.state.getName();
        }
        localStringBuilder.append(str);
        return localStringBuilder.toString();
      }
    }
  }
}
