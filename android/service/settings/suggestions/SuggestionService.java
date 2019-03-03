package android.service.settings.suggestions;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.List;

@SystemApi
public abstract class SuggestionService
  extends Service
{
  private static final boolean DEBUG = false;
  private static final String TAG = "SuggestionService";
  
  public SuggestionService() {}
  
  public IBinder onBind(Intent paramIntent)
  {
    new ISuggestionService.Stub()
    {
      public void dismissSuggestion(Suggestion paramAnonymousSuggestion)
      {
        onSuggestionDismissed(paramAnonymousSuggestion);
      }
      
      public List<Suggestion> getSuggestions()
      {
        return onGetSuggestions();
      }
      
      public void launchSuggestion(Suggestion paramAnonymousSuggestion)
      {
        onSuggestionLaunched(paramAnonymousSuggestion);
      }
    };
  }
  
  public abstract List<Suggestion> onGetSuggestions();
  
  public abstract void onSuggestionDismissed(Suggestion paramSuggestion);
  
  public abstract void onSuggestionLaunched(Suggestion paramSuggestion);
}
