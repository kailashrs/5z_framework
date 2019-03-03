package android.media.effect.effects;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.OneShotScheduler;
import android.filterpacks.videoproc.BackDropperFilter;
import android.filterpacks.videoproc.BackDropperFilter.LearningDoneListener;
import android.media.effect.EffectContext;
import android.media.effect.EffectUpdateListener;
import android.media.effect.FilterGraphEffect;

public class BackDropperEffect
  extends FilterGraphEffect
{
  private static final String mGraphDefinition = "@import android.filterpacks.base;\n@import android.filterpacks.videoproc;\n@import android.filterpacks.videosrc;\n\n@filter GLTextureSource foreground {\n  texId = 0;\n  width = 0;\n  height = 0;\n  repeatFrame = true;\n}\n\n@filter MediaSource background {\n  sourceUrl = \"no_file_specified\";\n  waitForNewFrame = false;\n  sourceIsUrl = true;\n}\n\n@filter BackDropperFilter replacer {\n  autowbToggle = 1;\n}\n\n@filter GLTextureTarget output {\n  texId = 0;\n}\n\n@connect foreground[frame]  => replacer[video];\n@connect background[video]  => replacer[background];\n@connect replacer[video]    => output[frame];\n";
  private EffectUpdateListener mEffectListener = null;
  private BackDropperFilter.LearningDoneListener mLearningListener = new BackDropperFilter.LearningDoneListener()
  {
    public void onLearningDone(BackDropperFilter paramAnonymousBackDropperFilter)
    {
      if (mEffectListener != null) {
        mEffectListener.onEffectUpdated(BackDropperEffect.this, null);
      }
    }
  };
  
  public BackDropperEffect(EffectContext paramEffectContext, String paramString)
  {
    super(paramEffectContext, paramString, "@import android.filterpacks.base;\n@import android.filterpacks.videoproc;\n@import android.filterpacks.videosrc;\n\n@filter GLTextureSource foreground {\n  texId = 0;\n  width = 0;\n  height = 0;\n  repeatFrame = true;\n}\n\n@filter MediaSource background {\n  sourceUrl = \"no_file_specified\";\n  waitForNewFrame = false;\n  sourceIsUrl = true;\n}\n\n@filter BackDropperFilter replacer {\n  autowbToggle = 1;\n}\n\n@filter GLTextureTarget output {\n  texId = 0;\n}\n\n@connect foreground[frame]  => replacer[video];\n@connect background[video]  => replacer[background];\n@connect replacer[video]    => output[frame];\n", "foreground", "output", OneShotScheduler.class);
    mGraph.getFilter("replacer").setInputValue("learningDoneListener", mLearningListener);
  }
  
  public void setParameter(String paramString, Object paramObject)
  {
    if (paramString.equals("source")) {
      mGraph.getFilter("background").setInputValue("sourceUrl", paramObject);
    } else if (paramString.equals("context")) {
      mGraph.getFilter("background").setInputValue("context", paramObject);
    }
  }
  
  public void setUpdateListener(EffectUpdateListener paramEffectUpdateListener)
  {
    mEffectListener = paramEffectUpdateListener;
  }
}
