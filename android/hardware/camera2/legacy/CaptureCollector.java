package android.hardware.camera2.legacy;

import android.hardware.camera2.CaptureRequest;
import android.util.Log;
import android.util.MutableLong;
import android.util.Pair;
import android.view.Surface;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CaptureCollector
{
  private static final boolean DEBUG = false;
  private static final int FLAG_RECEIVED_ALL_JPEG = 3;
  private static final int FLAG_RECEIVED_ALL_PREVIEW = 12;
  private static final int FLAG_RECEIVED_JPEG = 1;
  private static final int FLAG_RECEIVED_JPEG_TS = 2;
  private static final int FLAG_RECEIVED_PREVIEW = 4;
  private static final int FLAG_RECEIVED_PREVIEW_TS = 8;
  private static final int MAX_JPEGS_IN_FLIGHT = 1;
  private static final String TAG = "CaptureCollector";
  private final TreeSet<CaptureHolder> mActiveRequests;
  private final ArrayList<CaptureHolder> mCompletedRequests = new ArrayList();
  private final CameraDeviceState mDeviceState;
  private int mInFlight = 0;
  private int mInFlightPreviews = 0;
  private final Condition mIsEmpty;
  private final ArrayDeque<CaptureHolder> mJpegCaptureQueue;
  private final ArrayDeque<CaptureHolder> mJpegProduceQueue;
  private final ReentrantLock mLock = new ReentrantLock();
  private final int mMaxInFlight;
  private final Condition mNotFull;
  private final ArrayDeque<CaptureHolder> mPreviewCaptureQueue;
  private final ArrayDeque<CaptureHolder> mPreviewProduceQueue;
  private final Condition mPreviewsEmpty;
  
  public CaptureCollector(int paramInt, CameraDeviceState paramCameraDeviceState)
  {
    mMaxInFlight = paramInt;
    mJpegCaptureQueue = new ArrayDeque(1);
    mJpegProduceQueue = new ArrayDeque(1);
    mPreviewCaptureQueue = new ArrayDeque(mMaxInFlight);
    mPreviewProduceQueue = new ArrayDeque(mMaxInFlight);
    mActiveRequests = new TreeSet();
    mIsEmpty = mLock.newCondition();
    mNotFull = mLock.newCondition();
    mPreviewsEmpty = mLock.newCondition();
    mDeviceState = paramCameraDeviceState;
  }
  
  private void onPreviewCompleted()
  {
    mInFlightPreviews -= 1;
    if (mInFlightPreviews >= 0)
    {
      if (mInFlightPreviews == 0) {
        mPreviewsEmpty.signalAll();
      }
      return;
    }
    throw new IllegalStateException("More preview captures completed than requests queued.");
  }
  
  private void onRequestCompleted(CaptureHolder paramCaptureHolder)
  {
    mInFlight -= 1;
    if (mInFlight >= 0)
    {
      mCompletedRequests.add(paramCaptureHolder);
      mActiveRequests.remove(paramCaptureHolder);
      mNotFull.signalAll();
      if (mInFlight == 0) {
        mIsEmpty.signalAll();
      }
      return;
    }
    throw new IllegalStateException("More captures completed than requests queued.");
  }
  
  private boolean removeRequestIfCompleted(RequestHolder paramRequestHolder, MutableLong paramMutableLong)
  {
    int i = 0;
    Iterator localIterator = mCompletedRequests.iterator();
    while (localIterator.hasNext())
    {
      CaptureHolder localCaptureHolder = (CaptureHolder)localIterator.next();
      if (mRequest.equals(paramRequestHolder))
      {
        value = mTimestamp;
        mCompletedRequests.remove(i);
        return true;
      }
      i++;
    }
    return false;
  }
  
  public void failAll()
  {
    ReentrantLock localReentrantLock = mLock;
    localReentrantLock.lock();
    try
    {
      for (;;)
      {
        CaptureHolder localCaptureHolder = (CaptureHolder)mActiveRequests.pollFirst();
        if (localCaptureHolder == null) {
          break;
        }
        localCaptureHolder.setPreviewFailed();
        localCaptureHolder.setJpegFailed();
      }
      mPreviewCaptureQueue.clear();
      mPreviewProduceQueue.clear();
      mJpegCaptureQueue.clear();
      mJpegProduceQueue.clear();
      return;
    }
    finally
    {
      localReentrantLock.unlock();
    }
  }
  
  public void failNextJpeg()
  {
    ReentrantLock localReentrantLock = mLock;
    localReentrantLock.lock();
    try
    {
      CaptureHolder localCaptureHolder1 = (CaptureHolder)mJpegCaptureQueue.peek();
      CaptureHolder localCaptureHolder2 = (CaptureHolder)mJpegProduceQueue.peek();
      if ((localCaptureHolder1 != null) && ((localCaptureHolder2 == null) || (localCaptureHolder1.compareTo(localCaptureHolder2) <= 0))) {
        localCaptureHolder2 = localCaptureHolder1;
      }
      if (localCaptureHolder2 != null)
      {
        mJpegCaptureQueue.remove(localCaptureHolder2);
        mJpegProduceQueue.remove(localCaptureHolder2);
        mActiveRequests.remove(localCaptureHolder2);
        localCaptureHolder2.setJpegFailed();
      }
      return;
    }
    finally
    {
      localReentrantLock.unlock();
    }
  }
  
  public void failNextPreview()
  {
    ReentrantLock localReentrantLock = mLock;
    localReentrantLock.lock();
    try
    {
      Object localObject1 = (CaptureHolder)mPreviewCaptureQueue.peek();
      CaptureHolder localCaptureHolder = (CaptureHolder)mPreviewProduceQueue.peek();
      if ((localObject1 == null) || ((localCaptureHolder == null) || (((CaptureHolder)localObject1).compareTo(localCaptureHolder) > 0))) {
        localObject1 = localCaptureHolder;
      }
      if (localObject1 != null)
      {
        mPreviewCaptureQueue.remove(localObject1);
        mPreviewProduceQueue.remove(localObject1);
        mActiveRequests.remove(localObject1);
        ((CaptureHolder)localObject1).setPreviewFailed();
      }
      return;
    }
    finally
    {
      localReentrantLock.unlock();
    }
  }
  
  public boolean hasPendingPreviewCaptures()
  {
    ReentrantLock localReentrantLock = mLock;
    localReentrantLock.lock();
    try
    {
      boolean bool = mPreviewCaptureQueue.isEmpty();
      return bool ^ true;
    }
    finally
    {
      localReentrantLock.unlock();
    }
  }
  
  public RequestHolder jpegCaptured(long paramLong)
  {
    ReentrantLock localReentrantLock = mLock;
    localReentrantLock.lock();
    try
    {
      Object localObject1 = (CaptureHolder)mJpegCaptureQueue.poll();
      if (localObject1 == null)
      {
        Log.w("CaptureCollector", "jpegCaptured called with no jpeg request on queue!");
        return null;
      }
      ((CaptureHolder)localObject1).setJpegTimestamp(paramLong);
      localObject1 = mRequest;
      return localObject1;
    }
    finally
    {
      localReentrantLock.unlock();
    }
  }
  
  public Pair<RequestHolder, Long> jpegProduced()
  {
    ReentrantLock localReentrantLock = mLock;
    localReentrantLock.lock();
    try
    {
      Object localObject1 = (CaptureHolder)mJpegProduceQueue.poll();
      if (localObject1 == null)
      {
        Log.w("CaptureCollector", "jpegProduced called with no jpeg request on queue!");
        return null;
      }
      ((CaptureHolder)localObject1).setJpegProduced();
      localObject1 = new Pair(mRequest, Long.valueOf(mTimestamp));
      return localObject1;
    }
    finally
    {
      localReentrantLock.unlock();
    }
  }
  
  public Pair<RequestHolder, Long> previewCaptured(long paramLong)
  {
    ReentrantLock localReentrantLock = mLock;
    localReentrantLock.lock();
    try
    {
      Object localObject1 = (CaptureHolder)mPreviewCaptureQueue.poll();
      if (localObject1 == null) {
        return null;
      }
      ((CaptureHolder)localObject1).setPreviewTimestamp(paramLong);
      localObject1 = new Pair(mRequest, Long.valueOf(mTimestamp));
      return localObject1;
    }
    finally
    {
      localReentrantLock.unlock();
    }
  }
  
  public RequestHolder previewProduced()
  {
    ReentrantLock localReentrantLock = mLock;
    localReentrantLock.lock();
    try
    {
      Object localObject1 = (CaptureHolder)mPreviewProduceQueue.poll();
      if (localObject1 == null)
      {
        Log.w("CaptureCollector", "previewProduced called with no preview request on queue!");
        return null;
      }
      ((CaptureHolder)localObject1).setPreviewProduced();
      localObject1 = mRequest;
      return localObject1;
    }
    finally
    {
      localReentrantLock.unlock();
    }
  }
  
  public boolean queueRequest(RequestHolder paramRequestHolder, LegacyRequest paramLegacyRequest, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException
  {
    paramLegacyRequest = new CaptureHolder(paramRequestHolder, paramLegacyRequest);
    long l = paramTimeUnit.toNanos(paramLong);
    paramRequestHolder = mLock;
    paramRequestHolder.lock();
    try
    {
      if ((!needsJpeg) && (!needsPreview))
      {
        paramLegacyRequest = new java/lang/IllegalStateException;
        paramLegacyRequest.<init>("Request must target at least one output surface!");
        throw paramLegacyRequest;
      }
      paramLong = l;
      int i;
      if (needsJpeg)
      {
        for (paramLong = l;; paramLong = mIsEmpty.awaitNanos(paramLong))
        {
          i = mInFlight;
          if (i <= 0) {
            break;
          }
          if (paramLong <= 0L) {
            return false;
          }
        }
        mJpegCaptureQueue.add(paramLegacyRequest);
        mJpegProduceQueue.add(paramLegacyRequest);
      }
      if (needsPreview)
      {
        for (;;)
        {
          int j = mInFlight;
          i = mMaxInFlight;
          if (j < i) {
            break;
          }
          if (paramLong <= 0L) {
            return false;
          }
          paramLong = mNotFull.awaitNanos(paramLong);
        }
        mPreviewCaptureQueue.add(paramLegacyRequest);
        mPreviewProduceQueue.add(paramLegacyRequest);
        mInFlightPreviews += 1;
      }
      mActiveRequests.add(paramLegacyRequest);
      mInFlight += 1;
      return true;
    }
    finally
    {
      paramRequestHolder.unlock();
    }
  }
  
  public boolean waitForEmpty(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException
  {
    paramLong = paramTimeUnit.toNanos(paramLong);
    ReentrantLock localReentrantLock = mLock;
    localReentrantLock.lock();
    try
    {
      for (;;)
      {
        int i = mInFlight;
        if (i <= 0) {
          break;
        }
        if (paramLong <= 0L) {
          return false;
        }
        paramLong = mIsEmpty.awaitNanos(paramLong);
      }
      return true;
    }
    finally
    {
      localReentrantLock.unlock();
    }
  }
  
  public boolean waitForPreviewsEmpty(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException
  {
    paramLong = paramTimeUnit.toNanos(paramLong);
    paramTimeUnit = mLock;
    paramTimeUnit.lock();
    try
    {
      for (;;)
      {
        int i = mInFlightPreviews;
        if (i <= 0) {
          break;
        }
        if (paramLong <= 0L) {
          return false;
        }
        paramLong = mPreviewsEmpty.awaitNanos(paramLong);
      }
      return true;
    }
    finally
    {
      paramTimeUnit.unlock();
    }
  }
  
  public boolean waitForRequestCompleted(RequestHolder paramRequestHolder, long paramLong, TimeUnit paramTimeUnit, MutableLong paramMutableLong)
    throws InterruptedException
  {
    paramLong = paramTimeUnit.toNanos(paramLong);
    paramTimeUnit = mLock;
    paramTimeUnit.lock();
    try
    {
      for (;;)
      {
        boolean bool = removeRequestIfCompleted(paramRequestHolder, paramMutableLong);
        if (bool) {
          break;
        }
        if (paramLong <= 0L) {
          return false;
        }
        paramLong = mNotFull.awaitNanos(paramLong);
      }
      return true;
    }
    finally
    {
      paramTimeUnit.unlock();
    }
  }
  
  private class CaptureHolder
    implements Comparable<CaptureHolder>
  {
    private boolean mCompleted = false;
    private boolean mFailedJpeg = false;
    private boolean mFailedPreview = false;
    private boolean mHasStarted = false;
    private final LegacyRequest mLegacy;
    private boolean mPreviewCompleted = false;
    private int mReceivedFlags = 0;
    private final RequestHolder mRequest;
    private long mTimestamp = 0L;
    public final boolean needsJpeg;
    public final boolean needsPreview;
    
    public CaptureHolder(RequestHolder paramRequestHolder, LegacyRequest paramLegacyRequest)
    {
      mRequest = paramRequestHolder;
      mLegacy = paramLegacyRequest;
      needsJpeg = paramRequestHolder.hasJpegTargets();
      needsPreview = paramRequestHolder.hasPreviewTargets();
    }
    
    public int compareTo(CaptureHolder paramCaptureHolder)
    {
      int i;
      if (mRequest.getFrameNumber() > mRequest.getFrameNumber()) {
        i = 1;
      } else if (mRequest.getFrameNumber() == mRequest.getFrameNumber()) {
        i = 0;
      } else {
        i = -1;
      }
      return i;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool;
      if (((paramObject instanceof CaptureHolder)) && (compareTo((CaptureHolder)paramObject) == 0)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isCompleted()
    {
      boolean bool;
      if ((needsJpeg == isJpegCompleted()) && (needsPreview == isPreviewCompleted())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isJpegCompleted()
    {
      boolean bool;
      if ((mReceivedFlags & 0x3) == 3) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isPreviewCompleted()
    {
      boolean bool;
      if ((mReceivedFlags & 0xC) == 12) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void setJpegFailed()
    {
      if ((needsJpeg) && (!isJpegCompleted()))
      {
        mFailedJpeg = true;
        mReceivedFlags = (0x1 | mReceivedFlags);
        mReceivedFlags |= 0x2;
        tryComplete();
        return;
      }
    }
    
    public void setJpegProduced()
    {
      if (needsJpeg)
      {
        if (!isCompleted())
        {
          mReceivedFlags |= 0x1;
          tryComplete();
          return;
        }
        throw new IllegalStateException("setJpegProduced called on already completed request.");
      }
      throw new IllegalStateException("setJpegProduced called for capture with no jpeg targets.");
    }
    
    public void setJpegTimestamp(long paramLong)
    {
      if (needsJpeg)
      {
        if (!isCompleted())
        {
          mReceivedFlags |= 0x2;
          if (mTimestamp == 0L) {
            mTimestamp = paramLong;
          }
          if (!mHasStarted)
          {
            mHasStarted = true;
            mDeviceState.setCaptureStart(mRequest, mTimestamp, -1);
          }
          tryComplete();
          return;
        }
        throw new IllegalStateException("setJpegTimestamp called on already completed request.");
      }
      throw new IllegalStateException("setJpegTimestamp called for capture with no jpeg targets.");
    }
    
    public void setPreviewFailed()
    {
      if ((needsPreview) && (!isPreviewCompleted()))
      {
        mFailedPreview = true;
        mReceivedFlags |= 0x4;
        mReceivedFlags |= 0x8;
        tryComplete();
        return;
      }
    }
    
    public void setPreviewProduced()
    {
      if (needsPreview)
      {
        if (!isCompleted())
        {
          mReceivedFlags |= 0x4;
          tryComplete();
          return;
        }
        throw new IllegalStateException("setPreviewProduced called on already completed request.");
      }
      throw new IllegalStateException("setPreviewProduced called for capture with no preview targets.");
    }
    
    public void setPreviewTimestamp(long paramLong)
    {
      if (needsPreview)
      {
        if (!isCompleted())
        {
          mReceivedFlags |= 0x8;
          if (mTimestamp == 0L) {
            mTimestamp = paramLong;
          }
          if ((!needsJpeg) && (!mHasStarted))
          {
            mHasStarted = true;
            mDeviceState.setCaptureStart(mRequest, mTimestamp, -1);
          }
          tryComplete();
          return;
        }
        throw new IllegalStateException("setPreviewTimestamp called on already completed request.");
      }
      throw new IllegalStateException("setPreviewTimestamp called for capture with no preview targets.");
    }
    
    public void tryComplete()
    {
      if ((!mPreviewCompleted) && (needsPreview) && (isPreviewCompleted()))
      {
        CaptureCollector.this.onPreviewCompleted();
        mPreviewCompleted = true;
      }
      if ((isCompleted()) && (!mCompleted))
      {
        if ((mFailedPreview) || (mFailedJpeg)) {
          if (!mHasStarted)
          {
            mRequest.failRequest();
            mDeviceState.setCaptureStart(mRequest, mTimestamp, 3);
          }
          else
          {
            Iterator localIterator = mRequest.getRequest().getTargets().iterator();
            while (localIterator.hasNext())
            {
              Surface localSurface = (Surface)localIterator.next();
              try
              {
                if (mRequest.jpegType(localSurface))
                {
                  if (mFailedJpeg) {
                    mDeviceState.setCaptureResult(mRequest, null, 5, localSurface);
                  }
                }
                else if (mFailedPreview) {
                  mDeviceState.setCaptureResult(mRequest, null, 5, localSurface);
                }
              }
              catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException)
              {
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append("Unexpected exception when querying Surface: ");
                localStringBuilder.append(localBufferQueueAbandonedException);
                Log.e("CaptureCollector", localStringBuilder.toString());
              }
            }
          }
        }
        CaptureCollector.this.onRequestCompleted(this);
        mCompleted = true;
      }
    }
  }
}
