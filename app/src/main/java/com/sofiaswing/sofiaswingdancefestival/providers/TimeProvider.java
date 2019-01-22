package com.sofiaswing.sofiaswingdancefestival.providers;

public final class TimeProvider implements ProvidersInterfaces.ITimeProvider {
    @Override
    public long getCurrentTimeMilliseconds() {
        return System.currentTimeMillis();
    }
}
