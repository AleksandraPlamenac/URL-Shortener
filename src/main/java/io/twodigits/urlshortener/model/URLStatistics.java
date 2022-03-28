package io.twodigits.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class URLStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private Integer callCount;
    private LocalDateTime dateTime;
    private String userManager;
    private String counter;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="url_id", nullable=false)
    private URL url;

    public Integer getCallCount () {
        return callCount;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void countUserCalls ( Integer callCount) {
        this.callCount = callCount;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime localDateTime) {
        this.dateTime = localDateTime;
    }
    public String getUserManager () {
        return userManager;
    }
    public void setUserManager ( String userManager ) {
        this.userManager = userManager;
    }
    public String getCounter () {
        return counter;
    }
    public void setCounter ( String counter ) {
        this.counter = counter;
    }
    public URL getUrl() {
        return url;
    }
    public void setUrl(URL url) {
        this.url = url;
    }

}
