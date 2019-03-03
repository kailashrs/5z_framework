package android.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.Metadata;
import android.media.SubtitleController.Anchor;
import android.media.SubtitleTrack.RenderingWidget;
import android.media.SubtitleTrack.RenderingWidget.OnChangedListener;
import android.net.Uri;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.MeasureSpec;
import java.io.InputStream;
import java.util.Map;
import java.util.Vector;

public class VideoView
  extends SurfaceView
  implements MediaController.MediaPlayerControl, SubtitleController.Anchor
{
  private static final int STATE_ERROR = -1;
  private static final int STATE_IDLE = 0;
  private static final int STATE_PAUSED = 4;
  private static final int STATE_PLAYBACK_COMPLETED = 5;
  private static final int STATE_PLAYING = 3;
  private static final int STATE_PREPARED = 2;
  private static final int STATE_PREPARING = 1;
  private static final String TAG = "VideoView";
  private AudioAttributes mAudioAttributes = new AudioAttributes.Builder().setUsage(1).setContentType(3).build();
  private int mAudioFocusType = 1;
  private AudioManager mAudioManager = (AudioManager)mContext.getSystemService("audio");
  private int mAudioSession;
  private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener()
  {
    public void onBufferingUpdate(MediaPlayer paramAnonymousMediaPlayer, int paramAnonymousInt)
    {
      VideoView.access$2002(VideoView.this, paramAnonymousInt);
    }
  };
  private boolean mCanPause;
  private boolean mCanSeekBack;
  private boolean mCanSeekForward;
  private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener()
  {
    public void onCompletion(MediaPlayer paramAnonymousMediaPlayer)
    {
      VideoView.access$202(VideoView.this, 5);
      VideoView.access$1202(VideoView.this, 5);
      if (mMediaController != null) {
        mMediaController.hide();
      }
      if (mOnCompletionListener != null) {
        mOnCompletionListener.onCompletion(mMediaPlayer);
      }
      if (mAudioFocusType != 0) {
        mAudioManager.abandonAudioFocus(null);
      }
    }
  };
  private int mCurrentBufferPercentage;
  private int mCurrentState = 0;
  private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener()
  {
    public boolean onError(MediaPlayer paramAnonymousMediaPlayer, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      paramAnonymousMediaPlayer = new StringBuilder();
      paramAnonymousMediaPlayer.append("Error: ");
      paramAnonymousMediaPlayer.append(paramAnonymousInt1);
      paramAnonymousMediaPlayer.append(",");
      paramAnonymousMediaPlayer.append(paramAnonymousInt2);
      Log.d("VideoView", paramAnonymousMediaPlayer.toString());
      VideoView.access$202(VideoView.this, -1);
      VideoView.access$1202(VideoView.this, -1);
      if (mMediaController != null) {
        mMediaController.hide();
      }
      if ((mOnErrorListener != null) && (mOnErrorListener.onError(mMediaPlayer, paramAnonymousInt1, paramAnonymousInt2))) {
        return true;
      }
      if (getWindowToken() != null)
      {
        mContext.getResources();
        if (paramAnonymousInt1 == 200) {
          paramAnonymousInt1 = 17039381;
        } else {
          paramAnonymousInt1 = 17039377;
        }
        new AlertDialog.Builder(mContext).setMessage(paramAnonymousInt1).setPositiveButton(17039376, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
          {
            if (mOnCompletionListener != null) {
              mOnCompletionListener.onCompletion(mMediaPlayer);
            }
          }
        }).setCancelable(false).show();
      }
      return true;
    }
  };
  private Map<String, String> mHeaders;
  private MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener()
  {
    public boolean onInfo(MediaPlayer paramAnonymousMediaPlayer, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (mOnInfoListener != null) {
        mOnInfoListener.onInfo(paramAnonymousMediaPlayer, paramAnonymousInt1, paramAnonymousInt2);
      }
      return true;
    }
  };
  private MediaController mMediaController;
  private MediaPlayer mMediaPlayer = null;
  private MediaPlayer.OnCompletionListener mOnCompletionListener;
  private MediaPlayer.OnErrorListener mOnErrorListener;
  private MediaPlayer.OnInfoListener mOnInfoListener;
  private MediaPlayer.OnPreparedListener mOnPreparedListener;
  private final Vector<Pair<InputStream, MediaFormat>> mPendingSubtitleTracks = new Vector();
  MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener()
  {
    public void onPrepared(MediaPlayer paramAnonymousMediaPlayer)
    {
      VideoView.access$202(VideoView.this, 2);
      Metadata localMetadata = paramAnonymousMediaPlayer.getMetadata(false, false);
      if (localMetadata != null)
      {
        VideoView localVideoView = VideoView.this;
        boolean bool;
        if ((localMetadata.has(1)) && (!localMetadata.getBoolean(1))) {
          bool = false;
        } else {
          bool = true;
        }
        VideoView.access$302(localVideoView, bool);
        localVideoView = VideoView.this;
        if ((localMetadata.has(2)) && (!localMetadata.getBoolean(2))) {
          bool = false;
        } else {
          bool = true;
        }
        VideoView.access$402(localVideoView, bool);
        localVideoView = VideoView.this;
        if ((localMetadata.has(3)) && (!localMetadata.getBoolean(3))) {
          bool = false;
        } else {
          bool = true;
        }
        VideoView.access$502(localVideoView, bool);
      }
      else
      {
        VideoView.access$302(VideoView.this, VideoView.access$402(VideoView.this, VideoView.access$502(VideoView.this, true)));
      }
      if (mOnPreparedListener != null) {
        mOnPreparedListener.onPrepared(mMediaPlayer);
      }
      if (mMediaController != null) {
        mMediaController.setEnabled(true);
      }
      VideoView.access$002(VideoView.this, paramAnonymousMediaPlayer.getVideoWidth());
      VideoView.access$102(VideoView.this, paramAnonymousMediaPlayer.getVideoHeight());
      int i = mSeekWhenPrepared;
      if (i != 0) {
        seekTo(i);
      }
      if ((mVideoWidth != 0) && (mVideoHeight != 0))
      {
        getHolder().setFixedSize(mVideoWidth, mVideoHeight);
        if ((mSurfaceWidth == mVideoWidth) && (mSurfaceHeight == mVideoHeight)) {
          if (mTargetState == 3)
          {
            start();
            if (mMediaController != null) {
              mMediaController.show();
            }
          }
          else if ((!isPlaying()) && ((i != 0) || (getCurrentPosition() > 0)) && (mMediaController != null))
          {
            mMediaController.show(0);
          }
        }
      }
      else if (mTargetState == 3)
      {
        start();
      }
    }
  };
  SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback()
  {
    public void surfaceChanged(SurfaceHolder paramAnonymousSurfaceHolder, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      VideoView.access$1002(VideoView.this, paramAnonymousInt2);
      VideoView.access$1102(VideoView.this, paramAnonymousInt3);
      paramAnonymousInt1 = mTargetState;
      int i = 0;
      if (paramAnonymousInt1 == 3) {
        paramAnonymousInt1 = 1;
      } else {
        paramAnonymousInt1 = 0;
      }
      int j = i;
      if (mVideoWidth == paramAnonymousInt2)
      {
        j = i;
        if (mVideoHeight == paramAnonymousInt3) {
          j = 1;
        }
      }
      if ((mMediaPlayer != null) && (paramAnonymousInt1 != 0) && (j != 0))
      {
        if (mSeekWhenPrepared != 0) {
          seekTo(mSeekWhenPrepared);
        }
        start();
      }
    }
    
    public void surfaceCreated(SurfaceHolder paramAnonymousSurfaceHolder)
    {
      VideoView.access$2102(VideoView.this, paramAnonymousSurfaceHolder);
      VideoView.this.openVideo();
    }
    
    public void surfaceDestroyed(SurfaceHolder paramAnonymousSurfaceHolder)
    {
      VideoView.access$2102(VideoView.this, null);
      if (mMediaController != null) {
        mMediaController.hide();
      }
      VideoView.this.release(true);
    }
  };
  private int mSeekWhenPrepared;
  MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener()
  {
    public void onVideoSizeChanged(MediaPlayer paramAnonymousMediaPlayer, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      VideoView.access$002(VideoView.this, paramAnonymousMediaPlayer.getVideoWidth());
      VideoView.access$102(VideoView.this, paramAnonymousMediaPlayer.getVideoHeight());
      if ((mVideoWidth != 0) && (mVideoHeight != 0))
      {
        getHolder().setFixedSize(mVideoWidth, mVideoHeight);
        requestLayout();
      }
    }
  };
  private SubtitleTrack.RenderingWidget mSubtitleWidget;
  private SubtitleTrack.RenderingWidget.OnChangedListener mSubtitlesChangedListener;
  private int mSurfaceHeight;
  private SurfaceHolder mSurfaceHolder = null;
  private int mSurfaceWidth;
  private int mTargetState = 0;
  private Uri mUri;
  private int mVideoHeight = 0;
  private int mVideoWidth = 0;
  
  public VideoView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public VideoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public VideoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public VideoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    getHolder().addCallback(mSHCallback);
    getHolder().setType(3);
    setFocusable(true);
    setFocusableInTouchMode(true);
    requestFocus();
    mCurrentState = 0;
    mTargetState = 0;
  }
  
  private void attachMediaController()
  {
    if ((mMediaPlayer != null) && (mMediaController != null))
    {
      mMediaController.setMediaPlayer(this);
      Object localObject;
      if ((getParent() instanceof View)) {
        localObject = (View)getParent();
      } else {
        localObject = this;
      }
      mMediaController.setAnchorView((View)localObject);
      mMediaController.setEnabled(isInPlaybackState());
    }
  }
  
  private boolean isInPlaybackState()
  {
    MediaPlayer localMediaPlayer = mMediaPlayer;
    boolean bool = true;
    if ((localMediaPlayer == null) || (mCurrentState == -1) || (mCurrentState == 0) || (mCurrentState == 1)) {
      bool = false;
    }
    return bool;
  }
  
  private void measureAndLayoutSubtitleWidget()
  {
    int i = getWidth();
    int j = getPaddingLeft();
    int k = getPaddingRight();
    int m = getHeight();
    int n = getPaddingTop();
    int i1 = getPaddingBottom();
    mSubtitleWidget.setSize(i - j - k, m - n - i1);
  }
  
  /* Error */
  private void openVideo()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 344	android/widget/VideoView:mUri	Landroid/net/Uri;
    //   4: ifnull +550 -> 554
    //   7: aload_0
    //   8: getfield 125	android/widget/VideoView:mSurfaceHolder	Landroid/view/SurfaceHolder;
    //   11: ifnonnull +6 -> 17
    //   14: goto +540 -> 554
    //   17: aload_0
    //   18: iconst_0
    //   19: invokespecial 262	android/widget/VideoView:release	(Z)V
    //   22: aload_0
    //   23: getfield 129	android/widget/VideoView:mAudioFocusType	I
    //   26: ifeq +21 -> 47
    //   29: aload_0
    //   30: getfield 172	android/widget/VideoView:mAudioManager	Landroid/media/AudioManager;
    //   33: aconst_null
    //   34: aload_0
    //   35: getfield 188	android/widget/VideoView:mAudioAttributes	Landroid/media/AudioAttributes;
    //   38: aload_0
    //   39: getfield 129	android/widget/VideoView:mAudioFocusType	I
    //   42: iconst_0
    //   43: invokevirtual 348	android/media/AudioManager:requestAudioFocus	(Landroid/media/AudioManager$OnAudioFocusChangeListener;Landroid/media/AudioAttributes;II)I
    //   46: pop
    //   47: new 350	android/media/MediaPlayer
    //   50: astore_1
    //   51: aload_1
    //   52: invokespecial 351	android/media/MediaPlayer:<init>	()V
    //   55: aload_0
    //   56: aload_1
    //   57: putfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   60: aload_0
    //   61: invokevirtual 355	android/widget/VideoView:getContext	()Landroid/content/Context;
    //   64: astore_1
    //   65: new 357	android/media/SubtitleController
    //   68: astore_2
    //   69: aload_2
    //   70: aload_1
    //   71: aload_0
    //   72: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   75: invokevirtual 361	android/media/MediaPlayer:getMediaTimeProvider	()Landroid/media/MediaTimeProvider;
    //   78: aload_0
    //   79: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   82: invokespecial 364	android/media/SubtitleController:<init>	(Landroid/content/Context;Landroid/media/MediaTimeProvider;Landroid/media/SubtitleController$Listener;)V
    //   85: new 366	android/media/WebVttRenderer
    //   88: astore_3
    //   89: aload_3
    //   90: aload_1
    //   91: invokespecial 368	android/media/WebVttRenderer:<init>	(Landroid/content/Context;)V
    //   94: aload_2
    //   95: aload_3
    //   96: invokevirtual 372	android/media/SubtitleController:registerRenderer	(Landroid/media/SubtitleController$Renderer;)V
    //   99: new 374	android/media/TtmlRenderer
    //   102: astore_3
    //   103: aload_3
    //   104: aload_1
    //   105: invokespecial 375	android/media/TtmlRenderer:<init>	(Landroid/content/Context;)V
    //   108: aload_2
    //   109: aload_3
    //   110: invokevirtual 372	android/media/SubtitleController:registerRenderer	(Landroid/media/SubtitleController$Renderer;)V
    //   113: new 377	android/media/Cea708CaptionRenderer
    //   116: astore_3
    //   117: aload_3
    //   118: aload_1
    //   119: invokespecial 378	android/media/Cea708CaptionRenderer:<init>	(Landroid/content/Context;)V
    //   122: aload_2
    //   123: aload_3
    //   124: invokevirtual 372	android/media/SubtitleController:registerRenderer	(Landroid/media/SubtitleController$Renderer;)V
    //   127: new 380	android/media/ClosedCaptionRenderer
    //   130: astore_3
    //   131: aload_3
    //   132: aload_1
    //   133: invokespecial 381	android/media/ClosedCaptionRenderer:<init>	(Landroid/content/Context;)V
    //   136: aload_2
    //   137: aload_3
    //   138: invokevirtual 372	android/media/SubtitleController:registerRenderer	(Landroid/media/SubtitleController$Renderer;)V
    //   141: aload_0
    //   142: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   145: aload_2
    //   146: aload_0
    //   147: invokevirtual 385	android/media/MediaPlayer:setSubtitleAnchor	(Landroid/media/SubtitleController;Landroid/media/SubtitleController$Anchor;)V
    //   150: aload_0
    //   151: getfield 387	android/widget/VideoView:mAudioSession	I
    //   154: ifeq +17 -> 171
    //   157: aload_0
    //   158: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   161: aload_0
    //   162: getfield 387	android/widget/VideoView:mAudioSession	I
    //   165: invokevirtual 390	android/media/MediaPlayer:setAudioSessionId	(I)V
    //   168: goto +14 -> 182
    //   171: aload_0
    //   172: aload_0
    //   173: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   176: invokevirtual 393	android/media/MediaPlayer:getAudioSessionId	()I
    //   179: putfield 387	android/widget/VideoView:mAudioSession	I
    //   182: aload_0
    //   183: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   186: aload_0
    //   187: getfield 137	android/widget/VideoView:mPreparedListener	Landroid/media/MediaPlayer$OnPreparedListener;
    //   190: invokevirtual 397	android/media/MediaPlayer:setOnPreparedListener	(Landroid/media/MediaPlayer$OnPreparedListener;)V
    //   193: aload_0
    //   194: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   197: aload_0
    //   198: getfield 134	android/widget/VideoView:mSizeChangedListener	Landroid/media/MediaPlayer$OnVideoSizeChangedListener;
    //   201: invokevirtual 401	android/media/MediaPlayer:setOnVideoSizeChangedListener	(Landroid/media/MediaPlayer$OnVideoSizeChangedListener;)V
    //   204: aload_0
    //   205: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   208: aload_0
    //   209: getfield 140	android/widget/VideoView:mCompletionListener	Landroid/media/MediaPlayer$OnCompletionListener;
    //   212: invokevirtual 405	android/media/MediaPlayer:setOnCompletionListener	(Landroid/media/MediaPlayer$OnCompletionListener;)V
    //   215: aload_0
    //   216: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   219: aload_0
    //   220: getfield 146	android/widget/VideoView:mErrorListener	Landroid/media/MediaPlayer$OnErrorListener;
    //   223: invokevirtual 409	android/media/MediaPlayer:setOnErrorListener	(Landroid/media/MediaPlayer$OnErrorListener;)V
    //   226: aload_0
    //   227: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   230: aload_0
    //   231: getfield 143	android/widget/VideoView:mInfoListener	Landroid/media/MediaPlayer$OnInfoListener;
    //   234: invokevirtual 413	android/media/MediaPlayer:setOnInfoListener	(Landroid/media/MediaPlayer$OnInfoListener;)V
    //   237: aload_0
    //   238: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   241: aload_0
    //   242: getfield 149	android/widget/VideoView:mBufferingUpdateListener	Landroid/media/MediaPlayer$OnBufferingUpdateListener;
    //   245: invokevirtual 417	android/media/MediaPlayer:setOnBufferingUpdateListener	(Landroid/media/MediaPlayer$OnBufferingUpdateListener;)V
    //   248: aload_0
    //   249: iconst_0
    //   250: putfield 250	android/widget/VideoView:mCurrentBufferPercentage	I
    //   253: aload_0
    //   254: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   257: aload_0
    //   258: getfield 160	android/widget/VideoView:mContext	Landroid/content/Context;
    //   261: aload_0
    //   262: getfield 344	android/widget/VideoView:mUri	Landroid/net/Uri;
    //   265: aload_0
    //   266: getfield 419	android/widget/VideoView:mHeaders	Ljava/util/Map;
    //   269: invokevirtual 423	android/media/MediaPlayer:setDataSource	(Landroid/content/Context;Landroid/net/Uri;Ljava/util/Map;)V
    //   272: aload_0
    //   273: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   276: aload_0
    //   277: getfield 125	android/widget/VideoView:mSurfaceHolder	Landroid/view/SurfaceHolder;
    //   280: invokevirtual 427	android/media/MediaPlayer:setDisplay	(Landroid/view/SurfaceHolder;)V
    //   283: aload_0
    //   284: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   287: aload_0
    //   288: getfield 188	android/widget/VideoView:mAudioAttributes	Landroid/media/AudioAttributes;
    //   291: invokevirtual 431	android/media/MediaPlayer:setAudioAttributes	(Landroid/media/AudioAttributes;)V
    //   294: aload_0
    //   295: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   298: iconst_1
    //   299: invokevirtual 434	android/media/MediaPlayer:setScreenOnWhilePlaying	(Z)V
    //   302: aload_0
    //   303: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   306: invokevirtual 437	android/media/MediaPlayer:prepareAsync	()V
    //   309: aload_0
    //   310: getfield 119	android/widget/VideoView:mPendingSubtitleTracks	Ljava/util/Vector;
    //   313: invokevirtual 441	java/util/Vector:iterator	()Ljava/util/Iterator;
    //   316: astore_1
    //   317: aload_1
    //   318: invokeinterface 446 1 0
    //   323: ifeq +59 -> 382
    //   326: aload_1
    //   327: invokeinterface 450 1 0
    //   332: checkcast 452	android/util/Pair
    //   335: astore_2
    //   336: aload_0
    //   337: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   340: aload_2
    //   341: getfield 456	android/util/Pair:first	Ljava/lang/Object;
    //   344: checkcast 458	java/io/InputStream
    //   347: aload_2
    //   348: getfield 461	android/util/Pair:second	Ljava/lang/Object;
    //   351: checkcast 463	android/media/MediaFormat
    //   354: invokevirtual 467	android/media/MediaPlayer:addSubtitleSource	(Ljava/io/InputStream;Landroid/media/MediaFormat;)V
    //   357: goto +22 -> 379
    //   360: astore_2
    //   361: aload_0
    //   362: getfield 143	android/widget/VideoView:mInfoListener	Landroid/media/MediaPlayer$OnInfoListener;
    //   365: aload_0
    //   366: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   369: sipush 901
    //   372: iconst_0
    //   373: invokeinterface 473 4 0
    //   378: pop
    //   379: goto -62 -> 317
    //   382: aload_0
    //   383: iconst_1
    //   384: putfield 121	android/widget/VideoView:mCurrentState	I
    //   387: aload_0
    //   388: invokespecial 475	android/widget/VideoView:attachMediaController	()V
    //   391: aload_0
    //   392: getfield 119	android/widget/VideoView:mPendingSubtitleTracks	Ljava/util/Vector;
    //   395: invokevirtual 478	java/util/Vector:clear	()V
    //   398: return
    //   399: astore_1
    //   400: goto +145 -> 545
    //   403: astore_1
    //   404: new 480	java/lang/StringBuilder
    //   407: astore_2
    //   408: aload_2
    //   409: invokespecial 481	java/lang/StringBuilder:<init>	()V
    //   412: aload_2
    //   413: ldc_w 483
    //   416: invokevirtual 487	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   419: pop
    //   420: aload_2
    //   421: aload_0
    //   422: getfield 344	android/widget/VideoView:mUri	Landroid/net/Uri;
    //   425: invokevirtual 490	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   428: pop
    //   429: ldc 45
    //   431: aload_2
    //   432: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   435: aload_1
    //   436: invokestatic 500	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   439: pop
    //   440: aload_0
    //   441: iconst_m1
    //   442: putfield 121	android/widget/VideoView:mCurrentState	I
    //   445: aload_0
    //   446: iconst_m1
    //   447: putfield 123	android/widget/VideoView:mTargetState	I
    //   450: aload_0
    //   451: getfield 146	android/widget/VideoView:mErrorListener	Landroid/media/MediaPlayer$OnErrorListener;
    //   454: aload_0
    //   455: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   458: iconst_1
    //   459: iconst_0
    //   460: invokeinterface 505 4 0
    //   465: pop
    //   466: aload_0
    //   467: getfield 119	android/widget/VideoView:mPendingSubtitleTracks	Ljava/util/Vector;
    //   470: invokevirtual 478	java/util/Vector:clear	()V
    //   473: return
    //   474: astore_1
    //   475: new 480	java/lang/StringBuilder
    //   478: astore_2
    //   479: aload_2
    //   480: invokespecial 481	java/lang/StringBuilder:<init>	()V
    //   483: aload_2
    //   484: ldc_w 483
    //   487: invokevirtual 487	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   490: pop
    //   491: aload_2
    //   492: aload_0
    //   493: getfield 344	android/widget/VideoView:mUri	Landroid/net/Uri;
    //   496: invokevirtual 490	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   499: pop
    //   500: ldc 45
    //   502: aload_2
    //   503: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   506: aload_1
    //   507: invokestatic 500	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   510: pop
    //   511: aload_0
    //   512: iconst_m1
    //   513: putfield 121	android/widget/VideoView:mCurrentState	I
    //   516: aload_0
    //   517: iconst_m1
    //   518: putfield 123	android/widget/VideoView:mTargetState	I
    //   521: aload_0
    //   522: getfield 146	android/widget/VideoView:mErrorListener	Landroid/media/MediaPlayer$OnErrorListener;
    //   525: aload_0
    //   526: getfield 127	android/widget/VideoView:mMediaPlayer	Landroid/media/MediaPlayer;
    //   529: iconst_1
    //   530: iconst_0
    //   531: invokeinterface 505 4 0
    //   536: pop
    //   537: aload_0
    //   538: getfield 119	android/widget/VideoView:mPendingSubtitleTracks	Ljava/util/Vector;
    //   541: invokevirtual 478	java/util/Vector:clear	()V
    //   544: return
    //   545: aload_0
    //   546: getfield 119	android/widget/VideoView:mPendingSubtitleTracks	Ljava/util/Vector;
    //   549: invokevirtual 478	java/util/Vector:clear	()V
    //   552: aload_1
    //   553: athrow
    //   554: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	555	0	this	VideoView
    //   50	277	1	localObject1	Object
    //   399	1	1	localObject2	Object
    //   403	33	1	localIllegalArgumentException	IllegalArgumentException
    //   474	79	1	localIOException	java.io.IOException
    //   68	280	2	localObject3	Object
    //   360	1	2	localIllegalStateException	IllegalStateException
    //   407	96	2	localStringBuilder	StringBuilder
    //   88	50	3	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   336	357	360	java/lang/IllegalStateException
    //   47	168	399	finally
    //   171	182	399	finally
    //   182	317	399	finally
    //   317	336	399	finally
    //   336	357	399	finally
    //   361	379	399	finally
    //   382	391	399	finally
    //   404	466	399	finally
    //   475	537	399	finally
    //   47	168	403	java/lang/IllegalArgumentException
    //   171	182	403	java/lang/IllegalArgumentException
    //   182	317	403	java/lang/IllegalArgumentException
    //   317	336	403	java/lang/IllegalArgumentException
    //   336	357	403	java/lang/IllegalArgumentException
    //   361	379	403	java/lang/IllegalArgumentException
    //   382	391	403	java/lang/IllegalArgumentException
    //   47	168	474	java/io/IOException
    //   171	182	474	java/io/IOException
    //   182	317	474	java/io/IOException
    //   317	336	474	java/io/IOException
    //   336	357	474	java/io/IOException
    //   361	379	474	java/io/IOException
    //   382	391	474	java/io/IOException
  }
  
  private void release(boolean paramBoolean)
  {
    if (mMediaPlayer != null)
    {
      mMediaPlayer.reset();
      mMediaPlayer.release();
      mMediaPlayer = null;
      mPendingSubtitleTracks.clear();
      mCurrentState = 0;
      if (paramBoolean) {
        mTargetState = 0;
      }
      if (mAudioFocusType != 0) {
        mAudioManager.abandonAudioFocus(null);
      }
    }
  }
  
  private void toggleMediaControlsVisiblity()
  {
    if (mMediaController.isShowing()) {
      mMediaController.hide();
    } else {
      mMediaController.show();
    }
  }
  
  public void addSubtitleSource(InputStream paramInputStream, MediaFormat paramMediaFormat)
  {
    if (mMediaPlayer == null) {
      mPendingSubtitleTracks.add(Pair.create(paramInputStream, paramMediaFormat));
    } else {
      try
      {
        mMediaPlayer.addSubtitleSource(paramInputStream, paramMediaFormat);
      }
      catch (IllegalStateException paramInputStream)
      {
        mInfoListener.onInfo(mMediaPlayer, 901, 0);
      }
    }
  }
  
  public boolean canPause()
  {
    return mCanPause;
  }
  
  public boolean canSeekBackward()
  {
    return mCanSeekBack;
  }
  
  public boolean canSeekForward()
  {
    return mCanSeekForward;
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    if (mSubtitleWidget != null)
    {
      int i = paramCanvas.save();
      paramCanvas.translate(getPaddingLeft(), getPaddingTop());
      mSubtitleWidget.draw(paramCanvas);
      paramCanvas.restoreToCount(i);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return VideoView.class.getName();
  }
  
  public int getAudioSessionId()
  {
    if (mAudioSession == 0)
    {
      MediaPlayer localMediaPlayer = new MediaPlayer();
      mAudioSession = localMediaPlayer.getAudioSessionId();
      localMediaPlayer.release();
    }
    return mAudioSession;
  }
  
  public int getBufferPercentage()
  {
    if (mMediaPlayer != null) {
      return mCurrentBufferPercentage;
    }
    return 0;
  }
  
  public int getCurrentPosition()
  {
    if (isInPlaybackState()) {
      return mMediaPlayer.getCurrentPosition();
    }
    return 0;
  }
  
  public int getDuration()
  {
    if (isInPlaybackState()) {
      return mMediaPlayer.getDuration();
    }
    return -1;
  }
  
  public Looper getSubtitleLooper()
  {
    return Looper.getMainLooper();
  }
  
  public boolean isPlaying()
  {
    boolean bool;
    if ((isInPlaybackState()) && (mMediaPlayer.isPlaying())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (mSubtitleWidget != null) {
      mSubtitleWidget.onAttachedToWindow();
    }
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (mSubtitleWidget != null) {
      mSubtitleWidget.onDetachedFromWindow();
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    int i;
    if ((paramInt != 4) && (paramInt != 24) && (paramInt != 25) && (paramInt != 164) && (paramInt != 82) && (paramInt != 5) && (paramInt != 6)) {
      i = 1;
    } else {
      i = 0;
    }
    if ((isInPlaybackState()) && (i != 0) && (mMediaController != null)) {
      if ((paramInt != 79) && (paramInt != 85))
      {
        if (paramInt == 126)
        {
          if (!mMediaPlayer.isPlaying())
          {
            start();
            mMediaController.hide();
          }
          return true;
        }
        if ((paramInt != 86) && (paramInt != 127))
        {
          toggleMediaControlsVisiblity();
        }
        else
        {
          if (mMediaPlayer.isPlaying())
          {
            pause();
            mMediaController.show();
          }
          return true;
        }
      }
      else
      {
        if (mMediaPlayer.isPlaying())
        {
          pause();
          mMediaController.show();
        }
        else
        {
          start();
          mMediaController.hide();
        }
        return true;
      }
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (mSubtitleWidget != null) {
      measureAndLayoutSubtitleWidget();
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getDefaultSize(mVideoWidth, paramInt1);
    int j = getDefaultSize(mVideoHeight, paramInt2);
    int k = i;
    int m = j;
    if (mVideoWidth > 0)
    {
      k = i;
      m = j;
      if (mVideoHeight > 0)
      {
        int n = View.MeasureSpec.getMode(paramInt1);
        paramInt1 = View.MeasureSpec.getSize(paramInt1);
        int i1 = View.MeasureSpec.getMode(paramInt2);
        paramInt2 = View.MeasureSpec.getSize(paramInt2);
        if ((n == 1073741824) && (i1 == 1073741824))
        {
          if (mVideoWidth * paramInt2 < mVideoHeight * paramInt1)
          {
            k = mVideoWidth * paramInt2 / mVideoHeight;
            m = paramInt2;
          }
          else
          {
            k = paramInt1;
            m = paramInt2;
            if (mVideoWidth * paramInt2 > mVideoHeight * paramInt1)
            {
              m = mVideoHeight * paramInt1 / mVideoWidth;
              k = paramInt1;
            }
          }
        }
        else if (n == 1073741824)
        {
          j = mVideoHeight * paramInt1 / mVideoWidth;
          k = paramInt1;
          m = j;
          if (i1 == Integer.MIN_VALUE)
          {
            k = paramInt1;
            m = j;
            if (j > paramInt2)
            {
              k = paramInt1;
              m = paramInt2;
            }
          }
        }
        else if (i1 == 1073741824)
        {
          j = mVideoWidth * paramInt2 / mVideoHeight;
          k = j;
          m = paramInt2;
          if (n == Integer.MIN_VALUE)
          {
            k = j;
            m = paramInt2;
            if (j > paramInt1)
            {
              k = paramInt1;
              m = paramInt2;
            }
          }
        }
        else
        {
          k = mVideoWidth;
          m = mVideoHeight;
          i = k;
          j = m;
          if (i1 == Integer.MIN_VALUE)
          {
            i = k;
            j = m;
            if (m > paramInt2)
            {
              i = mVideoWidth * paramInt2 / mVideoHeight;
              j = paramInt2;
            }
          }
          k = i;
          m = j;
          if (n == Integer.MIN_VALUE)
          {
            k = i;
            m = j;
            if (i > paramInt1)
            {
              m = mVideoHeight * paramInt1 / mVideoWidth;
              k = paramInt1;
            }
          }
        }
      }
    }
    setMeasuredDimension(k, m);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((paramMotionEvent.getAction() == 0) && (isInPlaybackState()) && (mMediaController != null)) {
      toggleMediaControlsVisiblity();
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent)
  {
    if ((paramMotionEvent.getAction() == 0) && (isInPlaybackState()) && (mMediaController != null)) {
      toggleMediaControlsVisiblity();
    }
    return super.onTrackballEvent(paramMotionEvent);
  }
  
  public void pause()
  {
    if ((isInPlaybackState()) && (mMediaPlayer.isPlaying()))
    {
      mMediaPlayer.pause();
      mCurrentState = 4;
    }
    mTargetState = 4;
  }
  
  public int resolveAdjustedSize(int paramInt1, int paramInt2)
  {
    return getDefaultSize(paramInt1, paramInt2);
  }
  
  public void resume()
  {
    openVideo();
  }
  
  public void seekTo(int paramInt)
  {
    if (isInPlaybackState())
    {
      mMediaPlayer.seekTo(paramInt);
      mSeekWhenPrepared = 0;
    }
    else
    {
      mSeekWhenPrepared = paramInt;
    }
  }
  
  public void setAudioAttributes(AudioAttributes paramAudioAttributes)
  {
    if (paramAudioAttributes != null)
    {
      mAudioAttributes = paramAudioAttributes;
      return;
    }
    throw new IllegalArgumentException("Illegal null AudioAttributes");
  }
  
  public void setAudioFocusRequest(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2) && (paramInt != 3) && (paramInt != 4))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Illegal audio focus type ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    mAudioFocusType = paramInt;
  }
  
  public void setMediaController(MediaController paramMediaController)
  {
    if (mMediaController != null) {
      mMediaController.hide();
    }
    mMediaController = paramMediaController;
    attachMediaController();
  }
  
  public void setOnCompletionListener(MediaPlayer.OnCompletionListener paramOnCompletionListener)
  {
    mOnCompletionListener = paramOnCompletionListener;
  }
  
  public void setOnErrorListener(MediaPlayer.OnErrorListener paramOnErrorListener)
  {
    mOnErrorListener = paramOnErrorListener;
  }
  
  public void setOnInfoListener(MediaPlayer.OnInfoListener paramOnInfoListener)
  {
    mOnInfoListener = paramOnInfoListener;
  }
  
  public void setOnPreparedListener(MediaPlayer.OnPreparedListener paramOnPreparedListener)
  {
    mOnPreparedListener = paramOnPreparedListener;
  }
  
  public void setSubtitleWidget(SubtitleTrack.RenderingWidget paramRenderingWidget)
  {
    if (mSubtitleWidget == paramRenderingWidget) {
      return;
    }
    boolean bool = isAttachedToWindow();
    if (mSubtitleWidget != null)
    {
      if (bool) {
        mSubtitleWidget.onDetachedFromWindow();
      }
      mSubtitleWidget.setOnChangedListener(null);
    }
    mSubtitleWidget = paramRenderingWidget;
    if (paramRenderingWidget != null)
    {
      if (mSubtitlesChangedListener == null) {
        mSubtitlesChangedListener = new SubtitleTrack.RenderingWidget.OnChangedListener()
        {
          public void onChanged(SubtitleTrack.RenderingWidget paramAnonymousRenderingWidget)
          {
            invalidate();
          }
        };
      }
      setWillNotDraw(false);
      paramRenderingWidget.setOnChangedListener(mSubtitlesChangedListener);
      if (bool)
      {
        paramRenderingWidget.onAttachedToWindow();
        requestLayout();
      }
    }
    else
    {
      setWillNotDraw(true);
    }
    invalidate();
  }
  
  public void setVideoPath(String paramString)
  {
    setVideoURI(Uri.parse(paramString));
  }
  
  public void setVideoURI(Uri paramUri)
  {
    setVideoURI(paramUri, null);
  }
  
  public void setVideoURI(Uri paramUri, Map<String, String> paramMap)
  {
    mUri = paramUri;
    mHeaders = paramMap;
    mSeekWhenPrepared = 0;
    openVideo();
    requestLayout();
    invalidate();
  }
  
  public void start()
  {
    if (isInPlaybackState())
    {
      mMediaPlayer.start();
      mCurrentState = 3;
    }
    mTargetState = 3;
  }
  
  public void stopPlayback()
  {
    if (mMediaPlayer != null)
    {
      mMediaPlayer.stop();
      mMediaPlayer.release();
      mMediaPlayer = null;
      mCurrentState = 0;
      mTargetState = 0;
      mAudioManager.abandonAudioFocus(null);
    }
  }
  
  public void suspend()
  {
    release(false);
  }
}
