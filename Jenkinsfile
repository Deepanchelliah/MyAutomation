pipeline {
  agent any
  options {
    timestamps()
    skipDefaultCheckout(true)   // removes the extra "Declarative: Checkout SCM"
  }

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

          # Create a temp docker volume for this build
          VOL="ws_$(echo "$BUILD_TAG" | tr -cs 'A-Za-z0-9_.-' '_' )"
          docker volume create "$VOL" >/dev/null

          # Create a helper container with the volume mounted
          CID=$(docker create -v "$VOL":/ws alpine:3.19 sh -c "sleep 600")

          # Copy workspace contents into the volume via docker cp (no bind mounts)
          docker cp . "$CID":/ws

          # Sanity check: pom.xml must exist inside /ws
          docker start "$CID" >/dev/null
          docker exec "$CID" sh -lc 'ls -la /ws && test -f /ws/pom.xml'

          # Run Maven tests using the volume as /ws
          docker run --rm \
            -v "$VOL":/ws \
            -w /ws \
            maven:3.9-eclipse-temurin-17 \
            mvn -U -B clean test

          # Copy target/ back into Jenkins workspace so Jenkins can archive/publish
          docker cp "$CID":/ws/target ./target || true

          # Cleanup
          docker rm -f "$CID" >/dev/null
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
        // Requires Jenkins Allure plugin and results generated at target/allure-results
        allure results: [[path: 'target/allure-results']]
      }
    }
  }
}
