package com.konkera.demoneo4j.repository;

import com.konkera.demoneo4j.node.CompanyNode;
import com.konkera.demoneo4j.node.EmployeeNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Collection;
import java.util.List;

import static com.konkera.demoneo4j.relation.RelationConstant.RELATION_DEPARTMENT_MEMBER;

/**
 * @author konkera
 * @date 2021/8/18
 */
public interface EmployeeRepository extends Neo4jRepository<EmployeeNode, Long> {

    List<EmployeeNode> findAllByEmployeeNameIn(Collection<String> names);

    /**
     * 关联多对一关系，关系存在时不会重复创建
     *
     * @param departmentIds 上级节点id集合
     * @param employeeId    主节点id
     */
    @Query("match (d:DepartmentNode),(e:EmployeeNode) where id(d) in $0 and id(e)=$1 merge (d)-[:`" + RELATION_DEPARTMENT_MEMBER + "`]->(e)")
    void linkDepartments(List<Long> departmentIds, Long employeeId);

    /**
     * 查找返回特定条数并排序
     *
     * @param limit
     * @param sorted
     * @return
     */
    @Query("match (e:EmployeeNode) return e order by $1 limit $0")
    List<EmployeeNode> findWithLimit(Long limit, String sorted);

}
