package com.quick.system.api.factory;

import com.quick.system.api.ISysDictApi;
import com.quick.system.api.ISysOauthClientApi;
import com.quick.system.api.ISysUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
public class SystemApiFactory {

    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction reactorLoadBalancerExchangeFilterFunction;

    public WebClient webClient() {
        return WebClient.builder()
                .filter(reactorLoadBalancerExchangeFilterFunction)
                .baseUrl("lb://quick-boot-system-biz/").build();
    }

    @Bean
    public ISysDictApi sysDictApi() {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient())).build();
        return factory.createClient(ISysDictApi.class);
    }

    @Bean
    public ISysOauthClientApi sysOauthClientApi() {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient())).build();
        return factory.createClient(ISysOauthClientApi.class);
    }

    @Bean
    public ISysUserApi sysUserApi(){
        HttpServiceProxyFactory factory= HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient())).build();
        return factory.createClient(ISysUserApi.class);
    }

}
