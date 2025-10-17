package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached.
 * The cache maps the name of a breed (normalized, lowercase/trimmed) to its list of sub-breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.delegate = Objects.requireNonNull(fetcher);
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String key = normalize(breed);

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        callsMade++;
        List<String> result = delegate.getSubBreeds(breed);

        List<String> immutable = Collections.unmodifiableList(new ArrayList<>(result));
        cache.put(key, immutable);
        return immutable;
    }

    public int getCallsMade() {
        return callsMade;
    }

    private static String normalize(String breed) {
        if (breed == null) return "";
        return breed.trim().toLowerCase(Locale.ROOT);
    }
}