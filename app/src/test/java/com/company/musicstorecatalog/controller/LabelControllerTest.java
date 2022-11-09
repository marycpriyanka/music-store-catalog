package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.repository.LabelRepository;
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
@WebMvcTest(LabelController.class)
public class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LabelRepository labelRepository;

    ObjectMapper mapper = new ObjectMapper();

    private Label inputLabel1;
    private Label inputLabel2;
    private String inputLabel1Json;

    private Label outputLabel1;
    private Label outputLabel2;
    private String outputLabel1Json;
    private List<Label> outputLabelList = new ArrayList<>();
    private String outputLabelListJson;

    @Before
    public void setUp() throws Exception {
        inputLabel1 = new Label();
        inputLabel1.setName("Immortal Records");
        inputLabel1.setWebsite("www.immortalRecords.com");

        inputLabel2 = new Label();
        inputLabel2.setName("Island");
        inputLabel2.setWebsite("www.island.com");

        inputLabel1Json = mapper.writeValueAsString(inputLabel1);

        outputLabel1 = new Label();
        outputLabel1.setLabelId(1);
        outputLabel1.setName("Immortal Records");
        outputLabel1.setWebsite("www.immortalRecords.com");

        outputLabel2 = new Label();
        outputLabel2.setLabelId(2);
        outputLabel2.setName("Island");
        outputLabel2.setWebsite("www.island.com");

        outputLabel1Json = mapper.writeValueAsString(outputLabel1);

        outputLabelList.add(outputLabel1);
        outputLabelList.add(outputLabel2);

        outputLabelListJson = mapper.writeValueAsString(outputLabelList);
    }

    @Test
    public void shouldCreateLabel() throws Exception {
        doReturn(outputLabel1).when(labelRepository).save(inputLabel1);

        mockMvc.perform(
                post("/labels")
                        .content(inputLabel1Json)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputLabel1Json));
    }

    @Test
    public void shouldReturnErrorOnPostingInvalidLabelInputWhenAPropertyIsNull() throws Exception {
        Label inputLabel = new Label();
        inputLabel.setName("Island");

        String inputLabelJson = mapper.writeValueAsString(inputLabel);

        mockMvc.perform(
                post("/labels")
                        .content(inputLabelJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldUpdateLabelById() throws Exception {
        inputLabel1.setLabelId(1);
        inputLabel1.setName("New Label");

        String inputJson = mapper.writeValueAsString(inputLabel1);

        mockMvc.perform(
                put("/labels")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnErrorOnUpdatingLabelWhenAPropertyIsNull() throws Exception {
        Label inputLabel = new Label();
        inputLabel.setLabelId(1);
        inputLabel.setName("New Label");

        String inputLabelJson = mapper.writeValueAsString(inputLabel);

        mockMvc.perform(
                put("/labels")
                        .content(inputLabelJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllLabels() throws Exception {
        doReturn(outputLabelList).when(labelRepository).findAll();

        mockMvc.perform(get("/labels"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputLabelListJson));
    }

    @Test
    public void shouldReturnLabelById() throws Exception {
        doReturn(Optional.of(outputLabel1)).when(labelRepository).findById(1);

        mockMvc.perform(get("/labels/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputLabel1Json));
    }

    @Test
    public void shouldDeleteLabelById() throws Exception {
        mockMvc.perform(delete("/labels/2"))
                .andExpect(status().isNoContent());

        verify(labelRepository).deleteById(2);
    }
}