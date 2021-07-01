pipeline {
  agent any
  stages {
    stage('Clone the project') {
      steps {
        git(url: 'https://domocles.xp-dev.com/git/stocker', branch: 'master', credentialsId: 'stocker')
      }
    }

  }
}