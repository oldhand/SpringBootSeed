package com.github.init;

import com.github.cores.domain.Settings;
import com.github.cores.repository.SettingsRepository;
import com.github.domain.GenConfig;
import com.github.modules.security.domain.Application;
import com.github.modules.security.repository.ApplicationRepository;
import com.github.quartz.domain.QuartzJob;
import com.github.quartz.repository.QuartzJobRepository;
import com.github.repository.GenConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author oldhand
 * @date 2020-01-15
 *
*/

@Component
public class initData {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationRepository applicationrepository;

    @Autowired
    private GenConfigRepository genconfigrepository;

    @Autowired
    private QuartzJobRepository quartzjobrepository;

    @Autowired
    private SettingsRepository settingsrepository;


    @PersistenceContext
    private EntityManager em;

    // 通过注解实现注入
    @PostConstruct
    public void init() {
        Settings setting = new Settings();
        Example<Settings> settingexample = Example.of(setting);
        List<Settings> settings = settingsrepository.findAll(settingexample);
        if (settings.size() == 0) {
            setting.setName("SpringBootSeed");
            setting.setCompanyname("湖南网数科技有限公司");
            setting.setIcp("湘ICP备15009349号");
            setting.setSite("bitvalue.com.cn");
            setting.setSinglelogin(false);
            settingsrepository.saveAndFlush(setting);
        }


        Application application = new Application();
        Example<Application> example = Example.of(application);
        List<Application> applications = applicationrepository.findAll(example);
        if (applications.size() == 0) {
            application.setAppid("demo");
            application.setDescription("演示");
            application.setSecret("4c35458e913efbcf86ef621d22387b10");
            applicationrepository.saveAndFlush(application);
        }



        GenConfig genconfig = new GenConfig();
        Example<GenConfig> genconfigexample = Example.of(genconfig);
        List<GenConfig> genconfigs = genconfigrepository.findAll(genconfigexample);
        if (genconfigs.size() == 0) {
            genconfig.setAuthor("oldhand");
            genconfig.setCover(true);
            genconfig.setModuleName("samples");
            genconfig.setPrefix("base");
            genconfig.setPackageName("com.github.cores");
            genconfigrepository.saveAndFlush(genconfig);
        }

        QuartzJob quartzjob = new QuartzJob();
        Example<QuartzJob> quartzjobexample = Example.of(quartzjob);
        List<QuartzJob> quartzjobs = quartzjobrepository.findAll(quartzjobexample);
        if (quartzjobs.size() == 0) {
            quartzjob.setBeanName("testTask");
            quartzjob.setCronExpression("0/5 * * * * ?");
            quartzjob.setIsPause(true);
            quartzjob.setJobName("带参测试");
            quartzjob.setMethodName("run1");
            quartzjob.setParams("{a:test}");
            quartzjob.setRemark("带参测试，多参使用json");
            quartzjob.setCreateTime( new Timestamp(System.currentTimeMillis()));
            quartzjobrepository.saveAndFlush(quartzjob);

            quartzjob = new QuartzJob();
            quartzjob.setBeanName("testTask");
            quartzjob.setCronExpression("0/5 * * * * ?");
            quartzjob.setIsPause(true);
            quartzjob.setJobName("测试");
            quartzjob.setMethodName("run");
            quartzjob.setParams("");
            quartzjob.setRemark("不带参测试");
            quartzjob.setCreateTime( new Timestamp(System.currentTimeMillis()));
            quartzjobrepository.saveAndFlush(quartzjob);
        }
    }
}
