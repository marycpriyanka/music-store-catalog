package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Artist;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArtistRepositoryTest {
    @Autowired
    private ArtistRepository artistRepository;

    @Before
    public void setUp() throws Exception {
        artistRepository.deleteAll();
    }

    @Test
    public void shouldCreateReadDeleteArtist() {
        Artist artist = new Artist();
        artist.setName("Vampire Weekend");
        artist.setInstagram("@modernVampires");
        artist.setTwitter("@vampire_weekend");

        artist = artistRepository.save(artist);

        int id = artist.getArtistId();

        Optional<Artist> artist1 = artistRepository.findById(id);
        assertEquals(artist, artist1.get());

        artistRepository.deleteById(id);
        assertEquals(false, artistRepository.existsById(id));
    }

    @Test
    public void readAllArtists() {
        Artist artist = new Artist();
        artist.setName("Vampire Weekend");
        artist.setInstagram("@modernVampires");
        artist.setTwitter("@vampire_weekend");

        artist = artistRepository.save(artist);

        artist = new Artist();
        artist.setName("Rock star");
        artist.setInstagram("@RockStar");
        artist.setTwitter("@rock_star");

        artist = artistRepository.save(artist);

        List<Artist> artistList = artistRepository.findAll();
        assertEquals(2, artistList.size());
    }

    @Test
    public void shouldUpdateArtist() {
        String newName = "New name";

        Artist artist = new Artist();
        artist.setName("Vampire Weekend");
        artist.setInstagram("@modernVampires");
        artist.setTwitter("@vampire_weekend");

        artist = artistRepository.save(artist);

        artist.setName(newName);
        artist = artistRepository.save(artist);

        Optional<Artist> artist1 = artistRepository.findById(artist.getArtistId());
        assertEquals(artist, artist1.get());
        assertEquals(artist.getName(), newName);
    }
}