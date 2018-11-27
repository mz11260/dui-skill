package com.zm.skill.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zm.kit.dispatcher.RequestBus;
import com.zm.kit.dispatcher.service.SecurityService;
import com.zm.protocol.request.SkillRequest;
import com.zm.protocol.response.SkillResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/11/26.
 */
@RestController
@RequestMapping(value = "demo-skill")
@Slf4j
public class SongSkillController {

    private RequestBus requestBus;

    private HttpServletRequest request;

    private SecurityService securityService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Autowired
    @Qualifier(value = "demoRequestBus")
    public void setRequestBus(RequestBus requestBus) {
        this.requestBus = requestBus;
    }

    @RequestMapping(value = "speechlet", produces = {"application/json;charset=UTF-8"},
            method = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.HEAD})
    public String handleIntentRequest(@RequestBody String body) throws Exception {

        String signature = this.request.getHeader("Signature");
        if (!securityService.security(signature)) {
            log.error("签名验证失败: {} \n{}", signature, body);
            throw new Exception("签名验证失败: " + signature);
        }

        log.info("request body ====> {}", body);
        SkillRequest request = JSONObject.parseObject(body, SkillRequest.class);

        SkillResponse response = requestBus.service(request);
        String json = JSON.toJSONString(response);

        log.info("response body ====> {}", json);
        return json;
    }
}
