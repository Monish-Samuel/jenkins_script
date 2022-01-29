node{
  try{
   execute();
  }catch(Exception e){
    throw e;
    println ("Error executing pipeline")
  }
  finally{
    cleanWs();
  }
}

execute(){
  stage("Test"){
    echo 'Test 1 completed'
  }
  
  stage("Test"){
    echo 'Test 2 completed'
  }
}
