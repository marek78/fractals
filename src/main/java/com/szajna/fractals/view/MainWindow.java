package com.szajna.fractals.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import com.szajna.fractals.AppConfig;
import com.szajna.fractals.controler.FractalControler;
import com.szajna.fractals.controler.MenuControler;
import com.szajna.fractals.model.FractalModel;
import com.szajna.fractals.view.FractalView.ColorTheme;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private static final int MNEMONIC_MENU_FILE         = KeyEvent.VK_F;
    private static final int MNEMONIC_MENU_COLOR        = KeyEvent.VK_C;
    private static final int MNEMONIC_MENU_ITERATIONS   = KeyEvent.VK_I;
    private static final int MNEMONIC_MENU_HELP         = KeyEvent.VK_H;
    
    private static final String ACCEL_KEYSTROKE_SAVE_OPTIONS            = "control O";
    private static final String ACCEL_KEYSTROKE_SAVE_AS                 = "control S";
    private static final String ACCEL_KEYSTROKE_THEME_THEME_GRADIENT1   = "control 1";
    private static final String ACCEL_KEYSTROKE_THEME_THEME_GRADIENT2   = "control 2";
    private static final String ACCEL_KEYSTROKE_THEME_THEME_GRADIENT3   = "control 3";
    private static final String ACCEL_KEYSTROKE_THEME_HSB               = "control 4";
    private static final String ACCEL_KEYSTROKE_THEME_GRAYSCALE         = "control 5";
    private static final String ACCEL_KEYSTROKE_THEME_VIOLET_SCHADES    = "control 6";
    private static final String ACCEL_KEYSTROKE_ITERATIONS_PANEL        = "control I";
    private static final String ACCEL_KEYSTROKE_HELP                    = "control H";
    
    public static final String ACTION_CMD_MENU_FILE_SAVE_OPTIONS        = "MenuFile.SaveOptions";
    public static final String ACTION_CMD_MENU_FILE_SAVE_AS             = "MenuFile.SaveAs";
    public static final String ACTION_CMD_MENU_FILE_EXIT                = "MenuFile.Exit";
    
    public static final String ACTION_CMD_MENU_COLOR_THEME_GRADIENT1        = "MenuColor.ThemeGrad1";
    public static final String ACTION_CMD_MENU_COLOR_THEME_GRADIENT2        = "MenuColor.ThemeGrad2";
    public static final String ACTION_CMD_MENU_COLOR_THEME_GRADIENT3        = "MenuColor.ThemeGrad3";
    public static final String ACTION_CMD_MENU_COLOR_THEME_HSB              = "MenuColor.ThemeHSB";
    public static final String ACTION_CMD_MENU_COLOR_THEME_GRAYSCALE        = "MenuColor.ThemeGrayscale";
    public static final String ACTION_CMD_MENU_COLOR_THEME_VIOLET_SCHADES   = "MenuColor.ThemeVioletSchades";
    
    public static final String ACTION_CMD_MENU_ITERATIONS_ITERATIONS_PANEL  = "MenuIterations.IterationsPanel";
    public static final String ACTION_CMD_MENU_ITERATIONS_USE_OPEN_CL       = "MenuIterations.UseOpenCL";

    public static final String COMPONENT_ITERATIONS_SLIDER                  = "Main.IterationsSlider";
    public static final String ACTION_CMD_MENU_HELP_HELP                    = "MenuHelp.Help";

    private boolean componentFirstAppear = true;
    private Object mutex = new Object();

    
    private enum MenuItemType {
        TYPE_SIMPLE,
        TYPE_CHECKBOX,
    };
    
    public MainWindow(String appName) {
        createUi(appName);
    }
    
    private void createUi(String appName) {
        
        synchronized (mutex) {
            // read application configuration
            AppConfig.getInstance().readFromFile();
        }

        final FractalModel fractalModel = new FractalModel();
        final FractalView fractalView = new FractalView(ColorTheme.GRADIENT1);
        final FractalControler fractalControler = new FractalControler(fractalModel, fractalView);
        final MenuControler menuControler = new MenuControler(fractalModel, fractalView, this);

        this.setTitle(appName);
        
        URL iconUrl = MainWindow.class.getResource("/icon.png");
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            this.setIconImage(icon.getImage());
        }
        
        // add listeners
        fractalView.addMouseListener(fractalControler);
        fractalView.addMouseMotionListener(fractalControler);
        fractalView.addMouseWheelListener(fractalControler);
        fractalView.addKeyListener(fractalControler);
        fractalView.addComponentListener(fractalControler);
        
        this.getContentPane().add(fractalView);
        
        // create the menu bar.
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        JMenuItem menuItem;
        JMenu menu;
        
        // menu file
        menu = new JMenu("File");
        menu.setMnemonic(MNEMONIC_MENU_FILE);
        menu.getAccessibleContext().setAccessibleDescription("MenuFile");

        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_SIMPLE, "Save Options", 
                ACCEL_KEYSTROKE_SAVE_OPTIONS, ACTION_CMD_MENU_FILE_SAVE_OPTIONS, menuControler, menuControler);
        menu.add(menuItem);
        
        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_SIMPLE, "Save As...", 
                ACCEL_KEYSTROKE_SAVE_AS, ACTION_CMD_MENU_FILE_SAVE_AS, menuControler, menuControler);
        menu.add(menuItem);
        menu.addSeparator();
        
        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_SIMPLE, "Exit", null, 
                ACTION_CMD_MENU_FILE_EXIT, menuControler, menuControler);
        menu.add(menuItem);
        menuBar.add(menu);

        // menu color
        menu = new JMenu("Color");
        menu.setMnemonic(MNEMONIC_MENU_COLOR);
        menu.getAccessibleContext().setAccessibleDescription("MenuColor");

        ButtonGroup group = new ButtonGroup();
        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_CHECKBOX, "Theme Green-Beige-Red-Beige", 
                ACCEL_KEYSTROKE_THEME_THEME_GRADIENT1, ACTION_CMD_MENU_COLOR_THEME_GRADIENT1, 
                menuControler, menuControler);
        group.add(menuItem);
        menu.add(menuItem);
        if (fractalView.getColorTheme() == ColorTheme.GRADIENT1) {
            menuItem.setSelected(true);
        }
        
        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_CHECKBOX, "Theme Green-Tan-Orange-Khaki", 
                ACCEL_KEYSTROKE_THEME_THEME_GRADIENT2, ACTION_CMD_MENU_COLOR_THEME_GRADIENT2, 
                menuControler, menuControler);
        group.add(menuItem);
        menu.add(menuItem);
        if (fractalView.getColorTheme() == ColorTheme.GRADIENT2) {
            menuItem.setSelected(true);
        }
        
        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_CHECKBOX, "Theme Red-Orange-Blue", 
                ACCEL_KEYSTROKE_THEME_THEME_GRADIENT3, ACTION_CMD_MENU_COLOR_THEME_GRADIENT3, 
                menuControler, menuControler);
        group.add(menuItem);
        menu.add(menuItem);
        if (fractalView.getColorTheme() == ColorTheme.GRADIENT3) {
            menuItem.setSelected(true);
        }

        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_CHECKBOX, "Theme HSB", 
                ACCEL_KEYSTROKE_THEME_HSB, ACTION_CMD_MENU_COLOR_THEME_HSB, 
                menuControler, menuControler);
        group.add(menuItem);
        menu.add(menuItem);
        if (fractalView.getColorTheme() == ColorTheme.HSB) {
            menuItem.setSelected(true);
        }

        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_CHECKBOX, "Theme Grayscale", 
                ACCEL_KEYSTROKE_THEME_GRAYSCALE, ACTION_CMD_MENU_COLOR_THEME_GRAYSCALE, 
                menuControler, menuControler);
        group.add(menuItem);
        menu.add(menuItem);
        if (fractalView.getColorTheme() == ColorTheme.GRAYSCALE) {
            menuItem.setSelected(true);
        }

        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_CHECKBOX, "Theme Violet Shades", 
                ACCEL_KEYSTROKE_THEME_VIOLET_SCHADES, ACTION_CMD_MENU_COLOR_THEME_VIOLET_SCHADES, 
                menuControler, menuControler);
        group.add(menuItem);
        menu.add(menuItem);
        if (fractalView.getColorTheme() == ColorTheme.VIOLET_SHADES) {
            menuItem.setSelected(true);
        }
        menuBar.add(menu);

        // menu calculation
        menu = new JMenu("Calculation");
        menu.setMnemonic(MNEMONIC_MENU_ITERATIONS);
        menu.getAccessibleContext().setAccessibleDescription("MenuCalculation");

        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_CHECKBOX, "Iterations panel", 
                ACCEL_KEYSTROKE_ITERATIONS_PANEL, ACTION_CMD_MENU_ITERATIONS_ITERATIONS_PANEL, 
                menuControler, menuControler);
        menuItem.setSelected(true);
        menu.add(menuItem);

        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_CHECKBOX, "Use OpenCL", 
                null, ACTION_CMD_MENU_ITERATIONS_USE_OPEN_CL, 
                menuControler, menuControler);

        if (fractalModel.isOpenClSupported()) {
            menuItem.setEnabled(true);
            menuItem.setSelected(fractalModel.isOpenClEnabled());
            
        } else {
            menuItem.setEnabled(false);
            menuItem.setSelected(false);
        }
        menu.add(menuItem);
        menuBar.add(menu);
        
        // menu help
        menu = new JMenu("Help");
        menu.setMnemonic(MNEMONIC_MENU_HELP);
        menu.getAccessibleContext().setAccessibleDescription("MenuHelp");

        menuItem = MainWindow.createMenuItem(MenuItemType.TYPE_SIMPLE, "Help", 
                ACCEL_KEYSTROKE_HELP, ACTION_CMD_MENU_HELP_HELP, 
                menuControler, menuControler);
        menu.add(menuItem);
        menuBar.add(menu);

        this.setJMenuBar(menuBar);

        JSlider iterationsSlider = new JSlider(
                JSlider.VERTICAL, 
                FractalModel.ITERATIONS_COUNT_MIN, 
                FractalModel.ITERATIONS_COUNT_MAX,
                fractalModel.getIterationsCount());
        
        iterationsSlider.setMajorTickSpacing(50);
        iterationsSlider.setMinorTickSpacing(10);
        iterationsSlider.setSnapToTicks(true);
        iterationsSlider.setPaintLabels(true);
        iterationsSlider.setPaintTicks(true);
        iterationsSlider.addChangeListener(fractalControler);
        iterationsSlider.setVisible(true);
        iterationsSlider.setName(COMPONENT_ITERATIONS_SLIDER);
        iterationsSlider.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.getContentPane().add(iterationsSlider, BorderLayout.EAST, -1);
        
        this.addComponentListener(new ComponentListener() {
            
            @Override
            public void componentShown(ComponentEvent e) {
                
                if (componentFirstAppear && ! fractalModel.isOpenClSupported()) {
                    showOpenClNotInitialized();
                }
                componentFirstAppear = false; 
            }
            
            @Override
            public void componentResized(ComponentEvent e) {
            }
            
            @Override
            public void componentMoved(ComponentEvent e) {
            }
            
            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocation(100, 100);
        this.setMinimumSize(new Dimension(100, 100));
        // center on screen
        this.setLocationRelativeTo(null);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                
                synchronized (mutex) {
                    // write application configuration
                    AppConfig.getInstance().writeToFile();
                }
            }
        });
    }
    
    private void showOpenClNotInitialized() {
        
        String text = "OpenCL not initialized / no OpenCL device found.\n"
                + "Hardware accelerated computing not available.\n";
        JOptionPane.showMessageDialog(null, text, "Fractals", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static JMenuItem createMenuItem(MenuItemType type, String text, 
            String acceleratorKeyStroke, String actionCommand, 
            ActionListener actionListener, ItemListener itemListener) {
        
        JMenuItem menuItem;

        switch (type) {
        case TYPE_CHECKBOX:
            menuItem = new JCheckBoxMenuItem(text);
            break;
        case TYPE_SIMPLE:
        default:
            menuItem = new JMenuItem(text);
            break;
        }
        
        menuItem.setActionCommand(actionCommand);

        if (acceleratorKeyStroke != null && acceleratorKeyStroke.length() > 0) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(acceleratorKeyStroke);
            if (keyStroke != null)
                menuItem.setAccelerator(keyStroke);
        }

        if (actionListener != null)
            menuItem.addActionListener(actionListener);
        if (itemListener != null)
            menuItem.addItemListener(itemListener);

        return menuItem;
    }
}

