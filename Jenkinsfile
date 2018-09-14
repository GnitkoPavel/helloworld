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
        
    }
    post {
    success {       
      emailext (
          body: '''""<p>SUCCESSFUL: Job \'${env.JOB_NAME} [${env.BUILD_NUMBER}]\':</p>
           <p>Check console output at &QUOT;<a href=\'${env.BUILD_URL}\'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""''', 
          recipientProviders: [developers(pavel.gnitko@ukr.net), brokenTestsSuspects(pavel.gnitko@gmail.com), culprits(admin@lawstrust.com), requestor(pavel.gnitko@mail.ru)], 
          subject: 'SUCCESSFUL: Job \'${env.JOB_NAME} [${env.BUILD_NUMBER}]\'', 
          to: 'admin@lawstrust.com'
          )
    }
        

    failure {
      emailext (
          subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
          body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
            <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
          to: "admin@lawstrust.com"
        )
    }
  }
}

