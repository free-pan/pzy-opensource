import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.MediaType;
import org.apache.commons.io.IOUtils;
import org.pzy.opensource.comm.util.FileUtil;
import org.pzy.opensource.comm.util.InputStreamUtil;
import org.pzy.opensource.comm.util.JsonUtil;
import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.okhttp.domain.bo.OkHttp3BodyType;
import org.pzy.opensource.okhttp.domain.bo.OkHttp3Request;
import org.pzy.opensource.okhttp.domain.bo.OkHttp3Response;
import org.pzy.opensource.okhttp.support.builder.OkHttp3RequestBuilder;
import org.pzy.opensource.okhttp.support.util.OkHttp3Util;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class OkHttp3UtilTest {

    @Test
    public void test() throws IOException {

        String parentId = "10000349";
        Integer level = 2;
        String province = "湖南";

        Map<String, Node> rootTreeNode = findTreeNode();
        Node node = rootTreeNode.get(province);

        Map<String, String> headers = new HashMap<String, String>(2);
        headers.put("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzExMTExMTAiLCJpYXQiOjE1OTMzMjA3MTJ9.2BtQvxy0ZkBvceLI_XQC86hdPl_KsUW2CoMO9XqmE3WhKj4XD0hBayXsPiW5ON37F6myD2xldYl7LVt5GS2DEg");
        for (Node cityNode : node.getChildren()) {
            SingleTest city = new SingleTest(cityNode.getName(), parentId, level);
            String bodyContent = JsonUtil.toJsonString(Arrays.asList(city));
            OkHttp3Request okHttp3Request = OkHttp3RequestBuilder.postBody("http://10.100.160.174:9900/idr-user/area/add", MediaType.get("application/json"), bodyContent.getBytes()).headers(headers).encoding(GlobalConstant.DEFAULT_CHARSET).build();
            // 添加城市
            OkHttp3Response<String> resp = OkHttp3Util.postBodyRequest(okHttp3Request, OkHttp3BodyType.string(null));
            AreaInfo cityInfo = extractAreaInfo(resp.getBody());
            for (Node areaNode : cityNode.getChildren()) {
                SingleTest area = new SingleTest(areaNode.getName(), cityInfo.getAreaId(), 3);
                bodyContent = JsonUtil.toJsonString(Arrays.asList(area));
                okHttp3Request = OkHttp3RequestBuilder.postBody("http://10.100.160.174:9900/idr-user/area/add", MediaType.get("application/json"), bodyContent.getBytes()).headers(headers).encoding(GlobalConstant.DEFAULT_CHARSET).build();
                // 添加区域
                resp = OkHttp3Util.postBodyRequest(okHttp3Request, OkHttp3BodyType.string(null));
                AreaInfo areaInfo = extractAreaInfo(resp.getBody());

                // 添加项目
                for (Node projectNode : areaNode.getChildren()) {
                    SingleTest ld = new SingleTest(projectNode.getName(), areaInfo.getAreaId(), 4);
                    bodyContent = JsonUtil.toJsonString(Arrays.asList(ld));
                    okHttp3Request = OkHttp3RequestBuilder.postBody("http://10.100.160.174:9900/idr-user/area/add", MediaType.get("application/json"), bodyContent.getBytes()).headers(headers).encoding(GlobalConstant.DEFAULT_CHARSET).build();
                    // 添加楼层
                    resp = OkHttp3Util.postBodyRequest(okHttp3Request, OkHttp3BodyType.string(null));
                    AreaInfo ldInfo = extractAreaInfo(resp.getBody());
                    for (Node levelNode : projectNode.getChildren()) {
                        SingleTest levelObj = new SingleTest(levelNode.getName() + "层", ldInfo.getAreaId(), 5);
                        bodyContent = JsonUtil.toJsonString(Arrays.asList(levelObj));
                        okHttp3Request = OkHttp3RequestBuilder.postBody("http://10.100.160.174:9900/idr-user/area/add", MediaType.get("application/json"), bodyContent.getBytes()).headers(headers).encoding(GlobalConstant.DEFAULT_CHARSET).build();
                        // 添加城市
                        resp = OkHttp3Util.postBodyRequest(okHttp3Request, OkHttp3BodyType.string(null));
                    }

                }

            }
        }
    }

    public AreaInfo extractAreaInfo(String jsonStr) throws IOException {
        RespData resp = JsonUtil.toJavaBean(jsonStr, RespData.class);
        return resp.getData().get(0);
    }

    @Test
    public void testJson() throws IOException {
        String jsonStr = IOUtils.toString(InputStreamUtil.getSrcFileInputStream("test.json"), GlobalConstant.DEFAULT_CHARSET);
        RespData resp = JsonUtil.toJavaBean(jsonStr, RespData.class);
        System.out.println(resp);
    }

    public Map<String, Node> findTreeNode() {
        ImportParams params = new ImportParams();
        params.setHeadRows(2);
        List<ExcelRow> rowList = ExcelImportUtil.importExcel(new File("D:/考勤区域测试数据.xlsx"), ExcelRow.class, params);
        System.out.println(rowList);
        // key为省份名称
        Map<String, Node> provinceMap = new HashMap<>();
        for (ExcelRow excelRow : rowList) {
            // 查找省份节点
            Node provinceNode = provinceMap.get(excelRow.getProvince().trim());
            if (null == provinceNode) {
                provinceNode = new Node();
                provinceNode.setName(excelRow.getProvince().trim());
                provinceMap.put(excelRow.getProvince().trim(), provinceNode);
            }

            // 查找城市节点
            Node cityNode = findCityFromProvince(provinceNode, excelRow.getCity().trim());
            if (null == cityNode) {
                cityNode = new Node();
                cityNode.setName(excelRow.getCity().trim());
            }
            // 向省份下添加城市
            provinceNodeAddCity(provinceNode, cityNode);
            // 查找区域节点
            Node areaNode = findAreaFromCity(cityNode, excelRow.getArea().trim());
            if (null == areaNode) {
                areaNode = new Node();
                areaNode.setName(excelRow.getArea().trim());
            }
            // 向城市下添加区域
            cityNodeAddArea(cityNode, areaNode);

            // 查找区域节点
            Node projectNode = findProjectFromArea(areaNode, excelRow.getProject().trim());
            if (null == projectNode) {
                projectNode = new Node();
                projectNode.setName(excelRow.getProject().trim());
            }
            // 向区域下添加项目
            areaNodeAddProject(areaNode, projectNode);

            // 查找楼层节点
            Node levelNode = findLevelFromProject(projectNode, excelRow.getLevel().trim());
            if (null == levelNode) {
                levelNode = new Node();
                levelNode.setName(excelRow.getLevel().trim());
            }
            // 向项目下添加层级
            projectNodeAddLevel(projectNode, levelNode);
        }
        System.out.println(provinceMap);
        return provinceMap;
    }

    @Test
    public void testExcel() {
        ImportParams params = new ImportParams();
        params.setHeadRows(2);
        List<ExcelRow> rowList = ExcelImportUtil.importExcel(new File("D:/考勤区域测试数据.xlsx"), ExcelRow.class, params);
        System.out.println(rowList);
        // key为省份名称
        Map<String, Node> provinceMap = new HashMap<>();
        for (ExcelRow excelRow : rowList) {
            // 查找省份节点
            Node provinceNode = provinceMap.get(excelRow.getProvince().trim());
            if (null == provinceNode) {
                provinceNode = new Node();
                provinceNode.setName(excelRow.getProvince());
                provinceMap.put(excelRow.getProvince(), provinceNode);
            }

            // 查找城市节点
            Node cityNode = findCityFromProvince(provinceNode, excelRow.getCity());
            if (null == cityNode) {
                cityNode = new Node();
                cityNode.setName(excelRow.getCity());
            }
            // 向省份下添加城市
            provinceNodeAddCity(provinceNode, cityNode);
            // 查找区域节点
            Node areaNode = findAreaFromCity(cityNode, excelRow.getArea());
            if (null == areaNode) {
                areaNode = new Node();
                areaNode.setName(excelRow.getArea());
            }
            // 向城市下添加区域
            cityNodeAddArea(cityNode, areaNode);

            // 查找区域节点
            Node projectNode = findProjectFromArea(areaNode, excelRow.getProject());
            if (null == projectNode) {
                projectNode = new Node();
                projectNode.setName(excelRow.getProject());
            }
            // 向区域下添加项目
            areaNodeAddProject(areaNode, projectNode);

            // 查找楼层节点
            Node levelNode = findLevelFromProject(projectNode, excelRow.getLevel());
            if (null == levelNode) {
                levelNode = new Node();
                levelNode.setName(excelRow.getLevel());
            }
            // 向项目下添加层级
            projectNodeAddLevel(projectNode, levelNode);
        }
        System.out.println(provinceMap);
    }

    private void projectNodeAddLevel(Node projectNode, Node levelNode) {
        for (Node tmpLevelNode : projectNode.getChildren()) {
            if (tmpLevelNode.getName().trim().equals(levelNode.getName().trim())) {
                return;
            }
        }
        projectNode.getChildren().add(levelNode);
    }

    private Node findLevelFromProject(Node projectNode, String level) {
        for (Node levelNode : projectNode.getChildren()) {
            if (levelNode.getName().trim().equals(level.trim())) {
                return levelNode;
            }
        }
        return null;
    }

    private void areaNodeAddProject(Node areaNode, Node projectNode) {
        for (Node tmpProjectNode : areaNode.getChildren()) {
            if (tmpProjectNode.getName().trim().equals(projectNode.getName().trim())) {
                return;
            }
        }
        areaNode.getChildren().add(projectNode);
    }

    private Node findProjectFromArea(Node areaNode, String project) {
        for (Node projectNode : areaNode.getChildren()) {
            if (projectNode.getName().trim().equals(project.trim())) {
                return areaNode;
            }
        }
        return null;
    }

    private void cityNodeAddArea(Node cityNode, Node areaNode) {
        for (Node tmpAreaNode : cityNode.getChildren()) {
            if (tmpAreaNode.getName().trim().equals(areaNode.getName().trim())) {
                return;
            }
        }
        cityNode.getChildren().add(areaNode);
    }

    private Node findAreaFromCity(Node cityNode, String area) {
        for (Node areaNode : cityNode.getChildren()) {
            if (areaNode.getName().trim().equals(area.trim())) {
                return areaNode;
            }
        }
        return null;
    }

    private void provinceNodeAddCity(Node provinceNode, Node cityNode) {
        for (Node tmpCityNode : provinceNode.getChildren()) {
            if (tmpCityNode.getName().trim().equals(cityNode.getName().trim())) {
                return;
            }
        }
        provinceNode.getChildren().add(cityNode);
    }

    private Node findCityFromProvince(Node provinceNode, String city) {
        for (Node cityNode : provinceNode.getChildren()) {
            if (cityNode.getName().trim().equals(city.trim())) {
                return cityNode;
            }
        }
        return null;
    }
}

@Data
class RespData {
    private String code;
    private String msg;
    private List<AreaInfo> data;
}

@Data
class AreaInfo {
    private String id;
    private String areaId;
    private String name;
    private String parentId;
}

@Data
@AllArgsConstructor
class SingleTest {
    private String name;
    private String parentId;
    private Integer level;
}

@Data
class Node {
    private String areaId;
    private String name;

    private List<Node> children = new ArrayList<>();
}
