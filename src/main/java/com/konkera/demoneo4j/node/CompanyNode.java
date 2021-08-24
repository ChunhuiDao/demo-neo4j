package com.konkera.demoneo4j.node;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.List;

import static com.konkera.demoneo4j.relation.RelationConstant.RELATION_COMPANY_DEPARTMENT;

/**
 * “公司”标签对象
 *
 * @author konkera
 * @date 2021/8/18
 */
@Getter
@Setter
//@ToString
@NoArgsConstructor
@Node("CompanyNode") // 与数据库中节点标签名一致
public class CompanyNode {
    /**
     * 标签属性-id，数据库自动生成id，数据库内全局唯一
     * ：@Id 这里“@org.springframework.data.annotation.Id” 和 “@org.springframework.data.neo4j.core.schema.Id” 效果一样
     * ：@GeneratedValue id生成策略，默认使用GeneratedValue.InternalIdGenerator.class，即Long型id，若需要使用uuid作为id，可使用
     * “@GeneratedValue(GeneratedValue.UUIDGenerator.class)”
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * 标签属性-公司名称
     */
    private String companyName;
    /**
     * 标签属性-公司地址
     */
    private String address;
    /**
     * 标签属性-节点创建者
     */
    @CreatedBy
    private String createdBy;
    /**
     * 标签属性-节点创建时间
     */
    @CreatedDate
    private LocalDateTime created = LocalDateTime.now();
    /**
     * 标签属性-节点最后更新者
     * 更新时需要手动赋值
     */
    @LastModifiedBy
    private String updatedBy;
    /**
     * 标签属性-节点最后更新时间
     * 更新时需要手动赋值
     */
    @LastModifiedDate
    private LocalDateTime updated = LocalDateTime.now();
    /**
     * 事务并发时多版本控制相关属性
     * 除创建新节点时可空，更新时需要带上当前版本值
     */
    @Version
    private Long version;

    /**
     * 关系
     * type：关系标签名
     * direction：关系指向，这里 Relationship.Direction.INCOMING 代表subDepartments成员指向自己，
     * Relationship.Direction.OUTGOING 代表自己指向subDepartments成员
     */
    @Relationship(type = RELATION_COMPANY_DEPARTMENT, direction = Relationship.Direction.OUTGOING)
    private List<DepartmentNode> subDepartments;
}
