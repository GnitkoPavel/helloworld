pipeline {
    agent any
    tools {
        maven 'M3'
    }
    triggers {
        githubpush()
    }
    environment {
        WAR_NAME = 'hello-world' 
        DEPLOYMENT_PATH='/opt/wildfly/standalone/deployments'
        HOST_WILD = 'root@192.168.60.7'
        HOST_ART = 'http://192.168.60.8:8081/artifactory'
        MANAGER = 'admin@lawstrust.com'
        ARTIFACT = 'target/hello-world-war-1.0.0.war'
        REPOSITORIY = 'remote-test'
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
                script {
                    def server = Artifactory.server 'prudentional'
                    def uploadSpec = """{
                        "files": [
                            {
                                "pattern": "$ARTIFACT",
                                "target": "$REPOSITORIY/$WAR_NAME-${env.BUILD_NUMBER}.war"
                            }
                        ]
                    }"""
                    server.upload(uploadSpec)
                    def buildInfo = Artifactory.newBuildInfo()
                    server.upload spec: uploadSpec, buildInfo: buildInfo
                    server.publishBuildInfo buildInfo
                }
            }
        }
        stage('Deploy to wildfly server') {
            steps {
                sh "scp '${ARTIFACT}' '${HOST_WILD}:${DEPLOYMENT_PATH}/${WAR_NAME}-${env.BUILD_NUMBER}.war'"
                sh "ssh '${HOST_WILD}' 'touch ${DEPLOYMENT_PATH}/.dodeploy'"
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

