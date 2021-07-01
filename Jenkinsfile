pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        git(url: 'https://domocles.xp-dev.com/git/stocker', branch: 'master')

        sh "./mvnw clean package"
      }
    }

  }
}