package android.view.inputmethod;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.SpannedString;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.Objects;

public final class CursorAnchorInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CursorAnchorInfo> CREATOR = new Parcelable.Creator()
  {
    public CursorAnchorInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CursorAnchorInfo(paramAnonymousParcel);
    }
    
    public CursorAnchorInfo[] newArray(int paramAnonymousInt)
    {
      return new CursorAnchorInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_HAS_INVISIBLE_REGION = 2;
  public static final int FLAG_HAS_VISIBLE_REGION = 1;
  public static final int FLAG_IS_RTL = 4;
  private final SparseRectFArray mCharacterBoundsArray;
  private final CharSequence mComposingText;
  private final int mComposingTextStart;
  private final int mHashCode;
  private final float mInsertionMarkerBaseline;
  private final float mInsertionMarkerBottom;
  private final int mInsertionMarkerFlags;
  private final float mInsertionMarkerHorizontal;
  private final float mInsertionMarkerTop;
  private final float[] mMatrixValues;
  private final int mSelectionEnd;
  private final int mSelectionStart;
  
  public CursorAnchorInfo(Parcel paramParcel)
  {
    mHashCode = paramParcel.readInt();
    mSelectionStart = paramParcel.readInt();
    mSelectionEnd = paramParcel.readInt();
    mComposingTextStart = paramParcel.readInt();
    mComposingText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mInsertionMarkerFlags = paramParcel.readInt();
    mInsertionMarkerHorizontal = paramParcel.readFloat();
    mInsertionMarkerTop = paramParcel.readFloat();
    mInsertionMarkerBaseline = paramParcel.readFloat();
    mInsertionMarkerBottom = paramParcel.readFloat();
    mCharacterBoundsArray = ((SparseRectFArray)paramParcel.readParcelable(SparseRectFArray.class.getClassLoader()));
    mMatrixValues = paramParcel.createFloatArray();
  }
  
  private CursorAnchorInfo(Builder paramBuilder)
  {
    mSelectionStart = mSelectionStart;
    mSelectionEnd = mSelectionEnd;
    mComposingTextStart = mComposingTextStart;
    mComposingText = mComposingText;
    mInsertionMarkerFlags = mInsertionMarkerFlags;
    mInsertionMarkerHorizontal = mInsertionMarkerHorizontal;
    mInsertionMarkerTop = mInsertionMarkerTop;
    mInsertionMarkerBaseline = mInsertionMarkerBaseline;
    mInsertionMarkerBottom = mInsertionMarkerBottom;
    SparseRectFArray localSparseRectFArray;
    if (mCharacterBoundsArrayBuilder != null) {
      localSparseRectFArray = mCharacterBoundsArrayBuilder.build();
    } else {
      localSparseRectFArray = null;
    }
    mCharacterBoundsArray = localSparseRectFArray;
    mMatrixValues = new float[9];
    if (mMatrixInitialized) {
      System.arraycopy(mMatrixValues, 0, mMatrixValues, 0, 9);
    } else {
      Matrix.IDENTITY_MATRIX.getValues(mMatrixValues);
    }
    mHashCode = (Objects.hashCode(mComposingText) * 31 + Arrays.hashCode(mMatrixValues));
  }
  
  private static boolean areSameFloatImpl(float paramFloat1, float paramFloat2)
  {
    boolean bool1 = Float.isNaN(paramFloat1);
    boolean bool2 = true;
    if ((bool1) && (Float.isNaN(paramFloat2))) {
      return true;
    }
    if (paramFloat1 != paramFloat2) {
      bool2 = false;
    }
    return bool2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof CursorAnchorInfo)) {
      return false;
    }
    paramObject = (CursorAnchorInfo)paramObject;
    if (hashCode() != paramObject.hashCode()) {
      return false;
    }
    if ((mSelectionStart == mSelectionStart) && (mSelectionEnd == mSelectionEnd))
    {
      if ((mInsertionMarkerFlags == mInsertionMarkerFlags) && (areSameFloatImpl(mInsertionMarkerHorizontal, mInsertionMarkerHorizontal)) && (areSameFloatImpl(mInsertionMarkerTop, mInsertionMarkerTop)) && (areSameFloatImpl(mInsertionMarkerBaseline, mInsertionMarkerBaseline)) && (areSameFloatImpl(mInsertionMarkerBottom, mInsertionMarkerBottom)))
      {
        if (!Objects.equals(mCharacterBoundsArray, mCharacterBoundsArray)) {
          return false;
        }
        if ((mComposingTextStart == mComposingTextStart) && (Objects.equals(mComposingText, mComposingText)))
        {
          if (mMatrixValues.length != mMatrixValues.length) {
            return false;
          }
          for (int i = 0; i < mMatrixValues.length; i++) {
            if (mMatrixValues[i] != mMatrixValues[i]) {
              return false;
            }
          }
          return true;
        }
        return false;
      }
      return false;
    }
    return false;
  }
  
  public RectF getCharacterBounds(int paramInt)
  {
    if (mCharacterBoundsArray == null) {
      return null;
    }
    return mCharacterBoundsArray.get(paramInt);
  }
  
  public int getCharacterBoundsFlags(int paramInt)
  {
    if (mCharacterBoundsArray == null) {
      return 0;
    }
    return mCharacterBoundsArray.getFlags(paramInt, 0);
  }
  
  public CharSequence getComposingText()
  {
    return mComposingText;
  }
  
  public int getComposingTextStart()
  {
    return mComposingTextStart;
  }
  
  public float getInsertionMarkerBaseline()
  {
    return mInsertionMarkerBaseline;
  }
  
  public float getInsertionMarkerBottom()
  {
    return mInsertionMarkerBottom;
  }
  
  public int getInsertionMarkerFlags()
  {
    return mInsertionMarkerFlags;
  }
  
  public float getInsertionMarkerHorizontal()
  {
    return mInsertionMarkerHorizontal;
  }
  
  public float getInsertionMarkerTop()
  {
    return mInsertionMarkerTop;
  }
  
  public Matrix getMatrix()
  {
    Matrix localMatrix = new Matrix();
    localMatrix.setValues(mMatrixValues);
    return localMatrix;
  }
  
  public int getSelectionEnd()
  {
    return mSelectionEnd;
  }
  
  public int getSelectionStart()
  {
    return mSelectionStart;
  }
  
  public int hashCode()
  {
    return mHashCode;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CursorAnchorInfo{mHashCode=");
    localStringBuilder.append(mHashCode);
    localStringBuilder.append(" mSelection=");
    localStringBuilder.append(mSelectionStart);
    localStringBuilder.append(",");
    localStringBuilder.append(mSelectionEnd);
    localStringBuilder.append(" mComposingTextStart=");
    localStringBuilder.append(mComposingTextStart);
    localStringBuilder.append(" mComposingText=");
    localStringBuilder.append(Objects.toString(mComposingText));
    localStringBuilder.append(" mInsertionMarkerFlags=");
    localStringBuilder.append(mInsertionMarkerFlags);
    localStringBuilder.append(" mInsertionMarkerHorizontal=");
    localStringBuilder.append(mInsertionMarkerHorizontal);
    localStringBuilder.append(" mInsertionMarkerTop=");
    localStringBuilder.append(mInsertionMarkerTop);
    localStringBuilder.append(" mInsertionMarkerBaseline=");
    localStringBuilder.append(mInsertionMarkerBaseline);
    localStringBuilder.append(" mInsertionMarkerBottom=");
    localStringBuilder.append(mInsertionMarkerBottom);
    localStringBuilder.append(" mCharacterBoundsArray=");
    localStringBuilder.append(Objects.toString(mCharacterBoundsArray));
    localStringBuilder.append(" mMatrix=");
    localStringBuilder.append(Arrays.toString(mMatrixValues));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mHashCode);
    paramParcel.writeInt(mSelectionStart);
    paramParcel.writeInt(mSelectionEnd);
    paramParcel.writeInt(mComposingTextStart);
    TextUtils.writeToParcel(mComposingText, paramParcel, paramInt);
    paramParcel.writeInt(mInsertionMarkerFlags);
    paramParcel.writeFloat(mInsertionMarkerHorizontal);
    paramParcel.writeFloat(mInsertionMarkerTop);
    paramParcel.writeFloat(mInsertionMarkerBaseline);
    paramParcel.writeFloat(mInsertionMarkerBottom);
    paramParcel.writeParcelable(mCharacterBoundsArray, paramInt);
    paramParcel.writeFloatArray(mMatrixValues);
  }
  
  public static final class Builder
  {
    private SparseRectFArray.SparseRectFArrayBuilder mCharacterBoundsArrayBuilder = null;
    private CharSequence mComposingText = null;
    private int mComposingTextStart = -1;
    private float mInsertionMarkerBaseline = NaN.0F;
    private float mInsertionMarkerBottom = NaN.0F;
    private int mInsertionMarkerFlags = 0;
    private float mInsertionMarkerHorizontal = NaN.0F;
    private float mInsertionMarkerTop = NaN.0F;
    private boolean mMatrixInitialized = false;
    private float[] mMatrixValues = null;
    private int mSelectionEnd = -1;
    private int mSelectionStart = -1;
    
    public Builder() {}
    
    public Builder addCharacterBounds(int paramInt1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt2)
    {
      if (paramInt1 >= 0)
      {
        if (mCharacterBoundsArrayBuilder == null) {
          mCharacterBoundsArrayBuilder = new SparseRectFArray.SparseRectFArrayBuilder();
        }
        mCharacterBoundsArrayBuilder.append(paramInt1, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt2);
        return this;
      }
      throw new IllegalArgumentException("index must not be a negative integer.");
    }
    
    public CursorAnchorInfo build()
    {
      if (!mMatrixInitialized)
      {
        int i;
        if ((mCharacterBoundsArrayBuilder != null) && (!mCharacterBoundsArrayBuilder.isEmpty())) {
          i = 1;
        } else {
          i = 0;
        }
        if ((i != 0) || (!Float.isNaN(mInsertionMarkerHorizontal)) || (!Float.isNaN(mInsertionMarkerTop)) || (!Float.isNaN(mInsertionMarkerBaseline)) || (!Float.isNaN(mInsertionMarkerBottom))) {
          throw new IllegalArgumentException("Coordinate transformation matrix is required when positional parameters are specified.");
        }
      }
      return new CursorAnchorInfo(this, null);
    }
    
    public void reset()
    {
      mSelectionStart = -1;
      mSelectionEnd = -1;
      mComposingTextStart = -1;
      mComposingText = null;
      mInsertionMarkerFlags = 0;
      mInsertionMarkerHorizontal = NaN.0F;
      mInsertionMarkerTop = NaN.0F;
      mInsertionMarkerBaseline = NaN.0F;
      mInsertionMarkerBottom = NaN.0F;
      mMatrixInitialized = false;
      if (mCharacterBoundsArrayBuilder != null) {
        mCharacterBoundsArrayBuilder.reset();
      }
    }
    
    public Builder setComposingText(int paramInt, CharSequence paramCharSequence)
    {
      mComposingTextStart = paramInt;
      if (paramCharSequence == null) {
        mComposingText = null;
      } else {
        mComposingText = new SpannedString(paramCharSequence);
      }
      return this;
    }
    
    public Builder setInsertionMarkerLocation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt)
    {
      mInsertionMarkerHorizontal = paramFloat1;
      mInsertionMarkerTop = paramFloat2;
      mInsertionMarkerBaseline = paramFloat3;
      mInsertionMarkerBottom = paramFloat4;
      mInsertionMarkerFlags = paramInt;
      return this;
    }
    
    public Builder setMatrix(Matrix paramMatrix)
    {
      if (mMatrixValues == null) {
        mMatrixValues = new float[9];
      }
      if (paramMatrix == null) {
        paramMatrix = Matrix.IDENTITY_MATRIX;
      }
      paramMatrix.getValues(mMatrixValues);
      mMatrixInitialized = true;
      return this;
    }
    
    public Builder setSelectionRange(int paramInt1, int paramInt2)
    {
      mSelectionStart = paramInt1;
      mSelectionEnd = paramInt2;
      return this;
    }
  }
}
