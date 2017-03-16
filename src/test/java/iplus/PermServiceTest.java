package iplus;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sogou.iplus.config.DaoConfig;
import com.sogou.iplus.config.RootConfig;

import commons.saas.PermService.Person;
import commons.saas.PermService;

@ContextConfiguration(classes = { RootConfig.class, DaoConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PermServiceTest {

  @Autowired
  PermService permService;

  @Test
  public void test() {
    List<Person> persons = permService.getPerms();
    System.out.println(persons);

    Person person = permService.getPerm(210637);
    System.out.println(person);
  }

}
