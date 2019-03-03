package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.StateSet;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class StateListDrawable
  extends DrawableContainer
{
  private static final boolean DEBUG = false;
  private static final String TAG = "StateListDrawable";
  private boolean mMutated;
  private StateListState mStateListState;
  
  public StateListDrawable()
  {
    this(null, null);
  }
  
  StateListDrawable(StateListState paramStateListState)
  {
    if (paramStateListState != null) {
      setConstantState(paramStateListState);
    }
  }
  
  private StateListDrawable(StateListState paramStateListState, Resources paramResources)
  {
    setConstantState(new StateListState(paramStateListState, this, paramResources));
    onStateChange(getState());
  }
  
  private void inflateChildElements(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    StateListState localStateListState = mStateListState;
    int i = paramXmlPullParser.getDepth() + 1;
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (j == 1) {
        break;
      }
      int k = paramXmlPullParser.getDepth();
      if ((k < i) && (j == 3)) {
        break;
      }
      if ((j == 2) && (k <= i) && (paramXmlPullParser.getName().equals("item")))
      {
        Object localObject = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.StateListDrawableItem);
        Drawable localDrawable = ((TypedArray)localObject).getDrawable(0);
        ((TypedArray)localObject).recycle();
        int[] arrayOfInt = extractStateSet(paramAttributeSet);
        localObject = localDrawable;
        if (localDrawable == null)
        {
          do
          {
            k = paramXmlPullParser.next();
          } while (k == 4);
          if (k == 2)
          {
            localObject = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
          }
          else
          {
            paramResources = new StringBuilder();
            paramResources.append(paramXmlPullParser.getPositionDescription());
            paramResources.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
            throw new XmlPullParserException(paramResources.toString());
          }
        }
        localStateListState.addStateSet(arrayOfInt, (Drawable)localObject);
      }
    }
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    StateListState localStateListState = mStateListState;
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    mThemeAttrs = paramTypedArray.extractThemeAttrs();
    mVariablePadding = paramTypedArray.getBoolean(2, mVariablePadding);
    mConstantSize = paramTypedArray.getBoolean(3, mConstantSize);
    mEnterFadeDuration = paramTypedArray.getInt(4, mEnterFadeDuration);
    mExitFadeDuration = paramTypedArray.getInt(5, mExitFadeDuration);
    mDither = paramTypedArray.getBoolean(0, mDither);
    mAutoMirrored = paramTypedArray.getBoolean(6, mAutoMirrored);
  }
  
  public void addState(int[] paramArrayOfInt, Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      mStateListState.addStateSet(paramArrayOfInt, paramDrawable);
      onStateChange(getState());
    }
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    super.applyTheme(paramTheme);
    onStateChange(getState());
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    mMutated = false;
  }
  
  StateListState cloneConstantState()
  {
    return new StateListState(mStateListState, this, null);
  }
  
  int[] extractStateSet(AttributeSet paramAttributeSet)
  {
    int i = paramAttributeSet.getAttributeCount();
    int[] arrayOfInt = new int[i];
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      int m = paramAttributeSet.getAttributeNameResource(k);
      if (m != 0) {
        if ((m != 16842960) && (m != 16843161))
        {
          if (!paramAttributeSet.getAttributeBooleanValue(k, false)) {
            m = -m;
          }
          arrayOfInt[j] = m;
          j++;
        }
        else {}
      }
    }
    return StateSet.trimStateSet(arrayOfInt, j);
  }
  
  public int getStateCount()
  {
    return mStateListState.getChildCount();
  }
  
  public Drawable getStateDrawable(int paramInt)
  {
    return mStateListState.getChild(paramInt);
  }
  
  public int getStateDrawableIndex(int[] paramArrayOfInt)
  {
    return mStateListState.indexOfStateSet(paramArrayOfInt);
  }
  
  StateListState getStateListState()
  {
    return mStateListState;
  }
  
  public int[] getStateSet(int paramInt)
  {
    return mStateListState.mStateSets[paramInt];
  }
  
  public boolean hasFocusStateSpecified()
  {
    return mStateListState.hasFocusStateSpecified();
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.StateListDrawable);
    super.inflateWithAttributes(paramResources, paramXmlPullParser, localTypedArray, 1);
    updateStateFromTypedArray(localTypedArray);
    updateDensity(paramResources);
    localTypedArray.recycle();
    inflateChildElements(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    onStateChange(getState());
  }
  
  public boolean isStateful()
  {
    return true;
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mStateListState.mutate();
      mMutated = true;
    }
    return this;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    boolean bool = super.onStateChange(paramArrayOfInt);
    int i = mStateListState.indexOfStateSet(paramArrayOfInt);
    int j = i;
    if (i < 0) {
      j = mStateListState.indexOfStateSet(StateSet.WILD_CARD);
    }
    if ((!selectDrawable(j)) && (!bool)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  protected void setConstantState(DrawableContainer.DrawableContainerState paramDrawableContainerState)
  {
    super.setConstantState(paramDrawableContainerState);
    if ((paramDrawableContainerState instanceof StateListState)) {
      mStateListState = ((StateListState)paramDrawableContainerState);
    }
  }
  
  static class StateListState
    extends DrawableContainer.DrawableContainerState
  {
    int[][] mStateSets;
    int[] mThemeAttrs;
    
    StateListState(StateListState paramStateListState, StateListDrawable paramStateListDrawable, Resources paramResources)
    {
      super(paramStateListDrawable, paramResources);
      if (paramStateListState != null)
      {
        mThemeAttrs = mThemeAttrs;
        mStateSets = mStateSets;
      }
      else
      {
        mThemeAttrs = null;
        mStateSets = new int[getCapacity()][];
      }
    }
    
    int addStateSet(int[] paramArrayOfInt, Drawable paramDrawable)
    {
      int i = addChild(paramDrawable);
      mStateSets[i] = paramArrayOfInt;
      return i;
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mThemeAttrs == null) && (!super.canApplyTheme())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public void growArray(int paramInt1, int paramInt2)
    {
      super.growArray(paramInt1, paramInt2);
      int[][] arrayOfInt = new int[paramInt2][];
      System.arraycopy(mStateSets, 0, arrayOfInt, 0, paramInt1);
      mStateSets = arrayOfInt;
    }
    
    boolean hasFocusStateSpecified()
    {
      return StateSet.containsAttribute(mStateSets, 16842908);
    }
    
    int indexOfStateSet(int[] paramArrayOfInt)
    {
      int[][] arrayOfInt = mStateSets;
      int i = getChildCount();
      for (int j = 0; j < i; j++) {
        if (StateSet.stateSetMatches(arrayOfInt[j], paramArrayOfInt)) {
          return j;
        }
      }
      return -1;
    }
    
    void mutate()
    {
      int[] arrayOfInt;
      if (mThemeAttrs != null) {
        arrayOfInt = (int[])mThemeAttrs.clone();
      } else {
        arrayOfInt = null;
      }
      mThemeAttrs = arrayOfInt;
      int[][] arrayOfInt1 = new int[mStateSets.length][];
      for (int i = mStateSets.length - 1; i >= 0; i--)
      {
        if (mStateSets[i] != null) {
          arrayOfInt = (int[])mStateSets[i].clone();
        } else {
          arrayOfInt = null;
        }
        arrayOfInt1[i] = arrayOfInt;
      }
      mStateSets = arrayOfInt1;
    }
    
    public Drawable newDrawable()
    {
      return new StateListDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new StateListDrawable(this, paramResources, null);
    }
  }
}
