// Food.java
package org.example.traveldiary.model;

import lombok.Data;
import java.util.List;

@Data
public class Food {
    private int id;
    private String name;
    private String origin;
    private String type;
    private List<String> ingredients;
    private int calories;
}
