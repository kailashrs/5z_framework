package com.google.android.mms.util;

import android.content.ContentUris;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.Telephony.Mms;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public final class PduCache
  extends AbstractCache<Uri, PduCacheEntry>
{
  private static final boolean DEBUG = false;
  private static final boolean LOCAL_LOGV = false;
  private static final HashMap<Integer, Integer> MATCH_TO_MSGBOX_ID_MAP;
  private static final int MMS_ALL = 0;
  private static final int MMS_ALL_ID = 1;
  private static final int MMS_CONVERSATION = 10;
  private static final int MMS_CONVERSATION_ID = 11;
  private static final int MMS_DRAFTS = 6;
  private static final int MMS_DRAFTS_ID = 7;
  private static final int MMS_INBOX = 2;
  private static final int MMS_INBOX_ID = 3;
  private static final int MMS_OUTBOX = 8;
  private static final int MMS_OUTBOX_ID = 9;
  private static final int MMS_SENT = 4;
  private static final int MMS_SENT_ID = 5;
  private static final String TAG = "PduCache";
  private static final UriMatcher URI_MATCHER = new UriMatcher(-1);
  private static PduCache sInstance;
  private final HashMap<Integer, HashSet<Uri>> mMessageBoxes = new HashMap();
  private final HashMap<Long, HashSet<Uri>> mThreads = new HashMap();
  private final HashSet<Uri> mUpdating = new HashSet();
  
  static
  {
    URI_MATCHER.addURI("mms", null, 0);
    URI_MATCHER.addURI("mms", "#", 1);
    URI_MATCHER.addURI("mms", "inbox", 2);
    URI_MATCHER.addURI("mms", "inbox/#", 3);
    URI_MATCHER.addURI("mms", "sent", 4);
    URI_MATCHER.addURI("mms", "sent/#", 5);
    URI_MATCHER.addURI("mms", "drafts", 6);
    URI_MATCHER.addURI("mms", "drafts/#", 7);
    URI_MATCHER.addURI("mms", "outbox", 8);
    URI_MATCHER.addURI("mms", "outbox/#", 9);
    URI_MATCHER.addURI("mms-sms", "conversations", 10);
    URI_MATCHER.addURI("mms-sms", "conversations/#", 11);
    MATCH_TO_MSGBOX_ID_MAP = new HashMap();
    MATCH_TO_MSGBOX_ID_MAP.put(Integer.valueOf(2), Integer.valueOf(1));
    MATCH_TO_MSGBOX_ID_MAP.put(Integer.valueOf(4), Integer.valueOf(2));
    MATCH_TO_MSGBOX_ID_MAP.put(Integer.valueOf(6), Integer.valueOf(3));
    MATCH_TO_MSGBOX_ID_MAP.put(Integer.valueOf(8), Integer.valueOf(4));
  }
  
  private PduCache() {}
  
  public static final PduCache getInstance()
  {
    try
    {
      if (sInstance == null)
      {
        localPduCache = new com/google/android/mms/util/PduCache;
        localPduCache.<init>();
        sInstance = localPduCache;
      }
      PduCache localPduCache = sInstance;
      return localPduCache;
    }
    finally {}
  }
  
  private Uri normalizeKey(Uri paramUri)
  {
    int i = URI_MATCHER.match(paramUri);
    if (i != 1)
    {
      if ((i != 3) && (i != 5) && (i != 7) && (i != 9)) {
        return null;
      }
      paramUri = paramUri.getLastPathSegment();
      paramUri = Uri.withAppendedPath(Telephony.Mms.CONTENT_URI, paramUri);
    }
    return paramUri;
  }
  
  private void purgeByMessageBox(Integer paramInteger)
  {
    if (paramInteger != null)
    {
      paramInteger = (HashSet)mMessageBoxes.remove(paramInteger);
      if (paramInteger != null)
      {
        Iterator localIterator = paramInteger.iterator();
        while (localIterator.hasNext())
        {
          paramInteger = (Uri)localIterator.next();
          mUpdating.remove(paramInteger);
          PduCacheEntry localPduCacheEntry = (PduCacheEntry)super.purge(paramInteger);
          if (localPduCacheEntry != null) {
            removeFromThreads(paramInteger, localPduCacheEntry);
          }
        }
      }
    }
  }
  
  private void purgeByThreadId(long paramLong)
  {
    Object localObject = (HashSet)mThreads.remove(Long.valueOf(paramLong));
    if (localObject != null)
    {
      Iterator localIterator = ((HashSet)localObject).iterator();
      while (localIterator.hasNext())
      {
        Uri localUri = (Uri)localIterator.next();
        mUpdating.remove(localUri);
        localObject = (PduCacheEntry)super.purge(localUri);
        if (localObject != null) {
          removeFromMessageBoxes(localUri, (PduCacheEntry)localObject);
        }
      }
    }
  }
  
  private PduCacheEntry purgeSingleEntry(Uri paramUri)
  {
    mUpdating.remove(paramUri);
    PduCacheEntry localPduCacheEntry = (PduCacheEntry)super.purge(paramUri);
    if (localPduCacheEntry != null)
    {
      removeFromThreads(paramUri, localPduCacheEntry);
      removeFromMessageBoxes(paramUri, localPduCacheEntry);
      return localPduCacheEntry;
    }
    return null;
  }
  
  private void removeFromMessageBoxes(Uri paramUri, PduCacheEntry paramPduCacheEntry)
  {
    paramPduCacheEntry = (HashSet)mThreads.get(Long.valueOf(paramPduCacheEntry.getMessageBox()));
    if (paramPduCacheEntry != null) {
      paramPduCacheEntry.remove(paramUri);
    }
  }
  
  private void removeFromThreads(Uri paramUri, PduCacheEntry paramPduCacheEntry)
  {
    paramPduCacheEntry = (HashSet)mThreads.get(Long.valueOf(paramPduCacheEntry.getThreadId()));
    if (paramPduCacheEntry != null) {
      paramPduCacheEntry.remove(paramUri);
    }
  }
  
  public boolean isUpdating(Uri paramUri)
  {
    try
    {
      boolean bool = mUpdating.contains(paramUri);
      return bool;
    }
    finally
    {
      paramUri = finally;
      throw paramUri;
    }
  }
  
  public PduCacheEntry purge(Uri paramUri)
  {
    try
    {
      int i = URI_MATCHER.match(paramUri);
      switch (i)
      {
      default: 
        return null;
      case 11: 
        purgeByThreadId(ContentUris.parseId(paramUri));
        return null;
      case 3: 
      case 5: 
      case 7: 
      case 9: 
        paramUri = paramUri.getLastPathSegment();
        paramUri = purgeSingleEntry(Uri.withAppendedPath(Telephony.Mms.CONTENT_URI, paramUri));
        return paramUri;
      case 2: 
      case 4: 
      case 6: 
      case 8: 
        purgeByMessageBox((Integer)MATCH_TO_MSGBOX_ID_MAP.get(Integer.valueOf(i)));
        return null;
      case 1: 
        paramUri = purgeSingleEntry(paramUri);
        return paramUri;
      }
      purgeAll();
      return null;
    }
    finally {}
  }
  
  public void purgeAll()
  {
    try
    {
      super.purgeAll();
      mMessageBoxes.clear();
      mThreads.clear();
      mUpdating.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean put(Uri paramUri, PduCacheEntry paramPduCacheEntry)
  {
    try
    {
      int i = paramPduCacheEntry.getMessageBox();
      Object localObject1 = (HashSet)mMessageBoxes.get(Integer.valueOf(i));
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = new java/util/HashSet;
        ((HashSet)localObject2).<init>();
        mMessageBoxes.put(Integer.valueOf(i), localObject2);
      }
      long l = paramPduCacheEntry.getThreadId();
      Object localObject3 = (HashSet)mThreads.get(Long.valueOf(l));
      localObject1 = localObject3;
      if (localObject3 == null)
      {
        localObject1 = new java/util/HashSet;
        ((HashSet)localObject1).<init>();
        mThreads.put(Long.valueOf(l), localObject1);
      }
      localObject3 = normalizeKey(paramUri);
      boolean bool = super.put(localObject3, paramPduCacheEntry);
      if (bool)
      {
        ((HashSet)localObject2).add(localObject3);
        ((HashSet)localObject1).add(localObject3);
      }
      setUpdating(paramUri, false);
      return bool;
    }
    finally {}
  }
  
  public void setUpdating(Uri paramUri, boolean paramBoolean)
  {
    if (paramBoolean) {
      try
      {
        mUpdating.add(paramUri);
      }
      finally
      {
        break label34;
      }
    } else {
      mUpdating.remove(paramUri);
    }
    return;
    label34:
    throw paramUri;
  }
}
