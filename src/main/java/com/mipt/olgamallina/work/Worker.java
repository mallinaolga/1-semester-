package com.mipt.olgamallina.work;

public abstract class Worker {
    public abstract void work(int hours);
    public boolean goHome(String a, String b) {
        return a != null && a.equals(b);
    }
}
