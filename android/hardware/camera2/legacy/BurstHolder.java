package android.hardware.camera2.legacy;

import android.hardware.camera2.CaptureRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BurstHolder
{
  private static final String TAG = "BurstHolder";
  private final boolean mRepeating;
  private final ArrayList<RequestHolder.Builder> mRequestBuilders = new ArrayList();
  private final int mRequestId;
  
  public BurstHolder(int paramInt, boolean paramBoolean, CaptureRequest[] paramArrayOfCaptureRequest, Collection<Long> paramCollection)
  {
    int i = 0;
    int j = paramArrayOfCaptureRequest.length;
    for (int k = 0; k < j; k++)
    {
      CaptureRequest localCaptureRequest = paramArrayOfCaptureRequest[k];
      mRequestBuilders.add(new RequestHolder.Builder(paramInt, i, localCaptureRequest, paramBoolean, paramCollection));
      i++;
    }
    mRepeating = paramBoolean;
    mRequestId = paramInt;
  }
  
  public int getNumberOfRequests()
  {
    return mRequestBuilders.size();
  }
  
  public int getRequestId()
  {
    return mRequestId;
  }
  
  public boolean isRepeating()
  {
    return mRepeating;
  }
  
  public List<RequestHolder> produceRequestHolders(long paramLong)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    Iterator localIterator = mRequestBuilders.iterator();
    while (localIterator.hasNext())
    {
      localArrayList.add(((RequestHolder.Builder)localIterator.next()).build(i + paramLong));
      i++;
    }
    return localArrayList;
  }
}
