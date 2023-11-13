pipeline {
    agent any
    tools {
        maven 'jenkins-maven'
    }

    environment {
        BUILD_NUMBER_ENV = "${env.BUILD_NUMBER}"
        TEXT_SUCCESS_BUILD = "[#${env.BUILD_NUMBER}] Project Name : ${JOB_NAME} is Success"
        TEXT_FAILURE_BUILD = "[#${env.BUILD_NUMBER}] Project Name : ${JOB_NAME} is Failure"
    }

    stages {
        stage('Git Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/septianrezaandrianto/rnd-spring-boot-3']])
                bat 'mvn clean install'
                echo 'Git Checkout Completed'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube') {
                        bat 'mvn clean package'
                        bat ''' mvn clean verify package -Dmaven.plugin.validation=brief sonar:sonar -Dsonar.projectKey=rnd-springboot-3.0 -Dsonar.projectName='rnd-springboot-3.0' -Dsonar.host.url=http://localhost:9000 '''
                        echo 'SonarQube Analysis Completed'
                    }
                }
            }
        }

        stage("Quality Gate") {
            steps {
                waitForQualityGate abortPipeline: true
                echo 'Quality Gate Completed'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat 'docker build -t septianreza/rnd-springboot-3.0 .'
                    echo 'Build Docker Image Completed'
                }
            }
        }

//         stage('Docker Push') {
//             steps {
//                 script {
//                     withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhub-password')]) {
//                         bat ''' docker login -u septianreza -p "%dockerhub-password%" '''
//                     }
//                     bat 'docker push septianreza/rnd-springboot-3.0'
//                 }
//             }
//         }

        stage ('Docker Run') {
            steps {
                script {
                    bat 'docker run -d --name rnd-springboot-3.0 -p 8099:8080 septianreza/rnd-springboot-3.0'
                    echo 'Docker Run Completed'
                }
            }
        }

    }
    post {
        always {
            bat 'docker logout'
        }
        success {
            script{
                 withCredentials([string(credentialsId: 'telegram-token', variable: 'TOKEN'), string(credentialsId: 'telegram-chat-id', variable: 'CHAT_ID')]) {
                    bat ''' curl -s -X POST https://api.telegram.org/bot"%TOKEN%"/sendMessage -d chat_id="%CHAT_ID%" -d text="%TEXT_SUCCESS_BUILD%" '''
                 }
            }
        }
        failure {
            script{
                withCredentials([string(credentialsId: 'telegram-token', variable: 'TOKEN'), string(credentialsId: 'telegram-chat-id', variable: 'CHAT_ID')]) {
                    bat ''' curl -s -X POST https://api.telegram.org/bot"%TOKEN%"/sendMessage -d chat_id="%CHAT_ID%" -d text="%TEXT_FAILURE_BUILD%" '''
                }
            }
        }
    }
}