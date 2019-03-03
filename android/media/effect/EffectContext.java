package android.media.effect;

import android.filterfw.core.CachedFrameManager;
import android.filterfw.core.FilterContext;
import android.filterfw.core.GLEnvironment;
import android.opengl.GLES20;

public class EffectContext
{
  private final int GL_STATE_ARRAYBUFFER = 2;
  private final int GL_STATE_COUNT = 3;
  private final int GL_STATE_FBO = 0;
  private final int GL_STATE_PROGRAM = 1;
  private EffectFactory mFactory;
  FilterContext mFilterContext = new FilterContext();
  private int[] mOldState = new int[3];
  
  private EffectContext()
  {
    mFilterContext.setFrameManager(new CachedFrameManager());
    mFactory = new EffectFactory(this);
  }
  
  public static EffectContext createWithCurrentGlContext()
  {
    EffectContext localEffectContext = new EffectContext();
    localEffectContext.initInCurrentGlContext();
    return localEffectContext;
  }
  
  private void initInCurrentGlContext()
  {
    if (GLEnvironment.isAnyContextActive())
    {
      GLEnvironment localGLEnvironment = new GLEnvironment();
      localGLEnvironment.initWithCurrentContext();
      mFilterContext.initGLEnvironment(localGLEnvironment);
      return;
    }
    throw new RuntimeException("Attempting to initialize EffectContext with no active GL context!");
  }
  
  final void assertValidGLState()
  {
    GLEnvironment localGLEnvironment = mFilterContext.getGLEnvironment();
    if ((localGLEnvironment != null) && (localGLEnvironment.isContextActive())) {
      return;
    }
    if (GLEnvironment.isAnyContextActive()) {
      throw new RuntimeException("Applying effect in wrong GL context!");
    }
    throw new RuntimeException("Attempting to apply effect without valid GL context!");
  }
  
  public EffectFactory getFactory()
  {
    return mFactory;
  }
  
  public void release()
  {
    mFilterContext.tearDown();
    mFilterContext = null;
  }
  
  final void restoreGLState()
  {
    GLES20.glBindFramebuffer(36160, mOldState[0]);
    GLES20.glUseProgram(mOldState[1]);
    GLES20.glBindBuffer(34962, mOldState[2]);
  }
  
  final void saveGLState()
  {
    GLES20.glGetIntegerv(36006, mOldState, 0);
    GLES20.glGetIntegerv(35725, mOldState, 1);
    GLES20.glGetIntegerv(34964, mOldState, 2);
  }
}
