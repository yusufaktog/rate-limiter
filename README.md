
# 🚦 Rate Limiter for Spring Boot

A lightweight, flexible rate-limiting library for Spring Boot applications.  
Supports global, per-user, or per-endpoint request throttling using simple interface-based configuration — with zero third-party dependencies.

## 📦 Installation (via JitPack)

1. **Add JitPack repository** to your `pom.xml`:

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

2. **Add the dependency**:

```xml
<dependency>
  <groupId>com.github.yusufaktog</groupId>
  <artifactId>rate-limiter</artifactId>
  <version>1.3.1</version>
</dependency>
```

> 🏷 Replace `1.3.1` with the latest [release version](https://jitpack.io/#yusufaktog/rate-limiter).

---

## ⚙️ Usage

### 1. Implement `RateLimitConfigurer` in your application

```java
@Component
public class RateLimitConfiguration implements RateLimitConfigurer {

    @Override
    public Duration configureRateLimitDuration() {
        return Duration.ofMinutes(1);
    }

    @Override
    public int configureRateLimit() {
        return 5;
    }
}
```

### 2. Done! ✅

No need for any filter registration. If your app includes a Spring bean implementing `RateLimitConfigurer`, the rate limiting will be automatically applied to all HTTP endpoints.

---

## 🔧 Advanced Configuration

You can override default behavior with these optional methods:

```java
public interface RateLimitConfigurer {

    Duration configureRateLimitDuration();       // Time window (e.g., 1 minute)
    int configureRateLimit();                    // Max allowed requests in the window

    default String getRateLimitKey(HttpServletRequest request) {
        return request.getRemoteAddr();          // Key by IP (can customize per user, route, etc.)
    }

    default Duration configureRateLimitDurationByKey(String key) {
        return configureRateLimitDuration();     // Different durations per key if needed
    }

    default int configureRateLimitByKey(String key) {
        return configureRateLimit();             // Different limits per key
    }

    default boolean enableRateLimitResponseHeaders() {
        return true;                             // Add X-RateLimit headers to the response
    }

    default int getCurrentCount(String key) {
        return 0;                                // Optional for introspection/debug
    }
}
```

---

## 🧾 Response Headers (Optional)

If enabled (`enableRateLimitResponseHeaders()`), the filter adds headers to each response:

| Header | Description |
|--------|-------------|
| `X-RateLimit-Limit` | Max allowed requests |
| `X-RateLimit-Remaining` | Requests left in the current window |
| `X-RateLimit-Duration` | Rate limit window duration |
| `X-RateLimit-Reset` | When the window will reset |

---

## 🪶 Lightweight and Dependency-Free

✅ No third-party libraries  
✅ No Spring Boot starters required  
✅ Just plug, implement, and go

---

## 💡 Example Use Cases

- 🔐 Per-IP or per-user rate limiting
- 📊 Throttling expensive API endpoints
- 🧪 Custom rules by route, header, or user role

---

## 🚀 Future Plans

- [ ] Annotation support (`@RateLimit(5)`, `@RateLimitDuration(1, ChronoUnit.MINUTES)`)

---

## 🧑‍💻 Author

**Yusuf Aktoğ**  
[GitHub](https://github.com/yusufaktog)

