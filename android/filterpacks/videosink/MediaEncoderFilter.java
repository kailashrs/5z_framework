package android.filterpacks.videosink;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GLEnvironment;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.IOException;

public class MediaEncoderFilter
  extends Filter
{
  private static final int NO_AUDIO_SOURCE = -1;
  private static final String TAG = "MediaEncoderFilter";
  @GenerateFieldPort(hasDefault=true, name="audioSource")
  private int mAudioSource = -1;
  private boolean mCaptureTimeLapse = false;
  @GenerateFieldPort(hasDefault=true, name="errorListener")
  private MediaRecorder.OnErrorListener mErrorListener = null;
  @GenerateFieldPort(hasDefault=true, name="outputFileDescriptor")
  private FileDescriptor mFd = null;
  @GenerateFieldPort(hasDefault=true, name="framerate")
  private int mFps = 30;
  @GenerateFieldPort(hasDefault=true, name="height")
  private int mHeight = 0;
  @GenerateFieldPort(hasDefault=true, name="infoListener")
  private MediaRecorder.OnInfoListener mInfoListener = null;
  private long mLastTimeLapseFrameRealTimestampNs = 0L;
  private boolean mLogVerbose = Log.isLoggable("MediaEncoderFilter", 2);
  @GenerateFieldPort(hasDefault=true, name="maxDurationMs")
  private int mMaxDurationMs = 0;
  @GenerateFieldPort(hasDefault=true, name="maxFileSize")
  private long mMaxFileSize = 0L;
  private MediaRecorder mMediaRecorder;
  private int mNumFramesEncoded = 0;
  @GenerateFieldPort(hasDefault=true, name="orientationHint")
  private int mOrientationHint = 0;
  @GenerateFieldPort(hasDefault=true, name="outputFile")
  private String mOutputFile = new String("/sdcard/MediaEncoderOut.mp4");
  @GenerateFieldPort(hasDefault=true, name="outputFormat")
  private int mOutputFormat = 2;
  @GenerateFieldPort(hasDefault=true, name="recordingProfile")
  private CamcorderProfile mProfile = null;
  private ShaderProgram mProgram;
  @GenerateFieldPort(hasDefault=true, name="recording")
  private boolean mRecording = true;
  private boolean mRecordingActive = false;
  @GenerateFieldPort(hasDefault=true, name="recordingDoneListener")
  private OnRecordingDoneListener mRecordingDoneListener = null;
  private GLFrame mScreen;
  @GenerateFieldPort(hasDefault=true, name="inputRegion")
  private Quad mSourceRegion = new Quad(new Point(0.0F, 0.0F), new Point(1.0F, 0.0F), new Point(0.0F, 1.0F), new Point(1.0F, 1.0F));
  private int mSurfaceId;
  @GenerateFieldPort(hasDefault=true, name="timelapseRecordingIntervalUs")
  private long mTimeBetweenTimeLapseFrameCaptureUs = 0L;
  private long mTimestampNs = 0L;
  @GenerateFieldPort(hasDefault=true, name="videoEncoder")
  private int mVideoEncoder = 2;
  @GenerateFieldPort(hasDefault=true, name="width")
  private int mWidth = 0;
  
  public MediaEncoderFilter(String paramString)
  {
    super(paramString);
  }
  
  private void startRecording(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("MediaEncoderFilter", "Starting recording");
    }
    MutableFrameFormat localMutableFrameFormat = new MutableFrameFormat(2, 3);
    localMutableFrameFormat.setBytesPerSample(4);
    int i;
    if ((mWidth > 0) && (mHeight > 0)) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if ((mProfile != null) && (i == 0))
    {
      j = mProfile.videoFrameWidth;
      i = mProfile.videoFrameHeight;
    }
    else
    {
      j = mWidth;
      i = mHeight;
    }
    localMutableFrameFormat.setDimensions(j, i);
    mScreen = ((GLFrame)paramFilterContext.getFrameManager().newBoundFrame(localMutableFrameFormat, 101, 0L));
    mMediaRecorder = new MediaRecorder();
    updateMediaRecorderParams();
    try
    {
      mMediaRecorder.prepare();
      mMediaRecorder.start();
      if (mLogVerbose) {
        Log.v("MediaEncoderFilter", "Open: registering surface from Mediarecorder");
      }
      mSurfaceId = paramFilterContext.getGLEnvironment().registerSurfaceFromMediaRecorder(mMediaRecorder);
      mNumFramesEncoded = 0;
      mRecordingActive = true;
      return;
    }
    catch (Exception paramFilterContext)
    {
      throw new RuntimeException("Unknown Exception inMediaRecorder.prepare()!", paramFilterContext);
    }
    catch (IOException paramFilterContext)
    {
      throw new RuntimeException("IOException inMediaRecorder.prepare()!", paramFilterContext);
    }
    catch (IllegalStateException paramFilterContext)
    {
      throw paramFilterContext;
    }
  }
  
  private void stopRecording(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("MediaEncoderFilter", "Stopping recording");
    }
    mRecordingActive = false;
    mNumFramesEncoded = 0;
    paramFilterContext = paramFilterContext.getGLEnvironment();
    if (mLogVerbose) {
      Log.v("MediaEncoderFilter", String.format("Unregistering surface %d", new Object[] { Integer.valueOf(mSurfaceId) }));
    }
    paramFilterContext.unregisterSurfaceId(mSurfaceId);
    try
    {
      mMediaRecorder.stop();
      mMediaRecorder.release();
      mMediaRecorder = null;
      mScreen.release();
      mScreen = null;
      if (mRecordingDoneListener != null) {
        mRecordingDoneListener.onRecordingDone();
      }
      return;
    }
    catch (RuntimeException paramFilterContext)
    {
      throw new MediaRecorderStopException("MediaRecorder.stop() failed!", paramFilterContext);
    }
  }
  
  private void updateMediaRecorderParams()
  {
    boolean bool;
    if (mTimeBetweenTimeLapseFrameCaptureUs > 0L) {
      bool = true;
    } else {
      bool = false;
    }
    mCaptureTimeLapse = bool;
    mMediaRecorder.setVideoSource(2);
    if ((!mCaptureTimeLapse) && (mAudioSource != -1)) {
      mMediaRecorder.setAudioSource(mAudioSource);
    }
    if (mProfile != null)
    {
      mMediaRecorder.setProfile(mProfile);
      mFps = mProfile.videoFrameRate;
      if ((mWidth > 0) && (mHeight > 0)) {
        mMediaRecorder.setVideoSize(mWidth, mHeight);
      }
    }
    else
    {
      mMediaRecorder.setOutputFormat(mOutputFormat);
      mMediaRecorder.setVideoEncoder(mVideoEncoder);
      mMediaRecorder.setVideoSize(mWidth, mHeight);
      mMediaRecorder.setVideoFrameRate(mFps);
    }
    mMediaRecorder.setOrientationHint(mOrientationHint);
    mMediaRecorder.setOnInfoListener(mInfoListener);
    mMediaRecorder.setOnErrorListener(mErrorListener);
    if (mFd != null) {
      mMediaRecorder.setOutputFile(mFd);
    } else {
      mMediaRecorder.setOutputFile(mOutputFile);
    }
    try
    {
      mMediaRecorder.setMaxFileSize(mMaxFileSize);
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Setting maxFileSize on MediaRecorder unsuccessful! ");
      localStringBuilder.append(localException.getMessage());
      Log.w("MediaEncoderFilter", localStringBuilder.toString());
    }
    mMediaRecorder.setMaxDuration(mMaxDurationMs);
  }
  
  private void updateSourceRegion()
  {
    Quad localQuad = new Quad();
    p0 = mSourceRegion.p2;
    p1 = mSourceRegion.p3;
    p2 = mSourceRegion.p0;
    p3 = mSourceRegion.p1;
    mProgram.setSourceRegion(localQuad);
  }
  
  public void close(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("MediaEncoderFilter", "Closing");
    }
    if (mRecordingActive) {
      stopRecording(paramFilterContext);
    }
  }
  
  public void fieldPortValueUpdated(String paramString, FilterContext paramFilterContext)
  {
    if (mLogVerbose)
    {
      paramFilterContext = new StringBuilder();
      paramFilterContext.append("Port ");
      paramFilterContext.append(paramString);
      paramFilterContext.append(" has been updated");
      Log.v("MediaEncoderFilter", paramFilterContext.toString());
    }
    if (paramString.equals("recording")) {
      return;
    }
    if (paramString.equals("inputRegion"))
    {
      if (isOpen()) {
        updateSourceRegion();
      }
      return;
    }
    if ((isOpen()) && (mRecordingActive)) {
      throw new RuntimeException("Cannot change recording parameters when the filter is recording!");
    }
  }
  
  public void open(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("MediaEncoderFilter", "Opening");
    }
    updateSourceRegion();
    if (mRecording) {
      startRecording(paramFilterContext);
    }
  }
  
  public void prepare(FilterContext paramFilterContext)
  {
    if (mLogVerbose) {
      Log.v("MediaEncoderFilter", "Preparing");
    }
    mProgram = ShaderProgram.createIdentity(paramFilterContext);
    mRecordingActive = false;
  }
  
  public void process(FilterContext paramFilterContext)
  {
    GLEnvironment localGLEnvironment = paramFilterContext.getGLEnvironment();
    Frame localFrame = pullInput("videoframe");
    if ((!mRecordingActive) && (mRecording)) {
      startRecording(paramFilterContext);
    }
    if ((mRecordingActive) && (!mRecording)) {
      stopRecording(paramFilterContext);
    }
    if (!mRecordingActive) {
      return;
    }
    if (mCaptureTimeLapse)
    {
      if (!skipFrameAndModifyTimestamp(localFrame.getTimestamp())) {}
    }
    else {
      mTimestampNs = localFrame.getTimestamp();
    }
    localGLEnvironment.activateSurfaceWithId(mSurfaceId);
    mProgram.process(localFrame, mScreen);
    localGLEnvironment.setSurfaceTimestamp(mTimestampNs);
    localGLEnvironment.swapBuffers();
    mNumFramesEncoded += 1;
  }
  
  public void setupPorts()
  {
    addMaskedInputPort("videoframe", ImageFormat.create(3, 3));
  }
  
  public boolean skipFrameAndModifyTimestamp(long paramLong)
  {
    StringBuilder localStringBuilder;
    if (mNumFramesEncoded == 0)
    {
      mLastTimeLapseFrameRealTimestampNs = paramLong;
      mTimestampNs = paramLong;
      if (mLogVerbose)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("timelapse: FIRST frame, last real t= ");
        localStringBuilder.append(mLastTimeLapseFrameRealTimestampNs);
        localStringBuilder.append(", setting t = ");
        localStringBuilder.append(mTimestampNs);
        Log.v("MediaEncoderFilter", localStringBuilder.toString());
      }
      return false;
    }
    if ((mNumFramesEncoded >= 2) && (paramLong < mLastTimeLapseFrameRealTimestampNs + 1000L * mTimeBetweenTimeLapseFrameCaptureUs))
    {
      if (mLogVerbose) {
        Log.v("MediaEncoderFilter", "timelapse: skipping intermediate frame");
      }
      return true;
    }
    if (mLogVerbose)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("timelapse: encoding frame, Timestamp t = ");
      localStringBuilder.append(paramLong);
      localStringBuilder.append(", last real t= ");
      localStringBuilder.append(mLastTimeLapseFrameRealTimestampNs);
      localStringBuilder.append(", interval = ");
      localStringBuilder.append(mTimeBetweenTimeLapseFrameCaptureUs);
      Log.v("MediaEncoderFilter", localStringBuilder.toString());
    }
    mLastTimeLapseFrameRealTimestampNs = paramLong;
    mTimestampNs += 1000000000L / mFps;
    if (mLogVerbose)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("timelapse: encoding frame, setting t = ");
      localStringBuilder.append(mTimestampNs);
      localStringBuilder.append(", delta t = ");
      localStringBuilder.append(1000000000L / mFps);
      localStringBuilder.append(", fps = ");
      localStringBuilder.append(mFps);
      Log.v("MediaEncoderFilter", localStringBuilder.toString());
    }
    return false;
  }
  
  public void tearDown(FilterContext paramFilterContext)
  {
    if (mMediaRecorder != null) {
      mMediaRecorder.release();
    }
    if (mScreen != null) {
      mScreen.release();
    }
  }
  
  public static abstract interface OnRecordingDoneListener
  {
    public abstract void onRecordingDone();
  }
}
