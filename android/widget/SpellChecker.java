package android.widget;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.WordIterator;
import android.text.style.SpellCheckSpan;
import android.text.style.SuggestionSpan;
import android.util.Log;
import android.util.LruCache;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.util.Locale;

public class SpellChecker
  implements SpellCheckerSession.SpellCheckerSessionListener
{
  public static final int AVERAGE_WORD_LENGTH = 7;
  private static final boolean DBG = false;
  public static final int MAX_NUMBER_OF_WORDS = 50;
  private static final int MIN_SENTENCE_LENGTH = 50;
  private static final int SPELL_PAUSE_DURATION = 400;
  private static final int SUGGESTION_SPAN_CACHE_SIZE = 10;
  private static final String TAG = SpellChecker.class.getSimpleName();
  private static final int USE_SPAN_RANGE = -1;
  public static final int WORD_ITERATOR_INTERVAL = 350;
  final int mCookie;
  private Locale mCurrentLocale;
  private int[] mIds;
  private boolean mIsSentenceSpellCheckSupported;
  private int mLength;
  private int mSpanSequenceCounter = 0;
  private SpellCheckSpan[] mSpellCheckSpans;
  SpellCheckerSession mSpellCheckerSession;
  private SpellParser[] mSpellParsers = new SpellParser[0];
  private Runnable mSpellRunnable;
  private final LruCache<Long, SuggestionSpan> mSuggestionSpanCache = new LruCache(10);
  private TextServicesManager mTextServicesManager;
  private final TextView mTextView;
  private WordIterator mWordIterator;
  
  public SpellChecker(TextView paramTextView)
  {
    mTextView = paramTextView;
    mIds = ArrayUtils.newUnpaddedIntArray(1);
    mSpellCheckSpans = new SpellCheckSpan[mIds.length];
    setLocale(mTextView.getSpellCheckerLocale());
    mCookie = hashCode();
  }
  
  private void addSpellCheckSpan(Editable paramEditable, int paramInt1, int paramInt2)
  {
    int i = nextSpellCheckSpanIndex();
    SpellCheckSpan localSpellCheckSpan = mSpellCheckSpans[i];
    paramEditable.setSpan(localSpellCheckSpan, paramInt1, paramInt2, 33);
    localSpellCheckSpan.setSpellCheckInProgress(false);
    paramEditable = mIds;
    paramInt1 = mSpanSequenceCounter;
    mSpanSequenceCounter = (paramInt1 + 1);
    paramEditable[i] = paramInt1;
  }
  
  private void createMisspelledSuggestionSpan(Editable paramEditable, SuggestionsInfo paramSuggestionsInfo, SpellCheckSpan paramSpellCheckSpan, int paramInt1, int paramInt2)
  {
    int i = paramEditable.getSpanStart(paramSpellCheckSpan);
    int j = paramEditable.getSpanEnd(paramSpellCheckSpan);
    if ((i >= 0) && (j > i))
    {
      if ((paramInt1 != -1) && (paramInt2 != -1))
      {
        paramInt1 = i + paramInt1;
        j = paramInt1 + paramInt2;
        paramInt2 = paramInt1;
        paramInt1 = j;
      }
      else
      {
        paramInt2 = i;
        paramInt1 = j;
      }
      i = paramSuggestionsInfo.getSuggestionsCount();
      if (i > 0)
      {
        paramSpellCheckSpan = new String[i];
        for (j = 0; j < i; j++) {
          paramSpellCheckSpan[j] = paramSuggestionsInfo.getSuggestionAt(j);
        }
        paramSuggestionsInfo = paramSpellCheckSpan;
      }
      else
      {
        paramSuggestionsInfo = (String[])ArrayUtils.emptyArray(String.class);
      }
      SuggestionSpan localSuggestionSpan = new SuggestionSpan(mTextView.getContext(), paramSuggestionsInfo, 3);
      if (mIsSentenceSpellCheckSupported)
      {
        paramSuggestionsInfo = Long.valueOf(TextUtils.packRangeInLong(paramInt2, paramInt1));
        paramSpellCheckSpan = (SuggestionSpan)mSuggestionSpanCache.get(paramSuggestionsInfo);
        if (paramSpellCheckSpan != null) {
          paramEditable.removeSpan(paramSpellCheckSpan);
        }
        mSuggestionSpanCache.put(paramSuggestionsInfo, localSuggestionSpan);
      }
      paramEditable.setSpan(localSuggestionSpan, paramInt2, paramInt1, 33);
      mTextView.invalidateRegion(paramInt2, paramInt1, false);
      return;
    }
  }
  
  public static boolean haveWordBoundariesChanged(Editable paramEditable, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool;
    if ((paramInt4 != paramInt1) && (paramInt3 != paramInt2)) {
      bool = true;
    } else if ((paramInt4 == paramInt1) && (paramInt1 < paramEditable.length())) {
      bool = Character.isLetterOrDigit(Character.codePointAt(paramEditable, paramInt1));
    } else if ((paramInt3 == paramInt2) && (paramInt2 > 0)) {
      bool = Character.isLetterOrDigit(Character.codePointBefore(paramEditable, paramInt2));
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isSessionActive()
  {
    boolean bool;
    if (mSpellCheckerSession != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private int nextSpellCheckSpanIndex()
  {
    for (int i = 0; i < mLength; i++) {
      if (mIds[i] < 0) {
        return i;
      }
    }
    mIds = GrowingArrayUtils.append(mIds, mLength, 0);
    mSpellCheckSpans = ((SpellCheckSpan[])GrowingArrayUtils.append(mSpellCheckSpans, mLength, new SpellCheckSpan()));
    mLength += 1;
    return mLength - 1;
  }
  
  private SpellCheckSpan onGetSuggestionsInternal(SuggestionsInfo paramSuggestionsInfo, int paramInt1, int paramInt2)
  {
    if ((paramSuggestionsInfo != null) && (paramSuggestionsInfo.getCookie() == mCookie))
    {
      Editable localEditable = (Editable)mTextView.getText();
      int i = paramSuggestionsInfo.getSequence();
      int j = 0;
      for (int k = 0; k < mLength; k++) {
        if (i == mIds[k])
        {
          int m = paramSuggestionsInfo.getSuggestionsAttributes();
          if ((m & 0x1) > 0) {
            i = 1;
          } else {
            i = 0;
          }
          if ((m & 0x2) > 0) {
            j = 1;
          }
          SpellCheckSpan localSpellCheckSpan = mSpellCheckSpans[k];
          if ((i == 0) && (j != 0))
          {
            createMisspelledSuggestionSpan(localEditable, paramSuggestionsInfo, localSpellCheckSpan, paramInt1, paramInt2);
          }
          else
          {
            paramSuggestionsInfo = localSpellCheckSpan;
            if (mIsSentenceSpellCheckSupported)
            {
              k = localEditable.getSpanStart(paramSuggestionsInfo);
              i = localEditable.getSpanEnd(paramSuggestionsInfo);
              if ((paramInt1 != -1) && (paramInt2 != -1))
              {
                paramInt1 = k + paramInt1;
                j = paramInt1 + paramInt2;
                paramInt2 = paramInt1;
                paramInt1 = j;
              }
              else
              {
                paramInt2 = k;
                paramInt1 = i;
              }
              if ((k >= 0) && (i > k) && (paramInt1 > paramInt2))
              {
                Long localLong = Long.valueOf(TextUtils.packRangeInLong(paramInt2, paramInt1));
                paramSuggestionsInfo = (SuggestionSpan)mSuggestionSpanCache.get(localLong);
                if (paramSuggestionsInfo != null)
                {
                  localEditable.removeSpan(paramSuggestionsInfo);
                  mSuggestionSpanCache.remove(localLong);
                }
              }
            }
          }
          return localSpellCheckSpan;
        }
      }
      return null;
    }
    return null;
  }
  
  private void resetSession()
  {
    closeSession();
    mTextServicesManager = ((TextServicesManager)mTextView.getContext().getSystemService("textservices"));
    if ((mTextServicesManager.isSpellCheckerEnabled()) && (mCurrentLocale != null) && (mTextServicesManager.getCurrentSpellCheckerSubtype(true) != null))
    {
      mSpellCheckerSession = mTextServicesManager.newSpellCheckerSession(null, mCurrentLocale, this, false);
      mIsSentenceSpellCheckSupported = true;
    }
    else
    {
      mSpellCheckerSession = null;
    }
    for (int i = 0; i < mLength; i++) {
      mIds[i] = -1;
    }
    mLength = 0;
    mTextView.removeMisspelledSpans((Editable)mTextView.getText());
    mSuggestionSpanCache.evictAll();
  }
  
  private void scheduleNewSpellCheck()
  {
    if (mSpellRunnable == null) {
      mSpellRunnable = new Runnable()
      {
        public void run()
        {
          int i = mSpellParsers.length;
          for (int j = 0; j < i; j++)
          {
            SpellChecker.SpellParser localSpellParser = mSpellParsers[j];
            if (!localSpellParser.isFinished())
            {
              localSpellParser.parse();
              break;
            }
          }
        }
      };
    } else {
      mTextView.removeCallbacks(mSpellRunnable);
    }
    mTextView.postDelayed(mSpellRunnable, 400L);
  }
  
  private void setLocale(Locale paramLocale)
  {
    mCurrentLocale = paramLocale;
    resetSession();
    if (paramLocale != null) {
      mWordIterator = new WordIterator(paramLocale);
    }
    mTextView.onLocaleChanged();
  }
  
  private void spellCheck()
  {
    if (mSpellCheckerSession == null) {
      return;
    }
    Object localObject = (Editable)mTextView.getText();
    int i = Selection.getSelectionStart((CharSequence)localObject);
    int j = Selection.getSelectionEnd((CharSequence)localObject);
    TextInfo[] arrayOfTextInfo = new TextInfo[mLength];
    int k = 0;
    int m = 0;
    while (m < mLength)
    {
      SpellCheckSpan localSpellCheckSpan = mSpellCheckSpans[m];
      int n = k;
      if (mIds[m] >= 0) {
        if (localSpellCheckSpan.isSpellCheckInProgress())
        {
          n = k;
        }
        else
        {
          int i1 = ((Editable)localObject).getSpanStart(localSpellCheckSpan);
          int i2 = ((Editable)localObject).getSpanEnd(localSpellCheckSpan);
          int i3;
          if ((i == i2 + 1) && (WordIterator.isMidWordPunctuation(mCurrentLocale, Character.codePointBefore((CharSequence)localObject, i2 + 1)))) {
            i3 = 0;
          }
          for (;;)
          {
            break;
            if (mIsSentenceSpellCheckSupported)
            {
              if ((j > i1) && (i <= i2)) {
                i3 = 0;
              } else {
                i3 = 1;
              }
            }
            else if ((j >= i1) && (i <= i2)) {
              i3 = 0;
            } else {
              i3 = 1;
            }
          }
          n = k;
          if (i1 >= 0)
          {
            n = k;
            if (i2 > i1)
            {
              n = k;
              if (i3 != 0)
              {
                localSpellCheckSpan.setSpellCheckInProgress(true);
                arrayOfTextInfo[k] = new TextInfo((CharSequence)localObject, i1, i2, mCookie, mIds[m]);
                n = k + 1;
              }
            }
          }
        }
      }
      m++;
      k = n;
    }
    if (k > 0)
    {
      localObject = arrayOfTextInfo;
      if (k < arrayOfTextInfo.length)
      {
        localObject = new TextInfo[k];
        System.arraycopy(arrayOfTextInfo, 0, localObject, 0, k);
      }
      if (mIsSentenceSpellCheckSupported) {
        mSpellCheckerSession.getSentenceSuggestions((TextInfo[])localObject, 5);
      } else {
        mSpellCheckerSession.getSuggestions((TextInfo[])localObject, 5, false);
      }
    }
  }
  
  public void closeSession()
  {
    if (mSpellCheckerSession != null) {
      mSpellCheckerSession.close();
    }
    int i = mSpellParsers.length;
    for (int j = 0; j < i; j++) {
      mSpellParsers[j].stop();
    }
    if (mSpellRunnable != null) {
      mTextView.removeCallbacks(mSpellRunnable);
    }
  }
  
  public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] paramArrayOfSentenceSuggestionsInfo)
  {
    Editable localEditable = (Editable)mTextView.getText();
    for (int i = 0; i < paramArrayOfSentenceSuggestionsInfo.length; i++)
    {
      SentenceSuggestionsInfo localSentenceSuggestionsInfo = paramArrayOfSentenceSuggestionsInfo[i];
      if (localSentenceSuggestionsInfo != null)
      {
        Object localObject1 = null;
        int j = 0;
        while (j < localSentenceSuggestionsInfo.getSuggestionsCount())
        {
          Object localObject2 = localSentenceSuggestionsInfo.getSuggestionsInfoAt(j);
          if (localObject2 == null)
          {
            localObject2 = localObject1;
          }
          else
          {
            SpellCheckSpan localSpellCheckSpan = onGetSuggestionsInternal((SuggestionsInfo)localObject2, localSentenceSuggestionsInfo.getOffsetAt(j), localSentenceSuggestionsInfo.getLengthAt(j));
            localObject2 = localObject1;
            if (localObject1 == null)
            {
              localObject2 = localObject1;
              if (localSpellCheckSpan != null) {
                localObject2 = localSpellCheckSpan;
              }
            }
          }
          j++;
          localObject1 = localObject2;
        }
        if (localObject1 != null) {
          localEditable.removeSpan(localObject1);
        }
      }
    }
    scheduleNewSpellCheck();
  }
  
  public void onGetSuggestions(SuggestionsInfo[] paramArrayOfSuggestionsInfo)
  {
    Editable localEditable = (Editable)mTextView.getText();
    for (int i = 0; i < paramArrayOfSuggestionsInfo.length; i++)
    {
      SpellCheckSpan localSpellCheckSpan = onGetSuggestionsInternal(paramArrayOfSuggestionsInfo[i], -1, -1);
      if (localSpellCheckSpan != null) {
        localEditable.removeSpan(localSpellCheckSpan);
      }
    }
    scheduleNewSpellCheck();
  }
  
  public void onSelectionChanged()
  {
    spellCheck();
  }
  
  public void onSpellCheckSpanRemoved(SpellCheckSpan paramSpellCheckSpan)
  {
    for (int i = 0; i < mLength; i++) {
      if (mSpellCheckSpans[i] == paramSpellCheckSpan)
      {
        mIds[i] = -1;
        return;
      }
    }
  }
  
  public void spellCheck(int paramInt1, int paramInt2)
  {
    Object localObject = mTextView.getSpellCheckerLocale();
    boolean bool = isSessionActive();
    int i;
    int j;
    if ((localObject != null) && (mCurrentLocale != null) && (mCurrentLocale.equals(localObject)))
    {
      i = paramInt1;
      j = paramInt2;
      if (bool != mTextServicesManager.isSpellCheckerEnabled())
      {
        resetSession();
        i = paramInt1;
        j = paramInt2;
      }
    }
    else
    {
      setLocale((Locale)localObject);
      i = 0;
      j = mTextView.getText().length();
    }
    if (!bool) {
      return;
    }
    paramInt2 = mSpellParsers.length;
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
    {
      localObject = mSpellParsers[paramInt1];
      if (((SpellParser)localObject).isFinished())
      {
        ((SpellParser)localObject).parse(i, j);
        return;
      }
    }
    localObject = new SpellParser[paramInt2 + 1];
    System.arraycopy(mSpellParsers, 0, localObject, 0, paramInt2);
    mSpellParsers = ((SpellParser[])localObject);
    localObject = new SpellParser(null);
    mSpellParsers[paramInt2] = localObject;
    ((SpellParser)localObject).parse(i, j);
  }
  
  private class SpellParser
  {
    private Object mRange = new Object();
    
    private SpellParser() {}
    
    private void removeRangeSpan(Editable paramEditable)
    {
      paramEditable.removeSpan(mRange);
    }
    
    private <T> void removeSpansAt(Editable paramEditable, int paramInt, T[] paramArrayOfT)
    {
      int i = paramArrayOfT.length;
      for (int j = 0; j < i; j++)
      {
        T ? = paramArrayOfT[j];
        if ((paramEditable.getSpanStart(?) <= paramInt) && (paramEditable.getSpanEnd(?) >= paramInt)) {
          paramEditable.removeSpan(?);
        }
      }
    }
    
    private void setRangeSpan(Editable paramEditable, int paramInt1, int paramInt2)
    {
      paramEditable.setSpan(mRange, paramInt1, paramInt2, 33);
    }
    
    public boolean isFinished()
    {
      boolean bool;
      if (((Editable)mTextView.getText()).getSpanStart(mRange) < 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void parse()
    {
      Editable localEditable = (Editable)mTextView.getText();
      int i;
      if (mIsSentenceSpellCheckSupported) {
        i = Math.max(0, localEditable.getSpanStart(mRange) - 50);
      } else {
        i = localEditable.getSpanStart(mRange);
      }
      int j = localEditable.getSpanEnd(mRange);
      int k = Math.min(j, i + 350);
      mWordIterator.setCharSequence(localEditable, i, k);
      int m = mWordIterator.preceding(i);
      int i1;
      if (m == -1)
      {
        n = mWordIterator.following(i);
        i1 = n;
        if (n != -1)
        {
          m = mWordIterator.getBeginning(n);
          i1 = n;
        }
      }
      else
      {
        i1 = mWordIterator.getEnd(m);
      }
      if (i1 == -1)
      {
        removeRangeSpan(localEditable);
        return;
      }
      Object localObject1 = (SpellCheckSpan[])localEditable.getSpans(i - 1, j + 1, SpellCheckSpan.class);
      Object localObject2 = (SuggestionSpan[])localEditable.getSpans(i - 1, j + 1, SuggestionSpan.class);
      int i2 = 0;
      int i3 = 0;
      int i4 = 0;
      int n = 0;
      int i5;
      int i6;
      if (mIsSentenceSpellCheckSupported)
      {
        if (k < j) {
          n = 1;
        }
        i3 = mWordIterator.preceding(k);
        i4 = 1;
        if (i3 != -1) {
          i2 = 1;
        } else {
          i2 = 0;
        }
        i5 = i3;
        i6 = i2;
        if (i2 != 0)
        {
          i2 = mWordIterator.getEnd(i3);
          if (i2 != -1) {
            i5 = i4;
          } else {
            i5 = 0;
          }
          i6 = i5;
          i5 = i2;
        }
        if (i6 == 0)
        {
          removeRangeSpan(localEditable);
          return;
        }
        i4 = m;
        int i7 = 1;
        i6 = 0;
        i2 = m;
        m = i5;
        while (i6 < mLength)
        {
          localObject1 = mSpellCheckSpans[i6];
          if (mIds[i6] >= 0)
          {
            if (((SpellCheckSpan)localObject1).isSpellCheckInProgress())
            {
              i5 = m;
              i3 = i4;
            }
            else
            {
              int i8 = localEditable.getSpanStart(localObject1);
              int i9 = localEditable.getSpanEnd(localObject1);
              i5 = m;
              i3 = i4;
              if (i9 >= i4) {
                if (m < i8)
                {
                  i5 = m;
                  i3 = i4;
                }
                else
                {
                  if ((i8 <= i4) && (m <= i9))
                  {
                    i1 = 0;
                    break label546;
                  }
                  localEditable.removeSpan(localObject1);
                  i3 = Math.min(i8, i4);
                  i5 = Math.max(i9, m);
                }
              }
            }
          }
          else
          {
            i3 = i4;
            i5 = m;
          }
          i6++;
          m = i5;
          i4 = i3;
        }
        i1 = i7;
        label546:
        if (m >= i) {
          if (m <= i4)
          {
            localObject1 = SpellChecker.TAG;
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Trying to spellcheck invalid region, from ");
            ((StringBuilder)localObject2).append(i);
            ((StringBuilder)localObject2).append(" to ");
            ((StringBuilder)localObject2).append(j);
            Log.w((String)localObject1, ((StringBuilder)localObject2).toString());
          }
          else if (i1 != 0)
          {
            SpellChecker.this.addSpellCheckSpan(localEditable, i4, m);
          }
        }
        i5 = m;
        i1 = n;
      }
      else
      {
        n = i1;
        for (;;)
        {
          i5 = m;
          i1 = i3;
          if (m > j) {
            break;
          }
          i5 = i2;
          if (n >= i)
          {
            i5 = i2;
            if (n > m)
            {
              if (i2 >= 50)
              {
                i1 = 1;
                i5 = m;
                break;
              }
              if ((m < i) && (n > i))
              {
                removeSpansAt(localEditable, i, (Object[])localObject1);
                removeSpansAt(localEditable, i, (Object[])localObject2);
              }
              if ((m < j) && (n > j))
              {
                removeSpansAt(localEditable, j, (Object[])localObject1);
                removeSpansAt(localEditable, j, (Object[])localObject2);
              }
              i6 = 1;
              i1 = i6;
              if (n == i) {
                for (i5 = 0;; i5++)
                {
                  i1 = i6;
                  if (i5 >= localObject1.length) {
                    break;
                  }
                  if (localEditable.getSpanEnd(localObject1[i5]) == i)
                  {
                    i1 = 0;
                    break;
                  }
                }
              }
              i5 = i1;
              if (m == j) {
                for (i6 = 0;; i6++)
                {
                  i5 = i1;
                  if (i6 >= localObject1.length) {
                    break;
                  }
                  if (localEditable.getSpanStart(localObject1[i6]) == j)
                  {
                    i5 = 0;
                    break;
                  }
                }
              }
              if (i5 != 0) {
                SpellChecker.this.addSpellCheckSpan(localEditable, m, n);
              }
              i5 = i2 + 1;
            }
          }
          i6 = mWordIterator.following(n);
          i2 = k;
          i1 = i6;
          if (k < j) {
            if (i6 != -1)
            {
              i2 = k;
              i1 = i6;
              if (i6 < k) {}
            }
            else
            {
              i2 = Math.min(j, n + 350);
              mWordIterator.setCharSequence(localEditable, n, i2);
              i1 = mWordIterator.following(n);
            }
          }
          if (i1 == -1)
          {
            i1 = i4;
            break label1039;
          }
          m = mWordIterator.getBeginning(i1);
          if (m == -1)
          {
            i1 = i4;
            break label1039;
          }
          k = i2;
          n = i1;
          i2 = i5;
        }
      }
      m = i5;
      label1039:
      if ((i1 != 0) && (m != -1) && (m <= j)) {
        setRangeSpan(localEditable, m, j);
      } else {
        removeRangeSpan(localEditable);
      }
      SpellChecker.this.spellCheck();
    }
    
    public void parse(int paramInt1, int paramInt2)
    {
      int i = mTextView.length();
      if (paramInt2 > i)
      {
        String str = SpellChecker.TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Parse invalid region, from ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(" to ");
        localStringBuilder.append(paramInt2);
        Log.w(str, localStringBuilder.toString());
        paramInt2 = i;
      }
      if (paramInt2 > paramInt1)
      {
        setRangeSpan((Editable)mTextView.getText(), paramInt1, paramInt2);
        parse();
      }
    }
    
    public void stop()
    {
      removeRangeSpan((Editable)mTextView.getText());
    }
  }
}
