package android.media;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.view.accessibility.CaptioningManager;
import android.view.accessibility.CaptioningManager.CaptioningChangeListener;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

public class SubtitleController
{
  private static final int WHAT_HIDE = 2;
  private static final int WHAT_SELECT_DEFAULT_TRACK = 4;
  private static final int WHAT_SELECT_TRACK = 3;
  private static final int WHAT_SHOW = 1;
  private Anchor mAnchor;
  private final Handler.Callback mCallback = new Handler.Callback()
  {
    public boolean handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        return false;
      case 4: 
        SubtitleController.this.doSelectDefaultTrack();
        return true;
      case 3: 
        SubtitleController.this.doSelectTrack((SubtitleTrack)obj);
        return true;
      case 2: 
        SubtitleController.this.doHide();
        return true;
      }
      SubtitleController.this.doShow();
      return true;
    }
  };
  private CaptioningManager.CaptioningChangeListener mCaptioningChangeListener = new CaptioningManager.CaptioningChangeListener()
  {
    public void onEnabledChanged(boolean paramAnonymousBoolean)
    {
      selectDefaultTrack();
    }
    
    public void onLocaleChanged(Locale paramAnonymousLocale)
    {
      selectDefaultTrack();
    }
  };
  private CaptioningManager mCaptioningManager;
  private Handler mHandler;
  private Listener mListener;
  private Vector<Renderer> mRenderers;
  private SubtitleTrack mSelectedTrack;
  private boolean mShowing;
  private MediaTimeProvider mTimeProvider;
  private boolean mTrackIsExplicit = false;
  private Vector<SubtitleTrack> mTracks;
  private boolean mVisibilityIsExplicit = false;
  
  public SubtitleController(Context paramContext, MediaTimeProvider paramMediaTimeProvider, Listener paramListener)
  {
    mTimeProvider = paramMediaTimeProvider;
    mListener = paramListener;
    mRenderers = new Vector();
    mShowing = false;
    mTracks = new Vector();
    mCaptioningManager = ((CaptioningManager)paramContext.getSystemService("captioning"));
  }
  
  private void checkAnchorLooper() {}
  
  private void doHide()
  {
    mVisibilityIsExplicit = true;
    if (mSelectedTrack != null) {
      mSelectedTrack.hide();
    }
    mShowing = false;
  }
  
  private void doSelectDefaultTrack()
  {
    if (mTrackIsExplicit)
    {
      if (!mVisibilityIsExplicit)
      {
        if ((!mCaptioningManager.isEnabled()) && ((mSelectedTrack == null) || (mSelectedTrack.getFormat().getInteger("is-forced-subtitle", 0) == 0)))
        {
          if ((mSelectedTrack != null) && (mSelectedTrack.getTrackType() == 4)) {
            hide();
          }
        }
        else {
          show();
        }
        mVisibilityIsExplicit = false;
      }
      return;
    }
    SubtitleTrack localSubtitleTrack = getDefaultTrack();
    if (localSubtitleTrack != null)
    {
      selectTrack(localSubtitleTrack);
      mTrackIsExplicit = false;
      if (!mVisibilityIsExplicit)
      {
        show();
        mVisibilityIsExplicit = false;
      }
    }
  }
  
  private void doSelectTrack(SubtitleTrack paramSubtitleTrack)
  {
    mTrackIsExplicit = true;
    if (mSelectedTrack == paramSubtitleTrack) {
      return;
    }
    if (mSelectedTrack != null)
    {
      mSelectedTrack.hide();
      mSelectedTrack.setTimeProvider(null);
    }
    mSelectedTrack = paramSubtitleTrack;
    if (mAnchor != null) {
      mAnchor.setSubtitleWidget(getRenderingWidget());
    }
    if (mSelectedTrack != null)
    {
      mSelectedTrack.setTimeProvider(mTimeProvider);
      mSelectedTrack.show();
    }
    if (mListener != null) {
      mListener.onSubtitleTrackSelected(paramSubtitleTrack);
    }
  }
  
  private void doShow()
  {
    mShowing = true;
    mVisibilityIsExplicit = true;
    if (mSelectedTrack != null) {
      mSelectedTrack.show();
    }
  }
  
  private SubtitleTrack.RenderingWidget getRenderingWidget()
  {
    if (mSelectedTrack == null) {
      return null;
    }
    return mSelectedTrack.getRenderingWidget();
  }
  
  private void processOnAnchor(Message paramMessage)
  {
    if (Looper.myLooper() == mHandler.getLooper()) {
      mHandler.dispatchMessage(paramMessage);
    } else {
      mHandler.sendMessage(paramMessage);
    }
  }
  
  public SubtitleTrack addTrack(MediaFormat arg1)
  {
    synchronized (mRenderers)
    {
      Iterator localIterator = mRenderers.iterator();
      while (localIterator.hasNext())
      {
        Object localObject1 = (Renderer)localIterator.next();
        if (((Renderer)localObject1).supports(???))
        {
          localObject1 = ((Renderer)localObject1).createTrack(???);
          if (localObject1 != null) {
            synchronized (mTracks)
            {
              if (mTracks.size() == 0) {
                mCaptioningManager.addCaptioningChangeListener(mCaptioningChangeListener);
              }
              mTracks.add(localObject1);
              return localObject1;
            }
          }
        }
      }
      return null;
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    mCaptioningManager.removeCaptioningChangeListener(mCaptioningChangeListener);
    super.finalize();
  }
  
  public SubtitleTrack getDefaultTrack()
  {
    Object localObject1 = null;
    int i = -1;
    Locale localLocale = mCaptioningManager.getLocale();
    Object localObject2 = localLocale;
    Object localObject4 = localObject2;
    if (localObject2 == null) {
      localObject4 = Locale.getDefault();
    }
    boolean bool = mCaptioningManager.isEnabled();
    synchronized (mTracks)
    {
      Iterator localIterator = mTracks.iterator();
      localObject2 = localObject1;
      while (localIterator.hasNext())
      {
        SubtitleTrack localSubtitleTrack = (SubtitleTrack)localIterator.next();
        MediaFormat localMediaFormat = localSubtitleTrack.getFormat();
        localObject1 = localMediaFormat.getString("language");
        int j;
        if (localMediaFormat.getInteger("is-forced-subtitle", 0) != 0) {
          j = 1;
        } else {
          j = 0;
        }
        int k;
        if (localMediaFormat.getInteger("is-autoselect", 1) != 0) {
          k = 1;
        } else {
          k = 0;
        }
        int m;
        if (localMediaFormat.getInteger("is-default", 0) != 0) {
          m = 1;
        } else {
          m = 0;
        }
        int n;
        if ((localObject4 != null) && (!((Locale)localObject4).getLanguage().equals("")) && (!((Locale)localObject4).getISO3Language().equals(localObject1)) && (!((Locale)localObject4).getLanguage().equals(localObject1))) {
          n = 0;
        } else {
          n = 1;
        }
        if (j != 0) {
          i1 = 0;
        } else {
          i1 = 8;
        }
        int i2;
        if ((localLocale == null) && (m != 0)) {
          i2 = 4;
        } else {
          i2 = 0;
        }
        int i3;
        if (k != 0) {
          i3 = 0;
        } else {
          i3 = 2;
        }
        int i4;
        if (n != 0) {
          i4 = 1;
        } else {
          i4 = 0;
        }
        int i1 = i1 + i2 + i3 + i4;
        if ((!(bool ^ true)) || (j != 0)) {
          for (;;)
          {
            if ((localLocale != null) || (m == 0))
            {
              localObject1 = localObject2;
              m = i;
              if (n == 0) {
                break label360;
              }
              if ((k == 0) && (j == 0))
              {
                localObject1 = localObject2;
                m = i;
                if (localLocale == null) {
                  break label360;
                }
              }
            }
            localObject1 = localObject2;
            m = i;
            if (i1 > i)
            {
              m = i1;
              localObject1 = localSubtitleTrack;
            }
            label360:
            localObject2 = localObject1;
            i = m;
          }
        }
      }
      return localObject2;
    }
  }
  
  public SubtitleTrack getSelectedTrack()
  {
    return mSelectedTrack;
  }
  
  public SubtitleTrack[] getTracks()
  {
    synchronized (mTracks)
    {
      SubtitleTrack[] arrayOfSubtitleTrack = new SubtitleTrack[mTracks.size()];
      mTracks.toArray(arrayOfSubtitleTrack);
      return arrayOfSubtitleTrack;
    }
  }
  
  public boolean hasRendererFor(MediaFormat paramMediaFormat)
  {
    synchronized (mRenderers)
    {
      Iterator localIterator = mRenderers.iterator();
      while (localIterator.hasNext()) {
        if (((Renderer)localIterator.next()).supports(paramMediaFormat)) {
          return true;
        }
      }
      return false;
    }
  }
  
  public void hide()
  {
    processOnAnchor(mHandler.obtainMessage(2));
  }
  
  public void registerRenderer(Renderer paramRenderer)
  {
    synchronized (mRenderers)
    {
      if (!mRenderers.contains(paramRenderer)) {
        mRenderers.add(paramRenderer);
      }
      return;
    }
  }
  
  public void reset()
  {
    checkAnchorLooper();
    hide();
    selectTrack(null);
    mTracks.clear();
    mTrackIsExplicit = false;
    mVisibilityIsExplicit = false;
    mCaptioningManager.removeCaptioningChangeListener(mCaptioningChangeListener);
  }
  
  public void selectDefaultTrack()
  {
    processOnAnchor(mHandler.obtainMessage(4));
  }
  
  public boolean selectTrack(SubtitleTrack paramSubtitleTrack)
  {
    if ((paramSubtitleTrack != null) && (!mTracks.contains(paramSubtitleTrack))) {
      return false;
    }
    processOnAnchor(mHandler.obtainMessage(3, paramSubtitleTrack));
    return true;
  }
  
  public void setAnchor(Anchor paramAnchor)
  {
    if (mAnchor == paramAnchor) {
      return;
    }
    if (mAnchor != null)
    {
      checkAnchorLooper();
      mAnchor.setSubtitleWidget(null);
    }
    mAnchor = paramAnchor;
    mHandler = null;
    if (mAnchor != null)
    {
      mHandler = new Handler(mAnchor.getSubtitleLooper(), mCallback);
      checkAnchorLooper();
      mAnchor.setSubtitleWidget(getRenderingWidget());
    }
  }
  
  public void show()
  {
    processOnAnchor(mHandler.obtainMessage(1));
  }
  
  public static abstract interface Anchor
  {
    public abstract Looper getSubtitleLooper();
    
    public abstract void setSubtitleWidget(SubtitleTrack.RenderingWidget paramRenderingWidget);
  }
  
  public static abstract interface Listener
  {
    public abstract void onSubtitleTrackSelected(SubtitleTrack paramSubtitleTrack);
  }
  
  public static abstract class Renderer
  {
    public Renderer() {}
    
    public abstract SubtitleTrack createTrack(MediaFormat paramMediaFormat);
    
    public abstract boolean supports(MediaFormat paramMediaFormat);
  }
}
