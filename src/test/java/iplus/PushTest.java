package iplus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sogou.iplus.config.DaoConfig;
import com.sogou.iplus.config.RootConfig;

import commons.saas.XiaopService;
import commons.saas.XiaopService.PushParam;

@ContextConfiguration(classes = { RootConfig.class, DaoConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PushTest {

  @Autowired
  XiaopService pandoraService;

  @Test
  public void test() {
    PushParam param = new PushParam();
    param.setHTML(true);
    String text = "<h1><font color=\"#000000\">今日数据波动幅度较大的产品</font></h1><p>输入法&nbsp;&nbsp;<b><font color='#ff5b1a'>【上升了】</font></b>&nbsp;&nbsp;<font color='#ff5b1a'>10%</font></p>";
    param.setMessage(text);
    param.setOpenId("fengjin,zhengzhiyong,wangwenlong,liteng,wangjialin");
    param.setTitle("test");
    pandoraService.push(param);
  }
}
