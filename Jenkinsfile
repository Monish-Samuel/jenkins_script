pipeline{
    agent any
    
    environment{
        registry= '975072647018.dkr.ecr.ap-south-1.amazonaws.com/demo-repo'
    }
    
    stages{
        stage('Source_Code_Checkout'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Monish-Samuel/docker-pipeline.git']]])
            }
        }
        
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
        
       stage('Code Analysis'){
            steps{
                sh 'chmod +x ./code_analysis/analysis.sh'
                sh './code_analysis/analysis.sh && exit'

            }
        }
        
        stage('Image Build') {
            steps{
                script {
                    dockerImage = docker.build registry
                }
            }
        }
        
        stage('Image Publish') {
            steps{  
                script {
                    sh 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 975072647018.dkr.ecr.ap-south-1.amazonaws.com'
                    sh 'docker push 975072647018.dkr.ecr.ap-south-1.amazonaws.com/demo-repo:latest'
                }
            }
        }
        
        stage('Deploying Image') {
            steps {
                sh 'docker ps -f name=mypythonContainer -q | xargs --no-run-if-empty docker container stop'
                sh 'docker container ls -a -fname=mypythonContainer -q | xargs -r docker container rm'
                script {
                    sh 'docker run -d -p 8096:5000 --rm --name mypythonContainer 975072647018.dkr.ecr.ap-south-1.amazonaws.com/demo-repo:latest'
                }
            }
        }      
    }
}
