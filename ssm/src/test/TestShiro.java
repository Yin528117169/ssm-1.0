import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;

public class TestShiro extends BaseTest{
    @Test
    public void testHelloword(){
        //1.获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro/shiro-realm.ini");
        //2.得到SecurityManager实例，并绑定给SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3.得到Subject及创建用户名/密码身份验证Token(即用户身份/凭证)
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
        try{
            //4.登陆，即身份验证
            subject.login(token);
        }catch (AuthenticationException e){
            //身份验证失败
        }
        //5.断言用户已经登陆
        Assert.assertEquals(true,subject.isAuthenticated());
        //6.退出
        subject.logout();
    }

    public void login(String configFile,String username,String password){
        //1.获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory(configFile);
        //2.得到SecurityManager实例，并绑定给SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3.得到Subject及创建用户名/密码身份验证Token(即用户身份/凭证)
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);

        subject.login(token);
    }


    @Test
    public void testAllSuccessfulStrategyWithSuccess(){
        login("classpath:shiro/shiro-authenticator-all-success.ini","zhang","123");
        Subject subject = SecurityUtils.getSubject();

        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(2,principalCollection.asList().size());
    }

    @Test
    public void testHasRole(){
        login("classpath:shiro/shiro-role.ini","zhang","123");
        //判断拥有角色role1
        Subject subject = SecurityUtils.getSubject();
        Assert.assertTrue(subject.hasRole("role1"));
        //判断拥有角色role1 and role2
        Assert.assertTrue(subject.hasAllRoles(Arrays.asList("role1","role2")));
        //判断拥有角色：role1 and role2 and !role3
        boolean[] result = subject.hasRoles(Arrays.asList("role1","role2","role3"));
        Assert.assertEquals(true,result[0]);
        Assert.assertEquals(true,result[1]);
        Assert.assertEquals(false,result[2]);
    }

    @Test
    public void testIsPermitted(){
        login("classpath:shiro/shiro-permission.ini","zhang","123");
        //判断拥有权限：user:create
        Subject subject = SecurityUtils.getSubject();
        Assert.assertTrue(subject.isPermitted("user:create"));
        //判断拥有权限：user:update and user:delete
        Assert.assertTrue(subject.isPermittedAll("user:update","user:delete"));
        //判断没有权限：user:view
        Assert.assertFalse(subject.isPermitted("user:view"));
    }

    @Test
    public void testIsPermitted2(){
        login("classpath:shiro/shiro-authorizer.ini","zhang","123");
        //判断拥有权限：user:create
        Subject subject = SecurityUtils.getSubject();
        Assert.assertTrue(subject.isPermitted("user1:update"));
        Assert.assertTrue(subject.isPermitted("user2:update"));
        //通过二进制位的方式表示权限
        Assert.assertTrue(subject.isPermitted("+user1+2"));//新增权限
        Assert.assertTrue(subject.isPermitted("+user1+8"));//查看权限
        Assert.assertTrue(subject.isPermitted("+user2+10"));//新增及查看

        Assert.assertFalse(subject.isPermitted("+user1+4"));//没有删除权限

        Assert.assertTrue(subject.isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
    }
}
