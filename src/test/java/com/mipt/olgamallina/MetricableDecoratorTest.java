package com.mipt.olgamallina;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class MetricableDecoratorTest {

    static class TestMetricService extends MetricableDecorator.MetricService {
        boolean called = false;
        Duration lastDuration;

        @Override
        public void sendMetric(Duration duration) {
            called = true;
            lastDuration = duration;
        }
    }

    @Test
    void shouldSendMetricOnSave() {
        SimpleDataService base = new SimpleDataService();
        TestMetricService metricService = new TestMetricService();

        DataService metric = new MetricableDecorator(base, metricService);

        metric.saveData("k", "v");

        assertTrue(metricService.called);
        assertNotNull(metricService.lastDuration);
        assertFalse(metricService.lastDuration.isNegative());
    }

    @Test
    void shouldSendMetricOnFind() {
        SimpleDataService base = new SimpleDataService();
        base.saveData("k", "v");
        TestMetricService metricService = new TestMetricService();

        DataService metric = new MetricableDecorator(base, metricService);

        metric.findDataByKey("k");

        assertTrue(metricService.called);
    }
}
