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

          VOL="ws_${BUILD_TAG//[^a-zA-Z0-9_.-]/_}"

          # Create a temp volume for this build
          docker volume create "$VOL" >/dev/null

          # Copy Jenkins workspace into the volume
          docker run --rm \
            -v "$VOL":/ws \
            -v "$(pwd)":/src \
            alpine sh -lc 'cp -a /src/. /ws/'

          # Sanity check inside container
          docker run --rm -v "$VOL":/ws -w /ws alpine sh -lc 'ls -la; test -f pom.xml'

          # Run Maven
          docker run --rm \
            -v "$VOL":/ws \
            -w /ws \
            maven:3.9-eclipse-temurin-17 \
            mvn -U -B clean test

          # Copy results back to Jenkins workspace
          docker run --rm \
            -v "$VOL":/ws \
            -v "$(pwd)":/dst \
            alpine sh -lc 'cp -a /ws/target /dst/ || true'

          # Cleanup volume
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
