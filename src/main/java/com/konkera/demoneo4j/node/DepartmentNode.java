package com.konkera.demoneo4j.node;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.konkera.demoneo4j.relation.MixRelation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.List;

import static com.konkera.demoneo4j.relation.RelationConstant.*;

/**
 * "部门"标签对象
 *
 * @author konkera
 * @date 2021/8/18
 */
@Getter
@Setter
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class) // Jackson解决序列化循环依赖
@Node("DepartmentNode")
public class DepartmentNode {
    @Id
    @GeneratedValue
    private Long id;

    private String departmentName;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime created = LocalDateTime.now();

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updated = LocalDateTime.now();

    @Version
    private Long version;

    @Relationship(type = RELATION_COMPANY_DEPARTMENT, direction = Relationship.Direction.INCOMING)
    private List<CompanyNode> supCompanyNodes;

    @Relationship(type = RELATION_DEPARTMENT_MEMBER, direction = Relationship.Direction.OUTGOING)
    private List<EmployeeNode> subEmployeeNodes;

    // 存在内存泄漏问题
    @Override
    public String toString() {
        return "DepartmentNode{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", created=" + created +
                ", updatedBy='" + updatedBy + '\'' +
                ", updated=" + updated +
                ", version=" + version +
                ", supCompanyNodes=" + supCompanyNodes +
                ", subEmployeeNodes=" + subEmployeeNodes +
                '}';
    }
}
