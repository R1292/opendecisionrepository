
package nl.rug.search.odr.controller;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.JsfUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class UserMenuController {

    public static final String LOGIN_FILENAME = "login.xhtml";
    public static final String USER_MENU_FILENAME = "userMenu.xhtml";

    public String getUserMenu() {
        if (AuthenticationUtil.isAuthtenticated()) {
            return USER_MENU_FILENAME;
        } else {
            return LOGIN_FILENAME;
        }
    }

    public void logout() {
        AuthenticationUtil.logout();
        try {
            JsfUtil.redirect("/index.html");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getName() {
        return AuthenticationUtil.getUserName();
    }
}
