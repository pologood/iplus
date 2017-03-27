package commons.saas;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sogou.iplus.model.ApiResult;

import commons.utils.JsonHelper;

public class PermService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PermService.class);

  private RestTemplate restTemplate;
  private RestNameService restNameService;

  public PermService(RestTemplate restTemplate, RestNameService restNameService) {
    this.restTemplate = restTemplate;
    this.restNameService = restNameService;
  }

  @SuppressWarnings("rawtypes")
  public Person getPerm(long uid) {
    try {
      ResponseEntity<ApiResult> response = restTemplate.exchange(restNameService.lookup("GET_PERM"), HttpMethod.GET,
          null, ApiResult.class, uid);
      return JsonHelper.convertValue(response.getBody().getData(), Person.class);
    } catch (Exception e) {
      LOGGER.error("get perm error!", e);
      return null;
    }
  }

  @SuppressWarnings("rawtypes")
  public List<Person> getPerms() {
    try {
      ResponseEntity<ApiResult> response = restTemplate.exchange(restNameService.lookup("GET_PERMS"), HttpMethod.GET,
          null, ApiResult.class);
      return JsonHelper.convertValue(response.getBody().getData(), new TypeReference<List<Person>>() {});
    } catch (Exception e) {
      LOGGER.error("get perms error!", e);
      return null;
    }
  }

  public static class Person {
    private Long id;
    private String phone;
    private String email;
    private String name;
    private String headImg;
    private Long incId;
    private Long perm;
    private List<PersonPerm> grantPerms;
    private LocalDateTime createTime;
    private Map<String, List<Long>> permsMap;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
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

    public Long getIncId() {
      return incId;
    }

    public void setIncId(Long incId) {
      this.incId = incId;
    }

    public Long getPerm() {
      return perm;
    }

    public void setPerm(Long perm) {
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

    public Map<String, List<Long>> getPermsMap() {
      return permsMap;
    }

    public void setPermsMap(Map<String, List<Long>> permsMap) {
      this.permsMap = permsMap;
    }

    @JsonIgnore
    public String getEmailName() {
      return email.substring(0, email.length() - 14);
    }

    @Override
    public String toString() {
      return JsonHelper.writeValueAsString(this);
    }
  }

  public static final class PersonPerm {
    private String entity;
    private Long permId;

    public String getEntity() {
      return entity;
    }

    public void setEntity(String entity) {
      this.entity = entity;
    }

    public Long getPermId() {
      return permId;
    }

    public void setPermId(Long permId) {
      this.permId = permId;
    }
  }
}
