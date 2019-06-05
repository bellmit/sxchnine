package com.project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "category")
@Getter
@Setter
@ToString
public class Category implements Serializable {

    @Id
    private String id;
    private String name;
}
