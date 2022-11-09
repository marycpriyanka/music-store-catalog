package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlbumRepositoryTest {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Before
    public void setUp() throws Exception {
        trackRepository.deleteAll();
        albumRepository.deleteAll();
        labelRepository.deleteAll();
        artistRepository.deleteAll();
    }

    // Testcase for Create, Read by id, and Delete by id
    @Test
    public void shouldCreateReadDeleteAlbum() {
        // Need to create a label and artist first
        Label label = new Label();
        label.setName("Immortal Records");
        label.setWebsite("www.immortalRecords.com");
        label = labelRepository.save(label);

        Artist artist = new Artist();
        artist.setName("Vampire Weekend");
        artist.setInstagram("@modernVampires");
        artist.setTwitter("@vampire_weekend");
        artist = artistRepository.save(artist);

        Album album = new Album();
        album.setTitle("Greatest Hits");
        album.setArtistId(artist.getArtistId());
        album.setReleaseDate(LocalDate.of(2020, 1, 7));
        album.setLabelId(label.getLabelId());
        album.setListPrice(new BigDecimal("54.99"));
        album = albumRepository.save(album);

        int id = album.getAlbumId();

        Optional<Album> album1 = albumRepository.findById(id);
        assertEquals(album, album1.get());

        albumRepository.deleteById(id);
        assertEquals(false, albumRepository.existsById(id));
    }

    //Test for Read all
    @Test
    public void shouldReadAllAlbums() {
        Label label = new Label();
        label.setName("Immortal Records");
        label.setWebsite("www.immortalRecords.com");
        label = labelRepository.save(label);

        Artist artist = new Artist();
        artist.setName("Vampire Weekend");
        artist.setInstagram("@modernVampires");
        artist.setTwitter("@vampire_weekend");
        artist = artistRepository.save(artist);

        Album album = new Album();
        album.setTitle("Greatest Hits");
        album.setArtistId(artist.getArtistId());
        album.setReleaseDate(LocalDate.of(2020, 1, 7));
        album.setLabelId(label.getLabelId());
        album.setListPrice(new BigDecimal("54.99"));
        album = albumRepository.save(album);

        album = new Album();
        album.setTitle("Album2");
        album.setArtistId(artist.getArtistId());
        album.setReleaseDate(LocalDate.of(2022, 11, 12));
        album.setLabelId(label.getLabelId());
        album.setListPrice(new BigDecimal("10.99"));
        album = albumRepository.save(album);

        List<Album> albumList = albumRepository.findAll();
        assertEquals(2, albumList.size());
    }

    // Test for Update
    @Test
    public void shouldUpdateAlbum() {
        final String newTitle = "New title";

        Label label = new Label();
        label.setName("Immortal Records");
        label.setWebsite("www.immortalRecords.com");
        label = labelRepository.save(label);

        Artist artist = new Artist();
        artist.setName("Vampire Weekend");
        artist.setInstagram("@modernVampires");
        artist.setTwitter("@vampire_weekend");
        artist = artistRepository.save(artist);

        Album album = new Album();
        album.setTitle("Greatest Hits");
        album.setArtistId(artist.getArtistId());
        album.setReleaseDate(LocalDate.of(2020, 1, 7));
        album.setLabelId(label.getLabelId());
        album.setListPrice(new BigDecimal("54.99"));
        album = albumRepository.save(album);

        album.setTitle(newTitle);
        albumRepository.save(album);

        Optional<Album> updatedAlbum = albumRepository.findById(album.getAlbumId());
        assertEquals(updatedAlbum.get(), album);
        assertEquals(updatedAlbum.get().getTitle(), newTitle);
    }
}