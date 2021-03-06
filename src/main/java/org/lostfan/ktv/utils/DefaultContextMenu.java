package org.lostfan.ktv.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

public class DefaultContextMenu extends JPopupMenu {

    private Clipboard clipboard;

    private UndoManager undoManager;

    private JMenuItem undo;
    private JMenuItem redo;
    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem delete;
    private JMenuItem selectAll;

    private JTextComponent jTextComponent;

    public DefaultContextMenu() {
        undoManager = new UndoManager();
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        undo = new JMenuItem(ResourceBundles.getGuiBundle().getString("contexmenu.undo"));
        undo.setEnabled(false);
        undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        undo.addActionListener(e -> undoManager.undo());

        add(undo);

        redo = new JMenuItem(ResourceBundles.getGuiBundle().getString("contexmenu.redo"));
        redo.setEnabled(false);
        redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
        redo.addActionListener(e -> undoManager.redo());

        add(redo);

        add(new JSeparator());

        cut = new JMenuItem(ResourceBundles.getGuiBundle().getString("contexmenu.cut"));
        cut.setEnabled(false);
        cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
        cut.addActionListener(e -> jTextComponent.cut());

        add(cut);

        copy = new JMenuItem(ResourceBundles.getGuiBundle().getString("contexmenu.copy"));
        copy.setEnabled(false);
        copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copy.addActionListener(e -> jTextComponent.copy());

        add(copy);

        paste = new JMenuItem(ResourceBundles.getGuiBundle().getString("contexmenu.paste"));
        paste.setEnabled(false);
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        paste.addActionListener(e -> jTextComponent.paste());

        add(paste);

        delete = new JMenuItem(ResourceBundles.getGuiBundle().getString("contexmenu.delete"));
        delete.setEnabled(false);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        delete.addActionListener(e -> jTextComponent.replaceSelection(""));

        add(delete);

        add(new JSeparator());

        selectAll = new JMenuItem(ResourceBundles.getGuiBundle().getString("contexmenu.selectall"));
        selectAll.setEnabled(false);
        selectAll.setAccelerator(KeyStroke.getKeyStroke("control A"));
        selectAll.addActionListener(e -> jTextComponent.selectAll());

        add(selectAll);
    }

    public void add(JTextComponent jTextComponent) {
        jTextComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent pressedEvent) {
                if ((pressedEvent.getKeyCode() == KeyEvent.VK_Z)
                        && ((pressedEvent.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                }

                if ((pressedEvent.getKeyCode() == KeyEvent.VK_Y)
                        && ((pressedEvent.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
                }
            }
        });

        jTextComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent releasedEvent) {
                if (releasedEvent.getButton() == MouseEvent.BUTTON3) {
                    processClick(releasedEvent);
                }
            }
        });

        jTextComponent.getDocument().addUndoableEditListener(event -> undoManager.addEdit(event.getEdit()));
    }

    private void processClick(MouseEvent event) {
        jTextComponent = (JTextComponent) event.getSource();

        boolean enableUndo = undoManager.canUndo();
        boolean enableRedo = undoManager.canRedo();
        boolean enableCut = false;
        boolean enableCopy = false;
        boolean enablePaste = false;
        boolean enableDelete = false;
        boolean enableSelectAll = false;

        String selectedText = jTextComponent.getSelectedText();
        String text = jTextComponent.getText();

        if (text != null) {
            if (text.length() > 0) {
                enableSelectAll = true;
            }
        }

        if (selectedText != null) {
            if (selectedText.length() > 0) {
                enableCut = true;
                enableCopy = true;
                enableDelete = true;
            }
        }

        try {
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                enablePaste = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        undo.setEnabled(enableUndo);
        redo.setEnabled(enableRedo);
        cut.setEnabled(enableCut);
        copy.setEnabled(enableCopy);
        paste.setEnabled(enablePaste);
        delete.setEnabled(enableDelete);
        selectAll.setEnabled(enableSelectAll);

        show(jTextComponent, event.getX(), event.getY());
    }
}