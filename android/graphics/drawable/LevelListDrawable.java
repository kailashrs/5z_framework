package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LevelListDrawable
  extends DrawableContainer
{
  private LevelListState mLevelListState;
  private boolean mMutated;
  
  public LevelListDrawable()
  {
    this(null, null);
  }
  
  private LevelListDrawable(LevelListState paramLevelListState, Resources paramResources)
  {
    setConstantState(new LevelListState(paramLevelListState, this, paramResources));
    onLevelChange(getLevel());
  }
  
  private void inflateChildElements(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth() + 1;
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (j == 1) {
        break label270;
      }
      int k = paramXmlPullParser.getDepth();
      if ((k < i) && (j == 3)) {
        break label270;
      }
      if ((j == 2) && (k <= i) && (paramXmlPullParser.getName().equals("item")))
      {
        Object localObject = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.LevelListDrawableItem);
        j = ((TypedArray)localObject).getInt(1, 0);
        k = ((TypedArray)localObject).getInt(2, 0);
        int m = ((TypedArray)localObject).getResourceId(0, 0);
        ((TypedArray)localObject).recycle();
        if (k < 0) {
          break label232;
        }
        if (m != 0)
        {
          localObject = paramResources.getDrawable(m, paramTheme);
        }
        else
        {
          do
          {
            m = paramXmlPullParser.next();
          } while (m == 4);
          if (m != 2) {
            break;
          }
          localObject = Drawable.createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
        }
        mLevelListState.addLevel(j, k, (Drawable)localObject);
      }
    }
    paramResources = new StringBuilder();
    paramResources.append(paramXmlPullParser.getPositionDescription());
    paramResources.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
    throw new XmlPullParserException(paramResources.toString());
    label232:
    paramResources = new StringBuilder();
    paramResources.append(paramXmlPullParser.getPositionDescription());
    paramResources.append(": <item> tag requires a 'maxLevel' attribute");
    throw new XmlPullParserException(paramResources.toString());
    label270:
    onLevelChange(getLevel());
  }
  
  public void addLevel(int paramInt1, int paramInt2, Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      mLevelListState.addLevel(paramInt1, paramInt2, paramDrawable);
      onLevelChange(getLevel());
    }
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    mMutated = false;
  }
  
  LevelListState cloneConstantState()
  {
    return new LevelListState(mLevelListState, this, null);
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    updateDensity(paramResources);
    inflateChildElements(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mLevelListState.mutate();
      mMutated = true;
    }
    return this;
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    if (selectDrawable(mLevelListState.indexOfLevel(paramInt))) {
      return true;
    }
    return super.onLevelChange(paramInt);
  }
  
  protected void setConstantState(DrawableContainer.DrawableContainerState paramDrawableContainerState)
  {
    super.setConstantState(paramDrawableContainerState);
    if ((paramDrawableContainerState instanceof LevelListState)) {
      mLevelListState = ((LevelListState)paramDrawableContainerState);
    }
  }
  
  private static final class LevelListState
    extends DrawableContainer.DrawableContainerState
  {
    private int[] mHighs;
    private int[] mLows;
    
    LevelListState(LevelListState paramLevelListState, LevelListDrawable paramLevelListDrawable, Resources paramResources)
    {
      super(paramLevelListDrawable, paramResources);
      if (paramLevelListState != null)
      {
        mLows = mLows;
        mHighs = mHighs;
      }
      else
      {
        mLows = new int[getCapacity()];
        mHighs = new int[getCapacity()];
      }
    }
    
    private void mutate()
    {
      mLows = ((int[])mLows.clone());
      mHighs = ((int[])mHighs.clone());
    }
    
    public void addLevel(int paramInt1, int paramInt2, Drawable paramDrawable)
    {
      int i = addChild(paramDrawable);
      mLows[i] = paramInt1;
      mHighs[i] = paramInt2;
    }
    
    public void growArray(int paramInt1, int paramInt2)
    {
      super.growArray(paramInt1, paramInt2);
      int[] arrayOfInt = new int[paramInt2];
      System.arraycopy(mLows, 0, arrayOfInt, 0, paramInt1);
      mLows = arrayOfInt;
      arrayOfInt = new int[paramInt2];
      System.arraycopy(mHighs, 0, arrayOfInt, 0, paramInt1);
      mHighs = arrayOfInt;
    }
    
    public int indexOfLevel(int paramInt)
    {
      int[] arrayOfInt1 = mLows;
      int[] arrayOfInt2 = mHighs;
      int i = getChildCount();
      for (int j = 0; j < i; j++) {
        if ((paramInt >= arrayOfInt1[j]) && (paramInt <= arrayOfInt2[j])) {
          return j;
        }
      }
      return -1;
    }
    
    public Drawable newDrawable()
    {
      return new LevelListDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new LevelListDrawable(this, paramResources, null);
    }
  }
}
