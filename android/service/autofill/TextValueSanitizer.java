package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Slog;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextValueSanitizer
  extends InternalSanitizer
  implements Sanitizer, Parcelable
{
  public static final Parcelable.Creator<TextValueSanitizer> CREATOR = new Parcelable.Creator()
  {
    public TextValueSanitizer createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TextValueSanitizer((Pattern)paramAnonymousParcel.readSerializable(), paramAnonymousParcel.readString());
    }
    
    public TextValueSanitizer[] newArray(int paramAnonymousInt)
    {
      return new TextValueSanitizer[paramAnonymousInt];
    }
  };
  private static final String TAG = "TextValueSanitizer";
  private final Pattern mRegex;
  private final String mSubst;
  
  public TextValueSanitizer(Pattern paramPattern, String paramString)
  {
    mRegex = ((Pattern)Preconditions.checkNotNull(paramPattern));
    mSubst = ((String)Preconditions.checkNotNull(paramString));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public AutofillValue sanitize(AutofillValue paramAutofillValue)
  {
    if (paramAutofillValue == null)
    {
      Slog.w("TextValueSanitizer", "sanitize() called with null value");
      return null;
    }
    if (!paramAutofillValue.isText())
    {
      if (Helper.sDebug)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("sanitize() called with non-text value: ");
        ((StringBuilder)localObject).append(paramAutofillValue);
        Slog.d("TextValueSanitizer", ((StringBuilder)localObject).toString());
      }
      return null;
    }
    Object localObject = paramAutofillValue.getTextValue();
    try
    {
      localObject = mRegex.matcher((CharSequence)localObject);
      if (!((Matcher)localObject).matches())
      {
        if (Helper.sDebug)
        {
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("sanitize(): ");
          ((StringBuilder)localObject).append(mRegex);
          ((StringBuilder)localObject).append(" failed for ");
          ((StringBuilder)localObject).append(paramAutofillValue);
          Slog.d("TextValueSanitizer", ((StringBuilder)localObject).toString());
        }
        return null;
      }
      paramAutofillValue = AutofillValue.forText(((Matcher)localObject).replaceAll(mSubst));
      return paramAutofillValue;
    }
    catch (Exception paramAutofillValue)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Exception evaluating ");
      ((StringBuilder)localObject).append(mRegex);
      ((StringBuilder)localObject).append("/");
      ((StringBuilder)localObject).append(mSubst);
      ((StringBuilder)localObject).append(": ");
      ((StringBuilder)localObject).append(paramAutofillValue);
      Slog.w("TextValueSanitizer", ((StringBuilder)localObject).toString());
    }
    return null;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("TextValueSanitizer: [regex=");
    localStringBuilder.append(mRegex);
    localStringBuilder.append(", subst=");
    localStringBuilder.append(mSubst);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeSerializable(mRegex);
    paramParcel.writeString(mSubst);
  }
}
