package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

        try {
            if (breed == null || breed.isBlank()) {
                throw new BreedFetcher.BreedNotFoundException("Breed is blank");
            }

            String url = "https://dog.ceo/api/breed/"
                    + breed.trim().toLowerCase(Locale.ROOT)
                    + "/list";

            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.body() == null) {
                    throw new BreedFetcher.BreedNotFoundException("Empty API response");
                }

                String body = response.body().string();


                if (!response.isSuccessful()) {
                    throw new BreedFetcher.BreedNotFoundException("Breed not found: " + breed);
                }

                JSONObject json = new JSONObject(body);
                String status = json.optString("status", "");

                if ("success".equalsIgnoreCase(status)) {
                    JSONArray arr = json.optJSONArray("message");
                    List<String> result = new ArrayList<>();
                    if (arr != null) {
                        for (int i = 0; i < arr.length(); i++) {
                            result.add(arr.getString(i));
                        }
                    }
                    return result;
                } else {

                    throw new BreedFetcher.BreedNotFoundException("Breed not found: " + breed);
                }
            }
        } catch (BreedFetcher.BreedNotFoundException e) {
            throw e;
        } catch (Exception e) {

            throw new BreedFetcher.BreedNotFoundException("Failed to fetch sub-breeds for: " + breed);
        }
    }
}