package com.ibm.fa.example2.item.jsf;

import com.ibm.fa.example2.item.entity.Item;
import com.ibm.fa.example2.item.jsf.util.JsfUtil;
import com.ibm.fa.example2.item.jsf.util.PaginationHelper;
import com.ibm.fa.example2.item.bean.ItemFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.jboss.logging.Logger;

@Named("itemController")
@SessionScoped
public class ItemController implements Serializable {
    //注意,使用统一命名,一律为l,写法也统一,仅定义为private,不使用static
    private Logger l = Logger.getLogger(this.getClass().getName());
    
    //current为当前使用的ITEM对象
    private Item current;
    //selectedXXXIndex为当前对象在当前页面的INDEX, current和selectedItemIndex两个对象总要同时修改
    private int selectedItemIndex;
    //当前页面的ITEM DATA MODEL
    private DataModel items = null;
    @EJB
    private com.ibm.fa.example2.item.bean.ItemFacade ejbFacade;
    private PaginationHelper pagination;

    public ItemController() {
    }

    /**
     * 对应页面上itemController.selected的属性
     * @return 
     */
    public Item getSelected() {
        if (current == null) {
            current = new Item();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ItemFacade getFacade() {
        return ejbFacade;
    }

    /**
     * 创建分页对象,创建首页DATA MODEL
     * @return 
     */
    public PaginationHelper getPagination() {
        if (pagination == null) {
            //PaginationHelper是abstract的,不能直接使用
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    //总数每次访问都从DB查询得到
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    //创建第一页的DATA MODEL,作为初始值
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        l.debug("preparing list");
        recreateModel();
        return "List";
    }

    /**
     * 用于在LIST页面上跳转到显示VIEW的页面
     * <h:commandLink action="#{itemController.prepareView}" value="#{bundle.ListItemViewLink}"/>
     * @return 
     */
    public String prepareView() {
        l.info("preparing view");//可以在GLASSFISH SERVER CONSOLE中看到该输出:Info:   preparing view
        current = (Item) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        l.trace("selectedItemIndex: "+selectedItemIndex);
        return "View";
    }

    public String prepareCreate() {
        current = new Item();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Item) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            System.out.println("Description before saving: "+current.getDescription());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Item) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ItemDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    /**
     * 获取当前页的数据对象,如果还没有,则创建首页数据对象
     * @return 
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Item getItem(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Item.class)
    public static class ItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemController controller = (ItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemController");
            return controller.getItem(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Item) {
                Item o = (Item) object;
                return getStringKey(o.getItemId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Item.class.getName());
            }
        }

    }

}
