package org.ljx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ljx on 2018/5/8.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException {
        // 程序启动入口
        Properties properties = new Properties();
        InputStream in = Application.class.getClassLoader().getResourceAsStream("app.properties");
        properties.load(in);
        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(properties);
        app.run(args);
      /*EmbeddedServletContainerAutoConfiguration*/

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // TODO Auto-generated method stub
        builder.sources(this.getClass());
        return super.configure(builder);
    }
}
