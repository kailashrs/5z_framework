package android.print;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.IntConsumer;

public final class PrinterCapabilitiesInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PrinterCapabilitiesInfo> CREATOR = new Parcelable.Creator()
  {
    public PrinterCapabilitiesInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PrinterCapabilitiesInfo(paramAnonymousParcel, null);
    }
    
    public PrinterCapabilitiesInfo[] newArray(int paramAnonymousInt)
    {
      return new PrinterCapabilitiesInfo[paramAnonymousInt];
    }
  };
  private static final PrintAttributes.Margins DEFAULT_MARGINS = new PrintAttributes.Margins(0, 0, 0, 0);
  public static final int DEFAULT_UNDEFINED = -1;
  private static final int PROPERTY_COLOR_MODE = 2;
  private static final int PROPERTY_COUNT = 4;
  private static final int PROPERTY_DUPLEX_MODE = 3;
  private static final int PROPERTY_MEDIA_SIZE = 0;
  private static final int PROPERTY_RESOLUTION = 1;
  private int mColorModes;
  private final int[] mDefaults = new int[4];
  private int mDuplexModes;
  private List<PrintAttributes.MediaSize> mMediaSizes;
  private PrintAttributes.Margins mMinMargins = DEFAULT_MARGINS;
  private List<PrintAttributes.Resolution> mResolutions;
  
  public PrinterCapabilitiesInfo()
  {
    Arrays.fill(mDefaults, -1);
  }
  
  private PrinterCapabilitiesInfo(Parcel paramParcel)
  {
    mMinMargins = ((PrintAttributes.Margins)Preconditions.checkNotNull(readMargins(paramParcel)));
    readMediaSizes(paramParcel);
    readResolutions(paramParcel);
    mColorModes = paramParcel.readInt();
    enforceValidMask(mColorModes, _..Lambda.PrinterCapabilitiesInfo.2mJhwjGC7Dgi0vwDsnG83V2s6sE.INSTANCE);
    mDuplexModes = paramParcel.readInt();
    enforceValidMask(mDuplexModes, _..Lambda.PrinterCapabilitiesInfo.TL1SYHyXTbqj2Nseol9bDJQOn3U.INSTANCE);
    readDefaults(paramParcel);
    int i = mMediaSizes.size();
    paramParcel = mDefaults;
    boolean bool1 = false;
    if (i > paramParcel[0]) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Preconditions.checkArgument(bool2);
    boolean bool2 = bool1;
    if (mResolutions.size() > mDefaults[1]) {
      bool2 = true;
    }
    Preconditions.checkArgument(bool2);
  }
  
  public PrinterCapabilitiesInfo(PrinterCapabilitiesInfo paramPrinterCapabilitiesInfo)
  {
    copyFrom(paramPrinterCapabilitiesInfo);
  }
  
  private String colorModesToString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[');
    int i = mColorModes;
    while (i != 0)
    {
      int j = 1 << Integer.numberOfTrailingZeros(i);
      i &= j;
      if (localStringBuilder.length() > 1) {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append(PrintAttributes.colorModeToString(j));
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  private String duplexModesToString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[');
    int i = mDuplexModes;
    while (i != 0)
    {
      int j = 1 << Integer.numberOfTrailingZeros(i);
      i &= j;
      if (localStringBuilder.length() > 1) {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append(PrintAttributes.duplexModeToString(j));
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  private static void enforceValidMask(int paramInt, IntConsumer paramIntConsumer)
  {
    while (paramInt > 0)
    {
      int i = 1 << Integer.numberOfTrailingZeros(paramInt);
      paramInt &= i;
      paramIntConsumer.accept(i);
    }
  }
  
  private void readDefaults(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++) {
      mDefaults[j] = paramParcel.readInt();
    }
  }
  
  private PrintAttributes.Margins readMargins(Parcel paramParcel)
  {
    if (paramParcel.readInt() == 1) {
      paramParcel = PrintAttributes.Margins.createFromParcel(paramParcel);
    } else {
      paramParcel = null;
    }
    return paramParcel;
  }
  
  private void readMediaSizes(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    if ((i > 0) && (mMediaSizes == null)) {
      mMediaSizes = new ArrayList();
    }
    for (int j = 0; j < i; j++) {
      mMediaSizes.add(PrintAttributes.MediaSize.createFromParcel(paramParcel));
    }
  }
  
  private void readResolutions(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    if ((i > 0) && (mResolutions == null)) {
      mResolutions = new ArrayList();
    }
    for (int j = 0; j < i; j++) {
      mResolutions.add(PrintAttributes.Resolution.createFromParcel(paramParcel));
    }
  }
  
  private void writeDefaults(Parcel paramParcel)
  {
    int i = mDefaults.length;
    paramParcel.writeInt(i);
    for (int j = 0; j < i; j++) {
      paramParcel.writeInt(mDefaults[j]);
    }
  }
  
  private void writeMargins(PrintAttributes.Margins paramMargins, Parcel paramParcel)
  {
    if (paramMargins == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      paramMargins.writeToParcel(paramParcel);
    }
  }
  
  private void writeMediaSizes(Parcel paramParcel)
  {
    List localList = mMediaSizes;
    int i = 0;
    if (localList == null)
    {
      paramParcel.writeInt(0);
      return;
    }
    int j = mMediaSizes.size();
    paramParcel.writeInt(j);
    while (i < j)
    {
      ((PrintAttributes.MediaSize)mMediaSizes.get(i)).writeToParcel(paramParcel);
      i++;
    }
  }
  
  private void writeResolutions(Parcel paramParcel)
  {
    List localList = mResolutions;
    int i = 0;
    if (localList == null)
    {
      paramParcel.writeInt(0);
      return;
    }
    int j = mResolutions.size();
    paramParcel.writeInt(j);
    while (i < j)
    {
      ((PrintAttributes.Resolution)mResolutions.get(i)).writeToParcel(paramParcel);
      i++;
    }
  }
  
  public void copyFrom(PrinterCapabilitiesInfo paramPrinterCapabilitiesInfo)
  {
    if (this == paramPrinterCapabilitiesInfo) {
      return;
    }
    mMinMargins = mMinMargins;
    if (mMediaSizes != null)
    {
      if (mMediaSizes != null)
      {
        mMediaSizes.clear();
        mMediaSizes.addAll(mMediaSizes);
      }
      else
      {
        mMediaSizes = new ArrayList(mMediaSizes);
      }
    }
    else {
      mMediaSizes = null;
    }
    if (mResolutions != null)
    {
      if (mResolutions != null)
      {
        mResolutions.clear();
        mResolutions.addAll(mResolutions);
      }
      else
      {
        mResolutions = new ArrayList(mResolutions);
      }
    }
    else {
      mResolutions = null;
    }
    mColorModes = mColorModes;
    mDuplexModes = mDuplexModes;
    int i = mDefaults.length;
    for (int j = 0; j < i; j++) {
      mDefaults[j] = mDefaults[j];
    }
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
    paramObject = (PrinterCapabilitiesInfo)paramObject;
    if (mMinMargins == null)
    {
      if (mMinMargins != null) {
        return false;
      }
    }
    else if (!mMinMargins.equals(mMinMargins)) {
      return false;
    }
    if (mMediaSizes == null)
    {
      if (mMediaSizes != null) {
        return false;
      }
    }
    else if (!mMediaSizes.equals(mMediaSizes)) {
      return false;
    }
    if (mResolutions == null)
    {
      if (mResolutions != null) {
        return false;
      }
    }
    else if (!mResolutions.equals(mResolutions)) {
      return false;
    }
    if (mColorModes != mColorModes) {
      return false;
    }
    if (mDuplexModes != mDuplexModes) {
      return false;
    }
    return Arrays.equals(mDefaults, mDefaults);
  }
  
  public int getColorModes()
  {
    return mColorModes;
  }
  
  public PrintAttributes getDefaults()
  {
    PrintAttributes.Builder localBuilder = new PrintAttributes.Builder();
    localBuilder.setMinMargins(mMinMargins);
    int i = mDefaults[0];
    if (i >= 0) {
      localBuilder.setMediaSize((PrintAttributes.MediaSize)mMediaSizes.get(i));
    }
    i = mDefaults[1];
    if (i >= 0) {
      localBuilder.setResolution((PrintAttributes.Resolution)mResolutions.get(i));
    }
    i = mDefaults[2];
    if (i > 0) {
      localBuilder.setColorMode(i);
    }
    i = mDefaults[3];
    if (i > 0) {
      localBuilder.setDuplexMode(i);
    }
    return localBuilder.build();
  }
  
  public int getDuplexModes()
  {
    return mDuplexModes;
  }
  
  public List<PrintAttributes.MediaSize> getMediaSizes()
  {
    return Collections.unmodifiableList(mMediaSizes);
  }
  
  public PrintAttributes.Margins getMinMargins()
  {
    return mMinMargins;
  }
  
  public List<PrintAttributes.Resolution> getResolutions()
  {
    return Collections.unmodifiableList(mResolutions);
  }
  
  public int hashCode()
  {
    PrintAttributes.Margins localMargins = mMinMargins;
    int i = 0;
    int j;
    if (localMargins == null) {
      j = 0;
    } else {
      j = mMinMargins.hashCode();
    }
    int k;
    if (mMediaSizes == null) {
      k = 0;
    } else {
      k = mMediaSizes.hashCode();
    }
    if (mResolutions != null) {
      i = mResolutions.hashCode();
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * 1 + j) + k) + i) + mColorModes) + mDuplexModes) + Arrays.hashCode(mDefaults);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PrinterInfo{");
    localStringBuilder.append("minMargins=");
    localStringBuilder.append(mMinMargins);
    localStringBuilder.append(", mediaSizes=");
    localStringBuilder.append(mMediaSizes);
    localStringBuilder.append(", resolutions=");
    localStringBuilder.append(mResolutions);
    localStringBuilder.append(", colorModes=");
    localStringBuilder.append(colorModesToString());
    localStringBuilder.append(", duplexModes=");
    localStringBuilder.append(duplexModesToString());
    localStringBuilder.append("\"}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeMargins(mMinMargins, paramParcel);
    writeMediaSizes(paramParcel);
    writeResolutions(paramParcel);
    paramParcel.writeInt(mColorModes);
    paramParcel.writeInt(mDuplexModes);
    writeDefaults(paramParcel);
  }
  
  public static final class Builder
  {
    private final PrinterCapabilitiesInfo mPrototype;
    
    public Builder(PrinterId paramPrinterId)
    {
      if (paramPrinterId != null)
      {
        mPrototype = new PrinterCapabilitiesInfo();
        return;
      }
      throw new IllegalArgumentException("printerId cannot be null.");
    }
    
    private void throwIfDefaultAlreadySpecified(int paramInt)
    {
      if (mPrototype.mDefaults[paramInt] == -1) {
        return;
      }
      throw new IllegalArgumentException("Default already specified.");
    }
    
    public Builder addMediaSize(PrintAttributes.MediaSize paramMediaSize, boolean paramBoolean)
    {
      if (mPrototype.mMediaSizes == null) {
        PrinterCapabilitiesInfo.access$002(mPrototype, new ArrayList());
      }
      int i = mPrototype.mMediaSizes.size();
      mPrototype.mMediaSizes.add(paramMediaSize);
      if (paramBoolean)
      {
        throwIfDefaultAlreadySpecified(0);
        mPrototype.mDefaults[0] = i;
      }
      return this;
    }
    
    public Builder addResolution(PrintAttributes.Resolution paramResolution, boolean paramBoolean)
    {
      if (mPrototype.mResolutions == null) {
        PrinterCapabilitiesInfo.access$202(mPrototype, new ArrayList());
      }
      int i = mPrototype.mResolutions.size();
      mPrototype.mResolutions.add(paramResolution);
      if (paramBoolean)
      {
        throwIfDefaultAlreadySpecified(1);
        mPrototype.mDefaults[1] = i;
      }
      return this;
    }
    
    public PrinterCapabilitiesInfo build()
    {
      if ((mPrototype.mMediaSizes != null) && (!mPrototype.mMediaSizes.isEmpty()))
      {
        if (mPrototype.mDefaults[0] != -1)
        {
          if ((mPrototype.mResolutions != null) && (!mPrototype.mResolutions.isEmpty()))
          {
            if (mPrototype.mDefaults[1] != -1)
            {
              if (mPrototype.mColorModes != 0)
              {
                if (mPrototype.mDefaults[2] != -1)
                {
                  if (mPrototype.mDuplexModes == 0) {
                    setDuplexModes(1, 1);
                  }
                  if (mPrototype.mMinMargins != null) {
                    return mPrototype;
                  }
                  throw new IllegalArgumentException("margins cannot be null");
                }
                throw new IllegalStateException("No default color mode specified.");
              }
              throw new IllegalStateException("No color mode specified.");
            }
            throw new IllegalStateException("No default resolution specified.");
          }
          throw new IllegalStateException("No resolution specified.");
        }
        throw new IllegalStateException("No default media size specified.");
      }
      throw new IllegalStateException("No media size specified.");
    }
    
    public Builder setColorModes(int paramInt1, int paramInt2)
    {
      PrinterCapabilitiesInfo.enforceValidMask(paramInt1, _..Lambda.PrinterCapabilitiesInfo.Builder.dbsSt8pZfd6hqZ6hGCnpzhPK6Uk.INSTANCE);
      PrintAttributes.enforceValidColorMode(paramInt2);
      PrinterCapabilitiesInfo.access$502(mPrototype, paramInt1);
      mPrototype.mDefaults[2] = paramInt2;
      return this;
    }
    
    public Builder setDuplexModes(int paramInt1, int paramInt2)
    {
      PrinterCapabilitiesInfo.enforceValidMask(paramInt1, _..Lambda.PrinterCapabilitiesInfo.Builder.gsgXbNHGWpWENdPzemgHcCY8HnE.INSTANCE);
      PrintAttributes.enforceValidDuplexMode(paramInt2);
      PrinterCapabilitiesInfo.access$602(mPrototype, paramInt1);
      mPrototype.mDefaults[3] = paramInt2;
      return this;
    }
    
    public Builder setMinMargins(PrintAttributes.Margins paramMargins)
    {
      if (paramMargins != null)
      {
        PrinterCapabilitiesInfo.access$302(mPrototype, paramMargins);
        return this;
      }
      throw new IllegalArgumentException("margins cannot be null");
    }
  }
}
