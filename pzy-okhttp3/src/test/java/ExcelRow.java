import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class ExcelRow {
    @Excel(name="省")
    private String province;
    @Excel(name="市")
    private String city;
    @Excel(name="区")
    private String area;
    @Excel(name="楼栋名/项目名")
    private String project;
    @Excel(name="楼层")
    private String level;
}
