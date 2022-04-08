package io.twodigits.urlshortener.controller;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.model.URLStatistics;
import io.twodigits.urlshortener.repository.StatisticsRepository;
import io.twodigits.urlshortener.service.URLShortenerService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class URLController {

    private final Logger log = LoggerFactory.getLogger(RedirectURLController.class);

    private final URLShortenerService urlShortenerService;
    private final StatisticsRepository statisticsRepository;

    public URLController(URLShortenerService urlShortenerService, StatisticsRepository statisticsRepository) {
        this.urlShortenerService = urlShortenerService;
        this.statisticsRepository = statisticsRepository;
    }

    @GetMapping("/urlcreator/{id}") // getting the id from the parameter
    // PathVariable means it is coming in the url /{PathVariable}
    public RedirectView alias ( @PathVariable String id, HttpServletRequest request) {

        RedirectView redirectView = new RedirectView();
        try {
            log.debug("Your alias is: {}", id);
            // fetching the url record from the database - Service method will make a call to the database
            // database will return a url class
            Optional <URL> url = urlShortenerService.getURL(id);

            // checking whether URL is empty or not - trying to fetch recording for this URL.
            // if it does not exist - return exception
            if (url.isEmpty()) {
                return redirectView;
            }
            // if url exists, get it. using the get() method first to get an object
            // and then getURL() method to read the url. If it was a normal object, then object.getValue() would be used
            String alias = url.get().getURL(); // getting the url in alias http:// call
            log.debug("Redirecting alias to: {}", alias);
            redirectView.setUrl(alias); // setting the url value in the redirectView class as https://
            updateStatistics(url.get(), request); // updating the statistics, where there is a database class
        }

        catch(Exception exc) {
            System.out.println ("Exception ocurred!!!"); // returning exception if url does not exist
        }

        // returning the object - the redirectReview class - navigates the user to another url via https://
        return redirectView;
    }

    // contains a database class. user can see some stats when they come to this https
    private void updateStatistics(URL url, HttpServletRequest request) {
        String userManager = request.getHeader("User-Manager");
        String id = generateId (url.getId(), userManager);
        Optional<URLStatistics> urlStatistic = statisticsRepository.findById(id);
        urlStatistic.ifPresentOrElse((count) -> {
            alias (url, userManager, count);
        }, () -> {
            URLStatistics aliasStatistics = new URLStatistics();
            aliasStatistics.setId(id);
            alias (url, userManager, aliasStatistics);
        });

    }

    private void alias ( URL url, String userManager, URLStatistics count) {
        int numCalls = 1;
        if (count.getCallCount () != null){
            numCalls = count.getCallCount () + 1;
        }
        count.countUserCalls (numCalls);
        count.setDateTime( LocalDateTime.now());
        count.setUserManager (userManager);
        count.setUrl(url); // if url is not empty, set the url (which is fetched from database) and return it to browser
        statisticsRepository.save(count);
    }
    private String generateId ( String id, String userManager) {
        return String.format("id-%s-%s", id, userManager);
    }
}
