/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SelectionPanel.java
 *
 * Created on 10.10.2011, 10:24:35
 */
package nl.fontys.sofa.odr.odrvpplugin.dialog;

import com.vp.plugin.view.IDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.TableModel;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.HistoryDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author Vadim Emrich
 */
public class SelectionPanel extends javax.swing.JPanel {

    private IDialog dialog;
    private ProjectDTO project;
    private boolean allSelected = false;
    private List<DecisionDTO> selectedDecisions = null;
    private boolean canceled = false;
    private static final int COLUMNWIDTH = 80;

    /** Creates new form SelectionPanel */
    public SelectionPanel(ProjectDTO project) {
        this.project = project;
        initComponents();
        decisionTable.setModel(new CustomDefaultTableModel());
        CustomDefaultTableModel model = (CustomDefaultTableModel) decisionTable.getModel();
        int i = 0;
        Collections.sort(project.getDecisions(), new DecisionDTO.NameComparator());
        for (DecisionDTO decisison : project.getDecisions()) {
            HistoryDTO hist = decisison.getHistory().get(0);
            model.addRow(new Object[]{Boolean.FALSE, decisison.getName(), hist.getState()});
            model.setCellEditable(i, 0, true);
            i++;
        }
        decisionTable.getColumnModel().getColumn(0).setWidth(COLUMNWIDTH);
        decisionTable.getColumnModel().getColumn(0).setMaxWidth(COLUMNWIDTH);
        decisionTable.getColumnModel().getColumn(0).setMinWidth(COLUMNWIDTH);
        decisionTable.getColumnModel().getColumn(0).setPreferredWidth(COLUMNWIDTH);
        decisionTable.getColumnModel().getColumn(0).setResizable(false);

        decisionTable.getTableHeader().setReorderingAllowed(false);
    }

    SelectionPanel(ProjectDTO project, List<DecisionDTO> existingDecisions) {
        this(project);

        CustomBooleanCellRenderer boolRenderer = new CustomBooleanCellRenderer();
        decisionTable.getColumnModel().getColumn(0).setCellRenderer(boolRenderer);


        CustomDefaultTableModel model = (CustomDefaultTableModel) decisionTable.getModel();

        selectedDecisions = new ArrayList<DecisionDTO>();

        for (DecisionDTO d : existingDecisions) {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (d.getName().equals(model.getValueAt(i, 1).toString())) {
                    model.setValueAt(true, i, 0);
                    model.setCellEditable(i, 0, false);
                }
            }
        }
    }

    public void setDialog(IDialog dialog) {
        this.dialog = dialog;
    }

    public List<DecisionDTO> getSelectedDecisions() {
        if (selectedDecisions == null) {
            return new ArrayList<DecisionDTO>();
        } else {
            return selectedDecisions;
        }
    }

    boolean wasCanceled() {
        return canceled;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("all")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        decisionTable = new javax.swing.JTable();
        cancelButton = new javax.swing.JButton();
        importSelectedButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();

        decisionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Selection", "Name", "State"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        decisionTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(decisionTable);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        importSelectedButton.setText("Import selected decisions");
        importSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importSelectedButtonActionPerformed(evt);
            }
        });

        selectAllButton.setText("Select all");
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(selectAllButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 168, Short.MAX_VALUE)
                        .add(importSelectedButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(importSelectedButton)
                    .add(selectAllButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        evt.getID(); //to avoid unused formal paramter
        TableModel model = decisionTable.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            if (allSelected) {
                model.setValueAt(Boolean.FALSE, i, 0);
            } else {
                model.setValueAt(Boolean.TRUE, i, 0);
            }
        }
        allSelected = !allSelected;
        if (!allSelected) {
            selectAllButton.setText("Select all");
        } else {
            selectAllButton.setText("Deselect all");
        }
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void importSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importSelectedButtonActionPerformed
        evt.getID(); //to avoid unused formal paramter
        TableModel model = decisionTable.getModel();

        selectedDecisions = new ArrayList<DecisionDTO>();

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(Boolean.TRUE)) {
                selectedDecisions.add(project.getDecisions().get(i));
            }
        }

        if (dialog != null) {
            dialog.close();
        }
        canceled = false;
    }//GEN-LAST:event_importSelectedButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        evt.getID(); //to avoid unused formal paramter
        if (dialog != null) {
            dialog.close();
        }
        canceled = true;
    }//GEN-LAST:event_cancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTable decisionTable;
    private javax.swing.JButton importSelectedButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton selectAllButton;
    // End of variables declaration//GEN-END:variables
}
