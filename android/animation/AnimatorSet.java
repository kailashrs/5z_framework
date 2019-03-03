package android.animation;

import android.app.ActivityThread;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class AnimatorSet
  extends Animator
  implements AnimationHandler.AnimationFrameCallback
{
  private static final String TAG = "AnimatorSet";
  private boolean mChildrenInitialized;
  private ValueAnimator mDelayAnim;
  private boolean mDependencyDirty;
  private AnimatorListenerAdapter mDummyListener;
  private long mDuration;
  private final boolean mEndCanBeCalled;
  private ArrayList<AnimationEvent> mEvents = new ArrayList();
  private long mFirstFrame;
  private TimeInterpolator mInterpolator;
  private int mLastEventId;
  private long mLastFrameTime;
  private ArrayMap<Animator, Node> mNodeMap = new ArrayMap();
  private ArrayList<Node> mNodes = new ArrayList();
  private long mPauseTime;
  private ArrayList<Node> mPlayingSet = new ArrayList();
  private boolean mReversing;
  private Node mRootNode;
  private SeekState mSeekState;
  private boolean mSelfPulse;
  private final boolean mShouldIgnoreEndWithoutStart;
  private final boolean mShouldResetValuesAtStart;
  private long mStartDelay;
  private boolean mStarted;
  private long mTotalDuration;
  
  public AnimatorSet()
  {
    boolean bool1 = false;
    mDependencyDirty = false;
    mStarted = false;
    mStartDelay = 0L;
    mDelayAnim = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F }).setDuration(0L);
    mRootNode = new Node(mDelayAnim);
    mDuration = -1L;
    mInterpolator = null;
    mTotalDuration = 0L;
    mLastFrameTime = -1L;
    mFirstFrame = -1L;
    mLastEventId = -1;
    mReversing = false;
    mSelfPulse = true;
    mSeekState = new SeekState(null);
    mChildrenInitialized = false;
    mPauseTime = -1L;
    mDummyListener = new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (mNodeMap.get(paramAnonymousAnimator) != null)
        {
          mNodeMap.get(paramAnonymousAnimator)).mEnded = true;
          return;
        }
        throw new AndroidRuntimeException("Error: animation ended is not in the node map");
      }
    };
    mNodeMap.put(mDelayAnim, mRootNode);
    mNodes.add(mRootNode);
    Application localApplication = ActivityThread.currentApplication();
    int i;
    if ((localApplication != null) && (localApplication.getApplicationInfo() != null))
    {
      if (getApplicationInfotargetSdkVersion < 24) {
        mShouldIgnoreEndWithoutStart = true;
      } else {
        mShouldIgnoreEndWithoutStart = false;
      }
      if (getApplicationInfotargetSdkVersion < 26) {
        i = 1;
      } else {
        i = 0;
      }
    }
    else
    {
      mShouldIgnoreEndWithoutStart = true;
      i = 1;
    }
    if (i == 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mShouldResetValuesAtStart = bool2;
    boolean bool2 = bool1;
    if (i == 0) {
      bool2 = true;
    }
    mEndCanBeCalled = bool2;
  }
  
  private void addAnimationCallback(long paramLong)
  {
    if (!mSelfPulse) {
      return;
    }
    AnimationHandler.getInstance().addAnimationFrameCallback(this, paramLong);
  }
  
  private void addDummyListener()
  {
    for (int i = 1; i < mNodes.size(); i++) {
      mNodes.get(i)).mAnimation.addListener(mDummyListener);
    }
  }
  
  private void createDependencyGraph()
  {
    boolean bool = mDependencyDirty;
    int i = 0;
    int k;
    if (!bool)
    {
      j = 0;
      for (k = 0;; k++)
      {
        m = j;
        if (k >= mNodes.size()) {
          break;
        }
        localObject = mNodes.get(k)).mAnimation;
        if (mNodes.get(k)).mTotalDuration != ((Animator)localObject).getTotalDuration())
        {
          m = 1;
          break;
        }
      }
      if (m == 0) {
        return;
      }
    }
    mDependencyDirty = false;
    int j = mNodes.size();
    for (int m = 0; m < j; m++) {
      mNodes.get(m)).mParentsAdded = false;
    }
    for (m = 0; m < j; m++)
    {
      Node localNode = (Node)mNodes.get(m);
      if (!mParentsAdded)
      {
        mParentsAdded = true;
        if (mSiblings != null)
        {
          findSiblings(localNode, mSiblings);
          mSiblings.remove(localNode);
          int n = mSiblings.size();
          for (k = 0; k < n; k++) {
            localNode.addParents(mSiblings.get(k)).mParents);
          }
          for (k = 0; k < n; k++)
          {
            localObject = (Node)mSiblings.get(k);
            ((Node)localObject).addParents(mParents);
            mParentsAdded = true;
          }
        }
      }
    }
    for (m = i; m < j; m++)
    {
      localObject = (Node)mNodes.get(m);
      if ((localObject != mRootNode) && (mParents == null)) {
        ((Node)localObject).addParent(mRootNode);
      }
    }
    Object localObject = new ArrayList(mNodes.size());
    mRootNode.mStartTime = 0L;
    mRootNode.mEndTime = mDelayAnim.getDuration();
    updatePlayTime(mRootNode, (ArrayList)localObject);
    sortAnimationEvents();
    mTotalDuration = ((AnimationEvent)mEvents.get(mEvents.size() - 1)).getTime();
  }
  
  private void endAnimation()
  {
    mStarted = false;
    mLastFrameTime = -1L;
    mFirstFrame = -1L;
    mLastEventId = -1;
    mPaused = false;
    mPauseTime = -1L;
    mSeekState.reset();
    mPlayingSet.clear();
    removeAnimationCallback();
    if (mListeners != null)
    {
      ArrayList localArrayList = (ArrayList)mListeners.clone();
      int i = localArrayList.size();
      for (int j = 0; j < i; j++) {
        ((Animator.AnimatorListener)localArrayList.get(j)).onAnimationEnd(this, mReversing);
      }
    }
    removeDummyListener();
    mSelfPulse = true;
    mReversing = false;
  }
  
  private int findLatestEventIdForTime(long paramLong)
  {
    int i = mEvents.size();
    int j = mLastEventId;
    if (mReversing)
    {
      long l = getTotalDuration();
      if (mLastEventId == -1) {
        k = i;
      } else {
        k = mLastEventId;
      }
      mLastEventId = k;
      for (k = mLastEventId - 1;; k--)
      {
        m = j;
        if (k < 0) {
          break;
        }
        if (((AnimationEvent)mEvents.get(k)).getTime() >= l - paramLong) {
          j = k;
        }
      }
    }
    int m = mLastEventId;
    int k = j;
    for (;;)
    {
      j = m + 1;
      m = k;
      if (j >= i) {
        break;
      }
      AnimationEvent localAnimationEvent = (AnimationEvent)mEvents.get(j);
      m = k;
      if (localAnimationEvent.getTime() != -1L)
      {
        m = k;
        if (localAnimationEvent.getTime() <= paramLong) {
          m = j;
        }
      }
      k = m;
      m = j;
    }
    return m;
  }
  
  private void findSiblings(Node paramNode, ArrayList<Node> paramArrayList)
  {
    if (!paramArrayList.contains(paramNode))
    {
      paramArrayList.add(paramNode);
      if (mSiblings == null) {
        return;
      }
      for (int i = 0; i < mSiblings.size(); i++) {
        findSiblings((Node)mSiblings.get(i), paramArrayList);
      }
    }
  }
  
  private void forceToEnd()
  {
    if (mEndCanBeCalled)
    {
      end();
      return;
    }
    if (mReversing)
    {
      handleAnimationEvents(mLastEventId, 0, getTotalDuration());
    }
    else
    {
      long l1 = getTotalDuration();
      long l2 = l1;
      if (l1 == -1L) {
        l2 = 2147483647L;
      }
      handleAnimationEvents(mLastEventId, mEvents.size() - 1, l2);
    }
    mPlayingSet.clear();
    endAnimation();
  }
  
  private Node getNodeForAnimation(Animator paramAnimator)
  {
    Node localNode1 = (Node)mNodeMap.get(paramAnimator);
    Node localNode2 = localNode1;
    if (localNode1 == null)
    {
      localNode2 = new Node(paramAnimator);
      mNodeMap.put(paramAnimator, localNode2);
      mNodes.add(localNode2);
    }
    return localNode2;
  }
  
  private long getPlayTimeForNode(long paramLong, Node paramNode)
  {
    return getPlayTimeForNode(paramLong, paramNode, mReversing);
  }
  
  private long getPlayTimeForNode(long paramLong, Node paramNode, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      long l = getTotalDuration();
      return mEndTime - (l - paramLong);
    }
    return paramLong - mStartTime;
  }
  
  private void handleAnimationEvents(int paramInt1, int paramInt2, long paramLong)
  {
    Object localObject1;
    Object localObject2;
    if (mReversing)
    {
      if (paramInt1 == -1) {
        paramInt1 = mEvents.size();
      }
      paramInt1--;
      while (paramInt1 >= paramInt2)
      {
        localObject1 = (AnimationEvent)mEvents.get(paramInt1);
        localObject2 = mNode;
        if (mEvent == 2)
        {
          if (mAnimation.isStarted()) {
            mAnimation.cancel();
          }
          mEnded = false;
          mPlayingSet.add(mNode);
          mAnimation.startWithoutPulsing(true);
          pulseFrame((Node)localObject2, 0L);
        }
        else if ((mEvent == 1) && (!mEnded))
        {
          pulseFrame((Node)localObject2, getPlayTimeForNode(paramLong, (Node)localObject2));
        }
        paramInt1--;
      }
    }
    paramInt1++;
    while (paramInt1 <= paramInt2)
    {
      localObject2 = (AnimationEvent)mEvents.get(paramInt1);
      localObject1 = mNode;
      if (mEvent == 0)
      {
        mPlayingSet.add(mNode);
        if (mAnimation.isStarted()) {
          mAnimation.cancel();
        }
        mEnded = false;
        mAnimation.startWithoutPulsing(false);
        pulseFrame((Node)localObject1, 0L);
      }
      else if ((mEvent == 2) && (!mEnded))
      {
        pulseFrame((Node)localObject1, getPlayTimeForNode(paramLong, (Node)localObject1));
      }
      paramInt1++;
    }
  }
  
  private void initAnimation()
  {
    if (mInterpolator != null) {
      for (int i = 0; i < mNodes.size(); i++) {
        mNodes.get(i)).mAnimation.setInterpolator(mInterpolator);
      }
    }
    updateAnimatorsDuration();
    createDependencyGraph();
  }
  
  private void initChildren()
  {
    if (!isInitialized())
    {
      mChildrenInitialized = true;
      skipToEndValue(false);
    }
  }
  
  private static boolean isEmptySet(AnimatorSet paramAnimatorSet)
  {
    if (paramAnimatorSet.getStartDelay() > 0L) {
      return false;
    }
    for (int i = 0; i < paramAnimatorSet.getChildAnimations().size(); i++)
    {
      Animator localAnimator = (Animator)paramAnimatorSet.getChildAnimations().get(i);
      if (!(localAnimator instanceof AnimatorSet)) {
        return false;
      }
      if (!isEmptySet((AnimatorSet)localAnimator)) {
        return false;
      }
    }
    return true;
  }
  
  private void printChildCount()
  {
    ArrayList localArrayList = new ArrayList(mNodes.size());
    localArrayList.add(mRootNode);
    Log.d("AnimatorSet", "Current tree: ");
    int i = 0;
    while (i < localArrayList.size())
    {
      int j = localArrayList.size();
      StringBuilder localStringBuilder = new StringBuilder();
      while (i < j)
      {
        Node localNode1 = (Node)localArrayList.get(i);
        int k = 0;
        if (mChildNodes != null)
        {
          k = 0;
          int m = 0;
          while (m < mChildNodes.size())
          {
            Node localNode2 = (Node)mChildNodes.get(m);
            int n = k;
            if (mLatestParent == localNode1)
            {
              n = k + 1;
              localArrayList.add(localNode2);
            }
            m++;
            k = n;
          }
        }
        localStringBuilder.append(" ");
        localStringBuilder.append(k);
        i++;
      }
      Log.d("AnimatorSet", localStringBuilder.toString());
    }
  }
  
  private void pulseFrame(Node paramNode, long paramLong)
  {
    if (!mEnded)
    {
      float f = ValueAnimator.getDurationScale();
      if (f == 0.0F) {
        f = 1.0F;
      }
      mEnded = mAnimation.pulseAnimationFrame(((float)paramLong * f));
    }
  }
  
  private void removeAnimationCallback()
  {
    if (!mSelfPulse) {
      return;
    }
    AnimationHandler.getInstance().removeCallback(this);
  }
  
  private void removeDummyListener()
  {
    for (int i = 1; i < mNodes.size(); i++) {
      mNodes.get(i)).mAnimation.removeListener(mDummyListener);
    }
  }
  
  private void skipToStartValue(boolean paramBoolean)
  {
    skipToEndValue(paramBoolean ^ true);
  }
  
  private void sortAnimationEvents()
  {
    mEvents.clear();
    Object localObject;
    for (int i = 1; i < mNodes.size(); i++)
    {
      localObject = (Node)mNodes.get(i);
      mEvents.add(new AnimationEvent((Node)localObject, 0));
      mEvents.add(new AnimationEvent((Node)localObject, 1));
      mEvents.add(new AnimationEvent((Node)localObject, 2));
    }
    mEvents.sort(new Comparator()
    {
      public int compare(AnimatorSet.AnimationEvent paramAnonymousAnimationEvent1, AnimatorSet.AnimationEvent paramAnonymousAnimationEvent2)
      {
        long l1 = paramAnonymousAnimationEvent1.getTime();
        long l2 = paramAnonymousAnimationEvent2.getTime();
        if (l1 == l2)
        {
          if (mEvent + mEvent == 1) {
            return mEvent - mEvent;
          }
          return mEvent - mEvent;
        }
        if (l2 == -1L) {
          return -1;
        }
        if (l1 == -1L) {
          return 1;
        }
        return (int)(l1 - l2);
      }
    });
    int j = mEvents.size();
    i = 0;
    while (i < j)
    {
      localObject = (AnimationEvent)mEvents.get(i);
      if (mEvent == 2)
      {
        if (mNode.mStartTime == mNode.mEndTime) {}
        for (int k = 1;; k = 0)
        {
          break;
          if (mNode.mEndTime != mNode.mStartTime + mNode.mAnimation.getStartDelay()) {
            break label458;
          }
        }
        int m = j;
        int n = j;
        int i1 = i + 1;
        while ((i1 < j) && ((m >= j) || (n >= j)))
        {
          int i2 = m;
          int i3 = n;
          if (mEvents.get(i1)).mNode == mNode) {
            if (mEvents.get(i1)).mEvent == 0)
            {
              i2 = i1;
              i3 = n;
            }
            else
            {
              i2 = m;
              i3 = n;
              if (mEvents.get(i1)).mEvent == 1)
              {
                i3 = i1;
                i2 = m;
              }
            }
          }
          i1++;
          m = i2;
          n = i3;
        }
        if ((k != 0) && (m == mEvents.size())) {
          throw new UnsupportedOperationException("Something went wrong, no start isfound after stop for an animation that has the same start and endtime.");
        }
        if (n != mEvents.size())
        {
          i1 = i;
          if (k != 0)
          {
            localObject = (AnimationEvent)mEvents.remove(m);
            mEvents.add(i, localObject);
            i1 = i + 1;
          }
          localObject = (AnimationEvent)mEvents.remove(n);
          mEvents.add(i1, localObject);
          i = i1 + 2;
        }
        else
        {
          throw new UnsupportedOperationException("Something went wrong, no startdelay end is found after stop for an animation");
          label458:
          i++;
        }
      }
      else
      {
        i++;
      }
    }
    if ((!mEvents.isEmpty()) && (mEvents.get(0)).mEvent != 0)) {
      throw new UnsupportedOperationException("Sorting went bad, the start event should always be at index 0");
    }
    mEvents.add(0, new AnimationEvent(mRootNode, 0));
    mEvents.add(1, new AnimationEvent(mRootNode, 1));
    mEvents.add(2, new AnimationEvent(mRootNode, 2));
    if ((mEvents.get(mEvents.size() - 1)).mEvent != 0) && (mEvents.get(mEvents.size() - 1)).mEvent != 1)) {
      return;
    }
    throw new UnsupportedOperationException("Something went wrong, the last event is not an end event");
  }
  
  private void start(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (Looper.myLooper() != null)
    {
      mStarted = true;
      mSelfPulse = paramBoolean2;
      int i = 0;
      mPaused = false;
      mPauseTime = -1L;
      int j = mNodes.size();
      Object localObject;
      for (int k = 0; k < j; k++)
      {
        localObject = (Node)mNodes.get(k);
        mEnded = false;
        mAnimation.setAllowRunningAsynchronously(false);
      }
      initAnimation();
      if ((paramBoolean1) && (!canReverse())) {
        throw new UnsupportedOperationException("Cannot reverse infinite AnimatorSet");
      }
      mReversing = paramBoolean1;
      paramBoolean2 = isEmptySet(this);
      if (!paramBoolean2) {
        startAnimation();
      }
      if (mListeners != null)
      {
        localObject = (ArrayList)mListeners.clone();
        j = ((ArrayList)localObject).size();
        for (k = i; k < j; k++) {
          ((Animator.AnimatorListener)((ArrayList)localObject).get(k)).onAnimationStart(this, paramBoolean1);
        }
      }
      if (paramBoolean2) {
        end();
      }
      return;
    }
    throw new AndroidRuntimeException("Animators may only be run on Looper threads");
  }
  
  private void startAnimation()
  {
    addDummyListener();
    long l = 0L;
    addAnimationCallback(0L);
    if ((mSeekState.getPlayTimeNormalized() == 0L) && (mReversing)) {
      mSeekState.reset();
    }
    int i;
    if (mShouldResetValuesAtStart) {
      if (isInitialized())
      {
        skipToEndValue(mReversing ^ true);
      }
      else if (mReversing)
      {
        initChildren();
        skipToEndValue(mReversing ^ true);
      }
      else
      {
        for (i = mEvents.size() - 1; i >= 0; i--) {
          if (mEvents.get(i)).mEvent == 1)
          {
            Animator localAnimator = mEvents.get(i)).mNode.mAnimation;
            if (localAnimator.isInitialized()) {
              localAnimator.skipToEndValue(true);
            }
          }
        }
      }
    }
    if ((mReversing) || (mStartDelay == 0L) || (mSeekState.isActive()))
    {
      if (mSeekState.isActive())
      {
        mSeekState.updateSeekDirection(mReversing);
        l = mSeekState.getPlayTime();
      }
      int j = findLatestEventIdForTime(l);
      handleAnimationEvents(-1, j, l);
      for (i = mPlayingSet.size() - 1; i >= 0; i--) {
        if (mPlayingSet.get(i)).mEnded) {
          mPlayingSet.remove(i);
        }
      }
      mLastEventId = j;
    }
  }
  
  private void updateAnimatorsDuration()
  {
    if (mDuration >= 0L)
    {
      int i = mNodes.size();
      for (int j = 0; j < i; j++) {
        mNodes.get(j)).mAnimation.setDuration(mDuration);
      }
    }
    mDelayAnim.setDuration(mStartDelay);
  }
  
  private void updatePlayTime(Node paramNode, ArrayList<Node> paramArrayList)
  {
    Object localObject = mChildNodes;
    int i = 0;
    int j = 0;
    if (localObject == null)
    {
      if (paramNode == mRootNode) {
        for (i = j; i < mNodes.size(); i++)
        {
          paramNode = (Node)mNodes.get(i);
          if (paramNode != mRootNode)
          {
            mStartTime = -1L;
            mEndTime = -1L;
          }
        }
      }
      return;
    }
    paramArrayList.add(paramNode);
    int k = mChildNodes.size();
    while (i < k)
    {
      localObject = (Node)mChildNodes.get(i);
      mTotalDuration = mAnimation.getTotalDuration();
      j = paramArrayList.indexOf(localObject);
      if (j >= 0)
      {
        while (j < paramArrayList.size())
        {
          getmLatestParent = null;
          getmStartTime = -1L;
          getmEndTime = -1L;
          j++;
        }
        mStartTime = -1L;
        mEndTime = -1L;
        mLatestParent = null;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Cycle found in AnimatorSet: ");
        ((StringBuilder)localObject).append(this);
        Log.w("AnimatorSet", ((StringBuilder)localObject).toString());
      }
      else
      {
        if (mStartTime != -1L) {
          if (mEndTime == -1L)
          {
            mLatestParent = paramNode;
            mStartTime = -1L;
            mEndTime = -1L;
          }
          else
          {
            if (mEndTime >= mStartTime)
            {
              mLatestParent = paramNode;
              mStartTime = mEndTime;
            }
            long l;
            if (mTotalDuration == -1L) {
              l = -1L;
            } else {
              l = mStartTime + mTotalDuration;
            }
            mEndTime = l;
          }
        }
        updatePlayTime((Node)localObject, paramArrayList);
      }
      i++;
    }
    paramArrayList.remove(paramNode);
  }
  
  void animateBasedOnPlayTime(long paramLong1, long paramLong2, boolean paramBoolean)
  {
    if ((paramLong1 >= 0L) && (paramLong2 >= 0L))
    {
      long l1;
      long l2;
      if (paramBoolean)
      {
        if (getTotalDuration() != -1L)
        {
          l1 = getTotalDuration() - mStartDelay;
          l2 = Math.min(paramLong1, l1);
          paramLong1 = l1 - paramLong2;
          paramLong2 = l1 - l2;
          paramBoolean = false;
        }
        else
        {
          throw new UnsupportedOperationException("Cannot reverse AnimatorSet with infinite duration");
        }
      }
      else
      {
        l1 = paramLong1;
        paramLong1 = paramLong2;
        paramLong2 = l1;
      }
      int i = 0;
      skipToStartValue(false);
      ArrayList localArrayList = new ArrayList();
      Object localObject;
      for (int j = 0; j < mEvents.size(); j++)
      {
        localObject = (AnimationEvent)mEvents.get(j);
        if ((((AnimationEvent)localObject).getTime() > paramLong2) || (((AnimationEvent)localObject).getTime() == -1L)) {
          break;
        }
        if ((mEvent == 1) && ((mNode.mEndTime == -1L) || (mNode.mEndTime > paramLong2))) {
          localArrayList.add(mNode);
        }
        if (mEvent == 2) {
          mNode.mAnimation.skipToEndValue(false);
        }
      }
      for (j = i; j < localArrayList.size(); j++)
      {
        localObject = (Node)localArrayList.get(j);
        l2 = getPlayTimeForNode(paramLong2, (Node)localObject, paramBoolean);
        l1 = l2;
        if (!paramBoolean) {
          l1 = l2 - mAnimation.getStartDelay();
        }
        mAnimation.animateBasedOnPlayTime(l1, paramLong1, paramBoolean);
      }
      return;
    }
    throw new UnsupportedOperationException("Error: Play time should never be negative.");
  }
  
  public boolean canReverse()
  {
    boolean bool;
    if (getTotalDuration() != -1L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void cancel()
  {
    if (Looper.myLooper() != null)
    {
      if (isStarted())
      {
        ArrayList localArrayList = mListeners;
        int i = 0;
        if (localArrayList != null)
        {
          localArrayList = (ArrayList)mListeners.clone();
          j = localArrayList.size();
          for (k = 0; k < j; k++) {
            ((Animator.AnimatorListener)localArrayList.get(k)).onAnimationCancel(this);
          }
        }
        localArrayList = new ArrayList(mPlayingSet);
        int j = localArrayList.size();
        for (int k = i; k < j; k++) {
          getmAnimation.cancel();
        }
        mPlayingSet.clear();
        endAnimation();
      }
      return;
    }
    throw new AndroidRuntimeException("Animators may only be run on Looper threads");
  }
  
  public AnimatorSet clone()
  {
    final AnimatorSet localAnimatorSet = (AnimatorSet)super.clone();
    int i = mNodes.size();
    mStarted = false;
    mLastFrameTime = -1L;
    mFirstFrame = -1L;
    mLastEventId = -1;
    mPaused = false;
    mPauseTime = -1L;
    mSeekState = new SeekState(null);
    mSelfPulse = true;
    mPlayingSet = new ArrayList();
    mNodeMap = new ArrayMap();
    mNodes = new ArrayList(i);
    mEvents = new ArrayList();
    mDummyListener = new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (localAnimatorSetmNodeMap.get(paramAnonymousAnimator) != null)
        {
          localAnimatorSetmNodeMap.get(paramAnonymousAnimator)).mEnded = true;
          return;
        }
        throw new AndroidRuntimeException("Error: animation ended is not in the node map");
      }
    };
    mReversing = false;
    mDependencyDirty = true;
    HashMap localHashMap = new HashMap(i);
    Node localNode1;
    Node localNode2;
    for (int j = 0; j < i; j++)
    {
      localNode1 = (Node)mNodes.get(j);
      localNode2 = localNode1.clone();
      mAnimation.removeListener(mDummyListener);
      localHashMap.put(localNode1, localNode2);
      mNodes.add(localNode2);
      mNodeMap.put(mAnimation, localNode2);
    }
    mRootNode = ((Node)localHashMap.get(mRootNode));
    mDelayAnim = ((ValueAnimator)mRootNode.mAnimation);
    for (j = 0; j < i; j++)
    {
      Node localNode3 = (Node)mNodes.get(j);
      localNode1 = (Node)localHashMap.get(localNode3);
      if (mLatestParent == null) {
        localNode2 = null;
      } else {
        localNode2 = (Node)localHashMap.get(mLatestParent);
      }
      mLatestParent = localNode2;
      int k;
      if (mChildNodes == null) {
        k = 0;
      } else {
        k = mChildNodes.size();
      }
      for (int m = 0; m < k; m++) {
        mChildNodes.set(m, (Node)localHashMap.get(mChildNodes.get(m)));
      }
      if (mSiblings == null) {
        k = 0;
      } else {
        k = mSiblings.size();
      }
      for (m = 0; m < k; m++) {
        mSiblings.set(m, (Node)localHashMap.get(mSiblings.get(m)));
      }
      if (mParents == null) {
        k = 0;
      } else {
        k = mParents.size();
      }
      for (m = 0; m < k; m++) {
        mParents.set(m, (Node)localHashMap.get(mParents.get(m)));
      }
    }
    return localAnimatorSet;
  }
  
  public void commitAnimationFrame(long paramLong) {}
  
  public boolean doAnimationFrame(long paramLong)
  {
    float f = ValueAnimator.getDurationScale();
    if (f == 0.0F)
    {
      forceToEnd();
      return true;
    }
    if (mFirstFrame < 0L) {
      mFirstFrame = paramLong;
    }
    if (mPaused)
    {
      mPauseTime = paramLong;
      removeAnimationCallback();
      return false;
    }
    if (mPauseTime > 0L)
    {
      mFirstFrame += paramLong - mPauseTime;
      mPauseTime = -1L;
    }
    if (mSeekState.isActive())
    {
      mSeekState.updateSeekDirection(mReversing);
      if (mReversing) {
        mFirstFrame = (((float)paramLong - (float)mSeekState.getPlayTime() * f));
      } else {
        mFirstFrame = (((float)paramLong - (float)(mSeekState.getPlayTime() + mStartDelay) * f));
      }
      mSeekState.reset();
    }
    if ((!mReversing) && ((float)paramLong < (float)mFirstFrame + (float)mStartDelay * f)) {
      return false;
    }
    long l = ((float)(paramLong - mFirstFrame) / f);
    mLastFrameTime = paramLong;
    int i = findLatestEventIdForTime(l);
    handleAnimationEvents(mLastEventId, i, l);
    mLastEventId = i;
    for (i = 0; i < mPlayingSet.size(); i++)
    {
      Node localNode = (Node)mPlayingSet.get(i);
      if (!mEnded) {
        pulseFrame(localNode, getPlayTimeForNode(l, localNode));
      }
    }
    for (i = mPlayingSet.size() - 1; i >= 0; i--) {
      if (mPlayingSet.get(i)).mEnded) {
        mPlayingSet.remove(i);
      }
    }
    int j = 0;
    if (mReversing)
    {
      if ((mPlayingSet.size() == 1) && (mPlayingSet.get(0) == mRootNode))
      {
        i = 1;
      }
      else
      {
        i = j;
        if (mPlayingSet.isEmpty())
        {
          i = j;
          if (mLastEventId < 3) {
            i = 1;
          }
        }
      }
    }
    else if ((mPlayingSet.isEmpty()) && (mLastEventId == mEvents.size() - 1)) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      endAnimation();
      return true;
    }
    return false;
  }
  
  public void end()
  {
    if (Looper.myLooper() != null)
    {
      if ((mShouldIgnoreEndWithoutStart) && (!isStarted())) {
        return;
      }
      if (isStarted())
      {
        AnimationEvent localAnimationEvent;
        Animator localAnimator;
        if (mReversing)
        {
          int i;
          if (mLastEventId == -1) {
            i = mEvents.size();
          } else {
            i = mLastEventId;
          }
          mLastEventId = i;
          while (mLastEventId > 0)
          {
            mLastEventId -= 1;
            localAnimationEvent = (AnimationEvent)mEvents.get(mLastEventId);
            localAnimator = mNode.mAnimation;
            if (!mNodeMap.get(localAnimator)).mEnded) {
              if (mEvent == 2) {
                localAnimator.reverse();
              } else if ((mEvent == 1) && (localAnimator.isStarted())) {
                localAnimator.end();
              }
            }
          }
        }
        while (mLastEventId < mEvents.size() - 1)
        {
          mLastEventId += 1;
          localAnimationEvent = (AnimationEvent)mEvents.get(mLastEventId);
          localAnimator = mNode.mAnimation;
          if (!mNodeMap.get(localAnimator)).mEnded) {
            if (mEvent == 0) {
              localAnimator.start();
            } else if ((mEvent == 2) && (localAnimator.isStarted())) {
              localAnimator.end();
            }
          }
        }
        mPlayingSet.clear();
      }
      endAnimation();
      return;
    }
    throw new AndroidRuntimeException("Animators may only be run on Looper threads");
  }
  
  public int getChangingConfigurations()
  {
    int i = super.getChangingConfigurations();
    int j = mNodes.size();
    for (int k = 0; k < j; k++) {
      i |= mNodes.get(k)).mAnimation.getChangingConfigurations();
    }
    return i;
  }
  
  public ArrayList<Animator> getChildAnimations()
  {
    ArrayList localArrayList = new ArrayList();
    int i = mNodes.size();
    for (int j = 0; j < i; j++)
    {
      Node localNode = (Node)mNodes.get(j);
      if (localNode != mRootNode) {
        localArrayList.add(mAnimation);
      }
    }
    return localArrayList;
  }
  
  public long getCurrentPlayTime()
  {
    if (mSeekState.isActive()) {
      return mSeekState.getPlayTime();
    }
    if (mLastFrameTime == -1L) {
      return 0L;
    }
    float f = ValueAnimator.getDurationScale();
    if (f == 0.0F) {
      f = 1.0F;
    }
    if (mReversing) {
      return ((float)(mLastFrameTime - mFirstFrame) / f);
    }
    return ((float)(mLastFrameTime - mFirstFrame - mStartDelay) / f);
  }
  
  public long getDuration()
  {
    return mDuration;
  }
  
  public TimeInterpolator getInterpolator()
  {
    return mInterpolator;
  }
  
  public long getStartDelay()
  {
    return mStartDelay;
  }
  
  public long getTotalDuration()
  {
    updateAnimatorsDuration();
    createDependencyGraph();
    return mTotalDuration;
  }
  
  boolean isInitialized()
  {
    if (mChildrenInitialized) {
      return true;
    }
    boolean bool1 = true;
    boolean bool2;
    for (int i = 0;; i++)
    {
      bool2 = bool1;
      if (i >= mNodes.size()) {
        break;
      }
      if (!mNodes.get(i)).mAnimation.isInitialized())
      {
        bool2 = false;
        break;
      }
    }
    mChildrenInitialized = bool2;
    return mChildrenInitialized;
  }
  
  public boolean isRunning()
  {
    if (mStartDelay == 0L) {
      return mStarted;
    }
    boolean bool;
    if (mLastFrameTime > 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStarted()
  {
    return mStarted;
  }
  
  public void pause()
  {
    if (Looper.myLooper() != null)
    {
      boolean bool = mPaused;
      super.pause();
      if ((!bool) && (mPaused)) {
        mPauseTime = -1L;
      }
      return;
    }
    throw new AndroidRuntimeException("Animators may only be run on Looper threads");
  }
  
  public Builder play(Animator paramAnimator)
  {
    if (paramAnimator != null) {
      return new Builder(paramAnimator);
    }
    return null;
  }
  
  public void playSequentially(List<Animator> paramList)
  {
    if ((paramList != null) && (paramList.size() > 0))
    {
      int i = paramList.size();
      int j = 0;
      if (i == 1) {
        play((Animator)paramList.get(0));
      } else {
        while (j < paramList.size() - 1)
        {
          play((Animator)paramList.get(j)).before((Animator)paramList.get(j + 1));
          j++;
        }
      }
    }
  }
  
  public void playSequentially(Animator... paramVarArgs)
  {
    if (paramVarArgs != null)
    {
      int i = paramVarArgs.length;
      int j = 0;
      if (i == 1) {
        play(paramVarArgs[0]);
      } else {
        while (j < paramVarArgs.length - 1)
        {
          play(paramVarArgs[j]).before(paramVarArgs[(j + 1)]);
          j++;
        }
      }
    }
  }
  
  public void playTogether(Collection<Animator> paramCollection)
  {
    if ((paramCollection != null) && (paramCollection.size() > 0))
    {
      Animator localAnimator = null;
      Iterator localIterator = paramCollection.iterator();
      paramCollection = localAnimator;
      while (localIterator.hasNext())
      {
        localAnimator = (Animator)localIterator.next();
        if (paramCollection == null) {
          paramCollection = play(localAnimator);
        } else {
          paramCollection.with(localAnimator);
        }
      }
    }
  }
  
  public void playTogether(Animator... paramVarArgs)
  {
    if (paramVarArgs != null)
    {
      Builder localBuilder = play(paramVarArgs[0]);
      for (int i = 1; i < paramVarArgs.length; i++) {
        localBuilder.with(paramVarArgs[i]);
      }
    }
  }
  
  boolean pulseAnimationFrame(long paramLong)
  {
    return doAnimationFrame(paramLong);
  }
  
  public void resume()
  {
    if (Looper.myLooper() != null)
    {
      boolean bool = mPaused;
      super.resume();
      if ((bool) && (!mPaused) && (mPauseTime >= 0L)) {
        addAnimationCallback(0L);
      }
      return;
    }
    throw new AndroidRuntimeException("Animators may only be run on Looper threads");
  }
  
  public void reverse()
  {
    start(true, true);
  }
  
  public void setCurrentPlayTime(long paramLong)
  {
    if ((mReversing) && (getTotalDuration() == -1L)) {
      throw new UnsupportedOperationException("Error: Cannot seek in reverse in an infinite AnimatorSet");
    }
    if (((getTotalDuration() == -1L) || (paramLong <= getTotalDuration() - mStartDelay)) && (paramLong >= 0L))
    {
      initAnimation();
      if (!isStarted())
      {
        if (!mReversing)
        {
          if (!mSeekState.isActive())
          {
            findLatestEventIdForTime(0L);
            initChildren();
            skipToStartValue(mReversing);
            mSeekState.setPlayTime(0L, mReversing);
          }
          animateBasedOnPlayTime(paramLong, 0L, mReversing);
          mSeekState.setPlayTime(paramLong, mReversing);
        }
        else
        {
          throw new UnsupportedOperationException("Error: Something went wrong. mReversing should not be set when AnimatorSet is not started.");
        }
      }
      else {
        mSeekState.setPlayTime(paramLong, mReversing);
      }
      return;
    }
    throw new UnsupportedOperationException("Error: Play time should always be in between0 and duration.");
  }
  
  public AnimatorSet setDuration(long paramLong)
  {
    if (paramLong >= 0L)
    {
      mDependencyDirty = true;
      mDuration = paramLong;
      return this;
    }
    throw new IllegalArgumentException("duration must be a value of zero or greater");
  }
  
  public void setInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    mInterpolator = paramTimeInterpolator;
  }
  
  public void setStartDelay(long paramLong)
  {
    long l1 = paramLong;
    if (paramLong < 0L)
    {
      Log.w("AnimatorSet", "Start delay should always be non-negative");
      l1 = 0L;
    }
    long l2 = l1 - mStartDelay;
    if (l2 == 0L) {
      return;
    }
    mStartDelay = l1;
    if (!mDependencyDirty)
    {
      int i = mNodes.size();
      for (int j = 0;; j++)
      {
        l1 = -1L;
        if (j >= i) {
          break;
        }
        Node localNode = (Node)mNodes.get(j);
        if (localNode == mRootNode)
        {
          mEndTime = mStartDelay;
        }
        else
        {
          if (mStartTime == -1L) {
            paramLong = -1L;
          } else {
            paramLong = mStartTime + l2;
          }
          mStartTime = paramLong;
          if (mEndTime == -1L) {
            paramLong = l1;
          } else {
            paramLong = mEndTime + l2;
          }
          mEndTime = paramLong;
        }
      }
      if (mTotalDuration != -1L) {
        mTotalDuration += l2;
      }
    }
  }
  
  public void setTarget(Object paramObject)
  {
    int i = mNodes.size();
    for (int j = 0; j < i; j++)
    {
      Animator localAnimator = mNodes.get(j)).mAnimation;
      if ((localAnimator instanceof AnimatorSet)) {
        ((AnimatorSet)localAnimator).setTarget(paramObject);
      } else if ((localAnimator instanceof ObjectAnimator)) {
        ((ObjectAnimator)localAnimator).setTarget(paramObject);
      }
    }
  }
  
  public void setupEndValues()
  {
    int i = mNodes.size();
    for (int j = 0; j < i; j++)
    {
      Node localNode = (Node)mNodes.get(j);
      if (localNode != mRootNode) {
        mAnimation.setupEndValues();
      }
    }
  }
  
  public void setupStartValues()
  {
    int i = mNodes.size();
    for (int j = 0; j < i; j++)
    {
      Node localNode = (Node)mNodes.get(j);
      if (localNode != mRootNode) {
        mAnimation.setupStartValues();
      }
    }
  }
  
  public boolean shouldPlayTogether()
  {
    updateAnimatorsDuration();
    createDependencyGraph();
    ArrayList localArrayList = mRootNode.mChildNodes;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (localArrayList != null) {
      if (mRootNode.mChildNodes.size() == mNodes.size() - 1) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  void skipToEndValue(boolean paramBoolean)
  {
    if (isInitialized())
    {
      initAnimation();
      if (paramBoolean) {
        for (i = mEvents.size() - 1; i >= 0; i--) {
          if (mEvents.get(i)).mEvent == 1) {
            mEvents.get(i)).mNode.mAnimation.skipToEndValue(true);
          }
        }
      }
      for (int i = 0; i < mEvents.size(); i++) {
        if (mEvents.get(i)).mEvent == 2) {
          mEvents.get(i)).mNode.mAnimation.skipToEndValue(false);
        }
      }
      return;
    }
    throw new UnsupportedOperationException("Children must be initialized.");
  }
  
  public void start()
  {
    start(false, true);
  }
  
  void startWithoutPulsing(boolean paramBoolean)
  {
    start(paramBoolean, false);
  }
  
  public String toString()
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("AnimatorSet@");
    ((StringBuilder)localObject1).append(Integer.toHexString(hashCode()));
    ((StringBuilder)localObject1).append("{");
    localObject1 = ((StringBuilder)localObject1).toString();
    int i = mNodes.size();
    for (int j = 0; j < i; j++)
    {
      localObject2 = (Node)mNodes.get(j);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append((String)localObject1);
      localStringBuilder.append("\n    ");
      localStringBuilder.append(mAnimation.toString());
      localObject1 = localStringBuilder.toString();
    }
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("\n}");
    return ((StringBuilder)localObject2).toString();
  }
  
  private static class AnimationEvent
  {
    static final int ANIMATION_DELAY_ENDED = 1;
    static final int ANIMATION_END = 2;
    static final int ANIMATION_START = 0;
    final int mEvent;
    final AnimatorSet.Node mNode;
    
    AnimationEvent(AnimatorSet.Node paramNode, int paramInt)
    {
      mNode = paramNode;
      mEvent = paramInt;
    }
    
    long getTime()
    {
      if (mEvent == 0) {
        return mNode.mStartTime;
      }
      if (mEvent == 1)
      {
        long l1 = mNode.mStartTime;
        long l2 = -1L;
        if (l1 != -1L)
        {
          l2 = mNode.mStartTime;
          l2 = mNode.mAnimation.getStartDelay() + l2;
        }
        return l2;
      }
      return mNode.mEndTime;
    }
    
    public String toString()
    {
      String str;
      if (mEvent == 0) {
        str = "start";
      } else if (mEvent == 1) {
        str = "delay ended";
      } else {
        str = "end";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str);
      localStringBuilder.append(" ");
      localStringBuilder.append(mNode.mAnimation.toString());
      return localStringBuilder.toString();
    }
  }
  
  public class Builder
  {
    private AnimatorSet.Node mCurrentNode;
    
    Builder(Animator paramAnimator)
    {
      AnimatorSet.access$402(AnimatorSet.this, true);
      mCurrentNode = AnimatorSet.this.getNodeForAnimation(paramAnimator);
    }
    
    public Builder after(long paramLong)
    {
      ValueAnimator localValueAnimator = ValueAnimator.ofFloat(new float[] { 0.0F, 1.0F });
      localValueAnimator.setDuration(paramLong);
      after(localValueAnimator);
      return this;
    }
    
    public Builder after(Animator paramAnimator)
    {
      paramAnimator = AnimatorSet.this.getNodeForAnimation(paramAnimator);
      mCurrentNode.addParent(paramAnimator);
      return this;
    }
    
    public Builder before(Animator paramAnimator)
    {
      paramAnimator = AnimatorSet.this.getNodeForAnimation(paramAnimator);
      mCurrentNode.addChild(paramAnimator);
      return this;
    }
    
    public Builder with(Animator paramAnimator)
    {
      paramAnimator = AnimatorSet.this.getNodeForAnimation(paramAnimator);
      mCurrentNode.addSibling(paramAnimator);
      return this;
    }
  }
  
  private static class Node
    implements Cloneable
  {
    Animator mAnimation;
    ArrayList<Node> mChildNodes = null;
    long mEndTime = 0L;
    boolean mEnded = false;
    Node mLatestParent = null;
    ArrayList<Node> mParents;
    boolean mParentsAdded = false;
    ArrayList<Node> mSiblings;
    long mStartTime = 0L;
    long mTotalDuration = 0L;
    
    public Node(Animator paramAnimator)
    {
      mAnimation = paramAnimator;
    }
    
    void addChild(Node paramNode)
    {
      if (mChildNodes == null) {
        mChildNodes = new ArrayList();
      }
      if (!mChildNodes.contains(paramNode))
      {
        mChildNodes.add(paramNode);
        paramNode.addParent(this);
      }
    }
    
    public void addParent(Node paramNode)
    {
      if (mParents == null) {
        mParents = new ArrayList();
      }
      if (!mParents.contains(paramNode))
      {
        mParents.add(paramNode);
        paramNode.addChild(this);
      }
    }
    
    public void addParents(ArrayList<Node> paramArrayList)
    {
      if (paramArrayList == null) {
        return;
      }
      int i = paramArrayList.size();
      for (int j = 0; j < i; j++) {
        addParent((Node)paramArrayList.get(j));
      }
    }
    
    public void addSibling(Node paramNode)
    {
      if (mSiblings == null) {
        mSiblings = new ArrayList();
      }
      if (!mSiblings.contains(paramNode))
      {
        mSiblings.add(paramNode);
        paramNode.addSibling(this);
      }
    }
    
    public Node clone()
    {
      try
      {
        Node localNode = (Node)super.clone();
        mAnimation = mAnimation.clone();
        ArrayList localArrayList;
        if (mChildNodes != null)
        {
          localArrayList = new java/util/ArrayList;
          localArrayList.<init>(mChildNodes);
          mChildNodes = localArrayList;
        }
        if (mSiblings != null)
        {
          localArrayList = new java/util/ArrayList;
          localArrayList.<init>(mSiblings);
          mSiblings = localArrayList;
        }
        if (mParents != null)
        {
          localArrayList = new java/util/ArrayList;
          localArrayList.<init>(mParents);
          mParents = localArrayList;
        }
        mEnded = false;
        return localNode;
      }
      catch (CloneNotSupportedException localCloneNotSupportedException)
      {
        throw new AssertionError();
      }
    }
  }
  
  private class SeekState
  {
    private long mPlayTime = -1L;
    private boolean mSeekingInReverse = false;
    
    private SeekState() {}
    
    long getPlayTime()
    {
      return mPlayTime;
    }
    
    long getPlayTimeNormalized()
    {
      if (mReversing) {
        return getTotalDuration() - mStartDelay - mPlayTime;
      }
      return mPlayTime;
    }
    
    boolean isActive()
    {
      boolean bool;
      if (mPlayTime != -1L) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void reset()
    {
      mPlayTime = -1L;
      mSeekingInReverse = false;
    }
    
    void setPlayTime(long paramLong, boolean paramBoolean)
    {
      if (getTotalDuration() != -1L) {
        mPlayTime = Math.min(paramLong, getTotalDuration() - mStartDelay);
      }
      mPlayTime = Math.max(0L, mPlayTime);
      mSeekingInReverse = paramBoolean;
    }
    
    void updateSeekDirection(boolean paramBoolean)
    {
      if ((paramBoolean) && (getTotalDuration() == -1L)) {
        throw new UnsupportedOperationException("Error: Cannot reverse infinite animator set");
      }
      if ((mPlayTime >= 0L) && (paramBoolean != mSeekingInReverse))
      {
        mPlayTime = (getTotalDuration() - mStartDelay - mPlayTime);
        mSeekingInReverse = paramBoolean;
      }
    }
  }
}
