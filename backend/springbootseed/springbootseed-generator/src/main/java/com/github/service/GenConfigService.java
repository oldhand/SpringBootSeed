package com.github.service;

import com.github.domain.GenConfig;

/**
 * @author oldhand
 * @date 2019-12-16
*/
public interface GenConfigService {

    GenConfig find();

    GenConfig update(GenConfig genConfig);
}
