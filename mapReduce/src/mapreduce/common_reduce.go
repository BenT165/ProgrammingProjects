package mapreduce

import (
    "os"
    //"fmt"
    "log"
    "encoding/json"
)

// doReduce does the job of a reduce worker: it reads the intermediate
// key/value pairs (produced by the map phase) for this task, sorts the
// intermediate key/value pairs by key, calls the user-defined reduce function
// (reduceF) for each key, and writes the output to disk.
func doReduce(
	jobName string, // the name of the whole MapReduce job
	reduceTaskNumber int, // which reduce task this is
	nMap int, // the number of map tasks that were run ("M" in the paper)
	reduceF func(key string, values []string) string,
) {
	// TODO:
	// You will need to write this function.
	// You can find the intermediate file for this reduce task from map task number
	// m using reduceName(jobName, m, reduceTaskNumber).
	// Remember that you've encoded the values in the intermediate files, so you
	// will need to decode them. If you chose to use JSON, you can read out
	// multiple decoded values by creating a decoder, and then repeatedly calling
	// .Decode() on it until Decode() returns an error.
	//
	// You should write the reduced output in as JSON encoded KeyValue
	// objects to a file named mergeName(jobName, reduceTaskNumber). We require
	// you to use JSON here because that is what the merger than combines the
	// output from all the reduce tasks expects. There is nothing "special" about
	// JSON -- it is just the marshalling format we chose to use. It will look
	// something like this:
	//
	// enc := json.NewEncoder(mergeFile)
	// for key in ... {
	// 	enc.Encode(KeyValue{key, reduceF(...)})
	// }
	// file.Close()
	//
	// Use checkError to handle errors.


    //make a key value map (word maps to an array of counts)
    kvMap := make(map[string][]string)

    for i:=0; i<nMap; i++{

        //get name of intermediate file and read it
        inFile := reduceName(jobName, i, reduceTaskNumber)  
                    
        file, err := os.Open(inFile)
        if err != nil{
            log.Fatal(err)
        }
        defer file.Close()

        //create decoder object for the current file
        dec := json.NewDecoder(file)
       
        var kv KeyValue

        //decode all objects from file
        for {
        
            if err = dec.Decode(&kv); err != nil{

                break
            }

            //append value to string array for key 
            kvMap[kv.Key] = append(kvMap[kv.Key], kv.Value)
             
        } 
    }

    //get name for merge file       
	mergeFile := mergeName(jobName, reduceTaskNumber)

    //open merge file                   
    outFile, err := os.Create(mergeFile)

    if(err != nil){
        log.Fatal(err)
    }
    defer outFile.Close()

    //create the encoder
    enc := json.NewEncoder(outFile)

    var kv KeyValue

    //for each key
    for key := range(kvMap){
    
        //get combined value for current key 
        currStr := reduceF(key, kvMap[key]) 
       
        //get keyvalue to encode 
        kv = KeyValue{key, currStr} 
    
        //encode keyvalue in outFile
        if err := enc.Encode(&kv); err != nil {
            log.Fatal(err)
        }
    }

}
