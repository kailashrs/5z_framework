package android.view.textclassifier;

import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import com.android.internal.util.Preconditions;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class TextSelection
  implements Parcelable
{
  public static final Parcelable.Creator<TextSelection> CREATOR = new Parcelable.Creator()
  {
    public TextSelection createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TextSelection(paramAnonymousParcel, null);
    }
    
    public TextSelection[] newArray(int paramAnonymousInt)
    {
      return new TextSelection[paramAnonymousInt];
    }
  };
  private final int mEndIndex;
  private final EntityConfidence mEntityConfidence;
  private final String mId;
  private final int mStartIndex;
  
  private TextSelection(int paramInt1, int paramInt2, Map<String, Float> paramMap, String paramString)
  {
    mStartIndex = paramInt1;
    mEndIndex = paramInt2;
    mEntityConfidence = new EntityConfidence(paramMap);
    mId = paramString;
  }
  
  private TextSelection(Parcel paramParcel)
  {
    mStartIndex = paramParcel.readInt();
    mEndIndex = paramParcel.readInt();
    mEntityConfidence = ((EntityConfidence)EntityConfidence.CREATOR.createFromParcel(paramParcel));
    mId = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
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
  
  public String getId()
  {
    return mId;
  }
  
  public int getSelectionEndIndex()
  {
    return mEndIndex;
  }
  
  public int getSelectionStartIndex()
  {
    return mStartIndex;
  }
  
  public String toString()
  {
    return String.format(Locale.US, "TextSelection {id=%s, startIndex=%d, endIndex=%d, entities=%s}", new Object[] { mId, Integer.valueOf(mStartIndex), Integer.valueOf(mEndIndex), mEntityConfidence });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mStartIndex);
    paramParcel.writeInt(mEndIndex);
    mEntityConfidence.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(mId);
  }
  
  public static final class Builder
  {
    private final int mEndIndex;
    private final Map<String, Float> mEntityConfidence = new ArrayMap();
    private String mId;
    private final int mStartIndex;
    
    public Builder(int paramInt1, int paramInt2)
    {
      boolean bool1 = false;
      if (paramInt1 >= 0) {
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
      mStartIndex = paramInt1;
      mEndIndex = paramInt2;
    }
    
    public TextSelection build()
    {
      return new TextSelection(mStartIndex, mEndIndex, mEntityConfidence, mId, null);
    }
    
    public Builder setEntityType(String paramString, float paramFloat)
    {
      Preconditions.checkNotNull(paramString);
      mEntityConfidence.put(paramString, Float.valueOf(paramFloat));
      return this;
    }
    
    public Builder setId(String paramString)
    {
      mId = paramString;
      return this;
    }
  }
  
  public static final class Options
  {
    private boolean mDarkLaunchAllowed;
    private LocaleList mDefaultLocales;
    private final TextSelection.Request mRequest;
    private final TextClassificationSessionId mSessionId;
    
    public Options()
    {
      this(null, null);
    }
    
    private Options(TextClassificationSessionId paramTextClassificationSessionId, TextSelection.Request paramRequest)
    {
      mSessionId = paramTextClassificationSessionId;
      mRequest = paramRequest;
    }
    
    public static Options from(TextClassificationSessionId paramTextClassificationSessionId, TextSelection.Request paramRequest)
    {
      paramTextClassificationSessionId = new Options(paramTextClassificationSessionId, paramRequest);
      paramTextClassificationSessionId.setDefaultLocales(paramRequest.getDefaultLocales());
      return paramTextClassificationSessionId;
    }
    
    public LocaleList getDefaultLocales()
    {
      return mDefaultLocales;
    }
    
    public TextSelection.Request getRequest()
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
  }
  
  public static final class Request
    implements Parcelable
  {
    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator()
    {
      public TextSelection.Request createFromParcel(Parcel paramAnonymousParcel)
      {
        return new TextSelection.Request(paramAnonymousParcel, null);
      }
      
      public TextSelection.Request[] newArray(int paramAnonymousInt)
      {
        return new TextSelection.Request[paramAnonymousInt];
      }
    };
    private final boolean mDarkLaunchAllowed;
    private final LocaleList mDefaultLocales;
    private final int mEndIndex;
    private final int mStartIndex;
    private final CharSequence mText;
    
    private Request(Parcel paramParcel)
    {
      mText = paramParcel.readString();
      mStartIndex = paramParcel.readInt();
      mEndIndex = paramParcel.readInt();
      if (paramParcel.readInt() == 0) {
        paramParcel = null;
      } else {
        paramParcel = (LocaleList)LocaleList.CREATOR.createFromParcel(paramParcel);
      }
      mDefaultLocales = paramParcel;
      mDarkLaunchAllowed = false;
    }
    
    private Request(CharSequence paramCharSequence, int paramInt1, int paramInt2, LocaleList paramLocaleList, boolean paramBoolean)
    {
      mText = paramCharSequence;
      mStartIndex = paramInt1;
      mEndIndex = paramInt2;
      mDefaultLocales = paramLocaleList;
      mDarkLaunchAllowed = paramBoolean;
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
    
    public int getStartIndex()
    {
      return mStartIndex;
    }
    
    public CharSequence getText()
    {
      return mText;
    }
    
    public boolean isDarkLaunchAllowed()
    {
      return mDarkLaunchAllowed;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mText.toString());
      paramParcel.writeInt(mStartIndex);
      paramParcel.writeInt(mEndIndex);
      int i;
      if (mDefaultLocales != null) {
        i = 1;
      } else {
        i = 0;
      }
      paramParcel.writeInt(i);
      if (mDefaultLocales != null) {
        mDefaultLocales.writeToParcel(paramParcel, paramInt);
      }
    }
    
    public static final class Builder
    {
      private boolean mDarkLaunchAllowed;
      private LocaleList mDefaultLocales;
      private final int mEndIndex;
      private final int mStartIndex;
      private final CharSequence mText;
      
      public Builder(CharSequence paramCharSequence, int paramInt1, int paramInt2)
      {
        TextClassifier.Utils.checkArgument(paramCharSequence, paramInt1, paramInt2);
        mText = paramCharSequence;
        mStartIndex = paramInt1;
        mEndIndex = paramInt2;
      }
      
      public TextSelection.Request build()
      {
        return new TextSelection.Request(mText, mStartIndex, mEndIndex, mDefaultLocales, mDarkLaunchAllowed, null);
      }
      
      public Builder setDarkLaunchAllowed(boolean paramBoolean)
      {
        mDarkLaunchAllowed = paramBoolean;
        return this;
      }
      
      public Builder setDefaultLocales(LocaleList paramLocaleList)
      {
        mDefaultLocales = paramLocaleList;
        return this;
      }
    }
  }
}
