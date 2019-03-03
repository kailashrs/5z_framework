package android.view.textclassifier;

import android.metrics.LogMaker;
import android.util.ArrayMap;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.Preconditions;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
public final class GenerateLinksLogger
{
  private static final boolean DEBUG_LOG_ENABLED = false;
  private static final String LOG_TAG = "GenerateLinksLogger";
  private static final String ZERO = "0";
  private final MetricsLogger mMetricsLogger;
  private final Random mRng;
  private final int mSampleRate;
  
  public GenerateLinksLogger(int paramInt)
  {
    mSampleRate = paramInt;
    mRng = new Random(System.nanoTime());
    mMetricsLogger = new MetricsLogger();
  }
  
  @VisibleForTesting
  public GenerateLinksLogger(int paramInt, MetricsLogger paramMetricsLogger)
  {
    mSampleRate = paramInt;
    mRng = new Random(System.nanoTime());
    mMetricsLogger = paramMetricsLogger;
  }
  
  private static void debugLog(LogMaker paramLogMaker) {}
  
  private boolean shouldLog()
  {
    int i = mSampleRate;
    boolean bool = true;
    if (i <= 1) {
      return true;
    }
    if (mRng.nextInt(mSampleRate) != 0) {
      bool = false;
    }
    return bool;
  }
  
  private void writeStats(String paramString1, String paramString2, String paramString3, LinkifyStats paramLinkifyStats, CharSequence paramCharSequence, long paramLong)
  {
    paramString1 = new LogMaker(1313).setPackageName(paramString2).addTaggedData(1319, paramString1).addTaggedData(1316, Integer.valueOf(mNumLinks)).addTaggedData(1317, Integer.valueOf(mNumLinksTextLength)).addTaggedData(1315, Integer.valueOf(paramCharSequence.length())).addTaggedData(1314, Long.valueOf(paramLong));
    if (paramString3 != null) {
      paramString1.addTaggedData(1318, paramString3);
    }
    mMetricsLogger.write(paramString1);
    debugLog(paramString1);
  }
  
  public void logGenerateLinks(CharSequence paramCharSequence, TextLinks paramTextLinks, String paramString, long paramLong)
  {
    Preconditions.checkNotNull(paramCharSequence);
    Preconditions.checkNotNull(paramTextLinks);
    Preconditions.checkNotNull(paramString);
    if (!shouldLog()) {
      return;
    }
    Object localObject1 = new LinkifyStats(null);
    Object localObject2 = new ArrayMap();
    Iterator localIterator = paramTextLinks.getLinks().iterator();
    while (localIterator.hasNext())
    {
      TextLinks.TextLink localTextLink = (TextLinks.TextLink)localIterator.next();
      if (localTextLink.getEntityCount() != 0)
      {
        paramTextLinks = localTextLink.getEntity(0);
        if ((paramTextLinks != null) && (!"other".equals(paramTextLinks)) && (!"".equals(paramTextLinks)))
        {
          ((LinkifyStats)localObject1).countLink(localTextLink);
          ((LinkifyStats)((Map)localObject2).computeIfAbsent(paramTextLinks, _..Lambda.GenerateLinksLogger.vmbT_h7MLlbrIm0lJJwA_eHQhXk.INSTANCE)).countLink(localTextLink);
        }
      }
    }
    paramTextLinks = UUID.randomUUID().toString();
    writeStats(paramTextLinks, paramString, null, (LinkifyStats)localObject1, paramCharSequence, paramLong);
    localObject2 = ((Map)localObject2).entrySet().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (Map.Entry)((Iterator)localObject2).next();
      writeStats(paramTextLinks, paramString, (String)((Map.Entry)localObject1).getKey(), (LinkifyStats)((Map.Entry)localObject1).getValue(), paramCharSequence, paramLong);
    }
  }
  
  private static final class LinkifyStats
  {
    int mNumLinks;
    int mNumLinksTextLength;
    
    private LinkifyStats() {}
    
    void countLink(TextLinks.TextLink paramTextLink)
    {
      mNumLinks += 1;
      mNumLinksTextLength += paramTextLink.getEnd() - paramTextLink.getStart();
    }
  }
}
