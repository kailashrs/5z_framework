package android.content;

import android.net.Uri;
import java.util.ArrayList;
import java.util.Iterator;

public final class Entity
{
  private final ArrayList<NamedContentValues> mSubValues;
  private final ContentValues mValues;
  
  public Entity(ContentValues paramContentValues)
  {
    mValues = paramContentValues;
    mSubValues = new ArrayList();
  }
  
  public void addSubValue(Uri paramUri, ContentValues paramContentValues)
  {
    mSubValues.add(new NamedContentValues(paramUri, paramContentValues));
  }
  
  public ContentValues getEntityValues()
  {
    return mValues;
  }
  
  public ArrayList<NamedContentValues> getSubValues()
  {
    return mSubValues;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Entity: ");
    localStringBuilder.append(getEntityValues());
    Iterator localIterator = getSubValues().iterator();
    while (localIterator.hasNext())
    {
      NamedContentValues localNamedContentValues = (NamedContentValues)localIterator.next();
      localStringBuilder.append("\n  ");
      localStringBuilder.append(uri);
      localStringBuilder.append("\n  -> ");
      localStringBuilder.append(values);
    }
    return localStringBuilder.toString();
  }
  
  public static class NamedContentValues
  {
    public final Uri uri;
    public final ContentValues values;
    
    public NamedContentValues(Uri paramUri, ContentValues paramContentValues)
    {
      uri = paramUri;
      values = paramContentValues;
    }
  }
}
