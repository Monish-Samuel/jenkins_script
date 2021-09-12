pipeline{
agent any
    stages{	    
      stage('Artifact-Download'){
		    steps{
			    rtDownload (
   				 serverId: 'generic-libs-prod',
    					spec: '''{
          					"files": [
            						{
              						"pattern": "generic-libs-prod/app-$buildVersion.zip",
             						 "target": "./artifacts/"
           				 		}
          					]
   					 }''',
				    buildName: 'Flask-App',
				    buildNumber: buildVersion,
				)
		    }
	    }
    }
}
