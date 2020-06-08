package cn.com.taiji.fsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing
public class FsbApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(FsbApplication.class, args);
	}


	/**
	 * 配置不经过controller请求对应的页面
	 * @param registry
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/403").setViewName("status/403");
		registry.addViewController("/401").setViewName("status/401");
		registry.addViewController("/404").setViewName("status/404");
		registry.addViewController("/500").setViewName("status/500");
		registry.addViewController("/error").setViewName("status/error");
		//registry.addViewController("/menu").setViewName("frames/menu");
		registry.addViewController("/loginSuccess").setViewName("frames/index");
	}

	/**
	 * 配置错误状态的跳转
	 * @return
	 */
	@Bean
	public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer(){
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
				Set<ErrorPage> set = new HashSet();
				set.add(new ErrorPage(HttpStatus.NOT_FOUND,"/404"));
				set.add(new ErrorPage(HttpStatus.FORBIDDEN,"/403"));
				set.add(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/500"));
				set.add(new ErrorPage(HttpStatus.UNAUTHORIZED,"/401"));
				configurableEmbeddedServletContainer.setErrorPages(set);
			}
		};
	}

}
