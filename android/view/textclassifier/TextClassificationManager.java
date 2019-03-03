package android.view.textclassifier;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.ServiceManager.ServiceNotFoundException;
import android.provider.Settings.Global;
import android.service.textclassifier.TextClassifierService;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.lang.ref.WeakReference;

public final class TextClassificationManager
{
  private static final String LOG_TAG = "TextClassificationManager";
  private final Context mContext;
  @GuardedBy("mLock")
  private TextClassifier mCustomTextClassifier;
  private final TextClassificationSessionFactory mDefaultSessionFactory = new _..Lambda.TextClassificationManager.JIaezIJbMig__kVzN6oArzkTsJE(this);
  @GuardedBy("mLock")
  private TextClassifier mLocalTextClassifier;
  private final Object mLock = new Object();
  @GuardedBy("mLock")
  private TextClassificationSessionFactory mSessionFactory;
  @GuardedBy("mLock")
  private TextClassificationConstants mSettings;
  private final SettingsObserver mSettingsObserver;
  @GuardedBy("mLock")
  private TextClassifier mSystemTextClassifier;
  
  public TextClassificationManager(Context paramContext)
  {
    mContext = ((Context)Preconditions.checkNotNull(paramContext));
    mSessionFactory = mDefaultSessionFactory;
    mSettingsObserver = new SettingsObserver(this);
  }
  
  private TextClassifier getLocalTextClassifier()
  {
    synchronized (mLock)
    {
      if (mLocalTextClassifier == null) {
        if (getSettings().isLocalTextClassifierEnabled())
        {
          localObject2 = new android/view/textclassifier/TextClassifierImpl;
          ((TextClassifierImpl)localObject2).<init>(mContext, getSettings(), TextClassifier.NO_OP);
          mLocalTextClassifier = ((TextClassifier)localObject2);
        }
        else
        {
          Log.d("TextClassificationManager", "Local TextClassifier disabled");
          mLocalTextClassifier = TextClassifier.NO_OP;
        }
      }
      Object localObject2 = mLocalTextClassifier;
      return localObject2;
    }
  }
  
  private TextClassificationConstants getSettings()
  {
    synchronized (mLock)
    {
      if (mSettings == null) {
        mSettings = TextClassificationConstants.loadFromString(Settings.Global.getString(getApplicationContext().getContentResolver(), "text_classifier_constants"));
      }
      TextClassificationConstants localTextClassificationConstants = mSettings;
      return localTextClassificationConstants;
    }
  }
  
  public static TextClassificationConstants getSettings(Context paramContext)
  {
    Preconditions.checkNotNull(paramContext);
    TextClassificationManager localTextClassificationManager = (TextClassificationManager)paramContext.getSystemService(TextClassificationManager.class);
    if (localTextClassificationManager != null) {
      return localTextClassificationManager.getSettings();
    }
    return TextClassificationConstants.loadFromString(Settings.Global.getString(paramContext.getApplicationContext().getContentResolver(), "text_classifier_constants"));
  }
  
  private TextClassifier getSystemTextClassifier()
  {
    synchronized (mLock)
    {
      if (mSystemTextClassifier == null)
      {
        boolean bool = isSystemTextClassifierEnabled();
        if (bool) {
          try
          {
            SystemTextClassifier localSystemTextClassifier = new android/view/textclassifier/SystemTextClassifier;
            localSystemTextClassifier.<init>(mContext, getSettings());
            mSystemTextClassifier = localSystemTextClassifier;
            Log.d("TextClassificationManager", "Initialized SystemTextClassifier");
          }
          catch (ServiceManager.ServiceNotFoundException localServiceNotFoundException)
          {
            Log.e("TextClassificationManager", "Could not initialize SystemTextClassifier", localServiceNotFoundException);
          }
        }
      }
      if (mSystemTextClassifier != null) {
        return mSystemTextClassifier;
      }
      return TextClassifier.NO_OP;
    }
  }
  
  private void invalidate()
  {
    synchronized (mLock)
    {
      mSettings = null;
      mLocalTextClassifier = null;
      mSystemTextClassifier = null;
      return;
    }
  }
  
  private boolean isSystemTextClassifierEnabled()
  {
    boolean bool;
    if ((getSettings().isSystemTextClassifierEnabled()) && (TextClassifierService.getServiceComponentName(mContext) != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public TextClassifier createTextClassificationSession(TextClassificationContext paramTextClassificationContext)
  {
    Preconditions.checkNotNull(paramTextClassificationContext);
    paramTextClassificationContext = mSessionFactory.createTextClassificationSession(paramTextClassificationContext);
    Preconditions.checkNotNull(paramTextClassificationContext, "Session Factory should never return null");
    return paramTextClassificationContext;
  }
  
  public TextClassifier createTextClassificationSession(TextClassificationContext paramTextClassificationContext, TextClassifier paramTextClassifier)
  {
    Preconditions.checkNotNull(paramTextClassificationContext);
    Preconditions.checkNotNull(paramTextClassifier);
    return new TextClassificationSession(paramTextClassificationContext, paramTextClassifier);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mSettingsObserver != null) {
        getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsObserver);
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  Context getApplicationContext()
  {
    Context localContext;
    if (mContext.getApplicationContext() != null) {
      localContext = mContext.getApplicationContext();
    } else {
      localContext = mContext;
    }
    return localContext;
  }
  
  public TextClassifier getTextClassifier()
  {
    synchronized (mLock)
    {
      if (mCustomTextClassifier != null)
      {
        localTextClassifier = mCustomTextClassifier;
        return localTextClassifier;
      }
      if (isSystemTextClassifierEnabled())
      {
        localTextClassifier = getSystemTextClassifier();
        return localTextClassifier;
      }
      TextClassifier localTextClassifier = getLocalTextClassifier();
      return localTextClassifier;
    }
  }
  
  public TextClassifier getTextClassifier(int paramInt)
  {
    if (paramInt != 0) {
      return getSystemTextClassifier();
    }
    return getLocalTextClassifier();
  }
  
  public void setTextClassificationSessionFactory(TextClassificationSessionFactory paramTextClassificationSessionFactory)
  {
    Object localObject = mLock;
    if (paramTextClassificationSessionFactory != null) {
      try
      {
        mSessionFactory = paramTextClassificationSessionFactory;
      }
      finally
      {
        break label34;
      }
    } else {
      mSessionFactory = mDefaultSessionFactory;
    }
    return;
    label34:
    throw paramTextClassificationSessionFactory;
  }
  
  public void setTextClassifier(TextClassifier paramTextClassifier)
  {
    synchronized (mLock)
    {
      mCustomTextClassifier = paramTextClassifier;
      return;
    }
  }
  
  private static final class SettingsObserver
    extends ContentObserver
  {
    private final WeakReference<TextClassificationManager> mTcm;
    
    SettingsObserver(TextClassificationManager paramTextClassificationManager)
    {
      super();
      mTcm = new WeakReference(paramTextClassificationManager);
      paramTextClassificationManager.getApplicationContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("text_classifier_constants"), false, this);
    }
    
    public void onChange(boolean paramBoolean)
    {
      TextClassificationManager localTextClassificationManager = (TextClassificationManager)mTcm.get();
      if (localTextClassificationManager != null) {
        localTextClassificationManager.invalidate();
      }
    }
  }
}
