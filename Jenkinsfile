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

          echo "Current dir:"
          pwd
          echo "Repo contents:"
          ls -la
          echo "POM exists?"
          test -f pom.xml

          docker run --rm \
            -v "$(pwd)":/ws \
            -w /ws \
            maven:3.9-eclipse-temurin-17 \
            mvn -U -B clean test
        '''
      }
      post {
        always {
          junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
          archiveArtifacts artifacts: 'target/**', allowEmptyArchive: true
        }
      }
    }

    stage('Publish Allure Report') {
      steps {
        // Requires Allure Jenkins plugin
        allure results: [[path: 'target/allure-results']]
      }
    }
  }
}
