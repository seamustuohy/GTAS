package gov.gtas.test.util;

import gov.gtas.model.Role;
import gov.gtas.model.User;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleMeta;
import gov.gtas.model.udr.YesNoEnum;
import gov.gtas.services.RulePersistenceService;
import gov.gtas.services.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RuleServiceDataGenUtils {
  public static final String TEST_RULE_TITLE="Test Rule";
  
  public static final int TEST_ROLE1_ID = 1;
  public static final String TEST_ROLE1_DESCRIPTION = "admin";
  public static final String TEST_USER1_ID = "jpjones";
  
  public static final int TEST_ROLE2_ID = 99;
  public static final String TEST_ROLE2_DESCRIPTION = "readonly";
  public static final String TEST_USER2_ID = "pawnX";

  private UserService userService;
  private RulePersistenceService rulePersistenceService;
  
  public RuleServiceDataGenUtils(UserService usrSvc, RulePersistenceService rpSvc){
	 this.userService = usrSvc;
	 this.rulePersistenceService = rpSvc;
  }
   public void initUserData(){
	   try{
	   Role role = new Role();
	   User user = new User();
	   
	   role.setRoleDescription(TEST_ROLE1_DESCRIPTION);
	   role.setRoleId(TEST_ROLE1_ID);
	   user.setFirstName("JP");
	   user.setLastName("Jones");
	   user.setUserId(TEST_USER1_ID);
	   user.setPassword("passsword");
	   user.setUserRole(role);
	   List<User> roleUsers = new ArrayList<User>();
	   roleUsers.add(user);
	   role.setUserList(roleUsers);
	   userService.create(user);
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   public  Rule createRuleNoCondition(String descr, YesNoEnum enabled){
	   Rule rule = new Rule();
	   rule.setDeleted(YesNoEnum.N);
	   rule.setEditDt(new Date());
	   RuleMeta meta = createRuleMeta(TEST_RULE_TITLE, descr,enabled);
	   rule.setMetaData(meta);
	   return rule;
   }
   private RuleMeta createRuleMeta(String title, String descr, YesNoEnum enabled){
	   RuleMeta meta = new RuleMeta();
	   meta.setDescription(descr);
	   meta.setEnabled(enabled);
	   meta.setHitSharing(YesNoEnum.N);
	   meta.setPriorityHigh(YesNoEnum.N);
	   meta.setStartDt(new Date());
	   meta.setTitle(title);
       return meta;	   
   }
}
