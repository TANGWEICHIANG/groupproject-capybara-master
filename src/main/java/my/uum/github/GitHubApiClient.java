package my.uum.github;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class provides methods to fetch GitHub issues using the GitHub API.
 * It utilizes the RestTemplate for making HTTP requests.
 * Requires a GitHub personal access token for authentication.
 *
 * @author groupCapybara
 * @version 1.0
 * @since 2024-01-21
 */
public class GitHubApiClient {

    private static final String GITHUB_TOKEN = "your github token";

    /**
     * Fetches GitHub issues from the specified URL.
     *
     * @param url The URL of the GitHub issues.
     * @return The JSON response containing the GitHub issues.
     * @throws URISyntaxException If the URL is not a valid URI.
     */
    public static String fetchGitHubIssues(String url) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + GITHUB_TOKEN);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI(url));

        ResponseEntity<String> responseEntity = new RestTemplate().exchange(requestEntity, String.class);

        return responseEntity.getBody();
    }

    /**
     * Fetches GitHub issues from another specified URL.
     *
     * @param url The URL of the GitHub issues.
     * @return The JSON response containing the GitHub issues.
     * @throws URISyntaxException If the URL is not a valid URI.
     */
    public static String fetchGitHubIssues2(String url) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + GITHUB_TOKEN);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI(url));

        ResponseEntity<String> responseEntity = new RestTemplate().exchange(requestEntity, String.class);

        return responseEntity.getBody();
    }

    /**
     * Main method to demonstrate fetching GitHub issues and pretty-printing the JSON response.
     *
     * @param args Command-line arguments (not used).
     * @throws URISyntaxException If there is an issue with the URI syntax.
     */
    public static void main(String[] args) throws URISyntaxException {
        String issueCommentsUrl = "https://api.github.com/repos/TANGWEICHIANG/KafkaAPITest/issues/1/comments";
        String issueCommentsUrl2 = "https://api.github.com/repos/pavlovcik/ubiquibot/issues/30/comments";

        String issuesJson = fetchGitHubIssues(issueCommentsUrl);
        String issuesJson2 = fetchGitHubIssues2(issueCommentsUrl2);

        // Use Gson for pretty-printing
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(gson.fromJson(issuesJson, Object.class));
        String prettyJson2 = gson2.toJson(gson.fromJson(issuesJson2, Object.class));

        System.out.println(prettyJson);
        System.out.println(prettyJson2);
    }
}
