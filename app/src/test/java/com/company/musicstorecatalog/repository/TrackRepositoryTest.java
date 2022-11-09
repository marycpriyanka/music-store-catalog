package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.model.Track;
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
public class TrackRepositoryTest {
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Before
    public void setUp() throws Exception {
        trackRepository.deleteAll();
        albumRepository.deleteAll();
        labelRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void shouldCreateReadDelete() {
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

        Track track = new Track();
        track.setTitle("track1");
        track.setRunTime(100);
        track.setAlbumId(album.getAlbumId());

        track = trackRepository.save(track);

        Optional<Track> track1 = trackRepository.findById(track.getTrackId());

        assertEquals(track, track1.get());

        trackRepository.deleteById(track.getTrackId());
        track1 = trackRepository.findById(track.getTrackId());

        assertFalse(track1.isPresent());
    }

    @Test
    public void shouldGetAllTracks() {
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

        Track track = new Track();
        track.setTitle("track1");
        track.setRunTime(100);
        track.setAlbumId(album.getAlbumId());
        track = trackRepository.save(track);

        track = new Track();
        track.setTitle("track2");
        track.setRunTime(60);
        track.setAlbumId(album.getAlbumId());
        track = trackRepository.save(track);

        List<Track> trackList = trackRepository.findAll();

        assertEquals(2, trackList.size());
    }

    @Test
    public void shouldUpdateTrack() {
        String newName = "New name";

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

        Track track = new Track();
        track.setTitle("track1");
        track.setRunTime(100);
        track.setAlbumId(album.getAlbumId());
        track = trackRepository.save(track);

        track.setTitle(newName);
        track = trackRepository.save(track);

        Optional<Track> track1 = trackRepository.findById(track.getTrackId());
        assertEquals(track, track1.get());
        assertEquals(track.getTitle(), newName);
    }
}