package com.konkera.demoneo4j.node;

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

import static com.konkera.demoneo4j.relation.RelationConstant.RELATION_DEPARTMENT_MEMBER;

/**
 * “部门员工”标签对象
 *
 * @author konkera
 * @date 2021/8/18
 */
@Getter
@Setter
@NoArgsConstructor
@Node("EmployeeNode")
public class EmployeeNode {
    @Id
    @GeneratedValue
    private Long id;

    private String employeeName;

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

    @Relationship(type = RELATION_DEPARTMENT_MEMBER, direction = Relationship.Direction.INCOMING)
    private List<DepartmentNode> supDepartments;
}
