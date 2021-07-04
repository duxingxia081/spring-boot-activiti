package com.jf.springbootactiviti.web.controller;

import com.jf.springbootactiviti.api.CommonResult;
import com.jf.springbootactiviti.service.WorkFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "增员流程接口")
@RestController
@RequestMapping("/activiti")
public class ActivitiController {
    @Autowired
    WorkFlowService workFlowService;

    @ApiOperation(value = "发布流程", notes = "上传并发布一个流程")
    @PostMapping("/deployWorkflow")
    public CommonResult fileupload(@RequestParam MultipartFile uploadfile) {
        try {
            workFlowService.fileupload(uploadfile);
            return CommonResult.success("发布成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "获取发布的流程", notes = "获取所有已发布流程，未做分页处理")
    @GetMapping("/listDeployWorkflow")
    public CommonResult listWorkFlow() {
        try {
            return CommonResult.success(workFlowService.listDeployWorkflow());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "获取流程资源", notes = "可以获取已发布的流程资源")
    @GetMapping("/getWorkflowResource")
    public void export(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resource") String resource,
                       HttpServletResponse response) throws Exception {
        InputStream is = workFlowService.getWorkflowResource(processDefinitionId, resource);
        ServletOutputStream sos = response.getOutputStream();
        IOUtils.copy(is, sos);
    }

    @ApiOperation(value = "删除发布的流程", notes = "可以删除已发布的工作流")
    @PostMapping("/delDeployWorkflow")
    public CommonResult deletedeploy(@RequestParam("deploymentId") String deploymentId) {
        workFlowService.delDeployWorkflow(deploymentId);
        return CommonResult.success("删除成功");
    }

    @ApiOperation(value = "启动流程", notes = "可以生成信息的时候同时启动流程")
    @PostMapping("/startWorkflow")
    public CommonResult startWorkflow() {
        //如果流程中有变量，可以传变量到流程中
        Map<String, Object> variables = new HashMap<String, Object>(2);
        variables.put("role", 1000);
        variables.put("userId", "weizh");
        ProcessInstance ins = workFlowService.startWorkflow(variables);
        return CommonResult.success(ins.getId(), "启动流程成功");
    }

    @ApiOperation(value = "单位申报待办列表", notes = "查询单位申报待办列表")
    @GetMapping("/listUnitReportTask")
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

    @ApiOperation(value = "事业处初核待办列表", notes = "查询事业处初核待办列表")
    @GetMapping("/listHrCheckTask")
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

    @ApiOperation(value = "事业处复核待办列表", notes = "查询事业处复核待办列表")
    @GetMapping("/listHrRecheckTask")
    public CommonResult listHrRecheckTask() {
        try {
            //初核岗：可以根据情况使用角色id，或者直接使用复核岗，如果使用角色id，则流程中candidateGroups为角色id，这个可以在画图时选择某个角色进行决定
            List<String> list = workFlowService.listTask("复核岗");
            return CommonResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "单位上报", notes = "单位上报某个taskId的任务")
    @PostMapping("/unitReport")
    public CommonResult unitReport(@RequestParam("taskId") String taskId) {
        workFlowService.completeTask(null, taskId);
        return CommonResult.success("success");
    }

    @ApiOperation(value = "事业处初核同意", notes = "事业处初核同意某个taskId的任务")
    @PostMapping("/hrCheckAgree")
    public CommonResult hrCheckAgree(@RequestParam("taskId") String taskId) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("hrcheck", "Y");
        workFlowService.completeTask(variables, taskId);
        return CommonResult.success("success");
    }

    @ApiOperation(value = "事业处初核退回", notes = "事业处初核退回某个taskId的任务")
    @PostMapping("/hrCheckReturn")
    public CommonResult hrCheckReturn(@RequestParam("taskId") String taskId) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("hrcheck", "N");
        workFlowService.completeTask(variables, taskId);
        return CommonResult.success("success");
    }

    @ApiOperation(value = "事业处复核同意", notes = "事业处复核同意某个taskId的任务")
    @PostMapping("/hrReCheckAgree")
    public CommonResult hrReCheckAgree(@RequestParam("taskId") String taskId) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("hrrecheck", "Y");
        workFlowService.completeTask(variables, taskId);
        return CommonResult.success("success");
    }

    @ApiOperation(value = "事业处复核退回", notes = "事业处复核退回某个taskId的任务")
    @PostMapping("/hrReCheckReturn")
    public CommonResult hrReCheckReturn(@RequestParam("taskId") String taskId) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("hrrecheck", "N");
        workFlowService.completeTask(variables, taskId);
        return CommonResult.success("success");
    }

    @ApiOperation(value = "已办结的业务列表", notes = "查询已办结的业务列表")
    @PostMapping("/listFinishTask")
    public CommonResult listFinishTask(@RequestParam("current") int current,
                                       @RequestParam("rowCount") int rowCount) {
        return CommonResult.success(workFlowService.listFinishTask((current - 1) * rowCount, rowCount));
    }

    @ApiOperation(value = "查询办理过程", notes = "根据业务Id查询业务的办理过程")
    @GetMapping("/getTaskByBusinessId")
    public CommonResult getTaskByBusinessId(@RequestParam("businessId") String businessId) {
        return CommonResult.success(workFlowService.getTaskByBusinessId(businessId));
    }

    @ApiOperation(value = "绘图查看业务所在节点", notes = "绘图查看业务所在节点")
    @GetMapping("/generateDiagram/{processInstanceId}")
    public void generateDiagram(@PathVariable("processInstanceId") String processInstanceId, HttpServletResponse response)
            throws Exception {
        InputStream in = workFlowService.generateDiagram(processInstanceId);
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(in, output);
    }
}
