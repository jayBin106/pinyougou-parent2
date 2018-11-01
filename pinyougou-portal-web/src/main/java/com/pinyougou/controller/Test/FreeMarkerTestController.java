package com.pinyougou.controller.Test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lenovo on 2018/11/1.
 */
@Controller
@RequestMapping("/freeMaker")
public class FreeMarkerTestController {
    @RequestMapping("/")
    public String get(ModelMap modelMap) {
        modelMap.put("title", "HelloWordl!");
        return "freeMakerIndex";
    }
}
