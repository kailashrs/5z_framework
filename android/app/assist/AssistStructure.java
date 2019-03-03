package android.app.assist;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PooledStringReader;
import android.os.PooledStringWriter;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.ViewStructure;
import android.view.ViewStructure.HtmlInfo;
import android.view.ViewStructure.HtmlInfo.Builder;
import android.view.WindowManagerGlobal;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssistStructure
  implements Parcelable
{
  public static final Parcelable.Creator<AssistStructure> CREATOR = new Parcelable.Creator()
  {
    public AssistStructure createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AssistStructure(paramAnonymousParcel);
    }
    
    public AssistStructure[] newArray(int paramAnonymousInt)
    {
      return new AssistStructure[paramAnonymousInt];
    }
  };
  static final boolean DEBUG_PARCEL = false;
  static final boolean DEBUG_PARCEL_CHILDREN = false;
  static final boolean DEBUG_PARCEL_TREE = false;
  static final String DESCRIPTOR = "android.app.AssistStructure";
  static final String TAG = "AssistStructure";
  static final int TRANSACTION_XFER = 2;
  static final int VALIDATE_VIEW_TOKEN = 572662306;
  static final int VALIDATE_WINDOW_TOKEN = 286331153;
  private long mAcquisitionEndTime;
  private long mAcquisitionStartTime;
  ComponentName mActivityComponent;
  private int mFlags;
  boolean mHaveData;
  private boolean mIsHomeActivity;
  final ArrayList<ViewNodeBuilder> mPendingAsyncChildren = new ArrayList();
  IBinder mReceiveChannel;
  boolean mSanitizeOnWrite;
  SendChannel mSendChannel;
  Rect mTmpRect = new Rect();
  final ArrayList<WindowNode> mWindowNodes = new ArrayList();
  
  public AssistStructure()
  {
    mSanitizeOnWrite = false;
    mHaveData = true;
    mActivityComponent = null;
    mFlags = 0;
  }
  
  public AssistStructure(Activity paramActivity, boolean paramBoolean, int paramInt)
  {
    int i = 0;
    mSanitizeOnWrite = false;
    mHaveData = true;
    mActivityComponent = paramActivity.getComponentName();
    mFlags = paramInt;
    ArrayList localArrayList = WindowManagerGlobal.getInstance().getRootViews(paramActivity.getActivityToken());
    while (i < localArrayList.size())
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)localArrayList.get(i);
      if (localViewRootImpl.getView() == null)
      {
        paramActivity = new StringBuilder();
        paramActivity.append("Skipping window with dettached view: ");
        paramActivity.append(localViewRootImpl.getTitle());
        Log.w("AssistStructure", paramActivity.toString());
      }
      else
      {
        mWindowNodes.add(new WindowNode(this, localViewRootImpl, paramBoolean, paramInt));
      }
      i++;
    }
  }
  
  public AssistStructure(Parcel paramParcel)
  {
    boolean bool = false;
    mSanitizeOnWrite = false;
    if (paramParcel.readInt() == 1) {
      bool = true;
    }
    mIsHomeActivity = bool;
    mReceiveChannel = paramParcel.readStrongBinder();
  }
  
  public void clearSendChannel()
  {
    if (mSendChannel != null) {
      mSendChannel.mAssistStructure = null;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  void dump(String paramString, ViewNode paramViewNode, boolean paramBoolean)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(paramString);
    ((StringBuilder)localObject1).append("View [");
    ((StringBuilder)localObject1).append(paramViewNode.getLeft());
    ((StringBuilder)localObject1).append(",");
    ((StringBuilder)localObject1).append(paramViewNode.getTop());
    ((StringBuilder)localObject1).append(" ");
    ((StringBuilder)localObject1).append(paramViewNode.getWidth());
    ((StringBuilder)localObject1).append("x");
    ((StringBuilder)localObject1).append(paramViewNode.getHeight());
    ((StringBuilder)localObject1).append("] ");
    ((StringBuilder)localObject1).append(paramViewNode.getClassName());
    Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    int i = paramViewNode.getId();
    if (i != 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("  ID: #");
      localStringBuilder.append(Integer.toHexString(i));
      localObject1 = paramViewNode.getIdEntry();
      if (localObject1 != null)
      {
        localObject2 = paramViewNode.getIdType();
        String str = paramViewNode.getIdPackage();
        localStringBuilder.append(" ");
        localStringBuilder.append(str);
        localStringBuilder.append(":");
        localStringBuilder.append((String)localObject2);
        localStringBuilder.append("/");
        localStringBuilder.append((String)localObject1);
      }
      Log.i("AssistStructure", localStringBuilder.toString());
    }
    int j = paramViewNode.getScrollX();
    i = paramViewNode.getScrollY();
    if ((j != 0) || (i != 0))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Scroll: ");
      ((StringBuilder)localObject1).append(j);
      ((StringBuilder)localObject1).append(",");
      ((StringBuilder)localObject1).append(i);
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    Object localObject2 = paramViewNode.getTransformation();
    if (localObject2 != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Transformation: ");
      ((StringBuilder)localObject1).append(localObject2);
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    float f = paramViewNode.getElevation();
    if (f != 0.0F)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Elevation: ");
      ((StringBuilder)localObject1).append(f);
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    if (paramViewNode.getAlpha() != 0.0F)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Alpha: ");
      ((StringBuilder)localObject1).append(f);
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    localObject2 = paramViewNode.getContentDescription();
    if (localObject2 != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Content description: ");
      ((StringBuilder)localObject1).append(localObject2);
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    localObject1 = paramViewNode.getText();
    if (localObject1 != null)
    {
      if ((!paramViewNode.isSanitized()) && (!paramBoolean))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("REDACTED[");
        ((StringBuilder)localObject2).append(((CharSequence)localObject1).length());
        ((StringBuilder)localObject2).append(" chars]");
        localObject1 = ((StringBuilder)localObject2).toString();
      }
      else
      {
        localObject1 = ((CharSequence)localObject1).toString();
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("  Text (sel ");
      ((StringBuilder)localObject2).append(paramViewNode.getTextSelectionStart());
      ((StringBuilder)localObject2).append("-");
      ((StringBuilder)localObject2).append(paramViewNode.getTextSelectionEnd());
      ((StringBuilder)localObject2).append("): ");
      ((StringBuilder)localObject2).append((String)localObject1);
      Log.i("AssistStructure", ((StringBuilder)localObject2).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Text size: ");
      ((StringBuilder)localObject1).append(paramViewNode.getTextSize());
      ((StringBuilder)localObject1).append(" , style: #");
      ((StringBuilder)localObject1).append(paramViewNode.getTextStyle());
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Text color fg: #");
      ((StringBuilder)localObject1).append(Integer.toHexString(paramViewNode.getTextColor()));
      ((StringBuilder)localObject1).append(", bg: #");
      ((StringBuilder)localObject1).append(Integer.toHexString(paramViewNode.getTextBackgroundColor()));
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Input type: ");
      ((StringBuilder)localObject1).append(paramViewNode.getInputType());
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Resource id: ");
      ((StringBuilder)localObject1).append(paramViewNode.getTextIdEntry());
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    localObject1 = paramViewNode.getWebDomain();
    if (localObject1 != null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("  Web domain: ");
      ((StringBuilder)localObject2).append((String)localObject1);
      Log.i("AssistStructure", ((StringBuilder)localObject2).toString());
    }
    localObject2 = paramViewNode.getHtmlInfo();
    if (localObject2 != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  HtmlInfo: tag=");
      ((StringBuilder)localObject1).append(((ViewStructure.HtmlInfo)localObject2).getTag());
      ((StringBuilder)localObject1).append(", attr=");
      ((StringBuilder)localObject1).append(((ViewStructure.HtmlInfo)localObject2).getAttributes());
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    localObject1 = paramViewNode.getLocaleList();
    if (localObject1 != null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("  LocaleList: ");
      ((StringBuilder)localObject2).append(localObject1);
      Log.i("AssistStructure", ((StringBuilder)localObject2).toString());
    }
    localObject2 = paramViewNode.getHint();
    if (localObject2 != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Hint: ");
      ((StringBuilder)localObject1).append((String)localObject2);
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    localObject2 = paramViewNode.getExtras();
    if (localObject2 != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Extras: ");
      ((StringBuilder)localObject1).append(localObject2);
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    if (paramViewNode.isAssistBlocked())
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  BLOCKED");
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    localObject1 = paramViewNode.getAutofillId();
    if (localObject1 == null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append(" NO autofill ID");
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
    }
    else
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("Autofill info: id= ");
      ((StringBuilder)localObject2).append(localObject1);
      ((StringBuilder)localObject2).append(", type=");
      ((StringBuilder)localObject2).append(paramViewNode.getAutofillType());
      ((StringBuilder)localObject2).append(", options=");
      ((StringBuilder)localObject2).append(Arrays.toString(paramViewNode.getAutofillOptions()));
      ((StringBuilder)localObject2).append(", hints=");
      ((StringBuilder)localObject2).append(Arrays.toString(paramViewNode.getAutofillHints()));
      ((StringBuilder)localObject2).append(", value=");
      ((StringBuilder)localObject2).append(paramViewNode.getAutofillValue());
      ((StringBuilder)localObject2).append(", sanitized=");
      ((StringBuilder)localObject2).append(paramViewNode.isSanitized());
      ((StringBuilder)localObject2).append(", importantFor=");
      ((StringBuilder)localObject2).append(paramViewNode.getImportantForAutofill());
      Log.i("AssistStructure", ((StringBuilder)localObject2).toString());
    }
    i = paramViewNode.getChildCount();
    if (i > 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  Children:");
      Log.i("AssistStructure", ((StringBuilder)localObject1).toString());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("    ");
      paramString = ((StringBuilder)localObject1).toString();
      for (j = 0; j < i; j++) {
        dump(paramString, paramViewNode.getChildAt(j), paramBoolean);
      }
    }
  }
  
  public void dump(boolean paramBoolean)
  {
    if (mActivityComponent == null)
    {
      Log.i("AssistStructure", "dump(): calling ensureData() first");
      ensureData();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Activity: ");
    localStringBuilder.append(mActivityComponent.flattenToShortString());
    Log.i("AssistStructure", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Sanitize on write: ");
    localStringBuilder.append(mSanitizeOnWrite);
    Log.i("AssistStructure", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Flags: ");
    localStringBuilder.append(mFlags);
    Log.i("AssistStructure", localStringBuilder.toString());
    int i = getWindowNodeCount();
    for (int j = 0; j < i; j++)
    {
      WindowNode localWindowNode = getWindowNodeAt(j);
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Window #");
      localStringBuilder.append(j);
      localStringBuilder.append(" [");
      localStringBuilder.append(localWindowNode.getLeft());
      localStringBuilder.append(",");
      localStringBuilder.append(localWindowNode.getTop());
      localStringBuilder.append(" ");
      localStringBuilder.append(localWindowNode.getWidth());
      localStringBuilder.append("x");
      localStringBuilder.append(localWindowNode.getHeight());
      localStringBuilder.append("] ");
      localStringBuilder.append(localWindowNode.getTitle());
      Log.i("AssistStructure", localStringBuilder.toString());
      dump("  ", localWindowNode.getRootViewNode(), paramBoolean);
    }
  }
  
  public void ensureData()
  {
    if (mHaveData) {
      return;
    }
    mHaveData = true;
    new ParcelTransferReader(mReceiveChannel).go();
  }
  
  public void ensureDataForAutofill()
  {
    if (mHaveData) {
      return;
    }
    mHaveData = true;
    Binder.allowBlocking(mReceiveChannel);
    try
    {
      ParcelTransferReader localParcelTransferReader = new android/app/assist/AssistStructure$ParcelTransferReader;
      localParcelTransferReader.<init>(this, mReceiveChannel);
      localParcelTransferReader.go();
      return;
    }
    finally
    {
      Binder.defaultBlocking(mReceiveChannel);
    }
  }
  
  public long getAcquisitionEndTime()
  {
    ensureData();
    return mAcquisitionEndTime;
  }
  
  public long getAcquisitionStartTime()
  {
    ensureData();
    return mAcquisitionStartTime;
  }
  
  public ComponentName getActivityComponent()
  {
    ensureData();
    return mActivityComponent;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public WindowNode getWindowNodeAt(int paramInt)
  {
    ensureData();
    return (WindowNode)mWindowNodes.get(paramInt);
  }
  
  public int getWindowNodeCount()
  {
    ensureData();
    return mWindowNodes.size();
  }
  
  public boolean isHomeActivity()
  {
    return mIsHomeActivity;
  }
  
  public void sanitizeForParceling(boolean paramBoolean)
  {
    mSanitizeOnWrite = paramBoolean;
  }
  
  public void setAcquisitionEndTime(long paramLong)
  {
    mAcquisitionEndTime = paramLong;
  }
  
  public void setAcquisitionStartTime(long paramLong)
  {
    mAcquisitionStartTime = paramLong;
  }
  
  public void setActivityComponent(ComponentName paramComponentName)
  {
    ensureData();
    mActivityComponent = paramComponentName;
  }
  
  public void setHomeActivity(boolean paramBoolean)
  {
    mIsHomeActivity = paramBoolean;
  }
  
  boolean waitForReady()
  {
    int i = 0;
    try
    {
      long l1 = SystemClock.uptimeMillis() + 5000L;
      for (;;)
      {
        if (mPendingAsyncChildren.size() > 0)
        {
          long l2 = SystemClock.uptimeMillis();
          if (l2 < l1) {
            try
            {
              wait(l1 - l2);
            }
            catch (InterruptedException localInterruptedException)
            {
              for (;;) {}
            }
          }
        }
      }
      if (mPendingAsyncChildren.size() > 0)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Skipping assist structure, waiting too long for async children (have ");
        localStringBuilder.append(mPendingAsyncChildren.size());
        localStringBuilder.append(" remaining");
        Log.w("AssistStructure", localStringBuilder.toString());
        i = 1;
      }
      boolean bool;
      if (i == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally {}
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mIsHomeActivity);
    if (mHaveData)
    {
      if (mSendChannel == null) {
        mSendChannel = new SendChannel(this);
      }
      paramParcel.writeStrongBinder(mSendChannel);
    }
    else
    {
      paramParcel.writeStrongBinder(mReceiveChannel);
    }
  }
  
  public static class AutofillOverlay
  {
    public boolean focused;
    public AutofillValue value;
    
    public AutofillOverlay() {}
  }
  
  private static final class HtmlInfoNode
    extends ViewStructure.HtmlInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<HtmlInfoNode> CREATOR = new Parcelable.Creator()
    {
      public AssistStructure.HtmlInfoNode createFromParcel(Parcel paramAnonymousParcel)
      {
        AssistStructure.HtmlInfoNodeBuilder localHtmlInfoNodeBuilder = new AssistStructure.HtmlInfoNodeBuilder(paramAnonymousParcel.readString());
        String[] arrayOfString = paramAnonymousParcel.readStringArray();
        paramAnonymousParcel = paramAnonymousParcel.readStringArray();
        if ((arrayOfString != null) && (paramAnonymousParcel != null)) {
          if (arrayOfString.length != paramAnonymousParcel.length)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("HtmlInfo attributes mismatch: names=");
            localStringBuilder.append(arrayOfString.length);
            localStringBuilder.append(", values=");
            localStringBuilder.append(paramAnonymousParcel.length);
            Log.w("AssistStructure", localStringBuilder.toString());
          }
          else
          {
            for (int i = 0; i < arrayOfString.length; i++) {
              localHtmlInfoNodeBuilder.addAttribute(arrayOfString[i], paramAnonymousParcel[i]);
            }
          }
        }
        return localHtmlInfoNodeBuilder.build();
      }
      
      public AssistStructure.HtmlInfoNode[] newArray(int paramAnonymousInt)
      {
        return new AssistStructure.HtmlInfoNode[paramAnonymousInt];
      }
    };
    private ArrayList<Pair<String, String>> mAttributes;
    private final String[] mNames;
    private final String mTag;
    private final String[] mValues;
    
    private HtmlInfoNode(AssistStructure.HtmlInfoNodeBuilder paramHtmlInfoNodeBuilder)
    {
      mTag = AssistStructure.HtmlInfoNodeBuilder.access$300(paramHtmlInfoNodeBuilder);
      if (AssistStructure.HtmlInfoNodeBuilder.access$400(paramHtmlInfoNodeBuilder) == null)
      {
        mNames = null;
        mValues = null;
      }
      else
      {
        mNames = new String[AssistStructure.HtmlInfoNodeBuilder.access$400(paramHtmlInfoNodeBuilder).size()];
        mValues = new String[AssistStructure.HtmlInfoNodeBuilder.access$500(paramHtmlInfoNodeBuilder).size()];
        AssistStructure.HtmlInfoNodeBuilder.access$400(paramHtmlInfoNodeBuilder).toArray(mNames);
        AssistStructure.HtmlInfoNodeBuilder.access$500(paramHtmlInfoNodeBuilder).toArray(mValues);
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public List<Pair<String, String>> getAttributes()
    {
      if ((mAttributes == null) && (mNames != null))
      {
        mAttributes = new ArrayList(mNames.length);
        for (int i = 0; i < mNames.length; i++)
        {
          Pair localPair = new Pair(mNames[i], mValues[i]);
          mAttributes.add(i, localPair);
        }
      }
      return mAttributes;
    }
    
    public String getTag()
    {
      return mTag;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mTag);
      paramParcel.writeStringArray(mNames);
      paramParcel.writeStringArray(mValues);
    }
  }
  
  private static final class HtmlInfoNodeBuilder
    extends ViewStructure.HtmlInfo.Builder
  {
    private ArrayList<String> mNames;
    private final String mTag;
    private ArrayList<String> mValues;
    
    HtmlInfoNodeBuilder(String paramString)
    {
      mTag = paramString;
    }
    
    public ViewStructure.HtmlInfo.Builder addAttribute(String paramString1, String paramString2)
    {
      if (mNames == null)
      {
        mNames = new ArrayList();
        mValues = new ArrayList();
      }
      mNames.add(paramString1);
      mValues.add(paramString2);
      return this;
    }
    
    public AssistStructure.HtmlInfoNode build()
    {
      return new AssistStructure.HtmlInfoNode(this, null);
    }
  }
  
  final class ParcelTransferReader
  {
    private final IBinder mChannel;
    private Parcel mCurParcel;
    int mNumReadViews;
    int mNumReadWindows;
    PooledStringReader mStringReader;
    final float[] mTmpMatrix = new float[9];
    private IBinder mTransferToken;
    
    ParcelTransferReader(IBinder paramIBinder)
    {
      mChannel = paramIBinder;
    }
    
    /* Error */
    private void fetchData()
    {
      // Byte code:
      //   0: invokestatic 42	android/os/Parcel:obtain	()Landroid/os/Parcel;
      //   3: astore_1
      //   4: aload_1
      //   5: ldc 44
      //   7: invokevirtual 48	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
      //   10: aload_1
      //   11: aload_0
      //   12: getfield 50	android/app/assist/AssistStructure$ParcelTransferReader:mTransferToken	Landroid/os/IBinder;
      //   15: invokevirtual 54	android/os/Parcel:writeStrongBinder	(Landroid/os/IBinder;)V
      //   18: aload_0
      //   19: getfield 56	android/app/assist/AssistStructure$ParcelTransferReader:mCurParcel	Landroid/os/Parcel;
      //   22: ifnull +10 -> 32
      //   25: aload_0
      //   26: getfield 56	android/app/assist/AssistStructure$ParcelTransferReader:mCurParcel	Landroid/os/Parcel;
      //   29: invokevirtual 59	android/os/Parcel:recycle	()V
      //   32: aload_0
      //   33: invokestatic 42	android/os/Parcel:obtain	()Landroid/os/Parcel;
      //   36: putfield 56	android/app/assist/AssistStructure$ParcelTransferReader:mCurParcel	Landroid/os/Parcel;
      //   39: aload_0
      //   40: getfield 32	android/app/assist/AssistStructure$ParcelTransferReader:mChannel	Landroid/os/IBinder;
      //   43: iconst_2
      //   44: aload_1
      //   45: aload_0
      //   46: getfield 56	android/app/assist/AssistStructure$ParcelTransferReader:mCurParcel	Landroid/os/Parcel;
      //   49: iconst_0
      //   50: invokeinterface 65 5 0
      //   55: pop
      //   56: aload_1
      //   57: invokevirtual 59	android/os/Parcel:recycle	()V
      //   60: aload_0
      //   61: iconst_0
      //   62: putfield 67	android/app/assist/AssistStructure$ParcelTransferReader:mNumReadViews	I
      //   65: aload_0
      //   66: iconst_0
      //   67: putfield 69	android/app/assist/AssistStructure$ParcelTransferReader:mNumReadWindows	I
      //   70: return
      //   71: astore_2
      //   72: ldc 71
      //   74: ldc 73
      //   76: aload_2
      //   77: invokestatic 79	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   80: pop
      //   81: new 81	java/lang/IllegalStateException
      //   84: astore_3
      //   85: new 83	java/lang/StringBuilder
      //   88: astore 4
      //   90: aload 4
      //   92: invokespecial 84	java/lang/StringBuilder:<init>	()V
      //   95: aload 4
      //   97: ldc 86
      //   99: invokevirtual 90	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   102: pop
      //   103: aload 4
      //   105: aload_2
      //   106: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   109: pop
      //   110: aload_3
      //   111: aload 4
      //   113: invokevirtual 97	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   116: invokespecial 99	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
      //   119: aload_3
      //   120: athrow
      //   121: astore 4
      //   123: aload_1
      //   124: invokevirtual 59	android/os/Parcel:recycle	()V
      //   127: aload 4
      //   129: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	130	0	this	ParcelTransferReader
      //   3	121	1	localParcel	Parcel
      //   71	35	2	localRemoteException	RemoteException
      //   84	36	3	localIllegalStateException	IllegalStateException
      //   88	24	4	localStringBuilder	StringBuilder
      //   121	7	4	localObject	Object
      // Exception table:
      //   from	to	target	type
      //   39	56	71	android/os/RemoteException
      //   4	32	121	finally
      //   32	39	121	finally
      //   39	56	121	finally
      //   72	121	121	finally
    }
    
    void go()
    {
      fetchData();
      mActivityComponent = ComponentName.readFromParcel(mCurParcel);
      AssistStructure.access$002(AssistStructure.this, mCurParcel.readInt());
      AssistStructure.access$102(AssistStructure.this, mCurParcel.readLong());
      AssistStructure.access$202(AssistStructure.this, mCurParcel.readLong());
      int i = mCurParcel.readInt();
      if (i > 0)
      {
        mStringReader = new PooledStringReader(mCurParcel);
        for (int j = 0; j < i; j++) {
          mWindowNodes.add(new AssistStructure.WindowNode(this));
        }
      }
      mCurParcel.recycle();
      mCurParcel = null;
    }
    
    Parcel readParcel(int paramInt1, int paramInt2)
    {
      paramInt2 = mCurParcel.readInt();
      if (paramInt2 != 0)
      {
        if (paramInt2 == paramInt1) {
          return mCurParcel;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Got token ");
        localStringBuilder.append(Integer.toHexString(paramInt2));
        localStringBuilder.append(", expected token ");
        localStringBuilder.append(Integer.toHexString(paramInt1));
        throw new BadParcelableException(localStringBuilder.toString());
      }
      mTransferToken = mCurParcel.readStrongBinder();
      if (mTransferToken != null)
      {
        fetchData();
        mStringReader = new PooledStringReader(mCurParcel);
        mCurParcel.readInt();
        return mCurParcel;
      }
      throw new IllegalStateException("Reached end of partial data without transfer token");
    }
  }
  
  static final class ParcelTransferWriter
    extends Binder
  {
    AssistStructure.ViewStackEntry mCurViewStackEntry;
    int mCurViewStackPos;
    int mCurWindow;
    int mNumWindows;
    int mNumWrittenViews;
    int mNumWrittenWindows;
    final boolean mSanitizeOnWrite;
    final float[] mTmpMatrix = new float[9];
    final ArrayList<AssistStructure.ViewStackEntry> mViewStack = new ArrayList();
    final boolean mWriteStructure;
    
    ParcelTransferWriter(AssistStructure paramAssistStructure, Parcel paramParcel)
    {
      mSanitizeOnWrite = mSanitizeOnWrite;
      mWriteStructure = paramAssistStructure.waitForReady();
      ComponentName.writeToParcel(mActivityComponent, paramParcel);
      paramParcel.writeInt(mFlags);
      paramParcel.writeLong(mAcquisitionStartTime);
      paramParcel.writeLong(mAcquisitionEndTime);
      mNumWindows = mWindowNodes.size();
      if ((mWriteStructure) && (mNumWindows > 0)) {
        paramParcel.writeInt(mNumWindows);
      } else {
        paramParcel.writeInt(0);
      }
    }
    
    void pushViewStackEntry(AssistStructure.ViewNode paramViewNode, int paramInt)
    {
      AssistStructure.ViewStackEntry localViewStackEntry;
      if (paramInt >= mViewStack.size())
      {
        localViewStackEntry = new AssistStructure.ViewStackEntry();
        mViewStack.add(localViewStackEntry);
      }
      else
      {
        localViewStackEntry = (AssistStructure.ViewStackEntry)mViewStack.get(paramInt);
      }
      node = paramViewNode;
      numChildren = paramViewNode.getChildCount();
      curChild = 0;
      mCurViewStackEntry = localViewStackEntry;
    }
    
    boolean writeNextEntryToParcel(AssistStructure paramAssistStructure, Parcel paramParcel, PooledStringWriter paramPooledStringWriter)
    {
      if (mCurViewStackEntry != null)
      {
        if (mCurViewStackEntry.curChild < mCurViewStackEntry.numChildren)
        {
          paramAssistStructure = mCurViewStackEntry.node.mChildren[mCurViewStackEntry.curChild];
          AssistStructure.ViewStackEntry localViewStackEntry = mCurViewStackEntry;
          curChild += 1;
          writeView(paramAssistStructure, paramParcel, paramPooledStringWriter, 1);
          return true;
        }
        do
        {
          i = mCurViewStackPos - 1;
          mCurViewStackPos = i;
          if (i < 0)
          {
            mCurViewStackEntry = null;
            break;
          }
          mCurViewStackEntry = ((AssistStructure.ViewStackEntry)mViewStack.get(i));
        } while (mCurViewStackEntry.curChild >= mCurViewStackEntry.numChildren);
        return true;
      }
      int i = mCurWindow;
      if (i < mNumWindows)
      {
        paramAssistStructure = (AssistStructure.WindowNode)mWindowNodes.get(i);
        mCurWindow += 1;
        paramParcel.writeInt(286331153);
        paramAssistStructure.writeSelfToParcel(paramParcel, paramPooledStringWriter, mTmpMatrix);
        mNumWrittenWindows += 1;
        paramAssistStructure = mRoot;
        mCurViewStackPos = 0;
        writeView(paramAssistStructure, paramParcel, paramPooledStringWriter, 0);
        return true;
      }
      return false;
    }
    
    void writeToParcel(AssistStructure paramAssistStructure, Parcel paramParcel)
    {
      int i = paramParcel.dataPosition();
      mNumWrittenWindows = 0;
      mNumWrittenViews = 0;
      boolean bool = writeToParcelInner(paramAssistStructure, paramParcel);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Flattened ");
      if (bool) {
        paramAssistStructure = "partial";
      } else {
        paramAssistStructure = "final";
      }
      localStringBuilder.append(paramAssistStructure);
      localStringBuilder.append(" assist data: ");
      localStringBuilder.append(paramParcel.dataPosition() - i);
      localStringBuilder.append(" bytes, containing ");
      localStringBuilder.append(mNumWrittenWindows);
      localStringBuilder.append(" windows, ");
      localStringBuilder.append(mNumWrittenViews);
      localStringBuilder.append(" views");
      Log.i("AssistStructure", localStringBuilder.toString());
    }
    
    boolean writeToParcelInner(AssistStructure paramAssistStructure, Parcel paramParcel)
    {
      if (mNumWindows == 0) {
        return false;
      }
      PooledStringWriter localPooledStringWriter = new PooledStringWriter(paramParcel);
      while (writeNextEntryToParcel(paramAssistStructure, paramParcel, localPooledStringWriter)) {
        if (paramParcel.dataSize() > 65536)
        {
          paramParcel.writeInt(0);
          paramParcel.writeStrongBinder(this);
          localPooledStringWriter.finish();
          return true;
        }
      }
      localPooledStringWriter.finish();
      mViewStack.clear();
      return false;
    }
    
    void writeView(AssistStructure.ViewNode paramViewNode, Parcel paramParcel, PooledStringWriter paramPooledStringWriter, int paramInt)
    {
      paramParcel.writeInt(572662306);
      paramInt = paramViewNode.writeSelfToParcel(paramParcel, paramPooledStringWriter, mSanitizeOnWrite, mTmpMatrix);
      mNumWrittenViews += 1;
      if ((0x100000 & paramInt) != 0)
      {
        paramParcel.writeInt(mChildren.length);
        paramInt = mCurViewStackPos + 1;
        mCurViewStackPos = paramInt;
        pushViewStackEntry(paramViewNode, paramInt);
      }
    }
  }
  
  static final class SendChannel
    extends Binder
  {
    volatile AssistStructure mAssistStructure;
    
    SendChannel(AssistStructure paramAssistStructure)
    {
      mAssistStructure = paramAssistStructure;
    }
    
    protected boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 == 2)
      {
        AssistStructure localAssistStructure = mAssistStructure;
        if (localAssistStructure == null) {
          return true;
        }
        paramParcel1.enforceInterface("android.app.AssistStructure");
        paramParcel1 = paramParcel1.readStrongBinder();
        if (paramParcel1 != null)
        {
          if ((paramParcel1 instanceof AssistStructure.ParcelTransferWriter))
          {
            ((AssistStructure.ParcelTransferWriter)paramParcel1).writeToParcel(localAssistStructure, paramParcel2);
            return true;
          }
          paramParcel2 = new StringBuilder();
          paramParcel2.append("Caller supplied bad token type: ");
          paramParcel2.append(paramParcel1);
          Log.w("AssistStructure", paramParcel2.toString());
          return true;
        }
        new AssistStructure.ParcelTransferWriter(localAssistStructure, paramParcel2).writeToParcel(localAssistStructure, paramParcel2);
        return true;
      }
      return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
    }
  }
  
  public static class ViewNode
  {
    static final int FLAGS_ACCESSIBILITY_FOCUSED = 4096;
    static final int FLAGS_ACTIVATED = 8192;
    static final int FLAGS_ALL_CONTROL = -1048576;
    static final int FLAGS_ASSIST_BLOCKED = 128;
    static final int FLAGS_CHECKABLE = 256;
    static final int FLAGS_CHECKED = 512;
    static final int FLAGS_CLICKABLE = 1024;
    static final int FLAGS_CONTEXT_CLICKABLE = 16384;
    static final int FLAGS_DISABLED = 1;
    static final int FLAGS_FOCUSABLE = 16;
    static final int FLAGS_FOCUSED = 32;
    static final int FLAGS_HAS_ALPHA = 536870912;
    static final int FLAGS_HAS_AUTOFILL_DATA = Integer.MIN_VALUE;
    static final int FLAGS_HAS_CHILDREN = 1048576;
    static final int FLAGS_HAS_COMPLEX_TEXT = 8388608;
    static final int FLAGS_HAS_CONTENT_DESCRIPTION = 33554432;
    static final int FLAGS_HAS_ELEVATION = 268435456;
    static final int FLAGS_HAS_EXTRAS = 4194304;
    static final int FLAGS_HAS_ID = 2097152;
    static final int FLAGS_HAS_INPUT_TYPE = 262144;
    static final int FLAGS_HAS_LARGE_COORDS = 67108864;
    static final int FLAGS_HAS_LOCALE_LIST = 65536;
    static final int FLAGS_HAS_MATRIX = 1073741824;
    static final int FLAGS_HAS_SCROLL = 134217728;
    static final int FLAGS_HAS_TEXT = 16777216;
    static final int FLAGS_HAS_URL = 524288;
    static final int FLAGS_LONG_CLICKABLE = 2048;
    static final int FLAGS_OPAQUE = 32768;
    static final int FLAGS_SELECTED = 64;
    static final int FLAGS_VISIBILITY_MASK = 12;
    public static final int TEXT_COLOR_UNDEFINED = 1;
    public static final int TEXT_STYLE_BOLD = 1;
    public static final int TEXT_STYLE_ITALIC = 2;
    public static final int TEXT_STYLE_STRIKE_THRU = 8;
    public static final int TEXT_STYLE_UNDERLINE = 4;
    float mAlpha;
    String[] mAutofillHints;
    AutofillId mAutofillId;
    CharSequence[] mAutofillOptions;
    AssistStructure.AutofillOverlay mAutofillOverlay;
    int mAutofillType;
    AutofillValue mAutofillValue;
    ViewNode[] mChildren;
    String mClassName;
    CharSequence mContentDescription;
    float mElevation;
    Bundle mExtras;
    int mFlags;
    int mHeight;
    ViewStructure.HtmlInfo mHtmlInfo;
    int mId = -1;
    String mIdEntry;
    String mIdPackage;
    String mIdType;
    int mImportantForAutofill;
    int mInputType;
    LocaleList mLocaleList;
    Matrix mMatrix;
    int mMaxEms;
    int mMaxLength;
    int mMinEms;
    boolean mSanitized;
    int mScrollX;
    int mScrollY;
    AssistStructure.ViewNodeText mText;
    String mTextIdEntry;
    String mWebDomain;
    String mWebScheme;
    int mWidth;
    int mX;
    int mY;
    
    ViewNode()
    {
      mAutofillType = 0;
      mMinEms = -1;
      mMaxEms = -1;
      mMaxLength = -1;
      mAlpha = 1.0F;
    }
    
    ViewNode(AssistStructure.ParcelTransferReader paramParcelTransferReader, int paramInt)
    {
      int i = 0;
      mAutofillType = 0;
      mMinEms = -1;
      mMaxEms = -1;
      mMaxLength = -1;
      mAlpha = 1.0F;
      Parcel localParcel = paramParcelTransferReader.readParcel(572662306, paramInt);
      int j = mNumReadViews;
      boolean bool1 = true;
      mNumReadViews = (j + 1);
      PooledStringReader localPooledStringReader = mStringReader;
      mClassName = localPooledStringReader.readString();
      mFlags = localParcel.readInt();
      j = mFlags;
      if ((0x200000 & j) != 0)
      {
        mId = localParcel.readInt();
        if (mId != -1)
        {
          mIdEntry = localPooledStringReader.readString();
          if (mIdEntry != null)
          {
            mIdType = localPooledStringReader.readString();
            mIdPackage = localPooledStringReader.readString();
          }
        }
      }
      boolean bool2;
      if ((0x80000000 & j) != 0)
      {
        if (localParcel.readInt() == 1) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        mSanitized = bool2;
        mAutofillId = ((AutofillId)localParcel.readParcelable(null));
        mAutofillType = localParcel.readInt();
        mAutofillHints = localParcel.readStringArray();
        mAutofillValue = ((AutofillValue)localParcel.readParcelable(null));
        mAutofillOptions = localParcel.readCharSequenceArray();
        Parcelable localParcelable = localParcel.readParcelable(null);
        if ((localParcelable instanceof ViewStructure.HtmlInfo)) {
          mHtmlInfo = ((ViewStructure.HtmlInfo)localParcelable);
        }
        mMinEms = localParcel.readInt();
        mMaxEms = localParcel.readInt();
        mMaxLength = localParcel.readInt();
        mTextIdEntry = localPooledStringReader.readString();
        mImportantForAutofill = localParcel.readInt();
      }
      if ((0x4000000 & j) != 0)
      {
        mX = localParcel.readInt();
        mY = localParcel.readInt();
        mWidth = localParcel.readInt();
        mHeight = localParcel.readInt();
      }
      else
      {
        int k = localParcel.readInt();
        mX = (k & 0x7FFF);
        mY = (k >> 16 & 0x7FFF);
        k = localParcel.readInt();
        mWidth = (k & 0x7FFF);
        mHeight = (k >> 16 & 0x7FFF);
      }
      if ((0x8000000 & j) != 0)
      {
        mScrollX = localParcel.readInt();
        mScrollY = localParcel.readInt();
      }
      if ((0x40000000 & j) != 0)
      {
        mMatrix = new Matrix();
        localParcel.readFloatArray(mTmpMatrix);
        mMatrix.setValues(mTmpMatrix);
      }
      if ((0x10000000 & j) != 0) {
        mElevation = localParcel.readFloat();
      }
      if ((0x20000000 & j) != 0) {
        mAlpha = localParcel.readFloat();
      }
      if ((0x2000000 & j) != 0) {
        mContentDescription = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel));
      }
      if ((0x1000000 & j) != 0)
      {
        if ((0x800000 & j) == 0) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
        mText = new AssistStructure.ViewNodeText(localParcel, bool2);
      }
      if ((0x40000 & j) != 0) {
        mInputType = localParcel.readInt();
      }
      if ((0x80000 & j) != 0)
      {
        mWebScheme = localParcel.readString();
        mWebDomain = localParcel.readString();
      }
      if ((0x10000 & j) != 0) {
        mLocaleList = ((LocaleList)localParcel.readParcelable(null));
      }
      if ((0x400000 & j) != 0) {
        mExtras = localParcel.readBundle();
      }
      if ((0x100000 & j) != 0)
      {
        j = localParcel.readInt();
        mChildren = new ViewNode[j];
        while (i < j)
        {
          mChildren[i] = new ViewNode(paramParcelTransferReader, paramInt + 1);
          i++;
        }
      }
    }
    
    public float getAlpha()
    {
      return mAlpha;
    }
    
    public String[] getAutofillHints()
    {
      return mAutofillHints;
    }
    
    public AutofillId getAutofillId()
    {
      return mAutofillId;
    }
    
    public CharSequence[] getAutofillOptions()
    {
      return mAutofillOptions;
    }
    
    public int getAutofillType()
    {
      return mAutofillType;
    }
    
    public AutofillValue getAutofillValue()
    {
      return mAutofillValue;
    }
    
    public ViewNode getChildAt(int paramInt)
    {
      return mChildren[paramInt];
    }
    
    public int getChildCount()
    {
      int i;
      if (mChildren != null) {
        i = mChildren.length;
      } else {
        i = 0;
      }
      return i;
    }
    
    public String getClassName()
    {
      return mClassName;
    }
    
    public CharSequence getContentDescription()
    {
      return mContentDescription;
    }
    
    public float getElevation()
    {
      return mElevation;
    }
    
    public Bundle getExtras()
    {
      return mExtras;
    }
    
    public int getHeight()
    {
      return mHeight;
    }
    
    public String getHint()
    {
      String str;
      if (mText != null) {
        str = mText.mHint;
      } else {
        str = null;
      }
      return str;
    }
    
    public ViewStructure.HtmlInfo getHtmlInfo()
    {
      return mHtmlInfo;
    }
    
    public int getId()
    {
      return mId;
    }
    
    public String getIdEntry()
    {
      return mIdEntry;
    }
    
    public String getIdPackage()
    {
      return mIdPackage;
    }
    
    public String getIdType()
    {
      return mIdType;
    }
    
    public int getImportantForAutofill()
    {
      return mImportantForAutofill;
    }
    
    public int getInputType()
    {
      return mInputType;
    }
    
    public int getLeft()
    {
      return mX;
    }
    
    public LocaleList getLocaleList()
    {
      return mLocaleList;
    }
    
    public int getMaxTextEms()
    {
      return mMaxEms;
    }
    
    public int getMaxTextLength()
    {
      return mMaxLength;
    }
    
    public int getMinTextEms()
    {
      return mMinEms;
    }
    
    public int getScrollX()
    {
      return mScrollX;
    }
    
    public int getScrollY()
    {
      return mScrollY;
    }
    
    public CharSequence getText()
    {
      CharSequence localCharSequence;
      if (mText != null) {
        localCharSequence = mText.mText;
      } else {
        localCharSequence = null;
      }
      return localCharSequence;
    }
    
    public int getTextBackgroundColor()
    {
      int i;
      if (mText != null) {
        i = mText.mTextBackgroundColor;
      } else {
        i = 1;
      }
      return i;
    }
    
    public int getTextColor()
    {
      int i;
      if (mText != null) {
        i = mText.mTextColor;
      } else {
        i = 1;
      }
      return i;
    }
    
    public String getTextIdEntry()
    {
      return mTextIdEntry;
    }
    
    public int[] getTextLineBaselines()
    {
      int[] arrayOfInt;
      if (mText != null) {
        arrayOfInt = mText.mLineBaselines;
      } else {
        arrayOfInt = null;
      }
      return arrayOfInt;
    }
    
    public int[] getTextLineCharOffsets()
    {
      int[] arrayOfInt;
      if (mText != null) {
        arrayOfInt = mText.mLineCharOffsets;
      } else {
        arrayOfInt = null;
      }
      return arrayOfInt;
    }
    
    public int getTextSelectionEnd()
    {
      int i;
      if (mText != null) {
        i = mText.mTextSelectionEnd;
      } else {
        i = -1;
      }
      return i;
    }
    
    public int getTextSelectionStart()
    {
      int i;
      if (mText != null) {
        i = mText.mTextSelectionStart;
      } else {
        i = -1;
      }
      return i;
    }
    
    public float getTextSize()
    {
      float f;
      if (mText != null) {
        f = mText.mTextSize;
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    public int getTextStyle()
    {
      int i;
      if (mText != null) {
        i = mText.mTextStyle;
      } else {
        i = 0;
      }
      return i;
    }
    
    public int getTop()
    {
      return mY;
    }
    
    public Matrix getTransformation()
    {
      return mMatrix;
    }
    
    public int getVisibility()
    {
      return mFlags & 0xC;
    }
    
    public String getWebDomain()
    {
      return mWebDomain;
    }
    
    public String getWebScheme()
    {
      return mWebScheme;
    }
    
    public int getWidth()
    {
      return mWidth;
    }
    
    public boolean isAccessibilityFocused()
    {
      boolean bool;
      if ((mFlags & 0x1000) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isActivated()
    {
      boolean bool;
      if ((mFlags & 0x2000) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isAssistBlocked()
    {
      boolean bool;
      if ((mFlags & 0x80) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isCheckable()
    {
      boolean bool;
      if ((mFlags & 0x100) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isChecked()
    {
      boolean bool;
      if ((mFlags & 0x200) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isClickable()
    {
      boolean bool;
      if ((mFlags & 0x400) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isContextClickable()
    {
      boolean bool;
      if ((mFlags & 0x4000) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isEnabled()
    {
      int i = mFlags;
      boolean bool = true;
      if ((i & 0x1) != 0) {
        bool = false;
      }
      return bool;
    }
    
    public boolean isFocusable()
    {
      boolean bool;
      if ((mFlags & 0x10) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isFocused()
    {
      boolean bool;
      if ((mFlags & 0x20) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isLongClickable()
    {
      boolean bool;
      if ((mFlags & 0x800) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isOpaque()
    {
      boolean bool;
      if ((mFlags & 0x8000) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isSanitized()
    {
      return mSanitized;
    }
    
    public boolean isSelected()
    {
      boolean bool;
      if ((mFlags & 0x40) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void setAutofillOverlay(AssistStructure.AutofillOverlay paramAutofillOverlay)
    {
      mAutofillOverlay = paramAutofillOverlay;
    }
    
    public void setWebDomain(String paramString)
    {
      if (paramString == null) {
        return;
      }
      paramString = Uri.parse(paramString);
      if (paramString == null)
      {
        Log.w("AssistStructure", "Failed to parse web domain");
        return;
      }
      mWebScheme = paramString.getScheme();
      mWebDomain = paramString.getHost();
    }
    
    public void updateAutofillValue(AutofillValue paramAutofillValue)
    {
      mAutofillValue = paramAutofillValue;
      if (paramAutofillValue.isText())
      {
        if (mText == null) {
          mText = new AssistStructure.ViewNodeText();
        }
        mText.mText = paramAutofillValue.getTextValue();
      }
    }
    
    int writeSelfToParcel(Parcel paramParcel, PooledStringWriter paramPooledStringWriter, boolean paramBoolean, float[] paramArrayOfFloat)
    {
      boolean bool = true;
      int i = mFlags & 0xFFFFF;
      int j = i;
      if (mId != -1) {
        j = i | 0x200000;
      }
      i = j;
      if (mAutofillId != null) {
        i = j | 0x80000000;
      }
      if (((mX & 0x8000) == 0) && ((mY & 0x8000) == 0))
      {
        if ((mWidth & 0x8000) != 0) {
          k = 1;
        } else {
          k = 0;
        }
        int m;
        if ((mHeight & 0x8000) != 0) {
          m = 1;
        } else {
          m = 0;
        }
        j = i;
        if ((k | m) == 0) {}
      }
      else
      {
        j = i | 0x4000000;
      }
      if (mScrollX == 0)
      {
        k = j;
        if (mScrollY == 0) {}
      }
      else
      {
        k = j | 0x8000000;
      }
      i = k;
      if (mMatrix != null) {
        i = k | 0x40000000;
      }
      j = i;
      if (mElevation != 0.0F) {
        j = i | 0x10000000;
      }
      i = j;
      if (mAlpha != 1.0F) {
        i = j | 0x20000000;
      }
      j = i;
      if (mContentDescription != null) {
        j = i | 0x2000000;
      }
      i = j;
      if (mText != null)
      {
        j |= 0x1000000;
        i = j;
        if (!mText.isSimple()) {
          i = j | 0x800000;
        }
      }
      j = i;
      if (mInputType != 0) {
        j = i | 0x40000;
      }
      if (mWebScheme == null)
      {
        i = j;
        if (mWebDomain == null) {}
      }
      else
      {
        i = j | 0x80000;
      }
      j = i;
      if (mLocaleList != null) {
        j = i | 0x10000;
      }
      int k = j;
      if (mExtras != null) {
        k = j | 0x400000;
      }
      i = k;
      if (mChildren != null) {
        i = k | 0x100000;
      }
      paramPooledStringWriter.writeString(mClassName);
      k = i;
      j = k;
      if ((i & 0x80000000) != 0) {
        if (!mSanitized)
        {
          j = k;
          if (paramBoolean) {}
        }
        else
        {
          j = i & 0xFDFF;
        }
      }
      k = j;
      if (mAutofillOverlay != null) {
        if (mAutofillOverlay.focused) {
          k = j | 0x20;
        } else {
          k = j & 0xFFFFFFDF;
        }
      }
      paramParcel.writeInt(k);
      if ((0x200000 & i) != 0)
      {
        paramParcel.writeInt(mId);
        if (mId != -1)
        {
          paramPooledStringWriter.writeString(mIdEntry);
          if (mIdEntry != null)
          {
            paramPooledStringWriter.writeString(mIdType);
            paramPooledStringWriter.writeString(mIdPackage);
          }
        }
      }
      if ((i & 0x80000000) != 0)
      {
        if ((!mSanitized) && (paramBoolean)) {
          paramBoolean = false;
        } else {
          paramBoolean = true;
        }
        paramParcel.writeInt(mSanitized);
        paramParcel.writeParcelable(mAutofillId, 0);
        paramParcel.writeInt(mAutofillType);
        paramParcel.writeStringArray(mAutofillHints);
        AutofillValue localAutofillValue;
        if (paramBoolean) {
          localAutofillValue = mAutofillValue;
        }
        for (;;)
        {
          break;
          if ((mAutofillOverlay != null) && (mAutofillOverlay.value != null)) {
            localAutofillValue = mAutofillOverlay.value;
          } else {
            localAutofillValue = null;
          }
        }
        paramParcel.writeParcelable(localAutofillValue, 0);
        paramParcel.writeCharSequenceArray(mAutofillOptions);
        if ((mHtmlInfo instanceof Parcelable)) {
          paramParcel.writeParcelable((Parcelable)mHtmlInfo, 0);
        } else {
          paramParcel.writeParcelable(null, 0);
        }
        paramParcel.writeInt(mMinEms);
        paramParcel.writeInt(mMaxEms);
        paramParcel.writeInt(mMaxLength);
        paramPooledStringWriter.writeString(mTextIdEntry);
        paramParcel.writeInt(mImportantForAutofill);
        bool = paramBoolean;
      }
      if ((i & 0x4000000) != 0)
      {
        paramParcel.writeInt(mX);
        paramParcel.writeInt(mY);
        paramParcel.writeInt(mWidth);
        paramParcel.writeInt(mHeight);
      }
      else
      {
        paramParcel.writeInt(mY << 16 | mX);
        paramParcel.writeInt(mHeight << 16 | mWidth);
      }
      if ((i & 0x8000000) != 0)
      {
        paramParcel.writeInt(mScrollX);
        paramParcel.writeInt(mScrollY);
      }
      if ((i & 0x40000000) != 0)
      {
        mMatrix.getValues(paramArrayOfFloat);
        paramParcel.writeFloatArray(paramArrayOfFloat);
      }
      if ((i & 0x10000000) != 0) {
        paramParcel.writeFloat(mElevation);
      }
      if ((i & 0x20000000) != 0) {
        paramParcel.writeFloat(mAlpha);
      }
      if ((i & 0x2000000) != 0) {
        TextUtils.writeToParcel(mContentDescription, paramParcel, 0);
      }
      if ((i & 0x1000000) != 0)
      {
        paramPooledStringWriter = mText;
        if ((i & 0x800000) == 0) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
        paramPooledStringWriter.writeToParcel(paramParcel, paramBoolean, bool);
      }
      if ((i & 0x40000) != 0) {
        paramParcel.writeInt(mInputType);
      }
      if ((0x80000 & i) != 0)
      {
        paramParcel.writeString(mWebScheme);
        paramParcel.writeString(mWebDomain);
      }
      if ((0x10000 & i) != 0) {
        paramParcel.writeParcelable(mLocaleList, 0);
      }
      if ((0x400000 & i) != 0) {
        paramParcel.writeBundle(mExtras);
      }
      return i;
    }
  }
  
  static class ViewNodeBuilder
    extends ViewStructure
  {
    final AssistStructure mAssist;
    final boolean mAsync;
    final AssistStructure.ViewNode mNode;
    
    ViewNodeBuilder(AssistStructure paramAssistStructure, AssistStructure.ViewNode paramViewNode, boolean paramBoolean)
    {
      mAssist = paramAssistStructure;
      mNode = paramViewNode;
      mAsync = paramBoolean;
    }
    
    private final AssistStructure.ViewNodeText getNodeText()
    {
      if (mNode.mText != null) {
        return mNode.mText;
      }
      mNode.mText = new AssistStructure.ViewNodeText();
      return mNode.mText;
    }
    
    public int addChildCount(int paramInt)
    {
      if (mNode.mChildren == null)
      {
        setChildCount(paramInt);
        return 0;
      }
      int i = mNode.mChildren.length;
      AssistStructure.ViewNode[] arrayOfViewNode = new AssistStructure.ViewNode[i + paramInt];
      System.arraycopy(mNode.mChildren, 0, arrayOfViewNode, 0, i);
      mNode.mChildren = arrayOfViewNode;
      return i;
    }
    
    public void asyncCommit()
    {
      synchronized (mAssist)
      {
        if (mAsync)
        {
          if (mAssist.mPendingAsyncChildren.remove(this))
          {
            mAssist.notifyAll();
            return;
          }
          localObject1 = new java/lang/IllegalStateException;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Child ");
          ((StringBuilder)localObject2).append(this);
          ((StringBuilder)localObject2).append(" already committed");
          ((IllegalStateException)localObject1).<init>(((StringBuilder)localObject2).toString());
          throw ((Throwable)localObject1);
        }
        Object localObject2 = new java/lang/IllegalStateException;
        Object localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Child ");
        ((StringBuilder)localObject1).append(this);
        ((StringBuilder)localObject1).append(" was not created with ViewStructure.asyncNewChild");
        ((IllegalStateException)localObject2).<init>(((StringBuilder)localObject1).toString());
        throw ((Throwable)localObject2);
      }
    }
    
    public ViewStructure asyncNewChild(int paramInt)
    {
      synchronized (mAssist)
      {
        AssistStructure.ViewNode localViewNode = new android/app/assist/AssistStructure$ViewNode;
        localViewNode.<init>();
        mNode.mChildren[paramInt] = localViewNode;
        ViewNodeBuilder localViewNodeBuilder = new android/app/assist/AssistStructure$ViewNodeBuilder;
        localViewNodeBuilder.<init>(mAssist, localViewNode, true);
        mAssist.mPendingAsyncChildren.add(localViewNodeBuilder);
        return localViewNodeBuilder;
      }
    }
    
    public AutofillId getAutofillId()
    {
      return mNode.mAutofillId;
    }
    
    public int getChildCount()
    {
      int i;
      if (mNode.mChildren != null) {
        i = mNode.mChildren.length;
      } else {
        i = 0;
      }
      return i;
    }
    
    public Bundle getExtras()
    {
      if (mNode.mExtras != null) {
        return mNode.mExtras;
      }
      mNode.mExtras = new Bundle();
      return mNode.mExtras;
    }
    
    public CharSequence getHint()
    {
      String str;
      if (mNode.mText != null) {
        str = mNode.mText.mHint;
      } else {
        str = null;
      }
      return str;
    }
    
    public Rect getTempRect()
    {
      return mAssist.mTmpRect;
    }
    
    public CharSequence getText()
    {
      CharSequence localCharSequence;
      if (mNode.mText != null) {
        localCharSequence = mNode.mText.mText;
      } else {
        localCharSequence = null;
      }
      return localCharSequence;
    }
    
    public int getTextSelectionEnd()
    {
      int i;
      if (mNode.mText != null) {
        i = mNode.mText.mTextSelectionEnd;
      } else {
        i = -1;
      }
      return i;
    }
    
    public int getTextSelectionStart()
    {
      int i;
      if (mNode.mText != null) {
        i = mNode.mText.mTextSelectionStart;
      } else {
        i = -1;
      }
      return i;
    }
    
    public boolean hasExtras()
    {
      boolean bool;
      if (mNode.mExtras != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public ViewStructure newChild(int paramInt)
    {
      AssistStructure.ViewNode localViewNode = new AssistStructure.ViewNode();
      mNode.mChildren[paramInt] = localViewNode;
      return new ViewNodeBuilder(mAssist, localViewNode, false);
    }
    
    public ViewStructure.HtmlInfo.Builder newHtmlInfoBuilder(String paramString)
    {
      return new AssistStructure.HtmlInfoNodeBuilder(paramString);
    }
    
    public void setAccessibilityFocused(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 4096;
      } else {
        j = 0;
      }
      mFlags = (i & 0xEFFF | j);
    }
    
    public void setActivated(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 8192;
      } else {
        j = 0;
      }
      mFlags = (i & 0xDFFF | j);
    }
    
    public void setAlpha(float paramFloat)
    {
      mNode.mAlpha = paramFloat;
    }
    
    public void setAssistBlocked(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 128;
      } else {
        j = 0;
      }
      mFlags = (i & 0xFF7F | j);
    }
    
    public void setAutofillHints(String[] paramArrayOfString)
    {
      mNode.mAutofillHints = paramArrayOfString;
    }
    
    public void setAutofillId(AutofillId paramAutofillId)
    {
      mNode.mAutofillId = paramAutofillId;
    }
    
    public void setAutofillId(AutofillId paramAutofillId, int paramInt)
    {
      mNode.mAutofillId = new AutofillId(paramAutofillId, paramInt);
    }
    
    public void setAutofillOptions(CharSequence[] paramArrayOfCharSequence)
    {
      mNode.mAutofillOptions = paramArrayOfCharSequence;
    }
    
    public void setAutofillType(int paramInt)
    {
      mNode.mAutofillType = paramInt;
    }
    
    public void setAutofillValue(AutofillValue paramAutofillValue)
    {
      mNode.mAutofillValue = paramAutofillValue;
    }
    
    public void setCheckable(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 256;
      } else {
        j = 0;
      }
      mFlags = (i & 0xFEFF | j);
    }
    
    public void setChecked(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 512;
      } else {
        j = 0;
      }
      mFlags = (i & 0xFDFF | j);
    }
    
    public void setChildCount(int paramInt)
    {
      mNode.mChildren = new AssistStructure.ViewNode[paramInt];
    }
    
    public void setClassName(String paramString)
    {
      mNode.mClassName = paramString;
    }
    
    public void setClickable(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 1024;
      } else {
        j = 0;
      }
      mFlags = (i & 0xFBFF | j);
    }
    
    public void setContentDescription(CharSequence paramCharSequence)
    {
      mNode.mContentDescription = paramCharSequence;
    }
    
    public void setContextClickable(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 16384;
      } else {
        j = 0;
      }
      mFlags = (i & 0xBFFF | j);
    }
    
    public void setDataIsSensitive(boolean paramBoolean)
    {
      mNode.mSanitized = (paramBoolean ^ true);
    }
    
    public void setDimens(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
    {
      mNode.mX = paramInt1;
      mNode.mY = paramInt2;
      mNode.mScrollX = paramInt3;
      mNode.mScrollY = paramInt4;
      mNode.mWidth = paramInt5;
      mNode.mHeight = paramInt6;
    }
    
    public void setElevation(float paramFloat)
    {
      mNode.mElevation = paramFloat;
    }
    
    public void setEnabled(boolean paramBoolean)
    {
      mNode.mFlags = (mNode.mFlags & 0xFFFFFFFE | paramBoolean ^ true);
    }
    
    public void setFocusable(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 16;
      } else {
        j = 0;
      }
      mFlags = (i & 0xFFFFFFEF | j);
    }
    
    public void setFocused(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 32;
      } else {
        j = 0;
      }
      mFlags = (i & 0xFFFFFFDF | j);
    }
    
    public void setHint(CharSequence paramCharSequence)
    {
      AssistStructure.ViewNodeText localViewNodeText = getNodeText();
      if (paramCharSequence != null) {
        paramCharSequence = paramCharSequence.toString();
      } else {
        paramCharSequence = null;
      }
      mHint = paramCharSequence;
    }
    
    public void setHtmlInfo(ViewStructure.HtmlInfo paramHtmlInfo)
    {
      mNode.mHtmlInfo = paramHtmlInfo;
    }
    
    public void setId(int paramInt, String paramString1, String paramString2, String paramString3)
    {
      mNode.mId = paramInt;
      mNode.mIdPackage = paramString1;
      mNode.mIdType = paramString2;
      mNode.mIdEntry = paramString3;
    }
    
    public void setImportantForAutofill(int paramInt)
    {
      mNode.mImportantForAutofill = paramInt;
    }
    
    public void setInputType(int paramInt)
    {
      mNode.mInputType = paramInt;
    }
    
    public void setLocaleList(LocaleList paramLocaleList)
    {
      mNode.mLocaleList = paramLocaleList;
    }
    
    public void setLongClickable(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 2048;
      } else {
        j = 0;
      }
      mFlags = (i & 0xF7FF | j);
    }
    
    public void setMaxTextEms(int paramInt)
    {
      mNode.mMaxEms = paramInt;
    }
    
    public void setMaxTextLength(int paramInt)
    {
      mNode.mMaxLength = paramInt;
    }
    
    public void setMinTextEms(int paramInt)
    {
      mNode.mMinEms = paramInt;
    }
    
    public void setOpaque(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 32768;
      } else {
        j = 0;
      }
      mFlags = (i & 0xFFFF7FFF | j);
    }
    
    public void setSelected(boolean paramBoolean)
    {
      AssistStructure.ViewNode localViewNode = mNode;
      int i = mNode.mFlags;
      int j;
      if (paramBoolean) {
        j = 64;
      } else {
        j = 0;
      }
      mFlags = (i & 0xFFFFFFBF | j);
    }
    
    public void setText(CharSequence paramCharSequence)
    {
      AssistStructure.ViewNodeText localViewNodeText = getNodeText();
      mText = TextUtils.trimNoCopySpans(paramCharSequence);
      mTextSelectionEnd = -1;
      mTextSelectionStart = -1;
    }
    
    public void setText(CharSequence paramCharSequence, int paramInt1, int paramInt2)
    {
      AssistStructure.ViewNodeText localViewNodeText = getNodeText();
      mText = TextUtils.trimNoCopySpans(paramCharSequence);
      mTextSelectionStart = paramInt1;
      mTextSelectionEnd = paramInt2;
    }
    
    public void setTextIdEntry(String paramString)
    {
      mNode.mTextIdEntry = ((String)Preconditions.checkNotNull(paramString));
    }
    
    public void setTextLines(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    {
      AssistStructure.ViewNodeText localViewNodeText = getNodeText();
      mLineCharOffsets = paramArrayOfInt1;
      mLineBaselines = paramArrayOfInt2;
    }
    
    public void setTextStyle(float paramFloat, int paramInt1, int paramInt2, int paramInt3)
    {
      AssistStructure.ViewNodeText localViewNodeText = getNodeText();
      mTextColor = paramInt1;
      mTextBackgroundColor = paramInt2;
      mTextSize = paramFloat;
      mTextStyle = paramInt3;
    }
    
    public void setTransformation(Matrix paramMatrix)
    {
      if (paramMatrix == null) {
        mNode.mMatrix = null;
      } else {
        mNode.mMatrix = new Matrix(paramMatrix);
      }
    }
    
    public void setVisibility(int paramInt)
    {
      mNode.mFlags = (mNode.mFlags & 0xFFFFFFF3 | paramInt);
    }
    
    public void setWebDomain(String paramString)
    {
      mNode.setWebDomain(paramString);
    }
  }
  
  static final class ViewNodeText
  {
    String mHint;
    int[] mLineBaselines;
    int[] mLineCharOffsets;
    CharSequence mText;
    int mTextBackgroundColor = 1;
    int mTextColor = 1;
    int mTextSelectionEnd;
    int mTextSelectionStart;
    float mTextSize;
    int mTextStyle;
    
    ViewNodeText() {}
    
    ViewNodeText(Parcel paramParcel, boolean paramBoolean)
    {
      mText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      mTextSize = paramParcel.readFloat();
      mTextStyle = paramParcel.readInt();
      mTextColor = paramParcel.readInt();
      if (!paramBoolean)
      {
        mTextBackgroundColor = paramParcel.readInt();
        mTextSelectionStart = paramParcel.readInt();
        mTextSelectionEnd = paramParcel.readInt();
        mLineCharOffsets = paramParcel.createIntArray();
        mLineBaselines = paramParcel.createIntArray();
        mHint = paramParcel.readString();
      }
    }
    
    boolean isSimple()
    {
      int i = mTextBackgroundColor;
      boolean bool = true;
      if ((i != 1) || (mTextSelectionStart != 0) || (mTextSelectionEnd != 0) || (mLineCharOffsets != null) || (mLineBaselines != null) || (mHint != null)) {
        bool = false;
      }
      return bool;
    }
    
    void writeToParcel(Parcel paramParcel, boolean paramBoolean1, boolean paramBoolean2)
    {
      Object localObject;
      if (paramBoolean2) {
        localObject = mText;
      } else {
        localObject = "";
      }
      TextUtils.writeToParcel((CharSequence)localObject, paramParcel, 0);
      paramParcel.writeFloat(mTextSize);
      paramParcel.writeInt(mTextStyle);
      paramParcel.writeInt(mTextColor);
      if (!paramBoolean1)
      {
        paramParcel.writeInt(mTextBackgroundColor);
        paramParcel.writeInt(mTextSelectionStart);
        paramParcel.writeInt(mTextSelectionEnd);
        paramParcel.writeIntArray(mLineCharOffsets);
        paramParcel.writeIntArray(mLineBaselines);
        paramParcel.writeString(mHint);
      }
    }
  }
  
  static final class ViewStackEntry
  {
    int curChild;
    AssistStructure.ViewNode node;
    int numChildren;
    
    ViewStackEntry() {}
  }
  
  public static class WindowNode
  {
    final int mDisplayId;
    final int mHeight;
    final AssistStructure.ViewNode mRoot;
    final CharSequence mTitle;
    final int mWidth;
    final int mX;
    final int mY;
    
    WindowNode(AssistStructure.ParcelTransferReader paramParcelTransferReader)
    {
      Parcel localParcel = paramParcelTransferReader.readParcel(286331153, 0);
      mNumReadWindows += 1;
      mX = localParcel.readInt();
      mY = localParcel.readInt();
      mWidth = localParcel.readInt();
      mHeight = localParcel.readInt();
      mTitle = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel));
      mDisplayId = localParcel.readInt();
      mRoot = new AssistStructure.ViewNode(paramParcelTransferReader, 0);
    }
    
    WindowNode(AssistStructure paramAssistStructure, ViewRootImpl paramViewRootImpl, boolean paramBoolean, int paramInt)
    {
      View localView = paramViewRootImpl.getView();
      Rect localRect = new Rect();
      localView.getBoundsOnScreen(localRect);
      mX = (left - localView.getLeft());
      mY = (top - localView.getTop());
      mWidth = localRect.width();
      mHeight = localRect.height();
      mTitle = paramViewRootImpl.getTitle();
      mDisplayId = paramViewRootImpl.getDisplayId();
      mRoot = new AssistStructure.ViewNode();
      paramAssistStructure = new AssistStructure.ViewNodeBuilder(paramAssistStructure, mRoot, false);
      if ((paramViewRootImpl.getWindowFlags() & 0x2000) != 0) {
        if (paramBoolean)
        {
          localView.onProvideAutofillStructure(paramAssistStructure, resolveViewAutofillFlags(localView.getContext(), paramInt));
        }
        else
        {
          localView.onProvideStructure(paramAssistStructure);
          paramAssistStructure.setAssistBlocked(true);
          return;
        }
      }
      if (paramBoolean) {
        localView.dispatchProvideAutofillStructure(paramAssistStructure, resolveViewAutofillFlags(localView.getContext(), paramInt));
      } else {
        localView.dispatchProvideStructure(paramAssistStructure);
      }
    }
    
    public int getDisplayId()
    {
      return mDisplayId;
    }
    
    public int getHeight()
    {
      return mHeight;
    }
    
    public int getLeft()
    {
      return mX;
    }
    
    public AssistStructure.ViewNode getRootViewNode()
    {
      return mRoot;
    }
    
    public CharSequence getTitle()
    {
      return mTitle;
    }
    
    public int getTop()
    {
      return mY;
    }
    
    public int getWidth()
    {
      return mWidth;
    }
    
    int resolveViewAutofillFlags(Context paramContext, int paramInt)
    {
      if (((paramInt & 0x1) == 0) && (!paramContext.isAutofillCompatibilityEnabled())) {
        paramInt = 0;
      } else {
        paramInt = 1;
      }
      return paramInt;
    }
    
    void writeSelfToParcel(Parcel paramParcel, PooledStringWriter paramPooledStringWriter, float[] paramArrayOfFloat)
    {
      paramParcel.writeInt(mX);
      paramParcel.writeInt(mY);
      paramParcel.writeInt(mWidth);
      paramParcel.writeInt(mHeight);
      TextUtils.writeToParcel(mTitle, paramParcel, 0);
      paramParcel.writeInt(mDisplayId);
    }
  }
}
