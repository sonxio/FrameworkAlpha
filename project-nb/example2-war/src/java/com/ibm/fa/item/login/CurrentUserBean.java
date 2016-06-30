package com.ibm.fa.item.login;

import java.io.Serializable;
import java.security.Principal;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author song
 */
@ManagedBean(name = "CurrentUserBean") //注意:name属性决定了xhtml页面上引用该BEAN的名称,并不是对这个BEAN名称的简单重复.
@SessionScoped
public class CurrentUserBean implements Serializable {

    private String name;
    private String password;
    private String deviceId;
    private String welcomeMsg;
    private Boolean loggedIn;

    public Boolean getLoggedIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.getUserPrincipal() == null) {
            return false;
        }
        return true;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWelcomeMsg() {
        welcomeMsg = "欢迎你! " + name + ", 所提供的的设备编号为: " + deviceId + ". ";
        return welcomeMsg;
    }

    public void setWelcomeMsg(String welcomeMsg) {
        this.welcomeMsg = welcomeMsg;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 登录后把用户转向合适的VIEW
     *
     * @return
     */
    public String loginAndRedirectView() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            //判断当前SESSION是否已经有这个用户的登录记录
            final Principal p = request.getUserPrincipal();
            if (p != null) {
                System.out.println("Session already logged in as " + p.getName());
                //即使之前登录的用户和当前要登录的是同一个,也应该再次验证密码,以免带来不安全因素,例如用户跳转其他页面后离开,另一个人再次访问该系统.
                context.addMessage(null, new FacesMessage("将注销之前登录的用户" + p.getName()));
                this.logout();

            }
            //如果用户之前已经登录,将会抛出ServletException : Attempt to re-login while the user identity already exists
            request.login(this.name, this.password);
        } catch (ServletException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Login failed."));
            return "/login/form_error";
        }
        //根据用户组判断首页        
        return this.getViewByRole(request);
    }

    public String logoutAndRedirectView() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.getUserPrincipal() == null) {
            System.err.println("User not logged in when trying to logout.");
            return "/login/form_login";
        }
        try {
            System.out.println("Logging out current user: " + request.getUserPrincipal().getName());
            request.logout();
            //重置SESSION SCOPE BEAN中的所有变量
            HttpSession s = (HttpSession) context.getExternalContext().getSession(false);
            s.invalidate();
            return "/login/form_login";
        } catch (ServletException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Logout failed."));
            HttpSession s = (HttpSession) context.getExternalContext().getSession(false);
            s.invalidate();
        }
        return "/login/form_login";
    }

    private void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        if (request.getUserPrincipal() == null) {
            System.err.println("User not logged in when trying to logout.");
            return;
        }
        try {
            System.out.println("Logging out current user: " + request.getUserPrincipal().getName());
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage("Logout failed."));
        }
    }

    private String getViewByRole(HttpServletRequest request) {
        if (request.isUserInRole("appadmin")) {
            return "/admin/admin";
        }
        if (request.isUserInRole("appuser")) {
            return "/frontpage/inputDeviceId";
        }
        return "/frontpage/noRole";
    }
}
