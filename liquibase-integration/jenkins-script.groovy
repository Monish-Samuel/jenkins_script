node{
  try{
   cloneRepo('jenkins_script','master')
   execute();
  }catch(Exception e){
    println ("Error executing pipeline");
    throw e;
  }
//   finally{
//     deleteDir();
//   }
}

def execute(){
  stage('Clone-Repo'){
		cloneRepo('liquibase-jenkins-integration','master');
	}
  
  stage('Update Properties File'){
    dir('jenkins_script'){
      def propsFile= readFile file: "${env.WORKSPACE}/liquibase-integration/liquibase.properties"
      withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId:'db_creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
	      propsFile.replace('{{username}}',env.USERNAME)
	      propsFile.replace('{{password}}',env.PASSWORD)
	      writeFile file: "${env.WORKSPACE}/liquibase-jenkins-integration/liquibase.properties", text: propsFile
      }
    }
  }
}

def cloneRepo(repoName,branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
	  sh "git clone -b ${branchName} https://${TOKEN}@github.com/Monish-Samuel/${repoName}"
  }
}
