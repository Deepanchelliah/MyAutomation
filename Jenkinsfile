pipeline {
  agent {
    docker {
      image 'maven:3.9-eclipse-temurin-17'
    }
  }

  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Run Tests') {
      steps {
        sh 'mvn -U -B clean test'
      }
      post {
        always {
          junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
          archiveArtifacts artifacts: 'target/allure-results/**', allowEmptyArchive: true
        }
      }
    }

    stage('Publish Allure') {
      steps {
        allure results: [[path: 'target/allure-results']]
      }
    }
  }
}
