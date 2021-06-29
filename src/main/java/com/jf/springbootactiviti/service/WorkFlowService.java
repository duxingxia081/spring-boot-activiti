package com.jf.springbootactiviti.service;

import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;

public interface WorkFlowService {
    /**
     * @description: 根据key启动流程
     * @author: weizh
     * @date: 2021/6/29 21:20
     * @version: 1.0
     */
    ProcessInstance startWorkflow(Map<String,Object> variables);
    /**
     * @description: 获取某个环节待办数据
     * @param: group(用户角色)
     * @author: weizh
     * @date: 2021/6/29 17:26
     * @version: 1.0
     */
    List<String> listTask(String group);
}
