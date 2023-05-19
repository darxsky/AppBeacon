package com.example.appbeacon;

import org.altbeacon.beacon.BeaconDataNotifier;

import java.util.ArrayList;

public class BeaconData {

    String name;
    String uuid;
    int major;
    int minor;
    double rssi;
    double distance;
    double txpower;

    public BeaconData(String name, String uuid, int major, int minor, double rssi, double distance,double txpower) {
        this.name = name;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.rssi = rssi;
        this.distance = distance;
        this.txpower = txpower;
    }

 //   private List<BeaconData> beacon;

    /*public static ArrayList<BeaconData> createBeacon(){
        ArrayList<BeaconData> beacon = new ArrayList<>();
        beacon.add(new BeaconData("prova1","dsadadasdas",1,0,12.24,0));
        beacon.add(new BeaconData("prova2","dfasffsdfsdf",2,1,15.25,0));
        beacon.add(new BeaconData("prova3","nvbnfhdghdf",3,1,99.32,0));
        beacon.add(new BeaconData("prova4","nvbnfhdghdf",3,1,32.21,0));
        beacon.add(new BeaconData("prova5","dhsbdhasbdh",3,1,32.21,0));
        return beacon;
    }*/

    public static ArrayList<BeaconData> create(String name, String uuid, int major, int minor, double rssi, double distance,double txpower){
        ArrayList<BeaconData> beacon = new ArrayList<>();
        beacon.add(new BeaconData(name, uuid, major,minor,rssi,distance,txpower));
        return beacon;
    }

    public static ArrayList<BeaconData> clearBeacon(){
        ArrayList<BeaconData> beacon = new ArrayList<>();
        beacon.clear();
        return beacon;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public double getRssi() {
        return rssi;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTxpower() {
        return txpower;
    }

    public void setTxpower(double txpower) {
        this.txpower = txpower;
    }
}
