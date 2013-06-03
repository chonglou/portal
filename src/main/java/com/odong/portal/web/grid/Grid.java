package com.odong.portal.web.grid;

import com.odong.portal.web.ResponseItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: flamen
 * Date: 13-5-26
 * Time: 下午5:26
 */
public class Grid extends ResponseItem {
    public Grid(String id, int columnSize) {
        this(id, columnSize, 0);
    }

    public Grid(String id, int columnSize, int pageSize) {
        super(Type.grid);
        this.id = id;
        this.pageSize = pageSize;
        this.columns = new Column[columnSize];
        this.items = new ArrayList<>();
    }

    public void addRow(Object... items){
        if(items.length != columns.length){
            throw new IllegalArgumentException("单元格个数不对");
        }
        Collections.addAll(this.items, items);
    }

    private static final long serialVersionUID = -4735460726311781464L;
    private String id;
    private int pageSize;
    private Column[] columns;
    private List<Object> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Column[] getColumns() {
        return columns;
    }

    public void setColumns(Column[] columns) {
        this.columns = columns;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }
}
