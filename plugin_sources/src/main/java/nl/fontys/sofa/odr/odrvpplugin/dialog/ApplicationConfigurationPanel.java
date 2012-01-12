/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ApplicationConfigurationPanel.java
 *
 * Created on 14-nov-2011, 9:47:58
 */
package nl.fontys.sofa.odr.odrvpplugin.dialog;

import com.vp.plugin.view.IDialog;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JPanel;
import nl.rug.search.odr.ws.connection.WebServiceRequestException;
import nl.rug.search.odr.ws.connection.AuthorizedWebServiceFacade;

/**
 *
 * @author Theo Rutten
 */
public class ApplicationConfigurationPanel extends JPanel {

    private IDialog dialog;
    private JComponent loginTab;
    private JComponent colorsTab;
    private boolean cancelled = false;

    /** Creates new form ApplicationConfigurationPanel */
    public ApplicationConfigurationPanel() {
        initComponents();

        setupTabbedPanels();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("all")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tabbedPanelCategories = new javax.swing.JTabbedPane();
        confOkButton = new javax.swing.JButton();
        confCancelButton = new javax.swing.JButton();
        errorMessage = new javax.swing.JLabel();

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/banner.png"))); // NOI18N

        tabbedPanelCategories.setAutoscrolls(true);

        confOkButton.setText("Ok");
        confOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confOkButtonActionPerformed(evt);
            }
        });

        confCancelButton.setText("Cancel");
        confCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confCancelButtonActionPerformed(evt);
            }
        });

        errorMessage.setForeground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(errorMessage)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 704, Short.MAX_VALUE)
                                .addComponent(confCancelButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(confOkButton))
                            .addComponent(tabbedPanelCategories, javax.swing.GroupLayout.DEFAULT_SIZE, 871, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPanelCategories, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confOkButton)
                    .addComponent(confCancelButton)
                    .addComponent(errorMessage))
                .addContainerGap())
        );

        jLabel1.getAccessibleContext().setAccessibleName("labelHeader");
    }// </editor-fold>//GEN-END:initComponents

    private void confCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confCancelButtonActionPerformed
        evt.getID(); //to avoid unused formal paramter
        cancelled = true;
        if (dialog != null) {
            dialog.close();
        }
    }//GEN-LAST:event_confCancelButtonActionPerformed

    private void confOkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confOkButtonActionPerformed
        evt.getID(); //to avoid unused formal paramter
        try {
            String user = getLoginCredentialsPanel().getUsernameEntry();
            String password = getLoginCredentialsPanel().getPasswordEntry();
            String url = getLoginCredentialsPanel().getUrlEntry();

            AuthorizedWebServiceFacade facade = new AuthorizedWebServiceFacade(url, user, password);

            facade.getProjectOverview();


            if (dialog != null) {
                dialog.close();
            }
        } catch (IOException ex) {
            errorMessage.setText(ex.getMessage());
        } catch (WebServiceRequestException ex) {
            if (ex.isNotFoundStatus()) {
                errorMessage.setText("Wrong URL");
            } else if (ex.isUnauthorizedStatus()) {
                errorMessage.setText("Invalid credentials");
            } else {
                errorMessage.setText("Connection Error: " + ex.getResponseCode());
            }
        }

    }//GEN-LAST:event_confOkButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confCancelButton;
    private javax.swing.JButton confOkButton;
    private javax.swing.JLabel errorMessage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTabbedPane tabbedPanelCategories;
    // End of variables declaration//GEN-END:variables

    private void setupTabbedPanels() {
        // setup the login tab
        loginTab = new UserCredentialsPanel();
        tabbedPanelCategories.addTab("Login", loginTab);

        // setup the colors tab
        colorsTab = new ColorSettingPanel();
        tabbedPanelCategories.addTab("Color", colorsTab);
    }

    void setDialog(IDialog dialog) {
        this.dialog = dialog;
    }

    ColorSettingPanel getColorSettingsPanel() {
        return (ColorSettingPanel) colorsTab;
    }

    UserCredentialsPanel getLoginCredentialsPanel() {
        return (UserCredentialsPanel) loginTab;
    }

    public IDialog getDialog() {
        return dialog;
    }

    boolean wasCancelled() {
        return cancelled;
    }
}
