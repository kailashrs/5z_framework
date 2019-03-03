package android.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.text.style.AccessibilityClickableSpan;
import android.text.style.ClickableSpan;
import android.util.LongSparseArray;
import android.util.Slog;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.accessibility.AccessibilityRequestPreparer;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SomeArgs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

final class AccessibilityInteractionController
{
  private static final boolean CONSIDER_REQUEST_PREPARERS = false;
  private static final boolean ENFORCE_NODE_TREE_CONSISTENT = false;
  private static final boolean IGNORE_REQUEST_PREPARERS = true;
  private static final String LOG_TAG = "AccessibilityInteractionController";
  private static final long REQUEST_PREPARER_TIMEOUT_MS = 500L;
  private final AccessibilityManager mA11yManager;
  @GuardedBy("mLock")
  private int mActiveRequestPreparerId;
  private AddNodeInfosForViewId mAddNodeInfosForViewId;
  private final Handler mHandler;
  private final Object mLock = new Object();
  @GuardedBy("mLock")
  private List<MessageHolder> mMessagesWaitingForRequestPreparer;
  private final long mMyLooperThreadId;
  private final int mMyProcessId;
  @GuardedBy("mLock")
  private int mNumActiveRequestPreparers;
  private final AccessibilityNodePrefetcher mPrefetcher;
  private final ArrayList<AccessibilityNodeInfo> mTempAccessibilityNodeInfoList = new ArrayList();
  private final ArrayList<View> mTempArrayList = new ArrayList();
  private final Point mTempPoint = new Point();
  private final Rect mTempRect = new Rect();
  private final Rect mTempRect1 = new Rect();
  private final Rect mTempRect2 = new Rect();
  private final ViewRootImpl mViewRootImpl;
  
  public AccessibilityInteractionController(ViewRootImpl paramViewRootImpl)
  {
    Looper localLooper = mHandler.getLooper();
    mMyLooperThreadId = localLooper.getThread().getId();
    mMyProcessId = Process.myPid();
    mHandler = new PrivateHandler(localLooper);
    mViewRootImpl = paramViewRootImpl;
    mPrefetcher = new AccessibilityNodePrefetcher(null);
    mA11yManager = ((AccessibilityManager)mViewRootImpl.mContext.getSystemService(AccessibilityManager.class));
  }
  
  private void adjustIsVisibleToUserIfNeeded(AccessibilityNodeInfo paramAccessibilityNodeInfo, Region paramRegion)
  {
    if ((paramRegion != null) && (paramAccessibilityNodeInfo != null))
    {
      Rect localRect = mTempRect;
      paramAccessibilityNodeInfo.getBoundsInScreen(localRect);
      if (paramRegion.quickReject(localRect)) {
        paramAccessibilityNodeInfo.setVisibleToUser(false);
      }
      return;
    }
  }
  
  private void adjustIsVisibleToUserIfNeeded(List<AccessibilityNodeInfo> paramList, Region paramRegion)
  {
    if ((paramRegion != null) && (paramList != null))
    {
      int i = paramList.size();
      for (int j = 0; j < i; j++) {
        adjustIsVisibleToUserIfNeeded((AccessibilityNodeInfo)paramList.get(j), paramRegion);
      }
      return;
    }
  }
  
  private void applyAppScaleAndMagnificationSpecIfNeeded(AccessibilityNodeInfo paramAccessibilityNodeInfo, MagnificationSpec paramMagnificationSpec)
  {
    if (paramAccessibilityNodeInfo == null) {
      return;
    }
    float f = mViewRootImpl.mAttachInfo.mApplicationScale;
    if (!shouldApplyAppScaleAndMagnificationSpec(f, paramMagnificationSpec)) {
      return;
    }
    Object localObject1 = mTempRect;
    Rect localRect = mTempRect1;
    paramAccessibilityNodeInfo.getBoundsInParent((Rect)localObject1);
    paramAccessibilityNodeInfo.getBoundsInScreen(localRect);
    if (f != 1.0F)
    {
      ((Rect)localObject1).scale(f);
      localRect.scale(f);
    }
    if (paramMagnificationSpec != null)
    {
      ((Rect)localObject1).scale(scale);
      localRect.scale(scale);
      localRect.offset((int)offsetX, (int)offsetY);
    }
    paramAccessibilityNodeInfo.setBoundsInParent((Rect)localObject1);
    paramAccessibilityNodeInfo.setBoundsInScreen(localRect);
    Object localObject2;
    int i;
    if (paramAccessibilityNodeInfo.hasExtras())
    {
      localObject2 = paramAccessibilityNodeInfo.getExtras().getParcelableArray("android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_KEY");
      if (localObject2 != null) {
        for (i = 0; i < localObject2.length; i++)
        {
          localObject1 = (RectF)localObject2[i];
          ((RectF)localObject1).scale(f);
          if (paramMagnificationSpec != null)
          {
            ((RectF)localObject1).scale(scale);
            ((RectF)localObject1).offset(offsetX, offsetY);
          }
        }
      }
    }
    if (paramMagnificationSpec != null)
    {
      localObject2 = mViewRootImpl.mAttachInfo;
      if (mDisplay == null) {
        return;
      }
      f = mApplicationScale * scale;
      localObject1 = mTempRect1;
      left = ((int)(mWindowLeft * f + offsetX));
      top = ((int)(mWindowTop * f + offsetY));
      right = ((int)(left + mViewRootImpl.mWidth * f));
      bottom = ((int)(top + mViewRootImpl.mHeight * f));
      mDisplay.getRealSize(mTempPoint);
      i = mTempPoint.x;
      int j = mTempPoint.y;
      paramMagnificationSpec = mTempRect2;
      paramMagnificationSpec.set(0, 0, i, j);
      if (!((Rect)localObject1).intersect(paramMagnificationSpec)) {
        paramMagnificationSpec.setEmpty();
      }
      if (!((Rect)localObject1).intersects(left, top, right, bottom)) {
        paramAccessibilityNodeInfo.setVisibleToUser(false);
      }
    }
  }
  
  private void applyAppScaleAndMagnificationSpecIfNeeded(List<AccessibilityNodeInfo> paramList, MagnificationSpec paramMagnificationSpec)
  {
    if (paramList == null) {
      return;
    }
    if (shouldApplyAppScaleAndMagnificationSpec(mViewRootImpl.mAttachInfo.mApplicationScale, paramMagnificationSpec))
    {
      int i = paramList.size();
      for (int j = 0; j < i; j++) {
        applyAppScaleAndMagnificationSpecIfNeeded((AccessibilityNodeInfo)paramList.get(j), paramMagnificationSpec);
      }
    }
  }
  
  /* Error */
  private void findAccessibilityNodeInfoByAccessibilityIdUiThread(Message paramMessage)
  {
    // Byte code:
    //   0: aload_1
    //   1: getfield 360	android/os/Message:arg1	I
    //   4: istore_2
    //   5: aload_1
    //   6: getfield 363	android/os/Message:obj	Ljava/lang/Object;
    //   9: checkcast 365	com/android/internal/os/SomeArgs
    //   12: astore_1
    //   13: aload_1
    //   14: getfield 368	com/android/internal/os/SomeArgs:argi1	I
    //   17: istore_3
    //   18: aload_1
    //   19: getfield 371	com/android/internal/os/SomeArgs:argi2	I
    //   22: istore 4
    //   24: aload_1
    //   25: getfield 374	com/android/internal/os/SomeArgs:argi3	I
    //   28: istore 5
    //   30: aload_1
    //   31: getfield 376	com/android/internal/os/SomeArgs:arg1	Ljava/lang/Object;
    //   34: checkcast 378	android/view/accessibility/IAccessibilityInteractionConnectionCallback
    //   37: astore 6
    //   39: aload_1
    //   40: getfield 381	com/android/internal/os/SomeArgs:arg2	Ljava/lang/Object;
    //   43: checkcast 257	android/view/MagnificationSpec
    //   46: astore 7
    //   48: aload_1
    //   49: getfield 384	com/android/internal/os/SomeArgs:arg3	Ljava/lang/Object;
    //   52: checkcast 210	android/graphics/Region
    //   55: astore 8
    //   57: aload_1
    //   58: getfield 387	com/android/internal/os/SomeArgs:arg4	Ljava/lang/Object;
    //   61: checkcast 287	android/os/Bundle
    //   64: astore 9
    //   66: aload_1
    //   67: invokevirtual 390	com/android/internal/os/SomeArgs:recycle	()V
    //   70: aload_0
    //   71: getfield 75	android/view/AccessibilityInteractionController:mTempAccessibilityNodeInfoList	Ljava/util/ArrayList;
    //   74: astore 10
    //   76: aload 10
    //   78: invokeinterface 393 1 0
    //   83: aload_0
    //   84: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   87: getfield 397	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   90: ifnull +108 -> 198
    //   93: aload_0
    //   94: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   97: getfield 238	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   100: ifnonnull +6 -> 106
    //   103: goto +95 -> 198
    //   106: aload_0
    //   107: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   110: getfield 238	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   113: iload_2
    //   114: putfield 400	android/view/View$AttachInfo:mAccessibilityFetchFlags	I
    //   117: iload_3
    //   118: ldc_w 401
    //   121: if_icmpne +18 -> 139
    //   124: aload_0
    //   125: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   128: getfield 397	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   131: astore_1
    //   132: goto +13 -> 145
    //   135: astore_1
    //   136: goto +78 -> 214
    //   139: aload_0
    //   140: iload_3
    //   141: invokespecial 405	android/view/AccessibilityInteractionController:findViewByAccessibilityId	(I)Landroid/view/View;
    //   144: astore_1
    //   145: aload_1
    //   146: ifnull +37 -> 183
    //   149: aload_0
    //   150: aload_1
    //   151: invokespecial 176	android/view/AccessibilityInteractionController:isShown	(Landroid/view/View;)Z
    //   154: ifeq +29 -> 183
    //   157: aload_0
    //   158: getfield 138	android/view/AccessibilityInteractionController:mPrefetcher	Landroid/view/AccessibilityInteractionController$AccessibilityNodePrefetcher;
    //   161: astore 11
    //   163: aload 11
    //   165: aload_1
    //   166: iload 4
    //   168: iload_2
    //   169: aload 10
    //   171: aload 9
    //   173: invokevirtual 409	android/view/AccessibilityInteractionController$AccessibilityNodePrefetcher:prefetchAccessibilityNodeInfos	(Landroid/view/View;IILjava/util/List;Landroid/os/Bundle;)V
    //   176: goto +7 -> 183
    //   179: astore_1
    //   180: goto +34 -> 214
    //   183: aload_0
    //   184: aload 10
    //   186: aload 6
    //   188: iload 5
    //   190: aload 7
    //   192: aload 8
    //   194: invokespecial 413	android/view/AccessibilityInteractionController:updateInfosForViewportAndReturnFindNodeResult	(Ljava/util/List;Landroid/view/accessibility/IAccessibilityInteractionConnectionCallback;ILandroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   197: return
    //   198: aload_0
    //   199: aload 10
    //   201: aload 6
    //   203: iload 5
    //   205: aload 7
    //   207: aload 8
    //   209: invokespecial 413	android/view/AccessibilityInteractionController:updateInfosForViewportAndReturnFindNodeResult	(Ljava/util/List;Landroid/view/accessibility/IAccessibilityInteractionConnectionCallback;ILandroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   212: return
    //   213: astore_1
    //   214: aload_0
    //   215: aload 10
    //   217: aload 6
    //   219: iload 5
    //   221: aload 7
    //   223: aload 8
    //   225: invokespecial 413	android/view/AccessibilityInteractionController:updateInfosForViewportAndReturnFindNodeResult	(Ljava/util/List;Landroid/view/accessibility/IAccessibilityInteractionConnectionCallback;ILandroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   228: aload_1
    //   229: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	230	0	this	AccessibilityInteractionController
    //   0	230	1	paramMessage	Message
    //   4	165	2	i	int
    //   17	124	3	j	int
    //   22	145	4	k	int
    //   28	192	5	m	int
    //   37	181	6	localIAccessibilityInteractionConnectionCallback	IAccessibilityInteractionConnectionCallback
    //   46	176	7	localMagnificationSpec	MagnificationSpec
    //   55	169	8	localRegion	Region
    //   64	108	9	localBundle	Bundle
    //   74	142	10	localArrayList	ArrayList
    //   161	3	11	localAccessibilityNodePrefetcher	AccessibilityNodePrefetcher
    // Exception table:
    //   from	to	target	type
    //   124	132	135	finally
    //   163	176	179	finally
    //   83	103	213	finally
    //   106	117	213	finally
    //   139	145	213	finally
    //   149	163	213	finally
  }
  
  private void findAccessibilityNodeInfosByTextUiThread(Message paramMessage)
  {
    int i = arg1;
    paramMessage = (SomeArgs)obj;
    String str = (String)arg1;
    IAccessibilityInteractionConnectionCallback localIAccessibilityInteractionConnectionCallback = (IAccessibilityInteractionConnectionCallback)arg2;
    MagnificationSpec localMagnificationSpec = (MagnificationSpec)arg3;
    int j = argi1;
    int k = argi2;
    int m = argi3;
    Region localRegion = (Region)arg4;
    paramMessage.recycle();
    Object localObject1 = null;
    ArrayList localArrayList = null;
    try
    {
      if ((mViewRootImpl.mView != null) && (mViewRootImpl.mAttachInfo != null))
      {
        mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = i;
        View localView;
        if (j != 2147483646)
        {
          paramMessage = localArrayList;
          try
          {
            localView = findViewByAccessibilityId(j);
          }
          finally
          {
            break label435;
          }
        }
        else
        {
          localView = mViewRootImpl.mView;
        }
        Object localObject3 = localObject1;
        if (localView != null)
        {
          paramMessage = localArrayList;
          localObject3 = localObject1;
          if (isShown(localView))
          {
            paramMessage = localArrayList;
            localObject3 = localView.getAccessibilityNodeProvider();
            if (localObject3 != null)
            {
              paramMessage = localArrayList;
              localObject3 = ((AccessibilityNodeProvider)localObject3).findAccessibilityNodeInfosByText(str, k);
            }
            else
            {
              localObject3 = localObject1;
              if (k == -1)
              {
                paramMessage = localArrayList;
                Object localObject5 = mTempArrayList;
                paramMessage = localArrayList;
                ((ArrayList)localObject5).clear();
                paramMessage = localArrayList;
                localView.findViewsWithText((ArrayList)localObject5, str, 7);
                paramMessage = localArrayList;
                localObject3 = localObject1;
                if (!((ArrayList)localObject5).isEmpty())
                {
                  paramMessage = localArrayList;
                  localArrayList = mTempAccessibilityNodeInfoList;
                  paramMessage = localArrayList;
                  localArrayList.clear();
                  paramMessage = localArrayList;
                  i = ((ArrayList)localObject5).size();
                  j = 0;
                  localObject1 = localObject5;
                  for (;;)
                  {
                    localObject3 = localArrayList;
                    if (j >= i) {
                      break;
                    }
                    paramMessage = localArrayList;
                    localObject5 = (View)localObject1.get(j);
                    paramMessage = localArrayList;
                    if (isShown((View)localObject5))
                    {
                      paramMessage = localArrayList;
                      localObject3 = ((View)localObject5).getAccessibilityNodeProvider();
                      if (localObject3 != null)
                      {
                        paramMessage = localArrayList;
                        localObject3 = ((AccessibilityNodeProvider)localObject3).findAccessibilityNodeInfosByText(str, -1);
                        if (localObject3 != null)
                        {
                          paramMessage = localArrayList;
                          localArrayList.addAll((Collection)localObject3);
                        }
                      }
                      else
                      {
                        paramMessage = localArrayList;
                        localArrayList.add(((View)localObject5).createAccessibilityNodeInfo());
                      }
                    }
                    j++;
                  }
                }
              }
            }
          }
        }
        updateInfosForViewportAndReturnFindNodeResult((List)localObject3, localIAccessibilityInteractionConnectionCallback, m, localMagnificationSpec, localRegion);
        return;
      }
      updateInfosForViewportAndReturnFindNodeResult(null, localIAccessibilityInteractionConnectionCallback, m, localMagnificationSpec, localRegion);
      return;
    }
    finally
    {
      paramMessage = null;
      label435:
      updateInfosForViewportAndReturnFindNodeResult(paramMessage, localIAccessibilityInteractionConnectionCallback, m, localMagnificationSpec, localRegion);
    }
  }
  
  /* Error */
  private void findAccessibilityNodeInfosByViewIdUiThread(Message paramMessage)
  {
    // Byte code:
    //   0: aload_1
    //   1: getfield 360	android/os/Message:arg1	I
    //   4: istore_2
    //   5: aload_1
    //   6: getfield 451	android/os/Message:arg2	I
    //   9: istore_3
    //   10: aload_1
    //   11: getfield 363	android/os/Message:obj	Ljava/lang/Object;
    //   14: checkcast 365	com/android/internal/os/SomeArgs
    //   17: astore_1
    //   18: aload_1
    //   19: getfield 368	com/android/internal/os/SomeArgs:argi1	I
    //   22: istore 4
    //   24: aload_1
    //   25: getfield 376	com/android/internal/os/SomeArgs:arg1	Ljava/lang/Object;
    //   28: checkcast 378	android/view/accessibility/IAccessibilityInteractionConnectionCallback
    //   31: astore 5
    //   33: aload_1
    //   34: getfield 381	com/android/internal/os/SomeArgs:arg2	Ljava/lang/Object;
    //   37: checkcast 257	android/view/MagnificationSpec
    //   40: astore 6
    //   42: aload_1
    //   43: getfield 384	com/android/internal/os/SomeArgs:arg3	Ljava/lang/Object;
    //   46: checkcast 415	java/lang/String
    //   49: astore 7
    //   51: aload_1
    //   52: getfield 387	com/android/internal/os/SomeArgs:arg4	Ljava/lang/Object;
    //   55: checkcast 210	android/graphics/Region
    //   58: astore 8
    //   60: aload_1
    //   61: invokevirtual 390	com/android/internal/os/SomeArgs:recycle	()V
    //   64: aload_0
    //   65: getfield 75	android/view/AccessibilityInteractionController:mTempAccessibilityNodeInfoList	Ljava/util/ArrayList;
    //   68: astore 9
    //   70: aload 9
    //   72: invokeinterface 393 1 0
    //   77: aload_0
    //   78: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   81: getfield 397	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   84: ifnull +166 -> 250
    //   87: aload_0
    //   88: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   91: getfield 238	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   94: ifnonnull +6 -> 100
    //   97: goto +153 -> 250
    //   100: aload_0
    //   101: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   104: getfield 238	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   107: iload_2
    //   108: putfield 400	android/view/View$AttachInfo:mAccessibilityFetchFlags	I
    //   111: iload_3
    //   112: ldc_w 401
    //   115: if_icmpeq +16 -> 131
    //   118: aload_0
    //   119: iload_3
    //   120: invokespecial 405	android/view/AccessibilityInteractionController:findViewByAccessibilityId	(I)Landroid/view/View;
    //   123: astore_1
    //   124: goto +15 -> 139
    //   127: astore_1
    //   128: goto +138 -> 266
    //   131: aload_0
    //   132: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   135: getfield 397	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   138: astore_1
    //   139: aload_1
    //   140: ifnull +95 -> 235
    //   143: aload_1
    //   144: invokevirtual 455	android/view/View:getContext	()Landroid/content/Context;
    //   147: invokevirtual 459	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   150: aload 7
    //   152: aconst_null
    //   153: aconst_null
    //   154: invokevirtual 465	android/content/res/Resources:getIdentifier	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
    //   157: istore_3
    //   158: iload_3
    //   159: ifgt +18 -> 177
    //   162: aload_0
    //   163: aload 9
    //   165: aload 5
    //   167: iload 4
    //   169: aload 6
    //   171: aload 8
    //   173: invokespecial 413	android/view/AccessibilityInteractionController:updateInfosForViewportAndReturnFindNodeResult	(Ljava/util/List;Landroid/view/accessibility/IAccessibilityInteractionConnectionCallback;ILandroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   176: return
    //   177: aload_0
    //   178: getfield 467	android/view/AccessibilityInteractionController:mAddNodeInfosForViewId	Landroid/view/AccessibilityInteractionController$AddNodeInfosForViewId;
    //   181: ifnonnull +21 -> 202
    //   184: new 11	android/view/AccessibilityInteractionController$AddNodeInfosForViewId
    //   187: astore 7
    //   189: aload 7
    //   191: aload_0
    //   192: aconst_null
    //   193: invokespecial 468	android/view/AccessibilityInteractionController$AddNodeInfosForViewId:<init>	(Landroid/view/AccessibilityInteractionController;Landroid/view/AccessibilityInteractionController$1;)V
    //   196: aload_0
    //   197: aload 7
    //   199: putfield 467	android/view/AccessibilityInteractionController:mAddNodeInfosForViewId	Landroid/view/AccessibilityInteractionController$AddNodeInfosForViewId;
    //   202: aload_0
    //   203: getfield 467	android/view/AccessibilityInteractionController:mAddNodeInfosForViewId	Landroid/view/AccessibilityInteractionController$AddNodeInfosForViewId;
    //   206: iload_3
    //   207: aload 9
    //   209: invokevirtual 472	android/view/AccessibilityInteractionController$AddNodeInfosForViewId:init	(ILjava/util/List;)V
    //   212: aload_1
    //   213: aload_0
    //   214: getfield 467	android/view/AccessibilityInteractionController:mAddNodeInfosForViewId	Landroid/view/AccessibilityInteractionController$AddNodeInfosForViewId;
    //   217: invokevirtual 476	android/view/View:findViewByPredicate	(Ljava/util/function/Predicate;)Landroid/view/View;
    //   220: pop
    //   221: aload_0
    //   222: getfield 467	android/view/AccessibilityInteractionController:mAddNodeInfosForViewId	Landroid/view/AccessibilityInteractionController$AddNodeInfosForViewId;
    //   225: invokevirtual 479	android/view/AccessibilityInteractionController$AddNodeInfosForViewId:reset	()V
    //   228: goto +7 -> 235
    //   231: astore_1
    //   232: goto +34 -> 266
    //   235: aload_0
    //   236: aload 9
    //   238: aload 5
    //   240: iload 4
    //   242: aload 6
    //   244: aload 8
    //   246: invokespecial 413	android/view/AccessibilityInteractionController:updateInfosForViewportAndReturnFindNodeResult	(Ljava/util/List;Landroid/view/accessibility/IAccessibilityInteractionConnectionCallback;ILandroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   249: return
    //   250: aload_0
    //   251: aload 9
    //   253: aload 5
    //   255: iload 4
    //   257: aload 6
    //   259: aload 8
    //   261: invokespecial 413	android/view/AccessibilityInteractionController:updateInfosForViewportAndReturnFindNodeResult	(Ljava/util/List;Landroid/view/accessibility/IAccessibilityInteractionConnectionCallback;ILandroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   264: return
    //   265: astore_1
    //   266: aload_0
    //   267: aload 9
    //   269: aload 5
    //   271: iload 4
    //   273: aload 6
    //   275: aload 8
    //   277: invokespecial 413	android/view/AccessibilityInteractionController:updateInfosForViewportAndReturnFindNodeResult	(Ljava/util/List;Landroid/view/accessibility/IAccessibilityInteractionConnectionCallback;ILandroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   280: aload_1
    //   281: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	282	0	this	AccessibilityInteractionController
    //   0	282	1	paramMessage	Message
    //   4	104	2	i	int
    //   9	198	3	j	int
    //   22	250	4	k	int
    //   31	239	5	localIAccessibilityInteractionConnectionCallback	IAccessibilityInteractionConnectionCallback
    //   40	234	6	localMagnificationSpec	MagnificationSpec
    //   49	149	7	localObject	Object
    //   58	218	8	localRegion	Region
    //   68	200	9	localArrayList	ArrayList
    // Exception table:
    //   from	to	target	type
    //   118	124	127	finally
    //   177	202	231	finally
    //   202	228	231	finally
    //   77	97	265	finally
    //   100	111	265	finally
    //   131	139	265	finally
    //   143	158	265	finally
  }
  
  private void findFocusUiThread(Message paramMessage)
  {
    int i = arg1;
    int j = arg2;
    paramMessage = (SomeArgs)obj;
    int k = argi1;
    int m = argi2;
    int n = argi3;
    IAccessibilityInteractionConnectionCallback localIAccessibilityInteractionConnectionCallback = (IAccessibilityInteractionConnectionCallback)arg1;
    MagnificationSpec localMagnificationSpec = (MagnificationSpec)arg2;
    Region localRegion = (Region)arg3;
    paramMessage.recycle();
    View localView = null;
    AccessibilityNodeProvider localAccessibilityNodeProvider = null;
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = localObject1;
    try
    {
      if (mViewRootImpl.mView != null)
      {
        localObject3 = localObject1;
        if (mViewRootImpl.mAttachInfo != null)
        {
          localObject3 = localObject1;
          mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = i;
          Object localObject4;
          if (m != 2147483646)
          {
            localObject3 = localObject1;
            localObject4 = findViewByAccessibilityId(m);
          }
          else
          {
            localObject3 = localObject1;
            localObject4 = mViewRootImpl.mView;
          }
          paramMessage = localAccessibilityNodeProvider;
          if (localObject4 != null)
          {
            paramMessage = localAccessibilityNodeProvider;
            localObject3 = localObject1;
            if (isShown((View)localObject4))
            {
              switch (j)
              {
              default: 
                localObject3 = localObject1;
                paramMessage = new java/lang/IllegalArgumentException;
                break;
              case 2: 
                localObject3 = localObject1;
                localView = mViewRootImpl.mAccessibilityFocusedHost;
                paramMessage = localAccessibilityNodeProvider;
                if (localView == null) {
                  break label502;
                }
                localObject3 = localObject1;
                if (!ViewRootImpl.isViewDescendantOf(localView, (View)localObject4))
                {
                  paramMessage = localAccessibilityNodeProvider;
                }
                else
                {
                  localObject3 = localObject1;
                  if (!isShown(localView))
                  {
                    paramMessage = localAccessibilityNodeProvider;
                  }
                  else
                  {
                    localObject3 = localObject1;
                    if (localView.getAccessibilityNodeProvider() != null)
                    {
                      paramMessage = localObject2;
                      localObject3 = localObject1;
                      if (mViewRootImpl.mAccessibilityFocusedVirtualView != null)
                      {
                        localObject3 = localObject1;
                        paramMessage = AccessibilityNodeInfo.obtain(mViewRootImpl.mAccessibilityFocusedVirtualView);
                      }
                    }
                    else
                    {
                      paramMessage = localObject2;
                      if (n == -1)
                      {
                        localObject3 = localObject1;
                        paramMessage = localView.createAccessibilityNodeInfo();
                      }
                    }
                  }
                }
                break;
              case 1: 
                localObject3 = localObject1;
                localObject4 = ((View)localObject4).findFocus();
                paramMessage = localAccessibilityNodeProvider;
                if (localObject4 == null) {
                  break label502;
                }
                localObject3 = localObject1;
                if (!isShown((View)localObject4))
                {
                  paramMessage = localAccessibilityNodeProvider;
                }
                else
                {
                  localObject3 = localObject1;
                  localAccessibilityNodeProvider = ((View)localObject4).getAccessibilityNodeProvider();
                  paramMessage = localView;
                  if (localAccessibilityNodeProvider != null)
                  {
                    localObject3 = localObject1;
                    paramMessage = localAccessibilityNodeProvider.findFocus(j);
                  }
                  localObject3 = paramMessage;
                  if (paramMessage == null)
                  {
                    localObject3 = paramMessage;
                    paramMessage = ((View)localObject4).createAccessibilityNodeInfo();
                    localObject3 = paramMessage;
                  }
                  paramMessage = (Message)localObject3;
                }
                break;
              }
              localObject3 = localObject1;
              localObject4 = new java/lang/StringBuilder;
              localObject3 = localObject1;
              ((StringBuilder)localObject4).<init>();
              localObject3 = localObject1;
              ((StringBuilder)localObject4).append("Unknown focus type: ");
              localObject3 = localObject1;
              ((StringBuilder)localObject4).append(j);
              localObject3 = localObject1;
              paramMessage.<init>(((StringBuilder)localObject4).toString());
              localObject3 = localObject1;
              throw paramMessage;
            }
          }
          label502:
          updateInfoForViewportAndReturnFindNodeResult(paramMessage, localIAccessibilityInteractionConnectionCallback, k, localMagnificationSpec, localRegion);
          return;
        }
      }
      updateInfoForViewportAndReturnFindNodeResult(null, localIAccessibilityInteractionConnectionCallback, k, localMagnificationSpec, localRegion);
      return;
    }
    finally
    {
      updateInfoForViewportAndReturnFindNodeResult((AccessibilityNodeInfo)localObject3, localIAccessibilityInteractionConnectionCallback, k, localMagnificationSpec, localRegion);
    }
  }
  
  private View findViewByAccessibilityId(int paramInt)
  {
    View localView = mViewRootImpl.mView;
    if (localView == null) {
      return null;
    }
    localView = localView.findViewByAccessibilityId(paramInt);
    if ((localView != null) && (!isShown(localView))) {
      return null;
    }
    return localView;
  }
  
  private void focusSearchUiThread(Message paramMessage)
  {
    int i = arg1;
    int j = arg2;
    paramMessage = (SomeArgs)obj;
    int k = argi2;
    int m = argi3;
    IAccessibilityInteractionConnectionCallback localIAccessibilityInteractionConnectionCallback = (IAccessibilityInteractionConnectionCallback)arg1;
    MagnificationSpec localMagnificationSpec = (MagnificationSpec)arg2;
    Region localRegion = (Region)arg3;
    paramMessage.recycle();
    try
    {
      if ((mViewRootImpl.mView != null) && (mViewRootImpl.mAttachInfo != null))
      {
        mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = i;
        if (j != 2147483646) {
          paramMessage = findViewByAccessibilityId(j);
        } else {
          paramMessage = mViewRootImpl.mView;
        }
        if ((paramMessage != null) && (isShown(paramMessage)))
        {
          paramMessage = paramMessage.focusSearch(k);
          if (paramMessage != null)
          {
            paramMessage = paramMessage.createAccessibilityNodeInfo();
            break label152;
          }
        }
        paramMessage = null;
        label152:
        updateInfoForViewportAndReturnFindNodeResult(paramMessage, localIAccessibilityInteractionConnectionCallback, m, localMagnificationSpec, localRegion);
        return;
      }
      return;
    }
    finally
    {
      updateInfoForViewportAndReturnFindNodeResult(null, localIAccessibilityInteractionConnectionCallback, m, localMagnificationSpec, localRegion);
    }
  }
  
  private boolean handleClickableSpanActionUiThread(View paramView, int paramInt, Bundle paramBundle)
  {
    Parcelable localParcelable = paramBundle.getParcelable("android.view.accessibility.action.ACTION_ARGUMENT_ACCESSIBLE_CLICKABLE_SPAN");
    if (!(localParcelable instanceof AccessibilityClickableSpan)) {
      return false;
    }
    paramBundle = null;
    AccessibilityNodeProvider localAccessibilityNodeProvider = paramView.getAccessibilityNodeProvider();
    if (localAccessibilityNodeProvider != null) {
      paramBundle = localAccessibilityNodeProvider.createAccessibilityNodeInfo(paramInt);
    } else if (paramInt == -1) {
      paramBundle = paramView.createAccessibilityNodeInfo();
    }
    if (paramBundle == null) {
      return false;
    }
    paramBundle = ((AccessibilityClickableSpan)localParcelable).findClickableSpan(paramBundle.getOriginalText());
    if (paramBundle != null)
    {
      paramBundle.onClick(paramView);
      return true;
    }
    return false;
  }
  
  private boolean holdOffMessageIfNeeded(Message paramMessage, int paramInt, long paramLong)
  {
    synchronized (mLock)
    {
      if (mNumActiveRequestPreparers != 0)
      {
        queueMessageToHandleOncePrepared(paramMessage, paramInt, paramLong);
        return true;
      }
      int i = what;
      int j = 0;
      if (i != 2) {
        return false;
      }
      SomeArgs localSomeArgs1 = (SomeArgs)obj;
      Bundle localBundle = (Bundle)arg4;
      if (localBundle == null) {
        return false;
      }
      i = argi1;
      List localList = mA11yManager.getRequestPreparersForAccessibilityId(i);
      if (localList == null) {
        return false;
      }
      String str = localBundle.getString("android.view.accessibility.AccessibilityNodeInfo.extra_data_requested");
      if (str == null) {
        return false;
      }
      mNumActiveRequestPreparers = localList.size();
      while (j < localList.size())
      {
        Message localMessage1 = mHandler.obtainMessage(7);
        SomeArgs localSomeArgs2 = SomeArgs.obtain();
        if (argi2 == Integer.MAX_VALUE) {
          i = -1;
        } else {
          i = argi2;
        }
        argi1 = i;
        arg1 = localList.get(j);
        arg2 = str;
        arg3 = localBundle;
        Message localMessage2 = mHandler.obtainMessage(8);
        i = mActiveRequestPreparerId + 1;
        mActiveRequestPreparerId = i;
        arg1 = i;
        arg4 = localMessage2;
        obj = localSomeArgs2;
        scheduleMessage(localMessage1, paramInt, paramLong, true);
        mHandler.obtainMessage(9);
        mHandler.sendEmptyMessageDelayed(9, 500L);
        j++;
      }
      queueMessageToHandleOncePrepared(paramMessage, paramInt, paramLong);
      return true;
    }
  }
  
  private boolean isShown(View paramView)
  {
    boolean bool;
    if ((mAttachInfo != null) && (mAttachInfo.mWindowVisibility == 0) && (paramView.isShown())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void performAccessibilityActionUiThread(Message paramMessage)
  {
    int i = arg1;
    int j = arg2;
    paramMessage = (SomeArgs)obj;
    int k = argi1;
    int m = argi2;
    int n = argi3;
    IAccessibilityInteractionConnectionCallback localIAccessibilityInteractionConnectionCallback = (IAccessibilityInteractionConnectionCallback)arg1;
    Bundle localBundle = (Bundle)arg2;
    paramMessage.recycle();
    boolean bool1 = false;
    try
    {
      if ((mViewRootImpl.mView != null) && (mViewRootImpl.mAttachInfo != null) && (!mViewRootImpl.mStopped) && (!mViewRootImpl.mPausedForTransition))
      {
        mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = i;
        if (j != 2147483646) {
          paramMessage = findViewByAccessibilityId(j);
        } else {
          paramMessage = mViewRootImpl.mView;
        }
        boolean bool2 = bool1;
        if (paramMessage != null)
        {
          bool2 = bool1;
          if (isShown(paramMessage)) {
            if (m == 16908676)
            {
              bool2 = handleClickableSpanActionUiThread(paramMessage, k, localBundle);
            }
            else
            {
              AccessibilityNodeProvider localAccessibilityNodeProvider = paramMessage.getAccessibilityNodeProvider();
              if (localAccessibilityNodeProvider != null)
              {
                bool2 = localAccessibilityNodeProvider.performAction(k, m, localBundle);
              }
              else
              {
                bool2 = bool1;
                if (k == -1) {
                  bool2 = paramMessage.performAccessibilityAction(m, localBundle);
                }
              }
            }
          }
        }
        return;
      }
      return;
    }
    finally
    {
      try
      {
        mViewRootImpl.mAttachInfo.mAccessibilityFetchFlags = 0;
        localIAccessibilityInteractionConnectionCallback.setPerformAccessibilityActionResult(bool1, n);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  private void prepareForExtraDataRequestUiThread(Message paramMessage)
  {
    paramMessage = (SomeArgs)obj;
    int i = argi1;
    ((AccessibilityRequestPreparer)arg1).onPrepareExtraData(i, (String)arg2, (Bundle)arg3, (Message)arg4);
  }
  
  private void queueMessageToHandleOncePrepared(Message paramMessage, int paramInt, long paramLong)
  {
    if (mMessagesWaitingForRequestPreparer == null) {
      mMessagesWaitingForRequestPreparer = new ArrayList(1);
    }
    paramMessage = new MessageHolder(paramMessage, paramInt, paramLong);
    mMessagesWaitingForRequestPreparer.add(paramMessage);
  }
  
  private void recycleMagnificationSpecAndRegionIfNeeded(MagnificationSpec paramMagnificationSpec, Region paramRegion)
  {
    if (Process.myPid() != Binder.getCallingPid())
    {
      if (paramMagnificationSpec != null) {
        paramMagnificationSpec.recycle();
      }
    }
    else if (paramRegion != null) {
      paramRegion.recycle();
    }
  }
  
  private void requestPreparerDoneUiThread(Message paramMessage)
  {
    synchronized (mLock)
    {
      if (arg1 != mActiveRequestPreparerId)
      {
        Slog.e("AccessibilityInteractionController", "Surprising AccessibilityRequestPreparer callback (likely late)");
        return;
      }
      mNumActiveRequestPreparers -= 1;
      if (mNumActiveRequestPreparers <= 0)
      {
        mHandler.removeMessages(9);
        scheduleAllMessagesWaitingForRequestPreparerLocked();
      }
      return;
    }
  }
  
  private void requestPreparerTimeoutUiThread()
  {
    synchronized (mLock)
    {
      Slog.e("AccessibilityInteractionController", "AccessibilityRequestPreparer timed out");
      scheduleAllMessagesWaitingForRequestPreparerLocked();
      return;
    }
  }
  
  @GuardedBy("mLock")
  private void scheduleAllMessagesWaitingForRequestPreparerLocked()
  {
    int i = mMessagesWaitingForRequestPreparer.size();
    for (int j = 0; j < i; j++)
    {
      MessageHolder localMessageHolder = (MessageHolder)mMessagesWaitingForRequestPreparer.get(j);
      Message localMessage = mMessage;
      int k = mInterrogatingPid;
      long l = mInterrogatingTid;
      boolean bool;
      if (j == 0) {
        bool = true;
      } else {
        bool = false;
      }
      scheduleMessage(localMessage, k, l, bool);
    }
    mMessagesWaitingForRequestPreparer.clear();
    mNumActiveRequestPreparers = 0;
    mActiveRequestPreparerId = -1;
  }
  
  private void scheduleMessage(Message paramMessage, int paramInt, long paramLong, boolean paramBoolean)
  {
    if ((paramBoolean) || (!holdOffMessageIfNeeded(paramMessage, paramInt, paramLong))) {
      if ((paramInt == mMyProcessId) && (paramLong == mMyLooperThreadId)) {
        AccessibilityInteractionClient.getInstanceForThread(paramLong).setSameThreadMessage(paramMessage);
      } else {
        mHandler.sendMessage(paramMessage);
      }
    }
  }
  
  private boolean shouldApplyAppScaleAndMagnificationSpec(float paramFloat, MagnificationSpec paramMagnificationSpec)
  {
    boolean bool;
    if ((paramFloat == 1.0F) && ((paramMagnificationSpec == null) || (paramMagnificationSpec.isNop()))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  /* Error */
  private void updateInfoForViewportAndReturnFindNodeResult(AccessibilityNodeInfo paramAccessibilityNodeInfo, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt, MagnificationSpec paramMagnificationSpec, Region paramRegion)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   4: getfield 238	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   7: iconst_0
    //   8: putfield 400	android/view/View$AttachInfo:mAccessibilityFetchFlags	I
    //   11: aload_0
    //   12: aload_1
    //   13: aload 4
    //   15: invokespecial 354	android/view/AccessibilityInteractionController:applyAppScaleAndMagnificationSpecIfNeeded	(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/view/MagnificationSpec;)V
    //   18: aload_0
    //   19: aload_1
    //   20: aload 5
    //   22: invokespecial 230	android/view/AccessibilityInteractionController:adjustIsVisibleToUserIfNeeded	(Landroid/view/accessibility/AccessibilityNodeInfo;Landroid/graphics/Region;)V
    //   25: aload_2
    //   26: aload_1
    //   27: iload_3
    //   28: invokeinterface 697 3 0
    //   33: goto +15 -> 48
    //   36: astore_1
    //   37: aload_0
    //   38: aload 4
    //   40: aload 5
    //   42: invokespecial 699	android/view/AccessibilityInteractionController:recycleMagnificationSpecAndRegionIfNeeded	(Landroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   45: aload_1
    //   46: athrow
    //   47: astore_1
    //   48: aload_0
    //   49: aload 4
    //   51: aload 5
    //   53: invokespecial 699	android/view/AccessibilityInteractionController:recycleMagnificationSpecAndRegionIfNeeded	(Landroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   56: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	this	AccessibilityInteractionController
    //   0	57	1	paramAccessibilityNodeInfo	AccessibilityNodeInfo
    //   0	57	2	paramIAccessibilityInteractionConnectionCallback	IAccessibilityInteractionConnectionCallback
    //   0	57	3	paramInt	int
    //   0	57	4	paramMagnificationSpec	MagnificationSpec
    //   0	57	5	paramRegion	Region
    // Exception table:
    //   from	to	target	type
    //   0	33	36	finally
    //   0	33	47	android/os/RemoteException
  }
  
  /* Error */
  private void updateInfosForViewportAndReturnFindNodeResult(List<AccessibilityNodeInfo> paramList, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt, MagnificationSpec paramMagnificationSpec, Region paramRegion)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 133	android/view/AccessibilityInteractionController:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   4: getfield 238	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   7: iconst_0
    //   8: putfield 400	android/view/View$AttachInfo:mAccessibilityFetchFlags	I
    //   11: aload_0
    //   12: aload_1
    //   13: aload 4
    //   15: invokespecial 701	android/view/AccessibilityInteractionController:applyAppScaleAndMagnificationSpecIfNeeded	(Ljava/util/List;Landroid/view/MagnificationSpec;)V
    //   18: aload_0
    //   19: aload_1
    //   20: aload 5
    //   22: invokespecial 703	android/view/AccessibilityInteractionController:adjustIsVisibleToUserIfNeeded	(Ljava/util/List;Landroid/graphics/Region;)V
    //   25: aload_2
    //   26: aload_1
    //   27: iload_3
    //   28: invokeinterface 707 3 0
    //   33: aload_1
    //   34: ifnull +24 -> 58
    //   37: aload_1
    //   38: invokeinterface 393 1 0
    //   43: goto +15 -> 58
    //   46: astore_1
    //   47: aload_0
    //   48: aload 4
    //   50: aload 5
    //   52: invokespecial 699	android/view/AccessibilityInteractionController:recycleMagnificationSpecAndRegionIfNeeded	(Landroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   55: aload_1
    //   56: athrow
    //   57: astore_1
    //   58: aload_0
    //   59: aload 4
    //   61: aload 5
    //   63: invokespecial 699	android/view/AccessibilityInteractionController:recycleMagnificationSpecAndRegionIfNeeded	(Landroid/view/MagnificationSpec;Landroid/graphics/Region;)V
    //   66: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	67	0	this	AccessibilityInteractionController
    //   0	67	1	paramList	List<AccessibilityNodeInfo>
    //   0	67	2	paramIAccessibilityInteractionConnectionCallback	IAccessibilityInteractionConnectionCallback
    //   0	67	3	paramInt	int
    //   0	67	4	paramMagnificationSpec	MagnificationSpec
    //   0	67	5	paramRegion	Region
    // Exception table:
    //   from	to	target	type
    //   0	33	46	finally
    //   37	43	46	finally
    //   0	33	57	android/os/RemoteException
    //   37	43	57	android/os/RemoteException
  }
  
  public void findAccessibilityNodeInfoByAccessibilityIdClientThread(long paramLong1, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec, Bundle paramBundle)
  {
    Message localMessage = mHandler.obtainMessage();
    what = 2;
    arg1 = paramInt2;
    SomeArgs localSomeArgs = SomeArgs.obtain();
    argi1 = AccessibilityNodeInfo.getAccessibilityViewId(paramLong1);
    argi2 = AccessibilityNodeInfo.getVirtualDescendantId(paramLong1);
    argi3 = paramInt1;
    arg1 = paramIAccessibilityInteractionConnectionCallback;
    arg2 = paramMagnificationSpec;
    arg3 = paramRegion;
    arg4 = paramBundle;
    obj = localSomeArgs;
    scheduleMessage(localMessage, paramInt3, paramLong2, false);
  }
  
  public void findAccessibilityNodeInfosByTextClientThread(long paramLong1, String paramString, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec)
  {
    Message localMessage = mHandler.obtainMessage();
    what = 4;
    arg1 = paramInt2;
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramString;
    arg2 = paramIAccessibilityInteractionConnectionCallback;
    arg3 = paramMagnificationSpec;
    argi1 = AccessibilityNodeInfo.getAccessibilityViewId(paramLong1);
    argi2 = AccessibilityNodeInfo.getVirtualDescendantId(paramLong1);
    argi3 = paramInt1;
    arg4 = paramRegion;
    obj = localSomeArgs;
    scheduleMessage(localMessage, paramInt3, paramLong2, false);
  }
  
  public void findAccessibilityNodeInfosByViewIdClientThread(long paramLong1, String paramString, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec)
  {
    Message localMessage = mHandler.obtainMessage();
    what = 3;
    arg1 = paramInt2;
    arg2 = AccessibilityNodeInfo.getAccessibilityViewId(paramLong1);
    SomeArgs localSomeArgs = SomeArgs.obtain();
    argi1 = paramInt1;
    arg1 = paramIAccessibilityInteractionConnectionCallback;
    arg2 = paramMagnificationSpec;
    arg3 = paramString;
    arg4 = paramRegion;
    obj = localSomeArgs;
    scheduleMessage(localMessage, paramInt3, paramLong2, false);
  }
  
  public void findFocusClientThread(long paramLong1, int paramInt1, Region paramRegion, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2, MagnificationSpec paramMagnificationSpec)
  {
    Message localMessage = mHandler.obtainMessage();
    what = 5;
    arg1 = paramInt3;
    arg2 = paramInt1;
    SomeArgs localSomeArgs = SomeArgs.obtain();
    argi1 = paramInt2;
    argi2 = AccessibilityNodeInfo.getAccessibilityViewId(paramLong1);
    argi3 = AccessibilityNodeInfo.getVirtualDescendantId(paramLong1);
    arg1 = paramIAccessibilityInteractionConnectionCallback;
    arg2 = paramMagnificationSpec;
    arg3 = paramRegion;
    obj = localSomeArgs;
    scheduleMessage(localMessage, paramInt4, paramLong2, false);
  }
  
  public void focusSearchClientThread(long paramLong1, int paramInt1, Region paramRegion, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2, MagnificationSpec paramMagnificationSpec)
  {
    Message localMessage = mHandler.obtainMessage();
    what = 6;
    arg1 = paramInt3;
    arg2 = AccessibilityNodeInfo.getAccessibilityViewId(paramLong1);
    SomeArgs localSomeArgs = SomeArgs.obtain();
    argi2 = paramInt1;
    argi3 = paramInt2;
    arg1 = paramIAccessibilityInteractionConnectionCallback;
    arg2 = paramMagnificationSpec;
    arg3 = paramRegion;
    obj = localSomeArgs;
    scheduleMessage(localMessage, paramInt4, paramLong2, false);
  }
  
  public void performAccessibilityActionClientThread(long paramLong1, int paramInt1, Bundle paramBundle, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2)
  {
    Message localMessage = mHandler.obtainMessage();
    what = 1;
    arg1 = paramInt3;
    arg2 = AccessibilityNodeInfo.getAccessibilityViewId(paramLong1);
    SomeArgs localSomeArgs = SomeArgs.obtain();
    argi1 = AccessibilityNodeInfo.getVirtualDescendantId(paramLong1);
    argi2 = paramInt1;
    argi3 = paramInt2;
    arg1 = paramIAccessibilityInteractionConnectionCallback;
    arg2 = paramBundle;
    obj = localSomeArgs;
    scheduleMessage(localMessage, paramInt4, paramLong2, false);
  }
  
  private class AccessibilityNodePrefetcher
  {
    private static final int MAX_ACCESSIBILITY_NODE_INFO_BATCH_SIZE = 50;
    private final ArrayList<View> mTempViewList = new ArrayList();
    
    private AccessibilityNodePrefetcher() {}
    
    private void enforceNodeTreeConsistent(List<AccessibilityNodeInfo> paramList)
    {
      LongSparseArray localLongSparseArray = new LongSparseArray();
      int i = paramList.size();
      for (int j = 0; j < i; j++)
      {
        localObject1 = (AccessibilityNodeInfo)paramList.get(j);
        localLongSparseArray.put(((AccessibilityNodeInfo)localObject1).getSourceNodeId(), localObject1);
      }
      paramList = (AccessibilityNodeInfo)localLongSparseArray.valueAt(0);
      Object localObject1 = paramList;
      while (paramList != null)
      {
        localObject1 = paramList;
        paramList = (AccessibilityNodeInfo)localLongSparseArray.get(paramList.getParentNodeId());
      }
      Object localObject2 = null;
      paramList = null;
      HashSet localHashSet = new HashSet();
      LinkedList localLinkedList = new LinkedList();
      localLinkedList.add(localObject1);
      localObject1 = paramList;
      while (!localLinkedList.isEmpty())
      {
        paramList = (AccessibilityNodeInfo)localLinkedList.poll();
        if (localHashSet.add(paramList))
        {
          Object localObject3 = localObject2;
          if (paramList.isAccessibilityFocused()) {
            if (localObject2 == null)
            {
              localObject3 = paramList;
            }
            else
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Duplicate accessibility focus:");
              ((StringBuilder)localObject1).append(paramList);
              ((StringBuilder)localObject1).append(" in window:");
              ((StringBuilder)localObject1).append(mViewRootImpl.mAttachInfo.mAccessibilityWindowId);
              throw new IllegalStateException(((StringBuilder)localObject1).toString());
            }
          }
          Object localObject4 = localObject1;
          if (paramList.isFocused()) {
            if (localObject1 == null)
            {
              localObject4 = paramList;
            }
            else
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Duplicate input focus: ");
              ((StringBuilder)localObject1).append(paramList);
              ((StringBuilder)localObject1).append(" in window:");
              ((StringBuilder)localObject1).append(mViewRootImpl.mAttachInfo.mAccessibilityWindowId);
              throw new IllegalStateException(((StringBuilder)localObject1).toString());
            }
          }
          i = paramList.getChildCount();
          for (j = 0; j < i; j++)
          {
            localObject1 = (AccessibilityNodeInfo)localLongSparseArray.get(paramList.getChildId(j));
            if (localObject1 != null) {
              localLinkedList.add(localObject1);
            }
          }
          localObject2 = localObject3;
          localObject1 = localObject4;
        }
        else
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Duplicate node: ");
          ((StringBuilder)localObject1).append(paramList);
          ((StringBuilder)localObject1).append(" in window:");
          ((StringBuilder)localObject1).append(mViewRootImpl.mAttachInfo.mAccessibilityWindowId);
          throw new IllegalStateException(((StringBuilder)localObject1).toString());
        }
      }
      j = localLongSparseArray.size() - 1;
      while (j >= 0)
      {
        paramList = (AccessibilityNodeInfo)localLongSparseArray.valueAt(j);
        if (localHashSet.contains(paramList))
        {
          j--;
        }
        else
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Disconnected node: ");
          ((StringBuilder)localObject1).append(paramList);
          throw new IllegalStateException(((StringBuilder)localObject1).toString());
        }
      }
    }
    
    private void prefetchDescendantsOfRealNode(View paramView, List<AccessibilityNodeInfo> paramList)
    {
      if (!(paramView instanceof ViewGroup)) {
        return;
      }
      Object localObject1 = new HashMap();
      Object localObject2 = mTempViewList;
      ((ArrayList)localObject2).clear();
      try
      {
        paramView.addChildrenForAccessibility((ArrayList)localObject2);
        int i = ((ArrayList)localObject2).size();
        for (int j = 0; j < i; j++)
        {
          int k = paramList.size();
          if (k >= 50) {
            return;
          }
          paramView = (View)((ArrayList)localObject2).get(j);
          if (AccessibilityInteractionController.this.isShown(paramView))
          {
            Object localObject3 = paramView.getAccessibilityNodeProvider();
            if (localObject3 == null)
            {
              localObject3 = paramView.createAccessibilityNodeInfo();
              if (localObject3 != null)
              {
                paramList.add(localObject3);
                ((HashMap)localObject1).put(paramView, null);
              }
            }
            else
            {
              localObject3 = ((AccessibilityNodeProvider)localObject3).createAccessibilityNodeInfo(-1);
              if (localObject3 != null)
              {
                paramList.add(localObject3);
                ((HashMap)localObject1).put(paramView, localObject3);
              }
            }
          }
        }
        ((ArrayList)localObject2).clear();
        if (paramList.size() < 50)
        {
          localObject2 = ((HashMap)localObject1).entrySet().iterator();
          while (((Iterator)localObject2).hasNext())
          {
            localObject1 = (Map.Entry)((Iterator)localObject2).next();
            paramView = (View)((Map.Entry)localObject1).getKey();
            localObject1 = (AccessibilityNodeInfo)((Map.Entry)localObject1).getValue();
            if (localObject1 == null) {
              prefetchDescendantsOfRealNode(paramView, paramList);
            } else {
              prefetchDescendantsOfVirtualNode((AccessibilityNodeInfo)localObject1, paramView.getAccessibilityNodeProvider(), paramList);
            }
          }
        }
        return;
      }
      finally
      {
        ((ArrayList)localObject2).clear();
      }
    }
    
    private void prefetchDescendantsOfVirtualNode(AccessibilityNodeInfo paramAccessibilityNodeInfo, AccessibilityNodeProvider paramAccessibilityNodeProvider, List<AccessibilityNodeInfo> paramList)
    {
      int i = paramList.size();
      int j = paramAccessibilityNodeInfo.getChildCount();
      int k = 0;
      for (int m = 0; m < j; m++)
      {
        if (paramList.size() >= 50) {
          return;
        }
        long l = paramAccessibilityNodeInfo.getChildId(m);
        AccessibilityNodeInfo localAccessibilityNodeInfo = paramAccessibilityNodeProvider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(l));
        if (localAccessibilityNodeInfo != null) {
          paramList.add(localAccessibilityNodeInfo);
        }
      }
      if (paramList.size() < 50)
      {
        j = paramList.size();
        for (m = k; m < j - i; m++) {
          prefetchDescendantsOfVirtualNode((AccessibilityNodeInfo)paramList.get(i + m), paramAccessibilityNodeProvider, paramList);
        }
      }
    }
    
    private void prefetchPredecessorsOfRealNode(View paramView, List<AccessibilityNodeInfo> paramList)
    {
      for (paramView = paramView.getParentForAccessibility(); ((paramView instanceof View)) && (paramList.size() < 50); paramView = paramView.getParentForAccessibility())
      {
        AccessibilityNodeInfo localAccessibilityNodeInfo = ((View)paramView).createAccessibilityNodeInfo();
        if (localAccessibilityNodeInfo != null) {
          paramList.add(localAccessibilityNodeInfo);
        }
      }
    }
    
    private void prefetchPredecessorsOfVirtualNode(AccessibilityNodeInfo paramAccessibilityNodeInfo, View paramView, AccessibilityNodeProvider paramAccessibilityNodeProvider, List<AccessibilityNodeInfo> paramList)
    {
      int i = paramList.size();
      long l = paramAccessibilityNodeInfo.getParentNodeId();
      for (int j = AccessibilityNodeInfo.getAccessibilityViewId(l); j != Integer.MAX_VALUE; j = AccessibilityNodeInfo.getAccessibilityViewId(l))
      {
        if (paramList.size() >= 50) {
          return;
        }
        int k = AccessibilityNodeInfo.getVirtualDescendantId(l);
        if ((k == -1) && (j != paramView.getAccessibilityViewId()))
        {
          prefetchPredecessorsOfRealNode(paramView, paramList);
          return;
        }
        paramAccessibilityNodeInfo = paramAccessibilityNodeProvider.createAccessibilityNodeInfo(k);
        if (paramAccessibilityNodeInfo == null)
        {
          for (j = paramList.size() - 1; j >= i; j--) {
            paramList.remove(j);
          }
          return;
        }
        paramList.add(paramAccessibilityNodeInfo);
        l = paramAccessibilityNodeInfo.getParentNodeId();
      }
    }
    
    private void prefetchSiblingsOfRealNode(View paramView, List<AccessibilityNodeInfo> paramList)
    {
      Object localObject = paramView.getParentForAccessibility();
      if ((localObject instanceof ViewGroup))
      {
        localObject = (ViewGroup)localObject;
        ArrayList localArrayList = mTempViewList;
        localArrayList.clear();
        try
        {
          ((ViewGroup)localObject).addChildrenForAccessibility(localArrayList);
          int i = localArrayList.size();
          for (int j = 0; j < i; j++)
          {
            int k = paramList.size();
            if (k >= 50) {
              return;
            }
            localObject = (View)localArrayList.get(j);
            if ((((View)localObject).getAccessibilityViewId() != paramView.getAccessibilityViewId()) && (AccessibilityInteractionController.this.isShown((View)localObject)))
            {
              AccessibilityNodeProvider localAccessibilityNodeProvider = ((View)localObject).getAccessibilityNodeProvider();
              if (localAccessibilityNodeProvider == null) {
                localObject = ((View)localObject).createAccessibilityNodeInfo();
              } else {
                localObject = localAccessibilityNodeProvider.createAccessibilityNodeInfo(-1);
              }
              if (localObject != null) {
                paramList.add(localObject);
              }
            }
          }
        }
        finally
        {
          localArrayList.clear();
        }
      }
    }
    
    private void prefetchSiblingsOfVirtualNode(AccessibilityNodeInfo paramAccessibilityNodeInfo, View paramView, AccessibilityNodeProvider paramAccessibilityNodeProvider, List<AccessibilityNodeInfo> paramList)
    {
      long l = paramAccessibilityNodeInfo.getParentNodeId();
      int i = AccessibilityNodeInfo.getAccessibilityViewId(l);
      int j = AccessibilityNodeInfo.getVirtualDescendantId(l);
      if ((j == -1) && (i != paramView.getAccessibilityViewId()))
      {
        prefetchSiblingsOfRealNode(paramView, paramList);
      }
      else
      {
        paramView = paramAccessibilityNodeProvider.createAccessibilityNodeInfo(j);
        if (paramView != null)
        {
          j = paramView.getChildCount();
          for (i = 0; i < j; i++)
          {
            if (paramList.size() >= 50) {
              return;
            }
            l = paramView.getChildId(i);
            if (l != paramAccessibilityNodeInfo.getSourceNodeId())
            {
              AccessibilityNodeInfo localAccessibilityNodeInfo = paramAccessibilityNodeProvider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(l));
              if (localAccessibilityNodeInfo != null) {
                paramList.add(localAccessibilityNodeInfo);
              }
            }
          }
        }
      }
    }
    
    public void prefetchAccessibilityNodeInfos(View paramView, int paramInt1, int paramInt2, List<AccessibilityNodeInfo> paramList, Bundle paramBundle)
    {
      Object localObject = paramView.getAccessibilityNodeProvider();
      String str;
      if (paramBundle == null) {
        str = null;
      } else {
        str = paramBundle.getString("android.view.accessibility.AccessibilityNodeInfo.extra_data_requested");
      }
      if (localObject == null)
      {
        localObject = paramView.createAccessibilityNodeInfo();
        if (localObject != null)
        {
          if (str != null) {
            paramView.addExtraDataToAccessibilityNodeInfo((AccessibilityNodeInfo)localObject, str, paramBundle);
          }
          paramList.add(localObject);
          if ((paramInt2 & 0x1) != 0) {
            prefetchPredecessorsOfRealNode(paramView, paramList);
          }
          if ((paramInt2 & 0x2) != 0) {
            prefetchSiblingsOfRealNode(paramView, paramList);
          }
          if ((paramInt2 & 0x4) != 0) {
            prefetchDescendantsOfRealNode(paramView, paramList);
          }
        }
      }
      else
      {
        AccessibilityNodeInfo localAccessibilityNodeInfo = ((AccessibilityNodeProvider)localObject).createAccessibilityNodeInfo(paramInt1);
        if (localAccessibilityNodeInfo != null)
        {
          if (str != null) {
            ((AccessibilityNodeProvider)localObject).addExtraDataToAccessibilityNodeInfo(paramInt1, localAccessibilityNodeInfo, str, paramBundle);
          }
          paramList.add(localAccessibilityNodeInfo);
          if ((paramInt2 & 0x1) != 0) {
            prefetchPredecessorsOfVirtualNode(localAccessibilityNodeInfo, paramView, (AccessibilityNodeProvider)localObject, paramList);
          }
          if ((paramInt2 & 0x2) != 0) {
            prefetchSiblingsOfVirtualNode(localAccessibilityNodeInfo, paramView, (AccessibilityNodeProvider)localObject, paramList);
          }
          if ((paramInt2 & 0x4) != 0) {
            prefetchDescendantsOfVirtualNode(localAccessibilityNodeInfo, (AccessibilityNodeProvider)localObject, paramList);
          }
        }
      }
    }
  }
  
  private final class AddNodeInfosForViewId
    implements Predicate<View>
  {
    private List<AccessibilityNodeInfo> mInfos;
    private int mViewId = -1;
    
    private AddNodeInfosForViewId() {}
    
    public void init(int paramInt, List<AccessibilityNodeInfo> paramList)
    {
      mViewId = paramInt;
      mInfos = paramList;
    }
    
    public void reset()
    {
      mViewId = -1;
      mInfos = null;
    }
    
    public boolean test(View paramView)
    {
      if ((paramView.getId() == mViewId) && (AccessibilityInteractionController.this.isShown(paramView))) {
        mInfos.add(paramView.createAccessibilityNodeInfo());
      }
      return false;
    }
  }
  
  private static final class MessageHolder
  {
    final int mInterrogatingPid;
    final long mInterrogatingTid;
    final Message mMessage;
    
    MessageHolder(Message paramMessage, int paramInt, long paramLong)
    {
      mMessage = paramMessage;
      mInterrogatingPid = paramInt;
      mInterrogatingTid = paramLong;
    }
  }
  
  private class PrivateHandler
    extends Handler
  {
    private static final int MSG_APP_PREPARATION_FINISHED = 8;
    private static final int MSG_APP_PREPARATION_TIMEOUT = 9;
    private static final int MSG_FIND_ACCESSIBILITY_NODE_INFOS_BY_VIEW_ID = 3;
    private static final int MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID = 2;
    private static final int MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_TEXT = 4;
    private static final int MSG_FIND_FOCUS = 5;
    private static final int MSG_FOCUS_SEARCH = 6;
    private static final int MSG_PERFORM_ACCESSIBILITY_ACTION = 1;
    private static final int MSG_PREPARE_FOR_EXTRA_DATA_REQUEST = 7;
    
    public PrivateHandler(Looper paramLooper)
    {
      super();
    }
    
    public String getMessageName(Message paramMessage)
    {
      int i = what;
      switch (i)
      {
      default: 
        paramMessage = new StringBuilder();
        paramMessage.append("Unknown message type: ");
        paramMessage.append(i);
        throw new IllegalArgumentException(paramMessage.toString());
      case 9: 
        return "MSG_APP_PREPARATION_TIMEOUT";
      case 8: 
        return "MSG_APP_PREPARATION_FINISHED";
      case 7: 
        return "MSG_PREPARE_FOR_EXTRA_DATA_REQUEST";
      case 6: 
        return "MSG_FOCUS_SEARCH";
      case 5: 
        return "MSG_FIND_FOCUS";
      case 4: 
        return "MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_TEXT";
      case 3: 
        return "MSG_FIND_ACCESSIBILITY_NODE_INFOS_BY_VIEW_ID";
      case 2: 
        return "MSG_FIND_ACCESSIBILITY_NODE_INFO_BY_ACCESSIBILITY_ID";
      }
      return "MSG_PERFORM_ACCESSIBILITY_ACTION";
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      switch (i)
      {
      default: 
        paramMessage = new StringBuilder();
        paramMessage.append("Unknown message type: ");
        paramMessage.append(i);
        throw new IllegalArgumentException(paramMessage.toString());
      case 9: 
        AccessibilityInteractionController.this.requestPreparerTimeoutUiThread();
        break;
      case 8: 
        AccessibilityInteractionController.this.requestPreparerDoneUiThread(paramMessage);
        break;
      case 7: 
        AccessibilityInteractionController.this.prepareForExtraDataRequestUiThread(paramMessage);
        break;
      case 6: 
        AccessibilityInteractionController.this.focusSearchUiThread(paramMessage);
        break;
      case 5: 
        AccessibilityInteractionController.this.findFocusUiThread(paramMessage);
        break;
      case 4: 
        AccessibilityInteractionController.this.findAccessibilityNodeInfosByTextUiThread(paramMessage);
        break;
      case 3: 
        AccessibilityInteractionController.this.findAccessibilityNodeInfosByViewIdUiThread(paramMessage);
        break;
      case 2: 
        AccessibilityInteractionController.this.findAccessibilityNodeInfoByAccessibilityIdUiThread(paramMessage);
        break;
      case 1: 
        AccessibilityInteractionController.this.performAccessibilityActionUiThread(paramMessage);
      }
    }
  }
}
