package com.jf.springbootactiviti.service.impl;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.jf.springbootactiviti.form.Process;
import com.jf.springbootactiviti.service.WorkFlowService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 工作流程相关
 * @author: weizh
 * @date: 2021/7/3 21:14
 * @version: 1.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class WorkFlowServiceImpl implements WorkFlowService {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;

    /**
     * @description: 上传并发布一个流程
     * @author: weizh
     * @date: 2021/7/3 20:20
     * @version: 1.0
     */
    public void fileupload(MultipartFile uploadfile) throws IOException {
        String filename = uploadfile.getOriginalFilename();
        InputStream is = uploadfile.getInputStream();
        repositoryService.createDeployment().addInputStream(filename, is).deploy();
    }

    /**
     * @description: 获取所有已发布流程，未做分页处理
     * @author: weizh
     * @date: 2021/7/3 20:30
     * @version: 1.0
     */
    public List<Process> listDeployWorkflow() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        List<Process> listProcess = new ArrayList<Process>();
        list.stream().forEach(processDefinition -> {
            Process process = new Process();
            process.setId(processDefinition.getId());
            process.setDeploymentId(processDefinition.getDeploymentId());
            process.setKey(processDefinition.getKey());
            process.setName(processDefinition.getName());
            process.setResourceName(processDefinition.getResourceName());
            process.setDiagramresourcename(processDefinition.getDiagramResourceName());
            listProcess.add(process);
        });
        return listProcess;
    }

    /**
     * @description: 可以获取已发布的流程资源
     * @author: weizh
     * @date: 2021/7/3 20:42
     * @version: 1.0
     */
    public InputStream getWorkflowResource(String processDefinitionId, String resource) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        return repositoryService.getResourceAsStream(pd.getDeploymentId(), resource);
    }

    /**
     * @description: 可以删除已发布的工作流
     * @author: weizh
     * @date: 2021/7/3 20:56
     * @version: 1.0
     */
    public void delDeployWorkflow(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * @description: 根据key启动流程
     * @author: weizh
     * @date: 2021/7/3 21:20
     * @version: 1.0
     */
    @Override
    public ProcessInstance startWorkflow(Map<String, Object> variables) {
        //实际业务启动流程时需先生成task，然后把taskOid传到流程中，根据taskOid跟流程相关联
        //addPerson:比如增员可以使用这个启动增员的流程，所以这里可以动态指定使用哪个流程
        String taskOid = "1000";
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("addperson", taskOid, variables);
        //可以获取流程id，然后写到task中
        String instanceid = instance.getId();
        return instance;
    }

    /**
     * @description: 获取某个环节待办数据
     * @param: role(用户角色)
     * @author: weizh
     * @date: 2021/7/3 17:26
     * @version: 1.0
     */
    @Override
    public List<Map<String, String>> listTask(String role) {
        List<Map<String, String>> results = new ArrayList<>();
        //此例子可以使用分页
        int firstrow = 0;
        int rowcount = 30;
        //获取到当前role角色环节的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(role).listPage(firstrow, rowcount);
        for (Task task : tasks) {
            String processInstanceId = task.getProcessInstanceId();
            //根据任务获取启动运行时传的taskOid
            ProcessInstance proces = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String taskOid = proces.getBusinessKey();
            //TODO(根据taskOid查询业务信息，待补充)
            Map<String, String> map = new HashMap<>();
            map.put("taskId", task.getId());
            map.put("processInstanceId", processInstanceId);
            map.put("taskOid", taskOid);
            map.put("processDefinitionId", proces.getProcessDefinitionId());
            results.add(map);
        }
        return results;
    }

    /**
     * @description: 流程流转
     * @author: weizh
     * @date: 2021/7/4 12:20 下午
     * @version: 1.0
     */
    public void completeTask(Map<String, Object> variables, String taskId) {
        taskService.claim(taskId, "weizh");
        taskService.complete(taskId,variables);
    }

    /**
     * @description: 获取增员已办结的历史数据
     * @author: weizh
     * @date: 2021/7/4 12:37 下午
     * @version: 1.0
     */
    public List<String> listFinishTask() {
        List<String> results = new ArrayList<>();
        List<HistoricProcessInstance> ListHistory = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey("addperson").finished().list();
        if (CollectionUtils.isNotEmpty(ListHistory)) {
            ListHistory.stream().forEach((history) -> results.add(history.getBusinessKey()));
        }
        return results;
    }

    /**
     * @description: 获取历史数据
     * @author: weizh
     * @date: 2021/7/4 12:57 下午
     * @version: 1.0
     */
    public List<HistoricActivityInstance> getTaskByBusinessId(String businessId) {
        String instanceid = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("addperson")
                .processInstanceBusinessKey(businessId).singleResult().getId();
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceid).orderByHistoricActivityInstanceStartTime().asc().list();
    }

    /**
     * @description: 生成高亮流程图
     * @author: weizh
     * @date: 2021/7/4 12:59 下午
     * @version: 1.0
     */
    public InputStream generateDiagram(String executionId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(executionId).singleResult();
        if (processInstance != null) {
            BpmnModel model = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            if (model != null && model.getLocationMap().size() > 0) {
                List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
                ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
                // 生成流程图 已启动的task 高亮
                return generator.generateDiagram(model, activeActivityIds);
                // 生成流程图 都不高亮
                //return generator.generateDiagram(model, "宋体", "宋体", "宋体");
            }
        }
        return null;
    }
}
