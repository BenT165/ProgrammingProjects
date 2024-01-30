package mapreduce

import "sync"

// schedule starts and waits for all tasks in the given phase (Map or Reduce).
func (mr *Master) schedule(phase jobPhase) {
	var ntasks int
	var nios int // number of inputs (for reduce) or outputs (for map)
	switch phase {
	case mapPhase:
		ntasks = len(mr.files)
		nios = mr.nReduce
	case reducePhase:
		ntasks = mr.nReduce
		nios = len(mr.files)
	}

    

	debug("Schedule: %v %v tasks (%d I/Os)\n", ntasks, phase, nios)

	// All ntasks tasks have to be scheduled on workers, and only once all of
	// them have been completed successfully should the function return.
	// Remember that workers may fail, and that any given worker may finish
	// multiple tasks.

        
    //create wait group    
    var waitgroup sync.WaitGroup
    

    //add all tasks to wait group
    waitgroup.Add(ntasks)


    //for each task
    for i:=0; i<ntasks; i++ {

        
        //launch a go routine to assign that task to a worker
        go func(TaskNum int, phase jobPhase){
 
            defer waitgroup.Done() 

            for {

                var result bool
                var doTask DoTaskArgs

                //get next available worker from registerChannel
                current_worker := <-mr.registerChannel 

                //build DoTaskArgs struct for current task        
                if(phase == mapPhase){ 
                    doTask = DoTaskArgs{mr.jobName, mr.files[TaskNum] ,phase, TaskNum, nios}
            
                }else {
                    doTask = DoTaskArgs{mr.jobName, "" ,phase, TaskNum, nios}
                }
 
                //use rpc call to run worker on current task
                result = call(current_worker, "Worker.DoTask", doTask, nil)

                //put task back into master channel (prevent race condition)
                //block other rpc calls on this channel until routine has finished
                go func(){
                    mr.registerChannel <- current_worker
                }()

                //if worker succeeded, terminate the loop            
                if(result == true){
                    break
                }
            }    

        }(i, phase)
    }

    //wait for all tasks to finish
    waitgroup.Wait()

	debug("Schedule: %v phase done\n", phase)
}
