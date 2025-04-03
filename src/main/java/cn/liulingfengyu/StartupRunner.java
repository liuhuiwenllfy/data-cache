package cn.liulingfengyu;

import cn.liulingfengyu.system.service.IUserInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public void run(String... args) {
        log.info("应用启动成功，执行初始化操作...");
        userInfoService.initUserInfoToCache();
    }
}
