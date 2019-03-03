package android.view.textclassifier;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.view.View.OnClickListener;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class TextClassification
  implements Parcelable
{
  public static final Parcelable.Creator<TextClassification> CREATOR = new Parcelable.Creator()
  {
    public TextClassification createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TextClassification(paramAnonymousParcel, null);
    }
    
    public TextClassification[] newArray(int paramAnonymousInt)
    {
      return new TextClassification[paramAnonymousInt];
    }
  };
  static final TextClassification EMPTY = new Builder().build();
  private static final String LOG_TAG = "TextClassification";
  private static final int MAX_LEGACY_ICON_SIZE = 192;
  private final List<RemoteAction> mActions;
  private final EntityConfidence mEntityConfidence;
  private final String mId;
  private final Drawable mLegacyIcon;
  private final Intent mLegacyIntent;
  private final String mLegacyLabel;
  private final View.OnClickListener mLegacyOnClickListener;
  private final String mText;
  
  private TextClassification(Parcel paramParcel)
  {
    mText = paramParcel.readString();
    mActions = paramParcel.createTypedArrayList(RemoteAction.CREATOR);
    if (!mActions.isEmpty())
    {
      RemoteAction localRemoteAction = (RemoteAction)mActions.get(0);
      mLegacyIcon = maybeLoadDrawable(localRemoteAction.getIcon());
      mLegacyLabel = localRemoteAction.getTitle().toString();
      mLegacyOnClickListener = createIntentOnClickListener(((RemoteAction)mActions.get(0)).getActionIntent());
    }
    else
    {
      mLegacyIcon = null;
      mLegacyLabel = null;
      mLegacyOnClickListener = null;
    }
    mLegacyIntent = null;
    mEntityConfidence = ((EntityConfidence)EntityConfidence.CREATOR.createFromParcel(paramParcel));
    mId = paramParcel.readString();
  }
  
  private TextClassification(String paramString1, Drawable paramDrawable, String paramString2, Intent paramIntent, View.OnClickListener paramOnClickListener, List<RemoteAction> paramList, Map<String, Float> paramMap, String paramString3)
  {
    mText = paramString1;
    mLegacyIcon = paramDrawable;
    mLegacyLabel = paramString2;
    mLegacyIntent = paramIntent;
    mLegacyOnClickListener = paramOnClickListener;
    mActions = Collections.unmodifiableList(paramList);
    mEntityConfidence = new EntityConfidence(paramMap);
    mId = paramString3;
  }
  
  public static View.OnClickListener createIntentOnClickListener(PendingIntent paramPendingIntent)
  {
    Preconditions.checkNotNull(paramPendingIntent);
    return new _..Lambda.TextClassification.ysasaE5ZkXkkzjVWIJ06GTV92_g(paramPendingIntent);
  }
  
  public static PendingIntent createPendingIntent(Context paramContext, Intent paramIntent, int paramInt)
  {
    switch (getIntentType(paramIntent, paramContext))
    {
    default: 
      return null;
    case 1: 
      return PendingIntent.getService(paramContext, paramInt, paramIntent, 134217728);
    }
    return PendingIntent.getActivity(paramContext, paramInt, paramIntent, 134217728);
  }
  
  private static int getIntentType(Intent paramIntent, Context paramContext)
  {
    boolean bool;
    if (paramContext != null) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool);
    if (paramIntent != null) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool);
    ResolveInfo localResolveInfo = paramContext.getPackageManager().resolveActivity(paramIntent, 0);
    if (localResolveInfo != null)
    {
      if (paramContext.getPackageName().equals(activityInfo.packageName)) {
        return 0;
      }
      if ((activityInfo.exported) && (hasPermission(paramContext, activityInfo.permission))) {
        return 0;
      }
    }
    paramIntent = paramContext.getPackageManager().resolveService(paramIntent, 0);
    if (paramIntent != null)
    {
      if (paramContext.getPackageName().equals(serviceInfo.packageName)) {
        return 1;
      }
      if ((serviceInfo.exported) && (hasPermission(paramContext, serviceInfo.permission))) {
        return 1;
      }
    }
    return -1;
  }
  
  private static boolean hasPermission(Context paramContext, String paramString)
  {
    boolean bool;
    if ((paramString != null) && (paramContext.checkSelfPermission(paramString) != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static Drawable maybeLoadDrawable(Icon paramIcon)
  {
    if (paramIcon == null) {
      return null;
    }
    int i = paramIcon.getType();
    if (i != 1)
    {
      if (i != 3)
      {
        if (i != 5) {
          return null;
        }
        return new AdaptiveIconDrawable(null, new BitmapDrawable(Resources.getSystem(), paramIcon.getBitmap()));
      }
      return new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(paramIcon.getDataBytes(), paramIcon.getDataOffset(), paramIcon.getDataLength()));
    }
    return new BitmapDrawable(Resources.getSystem(), paramIcon.getBitmap());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<RemoteAction> getActions()
  {
    return mActions;
  }
  
  public float getConfidenceScore(String paramString)
  {
    return mEntityConfidence.getConfidenceScore(paramString);
  }
  
  public String getEntity(int paramInt)
  {
    return (String)mEntityConfidence.getEntities().get(paramInt);
  }
  
  public int getEntityCount()
  {
    return mEntityConfidence.getEntities().size();
  }
  
  @Deprecated
  public Drawable getIcon()
  {
    return mLegacyIcon;
  }
  
  public String getId()
  {
    return mId;
  }
  
  @Deprecated
  public Intent getIntent()
  {
    return mLegacyIntent;
  }
  
  @Deprecated
  public CharSequence getLabel()
  {
    return mLegacyLabel;
  }
  
  public View.OnClickListener getOnClickListener()
  {
    return mLegacyOnClickListener;
  }
  
  public String getText()
  {
    return mText;
  }
  
  public String toString()
  {
    return String.format(Locale.US, "TextClassification {text=%s, entities=%s, actions=%s, id=%s}", new Object[] { mText, mEntityConfidence, mActions, mId });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mText);
    paramParcel.writeTypedList(mActions);
    mEntityConfidence.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(mId);
  }
  
  public static final class Builder
  {
    private List<RemoteAction> mActions = new ArrayList();
    private final Map<String, Float> mEntityConfidence = new ArrayMap();
    private String mId;
    private Drawable mLegacyIcon;
    private Intent mLegacyIntent;
    private String mLegacyLabel;
    private View.OnClickListener mLegacyOnClickListener;
    private String mText;
    
    public Builder() {}
    
    public Builder addAction(RemoteAction paramRemoteAction)
    {
      boolean bool;
      if (paramRemoteAction != null) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool);
      mActions.add(paramRemoteAction);
      return this;
    }
    
    public TextClassification build()
    {
      return new TextClassification(mText, mLegacyIcon, mLegacyLabel, mLegacyIntent, mLegacyOnClickListener, mActions, mEntityConfidence, mId, null);
    }
    
    public Builder setEntityType(String paramString, float paramFloat)
    {
      mEntityConfidence.put(paramString, Float.valueOf(paramFloat));
      return this;
    }
    
    @Deprecated
    public Builder setIcon(Drawable paramDrawable)
    {
      mLegacyIcon = paramDrawable;
      return this;
    }
    
    public Builder setId(String paramString)
    {
      mId = paramString;
      return this;
    }
    
    @Deprecated
    public Builder setIntent(Intent paramIntent)
    {
      mLegacyIntent = paramIntent;
      return this;
    }
    
    @Deprecated
    public Builder setLabel(String paramString)
    {
      mLegacyLabel = paramString;
      return this;
    }
    
    @Deprecated
    public Builder setOnClickListener(View.OnClickListener paramOnClickListener)
    {
      mLegacyOnClickListener = paramOnClickListener;
      return this;
    }
    
    public Builder setText(String paramString)
    {
      mText = paramString;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface IntentType
  {
    public static final int ACTIVITY = 0;
    public static final int SERVICE = 1;
    public static final int UNSUPPORTED = -1;
  }
  
  public static final class Options
  {
    private LocaleList mDefaultLocales;
    private ZonedDateTime mReferenceTime;
    private final TextClassification.Request mRequest;
    private final TextClassificationSessionId mSessionId;
    
    public Options()
    {
      this(null, null);
    }
    
    private Options(TextClassificationSessionId paramTextClassificationSessionId, TextClassification.Request paramRequest)
    {
      mSessionId = paramTextClassificationSessionId;
      mRequest = paramRequest;
    }
    
    public static Options from(TextClassificationSessionId paramTextClassificationSessionId, TextClassification.Request paramRequest)
    {
      paramTextClassificationSessionId = new Options(paramTextClassificationSessionId, paramRequest);
      paramTextClassificationSessionId.setDefaultLocales(paramRequest.getDefaultLocales());
      paramTextClassificationSessionId.setReferenceTime(paramRequest.getReferenceTime());
      return paramTextClassificationSessionId;
    }
    
    public LocaleList getDefaultLocales()
    {
      return mDefaultLocales;
    }
    
    public ZonedDateTime getReferenceTime()
    {
      return mReferenceTime;
    }
    
    public TextClassification.Request getRequest()
    {
      return mRequest;
    }
    
    public TextClassificationSessionId getSessionId()
    {
      return mSessionId;
    }
    
    public Options setDefaultLocales(LocaleList paramLocaleList)
    {
      mDefaultLocales = paramLocaleList;
      return this;
    }
    
    public Options setReferenceTime(ZonedDateTime paramZonedDateTime)
    {
      mReferenceTime = paramZonedDateTime;
      return this;
    }
  }
  
  public static final class Request
    implements Parcelable
  {
    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator()
    {
      public TextClassification.Request createFromParcel(Parcel paramAnonymousParcel)
      {
        return new TextClassification.Request(paramAnonymousParcel, null);
      }
      
      public TextClassification.Request[] newArray(int paramAnonymousInt)
      {
        return new TextClassification.Request[paramAnonymousInt];
      }
    };
    private final LocaleList mDefaultLocales;
    private final int mEndIndex;
    private final ZonedDateTime mReferenceTime;
    private final int mStartIndex;
    private final CharSequence mText;
    
    private Request(Parcel paramParcel)
    {
      mText = paramParcel.readString();
      mStartIndex = paramParcel.readInt();
      mEndIndex = paramParcel.readInt();
      int i = paramParcel.readInt();
      Object localObject = null;
      LocaleList localLocaleList;
      if (i == 0) {
        localLocaleList = null;
      } else {
        localLocaleList = (LocaleList)LocaleList.CREATOR.createFromParcel(paramParcel);
      }
      mDefaultLocales = localLocaleList;
      if (paramParcel.readInt() == 0) {
        paramParcel = localObject;
      } else {
        paramParcel = ZonedDateTime.parse(paramParcel.readString());
      }
      mReferenceTime = paramParcel;
    }
    
    private Request(CharSequence paramCharSequence, int paramInt1, int paramInt2, LocaleList paramLocaleList, ZonedDateTime paramZonedDateTime)
    {
      mText = paramCharSequence;
      mStartIndex = paramInt1;
      mEndIndex = paramInt2;
      mDefaultLocales = paramLocaleList;
      mReferenceTime = paramZonedDateTime;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public LocaleList getDefaultLocales()
    {
      return mDefaultLocales;
    }
    
    public int getEndIndex()
    {
      return mEndIndex;
    }
    
    public ZonedDateTime getReferenceTime()
    {
      return mReferenceTime;
    }
    
    public int getStartIndex()
    {
      return mStartIndex;
    }
    
    public CharSequence getText()
    {
      return mText;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mText.toString());
      paramParcel.writeInt(mStartIndex);
      paramParcel.writeInt(mEndIndex);
      LocaleList localLocaleList = mDefaultLocales;
      int i = 0;
      int j;
      if (localLocaleList != null) {
        j = 1;
      } else {
        j = 0;
      }
      paramParcel.writeInt(j);
      if (mDefaultLocales != null) {
        mDefaultLocales.writeToParcel(paramParcel, paramInt);
      }
      paramInt = i;
      if (mReferenceTime != null) {
        paramInt = 1;
      }
      paramParcel.writeInt(paramInt);
      if (mReferenceTime != null) {
        paramParcel.writeString(mReferenceTime.toString());
      }
    }
    
    public static final class Builder
    {
      private LocaleList mDefaultLocales;
      private final int mEndIndex;
      private ZonedDateTime mReferenceTime;
      private final int mStartIndex;
      private final CharSequence mText;
      
      public Builder(CharSequence paramCharSequence, int paramInt1, int paramInt2)
      {
        TextClassifier.Utils.checkArgument(paramCharSequence, paramInt1, paramInt2);
        mText = paramCharSequence;
        mStartIndex = paramInt1;
        mEndIndex = paramInt2;
      }
      
      public TextClassification.Request build()
      {
        return new TextClassification.Request(mText, mStartIndex, mEndIndex, mDefaultLocales, mReferenceTime, null);
      }
      
      public Builder setDefaultLocales(LocaleList paramLocaleList)
      {
        mDefaultLocales = paramLocaleList;
        return this;
      }
      
      public Builder setReferenceTime(ZonedDateTime paramZonedDateTime)
      {
        mReferenceTime = paramZonedDateTime;
        return this;
      }
    }
  }
}
