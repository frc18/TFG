package tfg;


import java.io.IOException;
import java.text.DecimalFormat;

import org.json.*;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class ChatGPT {

    private static final String API_URL = "https://api.openai.com/v1/completions";
    private static final String AUTH_TOKEN = "sk-XXXXXXXXXXX";
    private static final String MODEL_NAME = "text-davinci-003";
    private static final int MAX_TOKENS = 1000;
    private static final double TEMPERATURE = 1.0;

    public static String chatGPT(String text) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(API_URL);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", "Bearer " + AUTH_TOKEN);

        JSONObject data = new JSONObject();
        data.put("model", MODEL_NAME);
        data.put("prompt", text);
        data.put("max_tokens", MAX_TOKENS);
        data.put("temperature", TEMPERATURE);

        StringEntity entity = new StringEntity(data.toString());
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity);

        String line = null;

        line ="[" + responseString + "]";

        JSONArray array = new JSONArray(line);
        System.out.println(line);
        JSONObject object = array.getJSONObject(0);

        String texto = object.getJSONArray("choices").getJSONObject(0).getString("text");

        httpClient.close();

        return texto;
    }


    public static void analyze(String text) {
        // Set up properties for Stanford CoreNLP
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        // Create an annotation object for the text
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);
        
        // Get the sentiment score for each sentence
        double totalSentiment = 0;
        int sentenceCount = 0;
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            double score = RNNCoreAnnotations.getPredictedClass(sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class));
            
            /*if (sentiment.equals("Negative")) {
                score = -score;
            }*/
            System.out.println("New: " + sentiment + " (" + score + ")");
            
            totalSentiment += score;
            sentenceCount++;
        }
        
        // Calculate the average sentiment score
        double averageSentiment = totalSentiment / sentenceCount;

        
        DecimalFormat formato = new DecimalFormat("#.#");
        
        String resultado = formato.format(averageSentiment);
        

        
        // Determine if the text is corroborating or refuting
        if (averageSentiment > 2.2) {
            System.out.println("Corroborando");
            MiVentana.colorTexto("Corroborando", resultado);
        } else if (averageSentiment < 1.7) {
            System.out.println("Desmintiendo");
            MiVentana.colorTexto("Desmintiendo", resultado);
        } else {
            System.out.println("Neutral");
            MiVentana.colorTexto("Neutral", resultado);
        }
    }

}
