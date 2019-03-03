package android.hardware.camera2.legacy;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.utils.SubmitInfo;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;

public class RequestQueue
{
  private static final long INVALID_FRAME = -1L;
  private static final String TAG = "RequestQueue";
  private long mCurrentFrameNumber = 0L;
  private long mCurrentRepeatingFrameNumber = -1L;
  private int mCurrentRequestId = 0;
  private final List<Long> mJpegSurfaceIds;
  private BurstHolder mRepeatingRequest = null;
  private final ArrayDeque<BurstHolder> mRequestQueue = new ArrayDeque();
  
  public RequestQueue(List<Long> paramList)
  {
    mJpegSurfaceIds = paramList;
  }
  
  private long calculateLastFrame(int paramInt)
  {
    long l = mCurrentFrameNumber;
    Iterator localIterator = mRequestQueue.iterator();
    while (localIterator.hasNext())
    {
      BurstHolder localBurstHolder = (BurstHolder)localIterator.next();
      l += localBurstHolder.getNumberOfRequests();
      if (localBurstHolder.getRequestId() == paramInt) {
        return l - 1L;
      }
    }
    throw new IllegalStateException("At least one request must be in the queue to calculate frame number");
  }
  
  public RequestQueueEntry getNext()
  {
    try
    {
      Object localObject1 = (BurstHolder)mRequestQueue.poll();
      boolean bool;
      if ((localObject1 != null) && (mRequestQueue.size() == 0)) {
        bool = true;
      } else {
        bool = false;
      }
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = localObject1;
        if (mRepeatingRequest != null)
        {
          localObject2 = mRepeatingRequest;
          mCurrentRepeatingFrameNumber = (mCurrentFrameNumber + ((BurstHolder)localObject2).getNumberOfRequests());
        }
      }
      if (localObject2 == null) {
        return null;
      }
      localObject1 = new android/hardware/camera2/legacy/RequestQueue$RequestQueueEntry;
      ((RequestQueueEntry)localObject1).<init>(this, (BurstHolder)localObject2, Long.valueOf(mCurrentFrameNumber), bool);
      mCurrentFrameNumber += ((BurstHolder)localObject2).getNumberOfRequests();
      return localObject1;
    }
    finally {}
  }
  
  public long stopRepeating()
  {
    try
    {
      if (mRepeatingRequest == null)
      {
        Log.e("RequestQueue", "cancel failed: no repeating request exists.");
        return -1L;
      }
      long l = stopRepeating(mRepeatingRequest.getRequestId());
      return l;
    }
    finally {}
  }
  
  public long stopRepeating(int paramInt)
  {
    long l = -1L;
    try
    {
      if ((mRepeatingRequest != null) && (mRepeatingRequest.getRequestId() == paramInt))
      {
        mRepeatingRequest = null;
        if (mCurrentRepeatingFrameNumber == -1L) {
          l = -1L;
        } else {
          l = mCurrentRepeatingFrameNumber - 1L;
        }
        mCurrentRepeatingFrameNumber = -1L;
        Log.i("RequestQueue", "Repeating capture request cancelled.");
      }
      else
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("cancel failed: no repeating request exists for request id: ");
        localStringBuilder.append(paramInt);
        Log.e("RequestQueue", localStringBuilder.toString());
      }
      return l;
    }
    finally {}
  }
  
  public SubmitInfo submit(CaptureRequest[] paramArrayOfCaptureRequest, boolean paramBoolean)
  {
    try
    {
      int i = mCurrentRequestId;
      mCurrentRequestId = (i + 1);
      BurstHolder localBurstHolder = new android/hardware/camera2/legacy/BurstHolder;
      localBurstHolder.<init>(i, paramBoolean, paramArrayOfCaptureRequest, mJpegSurfaceIds);
      long l = -1L;
      if (localBurstHolder.isRepeating())
      {
        Log.i("RequestQueue", "Repeating capture request set.");
        if (mRepeatingRequest != null) {
          if (mCurrentRepeatingFrameNumber == -1L) {
            l = -1L;
          } else {
            l = mCurrentRepeatingFrameNumber - 1L;
          }
        }
        mCurrentRepeatingFrameNumber = -1L;
        mRepeatingRequest = localBurstHolder;
      }
      else
      {
        mRequestQueue.offer(localBurstHolder);
        l = calculateLastFrame(localBurstHolder.getRequestId());
      }
      paramArrayOfCaptureRequest = new SubmitInfo(i, l);
      return paramArrayOfCaptureRequest;
    }
    finally {}
  }
  
  public final class RequestQueueEntry
  {
    private final BurstHolder mBurstHolder;
    private final Long mFrameNumber;
    private final boolean mQueueEmpty;
    
    public RequestQueueEntry(BurstHolder paramBurstHolder, Long paramLong, boolean paramBoolean)
    {
      mBurstHolder = paramBurstHolder;
      mFrameNumber = paramLong;
      mQueueEmpty = paramBoolean;
    }
    
    public BurstHolder getBurstHolder()
    {
      return mBurstHolder;
    }
    
    public Long getFrameNumber()
    {
      return mFrameNumber;
    }
    
    public boolean isQueueEmpty()
    {
      return mQueueEmpty;
    }
  }
}
