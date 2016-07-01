package com.ibm.fa.example2.item.bean;

import com.ibm.fa.example2.item.entity.Item;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author song
 */
@Stateless
public class ItemFacade extends AbstractFacade<Item> {

    @PersistenceContext(unitName = "example2-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ItemFacade() {
        super(Item.class);
    }
    
}
