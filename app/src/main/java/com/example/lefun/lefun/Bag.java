package com.example.lefun.lefun;

public class Bag{
    public int code;
    public Data data;
    public String message;
    public boolean success;
    public static class Data{
        public double match;
        public Batch[] buyPankou;
        public Batch[] sellPankou;
        public double preClose;
        public int type;
    }

    public static class Batch{
        public double price;
        public double volume;
    }
}