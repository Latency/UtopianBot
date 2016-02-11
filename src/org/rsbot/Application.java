package org.rsbot;

import org.rsbot.gui.BotGUI;
import org.rsbot.gui.LoadScreen;

public class Application {
  public static BotGUI gui;     // Used for RandomAccessFile.java
  
  public static void main(final String[] args) {
    final LoadScreen loader = new LoadScreen();
    loader.dispose();
    gui = new BotGUI();
    gui.setVisible(true);
  }
}
