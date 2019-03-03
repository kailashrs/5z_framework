package android.print;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public final class PrintAttributes
  implements Parcelable
{
  public static final int COLOR_MODE_COLOR = 2;
  public static final int COLOR_MODE_MONOCHROME = 1;
  public static final Parcelable.Creator<PrintAttributes> CREATOR = new Parcelable.Creator()
  {
    public PrintAttributes createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PrintAttributes(paramAnonymousParcel, null);
    }
    
    public PrintAttributes[] newArray(int paramAnonymousInt)
    {
      return new PrintAttributes[paramAnonymousInt];
    }
  };
  public static final int DUPLEX_MODE_LONG_EDGE = 2;
  public static final int DUPLEX_MODE_NONE = 1;
  public static final int DUPLEX_MODE_SHORT_EDGE = 4;
  private static final int VALID_COLOR_MODES = 3;
  private static final int VALID_DUPLEX_MODES = 7;
  private int mColorMode;
  private int mDuplexMode;
  private MediaSize mMediaSize;
  private Margins mMinMargins;
  private Resolution mResolution;
  
  PrintAttributes() {}
  
  private PrintAttributes(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    Object localObject1 = null;
    if (i == 1) {
      localObject2 = MediaSize.createFromParcel(paramParcel);
    } else {
      localObject2 = null;
    }
    mMediaSize = ((MediaSize)localObject2);
    if (paramParcel.readInt() == 1) {
      localObject2 = Resolution.createFromParcel(paramParcel);
    } else {
      localObject2 = null;
    }
    mResolution = ((Resolution)localObject2);
    Object localObject2 = localObject1;
    if (paramParcel.readInt() == 1) {
      localObject2 = Margins.createFromParcel(paramParcel);
    }
    mMinMargins = ((Margins)localObject2);
    mColorMode = paramParcel.readInt();
    if (mColorMode != 0) {
      enforceValidColorMode(mColorMode);
    }
    mDuplexMode = paramParcel.readInt();
    if (mDuplexMode != 0) {
      enforceValidDuplexMode(mDuplexMode);
    }
  }
  
  static String colorModeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "COLOR_MODE_UNKNOWN";
    case 2: 
      return "COLOR_MODE_COLOR";
    }
    return "COLOR_MODE_MONOCHROME";
  }
  
  static String duplexModeToString(int paramInt)
  {
    if (paramInt != 4)
    {
      switch (paramInt)
      {
      default: 
        return "DUPLEX_MODE_UNKNOWN";
      case 2: 
        return "DUPLEX_MODE_LONG_EDGE";
      }
      return "DUPLEX_MODE_NONE";
    }
    return "DUPLEX_MODE_SHORT_EDGE";
  }
  
  static void enforceValidColorMode(int paramInt)
  {
    if (((paramInt & 0x3) != 0) && (Integer.bitCount(paramInt) == 1)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid color mode: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  static void enforceValidDuplexMode(int paramInt)
  {
    if (((paramInt & 0x7) != 0) && (Integer.bitCount(paramInt) == 1)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid duplex mode: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public PrintAttributes asLandscape()
  {
    if (!isPortrait()) {
      return this;
    }
    PrintAttributes localPrintAttributes = new PrintAttributes();
    localPrintAttributes.setMediaSize(getMediaSize().asLandscape());
    Resolution localResolution = getResolution();
    localPrintAttributes.setResolution(new Resolution(localResolution.getId(), localResolution.getLabel(), localResolution.getVerticalDpi(), localResolution.getHorizontalDpi()));
    localPrintAttributes.setMinMargins(getMinMargins());
    localPrintAttributes.setColorMode(getColorMode());
    localPrintAttributes.setDuplexMode(getDuplexMode());
    return localPrintAttributes;
  }
  
  public PrintAttributes asPortrait()
  {
    if (isPortrait()) {
      return this;
    }
    PrintAttributes localPrintAttributes = new PrintAttributes();
    localPrintAttributes.setMediaSize(getMediaSize().asPortrait());
    Resolution localResolution = getResolution();
    localPrintAttributes.setResolution(new Resolution(localResolution.getId(), localResolution.getLabel(), localResolution.getVerticalDpi(), localResolution.getHorizontalDpi()));
    localPrintAttributes.setMinMargins(getMinMargins());
    localPrintAttributes.setColorMode(getColorMode());
    localPrintAttributes.setDuplexMode(getDuplexMode());
    return localPrintAttributes;
  }
  
  public void clear()
  {
    mMediaSize = null;
    mResolution = null;
    mMinMargins = null;
    mColorMode = 0;
    mDuplexMode = 0;
  }
  
  public void copyFrom(PrintAttributes paramPrintAttributes)
  {
    mMediaSize = mMediaSize;
    mResolution = mResolution;
    mMinMargins = mMinMargins;
    mColorMode = mColorMode;
    mDuplexMode = mDuplexMode;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (PrintAttributes)paramObject;
    if (mColorMode != mColorMode) {
      return false;
    }
    if (mDuplexMode != mDuplexMode) {
      return false;
    }
    if (mMinMargins == null)
    {
      if (mMinMargins != null) {
        return false;
      }
    }
    else if (!mMinMargins.equals(mMinMargins)) {
      return false;
    }
    if (mMediaSize == null)
    {
      if (mMediaSize != null) {
        return false;
      }
    }
    else if (!mMediaSize.equals(mMediaSize)) {
      return false;
    }
    if (mResolution == null)
    {
      if (mResolution != null) {
        return false;
      }
    }
    else if (!mResolution.equals(mResolution)) {
      return false;
    }
    return true;
  }
  
  public int getColorMode()
  {
    return mColorMode;
  }
  
  public int getDuplexMode()
  {
    return mDuplexMode;
  }
  
  public MediaSize getMediaSize()
  {
    return mMediaSize;
  }
  
  public Margins getMinMargins()
  {
    return mMinMargins;
  }
  
  public Resolution getResolution()
  {
    return mResolution;
  }
  
  public int hashCode()
  {
    int i = mColorMode;
    int j = mDuplexMode;
    Margins localMargins = mMinMargins;
    int k = 0;
    int m;
    if (localMargins == null) {
      m = 0;
    } else {
      m = mMinMargins.hashCode();
    }
    int n;
    if (mMediaSize == null) {
      n = 0;
    } else {
      n = mMediaSize.hashCode();
    }
    if (mResolution != null) {
      k = mResolution.hashCode();
    }
    return 31 * (31 * (31 * (31 * (31 * 1 + i) + j) + m) + n) + k;
  }
  
  public boolean isPortrait()
  {
    return mMediaSize.isPortrait();
  }
  
  public void setColorMode(int paramInt)
  {
    enforceValidColorMode(paramInt);
    mColorMode = paramInt;
  }
  
  public void setDuplexMode(int paramInt)
  {
    enforceValidDuplexMode(paramInt);
    mDuplexMode = paramInt;
  }
  
  public void setMediaSize(MediaSize paramMediaSize)
  {
    mMediaSize = paramMediaSize;
  }
  
  public void setMinMargins(Margins paramMargins)
  {
    mMinMargins = paramMargins;
  }
  
  public void setResolution(Resolution paramResolution)
  {
    mResolution = paramResolution;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PrintAttributes{");
    localStringBuilder.append("mediaSize: ");
    localStringBuilder.append(mMediaSize);
    if (mMediaSize != null)
    {
      localStringBuilder.append(", orientation: ");
      String str;
      if (mMediaSize.isPortrait()) {
        str = "portrait";
      } else {
        str = "landscape";
      }
      localStringBuilder.append(str);
    }
    else
    {
      localStringBuilder.append(", orientation: ");
      localStringBuilder.append("null");
    }
    localStringBuilder.append(", resolution: ");
    localStringBuilder.append(mResolution);
    localStringBuilder.append(", minMargins: ");
    localStringBuilder.append(mMinMargins);
    localStringBuilder.append(", colorMode: ");
    localStringBuilder.append(colorModeToString(mColorMode));
    localStringBuilder.append(", duplexMode: ");
    localStringBuilder.append(duplexModeToString(mDuplexMode));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mMediaSize != null)
    {
      paramParcel.writeInt(1);
      mMediaSize.writeToParcel(paramParcel);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mResolution != null)
    {
      paramParcel.writeInt(1);
      mResolution.writeToParcel(paramParcel);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mMinMargins != null)
    {
      paramParcel.writeInt(1);
      mMinMargins.writeToParcel(paramParcel);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mColorMode);
    paramParcel.writeInt(mDuplexMode);
  }
  
  public static final class Builder
  {
    private final PrintAttributes mAttributes = new PrintAttributes();
    
    public Builder() {}
    
    public PrintAttributes build()
    {
      return mAttributes;
    }
    
    public Builder setColorMode(int paramInt)
    {
      mAttributes.setColorMode(paramInt);
      return this;
    }
    
    public Builder setDuplexMode(int paramInt)
    {
      mAttributes.setDuplexMode(paramInt);
      return this;
    }
    
    public Builder setMediaSize(PrintAttributes.MediaSize paramMediaSize)
    {
      mAttributes.setMediaSize(paramMediaSize);
      return this;
    }
    
    public Builder setMinMargins(PrintAttributes.Margins paramMargins)
    {
      mAttributes.setMinMargins(paramMargins);
      return this;
    }
    
    public Builder setResolution(PrintAttributes.Resolution paramResolution)
    {
      mAttributes.setResolution(paramResolution);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface ColorMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface DuplexMode {}
  
  public static final class Margins
  {
    public static final Margins NO_MARGINS = new Margins(0, 0, 0, 0);
    private final int mBottomMils;
    private final int mLeftMils;
    private final int mRightMils;
    private final int mTopMils;
    
    public Margins(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      mTopMils = paramInt2;
      mLeftMils = paramInt1;
      mRightMils = paramInt3;
      mBottomMils = paramInt4;
    }
    
    static Margins createFromParcel(Parcel paramParcel)
    {
      return new Margins(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (Margins)paramObject;
      if (mBottomMils != mBottomMils) {
        return false;
      }
      if (mLeftMils != mLeftMils) {
        return false;
      }
      if (mRightMils != mRightMils) {
        return false;
      }
      return mTopMils == mTopMils;
    }
    
    public int getBottomMils()
    {
      return mBottomMils;
    }
    
    public int getLeftMils()
    {
      return mLeftMils;
    }
    
    public int getRightMils()
    {
      return mRightMils;
    }
    
    public int getTopMils()
    {
      return mTopMils;
    }
    
    public int hashCode()
    {
      return 31 * (31 * (31 * (31 * 1 + mBottomMils) + mLeftMils) + mRightMils) + mTopMils;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Margins{");
      localStringBuilder.append("leftMils: ");
      localStringBuilder.append(mLeftMils);
      localStringBuilder.append(", topMils: ");
      localStringBuilder.append(mTopMils);
      localStringBuilder.append(", rightMils: ");
      localStringBuilder.append(mRightMils);
      localStringBuilder.append(", bottomMils: ");
      localStringBuilder.append(mBottomMils);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeInt(mLeftMils);
      paramParcel.writeInt(mTopMils);
      paramParcel.writeInt(mRightMils);
      paramParcel.writeInt(mBottomMils);
    }
  }
  
  public static final class MediaSize
  {
    public static final MediaSize ISO_A0;
    public static final MediaSize ISO_A1;
    public static final MediaSize ISO_A10;
    public static final MediaSize ISO_A2;
    public static final MediaSize ISO_A3;
    public static final MediaSize ISO_A4;
    public static final MediaSize ISO_A5;
    public static final MediaSize ISO_A6;
    public static final MediaSize ISO_A7;
    public static final MediaSize ISO_A8;
    public static final MediaSize ISO_A9;
    public static final MediaSize ISO_B0;
    public static final MediaSize ISO_B1;
    public static final MediaSize ISO_B10;
    public static final MediaSize ISO_B2;
    public static final MediaSize ISO_B3;
    public static final MediaSize ISO_B4;
    public static final MediaSize ISO_B5;
    public static final MediaSize ISO_B6;
    public static final MediaSize ISO_B7;
    public static final MediaSize ISO_B8;
    public static final MediaSize ISO_B9;
    public static final MediaSize ISO_C0;
    public static final MediaSize ISO_C1;
    public static final MediaSize ISO_C10;
    public static final MediaSize ISO_C2;
    public static final MediaSize ISO_C3;
    public static final MediaSize ISO_C4;
    public static final MediaSize ISO_C5;
    public static final MediaSize ISO_C6;
    public static final MediaSize ISO_C7;
    public static final MediaSize ISO_C8;
    public static final MediaSize ISO_C9;
    public static final MediaSize JIS_B0;
    public static final MediaSize JIS_B1;
    public static final MediaSize JIS_B10;
    public static final MediaSize JIS_B2;
    public static final MediaSize JIS_B3;
    public static final MediaSize JIS_B4;
    public static final MediaSize JIS_B5;
    public static final MediaSize JIS_B6;
    public static final MediaSize JIS_B7;
    public static final MediaSize JIS_B8;
    public static final MediaSize JIS_B9;
    public static final MediaSize JIS_EXEC;
    public static final MediaSize JPN_CHOU2;
    public static final MediaSize JPN_CHOU3;
    public static final MediaSize JPN_CHOU4;
    public static final MediaSize JPN_HAGAKI;
    public static final MediaSize JPN_KAHU = new MediaSize("JPN_KAHU", "android", 17040406, 9449, 12681);
    public static final MediaSize JPN_KAKU2 = new MediaSize("JPN_KAKU2", "android", 17040407, 9449, 13071);
    public static final MediaSize JPN_OUFUKU;
    public static final MediaSize JPN_YOU4 = new MediaSize("JPN_YOU4", "android", 17040409, 4134, 9252);
    private static final String LOG_TAG = "MediaSize";
    public static final MediaSize NA_FOOLSCAP;
    public static final MediaSize NA_GOVT_LETTER;
    public static final MediaSize NA_INDEX_3X5;
    public static final MediaSize NA_INDEX_4X6;
    public static final MediaSize NA_INDEX_5X8;
    public static final MediaSize NA_JUNIOR_LEGAL;
    public static final MediaSize NA_LEDGER;
    public static final MediaSize NA_LEGAL;
    public static final MediaSize NA_LETTER;
    public static final MediaSize NA_MONARCH;
    public static final MediaSize NA_QUARTO;
    public static final MediaSize NA_TABLOID;
    public static final MediaSize OM_DAI_PA_KAI;
    public static final MediaSize OM_JUURO_KU_KAI;
    public static final MediaSize OM_PA_KAI;
    public static final MediaSize PRC_1;
    public static final MediaSize PRC_10;
    public static final MediaSize PRC_16K;
    public static final MediaSize PRC_2;
    public static final MediaSize PRC_3;
    public static final MediaSize PRC_4;
    public static final MediaSize PRC_5;
    public static final MediaSize PRC_6;
    public static final MediaSize PRC_7;
    public static final MediaSize PRC_8;
    public static final MediaSize PRC_9;
    public static final MediaSize ROC_16K;
    public static final MediaSize ROC_8K;
    public static final MediaSize UNKNOWN_LANDSCAPE;
    public static final MediaSize UNKNOWN_PORTRAIT;
    private static final Map<String, MediaSize> sIdToMediaSizeMap = new ArrayMap();
    private final int mHeightMils;
    private final String mId;
    public final String mLabel;
    public final int mLabelResId;
    public final String mPackageName;
    private final int mWidthMils;
    
    static
    {
      UNKNOWN_PORTRAIT = new MediaSize("UNKNOWN_PORTRAIT", "android", 17040423, 1, Integer.MAX_VALUE);
      UNKNOWN_LANDSCAPE = new MediaSize("UNKNOWN_LANDSCAPE", "android", 17040422, Integer.MAX_VALUE, 1);
      ISO_A0 = new MediaSize("ISO_A0", "android", 17040357, 33110, 46810);
      ISO_A1 = new MediaSize("ISO_A1", "android", 17040358, 23390, 33110);
      ISO_A2 = new MediaSize("ISO_A2", "android", 17040360, 16540, 23390);
      ISO_A3 = new MediaSize("ISO_A3", "android", 17040361, 11690, 16540);
      ISO_A4 = new MediaSize("ISO_A4", "android", 17040362, 8270, 11690);
      ISO_A5 = new MediaSize("ISO_A5", "android", 17040363, 5830, 8270);
      ISO_A6 = new MediaSize("ISO_A6", "android", 17040364, 4130, 5830);
      ISO_A7 = new MediaSize("ISO_A7", "android", 17040365, 2910, 4130);
      ISO_A8 = new MediaSize("ISO_A8", "android", 17040366, 2050, 2910);
      ISO_A9 = new MediaSize("ISO_A9", "android", 17040367, 1460, 2050);
      ISO_A10 = new MediaSize("ISO_A10", "android", 17040359, 1020, 1460);
      ISO_B0 = new MediaSize("ISO_B0", "android", 17040368, 39370, 55670);
      ISO_B1 = new MediaSize("ISO_B1", "android", 17040369, 27830, 39370);
      ISO_B2 = new MediaSize("ISO_B2", "android", 17040371, 19690, 27830);
      ISO_B3 = new MediaSize("ISO_B3", "android", 17040372, 13900, 19690);
      ISO_B4 = new MediaSize("ISO_B4", "android", 17040373, 9840, 13900);
      ISO_B5 = new MediaSize("ISO_B5", "android", 17040374, 6930, 9840);
      ISO_B6 = new MediaSize("ISO_B6", "android", 17040375, 4920, 6930);
      ISO_B7 = new MediaSize("ISO_B7", "android", 17040376, 3460, 4920);
      ISO_B8 = new MediaSize("ISO_B8", "android", 17040377, 2440, 3460);
      ISO_B9 = new MediaSize("ISO_B9", "android", 17040378, 1730, 2440);
      ISO_B10 = new MediaSize("ISO_B10", "android", 17040370, 1220, 1730);
      ISO_C0 = new MediaSize("ISO_C0", "android", 17040379, 36100, 51060);
      ISO_C1 = new MediaSize("ISO_C1", "android", 17040380, 25510, 36100);
      ISO_C2 = new MediaSize("ISO_C2", "android", 17040382, 18030, 25510);
      ISO_C3 = new MediaSize("ISO_C3", "android", 17040383, 12760, 18030);
      ISO_C4 = new MediaSize("ISO_C4", "android", 17040384, 9020, 12760);
      ISO_C5 = new MediaSize("ISO_C5", "android", 17040385, 6380, 9020);
      ISO_C6 = new MediaSize("ISO_C6", "android", 17040386, 4490, 6380);
      ISO_C7 = new MediaSize("ISO_C7", "android", 17040387, 3190, 4490);
      ISO_C8 = new MediaSize("ISO_C8", "android", 17040388, 2240, 3190);
      ISO_C9 = new MediaSize("ISO_C9", "android", 17040389, 1570, 2240);
      ISO_C10 = new MediaSize("ISO_C10", "android", 17040381, 1100, 1570);
      NA_LETTER = new MediaSize("NA_LETTER", "android", 17040418, 8500, 11000);
      NA_GOVT_LETTER = new MediaSize("NA_GOVT_LETTER", "android", 17040411, 8000, 10500);
      NA_LEGAL = new MediaSize("NA_LEGAL", "android", 17040417, 8500, 14000);
      NA_JUNIOR_LEGAL = new MediaSize("NA_JUNIOR_LEGAL", "android", 17040415, 8000, 5000);
      NA_LEDGER = new MediaSize("NA_LEDGER", "android", 17040416, 17000, 11000);
      NA_TABLOID = new MediaSize("NA_TABLOID", "android", 17040421, 11000, 17000);
      NA_INDEX_3X5 = new MediaSize("NA_INDEX_3X5", "android", 17040412, 3000, 5000);
      NA_INDEX_4X6 = new MediaSize("NA_INDEX_4X6", "android", 17040413, 4000, 6000);
      NA_INDEX_5X8 = new MediaSize("NA_INDEX_5X8", "android", 17040414, 5000, 8000);
      NA_MONARCH = new MediaSize("NA_MONARCH", "android", 17040419, 7250, 10500);
      NA_QUARTO = new MediaSize("NA_QUARTO", "android", 17040420, 8000, 10000);
      NA_FOOLSCAP = new MediaSize("NA_FOOLSCAP", "android", 17040410, 8000, 13000);
      ROC_8K = new MediaSize("ROC_8K", "android", 17040356, 10629, 15354);
      ROC_16K = new MediaSize("ROC_16K", "android", 17040355, 7677, 10629);
      PRC_1 = new MediaSize("PRC_1", "android", 17040344, 4015, 6496);
      PRC_2 = new MediaSize("PRC_2", "android", 17040347, 4015, 6929);
      PRC_3 = new MediaSize("PRC_3", "android", 17040348, 4921, 6929);
      PRC_4 = new MediaSize("PRC_4", "android", 17040349, 4330, 8189);
      PRC_5 = new MediaSize("PRC_5", "android", 17040350, 4330, 8661);
      PRC_6 = new MediaSize("PRC_6", "android", 17040351, 4724, 12599);
      PRC_7 = new MediaSize("PRC_7", "android", 17040352, 6299, 9055);
      PRC_8 = new MediaSize("PRC_8", "android", 17040353, 4724, 12165);
      PRC_9 = new MediaSize("PRC_9", "android", 17040354, 9016, 12756);
      PRC_10 = new MediaSize("PRC_10", "android", 17040345, 12756, 18032);
      PRC_16K = new MediaSize("PRC_16K", "android", 17040346, 5749, 8465);
      OM_PA_KAI = new MediaSize("OM_PA_KAI", "android", 17040343, 10512, 15315);
      OM_DAI_PA_KAI = new MediaSize("OM_DAI_PA_KAI", "android", 17040341, 10827, 15551);
      OM_JUURO_KU_KAI = new MediaSize("OM_JUURO_KU_KAI", "android", 17040342, 7796, 10827);
      JIS_B10 = new MediaSize("JIS_B10", "android", 17040396, 1259, 1772);
      JIS_B9 = new MediaSize("JIS_B9", "android", 17040404, 1772, 2520);
      JIS_B8 = new MediaSize("JIS_B8", "android", 17040403, 2520, 3583);
      JIS_B7 = new MediaSize("JIS_B7", "android", 17040402, 3583, 5049);
      JIS_B6 = new MediaSize("JIS_B6", "android", 17040401, 5049, 7165);
      JIS_B5 = new MediaSize("JIS_B5", "android", 17040400, 7165, 10118);
      JIS_B4 = new MediaSize("JIS_B4", "android", 17040399, 10118, 14331);
      JIS_B3 = new MediaSize("JIS_B3", "android", 17040398, 14331, 20276);
      JIS_B2 = new MediaSize("JIS_B2", "android", 17040397, 20276, 28661);
      JIS_B1 = new MediaSize("JIS_B1", "android", 17040395, 28661, 40551);
      JIS_B0 = new MediaSize("JIS_B0", "android", 17040394, 40551, 57323);
      JIS_EXEC = new MediaSize("JIS_EXEC", "android", 17040405, 8504, 12992);
      JPN_CHOU4 = new MediaSize("JPN_CHOU4", "android", 17040392, 3543, 8071);
      JPN_CHOU3 = new MediaSize("JPN_CHOU3", "android", 17040391, 4724, 9252);
      JPN_CHOU2 = new MediaSize("JPN_CHOU2", "android", 17040390, 4374, 5748);
      JPN_HAGAKI = new MediaSize("JPN_HAGAKI", "android", 17040393, 3937, 5827);
      JPN_OUFUKU = new MediaSize("JPN_OUFUKU", "android", 17040408, 5827, 7874);
    }
    
    public MediaSize(String paramString1, String paramString2, int paramInt1, int paramInt2)
    {
      this(paramString1, paramString2, null, paramInt1, paramInt2, 0);
    }
    
    public MediaSize(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
    {
      this(paramString1, null, paramString2, paramInt2, paramInt3, paramInt1);
      sIdToMediaSizeMap.put(mId, this);
    }
    
    public MediaSize(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3)
    {
      mPackageName = paramString3;
      mId = ((String)Preconditions.checkStringNotEmpty(paramString1, "id cannot be empty."));
      mLabelResId = paramInt3;
      mWidthMils = Preconditions.checkArgumentPositive(paramInt1, "widthMils cannot be less than or equal to zero.");
      mHeightMils = Preconditions.checkArgumentPositive(paramInt2, "heightMils cannot be less than or equal to zero.");
      mLabel = paramString2;
      boolean bool1 = TextUtils.isEmpty(paramString2);
      boolean bool2 = true;
      if ((!TextUtils.isEmpty(paramString3)) && (paramInt3 != 0)) {
        paramInt1 = 1;
      } else {
        paramInt1 = 0;
      }
      if ((bool1 ^ true) == paramInt1) {
        bool2 = false;
      }
      Preconditions.checkArgument(bool2, "label cannot be empty.");
    }
    
    static MediaSize createFromParcel(Parcel paramParcel)
    {
      return new MediaSize(paramParcel.readString(), paramParcel.readString(), paramParcel.readString(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
    }
    
    public static ArraySet<MediaSize> getAllPredefinedSizes()
    {
      ArraySet localArraySet = new ArraySet(sIdToMediaSizeMap.values());
      localArraySet.remove(UNKNOWN_PORTRAIT);
      localArraySet.remove(UNKNOWN_LANDSCAPE);
      return localArraySet;
    }
    
    public static MediaSize getStandardMediaSizeById(String paramString)
    {
      return (MediaSize)sIdToMediaSizeMap.get(paramString);
    }
    
    public MediaSize asLandscape()
    {
      if (!isPortrait()) {
        return this;
      }
      return new MediaSize(mId, mLabel, mPackageName, Math.max(mWidthMils, mHeightMils), Math.min(mWidthMils, mHeightMils), mLabelResId);
    }
    
    public MediaSize asPortrait()
    {
      if (isPortrait()) {
        return this;
      }
      return new MediaSize(mId, mLabel, mPackageName, Math.min(mWidthMils, mHeightMils), Math.max(mWidthMils, mHeightMils), mLabelResId);
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (MediaSize)paramObject;
      if (mWidthMils != mWidthMils) {
        return false;
      }
      return mHeightMils == mHeightMils;
    }
    
    public int getHeightMils()
    {
      return mHeightMils;
    }
    
    public String getId()
    {
      return mId;
    }
    
    public String getLabel(PackageManager paramPackageManager)
    {
      if ((!TextUtils.isEmpty(mPackageName)) && (mLabelResId > 0)) {
        try
        {
          paramPackageManager = paramPackageManager.getResourcesForApplication(mPackageName).getString(mLabelResId);
          return paramPackageManager;
        }
        catch (Resources.NotFoundException|PackageManager.NameNotFoundException paramPackageManager)
        {
          paramPackageManager = new StringBuilder();
          paramPackageManager.append("Could not load resouce");
          paramPackageManager.append(mLabelResId);
          paramPackageManager.append(" from package ");
          paramPackageManager.append(mPackageName);
          Log.w("MediaSize", paramPackageManager.toString());
        }
      }
      return mLabel;
    }
    
    public int getWidthMils()
    {
      return mWidthMils;
    }
    
    public int hashCode()
    {
      return 31 * (31 * 1 + mWidthMils) + mHeightMils;
    }
    
    public boolean isPortrait()
    {
      boolean bool;
      if (mHeightMils >= mWidthMils) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("MediaSize{");
      localStringBuilder.append("id: ");
      localStringBuilder.append(mId);
      localStringBuilder.append(", label: ");
      localStringBuilder.append(mLabel);
      localStringBuilder.append(", packageName: ");
      localStringBuilder.append(mPackageName);
      localStringBuilder.append(", heightMils: ");
      localStringBuilder.append(mHeightMils);
      localStringBuilder.append(", widthMils: ");
      localStringBuilder.append(mWidthMils);
      localStringBuilder.append(", labelResId: ");
      localStringBuilder.append(mLabelResId);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeString(mId);
      paramParcel.writeString(mLabel);
      paramParcel.writeString(mPackageName);
      paramParcel.writeInt(mWidthMils);
      paramParcel.writeInt(mHeightMils);
      paramParcel.writeInt(mLabelResId);
    }
  }
  
  public static final class Resolution
  {
    private final int mHorizontalDpi;
    private final String mId;
    private final String mLabel;
    private final int mVerticalDpi;
    
    public Resolution(String paramString1, String paramString2, int paramInt1, int paramInt2)
    {
      if (!TextUtils.isEmpty(paramString1))
      {
        if (!TextUtils.isEmpty(paramString2))
        {
          if (paramInt1 > 0)
          {
            if (paramInt2 > 0)
            {
              mId = paramString1;
              mLabel = paramString2;
              mHorizontalDpi = paramInt1;
              mVerticalDpi = paramInt2;
              return;
            }
            throw new IllegalArgumentException("verticalDpi cannot be less than or equal to zero.");
          }
          throw new IllegalArgumentException("horizontalDpi cannot be less than or equal to zero.");
        }
        throw new IllegalArgumentException("label cannot be empty.");
      }
      throw new IllegalArgumentException("id cannot be empty.");
    }
    
    static Resolution createFromParcel(Parcel paramParcel)
    {
      return new Resolution(paramParcel.readString(), paramParcel.readString(), paramParcel.readInt(), paramParcel.readInt());
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (Resolution)paramObject;
      if (mHorizontalDpi != mHorizontalDpi) {
        return false;
      }
      return mVerticalDpi == mVerticalDpi;
    }
    
    public int getHorizontalDpi()
    {
      return mHorizontalDpi;
    }
    
    public String getId()
    {
      return mId;
    }
    
    public String getLabel()
    {
      return mLabel;
    }
    
    public int getVerticalDpi()
    {
      return mVerticalDpi;
    }
    
    public int hashCode()
    {
      return 31 * (31 * 1 + mHorizontalDpi) + mVerticalDpi;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Resolution{");
      localStringBuilder.append("id: ");
      localStringBuilder.append(mId);
      localStringBuilder.append(", label: ");
      localStringBuilder.append(mLabel);
      localStringBuilder.append(", horizontalDpi: ");
      localStringBuilder.append(mHorizontalDpi);
      localStringBuilder.append(", verticalDpi: ");
      localStringBuilder.append(mVerticalDpi);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeString(mId);
      paramParcel.writeString(mLabel);
      paramParcel.writeInt(mHorizontalDpi);
      paramParcel.writeInt(mVerticalDpi);
    }
  }
}
