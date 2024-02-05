package main.java.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class MainWindow extends JFrame{
    private WorldView view;

    public MainWindow(WorldView view) {
        this.view = view;
    }

    public void setViewInputFocus() {
        view.requestFocus();
    }
}

