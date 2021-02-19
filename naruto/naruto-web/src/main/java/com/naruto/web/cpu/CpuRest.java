package com.naruto.web.cpu;

import com.naruto.service.channel.ChannelService;
import com.naruto.util.ResUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/cpu")
public class CpuRest {

    @Autowired
    ChannelService channelService;

    @GetMapping("/test")
    public ResUtil test(){
        return null;
    }
    
}
