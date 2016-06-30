package com.ibm.fa.prototype.sessionBean;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author song
 */
@Stateless
@LocalBean
public class OrderManager {

    public int placeOrder(String name, String email, String phone, String address, String cityRegion, String ccNumber) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
