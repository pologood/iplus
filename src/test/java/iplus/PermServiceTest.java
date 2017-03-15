package iplus;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.sogou.iplus.config.DaoConfig;
import com.sogou.iplus.config.RootConfig;

import commons.saas.PermService.Person;
import commons.saas.PermService;
import commons.saas.RestNameService;

@ContextConfiguration(classes = { RootConfig.class, DaoConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PermServiceTest {

  @Autowired
  RestNameService restNameService;

  @Autowired
  RestTemplate restTemplate;

  @Test
  public void test() {
    List<Person> persons = new PermService(restTemplate, restNameService).getPerms();
    System.out.println(persons);
  }

}
