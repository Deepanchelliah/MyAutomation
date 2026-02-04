pipeline {
  agent any
  options { timestamps() }

  stages {
    stage('Clean workspace') {
      steps {
        deleteDir()
      }
    }

    stage('Checkout') {
      steps {
        checkout scm
      }
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
    }
  }
}
