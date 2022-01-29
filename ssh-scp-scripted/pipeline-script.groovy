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
  
  def propsFile= 'shell_testing/build.properties'
  env.WORKSPACE= pwd()
  def props = readProperties  file:propsFile
	def majorver= props['MAJOR_VERSION']
	def minorver= props['MINOR_VERSION']
	def patchver= props['PATCH_VERSION']    
	def gitbranch= props['BRANCH_NAME']
	def buildNumber= majorver + "." + minorver + "." + patchver + "." + BUILD_NUMBER
	currentBuild.displayName = "${buildNumber}"
	currentBuild.description= "${gitbranch}"
	env.buildNo= buildNumber
  
  stage("Clone-Repo"){
    try{
      cloneRepo(gitbranch);
      println ("Cloned repo successfully")
    }catch(Exception e){
      println ("Error Cloning Repo")
      throw e;
    }
  }
}

def cloneRepo(branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
    sh "git clone -b ${branchName} https://${TOKEN}@github.com/Monish-Samuel/shell_testing"
  }
}
