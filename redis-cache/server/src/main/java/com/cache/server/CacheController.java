package com.cache.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
public class CacheController {

    // İŞTE EKSİK OLAN SİHİRLİ DEFTER (Bunu eklediğimizde tüm kırmızı çizgiler sönecek)
    private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();

    // 1. KAYDETME KAPISI
    @GetMapping("/setcache")
    public String setCache(@RequestParam String key, @RequestParam String value) {
        cache.put(key, value);
        return "Başarılı! '" + key + "' anahtarına '" + value + "' değeri hafızaya kaydedildi.";
    }

    // 2. OKUMA KAPISI
    @GetMapping("/getcache")
    public String getCache(@RequestParam String key) {
        String value = cache.get(key);
        if (value == null) {
            return "Hata: '" + key + "' adında bir kayıt bulunamadı!";
        }
        return "Sonuç: " + key + " = " + value;
    }

    // 3. SİLME KAPISI
    @GetMapping("/deletecache")
    public String deleteCache(@RequestParam String key) {
        if (!cache.containsKey(key)) {
            return "Hata: Silinmek istenen '" + key + "' adında bir kayıt zaten yok!";
        }
        cache.remove(key);
        return "Başarılı! '" + key + "' anahtarı ve içeriği hafızadan tamamen silindi.";
    }
}
