package com.duoyou_cps.appstore.entity;


public class OptionEntity {
    private int id;
    private int bgType;
    private String title;
    private String desc;
    private boolean canNext;
    private boolean isShowNext = true;
    private Integer resoureId = 0;
    private String url;

    public OptionEntity(int id, String title, String desc, boolean canNext) {
        this(id, title, 0, desc, canNext);
    }

    public OptionEntity(int id, String title, int resoureId, String desc, boolean canNext) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.resoureId = resoureId;
        this.canNext = canNext;
    }

    public OptionEntity(int id, String title, String desc, int bgType, boolean canNext) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.canNext = canNext;
        this.bgType = bgType;
    }

    public OptionEntity(int id, Integer resoure, String title, String desc, int bgType, boolean canNext) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.canNext = canNext;
        this.bgType = bgType;
        resoureId = resoure;

    }

    public OptionEntity(int id, String title, String desc, boolean canNext, boolean isShowNext) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.canNext = canNext;
        this.isShowNext = isShowNext;
    }

    public OptionEntity(int id, Integer resoure, String title, String desc, boolean canNext, boolean isShowNext) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.canNext = canNext;
        this.isShowNext = isShowNext;
        resoureId = resoure;
    }

    public OptionEntity(int id, Integer resoure, String title, String desc, String url, boolean canNext, boolean isShowNext) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.canNext = canNext;
        this.isShowNext = isShowNext;
        resoureId = resoure;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isShowNext() {
        return canNext && isShowNext;
    }

    public void setShowNext(boolean showNext) {
        isShowNext = showNext;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isCanNext() {
        return canNext;
    }

    public void setCanNext(boolean canNext) {
        this.canNext = canNext;
    }

    public int getBgType() {
        return bgType;
    }

    public void setBgType(int bgType) {
        this.bgType = bgType;
    }

    public Integer getResoureId() {
        if (resoureId == null) {
            return 0;
        }
        return resoureId;
    }

    public void setResoureId(Integer resoureId) {
        this.resoureId = resoureId;
    }
}
