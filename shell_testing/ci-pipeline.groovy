pipeline{
agent any
    stages{
        stage('Pre-Flight Checks'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Monish-Samuel/shell_testing.git']]])
		    script{
		    	def props = readProperties  file:'/var/lib/jenkins/workspace/shell_testing/ci-pipeline/build.properties'
			def majorver= props['MAJOR_VERSION']
			def minorver= props['MINOR_VERSION']
			def patchver= props['PATCH_VERSION']    
			def gitbranch= props['BRANCH_NAME']
			    def buildNumber= ${majorver} + "-" + ${majorver} + "-" + ${majorver} + "-" + ${env.BUILD_NUMBER}
			    echo "${buildNumber}"
		    }
	    }	
	}
// 	    stage('Build-Automation'){
// 		    steps{
// 			    sh "chmod +x -R ${env.WORKSPACE}"
// 			    sh './build_scripts/zip_creation.sh'
// 		    }
// 	    }
// 	    stage('Code Analysis'){
// 		    steps{
// 			    //sh "chmod +x -R ${env.WORKSPACE}"
// 			    sh './code_analysis/analysis.sh'    
// 		    }
// 	    }
// 	    stage('Build-Management'){
// 		    steps{
// 			    rtUpload (   
//    				 serverId: 'zip-libs-prod',
//     					spec: '''{
//           					"files": [
//             						{
//               						"pattern": "./myapp-$BUILD_NUMBER.zip",
//              						 "target": "zip-libs-prod/"
//            				 		}
//           					]
//    					 }''',
// 				    buildName: 'Flask-App',
// 				    buildNumber: BUILD_NUMBER,
// 				)
			    			    
// 			rtPublishBuildInfo (
//     				serverId: 'zip-libs-prod',
// 				buildName: 'Flask-App',
// 				buildNumber: BUILD_NUMBER,
// 			)
// 		    }
// 	    }
// 	    stage('Deployment'){
// 		    steps{
// 	    		build job: 'ps1_testing/cd-pipeline', parameters: [
// 				string(name: 'buildVersion', value: buildNo)
//                 ]
// 		    }
// 	    }
    }
}
