pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                git url: 'https://github.com/GnitkoPavel/helloworld.git'
            }
        }
        stage('Sonarqube') {
          environment {
            scannerHome = tool 'SonarQubeScanner'
          }
          steps {
            withSonarQubeEnv('sonarqube') {
              sh "/opt/sonar-scanner/bin/sonar-scanner"
           }
            timeout(time: 30, unit: 'SECONDS') {
              waitForQualityGate abortPipeline: true
           }
          }
        }
        stage('Unit testing') {
            steps {
              sh "mvn test"
            
            }
        
        
        }
        stage('Notification') {
            success {
            mail to: 'admin@lawstrust.com',
            subject: "Status of pipeline: ${currentBuild.fullDisplayName}",
            body: "${env.BUILD_URL} has result ${currentBuild.result}"
            }
        }
    }
}
