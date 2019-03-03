package android.service.textservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.text.method.WordIterator;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import com.android.internal.textservice.ISpellCheckerService.Stub;
import com.android.internal.textservice.ISpellCheckerServiceCallback;
import com.android.internal.textservice.ISpellCheckerSession.Stub;
import com.android.internal.textservice.ISpellCheckerSessionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

public abstract class SpellCheckerService
  extends Service
{
  private static final boolean DBG = false;
  public static final String SERVICE_INTERFACE = "android.service.textservice.SpellCheckerService";
  private static final String TAG = SpellCheckerService.class.getSimpleName();
  private final SpellCheckerServiceBinder mBinder = new SpellCheckerServiceBinder(this);
  
  public SpellCheckerService() {}
  
  public abstract Session createSession();
  
  public final IBinder onBind(Intent paramIntent)
  {
    return mBinder;
  }
  
  private static class InternalISpellCheckerSession
    extends ISpellCheckerSession.Stub
  {
    private final Bundle mBundle;
    private ISpellCheckerSessionListener mListener;
    private final String mLocale;
    private final SpellCheckerService.Session mSession;
    
    public InternalISpellCheckerSession(String paramString, ISpellCheckerSessionListener paramISpellCheckerSessionListener, Bundle paramBundle, SpellCheckerService.Session paramSession)
    {
      mListener = paramISpellCheckerSessionListener;
      mSession = paramSession;
      mLocale = paramString;
      mBundle = paramBundle;
      paramSession.setInternalISpellCheckerSession(this);
    }
    
    public Bundle getBundle()
    {
      return mBundle;
    }
    
    public String getLocale()
    {
      return mLocale;
    }
    
    public void onCancel()
    {
      int i = Process.getThreadPriority(Process.myTid());
      try
      {
        Process.setThreadPriority(10);
        mSession.onCancel();
        return;
      }
      finally
      {
        Process.setThreadPriority(i);
      }
    }
    
    public void onClose()
    {
      int i = Process.getThreadPriority(Process.myTid());
      try
      {
        Process.setThreadPriority(10);
        mSession.onClose();
        return;
      }
      finally
      {
        Process.setThreadPriority(i);
        mListener = null;
      }
    }
    
    public void onGetSentenceSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt)
    {
      try
      {
        mListener.onGetSentenceSuggestions(mSession.onGetSentenceSuggestionsMultiple(paramArrayOfTextInfo, paramInt));
      }
      catch (RemoteException paramArrayOfTextInfo) {}
    }
    
    /* Error */
    public void onGetSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt, boolean paramBoolean)
    {
      // Byte code:
      //   0: invokestatic 46	android/os/Process:myTid	()I
      //   3: invokestatic 50	android/os/Process:getThreadPriority	(I)I
      //   6: istore 4
      //   8: bipush 10
      //   10: invokestatic 54	android/os/Process:setThreadPriority	(I)V
      //   13: aload_0
      //   14: getfield 22	android/service/textservice/SpellCheckerService$InternalISpellCheckerSession:mListener	Lcom/android/internal/textservice/ISpellCheckerSessionListener;
      //   17: aload_0
      //   18: getfield 24	android/service/textservice/SpellCheckerService$InternalISpellCheckerSession:mSession	Landroid/service/textservice/SpellCheckerService$Session;
      //   21: aload_1
      //   22: iload_2
      //   23: iload_3
      //   24: invokevirtual 77	android/service/textservice/SpellCheckerService$Session:onGetSuggestionsMultiple	([Landroid/view/textservice/TextInfo;IZ)[Landroid/view/textservice/SuggestionsInfo;
      //   27: invokeinterface 81 2 0
      //   32: goto +12 -> 44
      //   35: astore_1
      //   36: iload 4
      //   38: invokestatic 54	android/os/Process:setThreadPriority	(I)V
      //   41: aload_1
      //   42: athrow
      //   43: astore_1
      //   44: iload 4
      //   46: invokestatic 54	android/os/Process:setThreadPriority	(I)V
      //   49: return
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	50	0	this	InternalISpellCheckerSession
      //   0	50	1	paramArrayOfTextInfo	TextInfo[]
      //   0	50	2	paramInt	int
      //   0	50	3	paramBoolean	boolean
      //   6	39	4	i	int
      // Exception table:
      //   from	to	target	type
      //   8	32	35	finally
      //   8	32	43	android/os/RemoteException
    }
  }
  
  private static class SentenceLevelAdapter
  {
    public static final SentenceSuggestionsInfo[] EMPTY_SENTENCE_SUGGESTIONS_INFOS = new SentenceSuggestionsInfo[0];
    private static final SuggestionsInfo EMPTY_SUGGESTIONS_INFO = new SuggestionsInfo(0, null);
    private final WordIterator mWordIterator;
    
    public SentenceLevelAdapter(Locale paramLocale)
    {
      mWordIterator = new WordIterator(paramLocale);
    }
    
    private SentenceTextInfoParams getSplitWords(TextInfo paramTextInfo)
    {
      WordIterator localWordIterator = mWordIterator;
      String str = paramTextInfo.getText();
      int i = paramTextInfo.getCookie();
      int j = str.length();
      ArrayList localArrayList = new ArrayList();
      localWordIterator.setCharSequence(str, 0, str.length());
      int k = localWordIterator.following(0);
      for (int m = localWordIterator.getBeginning(k); (m <= j) && (k != -1) && (m != -1); m = localWordIterator.getBeginning(k))
      {
        if ((k >= 0) && (k > m))
        {
          CharSequence localCharSequence = str.subSequence(m, k);
          localArrayList.add(new SentenceWordItem(new TextInfo(localCharSequence, 0, localCharSequence.length(), i, localCharSequence.hashCode()), m, k));
        }
        k = localWordIterator.following(k);
        if (k == -1) {
          break;
        }
      }
      return new SentenceTextInfoParams(paramTextInfo, localArrayList);
    }
    
    public static SentenceSuggestionsInfo reconstructSuggestions(SentenceTextInfoParams paramSentenceTextInfoParams, SuggestionsInfo[] paramArrayOfSuggestionsInfo)
    {
      if ((paramArrayOfSuggestionsInfo != null) && (paramArrayOfSuggestionsInfo.length != 0))
      {
        if (paramSentenceTextInfoParams == null) {
          return null;
        }
        int i = mOriginalTextInfo.getCookie();
        int j = mOriginalTextInfo.getSequence();
        int k = mSize;
        int[] arrayOfInt1 = new int[k];
        int[] arrayOfInt2 = new int[k];
        SuggestionsInfo[] arrayOfSuggestionsInfo = new SuggestionsInfo[k];
        for (int m = 0; m < k; m++)
        {
          SentenceWordItem localSentenceWordItem = (SentenceWordItem)mItems.get(m);
          Object localObject1 = null;
          Object localObject2;
          for (int n = 0;; n++)
          {
            localObject2 = localObject1;
            if (n >= paramArrayOfSuggestionsInfo.length) {
              break;
            }
            localObject2 = paramArrayOfSuggestionsInfo[n];
            if ((localObject2 != null) && (((SuggestionsInfo)localObject2).getSequence() == mTextInfo.getSequence()))
            {
              ((SuggestionsInfo)localObject2).setCookieAndSequence(i, j);
              break;
            }
          }
          arrayOfInt1[m] = mStart;
          arrayOfInt2[m] = mLength;
          if (localObject2 == null) {
            localObject2 = EMPTY_SUGGESTIONS_INFO;
          }
          arrayOfSuggestionsInfo[m] = localObject2;
        }
        return new SentenceSuggestionsInfo(arrayOfSuggestionsInfo, arrayOfInt1, arrayOfInt2);
      }
      return null;
    }
    
    public static class SentenceTextInfoParams
    {
      final ArrayList<SpellCheckerService.SentenceLevelAdapter.SentenceWordItem> mItems;
      final TextInfo mOriginalTextInfo;
      final int mSize;
      
      public SentenceTextInfoParams(TextInfo paramTextInfo, ArrayList<SpellCheckerService.SentenceLevelAdapter.SentenceWordItem> paramArrayList)
      {
        mOriginalTextInfo = paramTextInfo;
        mItems = paramArrayList;
        mSize = paramArrayList.size();
      }
    }
    
    public static class SentenceWordItem
    {
      public final int mLength;
      public final int mStart;
      public final TextInfo mTextInfo;
      
      public SentenceWordItem(TextInfo paramTextInfo, int paramInt1, int paramInt2)
      {
        mTextInfo = paramTextInfo;
        mStart = paramInt1;
        mLength = (paramInt2 - paramInt1);
      }
    }
  }
  
  public static abstract class Session
  {
    private SpellCheckerService.InternalISpellCheckerSession mInternalSession;
    private volatile SpellCheckerService.SentenceLevelAdapter mSentenceLevelAdapter;
    
    public Session() {}
    
    public Bundle getBundle()
    {
      return mInternalSession.getBundle();
    }
    
    public String getLocale()
    {
      return mInternalSession.getLocale();
    }
    
    public void onCancel() {}
    
    public void onClose() {}
    
    public abstract void onCreate();
    
    public SentenceSuggestionsInfo[] onGetSentenceSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt)
    {
      if ((paramArrayOfTextInfo != null) && (paramArrayOfTextInfo.length != 0))
      {
        Object localObject2;
        Object localObject3;
        if (mSentenceLevelAdapter == null) {
          try
          {
            if (mSentenceLevelAdapter == null)
            {
              localObject1 = getLocale();
              if (!TextUtils.isEmpty((CharSequence)localObject1))
              {
                localObject2 = new android/service/textservice/SpellCheckerService$SentenceLevelAdapter;
                localObject3 = new java/util/Locale;
                ((Locale)localObject3).<init>((String)localObject1);
                ((SpellCheckerService.SentenceLevelAdapter)localObject2).<init>((Locale)localObject3);
                mSentenceLevelAdapter = ((SpellCheckerService.SentenceLevelAdapter)localObject2);
              }
            }
          }
          finally {}
        }
        if (mSentenceLevelAdapter == null) {
          return SpellCheckerService.SentenceLevelAdapter.EMPTY_SENTENCE_SUGGESTIONS_INFOS;
        }
        int i = paramArrayOfTextInfo.length;
        Object localObject1 = new SentenceSuggestionsInfo[i];
        for (int j = 0; j < i; j++)
        {
          localObject2 = mSentenceLevelAdapter.getSplitWords(paramArrayOfTextInfo[j]);
          localObject3 = mItems;
          int k = ((ArrayList)localObject3).size();
          TextInfo[] arrayOfTextInfo = new TextInfo[k];
          for (int m = 0; m < k; m++) {
            arrayOfTextInfo[m] = getmTextInfo;
          }
          localObject1[j] = SpellCheckerService.SentenceLevelAdapter.reconstructSuggestions((SpellCheckerService.SentenceLevelAdapter.SentenceTextInfoParams)localObject2, onGetSuggestionsMultiple(arrayOfTextInfo, paramInt, true));
        }
        return localObject1;
      }
      return SpellCheckerService.SentenceLevelAdapter.EMPTY_SENTENCE_SUGGESTIONS_INFOS;
    }
    
    public abstract SuggestionsInfo onGetSuggestions(TextInfo paramTextInfo, int paramInt);
    
    public SuggestionsInfo[] onGetSuggestionsMultiple(TextInfo[] paramArrayOfTextInfo, int paramInt, boolean paramBoolean)
    {
      int i = paramArrayOfTextInfo.length;
      SuggestionsInfo[] arrayOfSuggestionsInfo = new SuggestionsInfo[i];
      for (int j = 0; j < i; j++)
      {
        arrayOfSuggestionsInfo[j] = onGetSuggestions(paramArrayOfTextInfo[j], paramInt);
        arrayOfSuggestionsInfo[j].setCookieAndSequence(paramArrayOfTextInfo[j].getCookie(), paramArrayOfTextInfo[j].getSequence());
      }
      return arrayOfSuggestionsInfo;
    }
    
    public final void setInternalISpellCheckerSession(SpellCheckerService.InternalISpellCheckerSession paramInternalISpellCheckerSession)
    {
      mInternalSession = paramInternalISpellCheckerSession;
    }
  }
  
  private static class SpellCheckerServiceBinder
    extends ISpellCheckerService.Stub
  {
    private final WeakReference<SpellCheckerService> mInternalServiceRef;
    
    public SpellCheckerServiceBinder(SpellCheckerService paramSpellCheckerService)
    {
      mInternalServiceRef = new WeakReference(paramSpellCheckerService);
    }
    
    public void getISpellCheckerSession(String paramString, ISpellCheckerSessionListener paramISpellCheckerSessionListener, Bundle paramBundle, ISpellCheckerServiceCallback paramISpellCheckerServiceCallback)
    {
      Object localObject = (SpellCheckerService)mInternalServiceRef.get();
      if (localObject == null)
      {
        paramString = null;
      }
      else
      {
        localObject = ((SpellCheckerService)localObject).createSession();
        paramString = new SpellCheckerService.InternalISpellCheckerSession(paramString, paramISpellCheckerSessionListener, paramBundle, (SpellCheckerService.Session)localObject);
        ((SpellCheckerService.Session)localObject).onCreate();
      }
      try
      {
        paramISpellCheckerServiceCallback.onSessionCreated(paramString);
      }
      catch (RemoteException paramString) {}
    }
  }
}
