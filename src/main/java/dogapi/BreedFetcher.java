package dogapi;

import java.util.List;

/**
 * Interface for the service of getting sub breeds of a given dog breed.
 */
public interface BreedFetcher {

    /**
     * Fetch the list of sub breeds for the given breed.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist
     */
    List<String> getSubBreeds(String breed) throws BreedNotFoundException;

    /** CHECKED exception (extends Exception, not RuntimeException). */
    class BreedNotFoundException extends Exception {
        public BreedNotFoundException() { super(); }
        public BreedNotFoundException(String message) { super(message); }
        public BreedNotFoundException(String message, Throwable cause) { super(message, cause); }
    }
}