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
  
  stage('Update Properties File'){
    dir('jenkins_script'){
      def propsFile= readFile file: "liquibase-integration/liquibase.properties"
      def username,password,newFile
      withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId:'db_creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
	      username=env.USERNAME
	      password=env.PASSWORD
	      newFile=propsFile.replace("{{username}}",username).replace("{{password}}",password);
	      writeFile file: "${env.WORKSPACE}/liquibase-jenkins-integration/liquibase.properties", text: newFile
      }
    }
  }
  stage('Execute SQL'){
    dir('liquibase-jenkins-integration'){
      bat "liquibase update --log-level=FINE"
    }
  }
}

def cloneRepo(repoName,branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
	  bat "git clone -b ${branchName} https://${TOKEN}@github.com/Monish-Samuel/${repoName}"
  }
}
