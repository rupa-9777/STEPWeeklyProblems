import java.util.*;

class TokenBucket {
    private int maxTokens;
    private double tokens;
    private double refillRate;
    private long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    private void refill() {
        long now = System.currentTimeMillis();
        double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;
        tokens = Math.min(maxTokens, tokens + tokensToAdd);
        lastRefillTime = now;
    }

    public synchronized boolean allowRequest() {
        refill();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    public synchronized int getRemainingTokens() {
        refill();
        return (int) tokens;
    }

    public synchronized long getRetryAfter() {
        refill();
        if (tokens >= 1) return 0;
        return (long) ((1 - tokens) / refillRate);
    }
}

class RateLimiter {
    private HashMap<String, TokenBucket> clients = new HashMap<>();
    private int maxRequests = 1000;
    private double refillRate = 1000.0 / 3600;

    public synchronized String checkRateLimit(String clientId) {
        clients.putIfAbsent(clientId, new TokenBucket(maxRequests, refillRate));
        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.getRemainingTokens() + " remaining)";
        } else {
            return "Denied (retry after " + bucket.getRetryAfter() + "s)";
        }
    }

    public synchronized String getRateLimitStatus(String clientId) {
        clients.putIfAbsent(clientId, new TokenBucket(maxRequests, refillRate));
        TokenBucket bucket = clients.get(clientId);

        int remaining = bucket.getRemainingTokens();
        int used = maxRequests - remaining;

        return "{used: " + used + ", limit: " + maxRequests + "}";
    }
}

public class WeeklyProblems {
    public static void main(String[] args) {
        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.checkRateLimit(client));
        }

        System.out.println(limiter.getRateLimitStatus(client));
    }
}