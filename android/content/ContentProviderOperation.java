package android.content;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ContentProviderOperation
  implements Parcelable
{
  public static final Parcelable.Creator<ContentProviderOperation> CREATOR = new Parcelable.Creator()
  {
    public ContentProviderOperation createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ContentProviderOperation(paramAnonymousParcel, null);
    }
    
    public ContentProviderOperation[] newArray(int paramAnonymousInt)
    {
      return new ContentProviderOperation[paramAnonymousInt];
    }
  };
  private static final String TAG = "ContentProviderOperation";
  public static final int TYPE_ASSERT = 4;
  public static final int TYPE_DELETE = 3;
  public static final int TYPE_INSERT = 1;
  public static final int TYPE_UPDATE = 2;
  private final Integer mExpectedCount;
  private final String mSelection;
  private final String[] mSelectionArgs;
  private final Map<Integer, Integer> mSelectionArgsBackReferences;
  private final int mType;
  private final Uri mUri;
  private final ContentValues mValues;
  private final ContentValues mValuesBackReferences;
  private final boolean mYieldAllowed;
  
  private ContentProviderOperation(Builder paramBuilder)
  {
    mType = mType;
    mUri = mUri;
    mValues = mValues;
    mSelection = mSelection;
    mSelectionArgs = mSelectionArgs;
    mExpectedCount = mExpectedCount;
    mSelectionArgsBackReferences = mSelectionArgsBackReferences;
    mValuesBackReferences = mValuesBackReferences;
    mYieldAllowed = mYieldAllowed;
  }
  
  public ContentProviderOperation(ContentProviderOperation paramContentProviderOperation, Uri paramUri)
  {
    mType = mType;
    mUri = paramUri;
    mValues = mValues;
    mSelection = mSelection;
    mSelectionArgs = mSelectionArgs;
    mExpectedCount = mExpectedCount;
    mSelectionArgsBackReferences = mSelectionArgsBackReferences;
    mValuesBackReferences = mValuesBackReferences;
    mYieldAllowed = mYieldAllowed;
  }
  
  private ContentProviderOperation(Parcel paramParcel)
  {
    mType = paramParcel.readInt();
    mUri = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    int i = paramParcel.readInt();
    Object localObject1 = null;
    if (i != 0) {
      localObject2 = (ContentValues)ContentValues.CREATOR.createFromParcel(paramParcel);
    } else {
      localObject2 = null;
    }
    mValues = ((ContentValues)localObject2);
    if (paramParcel.readInt() != 0) {
      localObject2 = paramParcel.readString();
    } else {
      localObject2 = null;
    }
    mSelection = ((String)localObject2);
    if (paramParcel.readInt() != 0) {
      localObject2 = paramParcel.readStringArray();
    } else {
      localObject2 = null;
    }
    mSelectionArgs = ((String[])localObject2);
    if (paramParcel.readInt() != 0) {
      localObject2 = Integer.valueOf(paramParcel.readInt());
    } else {
      localObject2 = null;
    }
    mExpectedCount = ((Integer)localObject2);
    if (paramParcel.readInt() != 0) {
      localObject2 = (ContentValues)ContentValues.CREATOR.createFromParcel(paramParcel);
    } else {
      localObject2 = null;
    }
    mValuesBackReferences = ((ContentValues)localObject2);
    Object localObject2 = localObject1;
    if (paramParcel.readInt() != 0) {
      localObject2 = new HashMap();
    }
    mSelectionArgsBackReferences = ((Map)localObject2);
    localObject2 = mSelectionArgsBackReferences;
    boolean bool = false;
    if (localObject2 != null)
    {
      int j = paramParcel.readInt();
      for (i = 0; i < j; i++) {
        mSelectionArgsBackReferences.put(Integer.valueOf(paramParcel.readInt()), Integer.valueOf(paramParcel.readInt()));
      }
    }
    if (paramParcel.readInt() != 0) {
      bool = true;
    }
    mYieldAllowed = bool;
  }
  
  private long backRefToValue(ContentProviderResult[] paramArrayOfContentProviderResult, int paramInt, Integer paramInteger)
  {
    if (paramInteger.intValue() < paramInt)
    {
      paramArrayOfContentProviderResult = paramArrayOfContentProviderResult[paramInteger.intValue()];
      long l;
      if (uri != null) {
        l = ContentUris.parseId(uri);
      } else {
        l = count.intValue();
      }
      return l;
    }
    Log.e("ContentProviderOperation", toString());
    paramArrayOfContentProviderResult = new StringBuilder();
    paramArrayOfContentProviderResult.append("asked for back ref ");
    paramArrayOfContentProviderResult.append(paramInteger);
    paramArrayOfContentProviderResult.append(" but there are only ");
    paramArrayOfContentProviderResult.append(paramInt);
    paramArrayOfContentProviderResult.append(" back refs");
    throw new ArrayIndexOutOfBoundsException(paramArrayOfContentProviderResult.toString());
  }
  
  public static Builder newAssertQuery(Uri paramUri)
  {
    return new Builder(4, paramUri, null);
  }
  
  public static Builder newDelete(Uri paramUri)
  {
    return new Builder(3, paramUri, null);
  }
  
  public static Builder newInsert(Uri paramUri)
  {
    return new Builder(1, paramUri, null);
  }
  
  public static Builder newUpdate(Uri paramUri)
  {
    return new Builder(2, paramUri, null);
  }
  
  public ContentProviderResult apply(ContentProvider paramContentProvider, ContentProviderResult[] paramArrayOfContentProviderResult, int paramInt)
    throws OperationApplicationException
  {
    Object localObject1 = resolveValueBackReferences(paramArrayOfContentProviderResult, paramInt);
    Object localObject2 = resolveSelectionArgsBackReferences(paramArrayOfContentProviderResult, paramInt);
    if (mType == 1)
    {
      paramContentProvider = paramContentProvider.insert(mUri, (ContentValues)localObject1);
      if (paramContentProvider != null) {
        return new ContentProviderResult(paramContentProvider);
      }
      throw new OperationApplicationException("insert failed");
    }
    if (mType == 3) {
      paramInt = paramContentProvider.delete(mUri, mSelection, (String[])localObject2);
    }
    Object localObject3;
    for (;;)
    {
      break;
      if (mType == 2)
      {
        paramInt = paramContentProvider.update(mUri, (ContentValues)localObject1, mSelection, (String[])localObject2);
      }
      else
      {
        if (mType != 4) {
          break label462;
        }
        paramArrayOfContentProviderResult = null;
        if (localObject1 != null)
        {
          localObject3 = new ArrayList();
          paramArrayOfContentProviderResult = ((ContentValues)localObject1).valueSet().iterator();
          while (paramArrayOfContentProviderResult.hasNext()) {
            ((ArrayList)localObject3).add((String)((Map.Entry)paramArrayOfContentProviderResult.next()).getKey());
          }
          paramArrayOfContentProviderResult = (String[])((ArrayList)localObject3).toArray(new String[((ArrayList)localObject3).size()]);
        }
        paramContentProvider = paramContentProvider.query(mUri, paramArrayOfContentProviderResult, mSelection, (String[])localObject2, null);
      }
    }
    try
    {
      int i = paramContentProvider.getCount();
      if (paramArrayOfContentProviderResult != null) {
        while (paramContentProvider.moveToNext())
        {
          for (paramInt = 0; paramInt < paramArrayOfContentProviderResult.length; paramInt++)
          {
            localObject3 = paramContentProvider.getString(paramInt);
            localObject2 = ((ContentValues)localObject1).getAsString(paramArrayOfContentProviderResult[paramInt]);
            if (!TextUtils.equals((CharSequence)localObject3, (CharSequence)localObject2)) {
              break label280;
            }
          }
          continue;
          label280:
          Log.e("ContentProviderOperation", toString());
          localObject1 = new android/content/OperationApplicationException;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Found value ");
          localStringBuilder.append((String)localObject3);
          localStringBuilder.append(" when expected ");
          localStringBuilder.append((String)localObject2);
          localStringBuilder.append(" for column ");
          localStringBuilder.append(paramArrayOfContentProviderResult[paramInt]);
          ((OperationApplicationException)localObject1).<init>(localStringBuilder.toString());
          throw ((Throwable)localObject1);
        }
      }
      paramContentProvider.close();
      paramInt = i;
      if ((mExpectedCount != null) && (mExpectedCount.intValue() != paramInt))
      {
        Log.e("ContentProviderOperation", toString());
        paramContentProvider = new StringBuilder();
        paramContentProvider.append("wrong number of rows: ");
        paramContentProvider.append(paramInt);
        throw new OperationApplicationException(paramContentProvider.toString());
      }
      return new ContentProviderResult(paramInt);
    }
    finally
    {
      paramContentProvider.close();
    }
    label462:
    Log.e("ContentProviderOperation", toString());
    paramContentProvider = new StringBuilder();
    paramContentProvider.append("bad type, ");
    paramContentProvider.append(mType);
    throw new IllegalStateException(paramContentProvider.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public Uri getUri()
  {
    return mUri;
  }
  
  public boolean isAssertQuery()
  {
    boolean bool;
    if (mType == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDelete()
  {
    boolean bool;
    if (mType == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isInsert()
  {
    int i = mType;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isReadOperation()
  {
    boolean bool;
    if (mType == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isUpdate()
  {
    boolean bool;
    if (mType == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isWriteOperation()
  {
    int i = mType;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 3)
    {
      bool2 = bool1;
      if (mType != 1) {
        if (mType == 2) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
  }
  
  public boolean isYieldAllowed()
  {
    return mYieldAllowed;
  }
  
  public String[] resolveSelectionArgsBackReferences(ContentProviderResult[] paramArrayOfContentProviderResult, int paramInt)
  {
    if (mSelectionArgsBackReferences == null) {
      return mSelectionArgs;
    }
    String[] arrayOfString = new String[mSelectionArgs.length];
    System.arraycopy(mSelectionArgs, 0, arrayOfString, 0, mSelectionArgs.length);
    Iterator localIterator = mSelectionArgsBackReferences.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Integer localInteger = (Integer)localEntry.getKey();
      int i = ((Integer)localEntry.getValue()).intValue();
      arrayOfString[localInteger.intValue()] = String.valueOf(backRefToValue(paramArrayOfContentProviderResult, paramInt, Integer.valueOf(i)));
    }
    return arrayOfString;
  }
  
  public ContentValues resolveValueBackReferences(ContentProviderResult[] paramArrayOfContentProviderResult, int paramInt)
  {
    if (mValuesBackReferences == null) {
      return mValues;
    }
    ContentValues localContentValues;
    if (mValues == null) {
      localContentValues = new ContentValues();
    } else {
      localContentValues = new ContentValues(mValues);
    }
    Iterator localIterator = mValuesBackReferences.valueSet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)((Map.Entry)localIterator.next()).getKey();
      Integer localInteger = mValuesBackReferences.getAsInteger(str);
      if (localInteger != null)
      {
        localContentValues.put(str, Long.valueOf(backRefToValue(paramArrayOfContentProviderResult, paramInt, localInteger)));
      }
      else
      {
        Log.e("ContentProviderOperation", toString());
        paramArrayOfContentProviderResult = new StringBuilder();
        paramArrayOfContentProviderResult.append("values backref ");
        paramArrayOfContentProviderResult.append(str);
        paramArrayOfContentProviderResult.append(" is not an integer");
        throw new IllegalArgumentException(paramArrayOfContentProviderResult.toString());
      }
    }
    return localContentValues;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("mType: ");
    localStringBuilder.append(mType);
    localStringBuilder.append(", mUri: ");
    localStringBuilder.append(mUri);
    localStringBuilder.append(", mSelection: ");
    localStringBuilder.append(mSelection);
    localStringBuilder.append(", mExpectedCount: ");
    localStringBuilder.append(mExpectedCount);
    localStringBuilder.append(", mYieldAllowed: ");
    localStringBuilder.append(mYieldAllowed);
    localStringBuilder.append(", mValues: ");
    localStringBuilder.append(mValues);
    localStringBuilder.append(", mValuesBackReferences: ");
    localStringBuilder.append(mValuesBackReferences);
    localStringBuilder.append(", mSelectionArgsBackReferences: ");
    localStringBuilder.append(mSelectionArgsBackReferences);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    Uri.writeToParcel(paramParcel, mUri);
    if (mValues != null)
    {
      paramParcel.writeInt(1);
      mValues.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mSelection != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeString(mSelection);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mSelectionArgs != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeStringArray(mSelectionArgs);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mExpectedCount != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeInt(mExpectedCount.intValue());
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mValuesBackReferences != null)
    {
      paramParcel.writeInt(1);
      mValuesBackReferences.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mSelectionArgsBackReferences != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeInt(mSelectionArgsBackReferences.size());
      Iterator localIterator = mSelectionArgsBackReferences.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramParcel.writeInt(((Integer)localEntry.getKey()).intValue());
        paramParcel.writeInt(((Integer)localEntry.getValue()).intValue());
      }
    }
    paramParcel.writeInt(0);
    paramParcel.writeInt(mYieldAllowed);
  }
  
  public static class Builder
  {
    private Integer mExpectedCount;
    private String mSelection;
    private String[] mSelectionArgs;
    private Map<Integer, Integer> mSelectionArgsBackReferences;
    private final int mType;
    private final Uri mUri;
    private ContentValues mValues;
    private ContentValues mValuesBackReferences;
    private boolean mYieldAllowed;
    
    private Builder(int paramInt, Uri paramUri)
    {
      if (paramUri != null)
      {
        mType = paramInt;
        mUri = paramUri;
        return;
      }
      throw new IllegalArgumentException("uri must not be null");
    }
    
    public ContentProviderOperation build()
    {
      if ((mType == 2) && ((mValues == null) || (mValues.isEmpty())) && ((mValuesBackReferences == null) || (mValuesBackReferences.isEmpty()))) {
        throw new IllegalArgumentException("Empty values");
      }
      if ((mType == 4) && ((mValues == null) || (mValues.isEmpty())) && ((mValuesBackReferences == null) || (mValuesBackReferences.isEmpty())) && (mExpectedCount == null)) {
        throw new IllegalArgumentException("Empty values");
      }
      return new ContentProviderOperation(this, null);
    }
    
    public Builder withExpectedCount(int paramInt)
    {
      if ((mType != 2) && (mType != 3) && (mType != 4)) {
        throw new IllegalArgumentException("only updates, deletes, and asserts can have expected counts");
      }
      mExpectedCount = Integer.valueOf(paramInt);
      return this;
    }
    
    public Builder withSelection(String paramString, String[] paramArrayOfString)
    {
      if ((mType != 2) && (mType != 3) && (mType != 4)) {
        throw new IllegalArgumentException("only updates, deletes, and asserts can have selections");
      }
      mSelection = paramString;
      if (paramArrayOfString == null)
      {
        mSelectionArgs = null;
      }
      else
      {
        mSelectionArgs = new String[paramArrayOfString.length];
        System.arraycopy(paramArrayOfString, 0, mSelectionArgs, 0, paramArrayOfString.length);
      }
      return this;
    }
    
    public Builder withSelectionBackReference(int paramInt1, int paramInt2)
    {
      if ((mType != 2) && (mType != 3) && (mType != 4)) {
        throw new IllegalArgumentException("only updates, deletes, and asserts can have selection back-references");
      }
      if (mSelectionArgsBackReferences == null) {
        mSelectionArgsBackReferences = new HashMap();
      }
      mSelectionArgsBackReferences.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
      return this;
    }
    
    public Builder withValue(String paramString, Object paramObject)
    {
      if ((mType != 1) && (mType != 2) && (mType != 4)) {
        throw new IllegalArgumentException("only inserts and updates can have values");
      }
      if (mValues == null) {
        mValues = new ContentValues();
      }
      if (paramObject == null)
      {
        mValues.putNull(paramString);
      }
      else if ((paramObject instanceof String))
      {
        mValues.put(paramString, (String)paramObject);
      }
      else if ((paramObject instanceof Byte))
      {
        mValues.put(paramString, (Byte)paramObject);
      }
      else if ((paramObject instanceof Short))
      {
        mValues.put(paramString, (Short)paramObject);
      }
      else if ((paramObject instanceof Integer))
      {
        mValues.put(paramString, (Integer)paramObject);
      }
      else if ((paramObject instanceof Long))
      {
        mValues.put(paramString, (Long)paramObject);
      }
      else if ((paramObject instanceof Float))
      {
        mValues.put(paramString, (Float)paramObject);
      }
      else if ((paramObject instanceof Double))
      {
        mValues.put(paramString, (Double)paramObject);
      }
      else if ((paramObject instanceof Boolean))
      {
        mValues.put(paramString, (Boolean)paramObject);
      }
      else
      {
        if (!(paramObject instanceof byte[])) {
          break label267;
        }
        mValues.put(paramString, (byte[])paramObject);
      }
      return this;
      label267:
      paramString = new StringBuilder();
      paramString.append("bad value type: ");
      paramString.append(paramObject.getClass().getName());
      throw new IllegalArgumentException(paramString.toString());
    }
    
    public Builder withValueBackReference(String paramString, int paramInt)
    {
      if ((mType != 1) && (mType != 2) && (mType != 4)) {
        throw new IllegalArgumentException("only inserts, updates, and asserts can have value back-references");
      }
      if (mValuesBackReferences == null) {
        mValuesBackReferences = new ContentValues();
      }
      mValuesBackReferences.put(paramString, Integer.valueOf(paramInt));
      return this;
    }
    
    public Builder withValueBackReferences(ContentValues paramContentValues)
    {
      if ((mType != 1) && (mType != 2) && (mType != 4)) {
        throw new IllegalArgumentException("only inserts, updates, and asserts can have value back-references");
      }
      mValuesBackReferences = paramContentValues;
      return this;
    }
    
    public Builder withValues(ContentValues paramContentValues)
    {
      if ((mType != 1) && (mType != 2) && (mType != 4)) {
        throw new IllegalArgumentException("only inserts, updates, and asserts can have values");
      }
      if (mValues == null) {
        mValues = new ContentValues();
      }
      mValues.putAll(paramContentValues);
      return this;
    }
    
    public Builder withYieldAllowed(boolean paramBoolean)
    {
      mYieldAllowed = paramBoolean;
      return this;
    }
  }
}
