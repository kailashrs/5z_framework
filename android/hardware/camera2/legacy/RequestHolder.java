package android.hardware.camera2.legacy;

import android.hardware.camera2.CaptureRequest;
import android.util.Log;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.Collection;
import java.util.Iterator;

public class RequestHolder
{
  private static final String TAG = "RequestHolder";
  private volatile boolean mFailed = false;
  private final long mFrameNumber;
  private final Collection<Long> mJpegSurfaceIds;
  private final int mNumJpegTargets;
  private final int mNumPreviewTargets;
  private boolean mOutputAbandoned = false;
  private final boolean mRepeating;
  private final CaptureRequest mRequest;
  private final int mRequestId;
  private final int mSubsequeceId;
  
  private RequestHolder(int paramInt1, int paramInt2, CaptureRequest paramCaptureRequest, boolean paramBoolean, long paramLong, int paramInt3, int paramInt4, Collection<Long> paramCollection)
  {
    mRepeating = paramBoolean;
    mRequest = paramCaptureRequest;
    mRequestId = paramInt1;
    mSubsequeceId = paramInt2;
    mFrameNumber = paramLong;
    mNumJpegTargets = paramInt3;
    mNumPreviewTargets = paramInt4;
    mJpegSurfaceIds = paramCollection;
  }
  
  public void failRequest()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Capture failed for request: ");
    localStringBuilder.append(getRequestId());
    Log.w("RequestHolder", localStringBuilder.toString());
    mFailed = true;
  }
  
  public long getFrameNumber()
  {
    return mFrameNumber;
  }
  
  public Collection<Surface> getHolderTargets()
  {
    return getRequest().getTargets();
  }
  
  public CaptureRequest getRequest()
  {
    return mRequest;
  }
  
  public int getRequestId()
  {
    return mRequestId;
  }
  
  public int getSubsequeceId()
  {
    return mSubsequeceId;
  }
  
  public boolean hasJpegTargets()
  {
    boolean bool;
    if (mNumJpegTargets > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasPreviewTargets()
  {
    boolean bool;
    if (mNumPreviewTargets > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOutputAbandoned()
  {
    return mOutputAbandoned;
  }
  
  public boolean isRepeating()
  {
    return mRepeating;
  }
  
  public boolean jpegType(Surface paramSurface)
    throws LegacyExceptionUtils.BufferQueueAbandonedException
  {
    return LegacyCameraDevice.containsSurfaceId(paramSurface, mJpegSurfaceIds);
  }
  
  public int numJpegTargets()
  {
    return mNumJpegTargets;
  }
  
  public int numPreviewTargets()
  {
    return mNumPreviewTargets;
  }
  
  public boolean requestFailed()
  {
    return mFailed;
  }
  
  public void setOutputAbandoned()
  {
    mOutputAbandoned = true;
  }
  
  public static final class Builder
  {
    private final Collection<Long> mJpegSurfaceIds;
    private final int mNumJpegTargets;
    private final int mNumPreviewTargets;
    private final boolean mRepeating;
    private final CaptureRequest mRequest;
    private final int mRequestId;
    private final int mSubsequenceId;
    
    public Builder(int paramInt1, int paramInt2, CaptureRequest paramCaptureRequest, boolean paramBoolean, Collection<Long> paramCollection)
    {
      Preconditions.checkNotNull(paramCaptureRequest, "request must not be null");
      mRequestId = paramInt1;
      mSubsequenceId = paramInt2;
      mRequest = paramCaptureRequest;
      mRepeating = paramBoolean;
      mJpegSurfaceIds = paramCollection;
      mNumJpegTargets = numJpegTargets(mRequest);
      mNumPreviewTargets = numPreviewTargets(mRequest);
    }
    
    private boolean jpegType(Surface paramSurface)
      throws LegacyExceptionUtils.BufferQueueAbandonedException
    {
      return LegacyCameraDevice.containsSurfaceId(paramSurface, mJpegSurfaceIds);
    }
    
    private int numJpegTargets(CaptureRequest paramCaptureRequest)
    {
      int i = 0;
      paramCaptureRequest = paramCaptureRequest.getTargets().iterator();
      while (paramCaptureRequest.hasNext())
      {
        Surface localSurface = (Surface)paramCaptureRequest.next();
        try
        {
          boolean bool = jpegType(localSurface);
          int j = i;
          if (bool) {
            j = i + 1;
          }
          i = j;
        }
        catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException)
        {
          Log.d("RequestHolder", "Surface abandoned, skipping...", localBufferQueueAbandonedException);
        }
      }
      return i;
    }
    
    private int numPreviewTargets(CaptureRequest paramCaptureRequest)
    {
      int i = 0;
      paramCaptureRequest = paramCaptureRequest.getTargets().iterator();
      while (paramCaptureRequest.hasNext())
      {
        Surface localSurface = (Surface)paramCaptureRequest.next();
        try
        {
          boolean bool = previewType(localSurface);
          int j = i;
          if (bool) {
            j = i + 1;
          }
          i = j;
        }
        catch (LegacyExceptionUtils.BufferQueueAbandonedException localBufferQueueAbandonedException)
        {
          Log.d("RequestHolder", "Surface abandoned, skipping...", localBufferQueueAbandonedException);
        }
      }
      return i;
    }
    
    private boolean previewType(Surface paramSurface)
      throws LegacyExceptionUtils.BufferQueueAbandonedException
    {
      return jpegType(paramSurface) ^ true;
    }
    
    public RequestHolder build(long paramLong)
    {
      return new RequestHolder(mRequestId, mSubsequenceId, mRequest, mRepeating, paramLong, mNumJpegTargets, mNumPreviewTargets, mJpegSurfaceIds, null);
    }
  }
}
