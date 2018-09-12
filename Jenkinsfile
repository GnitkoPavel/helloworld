pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                git url: 'https://git.epam.com/epm-rdua/EPMRDUAMSC.git", credentialsId: "bc5bf332-0191-424e-9593-44c8aa4e149c", branch: "develop_backend'
            }
        }
        stage('Sonarqube') {
    environment {
        scannerHome = tool 'SonarQubeScanner'
    }
    steps {
        withSonarQubeEnv('sonarqube') {
            sh "${scannerHome}/bin/sonar-scanner"
        }
        timeout(time: 30, unit: 'SECONDS') {
            waitForQualityGate abortPipeline: true
        }
    }
}
    }
}
