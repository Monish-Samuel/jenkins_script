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
  def serverIP= props['SERVER_IP']
  def data;
  def buildNumber= majorver + "." + minorver + "." + patchver + "." + BUILD_NUMBER
  currentBuild.displayName = "${buildNumber}"
  currentBuild.description= "${gitbranch}"
  env.buildNo= buildNumber
  workingDir= "${env.WORKSPACE}/shell_testing";	
	
// 	stage('Build-Automation'){
// 		sh "chmod +x -R ${env.WORKSPACE}"
// 		sh "${env.WORKSPACE}/shell_testing/build_scripts/zip_creation.sh"
// 	    }
	
	stage('Sonar Quality and Gate'){
		def SONARSCANNER = tool "sonar-scanner";
		dir(workingDir){
            		withSonarQubeEnv("sonar") {
				sh "${SONARSCANNER}/sonar-scanner"
			}
		}
		timeout(1){
			waitForQualityGate abortPipeline: true;
		}
	}
	
// 	stage('Build-Management'){
// 		rtUpload (   
//    				 serverId: 'generic-libs-prod',
//     					spec: '''{
//           					"files": [
//             						{
//               						"pattern": "$WORKSPACE/shell_testing/myapp-$buildNo.zip",
//              						 "target": "generic-libs-prod/"
//            				 		}
//           					]
//    					 }''',
// 				    buildName: 'Flask-App',
// 				    buildNumber: buildNo,
// 				)
			    			    
// 			rtPublishBuildInfo (
//     				serverId: 'generic-libs-prod',
// 				buildName: 'Flask-App',
// 				buildNumber: buildNo,
// 			)
// 	}
		
// 	stage('Move Package to EC2'){
		
// 		withCredentials([file(credentialsId: 'india-server.pem', variable: 'my_private_key')]){
// 			sh "mv ${env.WORKSPACE}/shell_testing/myapp-${buildNo}.zip ${env.WORKSPACE}/shell_testing/myapp.zip"
// 			writeFile file: "${env.WORKSPACE}/india-server.pem", text: readFile(my_private_key);
// 			sh "chmod 400 ${env.WORKSPACE}/india-server.pem";
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP}"
// 	        	sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} '[ -e myapp.zip ] && rm -- myapp.zip'"
// 			sh "scp -i ${env.WORKSPACE}/india-server.pem -r ${env.WORKSPACE}/shell_testing/myapp.zip ec2-user@${serverIP}:~/"	
// 		}
// 	}
		
// 	stage('Build and Deploy image'){
// 			// create folder and unzip
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} '[ -e flask-app ] | rm -r flask-app'"
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} 'mkdir flask-app'"
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} 'unzip myapp.zip -d flask-app'"
			
// 			// build docker image
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} 'cd flask-app && docker build -t flask-app .'"
			
// 			// tag image and push to ecr
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 975072647018.dkr.ecr.ap-south-1.amazonaws.com'"
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} 'docker tag flask-app:latest 975072647018.dkr.ecr.ap-south-1.amazonaws.com/demo-repo:${buildNo}'"
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} 'docker push 975072647018.dkr.ecr.ap-south-1.amazonaws.com/demo-repo:${buildNo}'"
		
// 			// run the container
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} 'docker ps -f name=mypythonContainer -q | xargs --no-run-if-empty docker container stop'"
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} 'docker container ls -a -fname=mypythonContainer -q | xargs -r docker container rm'"
// 			sh "ssh -o StrictHostKeyChecking=no -i ${env.WORKSPACE}/india-server.pem ec2-user@${serverIP} 'docker run -d -p 8096:5000 --rm --name mypythonContainer 975072647018.dkr.ecr.ap-south-1.amazonaws.com/demo-repo:${buildNo}'"
// 	}
  
}

def cloneRepo(branchName){
  withCredentials([string(credentialsId: 'commit_git_token', variable: 'TOKEN')]){
    sh "git clone -b ${branchName} https://${TOKEN}@github.com/Monish-Samuel/shell_testing"
  }
}
