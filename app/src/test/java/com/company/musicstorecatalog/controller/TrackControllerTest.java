package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.repository.TrackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TrackController.class)
public class TrackControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackRepository trackRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Track inputTrack1;
    private Track inputTrack2;
    private String inputTrack1Json;

    private Track outputTrack1;
    private Track outputTrack2;
    private String outputTrack1Json;

    private List<Track> outputTrackList = new ArrayList<>();
    private String outputTrackListJson;

    @Before
    public void setUp() throws Exception {
        inputTrack1 = new Track();
        inputTrack1.setTitle("track1");
        inputTrack1.setRunTime(100);
        inputTrack1.setAlbumId(1);

        inputTrack2 = new Track();
        inputTrack2.setTitle("track2");
        inputTrack2.setRunTime(80);
        inputTrack2.setAlbumId(1);

        inputTrack1Json = mapper.writeValueAsString(inputTrack1);

        outputTrack1 = new Track();
        outputTrack1.setTrackId(1);
        outputTrack1.setTitle("track1");
        outputTrack1.setRunTime(100);
        outputTrack1.setAlbumId(1);

        outputTrack2 = new Track();
        outputTrack2.setTrackId(2);
        outputTrack2.setTitle("track2");
        outputTrack2.setRunTime(80);
        outputTrack2.setAlbumId(1);

        outputTrack1Json = mapper.writeValueAsString(outputTrack1);

        outputTrackList.add(outputTrack1);
        outputTrackList.add(outputTrack2);

        outputTrackListJson = mapper.writeValueAsString(outputTrackList);
    }

    @Test
    public void shouldCreateTrack() throws Exception {
        doReturn(outputTrack1).when(trackRepository).save(inputTrack1);

        mockMvc.perform(
                post("/tracks")
                        .content(inputTrack1Json)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputTrack1Json));
    }

    @Test
    public void shouldReturnErrorOnPostingInvalidTrackWhenAPropertyIsNull() throws Exception {
        Track inputTrack = new Track();
        inputTrack.setAlbumId(1);
        inputTrack.setRunTime(100);


        String inputTrackJson = mapper.writeValueAsString(inputTrack);

        mockMvc.perform(
                post("/tracks")
                        .content(inputTrackJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldUpdateAlbumById() throws Exception {
        inputTrack1.setTrackId(1);
        inputTrack1.setTitle("New title");

        String inputJson = mapper.writeValueAsString(inputTrack1);

        mockMvc.perform(
                put("/tracks")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnErrorOnUpdatingInvalidTrackWhenAPropertyIsNull() throws Exception {
        Track inputTrack = new Track();
        inputTrack.setTrackId(1);
        inputTrack.setAlbumId(1);
        inputTrack.setRunTime(100);

        String inputTrackJson = mapper.writeValueAsString(inputTrack);

        mockMvc.perform(
                        put("/tracks")
                                .content(inputTrackJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllTracks() throws Exception {
        doReturn(outputTrackList).when(trackRepository).findAll();

        mockMvc.perform(get("/tracks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputTrackListJson));
    }

    @Test
    public void shouldReturnTrackById() throws Exception {
        doReturn(Optional.of(outputTrack1)).when(trackRepository).findById(1);

        mockMvc.perform(get("/tracks/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputTrack1Json));
    }

    @Test
    public void shouldDeleteTrackById() throws Exception {
        mockMvc.perform(delete("/tracks/2"))
                .andExpect(status().isNoContent());

        verify(trackRepository).deleteById(2);
    }
}