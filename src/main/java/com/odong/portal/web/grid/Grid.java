package com.odong.portal.web.grid;

import com.odong.portal.web.ResponseItem;

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
    public Grid(String id, int cols) {
        this(id, cols, 0);
    }

    public Grid(String id, int cols, int pages) {
        super(Type.grid);
        this.id = id;
        this.pages = pages;
        this.cols = new Column[cols];
        this.items = new ArrayList<>();
    }

    public void addRow(Object... items) {
        if (items.length != cols.length) {
            throw new IllegalArgumentException("单元格个数不对");
        }
        Collections.addAll(this.items, items);
    }

    private static final long serialVersionUID = -4735460726311781464L;
    private String id;
    private int pages;
    private Column[] cols;
    private List<Object> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Column[] getCols() {
        return cols;
    }

    public void setCols(Column[] cols) {
        this.cols = cols;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }
}
