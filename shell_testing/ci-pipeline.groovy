pipeline{
agent any
    stages{
        stage('Pre-Flight Checks'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Monish-Samuel/shell_testing.git']]])
	    }	
	}
	    stage('Build-Automation'){
		    steps{
			    sh 'zip myapp-$BUILD_NUMBER.zip ./src'
		    }
	    }
	    stage('Code Analysis'){
		    steps{
			    sh "chmod +x -R ${env.WORKSPACE}"
			    sh './code_analysis/analysis.sh'
		    }
	    }
	    stage('Build-Management'){
		    steps{
			    rtUpload (   
   				 serverId: 'zip-libs-prod',
    					spec: '''{
          					"files": [
            						{
              						"pattern": "./source-repo/app-$BUILD_NUMBER.zip",
             						 "target": "zip-libs-prod/"
           				 		}
          					]
   					 }''',
				    buildName: 'Flask-App',
				    buildNumber: BUILD_NUMBER,
				)
			    			    
			rtPublishBuildInfo (
    				serverId: 'generic-libs-prod',
				buildName: 'Flask-App',
				buildNumber: BUILD_NUMBER,
			)
		    }
	    }
// 	    stage('Deployment'){
// 		    steps{
// 	    		build job: 'ps1_testing/cd-pipeline', parameters: [
// 				string(name: 'buildVersion', value: buildNo)
//                 ]
// 		    }
// 	    }
    }
}
