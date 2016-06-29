package com.ibm.fa.item.jsf.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author song
 */
@ManagedBean(name = "CurrentUserBean") //注意:name属性决定了xhtml页面上引用该BEAN的名称,并不是对这个BEAN名称的简单重复.
@SessionScoped
//NOTE: 对于SESSIONSCOPED BEAN,应当实现Serializable接口.
public class CurrentUserBean implements Serializable {

    /*
    自动生成GETTER SETTER 在NETBEANS中:
    Insert Code (Alt-Insert; Ctrl-I on Mac)
     */
    String name;
    String id;
    String welcomeMsg;

    public String getWelcomeMsg() {
        welcomeMsg = "欢迎你! " + name + "(" + id + ")";
        return welcomeMsg;
    }

    public void setWelcomeMsg(String welcomeMsg) {
        this.welcomeMsg = welcomeMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Creates a new instance of CurrentUserBean 在此执行初始化操作,可以通过JPA访问DB获取初始信息
     */
    public CurrentUserBean() {
        //TODO 从登录信息中获取数据
        this.name = "测试用户1";
        this.id = "USER001";
    }

}
