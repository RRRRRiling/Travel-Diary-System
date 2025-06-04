package org.example.traveldiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenicSpot {
    private Integer id;
    private String name;
    private String location;
    private String description;
    private List<String> tags;
}