package com.konkera.demoneo4j.node;

import com.konkera.demoneo4j.relation.MixRelation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

import static com.konkera.demoneo4j.relation.RelationConstant.RELATION_MIX;

/**
 * @author konkera
 * @date 2021/8/26
 */
@Setter
@Getter
@NoArgsConstructor
@Node("MixNodeSon")
public class MixNodeSon {
    @Id
    @GeneratedValue
    private Long id;

    private String mixName;

    @Relationship(type = RELATION_MIX, direction = Relationship.Direction.INCOMING)
    private List<MixRelation> mixNodeFathers = new ArrayList<>();

//    @Relationship(type = RELATION_MIX, direction = Relationship.Direction.INCOMING)
//    private List<MixNodeFather> fathers = new ArrayList<>();
}
