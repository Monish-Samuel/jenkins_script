pipeline{
agent any
    stages{	    
      stage('Artifact-Download'){
		    steps{
			    //Initial Clean up of folder before downloading artifact
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
        sh 'unzip myapp-$buildVersion.zip'
        }
      }
    }
}
