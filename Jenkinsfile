pipeline {
    agent any
    
    environment {
        NEXT_CLOUD_PASSWORD = credentials('next-cloud-password')
    }
    
    
    stages {
        
        stage('download .env file') {
            steps {
                script {
                    sh 'curl -u server:$NEXT_CLOUD_PASSWORD http://140.134.26.65:50603/remote.php/dav/files/server/ProgEdu-CI-CD/$BRANCH_NAME/.env -X GET -O'
                }
            }
        }
        
        stage('check .env Exists file') {
            steps {
                script {
                    if( ! fileExists(".env") ) {
                        error('not exists .env file')
                    }
                }
            }
        }

        stage('build') {
            steps {
                sh "docker-compose --compatibility -p $BRANCH_NAME-progedu up -d --build"
            }
        }
    }
}