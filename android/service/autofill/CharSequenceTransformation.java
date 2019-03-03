package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.util.Pair;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CharSequenceTransformation
  extends InternalTransformation
  implements Transformation, Parcelable
{
  public static final Parcelable.Creator<CharSequenceTransformation> CREATOR = new Parcelable.Creator()
  {
    public CharSequenceTransformation createFromParcel(Parcel paramAnonymousParcel)
    {
      AutofillId[] arrayOfAutofillId = (AutofillId[])paramAnonymousParcel.readParcelableArray(null, AutofillId.class);
      Pattern[] arrayOfPattern = (Pattern[])paramAnonymousParcel.readSerializable();
      String[] arrayOfString = paramAnonymousParcel.createStringArray();
      paramAnonymousParcel = new CharSequenceTransformation.Builder(arrayOfAutofillId[0], arrayOfPattern[0], arrayOfString[0]);
      int i = arrayOfAutofillId.length;
      for (int j = 1; j < i; j++) {
        paramAnonymousParcel.addField(arrayOfAutofillId[j], arrayOfPattern[j], arrayOfString[j]);
      }
      return paramAnonymousParcel.build();
    }
    
    public CharSequenceTransformation[] newArray(int paramAnonymousInt)
    {
      return new CharSequenceTransformation[paramAnonymousInt];
    }
  };
  private static final String TAG = "CharSequenceTransformation";
  private final LinkedHashMap<AutofillId, Pair<Pattern, String>> mFields;
  
  private CharSequenceTransformation(Builder paramBuilder)
  {
    mFields = mFields;
  }
  
  public void apply(ValueFinder paramValueFinder, RemoteViews paramRemoteViews, int paramInt)
    throws Exception
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = mFields.size();
    Object localObject1;
    if (Helper.sDebug)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(i);
      ((StringBuilder)localObject1).append(" multiple fields on id ");
      ((StringBuilder)localObject1).append(paramInt);
      Log.d("CharSequenceTransformation", ((StringBuilder)localObject1).toString());
    }
    Iterator localIterator = mFields.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject2 = (Map.Entry)localIterator.next();
      localObject1 = (AutofillId)((Map.Entry)localObject2).getKey();
      localObject2 = (Pair)((Map.Entry)localObject2).getValue();
      Object localObject3 = paramValueFinder.findByAutofillId((AutofillId)localObject1);
      if (localObject3 == null)
      {
        paramValueFinder = new StringBuilder();
        paramValueFinder.append("No value for id ");
        paramValueFinder.append(localObject1);
        Log.w("CharSequenceTransformation", paramValueFinder.toString());
        return;
      }
      try
      {
        localObject3 = ((Pattern)first).matcher((CharSequence)localObject3);
        if (!((Matcher)localObject3).find())
        {
          if (Helper.sDebug)
          {
            paramValueFinder = new java/lang/StringBuilder;
            paramValueFinder.<init>();
            paramValueFinder.append("match for ");
            paramValueFinder.append(first);
            paramValueFinder.append(" failed on id ");
            paramValueFinder.append(localObject1);
            Log.d("CharSequenceTransformation", paramValueFinder.toString());
          }
          return;
        }
        localStringBuilder.append(((Matcher)localObject3).replaceAll((String)second));
      }
      catch (Exception paramRemoteViews)
      {
        paramValueFinder = new StringBuilder();
        paramValueFinder.append("Cannot apply ");
        paramValueFinder.append(((Pattern)first).pattern());
        paramValueFinder.append("->");
        paramValueFinder.append((String)second);
        paramValueFinder.append(" to field with autofill id");
        paramValueFinder.append(localObject1);
        paramValueFinder.append(": ");
        paramValueFinder.append(paramRemoteViews.getClass());
        Log.w("CharSequenceTransformation", paramValueFinder.toString());
        throw paramRemoteViews;
      }
    }
    paramRemoteViews.setCharSequence(paramInt, "setText", localStringBuilder);
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
    localStringBuilder.append("MultipleViewsCharSequenceTransformation: [fields=");
    localStringBuilder.append(mFields);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = mFields.size();
    AutofillId[] arrayOfAutofillId = new AutofillId[i];
    Pattern[] arrayOfPattern = new Pattern[i];
    String[] arrayOfString = new String[i];
    i = 0;
    Iterator localIterator = mFields.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (Map.Entry)localIterator.next();
      arrayOfAutofillId[i] = ((AutofillId)((Map.Entry)localObject).getKey());
      localObject = (Pair)((Map.Entry)localObject).getValue();
      arrayOfPattern[i] = ((Pattern)first);
      arrayOfString[i] = ((String)second);
      i++;
    }
    paramParcel.writeParcelableArray(arrayOfAutofillId, paramInt);
    paramParcel.writeSerializable(arrayOfPattern);
    paramParcel.writeStringArray(arrayOfString);
  }
  
  public static class Builder
  {
    private boolean mDestroyed;
    private final LinkedHashMap<AutofillId, Pair<Pattern, String>> mFields = new LinkedHashMap();
    
    public Builder(AutofillId paramAutofillId, Pattern paramPattern, String paramString)
    {
      addField(paramAutofillId, paramPattern, paramString);
    }
    
    private void throwIfDestroyed()
    {
      Preconditions.checkState(mDestroyed ^ true, "Already called build()");
    }
    
    public Builder addField(AutofillId paramAutofillId, Pattern paramPattern, String paramString)
    {
      throwIfDestroyed();
      Preconditions.checkNotNull(paramAutofillId);
      Preconditions.checkNotNull(paramPattern);
      Preconditions.checkNotNull(paramString);
      mFields.put(paramAutofillId, new Pair(paramPattern, paramString));
      return this;
    }
    
    public CharSequenceTransformation build()
    {
      throwIfDestroyed();
      mDestroyed = true;
      return new CharSequenceTransformation(this, null);
    }
  }
}
