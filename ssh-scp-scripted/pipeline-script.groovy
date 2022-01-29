node{
  try{
   cloneRepo('master')
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
  def props = readProperties  file:propsFile
  def majorver= props['MAJOR_VERSION']
  def minorver= props['MINOR_VERSION']
  def patchver= props['PATCH_VERSION']    
  def gitbranch= props['BRANCH_NAME']
  def buildNumber= majorver + "." + minorver + "." + patchver + "." + BUILD_NUMBER
  currentBuild.displayName = "${buildNumber}"
  currentBuild.description= "${gitbranch}"
  env.buildNo= buildNumber
	
	stage('Build-Automation'){
		sh "chmod +x -R ${env.WORKSPACE}"
		sh "${env.WORKSPACE}/shell_testing/build_scripts/zip_creation.sh"
		sh "cd ${env.WORKSPACE} && ls -lRt"
	    }
	
	stage('Build-Management'){
		rtUpload (   
   				 serverId: 'generic-libs-prod',
    					spec: '''{
          					"files": [
            						{
              						"pattern": "./myapp-$buildNo.zip",
             						 "target": "generic-libs-prod/"
           				 		}
          					]
   					 }''',
				    buildName: 'Flask-App',
				    buildNumber: buildNo,
				)
			    			    
			rtPublishBuildInfo (
    				serverId: 'generic-libs-prod',
				buildName: 'Flask-App',
				buildNumber: buildNo,
			)
	}
  
}

def cloneRepo(branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
    sh "git clone -b ${branchName} https://${TOKEN}@github.com/Monish-Samuel/shell_testing"
  }
}
