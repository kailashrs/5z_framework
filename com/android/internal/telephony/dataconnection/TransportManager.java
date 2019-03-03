package com.android.internal.telephony.dataconnection;

import android.telephony.Rlog;
import java.util.ArrayList;
import java.util.List;

public class TransportManager
{
  private static final String TAG = TransportManager.class.getSimpleName();
  private List<Integer> mAvailableTransports = new ArrayList();
  
  public TransportManager()
  {
    mAvailableTransports.add(Integer.valueOf(1));
  }
  
  private void log(String paramString)
  {
    Rlog.d(TAG, paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e(TAG, paramString);
  }
  
  public List<Integer> getAvailableTransports()
  {
    return new ArrayList(mAvailableTransports);
  }
}
