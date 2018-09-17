pipeline {
    agent any
    environment {
        WAR_NAME = 'hello-world' 
        DEPLOYMENT_PATH='/opt/wildfly/standalone/deployments'
        HOST_WILD = 'root@192.168.60.7'
        HOST_ART = 'http://192.168.60.8:8081'
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
        stage('Sonarqube') {
            environment {
                scannerHome = tool 'SonarQubeScanner'
            }
            steps {
                withSonarQubeEnv('sonarqube') {
                sh "/opt/sonar-scanner/bin/sonar-scanner"
            }
            timeout(time: 25, unit: 'SECONDS') {
              waitForQualityGate abortPipeline: true
            }
            }
        }
        stage('Unit testing') {
            steps {
                sh "mvn test"
                sh "mvn clean"
            }
        }
        stage('Build') {
            steps {
                sh "mvn clean install"
            }
        }
        stage('Publish artefactory') {
            steps {
                sh "curl -uadmin:AP4ZJb9HkMJN2KAxwnhjRxyqNRf -T target/hello-world-war-1.0.0.war '$HOST_ART/artifactory/helloworld/$WAR_NAME-${env.BUILD_NUMBER}.war'"
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
                subject: "SUCCESSFUL: This Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """<p>SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                recipientProviders: [developers(), brokenTestsSuspects(), culprits(), requestor()], 
                to: 'admin@lawstrust.com'
            )
        }
        failure {
            emailext (
                subject: "FAILED1: Job1 '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                recipientProviders: [developers(), brokenTestsSuspects(), culprits(), requestor()],
                to: "admin@lawstrust.com"
            )
        }
    }
}

