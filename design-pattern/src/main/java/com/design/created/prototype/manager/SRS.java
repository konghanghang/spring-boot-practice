package com.design.created.prototype.manager;

public class SRS implements OfficialDocument {
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
        System.out.println("《软件需求规格说明书》");
    }
}
