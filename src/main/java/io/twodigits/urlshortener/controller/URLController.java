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

    @GetMapping("/{id}")
    public RedirectView alias ( @PathVariable String id, HttpServletRequest request) {
        log.debug("Your alias is: {}", id);
        Optional <URL> url = urlShortenerService.getURL(id);
        RedirectView redirectView = new RedirectView();
        if (url.isEmpty()) {
            return redirectView;
        }
        String alias = url.get().getURL();
        log.debug("Redirecting alias to: {}", alias);
        redirectView.setUrl(alias);
        updateStatistics(url.get(), request);
        return redirectView;
    }

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
        count.setUrl(url);
        statisticsRepository.save(count);
    }
    private String generateId ( String id, String userManager) {
        return String.format("id-%s-%s", id, userManager);
    }
}
