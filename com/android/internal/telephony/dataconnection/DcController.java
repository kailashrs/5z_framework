package com.android.internal.telephony.dataconnection;

import android.content.Context;
import android.net.INetworkPolicyListener;
import android.net.NetworkPolicyManager;
import android.net.NetworkPolicyManager.Listener;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DcController
  extends StateMachine
{
  static final int DATA_CONNECTION_ACTIVE_PH_LINK_DORMANT = 1;
  static final int DATA_CONNECTION_ACTIVE_PH_LINK_INACTIVE = 0;
  static final int DATA_CONNECTION_ACTIVE_PH_LINK_UP = 2;
  static final int DATA_CONNECTION_ACTIVE_UNKNOWN = Integer.MAX_VALUE;
  private static final boolean DBG = true;
  private static final boolean VDBG = false;
  private final DataServiceManager mDataServiceManager;
  private final HashMap<Integer, DataConnection> mDcListActiveByCid = new HashMap();
  final ArrayList<DataConnection> mDcListAll = new ArrayList();
  private final DcTesterDeactivateAll mDcTesterDeactivateAll;
  private DccDefaultState mDccDefaultState;
  private final DcTracker mDct;
  private volatile boolean mExecutingCarrierChange;
  private final INetworkPolicyListener mListener;
  final NetworkPolicyManager mNetworkPolicyManager;
  private final Phone mPhone;
  private PhoneStateListener mPhoneStateListener;
  final TelephonyManager mTelephonyManager;
  
  private DcController(String paramString, Phone paramPhone, DcTracker paramDcTracker, DataServiceManager paramDataServiceManager, Handler paramHandler)
  {
    super(paramString, paramHandler);
    paramString = null;
    mDccDefaultState = new DccDefaultState(null);
    mListener = new NetworkPolicyManager.Listener()
    {
      public void onSubscriptionOverride(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        if ((mPhone != null) && (mPhone.getSubId() == paramAnonymousInt1)) {
          synchronized (mDcListAll)
          {
            HashMap localHashMap = new java/util/HashMap;
            localHashMap.<init>(mDcListActiveByCid);
            ??? = localHashMap.values().iterator();
            while (((Iterator)???).hasNext()) {
              ((DataConnection)((Iterator)???).next()).onSubscriptionOverride(paramAnonymousInt2, paramAnonymousInt3);
            }
            return;
          }
        }
      }
    };
    setLogRecSize(300);
    log("E ctor");
    mPhone = paramPhone;
    mDct = paramDcTracker;
    mDataServiceManager = paramDataServiceManager;
    addState(mDccDefaultState);
    setInitialState(mDccDefaultState);
    log("X ctor");
    mPhoneStateListener = new PhoneStateListener(paramHandler.getLooper())
    {
      public void onCarrierNetworkChange(boolean paramAnonymousBoolean)
      {
        DcController.access$102(DcController.this, paramAnonymousBoolean);
      }
    };
    mTelephonyManager = ((TelephonyManager)paramPhone.getContext().getSystemService("phone"));
    mNetworkPolicyManager = ((NetworkPolicyManager)paramPhone.getContext().getSystemService("netpolicy"));
    if (Build.IS_DEBUGGABLE) {
      paramString = new DcTesterDeactivateAll(mPhone, this, getHandler());
    }
    mDcTesterDeactivateAll = paramString;
    if (mTelephonyManager != null) {
      mTelephonyManager.listen(mPhoneStateListener, 65536);
    }
  }
  
  private void lr(String paramString)
  {
    logAndAddLogRec(paramString);
  }
  
  public static DcController makeDcc(Phone paramPhone, DcTracker paramDcTracker, DataServiceManager paramDataServiceManager, Handler paramHandler)
  {
    return new DcController("Dcc", paramPhone, paramDcTracker, paramDataServiceManager, paramHandler);
  }
  
  public void addActiveDcByCid(DataConnection paramDataConnection)
  {
    if (mCid < 0)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("addActiveDcByCid dc.mCid < 0 dc=");
      ((StringBuilder)???).append(paramDataConnection);
      log(((StringBuilder)???).toString());
    }
    synchronized (mDcListAll)
    {
      mDcListActiveByCid.put(Integer.valueOf(mCid), paramDataConnection);
      return;
    }
  }
  
  void addDc(DataConnection paramDataConnection)
  {
    synchronized (mDcListAll)
    {
      mDcListAll.add(paramDataConnection);
      return;
    }
  }
  
  void dispose()
  {
    log("dispose: call quiteNow()");
    if (mTelephonyManager != null) {
      mTelephonyManager.listen(mPhoneStateListener, 0);
    }
    quitNow();
  }
  
  public void dump(FileDescriptor arg1, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(???, paramPrintWriter, paramArrayOfString);
    ??? = new StringBuilder();
    ???.append(" mPhone=");
    ???.append(mPhone);
    paramPrintWriter.println(???.toString());
    synchronized (mDcListAll)
    {
      paramArrayOfString = new java/lang/StringBuilder;
      paramArrayOfString.<init>();
      paramArrayOfString.append(" mDcListAll=");
      paramArrayOfString.append(mDcListAll);
      paramPrintWriter.println(paramArrayOfString.toString());
      paramArrayOfString = new java/lang/StringBuilder;
      paramArrayOfString.<init>();
      paramArrayOfString.append(" mDcListActiveByCid=");
      paramArrayOfString.append(mDcListActiveByCid);
      paramPrintWriter.println(paramArrayOfString.toString());
      return;
    }
  }
  
  public DataConnection getActiveDcByCid(int paramInt)
  {
    synchronized (mDcListAll)
    {
      DataConnection localDataConnection = (DataConnection)mDcListActiveByCid.get(Integer.valueOf(paramInt));
      return localDataConnection;
    }
  }
  
  protected String getWhatToString(int paramInt)
  {
    String str1 = DataConnection.cmdToString(paramInt);
    String str2 = str1;
    if (str1 == null) {
      str2 = DcAsyncChannel.cmdToString(paramInt);
    }
    return str2;
  }
  
  boolean isExecutingCarrierChange()
  {
    return mExecutingCarrierChange;
  }
  
  protected void log(String paramString)
  {
    Rlog.d(getName(), paramString);
  }
  
  protected void loge(String paramString)
  {
    Rlog.e(getName(), paramString);
  }
  
  void removeActiveDcByCid(DataConnection paramDataConnection)
  {
    synchronized (mDcListAll)
    {
      if ((DataConnection)mDcListActiveByCid.remove(Integer.valueOf(mCid)) == null)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("removeActiveDcByCid removedDc=null dc=");
        localStringBuilder.append(paramDataConnection);
        log(localStringBuilder.toString());
      }
      return;
    }
  }
  
  void removeDc(DataConnection paramDataConnection)
  {
    synchronized (mDcListAll)
    {
      mDcListActiveByCid.remove(Integer.valueOf(mCid));
      mDcListAll.remove(paramDataConnection);
      return;
    }
  }
  
  public String toString()
  {
    synchronized (mDcListAll)
    {
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("mDcListAll=");
      ((StringBuilder)localObject1).append(mDcListAll);
      ((StringBuilder)localObject1).append(" mDcListActiveByCid=");
      ((StringBuilder)localObject1).append(mDcListActiveByCid);
      localObject1 = ((StringBuilder)localObject1).toString();
      return localObject1;
    }
  }
  
  private class DccDefaultState
    extends State
  {
    private DccDefaultState() {}
    
    /* Error */
    private void onDataStateChanged(ArrayList<android.telephony.data.DataCallResponse> paramArrayList)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   4: getfield 26	com/android/internal/telephony/dataconnection/DcController:mDcListAll	Ljava/util/ArrayList;
      //   7: astore_2
      //   8: aload_2
      //   9: monitorenter
      //   10: new 28	java/util/ArrayList
      //   13: astore_3
      //   14: aload_3
      //   15: aload_0
      //   16: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   19: getfield 26	com/android/internal/telephony/dataconnection/DcController:mDcListAll	Ljava/util/ArrayList;
      //   22: invokespecial 31	java/util/ArrayList:<init>	(Ljava/util/Collection;)V
      //   25: new 33	java/util/HashMap
      //   28: astore 4
      //   30: aload 4
      //   32: aload_0
      //   33: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   36: invokestatic 37	com/android/internal/telephony/dataconnection/DcController:access$300	(Lcom/android/internal/telephony/dataconnection/DcController;)Ljava/util/HashMap;
      //   39: invokespecial 40	java/util/HashMap:<init>	(Ljava/util/Map;)V
      //   42: aload_2
      //   43: monitorexit
      //   44: aload_0
      //   45: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   48: astore 5
      //   50: new 42	java/lang/StringBuilder
      //   53: dup
      //   54: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   57: astore_2
      //   58: aload_2
      //   59: ldc 45
      //   61: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   64: pop
      //   65: aload_2
      //   66: aload_1
      //   67: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   70: pop
      //   71: aload_2
      //   72: ldc 54
      //   74: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   77: pop
      //   78: aload_2
      //   79: aload 4
      //   81: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   84: pop
      //   85: aload 5
      //   87: aload_2
      //   88: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   91: invokestatic 62	com/android/internal/telephony/dataconnection/DcController:access$700	(Lcom/android/internal/telephony/dataconnection/DcController;Ljava/lang/String;)V
      //   94: new 33	java/util/HashMap
      //   97: dup
      //   98: invokespecial 63	java/util/HashMap:<init>	()V
      //   101: astore_2
      //   102: aload_1
      //   103: invokevirtual 67	java/util/ArrayList:iterator	()Ljava/util/Iterator;
      //   106: astore 6
      //   108: aload 6
      //   110: invokeinterface 73 1 0
      //   115: ifeq +33 -> 148
      //   118: aload 6
      //   120: invokeinterface 77 1 0
      //   125: checkcast 79	android/telephony/data/DataCallResponse
      //   128: astore 5
      //   130: aload_2
      //   131: aload 5
      //   133: invokevirtual 83	android/telephony/data/DataCallResponse:getCallId	()I
      //   136: invokestatic 89	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   139: aload 5
      //   141: invokevirtual 93	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   144: pop
      //   145: goto -37 -> 108
      //   148: new 28	java/util/ArrayList
      //   151: dup
      //   152: invokespecial 94	java/util/ArrayList:<init>	()V
      //   155: astore 5
      //   157: aload 4
      //   159: invokevirtual 98	java/util/HashMap:values	()Ljava/util/Collection;
      //   162: invokeinterface 101 1 0
      //   167: astore 6
      //   169: aload 6
      //   171: invokeinterface 73 1 0
      //   176: ifeq +82 -> 258
      //   179: aload 6
      //   181: invokeinterface 77 1 0
      //   186: checkcast 103	com/android/internal/telephony/dataconnection/DataConnection
      //   189: astore 7
      //   191: aload_2
      //   192: aload 7
      //   194: getfield 107	com/android/internal/telephony/dataconnection/DataConnection:mCid	I
      //   197: invokestatic 89	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   200: invokevirtual 111	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
      //   203: ifnonnull +52 -> 255
      //   206: aload_0
      //   207: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   210: astore 8
      //   212: new 42	java/lang/StringBuilder
      //   215: dup
      //   216: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   219: astore 9
      //   221: aload 9
      //   223: ldc 113
      //   225: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   228: pop
      //   229: aload 9
      //   231: aload 7
      //   233: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   236: pop
      //   237: aload 8
      //   239: aload 9
      //   241: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   244: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   247: aload 5
      //   249: aload 7
      //   251: invokevirtual 121	java/util/ArrayList:add	(Ljava/lang/Object;)Z
      //   254: pop
      //   255: goto -86 -> 169
      //   258: aload_0
      //   259: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   262: astore 6
      //   264: new 42	java/lang/StringBuilder
      //   267: dup
      //   268: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   271: astore 9
      //   273: aload 9
      //   275: ldc 123
      //   277: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   280: pop
      //   281: aload 9
      //   283: aload 5
      //   285: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   288: pop
      //   289: aload 6
      //   291: aload 9
      //   293: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   296: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   299: new 28	java/util/ArrayList
      //   302: dup
      //   303: invokespecial 94	java/util/ArrayList:<init>	()V
      //   306: astore 6
      //   308: iconst_0
      //   309: istore 10
      //   311: iconst_0
      //   312: istore 11
      //   314: aload_1
      //   315: invokevirtual 67	java/util/ArrayList:iterator	()Ljava/util/Iterator;
      //   318: astore 9
      //   320: aload 4
      //   322: astore_1
      //   323: aload_2
      //   324: astore 4
      //   326: aload 9
      //   328: invokeinterface 73 1 0
      //   333: ifeq +974 -> 1307
      //   336: aload 9
      //   338: invokeinterface 77 1 0
      //   343: checkcast 79	android/telephony/data/DataCallResponse
      //   346: astore 7
      //   348: aload_1
      //   349: aload 7
      //   351: invokevirtual 83	android/telephony/data/DataCallResponse:getCallId	()I
      //   354: invokestatic 89	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   357: invokevirtual 111	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
      //   360: checkcast 103	com/android/internal/telephony/dataconnection/DataConnection
      //   363: astore 8
      //   365: aload 8
      //   367: ifnonnull +15 -> 382
      //   370: aload_0
      //   371: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   374: ldc 125
      //   376: invokevirtual 128	com/android/internal/telephony/dataconnection/DcController:loge	(Ljava/lang/String;)V
      //   379: goto -53 -> 326
      //   382: aload 8
      //   384: getfield 132	com/android/internal/telephony/dataconnection/DataConnection:mApnContexts	Ljava/util/HashMap;
      //   387: invokevirtual 135	java/util/HashMap:size	()I
      //   390: ifne +15 -> 405
      //   393: aload_0
      //   394: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   397: ldc -119
      //   399: invokevirtual 128	com/android/internal/telephony/dataconnection/DcController:loge	(Ljava/lang/String;)V
      //   402: goto +878 -> 1280
      //   405: aload_0
      //   406: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   409: astore_2
      //   410: new 42	java/lang/StringBuilder
      //   413: dup
      //   414: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   417: astore 12
      //   419: aload 12
      //   421: ldc -117
      //   423: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   426: pop
      //   427: aload 12
      //   429: aload 7
      //   431: invokevirtual 83	android/telephony/data/DataCallResponse:getCallId	()I
      //   434: invokevirtual 142	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   437: pop
      //   438: aload 12
      //   440: ldc -112
      //   442: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   445: pop
      //   446: aload 12
      //   448: aload 7
      //   450: invokevirtual 145	android/telephony/data/DataCallResponse:toString	()Ljava/lang/String;
      //   453: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   456: pop
      //   457: aload_2
      //   458: aload 12
      //   460: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   463: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   466: aload 7
      //   468: invokevirtual 148	android/telephony/data/DataCallResponse:getActive	()I
      //   471: ifne +261 -> 732
      //   474: aload_0
      //   475: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   478: invokestatic 152	com/android/internal/telephony/dataconnection/DcController:access$800	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/dataconnection/DcTracker;
      //   481: getfield 158	com/android/internal/telephony/dataconnection/DcTracker:isCleanupRequired	Ljava/util/concurrent/atomic/AtomicBoolean;
      //   484: invokevirtual 162	java/util/concurrent/atomic/AtomicBoolean:get	()Z
      //   487: ifeq +34 -> 521
      //   490: aload 6
      //   492: aload 8
      //   494: getfield 132	com/android/internal/telephony/dataconnection/DataConnection:mApnContexts	Ljava/util/HashMap;
      //   497: invokevirtual 166	java/util/HashMap:keySet	()Ljava/util/Set;
      //   500: invokevirtual 170	java/util/ArrayList:addAll	(Ljava/util/Collection;)Z
      //   503: pop
      //   504: aload_0
      //   505: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   508: invokestatic 152	com/android/internal/telephony/dataconnection/DcController:access$800	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/dataconnection/DcTracker;
      //   511: getfield 158	com/android/internal/telephony/dataconnection/DcTracker:isCleanupRequired	Ljava/util/concurrent/atomic/AtomicBoolean;
      //   514: iconst_0
      //   515: invokevirtual 174	java/util/concurrent/atomic/AtomicBoolean:set	(Z)V
      //   518: goto -116 -> 402
      //   521: aload 7
      //   523: invokevirtual 177	android/telephony/data/DataCallResponse:getStatus	()I
      //   526: invokestatic 183	com/android/internal/telephony/dataconnection/DcFailCause:fromInt	(I)Lcom/android/internal/telephony/dataconnection/DcFailCause;
      //   529: astore_2
      //   530: aload_2
      //   531: aload_0
      //   532: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   535: invokestatic 187	com/android/internal/telephony/dataconnection/DcController:access$200	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/Phone;
      //   538: invokevirtual 193	com/android/internal/telephony/Phone:getContext	()Landroid/content/Context;
      //   541: aload_0
      //   542: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   545: invokestatic 187	com/android/internal/telephony/dataconnection/DcController:access$200	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/Phone;
      //   548: invokevirtual 196	com/android/internal/telephony/Phone:getSubId	()I
      //   551: invokevirtual 200	com/android/internal/telephony/dataconnection/DcFailCause:isRestartRadioFail	(Landroid/content/Context;I)Z
      //   554: ifeq +56 -> 610
      //   557: aload_0
      //   558: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   561: astore 12
      //   563: new 42	java/lang/StringBuilder
      //   566: dup
      //   567: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   570: astore 8
      //   572: aload 8
      //   574: ldc -54
      //   576: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   579: pop
      //   580: aload 8
      //   582: aload_2
      //   583: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   586: pop
      //   587: aload 12
      //   589: aload 8
      //   591: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   594: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   597: aload_0
      //   598: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   601: invokestatic 152	com/android/internal/telephony/dataconnection/DcController:access$800	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/dataconnection/DcTracker;
      //   604: invokevirtual 205	com/android/internal/telephony/dataconnection/DcTracker:sendRestartRadio	()V
      //   607: goto +122 -> 729
      //   610: aload_0
      //   611: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   614: invokestatic 152	com/android/internal/telephony/dataconnection/DcController:access$800	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/dataconnection/DcTracker;
      //   617: aload_2
      //   618: invokevirtual 209	com/android/internal/telephony/dataconnection/DcTracker:isPermanentFailure	(Lcom/android/internal/telephony/dataconnection/DcFailCause;)Z
      //   621: ifeq +60 -> 681
      //   624: aload_0
      //   625: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   628: astore 12
      //   630: new 42	java/lang/StringBuilder
      //   633: dup
      //   634: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   637: astore 13
      //   639: aload 13
      //   641: ldc -45
      //   643: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   646: pop
      //   647: aload 13
      //   649: aload_2
      //   650: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   653: pop
      //   654: aload 12
      //   656: aload 13
      //   658: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   661: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   664: aload 6
      //   666: aload 8
      //   668: getfield 132	com/android/internal/telephony/dataconnection/DataConnection:mApnContexts	Ljava/util/HashMap;
      //   671: invokevirtual 166	java/util/HashMap:keySet	()Ljava/util/Set;
      //   674: invokevirtual 170	java/util/ArrayList:addAll	(Ljava/util/Collection;)Z
      //   677: pop
      //   678: goto +51 -> 729
      //   681: aload_0
      //   682: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   685: astore 12
      //   687: new 42	java/lang/StringBuilder
      //   690: dup
      //   691: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   694: astore 13
      //   696: aload 13
      //   698: ldc -43
      //   700: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   703: pop
      //   704: aload 13
      //   706: aload_2
      //   707: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   710: pop
      //   711: aload 12
      //   713: aload 13
      //   715: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   718: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   721: aload 5
      //   723: aload 8
      //   725: invokevirtual 121	java/util/ArrayList:add	(Ljava/lang/Object;)Z
      //   728: pop
      //   729: goto -327 -> 402
      //   732: aload 8
      //   734: aload 7
      //   736: invokevirtual 217	com/android/internal/telephony/dataconnection/DataConnection:updateLinkProperty	(Landroid/telephony/data/DataCallResponse;)Lcom/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult;
      //   739: astore 12
      //   741: aload 12
      //   743: getfield 223	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:oldLp	Landroid/net/LinkProperties;
      //   746: aload 12
      //   748: getfield 226	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:newLp	Landroid/net/LinkProperties;
      //   751: invokevirtual 231	android/net/LinkProperties:equals	(Ljava/lang/Object;)Z
      //   754: ifeq +15 -> 769
      //   757: aload_0
      //   758: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   761: ldc -23
      //   763: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   766: goto -364 -> 402
      //   769: aload 12
      //   771: getfield 223	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:oldLp	Landroid/net/LinkProperties;
      //   774: aload 12
      //   776: getfield 226	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:newLp	Landroid/net/LinkProperties;
      //   779: invokevirtual 237	android/net/LinkProperties:isIdenticalInterfaceName	(Landroid/net/LinkProperties;)Z
      //   782: ifeq +441 -> 1223
      //   785: aload 12
      //   787: getfield 223	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:oldLp	Landroid/net/LinkProperties;
      //   790: aload 12
      //   792: getfield 226	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:newLp	Landroid/net/LinkProperties;
      //   795: invokevirtual 240	android/net/LinkProperties:isIdenticalDnses	(Landroid/net/LinkProperties;)Z
      //   798: ifeq +66 -> 864
      //   801: aload 12
      //   803: getfield 223	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:oldLp	Landroid/net/LinkProperties;
      //   806: aload 12
      //   808: getfield 226	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:newLp	Landroid/net/LinkProperties;
      //   811: invokevirtual 243	android/net/LinkProperties:isIdenticalRoutes	(Landroid/net/LinkProperties;)Z
      //   814: ifeq +50 -> 864
      //   817: aload 12
      //   819: getfield 223	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:oldLp	Landroid/net/LinkProperties;
      //   822: aload 12
      //   824: getfield 226	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:newLp	Landroid/net/LinkProperties;
      //   827: invokevirtual 246	android/net/LinkProperties:isIdenticalHttpProxy	(Landroid/net/LinkProperties;)Z
      //   830: ifeq +34 -> 864
      //   833: aload 12
      //   835: getfield 223	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:oldLp	Landroid/net/LinkProperties;
      //   838: aload 12
      //   840: getfield 226	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:newLp	Landroid/net/LinkProperties;
      //   843: invokevirtual 249	android/net/LinkProperties:isIdenticalAddresses	(Landroid/net/LinkProperties;)Z
      //   846: ifne +6 -> 852
      //   849: goto +15 -> 864
      //   852: aload_0
      //   853: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   856: ldc -5
      //   858: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   861: goto -459 -> 402
      //   864: aload 12
      //   866: getfield 223	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:oldLp	Landroid/net/LinkProperties;
      //   869: aload 12
      //   871: getfield 226	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:newLp	Landroid/net/LinkProperties;
      //   874: invokevirtual 255	android/net/LinkProperties:compareAddresses	(Landroid/net/LinkProperties;)Landroid/net/LinkProperties$CompareResult;
      //   877: astore 13
      //   879: aload_0
      //   880: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   883: astore_2
      //   884: new 42	java/lang/StringBuilder
      //   887: dup
      //   888: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   891: astore 14
      //   893: aload 14
      //   895: ldc_w 257
      //   898: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   901: pop
      //   902: aload 14
      //   904: aload 12
      //   906: getfield 223	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:oldLp	Landroid/net/LinkProperties;
      //   909: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   912: pop
      //   913: aload 14
      //   915: ldc_w 259
      //   918: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   921: pop
      //   922: aload 14
      //   924: aload 12
      //   926: getfield 226	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:newLp	Landroid/net/LinkProperties;
      //   929: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   932: pop
      //   933: aload 14
      //   935: ldc_w 261
      //   938: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   941: pop
      //   942: aload 14
      //   944: aload 13
      //   946: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   949: pop
      //   950: aload_2
      //   951: aload 14
      //   953: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   956: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   959: iconst_0
      //   960: istore 15
      //   962: aload 13
      //   964: getfield 267	android/net/LinkProperties$CompareResult:added	Ljava/util/List;
      //   967: invokeinterface 270 1 0
      //   972: astore 14
      //   974: aload 14
      //   976: invokeinterface 73 1 0
      //   981: ifeq +74 -> 1055
      //   984: aload 14
      //   986: invokeinterface 77 1 0
      //   991: checkcast 272	android/net/LinkAddress
      //   994: astore 16
      //   996: aload 13
      //   998: getfield 275	android/net/LinkProperties$CompareResult:removed	Ljava/util/List;
      //   1001: invokeinterface 270 1 0
      //   1006: astore_2
      //   1007: aload_2
      //   1008: invokeinterface 73 1 0
      //   1013: ifeq +39 -> 1052
      //   1016: aload_2
      //   1017: invokeinterface 77 1 0
      //   1022: checkcast 272	android/net/LinkAddress
      //   1025: astore 17
      //   1027: aload 17
      //   1029: invokevirtual 279	android/net/LinkAddress:getAddress	()Ljava/net/InetAddress;
      //   1032: aload 16
      //   1034: invokevirtual 279	android/net/LinkAddress:getAddress	()Ljava/net/InetAddress;
      //   1037: invokestatic 285	android/net/NetworkUtils:addressTypeMatches	(Ljava/net/InetAddress;Ljava/net/InetAddress;)Z
      //   1040: ifeq +9 -> 1049
      //   1043: iconst_1
      //   1044: istore 15
      //   1046: goto +6 -> 1052
      //   1049: goto -42 -> 1007
      //   1052: goto -78 -> 974
      //   1055: iload 15
      //   1057: ifeq +97 -> 1154
      //   1060: aload_0
      //   1061: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1064: astore 13
      //   1066: new 42	java/lang/StringBuilder
      //   1069: dup
      //   1070: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   1073: astore_2
      //   1074: aload_2
      //   1075: ldc_w 287
      //   1078: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1081: pop
      //   1082: aload_2
      //   1083: aload 8
      //   1085: getfield 132	com/android/internal/telephony/dataconnection/DataConnection:mApnContexts	Ljava/util/HashMap;
      //   1088: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   1091: pop
      //   1092: aload_2
      //   1093: ldc_w 289
      //   1096: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1099: pop
      //   1100: aload_2
      //   1101: aload 12
      //   1103: getfield 223	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:oldLp	Landroid/net/LinkProperties;
      //   1106: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   1109: pop
      //   1110: aload_2
      //   1111: ldc_w 259
      //   1114: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1117: pop
      //   1118: aload_2
      //   1119: aload 12
      //   1121: getfield 226	com/android/internal/telephony/dataconnection/DataConnection$UpdateLinkPropertyResult:newLp	Landroid/net/LinkProperties;
      //   1124: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   1127: pop
      //   1128: aload 13
      //   1130: aload_2
      //   1131: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   1134: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   1137: aload 6
      //   1139: aload 8
      //   1141: getfield 132	com/android/internal/telephony/dataconnection/DataConnection:mApnContexts	Ljava/util/HashMap;
      //   1144: invokevirtual 166	java/util/HashMap:keySet	()Ljava/util/Set;
      //   1147: invokevirtual 170	java/util/ArrayList:addAll	(Ljava/util/Collection;)Z
      //   1150: pop
      //   1151: goto +69 -> 1220
      //   1154: aload_0
      //   1155: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1158: ldc_w 291
      //   1161: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   1164: aload 8
      //   1166: getfield 132	com/android/internal/telephony/dataconnection/DataConnection:mApnContexts	Ljava/util/HashMap;
      //   1169: invokevirtual 166	java/util/HashMap:keySet	()Ljava/util/Set;
      //   1172: invokeinterface 294 1 0
      //   1177: astore 8
      //   1179: aload 8
      //   1181: invokeinterface 73 1 0
      //   1186: ifeq +34 -> 1220
      //   1189: aload 8
      //   1191: invokeinterface 77 1 0
      //   1196: checkcast 296	com/android/internal/telephony/dataconnection/ApnContext
      //   1199: astore_2
      //   1200: aload_0
      //   1201: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1204: invokestatic 187	com/android/internal/telephony/dataconnection/DcController:access$200	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/Phone;
      //   1207: ldc_w 298
      //   1210: aload_2
      //   1211: invokevirtual 301	com/android/internal/telephony/dataconnection/ApnContext:getApnType	()Ljava/lang/String;
      //   1214: invokevirtual 305	com/android/internal/telephony/Phone:notifyDataConnection	(Ljava/lang/String;Ljava/lang/String;)V
      //   1217: goto -38 -> 1179
      //   1220: goto +60 -> 1280
      //   1223: aload 6
      //   1225: aload 8
      //   1227: getfield 132	com/android/internal/telephony/dataconnection/DataConnection:mApnContexts	Ljava/util/HashMap;
      //   1230: invokevirtual 166	java/util/HashMap:keySet	()Ljava/util/Set;
      //   1233: invokevirtual 170	java/util/ArrayList:addAll	(Ljava/util/Collection;)Z
      //   1236: pop
      //   1237: aload_0
      //   1238: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1241: astore_2
      //   1242: new 42	java/lang/StringBuilder
      //   1245: dup
      //   1246: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   1249: astore 12
      //   1251: aload 12
      //   1253: ldc_w 307
      //   1256: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1259: pop
      //   1260: aload 12
      //   1262: aload 8
      //   1264: getfield 132	com/android/internal/telephony/dataconnection/DataConnection:mApnContexts	Ljava/util/HashMap;
      //   1267: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   1270: pop
      //   1271: aload_2
      //   1272: aload 12
      //   1274: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   1277: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   1280: aload 7
      //   1282: invokevirtual 148	android/telephony/data/DataCallResponse:getActive	()I
      //   1285: iconst_2
      //   1286: if_icmpne +6 -> 1292
      //   1289: iconst_1
      //   1290: istore 11
      //   1292: aload 7
      //   1294: invokevirtual 148	android/telephony/data/DataCallResponse:getActive	()I
      //   1297: iconst_1
      //   1298: if_icmpne +6 -> 1304
      //   1301: iconst_1
      //   1302: istore 10
      //   1304: goto -978 -> 326
      //   1307: iload 10
      //   1309: ifeq +34 -> 1343
      //   1312: iload 11
      //   1314: ifne +29 -> 1343
      //   1317: aload_0
      //   1318: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1321: ldc_w 309
      //   1324: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   1327: aload_0
      //   1328: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1331: invokestatic 152	com/android/internal/telephony/dataconnection/DcController:access$800	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/dataconnection/DcTracker;
      //   1334: getstatic 315	com/android/internal/telephony/DctConstants$Activity:DORMANT	Lcom/android/internal/telephony/DctConstants$Activity;
      //   1337: invokevirtual 319	com/android/internal/telephony/dataconnection/DcTracker:sendStopNetStatPoll	(Lcom/android/internal/telephony/DctConstants$Activity;)V
      //   1340: goto +74 -> 1414
      //   1343: aload_0
      //   1344: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1347: astore 4
      //   1349: new 42	java/lang/StringBuilder
      //   1352: dup
      //   1353: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   1356: astore_1
      //   1357: aload_1
      //   1358: ldc_w 321
      //   1361: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1364: pop
      //   1365: aload_1
      //   1366: iload 11
      //   1368: invokevirtual 324	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   1371: pop
      //   1372: aload_1
      //   1373: ldc_w 326
      //   1376: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1379: pop
      //   1380: aload_1
      //   1381: iload 10
      //   1383: invokevirtual 324	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   1386: pop
      //   1387: aload 4
      //   1389: aload_1
      //   1390: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   1393: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   1396: iload 11
      //   1398: ifeq +16 -> 1414
      //   1401: aload_0
      //   1402: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1405: invokestatic 152	com/android/internal/telephony/dataconnection/DcController:access$800	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/dataconnection/DcTracker;
      //   1408: getstatic 329	com/android/internal/telephony/DctConstants$Activity:NONE	Lcom/android/internal/telephony/DctConstants$Activity;
      //   1411: invokevirtual 332	com/android/internal/telephony/dataconnection/DcTracker:sendStartNetStatPoll	(Lcom/android/internal/telephony/DctConstants$Activity;)V
      //   1414: aload_0
      //   1415: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1418: astore_1
      //   1419: new 42	java/lang/StringBuilder
      //   1422: dup
      //   1423: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   1426: astore 4
      //   1428: aload 4
      //   1430: ldc 123
      //   1432: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1435: pop
      //   1436: aload 4
      //   1438: aload 5
      //   1440: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   1443: pop
      //   1444: aload 4
      //   1446: ldc_w 334
      //   1449: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1452: pop
      //   1453: aload 4
      //   1455: aload 6
      //   1457: invokevirtual 52	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   1460: pop
      //   1461: aload_1
      //   1462: aload 4
      //   1464: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   1467: invokestatic 62	com/android/internal/telephony/dataconnection/DcController:access$700	(Lcom/android/internal/telephony/dataconnection/DcController;Ljava/lang/String;)V
      //   1470: aload 6
      //   1472: invokevirtual 67	java/util/ArrayList:iterator	()Ljava/util/Iterator;
      //   1475: astore_1
      //   1476: aload_1
      //   1477: invokeinterface 73 1 0
      //   1482: ifeq +30 -> 1512
      //   1485: aload_1
      //   1486: invokeinterface 77 1 0
      //   1491: checkcast 296	com/android/internal/telephony/dataconnection/ApnContext
      //   1494: astore 4
      //   1496: aload_0
      //   1497: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1500: invokestatic 152	com/android/internal/telephony/dataconnection/DcController:access$800	(Lcom/android/internal/telephony/dataconnection/DcController;)Lcom/android/internal/telephony/dataconnection/DcTracker;
      //   1503: iconst_1
      //   1504: aload 4
      //   1506: invokevirtual 338	com/android/internal/telephony/dataconnection/DcTracker:sendCleanUpConnection	(ZLcom/android/internal/telephony/dataconnection/ApnContext;)V
      //   1509: goto -33 -> 1476
      //   1512: aload 5
      //   1514: invokevirtual 67	java/util/ArrayList:iterator	()Ljava/util/Iterator;
      //   1517: astore_1
      //   1518: aload_1
      //   1519: invokeinterface 73 1 0
      //   1524: ifeq +67 -> 1591
      //   1527: aload_1
      //   1528: invokeinterface 77 1 0
      //   1533: checkcast 103	com/android/internal/telephony/dataconnection/DataConnection
      //   1536: astore_2
      //   1537: aload_0
      //   1538: getfield 13	com/android/internal/telephony/dataconnection/DcController$DccDefaultState:this$0	Lcom/android/internal/telephony/dataconnection/DcController;
      //   1541: astore 4
      //   1543: new 42	java/lang/StringBuilder
      //   1546: dup
      //   1547: invokespecial 43	java/lang/StringBuilder:<init>	()V
      //   1550: astore_3
      //   1551: aload_3
      //   1552: ldc_w 340
      //   1555: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1558: pop
      //   1559: aload_3
      //   1560: aload_2
      //   1561: getfield 343	com/android/internal/telephony/dataconnection/DataConnection:mTag	I
      //   1564: invokevirtual 142	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   1567: pop
      //   1568: aload 4
      //   1570: aload_3
      //   1571: invokevirtual 58	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   1574: invokevirtual 117	com/android/internal/telephony/dataconnection/DcController:log	(Ljava/lang/String;)V
      //   1577: aload_2
      //   1578: ldc_w 344
      //   1581: aload_2
      //   1582: getfield 343	com/android/internal/telephony/dataconnection/DataConnection:mTag	I
      //   1585: invokevirtual 348	com/android/internal/telephony/dataconnection/DataConnection:sendMessage	(II)V
      //   1588: goto -70 -> 1518
      //   1591: return
      //   1592: astore_1
      //   1593: aload_2
      //   1594: monitorexit
      //   1595: aload_1
      //   1596: athrow
      //   1597: astore_1
      //   1598: goto -5 -> 1593
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	1601	0	this	DccDefaultState
      //   0	1601	1	paramArrayList	ArrayList<android.telephony.data.DataCallResponse>
      //   7	1587	2	localObject1	Object
      //   13	1558	3	localObject2	Object
      //   28	1541	4	localObject3	Object
      //   48	1465	5	localObject4	Object
      //   106	1365	6	localObject5	Object
      //   189	1104	7	localObject6	Object
      //   210	1053	8	localObject7	Object
      //   219	118	9	localObject8	Object
      //   309	1073	10	bool1	boolean
      //   312	1085	11	bool2	boolean
      //   417	856	12	localObject9	Object
      //   637	492	13	localObject10	Object
      //   891	94	14	localObject11	Object
      //   960	96	15	i	int
      //   994	39	16	localLinkAddress1	android.net.LinkAddress
      //   1025	3	17	localLinkAddress2	android.net.LinkAddress
      // Exception table:
      //   from	to	target	type
      //   10	44	1592	finally
      //   1593	1595	1597	finally
    }
    
    public void enter()
    {
      if ((mPhone != null) && (mDataServiceManager.getTransportType() == 1)) {
        mPhone.mCi.registerForRilConnected(getHandler(), 262149, null);
      }
      mDataServiceManager.registerForDataCallListChanged(getHandler(), 262151);
      if (mNetworkPolicyManager != null) {
        mNetworkPolicyManager.registerListener(mListener);
      }
    }
    
    public void exit()
    {
      Phone localPhone = mPhone;
      int i = 0;
      int j;
      if (localPhone != null) {
        j = 1;
      } else {
        j = 0;
      }
      if (mDataServiceManager.getTransportType() == 1) {
        i = 1;
      }
      if ((j & i) != 0) {
        mPhone.mCi.unregisterForRilConnected(getHandler());
      }
      mDataServiceManager.unregisterForDataCallListChanged(getHandler());
      if (mDcTesterDeactivateAll != null) {
        mDcTesterDeactivateAll.dispose();
      }
      if (mNetworkPolicyManager != null) {
        mNetworkPolicyManager.unregisterListener(mListener);
      }
    }
    
    public boolean processMessage(Message paramMessage)
    {
      int i = what;
      if (i != 262149)
      {
        if (i == 262151)
        {
          paramMessage = (AsyncResult)obj;
          if (exception == null) {
            onDataStateChanged((ArrayList)result);
          } else {
            log("DccDefaultState: EVENT_DATA_STATE_CHANGED: exception; likely radio not available, ignore");
          }
        }
      }
      else
      {
        paramMessage = (AsyncResult)obj;
        if (exception == null)
        {
          DcController localDcController = DcController.this;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("DccDefaultState: msg.what=EVENT_RIL_CONNECTED mRilVersion=");
          localStringBuilder.append(result);
          localDcController.log(localStringBuilder.toString());
        }
        else
        {
          log("DccDefaultState: Unexpected exception on EVENT_RIL_CONNECTED");
        }
      }
      return true;
    }
  }
}
