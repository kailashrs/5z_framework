package android.media;

import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public abstract class SubtitleTrack
  implements MediaTimeProvider.OnMediaTimeListener
{
  private static final String TAG = "SubtitleTrack";
  public boolean DEBUG = false;
  protected final Vector<Cue> mActiveCues = new Vector();
  protected CueList mCues;
  private MediaFormat mFormat;
  protected Handler mHandler = new Handler();
  private long mLastTimeMs;
  private long mLastUpdateTimeMs;
  private long mNextScheduledTimeMs = -1L;
  private Runnable mRunnable;
  protected final LongSparseArray<Run> mRunsByEndTime = new LongSparseArray();
  protected final LongSparseArray<Run> mRunsByID = new LongSparseArray();
  protected MediaTimeProvider mTimeProvider;
  protected boolean mVisible;
  
  public SubtitleTrack(MediaFormat paramMediaFormat)
  {
    mFormat = paramMediaFormat;
    mCues = new CueList();
    clearActiveCues();
    mLastTimeMs = -1L;
  }
  
  private void removeRunsByEndTimeIndex(int paramInt)
  {
    Object localObject2;
    for (Object localObject1 = (Run)mRunsByEndTime.valueAt(paramInt); localObject1 != null; localObject1 = localObject2)
    {
      Cue localCue;
      for (localObject2 = mFirstCue; localObject2 != null; localObject2 = localCue)
      {
        mCues.remove((Cue)localObject2);
        localCue = mNextInRun;
        mNextInRun = null;
      }
      mRunsByID.remove(mRunID);
      localObject2 = mNextRunAtEndTimeMs;
      mPrevRunAtEndTimeMs = null;
      mNextRunAtEndTimeMs = null;
    }
    mRunsByEndTime.removeAt(paramInt);
  }
  
  private void takeTime(long paramLong)
  {
    try
    {
      mLastTimeMs = paramLong;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  protected boolean addCue(Cue paramCue)
  {
    try
    {
      mCues.add(paramCue);
      if (mRunID != 0L)
      {
        Run localRun = (Run)mRunsByID.get(mRunID);
        if (localRun == null)
        {
          localObject = new android/media/SubtitleTrack$Run;
          ((Run)localObject).<init>(null);
          mRunsByID.put(mRunID, localObject);
          mEndTimeMs = mEndTimeMs;
        }
        else
        {
          localObject = localRun;
          if (mEndTimeMs < mEndTimeMs)
          {
            mEndTimeMs = mEndTimeMs;
            localObject = localRun;
          }
        }
        mNextInRun = mFirstCue;
        mFirstCue = paramCue;
      }
      long l1 = -1L;
      Object localObject = mTimeProvider;
      long l2 = l1;
      if (localObject != null) {
        try
        {
          l2 = mTimeProvider.getCurrentTimeUs(false, true) / 1000L;
        }
        catch (IllegalStateException localIllegalStateException)
        {
          l2 = l1;
        }
      }
      if (DEBUG)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("mVisible=");
        localStringBuilder.append(mVisible);
        localStringBuilder.append(", ");
        localStringBuilder.append(mStartTimeMs);
        localStringBuilder.append(" <= ");
        localStringBuilder.append(l2);
        localStringBuilder.append(", ");
        localStringBuilder.append(mEndTimeMs);
        localStringBuilder.append(" >= ");
        localStringBuilder.append(mLastTimeMs);
        Log.v("SubtitleTrack", localStringBuilder.toString());
      }
      if ((mVisible) && (mStartTimeMs <= l2) && (mEndTimeMs >= mLastTimeMs))
      {
        if (mRunnable != null) {
          mHandler.removeCallbacks(mRunnable);
        }
        paramCue = new android/media/SubtitleTrack$1;
        paramCue.<init>(this, this, l2);
        mRunnable = paramCue;
        if (mHandler.postDelayed(mRunnable, 10L))
        {
          if (DEBUG) {
            Log.v("SubtitleTrack", "scheduling update");
          }
        }
        else if (DEBUG) {
          Log.w("SubtitleTrack", "failed to schedule subtitle view update");
        }
        return true;
      }
      if ((mVisible) && (mEndTimeMs >= mLastTimeMs) && ((mStartTimeMs < mNextScheduledTimeMs) || (mNextScheduledTimeMs < 0L))) {
        scheduleTimedEvents();
      }
      return false;
    }
    finally {}
  }
  
  protected void clearActiveCues()
  {
    try
    {
      if (DEBUG)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Clearing ");
        localStringBuilder.append(mActiveCues.size());
        localStringBuilder.append(" active cues");
        Log.v("SubtitleTrack", localStringBuilder.toString());
      }
      mActiveCues.clear();
      mLastUpdateTimeMs = -1L;
      return;
    }
    finally {}
  }
  
  protected void finalize()
    throws Throwable
  {
    for (int i = mRunsByEndTime.size() - 1; i >= 0; i--) {
      removeRunsByEndTimeIndex(i);
    }
    super.finalize();
  }
  
  protected void finishedRun(long paramLong)
  {
    if ((paramLong != 0L) && (paramLong != -1L))
    {
      Run localRun = (Run)mRunsByID.get(paramLong);
      if (localRun != null) {
        localRun.storeByEndTimeMs(mRunsByEndTime);
      }
    }
  }
  
  public final MediaFormat getFormat()
  {
    return mFormat;
  }
  
  public abstract RenderingWidget getRenderingWidget();
  
  public int getTrackType()
  {
    int i;
    if (getRenderingWidget() == null) {
      i = 3;
    } else {
      i = 4;
    }
    return i;
  }
  
  public void hide()
  {
    if (!mVisible) {
      return;
    }
    if (mTimeProvider != null) {
      mTimeProvider.cancelNotifications(this);
    }
    RenderingWidget localRenderingWidget = getRenderingWidget();
    if (localRenderingWidget != null) {
      localRenderingWidget.setVisible(false);
    }
    mVisible = false;
  }
  
  protected void onData(SubtitleData paramSubtitleData)
  {
    long l = paramSubtitleData.getStartTimeUs() + 1L;
    onData(paramSubtitleData.getData(), true, l);
    setRunDiscardTimeMs(l, (paramSubtitleData.getStartTimeUs() + paramSubtitleData.getDurationUs()) / 1000L);
  }
  
  public abstract void onData(byte[] paramArrayOfByte, boolean paramBoolean, long paramLong);
  
  public void onSeek(long paramLong)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onSeek ");
      localStringBuilder.append(paramLong);
      Log.d("SubtitleTrack", localStringBuilder.toString());
    }
    try
    {
      paramLong /= 1000L;
      updateActiveCues(true, paramLong);
      takeTime(paramLong);
      updateView(mActiveCues);
      scheduleTimedEvents();
      return;
    }
    finally {}
  }
  
  public void onStop()
  {
    try
    {
      if (DEBUG) {
        Log.d("SubtitleTrack", "onStop");
      }
      clearActiveCues();
      mLastTimeMs = -1L;
      updateView(mActiveCues);
      mNextScheduledTimeMs = -1L;
      mTimeProvider.notifyAt(-1L, this);
      return;
    }
    finally {}
  }
  
  public void onTimedEvent(long paramLong)
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onTimedEvent ");
      localStringBuilder.append(paramLong);
      Log.d("SubtitleTrack", localStringBuilder.toString());
    }
    try
    {
      paramLong /= 1000L;
      updateActiveCues(false, paramLong);
      takeTime(paramLong);
      updateView(mActiveCues);
      scheduleTimedEvents();
      return;
    }
    finally {}
  }
  
  protected void scheduleTimedEvents()
  {
    if (mTimeProvider != null)
    {
      mNextScheduledTimeMs = mCues.nextTimeAfter(mLastTimeMs);
      if (DEBUG)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("sched @");
        ((StringBuilder)localObject).append(mNextScheduledTimeMs);
        ((StringBuilder)localObject).append(" after ");
        ((StringBuilder)localObject).append(mLastTimeMs);
        Log.d("SubtitleTrack", ((StringBuilder)localObject).toString());
      }
      Object localObject = mTimeProvider;
      long l;
      if (mNextScheduledTimeMs >= 0L) {
        l = mNextScheduledTimeMs * 1000L;
      } else {
        l = -1L;
      }
      ((MediaTimeProvider)localObject).notifyAt(l, this);
    }
  }
  
  public void setRunDiscardTimeMs(long paramLong1, long paramLong2)
  {
    if ((paramLong1 != 0L) && (paramLong1 != -1L))
    {
      Run localRun = (Run)mRunsByID.get(paramLong1);
      if (localRun != null)
      {
        mEndTimeMs = paramLong2;
        localRun.storeByEndTimeMs(mRunsByEndTime);
      }
    }
  }
  
  public void setTimeProvider(MediaTimeProvider paramMediaTimeProvider)
  {
    try
    {
      MediaTimeProvider localMediaTimeProvider = mTimeProvider;
      if (localMediaTimeProvider == paramMediaTimeProvider) {
        return;
      }
      if (mTimeProvider != null) {
        mTimeProvider.cancelNotifications(this);
      }
      mTimeProvider = paramMediaTimeProvider;
      if (mTimeProvider != null) {
        mTimeProvider.scheduleUpdate(this);
      }
      return;
    }
    finally {}
  }
  
  public void show()
  {
    if (mVisible) {
      return;
    }
    mVisible = true;
    RenderingWidget localRenderingWidget = getRenderingWidget();
    if (localRenderingWidget != null) {
      localRenderingWidget.setVisible(true);
    }
    if (mTimeProvider != null) {
      mTimeProvider.scheduleUpdate(this);
    }
  }
  
  protected void updateActiveCues(boolean paramBoolean, long paramLong)
  {
    if (!paramBoolean) {
      try
      {
        if (mLastUpdateTimeMs <= paramLong) {
          break label27;
        }
      }
      finally
      {
        break label321;
      }
    }
    clearActiveCues();
    label27:
    Iterator localIterator = mCues.entriesBetween(mLastUpdateTimeMs, paramLong).iterator();
    while (localIterator.hasNext())
    {
      Object localObject2 = (Pair)localIterator.next();
      Cue localCue = (Cue)second;
      if (mEndTimeMs == ((Long)first).longValue())
      {
        if (DEBUG)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Removing ");
          ((StringBuilder)localObject2).append(localCue);
          Log.v("SubtitleTrack", ((StringBuilder)localObject2).toString());
        }
        mActiveCues.remove(localCue);
        if (mRunID == 0L) {
          localIterator.remove();
        }
      }
      else if (mStartTimeMs == ((Long)first).longValue())
      {
        if (DEBUG)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Adding ");
          ((StringBuilder)localObject2).append(localCue);
          Log.v("SubtitleTrack", ((StringBuilder)localObject2).toString());
        }
        if (mInnerTimesMs != null) {
          localCue.onTime(paramLong);
        }
        mActiveCues.add(localCue);
      }
      else if (mInnerTimesMs != null)
      {
        localCue.onTime(paramLong);
      }
    }
    while ((mRunsByEndTime.size() > 0) && (mRunsByEndTime.keyAt(0) <= paramLong)) {
      removeRunsByEndTimeIndex(0);
    }
    mLastUpdateTimeMs = paramLong;
    return;
    label321:
    throw localIterator;
  }
  
  public abstract void updateView(Vector<Cue> paramVector);
  
  public static class Cue
  {
    public long mEndTimeMs;
    public long[] mInnerTimesMs;
    public Cue mNextInRun;
    public long mRunID;
    public long mStartTimeMs;
    
    public Cue() {}
    
    public void onTime(long paramLong) {}
  }
  
  static class CueList
  {
    private static final String TAG = "CueList";
    public boolean DEBUG = false;
    private SortedMap<Long, Vector<SubtitleTrack.Cue>> mCues = new TreeMap();
    
    CueList() {}
    
    private boolean addEvent(SubtitleTrack.Cue paramCue, long paramLong)
    {
      Vector localVector1 = (Vector)mCues.get(Long.valueOf(paramLong));
      Vector localVector2;
      if (localVector1 == null)
      {
        localVector2 = new Vector(2);
        mCues.put(Long.valueOf(paramLong), localVector2);
      }
      else
      {
        localVector2 = localVector1;
        if (localVector1.contains(paramCue)) {
          return false;
        }
      }
      localVector2.add(paramCue);
      return true;
    }
    
    private void removeEvent(SubtitleTrack.Cue paramCue, long paramLong)
    {
      Vector localVector = (Vector)mCues.get(Long.valueOf(paramLong));
      if (localVector != null)
      {
        localVector.remove(paramCue);
        if (localVector.size() == 0) {
          mCues.remove(Long.valueOf(paramLong));
        }
      }
    }
    
    public void add(SubtitleTrack.Cue paramCue)
    {
      if (mStartTimeMs >= mEndTimeMs) {
        return;
      }
      if (!addEvent(paramCue, mStartTimeMs)) {
        return;
      }
      long l1 = mStartTimeMs;
      if (mInnerTimesMs != null)
      {
        long[] arrayOfLong = mInnerTimesMs;
        int i = arrayOfLong.length;
        int j = 0;
        while (j < i)
        {
          long l2 = arrayOfLong[j];
          long l3 = l1;
          if (l2 > l1)
          {
            l3 = l1;
            if (l2 < mEndTimeMs)
            {
              addEvent(paramCue, l2);
              l3 = l2;
            }
          }
          j++;
          l1 = l3;
        }
      }
      addEvent(paramCue, mEndTimeMs);
    }
    
    public Iterable<Pair<Long, SubtitleTrack.Cue>> entriesBetween(final long paramLong1, long paramLong2)
    {
      new Iterable()
      {
        public Iterator<Pair<Long, SubtitleTrack.Cue>> iterator()
        {
          Object localObject;
          if (DEBUG)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("slice (");
            ((StringBuilder)localObject).append(paramLong1);
            ((StringBuilder)localObject).append(", ");
            ((StringBuilder)localObject).append(val$timeMs);
            ((StringBuilder)localObject).append("]=");
            Log.d("CueList", ((StringBuilder)localObject).toString());
          }
          try
          {
            localObject = new SubtitleTrack.CueList.EntryIterator(SubtitleTrack.CueList.this, mCues.subMap(Long.valueOf(paramLong1 + 1L), Long.valueOf(val$timeMs + 1L)));
            return localObject;
          }
          catch (IllegalArgumentException localIllegalArgumentException) {}
          return new SubtitleTrack.CueList.EntryIterator(SubtitleTrack.CueList.this, null);
        }
      };
    }
    
    public long nextTimeAfter(long paramLong)
    {
      try
      {
        SortedMap localSortedMap = mCues.tailMap(Long.valueOf(1L + paramLong));
        if (localSortedMap != null)
        {
          paramLong = ((Long)localSortedMap.firstKey()).longValue();
          return paramLong;
        }
        return -1L;
      }
      catch (NoSuchElementException localNoSuchElementException)
      {
        return -1L;
      }
      catch (IllegalArgumentException localIllegalArgumentException) {}
      return -1L;
    }
    
    public void remove(SubtitleTrack.Cue paramCue)
    {
      removeEvent(paramCue, mStartTimeMs);
      if (mInnerTimesMs != null)
      {
        long[] arrayOfLong = mInnerTimesMs;
        int i = arrayOfLong.length;
        for (int j = 0; j < i; j++) {
          removeEvent(paramCue, arrayOfLong[j]);
        }
      }
      removeEvent(paramCue, mEndTimeMs);
    }
    
    class EntryIterator
      implements Iterator<Pair<Long, SubtitleTrack.Cue>>
    {
      private long mCurrentTimeMs;
      private boolean mDone;
      private Pair<Long, SubtitleTrack.Cue> mLastEntry;
      private Iterator<SubtitleTrack.Cue> mLastListIterator;
      private Iterator<SubtitleTrack.Cue> mListIterator;
      private SortedMap<Long, Vector<SubtitleTrack.Cue>> mRemainingCues;
      
      public EntryIterator()
      {
        Object localObject;
        if (DEBUG)
        {
          this$1 = new StringBuilder();
          append(localObject);
          append("");
          Log.v("CueList", toString());
        }
        mRemainingCues = localObject;
        mLastListIterator = null;
        nextKey();
      }
      
      private void nextKey()
      {
        try
        {
          while (mRemainingCues != null)
          {
            mCurrentTimeMs = ((Long)mRemainingCues.firstKey()).longValue();
            mListIterator = ((Vector)mRemainingCues.get(Long.valueOf(mCurrentTimeMs))).iterator();
            try
            {
              mRemainingCues = mRemainingCues.tailMap(Long.valueOf(mCurrentTimeMs + 1L));
            }
            catch (IllegalArgumentException localIllegalArgumentException)
            {
              mRemainingCues = null;
            }
            mDone = false;
            if (mListIterator.hasNext()) {
              return;
            }
          }
          NoSuchElementException localNoSuchElementException1 = new java/util/NoSuchElementException;
          localNoSuchElementException1.<init>("");
          throw localNoSuchElementException1;
        }
        catch (NoSuchElementException localNoSuchElementException2)
        {
          mDone = true;
          mRemainingCues = null;
          mListIterator = null;
        }
      }
      
      public boolean hasNext()
      {
        return mDone ^ true;
      }
      
      public Pair<Long, SubtitleTrack.Cue> next()
      {
        if (!mDone)
        {
          mLastEntry = new Pair(Long.valueOf(mCurrentTimeMs), (SubtitleTrack.Cue)mListIterator.next());
          mLastListIterator = mListIterator;
          if (!mListIterator.hasNext()) {
            nextKey();
          }
          return mLastEntry;
        }
        throw new NoSuchElementException("");
      }
      
      public void remove()
      {
        if ((mLastListIterator != null) && (mLastEntry.second).mEndTimeMs == ((Long)mLastEntry.first).longValue()))
        {
          mLastListIterator.remove();
          mLastListIterator = null;
          if (((Vector)mCues.get(mLastEntry.first)).size() == 0) {
            mCues.remove(mLastEntry.first);
          }
          SubtitleTrack.Cue localCue = (SubtitleTrack.Cue)mLastEntry.second;
          SubtitleTrack.CueList.this.removeEvent(localCue, mStartTimeMs);
          if (mInnerTimesMs != null) {
            for (long l : mInnerTimesMs) {
              SubtitleTrack.CueList.this.removeEvent(localCue, l);
            }
          }
          return;
        }
        throw new IllegalStateException("");
      }
    }
  }
  
  public static abstract interface RenderingWidget
  {
    public abstract void draw(Canvas paramCanvas);
    
    public abstract void onAttachedToWindow();
    
    public abstract void onDetachedFromWindow();
    
    public abstract void setOnChangedListener(OnChangedListener paramOnChangedListener);
    
    public abstract void setSize(int paramInt1, int paramInt2);
    
    public abstract void setVisible(boolean paramBoolean);
    
    public static abstract interface OnChangedListener
    {
      public abstract void onChanged(SubtitleTrack.RenderingWidget paramRenderingWidget);
    }
  }
  
  private static class Run
  {
    public long mEndTimeMs = -1L;
    public SubtitleTrack.Cue mFirstCue;
    public Run mNextRunAtEndTimeMs;
    public Run mPrevRunAtEndTimeMs;
    public long mRunID = 0L;
    private long mStoredEndTimeMs = -1L;
    
    private Run() {}
    
    public void removeAtEndTimeMs()
    {
      Run localRun = mPrevRunAtEndTimeMs;
      if (mPrevRunAtEndTimeMs != null)
      {
        mPrevRunAtEndTimeMs.mNextRunAtEndTimeMs = mNextRunAtEndTimeMs;
        mPrevRunAtEndTimeMs = null;
      }
      if (mNextRunAtEndTimeMs != null)
      {
        mNextRunAtEndTimeMs.mPrevRunAtEndTimeMs = localRun;
        mNextRunAtEndTimeMs = null;
      }
    }
    
    public void storeByEndTimeMs(LongSparseArray<Run> paramLongSparseArray)
    {
      int i = paramLongSparseArray.indexOfKey(mStoredEndTimeMs);
      if (i >= 0)
      {
        if (mPrevRunAtEndTimeMs == null) {
          if (mNextRunAtEndTimeMs == null) {
            paramLongSparseArray.removeAt(i);
          } else {
            paramLongSparseArray.setValueAt(i, mNextRunAtEndTimeMs);
          }
        }
        removeAtEndTimeMs();
      }
      if (mEndTimeMs >= 0L)
      {
        mPrevRunAtEndTimeMs = null;
        mNextRunAtEndTimeMs = ((Run)paramLongSparseArray.get(mEndTimeMs));
        if (mNextRunAtEndTimeMs != null) {
          mNextRunAtEndTimeMs.mPrevRunAtEndTimeMs = this;
        }
        paramLongSparseArray.put(mEndTimeMs, this);
        mStoredEndTimeMs = mEndTimeMs;
      }
    }
  }
}
