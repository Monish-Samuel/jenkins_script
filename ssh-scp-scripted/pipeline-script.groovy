node{
  try{
   execute();
  }catch(Exception e){
    println ("Error executing pipeline");
    throw e;
  }
  finally{
    cleanWs();
  }
}

def execute(){
  
  def gitBranch= 'm';
  
  stage("Clone-Repo"){
    try{
      cloneRepo(gitBranch);
      println ("Cloned repo successfully")
    }catch(Exception e){
      println ("Error Cloning Repo")
      throw e;
    }
  }
  
  stage("Test"){
    echo 'Test 2 completed'
  }
}

def cloneRepo(branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
    sh "git clone -b ${branchName} https://${TOKEN}@github.com/Monish-Samuel/shell_testing"
  }
}
