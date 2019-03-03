package android.view.textclassifier;

import android.os.LocaleList;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract interface TextClassifier
{
  public static final String DEFAULT_LOG_TAG = "androidtc";
  public static final String HINT_TEXT_IS_EDITABLE = "android.text_is_editable";
  public static final String HINT_TEXT_IS_NOT_EDITABLE = "android.text_is_not_editable";
  public static final int LOCAL = 0;
  public static final TextClassifier NO_OP = new TextClassifier() {};
  public static final int SYSTEM = 1;
  public static final String TYPE_ADDRESS = "address";
  public static final String TYPE_DATE = "date";
  public static final String TYPE_DATE_TIME = "datetime";
  public static final String TYPE_EMAIL = "email";
  public static final String TYPE_FLIGHT_NUMBER = "flight";
  public static final String TYPE_OTHER = "other";
  public static final String TYPE_PHONE = "phone";
  public static final String TYPE_UNKNOWN = "";
  public static final String TYPE_URL = "url";
  public static final String WIDGET_TYPE_CUSTOM_EDITTEXT = "customedit";
  public static final String WIDGET_TYPE_CUSTOM_TEXTVIEW = "customview";
  public static final String WIDGET_TYPE_CUSTOM_UNSELECTABLE_TEXTVIEW = "nosel-customview";
  public static final String WIDGET_TYPE_EDITTEXT = "edittext";
  public static final String WIDGET_TYPE_EDIT_WEBVIEW = "edit-webview";
  public static final String WIDGET_TYPE_TEXTVIEW = "textview";
  public static final String WIDGET_TYPE_UNKNOWN = "unknown";
  public static final String WIDGET_TYPE_UNSELECTABLE_TEXTVIEW = "nosel-textview";
  public static final String WIDGET_TYPE_WEBVIEW = "webview";
  
  public TextClassification classifyText(TextClassification.Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    Utils.checkMainThread();
    return TextClassification.EMPTY;
  }
  
  public TextClassification classifyText(CharSequence paramCharSequence, int paramInt1, int paramInt2, LocaleList paramLocaleList)
  {
    return classifyText(new TextClassification.Request.Builder(paramCharSequence, paramInt1, paramInt2).setDefaultLocales(paramLocaleList).build());
  }
  
  public TextClassification classifyText(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextClassification.Options paramOptions)
  {
    if (paramOptions == null) {
      return classifyText(new TextClassification.Request.Builder(paramCharSequence, paramInt1, paramInt2).build());
    }
    if (paramOptions.getRequest() != null) {
      return classifyText(paramOptions.getRequest());
    }
    return classifyText(new TextClassification.Request.Builder(paramCharSequence, paramInt1, paramInt2).setDefaultLocales(paramOptions.getDefaultLocales()).setReferenceTime(paramOptions.getReferenceTime()).build());
  }
  
  public void destroy() {}
  
  public TextLinks generateLinks(TextLinks.Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    Utils.checkMainThread();
    return new TextLinks.Builder(paramRequest.getText().toString()).build();
  }
  
  public TextLinks generateLinks(CharSequence paramCharSequence, TextLinks.Options paramOptions)
  {
    if (paramOptions == null) {
      return generateLinks(new TextLinks.Request.Builder(paramCharSequence).build());
    }
    if (paramOptions.getRequest() != null) {
      return generateLinks(paramOptions.getRequest());
    }
    return generateLinks(new TextLinks.Request.Builder(paramCharSequence).setDefaultLocales(paramOptions.getDefaultLocales()).setEntityConfig(paramOptions.getEntityConfig()).build());
  }
  
  public int getMaxGenerateLinksTextLength()
  {
    return Integer.MAX_VALUE;
  }
  
  public boolean isDestroyed()
  {
    return false;
  }
  
  public void onSelectionEvent(SelectionEvent paramSelectionEvent) {}
  
  public TextSelection suggestSelection(TextSelection.Request paramRequest)
  {
    Preconditions.checkNotNull(paramRequest);
    Utils.checkMainThread();
    return new TextSelection.Builder(paramRequest.getStartIndex(), paramRequest.getEndIndex()).build();
  }
  
  public TextSelection suggestSelection(CharSequence paramCharSequence, int paramInt1, int paramInt2, LocaleList paramLocaleList)
  {
    return suggestSelection(new TextSelection.Request.Builder(paramCharSequence, paramInt1, paramInt2).setDefaultLocales(paramLocaleList).build());
  }
  
  public TextSelection suggestSelection(CharSequence paramCharSequence, int paramInt1, int paramInt2, TextSelection.Options paramOptions)
  {
    if (paramOptions == null) {
      return suggestSelection(new TextSelection.Request.Builder(paramCharSequence, paramInt1, paramInt2).build());
    }
    if (paramOptions.getRequest() != null) {
      return suggestSelection(paramOptions.getRequest());
    }
    return suggestSelection(new TextSelection.Request.Builder(paramCharSequence, paramInt1, paramInt2).setDefaultLocales(paramOptions.getDefaultLocales()).build());
  }
  
  public static final class EntityConfig
    implements Parcelable
  {
    public static final Parcelable.Creator<EntityConfig> CREATOR = new Parcelable.Creator()
    {
      public TextClassifier.EntityConfig createFromParcel(Parcel paramAnonymousParcel)
      {
        return new TextClassifier.EntityConfig(paramAnonymousParcel, null);
      }
      
      public TextClassifier.EntityConfig[] newArray(int paramAnonymousInt)
      {
        return new TextClassifier.EntityConfig[paramAnonymousInt];
      }
    };
    private final Collection<String> mExcludedEntityTypes;
    private final Collection<String> mHints;
    private final Collection<String> mIncludedEntityTypes;
    private final boolean mUseHints;
    
    private EntityConfig(Parcel paramParcel)
    {
      mHints = new ArraySet(paramParcel.createStringArrayList());
      mExcludedEntityTypes = new ArraySet(paramParcel.createStringArrayList());
      mIncludedEntityTypes = new ArraySet(paramParcel.createStringArrayList());
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      mUseHints = bool;
    }
    
    private EntityConfig(boolean paramBoolean, Collection<String> paramCollection1, Collection<String> paramCollection2, Collection<String> paramCollection3)
    {
      if (paramCollection1 == null) {
        paramCollection1 = Collections.EMPTY_LIST;
      } else {
        paramCollection1 = Collections.unmodifiableCollection(new ArraySet(paramCollection1));
      }
      mHints = paramCollection1;
      if (paramCollection3 == null) {
        paramCollection1 = Collections.EMPTY_LIST;
      } else {
        paramCollection1 = new ArraySet(paramCollection3);
      }
      mExcludedEntityTypes = paramCollection1;
      if (paramCollection2 == null) {
        paramCollection1 = Collections.EMPTY_LIST;
      } else {
        paramCollection1 = new ArraySet(paramCollection2);
      }
      mIncludedEntityTypes = paramCollection1;
      mUseHints = paramBoolean;
    }
    
    public static EntityConfig create(Collection<String> paramCollection)
    {
      return createWithHints(paramCollection);
    }
    
    public static EntityConfig create(Collection<String> paramCollection1, Collection<String> paramCollection2, Collection<String> paramCollection3)
    {
      return new EntityConfig(true, paramCollection1, paramCollection2, paramCollection3);
    }
    
    public static EntityConfig createWithEntityList(Collection<String> paramCollection)
    {
      return createWithExplicitEntityList(paramCollection);
    }
    
    public static EntityConfig createWithExplicitEntityList(Collection<String> paramCollection)
    {
      return new EntityConfig(false, null, paramCollection, null);
    }
    
    public static EntityConfig createWithHints(Collection<String> paramCollection)
    {
      return new EntityConfig(true, paramCollection, null, null);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public Collection<String> getHints()
    {
      return mHints;
    }
    
    public Collection<String> resolveEntityListModifications(Collection<String> paramCollection)
    {
      HashSet localHashSet = new HashSet();
      if (mUseHints) {
        localHashSet.addAll(paramCollection);
      }
      localHashSet.addAll(mIncludedEntityTypes);
      localHashSet.removeAll(mExcludedEntityTypes);
      return localHashSet;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeStringList(new ArrayList(mHints));
      paramParcel.writeStringList(new ArrayList(mExcludedEntityTypes));
      paramParcel.writeStringList(new ArrayList(mIncludedEntityTypes));
      paramParcel.writeInt(mUseHints);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EntityType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Hints {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TextClassifierType {}
  
  public static final class Utils
  {
    public Utils() {}
    
    private static void addLinks(TextLinks.Builder paramBuilder, String paramString1, String paramString2)
    {
      paramString1 = new SpannableString(paramString1);
      if (Linkify.addLinks(paramString1, linkMask(paramString2)))
      {
        int i = paramString1.length();
        int j = 0;
        URLSpan[] arrayOfURLSpan = (URLSpan[])paramString1.getSpans(0, i, URLSpan.class);
        i = arrayOfURLSpan.length;
        while (j < i)
        {
          URLSpan localURLSpan = arrayOfURLSpan[j];
          paramBuilder.addLink(paramString1.getSpanStart(localURLSpan), paramString1.getSpanEnd(localURLSpan), entityScores(paramString2), localURLSpan);
          j++;
        }
      }
    }
    
    static void checkArgument(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      boolean bool1 = false;
      if (paramCharSequence != null) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Preconditions.checkArgument(bool2);
      if (paramInt1 >= 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Preconditions.checkArgument(bool2);
      if (paramInt2 <= paramCharSequence.length()) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Preconditions.checkArgument(bool2);
      boolean bool2 = bool1;
      if (paramInt2 > paramInt1) {
        bool2 = true;
      }
      Preconditions.checkArgument(bool2);
    }
    
    static void checkMainThread()
    {
      if (Looper.myLooper() == Looper.getMainLooper()) {
        Log.w("androidtc", "TextClassifier called on main thread");
      }
    }
    
    static void checkTextLength(CharSequence paramCharSequence, int paramInt)
    {
      Preconditions.checkArgumentInRange(paramCharSequence.length(), 0, paramInt, "text.length()");
    }
    
    private static Map<String, Float> entityScores(String paramString)
    {
      ArrayMap localArrayMap = new ArrayMap();
      localArrayMap.put(paramString, Float.valueOf(1.0F));
      return localArrayMap;
    }
    
    public static TextLinks generateLegacyLinks(TextLinks.Request paramRequest)
    {
      String str = paramRequest.getText().toString();
      TextLinks.Builder localBuilder = new TextLinks.Builder(str);
      paramRequest = paramRequest.getEntityConfig().resolveEntityListModifications(Collections.emptyList());
      if (paramRequest.contains("url")) {
        addLinks(localBuilder, str, "url");
      }
      if (paramRequest.contains("phone")) {
        addLinks(localBuilder, str, "phone");
      }
      if (paramRequest.contains("email")) {
        addLinks(localBuilder, str, "email");
      }
      return localBuilder.build();
    }
    
    private static int linkMask(String paramString)
    {
      int i = paramString.hashCode();
      if (i != 116079)
      {
        if (i != 96619420)
        {
          if ((i == 106642798) && (paramString.equals("phone")))
          {
            i = 1;
            break label70;
          }
        }
        else if (paramString.equals("email"))
        {
          i = 2;
          break label70;
        }
      }
      else if (paramString.equals("url"))
      {
        i = 0;
        break label70;
      }
      i = -1;
      switch (i)
      {
      default: 
        return 0;
      case 2: 
        return 2;
      case 1: 
        label70:
        return 4;
      }
      return 1;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface WidgetType {}
}
