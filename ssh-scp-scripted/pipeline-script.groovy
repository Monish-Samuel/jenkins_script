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

def execute(){
  stage("Clone-Repo"){
    cloneRepo('master');
  }
  
  stage("Test"){
    echo 'Test 2 completed'
  }
}

def cloneRepo(branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
    sh 'git clone -b ${branchName} https://${TOKEN}@https://github.com/Monish-Samuel/shell_testing'
  }
}
