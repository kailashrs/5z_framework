package android.view.textclassifier;

import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.ParcelFileDescriptor;
import android.os.UserManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextClassifierImpl
  implements TextClassifier
{
  private static final String LOG_TAG = "androidtc";
  private static final String MODEL_DIR = "/etc/textclassifier/";
  private static final String MODEL_FILE_REGEX = "textclassifier\\.(.*)\\.model";
  private static final String UPDATED_MODEL_FILE_PATH = "/data/misc/textclassifier/textclassifier.model";
  @GuardedBy("mLock")
  private List<ModelFile> mAllModelFiles;
  private final Context mContext;
  private final TextClassifier mFallback;
  private final GenerateLinksLogger mGenerateLinksLogger;
  private final Object mLock = new Object();
  private final Object mLoggerLock = new Object();
  @GuardedBy("mLock")
  private ModelFile mModel;
  @GuardedBy("mLock")
  private TextClassifierImplNative mNative;
  @GuardedBy("mLoggerLock")
  private SelectionSessionLogger mSessionLogger;
  private final TextClassificationConstants mSettings;
  
  public TextClassifierImpl(Context paramContext, TextClassificationConstants paramTextClassificationConstants)
  {
    this(paramContext, paramTextClassificationConstants, TextClassifier.NO_OP);
  }
  
  public TextClassifierImpl(Context paramContext, TextClassificationConstants paramTextClassificationConstants, TextClassifier paramTextClassifier)
  {
    mContext = ((Context)Preconditions.checkNotNull(paramContext));
    mFallback = ((TextClassifier)Preconditions.checkNotNull(paramTextClassifier));
    mSettings = ((TextClassificationConstants)Preconditions.checkNotNull(paramTextClassificationConstants));
    mGenerateLinksLogger = new GenerateLinksLogger(mSettings.getGenerateLinksLogSampleRate());
  }
  
  private static void closeAndLogError(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    try
    {
      paramParcelFileDescriptor.close();
    }
    catch (IOException paramParcelFileDescriptor)
    {
      Log.e("androidtc", "Error closing file.", paramParcelFileDescriptor);
    }
  }
  
  private static String concatenateLocales(LocaleList paramLocaleList)
  {
    if (paramLocaleList == null) {
      paramLocaleList = "";
    } else {
      paramLocaleList = paramLocaleList.toLanguageTags();
    }
    return paramLocaleList;
  }
  
  private TextClassification createClassificationResult(TextClassifierImplNative.ClassificationResult[] paramArrayOfClassificationResult, String paramString, int paramInt1, int paramInt2, Instant paramInstant)
  {
    String str = paramString.substring(paramInt1, paramInt2);
    TextClassification.Builder localBuilder = new TextClassification.Builder().setText(str);
    int i = paramArrayOfClassificationResult.length;
    Object localObject = null;
    float f1 = Float.MIN_VALUE;
    int j = 0;
    while (j < i)
    {
      localBuilder.setEntityType(paramArrayOfClassificationResult[j].getCollection(), paramArrayOfClassificationResult[j].getScore());
      float f2 = f1;
      if (paramArrayOfClassificationResult[j].getScore() > f1)
      {
        localObject = paramArrayOfClassificationResult[j];
        f2 = paramArrayOfClassificationResult[j].getScore();
      }
      j++;
      f1 = f2;
    }
    j = 1;
    localObject = IntentFactory.create(mContext, paramInstant, (TextClassifierImplNative.ClassificationResult)localObject, str).iterator();
    while (((Iterator)localObject).hasNext())
    {
      paramArrayOfClassificationResult = (LabeledIntent)((Iterator)localObject).next();
      paramInstant = paramArrayOfClassificationResult.asRemoteAction(mContext);
      if (paramInstant != null)
      {
        i = j;
        if (j != 0)
        {
          localBuilder.setIcon(paramInstant.getIcon().loadDrawable(mContext));
          localBuilder.setLabel(paramInstant.getTitle().toString());
          localBuilder.setIntent(paramArrayOfClassificationResult.getIntent());
          localBuilder.setOnClickListener(TextClassification.createIntentOnClickListener(TextClassification.createPendingIntent(mContext, paramArrayOfClassificationResult.getIntent(), paramArrayOfClassificationResult.getRequestCode())));
          i = 0;
        }
        localBuilder.addAction(paramInstant);
        j = i;
      }
    }
    return localBuilder.setId(createId(paramString, paramInt1, paramInt2)).build();
  }
  
  private String createId(String paramString, int paramInt1, int paramInt2)
  {
    synchronized (mLock)
    {
      paramString = SelectionSessionLogger.createId(paramString, paramInt1, paramInt2, mContext, mModel.getVersion(), mModel.getSupportedLocales());
      return paramString;
    }
  }
  
  @GuardedBy("mLock")
  private void destroyNativeIfExistsLocked()
  {
    if (mNative != null)
    {
      mNative.close();
      mNative = null;
    }
  }
  
  @GuardedBy("mLock")
  private ModelFile findBestModelLocked(LocaleList paramLocaleList)
  {
    if (paramLocaleList.isEmpty())
    {
      paramLocaleList = LocaleList.getDefault().toLanguageTags();
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramLocaleList.toLanguageTags());
      ((StringBuilder)localObject).append(",");
      ((StringBuilder)localObject).append(LocaleList.getDefault().toLanguageTags());
      paramLocaleList = ((StringBuilder)localObject).toString();
    }
    List localList = Locale.LanguageRange.parse(paramLocaleList);
    Object localObject = null;
    Iterator localIterator = listAllModelsLocked().iterator();
    while (localIterator.hasNext())
    {
      ModelFile localModelFile = (ModelFile)localIterator.next();
      paramLocaleList = (LocaleList)localObject;
      if (localModelFile.isAnyLanguageSupported(localList))
      {
        paramLocaleList = (LocaleList)localObject;
        if (localModelFile.isPreferredTo((ModelFile)localObject)) {
          paramLocaleList = localModelFile;
        }
      }
      localObject = paramLocaleList;
    }
    return localObject;
  }
  
  private Collection<String> getEntitiesForHints(Collection<String> paramCollection)
  {
    boolean bool = paramCollection.contains("android.text_is_editable");
    int i;
    if (bool == paramCollection.contains("android.text_is_not_editable")) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      return mSettings.getEntityListDefault();
    }
    if (bool) {
      return mSettings.getEntityListEditable();
    }
    return mSettings.getEntityListNotEditable();
  }
  
  private TextClassifierImplNative getNative(LocaleList paramLocaleList)
    throws FileNotFoundException
  {
    Object localObject1 = mLock;
    if (paramLocaleList == null) {
      try
      {
        paramLocaleList = LocaleList.getEmptyLocaleList();
      }
      finally
      {
        break label185;
      }
    }
    Object localObject2 = findBestModelLocked(paramLocaleList);
    if (localObject2 != null)
    {
      if ((mNative == null) || (!Objects.equals(mModel, localObject2)))
      {
        paramLocaleList = new java/lang/StringBuilder;
        paramLocaleList.<init>();
        paramLocaleList.append("Loading ");
        paramLocaleList.append(localObject2);
        Log.d("androidtc", paramLocaleList.toString());
        destroyNativeIfExistsLocked();
        paramLocaleList = new java/io/File;
        paramLocaleList.<init>(((ModelFile)localObject2).getPath());
        localObject3 = ParcelFileDescriptor.open(paramLocaleList, 268435456);
        paramLocaleList = new android/view/textclassifier/TextClassifierImplNative;
        paramLocaleList.<init>(((ParcelFileDescriptor)localObject3).getFd());
        mNative = paramLocaleList;
        closeAndLogError((ParcelFileDescriptor)localObject3);
        mModel = ((ModelFile)localObject2);
      }
      paramLocaleList = mNative;
      return paramLocaleList;
    }
    Object localObject3 = new java/io/FileNotFoundException;
    localObject2 = new java/lang/StringBuilder;
    ((StringBuilder)localObject2).<init>();
    ((StringBuilder)localObject2).append("No model for ");
    ((StringBuilder)localObject2).append(paramLocaleList.toLanguageTags());
    ((FileNotFoundException)localObject3).<init>(((StringBuilder)localObject2).toString());
    throw ((Throwable)localObject3);
    label185:
    throw paramLocaleList;
  }
  
  @GuardedBy("mLock")
  private List<ModelFile> listAllModelsLocked()
  {
    if (mAllModelFiles == null)
    {
      ArrayList localArrayList = new ArrayList();
      if (new File("/data/misc/textclassifier/textclassifier.model").exists())
      {
        localObject1 = ModelFile.fromPath("/data/misc/textclassifier/textclassifier.model");
        if (localObject1 != null) {
          localArrayList.add(localObject1);
        }
      }
      Object localObject1 = new File("/etc/textclassifier/");
      if ((((File)localObject1).exists()) && (((File)localObject1).isDirectory()))
      {
        File[] arrayOfFile = ((File)localObject1).listFiles();
        localObject1 = Pattern.compile("textclassifier\\.(.*)\\.model");
        int i = arrayOfFile.length;
        for (int j = 0; j < i; j++)
        {
          Object localObject2 = arrayOfFile[j];
          if ((((Pattern)localObject1).matcher(((File)localObject2).getName()).matches()) && (((File)localObject2).isFile()))
          {
            localObject2 = ModelFile.fromPath(((File)localObject2).getAbsolutePath());
            if (localObject2 != null) {
              localArrayList.add(localObject2);
            }
          }
        }
      }
      mAllModelFiles = localArrayList;
    }
    return mAllModelFiles;
  }
  
  public TextClassification classifyText(TextClassification.Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    TextClassifier.Utils.checkMainThread();
    try
    {
      int i = paramRequest.getEndIndex();
      int j = paramRequest.getStartIndex();
      String str = paramRequest.getText().toString();
      if ((str.length() > 0) && (i - j <= mSettings.getClassifyTextMaxRangeLength()))
      {
        Object localObject1 = concatenateLocales(paramRequest.getDefaultLocales());
        Object localObject2;
        if (paramRequest.getReferenceTime() != null) {
          localObject2 = paramRequest.getReferenceTime();
        } else {
          localObject2 = ZonedDateTime.now();
        }
        TextClassifierImplNative localTextClassifierImplNative = getNative(paramRequest.getDefaultLocales());
        i = paramRequest.getStartIndex();
        j = paramRequest.getEndIndex();
        TextClassifierImplNative.ClassificationOptions localClassificationOptions = new android/view/textclassifier/TextClassifierImplNative$ClassificationOptions;
        localClassificationOptions.<init>(((ZonedDateTime)localObject2).toInstant().toEpochMilli(), ((ZonedDateTime)localObject2).getZone().getId(), (String)localObject1);
        localObject1 = localTextClassifierImplNative.classifyText(str, i, j, localClassificationOptions);
        if (localObject1.length > 0)
        {
          localObject2 = createClassificationResult((TextClassifierImplNative.ClassificationResult[])localObject1, str, paramRequest.getStartIndex(), paramRequest.getEndIndex(), ((ZonedDateTime)localObject2).toInstant());
          return localObject2;
        }
      }
    }
    catch (Throwable localThrowable)
    {
      Log.e("androidtc", "Error getting text classification info.", localThrowable);
    }
    return mFallback.classifyText(paramRequest);
  }
  
  public TextLinks generateLinks(TextLinks.Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    TextClassifier.Utils.checkTextLength(paramRequest.getText(), getMaxGenerateLinksTextLength());
    TextClassifier.Utils.checkMainThread();
    if ((!mSettings.isSmartLinkifyEnabled()) && (paramRequest.isLegacyFallback())) {
      return TextClassifier.Utils.generateLegacyLinks(paramRequest);
    }
    Object localObject1 = paramRequest.getText().toString();
    TextLinks.Builder localBuilder = new TextLinks.Builder((String)localObject1);
    try
    {
      long l1 = System.currentTimeMillis();
      Object localObject2 = ZonedDateTime.now();
      if (paramRequest.getEntityConfig() != null) {
        localObject3 = paramRequest.getEntityConfig().resolveEntityListModifications(getEntitiesForHints(paramRequest.getEntityConfig().getHints()));
      } else {
        localObject3 = mSettings.getEntityListDefault();
      }
      Object localObject4 = getNative(paramRequest.getDefaultLocales());
      TextClassifierImplNative.AnnotationOptions localAnnotationOptions = new android/view/textclassifier/TextClassifierImplNative$AnnotationOptions;
      localAnnotationOptions.<init>(((ZonedDateTime)localObject2).toInstant().toEpochMilli(), ((ZonedDateTime)localObject2).getZone().getId(), concatenateLocales(paramRequest.getDefaultLocales()));
      for (localAnnotationOptions : ((TextClassifierImplNative)localObject4).annotate((String)localObject1, localAnnotationOptions))
      {
        TextClassifierImplNative.ClassificationResult[] arrayOfClassificationResult = localAnnotationOptions.getClassification();
        if ((arrayOfClassificationResult.length != 0) && (((Collection)localObject3).contains(arrayOfClassificationResult[0].getCollection())))
        {
          localObject4 = new java/util/HashMap;
          ((HashMap)localObject4).<init>();
          for (int k = 0; k < arrayOfClassificationResult.length; k++) {
            ((Map)localObject4).put(arrayOfClassificationResult[k].getCollection(), Float.valueOf(arrayOfClassificationResult[k].getScore()));
          }
          localBuilder.addLink(localAnnotationOptions.getStartIndex(), localAnnotationOptions.getEndIndex(), (Map)localObject4);
        }
      }
      Object localObject3 = localBuilder.build();
      long l2 = System.currentTimeMillis();
      if (paramRequest.getCallingPackageName() == null) {
        localObject2 = mContext.getPackageName();
      } else {
        localObject2 = paramRequest.getCallingPackageName();
      }
      mGenerateLinksLogger.logGenerateLinks(paramRequest.getText(), (TextLinks)localObject3, (String)localObject2, l2 - l1);
      return localObject3;
    }
    catch (Throwable localThrowable)
    {
      Log.e("androidtc", "Error getting links info.", localThrowable);
    }
    return mFallback.generateLinks(paramRequest);
  }
  
  public int getMaxGenerateLinksTextLength()
  {
    return mSettings.getGenerateLinksMaxTextLength();
  }
  
  public void onSelectionEvent(SelectionEvent paramSelectionEvent)
  {
    Preconditions.checkNotNull(paramSelectionEvent);
    synchronized (mLoggerLock)
    {
      if (mSessionLogger == null)
      {
        SelectionSessionLogger localSelectionSessionLogger = new android/view/textclassifier/SelectionSessionLogger;
        localSelectionSessionLogger.<init>();
        mSessionLogger = localSelectionSessionLogger;
      }
      mSessionLogger.writeEvent(paramSelectionEvent);
      return;
    }
  }
  
  public TextSelection suggestSelection(TextSelection.Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    TextClassifier.Utils.checkMainThread();
    try
    {
      int i = paramRequest.getEndIndex();
      int j = paramRequest.getStartIndex();
      String str1 = paramRequest.getText().toString();
      if ((str1.length() > 0) && (i - j <= mSettings.getSuggestSelectionMaxRangeLength()))
      {
        String str2 = concatenateLocales(paramRequest.getDefaultLocales());
        ZonedDateTime localZonedDateTime = ZonedDateTime.now();
        Object localObject1 = getNative(paramRequest.getDefaultLocales());
        boolean bool = mSettings.isModelDarkLaunchEnabled();
        int k = 0;
        Object localObject2;
        if ((bool) && (!paramRequest.isDarkLaunchAllowed()))
        {
          i = paramRequest.getStartIndex();
          j = paramRequest.getEndIndex();
        }
        else
        {
          i = paramRequest.getStartIndex();
          j = paramRequest.getEndIndex();
          localObject2 = new android/view/textclassifier/TextClassifierImplNative$SelectionOptions;
          ((TextClassifierImplNative.SelectionOptions)localObject2).<init>(str2);
          localObject2 = ((TextClassifierImplNative)localObject1).suggestSelection(str1, i, j, (TextClassifierImplNative.SelectionOptions)localObject2);
          i = localObject2[0];
          j = localObject2[1];
        }
        if ((i < j) && (i >= 0) && (j <= str1.length()) && (i <= paramRequest.getStartIndex()) && (j >= paramRequest.getEndIndex()))
        {
          localObject2 = new android/view/textclassifier/TextSelection$Builder;
          ((TextSelection.Builder)localObject2).<init>(i, j);
          TextClassifierImplNative.ClassificationOptions localClassificationOptions = new android/view/textclassifier/TextClassifierImplNative$ClassificationOptions;
          localClassificationOptions.<init>(localZonedDateTime.toInstant().toEpochMilli(), localZonedDateTime.getZone().getId(), str2);
          localObject1 = ((TextClassifierImplNative)localObject1).classifyText(str1, i, j, localClassificationOptions);
          j = localObject1.length;
          for (i = k; i < j; i++) {
            ((TextSelection.Builder)localObject2).setEntityType(localObject1[i].getCollection(), localObject1[i].getScore());
          }
          return ((TextSelection.Builder)localObject2).setId(createId(str1, paramRequest.getStartIndex(), paramRequest.getEndIndex())).build();
        }
        Log.d("androidtc", "Got bad indices for input text. Ignoring result.");
      }
    }
    catch (Throwable localThrowable)
    {
      Log.e("androidtc", "Error suggesting selection for text. No changes to selection suggested.", localThrowable);
    }
    return mFallback.suggestSelection(paramRequest);
  }
  
  static final class IntentFactory
  {
    private static final long DEFAULT_EVENT_DURATION = TimeUnit.HOURS.toMillis(1L);
    private static final long MIN_EVENT_FUTURE_MILLIS = TimeUnit.MINUTES.toMillis(5L);
    
    private IntentFactory() {}
    
    public static List<TextClassifierImpl.LabeledIntent> create(Context paramContext, Instant paramInstant, TextClassifierImplNative.ClassificationResult paramClassificationResult, String paramString)
    {
      String str = paramClassificationResult.getCollection().trim().toLowerCase(Locale.ENGLISH);
      paramString = paramString.trim();
      switch (str.hashCode())
      {
      default: 
        break;
      case 1793702779: 
        if (str.equals("datetime")) {
          i = 5;
        }
        break;
      case 106642798: 
        if (str.equals("phone")) {
          i = 1;
        }
        break;
      case 96619420: 
        if (str.equals("email")) {
          i = 0;
        }
        break;
      case 3076014: 
        if (str.equals("date")) {
          i = 4;
        }
        break;
      case 116079: 
        if (str.equals("url")) {
          i = 3;
        }
        break;
      case -1147692044: 
        if (str.equals("address")) {
          i = 2;
        }
        break;
      case -1271823248: 
        if (str.equals("flight")) {
          i = 6;
        }
        break;
      }
      int i = -1;
      switch (i)
      {
      default: 
        return new ArrayList();
      case 6: 
        return createForFlight(paramContext, paramString);
      case 4: 
      case 5: 
        if (paramClassificationResult.getDatetimeResult() != null) {
          return createForDatetime(paramContext, str, paramInstant, Instant.ofEpochMilli(paramClassificationResult.getDatetimeResult().getTimeMsUtc()));
        }
        return new ArrayList();
      case 3: 
        return createForUrl(paramContext, paramString);
      case 2: 
        return createForAddress(paramContext, paramString);
      case 1: 
        return createForPhone(paramContext, paramString);
      }
      return createForEmail(paramContext, paramString);
    }
    
    private static TextClassifierImpl.LabeledIntent createCalendarCreateEventIntent(Context paramContext, Instant paramInstant, String paramString)
    {
      boolean bool = "date".equals(paramString);
      return new TextClassifierImpl.LabeledIntent(paramContext.getString(17039449), paramContext.getString(17039450), new Intent("android.intent.action.INSERT").setData(CalendarContract.Events.CONTENT_URI).putExtra("allDay", bool).putExtra("beginTime", paramInstant.toEpochMilli()).putExtra("endTime", paramInstant.toEpochMilli() + DEFAULT_EVENT_DURATION), paramInstant.hashCode());
    }
    
    private static TextClassifierImpl.LabeledIntent createCalendarViewIntent(Context paramContext, Instant paramInstant)
    {
      Uri.Builder localBuilder = CalendarContract.CONTENT_URI.buildUpon();
      localBuilder.appendPath("time");
      ContentUris.appendId(localBuilder, paramInstant.toEpochMilli());
      return new TextClassifierImpl.LabeledIntent(paramContext.getString(17041167), paramContext.getString(17041168), new Intent("android.intent.action.VIEW").setData(localBuilder.build()), 0);
    }
    
    private static List<TextClassifierImpl.LabeledIntent> createForAddress(Context paramContext, String paramString)
    {
      ArrayList localArrayList = new ArrayList();
      try
      {
        String str1 = URLEncoder.encode(paramString, "UTF-8");
        paramString = new android/view/textclassifier/TextClassifierImpl$LabeledIntent;
        String str2 = paramContext.getString(17040307);
        paramContext = paramContext.getString(17040308);
        Intent localIntent = new android/content/Intent;
        localIntent.<init>("android.intent.action.VIEW");
        paramString.<init>(str2, paramContext, localIntent.setData(Uri.parse(String.format("geo:0,0?q=%s", new Object[] { str1 }))), 0);
        localArrayList.add(paramString);
      }
      catch (UnsupportedEncodingException paramContext)
      {
        Log.e("androidtc", "Could not encode address", paramContext);
      }
      return localArrayList;
    }
    
    private static List<TextClassifierImpl.LabeledIntent> createForDatetime(Context paramContext, String paramString, Instant paramInstant1, Instant paramInstant2)
    {
      Instant localInstant = paramInstant1;
      if (paramInstant1 == null) {
        localInstant = Instant.now();
      }
      paramInstant1 = new ArrayList();
      paramInstant1.add(createCalendarViewIntent(paramContext, paramInstant2));
      if (localInstant.until(paramInstant2, ChronoUnit.MILLIS) > MIN_EVENT_FUTURE_MILLIS) {
        paramInstant1.add(createCalendarCreateEventIntent(paramContext, paramInstant2, paramString));
      }
      return paramInstant1;
    }
    
    private static List<TextClassifierImpl.LabeledIntent> createForEmail(Context paramContext, String paramString)
    {
      return Arrays.asList(new TextClassifierImpl.LabeledIntent[] { new TextClassifierImpl.LabeledIntent(paramContext.getString(17039910), paramContext.getString(17039916), new Intent("android.intent.action.SENDTO").setData(Uri.parse(String.format("mailto:%s", new Object[] { paramString }))), 0), new TextClassifierImpl.LabeledIntent(paramContext.getString(17039451), paramContext.getString(17039452), new Intent("android.intent.action.INSERT_OR_EDIT").setType("vnd.android.cursor.item/contact").putExtra("email", paramString), paramString.hashCode()) });
    }
    
    private static List<TextClassifierImpl.LabeledIntent> createForFlight(Context paramContext, String paramString)
    {
      return Arrays.asList(new TextClassifierImpl.LabeledIntent[] { new TextClassifierImpl.LabeledIntent(paramContext.getString(17041169), paramContext.getString(17041170), new Intent("android.intent.action.WEB_SEARCH").putExtra("query", paramString), paramString.hashCode()) });
    }
    
    private static List<TextClassifierImpl.LabeledIntent> createForPhone(Context paramContext, String paramString)
    {
      ArrayList localArrayList = new ArrayList();
      Object localObject = (UserManager)paramContext.getSystemService(UserManager.class);
      if (localObject != null) {
        localObject = ((UserManager)localObject).getUserRestrictions();
      } else {
        localObject = new Bundle();
      }
      if (!((Bundle)localObject).getBoolean("no_outgoing_calls", false)) {
        localArrayList.add(new TextClassifierImpl.LabeledIntent(paramContext.getString(17039886), paramContext.getString(17039887), new Intent("android.intent.action.DIAL").setData(Uri.parse(String.format("tel:%s", new Object[] { paramString }))), 0));
      }
      localArrayList.add(new TextClassifierImpl.LabeledIntent(paramContext.getString(17039451), paramContext.getString(17039452), new Intent("android.intent.action.INSERT_OR_EDIT").setType("vnd.android.cursor.item/contact").putExtra("phone", paramString), paramString.hashCode()));
      if (!((Bundle)localObject).getBoolean("no_sms", false)) {
        localArrayList.add(new TextClassifierImpl.LabeledIntent(paramContext.getString(17041025), paramContext.getString(17041030), new Intent("android.intent.action.SENDTO").setData(Uri.parse(String.format("smsto:%s", new Object[] { paramString }))), 0));
      }
      return localArrayList;
    }
    
    private static List<TextClassifierImpl.LabeledIntent> createForUrl(Context paramContext, String paramString)
    {
      Object localObject = paramString;
      if (Uri.parse(paramString).getScheme() == null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("http://");
        ((StringBuilder)localObject).append(paramString);
        localObject = ((StringBuilder)localObject).toString();
      }
      return Arrays.asList(new TextClassifierImpl.LabeledIntent[] { new TextClassifierImpl.LabeledIntent(paramContext.getString(17039615), paramContext.getString(17039616), new Intent("android.intent.action.VIEW", Uri.parse((String)localObject)).putExtra("com.android.browser.application_id", paramContext.getPackageName()), 0) });
    }
  }
  
  private static final class LabeledIntent
  {
    static final int DEFAULT_REQUEST_CODE = 0;
    private final String mDescription;
    private final Intent mIntent;
    private final int mRequestCode;
    private final String mTitle;
    
    LabeledIntent(String paramString1, String paramString2, Intent paramIntent, int paramInt)
    {
      mTitle = paramString1;
      mDescription = paramString2;
      mIntent = paramIntent;
      mRequestCode = paramInt;
    }
    
    RemoteAction asRemoteAction(Context paramContext)
    {
      ResolveInfo localResolveInfo = paramContext.getPackageManager().resolveActivity(mIntent, 0);
      if ((localResolveInfo != null) && (activityInfo != null)) {
        localObject1 = activityInfo.packageName;
      } else {
        localObject1 = null;
      }
      Object localObject2 = null;
      boolean bool1 = false;
      Object localObject3 = localObject2;
      boolean bool2 = bool1;
      if (localObject1 != null)
      {
        localObject3 = localObject2;
        bool2 = bool1;
        if (!"android".equals(localObject1))
        {
          mIntent.setComponent(new ComponentName((String)localObject1, activityInfo.name));
          localObject3 = localObject2;
          bool2 = bool1;
          if (activityInfo.getIconResource() != 0)
          {
            localObject3 = Icon.createWithResource((String)localObject1, activityInfo.getIconResource());
            bool2 = true;
          }
        }
      }
      Object localObject1 = localObject3;
      if (localObject3 == null) {
        localObject1 = Icon.createWithResource("android", 17302958);
      }
      paramContext = TextClassification.createPendingIntent(paramContext, mIntent, mRequestCode);
      if (paramContext == null) {
        return null;
      }
      paramContext = new RemoteAction((Icon)localObject1, mTitle, mDescription, paramContext);
      paramContext.setShouldShowIcon(bool2);
      return paramContext;
    }
    
    String getDescription()
    {
      return mDescription;
    }
    
    Intent getIntent()
    {
      return mIntent;
    }
    
    int getRequestCode()
    {
      return mRequestCode;
    }
    
    String getTitle()
    {
      return mTitle;
    }
  }
  
  private static final class ModelFile
  {
    private final boolean mLanguageIndependent;
    private final String mName;
    private final String mPath;
    private final List<Locale> mSupportedLocales;
    private final int mVersion;
    
    private ModelFile(String paramString1, String paramString2, int paramInt, List<Locale> paramList, boolean paramBoolean)
    {
      mPath = paramString1;
      mName = paramString2;
      mVersion = paramInt;
      mSupportedLocales = paramList;
      mLanguageIndependent = paramBoolean;
    }
    
    static ModelFile fromPath(String paramString)
    {
      File localFile = new File(paramString);
      try
      {
        localObject1 = ParcelFileDescriptor.open(localFile, 268435456);
        int i = TextClassifierImplNative.getVersion(((ParcelFileDescriptor)localObject1).getFd());
        Object localObject2 = TextClassifierImplNative.getLocales(((ParcelFileDescriptor)localObject1).getFd());
        if (((String)localObject2).isEmpty())
        {
          paramString = new java/lang/StringBuilder;
          paramString.<init>();
          paramString.append("Ignoring ");
          paramString.append(localFile.getAbsolutePath());
          Log.d("androidtc", paramString.toString());
          return null;
        }
        boolean bool = ((String)localObject2).equals("*");
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>();
        localObject2 = ((String)localObject2).split(",");
        int j = localObject2.length;
        for (int k = 0; k < j; k++) {
          localArrayList.add(Locale.forLanguageTag(localObject2[k]));
        }
        TextClassifierImpl.closeAndLogError((ParcelFileDescriptor)localObject1);
        paramString = new ModelFile(paramString, localFile.getName(), i, localArrayList, bool);
        return paramString;
      }
      catch (FileNotFoundException paramString)
      {
        Object localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Failed to peek ");
        ((StringBuilder)localObject1).append(localFile.getAbsolutePath());
        Log.e("androidtc", ((StringBuilder)localObject1).toString(), paramString);
      }
      return null;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (ModelFile.class.isAssignableFrom(paramObject.getClass())))
      {
        paramObject = (ModelFile)paramObject;
        return mPath.equals(mPath);
      }
      return false;
    }
    
    String getName()
    {
      return mName;
    }
    
    String getPath()
    {
      return mPath;
    }
    
    List<Locale> getSupportedLocales()
    {
      return Collections.unmodifiableList(mSupportedLocales);
    }
    
    int getVersion()
    {
      return mVersion;
    }
    
    boolean isAnyLanguageSupported(List<Locale.LanguageRange> paramList)
    {
      boolean bool;
      if ((!mLanguageIndependent) && (Locale.lookup(paramList, mSupportedLocales) == null)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean isPreferredTo(ModelFile paramModelFile)
    {
      if (paramModelFile == null) {
        return true;
      }
      if ((!mLanguageIndependent) && (mLanguageIndependent)) {
        return true;
      }
      return getVersion() > paramModelFile.getVersion();
    }
    
    public String toString()
    {
      StringJoiner localStringJoiner = new StringJoiner(",");
      Iterator localIterator = mSupportedLocales.iterator();
      while (localIterator.hasNext()) {
        localStringJoiner.add(((Locale)localIterator.next()).toLanguageTag());
      }
      return String.format(Locale.US, "ModelFile { path=%s name=%s version=%d locales=%s }", new Object[] { mPath, mName, Integer.valueOf(mVersion), localStringJoiner.toString() });
    }
  }
}
