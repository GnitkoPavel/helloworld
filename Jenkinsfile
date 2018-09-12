pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                git url: 'https://git.epam.com/epm-rdua/EPMRDUAMSC.git", credentialsId: "f69123a6-e99a-445b-bf50-5579a2f0aeef", branch: "develop_backend'
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
