package com.saas.saasuser.view.stickylistview;


public class NewRecordItems extends OrderRecord {

    public static final int ITEM = 0;
    public static final int SECTION = 1;
    public String groupName;
    public int type;

    public NewRecordItems(int type, OrderRecord orderRecord) {
        super(orderRecord);
        this.type = type;
    }

    public NewRecordItems(int type, String groupName) {
        super(null);
        this.type = type;
        this.groupName = groupName;
    }

}
