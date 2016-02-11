package org.rsbot.event.events;

import java.util.EventListener;

import org.rsbot.event.EventMulticaster;
import org.rsbot.event.listeners.CharacterMovedListener;
import org.rsbot.script.provider.MethodContext;

/**
 * A character moved event.
 */
public class CharacterMovedEvent extends RSEvent {
  private static final long                     serialVersionUID = 8883312847545757405L;

  private final MethodContext                   methods;
  private final org.rsbot.client.RSCharacter    character;
  private final int                             direction;
  private org.rsbot.script.wrappers.RSCharacter wrapped;

  public CharacterMovedEvent(final MethodContext ctx, final org.rsbot.client.RSCharacter character, final int direction) {
    this.methods = ctx;
    this.character = character;
    this.direction = direction;
  }

  @Override
  public void dispatch(final EventListener el) {
    ((CharacterMovedListener) el).characterMoved(this);
  }

  public org.rsbot.script.wrappers.RSCharacter getCharacter() {
    if (wrapped == null) {
      if (character instanceof org.rsbot.client.RSNPC) {
        final org.rsbot.client.RSNPC npc = (org.rsbot.client.RSNPC) character;
        wrapped = new org.rsbot.script.wrappers.RSNPC(methods, npc);
      } else if (character instanceof org.rsbot.client.RSPlayer) {
        final org.rsbot.client.RSPlayer player = (org.rsbot.client.RSPlayer) character;
        wrapped = new org.rsbot.script.wrappers.RSPlayer(methods, player);
      }
    }
    return wrapped;
  }

  /**
   * 0 = NW 1 = N 2 = NE 3 = W 4 = E 5 = SW 6 = S 7 = SE
   * 
   * @return Returns the direction of the character movement event.
   */
  public int getDirection() {
    return direction;
  }

  @Override
  public long getMask() {
    return EventMulticaster.CHARACTER_MOVED_EVENT;
  }
}
