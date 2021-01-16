package com.tasks;

import com.data_base.entities.Task;
import com.services.DictionaryService;
import com.services.HeadHunterService;
import com.services.NeuralNetworkService;
import com.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
@Qualifier("tasksexecutor")
public class TasksExecutingThread extends Thread {
    @Autowired
    private TaskService taskService;

    @Autowired
    private NeuralNetworkService neuralNetworkService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private HeadHunterService headHunterService;

    @Override
    public void run(){
        while(true){
            List<Task> tasks = this.taskService.getUnexecutedTasks();

            for(Task task: tasks){
                new Thread(() -> executeTask(task)).start();
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void teach(){
        this.headHunterService.loadResumeFromHeadHunter(1, 0, 1);
        //neuralNetworkService.teachNeuralNetwork(3);
    }

    private void executeTask(Task task){
        try {
            switch ((int) task.getTypeId()) {
                case 1:
                    if (ObjectUtils.isEmpty(task.getSpecialityId())) {
                        neuralNetworkService.teachNeuralNetwork();
                        neuralNetworkService.rebuildAllNetworks();
                    } else {
                        System.out.println(task.getSpecialityId());
                        neuralNetworkService.teachNeuralNetwork(task.getSpecialityId());
                        neuralNetworkService.rebuildNetwork(task.getSpecialityId());
                    }

                    taskService.updateTaskStatus(task.getId(), 2);
                    break;
                case 2:
                    this.headHunterService.loadResumeFromHeadHunter(task.getSpecialityId(), task.getPageFrom(), task.getPagesCount());
                    taskService.updateTaskStatus(task.getId(), 2);
                    break;
                default:
                    break;
            }
        } catch(Exception ex){
            ex.printStackTrace();
            taskService.updateTaskStatus(task.getId(), 3);
        }
    }
}
