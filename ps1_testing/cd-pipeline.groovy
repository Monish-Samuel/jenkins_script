pipeline{
agent any
    stages{	    
      stage('Artifact-Download'){
		    steps{
			    //Initial Clean up of folder before downloading artifact
			    powershell 'Get-ChildItem -Path ".\" -Directory -Filter "artifacts" | Remove-Item -Recurse -Confirm:$false -Force'
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
