package com.thermsx.localbuoys.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationNode {
    public static final int TYPE_BROWSE = 0;
    public static final int TYPE_ITEM = 2;

    @SerializedName("LocationId")
    private long mLocationId;
    @SerializedName("ParentId")
    private long mParentId;
    @SerializedName("ItemType")
    private int mType;
    @SerializedName("Name")
    private String mName;
    @SerializedName("Items")
    private List<LocationNode> mLocationNodes;
    @SerializedName("VisibleOnBuoys")
    private boolean mVisibleOnBuoys;
    @SerializedName("VisibleOnWeatherForecast")
    private boolean mVisibleOnWeatherForecast;
    @SerializedName("VisibleOnMarineForecast")
    private boolean mVisibleOnMarineForecast;
    @SerializedName("VisibleOnTides")
    private boolean mVisibleOnTides;
    @SerializedName("VisibleOnMoonPhases")
    private boolean mVisibleOnMoonPhases;
    @SerializedName("VisibleOnRadar")
    private boolean mVisibleOnRadar;
    @SerializedName("VisibleOnWavewatch")
    private boolean mVisibleOnWavewatch;
    @SerializedName("VisibleOnSeaSurfaceTemp")
    private boolean mVisibleOnSeaSurfaceTemp;

    public boolean isBrowsable() {
        return mType != TYPE_ITEM;
    }

    public long getLocationId() {
        return mLocationId;
    }

    public void setLocationId(long locationId) {
        mLocationId = locationId;
    }

    public long getParentId() {
        return mParentId;
    }

    public void setParentId(long parentId) {
        mParentId = parentId;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<LocationNode> getLocationNodes() {
        return mLocationNodes;
    }

    public void setLocationNodes(List<LocationNode> locationNodes) {
        mLocationNodes = locationNodes;
    }

    public boolean isVisibleOnBuoys() {
        return mVisibleOnBuoys;
    }

    public void setVisibleOnBuoys(boolean visibleOnBuoys) {
        mVisibleOnBuoys = visibleOnBuoys;
    }

    public boolean isVisibleOnWeatherForecast() {
        return mVisibleOnWeatherForecast;
    }

    public void setVisibleOnWeatherForecast(boolean visibleOnWeatherForecast) {
        mVisibleOnWeatherForecast = visibleOnWeatherForecast;
    }

    public boolean isVisibleOnMarineForecast() {
        return mVisibleOnMarineForecast;
    }

    public void setVisibleOnMarineForecast(boolean visibleOnMarineForecast) {
        mVisibleOnMarineForecast = visibleOnMarineForecast;
    }

    public boolean isVisibleOnTides() {
        return mVisibleOnTides;
    }

    public void setVisibleOnTides(boolean visibleOnTides) {
        mVisibleOnTides = visibleOnTides;
    }

    public boolean isVisibleOnMoonPhases() {
        return mVisibleOnMoonPhases;
    }

    public void setVisibleOnMoonPhases(boolean visibleOnMoonPhases) {
        mVisibleOnMoonPhases = visibleOnMoonPhases;
    }

    public boolean isVisibleOnRadar() {
        return mVisibleOnRadar;
    }

    public void setVisibleOnRadar(boolean visibleOnRadar) {
        mVisibleOnRadar = visibleOnRadar;
    }

    public boolean isVisibleOnWavewatch() {
        return mVisibleOnWavewatch;
    }

    public void setVisibleOnWavewatch(boolean visibleOnWavewatch) {
        mVisibleOnWavewatch = visibleOnWavewatch;
    }

    public boolean isVisibleOnSeaSurfaceTemp() {
        return mVisibleOnSeaSurfaceTemp;
    }

    public void setVisibleOnSeaSurfaceTemp(boolean visibleOnSeaSurfaceTemp) {
        mVisibleOnSeaSurfaceTemp = visibleOnSeaSurfaceTemp;
    }
}
