package com.android.internal.inputmethod;

import android.icu.util.ULocale;
import android.os.LocaleList;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public final class LocaleUtils
{
  public LocaleUtils() {}
  
  private static byte calculateMatchingSubScore(ULocale paramULocale1, ULocale paramULocale2)
  {
    if (paramULocale1.equals(paramULocale2)) {
      return 3;
    }
    String str = paramULocale1.getScript();
    if ((!str.isEmpty()) && (str.equals(paramULocale2.getScript())))
    {
      paramULocale1 = paramULocale1.getCountry();
      if ((!paramULocale1.isEmpty()) && (paramULocale1.equals(paramULocale2.getCountry()))) {
        return 3;
      }
      return 2;
    }
    return 1;
  }
  
  @VisibleForTesting
  public static <T> void filterByLanguage(List<T> paramList, LocaleExtractor<T> paramLocaleExtractor, LocaleList paramLocaleList, ArrayList<T> paramArrayList)
  {
    if (paramLocaleList.isEmpty()) {
      return;
    }
    int i = paramLocaleList.size();
    HashMap localHashMap = new HashMap();
    byte[] arrayOfByte = new byte[i];
    ULocale[] arrayOfULocale = new ULocale[i];
    int j = paramList.size();
    int k = 0;
    for (int m = 0; m < j; m++)
    {
      Object localObject1 = paramLocaleExtractor.get(paramList.get(m));
      if (localObject1 != null)
      {
        n = 1;
        int i1 = 0;
        Object localObject2;
        while (i1 < i)
        {
          localObject2 = paramLocaleList.get(i1);
          int i2;
          if (!TextUtils.equals(((Locale)localObject1).getLanguage(), ((Locale)localObject2).getLanguage()))
          {
            arrayOfByte[i1] = ((byte)0);
            i2 = n;
          }
          else
          {
            if (arrayOfULocale[i1] == null) {
              arrayOfULocale[i1] = ULocale.addLikelySubtags(ULocale.forLocale((Locale)localObject2));
            }
            arrayOfByte[i1] = calculateMatchingSubScore(arrayOfULocale[i1], ULocale.addLikelySubtags(ULocale.forLocale((Locale)localObject1)));
            i2 = n;
            if (n != 0)
            {
              i2 = n;
              if (arrayOfByte[i1] != 0) {
                i2 = 0;
              }
            }
          }
          i1++;
          n = i2;
        }
        if (n == 0)
        {
          localObject1 = ((Locale)localObject1).getLanguage();
          localObject2 = (ScoreEntry)localHashMap.get(localObject1);
          if (localObject2 == null) {
            localHashMap.put(localObject1, new ScoreEntry(arrayOfByte, m));
          } else {
            ((ScoreEntry)localObject2).updateIfBetter(arrayOfByte, m);
          }
        }
      }
    }
    paramLocaleExtractor = (ScoreEntry[])localHashMap.values().toArray(new ScoreEntry[localHashMap.size()]);
    Arrays.sort(paramLocaleExtractor);
    int n = paramLocaleExtractor.length;
    for (m = k; m < n; m++) {
      paramArrayList.add(paramList.get(mIndex));
    }
  }
  
  @VisibleForTesting
  public static abstract interface LocaleExtractor<T>
  {
    public abstract Locale get(T paramT);
  }
  
  private static final class ScoreEntry
    implements Comparable<ScoreEntry>
  {
    public int mIndex = -1;
    public final byte[] mScore;
    
    ScoreEntry(byte[] paramArrayOfByte, int paramInt)
    {
      mScore = new byte[paramArrayOfByte.length];
      set(paramArrayOfByte, paramInt);
    }
    
    private static int compare(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      for (int i = 0; i < paramArrayOfByte1.length; i++)
      {
        if (paramArrayOfByte1[i] > paramArrayOfByte2[i]) {
          return 1;
        }
        if (paramArrayOfByte1[i] < paramArrayOfByte2[i]) {
          return -1;
        }
      }
      return 0;
    }
    
    private void set(byte[] paramArrayOfByte, int paramInt)
    {
      for (int i = 0; i < mScore.length; i++) {
        mScore[i] = ((byte)paramArrayOfByte[i]);
      }
      mIndex = paramInt;
    }
    
    public int compareTo(ScoreEntry paramScoreEntry)
    {
      return -1 * compare(mScore, mScore);
    }
    
    public void updateIfBetter(byte[] paramArrayOfByte, int paramInt)
    {
      if (compare(mScore, paramArrayOfByte) == -1) {
        set(paramArrayOfByte, paramInt);
      }
    }
  }
}
