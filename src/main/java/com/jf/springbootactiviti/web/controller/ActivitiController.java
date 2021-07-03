package com.jf.springbootactiviti.web.controller;

import com.jf.springbootactiviti.api.CommonResult;
import com.jf.springbootactiviti.form.Process;
import com.jf.springbootactiviti.service.WorkFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "增员流程接口")
@RestController
@RequestMapping("/activiti")
public class ActivitiController {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    WorkFlowService workFlowService;

    @ApiOperation(value = "发布流程", notes = "上传并发布一个流程")
    @PostMapping("deployworkflow")
    public CommonResult fileupload(@RequestParam MultipartFile uploadfile) {
        try {
            String filename = uploadfile.getOriginalFilename();
            InputStream is = uploadfile.getInputStream();
            repositoryService.createDeployment().addInputStream(filename, is).deploy();
            return CommonResult.success("发布成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "获取发布的流程", notes = "获取所有已发布流程，未做分页处理")
    @GetMapping("/listworkflow")
    public CommonResult listWorkFlow() {
        try {
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
            return CommonResult.success(listProcess);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "启动流程", notes = "可以生成信息的时候同时启动流程")
    @PostMapping("startworkflow")
    public CommonResult startWorkflow() {
        //如果流程中有变量，可以传变量到流程中
        Map<String, Object> variables = new HashMap<String, Object>(2);
        variables.put("role", 1000);
        variables.put("userId", "weizh");
        ProcessInstance ins = workFlowService.startWorkflow(variables);
        return CommonResult.success(ins.getId(), "启动流程成功");
    }
    @ApiOperation(value = "获取单位申报待办列表", notes = "查询单位申报待办列表")
    @GetMapping("listunitreporttask")
    public CommonResult listUnitReportTask() {
        try {
            //申报岗：可以根据情况使用角色id，或者直接使用申报岗，如果使用角色id，则流程中candidateGroups为角色id，这个可以在画图时选择某个角色进行决定
            List<String> list = workFlowService.listTask("申报岗");
            return CommonResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @ApiOperation(value = "获取事业处初核待办列表", notes = "查询事业处初核待办列表")
    @GetMapping("listhrchecktask")
    public CommonResult listHrCheckTask() {
        try {
            //初核岗：可以根据情况使用角色id，或者直接使用初核岗，如果使用角色id，则流程中candidateGroups为角色id，这个可以在画图时选择某个角色进行决定
            List<String> list = workFlowService.listTask("初核岗");
            return CommonResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
