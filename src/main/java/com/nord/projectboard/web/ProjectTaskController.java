package com.nord.projectboard.web;

import com.nord.projectboard.domain.ProjectTask;
import com.nord.projectboard.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/api/board")
@CrossOrigin
public class ProjectTaskController {

    @Autowired
    private ProjectTaskService projectTaskService;

    private HashMap<String, String> errorMap;

    @PostMapping("")
    public ResponseEntity<?> addPTToBoard(@Valid @RequestBody ProjectTask projectTask, BindingResult result){
        if (result.hasErrors()){ return addErrorsToMap(result); }
        ProjectTask newPT = projectTaskService.saveOrUpdateProjectTask(projectTask);
        return new ResponseEntity<ProjectTask>(newPT,HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public Iterable<ProjectTask> getAllPTs(){ return projectTaskService.findAll(); }

    @GetMapping("/{pt_id}")
    public ResponseEntity<ProjectTask> getPTById(@PathVariable Long pt_id){
        ProjectTask projectTask = projectTaskService.findById(pt_id);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @DeleteMapping("/{pt_id}")
    public ResponseEntity<?> deletePTById(@PathVariable Long pt_id){
        projectTaskService.delete(pt_id);
        return new ResponseEntity<String>("Project Task deleted", HttpStatus.OK);
    }

    private ResponseEntity<?> addErrorsToMap(BindingResult result) {
        errorMap = new HashMap<>();

        for (FieldError error: result.getFieldErrors()){
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<HashMap<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
