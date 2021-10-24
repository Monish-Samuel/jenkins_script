pipeline{
    agent any
    
    environment{
        AWS_CREDS_REPO= credentials('jenkins-docker-aws-id')
        AWS_ECR_REPO= credentials('ECR-Repo-Name')
        registry= '${AWS_CREDS_REPO}'
    }
    
    stages{
        stage('Source_Code_Checkout'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Monish-Samuel/docker-pipeline.git']]])
            }
        }
        
        stage('Image Build') {
            steps{
                script {
                    dockerImage = docker.build registry
                }
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
        
        stage('Image Publish') {
            steps{  
                script {
                    sh 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin ${AWS_ECR_REPO}'
                    sh 'docker tag ${AWS_CREDS_REPO}:latest ${AWS_CREDS_REPO}:${BUILD_NUMBER}'
                    sh 'docker push ${AWS_CREDS_REPO}:${BUILD_NUMBER}'
                }
            }
        }
        
        stage('Deploying Image') {
            steps {
                sh 'docker ps -f name=mypythonContainer -q | xargs --no-run-if-empty docker container stop'
                sh 'docker container ls -a -fname=mypythonContainer -q | xargs -r docker container rm'
                script {
                    sh 'docker run -d -p 8096:5000 --rm --name mypythonContainer ${AWS_CREDS_REPO}:${BUILD_NUMBER}'
                }
            }
        }
    }
}

