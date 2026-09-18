package com.cache.server;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
public class CacheController {

    private static final Duration EXPIRATION_DURATION = Duration.ofDays(3);

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredEntries() {
        Instant now = Instant.now();
        cache.entrySet().removeIf(entry ->
                Duration.between(entry.getValue().lastAccessedAt(), now).compareTo(EXPIRATION_DURATION) > 0
        );
    }

    private final ConcurrentMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @GetMapping("/setcache")
    public String setCache(@RequestParam String key, @RequestParam String value) {
        cache.put(key, new CacheEntry(value, Instant.now()));
        return "Başarılı! '" + key + "' anahtarına '" + value + "' değeri hafızaya kaydedildi.";
    }

    @GetMapping("/getcache")
    public String getCache(@RequestParam String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return "Hata: '" + key + "' adında bir kayıt bulunamadı!";
        }

        Duration idleTime = Duration.between(entry.lastAccessedAt(), Instant.now());
        if (idleTime.compareTo(EXPIRATION_DURATION) > 0) {
            cache.remove(key);
            return "Hata: '" + key + "' adında bir kayıt bulunamadı!";
        }

        cache.put(key, new CacheEntry(entry.value(), Instant.now()));
        return "Sonuç: " + key + " = " + entry.value();
    }

    @GetMapping("/deletecache")
    public String deleteCache(@RequestParam String key) {
        if (!cache.containsKey(key)) {
            return "Hata: Silinmek istenen '" + key + "' adında bir kayıt zaten yok!";
        }
        cache.remove(key);
        return "Başarılı! '" + key + "' anahtarı ve içeriği hafızadan tamamen silindi.";
    }
}