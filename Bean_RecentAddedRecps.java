package com.example.chetana.kitchenmantra.Classes;

public class Bean_RecentAddedRecps {

    String RecpHeaderId, RecpDetailId,Recpname, Favcnt, RecUploaderName, RecpTime, addedDt, preparation_desc, Category, feedback,
            RecpmasterId, Ingrd_desc, Ingrd_qty, Ingrd_unit, Ingr_itemasterid;

    String RecpImg;
    byte[] RecpImgDATA;

    public String getRecpHeaderId() { return RecpHeaderId;  }

    public void setRecpHeaderId(String recpHeaderId) { RecpHeaderId = recpHeaderId; }

    public String getRecpDetailId() { return RecpDetailId; }

    public void setRecpDetailId(String recpDetailId) { RecpDetailId = recpDetailId; }

    public String getRecpname() {
        return Recpname;
    }

    public void setRecpname(String recpname) {
        Recpname = recpname;
    }

    public String getFavcnt() {
        return Favcnt;
    }

    public void setFavcnt(String favcnt) {
        Favcnt = favcnt;
    }

    public String getRecUploaderName() {
        return RecUploaderName;
    }

    public void setRecUploaderName(String recUploaderName) {
        RecUploaderName = recUploaderName;
    }

    public String getRecpTime() {
        return RecpTime;
    }

    public void setRecpTime(String recpTime) {
        RecpTime = recpTime;
    }

    public String getRecpImg() {
        return RecpImg;
    }

    public void setRecpImg(String recpImg) { RecpImg = recpImg;  }

    public String getAddedDt() {
        return addedDt;
    }

    public void setAddedDt(String addedDt) {
        this.addedDt = addedDt;
    }

    public String getPreparation_desc() {
        return preparation_desc;
    }

    public void setPreparation_desc(String preparation_desc) {
        this.preparation_desc = preparation_desc;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRecpmasterId() {
        return RecpmasterId;
    }

    public void setRecpmasterId(String recpmasterId) {
        RecpmasterId = recpmasterId;
    }

    public String getIngrd_desc() {
        return Ingrd_desc;
    }

    public void setIngrd_desc(String ingrd_desc) {
        Ingrd_desc = ingrd_desc;
    }

    public String getIngrd_qty() {
        return Ingrd_qty;
    }

    public void setIngrd_qty(String ingrd_qty) {
        Ingrd_qty = ingrd_qty;
    }

    public String getIngrd_unit() {
        return Ingrd_unit;
    }

    public void setIngrd_unit(String ingrd_unit) {
        Ingrd_unit = ingrd_unit;
    }

    public String getIngr_itemasterid() { return Ingr_itemasterid; }

    public void setIngr_itemasterid(String ingr_itemasterid) { Ingr_itemasterid = ingr_itemasterid; }

    public byte[] getRecpImgDATA() { return RecpImgDATA; }

    public void setRecpImgDATA(byte[] recpImgDATA) { RecpImgDATA = recpImgDATA; }

}
