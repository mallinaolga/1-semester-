package com.mipt.olgamallina;

import java.util.Optional;

public class LoggingDecorator implements DataService {

    private final DataService delegate;

    public LoggingDecorator(DataService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<String> findDataByKey(String key) {
        System.out.println("[LOG] findDataByKey: key=" + key);
        Optional<String> result = delegate.findDataByKey(key);
        System.out.println("[LOG] findDataByKey result: " + result);
        return result;
    }

    @Override
    public void saveData(String key, String data) {
        System.out.println("[LOG] saveData: key=" + key + ", data=" + data);
        delegate.saveData(key, data);
    }

    @Override
    public boolean deleteData(String key) {
        System.out.println("[LOG] deleteData: key=" + key);
        boolean deleted = delegate.deleteData(key);
        System.out.println("[LOG] deleteData result: " + deleted);
        return deleted;
    }
}
