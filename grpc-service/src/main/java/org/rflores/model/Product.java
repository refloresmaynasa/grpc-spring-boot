package org.rflores.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String color;
    private String gas;
    private Integer year;
    private String description;
    private Long price;
    private String product_line_id;
}
