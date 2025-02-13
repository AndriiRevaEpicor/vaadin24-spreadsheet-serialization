package org.vaadin.example;

import java.io.Serializable;

public class WorkbookData implements Serializable {

    private byte[] data;
    private String selection;

    public WorkbookData(byte[] data, String selection) {
        this.data = data;
        this.selection = selection;
    }

    public byte[] getData() {
        return data;
    }

    public String getSelection() {
        return selection;
    }
}
