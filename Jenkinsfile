pipeline {
  agent any
  options { timestamps() }

  stages {
    stage('Clean workspace') {
      steps { deleteDir() }
    }

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Run Tests (Maven in Docker)') {
      steps {
        sh '''
          set -eux
          docker version

          docker run --rm \
            -v "$WORKSPACE":/ws \
            -w /ws \
            maven:3.9-eclipse-temurin-17 \
            mvn -U -B clean test
        '''
      }
      post {
        always {
          // JUnit reports (helps Jenkins show test results)
          junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
          // Archive the raw Allure results
          archiveArtifacts artifacts: 'target/allure-results/**', allowEmptyArchive: true
        }
      }
    }

    stage('Publish Allure Report') {
      steps {
        // Requires Jenkins Allure plugin
        allure results: [[path: 'target/allure-results']]
      }
    }
  }
}
