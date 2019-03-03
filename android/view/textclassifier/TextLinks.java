package android.view.textclassifier;

import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public final class TextLinks
  implements Parcelable
{
  public static final int APPLY_STRATEGY_IGNORE = 0;
  public static final int APPLY_STRATEGY_REPLACE = 1;
  public static final Parcelable.Creator<TextLinks> CREATOR = new Parcelable.Creator()
  {
    public TextLinks createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TextLinks(paramAnonymousParcel, null);
    }
    
    public TextLinks[] newArray(int paramAnonymousInt)
    {
      return new TextLinks[paramAnonymousInt];
    }
  };
  public static final int STATUS_DIFFERENT_TEXT = 3;
  public static final int STATUS_LINKS_APPLIED = 0;
  public static final int STATUS_NO_LINKS_APPLIED = 2;
  public static final int STATUS_NO_LINKS_FOUND = 1;
  private final String mFullText;
  private final List<TextLink> mLinks;
  
  private TextLinks(Parcel paramParcel)
  {
    mFullText = paramParcel.readString();
    mLinks = paramParcel.createTypedArrayList(TextLink.CREATOR);
  }
  
  private TextLinks(String paramString, ArrayList<TextLink> paramArrayList)
  {
    mFullText = paramString;
    mLinks = Collections.unmodifiableList(paramArrayList);
  }
  
  public int apply(Spannable paramSpannable, int paramInt, Function<TextLink, TextLinkSpan> paramFunction)
  {
    Preconditions.checkNotNull(paramSpannable);
    return new TextLinksParams.Builder().setApplyStrategy(paramInt).setSpanFactory(paramFunction).build().apply(paramSpannable, this);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Collection<TextLink> getLinks()
  {
    return mLinks;
  }
  
  public String getText()
  {
    return mFullText;
  }
  
  public String toString()
  {
    return String.format(Locale.US, "TextLinks{fullText=%s, links=%s}", new Object[] { mFullText, mLinks });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mFullText);
    paramParcel.writeTypedList(mLinks);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ApplyStrategy {}
  
  public static final class Builder
  {
    private final String mFullText;
    private final ArrayList<TextLinks.TextLink> mLinks;
    
    public Builder(String paramString)
    {
      mFullText = ((String)Preconditions.checkNotNull(paramString));
      mLinks = new ArrayList();
    }
    
    public Builder addLink(int paramInt1, int paramInt2, Map<String, Float> paramMap)
    {
      mLinks.add(new TextLinks.TextLink(paramInt1, paramInt2, paramMap, null));
      return this;
    }
    
    Builder addLink(int paramInt1, int paramInt2, Map<String, Float> paramMap, URLSpan paramURLSpan)
    {
      mLinks.add(new TextLinks.TextLink(paramInt1, paramInt2, paramMap, paramURLSpan));
      return this;
    }
    
    public TextLinks build()
    {
      return new TextLinks(mFullText, mLinks, null);
    }
    
    public Builder clearTextLinks()
    {
      mLinks.clear();
      return this;
    }
  }
  
  public static final class Options
  {
    private int mApplyStrategy;
    private String mCallingPackageName;
    private LocaleList mDefaultLocales;
    private TextClassifier.EntityConfig mEntityConfig;
    private boolean mLegacyFallback;
    private final TextLinks.Request mRequest;
    private final TextClassificationSessionId mSessionId;
    private Function<TextLinks.TextLink, TextLinks.TextLinkSpan> mSpanFactory;
    
    public Options()
    {
      this(null, null);
    }
    
    private Options(TextClassificationSessionId paramTextClassificationSessionId, TextLinks.Request paramRequest)
    {
      mSessionId = paramTextClassificationSessionId;
      mRequest = paramRequest;
    }
    
    private static void checkValidApplyStrategy(int paramInt)
    {
      if ((paramInt != 0) && (paramInt != 1)) {
        throw new IllegalArgumentException("Invalid apply strategy. See TextLinks.ApplyStrategy for options.");
      }
    }
    
    public static Options from(TextClassificationSessionId paramTextClassificationSessionId, TextLinks.Request paramRequest)
    {
      paramTextClassificationSessionId = new Options(paramTextClassificationSessionId, paramRequest);
      paramTextClassificationSessionId.setDefaultLocales(paramRequest.getDefaultLocales());
      paramTextClassificationSessionId.setEntityConfig(paramRequest.getEntityConfig());
      return paramTextClassificationSessionId;
    }
    
    public static Options fromLinkMask(int paramInt)
    {
      ArrayList localArrayList = new ArrayList();
      if ((paramInt & 0x1) != 0) {
        localArrayList.add("url");
      }
      if ((paramInt & 0x2) != 0) {
        localArrayList.add("email");
      }
      if ((paramInt & 0x4) != 0) {
        localArrayList.add("phone");
      }
      if ((paramInt & 0x8) != 0) {
        localArrayList.add("address");
      }
      return new Options().setEntityConfig(TextClassifier.EntityConfig.createWithEntityList(localArrayList));
    }
    
    public int getApplyStrategy()
    {
      return mApplyStrategy;
    }
    
    public LocaleList getDefaultLocales()
    {
      return mDefaultLocales;
    }
    
    public TextClassifier.EntityConfig getEntityConfig()
    {
      return mEntityConfig;
    }
    
    public TextLinks.Request getRequest()
    {
      return mRequest;
    }
    
    public TextClassificationSessionId getSessionId()
    {
      return mSessionId;
    }
    
    public Function<TextLinks.TextLink, TextLinks.TextLinkSpan> getSpanFactory()
    {
      return mSpanFactory;
    }
    
    public Options setApplyStrategy(int paramInt)
    {
      checkValidApplyStrategy(paramInt);
      mApplyStrategy = paramInt;
      return this;
    }
    
    public Options setDefaultLocales(LocaleList paramLocaleList)
    {
      mDefaultLocales = paramLocaleList;
      return this;
    }
    
    public Options setEntityConfig(TextClassifier.EntityConfig paramEntityConfig)
    {
      mEntityConfig = paramEntityConfig;
      return this;
    }
    
    public Options setSpanFactory(Function<TextLinks.TextLink, TextLinks.TextLinkSpan> paramFunction)
    {
      mSpanFactory = paramFunction;
      return this;
    }
  }
  
  public static final class Request
    implements Parcelable
  {
    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator()
    {
      public TextLinks.Request createFromParcel(Parcel paramAnonymousParcel)
      {
        return new TextLinks.Request(paramAnonymousParcel, null);
      }
      
      public TextLinks.Request[] newArray(int paramAnonymousInt)
      {
        return new TextLinks.Request[paramAnonymousInt];
      }
    };
    private String mCallingPackageName;
    private final LocaleList mDefaultLocales;
    private final TextClassifier.EntityConfig mEntityConfig;
    private final boolean mLegacyFallback;
    private final CharSequence mText;
    
    private Request(Parcel paramParcel)
    {
      mText = paramParcel.readString();
      int i = paramParcel.readInt();
      Object localObject1 = null;
      Object localObject2;
      if (i == 0) {
        localObject2 = null;
      } else {
        localObject2 = (LocaleList)LocaleList.CREATOR.createFromParcel(paramParcel);
      }
      mDefaultLocales = ((LocaleList)localObject2);
      if (paramParcel.readInt() == 0) {
        localObject2 = localObject1;
      } else {
        localObject2 = (TextClassifier.EntityConfig)TextClassifier.EntityConfig.CREATOR.createFromParcel(paramParcel);
      }
      mEntityConfig = ((TextClassifier.EntityConfig)localObject2);
      mLegacyFallback = true;
      mCallingPackageName = paramParcel.readString();
    }
    
    private Request(CharSequence paramCharSequence, LocaleList paramLocaleList, TextClassifier.EntityConfig paramEntityConfig, boolean paramBoolean, String paramString)
    {
      mText = paramCharSequence;
      mDefaultLocales = paramLocaleList;
      mEntityConfig = paramEntityConfig;
      mLegacyFallback = paramBoolean;
      mCallingPackageName = paramString;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String getCallingPackageName()
    {
      return mCallingPackageName;
    }
    
    public LocaleList getDefaultLocales()
    {
      return mDefaultLocales;
    }
    
    public TextClassifier.EntityConfig getEntityConfig()
    {
      return mEntityConfig;
    }
    
    public CharSequence getText()
    {
      return mText;
    }
    
    public boolean isLegacyFallback()
    {
      return mLegacyFallback;
    }
    
    void setCallingPackageName(String paramString)
    {
      mCallingPackageName = paramString;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mText.toString());
      LocaleList localLocaleList = mDefaultLocales;
      int i = 0;
      if (localLocaleList != null) {
        j = 1;
      } else {
        j = 0;
      }
      paramParcel.writeInt(j);
      if (mDefaultLocales != null) {
        mDefaultLocales.writeToParcel(paramParcel, paramInt);
      }
      int j = i;
      if (mEntityConfig != null) {
        j = 1;
      }
      paramParcel.writeInt(j);
      if (mEntityConfig != null) {
        mEntityConfig.writeToParcel(paramParcel, paramInt);
      }
      paramParcel.writeString(mCallingPackageName);
    }
    
    public static final class Builder
    {
      private String mCallingPackageName;
      private LocaleList mDefaultLocales;
      private TextClassifier.EntityConfig mEntityConfig;
      private boolean mLegacyFallback = true;
      private final CharSequence mText;
      
      public Builder(CharSequence paramCharSequence)
      {
        mText = ((CharSequence)Preconditions.checkNotNull(paramCharSequence));
      }
      
      public TextLinks.Request build()
      {
        return new TextLinks.Request(mText, mDefaultLocales, mEntityConfig, mLegacyFallback, mCallingPackageName, null);
      }
      
      public Builder setCallingPackageName(String paramString)
      {
        mCallingPackageName = paramString;
        return this;
      }
      
      public Builder setDefaultLocales(LocaleList paramLocaleList)
      {
        mDefaultLocales = paramLocaleList;
        return this;
      }
      
      public Builder setEntityConfig(TextClassifier.EntityConfig paramEntityConfig)
      {
        mEntityConfig = paramEntityConfig;
        return this;
      }
      
      public Builder setLegacyFallback(boolean paramBoolean)
      {
        mLegacyFallback = paramBoolean;
        return this;
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Status {}
  
  public static final class TextLink
    implements Parcelable
  {
    public static final Parcelable.Creator<TextLink> CREATOR = new Parcelable.Creator()
    {
      public TextLinks.TextLink createFromParcel(Parcel paramAnonymousParcel)
      {
        return new TextLinks.TextLink(paramAnonymousParcel, null);
      }
      
      public TextLinks.TextLink[] newArray(int paramAnonymousInt)
      {
        return new TextLinks.TextLink[paramAnonymousInt];
      }
    };
    private final int mEnd;
    private final EntityConfidence mEntityScores;
    private final int mStart;
    final URLSpan mUrlSpan;
    
    TextLink(int paramInt1, int paramInt2, Map<String, Float> paramMap, URLSpan paramURLSpan)
    {
      Preconditions.checkNotNull(paramMap);
      boolean bool1 = paramMap.isEmpty();
      boolean bool2 = true;
      Preconditions.checkArgument(bool1 ^ true);
      if (paramInt1 > paramInt2) {
        bool2 = false;
      }
      Preconditions.checkArgument(bool2);
      mStart = paramInt1;
      mEnd = paramInt2;
      mEntityScores = new EntityConfidence(paramMap);
      mUrlSpan = paramURLSpan;
    }
    
    private TextLink(Parcel paramParcel)
    {
      mEntityScores = ((EntityConfidence)EntityConfidence.CREATOR.createFromParcel(paramParcel));
      mStart = paramParcel.readInt();
      mEnd = paramParcel.readInt();
      mUrlSpan = null;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public float getConfidenceScore(String paramString)
    {
      return mEntityScores.getConfidenceScore(paramString);
    }
    
    public int getEnd()
    {
      return mEnd;
    }
    
    public String getEntity(int paramInt)
    {
      return (String)mEntityScores.getEntities().get(paramInt);
    }
    
    public int getEntityCount()
    {
      return mEntityScores.getEntities().size();
    }
    
    public int getStart()
    {
      return mStart;
    }
    
    public String toString()
    {
      return String.format(Locale.US, "TextLink{start=%s, end=%s, entityScores=%s, urlSpan=%s}", new Object[] { Integer.valueOf(mStart), Integer.valueOf(mEnd), mEntityScores, mUrlSpan });
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      mEntityScores.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(mStart);
      paramParcel.writeInt(mEnd);
    }
  }
  
  public static class TextLinkSpan
    extends ClickableSpan
  {
    public static final int INVOCATION_METHOD_KEYBOARD = 1;
    public static final int INVOCATION_METHOD_TOUCH = 0;
    public static final int INVOCATION_METHOD_UNSPECIFIED = -1;
    private final TextLinks.TextLink mTextLink;
    
    public TextLinkSpan(TextLinks.TextLink paramTextLink)
    {
      mTextLink = paramTextLink;
    }
    
    public final TextLinks.TextLink getTextLink()
    {
      return mTextLink;
    }
    
    @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
    public final String getUrl()
    {
      if (mTextLink.mUrlSpan != null) {
        return mTextLink.mUrlSpan.getURL();
      }
      return null;
    }
    
    public void onClick(View paramView)
    {
      onClick(paramView, -1);
    }
    
    public final void onClick(View paramView, int paramInt)
    {
      if ((paramView instanceof TextView))
      {
        paramView = (TextView)paramView;
        if (TextClassificationManager.getSettings(paramView.getContext()).isSmartLinkifyEnabled())
        {
          if (paramInt != 0) {
            paramView.handleClick(this);
          } else {
            paramView.requestActionMode(this);
          }
        }
        else if (mTextLink.mUrlSpan != null) {
          mTextLink.mUrlSpan.onClick(paramView);
        } else {
          paramView.handleClick(this);
        }
      }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface InvocationMethod {}
  }
}
