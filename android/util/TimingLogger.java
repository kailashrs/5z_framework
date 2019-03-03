package android.util;

import android.os.SystemClock;
import java.util.ArrayList;

public class TimingLogger
{
  private boolean mDisabled;
  private String mLabel;
  ArrayList<String> mSplitLabels;
  ArrayList<Long> mSplits;
  private String mTag;
  
  public TimingLogger(String paramString1, String paramString2)
  {
    reset(paramString1, paramString2);
  }
  
  public void addSplit(String paramString)
  {
    if (mDisabled) {
      return;
    }
    long l = SystemClock.elapsedRealtime();
    mSplits.add(Long.valueOf(l));
    mSplitLabels.add(paramString);
  }
  
  public void dumpToLog()
  {
    if (mDisabled) {
      return;
    }
    String str = mTag;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(mLabel);
    ((StringBuilder)localObject).append(": begin");
    Log.d(str, ((StringBuilder)localObject).toString());
    long l1 = ((Long)mSplits.get(0)).longValue();
    long l2 = l1;
    for (int i = 1; i < mSplits.size(); i++)
    {
      l2 = ((Long)mSplits.get(i)).longValue();
      localObject = (String)mSplitLabels.get(i);
      long l3 = ((Long)mSplits.get(i - 1)).longValue();
      str = mTag;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(mLabel);
      localStringBuilder.append(":      ");
      localStringBuilder.append(l2 - l3);
      localStringBuilder.append(" ms, ");
      localStringBuilder.append((String)localObject);
      Log.d(str, localStringBuilder.toString());
    }
    str = mTag;
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(mLabel);
    ((StringBuilder)localObject).append(": end, ");
    ((StringBuilder)localObject).append(l2 - l1);
    ((StringBuilder)localObject).append(" ms");
    Log.d(str, ((StringBuilder)localObject).toString());
  }
  
  public void reset()
  {
    mDisabled = (Log.isLoggable(mTag, 2) ^ true);
    if (mDisabled) {
      return;
    }
    if (mSplits == null)
    {
      mSplits = new ArrayList();
      mSplitLabels = new ArrayList();
    }
    else
    {
      mSplits.clear();
      mSplitLabels.clear();
    }
    addSplit(null);
  }
  
  public void reset(String paramString1, String paramString2)
  {
    mTag = paramString1;
    mLabel = paramString2;
    reset();
  }
}
