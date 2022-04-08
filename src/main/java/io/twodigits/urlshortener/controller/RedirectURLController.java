package io.twodigits.urlshortener.controller;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.service.URLShortenerService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/url-alias") // goes to th 1st method: createUrl. url object contains id,
// which does bo have to be passed in the body: need to pass only id and username
public class RedirectURLController {
    private final Logger log = LoggerFactory.getLogger(RedirectURLController.class);
    private final URLShortenerService urlShortenerService;
    public RedirectURLController( URLShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    /**
     * {@code POST  /url-shorteners}  : Creates a new url short.
     *
     * @param url the url to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new url or {@code 400 (badRequest)}
     */
    @PostMapping
    public ResponseEntity<URL> createUrl(@RequestBody URL url) throws URISyntaxException {
        log.debug("REST request to save url : {}", url);
        if (url.getId() != null) { // validation whether url id is empty
            return ResponseEntity.badRequest().build();
        }
        // if not null, return ResponseEntity object
        return ResponseEntity.ok(urlShortenerService.addURL(url.getUser(), url.getURL()));
    }

    // Create new alias
    // response method
    @GetMapping
    public ResponseEntity<Iterable<URL>> getAllUrl(@RequestParam String user) {
        if (!StringUtils.hasText(user)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(urlShortenerService.listURLs(user));
    }

    // Bad request: id not found
    @GetMapping("/{id}")
    public ResponseEntity<URL> getURLId ( @PathVariable String id) {
        log.debug("REST request to get Url : {}", id);
        if (!StringUtils.hasText(id)) {
            return ResponseEntity.badRequest().build(); // trying to get record from the data to see whether already exists or not
        }
        Optional<URL> urlOptional = urlShortenerService.getURL(id);
        return wrapOrNotFoundResponse(urlOptional);
    }

    // Bad request: user and id not found
    @GetMapping("/{user}/{id}")
    public ResponseEntity<URL> getURLUserAndId ( @PathVariable String user, @PathVariable String id) {
        log.debug("REST request to get Url : {}", id);
        if (!StringUtils.hasText(id)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<URL> urlOptional = urlShortenerService.getURL(user, id);
        return wrapOrNotFoundResponse(urlOptional);
    }

    private ResponseEntity<URL> wrapOrNotFoundResponse(Optional<URL> urlOptional) {
        return urlOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Bad request: delete empty url
    @DeleteMapping
    public ResponseEntity<Void> removeUrl ( @RequestBody URL url) {
        log.debug("REST request to delete Url: {}", url);
        if (url.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        // when delete() method is called - delete must also be selected in Postman
        urlShortenerService.deleteURL(url.getUser(), url.getId());
        // passing the url
        return ResponseEntity.noContent().build();
    }
}
