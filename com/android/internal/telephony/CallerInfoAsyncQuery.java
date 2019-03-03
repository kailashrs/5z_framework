package com.android.internal.telephony;

import android.app.ActivityManager;
import android.content.AsyncQueryHandler;
import android.content.AsyncQueryHandler.WorkerArgs;
import android.content.AsyncQueryHandler.WorkerHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallerInfoAsyncQuery
{
  private static final boolean DBG = false;
  private static final boolean ENABLE_UNKNOWN_NUMBER_GEO_DESCRIPTION = true;
  private static final int EVENT_ADD_LISTENER = 2;
  private static final int EVENT_EMERGENCY_NUMBER = 4;
  private static final int EVENT_END_OF_QUEUE = 3;
  private static final int EVENT_GET_GEO_DESCRIPTION = 6;
  private static final int EVENT_NEW_QUERY = 1;
  private static final int EVENT_VOICEMAIL_NUMBER = 5;
  private static final String LOG_TAG = "CallerInfoAsyncQuery";
  private CallerInfoAsyncQueryHandler mHandler;
  
  private CallerInfoAsyncQuery() {}
  
  private void allocate(Context paramContext, Uri paramUri)
  {
    if ((paramContext != null) && (paramUri != null))
    {
      mHandler = new CallerInfoAsyncQueryHandler(paramContext, null);
      CallerInfoAsyncQueryHandler.access$602(mHandler, paramUri);
      return;
    }
    throw new QueryPoolException("Bad context or query uri.");
  }
  
  static ContentResolver getCurrentProfileContentResolver(Context paramContext)
  {
    int i = ActivityManager.getCurrentUser();
    if (UserManager.get(paramContext).getUserHandle() != i) {
      try
      {
        String str = paramContext.getPackageName();
        Object localObject = new android/os/UserHandle;
        ((UserHandle)localObject).<init>(i);
        localObject = paramContext.createPackageContextAsUser(str, 0, (UserHandle)localObject).getContentResolver();
        return localObject;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Rlog.e("CallerInfoAsyncQuery", "Can't find self package", localNameNotFoundException);
      }
    }
    return paramContext.getContentResolver();
  }
  
  private void release()
  {
    CallerInfoAsyncQueryHandler.access$102(mHandler, null);
    CallerInfoAsyncQueryHandler.access$602(mHandler, null);
    CallerInfoAsyncQueryHandler.access$402(mHandler, null);
    mHandler = null;
  }
  
  private static String sanitizeUriToString(Uri paramUri)
  {
    if (paramUri != null)
    {
      paramUri = paramUri.toString();
      int i = paramUri.lastIndexOf('/');
      if (i > 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramUri.substring(0, i));
        localStringBuilder.append("/xxxxxxx");
        return localStringBuilder.toString();
      }
      return paramUri;
    }
    return "";
  }
  
  public static CallerInfoAsyncQuery startQuery(int paramInt, Context paramContext, Uri paramUri, OnQueryCompleteListener paramOnQueryCompleteListener, Object paramObject)
  {
    CallerInfoAsyncQuery localCallerInfoAsyncQuery = new CallerInfoAsyncQuery();
    localCallerInfoAsyncQuery.allocate(paramContext, paramUri);
    paramContext = new CookieWrapper(null);
    listener = paramOnQueryCompleteListener;
    cookie = paramObject;
    event = 1;
    subId = -1;
    preferContactId = null;
    mHandler.startQuery(paramInt, paramContext, paramUri, null, null, null, null);
    return localCallerInfoAsyncQuery;
  }
  
  public static CallerInfoAsyncQuery startQuery(int paramInt, Context paramContext, String paramString, OnQueryCompleteListener paramOnQueryCompleteListener, Object paramObject)
  {
    return startQuery(paramInt, paramContext, paramString, paramOnQueryCompleteListener, paramObject, SubscriptionManager.getDefaultSubscriptionId());
  }
  
  public static CallerInfoAsyncQuery startQuery(int paramInt1, Context paramContext, String paramString, OnQueryCompleteListener paramOnQueryCompleteListener, Object paramObject, int paramInt2)
  {
    return startQuery(paramInt1, paramContext, paramString, paramOnQueryCompleteListener, paramObject, paramInt2, null);
  }
  
  public static CallerInfoAsyncQuery startQuery(int paramInt1, Context paramContext, String paramString1, OnQueryCompleteListener paramOnQueryCompleteListener, Object paramObject, int paramInt2, String paramString2)
  {
    Uri localUri = ContactsContract.PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI.buildUpon().appendPath(paramString1).appendQueryParameter("sip", String.valueOf(PhoneNumberUtils.isUriNumber(paramString1))).build();
    CallerInfoAsyncQuery localCallerInfoAsyncQuery = new CallerInfoAsyncQuery();
    localCallerInfoAsyncQuery.allocate(paramContext, localUri);
    CookieWrapper localCookieWrapper = new CookieWrapper(null);
    listener = paramOnQueryCompleteListener;
    cookie = paramObject;
    number = paramString1;
    subId = paramInt2;
    preferContactId = paramString2;
    if (PhoneNumberUtils.isLocalEmergencyNumber(paramContext, paramString1)) {
      event = 4;
    } else if (PhoneNumberUtils.isVoiceMailNumber(paramContext, paramInt2, paramString1)) {
      event = 5;
    } else {
      event = 1;
    }
    mHandler.startQuery(paramInt1, localCookieWrapper, localUri, null, null, null, null);
    return localCallerInfoAsyncQuery;
  }
  
  public void addQueryListener(int paramInt, OnQueryCompleteListener paramOnQueryCompleteListener, Object paramObject)
  {
    CookieWrapper localCookieWrapper = new CookieWrapper(null);
    listener = paramOnQueryCompleteListener;
    cookie = paramObject;
    event = 2;
    subId = -1;
    preferContactId = null;
    mHandler.startQuery(paramInt, localCookieWrapper, null, null, null, null, null);
  }
  
  private class CallerInfoAsyncQueryHandler
    extends AsyncQueryHandler
  {
    private CallerInfo mCallerInfo;
    private Context mContext;
    private List<Runnable> mPendingListenerCallbacks = new ArrayList();
    private Uri mQueryUri;
    
    private CallerInfoAsyncQueryHandler(Context paramContext)
    {
      super();
      mContext = paramContext;
    }
    
    protected Handler createHandler(Looper paramLooper)
    {
      return new CallerInfoWorkerHandler(paramLooper);
    }
    
    protected void onQueryComplete(final int paramInt, final Object paramObject, Cursor paramCursor)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("##### onQueryComplete() #####   query complete for token: ");
      ((StringBuilder)localObject).append(paramInt);
      Rlog.d("CallerInfoAsyncQuery", ((StringBuilder)localObject).toString());
      paramObject = (CallerInfoAsyncQuery.CookieWrapper)paramObject;
      if (paramObject == null)
      {
        Rlog.i("CallerInfoAsyncQuery", "Cookie is null, ignoring onQueryComplete() request.");
        if (paramCursor != null) {
          paramCursor.close();
        }
        return;
      }
      if (event == 3)
      {
        paramObject = mPendingListenerCallbacks.iterator();
        while (paramObject.hasNext()) {
          ((Runnable)paramObject.next()).run();
        }
        mPendingListenerCallbacks.clear();
        CallerInfoAsyncQuery.this.release();
        if (paramCursor != null) {
          paramCursor.close();
        }
        return;
      }
      if (event == 6)
      {
        if (mCallerInfo != null) {
          mCallerInfo.geoDescription = geoDescription;
        }
        localObject = new CallerInfoAsyncQuery.CookieWrapper(null);
        event = 3;
        startQuery(paramInt, localObject, null, null, null, null, null);
      }
      if (mCallerInfo == null) {
        if ((mContext != null) && (mQueryUri != null))
        {
          if (event == 4)
          {
            mCallerInfo = new CallerInfo().markAsEmergency(mContext);
          }
          else if (event == 5)
          {
            mCallerInfo = new CallerInfo().markAsVoiceMail(subId);
          }
          else
          {
            if (subId != -1) {
              mCallerInfo = CallerInfo.getCallerInfo(mContext, mQueryUri, paramCursor, subId, preferContactId);
            } else {
              mCallerInfo = CallerInfo.getCallerInfo(mContext, mQueryUri, paramCursor);
            }
            localObject = CallerInfo.doSecondaryLookupIfNecessary(mContext, number, mCallerInfo);
            if (localObject != mCallerInfo) {
              mCallerInfo = ((CallerInfo)localObject);
            }
            if (!TextUtils.isEmpty(number)) {
              mCallerInfo.phoneNumber = PhoneNumberUtils.formatNumber(number, mCallerInfo.normalizedNumber, CallerInfo.getCurrentCountryIso(mContext));
            }
            if (TextUtils.isEmpty(mCallerInfo.name))
            {
              event = 6;
              startQuery(paramInt, paramObject, null, null, null, null, null);
              return;
            }
          }
          localObject = new CallerInfoAsyncQuery.CookieWrapper(null);
          event = 3;
          startQuery(paramInt, localObject, null, null, null, null, null);
        }
        else
        {
          throw new CallerInfoAsyncQuery.QueryPoolException("Bad context or query uri, or CallerInfoAsyncQuery already released.");
        }
      }
      if (listener != null) {
        mPendingListenerCallbacks.add(new Runnable()
        {
          public void run()
          {
            paramObjectlistener.onQueryComplete(paramInt, paramObjectcookie, mCallerInfo);
          }
        });
      } else {
        Rlog.w("CallerInfoAsyncQuery", "There is no listener to notify for this query.");
      }
      if (paramCursor != null) {
        paramCursor.close();
      }
    }
    
    protected class CallerInfoWorkerHandler
      extends AsyncQueryHandler.WorkerHandler
    {
      public CallerInfoWorkerHandler(Looper paramLooper)
      {
        super(paramLooper);
      }
      
      private void handleGeoDescription(Message paramMessage)
      {
        AsyncQueryHandler.WorkerArgs localWorkerArgs = (AsyncQueryHandler.WorkerArgs)obj;
        Object localObject = (CallerInfoAsyncQuery.CookieWrapper)cookie;
        if ((!TextUtils.isEmpty(number)) && (cookie != null) && (mContext != null))
        {
          SystemClock.elapsedRealtime();
          geoDescription = CallerInfo.getGeoDescription(mContext, number);
          SystemClock.elapsedRealtime();
        }
        localObject = handler.obtainMessage(what);
        obj = localWorkerArgs;
        arg1 = arg1;
        ((Message)localObject).sendToTarget();
      }
      
      public void handleMessage(Message paramMessage)
      {
        Object localObject1 = (AsyncQueryHandler.WorkerArgs)obj;
        Object localObject2 = (CallerInfoAsyncQuery.CookieWrapper)cookie;
        if (localObject2 == null)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unexpected command (CookieWrapper is null): ");
          ((StringBuilder)localObject1).append(what);
          ((StringBuilder)localObject1).append(" ignored by CallerInfoWorkerHandler, passing onto parent.");
          Rlog.i("CallerInfoAsyncQuery", ((StringBuilder)localObject1).toString());
          super.handleMessage(paramMessage);
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Processing event: ");
          localStringBuilder.append(event);
          localStringBuilder.append(" token (arg1): ");
          localStringBuilder.append(arg1);
          localStringBuilder.append(" command: ");
          localStringBuilder.append(what);
          localStringBuilder.append(" query URI: ");
          localStringBuilder.append(CallerInfoAsyncQuery.sanitizeUriToString(uri));
          Rlog.d("CallerInfoAsyncQuery", localStringBuilder.toString());
          switch (event)
          {
          default: 
            break;
          case 6: 
            handleGeoDescription(paramMessage);
            break;
          case 2: 
          case 3: 
          case 4: 
          case 5: 
            localObject2 = handler.obtainMessage(what);
            obj = localObject1;
            arg1 = arg1;
            ((Message)localObject2).sendToTarget();
            break;
          case 1: 
            super.handleMessage(paramMessage);
          }
        }
      }
    }
  }
  
  private static final class CookieWrapper
  {
    public Object cookie;
    public int event;
    public String geoDescription;
    public CallerInfoAsyncQuery.OnQueryCompleteListener listener;
    public String number;
    public String preferContactId;
    public int subId;
    
    private CookieWrapper() {}
  }
  
  public static abstract interface OnQueryCompleteListener
  {
    public abstract void onQueryComplete(int paramInt, Object paramObject, CallerInfo paramCallerInfo);
  }
  
  public static class QueryPoolException
    extends SQLException
  {
    public QueryPoolException(String paramString)
    {
      super();
    }
  }
}
