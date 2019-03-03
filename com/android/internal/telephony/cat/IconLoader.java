package com.android.internal.telephony.cat;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.HashMap;

class IconLoader
  extends Handler
{
  private static final int CLUT_ENTRY_SIZE = 3;
  private static final int CLUT_LOCATION_OFFSET = 4;
  private static final int EVENT_READ_CLUT_DONE = 3;
  private static final int EVENT_READ_EF_IMG_RECOED_DONE = 1;
  private static final int EVENT_READ_ICON_DONE = 2;
  private static final int STATE_MULTI_ICONS = 2;
  private static final int STATE_SINGLE_ICON = 1;
  private static IconLoader sLoader = null;
  private static HandlerThread sThread = null;
  private Bitmap mCurrentIcon = null;
  private int mCurrentRecordIndex = 0;
  private Message mEndMsg = null;
  private byte[] mIconData = null;
  private Bitmap[] mIcons = null;
  private HashMap<Integer, Bitmap> mIconsCache = null;
  private ImageDescriptor mId = null;
  private int mRecordNumber;
  private int[] mRecordNumbers = null;
  private IccFileHandler mSimFH = null;
  private int mState = 1;
  
  private IconLoader(Looper paramLooper, IccFileHandler paramIccFileHandler)
  {
    super(paramLooper);
    mSimFH = paramIccFileHandler;
    mIconsCache = new HashMap(50);
  }
  
  private static int bitToBnW(int paramInt)
  {
    if (paramInt == 1) {
      return -1;
    }
    return -16777216;
  }
  
  static IconLoader getInstance(Handler paramHandler, IccFileHandler paramIccFileHandler)
  {
    if (sLoader != null) {
      return sLoader;
    }
    if (paramIccFileHandler != null)
    {
      sThread = new HandlerThread("Cat Icon Loader");
      sThread.start();
      return new IconLoader(sThread.getLooper(), paramIccFileHandler);
    }
    return null;
  }
  
  private static int getMask(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    default: 
      paramInt = i;
      break;
    case 8: 
      paramInt = 255;
      break;
    case 7: 
      paramInt = 127;
      break;
    case 6: 
      paramInt = 63;
      break;
    case 5: 
      paramInt = 31;
      break;
    case 4: 
      paramInt = 15;
      break;
    case 3: 
      paramInt = 7;
      break;
    case 2: 
      paramInt = 3;
      break;
    case 1: 
      paramInt = 1;
    }
    return paramInt;
  }
  
  private boolean handleImageDescriptor(byte[] paramArrayOfByte)
  {
    mId = ImageDescriptor.parse(paramArrayOfByte, 1);
    return mId != null;
  }
  
  public static Bitmap parseToBnW(byte[] paramArrayOfByte, int paramInt)
  {
    paramInt = 0 + 1;
    int i = paramArrayOfByte[0] & 0xFF;
    int j = paramInt + 1;
    int k = paramArrayOfByte[paramInt] & 0xFF;
    int m = i * k;
    int[] arrayOfInt = new int[m];
    int n = 0;
    paramInt = 7;
    int i1 = 0;
    while (n < m)
    {
      int i2 = j;
      if (n % 8 == 0)
      {
        i1 = paramArrayOfByte[j];
        paramInt = 7;
        i2 = j + 1;
      }
      arrayOfInt[n] = bitToBnW(i1 >> paramInt & 0x1);
      n++;
      paramInt--;
      j = i2;
    }
    if (n != m) {
      CatLog.d("IconLoader", "parseToBnW; size error");
    }
    return Bitmap.createBitmap(arrayOfInt, i, k, Bitmap.Config.ARGB_8888);
  }
  
  public static Bitmap parseToRGB(byte[] paramArrayOfByte1, int paramInt, boolean paramBoolean, byte[] paramArrayOfByte2)
  {
    paramInt = 0 + 1;
    int i = paramArrayOfByte1[0] & 0xFF;
    int j = paramInt + 1;
    int k = paramArrayOfByte1[paramInt] & 0xFF;
    paramInt = j + 1;
    int m = paramArrayOfByte1[j] & 0xFF;
    int n = paramArrayOfByte1[paramInt] & 0xFF;
    int i1 = 0;
    if (true == paramBoolean) {
      paramArrayOfByte2[(n - 1)] = ((byte)0);
    }
    int i2 = i * k;
    int[] arrayOfInt = new int[i2];
    int i3 = 0;
    int i4 = 8 - m;
    int i5 = i4;
    int i6 = 6 + 1;
    int i7 = paramArrayOfByte1[6];
    int i8 = getMask(m);
    int i9 = n;
    j = i7;
    int i10 = i3;
    paramInt = i5;
    int i11 = i6;
    if (8 % m == 0)
    {
      i1 = 1;
      i11 = i6;
      paramInt = i5;
      i10 = i3;
      j = i7;
      i9 = n;
    }
    while (i10 < i2)
    {
      i3 = paramInt;
      i5 = i11;
      if (paramInt < 0)
      {
        j = paramArrayOfByte1[i11];
        if (i1 != 0) {
          paramInt = i4;
        } else {
          paramInt *= -1;
        }
        i5 = i11 + 1;
        i3 = paramInt;
      }
      paramInt = (j >> i3 & i8) * 3;
      arrayOfInt[i10] = Color.rgb(paramArrayOfByte2[paramInt], paramArrayOfByte2[(paramInt + 1)], paramArrayOfByte2[(paramInt + 2)]);
      paramInt = i3 - m;
      i10++;
      i11 = i5;
    }
    return Bitmap.createBitmap(arrayOfInt, i, k, Bitmap.Config.ARGB_8888);
  }
  
  private void postIcon()
  {
    if (mState == 1)
    {
      mEndMsg.obj = mCurrentIcon;
      mEndMsg.sendToTarget();
    }
    else if (mState == 2)
    {
      Bitmap[] arrayOfBitmap = mIcons;
      int i = mCurrentRecordIndex;
      mCurrentRecordIndex = (i + 1);
      arrayOfBitmap[i] = mCurrentIcon;
      if (mCurrentRecordIndex < mRecordNumbers.length)
      {
        startLoadingIcon(mRecordNumbers[mCurrentRecordIndex]);
      }
      else
      {
        mEndMsg.obj = mIcons;
        mEndMsg.sendToTarget();
      }
    }
  }
  
  private void readClut()
  {
    int i = mIconData[3];
    Message localMessage = obtainMessage(3);
    mSimFH.loadEFImgTransparent(mId.mImageId, mIconData[4], mIconData[5], i * 3, localMessage);
  }
  
  private void readIconData()
  {
    Message localMessage = obtainMessage(2);
    mSimFH.loadEFImgTransparent(mId.mImageId, 0, 0, mId.mLength, localMessage);
  }
  
  private void readId()
  {
    if (mRecordNumber < 0)
    {
      mCurrentIcon = null;
      postIcon();
      return;
    }
    Message localMessage = obtainMessage(1);
    mSimFH.loadEFImgLinearFixed(mRecordNumber, localMessage);
  }
  
  private void startLoadingIcon(int paramInt)
  {
    mId = null;
    mIconData = null;
    mCurrentIcon = null;
    mRecordNumber = paramInt;
    if (mIconsCache.containsKey(Integer.valueOf(paramInt)))
    {
      mCurrentIcon = ((Bitmap)mIconsCache.get(Integer.valueOf(paramInt)));
      postIcon();
      return;
    }
    readId();
  }
  
  public void dispose()
  {
    mSimFH = null;
    if (sThread != null)
    {
      sThread.quit();
      sThread = null;
    }
    mIconsCache = null;
    sLoader = null;
  }
  
  public void handleMessage(Message paramMessage)
  {
    try
    {
      switch (what)
      {
      default: 
        break;
      case 3: 
        paramMessage = (byte[])obj).result;
        mCurrentIcon = parseToRGB(mIconData, mIconData.length, false, paramMessage);
        mIconsCache.put(Integer.valueOf(mRecordNumber), mCurrentIcon);
        postIcon();
        break;
      case 2: 
        CatLog.d(this, "load icon done");
        paramMessage = (byte[])obj).result;
        if (mId.mCodingScheme == 17)
        {
          mCurrentIcon = parseToBnW(paramMessage, paramMessage.length);
          mIconsCache.put(Integer.valueOf(mRecordNumber), mCurrentIcon);
          postIcon();
        }
        else if (mId.mCodingScheme == 33)
        {
          mIconData = paramMessage;
          readClut();
        }
        else
        {
          CatLog.d(this, "else  /postIcon ");
          postIcon();
        }
        break;
      case 1: 
        if (handleImageDescriptor((byte[])obj).result))
        {
          readIconData();
        }
        else
        {
          paramMessage = new java/lang/Exception;
          paramMessage.<init>("Unable to parse image descriptor");
          throw paramMessage;
        }
        break;
      }
    }
    catch (Exception paramMessage)
    {
      CatLog.d(this, "Icon load failed");
      postIcon();
    }
  }
  
  void loadIcon(int paramInt, Message paramMessage)
  {
    if (paramMessage == null) {
      return;
    }
    mEndMsg = paramMessage;
    mState = 1;
    startLoadingIcon(paramInt);
  }
  
  void loadIcons(int[] paramArrayOfInt, Message paramMessage)
  {
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length != 0) && (paramMessage != null))
    {
      mEndMsg = paramMessage;
      mIcons = new Bitmap[paramArrayOfInt.length];
      mRecordNumbers = paramArrayOfInt;
      mCurrentRecordIndex = 0;
      mState = 2;
      startLoadingIcon(paramArrayOfInt[0]);
      return;
    }
  }
}
