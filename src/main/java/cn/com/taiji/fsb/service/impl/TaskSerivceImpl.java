package cn.com.taiji.fsb.service.impl;

import cn.com.taiji.fsb.domain.Task;
import cn.com.taiji.fsb.dto.TaskDto;
import cn.com.taiji.fsb.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSerivceImpl {

    @Autowired
    private TaskRepository taskRepository;
    public void save(TaskDto dto){
        Task task = new Task(dto);
        taskRepository.save(task);
    }

    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }

    public Task getTaskById(String taskId){
        return taskRepository.findOne(taskId);
    }
}
