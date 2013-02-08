package com.szajna.fractals.controler;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.filechooser.FileFilter;

import com.szajna.fractals.model.FractalModel;
import com.szajna.fractals.model.HelpModel;
import com.szajna.fractals.model.Resolution;
import com.szajna.fractals.model.SaveOptionsModel;
import com.szajna.fractals.model.SaveOptionsModel.ResolutionType;
import com.szajna.fractals.view.FractalView;
import com.szajna.fractals.view.FractalView.ColorTheme;
import com.szajna.fractals.view.HelpDialog;
import com.szajna.fractals.view.MainWindow;
import com.szajna.fractals.view.SaveOptionsDialog;

public class MenuControler implements ActionListener, ItemListener {

	private static final String IMAGE_FILES_EXT_DESCRIPTION = ".png, .bmp, .jpg, .jpeg images";
	private static final String[] IMAGE_FILE_EXTENSIONS = { "png", "bmp", "jpg", "jpeg" };
	
	private JFileChooser fileChooser;
	private FractalModel model;
	private FractalView view;
	private JFrame frame;
	private SaveOptionsControler saveOptionsControler;
	private HelpControler helpControler;
	
	public MenuControler(FractalModel fractalModel, FractalView fractalView, JFrame frame) {
		
		fileChooser = new JFileChooser();
		this.model = fractalModel;
		this.view = fractalView;
		this.frame = frame;
		
		SaveOptionsDialog optionsDialogView = new SaveOptionsDialog(frame, "Save Options", true);
		SaveOptionsModel optionsDialogModel = new SaveOptionsModel();
		this.saveOptionsControler = new SaveOptionsControler(
				optionsDialogModel, optionsDialogView, fractalView);
		
		HelpDialog helpDialogView = new HelpDialog(frame, "Help", true);
		HelpModel helpModel = new HelpModel();
		this.helpControler = new HelpControler(helpModel, helpDialogView);
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		Object source = e.getSource();
		if (source instanceof JCheckBoxMenuItem) {
		
			JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)e.getSource();
			if (menuItem.getActionCommand().equals(MainWindow.ACTION_CMD_MENU_ITERATIONS_ITERATIONS_PANEL)) {
			
				JSlider slider = (JSlider)getComponent(MainWindow.COMPONENT_ITERATIONS_SLIDER);
				if (slider != null) {
					
					slider.setVisible(menuItem.isSelected());
					model.calculate();
					view.setFractalData(model.getFractalData(), model.getIterationsCount());
					
					if (! menuItem.isSelected())
						view.requestFocus();
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String cmd = e.getActionCommand();
		if (cmd.equals(MainWindow.ACTION_CMD_MENU_FILE_SAVE_OPTIONS)) {

			saveOptionsControler.getView().setVisible(true);

		} else if (cmd.equals(MainWindow.ACTION_CMD_MENU_HELP_HELP)) {
			
			helpControler.getView().setVisible(true);
			
		} else if (cmd.equals(MainWindow.ACTION_CMD_MENU_FILE_SAVE_AS)) {
			
			FileFilter filter = new ExtFileFilter(IMAGE_FILES_EXT_DESCRIPTION, IMAGE_FILE_EXTENSIONS);
			fileChooser.resetChoosableFileFilters();
			fileChooser.addChoosableFileFilter(filter);
			fileChooser.setSelectedFile(new File("fractal.png"));
			
			int result = fileChooser.showSaveDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				
				File file = fileChooser.getSelectedFile();
				
				ResolutionType resType = saveOptionsControler.getModel().getResolutionType();
				Resolution res = saveOptionsControler.getModel().getResolution();
				
				if (resType == ResolutionType.FRACTAL_VIEW) {

					MenuControler.saveImage(view.getCanvas(), file);
					
				} else if (resType == ResolutionType.SCREEN || 
						(resType == ResolutionType.CUSTOM && 
						res.getWidth() > 0 && res.getHeight() > 0)) {
					
					int width; 
					int height;
					if (resType == ResolutionType.SCREEN) {
						
						Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
						width = screenSize.width;
						height = screenSize.height;
					} else {
						
						width = res.getWidth();
						height = res.getHeight();
					}

					FractalModel newModel = new FractalModel(width, height, model);
					newModel.calculate();
					BufferedImage img = view.renderImage(
							newModel.getFractalData(), newModel.getIterationsCount());
					
					MenuControler.saveImage(img, file);
				}
			}
			
		} else if (cmd.equals(MainWindow.ACTION_CMD_MENU_FILE_EXIT)) {
			
			frame.dispose();
			// exit
			
		} else if (cmd.equals(MainWindow.ACTION_CMD_MENU_COLOR_THEME_GRADIENT1)) {

			view.setColorTheme(ColorTheme.GRADIENT1);
			view.repaint();
		} else if (cmd.equals(MainWindow.ACTION_CMD_MENU_COLOR_THEME_GRADIENT2)) {

			view.setColorTheme(ColorTheme.GRADIENT2);
			view.repaint();
		} else if (cmd.equals(MainWindow.ACTION_CMD_MENU_COLOR_THEME_GRADIENT3)) {

			view.setColorTheme(ColorTheme.GRADIENT3);
			view.repaint();
		} else if (cmd.equals(MainWindow.ACTION_CMD_MENU_COLOR_THEME_HSB)) {
			
			view.setColorTheme(ColorTheme.HSB);
			view.repaint();
		} else if (cmd.equals(MainWindow.ACTION_CMD_MENU_COLOR_THEME_GRAYSCALE)) {
			
			view.setColorTheme(ColorTheme.GRAYSCALE);
			view.repaint();
		} else if (cmd.equals(MainWindow.ACTION_CMD_MENU_COLOR_THEME_VIOLET_SCHADES)) {
			
			view.setColorTheme(ColorTheme.VIOLET_SCHADES);
			view.repaint();
		}
	}
	
	private Component getComponent(String name) {
		
		Component[] components = frame.getContentPane().getComponents();
		for (Component c : components) {

			String componentName = c.getName();
			if (componentName != null && componentName.equals(name))
				return c;
		}
		return null;
	}
	
	private static void saveImage(BufferedImage image, File file) {
        
		String extension = "png";
		String [] extentions = IMAGE_FILE_EXTENSIONS;
		
        try {
        	
        	String path = file.getPath();
        	boolean extentionApproved = false;
        	
        	for (int i = 0; i < extentions.length; ++i) {
        		if (path.endsWith("." + extentions[i])) {

        			extension = extentions[i];
        			extentionApproved = true;
        			break;
        		}
        	}
        	
        	// use default .png on error 
        	if (! extentionApproved) {
        		extension = "png";
        		file = new File(file.getPath() + "." + extension);
        	}
        	
        	boolean writeFile = true;
        	
        	// check if file exists
        	if (file.exists()) {
        		
        		String header = "File exists";
        		String message = "File " + file.getName() + " already exists." + "\n" + "Overwrite?";
        		//Object [] options = { "OK", "Cancel" };
        		
        		int selectedOption = JOptionPane.showOptionDialog(null, message, header, 
        				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        		
        		if (selectedOption != JOptionPane.OK_OPTION)
        			writeFile = false;
        	}
        	if (writeFile) {
        		ImageIO.write(image, extension, file);  // ignore returned boolean
        	}

        } catch (Exception e) {

        	String header = "Error";
    		String message = "Image write error. Check if the path and file name are correct.";
    		if (e.getMessage() != null)
    			message += "\n" + e.getMessage();
    		
        	JOptionPane.showMessageDialog(null, message, header, JOptionPane.ERROR_MESSAGE);
        } 
    }
}

