pipeline {
    agent any
    environment {
        WAR_NAME = 'hello-world' 
        DEPLOYMENT_PATH='/opt/wildfly/standalone/deployments'
        HOST_WILD = 'root@192.168.60.7'
        HOST_ART = 'http://192.168.60.8:8081'
        MANAGER = 'admin@lawstrust.com'
    }
    stages {
        stage('SCM') {
            steps {
                script {
                    cleanWs()
                        git url: 'https://github.com/GnitkoPavel/helloworld.git'
                }
            }
        }
        stage('Build') {
            steps {
                sh "mvn clean install"
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
            timeout(time: 25, unit: 'SECONDS') {
              waitForQualityGate abortPipeline: true
            }
            }
        }
        stage('Unit testing') {
            steps {
                sh "mvn test"
            }
        }
        stage('Publish artefactory') {
            steps {
                def server = Artifactory.newServer url: '$HOST_ART', credentialsId: '079ba046-5260-4ec3-a97e-b65537ef0e19'
                def uploadSpec = """{
                    "files": [{
                       "pattern": "target/$WAR_NAME-${env.BUILD_NUMBER}.war",
                       "target": "helloworld/"
                    }]
                 }"""
                 server.upload(uploadSpec)
            }
        }
        stage('Deploy to wildfly server') {
            steps {
                sh "scp target/hello-world-war-1.0.0.war 'root@192.168.60.7:/opt/wildfly/standalone/deployments/hello-world.war'"
                sh "ssh 'root@192.168.60.7' 'touch /opt/wildfly/standalone/deployments/.dodeploy'"
            }
        }
        
    }
    post {
        success {       
            emailext (
                to: "admin@lawstrust.com, pavel.gnitko@ukr.net",
                subject: "SUCCESSFUL: This Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """<p>SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                <p>Check console output at <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>""",
                recipientProviders: [developers(), brokenTestsSuspects(), culprits(), requestor()]
            )
        }
        failure {
            emailext (
                to: "admin@lawstrust.com",
                subject: "FAILED1: Job1 '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                <p>Check console output at <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>""",
                recipientProviders: [developers(), brokenTestsSuspects(), culprits(), requestor()]
            )
        }
    }
}

