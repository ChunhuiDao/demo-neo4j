package com.konkera.demoneo4j.relation;

import com.konkera.demoneo4j.node.MixNodeFather;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.ArrayList;
import java.util.List;

import static com.konkera.demoneo4j.relation.RelationConstant.RELATION_MIX;

/**
 * 复杂关系（关系对象）
 *
 * @author konkera
 * @date 2021/8/26
 */
@Setter
@Getter
@NoArgsConstructor
@RelationshipProperties
public class MixRelation {
    @Id
    @GeneratedValue
    private Long id;

    private String desc;

    private String relationName = RELATION_MIX;

    @TargetNode
    private MixNodeFather mixNodeFather;

    private void setRelationName(String relationName) {
        this.relationName = relationName;
    }
}
