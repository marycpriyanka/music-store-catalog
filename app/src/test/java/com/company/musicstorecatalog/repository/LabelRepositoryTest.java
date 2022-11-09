package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
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
public class LabelRepositoryTest {
    @Autowired
    private LabelRepository labelRepository;

    @Before
    public void setUp() throws Exception {
        labelRepository.deleteAll();
    }

    @Test
    public void shouldCreateReadDeleteLabel() {
        Label label = new Label();
        label.setName("Immortal Records");
        label.setWebsite("www.immortalRecords.com");

        label = labelRepository.save(label);

        int id = label.getLabelId();

        Optional<Label> label1 = labelRepository.findById(id);
        assertEquals(label, label1.get());

        labelRepository.deleteById(id);
        assertEquals(false, labelRepository.existsById(id));
    }

    @Test
    public void readAllLabels() {
        Label label = new Label();
        label.setName("Immortal Records");
        label.setWebsite("www.immortalRecords.com");

        label = labelRepository.save(label);

        label = new Label();
        label.setName("Island");
        label.setWebsite("www.island.com");

        label = labelRepository.save(label);

        List<Label> labelList = labelRepository.findAll();
        assertEquals(2, labelList.size());
    }

    @Test
    public void shouldUpdateLabel() {
        String newName = "New name";

        Label label = new Label();
        label.setName("Immortal Records");
        label.setWebsite("www.immortalRecords.com");

        label = labelRepository.save(label);

        label.setName(newName);
        label = labelRepository.save(label);

        Optional<Label> label1 = labelRepository.findById(label.getLabelId());
        assertEquals(label, label1.get());
        assertEquals(label.getName(), newName);
    }
}