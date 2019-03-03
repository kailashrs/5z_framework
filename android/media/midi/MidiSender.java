package android.media.midi;

public abstract class MidiSender
{
  public MidiSender() {}
  
  public void connect(MidiReceiver paramMidiReceiver)
  {
    if (paramMidiReceiver != null)
    {
      onConnect(paramMidiReceiver);
      return;
    }
    throw new NullPointerException("receiver null in MidiSender.connect");
  }
  
  public void disconnect(MidiReceiver paramMidiReceiver)
  {
    if (paramMidiReceiver != null)
    {
      onDisconnect(paramMidiReceiver);
      return;
    }
    throw new NullPointerException("receiver null in MidiSender.disconnect");
  }
  
  public abstract void onConnect(MidiReceiver paramMidiReceiver);
  
  public abstract void onDisconnect(MidiReceiver paramMidiReceiver);
}
