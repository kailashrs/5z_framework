package android.renderscript;

public abstract class ScriptIntrinsic
  extends Script
{
  ScriptIntrinsic(long paramLong, RenderScript paramRenderScript)
  {
    super(paramLong, paramRenderScript);
    if (paramLong != 0L) {
      return;
    }
    throw new RSRuntimeException("Loading of ScriptIntrinsic failed.");
  }
}
