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
            sh "${scannerHome}/bin/sonar-scanner mvn clean -Dsonar.host.url=http://192.168.60.4:9000/sonar Dsonar.projectName='Simple Java project analyzed with the SonarQube Runner' -Dsonar.projectVersion=1.0 -Dsonar.projectKey='my:java-sonar-runner-simple' -Dsonar.sources=src"
        }
        timeout(time: 10, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}
    }
}
