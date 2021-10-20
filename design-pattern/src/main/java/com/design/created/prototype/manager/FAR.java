package com.design.created.prototype.manager;

public class FAR implements OfficialDocument {
    @Override
    public OfficialDocument clone() {
        OfficialDocument officialDocument = null;
        try {
            officialDocument = (OfficialDocument) super.clone();
        } catch (Exception e){
            e.printStackTrace();
        }
        return officialDocument;
    }

    @Override
    public void display() {
        System.out.println("《可行性分析报告》");
    }
}
