package android.media;

import android.util.Log;

class Tokenizer
{
  private static final String TAG = "Tokenizer";
  private TokenizerPhase mDataTokenizer = new DataTokenizer();
  private int mHandledLen;
  private String mLine;
  private OnTokenListener mListener;
  private TokenizerPhase mPhase;
  private TokenizerPhase mTagTokenizer = new TagTokenizer();
  
  Tokenizer(OnTokenListener paramOnTokenListener)
  {
    reset();
    mListener = paramOnTokenListener;
  }
  
  void reset()
  {
    mPhase = mDataTokenizer.start();
  }
  
  void tokenize(String paramString)
  {
    mHandledLen = 0;
    mLine = paramString;
    while (mHandledLen < mLine.length()) {
      mPhase.tokenize();
    }
    if (!(mPhase instanceof TagTokenizer)) {
      mListener.onLineEnd();
    }
  }
  
  class DataTokenizer
    implements Tokenizer.TokenizerPhase
  {
    private StringBuilder mData;
    
    DataTokenizer() {}
    
    private boolean replaceEscape(String paramString1, String paramString2, int paramInt)
    {
      if (mLine.startsWith(paramString1, paramInt))
      {
        mData.append(mLine.substring(mHandledLen, paramInt));
        mData.append(paramString2);
        Tokenizer.access$102(Tokenizer.this, paramString1.length() + paramInt);
        return true;
      }
      return false;
    }
    
    public Tokenizer.TokenizerPhase start()
    {
      mData = new StringBuilder();
      return this;
    }
    
    public void tokenize()
    {
      int i = mLine.length();
      int k;
      for (int j = mHandledLen;; j++)
      {
        k = i;
        if (j >= mLine.length()) {
          break;
        }
        if (mLine.charAt(j) == '&' ? (!replaceEscape("&amp;", "&", j)) && (!replaceEscape("&lt;", "<", j)) && (!replaceEscape("&gt;", ">", j)) && (!replaceEscape("&lrm;", "‎", j)) && (!replaceEscape("&rlm;", "‏", j)) && (replaceEscape("&nbsp;", " ", j)) : mLine.charAt(j) == '<')
        {
          Tokenizer.access$202(Tokenizer.this, mTagTokenizer.start());
          k = j;
          break;
        }
      }
      mData.append(mLine.substring(mHandledLen, k));
      mListener.onData(mData.toString());
      mData.delete(0, mData.length());
      Tokenizer.access$102(Tokenizer.this, k);
    }
  }
  
  static abstract interface OnTokenListener
  {
    public abstract void onData(String paramString);
    
    public abstract void onEnd(String paramString);
    
    public abstract void onLineEnd();
    
    public abstract void onStart(String paramString1, String[] paramArrayOfString, String paramString2);
    
    public abstract void onTimeStamp(long paramLong);
  }
  
  class TagTokenizer
    implements Tokenizer.TokenizerPhase
  {
    private String mAnnotation;
    private boolean mAtAnnotation;
    private String mName;
    
    TagTokenizer() {}
    
    private void yield_tag()
    {
      if (mName.startsWith("/"))
      {
        mListener.onEnd(mName.substring(1));
      }
      else
      {
        Object localObject;
        if ((mName.length() > 0) && (Character.isDigit(mName.charAt(0))))
        {
          try
          {
            long l = WebVttParser.parseTimestampMs(mName);
            mListener.onTimeStamp(l);
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("invalid timestamp tag: <");
            ((StringBuilder)localObject).append(mName);
            ((StringBuilder)localObject).append(">");
            Log.d("Tokenizer", ((StringBuilder)localObject).toString());
          }
        }
        else
        {
          mAnnotation = mAnnotation.replaceAll("\\s+", " ");
          if (mAnnotation.startsWith(" ")) {
            mAnnotation = mAnnotation.substring(1);
          }
          if (mAnnotation.endsWith(" ")) {
            mAnnotation = mAnnotation.substring(0, mAnnotation.length() - 1);
          }
          localObject = null;
          int i = mName.indexOf('.');
          if (i >= 0)
          {
            localObject = mName.substring(i + 1).split("\\.");
            mName = mName.substring(0, i);
          }
          mListener.onStart(mName, (String[])localObject, mAnnotation);
        }
      }
    }
    
    public Tokenizer.TokenizerPhase start()
    {
      mAnnotation = "";
      mName = "";
      mAtAnnotation = false;
      return this;
    }
    
    public void tokenize()
    {
      if (!mAtAnnotation) {
        Tokenizer.access$108(Tokenizer.this);
      }
      if (mHandledLen < mLine.length())
      {
        Object localObject;
        if ((!mAtAnnotation) && (mLine.charAt(mHandledLen) != '/')) {
          localObject = mLine.substring(mHandledLen).split("[\t\f >]");
        } else {
          localObject = mLine.substring(mHandledLen).split(">");
        }
        String str = mLine.substring(mHandledLen, mHandledLen + localObject[0].length());
        Tokenizer.access$112(Tokenizer.this, localObject[0].length());
        if (mAtAnnotation)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(mAnnotation);
          ((StringBuilder)localObject).append(" ");
          ((StringBuilder)localObject).append(str);
          mAnnotation = ((StringBuilder)localObject).toString();
        }
        else
        {
          mName = str;
        }
      }
      mAtAnnotation = true;
      if ((mHandledLen < mLine.length()) && (mLine.charAt(mHandledLen) == '>'))
      {
        yield_tag();
        Tokenizer.access$202(Tokenizer.this, mDataTokenizer.start());
        Tokenizer.access$108(Tokenizer.this);
      }
    }
  }
  
  static abstract interface TokenizerPhase
  {
    public abstract TokenizerPhase start();
    
    public abstract void tokenize();
  }
}
