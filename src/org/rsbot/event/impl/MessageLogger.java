package org.rsbot.event.impl;

import java.util.logging.Logger;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;

public class MessageLogger implements MessageListener {

  private final Logger log = Logger.getLogger(MessageLogger.class.getName());

  @Override
  public void messageReceived(final MessageEvent e) {
    if (e.getSender().equals(""))
      log.info("[" + e.getID() + "] " + e.getMessage());
    else
      log.info("[" + e.getID() + "] " + e.getSender() + ": " + e.getMessage());
  }
}