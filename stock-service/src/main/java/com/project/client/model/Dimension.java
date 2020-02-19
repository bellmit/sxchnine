package com.project.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Dimension implements Serializable {

    private int height;
    private int width;
    private int length;
}
