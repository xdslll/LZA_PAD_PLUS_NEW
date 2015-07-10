package com.lza.pad.helper;

import com.google.gson.reflect.TypeToken;
import com.lza.pad.db.model.*;

import java.lang.reflect.Type;

import lza.com.lza.library.util.GsonHelper;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 11/4/14.
 */
public class JsonParseHelper {

    /**
     * Sample Code
     */
    public static ResponseData parseSimpleResponse(String json) {
        try {
            Type type = new TypeToken<ResponseData>() {}.getType();
            return GsonHelper.instance().fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResponseData<SchoolVersion> parseSchoolVersion(String json) {
        try {
            Type type = new TypeToken<ResponseData<SchoolVersion>>() {}.getType();
            return GsonHelper.instance().fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResponseData<VersionModule> parseVersionModule(String json) {
        try {
            Type type = new TypeToken<ResponseData<VersionModule>>() {}.getType();
            return GsonHelper.instance().fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResponseData<Config> parseConfig(String json) {
        try {
            Type type = new TypeToken<ResponseData<Config>>() {}.getType();
            return GsonHelper.instance().fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResponseData<Configs> parseConfigs(String json) {
        try {
            Type type = new TypeToken<ResponseData<Configs>>() {}.getType();
            return GsonHelper.instance().fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResponseData<NewVersionInfo> parseNewVersionInfo(String json){
        try {
            Type type = new TypeToken<ResponseData<NewVersionInfo>>() {}.getType();
            return GsonHelper.instance().fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResponseData<PadResource> parseResourceResponse(String json) {
        try {
            Type type = new TypeToken<ResponseData<PadResource>>() {}.getType();
            return GsonHelper.instance().fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResponseData<MagazineInfo> parseMagazineInfo(String json){
        try {
            Type type = new TypeToken<ResponseData<MagazineInfo>>() {}.getType();
            return GsonHelper.instance().fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
