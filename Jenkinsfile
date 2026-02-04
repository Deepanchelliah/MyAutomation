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

          # POSIX-safe "sanitize" for volume name
          VOL="ws_$(echo "$BUILD_TAG" | tr -cs 'A-Za-z0-9_.-' '_' )"

          docker volume create "$VOL" >/dev/null

          docker run --rm \
            -v "$VOL":/ws \
            -v "$(pwd)":/src \
            alpine sh -lc 'cp -a /src/. /ws/'

          docker run --rm -v "$VOL":/ws -w /ws alpine sh -lc 'ls -la; test -f pom.xml'

          docker run --rm \
            -v "$VOL":/ws \
            -w /ws \
            maven:3.9-eclipse-temurin-17 \
            mvn -U -B clean test

          docker run --rm \
            -v "$VOL":/ws \
            -v "$(pwd)":/dst \
            alpine sh -lc 'cp -a /ws/target /dst/ || true'

          docker volume rm "$VOL" >/dev/null
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
