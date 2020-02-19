package com.project.model;

import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SizeQte {

    private char size;
    private int qte;


/*    public SizeQte() {
        super(SizeQte.class);
    }*/
}
