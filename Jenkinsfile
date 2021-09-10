pipeline{
    agent any
    
    environment{
        registry= '975072647018.dkr.ecr.ap-south-1.amazonaws.com/demo-repo'
    }
    
    stages{
        stage('Compiling Stage'){
            steps{
                sh 'python3 src/Alphabet.py'
            }
        }
        
        stage('Testing Stage'){
            steps{
                sh 'python3 src/test_Alphabet.py'
            }
        }
        
        stage('Building image') {
            steps{
                script {
                    dockerImage = docker.build registry
                }
            }
        }
        
        stage('Pushing to ECR') {
            steps{  
                script {
                    sh 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 975072647018.dkr.ecr.ap-south-1.amazonaws.com'
                    sh 'docker push 975072647018.dkr.ecr.ap-south-1.amazonaws.com/demo-repo:latest'
                }
            }
        }
        
        stage('Stop previous containers') {
            steps {
                sh 'docker ps -f name=mypythonContainer -q | xargs --no-run-if-empty docker container stop'
                sh 'docker container ls -a -fname=mypythonContainer -q | xargs -r docker container rm'
            }
        }
      
        stage('Docker Run') {
            steps{
                script {
                    sh 'docker run -d -p 8096:5000 --rm --name mypythonContainer 975072647018.dkr.ecr.ap-south-1.amazonaws.com/demo-repo:latest'
                }
            }
        }
    }
}
