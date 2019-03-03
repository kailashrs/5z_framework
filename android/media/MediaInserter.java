package android.media;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.net.Uri;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MediaInserter
{
  private final int mBufferSizePerUri;
  private final HashMap<Uri, List<ContentValues>> mPriorityRowMap = new HashMap();
  private final ContentProviderClient mProvider;
  private final HashMap<Uri, List<ContentValues>> mRowMap = new HashMap();
  
  public MediaInserter(ContentProviderClient paramContentProviderClient, int paramInt)
  {
    mProvider = paramContentProviderClient;
    mBufferSizePerUri = paramInt;
  }
  
  private void flush(Uri paramUri, List<ContentValues> paramList)
    throws RemoteException
  {
    if (!paramList.isEmpty())
    {
      ContentValues[] arrayOfContentValues = (ContentValues[])paramList.toArray(new ContentValues[paramList.size()]);
      mProvider.bulkInsert(paramUri, arrayOfContentValues);
      paramList.clear();
    }
  }
  
  private void flushAllPriority()
    throws RemoteException
  {
    Iterator localIterator = mPriorityRowMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      Uri localUri = (Uri)localIterator.next();
      flush(localUri, (List)mPriorityRowMap.get(localUri));
    }
    mPriorityRowMap.clear();
  }
  
  private void insert(Uri paramUri, ContentValues paramContentValues, boolean paramBoolean)
    throws RemoteException
  {
    HashMap localHashMap;
    if (paramBoolean) {
      localHashMap = mPriorityRowMap;
    } else {
      localHashMap = mRowMap;
    }
    List localList = (List)localHashMap.get(paramUri);
    Object localObject = localList;
    if (localList == null)
    {
      localObject = new ArrayList();
      localHashMap.put(paramUri, localObject);
    }
    ((List)localObject).add(new ContentValues(paramContentValues));
    if (((List)localObject).size() >= mBufferSizePerUri)
    {
      flushAllPriority();
      flush(paramUri, (List)localObject);
    }
  }
  
  public void flushAll()
    throws RemoteException
  {
    flushAllPriority();
    Iterator localIterator = mRowMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      Uri localUri = (Uri)localIterator.next();
      flush(localUri, (List)mRowMap.get(localUri));
    }
    mRowMap.clear();
  }
  
  public void insert(Uri paramUri, ContentValues paramContentValues)
    throws RemoteException
  {
    insert(paramUri, paramContentValues, false);
  }
  
  public void insertwithPriority(Uri paramUri, ContentValues paramContentValues)
    throws RemoteException
  {
    insert(paramUri, paramContentValues, true);
  }
}
