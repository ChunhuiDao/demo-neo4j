package com.konkera.demoneo4j.node;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * @author konkera
 * @date 2021/8/26
 */
@Setter
@Getter
@NoArgsConstructor
@Node("MixNodeFather")
public class MixNodeFather {
    @Id
    @GeneratedValue
    private Long id;

    private String mixName;
}
