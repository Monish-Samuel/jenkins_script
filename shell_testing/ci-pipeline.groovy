pipeline{
agent any
    stages{
        stage('Pre-Flight Checks'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Monish-Samuel/shell_testing.git']]])
			script{
			sh 'pwsh'
			def file= powershell returnStdout: true, script: './build_scripts/build_prep.ps1'
			def buildNumber= powershell (returnStdout: true, script: 'Get-Content ./buildNo.txt').trim()
			currentBuild.displayName = "${buildNumber}"
			env.buildNo= buildNumber
			def buildbranch= powershell (returnStdout: true, script: 'Get-Content ./buildbranch.txt').trim()
			currentBuild.description= "${buildbranch}"
		}
	    }	
	}
	    stage('Build-Automation'){
		    steps{
			    powershell './build_scripts/zip_creation.ps1'
		    }
	    }
	    stage('Code Analysis'){
		    steps{
			    powershell './code_analysis/analysis.ps1'
		    }
	    }
	    stage('Build-Management'){
		    steps{
			    rtUpload (   
   				 serverId: 'generic-libs-prod',
    					spec: '''{
          					"files": [
            						{
              						"pattern": "./source-repo/app-$buildNo.zip",
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
	    stage('Deployment'){
		    steps{
	    		build job: 'ps1_testing/cd-pipeline', parameters: [
				string(name: 'buildVersion', value: buildNo)
                ]
		    }
	    }
    }
}
