node{
  try{
   cloneRepo('jenkins_script','master')
   execute();
  }catch(Exception e){
    println ("Error executing pipeline");
    throw e;
  }
  finally{
    deleteDir();
  }
}

def execute(){
  stage('Clone-Repo'){
		cloneRepo('liquibase-jenkins-integration','master');
	}
  
  stage('Copy Properties File'){
    dir('jenkins_script'){
      powershell "Move-Item –Path ${env.WORKSPACE}/liquibase-integration/liquibase.properties -Destination ${env.WORKSPACE}/liquibase-jenkins-integration"
    }
  }
}

def cloneRepo(repoName,branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
	  sh "git clone -b ${branchName} https://${TOKEN}@github.com/Monish-Samuel/${repoName}"
  }
}
