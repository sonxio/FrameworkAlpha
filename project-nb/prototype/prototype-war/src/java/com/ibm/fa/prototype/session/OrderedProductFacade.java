package com.ibm.fa.prototype.session;

import com.ibm.fa.prototype.entity.OrderedProduct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author song
 */
@Stateless
public class OrderedProductFacade extends AbstractFacade<OrderedProduct> {

    @PersistenceContext(unitName = "prototype-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderedProductFacade() {
        super(OrderedProduct.class);
    }
    
}
