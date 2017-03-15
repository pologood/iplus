package commons.saas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sogou.iplus.model.ApiResult;

import commons.utils.JsonHelper;

public class PermService {

  private RestTemplate restTemplate;
  private RestNameService restNameService;

  public PermService(RestTemplate restTemplate, RestNameService restNameService) {
    this.restTemplate = restTemplate;
    this.restNameService = restNameService;
  }

  @SuppressWarnings("rawtypes")
  public List<Person> getPerms() {
    ResponseEntity<ApiResult> response = restTemplate.exchange(restNameService.lookup("GET_PERMS"), HttpMethod.GET,
        null, ApiResult.class);
    List<Person> persons = JsonHelper.convertValue(response.getBody().getData(), new TypeReference<List<Person>>() {});
    return persons;
  }

  public static class Person {
    private Integer id;
    private String email;
    private String name;
    private String headImg;
    private Integer incId;
    private Integer perm;
    private List<PersonPerm> grantPerms;
    private LocalDateTime createTime;
    private Map<String, List<Integer>> permsMap;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getHeadImg() {
      return headImg;
    }

    public void setHeadImg(String headImg) {
      this.headImg = headImg;
    }

    public Integer getIncId() {
      return incId;
    }

    public void setIncId(Integer incId) {
      this.incId = incId;
    }

    public Integer getPerm() {
      return perm;
    }

    public void setPerm(Integer perm) {
      this.perm = perm;
    }

    public List<PersonPerm> getGrantPerms() {
      return grantPerms;
    }

    public void setGrantPerms(List<PersonPerm> grantPerms) {
      this.grantPerms = grantPerms;
    }

    public LocalDateTime getCreateTime() {
      return createTime;
    }

    public void setCreateTime(String createTime) {
      this.createTime = LocalDateTime.parse(createTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Map<String, List<Integer>> getPermsMap() {
      return permsMap;
    }

    public void setPermsMap(Map<String, List<Integer>> permsMap) {
      this.permsMap = permsMap;
    }
  }

  public static final class PersonPerm {
    private String entity;
    private Integer permId;

    public String getEntity() {
      return entity;
    }

    public void setEntity(String entity) {
      this.entity = entity;
    }

    public Integer getPermId() {
      return permId;
    }

    public void setPermId(Integer permId) {
      this.permId = permId;
    }
  }
}
