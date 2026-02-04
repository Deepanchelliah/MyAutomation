pipeline {
  agent any

  tools {
    jdk 'JDK22'
    maven 'Maven3'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Run Tests') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: 'api-login',
          usernameVariable: 'USERNAME',
          passwordVariable: 'PASSWORD'
        )]) {
          sh """
            mvn clean test \
              -Denv=dev \
              -Dusername=$USERNAME \
              -Dpassword=$PASSWORD \
              -Dallure.results.directory=target/allure-results
          """
        }
      }
    }
  }

  post {
    always {
      junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
      archiveArtifacts artifacts: 'target/allure-results/**/*', allowEmptyArchive: true

      allure([
        includeProperties: false,
        jdk: '',
        commandline: 'Allure2',
        results: [[path: 'target/allure-results']]
      ])
    }
  }

}
