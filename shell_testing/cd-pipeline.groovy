pipeline{
agent any
    stages{	    
      stage('Artifact-Download'){
		    steps{
			    //Initial Clean up of folder before downloading artifact
			    checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Monish-Samuel/shell_testing.git']]])
			    sh 'find . -name "*.zip" -type f -delete'
			    rtDownload (
   				 serverId: 'zip-libs-prod',
    					spec: '''{
          					"files": [
            						{
              						"pattern": "zip-libs-prod/myapp-$buildVersion.zip",
             						 "target": ""
           				 		}
          					]
   					 }''',
				    buildName: 'Flask-App',
				    buildNumber: buildVersion,
				)
		    }
	    }
      stage ('Unzipping Artiffact'){
        steps{
		sh "chmod +x -R ${env.WORKSPACE}"
		sh './build_scripts/unzip_deploy.sh'
        }
      }
    }
}
