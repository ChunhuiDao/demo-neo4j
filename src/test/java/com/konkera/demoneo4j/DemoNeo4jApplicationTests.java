package com.konkera.demoneo4j;

import com.alibaba.fastjson.JSON;
import com.konkera.demoneo4j.config.Neo4jCustomizeCqlExecutor;
import com.konkera.demoneo4j.mapper.DchUserMapper;
import com.konkera.demoneo4j.node.CompanyNode;
import com.konkera.demoneo4j.node.DepartmentNode;
import com.konkera.demoneo4j.node.EmployeeNode;
import com.konkera.demoneo4j.repository.CompanyRepository;
import com.konkera.demoneo4j.repository.DepartmentRepository;
import com.konkera.demoneo4j.repository.EmployeeRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.internal.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.konkera.demoneo4j.relation.RelationConstant.RELATION_COMPANY_DEPARTMENT;

@SpringBootTest
class DemoNeo4jApplicationTests {

    @Autowired
    private DchUserMapper userMapper;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * 测试与 MySQL 数据库连接共存
     */
    @Test
    void testWithMysql() {
        System.out.println(userMapper.selectById(1L));
    }

    @Test
    void tempTest() {
//        CompanyNode companyNode = companyRepository.findFirstByCompanyName("1公司");
//        System.out.println("companyNode = " + JSON.toJSONString(companyNode));

        // 内存泄漏重现
//        System.out.println("companyNode = " + companyNode.toString());
//        System.out.println("companyNode = " + JsonUtil.toJsonString(companyNode));
//        String cql = "match (n:CompanyNode) with n limit 3 return collect(n)";
        String cql = "match (n:CompanyNode) with n limit 3 return n";

        Result result = Neo4jCustomizeCqlExecutor.executeCql(cql);
        System.out.println(JSON.toJSONString(result));
    }

    /**
     * save()
     */
    @Test
    void testSaveNode() {
        /*
         * 单纯保存节点数据
         */
        String[] prefixArr = {"1", "2"};
        for (String prefix : prefixArr) {
            String companyName = prefix + "公司";
            CompanyNode companyNode = companyRepository.findFirstByCompanyName(companyName); // 仅演示使用
            if (companyNode == null) {
                companyNode = new CompanyNode();
                companyNode.setCompanyName(companyName);
                companyNode.setAddress(prefix + "云南省昆明市环城南路xxx号");
                companyNode.setCreatedBy(prefix + "管理员a");
                companyNode.setUpdatedBy(prefix + "管理员a");

                companyRepository.save(companyNode);
            }
            System.out.println("companyNode = " + JSON.toJSONString(companyNode));
        }

        /*
         * 保存节点并创建关系
         */
        String prefix = "3";
        String companyName = prefix + "公司";
        CompanyNode companyNode = companyRepository.findFirstByCompanyName(companyName);
        if (companyNode == null) {
            companyNode = new CompanyNode();
            companyNode.setCompanyName(companyName);
            companyNode.setAddress(prefix + "云南省昆明市环城南路xxx号");
            companyNode.setCreatedBy(prefix + "管理员a");
            companyNode.setUpdatedBy(prefix + "管理员a");
        }

        String departmentName = companyName + "运营部门";
        DepartmentNode opDepartment = departmentRepository.findFirstByDepartmentName(departmentName);
        if (opDepartment == null) {
            opDepartment = new DepartmentNode();
            opDepartment.setDepartmentName(companyName + "运营部门");
            opDepartment.setCreatedBy("管理员a");
            opDepartment.setUpdatedBy("管理员a");
        }

        List<CompanyNode> companyNodes = companyRepository.listByCompanyNameAndDepartmentName(companyName, departmentName);
        if (companyNodes.size() > 0) return;

        List<DepartmentNode> subDepartments = new ArrayList<>();
        subDepartments.add(opDepartment);
        companyNode.setSubDepartments(subDepartments);
        companyRepository.save(companyNode);
    }

    /**
     * saveAll()
     */
    @Test
    void testSaveNodes() {
        String[] prefixArr = {"1", "2"};

        ArrayList<DepartmentNode> departmentNodes = new ArrayList<>();
        Map<String, String> departmentNameMap = new HashMap<>();
        for (String prefix : prefixArr) {
            String companyName = prefix + "公司";

            String opDepartmentName = companyName + "运营部门";
            departmentNameMap.put(opDepartmentName, opDepartmentName);

            DepartmentNode opDepartment = new DepartmentNode();
            opDepartment.setDepartmentName(opDepartmentName);
            opDepartment.setCreatedBy("管理员a");
            opDepartment.setUpdatedBy("管理员a");
            departmentNodes.add(opDepartment);

            String finDepartmentName = companyName + "财务部门";
            departmentNameMap.put(finDepartmentName, finDepartmentName);

            DepartmentNode finDepartment = new DepartmentNode();
            finDepartment.setDepartmentName(finDepartmentName);
            finDepartment.setCreatedBy("管理员a");
            finDepartment.setUpdatedBy("管理员a");
            departmentNodes.add(finDepartment);
        }

        /*
         * 为防止脏数据产生，作简单幂等性处理，可使用merge语句代替
         */
        List<DepartmentNode> departmentNodeList = departmentRepository.findAllByDepartmentNameIn(departmentNameMap.values());
        if (departmentNodeList.size() > 0) {
            departmentNodes.removeIf(node -> departmentNameMap.get(node.getDepartmentName()) != null);
        }
        if (departmentNodes.size() > 0) {
            departmentRepository.saveAll(departmentNodes);
        }

        List<EmployeeNode> employeeNodes = new ArrayList<>();
        Map<String, String> employeeNodeNameMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            EmployeeNode opEmployee = new EmployeeNode();
            opEmployee.setEmployeeName(i + "运营员工A");
            opEmployee.setCreatedBy("管理员a");
            opEmployee.setUpdatedBy("管理员a");
            employeeNodes.add(opEmployee);
            employeeNodeNameMap.put(i + "运营员工A", i + "运营员工A");

            EmployeeNode finEmployee = new EmployeeNode();
            finEmployee.setEmployeeName(i + "财务员工A");
            finEmployee.setCreatedBy("管理员a");
            finEmployee.setUpdatedBy("管理员a");
            employeeNodes.add(finEmployee);
            employeeNodeNameMap.put(i + "财务员工A", i + "财务员工A");
        }

        List<EmployeeNode> employeeNodeList = employeeRepository.findAllByEmployeeNameIn(employeeNodeNameMap.values());
        if (employeeNodeList.size() > 0) {
            employeeNodes.removeIf(node -> employeeNodeNameMap.get(node.getEmployeeName()) != null);
        }
        if (employeeNodes.size() > 0) {
            employeeRepository.saveAll(employeeNodes);
        }
    }

    /**
     * 关联关系
     */
    @Test
    void testLinkRelation() {
        String prefix = "2";

        CompanyNode companyNode = companyRepository.findFirstByCompanyName(prefix + "公司");
        if (companyNode == null) return;

        List<DepartmentNode> departmentNodes = departmentRepository.findAllByDepartmentNameIn(new ArrayList<String>() {{
            add(companyNode.getCompanyName() + "运营部门");
            add(companyNode.getCompanyName() + "财务部门");
        }});

        if (departmentNodes == null || departmentNodes.isEmpty()) return;
        List<Long> departmentNodeIds = departmentNodes.stream().map(DepartmentNode::getId).collect(Collectors.toList());

        // merge语句创建关系
        companyRepository.linkDepartments(companyNode.getId(), departmentNodeIds, "RELATION_COMPANY_DEPARTMENT");
    }

    /**
     * 关联关系2
     */
    @Test
    void testLinkRelation2() {
        String opDepartment1 = "1公司运营部门";
        String opDepartment2 = "2公司运营部门";

        List<DepartmentNode> departmentNodes = departmentRepository.findAllByDepartmentNameIn(new ArrayList<String>() {{
            add(opDepartment1);
            add(opDepartment2);
        }});
        if (departmentNodes == null || departmentNodes.isEmpty()) return;

        List<EmployeeNode> employeeNodes = employeeRepository.findAllById(new ArrayList<Long>() {{
            add(10L);
        }});
        if (employeeNodes.isEmpty()) return;

        List<Long> departmentNodeIds = departmentNodes.stream().map(DepartmentNode::getId).collect(Collectors.toList());
        employeeRepository.linkDepartments(departmentNodeIds, employeeNodes.get(0).getId());
    }

    /**
     * 查询单节点
     */
    @Test
    void testFindSingleNode() {
        // 关键字方法查询
        CompanyNode companyNode1 = companyRepository.findFirstByCompanyName("1公司");
        System.out.println("companyNode1 = " + JSON.toJSONString(companyNode1));
        // 手动定义查询
        CompanyNode companyNode2 = companyRepository.getById(0L);
        System.out.println("companyNode2 = " + JSON.toJSONString(companyNode2));
    }

    /**
     * api自定义复杂查询
     * 结果也会包含整个关系网络数据
     *
     * @see ExampleMatcher
     */
    @Test
    void testApiQuery() {
        /*
         * 定义条件数据
         * 这里代表传参属性为companyName，参数值为“公司”
         */
        CompanyNode companyNode = new CompanyNode();
        companyNode.setCompanyName("公司");

        /*
         * 定义条件方式
         * withMatcher()代表需要匹配，匹配方式：
         * |- ExampleMatcher.GenericPropertyMatchers.endsWith()：代表匹配的方式为：结尾是传入的参数值
         * |- ExampleMatcher.GenericPropertyMatchers.startsWith()：代表匹配的方式为：开头是传入的参数值
         * |- ExampleMatcher.GenericPropertyMatchers.caseSensitive()：代表匹配的方式为：区分大小写
         * |- ExampleMatcher.GenericPropertyMatchers.ignoreCase()：代表匹配的方式为：不区分大小写
         * |- ExampleMatcher.GenericPropertyMatchers.exact()：代表匹配的方式为：精确匹配
         * |- ExampleMatcher.GenericPropertyMatchers.contains()：代表匹配的方式为：包含，类似 like "%param%"
         * |- ExampleMatcher.GenericPropertyMatchers.regex()：代表匹配的方式为：正则匹配
         * |- ExampleMatcher.GenericPropertyMatchers.storeDefaultMatching()：代表匹配的方式为：效果和精确匹配大致相同
         *
         * withIgnorePaths()表示这些属性忽略，不参与条件匹配
         */
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("companyName", ExampleMatcher.GenericPropertyMatchers.endsWith())
                .withIgnorePaths("created")
                .withIgnorePaths("updated");

        /*
         * 真正条件传参对象
         * 由 “数据” 和 “匹配方式” 构成
         */
        Example<CompanyNode> example = Example.of(companyNode, matcher);

        val companyNodes = companyRepository.findAll(example);

        System.out.println("companyNodes = " + JSON.toJSONString(companyNodes));
    }

    /**
     * 根据关系查找
     */
    @Test
    void findByRelation() {
        val companyNodes = companyRepository.findByRelation(RELATION_COMPANY_DEPARTMENT);
        System.out.println("companyNodes = " + JSON.toJSONString(companyNodes));

        val companyNodes2 = companyRepository.findAllByRelation(RELATION_COMPANY_DEPARTMENT);
        System.out.println("companyNodes2 = " + JSON.toJSONString(companyNodes2));
    }

    /**
     * 查找特定关系且指定深度
     */
    @Test
    void findByRelationAndLevel() {
        val companyNodes = companyRepository.findByRelationAndLevel(RELATION_COMPANY_DEPARTMENT, 1L);
        System.out.println("companyNodes = " + JSON.toJSONString(companyNodes));
    }

    /**
     * 查找返回特定条数并排序
     */
    @Test
    void findWithLimit() {
        Long limit = 10L;
        String sorted = "employeeName desc";
        val employeeNodes = employeeRepository.findWithLimit(limit, sorted);
        System.out.println("employeeNodes = " + JSON.toJSONString(employeeNodes));
    }

    /**
     * 查询两节点间关系路径长度
     */
    @Test
    void findLength() {
        val length = companyRepository.findLength();
        System.out.println("length = " + length);
    }

    /**
     * 分页查询
     */
    @Test
    void findPage() {
        val companyNodes = companyRepository.findPage(1L, 5L);
        System.out.println("companyNodes = " + JSON.toJSONString(companyNodes));
    }

    /**
     * 复杂关系
     */
    @Test
    void mixRelation() {

    }

    /**
     * 模糊查询
     */
    @Test
    void findLike() {
        val companyNodes = companyRepository.findLike("公司");
        System.out.println("companyNodes = " + JSON.toJSONString(companyNodes));
    }

    /**
     * collect接收返回值和非collect接收
     * collect接收会聚合去重
     */
    @Test
    void findCollect() {

    }

}
