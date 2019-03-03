package android.security.net.config;

import android.util.ArraySet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public final class PinSet
{
  public static final PinSet EMPTY_PINSET = new PinSet(Collections.emptySet(), Long.MAX_VALUE);
  public final long expirationTime;
  public final Set<Pin> pins;
  
  public PinSet(Set<Pin> paramSet, long paramLong)
  {
    if (paramSet != null)
    {
      pins = paramSet;
      expirationTime = paramLong;
      return;
    }
    throw new NullPointerException("pins must not be null");
  }
  
  Set<String> getPinAlgorithms()
  {
    ArraySet localArraySet = new ArraySet();
    Iterator localIterator = pins.iterator();
    while (localIterator.hasNext()) {
      localArraySet.add(nextdigestAlgorithm);
    }
    return localArraySet;
  }
}
