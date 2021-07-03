package com.jf.springbootactiviti.service.impl;

import com.jf.springbootactiviti.service.WorkFlowService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 工作流程相关
 * @author: weizh
 * @date: 2021/6/29 21:14
 * @version: 1.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class WorkFlowServiceImpl implements WorkFlowService {
    @Autowired
    RuntimeService runtimeservice;
    @Autowired
    TaskService taskservice;

    /**
     * @description: 根据key启动流程
     * @author: weizh
     * @date: 2021/6/29 21:20
     * @version: 1.0
     */
    @Override
    public ProcessInstance startWorkflow(Map<String, Object> variables) {
        //实际业务启动流程时需先生成task，然后把taskOid传到流程中，根据taskOid跟流程相关联
        //leave:比如增员可以使用这个启动增员的流程，所以这里可以动态指定使用哪个流程
        String taskOid = "1000";
        ProcessInstance instance = runtimeservice.startProcessInstanceByKey("addperson", taskOid, variables);
        //可以获取流程id，然后写到task中
        String instanceid = instance.getId();
        return instance;
    }
    /**
     * @description: 获取某个环节待办数据
     * @param: role(用户角色)
     * @author: weizh
     * @date: 2021/6/29 17:26
     * @version: 1.0
     */
    @Override
    public List<String> listTask(String role) {
        List<String> results = new ArrayList<>();
        //此例子可以使用分页
        int firstrow = 0;
        int rowcount = 30;
        //获取到当前role角色环节的任务
        List<Task> tasks = taskservice.createTaskQuery().taskCandidateGroup(role).listPage(firstrow, rowcount);
        for (Task task : tasks) {
            String instanceid = task.getProcessInstanceId();
            //根据任务获取启动运行时传的taskOid
            ProcessInstance proces = runtimeservice.createProcessInstanceQuery().processInstanceId(instanceid).singleResult();
            String taskOid = proces.getBusinessKey();
            //TODO(根据taskOid查询业务信息，待补充)
            results.add(taskOid);
        }
        return results;
    }
}
