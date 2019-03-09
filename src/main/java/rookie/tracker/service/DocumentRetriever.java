package rookie.tracker.service;

import lombok.NoArgsConstructor;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@NoArgsConstructor
public class DocumentRetriever {

    @SuppressWarnings("deprecation")
    public Document getDocument(String url) {
        try {
            return Jsoup.connect(url).validateTLSCertificates(false).get();
        } catch (HttpStatusException e) {
            System.out.printf("Error: 404 status for URL: %s\n", url);
            return null; // fix
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch(Exception e) {
            return null;
        }
    }

}
