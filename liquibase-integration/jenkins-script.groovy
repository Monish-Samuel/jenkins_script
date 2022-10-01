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
  stage('Checkout-Repo'){
	  bat "mkdir source-repo"
	  dir('source-repo'){
		checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Monish-Samuel/liquibase-jenkins-integration']]]);
	  }
	}
  
  stage('Update Properties File'){
    dir('jenkins_script'){
      def propsFile= readFile file: "liquibase-integration/liquibase.properties"
      def username,password,newFile
      withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId:'db_creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
	      username=env.USERNAME
	      password=env.PASSWORD
	      newFile=propsFile.replace("{{username}}",username).replace("{{password}}",password);
	      writeFile file: "${env.WORKSPACE}/source-repo/liquibase.properties", text: newFile
      }
    }
  }
  stage('Execute SQL'){
    dir('source-repo'){
      bat "liquibase update --log-level=INFO"
	    bat "liquibase tag ${env.BUILD_NUMBER}"
    }
  }
}

def cloneRepo(repoName,branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
	  bat "git clone -b ${branchName} https://${TOKEN}@github.com/Monish-Samuel/${repoName}"
  }
}
