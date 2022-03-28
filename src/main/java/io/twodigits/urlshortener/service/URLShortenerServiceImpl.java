package io.twodigits.urlshortener.service;

import io.twodigits.urlshortener.model.URL;
import io.twodigits.urlshortener.repository.URLRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class URLShortenerServiceImpl implements URLShortenerService {
    @Autowired
    private URLRepository repository;

    @Override
    public Iterable<URL> listURLs(String user) {
        return repository.findByUser(user);
    }

    @Override
    public URL addURL(String user, String url) {
        // TODO: Implement
        String id = new DigestUtils ("SHA3-256").digestAsHex(url);
        URL alias = new URL();
        alias.setUser(user);
        alias.setUrl(url);
        alias.setId(id);
        return repository.save(alias);

    }

    @Override
    public Optional<URL> getURL(String user, String id) {
        return repository.findByIdAndUser(id, user);
    }

    @Override
    public Optional<URL> getURL(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteURL(String user, String id) {
        Optional<URL> url = repository.findByIdAndUser(id, user);
        if(url.isPresent()) {
            repository.delete(url.get());
        }
    }
}
