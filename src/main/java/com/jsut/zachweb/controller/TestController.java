package com.jsut.zachweb.controller;

import com.jsut.zachweb.dao.AdMapper;
import com.jsut.zachweb.model.Ad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class TestController {

    private static Logger log = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private AdMapper adMapper;
    @RequestMapping(value = "/aa")
    @ResponseBody
    public Ad test() throws ParseException {
        Ad ad = new Ad();
        ad.setAdId(1);
        ad.setAdUserId(1);
        ad.setAdPrice(20.11);
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = dFormat.format(new Date());
        ad.setAdAddTime(dFormat.parse(formatDate));
        ad.setAdLastUpdateTime(dFormat.parse(formatDate));
        log.info("sadakgaksjdhaskjdhalksdalksdakj");
        adMapper.insert(ad);
        return ad;
    }


}
