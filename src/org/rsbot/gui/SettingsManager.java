package org.rsbot.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.rsbot.Configuration;
import org.rsbot.Configuration.OperatingSystem;
import org.rsbot.bot.Languages.Language;
import org.rsbot.gui.component.Messages;
import org.rsbot.locale.OutputManager;
import org.rsbot.service.DRM;
import org.rsbot.service.Preferences;
import org.rsbot.util.StringUtil;

public class SettingsManager extends JDialog {
  private static final long   serialVersionUID = 1657935322078534422L;
  private static final String DEFAULT_PASSWORD = "\0\0\0\0\0\0\0\0";
  private final Preferences   preferences      = Preferences.getInstance();
  
  public Preferences getPreferences() {
    return preferences;
  }

  public SettingsManager(final BotGUI owner) {
    super(owner, true);
    final OutputManager om = owner.getOutputManager();
    super.setTitle(om.OS.toString("OPTIONS"));
    preferences.load();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setIconImage(Configuration.getImage(Configuration.Paths.Resources.ICON_WRENCH));

    final JPanel panelLogin = new JPanel(new GridLayout(2, 1));
    panelLogin.setBorder(BorderFactory.createTitledBorder(om.OS.toString("panelLogin")));
    final JPanel panelOptions = new JPanel(new GridLayout(0, 1));
    panelOptions.setBorder(BorderFactory.createTitledBorder(om.OS.toString("panelOptions")));
    final JPanel panelInternal = new JPanel(new GridLayout(0, 1));
    panelInternal.setBorder(BorderFactory.createTitledBorder(om.OS.toString("panelInternal")));
    final JPanel panelWeb = new JPanel(new GridLayout(2, 1));
    panelWeb.setBorder(BorderFactory.createTitledBorder(om.OS.toString("panelWeb")));

    final JPanel[] panelLoginOptions = new JPanel[2];
    for (int i = 0; i < panelLoginOptions.length; i++)
      panelLoginOptions[i] = new JPanel(new GridLayout(1, 2));
    panelLoginOptions[0].add(new JLabel("  " + om.OS.toString("panelLoginOptions.0") + ":"));
    final JTextField textLoginUser = new JTextField(preferences.user);
    textLoginUser.setToolTipText(Configuration.Paths.URLs.HOST + " " + om.OS.toString("textLoginUser.toolTip"));
    panelLoginOptions[0].add(textLoginUser);
    panelLoginOptions[1].add(new JLabel("  " + om.OS.toString("panelLoginOptions.1") + ":"));
    final JPasswordField textLoginPass = new JPasswordField(preferences.user.length() == 0 ? "" : DEFAULT_PASSWORD);
    panelLoginOptions[1].add(textLoginPass);
    panelLogin.add(panelLoginOptions[0]);
    panelLogin.add(panelLoginOptions[1]);

    final JMenuBar mnuBarLanguages = new JMenuBar();
    final JMenu mnuLanguages = new JMenu();
    final HashMap<Language, JMenuItem> mnuItemsLanguage = new HashMap<Language, JMenuItem>();
    for (final Language item : Language.values()) {
      final JMenuItem menu = new JMenuItem(item.getName(), new ImageIcon(Configuration.getFlag(item.getFlag())));
      menu.setToolTipText(om.OS.toString("menu.toolTip") + " '" + item.getName() + "'");
      menu.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          final JMenuItem item = (JMenuItem) e.getSource();
          mnuLanguages.setText(item.getText() + String.format("%60s", " "));
          mnuLanguages.setIcon(item.getIcon());
        }
      });
      mnuItemsLanguage.put(item, menu);
      mnuLanguages.add(menu);
    }
    mnuLanguages.setText(mnuItemsLanguage.get(preferences.language).getText() + String.format("%60s", " "));
    mnuLanguages.setIcon(mnuItemsLanguage.get(preferences.language).getIcon());
    mnuBarLanguages.add(mnuLanguages);
    
    final JCheckBox checkAds = new JCheckBox(om.OS.toString("DISABLEADS"));
    checkAds.setToolTipText("Show logo on startup");
    checkAds.setSelected(preferences.hideAds);
    
    final JCheckBox checkConfirmations = new JCheckBox(om.OS.toString("DISABLECONFIRMATIONS"));
    checkConfirmations.setToolTipText(om.OS.toString("checkConfirmations.ToolTip"));
    checkConfirmations.setSelected(preferences.confirmations);

    final JPanel panelShutdown = new JPanel(new GridLayout(1, 2));
    final JCheckBox checkShutdown = new JCheckBox(om.OS.toString("AUTOSHUTDOWN"));
    checkShutdown.setToolTipText(om.OS.toString("checkShutdown.toolTip"));
    checkShutdown.setSelected(preferences.shutdown);
    panelShutdown.add(checkShutdown);
    final SpinnerNumberModel modelShutdown = new SpinnerNumberModel(preferences.shutdownTime, 3, 60, 1);
    final JSpinner valueShutdown = new JSpinner(modelShutdown);
    panelShutdown.add(valueShutdown);
    checkShutdown.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent arg0) {
        valueShutdown.setEnabled(((JCheckBox) arg0.getSource()).isSelected());
      }
    });
    checkShutdown.setEnabled(Configuration.getCurrentOperatingSystem() == OperatingSystem.WINDOWS);
    valueShutdown.setEnabled(checkShutdown.isEnabled() && checkShutdown.isSelected());

    final JPanel[] panelWebOptions = new JPanel[2];
    for (int i = 0; i < panelWebOptions.length; i++) {
      panelWebOptions[i] = new JPanel(new GridLayout(1, 2));
    }
    final JCheckBox checkWeb = new JCheckBox(Messages.BINDTO);
    checkWeb.setToolTipText(om.OS.toString("checkWeb.toolTip"));
    checkWeb.setSelected(preferences.web);
    panelWebOptions[0].add(checkWeb);
    final JFormattedTextField textWebBind = new JFormattedTextField(preferences.webBind);
    textWebBind.setToolTipText(om.OS.toString("textWebBind.toolTip") + " localhost:9500");
    panelWebOptions[0].add(textWebBind);
    final JCheckBox checkWebPass = new JCheckBox(Messages.USEPASSWORD);
    checkWebPass.setSelected(preferences.webPassRequire);
    panelWebOptions[1].add(checkWebPass);
    final JPasswordField textWebPass = new JPasswordField(DEFAULT_PASSWORD);
    textWebPass.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(final FocusEvent e) {
        textWebPass.setText("");
      }
    });
    panelWebOptions[1].add(textWebPass);
    checkWebPass.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        textWebPass.setEnabled(checkWebPass.isSelected() && checkWebPass.isEnabled());
      }
    });
    checkWeb.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        final boolean enabled = checkWeb.isSelected();
        textWebBind.setEnabled(enabled);
        checkWebPass.setEnabled(enabled);
        for (final ActionListener action : checkWebPass.getActionListeners()) {
          action.actionPerformed(null);
        }
      }
    });
    for (final ActionListener action : checkWeb.getActionListeners()) {
      action.actionPerformed(null);
    }

    panelOptions.add(mnuBarLanguages);
    panelOptions.add(checkAds);
    panelOptions.add(checkConfirmations);
    panelInternal.add(panelShutdown);
    panelWeb.add(panelWebOptions[0]);
    panelWeb.add(panelWebOptions[1]);

    final GridLayout gridAction = new GridLayout(1, 2);
    gridAction.setHgap(5);
    final JPanel panelAction = new JPanel(gridAction);
    final int pad = 6;
    panelAction.setBorder(BorderFactory.createEmptyBorder(pad, pad, pad, pad));
    panelAction.add(Box.createHorizontalGlue());

    final JButton buttonOk = new JButton(om.OS.toString("OK"));
    buttonOk.setPreferredSize(new Dimension(85, 30));
    buttonOk.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        setVisible(false);
        preferences.language = Language.toEnum(mnuLanguages.getText().trim());
        preferences.hideAds = checkAds.isSelected();
        preferences.confirmations = checkConfirmations.isSelected();
        preferences.shutdown = checkShutdown.isSelected();
        if (checkShutdown.isSelected())
          BotToolBar.shutdown_timer = modelShutdown.getNumber().intValue();
        preferences.shutdownTime = modelShutdown.getNumber().intValue();
        preferences.web = checkWeb.isSelected();
        preferences.webBind = textWebBind.getText();
        final String webUser = textLoginUser.getText(), webPass = new String(textWebPass.getPassword());
        if (!webUser.equals(preferences.user) || !webPass.equals(DEFAULT_PASSWORD))
          preferences.webPass = StringUtil.sha1sum(webPass);
        preferences.user = webUser;
        preferences.webPassRequire = checkWebPass.isSelected() && checkWebPass.isEnabled();
        final String loginPass = new String(textLoginPass.getPassword());
        if (!loginPass.equals(DEFAULT_PASSWORD)) {
          if (!DRM.login(preferences.user, loginPass))
            preferences.user = "";
        }
        preferences.save();
        textLoginPass.setText(DEFAULT_PASSWORD);
        textWebPass.setText(DEFAULT_PASSWORD);
        dispose();
      }
    });
    final JButton buttonCancel = new JButton(om.OS.toString("CANCEL"));
    buttonCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        setVisible(false);
        mnuLanguages.setSelectedIcon(mnuItemsLanguage.get(preferences.language).getIcon());
        checkAds.setSelected(preferences.hideAds);
        checkConfirmations.setSelected(preferences.confirmations);
        checkShutdown.setSelected(preferences.shutdown);
        modelShutdown.setValue(preferences.shutdownTime);
        dispose();
      }
    });

    panelAction.add(buttonOk);
    panelAction.add(buttonCancel);

    final JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.setBorder(panelAction.getBorder());
    
    panel.add(panelLogin);
    panel.add(panelOptions);
    panel.add(panelInternal);

    if (!Configuration.RUNNING_FROM_JAR)
      panel.add(panelWeb); // hide web options from non-development builds for now
      
    add(panel);
    add(panelAction, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(buttonOk);
    buttonOk.requestFocus();

    pack();
    setLocationRelativeTo(getOwner());
    setResizable(false);

    addWindowListener(new WindowListener() {
      @Override
      public void windowClosing(WindowEvent arg0) {
        buttonCancel.doClick();
      }

      @Override
      public void windowActivated(WindowEvent arg0) {
      }

      @Override
      public void windowClosed(WindowEvent arg0) {
      }

      @Override
      public void windowDeactivated(WindowEvent arg0) {
      }

      @Override
      public void windowDeiconified(WindowEvent arg0) {
      }

      @Override
      public void windowIconified(WindowEvent arg0) {
      }

      @Override
      public void windowOpened(WindowEvent arg0) {
      }
    });
  }

  public void display() {
    setVisible(true);
  }
}