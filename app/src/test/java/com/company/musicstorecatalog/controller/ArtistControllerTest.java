package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.repository.ArtistRepository;
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
@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistRepository artistRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Artist inputArtist1;
    private Artist inputArtist2;

    private String inputArtist1Json;

    private Artist outputArtist1;
    private Artist outputArtist2;
    private String outputArtist1Json;

    private List<Artist> outputArtistList = new ArrayList<>();
    private String outputArtistListJson;

    @Before
    public void setUp() throws Exception {
        inputArtist1 = new Artist();
        inputArtist1.setName("Vampire Weekend");
        inputArtist1.setInstagram("@modernVampires");
        inputArtist1.setTwitter("@vampire_weekend");

        inputArtist1Json = mapper.writeValueAsString(inputArtist1);

        inputArtist2 = new Artist();
        inputArtist2.setName("Rock star");
        inputArtist2.setInstagram("@RockStar");
        inputArtist2.setTwitter("@rock_star");

        outputArtist1 = new Artist();
        outputArtist1.setArtistId(1);
        outputArtist1.setName("Vampire Weekend");
        outputArtist1.setInstagram("@modernVampires");
        outputArtist1.setTwitter("@vampire_weekend");

        outputArtist1Json = mapper.writeValueAsString(outputArtist1);

        outputArtist2 = new Artist();
        outputArtist2.setArtistId(2);
        outputArtist2.setName("Rock star");
        outputArtist2.setInstagram("@RockStar");
        outputArtist2.setTwitter("@rock_star");

        outputArtistList.add(outputArtist1);
        outputArtistList.add(outputArtist2);

        outputArtistListJson = mapper.writeValueAsString(outputArtistList);
    }

    @Test
    public void shouldCreateArtist() throws Exception {
        doReturn(outputArtist1).when(artistRepository).save(inputArtist1);

        mockMvc.perform(
                post("/artists")
                        .content(inputArtist1Json)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputArtist1Json));
    }

    @Test
    public void shouldReturnErrorOnPostingInvalidArtistWhenAPropertyIsNull() throws Exception {
        Artist inputArtist = new Artist();
        inputArtist.setName("Vampire Weekend");
        inputArtist.setInstagram("@modernVampires");

        String inputArtistJson = mapper.writeValueAsString(inputArtist);

        mockMvc.perform(
                post("/artists")
                        .content(inputArtistJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldUpdateArtistById() throws Exception {
        inputArtist1.setArtistId(1);
        inputArtist1.setName("New artist name");

        String inputJson = mapper.writeValueAsString(inputArtist1);

        mockMvc.perform(
                put("/artists")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnErrorOnUpdatingArtistWhenAPropertyIsNull() throws Exception {
        Artist inputArtist = new Artist();
        inputArtist.setArtistId(1);
        inputArtist.setName("New artist name");
        inputArtist.setInstagram("@modernVampires");

        String inputArtistJson = mapper.writeValueAsString(inputArtist);

        mockMvc.perform(
                        put("/artists")
                                .content(inputArtistJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllGames() throws Exception {
        doReturn(outputArtistList).when(artistRepository).findAll();

        mockMvc.perform(get("/artists"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputArtistListJson));
    }

    @Test
    public void shouldReturnArtistById() throws Exception {
        doReturn(Optional.of(outputArtist1)).when(artistRepository).findById(1);

        mockMvc.perform(get("/artists/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputArtist1Json));
    }

    @Test
    public void shouldDeleteArtistById() throws Exception {
        mockMvc.perform(delete("/artists/1"))
                .andExpect(status().isNoContent());

        verify(artistRepository).deleteById(1);
    }
}