package io.javabrains.moviecatalogservice.controller;

import com.netflix.discovery.DiscoveryClient;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    private WebClient.Builder webclientBuilder;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratings/users/" + userId, UserRating.class);

        return userRating.getRatingList().stream().map(rating -> {
            /*       Movie movie = webclientBuilder.build()
                    .get()
                    .uri("http://localhost:8097/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block(); **/
             Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
             return new CatalogItem(movie.getName(), "Test", rating.getRating());
      }).collect(Collectors.toList());
    }

}
