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
      // Publish Allure Report
      allure([
        includeProperties: false,
        jdk: '',
        results: [[path: 'target/allure-results']]
      ])

      // Publish JUnit report (optional but useful)
      junit 'target/surefire-reports/*.xml'
    }
  }
}
