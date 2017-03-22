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
    String text = "<font color=\"red\">hello</font><font color=\"green\"> world</font>";
    param.setMessage(text);
    param.setOpenId("fengjin,zhengzhiyong,wangwenlong,liteng");
    param.setTitle("test");
    pandoraService.push(param);
  }
}
