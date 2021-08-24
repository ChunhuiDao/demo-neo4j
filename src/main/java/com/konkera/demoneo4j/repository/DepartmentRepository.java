package com.konkera.demoneo4j.repository;

import com.konkera.demoneo4j.node.DepartmentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author konkera
 * @date 2021/8/18
 */
public interface DepartmentRepository extends Neo4jRepository<DepartmentNode, Long> {
    /**
     * 根据departmentName查询第一个DepartmentNode对象
     * 推荐使用
     *
     * @param departmentName
     * @return
     */
    DepartmentNode findFirstByDepartmentName(String departmentName);

    /**
     * 相当于 select * from DepartmentNode where departmentName in ()
     * 推荐使用
     *
     * @param departmentNames
     * @return
     */
    List<DepartmentNode> findAllByDepartmentNameIn(Collection<String> departmentNames);
}
