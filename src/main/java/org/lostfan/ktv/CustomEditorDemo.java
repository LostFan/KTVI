package org.lostfan.ktv;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public class CustomEditorDemo extends JFrame{

    public CustomEditorDemo(){
        super("CustomEditor - santhosh@in.fiorano.com");

        JTable table = new JTable(
                new Object[][]{
                        {"JavaTutorial", "This is sun's java tutorial.\nthis is best for beginners"},
                        {"JavaSwing", "This is book dedicated to swing.\nIt covers in-depth knowledge of swing"},
                        {"", ""}
                },
                new String[]{"Title of Book", "Description of Book"}
        );

        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createEmptyBorder());
        DefaultCellEditor editor = new DefaultCellEditor(textField);
        editor.setClickCountToStart(1);

        table.getColumn(table.getColumnName(0)).setCellEditor(editor);
        table.getColumn(table.getColumnName(1)).setCellEditor(new StringActionTableCellEditor(editor));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        getContentPane().add(scroll);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    public static void main(String[] args){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            e.printStackTrace();
        }

        new CustomEditorDemo().setVisible(true);
    }
    public class StringActionTableCellEditor extends ActionTableCellEditor{
        public StringActionTableCellEditor(TableCellEditor editor){
            super(editor);
        }

        protected void editCell(JTable table, int row, int column){
            JTextArea textArea = new JTextArea(10, 50);
            Object value = table.getValueAt(row, column);
            if(value!=null){
                textArea.setText((String)value);
                textArea.setCaretPosition(0);
            }
            int result = JOptionPane.showOptionDialog(table
                    , new JScrollPane(textArea), (String)table.getColumnName(column)
                    , JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if(result==JOptionPane.OK_OPTION)
                table.setValueAt(textArea.getText(), row, column);
        }
    }

    public abstract class ActionTableCellEditor implements TableCellEditor, ActionListener{
//        public final Icon DOTDOTDOT_ICON = new ImageIcon(getClass().getResource("dotdotdot.gif"));

        private TableCellEditor editor;
        private JButton customEditorButton = new JButton("...");

        protected JTable table;
        protected int row, column;

        public ActionTableCellEditor(TableCellEditor editor){
            this.editor = editor;
            customEditorButton.addActionListener(this);

            // ui-tweaking
            customEditorButton.setFocusable(false);
            customEditorButton.setFocusPainted(false);
            customEditorButton.setMargin(new Insets(0, 0, 0, 0));
        }

        public Component getTableCellEditorComponent(JTable table, Object value
                , boolean isSelected, int row, int column){
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(editor.getTableCellEditorComponent(table, value, isSelected, row, column));
            panel.add(customEditorButton, BorderLayout.EAST);
            this.table = table;
            this.row = row;
            this.column = column;
            return panel;
        }

        public Object getCellEditorValue(){
            return editor.getCellEditorValue();
        }

        public boolean isCellEditable(EventObject anEvent){
            return editor.isCellEditable(anEvent);
        }

        public boolean shouldSelectCell(EventObject anEvent){
            return editor.shouldSelectCell(anEvent);
        }

        public boolean stopCellEditing(){
            return editor.stopCellEditing();
        }

        public void cancelCellEditing(){
            editor.cancelCellEditing();
        }

        public void addCellEditorListener(CellEditorListener l){
            editor.addCellEditorListener(l);
        }

        public void removeCellEditorListener(CellEditorListener l){
            editor.removeCellEditorListener(l);
        }

        public final void actionPerformed(ActionEvent e){
            editor.cancelCellEditing();
            editCell(table, row, column);
        }

        protected abstract void editCell(JTable table, int row, int column);
    }
}
