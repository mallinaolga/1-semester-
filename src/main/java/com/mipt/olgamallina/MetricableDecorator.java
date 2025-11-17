package com.mipt.olgamallina;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
public class MetricableDecorator implements DataService {

    private final DataService delegate;
    private final MetricService metricService;

    public MetricableDecorator(DataService delegate) {
        this(delegate, new MetricService());
    }

    public MetricableDecorator(DataService delegate, MetricService metricService) {
        this.delegate = delegate;
        this.metricService = metricService;
    }

    @Override
    public Optional<String> findDataByKey(String key) {
        Instant start = Instant.now();
        try {
            return delegate.findDataByKey(key);
        } finally {
            Instant end = Instant.now();
            metricService.sendMetric(Duration.between(start, end));
        }
    }

    @Override
    public void saveData(String key, String data) {
        Instant start = Instant.now();
        try {
            delegate.saveData(key, data);
        } finally {
            Instant end = Instant.now();
            metricService.sendMetric(Duration.between(start, end));
        }
    }

    @Override
    public boolean deleteData(String key) {
        Instant start = Instant.now();
        try {
            return delegate.deleteData(key);
        } finally {
            Instant end = Instant.now();
            metricService.sendMetric(Duration.between(start, end));
        }
    }

    public static class MetricService {
        public void sendMetric(Duration duration) {
            System.out.println("Метод выполнялся: " + duration.toString());
        }
    }
}
