node{
  try{
   execute();
  }catch(Exception e){
    throw e;
    println ("Error executing pipeline")
  }
  finally{
    deleteDir();
  }
}

execute(){
  stage("Test"){
    echo 'Test 1 completed'
  }
  
  stage("Test"){
    echo 'Test 2 completed'
  }
}

def deleteDir(){
  post {
        // Clean after build
        always {
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    disableDeferredWipeout: true,
                    notFailBuild: true,
                    patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                               [pattern: '.propsfile', type: 'EXCLUDE']])
        }
    }
}
