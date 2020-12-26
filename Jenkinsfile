pipeline {
    agent any
    stages {
        stage('check .env file') {
            steps {
                script {
                    if( fileExists("../../progedu-ci-cd/$BRANCH_NAME/.env") ) {
                        sh "cp ../../progedu-ci-cd/$BRANCH_NAME/.env .env"
                    } else {
                        error('not exists .env file')
                    }
                }
            }
        }

        stage('build') {
            steps {
                sh 'docker-compose up -d --build'
            }
        }
    }
}