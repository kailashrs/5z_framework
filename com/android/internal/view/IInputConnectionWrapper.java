package com.android.internal.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SomeArgs;

public abstract class IInputConnectionWrapper
  extends IInputContext.Stub
{
  private static final boolean DEBUG = false;
  private static final int DO_BEGIN_BATCH_EDIT = 90;
  private static final int DO_CLEAR_META_KEY_STATES = 130;
  private static final int DO_CLOSE_CONNECTION = 150;
  private static final int DO_COMMIT_COMPLETION = 55;
  private static final int DO_COMMIT_CONTENT = 160;
  private static final int DO_COMMIT_CORRECTION = 56;
  private static final int DO_COMMIT_TEXT = 50;
  private static final int DO_DELETE_SURROUNDING_TEXT = 80;
  private static final int DO_DELETE_SURROUNDING_TEXT_IN_CODE_POINTS = 81;
  private static final int DO_END_BATCH_EDIT = 95;
  private static final int DO_FINISH_COMPOSING_TEXT = 65;
  private static final int DO_GET_CURSOR_CAPS_MODE = 30;
  private static final int DO_GET_EXTRACTED_TEXT = 40;
  private static final int DO_GET_SELECTED_TEXT = 25;
  private static final int DO_GET_TEXT_AFTER_CURSOR = 10;
  private static final int DO_GET_TEXT_BEFORE_CURSOR = 20;
  private static final int DO_PERFORM_CONTEXT_MENU_ACTION = 59;
  private static final int DO_PERFORM_EDITOR_ACTION = 58;
  private static final int DO_PERFORM_PRIVATE_COMMAND = 120;
  private static final int DO_REQUEST_UPDATE_CURSOR_ANCHOR_INFO = 140;
  private static final int DO_SEND_KEY_EVENT = 70;
  private static final int DO_SET_COMPOSING_REGION = 63;
  private static final int DO_SET_COMPOSING_TEXT = 60;
  private static final int DO_SET_SELECTION = 57;
  private static final String TAG = "IInputConnectionWrapper";
  @GuardedBy("mLock")
  private boolean mFinished = false;
  private Handler mH;
  @GuardedBy("mLock")
  private InputConnection mInputConnection;
  private Object mLock = new Object();
  private Looper mMainLooper;
  
  public IInputConnectionWrapper(Looper paramLooper, InputConnection paramInputConnection)
  {
    mInputConnection = paramInputConnection;
    mMainLooper = paramLooper;
    mH = new MyHandler(mMainLooper);
  }
  
  public void beginBatchEdit()
  {
    dispatchMessage(obtainMessage(90));
  }
  
  public void clearMetaKeyStates(int paramInt)
  {
    dispatchMessage(obtainMessageII(130, paramInt, 0));
  }
  
  public void closeConnection()
  {
    dispatchMessage(obtainMessage(150));
  }
  
  public void commitCompletion(CompletionInfo paramCompletionInfo)
  {
    dispatchMessage(obtainMessageO(55, paramCompletionInfo));
  }
  
  public void commitContent(InputContentInfo paramInputContentInfo, int paramInt1, Bundle paramBundle, int paramInt2, IInputContextCallback paramIInputContextCallback)
  {
    dispatchMessage(obtainMessageIOOSC(160, paramInt1, paramInputContentInfo, paramBundle, paramInt2, paramIInputContextCallback));
  }
  
  public void commitCorrection(CorrectionInfo paramCorrectionInfo)
  {
    dispatchMessage(obtainMessageO(56, paramCorrectionInfo));
  }
  
  public void commitText(CharSequence paramCharSequence, int paramInt)
  {
    dispatchMessage(obtainMessageIO(50, paramInt, paramCharSequence));
  }
  
  public void deleteSurroundingText(int paramInt1, int paramInt2)
  {
    dispatchMessage(obtainMessageII(80, paramInt1, paramInt2));
  }
  
  public void deleteSurroundingTextInCodePoints(int paramInt1, int paramInt2)
  {
    dispatchMessage(obtainMessageII(81, paramInt1, paramInt2));
  }
  
  void dispatchMessage(Message paramMessage)
  {
    if (Looper.myLooper() == mMainLooper)
    {
      executeMessage(paramMessage);
      paramMessage.recycle();
      return;
    }
    mH.sendMessage(paramMessage);
  }
  
  public void endBatchEdit()
  {
    dispatchMessage(obtainMessage(95));
  }
  
  /* Error */
  void executeMessage(Message paramMessage)
  {
    // Byte code:
    //   0: aload_1
    //   1: getfield 161	android/os/Message:what	I
    //   4: istore_2
    //   5: iload_2
    //   6: tableswitch	default:+38->44, 55:+1936->1942, 56:+1892->1898, 57:+1847->1853, 58:+1806->1812, 59:+1765->1771, 60:+1713->1719
    //   44: iload_2
    //   45: tableswitch	default:+23->68, 80:+1629->1674, 81:+1584->1629
    //   68: iload_2
    //   69: lookupswitch	default:+139->208, 10:+1442->1511, 20:+1324->1393, 25:+1210->1279, 30:+1096->1165, 40:+975->1044, 50:+923->992, 63:+878->947, 65:+843->912, 70:+795->864, 90:+758->827, 95:+721->790, 120:+638->707, 130:+597->666, 140:+483->552, 150:+373->442, 160:+174->243
    //   208: new 163	java/lang/StringBuilder
    //   211: dup
    //   212: invokespecial 164	java/lang/StringBuilder:<init>	()V
    //   215: astore_3
    //   216: aload_3
    //   217: ldc -90
    //   219: invokevirtual 170	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   222: pop
    //   223: aload_3
    //   224: aload_1
    //   225: getfield 161	android/os/Message:what	I
    //   228: invokevirtual 173	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   231: pop
    //   232: ldc 63
    //   234: aload_3
    //   235: invokevirtual 177	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   238: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   241: pop
    //   242: return
    //   243: aload_1
    //   244: getfield 186	android/os/Message:arg1	I
    //   247: istore_2
    //   248: aload_1
    //   249: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   252: checkcast 191	com/android/internal/os/SomeArgs
    //   255: astore_1
    //   256: aload_1
    //   257: getfield 194	com/android/internal/os/SomeArgs:arg6	Ljava/lang/Object;
    //   260: checkcast 196	com/android/internal/view/IInputContextCallback
    //   263: astore_3
    //   264: aload_1
    //   265: getfield 199	com/android/internal/os/SomeArgs:argi6	I
    //   268: istore 4
    //   270: aload_0
    //   271: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   274: astore 5
    //   276: aload 5
    //   278: ifnull +117 -> 395
    //   281: aload_0
    //   282: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   285: ifne +6 -> 291
    //   288: goto +107 -> 395
    //   291: aload_1
    //   292: getfield 209	com/android/internal/os/SomeArgs:arg1	Ljava/lang/Object;
    //   295: checkcast 211	android/view/inputmethod/InputContentInfo
    //   298: astore 6
    //   300: aload 6
    //   302: ifnull +42 -> 344
    //   305: aload 6
    //   307: invokevirtual 214	android/view/inputmethod/InputContentInfo:validate	()Z
    //   310: ifne +6 -> 316
    //   313: goto +31 -> 344
    //   316: aload_3
    //   317: aload 5
    //   319: aload 6
    //   321: iload_2
    //   322: aload_1
    //   323: getfield 217	com/android/internal/os/SomeArgs:arg2	Ljava/lang/Object;
    //   326: checkcast 219	android/os/Bundle
    //   329: invokeinterface 224 4 0
    //   334: iload 4
    //   336: invokeinterface 228 3 0
    //   341: goto +90 -> 431
    //   344: new 163	java/lang/StringBuilder
    //   347: astore 5
    //   349: aload 5
    //   351: invokespecial 164	java/lang/StringBuilder:<init>	()V
    //   354: aload 5
    //   356: ldc -26
    //   358: invokevirtual 170	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   361: pop
    //   362: aload 5
    //   364: aload 6
    //   366: invokevirtual 233	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   369: pop
    //   370: ldc 63
    //   372: aload 5
    //   374: invokevirtual 177	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   377: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   380: pop
    //   381: aload_3
    //   382: iconst_0
    //   383: iload 4
    //   385: invokeinterface 228 3 0
    //   390: aload_1
    //   391: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   394: return
    //   395: ldc 63
    //   397: ldc -20
    //   399: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   402: pop
    //   403: aload_3
    //   404: iconst_0
    //   405: iload 4
    //   407: invokeinterface 228 3 0
    //   412: aload_1
    //   413: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   416: return
    //   417: astore_3
    //   418: goto +18 -> 436
    //   421: astore_3
    //   422: ldc 63
    //   424: ldc -18
    //   426: aload_3
    //   427: invokestatic 241	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   430: pop
    //   431: aload_1
    //   432: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   435: return
    //   436: aload_1
    //   437: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   440: aload_3
    //   441: athrow
    //   442: aload_0
    //   443: invokevirtual 244	com/android/internal/view/IInputConnectionWrapper:isFinished	()Z
    //   446: ifeq +4 -> 450
    //   449: return
    //   450: aload_0
    //   451: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   454: astore_1
    //   455: aload_1
    //   456: ifnonnull +28 -> 484
    //   459: aload_0
    //   460: getfield 84	com/android/internal/view/IInputConnectionWrapper:mLock	Ljava/lang/Object;
    //   463: astore_1
    //   464: aload_1
    //   465: monitorenter
    //   466: aload_0
    //   467: aconst_null
    //   468: putfield 88	com/android/internal/view/IInputConnectionWrapper:mInputConnection	Landroid/view/inputmethod/InputConnection;
    //   471: aload_0
    //   472: iconst_1
    //   473: putfield 86	com/android/internal/view/IInputConnectionWrapper:mFinished	Z
    //   476: aload_1
    //   477: monitorexit
    //   478: return
    //   479: astore_3
    //   480: aload_1
    //   481: monitorexit
    //   482: aload_3
    //   483: athrow
    //   484: aload_1
    //   485: invokestatic 250	android/view/inputmethod/InputConnectionInspector:getMissingMethodFlags	(Landroid/view/inputmethod/InputConnection;)I
    //   488: bipush 64
    //   490: iand
    //   491: ifne +9 -> 500
    //   494: aload_1
    //   495: invokeinterface 252 1 0
    //   500: aload_0
    //   501: getfield 84	com/android/internal/view/IInputConnectionWrapper:mLock	Ljava/lang/Object;
    //   504: astore_3
    //   505: aload_3
    //   506: monitorenter
    //   507: aload_0
    //   508: aconst_null
    //   509: putfield 88	com/android/internal/view/IInputConnectionWrapper:mInputConnection	Landroid/view/inputmethod/InputConnection;
    //   512: aload_0
    //   513: iconst_1
    //   514: putfield 86	com/android/internal/view/IInputConnectionWrapper:mFinished	Z
    //   517: aload_3
    //   518: monitorexit
    //   519: return
    //   520: astore_1
    //   521: aload_3
    //   522: monitorexit
    //   523: aload_1
    //   524: athrow
    //   525: astore_3
    //   526: aload_0
    //   527: getfield 84	com/android/internal/view/IInputConnectionWrapper:mLock	Ljava/lang/Object;
    //   530: astore_1
    //   531: aload_1
    //   532: monitorenter
    //   533: aload_0
    //   534: aconst_null
    //   535: putfield 88	com/android/internal/view/IInputConnectionWrapper:mInputConnection	Landroid/view/inputmethod/InputConnection;
    //   538: aload_0
    //   539: iconst_1
    //   540: putfield 86	com/android/internal/view/IInputConnectionWrapper:mFinished	Z
    //   543: aload_1
    //   544: monitorexit
    //   545: aload_3
    //   546: athrow
    //   547: astore_3
    //   548: aload_1
    //   549: monitorexit
    //   550: aload_3
    //   551: athrow
    //   552: aload_1
    //   553: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   556: checkcast 191	com/android/internal/os/SomeArgs
    //   559: astore_3
    //   560: aload_3
    //   561: getfield 194	com/android/internal/os/SomeArgs:arg6	Ljava/lang/Object;
    //   564: checkcast 196	com/android/internal/view/IInputContextCallback
    //   567: astore 5
    //   569: aload_3
    //   570: getfield 199	com/android/internal/os/SomeArgs:argi6	I
    //   573: istore_2
    //   574: aload_0
    //   575: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   578: astore 6
    //   580: aload 6
    //   582: ifnull +35 -> 617
    //   585: aload_0
    //   586: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   589: ifne +6 -> 595
    //   592: goto +25 -> 617
    //   595: aload 5
    //   597: aload 6
    //   599: aload_1
    //   600: getfield 186	android/os/Message:arg1	I
    //   603: invokeinterface 256 2 0
    //   608: iload_2
    //   609: invokeinterface 259 3 0
    //   614: goto +41 -> 655
    //   617: ldc 63
    //   619: ldc_w 261
    //   622: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   625: pop
    //   626: aload 5
    //   628: iconst_0
    //   629: iload_2
    //   630: invokeinterface 259 3 0
    //   635: aload_3
    //   636: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   639: return
    //   640: astore_1
    //   641: goto +19 -> 660
    //   644: astore_1
    //   645: ldc 63
    //   647: ldc_w 263
    //   650: aload_1
    //   651: invokestatic 241	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   654: pop
    //   655: aload_3
    //   656: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   659: return
    //   660: aload_3
    //   661: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   664: aload_1
    //   665: athrow
    //   666: aload_0
    //   667: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   670: astore_3
    //   671: aload_3
    //   672: ifnull +25 -> 697
    //   675: aload_0
    //   676: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   679: ifne +6 -> 685
    //   682: goto +15 -> 697
    //   685: aload_3
    //   686: aload_1
    //   687: getfield 186	android/os/Message:arg1	I
    //   690: invokeinterface 265 2 0
    //   695: pop
    //   696: return
    //   697: ldc 63
    //   699: ldc_w 267
    //   702: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   705: pop
    //   706: return
    //   707: aload_1
    //   708: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   711: checkcast 191	com/android/internal/os/SomeArgs
    //   714: astore_1
    //   715: aload_1
    //   716: getfield 209	com/android/internal/os/SomeArgs:arg1	Ljava/lang/Object;
    //   719: checkcast 269	java/lang/String
    //   722: astore_3
    //   723: aload_1
    //   724: getfield 217	com/android/internal/os/SomeArgs:arg2	Ljava/lang/Object;
    //   727: checkcast 219	android/os/Bundle
    //   730: astore 6
    //   732: aload_0
    //   733: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   736: astore 5
    //   738: aload 5
    //   740: ifnull +29 -> 769
    //   743: aload_0
    //   744: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   747: ifne +6 -> 753
    //   750: goto +19 -> 769
    //   753: aload 5
    //   755: aload_3
    //   756: aload 6
    //   758: invokeinterface 273 3 0
    //   763: pop
    //   764: aload_1
    //   765: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   768: return
    //   769: ldc 63
    //   771: ldc_w 275
    //   774: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   777: pop
    //   778: aload_1
    //   779: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   782: return
    //   783: astore_3
    //   784: aload_1
    //   785: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   788: aload_3
    //   789: athrow
    //   790: aload_0
    //   791: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   794: astore_1
    //   795: aload_1
    //   796: ifnull +21 -> 817
    //   799: aload_0
    //   800: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   803: ifne +6 -> 809
    //   806: goto +11 -> 817
    //   809: aload_1
    //   810: invokeinterface 277 1 0
    //   815: pop
    //   816: return
    //   817: ldc 63
    //   819: ldc_w 279
    //   822: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   825: pop
    //   826: return
    //   827: aload_0
    //   828: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   831: astore_1
    //   832: aload_1
    //   833: ifnull +21 -> 854
    //   836: aload_0
    //   837: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   840: ifne +6 -> 846
    //   843: goto +11 -> 854
    //   846: aload_1
    //   847: invokeinterface 281 1 0
    //   852: pop
    //   853: return
    //   854: ldc 63
    //   856: ldc_w 283
    //   859: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   862: pop
    //   863: return
    //   864: aload_0
    //   865: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   868: astore_3
    //   869: aload_3
    //   870: ifnull +32 -> 902
    //   873: aload_0
    //   874: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   877: ifne +6 -> 883
    //   880: goto +22 -> 902
    //   883: aload_3
    //   884: aload_1
    //   885: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   888: checkcast 285	android/view/KeyEvent
    //   891: invokeinterface 289 2 0
    //   896: pop
    //   897: aload_0
    //   898: invokevirtual 292	com/android/internal/view/IInputConnectionWrapper:onUserAction	()V
    //   901: return
    //   902: ldc 63
    //   904: ldc_w 294
    //   907: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   910: pop
    //   911: return
    //   912: aload_0
    //   913: invokevirtual 244	com/android/internal/view/IInputConnectionWrapper:isFinished	()Z
    //   916: ifeq +4 -> 920
    //   919: return
    //   920: aload_0
    //   921: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   924: astore_1
    //   925: aload_1
    //   926: ifnonnull +13 -> 939
    //   929: ldc 63
    //   931: ldc_w 296
    //   934: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   937: pop
    //   938: return
    //   939: aload_1
    //   940: invokeinterface 299 1 0
    //   945: pop
    //   946: return
    //   947: aload_0
    //   948: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   951: astore_3
    //   952: aload_3
    //   953: ifnull +29 -> 982
    //   956: aload_0
    //   957: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   960: ifne +6 -> 966
    //   963: goto +19 -> 982
    //   966: aload_3
    //   967: aload_1
    //   968: getfield 186	android/os/Message:arg1	I
    //   971: aload_1
    //   972: getfield 301	android/os/Message:arg2	I
    //   975: invokeinterface 305 3 0
    //   980: pop
    //   981: return
    //   982: ldc 63
    //   984: ldc_w 307
    //   987: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   990: pop
    //   991: return
    //   992: aload_0
    //   993: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   996: astore_3
    //   997: aload_3
    //   998: ifnull +36 -> 1034
    //   1001: aload_0
    //   1002: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1005: ifne +6 -> 1011
    //   1008: goto +26 -> 1034
    //   1011: aload_3
    //   1012: aload_1
    //   1013: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   1016: checkcast 309	java/lang/CharSequence
    //   1019: aload_1
    //   1020: getfield 186	android/os/Message:arg1	I
    //   1023: invokeinterface 312 3 0
    //   1028: pop
    //   1029: aload_0
    //   1030: invokevirtual 292	com/android/internal/view/IInputConnectionWrapper:onUserAction	()V
    //   1033: return
    //   1034: ldc 63
    //   1036: ldc_w 314
    //   1039: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1042: pop
    //   1043: return
    //   1044: aload_1
    //   1045: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   1048: checkcast 191	com/android/internal/os/SomeArgs
    //   1051: astore_3
    //   1052: aload_3
    //   1053: getfield 194	com/android/internal/os/SomeArgs:arg6	Ljava/lang/Object;
    //   1056: checkcast 196	com/android/internal/view/IInputContextCallback
    //   1059: astore 6
    //   1061: aload_3
    //   1062: getfield 199	com/android/internal/os/SomeArgs:argi6	I
    //   1065: istore_2
    //   1066: aload_0
    //   1067: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1070: astore 5
    //   1072: aload 5
    //   1074: ifnull +42 -> 1116
    //   1077: aload_0
    //   1078: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1081: ifne +6 -> 1087
    //   1084: goto +32 -> 1116
    //   1087: aload 6
    //   1089: aload 5
    //   1091: aload_3
    //   1092: getfield 209	com/android/internal/os/SomeArgs:arg1	Ljava/lang/Object;
    //   1095: checkcast 316	android/view/inputmethod/ExtractedTextRequest
    //   1098: aload_1
    //   1099: getfield 186	android/os/Message:arg1	I
    //   1102: invokeinterface 320 3 0
    //   1107: iload_2
    //   1108: invokeinterface 324 3 0
    //   1113: goto +41 -> 1154
    //   1116: ldc 63
    //   1118: ldc_w 326
    //   1121: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1124: pop
    //   1125: aload 6
    //   1127: aconst_null
    //   1128: iload_2
    //   1129: invokeinterface 324 3 0
    //   1134: aload_3
    //   1135: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1138: return
    //   1139: astore_1
    //   1140: goto +19 -> 1159
    //   1143: astore_1
    //   1144: ldc 63
    //   1146: ldc_w 328
    //   1149: aload_1
    //   1150: invokestatic 241	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1153: pop
    //   1154: aload_3
    //   1155: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1158: return
    //   1159: aload_3
    //   1160: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1163: aload_1
    //   1164: athrow
    //   1165: aload_1
    //   1166: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   1169: checkcast 191	com/android/internal/os/SomeArgs
    //   1172: astore_3
    //   1173: aload_3
    //   1174: getfield 194	com/android/internal/os/SomeArgs:arg6	Ljava/lang/Object;
    //   1177: checkcast 196	com/android/internal/view/IInputContextCallback
    //   1180: astore 6
    //   1182: aload_3
    //   1183: getfield 199	com/android/internal/os/SomeArgs:argi6	I
    //   1186: istore_2
    //   1187: aload_0
    //   1188: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1191: astore 5
    //   1193: aload 5
    //   1195: ifnull +35 -> 1230
    //   1198: aload_0
    //   1199: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1202: ifne +6 -> 1208
    //   1205: goto +25 -> 1230
    //   1208: aload 6
    //   1210: aload 5
    //   1212: aload_1
    //   1213: getfield 186	android/os/Message:arg1	I
    //   1216: invokeinterface 332 2 0
    //   1221: iload_2
    //   1222: invokeinterface 335 3 0
    //   1227: goto +41 -> 1268
    //   1230: ldc 63
    //   1232: ldc_w 337
    //   1235: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1238: pop
    //   1239: aload 6
    //   1241: iconst_0
    //   1242: iload_2
    //   1243: invokeinterface 335 3 0
    //   1248: aload_3
    //   1249: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1252: return
    //   1253: astore_1
    //   1254: goto +19 -> 1273
    //   1257: astore_1
    //   1258: ldc 63
    //   1260: ldc_w 339
    //   1263: aload_1
    //   1264: invokestatic 241	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1267: pop
    //   1268: aload_3
    //   1269: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1272: return
    //   1273: aload_3
    //   1274: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1277: aload_1
    //   1278: athrow
    //   1279: aload_1
    //   1280: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   1283: checkcast 191	com/android/internal/os/SomeArgs
    //   1286: astore_3
    //   1287: aload_3
    //   1288: getfield 194	com/android/internal/os/SomeArgs:arg6	Ljava/lang/Object;
    //   1291: checkcast 196	com/android/internal/view/IInputContextCallback
    //   1294: astore 5
    //   1296: aload_3
    //   1297: getfield 199	com/android/internal/os/SomeArgs:argi6	I
    //   1300: istore_2
    //   1301: aload_0
    //   1302: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1305: astore 6
    //   1307: aload 6
    //   1309: ifnull +35 -> 1344
    //   1312: aload_0
    //   1313: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1316: ifne +6 -> 1322
    //   1319: goto +25 -> 1344
    //   1322: aload 5
    //   1324: aload 6
    //   1326: aload_1
    //   1327: getfield 186	android/os/Message:arg1	I
    //   1330: invokeinterface 343 2 0
    //   1335: iload_2
    //   1336: invokeinterface 346 3 0
    //   1341: goto +41 -> 1382
    //   1344: ldc 63
    //   1346: ldc_w 348
    //   1349: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1352: pop
    //   1353: aload 5
    //   1355: aconst_null
    //   1356: iload_2
    //   1357: invokeinterface 346 3 0
    //   1362: aload_3
    //   1363: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1366: return
    //   1367: astore_1
    //   1368: goto +19 -> 1387
    //   1371: astore_1
    //   1372: ldc 63
    //   1374: ldc_w 350
    //   1377: aload_1
    //   1378: invokestatic 241	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1381: pop
    //   1382: aload_3
    //   1383: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1386: return
    //   1387: aload_3
    //   1388: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1391: aload_1
    //   1392: athrow
    //   1393: aload_1
    //   1394: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   1397: checkcast 191	com/android/internal/os/SomeArgs
    //   1400: astore_3
    //   1401: aload_3
    //   1402: getfield 194	com/android/internal/os/SomeArgs:arg6	Ljava/lang/Object;
    //   1405: checkcast 196	com/android/internal/view/IInputContextCallback
    //   1408: astore 6
    //   1410: aload_3
    //   1411: getfield 199	com/android/internal/os/SomeArgs:argi6	I
    //   1414: istore_2
    //   1415: aload_0
    //   1416: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1419: astore 5
    //   1421: aload 5
    //   1423: ifnull +39 -> 1462
    //   1426: aload_0
    //   1427: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1430: ifne +6 -> 1436
    //   1433: goto +29 -> 1462
    //   1436: aload 6
    //   1438: aload 5
    //   1440: aload_1
    //   1441: getfield 186	android/os/Message:arg1	I
    //   1444: aload_1
    //   1445: getfield 301	android/os/Message:arg2	I
    //   1448: invokeinterface 354 3 0
    //   1453: iload_2
    //   1454: invokeinterface 357 3 0
    //   1459: goto +41 -> 1500
    //   1462: ldc 63
    //   1464: ldc_w 359
    //   1467: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1470: pop
    //   1471: aload 6
    //   1473: aconst_null
    //   1474: iload_2
    //   1475: invokeinterface 357 3 0
    //   1480: aload_3
    //   1481: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1484: return
    //   1485: astore_1
    //   1486: goto +19 -> 1505
    //   1489: astore_1
    //   1490: ldc 63
    //   1492: ldc_w 361
    //   1495: aload_1
    //   1496: invokestatic 241	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1499: pop
    //   1500: aload_3
    //   1501: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1504: return
    //   1505: aload_3
    //   1506: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1509: aload_1
    //   1510: athrow
    //   1511: aload_1
    //   1512: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   1515: checkcast 191	com/android/internal/os/SomeArgs
    //   1518: astore_3
    //   1519: aload_3
    //   1520: getfield 194	com/android/internal/os/SomeArgs:arg6	Ljava/lang/Object;
    //   1523: checkcast 196	com/android/internal/view/IInputContextCallback
    //   1526: astore 6
    //   1528: aload_3
    //   1529: getfield 199	com/android/internal/os/SomeArgs:argi6	I
    //   1532: istore_2
    //   1533: aload_0
    //   1534: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1537: astore 5
    //   1539: aload 5
    //   1541: ifnull +39 -> 1580
    //   1544: aload_0
    //   1545: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1548: ifne +6 -> 1554
    //   1551: goto +29 -> 1580
    //   1554: aload 6
    //   1556: aload 5
    //   1558: aload_1
    //   1559: getfield 186	android/os/Message:arg1	I
    //   1562: aload_1
    //   1563: getfield 301	android/os/Message:arg2	I
    //   1566: invokeinterface 364 3 0
    //   1571: iload_2
    //   1572: invokeinterface 367 3 0
    //   1577: goto +41 -> 1618
    //   1580: ldc 63
    //   1582: ldc_w 369
    //   1585: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1588: pop
    //   1589: aload 6
    //   1591: aconst_null
    //   1592: iload_2
    //   1593: invokeinterface 367 3 0
    //   1598: aload_3
    //   1599: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1602: return
    //   1603: astore_1
    //   1604: goto +19 -> 1623
    //   1607: astore_1
    //   1608: ldc 63
    //   1610: ldc_w 371
    //   1613: aload_1
    //   1614: invokestatic 241	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1617: pop
    //   1618: aload_3
    //   1619: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1622: return
    //   1623: aload_3
    //   1624: invokevirtual 234	com/android/internal/os/SomeArgs:recycle	()V
    //   1627: aload_1
    //   1628: athrow
    //   1629: aload_0
    //   1630: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1633: astore_3
    //   1634: aload_3
    //   1635: ifnull +29 -> 1664
    //   1638: aload_0
    //   1639: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1642: ifne +6 -> 1648
    //   1645: goto +19 -> 1664
    //   1648: aload_3
    //   1649: aload_1
    //   1650: getfield 186	android/os/Message:arg1	I
    //   1653: aload_1
    //   1654: getfield 301	android/os/Message:arg2	I
    //   1657: invokeinterface 373 3 0
    //   1662: pop
    //   1663: return
    //   1664: ldc 63
    //   1666: ldc_w 375
    //   1669: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1672: pop
    //   1673: return
    //   1674: aload_0
    //   1675: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1678: astore_3
    //   1679: aload_3
    //   1680: ifnull +29 -> 1709
    //   1683: aload_0
    //   1684: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1687: ifne +6 -> 1693
    //   1690: goto +19 -> 1709
    //   1693: aload_3
    //   1694: aload_1
    //   1695: getfield 186	android/os/Message:arg1	I
    //   1698: aload_1
    //   1699: getfield 301	android/os/Message:arg2	I
    //   1702: invokeinterface 377 3 0
    //   1707: pop
    //   1708: return
    //   1709: ldc 63
    //   1711: ldc_w 379
    //   1714: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1717: pop
    //   1718: return
    //   1719: aload_0
    //   1720: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1723: astore_3
    //   1724: aload_3
    //   1725: ifnull +36 -> 1761
    //   1728: aload_0
    //   1729: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1732: ifne +6 -> 1738
    //   1735: goto +26 -> 1761
    //   1738: aload_3
    //   1739: aload_1
    //   1740: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   1743: checkcast 309	java/lang/CharSequence
    //   1746: aload_1
    //   1747: getfield 186	android/os/Message:arg1	I
    //   1750: invokeinterface 382 3 0
    //   1755: pop
    //   1756: aload_0
    //   1757: invokevirtual 292	com/android/internal/view/IInputConnectionWrapper:onUserAction	()V
    //   1760: return
    //   1761: ldc 63
    //   1763: ldc_w 384
    //   1766: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1769: pop
    //   1770: return
    //   1771: aload_0
    //   1772: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1775: astore_3
    //   1776: aload_3
    //   1777: ifnull +25 -> 1802
    //   1780: aload_0
    //   1781: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1784: ifne +6 -> 1790
    //   1787: goto +15 -> 1802
    //   1790: aload_3
    //   1791: aload_1
    //   1792: getfield 186	android/os/Message:arg1	I
    //   1795: invokeinterface 387 2 0
    //   1800: pop
    //   1801: return
    //   1802: ldc 63
    //   1804: ldc_w 389
    //   1807: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1810: pop
    //   1811: return
    //   1812: aload_0
    //   1813: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1816: astore_3
    //   1817: aload_3
    //   1818: ifnull +25 -> 1843
    //   1821: aload_0
    //   1822: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1825: ifne +6 -> 1831
    //   1828: goto +15 -> 1843
    //   1831: aload_3
    //   1832: aload_1
    //   1833: getfield 186	android/os/Message:arg1	I
    //   1836: invokeinterface 392 2 0
    //   1841: pop
    //   1842: return
    //   1843: ldc 63
    //   1845: ldc_w 394
    //   1848: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1851: pop
    //   1852: return
    //   1853: aload_0
    //   1854: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1857: astore_3
    //   1858: aload_3
    //   1859: ifnull +29 -> 1888
    //   1862: aload_0
    //   1863: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1866: ifne +6 -> 1872
    //   1869: goto +19 -> 1888
    //   1872: aload_3
    //   1873: aload_1
    //   1874: getfield 186	android/os/Message:arg1	I
    //   1877: aload_1
    //   1878: getfield 301	android/os/Message:arg2	I
    //   1881: invokeinterface 397 3 0
    //   1886: pop
    //   1887: return
    //   1888: ldc 63
    //   1890: ldc_w 399
    //   1893: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1896: pop
    //   1897: return
    //   1898: aload_0
    //   1899: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1902: astore_3
    //   1903: aload_3
    //   1904: ifnull +28 -> 1932
    //   1907: aload_0
    //   1908: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1911: ifne +6 -> 1917
    //   1914: goto +18 -> 1932
    //   1917: aload_3
    //   1918: aload_1
    //   1919: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   1922: checkcast 401	android/view/inputmethod/CorrectionInfo
    //   1925: invokeinterface 404 2 0
    //   1930: pop
    //   1931: return
    //   1932: ldc 63
    //   1934: ldc_w 406
    //   1937: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1940: pop
    //   1941: return
    //   1942: aload_0
    //   1943: invokevirtual 203	com/android/internal/view/IInputConnectionWrapper:getInputConnection	()Landroid/view/inputmethod/InputConnection;
    //   1946: astore_3
    //   1947: aload_3
    //   1948: ifnull +28 -> 1976
    //   1951: aload_0
    //   1952: invokevirtual 207	com/android/internal/view/IInputConnectionWrapper:isActive	()Z
    //   1955: ifne +6 -> 1961
    //   1958: goto +18 -> 1976
    //   1961: aload_3
    //   1962: aload_1
    //   1963: getfield 189	android/os/Message:obj	Ljava/lang/Object;
    //   1966: checkcast 408	android/view/inputmethod/CompletionInfo
    //   1969: invokeinterface 411 2 0
    //   1974: pop
    //   1975: return
    //   1976: ldc 63
    //   1978: ldc_w 413
    //   1981: invokestatic 183	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   1984: pop
    //   1985: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1986	0	this	IInputConnectionWrapper
    //   0	1986	1	paramMessage	Message
    //   4	1589	2	i	int
    //   215	189	3	localObject1	Object
    //   417	1	3	localObject2	Object
    //   421	20	3	localRemoteException	android.os.RemoteException
    //   479	4	3	localObject3	Object
    //   504	18	3	localObject4	Object
    //   525	21	3	localObject5	Object
    //   547	4	3	localObject6	Object
    //   559	197	3	localObject7	Object
    //   783	6	3	localObject8	Object
    //   868	1094	3	localObject9	Object
    //   268	138	4	j	int
    //   274	1283	5	localObject10	Object
    //   298	1292	6	localObject11	Object
    // Exception table:
    //   from	to	target	type
    //   256	276	417	finally
    //   281	288	417	finally
    //   291	300	417	finally
    //   305	313	417	finally
    //   316	341	417	finally
    //   344	390	417	finally
    //   395	412	417	finally
    //   422	431	417	finally
    //   256	276	421	android/os/RemoteException
    //   281	288	421	android/os/RemoteException
    //   291	300	421	android/os/RemoteException
    //   305	313	421	android/os/RemoteException
    //   316	341	421	android/os/RemoteException
    //   344	390	421	android/os/RemoteException
    //   395	412	421	android/os/RemoteException
    //   466	478	479	finally
    //   480	482	479	finally
    //   507	519	520	finally
    //   521	523	520	finally
    //   450	455	525	finally
    //   484	500	525	finally
    //   533	545	547	finally
    //   548	550	547	finally
    //   560	580	640	finally
    //   585	592	640	finally
    //   595	614	640	finally
    //   617	635	640	finally
    //   645	655	640	finally
    //   560	580	644	android/os/RemoteException
    //   585	592	644	android/os/RemoteException
    //   595	614	644	android/os/RemoteException
    //   617	635	644	android/os/RemoteException
    //   715	738	783	finally
    //   743	750	783	finally
    //   753	764	783	finally
    //   769	778	783	finally
    //   1052	1072	1139	finally
    //   1077	1084	1139	finally
    //   1087	1113	1139	finally
    //   1116	1134	1139	finally
    //   1144	1154	1139	finally
    //   1052	1072	1143	android/os/RemoteException
    //   1077	1084	1143	android/os/RemoteException
    //   1087	1113	1143	android/os/RemoteException
    //   1116	1134	1143	android/os/RemoteException
    //   1173	1193	1253	finally
    //   1198	1205	1253	finally
    //   1208	1227	1253	finally
    //   1230	1248	1253	finally
    //   1258	1268	1253	finally
    //   1173	1193	1257	android/os/RemoteException
    //   1198	1205	1257	android/os/RemoteException
    //   1208	1227	1257	android/os/RemoteException
    //   1230	1248	1257	android/os/RemoteException
    //   1287	1307	1367	finally
    //   1312	1319	1367	finally
    //   1322	1341	1367	finally
    //   1344	1362	1367	finally
    //   1372	1382	1367	finally
    //   1287	1307	1371	android/os/RemoteException
    //   1312	1319	1371	android/os/RemoteException
    //   1322	1341	1371	android/os/RemoteException
    //   1344	1362	1371	android/os/RemoteException
    //   1401	1421	1485	finally
    //   1426	1433	1485	finally
    //   1436	1459	1485	finally
    //   1462	1480	1485	finally
    //   1490	1500	1485	finally
    //   1401	1421	1489	android/os/RemoteException
    //   1426	1433	1489	android/os/RemoteException
    //   1436	1459	1489	android/os/RemoteException
    //   1462	1480	1489	android/os/RemoteException
    //   1519	1539	1603	finally
    //   1544	1551	1603	finally
    //   1554	1577	1603	finally
    //   1580	1598	1603	finally
    //   1608	1618	1603	finally
    //   1519	1539	1607	android/os/RemoteException
    //   1544	1551	1607	android/os/RemoteException
    //   1554	1577	1607	android/os/RemoteException
    //   1580	1598	1607	android/os/RemoteException
  }
  
  public void finishComposingText()
  {
    dispatchMessage(obtainMessage(65));
  }
  
  public void getCursorCapsMode(int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
  {
    dispatchMessage(obtainMessageISC(30, paramInt1, paramInt2, paramIInputContextCallback));
  }
  
  public void getExtractedText(ExtractedTextRequest paramExtractedTextRequest, int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
  {
    dispatchMessage(obtainMessageIOSC(40, paramInt1, paramExtractedTextRequest, paramInt2, paramIInputContextCallback));
  }
  
  public InputConnection getInputConnection()
  {
    synchronized (mLock)
    {
      InputConnection localInputConnection = mInputConnection;
      return localInputConnection;
    }
  }
  
  public void getSelectedText(int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
  {
    dispatchMessage(obtainMessageISC(25, paramInt1, paramInt2, paramIInputContextCallback));
  }
  
  public void getTextAfterCursor(int paramInt1, int paramInt2, int paramInt3, IInputContextCallback paramIInputContextCallback)
  {
    dispatchMessage(obtainMessageIISC(10, paramInt1, paramInt2, paramInt3, paramIInputContextCallback));
  }
  
  public void getTextBeforeCursor(int paramInt1, int paramInt2, int paramInt3, IInputContextCallback paramIInputContextCallback)
  {
    dispatchMessage(obtainMessageIISC(20, paramInt1, paramInt2, paramInt3, paramIInputContextCallback));
  }
  
  protected abstract boolean isActive();
  
  protected boolean isFinished()
  {
    synchronized (mLock)
    {
      boolean bool = mFinished;
      return bool;
    }
  }
  
  Message obtainMessage(int paramInt)
  {
    return mH.obtainMessage(paramInt);
  }
  
  Message obtainMessageII(int paramInt1, int paramInt2, int paramInt3)
  {
    return mH.obtainMessage(paramInt1, paramInt2, paramInt3);
  }
  
  Message obtainMessageIISC(int paramInt1, int paramInt2, int paramInt3, int paramInt4, IInputContextCallback paramIInputContextCallback)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg6 = paramIInputContextCallback;
    argi6 = paramInt4;
    return mH.obtainMessage(paramInt1, paramInt2, paramInt3, localSomeArgs);
  }
  
  Message obtainMessageIO(int paramInt1, int paramInt2, Object paramObject)
  {
    return mH.obtainMessage(paramInt1, paramInt2, 0, paramObject);
  }
  
  Message obtainMessageIOOSC(int paramInt1, int paramInt2, Object paramObject1, Object paramObject2, int paramInt3, IInputContextCallback paramIInputContextCallback)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    arg6 = paramIInputContextCallback;
    argi6 = paramInt3;
    return mH.obtainMessage(paramInt1, paramInt2, 0, localSomeArgs);
  }
  
  Message obtainMessageIOSC(int paramInt1, int paramInt2, Object paramObject, int paramInt3, IInputContextCallback paramIInputContextCallback)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject;
    arg6 = paramIInputContextCallback;
    argi6 = paramInt3;
    return mH.obtainMessage(paramInt1, paramInt2, 0, localSomeArgs);
  }
  
  Message obtainMessageISC(int paramInt1, int paramInt2, int paramInt3, IInputContextCallback paramIInputContextCallback)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg6 = paramIInputContextCallback;
    argi6 = paramInt3;
    return mH.obtainMessage(paramInt1, paramInt2, 0, localSomeArgs);
  }
  
  Message obtainMessageO(int paramInt, Object paramObject)
  {
    return mH.obtainMessage(paramInt, 0, 0, paramObject);
  }
  
  Message obtainMessageOO(int paramInt, Object paramObject1, Object paramObject2)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    return mH.obtainMessage(paramInt, 0, 0, localSomeArgs);
  }
  
  protected abstract void onUserAction();
  
  public void performContextMenuAction(int paramInt)
  {
    dispatchMessage(obtainMessageII(59, paramInt, 0));
  }
  
  public void performEditorAction(int paramInt)
  {
    dispatchMessage(obtainMessageII(58, paramInt, 0));
  }
  
  public void performPrivateCommand(String paramString, Bundle paramBundle)
  {
    dispatchMessage(obtainMessageOO(120, paramString, paramBundle));
  }
  
  public void requestUpdateCursorAnchorInfo(int paramInt1, int paramInt2, IInputContextCallback paramIInputContextCallback)
  {
    dispatchMessage(obtainMessageISC(140, paramInt1, paramInt2, paramIInputContextCallback));
  }
  
  public void sendKeyEvent(KeyEvent paramKeyEvent)
  {
    dispatchMessage(obtainMessageO(70, paramKeyEvent));
  }
  
  public void setComposingRegion(int paramInt1, int paramInt2)
  {
    dispatchMessage(obtainMessageII(63, paramInt1, paramInt2));
  }
  
  public void setComposingText(CharSequence paramCharSequence, int paramInt)
  {
    dispatchMessage(obtainMessageIO(60, paramInt, paramCharSequence));
  }
  
  public void setSelection(int paramInt1, int paramInt2)
  {
    dispatchMessage(obtainMessageII(57, paramInt1, paramInt2));
  }
  
  class MyHandler
    extends Handler
  {
    MyHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      executeMessage(paramMessage);
    }
  }
}
