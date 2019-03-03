package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SystemApi
@Deprecated
public class BatchedScanResult
  implements Parcelable
{
  public static final Parcelable.Creator<BatchedScanResult> CREATOR = new Parcelable.Creator()
  {
    public BatchedScanResult createFromParcel(Parcel paramAnonymousParcel)
    {
      BatchedScanResult localBatchedScanResult = new BatchedScanResult();
      int i = paramAnonymousParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      truncated = bool;
      for (i = paramAnonymousParcel.readInt(); i > 0; i--) {
        scanResults.add((ScanResult)ScanResult.CREATOR.createFromParcel(paramAnonymousParcel));
      }
      return localBatchedScanResult;
    }
    
    public BatchedScanResult[] newArray(int paramAnonymousInt)
    {
      return new BatchedScanResult[paramAnonymousInt];
    }
  };
  private static final String TAG = "BatchedScanResult";
  public final List<ScanResult> scanResults = new ArrayList();
  public boolean truncated;
  
  public BatchedScanResult() {}
  
  public BatchedScanResult(BatchedScanResult paramBatchedScanResult)
  {
    truncated = truncated;
    Iterator localIterator = scanResults.iterator();
    while (localIterator.hasNext())
    {
      paramBatchedScanResult = (ScanResult)localIterator.next();
      scanResults.add(new ScanResult(paramBatchedScanResult));
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("BatchedScanResult: ");
    localStringBuffer.append("truncated: ");
    localStringBuffer.append(String.valueOf(truncated));
    localStringBuffer.append("scanResults: [");
    Iterator localIterator = scanResults.iterator();
    while (localIterator.hasNext())
    {
      ScanResult localScanResult = (ScanResult)localIterator.next();
      localStringBuffer.append(" <");
      localStringBuffer.append(localScanResult.toString());
      localStringBuffer.append("> ");
    }
    localStringBuffer.append(" ]");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(truncated);
    paramParcel.writeInt(scanResults.size());
    Iterator localIterator = scanResults.iterator();
    while (localIterator.hasNext()) {
      ((ScanResult)localIterator.next()).writeToParcel(paramParcel, paramInt);
    }
  }
}
