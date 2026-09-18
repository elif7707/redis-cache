package com.cache.server;
import java.time.Instant;


public record CacheEntry(String value, Instant lastAccessedAt) {}