package android.bluetooth.le;

import android.annotation.SystemApi;
import java.util.List;

@SystemApi
public final class TruncatedFilter
{
  private final ScanFilter mFilter;
  private final List<ResultStorageDescriptor> mStorageDescriptors;
  
  public TruncatedFilter(ScanFilter paramScanFilter, List<ResultStorageDescriptor> paramList)
  {
    mFilter = paramScanFilter;
    mStorageDescriptors = paramList;
  }
  
  public ScanFilter getFilter()
  {
    return mFilter;
  }
  
  public List<ResultStorageDescriptor> getStorageDescriptors()
  {
    return mStorageDescriptors;
  }
}
