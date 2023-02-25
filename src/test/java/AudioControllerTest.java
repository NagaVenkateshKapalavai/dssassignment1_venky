import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.controller.AudioController;
import org.example.models.Audio;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioControllerTest {
    @InjectMocks
    private AudioController audioController;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    public final void doGet() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/api/audio");

        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
            response.close();
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public final void doGetLatencies() throws IOException {
        long[] latencies = new long[10];
        long[] test = new long[10];
        for (int i = 0; i < 10; i++) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8080/api/audio");

            try {
                long startTime = System.currentTimeMillis();
                CloseableHttpResponse response = httpclient.execute(httpGet);
//                assertEquals( HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

                long endTime = System.currentTimeMillis();
                long timeTaken = endTime - startTime;
                latencies[i] = timeTaken;
                response.close();
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assertEquals(10, latencies.length);
        assertNotEquals(test, latencies);
        System.out.println("Latencies: ");
        // for loop to print out the array
        for (int i = 0; i < latencies.length; i++) {
            System.out.printf("No: %d = %d ms\n", i + 1, latencies[i]);
        }
    }


    @Test
    public final void doPost() throws IOException {
        try {
            String jsonBody = "{\n" +
                    "    \"name\": \"Beat it\",\n" +
                    "    \"path\": \"/beatit\",\n" +
                    "    \"singer\": \"MJ\",\n" +
                    "    \"album\": \"MJ Forever\",\n" +
                    "    \"genre\": \"Rock\",\n" +
                    "    \"year\": \"1999\",\n" +
                    "    \"duration\": \"3:42\",\n" +
                    "    \"rating\": 1\n" +
                    "}";

            HttpClient httpClient = HttpClientBuilder.create().build();

            // Create the HttpPost request with the JSON object as the request entity
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/audio");
            StringEntity requestEntity = new StringEntity(jsonBody);
            httpPost.setEntity(requestEntity);
            httpPost.setHeader("Content-type", "application/json");

            // Execute the request and get the response
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            // Get the response body as a String
            HttpEntity entity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(entity);

            // Print the response status code and body
            assertEquals(HttpStatus.SC_OK, statusCode);
            System.out.println("Response body: " + responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public final void doPostLatancies() throws IOException {
        try {
            List<Audio> songList = new ArrayList<>();
            List<String> jsonList = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            long[] latencies = new long[10];
            long[] test = new long[10];

            songList.add(new Audio("Beat it", "/beatit", "MJ", "MJ Forever", "Rock", "1999", "3:42", 1));
            songList.add(new Audio("Thriller", "/thriller", "MJ", "MJ Forever", "Pop", "1982", "5:57", 2));
            songList.add(new Audio("Bohemian Rhapsody", "/bohemianrhapsody", "Queen", "A Night at the Opera", "Rock", "1975", "5:54", 5));
            songList.add(new Audio("Hotel California", "/hotelcalifornia", "Eagles", "Hotel California", "Rock", "1977", "6:31", 4));
            songList.add(new Audio("Stairway to Heaven", "/stairwaytoheaven", "Led Zeppelin", "Led Zeppelin IV", "Rock", "1971", "8:02", 5));
            songList.add(new Audio("Like a Rolling Stone", "/likearollingstone", "Bob Dylan", "Highway 61 Revisited", "Rock", "1965", "6:13", 4));
            songList.add(new Audio("Boogie Wonderland", "/boogiewonderland", "Earth, Wind & Fire", "I Am", "Disco", "1979", "4:48", 3));
            songList.add(new Audio("Imagine", "/imagine", "John Lennon", "Imagine", "Rock", "1971", "3:03", 5));
            songList.add(new Audio("Smells Like Teen Spirit", "/smellsliketeenspirit", "Nirvana", "Nevermind", "Grunge", "1991", "5:01", 4));
            songList.add(new Audio("Sweet Child o' Mine", "/sweetchildomine", "Guns N' Roses", "Appetite for Destruction", "Rock", "1987", "5:56", 4));

            for (int i = 0; i < 10; i = i + 1) {
                long startTime = System.currentTimeMillis();
                String json = objectMapper.writeValueAsString(songList.get(i));
                jsonList.add(json);

                CloseableHttpClient httpClient = HttpClients.createDefault();

                // Create the HttpPost request with the JSON object as the request entity
                HttpPost httpPost = new HttpPost("http://localhost:8080/api/audio");
                StringEntity requestEntity = new StringEntity(jsonList.get(i));
                httpPost.setEntity(requestEntity);
                httpPost.setHeader("Content-type", "application/json");

                // Execute the request and get the response
                HttpResponse httpResponse = httpClient.execute(httpPost);
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // Get the response body as a String
                HttpEntity entity = httpResponse.getEntity();
                String responseBody = EntityUtils.toString(entity);

                CloseableHttpResponse response = httpClient.execute(httpPost);

                // Print the response status code and body
                assertEquals(HttpStatus.SC_OK, statusCode);
                if (responseBody != null) {
                    TypeReference<List<Audio>> typeRef = new TypeReference<List<Audio>>() {
                    };
                    List<Audio> responseSongs = objectMapper.readValue(responseBody, typeRef);
                    assertEquals(songList.get(i).getName(), responseSongs.get(responseSongs.size() - 1).getName());
                }
                long endTime = System.currentTimeMillis();
                long timeTaken = endTime - startTime;
                latencies[i] = timeTaken;
                response.close();
                httpClient.close();
            }
            assertEquals(10, latencies.length);
            assertNotEquals(test, latencies);
            System.out.println("Latencies: ");
            // for loop to print out the array
            for (int i = 0; i < latencies.length; i++) {
                System.out.printf("No: %d = %d ms\n", i + 1, latencies[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoGetConcurrentConnections() throws Exception {
        try {
            AtomicInteger successfulRequests = new AtomicInteger();
            List<Long> latencies = new ArrayList<>();

            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
            connManager.setMaxTotal(100);

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();

            ExecutorService executor = Executors.newFixedThreadPool(50);
            for (int i = 0; i < 200; i++) {
                executor.execute(() -> {
                    HttpGet httpGet = new HttpGet("http://localhost:8080/api/audio");
                    try {
                        long startTime = System.currentTimeMillis();
                        CloseableHttpResponse response = httpClient.execute(httpGet);
                        HttpEntity responseEntity = response.getEntity();
                        if (responseEntity != null) {
                            String responseBody = EntityUtils.toString(responseEntity);
                            assertTrue(responseBody.length() > 0);
                            assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                            if (response.getStatusLine().getStatusCode() == 200) {
                                successfulRequests.getAndIncrement();
                            }
                        }
                        response.close();
                        long endTime = System.currentTimeMillis();
                        long timeTaken = endTime - startTime;
                        latencies.add(timeTaken);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.MINUTES);

            assertEquals(200, successfulRequests.get());
            assertTrue(successfulRequests.get() > 100);
            System.out.println("Successful requests: " + successfulRequests.get());
            System.out.println("Latencies average: " + latencies.stream().mapToLong(Long::longValue).average().getAsDouble() + " ms");
            System.out.println("Latencies max: " + latencies.stream().mapToLong(Long::longValue).max().getAsLong() + " ms");
            System.out.println("Latencies min: " + latencies.stream().mapToLong(Long::longValue).min().getAsLong() + " ms");
            System.out.println("Latencies:");
            for (int i = 0; i < latencies.size(); i++) {
                System.out.printf("No: %d = %d ms\n", i + 1, latencies.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoPostConcurrentConnections() throws Exception {
        try {
            AtomicInteger successfulRequests = new AtomicInteger();
            List<Long> latencies = new ArrayList<>();

            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
            connManager.setMaxTotal(100);

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();

            ExecutorService executor = Executors.newFixedThreadPool(50);
            for (int i = 0; i < 200; i++) {
                executor.execute(() -> {
                    HttpPost httpPost = new HttpPost("http://localhost:8080/api/audio");
                    try {
                        String jsonBody = "{\n" +
                                "    \"name\": \"Beat it\",\n" +
                                "    \"path\": \"/beatit\",\n" +
                                "    \"singer\": \"MJ\",\n" +
                                "    \"album\": \"MJ Forever\",\n" +
                                "    \"genre\": \"Rock\",\n" +
                                "    \"year\": \"1999\",\n" +
                                "    \"duration\": \"3:42\",\n" +
                                "    \"rating\": 1\n" +
                                "}";
                        long startTime = System.currentTimeMillis();


                        StringEntity requestEntity = new StringEntity(jsonBody);
                        httpPost.setEntity(requestEntity);
                        httpPost.setHeader("Content-type", "application/json");

                        CloseableHttpResponse response = httpClient.execute(httpPost);
                        long endTime = System.currentTimeMillis();
                        long timeTaken = endTime - startTime;
                        latencies.add(timeTaken);
                        if (response.getStatusLine().getStatusCode() == 200) {
                            successfulRequests.getAndIncrement();
                        }
                        response.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.MINUTES);

            assertEquals(200, successfulRequests.get());
            assertTrue(successfulRequests.get() > 100);
            System.out.println("Successful requests: " + successfulRequests.get());
            System.out.println("Latencies average: " + latencies.stream().mapToLong(Long::longValue).average().getAsDouble() + " ms");
            System.out.println("Latencies max: " + latencies.stream().mapToLong(Long::longValue).max().getAsLong() + " ms");
            System.out.println("Latencies min: " + latencies.stream().mapToLong(Long::longValue).min().getAsLong() + " ms");
            System.out.println("Latencies:");
            for (int i = 0; i < latencies.size(); i++) {
                System.out.printf("No: %d = %d ms\n", i + 1, latencies.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
