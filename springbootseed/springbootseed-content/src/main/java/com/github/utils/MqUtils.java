package com.github.utils;

import com.github.cores.domain.Modentitynos;
import com.github.cores.domain.Tabs;
import com.github.cores.repository.TabsRepository;
import com.github.cores.service.ModentitynosService;
import com.github.cores.service.dto.ModentitynosDTO;
import com.github.cores.service.mapper.ModentitynosMapper;
import com.github.rabbitmq.domain.MqMessage;
import com.github.rabbitmq.producer.Sender;
import com.github.rabbitmq.service.MqService;
import com.github.rabbitmq.service.dto.MqDTO;
import com.github.service.ContentIdsService;
import com.github.service.YearContentIdsService;
import com.github.service.YearMonthContentIdsService;
import com.github.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Map;

/**
 *
 * @author oldhand
 * @date 2019-12-16
 *
*/

@Slf4j
@Component
public class MqUtils {

    private static MqUtils staticinstance;

    @Autowired
    private TabsRepository tabsrepository;

    @Autowired
    private ModentitynosService modentitynosservice;

    @Autowired
    private ModentitynosMapper modentitynosmapper;

    @Autowired
    private MqService mqService;

    @Autowired
    private Sender sender;

    // 通过注解实现注入
    @PostConstruct
    public void init() {
        staticinstance = new MqUtils();
        staticinstance.mqService = mqService;
        staticinstance.modentitynosservice = modentitynosservice;
        staticinstance.tabsrepository = tabsrepository;
        staticinstance.modentitynosmapper = modentitynosmapper;
        staticinstance.sender = sender;
    }

    public String run(String msg) throws Exception {
        log.info("执行编号生成消息队列! 参数为： {}", msg);
        try {
            int tabid = Integer.parseInt(msg);
            ModentitynosDTO modentitynosdto = staticinstance.modentitynosservice.findByTabid(tabid);
            if (modentitynosdto == null) {
                throw new Exception("[" + tabid + "]编号没有找到");
            }
            if (!modentitynosdto.getActive()) {
                throw new Exception("[" + tabid + "]编号设置没有激活");
            }
            Modentitynos modentityno = staticinstance.modentitynosmapper.toEntity(modentitynosdto);
            String curdate = modentityno.getCurDate();
            String prefix = modentityno.getPrefix();
            Boolean includedate = modentityno.getIncludeDate();
            int curid = modentityno.getCurId();
            int length = modentityno.getLength();
            int startid = modentityno.getStartId();

            String sysdate = DateTimeUtils.getDatetime("yyMMdd");
            String entityno;
            if (includedate) {
                 if (sysdate.equals(curdate)) {
                     curid ++;
                     entityno = prefix + curdate + String.format("%0" +length + "d", curid);
                 }
                 else {
                     curid = startid;
                     curdate = sysdate;
                     entityno = prefix + sysdate + String.format("%0" +length + "d", curid);
                 }
            }
            else {
                curid ++;
                entityno = prefix + String.format("%0" +length + "d", curid);
            }

            staticinstance.modentitynosservice.update(modentitynosdto.getId(),curid,curdate);
            return entityno;

        }
        catch(Exception e) {
            throw e;
        }
    }

    public static String makeModEntityNo(String Tabname) throws Exception {
        Tabs tabs = staticinstance.tabsrepository.findByTabname(Tabname);
        if (tabs == null) {
            throw new Exception("[" + Tabname + "]模块没有找到");
        }
        return makeModEntityNo(tabs.getTabid());
    }

    public static String makeModEntityNo(int tabid) throws Exception {
        try {
            ModentitynosDTO modentitynosdto = staticinstance.modentitynosservice.findByTabid(tabid);
            if (modentitynosdto == null) {
                throw new Exception("[" + tabid + "]编号没有找到");
            }
            MqMessage resources = new MqMessage();
            resources.setIsasync(true);
            resources.setIslock(true);
            resources.setName("makeModEntityNo");
            resources.setUniquekey(tabid + "_" + Long.toString(DateTimeUtils.gettimeStamp()));
            resources.setMessage(Integer.toString(tabid));
            log.info("执行编号生成消息队列[tabid: {}]...",tabid);
            MqDTO mq = staticinstance.mqService.create(resources);
//            staticinstance.mqService.updateAck(mq.getId(),"0","");
            Object result = staticinstance.sender.sendMessage(mq);
            if (result != null) {
                Map<String, String> res = (Map<String, String>)result;
                mq.setAck(Integer.parseInt(res.get("ack")));
                mq.setResult(res.get("result"));
                mq.setAcktime(new Timestamp(System.currentTimeMillis()));
                if (res.get("ack").toString().equals("2")) {
                    return res.get("result");
                }
                throw new Exception(res.get("result"));
            }
            else {
                throw new Exception("执行队列失败");
            }
        }
        catch(Exception e) {
            throw e;
        }
    }
}
