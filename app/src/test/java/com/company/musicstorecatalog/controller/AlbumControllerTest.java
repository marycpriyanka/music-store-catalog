package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.repository.AlbumRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
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
@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumRepository albumRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Album inputAlbum1;
    private Album inputAlbum2;
    private String inputAlbumJson1;

    private Album outputAlbum1;
    private Album outputAlbum2;
    private String outputAlbum1Json;

    private List<Album> outputAlbumList = new ArrayList<>();
    private String outputAlbumListJson;

    @Before
    public void setUp() throws Exception {
        inputAlbum1 = new Album();
        inputAlbum1.setTitle("Greatest Hits");
        inputAlbum1.setArtistId(1);
        inputAlbum1.setReleaseDate(LocalDate.of(2020, 1, 7));
        inputAlbum1.setLabelId(1);
        inputAlbum1.setListPrice(new BigDecimal("54.99"));

        inputAlbum2 = new Album();
        inputAlbum2.setTitle("Super Hits");
        inputAlbum2.setArtistId(1);
        inputAlbum2.setReleaseDate(LocalDate.of(2022, 11, 12));
        inputAlbum2.setLabelId(1);
        inputAlbum2.setListPrice(new BigDecimal("10.99"));

        inputAlbumJson1 = mapper.writeValueAsString(inputAlbum1);

        outputAlbum1 = new Album();
        outputAlbum1.setAlbumId(1);
        outputAlbum1.setTitle("Greatest Hits");
        outputAlbum1.setArtistId(1);
        outputAlbum1.setReleaseDate(LocalDate.of(2020, 1, 7));
        outputAlbum1.setLabelId(1);
        outputAlbum1.setListPrice(new BigDecimal("54.99"));

        outputAlbum2 = new Album();
        outputAlbum2.setAlbumId(2);
        outputAlbum2.setTitle("Super Hits");
        outputAlbum2.setArtistId(1);
        outputAlbum2.setReleaseDate(LocalDate.of(2022, 11, 12));
        outputAlbum2.setLabelId(1);
        outputAlbum2.setListPrice(new BigDecimal("10.99"));

        outputAlbum1Json = mapper.writeValueAsString(outputAlbum1);

        outputAlbumList.add(outputAlbum1);
        outputAlbumList.add(outputAlbum2);

        outputAlbumListJson = mapper.writeValueAsString(outputAlbumList);
    }

    @Test
    public void shouldCreateAlbum() throws Exception {
        doReturn(outputAlbum1).when(albumRepository).save(inputAlbum1);

        mockMvc.perform(
                post("/albums")
                        .content(inputAlbumJson1)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputAlbum1Json));
    }

    @Test
    public void shouldReturnErrorOnPostingInvalidAlbumWhenAPropertyIsNull() throws Exception {
        Album album = new Album();
        album.setReleaseDate(LocalDate.of(2020, 1, 7));
        album.setLabelId(1);
        album.setListPrice(new BigDecimal("54.99"));

        String inputAlbumJson = mapper.writeValueAsString(album);

        mockMvc.perform(
                post("/albums")
                        .content(inputAlbumJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnErrorOnPostingInvalidAlbumWhenListPriceIsOtherThan5IntegersAnd2Fraction() throws Exception {
        Album album = new Album();
        album.setTitle("Artist");
        album.setReleaseDate(LocalDate.of(2020, 1, 7));
        album.setLabelId(1);
        album.setListPrice(new BigDecimal("545.969"));

        String inputAlbumJson = mapper.writeValueAsString(album);

        mockMvc.perform(
                        post("/albums")
                                .content(inputAlbumJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldUpdateAlbumById() throws Exception {
        inputAlbum1.setAlbumId(1);
        inputAlbum1.setTitle("New title");

        String inputJson = mapper.writeValueAsString(inputAlbum1);

        mockMvc.perform(
                put("/albums")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnErrorOnUpdatingWhenAPropertyIsMissing() throws Exception {
        Album album = new Album();
        album.setArtistId(1);
        album.setReleaseDate(LocalDate.of(2020, 1, 7));
        album.setLabelId(1);
        album.setListPrice(new BigDecimal("54.99"));

        String inputAlbumJson = mapper.writeValueAsString(album);

        mockMvc.perform(
                put("/albums")
                        .content(inputAlbumJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnErrorOnUpdatingWhenListPriceIsMoreThan2Fractions() throws Exception {
        Album album = new Album();
        album.setArtistId(1);
        album.setTitle("New title");
        album.setReleaseDate(LocalDate.of(2020, 1, 7));
        album.setLabelId(1);
        album.setListPrice(new BigDecimal("54.999"));

        String inputAlbumJson = mapper.writeValueAsString(album);

        mockMvc.perform(
                        put("/albums")
                                .content(inputAlbumJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllAlbums() throws Exception {
        doReturn(outputAlbumList).when(albumRepository).findAll();

        mockMvc.perform(get("/albums"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputAlbumListJson));
    }

    @Test
    public void shouldReturnAlbumById() throws Exception {
        doReturn(Optional.of(outputAlbum1)).when(albumRepository).findById(1);

        mockMvc.perform(get("/albums/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(outputAlbum1Json));
    }

    @Test
    public void shouldDeleteAlbumById() throws Exception {
        mockMvc.perform(delete("/albums/2"))
                .andExpect(status().isNoContent());

        verify(albumRepository).deleteById(2);
    }
}