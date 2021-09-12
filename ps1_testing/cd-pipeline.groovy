pipeline{
agent any
    stages{	    
      stage('Artifact-Download'){
		    steps{
			    powershell 'Get-ChildItem -Path ".\" -Directory -Filter "artifacts" | Remove-Item -Recurse -Confirm:$false -Force'
			    powershell '$value= $env:buildVersion'
			    powershell 'Write-Host "Requested Artifact to Download :app-$value.zip"'
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
