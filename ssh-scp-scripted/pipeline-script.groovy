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
  def data;
  def buildNumber= majorver + "." + minorver + "." + patchver + "." + BUILD_NUMBER
  currentBuild.displayName = "${buildNumber}"
  currentBuild.description= "${gitbranch}"
  env.buildNo= buildNumber
	
	stage('Build-Automation'){
		sh "chmod +x -R ${env.WORKSPACE}"
		sh "${env.WORKSPACE}/shell_testing/build_scripts/zip_creation.sh"
	    }
	
	stage('Build-Management'){
		rtUpload (   
   				 serverId: 'generic-libs-prod',
    					spec: '''{
          					"files": [
            						{
              						"pattern": "$WORKSPACE/shell_testing/myapp-$buildNo.zip",
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
	stage('Move Package to EC2'){
		
		withCredentials([file(credentialsId: 'india-server.pem', variable: 'my_private_key')]){
			sh "mv ${env.WORKSPACE}/shell_testing/myapp-${buildNo}.zip ${env.WORKSPACE}/shell_testing/myapp.zip"
			writeFile file: 'private.pem', text: readFile(my_private_key);
			sh 'cat private.pem'
			sh "ssh -i -t private.pem ec2-user@ec2-13-232-137-52.ap-south-1.compute.amazonaws.com"
	        	sh "ssh -i -t private.pem ec2-user@ec2-13-232-137-52.ap-south-1.compute.amazonaws.com [ -e myapp.zip ] && rm -- myapp.zip"
			sh "scp -i -t private.pem myapp.zip ec2-user@ec2-13-232-137-52.ap-south-1.compute.amazonaws.com:/"	
		}
	}
  
}

def cloneRepo(branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
    sh "git clone -b ${branchName} https://${TOKEN}@github.com/Monish-Samuel/shell_testing"
  }
}
