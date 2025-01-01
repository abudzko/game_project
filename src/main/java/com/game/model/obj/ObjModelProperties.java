package com.game.model.obj;

public class ObjModelProperties {
    private String objPath;
    private String texturePath;

    public static ObjModelProperties create(String objPath, String texturePath) {
        var objModelProperties = new ObjModelProperties();
        objModelProperties.setObjPath(objPath);
        objModelProperties.setTexturePath(texturePath);
        return objModelProperties;
    }

    public String getObjPath() {
        return objPath;
    }

    public void setObjPath(String objPath) {
        this.objPath = objPath;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

}
