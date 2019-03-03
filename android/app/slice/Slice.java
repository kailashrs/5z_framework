package android.app.slice;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Slice
  implements Parcelable
{
  public static final Parcelable.Creator<Slice> CREATOR = new Parcelable.Creator()
  {
    public Slice createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Slice(paramAnonymousParcel);
    }
    
    public Slice[] newArray(int paramAnonymousInt)
    {
      return new Slice[paramAnonymousInt];
    }
  };
  public static final String EXTRA_RANGE_VALUE = "android.app.slice.extra.RANGE_VALUE";
  @Deprecated
  public static final String EXTRA_SLIDER_VALUE = "android.app.slice.extra.SLIDER_VALUE";
  public static final String EXTRA_TOGGLE_STATE = "android.app.slice.extra.TOGGLE_STATE";
  public static final String HINT_ACTIONS = "actions";
  public static final String HINT_CALLER_NEEDED = "caller_needed";
  public static final String HINT_ERROR = "error";
  public static final String HINT_HORIZONTAL = "horizontal";
  public static final String HINT_KEYWORDS = "keywords";
  public static final String HINT_LARGE = "large";
  public static final String HINT_LAST_UPDATED = "last_updated";
  public static final String HINT_LIST = "list";
  public static final String HINT_LIST_ITEM = "list_item";
  public static final String HINT_NO_TINT = "no_tint";
  public static final String HINT_PARTIAL = "partial";
  public static final String HINT_PERMISSION_REQUEST = "permission_request";
  public static final String HINT_SEE_MORE = "see_more";
  public static final String HINT_SELECTED = "selected";
  public static final String HINT_SHORTCUT = "shortcut";
  public static final String HINT_SUMMARY = "summary";
  public static final String HINT_TITLE = "title";
  public static final String HINT_TOGGLE = "toggle";
  public static final String HINT_TTL = "ttl";
  public static final String SUBTYPE_COLOR = "color";
  public static final String SUBTYPE_CONTENT_DESCRIPTION = "content_description";
  public static final String SUBTYPE_LAYOUT_DIRECTION = "layout_direction";
  public static final String SUBTYPE_MAX = "max";
  public static final String SUBTYPE_MESSAGE = "message";
  public static final String SUBTYPE_MILLIS = "millis";
  public static final String SUBTYPE_PRIORITY = "priority";
  public static final String SUBTYPE_RANGE = "range";
  @Deprecated
  public static final String SUBTYPE_SLIDER = "slider";
  public static final String SUBTYPE_SOURCE = "source";
  public static final String SUBTYPE_TOGGLE = "toggle";
  public static final String SUBTYPE_VALUE = "value";
  private final String[] mHints;
  private final SliceItem[] mItems;
  private SliceSpec mSpec;
  private Uri mUri;
  
  protected Slice(Parcel paramParcel)
  {
    mHints = paramParcel.readStringArray();
    int i = paramParcel.readInt();
    mItems = new SliceItem[i];
    for (int j = 0; j < i; j++) {
      mItems[j] = ((SliceItem)SliceItem.CREATOR.createFromParcel(paramParcel));
    }
    mUri = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    mSpec = ((SliceSpec)paramParcel.readTypedObject(SliceSpec.CREATOR));
  }
  
  Slice(ArrayList<SliceItem> paramArrayList, String[] paramArrayOfString, Uri paramUri, SliceSpec paramSliceSpec)
  {
    mHints = paramArrayOfString;
    mItems = ((SliceItem[])paramArrayList.toArray(new SliceItem[paramArrayList.size()]));
    mUri = paramUri;
    mSpec = paramSliceSpec;
  }
  
  private String toString(String paramString)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    for (int i = 0; i < mItems.length; i++)
    {
      localStringBuilder1.append(paramString);
      if (Objects.equals(mItems[i].getFormat(), "slice"))
      {
        localStringBuilder1.append("slice:\n");
        Slice localSlice = mItems[i].getSlice();
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(paramString);
        localStringBuilder2.append("   ");
        localStringBuilder1.append(localSlice.toString(localStringBuilder2.toString()));
      }
      else if (Objects.equals(mItems[i].getFormat(), "text"))
      {
        localStringBuilder1.append("text: ");
        localStringBuilder1.append(mItems[i].getText());
        localStringBuilder1.append("\n");
      }
      else
      {
        localStringBuilder1.append(mItems[i].getFormat());
        localStringBuilder1.append("\n");
      }
    }
    return localStringBuilder1.toString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<String> getHints()
  {
    return Arrays.asList(mHints);
  }
  
  public List<SliceItem> getItems()
  {
    return Arrays.asList(mItems);
  }
  
  public SliceSpec getSpec()
  {
    return mSpec;
  }
  
  public Uri getUri()
  {
    return mUri;
  }
  
  public boolean hasHint(String paramString)
  {
    return ArrayUtils.contains(mHints, paramString);
  }
  
  public boolean isCallerNeeded()
  {
    return hasHint("caller_needed");
  }
  
  public String toString()
  {
    return toString("");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStringArray(mHints);
    paramParcel.writeInt(mItems.length);
    for (int i = 0; i < mItems.length; i++) {
      mItems[i].writeToParcel(paramParcel, paramInt);
    }
    mUri.writeToParcel(paramParcel, 0);
    paramParcel.writeTypedObject(mSpec, paramInt);
  }
  
  public static class Builder
  {
    private ArrayList<String> mHints = new ArrayList();
    private ArrayList<SliceItem> mItems = new ArrayList();
    private SliceSpec mSpec;
    private final Uri mUri;
    
    public Builder(Builder paramBuilder)
    {
      mUri = mUri.buildUpon().appendPath("_gen").appendPath(String.valueOf(mItems.size())).build();
    }
    
    @Deprecated
    public Builder(Uri paramUri)
    {
      mUri = paramUri;
    }
    
    public Builder(Uri paramUri, SliceSpec paramSliceSpec)
    {
      mUri = paramUri;
      mSpec = paramSliceSpec;
    }
    
    public Builder addAction(PendingIntent paramPendingIntent, Slice paramSlice, String paramString)
    {
      Preconditions.checkNotNull(paramPendingIntent);
      Preconditions.checkNotNull(paramSlice);
      List localList = paramSlice.getHints();
      Slice.access$002(paramSlice, null);
      mItems.add(new SliceItem(paramPendingIntent, paramSlice, "action", paramString, (String[])localList.toArray(new String[localList.size()])));
      return this;
    }
    
    public Builder addBundle(Bundle paramBundle, String paramString, List<String> paramList)
    {
      Preconditions.checkNotNull(paramBundle);
      mItems.add(new SliceItem(paramBundle, "bundle", paramString, paramList));
      return this;
    }
    
    public Builder addHints(List<String> paramList)
    {
      mHints.addAll(paramList);
      return this;
    }
    
    public Builder addIcon(Icon paramIcon, String paramString, List<String> paramList)
    {
      Preconditions.checkNotNull(paramIcon);
      mItems.add(new SliceItem(paramIcon, "image", paramString, paramList));
      return this;
    }
    
    public Builder addInt(int paramInt, String paramString, List<String> paramList)
    {
      mItems.add(new SliceItem(Integer.valueOf(paramInt), "int", paramString, paramList));
      return this;
    }
    
    public Builder addLong(long paramLong, String paramString, List<String> paramList)
    {
      mItems.add(new SliceItem(Long.valueOf(paramLong), "long", paramString, (String[])paramList.toArray(new String[paramList.size()])));
      return this;
    }
    
    public Builder addRemoteInput(RemoteInput paramRemoteInput, String paramString, List<String> paramList)
    {
      Preconditions.checkNotNull(paramRemoteInput);
      mItems.add(new SliceItem(paramRemoteInput, "input", paramString, paramList));
      return this;
    }
    
    public Builder addSubSlice(Slice paramSlice, String paramString)
    {
      Preconditions.checkNotNull(paramSlice);
      mItems.add(new SliceItem(paramSlice, "slice", paramString, (String[])paramSlice.getHints().toArray(new String[paramSlice.getHints().size()])));
      return this;
    }
    
    public Builder addText(CharSequence paramCharSequence, String paramString, List<String> paramList)
    {
      mItems.add(new SliceItem(paramCharSequence, "text", paramString, paramList));
      return this;
    }
    
    @Deprecated
    public Builder addTimestamp(long paramLong, String paramString, List<String> paramList)
    {
      return addLong(paramLong, paramString, paramList);
    }
    
    public Slice build()
    {
      return new Slice(mItems, (String[])mHints.toArray(new String[mHints.size()]), mUri, mSpec);
    }
    
    public Builder setCallerNeeded(boolean paramBoolean)
    {
      if (paramBoolean) {
        mHints.add("caller_needed");
      } else {
        mHints.remove("caller_needed");
      }
      return this;
    }
    
    public Builder setSpec(SliceSpec paramSliceSpec)
    {
      mSpec = paramSliceSpec;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SliceHint {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SliceSubtype {}
}
