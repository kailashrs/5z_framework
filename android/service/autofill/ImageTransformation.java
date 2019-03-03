package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImageTransformation
  extends InternalTransformation
  implements Transformation, Parcelable
{
  public static final Parcelable.Creator<ImageTransformation> CREATOR = new Parcelable.Creator()
  {
    public ImageTransformation createFromParcel(Parcel paramAnonymousParcel)
    {
      AutofillId localAutofillId = (AutofillId)paramAnonymousParcel.readParcelable(null);
      Pattern[] arrayOfPattern = (Pattern[])paramAnonymousParcel.readSerializable();
      int[] arrayOfInt = paramAnonymousParcel.createIntArray();
      CharSequence[] arrayOfCharSequence = paramAnonymousParcel.readCharSequenceArray();
      paramAnonymousParcel = arrayOfCharSequence[0];
      if (paramAnonymousParcel != null) {
        paramAnonymousParcel = new ImageTransformation.Builder(localAutofillId, arrayOfPattern[0], arrayOfInt[0], paramAnonymousParcel);
      } else {
        paramAnonymousParcel = new ImageTransformation.Builder(localAutofillId, arrayOfPattern[0], arrayOfInt[0]);
      }
      int i = arrayOfPattern.length;
      for (int j = 1; j < i; j++) {
        if (arrayOfCharSequence[j] != null) {
          paramAnonymousParcel.addOption(arrayOfPattern[j], arrayOfInt[j], arrayOfCharSequence[j]);
        } else {
          paramAnonymousParcel.addOption(arrayOfPattern[j], arrayOfInt[j]);
        }
      }
      return paramAnonymousParcel.build();
    }
    
    public ImageTransformation[] newArray(int paramAnonymousInt)
    {
      return new ImageTransformation[paramAnonymousInt];
    }
  };
  private static final String TAG = "ImageTransformation";
  private final AutofillId mId;
  private final ArrayList<Option> mOptions;
  
  private ImageTransformation(Builder paramBuilder)
  {
    mId = mId;
    mOptions = mOptions;
  }
  
  public void apply(ValueFinder paramValueFinder, RemoteViews paramRemoteViews, int paramInt)
    throws Exception
  {
    Object localObject = paramValueFinder.findByAutofillId(mId);
    if (localObject == null)
    {
      paramValueFinder = new StringBuilder();
      paramValueFinder.append("No view for id ");
      paramValueFinder.append(mId);
      Log.w("ImageTransformation", paramValueFinder.toString());
      return;
    }
    int i = mOptions.size();
    if (Helper.sDebug)
    {
      paramValueFinder = new StringBuilder();
      paramValueFinder.append(i);
      paramValueFinder.append(" multiple options on id ");
      paramValueFinder.append(paramInt);
      paramValueFinder.append(" to compare against");
      Log.d("ImageTransformation", paramValueFinder.toString());
    }
    int j = 0;
    while (j < i)
    {
      paramValueFinder = (Option)mOptions.get(j);
      try
      {
        if (pattern.matcher((CharSequence)localObject).matches())
        {
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("Found match at ");
          ((StringBuilder)localObject).append(j);
          ((StringBuilder)localObject).append(": ");
          ((StringBuilder)localObject).append(paramValueFinder);
          Log.d("ImageTransformation", ((StringBuilder)localObject).toString());
          paramRemoteViews.setImageViewResource(paramInt, resId);
          if (contentDescription != null) {
            paramRemoteViews.setContentDescription(paramInt, contentDescription);
          }
          return;
        }
        j++;
      }
      catch (Exception paramRemoteViews)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Error matching regex #");
        ((StringBuilder)localObject).append(j);
        ((StringBuilder)localObject).append("(");
        ((StringBuilder)localObject).append(pattern);
        ((StringBuilder)localObject).append(") on id ");
        ((StringBuilder)localObject).append(resId);
        ((StringBuilder)localObject).append(": ");
        ((StringBuilder)localObject).append(paramRemoteViews.getClass());
        Log.w("ImageTransformation", ((StringBuilder)localObject).toString());
        throw paramRemoteViews;
      }
    }
    if (Helper.sDebug)
    {
      paramValueFinder = new StringBuilder();
      paramValueFinder.append("No match for ");
      paramValueFinder.append((String)localObject);
      Log.d("ImageTransformation", paramValueFinder.toString());
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ImageTransformation: [id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", options=");
    localStringBuilder.append(mOptions);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mId, paramInt);
    int i = mOptions.size();
    Pattern[] arrayOfPattern = new Pattern[i];
    int[] arrayOfInt = new int[i];
    String[] arrayOfString = new String[i];
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      Option localOption = (Option)mOptions.get(paramInt);
      arrayOfPattern[paramInt] = pattern;
      arrayOfInt[paramInt] = resId;
      arrayOfString[paramInt] = contentDescription;
    }
    paramParcel.writeSerializable(arrayOfPattern);
    paramParcel.writeIntArray(arrayOfInt);
    paramParcel.writeCharSequenceArray(arrayOfString);
  }
  
  public static class Builder
  {
    private boolean mDestroyed;
    private final AutofillId mId;
    private final ArrayList<ImageTransformation.Option> mOptions = new ArrayList();
    
    @Deprecated
    public Builder(AutofillId paramAutofillId, Pattern paramPattern, int paramInt)
    {
      mId = ((AutofillId)Preconditions.checkNotNull(paramAutofillId));
      addOption(paramPattern, paramInt);
    }
    
    public Builder(AutofillId paramAutofillId, Pattern paramPattern, int paramInt, CharSequence paramCharSequence)
    {
      mId = ((AutofillId)Preconditions.checkNotNull(paramAutofillId));
      addOption(paramPattern, paramInt, paramCharSequence);
    }
    
    private void addOptionInternal(Pattern paramPattern, int paramInt, CharSequence paramCharSequence)
    {
      throwIfDestroyed();
      Preconditions.checkNotNull(paramPattern);
      boolean bool;
      if (paramInt != 0) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool);
      mOptions.add(new ImageTransformation.Option(paramPattern, paramInt, paramCharSequence));
    }
    
    private void throwIfDestroyed()
    {
      Preconditions.checkState(mDestroyed ^ true, "Already called build()");
    }
    
    @Deprecated
    public Builder addOption(Pattern paramPattern, int paramInt)
    {
      addOptionInternal(paramPattern, paramInt, null);
      return this;
    }
    
    public Builder addOption(Pattern paramPattern, int paramInt, CharSequence paramCharSequence)
    {
      addOptionInternal(paramPattern, paramInt, (CharSequence)Preconditions.checkNotNull(paramCharSequence));
      return this;
    }
    
    public ImageTransformation build()
    {
      throwIfDestroyed();
      mDestroyed = true;
      return new ImageTransformation(this, null);
    }
  }
  
  private static final class Option
  {
    public final CharSequence contentDescription;
    public final Pattern pattern;
    public final int resId;
    
    Option(Pattern paramPattern, int paramInt, CharSequence paramCharSequence)
    {
      pattern = paramPattern;
      resId = paramInt;
      contentDescription = TextUtils.trimNoCopySpans(paramCharSequence);
    }
  }
}
