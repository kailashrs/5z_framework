package android.app;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArraySet;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class RemoteInput
  implements Parcelable
{
  public static final Parcelable.Creator<RemoteInput> CREATOR = new Parcelable.Creator()
  {
    public RemoteInput createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RemoteInput(paramAnonymousParcel, null);
    }
    
    public RemoteInput[] newArray(int paramAnonymousInt)
    {
      return new RemoteInput[paramAnonymousInt];
    }
  };
  private static final int DEFAULT_FLAGS = 1;
  private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
  public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
  private static final String EXTRA_RESULTS_SOURCE = "android.remoteinput.resultsSource";
  private static final int FLAG_ALLOW_FREE_FORM_INPUT = 1;
  public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
  public static final int SOURCE_CHOICE = 1;
  public static final int SOURCE_FREE_FORM_INPUT = 0;
  private final ArraySet<String> mAllowedDataTypes;
  private final CharSequence[] mChoices;
  private final Bundle mExtras;
  private final int mFlags;
  private final CharSequence mLabel;
  private final String mResultKey;
  
  private RemoteInput(Parcel paramParcel)
  {
    mResultKey = paramParcel.readString();
    mLabel = paramParcel.readCharSequence();
    mChoices = paramParcel.readCharSequenceArray();
    mFlags = paramParcel.readInt();
    mExtras = paramParcel.readBundle();
    mAllowedDataTypes = paramParcel.readArraySet(null);
  }
  
  private RemoteInput(String paramString, CharSequence paramCharSequence, CharSequence[] paramArrayOfCharSequence, int paramInt, Bundle paramBundle, ArraySet<String> paramArraySet)
  {
    mResultKey = paramString;
    mLabel = paramCharSequence;
    mChoices = paramArrayOfCharSequence;
    mFlags = paramInt;
    mExtras = paramBundle;
    mAllowedDataTypes = paramArraySet;
  }
  
  public static void addDataResultToIntent(RemoteInput paramRemoteInput, Intent paramIntent, Map<String, Uri> paramMap)
  {
    Object localObject1 = getClipDataIntentFromIntent(paramIntent);
    Object localObject2 = localObject1;
    if (localObject1 == null) {
      localObject2 = new Intent();
    }
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      paramMap = (Map.Entry)localIterator.next();
      String str = (String)paramMap.getKey();
      Uri localUri = (Uri)paramMap.getValue();
      if (str != null)
      {
        localObject1 = ((Intent)localObject2).getBundleExtra(getExtraResultsKeyForData(str));
        paramMap = (Map<String, Uri>)localObject1;
        if (localObject1 == null) {
          paramMap = new Bundle();
        }
        paramMap.putString(paramRemoteInput.getResultKey(), localUri.toString());
        ((Intent)localObject2).putExtra(getExtraResultsKeyForData(str), paramMap);
      }
    }
    paramIntent.setClipData(ClipData.newIntent("android.remoteinput.results", (Intent)localObject2));
  }
  
  public static void addResultsToIntent(RemoteInput[] paramArrayOfRemoteInput, Intent paramIntent, Bundle paramBundle)
  {
    Object localObject1 = getClipDataIntentFromIntent(paramIntent);
    Object localObject2 = localObject1;
    if (localObject1 == null) {
      localObject2 = new Intent();
    }
    Object localObject3 = ((Intent)localObject2).getBundleExtra("android.remoteinput.resultsData");
    localObject1 = localObject3;
    if (localObject3 == null) {
      localObject1 = new Bundle();
    }
    int i = paramArrayOfRemoteInput.length;
    for (int j = 0; j < i; j++)
    {
      localObject3 = paramArrayOfRemoteInput[j];
      Object localObject4 = paramBundle.get(((RemoteInput)localObject3).getResultKey());
      if ((localObject4 instanceof CharSequence)) {
        ((Bundle)localObject1).putCharSequence(((RemoteInput)localObject3).getResultKey(), (CharSequence)localObject4);
      }
    }
    ((Intent)localObject2).putExtra("android.remoteinput.resultsData", (Bundle)localObject1);
    paramIntent.setClipData(ClipData.newIntent("android.remoteinput.results", (Intent)localObject2));
  }
  
  private static Intent getClipDataIntentFromIntent(Intent paramIntent)
  {
    ClipData localClipData = paramIntent.getClipData();
    if (localClipData == null) {
      return null;
    }
    paramIntent = localClipData.getDescription();
    if (!paramIntent.hasMimeType("text/vnd.android.intent")) {
      return null;
    }
    if (!paramIntent.getLabel().equals("android.remoteinput.results")) {
      return null;
    }
    return localClipData.getItemAt(0).getIntent();
  }
  
  public static Map<String, Uri> getDataResultsFromIntent(Intent paramIntent, String paramString)
  {
    Intent localIntent = getClipDataIntentFromIntent(paramIntent);
    Object localObject = null;
    if (localIntent == null) {
      return null;
    }
    paramIntent = new HashMap();
    Iterator localIterator = localIntent.getExtras().keySet().iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      if (str1.startsWith("android.remoteinput.dataTypeResultsData"))
      {
        String str2 = str1.substring("android.remoteinput.dataTypeResultsData".length());
        if ((str2 != null) && (!str2.isEmpty()))
        {
          str1 = localIntent.getBundleExtra(str1).getString(paramString);
          if ((str1 != null) && (!str1.isEmpty())) {
            paramIntent.put(str2, Uri.parse(str1));
          }
        }
      }
    }
    if (paramIntent.isEmpty()) {
      paramIntent = localObject;
    }
    return paramIntent;
  }
  
  private static String getExtraResultsKeyForData(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("android.remoteinput.dataTypeResultsData");
    localStringBuilder.append(paramString);
    return localStringBuilder.toString();
  }
  
  public static Bundle getResultsFromIntent(Intent paramIntent)
  {
    paramIntent = getClipDataIntentFromIntent(paramIntent);
    if (paramIntent == null) {
      return null;
    }
    return (Bundle)paramIntent.getExtras().getParcelable("android.remoteinput.resultsData");
  }
  
  public static int getResultsSource(Intent paramIntent)
  {
    paramIntent = getClipDataIntentFromIntent(paramIntent);
    if (paramIntent == null) {
      return 0;
    }
    return paramIntent.getExtras().getInt("android.remoteinput.resultsSource", 0);
  }
  
  public static void setResultsSource(Intent paramIntent, int paramInt)
  {
    Intent localIntent1 = getClipDataIntentFromIntent(paramIntent);
    Intent localIntent2 = localIntent1;
    if (localIntent1 == null) {
      localIntent2 = new Intent();
    }
    localIntent2.putExtra("android.remoteinput.resultsSource", paramInt);
    paramIntent.setClipData(ClipData.newIntent("android.remoteinput.results", localIntent2));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean getAllowFreeFormInput()
  {
    int i = mFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public Set<String> getAllowedDataTypes()
  {
    return mAllowedDataTypes;
  }
  
  public CharSequence[] getChoices()
  {
    return mChoices;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public CharSequence getLabel()
  {
    return mLabel;
  }
  
  public String getResultKey()
  {
    return mResultKey;
  }
  
  public boolean isDataOnly()
  {
    boolean bool;
    if ((!getAllowFreeFormInput()) && ((getChoices() == null) || (getChoices().length == 0)) && (!getAllowedDataTypes().isEmpty())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mResultKey);
    paramParcel.writeCharSequence(mLabel);
    paramParcel.writeCharSequenceArray(mChoices);
    paramParcel.writeInt(mFlags);
    paramParcel.writeBundle(mExtras);
    paramParcel.writeArraySet(mAllowedDataTypes);
  }
  
  public static final class Builder
  {
    private final ArraySet<String> mAllowedDataTypes = new ArraySet();
    private CharSequence[] mChoices;
    private final Bundle mExtras = new Bundle();
    private int mFlags = 1;
    private CharSequence mLabel;
    private final String mResultKey;
    
    public Builder(String paramString)
    {
      if (paramString != null)
      {
        mResultKey = paramString;
        return;
      }
      throw new IllegalArgumentException("Result key can't be null");
    }
    
    private void setFlag(int paramInt, boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= paramInt;
      } else {
        mFlags &= paramInt;
      }
    }
    
    public Builder addExtras(Bundle paramBundle)
    {
      if (paramBundle != null) {
        mExtras.putAll(paramBundle);
      }
      return this;
    }
    
    public RemoteInput build()
    {
      return new RemoteInput(mResultKey, mLabel, mChoices, mFlags, mExtras, mAllowedDataTypes, null);
    }
    
    public Bundle getExtras()
    {
      return mExtras;
    }
    
    public Builder setAllowDataType(String paramString, boolean paramBoolean)
    {
      if (paramBoolean) {
        mAllowedDataTypes.add(paramString);
      } else {
        mAllowedDataTypes.remove(paramString);
      }
      return this;
    }
    
    public Builder setAllowFreeFormInput(boolean paramBoolean)
    {
      setFlag(mFlags, paramBoolean);
      return this;
    }
    
    public Builder setChoices(CharSequence[] paramArrayOfCharSequence)
    {
      if (paramArrayOfCharSequence == null)
      {
        mChoices = null;
      }
      else
      {
        mChoices = new CharSequence[paramArrayOfCharSequence.length];
        for (int i = 0; i < paramArrayOfCharSequence.length; i++) {
          mChoices[i] = Notification.safeCharSequence(paramArrayOfCharSequence[i]);
        }
      }
      return this;
    }
    
    public Builder setLabel(CharSequence paramCharSequence)
    {
      mLabel = Notification.safeCharSequence(paramCharSequence);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Source {}
}
