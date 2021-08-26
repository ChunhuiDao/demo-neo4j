package com.konkera.demoneo4j.repository;

import com.konkera.demoneo4j.node.MixNodeSon;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author konkera
 * @date 2021/8/26
 */
public interface MixNodeSonRepository extends Neo4jRepository<MixNodeSon, Long> {
}
