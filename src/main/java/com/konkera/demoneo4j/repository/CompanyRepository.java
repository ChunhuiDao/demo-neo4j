package com.konkera.demoneo4j.repository;

import com.konkera.demoneo4j.node.CompanyNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.konkera.demoneo4j.relation.RelationConstant.RELATION_COMPANY_DEPARTMENT;

/**
 * @author konkera
 * @date 2021/8/18
 */
public interface CompanyRepository extends Neo4jRepository<CompanyNode, Long> {

    /**
     * 查询结果只返回第一个节点，相当于 select * from CompanyNode where companyName = $companyName limit 1
     *
     * @param companyName 公司名称
     * @return
     */
    CompanyNode findFirstByCompanyName(String companyName);

    /**
     * 根据id查询节点
     *
     * @param id
     * @return
     */
    @Query("match (c:CompanyNode) where id(c)=$0 return c")
    CompanyNode getById(Long id);

    /**
     * 根据公司名称和部门名称查询公司节点
     *
     * @return
     */
    @Query("match (c:CompanyNode)-[]->(d:DepartmentNode) where c.companyName=$0 and d.departmentName=$departmentName return c")
    List<CompanyNode> listByCompanyNameAndDepartmentName(String companyName, String departmentName);

    /**
     * 关联一对多关系，关系存在时不会重复创建
     * 关系关联推荐使用自定义方法，原生save()方法使用不当会导致关系链丢失或错误
     *
     * @param companyId     主节点id
     * @param departmentIds 下级节点id集合
     */
    @Query("match (c:CompanyNode),(d:DepartmentNode) where id(c)=$companyId and id(d) in $1 merge (c)-[:`" + RELATION_COMPANY_DEPARTMENT + "`]->(d)")
    void linkDepartments(@Param("companyId") Long companyId, List<Long> departmentIds);

    /**
     * 根据关系查询节点数据（包含下一级节点）
     *
     * @param relation 关系
     * @return
     */
    @Query("match data=(c:CompanyNode)-[r]->(d) where type(r)=$0 with collect(data) as results return results")
    List<CompanyNode> findByRelation(String relation);

    /**
     * 根据关系查询节点数据（包含节点整个关系网节点）
     *
     * @param relation 关系
     * @return
     */
    @Query("match data=(c:CompanyNode)-[r]->(d)-[*0..]->(e) where type(r)=$0 with collect(data) as results return results")
    List<CompanyNode> findAllByRelation(String relation);

    /**
     * 查找特定关系且深度为3（包含节点整个关系网节点）
     *
     * @param relation 关系
     * @return
     */
//    @Query("match data=(c:CompanyNode)-[r]->(d)-[*$level..$level]->(e) where type(r)=$0 with collect(data) as results return results")
//    List<CompanyNode> findByRelationAndLevel(String relation, Long level);
    @Query("match data=(c:CompanyNode)-[r]->(d)-->(e) where type(r)=$0 with collect(data) as results return results")
    List<CompanyNode> findByRelationAndLevel(String relation);

    /**
     * 查询两节点间关系路径长度
     *
     * @return
     */
    @Query("match data=(c:CompanyNode)-[r *]->() return length(data) as length order by length desc limit 1")
    Long findLength();
}
