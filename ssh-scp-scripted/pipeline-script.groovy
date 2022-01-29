node{
  try{
    def execute()
  }catch(Exception e){
    throw e;
    println (Error executing pipeline)
  }
}

execute(){
  stage("Test"){
    echo 'Test 1 completed
  }
  
  stage("Test"){
    echo 'Test 2 completed
  }
}
