package com.jf.springbootactiviti.service;

import com.jf.springbootactiviti.form.Process;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface WorkFlowService {
    /**
     * @description: 上传并发布一个流程
     * @author: weizh
     * @date: 2021/7/3 20:20
     * @version: 1.0
     */
    void fileupload(MultipartFile uploadfile) throws IOException;
    /**
     * @description: 获取所有已发布流程，未做分页处理
     * @author: weizh
     * @date: 2021/7/3 20:30
     * @version: 1.0
     */
    List<Process> listDeployWorkflow();
    /**
     * @description: 可以获取已发布的流程资源
     * @author: weizh
     * @date: 2021/7/3 20:42
     * @version: 1.0
     */
    InputStream getWorkflowResource(String processDefinitionId,String resource);
    /**
     * @description: 可以删除已发布的工作流
     * @author: weizh
     * @date: 2021/7/3 20:56
     * @version: 1.0
     */
    void delDeployWorkflow(String deploymentId);
    /**
     * @description: 根据key启动流程
     * @author: weizh
     * @date: 2021/7/3 21:20
     * @version: 1.0
     */
    ProcessInstance startWorkflow(Map<String,Object> variables);
    /**
     * @description: 获取某个环节待办数据
     * @param: group(用户角色)
     * @author: weizh
     * @date: 2021/7/3 17:26
     * @version: 1.0
     */
    List<Map<String,String>> listTask(String group);
    /**
     * @description: 流程流转
     * @author: weizh
     * @date: 2021/7/4 12:20 下午
     * @version: 1.0
     */
    void completeTask(Map<String, Object> variables,String taskId);
    /**
     * @description: 获取历史数据
     * @author: weizh
     * @date: 2021/7/4 12:37 下午
     * @version: 1.0
     */
    List<String> listFinishTask();
    /**
     * @description: 获取历史数据
     * @author: weizh
     * @date: 2021/7/4 12:57 下午
     * @version: 1.0
     */
    List<HistoricActivityInstance> getTaskByBusinessId(String businessId);
    /**
     * @description: 生成高亮流程图
     * @author: weizh
     * @date: 2021/7/4 12:59 下午
     * @version: 1.0
     */
    InputStream generateDiagram(String executionId);
}
