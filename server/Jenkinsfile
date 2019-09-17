pipeline {
  agent any
  stages {
    stage('hello') {
      steps {
        echo 'hello'
      }
    }
    stage('maven ') {
      steps {
        sh 'mvn package'
      }
    }
  }
}