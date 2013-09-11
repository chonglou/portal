package com.odong;

import com.odong.portal.entity.User;
import com.odong.portal.util.JsonHelper;
import com.odong.portal.util.impl.JsonHelperImpl;
import com.odong.portal.web.form.*;
import com.odong.portal.web.grid.Column;
import com.odong.portal.web.grid.Grid;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Unit test for  App.
 */
public class AppTest {

    //@Test
    public void testGrid() {
        Grid grid1 = new Grid("test1", "测试表1", new Column("aaa"), new Column("bbb"), new Column("ccc"));
        Grid grid2 = new Grid("test2", "测试表2", new Column("aaa"), new Column("bbb"), new Column("ccc"));
        grid1.setAction("/aaa");
        for (int i = 0; i < 100; i++) {
            grid1.addRow("a," + i, "b," + i, "c," + i, "" + i);
            grid2.addRow("a," + i, "b," + i, "c," + i);
        }
        grid1.able(true, true, true, true);
        grid1.setOk(true);
        log(grid1, grid2);
    }

    //@Test
    public void testForm() {
        Form form = new Form("test", "测试表单", "/test");
        form.setMethod(Form.Method.get);
        form.setOk(true);
        form.addButton(new Button("button", "按钮", "按钮提示信息"));
        CheckBoxField<Integer> cbf = new CheckBoxField<>("checkbox", "多选框", "多选框提示信息");
        for (int i = 0; i < 23; i++) {
            cbf.addOption("值" + i, i, i % 2 == 1);
        }
        form.addField(cbf);

        form.addField(new HiddenField<>("hidden", "隐藏数据"));
        form.addField(new PasswordField("password", "密码框", "密码提示信息"));
        RadioField<Integer> rf = new RadioField<Integer>("radio", "单选框", 2);
        for (int i = 0; i < 31; i++) {
            rf.addOption("值" + i, i);
        }
        form.addField(rf);
        SelectField<Integer> sf = new SelectField<Integer>("select", "下拉框", 2);
        for (int i = 0; i < 5; i++) {
            sf.addOption("值" + i, i);
        }
        form.addField(sf);
        TextAreaField taf = new TextAreaField("textarea", "多行文本", "<br/><h4>多行文本</h4>");
        form.addField(taf);
        form.addField(new TextField<>("text", "文本", "测试数据", "文本提示信息"));
        form.setCaptcha(true);

        log(form);

    }


    @Test
    public void testName() {
        Class<User> clazz = User.class;
        log(clazz.toString(), clazz.getName(), clazz.getSimpleName());
    }


    private void log(Object... objects) {
        for (Object o : objects) {
            System.out.print(jsonHelper.object2json(o));
            System.out.print("\t");
        }
        System.out.println();
    }

    @BeforeTest
    public void setup() {
        JsonHelperImpl jh = new JsonHelperImpl();
        jh.init();
        jsonHelper = jh;
    }

    private JsonHelper jsonHelper;
}
